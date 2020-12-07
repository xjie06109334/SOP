package com.gitee.sop.servercommon.param;

import com.gitee.sop.servercommon.annotation.Open;
import com.gitee.sop.servercommon.bean.ServiceConfig;

import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存放单个参数
 * @author tanghc
 */
public class SingleParameterContext {

    private static SingleParameterClassCreator singleFieldWrapper = new SingleParameterClassCreator();

    private static final Map<String, SingleParameterContextValue> context = new ConcurrentHashMap<>(16);

    /**
     * 添加单个参数
     * @param parameter 接口单个参数
     * @param open open注解
     */
    public static void add(Parameter parameter, Open open) {
        String version = open.version();
        version = "".equals(version) ? ServiceConfig.getInstance().getDefaultVersion() : version;
        String key = open.value() + version;
        String parameterName = parameter.getName();
        Class<?> wrapClass = singleFieldWrapper.create(parameter, parameterName);
        SingleParameterContextValue value = new SingleParameterContextValue();
        value.setParameterName(parameterName);
        value.setWrapClass(wrapClass);
        context.put(key, value);
    }

    public static SingleParameterContextValue get(String name, String version) {
        return context.get(name + version);
    }

    public static void setSingleFieldWrapper(SingleParameterClassCreator singleFieldWrapper) {
        SingleParameterContext.singleFieldWrapper = singleFieldWrapper;
    }
}
