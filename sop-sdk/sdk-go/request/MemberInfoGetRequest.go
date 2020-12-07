package request

import "../common"

type MemberInfoGetRequest struct {
	common.Model
}

func (MemberInfoGetRequest) GetMethod() string {
	return "member.info.get"
}

func (MemberInfoGetRequest) GetVersion() string {
	return "1.0"
}

func (MemberInfoGetRequest) GetRequestType() common.RequestType {
	return common.GET
}

func (req MemberInfoGetRequest) GetModel() common.Model {
	return req.Model
}
