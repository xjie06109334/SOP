package common

import (
	"crypto"
	"crypto/rand"
	"crypto/rsa"
	"crypto/x509"
	"encoding/base64"
	"encoding/pem"
	"errors"
	"sort"
	"strings"
)

const (
	PEM_BEGIN = "-----BEGIN RSA PRIVATE KEY-----\n"
	PEM_END = "\n-----END RSA PRIVATE KEY-----"
)

func CreateSign(allParams map[string]string, privateKey string, signType string) string {
	signContent := GetSignContent(allParams)
	return Sign(signContent, privateKey, signType)
}

func Sign(signContent string, privateKey string, signType string) string  {
	if signType == "RSA" {
		return RsaSign(signContent, privateKey, crypto.SHA1)
	} else if signType == "RSA2"  {
		return RsaSign(signContent, privateKey, crypto.SHA256)
	} else {
		panic(errors.New("signType错误"))
	} 
}

func RsaSign(signContent string, privateKey string, hash crypto.Hash) string {
	shaNew := hash.New()
	shaNew.Write([]byte(signContent))
	hashed := shaNew.Sum(nil)
	priKey, err := ParsePrivateKey(privateKey)
	if err != nil {
		panic(err)
	}

	signature, err := rsa.SignPKCS1v15(rand.Reader, priKey, hash, hashed)
	if err != nil {
		panic(err)
	}
	return base64.StdEncoding.EncodeToString(signature)
}

func ParsePrivateKey(privateKey string)(*rsa.PrivateKey, error) {
	privateKey = FormatPrivateKey(privateKey)
	// 2、解码私钥字节，生成加密对象
	block, _ := pem.Decode([]byte(privateKey))
	if block == nil {
		return nil, errors.New("私钥信息错误！")
	}
	// 3、解析DER编码的私钥，生成私钥对象
	priKey, err := x509.ParsePKCS1PrivateKey(block.Bytes)
	if err != nil {
		return nil, err
	}
	return priKey, nil
}

func FormatPrivateKey(privateKey string) string  {
	if !strings.HasPrefix(privateKey, PEM_BEGIN) {
		privateKey = PEM_BEGIN + privateKey
	}
	if !strings.HasSuffix(privateKey, PEM_END) {
		privateKey = privateKey + PEM_END
	}
	return privateKey
}


/**
1.筛选并排序
获取所有请求参数，不包括字节类型参数，如文件、字节流，剔除sign字段，剔除值为空的参数，并按照参数名ASCII码递增排序（字母升序排序），
如果遇到相同字符则按照第二个字符的键值ASCII码递增排序，以此类推。

2.拼接
将排序后的参数与其对应值，组合成“参数=参数值”的格式，并且把这些参数用&字符连接起来，此时生成的字符串为待签名字符串。
 */
func GetSignContent(allParams map[string]string) string {
	keys := make([]string, 0, len(allParams))
	var result []string
	for k := range allParams {
		keys = append(keys, k)
	}
	sort.Strings(keys)
	for _, key := range keys {
		val := allParams[key]
		if len(val) > 0 {
			result = append(result, key + "=" + val)
		}
	}
	return strings.Join(result, "&")
}