package com.gitee.sop.servercommon.param;

/**
 * @author tanghc
 */
public class SingleParameterWrapper {
    /** 包裹类实例 */
    private Object wrapperObject;
    /** 参数值 */
    private Object paramValue;

    public Object getWrapperObject() {
        return wrapperObject;
    }

    public void setWrapperObject(Object wrapperObject) {
        this.wrapperObject = wrapperObject;
    }

    public Object getParamValue() {
        return paramValue;
    }

    public void setParamValue(Object paramValue) {
        this.paramValue = paramValue;
    }
}
