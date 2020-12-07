package com.gitee.sop.storyweb.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gitee.sop.servercommon.annotation.BizCode;
import com.gitee.sop.servercommon.annotation.Open;
import com.gitee.sop.servercommon.bean.OpenContext;
import com.gitee.sop.servercommon.bean.ServiceContext;
import com.gitee.sop.servercommon.exception.ServiceException;
import com.gitee.sop.storyweb.controller.param.CategoryParam;
import com.gitee.sop.storyweb.controller.param.LargeTextParam;
import com.gitee.sop.storyweb.controller.param.StoryParam;
import com.gitee.sop.storyweb.controller.param.TypeEnum;
import com.gitee.sop.storyweb.controller.result.CategoryResult;
import com.gitee.sop.storyweb.controller.result.StoryResult;
import com.gitee.sop.storyweb.controller.result.TestResult;
import com.gitee.sop.storyweb.controller.result.TreeResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 签名验证通过后，到达这里进行具体的业务处理。
 *
 * @author tanghc
 */
@RestController
@RequestMapping("story")
@Slf4j
@Api(tags = "故事接口")
public class Example1001_BaseController {

    @Value("${server.port}")
    private int port;

    // http://localhost:2222/stroy/get
    // 原生的接口，可正常调用
    @RequestMapping("/get")
    public StoryResult get() {
        StoryResult result = new StoryResult();
        result.setId(1L);
        result.setName("海底小纵队(原生)");
        return result;
    }

    // 基础用法
    @ApiOperation(value = "获取故事信息（首位）", notes = "获取故事信息的详细信息", position = -100/* position默认0，值越小越靠前 */)
    @Open(value = "story.get", bizCode = {
            // 定义业务错误码，用于文档显示
            @BizCode(code = "100001", msg = "姓名错误", solution = "填写正确的姓名"),
            @BizCode(code = "100002", msg = "备注错误", solution = "填写正确备注"),
    })
    @RequestMapping("/get/v1")
    public StoryResult get_v1(StoryParam param) {
        StoryResult story = new StoryResult();
        story.setId(1L);
        story.setName("海底小纵队(story.get1.0), " + "param:" + param + ", port:" + port);
        return story;
    }

    // 指定版本号
    @ApiOperation(value = "获取故事信息", notes = "获取故事信息的详细信息")
    @Open(value = "story.get", version = "2.0", bizCode = {
            // 定义业务错误码，用于文档显示
            @BizCode(code = "100001", msg = "姓名错误", solution = "填写正确的姓名"),
            @BizCode(code = "100002", msg = "备注错误", solution = "填写正确备注"),
    })
    @RequestMapping("/get/v2")
    public StoryResult get_v2(StoryParam param) {
        StoryResult story = new StoryResult();
        story.setId(1L);
        story.setName("海底小纵队(story.get2.0), " + "param:" + param);
        return story;
    }

    @Open(value = "story.system.param.get")
    @GetMapping("/get/system/param/v1")
    public StoryResult systemParam(StoryParam param) {
        StoryResult result = new StoryResult();
        OpenContext openContext = ServiceContext.getCurrentContext().getOpenContext();
        System.out.println(param == openContext.getBizObject());
        System.out.println("app_id:" + openContext.getAppId());
        System.out.println("token:" + openContext.getAppAuthToken());
        result.setName("系统参数：" + openContext);
        return result;
    }

    // 参数绑定，少量参数可以这样写，参数多了建议放进类里面
    @Open(value = "story.oneparam")
    @GetMapping("/oneParam/v1")
    public StoryResult oneParam(@NotBlank(message = "id不能为空") String id, @NotBlank(message = "name不能为空")  String name) {
        StoryResult result = new StoryResult();
        result.setName("id：" + id + ", name:" + name);
        return result;
    }

    // 参数绑定
    @Open(value = "story.oneparam", version = "1.1")
    @GetMapping("/oneParam/v2")
    public StoryResult oneParamV2(
            @NotNull(message = "id不能为空")
            @Min(value = 1, message = "id必须大于0") Integer id) {
        StoryResult result = new StoryResult();
        result.setName("id：" + id);
        return result;
    }

    // 参数绑定，枚举
    @Open(value = "story.oneparam", version = "1.2")
    @GetMapping("/oneParam/v3")
    public StoryResult oneParamV2(@NotNull(message = "typeEnum不能为空") TypeEnum typeEnum) {
        StoryResult result = new StoryResult();
        result.setName("typeEnum：" + typeEnum.name());
        return result;
    }

    // 参数绑定
    @Open(value = "story.param.bind", mergeResult = false)
    @RequestMapping("/get/param/v1")
    public StoryResult param(int id, String name) {
        StoryResult result = new StoryResult();
        result.setName("参数绑定：id:" + id + ", name:" + name);
        return result;
    }

    // 忽略验证
    @ApiOperation(value = "忽略签名验证", notes = "忽略签名验证")
    @Open(value = "story.get.ignore", ignoreValidate = true)
    @PostMapping(value = "/get/ignore/v1")
    public StoryResult getStory21(@RequestBody StoryParam story) {
        StoryResult result = new StoryResult();
        result.setId((long) story.getId());
        result.setName(story.getName() + ", ignoreValidate = true");
        return result;
    }

    @Open(value = "story.get.large")
    @RequestMapping("/get/large/v1")
    public StoryResult getStoryLarge(LargeTextParam param) {
        StoryResult result = new StoryResult();
        int length = param.getContent().getBytes(StandardCharsets.UTF_8).length;
        result.setName("length:" + length);
        return result;
    }

    // 绑定复杂对象
    @Open(value = "sdt.get",version = "4.0")
    @RequestMapping("/get/v4")
    public TestResult getV4(@RequestBody TestResult testResult) {
        if(StringUtils.isEmpty(testResult.getType())) {
            throw new ServiceException("testResult.getType() 不能为null");
        }
        return testResult;
    }

    // 获取header
    @Open(value = "test.head",version = "1.0")
    @GetMapping("/get/header/v1")
    public StoryResult header(@RequestBody StoryParam story, HttpServletRequest request) {
        HttpServletRequest servletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        StoryResult storyResult = new StoryResult();
        storyResult.setId(1L);
        storyResult.setName(story.getName()
                + ", token1:" + request.getHeader("token")
                + ", token2:" + servletRequest.getHeader("token"));
        return storyResult;
    }

    // 返回数组结果
    @ApiOperation(value = "返回数组结果（第二）", notes = "返回数组结果", position = -99)
    @Open("story.list")
    @RequestMapping("/list/v1")
    public List<StoryResult> getStory3(StoryParam story) {
        int index = 0;
        StoryResult storyVO = new StoryResult();
        storyVO.setId(1L);
        storyVO.setName("白雪公主, index:" + index++);
        storyVO.setGmt_create(new Date());

        StoryResult storyVO2 = new StoryResult();
        storyVO2.setId(1L);
        storyVO2.setName("白雪公主, index:" + index++);
        storyVO2.setGmt_create(new Date());

        return Arrays.asList(storyVO, storyVO2);
    }

    // 演示文档表格树
    @ApiOperation(value = "获取分类信息", notes = "演示表格树")
    @Open("category.get")
    @PostMapping("/category/get/v1")
    public CategoryResult getCategory(CategoryParam param) {
        System.out.println(param);
        StoryResult result = new StoryResult();
        result.setId(1L);
        result.setName("白雪公主");
        result.setGmt_create(new Date());
        CategoryResult categoryResult = new CategoryResult();
        categoryResult.setCategoryName("娱乐");
        categoryResult.setStoryResult(result);
        return categoryResult;
    }

    // 演示文档页树状返回
    @ApiOperation(value = "树状返回", notes = "树状返回")
    @Open("story.tree.get")
    @PostMapping("/tree/v1")
    public TreeResult tree(StoryParam param) {
        int id = 0;
        TreeResult parent = new TreeResult();
        parent.setId(++id);
        parent.setName("父节点");
        parent.setPid(0);

        TreeResult child1 = new TreeResult();
        child1.setId(++id);
        child1.setName("子节点1");
        child1.setPid(1);

        TreeResult child2 = new TreeResult();
        child2.setId(++id);
        child2.setName("子节点2");
        child2.setPid(1);

        parent.setChildren(Arrays.asList(child1, child2));

        return parent;
    }

    private static String json = "{\"flightDate\":\"2020-09-01\",\"flightNo\":\"HO1705\",\"departureAirport\":\"ZSCN\",\"arrivalAirport\":\"ZPLJ\",\"ycy\":\"11521\",\"lcy\":\"4354\",\"cr\":\"145\",\"et\":\"1\",\"ye\":\"0\",\"td\":\"0\",\"gw\":\"0\",\"ew\":\"146\",\"xl\":\"1018\",\"yj\":\"0\",\"hw\":\"635\"}";
    // 返回大数据
    @Open(value = "bigdata.get")
    @RequestMapping("/bigdata/v1")
    public Map<String, Object> bigData(StoryParam param) {
        int len = 2000;
        List<JSONObject> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            list.add(JSON.parseObject(json));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("data", list);
        return map;
    }

}
