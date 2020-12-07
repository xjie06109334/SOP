extern crate sdk;

use std::collections::HashMap;

use sdk::client::OpenClient;
use sdk::http::UploadFile;
use sdk::request::BaseRequest;
use sdk::request::memberinfoget::MemberInfoGetRequest;
use sdk::response::memberinfoget::MemberInfoGetResponse;
use std::env;

fn main() {
    // 创建请求客户端
    let client = OpenClient {
        // 应用ID
        app_id: "2020051325943082302177280",
        // 应用私钥
        private_key: "MIICXAIBAAKBgQCHJlAPN+1dCbgc3HiahQkT2W/skecGWOCkSX4CPvEc8oIk6544\nxihEwShHnfrapiQdF2fndv5agrhg4FyOHheST42L5MnCk+4Km+mWm5GDvmFS7Sa2\naZ5o3regY0MUoJ7D74dYjE3UYFuTujAXiXjGpAwa9qOcKotov5LCkSfUeQIDAQAB\nAoGAB1cyw8LYRQSHQCUO9Wiaq730jPNHSrJW4EGAIz/XMYjv/fCgx0lnDEX4CbzI\nUGoz/bME4R721YRyXoutJ0h14/cGrt/TEn/TMI0xnISzJHr8VSlyBkQEdfO/W3LO\nqjs/UYq2Bz4+kJROJHreM+7d5hiIWLzLBlyI8cSU92ySmHECQQDwju2SoRu88kQP\n1qr4seZyKQa8DHTVyCoa6LtPLXyJsdgWgY4KyqJHwMUumEC2Zhhu833CR0ZXbfta\nuQDmwAVJAkEAj9M225jrPasaD5vPO7thxtEqgV4F/ZyNKH0Z8bDH27KaKkQ+8GMt\nkxwKVckZXs2bMvg/6tCiDZkWAxawNrvFsQJBANmTrPWOmpQPW9gnhYRjA9gFm33C\nlno2DT9BeQloTtgL7zKMA3lnRdg4VyCJvR48waS4vupVpR228D1iT5pl22ECQF1M\nJUzkcM0rPheb+h2EW1QOgWU0Keyvbj4ykO7gv3T78dezN6TWoUzJpsapUiTWeXPh\n6AyZ1FW/1bChOiP3QLECQGAbObmsYlN0bjzPYChwWYeYjErXuv51a44GZCNWinFw\nGGiHU9ZAqF8RzmBVW4htwj0j/Yry/V1Sp0uoP0zu3uA=",
        // 请求地址
        url: "http://localhost:7071/prod/gw68uy85",
    };
    // 业务参数
    let mut biz_model = HashMap::new();
    biz_model.insert("name", "jim".to_string());
    biz_model.insert("address", "xx".to_string());
    biz_model.insert("age", "22".to_string());

    let mut files = vec![];

    // 添加上传文件
    /*let mod_dir = env::current_dir().unwrap().display().to_string();
    files = vec![
        UploadFile { name:"file1", path: format!("{}/{}", &mod_dir, "aa.txt")},
        UploadFile { name:"file2", path: format!("{}/{}", &mod_dir, "bb.txt") },
    ];*/


    // 创建请求，设置业务参数
    let request = MemberInfoGetRequest {
        base: BaseRequest { biz_model: biz_model, files: files }
    };

    // 发送请求
    let response:MemberInfoGetResponse = client.execute(request);

    // 成功
    if response.sub_code.len() == 0 {
        println!("resp:{:#?}", response)
    } else {
        println!("error:{:#?}", response)
    }
}

#[cfg(test)]
mod tests {
    use sdk::sign::{HashType, SignUtil};
    use sdk::http::HttpTool;
    use std::collections::HashMap;

    #[test]
    fn it_works() {
        let content = "123";
        let private_key = "MIICXAIBAAKBgQCHJlAPN+1dCbgc3HiahQkT2W/skecGWOCkSX4CPvEc8oIk6544\nxihEwShHnfrapiQdF2fndv5agrhg4FyOHheST42L5MnCk+4Km+mWm5GDvmFS7Sa2\naZ5o3regY0MUoJ7D74dYjE3UYFuTujAXiXjGpAwa9qOcKotov5LCkSfUeQIDAQAB\nAoGAB1cyw8LYRQSHQCUO9Wiaq730jPNHSrJW4EGAIz/XMYjv/fCgx0lnDEX4CbzI\nUGoz/bME4R721YRyXoutJ0h14/cGrt/TEn/TMI0xnISzJHr8VSlyBkQEdfO/W3LO\nqjs/UYq2Bz4+kJROJHreM+7d5hiIWLzLBlyI8cSU92ySmHECQQDwju2SoRu88kQP\n1qr4seZyKQa8DHTVyCoa6LtPLXyJsdgWgY4KyqJHwMUumEC2Zhhu833CR0ZXbfta\nuQDmwAVJAkEAj9M225jrPasaD5vPO7thxtEqgV4F/ZyNKH0Z8bDH27KaKkQ+8GMt\nkxwKVckZXs2bMvg/6tCiDZkWAxawNrvFsQJBANmTrPWOmpQPW9gnhYRjA9gFm33C\nlno2DT9BeQloTtgL7zKMA3lnRdg4VyCJvR48waS4vupVpR228D1iT5pl22ECQF1M\nJUzkcM0rPheb+h2EW1QOgWU0Keyvbj4ykO7gv3T78dezN6TWoUzJpsapUiTWeXPh\n6AyZ1FW/1bChOiP3QLECQGAbObmsYlN0bjzPYChwWYeYjErXuv51a44GZCNWinFw\nGGiHU9ZAqF8RzmBVW4htwj0j/Yry/V1Sp0uoP0zu3uA=";
        let sign = SignUtil::rsa_sign(content, private_key, HashType::Sha256);
        println!("sign:{}", sign);
    }

    #[test]
    fn test_http() {
        let mut map = HashMap::new();
        map.insert("aaa", "bbb");
        let response = HttpTool::get("http://baidu.com", &map, &HashMap::new());
        println!("response:{:#?}", response);
    }

}