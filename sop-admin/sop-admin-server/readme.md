# sop admin服务端

# 使用步骤

- 打开`application-dev.yml`，修改数据库`username/password`，指定nacos地址
- 运行`SopAdminServerApplication.java`
- 访问：`http://localhost:8082`

登录账号：admin/123456

后台用户表：admin_user_info

- 密码保存规则：`md5(username + md5(password) + username)`

```text
username: admin
password: 123456
即
md5("admin" + md5(123456) + "admin")
```

详见：com.gitee.sop.adminserver.AccountTest
