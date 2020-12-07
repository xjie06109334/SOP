#!/usr/bin/python
# -*- coding: UTF-8 -*-
from common import RequestTypes
from request.BaseRequest import BaseRequest


class MemberInfoGetRequest(BaseRequest):
    """获取会员信息请求"""

    def __init__(self):
        BaseRequest.__init__(self)

    def get_method(self):
        return 'member.info.get'

    def get_version(self):
        return '1.0'

    def get_request_type(self):
        return RequestTypes.GET
