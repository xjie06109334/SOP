const {Class} = require("../common/Class");

/**
 * 请求类父类
 */
exports.BaseRequest = Class.create({
    init: function(){
        this.bizModel = {}
        /*
        [
            {name: 'file1', path: 'd:/dd/1.txt'},
            {name: 'file2', path: 'd:/dd/2.txt'}
        ]
         */
        this.files = []
    },
    /**
     * 返回接口名称
     */
    getMethod: function() {
        throw `未实现BaseRequest类getMethod()方法`;
    },
    /**
     * 返回版本号
     */
    getVersion: function() {
        throw '未实现BaseRequest类getVersion()方法';
    },
    /**
     * 返回请求类型，使用RequestType.js
     */
    getRequestType: function() {
        throw '未实现BaseRequest类getRequestType()方法';
    },
    /**
     * 解析返回结果，子类可以覆盖实现
     * @param responseData 服务器返回内容
     * @returns 返回结果
     */
    parseResponse: function (responseData) {
        let data = responseData['error_response'];
        if (!data) {
            const dataNodeName = this.getMethod().replace(/\./g, '_') + '_response'
            data = responseData[dataNodeName]
        }
        return data;
    }
})
