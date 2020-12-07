

#ifndef SDK_CXX_SIGN_H
#define SDK_CXX_SIGN_H

#include <map>
#include <iostream>

#include <openssl/objects.h>
#include <openssl/rsa.h>
#include <openssl/pem.h>
#include <openssl/bio.h>
#include <openssl/err.h>
#include <string>

using namespace std;

namespace signutil {

    string createSign(map<string, string> params, const string& privateKeyFilepath, const string& signType);

    string sign(const string& content, const string& privateKeyFilepath, const string& hash);

    string getSignContent(map<string, string> params);

    void ERR(const string &msg);
}


#endif //SDK_CXX_SIGN_H
