package com.gitee.sop.storyweb.controller.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class TestResult {

	@ApiModelProperty(value = "标签", example = "学习")
    private String label;

	@ApiModelProperty(value = "类型", example = "1 超管 2 普管")
    private String type;
	
	@ApiModelProperty(value = "集合", example = "集合")
	List<TestResult> ss;
	
}