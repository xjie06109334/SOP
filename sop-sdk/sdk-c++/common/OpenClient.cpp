#include "OpenClient.h"
#include "httplib.h"
#include "tool.h"
#include "sign.h"

#include "../thirdparty/CJsonObject/CJsonObject.hpp"

httplib::Headers headers = {
        {"Accept-Encoding", "identity"}
};

const string ERROR_NODE = "error_response";

OpenClient::OpenClient(const string &appId, const string &privateKeyFilePath, const string &url) {
    this->appId = appId;
    this->privateKeyFilePath = privateKeyFilePath;

    char *_url = const_cast<char *>(url.c_str());
    char *host;
    int port;
    char *path;
    tool::parse_url(_url, &host, &port, &path);
    this->hostInfo = HostInfo{
            host = host,
            port = port,
            path = path
    };
}

neb::CJsonObject OpenClient::execute(BaseRequest *request) {
    return this->execute(request, "");
}

neb::CJsonObject OpenClient::execute(BaseRequest *request,  const string &token) {
    string method = request->getMethod();
    string version = request->getVersion();
    RequestType requestType = request->getRequestType();
    map<string, string> bizModel = request->bizModel;
    // 创建HTTP请求客户端
    httplib::Client cli(this->hostInfo.host, this->hostInfo.port);
    // 构建请求参数
    map<string, string> allParams = this->buildParams(request, token);
    char *path = this->hostInfo.path;
    string responseBody;
    // 如果有文件上传
    if (!request->getFiles().empty()) {
        httplib::MultipartFormDataItems items = OpenClient::getMultipartFormDataItems(
                allParams, request->getFiles());
        responseBody = cli.Post(path, headers, items)->body;
    } else {
        switch (requestType) {
            case GET: {
                responseBody = cli.Get(path, allParams, headers)->body;
                break;
            }
            case POST_FORM: {
                responseBody = cli.Post(path, headers, OpenClient::getParams(allParams))->body;
                break;
            }
            case POST_JSON: {
                string json = tool::mapToJson(allParams);
                responseBody = cli.Post(path, json, "application/json")->body;
                break;
            }
            case POST_FILE: {
                httplib::MultipartFormDataItems items = OpenClient::getMultipartFormDataItems(
                        allParams, request->getFiles());
                responseBody = cli.Post(path, headers, items)->body;
            }
        }
    }
    return OpenClient::parseResponse(responseBody, request);
}


httplib::Params OpenClient::getParams(map<string, string> allParams) {
    httplib::Params params;
    map<string, string>::iterator it;
    for (it = allParams.begin(); it != allParams.end(); ++it) {
        params.emplace(it->first, it->second);
    }
    return params;
}

map<string, string> OpenClient::buildParams(BaseRequest *request, const string &token) {
    map<string, string> allParams;
    allParams["app_id"] = this->appId;
    allParams["method"] = request->getMethod();
    allParams["charset"] = "UTF-8";
    allParams["sign_type"] = "RSA2";
    allParams["timestamp"] = tool::getTime();
    allParams["version"] = request->getVersion();

    if (!token.empty()) {
        allParams["app_auth_token"] = token;
    }

    map<string, string> bizModel = request->bizModel;

    allParams.insert(bizModel.begin(), bizModel.end());

    // 生成签名
    string sign = signutil::createSign(allParams, this->privateKeyFilePath, "RSA2");
    allParams["sign"] = sign;
    return allParams;
}

httplib::MultipartFormDataItems
OpenClient::getMultipartFormDataItems(map<string, string> allParams, vector<FileInfo> fileInfoList) {
    httplib::MultipartFormDataItems items = {};
    map<string, string>::iterator it;
    for (it = allParams.begin(); it != allParams.end(); ++it) {
        items.push_back({it->first, it->second, "", ""});
    }
    // 添加上传文件
    vector<FileInfo>::iterator vit;
    for (vit = fileInfoList.begin(); vit != fileInfoList.end(); vit++) {
        string content = tool::getFileContent(vit->filepath);
        items.push_back({vit->name, content, tool::getFilename(vit->filepath), "application/octet-stream"});
    }
    return items;
}

neb::CJsonObject OpenClient::parseResponse(const string& responseBody, BaseRequest *request) {
    neb::CJsonObject oJson(responseBody);
    neb::CJsonObject data = oJson[ERROR_NODE];
    if (data.IsEmpty()) {
        string method = request->getMethod();
        string nodeName = tool::replace(method.c_str(),".","_") + "_response";
        data = oJson[nodeName];
    }
    return data;
}
