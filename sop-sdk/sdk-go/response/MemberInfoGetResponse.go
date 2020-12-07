package response

type MemberInfoGetResponse struct {
	BaseResponse

	Id         int32      `json:"id"`
	Name       string     `json:"name"`
	MemberInfo MemberInfo `json:"member_info"`
}

type MemberInfo struct {
	IsVip      int32      `json:"is_vip"`
	VipEndtime string `json:"vip_endtime"`
}
