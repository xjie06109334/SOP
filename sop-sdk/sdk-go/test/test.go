package main

import (
	"../common"
	"../model"
	"../request"
	"../response"
	"fmt"
)

// 应用ID
const appId string = "201904035630907729292csharp"
// 应用私钥
const privateKey string = "MIIEowIBAAKCAQEA5+OvJxeSzf44NxQ/cl7Ii+BzPg2k6sRcvH4ffOtU5Dzq1/oEvg02nxIhmwOHBZmjbmuUu0aLsfglUTAwqfXftfAKZidshsgj9NNh0/kxk0avRZ1UoljWGz/FxVZA0ogbxxhohPZ9jWcD+eBQcIwF2DtHfAJqWWZrYFnCMeHD8mPzxo2kwXSvDzi0vf9I2tKiYvNG26a9FqeYtPOoi81sdS3+70HOMdxP8ejXtyfnKpKz7Dx506LCIRS5moWS3Q5eTLV3NGX/1CSJ8wpQA2DAQTjVhX5eVu7Yqz12t8W+sjWM/tHUR6cgwYYR10p7tSCeCPzkigjGxKm4cYXWtATQJQIDAQABAoIBAHFDsgrrJca+NKEan77ycwx3jnKx4WrWjOF4zVKL9AQjiSYDNgvKknJyPb3kpC/lEoHdxGERHSzJoxib7DkoIqRQYhPxj73pxj5QfYk3P7LLJNNg/LTrpXDb3nL8JV9wIflGf87qQvstZTDJEyFWE4jBs7Hr0BxovWvri8InnzkmERJ1cbGJgNHe1Y3Zo2tw0yaHxQCxLuajP+notRZhD9bEp7uKeI0w9AvlW6k8m/7y10F0BK/TlyW8rQiEC391yOiRYoMcUh4hd2Q9bMx3jngZgX8PXIvZZcup4/pvWlv1alwhB2tsnLdazP62r1MO80vLyLunzGO+7WwCjEYlVaECgYEA+lQRFmbhKaPuAuXMtY31Fbga8nedka5TjnEV7+/kX+yowE2OlNujF+ZG8UTddTxAGv56yVNi/mjRlgD74j8z0eOsgvOq9mwbCrgLhLo51H9O/wAxtb+hBKtC5l50pBr4gER6d8W6EQNTSGojnMIaLXTkAZ5Qf6Z8e2HFVdOn0X0CgYEA7SSrTokwzukt5KldNu5ukyyd+C3D1i6orbg6qD73EP9CfNMfGSBn7dDv9wMSJH01+Ty+RgTROgtjGRDbMJWnfbdt/61NePr9ar5sb6Nbsf7/I0w7cZF5dsaFYgzaOfQYquzXPbLQHkpMT64bqpv/Mwy4F2lFvaYWY5fA4pC2uckCgYEAg75Ym9ybJaoTqky8ttQ2Jy8UZ4VSVQhVC0My02sCWwWXLlXi8y7An+Rec73Ve0yxREOn5WrQT6pkmzh7V/ABWrYi5WxODpCIjtSbo0fLBa3Wqle00b0/hdCITetqIa/cFs1zUrOqICgK3bKWeXqiAkhhcwSZwwSgwOKM04Wn7ZUCgYBvhHX2mbdVJfyJ8kc+hMOE/E9RHRxiBVEXWHJlGi8PVCqNDq8qHr4g7Mdbzprig+s0yKblwHAvrpkseWvKHiZEjVTyDipHgShY4TGXEigVvUd37uppTrLi8xpYcJjS9gH/px7VCdiq1d+q/MJP6coJ1KphgATm2UrgDMYNBWaYWQKBgEHRxrmER7btUF60/YgcqPHFc8RpYQB2ZZE0kyKGDqk2Data1XYUY6vsPAU28yRLAaWr/D2H17iyLkxP80VLm6QhifxCadv90Q/Wl1DFfOJQMW6avyQ0so6G0wFq/LJxaFK4iLXQn1RJnmTp6BYiJMmK2BhFbRzw8ssMoF6ad2rr"
// 请求路径
const url string = "http://localhost:8081"

// 请求客户端
var openClient = common.OpenClient{AppId: appId, PrivateKey: privateKey, Url: url}

func main() {
	// 创建请求
	memberInfoGetRequest := request.MemberInfoGetRequest{}
	// 设置请求参数
	memberInfoGetRequest.BizModel = model.MemberInfoGetModel{Name: "jim", Age: 22, Address: "xx"}

	// 添加上传文件
	//path, _ := os.Getwd()
	//files := []common.UploadFile{
	//	{Name:"file1", Filepath:path + "/test/aa.txt"},
	//	{Name:"file2", Filepath:path + "/test/bb.txt"},
	//}
	//memberInfoGetRequest.Files = files

	// 发送请求，返回json bytes
	var jsonBytes = openClient.Execute(memberInfoGetRequest)
	fmt.Printf("data:%s\n", string(jsonBytes))
	// 转换结果
	var memberInfoGetResponse response.MemberInfoGetResponse
	response.ConvertResponse(jsonBytes, &memberInfoGetResponse)

	if memberInfoGetResponse.IsSuccess() {
		fmt.Printf("is_vip:%d, vip_endtime:%s\n", memberInfoGetResponse.MemberInfo.IsVip, memberInfoGetResponse.MemberInfo.VipEndtime)
	} else {
		fmt.Printf("code:%s, msg:%s, subCode:%s, subMsg:%s\n",
			memberInfoGetResponse.Code, memberInfoGetResponse.Msg, memberInfoGetResponse.SubCode, memberInfoGetResponse.SubMsg)
	}
}
