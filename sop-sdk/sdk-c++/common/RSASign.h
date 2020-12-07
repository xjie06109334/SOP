#pragma once

#include <openssl/objects.h>
#include <openssl/rsa.h>
#include <openssl/pem.h> 
#include <openssl/bio.h>
#include <openssl/err.h>  
#include <string>

using namespace std;

#define PUBLIC_KEY_FILE_EX		"publicEx.pem"		//公钥文件
#define PRIVATE_KEY_FILE_EX		"privateEx.pem"		//私钥文件

RSA* GetPublicKeyEx(char* szPath);
RSA* GetPrivateKeyEx(char* szPath);

bool RSASignAction(const string& strEnData,char *privateKeyFilePath, string &strSigned);
bool RSAVerifyAction(string strEnData, string &strSigned);
