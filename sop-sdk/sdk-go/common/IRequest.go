package common

type RequestType string

const (
	GET         RequestType = "GET"
	POST_JSON   RequestType = "POST_JSON"
	POST_FORM   RequestType = "POST_FORM"
	POST_UPLOAD RequestType = "POST_UPLOAD"
)

type Model struct {
	// 业务参数
	BizModel interface{}
	// 上传文件
	Files []UploadFile
}

type IRequest interface {
	GetMethod() string
	GetVersion() string
	GetRequestType() RequestType
	GetModel() Model
}
