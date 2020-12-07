#!/usr/bin/python
# -*- coding: UTF-8 -*-
import unittest

from common import JsonUtil
from model.MemberInfoGetModel import MemberInfoGetModel
from request.MemberInfoGetRequest import MemberInfoGetRequest
from common.OpenClient import OpenClient

import sys

reload(sys)
sys.setdefaultencoding('utf8')


class MyTestCase(unittest.TestCase):
    # 应用id
    app_id = '201904035630907729292csharp'
    # 应用私钥
    private_key = 'MIIEowIBAAKCAQEA5+OvJxeSzf44NxQ/cl7Ii+BzPg2k6sRcvH4ffOtU5Dzq1/oEvg02nxIhmwOHBZmjbmuUu0aLsfglUTAwqfXftfAKZidshsgj9NNh0/kxk0avRZ1UoljWGz/FxVZA0ogbxxhohPZ9jWcD+eBQcIwF2DtHfAJqWWZrYFnCMeHD8mPzxo2kwXSvDzi0vf9I2tKiYvNG26a9FqeYtPOoi81sdS3+70HOMdxP8ejXtyfnKpKz7Dx506LCIRS5moWS3Q5eTLV3NGX/1CSJ8wpQA2DAQTjVhX5eVu7Yqz12t8W+sjWM/tHUR6cgwYYR10p7tSCeCPzkigjGxKm4cYXWtATQJQIDAQABAoIBAHFDsgrrJca+NKEan77ycwx3jnKx4WrWjOF4zVKL9AQjiSYDNgvKknJyPb3kpC/lEoHdxGERHSzJoxib7DkoIqRQYhPxj73pxj5QfYk3P7LLJNNg/LTrpXDb3nL8JV9wIflGf87qQvstZTDJEyFWE4jBs7Hr0BxovWvri8InnzkmERJ1cbGJgNHe1Y3Zo2tw0yaHxQCxLuajP+notRZhD9bEp7uKeI0w9AvlW6k8m/7y10F0BK/TlyW8rQiEC391yOiRYoMcUh4hd2Q9bMx3jngZgX8PXIvZZcup4/pvWlv1alwhB2tsnLdazP62r1MO80vLyLunzGO+7WwCjEYlVaECgYEA+lQRFmbhKaPuAuXMtY31Fbga8nedka5TjnEV7+/kX+yowE2OlNujF+ZG8UTddTxAGv56yVNi/mjRlgD74j8z0eOsgvOq9mwbCrgLhLo51H9O/wAxtb+hBKtC5l50pBr4gER6d8W6EQNTSGojnMIaLXTkAZ5Qf6Z8e2HFVdOn0X0CgYEA7SSrTokwzukt5KldNu5ukyyd+C3D1i6orbg6qD73EP9CfNMfGSBn7dDv9wMSJH01+Ty+RgTROgtjGRDbMJWnfbdt/61NePr9ar5sb6Nbsf7/I0w7cZF5dsaFYgzaOfQYquzXPbLQHkpMT64bqpv/Mwy4F2lFvaYWY5fA4pC2uckCgYEAg75Ym9ybJaoTqky8ttQ2Jy8UZ4VSVQhVC0My02sCWwWXLlXi8y7An+Rec73Ve0yxREOn5WrQT6pkmzh7V/ABWrYi5WxODpCIjtSbo0fLBa3Wqle00b0/hdCITetqIa/cFs1zUrOqICgK3bKWeXqiAkhhcwSZwwSgwOKM04Wn7ZUCgYBvhHX2mbdVJfyJ8kc+hMOE/E9RHRxiBVEXWHJlGi8PVCqNDq8qHr4g7Mdbzprig+s0yKblwHAvrpkseWvKHiZEjVTyDipHgShY4TGXEigVvUd37uppTrLi8xpYcJjS9gH/px7VCdiq1d+q/MJP6coJ1KphgATm2UrgDMYNBWaYWQKBgEHRxrmER7btUF60/YgcqPHFc8RpYQB2ZZE0kyKGDqk2Data1XYUY6vsPAU28yRLAaWr/D2H17iyLkxP80VLm6QhifxCadv90Q/Wl1DFfOJQMW6avyQ0so6G0wFq/LJxaFK4iLXQn1RJnmTp6BYiJMmK2BhFbRzw8ssMoF6ad2rr'
    # 请求URL
    url = 'http://localhost:8081'
    # 创建请求客户端
    client = OpenClient(app_id, private_key, url)

    def test_api(self):
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

        # 关闭文件
        # for f in files.values():
        #     f.close()

        if response.is_success():
            print 'response: ', response
            print 'is_vip:', response.get('member_info').get('is_vip', 0)
        else:
            print '请求失败,code:%s, msg:%s, sub_code:%s, sub_msg:%s' % \
                  (response.code, response.msg, response.sub_code, response.sub_msg)

    def test_to_json_string(self):
        model = MemberInfoGetModel()
        model.age = 1
        model.name = '张三'
        model.address = 'xx'
        json_string = JsonUtil.to_json_string(model)
        print 'json:', json_string


if __name__ == '__main__':
    unittest.main()
