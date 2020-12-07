extern crate rsa;
extern crate crypto;

use std::collections::HashMap;

use rsa::{RSAPrivateKey, PaddingScheme};
use rsa::Hash;

use crypto::sha2::Sha256;
use crypto::digest::Digest;
use std::iter::repeat;

pub struct SignUtil {}

pub enum SignType {
    RSA,
    RSA2,
}

pub enum HashType {
    Sha1,
    Sha256
}

impl SignUtil {

    pub fn create_sign(all_params: &HashMap<&str, String>, private_key: &str, sign_type: SignType) -> String {
        let content = SignUtil::get_sign_content(all_params);
        // println!("content:{}", content);
        let hash_type;
        match sign_type {
            SignType::RSA => hash_type = HashType::Sha1,
            SignType::RSA2 => hash_type = HashType::Sha256,
        }

        SignUtil::rsa_sign(content.as_str(), private_key,  hash_type)
    }

    /// RSA签名
    ///
    /// - content: 签名内容
    /// - private_key: 私钥，PKCS#1
    /// - hash_type: hash类型
    ///
    /// # Examples
    ///
    /// ```
    /// use sdk::sign::{SignUtil, HashType};
    ///
    /// let content = "123";
    /// let private_key = "your private key";
    /// let sign = SignUtil::rsa_sign(content, private_key, HashType::Sha256);
    ///
    /// println!("sign:{}", sign);
    /// ```
    /// return: 返回base64字符串
    pub fn rsa_sign(content: &str, private_key: &str, hash_type: HashType) -> String {
        // 格式化私钥
        let der_encoded = private_key
             .lines()
             .filter(|line| !line.starts_with("-"))
             .fold(String::new(), |mut data, line| {
                 data.push_str(&line);
                 data
             });
        let der_bytes = base64::decode(der_encoded).expect("failed to decode base64 content");
        // 获取私钥对象
        let private_key = RSAPrivateKey::from_pkcs1(&der_bytes).expect("failed to parse key");

        // 创建一个Sha256对象
        let mut hasher = Sha256::new();
        // 对内容进行摘要
        hasher.input_str(content);
        // 将摘要结果保存到buf中
        let mut buf: Vec<u8> = repeat(0).take((hasher.output_bits()+7)/8).collect();
        hasher.result(&mut buf);

        // 对摘要进行签名
        let hash;
        match hash_type {
            HashType::Sha1 => hash = Hash::SHA1,
            HashType::Sha256 => hash = Hash::SHA2_256
        }
        let sign_result = private_key.sign(PaddingScheme::PKCS1v15Sign {hash: Option::from(hash) }, &buf);
        // 签名结果转化为base64
        let vec = sign_result.expect("create sign error for base64");

        base64::encode(vec)
    }

    fn get_sign_content(all_params: &HashMap<&str, String>) -> String {
        let mut content = Vec::new();
        let keys = all_params.keys();
        let mut sorted_keys = Vec::new();
        for key in keys {
            sorted_keys.push(key);
        }
        // 排序
        sorted_keys.sort();
        for key in sorted_keys {
            let val = all_params.get(key);
            if val.is_some() {
                content.push(key.to_string() + "=" + val.unwrap());
            }
        }
        content.join("&")
    }
}