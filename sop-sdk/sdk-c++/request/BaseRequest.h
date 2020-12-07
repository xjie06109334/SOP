#ifndef SDK_CXX_BASEREQUEST_H
#define SDK_CXX_BASEREQUEST_H

#include <string>
#include <map>
#include <vector>
#include "../common/RequestType.h"

using namespace std;

struct FileInfo {
    /** 表单名称 */
    string name;
    /** 文件完整路径 */
    string filepath;
};


/**
 * 请求类基类，其它请求类需要继承这个类
 */
class BaseRequest {
public:

    /**
     * 业务参数
     */
    map<string, string> bizModel;

    /**
     * 定义接口名称
     * @return 返回接口名称
     */
    virtual string getMethod() = 0;

    /**
     * 定义接口版本号
     * @return 返回版本号
     */
    virtual string getVersion() = 0;

    /**
     * 定义请求方式
     * @return 返回请求方式
     */
    virtual RequestType getRequestType() = 0;

    vector<FileInfo> getFiles();

    void setFiles(vector<FileInfo> files);

private:
    vector<FileInfo> files = {};

    void addFile(const FileInfo& filepath);
};



#endif //SDK_CXX_BASEREQUEST_H
