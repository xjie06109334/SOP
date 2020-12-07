package com.gitee.sop.servercommon.swagger;

import com.alibaba.fastjson.JSON;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.members.RawField;
import com.gitee.sop.servercommon.annotation.BizCode;
import com.gitee.sop.servercommon.annotation.Open;
import com.gitee.sop.servercommon.bean.ServiceConfig;
import com.google.common.base.Optional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import springfox.documentation.service.Operation;
import springfox.documentation.service.StringVendorExtension;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author tanghc
 */
public class DocumentationPluginsManagerExt extends DocumentationPluginsManager {

    private static final String SOP_NAME = "sop_name";
    private static final String SOP_VERSION = "sop_version";
    private static final String MODULE_ORDER = "module_order";
    private static final String API_ORDER = "api_order";
    private static final String BIZ_CODE = "biz_code";

    @Override
    public Operation operation(OperationContext operationContext) {
        Operation operation = super.operation(operationContext);
        this.setVendorExtension(operation, operationContext);
        return operation;
    }

    private void setVendorExtension(Operation operation, OperationContext operationContext) {
        List<VendorExtension> vendorExtensions = operation.getVendorExtensions();
        Optional<Open> mappingOptional = operationContext.findAnnotation(Open.class);
        if (mappingOptional.isPresent()) {
            Open open = mappingOptional.get();
            String name = open.value();
            String version = buildVersion(open.version());
            vendorExtensions.add(new StringVendorExtension(SOP_NAME, name));
            vendorExtensions.add(new StringVendorExtension(SOP_VERSION, version));
            this.setBizCode(open, vendorExtensions);
            this.setResultExtProperties(operationContext);
        }
        Optional<Api> apiOptional = operationContext.findControllerAnnotation(Api.class);
        int order = 0;
        if (apiOptional.isPresent()) {
            order = apiOptional.get().position();
        } else {
            Optional<Order> orderOptional = operationContext.findControllerAnnotation(Order.class);
            if (orderOptional.isPresent()) {
                order = orderOptional.get().value();
            }
        }
        vendorExtensions.add(new StringVendorExtension(MODULE_ORDER, String.valueOf(order)));
        Optional<ApiOperation> apiOperationOptional = operationContext.findAnnotation(ApiOperation.class);
        int methodOrder = 0;
        if (apiOperationOptional.isPresent()) {
            methodOrder = apiOperationOptional.get().position();
        }
        vendorExtensions.add(new StringVendorExtension(API_ORDER, String.valueOf(methodOrder)));
    }

    /**
     * 设置返回结果额外属性，如最大长度
     * @param operationContext
     */
    private void setResultExtProperties(OperationContext operationContext) {
        List<VendorExtension> vendorExtensions = operationContext.getDocumentationContext().getVendorExtentions();
        ResolvedType returnType = operationContext.getReturnType();
        Class<?> erasedType = returnType.getErasedType();
        String className = erasedType.getSimpleName();
        boolean exist = vendorExtensions.stream().anyMatch(p -> Objects.equals(p.getName(), className));
        if (!exist) {
            List<RawField> memberFields = returnType.getMemberFields();
            Map<String, Map<String, Object>> fieldProperties = new HashMap<>(16);
            for (RawField memberField : memberFields) {
                String key = memberField.getName();
                Length length = AnnotationUtils.findAnnotation(memberField.getRawMember(), Length.class);
                if (length != null) {
                    Map<String, Object> properties = fieldProperties.computeIfAbsent(key, k -> new HashMap<>(16));
                    properties.computeIfAbsent("maxLength", k -> length.max());
                    properties.computeIfAbsent("minLength", k -> length.max());
                }
                ApiModelProperty apiModelProperty = AnnotationUtils.findAnnotation(memberField.getRawMember(), ApiModelProperty.class);
                if (apiModelProperty != null) {
                    Map<String, Object> properties = fieldProperties.computeIfAbsent(key, k -> new HashMap<>(16));
                    boolean required = apiModelProperty.required();
                    // 只有在必填的情况下设置
                    if (required) {
                        properties.put("required", required);
                    }
                }
            }
            vendorExtensions.add(new StringVendorExtension(className, JSON.toJSONString(fieldProperties)));
        }
    }

    private Class<?> getGenericType(Field curField) {
        // 当前集合的泛型类型
        Type genericType = curField.getGenericType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) genericType;
            // 得到泛型里的class类型对象
            return (Class<?>) pt.getActualTypeArguments()[0];
        }
        return null;
    }

    /**
     * 设置业务错误码
     * @param open
     * @param vendorExtensions
     */
    private void setBizCode(Open open, List<VendorExtension> vendorExtensions) {
        BizCode[] bizCodes = open.bizCode();
        List<BizCodeObj> codeObjList = Stream.of(bizCodes)
                .map(bizCode -> new BizCodeObj(bizCode.code(), bizCode.msg(), bizCode.solution()))
                .collect(Collectors.toList());
        vendorExtensions.add(new StringVendorExtension(BIZ_CODE, JSON.toJSONString(codeObjList)));
    }

    @Data
    @AllArgsConstructor
    private static class BizCodeObj {
        private String code;
        private String msg;
        private String solution;
    }

    private String buildVersion(String version) {
        if ("".equals(version)) {
            return ServiceConfig.getInstance().getDefaultVersion();
        } else {
            return version;
        }
    }
}
