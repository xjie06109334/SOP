# 门户网站

纯静态网站，作为开放平台基本介绍使用，可独立部署，logo制作：http://www.uugai.com/

# 初始化

安装node，建议安装11版本，12版本会有不兼容问题。

安装完毕执行：

npm install

# 启动

docsite start

# 打包

首先，需要配置下站点的根路径，修改site_config/site.js中的rootPath字段。规则如下：

当部署根路径为/，则设置为''空字符串即可。

当部署根路径不为/，则设置为具体的根路径，注意需以/开头，但不能有尾/。

运行`build.sh`

将dist内容上传到服务器

