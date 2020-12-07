package com.gitee.sop.servercommon.param;

public class SingleParameterContextValue {
    /** 参数名称 */
    private String parameterName;
    private Class<?> wrapClass;

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public Class<?> getWrapClass() {
        return wrapClass;
    }

    public void setWrapClass(Class<?> wrapClass) {
        this.wrapClass = wrapClass;
    }

}