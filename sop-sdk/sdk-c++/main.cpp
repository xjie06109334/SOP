#include <iostream>

#include "common/OpenClient.h"
#include "request/BaseRequest.h"
#include "request/MemberInfoGetRequest.hpp"

// 应用ID
string appId = "2020051325943082302177280";
// 存放私钥的文件路径
string privateKeyFile = "/Users/thc/IdeaProjects/opc/opc-sdk/sdk-c++/privateEx.pem";
// 请求接口
string url = "http://localhost:7071/prod/gw68uy85";

OpenClient openClient(appId, privateKeyFile, url);

int main() {
    // 创建请求
    MemberInfoGetRequest request;

    // 业务参数
    map<string, string> bizModel;
    bizModel["name"] = "jim";
    bizModel["age"] = "22";
    bizModel["address"] = "xx";

    request.bizModel = bizModel;

    // 添加上传文件
//    request->setFiles({
//        FileInfo{"aa", "/Users/thc/IdeaProjects/opc/opc-sdk/sdk-c++/aa.txt"},
//        FileInfo{"bb", "/Users/thc/IdeaProjects/opc/opc-sdk/sdk-c++/bb.txt"}
//    });

    // 发送请求
    neb::CJsonObject jsonObj = openClient.execute(&request);
    std::cout << jsonObj.ToString() << std::endl;
    std::cout << "id:" << jsonObj["id"].ToString() << std::endl;
    std::cout << "is_vip:" << jsonObj["member_info"]["is_vip"].ToString() << std::endl;
    return 0;
}

