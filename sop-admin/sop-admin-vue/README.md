# SOP Admin 前端vue实现

此工程基于前端脚手架：`https://github.com/PanJiaChen/vue-element-admin`

  前提：先安装好npm，[npm安装教程](https://blog.csdn.net/zhangwenwu2/article/details/52778521)

1. 启动服务端程序，运行`SopAdminServerApplication.java`
2. `cd sop-admin-vue`
3. 执行`npm install --registry=https://registry.npm.taobao.org`
4. 执行`npm run dev`，访问`http://localhost:9528/`，用户名密码：`admin/123456`


- 修改端口号：打开`vue.config.js`，找到`port`属性

## 打包放入到服务端步骤

如果想要把vue打包放到服务端，步骤如下：

- 执行`npm run build:prod`进行打包，结果在`dist`下
- 打包完成后，把dest中的所有文件，放到`sop-admin-server/src/main/resources/public`下

## 前后端分离部署

- 修改`.env.production`，指定服务端地址
- 执行`npm run build:prod`进行打包，结果在`dist`下
- 把`dist`中的文件放到静态服务器上