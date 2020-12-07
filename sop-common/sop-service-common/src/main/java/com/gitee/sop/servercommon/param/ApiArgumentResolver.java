package com.gitee.sop.servercommon.param;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;
import com.gitee.sop.servercommon.annotation.Open;
import com.gitee.sop.servercommon.bean.OpenContext;
import com.gitee.sop.servercommon.bean.OpenContextImpl;
import com.gitee.sop.servercommon.bean.ParamNames;
import com.gitee.sop.servercommon.bean.ServiceContext;
import com.gitee.sop.servercommon.util.FieldUtil;
import com.gitee.sop.servercommon.util.OpenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestMethodArgumentResolver;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Parameter;
import java.security.Principal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 解析request参数中的业务参数，绑定到方法参数上
 *
 * @author tanghc
 */
@Slf4j
public class ApiArgumentResolver implements SopHandlerMethodArgumentResolver {

    private final Map<MethodParameter, HandlerMethodArgumentResolver> argumentResolverCache = new ConcurrentHashMap<>(256);

    private ParamValidator paramValidator = new ServiceParamValidator();

    private static Class<?> pushBuilder;

    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    static {
        try {
            pushBuilder = ClassUtils.forName("javax.servlet.http.PushBuilder",
                    ServletRequestMethodArgumentResolver.class.getClassLoader());
        } catch (ClassNotFoundException ex) {
            // Servlet 4.0 PushBuilder not found - not supported for injection
            pushBuilder = null;
        }
    }

    @Override
    public void setRequestMappingHandlerAdapter(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
        List<HandlerMethodArgumentResolver> argumentResolversNew = new ArrayList<>(64);
        // 先加自己，确保在第一个位置
        argumentResolversNew.add(this);
        argumentResolversNew.addAll(requestMappingHandlerAdapter.getArgumentResolvers());
        requestMappingHandlerAdapter.setArgumentResolvers(argumentResolversNew);
        this.requestMappingHandlerAdapter = requestMappingHandlerAdapter;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Open open = methodParameter.getMethodAnnotation(Open.class);
        if (open == null) {
            return false;
        }
        Class<?> paramType = methodParameter.getParameterType();
        // 排除的
        boolean exclude = (
                WebRequest.class.isAssignableFrom(paramType) ||
                        ServletRequest.class.isAssignableFrom(paramType) ||
                        MultipartRequest.class.isAssignableFrom(paramType) ||
                        HttpSession.class.isAssignableFrom(paramType) ||
                        (pushBuilder != null && pushBuilder.isAssignableFrom(paramType)) ||
                        Principal.class.isAssignableFrom(paramType) ||
                        InputStream.class.isAssignableFrom(paramType) ||
                        Reader.class.isAssignableFrom(paramType) ||
                        HttpMethod.class == paramType ||
                        Locale.class == paramType ||
                        TimeZone.class == paramType ||
                        ZoneId.class == paramType ||
                        ServletResponse.class.isAssignableFrom(paramType) ||
                        OutputStream.class.isAssignableFrom(paramType) ||
                        Writer.class.isAssignableFrom(paramType)
        );
        // 除此之外都匹配
        boolean support = !exclude;
        if (support) {
            this.wrapSingleParam(methodParameter, open);
        }
        return support;
    }

    /**
     * 包装单个值参数
     * @param methodParameter 参数信息
     * @param open open注解
     */
    private void wrapSingleParam(MethodParameter methodParameter, Open open) {
        Parameter parameter = methodParameter.getParameter();
        boolean isNumberStringEnumType = FieldUtil.isNumberStringEnumType(parameter.getType());
        if (isNumberStringEnumType) {
            log.debug("包装参数，方法：{}，参数名：{}", methodParameter.getMethod(), parameter.getName());
            SingleParameterContext.add(parameter, open);
        }
    }

    @Override
    public Object resolveArgument(
            MethodParameter methodParameter
            , ModelAndViewContainer modelAndViewContainer
            , NativeWebRequest nativeWebRequest
            , WebDataBinderFactory webDataBinderFactory
    ) throws Exception {
        nativeWebRequest = new SopServletWebRequest(
                (HttpServletRequest) nativeWebRequest.getNativeRequest(),
                (HttpServletResponse) nativeWebRequest.getNativeResponse()
        );
        Object paramObj = this.getParamObject(methodParameter, nativeWebRequest);
        if (paramObj != null) {
            // JSR-303验证

            if (paramObj instanceof SingleParameterWrapper) {
                SingleParameterWrapper parameterWrapper = (SingleParameterWrapper) paramObj;
                paramValidator.validateBizParam(parameterWrapper.getWrapperObject());
                return parameterWrapper.getParamValue();
            } else {
                paramValidator.validateBizParam(paramObj);
                return paramObj;
            }
        }
        HandlerMethodArgumentResolver resolver = getOtherArgumentResolver(methodParameter);
        if (resolver != null) {
            Object param = resolver.resolveArgument(
                    methodParameter
                    , modelAndViewContainer
                    , nativeWebRequest
                    , webDataBinderFactory
            );
            OpenContext openContext = ServiceContext.getCurrentContext().getOpenContext();
            if (openContext instanceof OpenContextImpl) {
                OpenContextImpl openContextImpl = (OpenContextImpl) openContext;
                openContextImpl.setBizObject(param);
            }
            return param;
        }
        return null;
    }


    /**
     * 获取参数对象，将request中的参数绑定到实体对象中去
     *
     * @param methodParameter  方法参数
     * @param nativeWebRequest request
     * @return 返回参数绑定的对象，没有返回null
     */
    protected Object getParamObject(MethodParameter methodParameter, NativeWebRequest nativeWebRequest) {
        HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();
        ServiceContext currentContext = ServiceContext.getCurrentContext();
        JSONObject requestParams = OpenUtil.getRequestParams(request);
        OpenContextImpl openContext = new OpenContextImpl(requestParams);
        currentContext.setOpenContext(openContext);
        String bizContent = requestParams.getString(ParamNames.BIZ_CONTENT_NAME);
        if (bizContent == null) {
            return null;
        }
        // 方法参数类型
        Class<?> parameterType = methodParameter.getParameterType();
        SingleParameterContextValue singleValue = SingleParameterContext.get(openContext.getMethod(), openContext.getVersion());
        // 如果是单值参数
        if (singleValue != null) {
            JSONObject jsonObj = JSON.parseObject(bizContent);
            Object paramValue = jsonObj.getObject(singleValue.getParameterName(), parameterType);
            Object wrapperObject = jsonObj.toJavaObject(singleValue.getWrapClass());
            SingleParameterWrapper singleParameterWrapper = new SingleParameterWrapper();
            singleParameterWrapper.setParamValue(paramValue);
            singleParameterWrapper.setWrapperObject(wrapperObject);
            return singleParameterWrapper;
        }
        Object param;
        // 如果是json字符串
        if (JSONValidator.from(bizContent).validate()) {
            param = JSON.parseObject(bizContent, parameterType);
        } else {
            // 否则认为是 aa=1&bb=33 形式
            Map<String, Object> query = OpenUtil.parseQueryToMap(bizContent);
            param = new JSONObject(query).toJavaObject(parameterType);
        }
        openContext.setBizObject(param);
        this.bindUploadFile(param, request);
        return param;
    }


    /**
     * 将上传文件对象绑定到属性中
     *
     * @param param              业务参数
     * @param httpServletRequest
     */
    protected void bindUploadFile(Object param, HttpServletRequest httpServletRequest) {
        if (httpServletRequest instanceof MyServletRequestWrapper) {
            httpServletRequest = (HttpServletRequest)((MyServletRequestWrapper) httpServletRequest).getRequest();
        }
        if (param == null) {
            return;
        }
        if (OpenUtil.isMultipart(httpServletRequest)) {
            MultipartHttpServletRequest request = (MultipartHttpServletRequest) httpServletRequest;
            Class<?> bizClass = param.getClass();
            ReflectionUtils.doWithFields(bizClass, field -> {
                ReflectionUtils.makeAccessible(field);
                String name = field.getName();
                MultipartFile multipartFile = request.getFile(name);
                ReflectionUtils.setField(field, param, multipartFile);
            }, field -> field.getType() == MultipartFile.class);
        }
    }

    /**
     * 获取其它的参数解析器
     *
     * @param parameter 业务参数
     * @return 返回合适参数解析器，没有返回null
     */
    protected HandlerMethodArgumentResolver getOtherArgumentResolver(MethodParameter parameter) {
        HandlerMethodArgumentResolver result = this.argumentResolverCache.get(parameter);
        if (result == null) {
            List<HandlerMethodArgumentResolver> argumentResolvers = this.requestMappingHandlerAdapter.getArgumentResolvers();
            for (HandlerMethodArgumentResolver methodArgumentResolver : argumentResolvers) {
                if (methodArgumentResolver instanceof SopHandlerMethodArgumentResolver) {
                    continue;
                }
                if (methodArgumentResolver.supportsParameter(parameter)) {
                    result = methodArgumentResolver;
                    this.argumentResolverCache.put(parameter, result);
                    break;
                }
            }
        }
        return result;
    }

    public void setParamValidator(ParamValidator paramValidator) {
        this.paramValidator = paramValidator;
    }

    private static final class SopServletWebRequest extends ServletWebRequest {
        public SopServletWebRequest(HttpServletRequest request, HttpServletResponse response) {
            super(new MyServletRequestWrapper(request), response);
        }
    }
}