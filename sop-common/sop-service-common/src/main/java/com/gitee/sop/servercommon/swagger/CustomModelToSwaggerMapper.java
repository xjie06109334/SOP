package com.gitee.sop.servercommon.swagger;

import io.swagger.models.parameters.Parameter;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2MapperImpl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CustomModelToSwaggerMapper extends ServiceModelToSwagger2MapperImpl {

    @Override
    protected List<Parameter> parameterListToParameterList(List<springfox.documentation.service.Parameter> list) {
        // list需要根据order|postion排序
        list = list.stream()
                .sorted(Comparator.comparingInt(springfox.documentation.service.Parameter::getOrder))
                .collect(Collectors.toList());
        return super.parameterListToParameterList(list);
    }
}
