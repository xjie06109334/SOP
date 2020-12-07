extern crate serde;

use serde::{Deserialize};

// 响应参数
#[derive(Deserialize, Debug)]
pub struct MemberInfoGetResponse {
    // ~~~ 固定部分 ~~~
    pub code: String,
    pub msg: String,
    // json中可能没有sub_code属性，因此需要加上 #[serde(default)]
    // 详见：https://serde.rs/field-attrs.html
    #[serde(default)]
    pub sub_code: String,
    #[serde(default)]
    pub sub_msg: String,
    // ~~~ 固定部分 ~~~

    // 下面是业务字段
    #[serde(default)]
    pub id: u32,

    #[serde(default)]
    pub name: String,

    pub member_info: MemberInfo
}

#[derive(Deserialize, Debug)]
pub struct MemberInfo {
    #[serde(default)]
    pub is_vip: i8,

    #[serde(default)]
    pub vip_endtime: String
}