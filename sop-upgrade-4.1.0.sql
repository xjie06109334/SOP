-- 4.1.0升级脚本

use sop;

CREATE TABLE `monitor_info` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `route_id` varchar(128) NOT NULL DEFAULT '' COMMENT '路由id',
  `name` varchar(128) NOT NULL DEFAULT '' COMMENT '接口名',
  `version` varchar(64) NOT NULL DEFAULT '' COMMENT '版本号',
  `service_id` varchar(64) NOT NULL DEFAULT '',
  `instance_id` varchar(128) NOT NULL DEFAULT '',
  `max_time` int(11) NOT NULL DEFAULT '0' COMMENT '请求耗时最长时间',
  `min_time` int(11) NOT NULL DEFAULT '0' COMMENT '请求耗时最小时间',
  `total_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '总时长，毫秒',
  `total_request_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '总调用次数',
  `success_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '成功次数',
  `error_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '失败次数（业务主动抛出的异常算作成功，如参数校验，未知的错误算失败）',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_routeid` (`route_id`,`instance_id`) USING BTREE,
  KEY `idex_name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='接口监控信息';



CREATE TABLE `monitor_info_error` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `error_id` varchar(64) NOT NULL DEFAULT '' COMMENT '错误id,md5Hex(instanceId + routeId + errorMsg)',
  `instance_id` varchar(128) NOT NULL DEFAULT '' COMMENT '实例id',
  `route_id` varchar(128) NOT NULL DEFAULT '',
  `error_msg` text NOT NULL,
  `error_status` int(11) NOT NULL DEFAULT '0' COMMENT 'http status，非200错误',
  `count` int(11) NOT NULL DEFAULT '0' COMMENT '错误次数',
  `is_deleted` tinyint(4) NOT NULL DEFAULT '0',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_errorid` (`error_id`) USING BTREE,
  KEY `idx_routeid` (`route_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;