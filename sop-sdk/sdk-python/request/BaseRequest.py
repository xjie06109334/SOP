#!/usr/bin/python
# -*- coding: UTF-8 -*-
from response.BaseResponse import BaseResponse


class BaseRequest:
    """请求类的父类"""

    biz_model = None
    """请求参数"""

    files = None
    """上传文件"""

    def __init__(self):
        pass

    def get_method(self):
        """返回接口名

        :return: 返回接口名
        :rtype: str
        """
        raise Exception('未实现BaseRequest.get_method()方法')

    def get_version(self):
        """返回接口版本号

        :return: 返回版本号，如：1.0
        :rtype: str
        """
        raise Exception('未实现BaseRequest.get_version()方法')

    def get_request_type(self):
        """返回请求类型

        :return: 返回RequestType类实例
        :rtype:  common.RequestType
        """
        raise Exception('未实现BaseRequest.get_request_type()方法')

    def parse_response(self, response_dict):
        response_data = response_dict.get('error_response')
        if response_data is None:
            data_name = self.get_method().replace('.', '_') + '_response'
            response_data = response_dict.get(data_name)
        return response_data






