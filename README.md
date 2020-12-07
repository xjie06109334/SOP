# SOP(Simple Open Platform)

一个开放平台解决方案项目，基于Spring Cloud实现，目标让用户快速搭建自己的开放平台。

通过简单的配置后，你的项目就具备了和支付宝开放平台的一样的接口提供能力。

SOP封装了开放平台大部分功能包括：签名验证、统一异常处理、统一返回内容 、业务参数验证（JSR-303）、秘钥管理等，未来还会实现更多功能。

## 项目特点

- 接入方式简单，与老项目不冲突，老项目注册到注册中心，然后在方法上加上注解即可。
- 架构松耦合，业务代码实现在各自微服务上，SOP不参与业务实现，这也是Spring Cloud微服务体系带来的好处。
- 扩展简单，开放平台对应的功能各自独立，可以自定义实现自己的需求，如：更改参数，更改签名规则等。

## 谁可以使用这个项目

- 有现成的项目，想改造成开放平台供他人调用
- 有现成的项目，想暴露其中几个接口并通过开放平台供他人调用
- 想搭一个开放平台新项目，并结合微服务的方式去维护
- 对开放平台感兴趣的朋友

以上情况都可以考虑使用SOP

## 例子

```java
// 加一个注解即可
@Open("story.get")
@RequestMapping("/get")
public StoryResult get() {
    StoryResult result = new StoryResult();
    result.setId(1L);
    result.setName("海底小纵队");
    return result;
}
```

调用：

```java
// 公共请求参数
Map<String, String> params = new HashMap<String, String>();
params.put("app_id", appId);
params.put("method", "story.get");
params.put("format", "json");
params.put("charset", "utf-8");
params.put("sign_type", "RSA2");
params.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
params.put("version", "1.0");

// 业务参数
Map<String, String> bizContent = new HashMap<>();
bizContent.put("id", "1");
bizContent.put("name", "葫芦娃");

params.put("biz_content", JSON.toJSONString(bizContent));

System.out.println("----------- 请求信息 -----------");
System.out.println("请求参数：" + buildParamQuery(params));
System.out.println("商户秘钥：" + privateKey);
String content = AlipaySignature.getSignContent(params);
System.out.println("待签名内容：" + content);
String sign = AlipaySignature.rsa256Sign(content, privateKey, "utf-8");
System.out.println("签名(sign)：" + sign);

params.put("sign", sign);
System.out.println("URL参数：" + buildUrlQuery(params));

System.out.println("----------- 返回结果 -----------");
String responseData = get(url, params);// 发送请求
System.out.println(responseData);
```

## 架构图

![架构图](https://images.gitee.com/uploads/images/2019/1227/145216_c9b45109_332975.png "sop3.png")

> 如上图所示，整个系统运行后，开发者只需关注微服务中的业务代码，接口变更后重新部署微服务应用即可


## 已完成列表

- 签名验证
- 统一异常处理
- 统一返回内容
- session管理
- 秘钥管理
- 微服务端自动验证（JSR-303）
- Admin管理平台，统一管理微服务配置，管理路由管理，微服务上下线
- 门户网站，提供用户注册账号
- 接入方管理+秘钥管理
- 接口权限分配
- 文件上传/下载
- 提供基础SDK（含：Java,C++,C#,Python,Go,Rust,Nodejs）
- 接口限流
- 文档整合
- 应用授权
- 监控日志
- 注册中心支持nacos/eureka
- 网关动态修改参数
- 预发布/灰度环境切换

## 界面预览

![服务列表](https://images.gitee.com/uploads/images/2020/1016/134354_c1915902_332975.png "service.png")

![路由管理](https://images.gitee.com/uploads/images/2020/1016/134039_bed1608d_332975.png "route.png")

![限流管理](https://images.gitee.com/uploads/images/2020/1016/134102_f2dcfb25_332975.png "limit.png")

![秘钥信息](https://images.gitee.com/uploads/images/2019/0711/174921_bd817533_332975.png "秘钥信息")

- 门户网站

![首页](https://images.gitee.com/uploads/images/2020/1107/104607_b86161f0_332975.png "portal0.png")

![文档页](https://images.gitee.com/uploads/images/2020/1107/104342_d44849a9_332975.png "portal1.png")

## 工程说明

> 运行环境：JDK8，Maven3，[Nacos](https://nacos.io/zh-cn/docs/what-is-nacos.html)，Mysql

- doc：开发文档
- sop-common：公共模块，封装常用功能，包含签名校验、错误处理、限流等功能
- sop-gateway：网关，统一访问入口，含`Spring Cloud Zuul`和`Spring Cloud Gateway`实现
- sop-example：微服务示例，含springboot,springmvc示例
- sop-website：开放平台对应网站，提供文档API、沙箱测试等内容
- sop-auth：应用授权服务示例
- sop-admin：后台管理
- sop-sdk：基础sdk，含Java、C#版本
- sop-test：接口调用测试用例

## 分支说明

- master：发版分支（当前为4.0版本）
- develop：日常开发分支
- eureka：使用eureka注册中心
- pr：接受PR的分支，提交PR请提交到此分支

## 相关文档

[开发文档](http://durcframework.gitee.io/sop)

## 沟通交流

Q群：167643071

![SOP2群](https://images.gitee.com/uploads/images/2020/0203/103119_bdf84eb6_332975.png "SOP2群.png")
