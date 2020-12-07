#!/usr/bin/python
# -*- coding: UTF-8 -*-
from UserDict import UserDict


class BaseResponse(UserDict):
    """返回类"""
    request_id = None
    code = None
    msg = None
    sub_code = None
    sub_msg = None

    def __init__(self, _dict=None, **kwargs):
        UserDict.__init__(self, _dict, **kwargs)

    def is_success(self):
        """是否成功

        :return: True,成功
        :rtype: bool
        """
        return self.sub_code is None
