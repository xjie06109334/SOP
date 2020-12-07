package com.gitee.sop.websiteserver.service;

import com.gitee.fastmybatis.core.query.Query;
import com.gitee.sop.gatewaycommon.bean.NacosConfigs;
import com.gitee.sop.gatewaycommon.sync.MyNamedThreadFactory;
import com.gitee.sop.gatewaycommon.util.CopyUtil;
import com.gitee.sop.websiteserver.bean.ChannelMsg;
import com.gitee.sop.websiteserver.bean.ChannelOperation;
import com.gitee.sop.websiteserver.bean.IdGen;
import com.gitee.sop.websiteserver.bean.IsvDetailDTO;
import com.gitee.sop.websiteserver.bean.IsvKeysGenVO;
import com.gitee.sop.websiteserver.bean.LoginUser;
import com.gitee.sop.websiteserver.bean.RSATool;
import com.gitee.sop.websiteserver.controller.param.RegParam;
import com.gitee.sop.websiteserver.entity.IsvInfo;
import com.gitee.sop.websiteserver.entity.IsvKeys;
import com.gitee.sop.websiteserver.entity.IsvPortal;
import com.gitee.sop.websiteserver.entity.UserAccount;
import com.gitee.sop.websiteserver.mapper.IsvInfoMapper;
import com.gitee.sop.websiteserver.mapper.IsvKeysMapper;
import com.gitee.sop.websiteserver.mapper.UserAccountMapper;
import com.gitee.sop.websiteserver.util.GenerateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author tanghc
 */
@Service
@Slf4j
public class UserService {

    public static final byte STATUS_ENABLE = 1;

    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 4,
                0L,TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), new MyNamedThreadFactory("push-thread"));

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private IsvInfoMapper isvInfoMapper;

    @Autowired
    private IsvKeysMapper isvKeysMapper;

    @Autowired
    private ConfigPushService configPushService;

    public UserAccount getUserAccountByUsername(String username) {
        return userAccountMapper.getByColumn("username", username);
    }

    public UserAccount getUserAccountByUserId(long userId) {
        return userAccountMapper.getByColumn("id", userId);
    }

    public void updateUserAccount(UserAccount userAccount) {
        userAccountMapper.updateIgnoreNull(userAccount);
    }

    public UserAccount getUserInfoAppKey(String appKey) {
        return userAccountMapper.getByColumn("app_key", appKey);
    }

    public IsvPortal getIsvPortal(long userId) {
        return isvInfoMapper.getIsvPortal(userId);
    }

    public IsvKeys getIsvKeys(String appKey) {
        return isvKeysMapper.getByColumn("app_key", appKey);
    }

    public void saveIsvKey(IsvKeys isvKeys) {
        isvKeysMapper.saveIgnoreNull(isvKeys);
    }

    /**
     * 保存用户秘钥
     * @param isvKeys
     */
    public void updateIsvKey(IsvKeys isvKeys) {
        isvKeysMapper.updateIgnoreNull(isvKeys);
    }

    public void sendChannelMsg(String appKey) {
        threadPoolExecutor.execute(() -> {
            IsvDetailDTO isvDetail = isvInfoMapper.getIsvDetail(appKey);
            if (isvDetail == null) {
                return;
            }
            ChannelMsg channelMsg = new ChannelMsg(ChannelOperation.ISV_INFO_UPDATE, isvDetail);
            configPushService.publishConfig(NacosConfigs.DATA_ID_ISV, NacosConfigs.GROUP_CHANNEL, channelMsg);
        });
    }

    public LoginUser getLoginUser(String username, String password) {
        return isvInfoMapper.getLoginUser(username, password);
    }

    public UserAccount saveUser(RegParam param) {
        UserAccount userInfo = new UserAccount();
        String password = GenerateUtil.getUserPassword(param.getUsername(), param.getPassword());
        userInfo.setUsername(param.getUsername());
        userInfo.setPassword(password);
        userInfo.setStatus(STATUS_ENABLE);
        userAccountMapper.saveIgnoreNull(userInfo);
        return userInfo;
    }

    public void createIsv(UserAccount userInfo) {
        String appKey = new SimpleDateFormat("yyyyMMdd").format(new Date()) + IdGen.nextId();
        IsvInfo rec = new IsvInfo();
        rec.setAppKey(appKey);
        rec.setUserId(userInfo.getId());
        rec.setStatus(STATUS_ENABLE);
        isvInfoMapper.saveIgnoreNull(rec);
//        IsvKeysGenVO isvKeysGenVO = null;
//        try {
//            isvKeysGenVO = this.createIsvKeys();
//        } catch (Exception e) {
//            throw new RuntimeException("创建ISV秘钥失败", e);
//        }
//        IsvKeys isvKeys = new IsvKeys();
//        isvKeys.setAppKey(appKey);
//        isvKeys.setSignType(SIGN_TYPE_RSA);
//        CopyUtil.copyPropertiesIgnoreNull(isvKeysGenVO, isvKeys);
//        isvKeysMapper.saveIgnoreNull(isvKeys);

//        this.sendChannelMsg(rec.getAppKey());
    }

    private IsvKeysGenVO createIsvKeys() throws Exception {
        IsvKeysGenVO isvFormVO = new IsvKeysGenVO();
        String secret = IdGen.uuid();

        isvFormVO.setSecret(secret);

        RSATool rsaToolIsv = new RSATool(RSATool.KeyFormat.PKCS8, RSATool.KeyLength.LENGTH_2048);
        RSATool.KeyStore keyStoreIsv = rsaToolIsv.createKeys();
        isvFormVO.setPublicKeyIsv(keyStoreIsv.getPublicKey());
        isvFormVO.setPrivateKeyIsv(keyStoreIsv.getPrivateKey());

        isvFormVO.setPublicKeyPlatform("");
        isvFormVO.setPrivateKeyPlatform("");
        return isvFormVO;
    }


}
