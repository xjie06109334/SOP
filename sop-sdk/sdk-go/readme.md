# sdk-go

```go

// 应用ID
const appId string = "xx"
// 应用私钥
const privateKey string = "xx"
// 请求地址
const url string = "http://localhost:7071/prod/gw68uy85"

// 请求客户端
var openClient = common.OpenClient{AppId: appId, PrivateKey: privateKey, Url: url}

func main() {
	// 创建请求
	memberInfoGetRequest := request.MemberInfoGetRequest{}
	// 请求参数
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
```