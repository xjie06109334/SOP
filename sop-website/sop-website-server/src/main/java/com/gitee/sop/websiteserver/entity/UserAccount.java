package com.gitee.sop.websiteserver.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


/**
 * 表名：user_account
 * 备注：用户信息
 *
 * @author tanghc
 */
@Table(name = "user_account")
@Data
public class UserAccount {
    /**  数据库字段：id */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 用户名（邮箱）, 数据库字段：username */
    private String username;

    /** 密码, 数据库字段：password */
    private String password;

    /** 2：邮箱未验证，1：启用，0：禁用, 数据库字段：status */
    private Byte status;

    /**  数据库字段：gmt_create */
    private Date gmtCreate;

    /**  数据库字段：gmt_modified */
    private Date gmtModified;
}
