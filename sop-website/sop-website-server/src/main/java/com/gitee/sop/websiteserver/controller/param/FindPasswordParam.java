package com.gitee.sop.websiteserver.controller.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author tanghc
 */
@Data
public class FindPasswordParam  {
    @NotBlank @Length(max = 300) String username;

}
