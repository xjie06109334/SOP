#include <clocale>
#include "tool.h"
#include <map>
#include <fstream>
#include <sstream>

namespace tool {
    bool endWith(const string &str, const string &tail) {
        return str.compare(str.size() - tail.size(), tail.size(), tail) == 0;
    }

    bool startWith(const string &str, const string &head) {
        return str.compare(0, head.size(), head) == 0;
    }

    string getTime() {
        time_t timep;
        time(&timep);
        char tmp[64];
        strftime(tmp, sizeof(tmp), "%Y-%m-%d %H:%M:%S", localtime(&timep));
        return tmp;
    }

    int parse_url(char *url, char **serverstrp, int *portp, char **pathstrp) {
        char buf[256];
        int serverlen, numread = 0;
        /* go through the url */

        /* reset url to point PAST the http:// */

        /* assume it's always 7 chars! */
        string _url = url;
        bool isHttps = startWith(_url, "https");
        int len = 7;
        if (isHttps) {
            len = 8;
        }
        url = url + len;
        /* no http:// now... server is simply up to the next / or : */

        sscanf(url, "%255[^/:]", buf);
        serverlen = strlen(buf);
        *serverstrp = (char *) malloc(serverlen + 1);
        strcpy(*serverstrp, buf);
        if (url[serverlen] == ':') {
            /* get the port */

            sscanf(&url[serverlen + 1], "%d%n", portp, &numread);
            /* add one to go PAST it */

            numread++;

        } else {
            if (isHttps) {
                *portp = 443;
            } else {
                *portp = 80;
            }

        }
        /* the path is a pointer into the rest of url */

        *pathstrp = &url[serverlen + numread];
        return 0;
    }

    std::string url_encode(const std::string &str) {
        std::string strTemp = "";
        size_t length = str.length();
        for (size_t i = 0; i < length; i++) {
            if (isalnum((unsigned char) str[i]) ||
                (str[i] == '-') ||
                (str[i] == '_') ||
                (str[i] == '.') ||
                (str[i] == '~'))
                strTemp += str[i];
            else if (str[i] == ' ')
                strTemp += "+";
            else {
                strTemp += '%';
                strTemp += ToHex((unsigned char) str[i] >> 4);
                strTemp += ToHex((unsigned char) str[i] % 16);
            }
        }
        return strTemp;
    }


    std::string url_decode(const std::string &str) {
        std::string strTemp = "";
        size_t length = str.length();
        for (size_t i = 0; i < length; i++) {
            if (str[i] == '+') strTemp += ' ';
            else if (str[i] == '%') {
                assert(i + 2 < length);
                unsigned char high = FromHex((unsigned char) str[++i]);
                unsigned char low = FromHex((unsigned char) str[++i]);
                strTemp += high * 16 + low;
            } else strTemp += str[i];
        }
        return strTemp;
    }


    unsigned char ToHex(unsigned char x) {
        return x > 9 ? x + 55 : x + 48;
    }

    unsigned char FromHex(unsigned char x) {
        unsigned char y;
        if (x >= 'A' && x <= 'Z') y = x - 'A' + 10;
        else if (x >= 'a' && x <= 'z') y = x - 'a' + 10;
        else if (x >= '0' && x <= '9') y = x - '0';
        else
            assert(0);
        return y;
    }

    string mapToJson(std::map<string, string> map_info) {
//        Json::Value jObject;
//        for (map<string, string>::const_iterator iter = map_info.begin( ); iter != map_info.end( ); ++iter)
//        {
//            jObject[iter->first] = iter->second;
//        }
//        return jObject.toStyledString();
        return "{}";
    }

    string getFilename(string filepath) {
        {
            if (filepath.empty()) {
                return "";
            }
            std::string::size_type iPos;
#ifdef Q_OS_WIN
            iPos = strFullName.find_last_of('\\') + 1;
#else
            iPos = filepath.find_last_of('/') + 1;
#endif

            return filepath.substr(iPos, filepath.length() - iPos);
        }
    }

    string getFileContent(string filepath) {
        ifstream fin(filepath);
        stringstream buffer;
        buffer << fin.rdbuf();
        string fileContent(buffer.str());
        fin.close();
        return fileContent;
    }

    std::string replace(const char *pszSrc, const char *pszOld, const char *pszNew)
    {
        std::string strContent, strTemp;
        strContent.assign( pszSrc );
        std::string::size_type nPos = 0;
        while( true )
        {
            nPos = strContent.find(pszOld, nPos);
            strTemp = strContent.substr(nPos+strlen(pszOld), strContent.length());
            if ( nPos == std::string::npos )
            {
                break;
            }
            strContent.replace(nPos,strContent.length(), pszNew );
            strContent.append(strTemp);
            nPos +=strlen(pszNew) - strlen(pszOld)+1; //防止重复替换 避免死循环
        }
        return strContent;
    }

}