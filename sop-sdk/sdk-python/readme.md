# sdk-python

安装

`pip install requests` 

`pip install rsa` 

- 调用方式

```python
    # 创建请求
    request = MemberInfoGetRequest()
    # 请求参数
    model = MemberInfoGetModel()
    model.age = 22
    model.name = 'jim'
    model.address = 'xx'
    # 添加请求参数
    request.biz_model = model

    # 添加上传文件
    # files = {
    #     'file1': open('aa.txt', 'rb'),
    #     'file2': open('bb.txt', 'rb')
    # }
    # request.files = files

    # 调用请求
    response = self.client.execute(request)

    if response.is_success():
        print 'response: ', response
        print 'is_vip:', response.get('member_info').get('is_vip', 0)
    else:
        print '请求失败,code:%s, msg:%s, sub_code:%s, sub_msg:%s' % \
              (response.code, response.msg, response.sub_code, response.sub_msg)
```

详见`test.py`

代码规范：

| Type                       | Public             | Internal                                                          | 
| -------------------------- | ------------------ | ----------------------------------------------------------------- |  
| Modules                    | lower_with_under   | _lower_with_under                                                 |  
| Packages                   | lower_with_under   |                                                                   |  
| Classes                    | CapWords           | _CapWords                                                         |  
| Exceptions                 | CapWords           |                                                                   |  
| Functions                  | lower_with_under() | _lower_with_under()                                               |  
| Global/Class Constants     | CAPS_WITH_UNDER    | _CAPS_WITH_UNDER                                                  |  
| Global/Class Variables     | lower_with_under   | _lower_with_under                                                 |  
| Instance Variables         | lower_with_under   | _lower_with_under (protected) or __lower_with_under (private)     |  
| Method Names               | lower_with_under() | _lower_with_under() (protected) or __lower_with_under() (private) |  
| Function/Method Parameters | lower_with_under   |                                                                   |  
| Local Variables            | lower_with_under   |              