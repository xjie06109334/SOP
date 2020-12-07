#!/bin/sh

echo "开始构建..."

dist_dir="dist"

if [ ! -d "$dist_dir" ]; then
  mkdir $dist_dir
fi

docsite build

echo "复制文件到${dist_dir}目录"

rm -rf $dist_dir/*
cp -r zh-cn/* $dist_dir
cp -r build $dist_dir/build
cp -r img $dist_dir/img

echo "构建完毕"
