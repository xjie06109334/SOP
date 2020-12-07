use crate::request::{BaseRequest, Request, RequestType};

pub struct MemberInfoGetRequest {
    // 固定这么写
    pub base: BaseRequest
}

impl Request for MemberInfoGetRequest {

    fn get_method(&self) -> &str {
        "member.info.get"
    }

    fn get_version(&self) -> &str {
        "1.0"
    }

    fn get_request_type(&self) -> RequestType {
        RequestType::Get
    }

    // 固定这么写
    fn get_base(&self) -> &BaseRequest {
        &self.base
    }
}
