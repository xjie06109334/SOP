#include <utility>

#include "../thirdparty/base64/base64.h"
#include "sign.h"
#include "RSASign.h"
#include "tool.h"

namespace signutil {
    string createSign(map<string, string> params, const string& privateKeyFilepath, const string& signType) {
        string content = getSignContent(std::move(params));
        return sign(content, privateKeyFilepath, "");
    }

    string sign(const string& content, const string& privateKeyFilepath, const string& hash) {
        BIO *bufio = NULL;
        RSA *rsa = NULL;
        EVP_PKEY *evpKey = NULL;
        bool verify = false;
        EVP_MD_CTX ctx;
        int result = 0;
        unsigned int size = 0;
        char *sign = NULL;
        std::string signStr = "";

        //bufio = BIO_new_mem_buf((void*)private_key, -1);
        //if (bufio == NULL) {
        //	ERR("BIO_new_mem_buf failed");
        //	goto safe_exit;
        //}
        bufio = BIO_new(BIO_s_file());
        // 读取私钥文件
        BIO_read_filename(bufio, privateKeyFilepath.c_str());

        // 私钥字符串转换成私钥对象
        rsa = PEM_read_bio_RSAPrivateKey(bufio, NULL, NULL, NULL);
        if (rsa == NULL) {
            ERR("PEM_read_bio_RSAPrivateKey failed");
            goto safe_exit;
        }

        evpKey = EVP_PKEY_new();
        if (evpKey == NULL) {
            ERR("EVP_PKEY_new failed");
            goto safe_exit;
        }

        if ((result = EVP_PKEY_set1_RSA(evpKey, rsa)) != 1) {
            ERR("EVP_PKEY_set1_RSA failed");
            goto safe_exit;
        }

        EVP_MD_CTX_init(&ctx);

        // SHA256签名
        if (result == 1 && (result = EVP_SignInit_ex(&ctx,
                                                     EVP_sha256(), NULL)) != 1) {
            ERR("EVP_SignInit_ex failed");
        }

        if (result == 1 && (result = EVP_SignUpdate(&ctx,
                                                    content.c_str(), content.size())) != 1) {
            ERR("EVP_SignUpdate failed");
        }

        size = EVP_PKEY_size(evpKey);
        sign = (char*)malloc(size+1);
        memset(sign, 0, size+1);

        if (result == 1 && (result = EVP_SignFinal(&ctx,
                                                   (unsigned char*)sign,
                                                   &size, evpKey)) != 1) {
            ERR("EVP_SignFinal failed");
        }

        if (result == 1) {
            verify = true;
        } else {
            ERR("verify failed");
        }

        signStr = base64_encode((const unsigned char*)sign, size);
        EVP_MD_CTX_cleanup(&ctx);
        free(sign);

        safe_exit:
        if (rsa != NULL) {
            RSA_free(rsa);
            rsa = NULL;
        }

        if (evpKey != NULL) {
            EVP_PKEY_free(evpKey);
            evpKey = NULL;
        }

        if (bufio != NULL) {
            BIO_free_all(bufio);
            bufio = NULL;
        }

        return signStr;

    }

    string getSignContent(map<string, string> params){
        map<string, string>::iterator iter;
        string content;
        for(iter = params.begin(); iter != params.end(); iter++) {
            content.append("&").append(iter->first + "=" + iter->second);
        }
        return content.substr(1);
    }

    void ERR(const string &msg) {
        throw msg;
    }
}

