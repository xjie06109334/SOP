

#ifndef SDK_CXX_TOOL_H
#define SDK_CXX_TOOL_H

#include <string>
#include <map>

using namespace std;

namespace tool {

    bool endWith(const string &str, const string &tail);

    bool startWith(const string &str, const string &head);

    string getTime();

    int parse_url(char *url, char **serverstrp, int *portp, char **pathstrp);

    std::string url_encode(const std::string &szToEncode);

    std::string url_decode(const std::string &SRC);

    unsigned char ToHex(unsigned char x);

    unsigned char FromHex(unsigned char x);

    string mapToJson(std::map<string, string> m);

    string getFilename(string filepath);

    string getFileContent(string filepath);


    /**
     *	函数：
     *		 replace（替换字符串）
     *  参数：
     *		pszSrc：源字符串
     *		pszOld：需要替换的字符串
     *		pszNew：新字符串
     *  返回值：
     *		返回替换后的字符串
     *	备注：
     *	需要添加#include <string>头文件
     * ssdwujianhua 2017/08/30
     */
    std::string replace(const char *pszSrc, const char *pszOld, const char *pszNew);
}

#endif //SDK_CXX_TOOL_H
