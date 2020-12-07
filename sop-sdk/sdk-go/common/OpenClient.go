package common

import (
	"encoding/json"
	"errors"
	"reflect"
	"strings"
	"time"
)

var headers = map[string]string{
	"Accept-Encoding": "identity",
}

type IClient interface {
	Execute() string
}

type OpenClient struct {
	AppId      string
	PrivateKey string
	Url        string
}

func (client OpenClient) ExecuteToken(iRequest IRequest, token string) []byte {
	model := iRequest.GetModel()
	bizModel := model.BizModel
	types := reflect.TypeOf(bizModel)
	values := reflect.ValueOf(bizModel)
	params := make(map[string]interface{})
	//遍历结构体的所有字段
	for i := 0; i < values.NumField(); i++ {
		// 获取到struct标签，需要通过reflect.Type来获取tag标签的值
		fieldName := types.Field(i).Tag.Get("json")
		// 如果该字段有tag标签就显示，否则就不显示
		if fieldName != "" {
			params[fieldName] = values.Field(i).Interface()
		}
	}

	requestType := iRequest.GetRequestType()
	var response string
	allParams := client.buildParams(iRequest, params, token)
	if model.Files != nil && len(model.Files) > 0 {
		response = PostFile(client.Url, allParams, model.Files, headers)
	} else {
		switch requestType {
		case GET:
			response = Get(client.Url, allParams, headers)
		case POST_FORM:
			response = PostForm(client.Url, allParams, headers)
		case POST_JSON:
			response = PostJson(client.Url, allParams, headers)
		case POST_UPLOAD:
			response = PostFile(client.Url, allParams, model.Files, headers)
		default:
			panic(errors.New("GetRequestType()返回错误"))
		}
	}
	return parseResponseResult(iRequest, response)
}

func parseResponseResult(iRequest IRequest, response string) []byte {
	var responseRoot = map[string]interface{}{}
	var err = json.Unmarshal([]byte(response), &responseRoot)
	if err != nil {
		panic(err)
	}
	requestId := responseRoot["request_id"].(string)
	var responseDataMap = responseRoot["error_response"]
	if responseDataMap == nil {
		dataName := strings.ReplaceAll(iRequest.GetMethod(), ".", "_") + "_response"
		responseDataMap = responseRoot[dataName]
	}
	responseDataMap.(map[string]interface{})["request_id"] = requestId
	// json数据
	dataJsonBytes, _ := json.Marshal(responseDataMap)
	return dataJsonBytes
}

func (client OpenClient) buildParams(iRequest IRequest, params map[string]interface{}, token string) map[string]string {
	allParams := map[string]string{
		"app_id":    client.AppId,
		"method":    iRequest.GetMethod(),
		"charset":   "UTF-8",
		"sign_type":   "RSA2",
		"timestamp": time.Now().Format("2006-01-02 15:04:05"),
		"version":   iRequest.GetVersion(),
	}

	if token != "" {
		allParams["access_token"] = token
	}

	// 添加业务参数
	for k, v := range params {
		allParams[k] = ToString(v)
	}

	// 构建sign
	sign := CreateSign(allParams, client.PrivateKey, "RSA2")
	allParams["sign"] = sign
	return allParams
}

func (client OpenClient) Execute(iRequest IRequest) []byte {
	return client.ExecuteToken(iRequest, "")
}
