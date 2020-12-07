#!/bin/bash

# 打包
mvn clean package
# 创建镜像
docker build -t gitee.com/sop .

# 获取镜像id
image_id=`docker images gitee.com/sop --format "{{.ID}}" | awk '{print $1}'`

# 运行镜像
docker run --name sop -p 8081:8081 -p 8082:8082 -p 8083:8083 -p 8087:8087 -p 2222:2222 -d $image_id