#!/usr/bin/python
# -*- coding: UTF-8 -*-

import json
import time

import requests

from common import SignUtil, RequestTypes
from common.RequestType import RequestType

_headers = {'Accept-Encoding': 'identity'}


class OpenClient:
    """调用客户端"""
    __app_id = ''
    __private_key = ''
    __url = ''

    def __init__(self, app_id, private_key, url):
        """客户端

        :param app_id: 应用ID
        :type app_id: str

        :param private_key: 应用私钥
        :type private_key: str

        :param url: 请求URL
        :type url: str
        """
        self.__app_id = app_id
        self.__private_key = private_key
        self.__url = url

    def execute(self, request, token=None):
        """

        :param request: 请求对象，BaseRequest的子类

        :param token: (Optional) token
        :type token: str

        :return: 返回请求结果
        :rtype: BaseResponse
        """
        biz_model = request.biz_model
        request_type = request.get_request_type()
        if not isinstance(request_type, RequestType):
            raise Exception('get_request_type返回错误类型，正确方式：RequestTypes.XX')

        params = biz_model.__dict__
        if request.files is not None:
            response = self._post_file(request, params, token)
        elif request_type == RequestTypes.GET:
            response = self._get(request, params, token)
        elif request_type == RequestTypes.POST_FORM:
            response = self._post_form(request, params, token)
        elif request_type == RequestTypes.POST_JSON:
            response = self._post_json(request, params, token)
        elif request_type == RequestTypes.POST_UPLOAD:
            response = self._post_file(request, params, token)
        else:
            raise Exception('get_request_type设置错误')

        return self._parse_response(response, request)

    def _get(self, request, params, token):
        all_params = self._build_params(request, params, token)
        return requests.get(self.__url, all_params, headers=_headers).text

    def _post_form(self, request, params, token):
        all_params = self._build_params(request, params, token)
        return requests.post(self.__url, data=all_params, headers=_headers).text

    def _post_json(self, request, params, token):
        all_params = self._build_params(request, params, token)
        return requests.post(self.__url, json=all_params, headers=_headers).text

    def _post_file(self, request, params, token):
        all_params = self._build_params(request, params, token)
        return requests.request('POST', self.__url, data=all_params, files=request.files, headers=_headers).text

    def _build_params(self, request, params, token):
        """构建所有的请求参数

        :param request: 请求对象
        :type request: request.BaseRequest

        :param params: 业务请求参数
        :type params: dict

        :param token: token
        :type token: str

        :return: 返回请求参数
        :rtype: str
        """
        all_params = {
            'app_id': self.__app_id,
            'method': request.get_method(),
            'charset': 'UTF-8',
            'sign_type': 'RSA2',
            'timestamp': time.strftime("%Y-%m-%d %H:%M:%S", time.localtime()),
            'version': request.get_version()
        }

        if token is not None:
            all_params['access_token'] = token

        # 添加业务参数
        all_params.update(params)

        # 构建sign
        sign = SignUtil.create_sign(all_params, self.__private_key, 'RSA2')
        all_params['sign'] = sign
        return all_params

    def _parse_response(self, resp, request):
        response_dict = json.loads(resp)
        return request.parse_response(response_dict)
