use std::collections::HashMap;

use chrono::Local;
use serde::de::DeserializeOwned;

use crate::http::HttpTool;
use crate::request::{Request, RequestType};
use crate::sign::{SignType, SignUtil};
use serde_json::Value;

pub struct OpenClient {
    /// 应用ID
    pub app_id: &'static str,
    /// 应用私钥，PKCS#1
    pub private_key: &'static str,
    /// 请求url
    pub url: &'static str,
}

impl OpenClient {

    /// 发送请求
    ///
    /// - request： 请求对象
    ///
    /// 返回结果
    pub fn execute<T: DeserializeOwned>(&self, request: impl Request) -> T {
        self.execute_token(request, "")
    }

    ///发送请求
    ///
    /// - request： 请求对象
    /// - token：token
    ///
    /// 返回结果
    pub fn execute_token<T: DeserializeOwned>(&self, request: impl Request, token: &'static str) -> T {
        let struct_obj: T;

        let request_type = request.get_request_type();
        let headers = &OpenClient::get_default_headers();
        let all_params = &self.build_params(&request, token);

        if request.get_base().files.len() > 0 {
            let base = request.get_base();
            let files = &base.files;
            let resp = HttpTool::post_file(self.url, all_params, files, headers);
            struct_obj = self.parse_response(resp, &request);
        } else {
            match request_type {
                RequestType::Get => {
                    let resp = HttpTool::get(self.url, all_params, headers);
                    struct_obj = self.parse_response(resp, &request);
                }
                RequestType::PostForm => {
                    let resp = HttpTool::post_form(self.url, all_params, headers);
                    struct_obj = self.parse_response(resp, &request);
                }
                RequestType::PostJson => {
                    let resp = HttpTool::post_json(self.url, all_params, headers);
                    struct_obj = self.parse_response(resp, &request);
                }
                RequestType::PostFile => {
                    let base = request.get_base();
                    let files = &base.files;
                    let resp = HttpTool::post_file(self.url, all_params, files, headers);
                    struct_obj = self.parse_response(resp, &request);
                }
            }
        }
        struct_obj
    }

    fn get_default_headers() -> HashMap<&'static str, &'static str> {
        let mut headers = HashMap::new();
        headers.insert("Accept-Encoding", "identity");
        headers
    }

    fn parse_response<T: DeserializeOwned>(&self, resp: reqwest::blocking::Response, request: &impl Request) -> T {
        let root: HashMap<String, Value> = resp.json().expect("error");
        request.parse_response(root)
    }

    /// 构建请求参数
    fn build_params(&self, request: &impl Request, token: &str) -> HashMap<&'static str, String> {
        let method = request.get_method();
        let version = request.get_version();

        let mut all_params = HashMap::new();
        all_params.insert("app_id", self.app_id.to_string());
        all_params.insert("method", method.to_string());
        all_params.insert("charset", "UTF-8".to_string());
        all_params.insert("timestamp", Local::now().format("%Y-%m-%d %H:%M:%S").to_string());
        all_params.insert("version", version.to_string());

        // 添加业务参数
        for entry in &request.get_base().biz_model {
            all_params.insert(entry.0, entry.1.to_string());
        }

        if !token.is_empty() {
            all_params.insert("app_auth_token", token.to_string());
        }

        // 创建签名
        let sign = SignUtil::create_sign(&all_params, self.private_key, SignType::RSA2);
        all_params.insert("sign", sign);

        all_params
    }
}