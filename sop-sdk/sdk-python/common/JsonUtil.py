#!/usr/bin/python
# -*- coding: UTF-8 -*-

import json


def to_json_string(obj):
    """将对象转换成json字符串

    :param obj: 对象
    :type obj: object

    :return: 返回json
    :rtype: str
    """
    if isinstance(obj, dict):
        param = obj
    else:
        param = obj.__dict__
    return json.dumps(param, ensure_ascii=False)

