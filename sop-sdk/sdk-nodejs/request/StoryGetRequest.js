const {Class} = require("../common/Class");
const {RequestType} = require("../common/RequestType");
const {BaseRequest} = require('./BaseRequest')

/**
 * 创建一个请求类，继承BaseRequest，重写三个函数
 */
const StoryGetRequest = Class.create({

    getMethod: function () {
        return "story.get"
    },
    getVersion: function () {
        return "1.0"
    },
    getRequestType: function () {
        return RequestType.GET
    }

}, BaseRequest) // 继承BaseRequest

module.exports.StoryGetRequest = StoryGetRequest
