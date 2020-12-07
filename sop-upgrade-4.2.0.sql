use sop;

ALTER TABLE `sop`.`isv_info` ADD COLUMN `user_id` BIGINT NULL DEFAULT 0 COMMENT 'user_account.id' AFTER `remark`;

CREATE  INDEX `idx_userid` USING BTREE ON `sop`.`isv_info` (`user_id`);


CREATE TABLE `user_account` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(128) NOT NULL DEFAULT '' COMMENT '用户名（邮箱）',
  `password` varchar(128) NOT NULL DEFAULT '' COMMENT '密码',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '2：邮箱未验证，1：启用，0：禁用',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息';

CREATE TABLE `isp_resource` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT '资源名称',
  `content` varchar(128) NOT NULL DEFAULT '' COMMENT '资源内容（URL）',
  `ext_content` text,
  `version` varchar(32) NOT NULL DEFAULT '' COMMENT '版本',
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '资源类型：0：SDK链接',
  `is_deleted` tinyint(4) NOT NULL DEFAULT '0',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='ISP资源表';