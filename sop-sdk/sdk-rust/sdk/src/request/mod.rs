
use std::collections::HashMap;

use serde_json::Value;

use crate::http::UploadFile;
use serde::de::DeserializeOwned;

pub mod memberinfoget;

pub enum RequestType {
    Get,
    PostJson,
    PostForm,
    PostFile,
}

pub trait Request {
    /// 返回接口名称
    fn get_method(&self) -> &str;

    /// 返回版本号
    fn get_version(&self) -> &str;

    /// 返回请求方式
    fn get_request_type(&self) -> RequestType;

    /// 返回base
    fn get_base(&self) -> &BaseRequest;

    fn parse_response<T: DeserializeOwned>(&self, root: HashMap<String, Value>) -> T {
        let mut data = root.get("error_response");
        if data.is_none() {
            let data_name = self.get_method().replace(".", "_") + "_response";
            data = root.get(data_name.as_str());
        }
        let value = serde_json::to_value(data.unwrap()).unwrap();
        serde_json::from_value(value).unwrap()
    }
}

pub struct BaseRequest {
    pub biz_model: HashMap<&'static str, String>,
    pub files: Vec<UploadFile>
}
