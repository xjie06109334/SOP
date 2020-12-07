#include "stdafx.h"
#include "RSASign.h"
#include "../thirdparty/base64/base64.h"

RSA* GetPublicKeyEx(char* szPath)
{
    RSA *pubkey = RSA_new();

    BIO *pubio;

    pubio = BIO_new_file(szPath, "rb");
    pubkey = PEM_read_bio_RSAPublicKey(pubio, &pubkey, NULL, NULL);

    BIO_free(pubio);

    return pubkey;
}

RSA* GetPrivateKeyEx(char* szPath)
{
    RSA *prikey = RSA_new();

    BIO *priio;

    priio = BIO_new_file(szPath, "rb");
    prikey = PEM_read_bio_RSAPrivateKey(priio, &prikey, NULL, NULL);

    BIO_free(priio);

    return prikey;
}

bool RSASignAction(const string& strEnData, char *privateKeyFilePath, string &strSigned)
{
    int nlen = strEnData.length();
    RSA *prsa = NULL;
    if (NULL == (prsa = GetPrivateKeyEx(privateKeyFilePath)))
    {
        RSA_free(prsa);
        printf("获取私钥失败\n");
        return false;
    }

    char  szTmp[1024] = { 0 };
    //对待签名数据做SHA1摘要
    SHA256((const unsigned char*)strEnData.c_str(), nlen, (unsigned char*)szTmp);
    int nLength;
    unsigned int nLengthRet;
    char  szTmp1[1024] = { 0 };	//位数一定不能小于等于RSA的128位，没有‘\0’,数据会增加后面位数
//    unsigned char *szTmp1 = {0};
    nLength = RSA_sign(NID_sha256, (unsigned char *)szTmp, 32, (unsigned char *)szTmp1, &nLengthRet, prsa);
    if (nLength != 1)
    {
        RSA_free(prsa);
        return false;
    }
    strSigned = base64_encode((unsigned char *)szTmp1, strlen((const char *)szTmp1));
    RSA_free(prsa);

    return true;
}

bool RSAVerifyAction(string strEnData, string &strSigned)
{
    int nlen = strEnData.length();
    printf("验签的原始数据:[%s]\n", strEnData.c_str());

    RSA *prsa = NULL;
    if (NULL == (prsa = GetPublicKeyEx(PUBLIC_KEY_FILE_EX)))
    {
        RSA_free(prsa);
        printf("获取公钥失败\n");
        return -1;
    }

    //对待签名数据做SHA1摘要
    char  szTmp[1024] = { 0 };
    int nLen = strEnData.length();
    SHA256((const unsigned char*)strEnData.c_str(), nLen, (unsigned char*)szTmp);

    int nLength;
    unsigned int nLengthRet = strSigned.length();
    printf("nLengthRet2 = %d\n", nLengthRet);
    nLength = RSA_verify(NID_sha256, (unsigned char *)szTmp, 32, (unsigned char*)strSigned.c_str(), nLengthRet, prsa);
    if (nLength != 1)
    {
        RSA_free(prsa);
        return false;
    }

    RSA_free(prsa);

    return true;
}