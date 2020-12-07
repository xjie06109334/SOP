#!/bin/sh

echo "开始构建..."

rm -rf dist/*
npm run build:prod
echo "复制dist文件内容到sop-admin-server/src/main/resources/public"
rm -rf ../sop-admin-server/src/main/resources/public/*
cp -r dist/* ../sop-admin-server/src/main/resources/public

echo "构建完毕"
