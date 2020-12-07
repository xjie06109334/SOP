/*
Cargo.toml:

[dependencies]
reqwest = { version = "0.10", features = ["blocking", "json"] }
tokio = { version = "0.2", features = ["full"] }
serde = { version = "1.0.114", features = ["derive"] }
serde_json = "1.0.56"
 */
use std::collections::HashMap;

use reqwest::blocking::Response;
use reqwest::header::{HeaderMap, HeaderValue, HeaderName};
use std::str::FromStr;
use serde::Serialize;

/// HTTP请求工具
pub struct HttpTool {}

/// 上传文件对象
pub struct UploadFile {
    /// 上传文件表单名称
    pub name: &'static str,
    /// 文件全路径
    pub path: String,
}

impl HttpTool {

    /// get请求
    ///
    /// - url：请求url
    /// - params：请求参数
    /// - headers：请求header
    ///
    /// # Example
    ///
    /// ```rust
    /// let mut params = HashMap::new();
    /// params.insert("name", "Jim");
    /// params.insert("age", "12");
    /// let resp = HttpTool::get(url, &params, &HashMap::new());
    /// ```
    /// return: Response对象
    pub fn get<T: Serialize + ?Sized>(url: &str, params: &T, headers: &HashMap<&str, &str>) -> Response {
        let client = reqwest::blocking::Client::new();
        let res = client
            .get(url)
            .query(params)
            .headers(super::http::HttpTool::get_headers_map(headers))
            .send();
        let resp = res.expect("request error");
        resp
    }

    /// post模拟表单请求
    ///
    /// - url：请求url
    /// - params：请求参数
    /// - headers：请求header
    ///
    /// # Example
    ///
    /// ```rust
    /// let mut params = HashMap::new();
    /// params.insert("name", "Jim");
    /// params.insert("age", "12");
    /// let resp = HttpTool::post_form(url, &params, &HashMap::new());
    /// ```
    /// return: Response对象
    pub fn post_form<T: Serialize + ?Sized>(url: &str, params: &T, headers: &HashMap<&str, &str>) -> Response {
        let client = reqwest::blocking::Client::new();
        let res = client
            .post(url)
            .form(params)
            .headers(super::http::HttpTool::get_headers_map(headers))
            .send();
        let resp = res.expect("request error");
        resp
    }

    /// post发送json
    ///
    /// - url：请求url
    /// - params：请求参数
    /// - headers：请求header
    ///
    /// # Example
    ///
    /// ```rust
    /// let mut params = HashMap::new();
    /// params.insert("name", "Jim");
    /// params.insert("age", "12");
    /// let resp = HttpTool::post_json(url, &params, &HashMap::new());
    /// ```
    /// return: Response对象
    pub fn post_json<T: Serialize + ?Sized>(url: &str, params: &T, headers: &HashMap<&str, &str>) -> Response {
        let client = reqwest::blocking::Client::new();
        let res = client
            .post(url)
            .json(params)
            .headers(super::http::HttpTool::get_headers_map(headers))
            .send();
        let resp = res.expect("request error");
        resp
    }

    /// post上传文件
    ///
    /// - url：请求url
    /// - params：请求参数
    /// - files：上传文件
    /// - headers：请求header
    ///
    /// # Example
    ///
    /// ```rust
    /// let mut params = HashMap::new();
    /// params.insert("name", String::from("Jim"));
    /// params.insert("age",String::from("12"));
    /// 
    /// // 设置上传文件
    /// let mut files = vec![
    ///     UploadFile { name:"file1", path: String::from("/User/xx/aa.txt") },
    ///     UploadFile { name:"file2", path: String::from("/User/xx/bb.txt") },
    /// ];
    ///
    /// let resp = HttpTool::post_file(url, &params, &files, &HashMap::new());
    /// ```
    /// return: Response对象
    pub fn post_file(url: &str, params: &HashMap<&str, String>, files: &Vec<UploadFile>, headers: &HashMap<&str, &str>) -> Response {
        let client = reqwest::blocking::Client::new();
        let mut form = reqwest::blocking::multipart::Form::new();
        // 添加普通参数
        for entry in params {
            form = form.text(entry.0.to_string(), entry.1.to_string());
        }
        // 添加文件
        for file in files {
            form = form.file(file.name.to_string(), file.path.to_string()).unwrap();
        }
        let res = client
            .post(url)
            .multipart(form)
            .headers(super::http::HttpTool::get_headers_map(headers))
            .send();
        let resp = res.expect("request error");
        resp
    }

    fn get_headers_map(headers: &HashMap<&str, &str>) -> HeaderMap {
        let mut header_map = HeaderMap::new();
        for entry in headers {
            header_map.insert(HeaderName::from_str(entry.0).unwrap(), HeaderValue::from_str(entry.1).unwrap());
        }
        header_map
    }
}