package response

import "encoding/json"

type IResponse interface {
	IsSuccess() bool
}

type BaseResponse struct {
	RequestId string `json:"request_id"`
	Code      string `json:"code"`
	Msg       string `json:"msg"`
	SubCode   string `json:"sub_code"`
	SubMsg    string `json:"sub_msg"`
}

func (resp BaseResponse) IsSuccess() bool {
	return len(resp.SubCode) == 0
}

func ConvertResponse(data []byte, ptr interface{})  {
	err := json.Unmarshal(data, ptr)
	if err != nil {
		panic(err)
	}
}
