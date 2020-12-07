package com.gitee.sop.websiteserver.controller;

import com.gitee.fastmybatis.core.query.Query;
import com.gitee.sop.websiteserver.bean.DocInfo;
import com.gitee.sop.websiteserver.bean.DocItem;
import com.gitee.sop.websiteserver.bean.LoginUser;
import com.gitee.sop.websiteserver.bean.RSATool;
import com.gitee.sop.websiteserver.bean.Result;
import com.gitee.sop.websiteserver.bean.UserContext;
import com.gitee.sop.websiteserver.bean.Validates;
import com.gitee.sop.websiteserver.controller.param.IsvPublicKeyUploadParam;
import com.gitee.sop.websiteserver.controller.param.UpdatePasswordParam;
import com.gitee.sop.websiteserver.controller.result.DocVO;
import com.gitee.sop.websiteserver.controller.result.IsvInfoResult;
import com.gitee.sop.websiteserver.controller.result.IsvPublicKeyUploadResult;
import com.gitee.sop.websiteserver.controller.result.MenuDocItem;
import com.gitee.sop.websiteserver.controller.result.MenuModule;
import com.gitee.sop.websiteserver.controller.result.MenuProject;
import com.gitee.sop.websiteserver.entity.IspResource;
import com.gitee.sop.websiteserver.entity.IsvKeys;
import com.gitee.sop.websiteserver.entity.IsvPortal;
import com.gitee.sop.websiteserver.entity.UserAccount;
import com.gitee.sop.websiteserver.manager.DocManager;
import com.gitee.sop.websiteserver.mapper.IspResourceMapper;
import com.gitee.sop.websiteserver.service.UserService;
import com.gitee.sop.websiteserver.util.GenerateUtil;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author tanghc
 */
@RestController
@RequestMapping("portal/isv")
public class IsvController {

    @Autowired
    private UserService userService;

    @Autowired
    DocManager docManager;

    @Autowired
    IspResourceMapper ispResourceMapper;

    @Value("${api.url-test}")
    String urlTest;

    @Value("${api.url-prod}")
    String urlProd;

    @Value("${api.url-sandbox}")
    String gatewayUrl;

    @GetMapping("/getIsvPortal")
    public Result<IsvInfoResult> getIsvInfo() {
        LoginUser loginUser = UserContext.getLoginUser();
        IsvInfoResult isvInfoResult = new IsvInfoResult();
        IsvPortal isvPortal = userService.getIsvPortal(loginUser.getUserId());
        boolean hasPublicKey = StringUtils.isNotBlank(isvPortal.getPublicKeyIsv());
        isvInfoResult.setIsUploadPublicKey(BooleanUtils.toInteger(hasPublicKey));
        isvInfoResult.setAppId(isvPortal.getAppId());
        return Result.ok(isvInfoResult);
    }

    @GetMapping("/getPublicKey")
    public Result<IsvPublicKeyUploadResult> getPublicKey() {
        LoginUser loginUser = UserContext.getLoginUser();
        IsvKeys isvKeys = userService.getIsvKeys(loginUser.getAppKey());
        IsvPublicKeyUploadResult isvPublicKeyUploadResult = new IsvPublicKeyUploadResult();
        isvPublicKeyUploadResult.setIsUploadPublicKey(BooleanUtils.toInteger(isvKeys != null));
        isvPublicKeyUploadResult.setPublicKeyIsv(isvKeys == null ? "" : isvKeys.getPublicKeyIsv());
        isvPublicKeyUploadResult.setPublicKeyPlatform(isvKeys == null ? "" : isvKeys.getPublicKeyPlatform());
        return Result.ok(isvPublicKeyUploadResult);
    }

    /**
     * 上传公钥
     * @param param
     * @return
     */
    @PostMapping("/uploadPublicKey")
    public Result<IsvPublicKeyUploadResult> uploadPublicKey(@RequestBody @Valid IsvPublicKeyUploadParam param) {
        String publicKeyIsv = param.getPublicKeyIsv();
        LoginUser loginUser = UserContext.getLoginUser();
        String appKey = loginUser.getAppKey();
        IsvKeys isvKeys = userService.getIsvKeys(appKey);
        RSATool rsaToolIsv = new RSATool(RSATool.KeyFormat.PKCS8, RSATool.KeyLength.LENGTH_2048);
        RSATool.KeyStore keyStorePlatform;
        try {
            // 生成平台公私钥
            keyStorePlatform = rsaToolIsv.createKeys();
        } catch (Exception e) {
            throw new RuntimeException("上传公钥失败", e);
        }
        String publicKeyPlatform = keyStorePlatform.getPublicKey();
        String privateKeyPlatform = keyStorePlatform.getPrivateKey();
        if (isvKeys == null) {
            isvKeys = new IsvKeys();
            isvKeys.setAppKey(appKey);
            // 私钥自己保存
            isvKeys.setPrivateKeyIsv("");
            isvKeys.setPublicKeyIsv(publicKeyIsv);
            // 设置平台公私钥
            isvKeys.setPrivateKeyPlatform(privateKeyPlatform);
            isvKeys.setPublicKeyPlatform(publicKeyPlatform);
            userService.saveIsvKey(isvKeys);
        } else {
            isvKeys.setPrivateKeyPlatform(privateKeyPlatform);
            isvKeys.setPublicKeyPlatform(publicKeyPlatform);
            isvKeys.setPublicKeyIsv(publicKeyIsv);
            userService.updateIsvKey(isvKeys);
        }
        IsvPublicKeyUploadResult isvPublicKeyUploadResult = new IsvPublicKeyUploadResult();
        isvPublicKeyUploadResult.setIsUploadPublicKey(BooleanUtils.toInteger(true));
        isvPublicKeyUploadResult.setPublicKeyIsv(isvKeys.getPublicKeyIsv());
        isvPublicKeyUploadResult.setPublicKeyPlatform(isvKeys.getPublicKeyPlatform());
        userService.sendChannelMsg(appKey);
        return Result.ok(isvPublicKeyUploadResult);
    }

    /**
     * 修改密码
     * @param param
     * @return
     */
    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody @Valid UpdatePasswordParam param) {
        long userId = UserContext.getLoginUser().getUserId();
        UserAccount userAccount = userService.getUserAccountByUserId(userId);
        String oldPwdHex = GenerateUtil.getUserPassword(userAccount.getUsername(), param.getOldPassword());
        Validates.isTrue(!Objects.equals(oldPwdHex, userAccount.getPassword()), "旧密码错误");
        String newPwdHex = GenerateUtil.getUserPassword(userAccount.getUsername(), param.getPassword());
        userAccount.setPassword(newPwdHex);
        userService.updateUserAccount(userAccount);
        return Result.ok();
    }

    /**
     * 获取文档页菜单数据
     * @return
     */
    @GetMapping("/getDocMenus")
    public Result<DocVO> getAllDoc() {
        Collection<DocInfo> docInfos = docManager.listAll();
        List<MenuProject> menuProjects = docInfos.stream()
                .map(docInfo -> {
                    MenuProject menuProject = new MenuProject();
                    menuProject.setLabel(docInfo.getTitle());
                    // 构建模块
                    List<MenuModule> menuModules = docInfo.getDocModuleList()
                            .stream()
                            .map(docModule -> {
                                MenuModule menuModule = new MenuModule();
                                menuModule.setLabel(docModule.getModule());
                                // 构建文档
                                List<MenuDocItem> docItems = docModule.getDocItems().stream()
                                        .map(docItem -> {
                                            MenuDocItem menuDocItem = new MenuDocItem();
                                            menuDocItem.setId(docItem.getId());
                                            menuDocItem.setLabel(docItem.getSummary());
                                            menuDocItem.setName(docItem.getName());
                                            menuDocItem.setVersion(docItem.getVersion());
                                            return menuDocItem;
                                        }).collect(Collectors.toList());
                                menuModule.setChildren(docItems);
                                return menuModule;
                            }).collect(Collectors.toList());
                    menuProject.setChildren(menuModules);
                    return menuProject;
                }).collect(Collectors.toList());
        DocVO docVO = new DocVO();
        LoginUser loginUser = UserContext.getLoginUser();
        docVO.setGatewayUrl(gatewayUrl);
        docVO.setAppId(loginUser.getAppKey());
        docVO.setMenuProjects(menuProjects);
        docVO.setUrlProd(urlProd);
        docVO.setUrlTest(urlTest);
        return Result.ok(docVO);
    }

    /**
     * 根据文档id查询文档内容
     * @param id 文档id
     * @return
     */
    @GetMapping("/getDocItem")
    public Result<DocItem> getDocItem(String id) {
        DocItem docItem = docManager.getDocItem(id);
        return Result.ok(docItem);
    }

    /**
     * 获取SDK列表
     * @return
     */
    @GetMapping("listSdk")
    public Result<List<IspResource>> list() {
        List<IspResource> resourceList = ispResourceMapper.list(new Query());
        return Result.ok(resourceList);
    }
}
