/*
 Navicat Premium Dump SQL

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80026 (8.0.26)
 Source Host           : localhost:3306
 Source Schema         : letao_config

 Target Server Type    : MySQL
 Target Server Version : 80026 (8.0.26)
 File Encoding         : 65001

 Date: 12/10/2024 12:26:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'group_id',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'configuration description',
  `c_use` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'configuration usage',
  `effect` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '配置生效的描述',
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '配置的类型',
  `c_schema` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '配置的模式',
  `encrypted_data_key` varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '密钥',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfo_datagrouptenant`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info` VALUES (1, 'letaotao-trade-app-dev.properties', 'dev', '#设置端口号\nserver.port=9998\n#数据库驱动\nspring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver\n#数据库连接地址\nspring.datasource.url=jdbc:mysql://localhost:3306/letao_trade?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai\n#数据库用户名\nspring.datasource.username=root\n#数据库密码\nspring.datasource.password=123456\n#加载映射文件\nmybatis-plus.mapper-locations=classpath*:/mapper/*.xml\n#设置别名\nmybatis-plus.type-aliases-package=com.gxcy.letaotao.web.app.entity\n#关闭驼峰命名映射\n#mybatis-plus.configuration.map-underscore-to-camel-case=false\n#显示日志\nlogging.level.com.gxcy.mapper=debug\n#JSON时区设置\nspring.jackson.time-zone=GMT+8\n#jwt配置\n#密钥\njwt.secret=com.gxcycom.gxcycom.gxcycom.gxcy\n#过期时间\njwt.expiration=1800000\nspring.mvc.pathmatch.matching-strategy=ant_path_matcher\n################################# Redis相关配置 #################################\nspring.data.redis.host=localhost\nspring.data.redis.port=6379\nspring.data.redis.database=0\nspring.data.redis.timeout=10000\n#自定义属性\nspring.redis.expire=1\n#登录请求地址(自定义)\nrequest.wxlogin.url=/api/wx_user/login\n#全局逻辑删除的实体字段名\nmybatis-plus.global-config.db-config.logic-delete-field=isDelete\n#逻辑删除值，默认为1\nmybatis-plus.global-config.db-config.logic-delete-value=1\n#逻辑未删除值，默认为0\nmybatis-plus.global-config.db-config.logic-not-delete-value=0\n#sql只会打印到控制台不会输出到日志文件种\nmybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl\n#阿里云OSS配置\naliyun.oss.file.endpoint=阿里云OSSEndpoint\naliyun.oss.file.keyid=阿里云AccessKeyId\naliyun.oss.file.keysecret=阿里云AccessKeySecret\n#bucket名称\naliyun.oss.file.bucketname=阿里云OSSBucketName\n#小程序appid和secret\nwechat.appid=微信appid\nwechat.secret=微信secret\nwechat.default-nickname=微信用户\nwechat.default-avatar-url=https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132', '1be7b46e42901d755f8449a65fa72781', '2024-10-12 10:33:49', '2024-10-12 12:25:55', NULL, '127.0.0.1', '', '', 'letaotao-trade-app-dev.properties', '', '', 'properties', '', '');
INSERT INTO `config_info` VALUES (2, 'letaotao-trade-admin-dev.properties', 'dev', '#设置端口号\nserver.port=9999\n#数据库驱动\nspring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver\n#数据库连接地址\nspring.datasource.url=jdbc:mysql://localhost:3306/letao_trade?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai\n#数据库用户名\nspring.datasource.username=root\n#数据库密码\nspring.datasource.password=123456\n#加载映射文件\nmybatis-plus.mapper-locations=classpath*:/mapper/*.xml\n#设置别名\nmybatis-plus.type-aliases-package=com.gxcy.letaotao.web.admin.entity\n#关闭驼峰命名映射\n#mybatis-plus.configuration.map-underscore-to-camel-case=false\n#显示日志\nlogging.level.com.gxcy.mapper=debug\n#JSON时区设置\nspring.jackson.time-zone=GMT+8\n#jwt配置\n#密钥\njwt.secret=com.gxcycom.gxcycom.gxcycom.gxcy\n#过期时间\njwt.expiration=1800000\nspring.mvc.pathmatch.matching-strategy=ant_path_matcher\n################################# Redis相关配置 #################################\nspring.data.redis.host=localhost\nspring.data.redis.port=6379\nspring.data.redis.database=0\nspring.data.redis.timeout=10000\n#自定义属性\nspring.redis.expire=1\n#登录请求地址(自定义)\nrequest.login.url=/api/sysUser/login\n#全局逻辑删除的实体字段名\nmybatis-plus.global-config.db-config.logic-delete-field=isDelete\n#逻辑删除值，默认为1\nmybatis-plus.global-config.db-config.logic-delete-value=1\n#逻辑未删除值，默认为0\nmybatis-plus.global-config.db-config.logic-not-delete-value=0\n#sql只会打印到控制台不会输出到日志文件种\nmybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl\n#阿里云OSS配置\naliyun.oss.file.endpoint=阿里云OSSEndpoint\naliyun.oss.file.keyid=阿里云AccessKeyId\naliyun.oss.file.keysecret=阿里云AccessKeySecret\n#bucket名称\naliyun.oss.file.bucketname=阿里云OSSBucketName', '0b6811d742344218190040e78331e73e', '2024-10-12 10:56:58', '2024-10-12 12:23:06', NULL, '127.0.0.1', '', '', 'letaotao-trade-admin-dev.properties', '', '', 'properties', '', '');
INSERT INTO `config_info` VALUES (3, 'letaotao-trade-admin-prod.properties', 'prod', '#设置端口号\nserver.port=9999\n#数据库驱动\nspring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver\n#数据库连接地址\nspring.datasource.url=jdbc:mysql://localhost:3306/letao_trade?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai\n#数据库用户名\nspring.datasource.username=root\n#数据库密码\nspring.datasource.password=123456\n#加载映射文件\nmybatis-plus.mapper-locations=classpath*:/mapper/*.xml\n#设置别名\nmybatis-plus.type-aliases-package=com.gxcy.letaotao.web.admin.entity\n#关闭驼峰命名映射\n#mybatis-plus.configuration.map-underscore-to-camel-case=false\n#显示日志\nlogging.level.com.gxcy.mapper=debug\n#JSON时区设置\nspring.jackson.time-zone=GMT+8\n#jwt配置\n#密钥\njwt.secret=com.gxcycom.gxcycom.gxcycom.gxcy\n#过期时间\njwt.expiration=1800000\nspring.mvc.pathmatch.matching-strategy=ant_path_matcher\n################################# Redis相关配置 #################################\nspring.data.redis.host=localhost\nspring.data.redis.port=6379\nspring.data.redis.database=0\nspring.data.redis.timeout=10000\n#自定义属性\nspring.redis.expire=1\n#登录请求地址(自定义)\nrequest.login.url=/api/sysUser/login\n#全局逻辑删除的实体字段名\nmybatis-plus.global-config.db-config.logic-delete-field=isDelete\n#逻辑删除值，默认为1\nmybatis-plus.global-config.db-config.logic-delete-value=1\n#逻辑未删除值，默认为0\nmybatis-plus.global-config.db-config.logic-not-delete-value=0\n#sql只会打印到控制台不会输出到日志文件种\nmybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl\n#阿里云OSS配置\naliyun.oss.file.endpoint=阿里云OSSEndpoint\naliyun.oss.file.keyid=阿里云AccessKeyId\naliyun.oss.file.keysecret=阿里云AccessKeySecret\n#bucket名称\naliyun.oss.file.bucketname=阿里云OSSBucketName', '0b6811d742344218190040e78331e73e', '2024-10-12 11:01:28', '2024-10-12 12:24:14', NULL, '127.0.0.1', '', '', 'letaotao-trade-admin-prod.properties', '', '', 'properties', '', '');
INSERT INTO `config_info` VALUES (4, 'letaotao-trade-app-prod.properties', 'prod', '#设置端口号\nserver.port=9998\n#数据库驱动\nspring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver\n#数据库连接地址\nspring.datasource.url=jdbc:mysql://localhost:3306/letao_trade?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai\n#数据库用户名\nspring.datasource.username=root\n#数据库密码\nspring.datasource.password=123456\n#加载映射文件\nmybatis-plus.mapper-locations=classpath*:/mapper/*.xml\n#设置别名\nmybatis-plus.type-aliases-package=com.gxcy.letaotao.web.app.entity\n#关闭驼峰命名映射\n#mybatis-plus.configuration.map-underscore-to-camel-case=false\n#显示日志\nlogging.level.com.gxcy.mapper=debug\n#JSON时区设置\nspring.jackson.time-zone=GMT+8\n#jwt配置\n#密钥\njwt.secret=com.gxcycom.gxcycom.gxcycom.gxcy\n#过期时间\njwt.expiration=1800000\nspring.mvc.pathmatch.matching-strategy=ant_path_matcher\n################################# Redis相关配置 #################################\nspring.data.redis.host=localhost\nspring.data.redis.port=6379\nspring.data.redis.database=0\nspring.data.redis.timeout=10000\n#自定义属性\nspring.redis.expire=1\n#登录请求地址(自定义)\nrequest.wxlogin.url=/api/wx_user/login\n#全局逻辑删除的实体字段名\nmybatis-plus.global-config.db-config.logic-delete-field=isDelete\n#逻辑删除值，默认为1\nmybatis-plus.global-config.db-config.logic-delete-value=1\n#逻辑未删除值，默认为0\nmybatis-plus.global-config.db-config.logic-not-delete-value=0\n#sql只会打印到控制台不会输出到日志文件种\nmybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl\n#阿里云OSS配置\naliyun.oss.file.endpoint=阿里云OSSEndpoint\naliyun.oss.file.keyid=阿里云AccessKeyId\naliyun.oss.file.keysecret=阿里云AccessKeySecret\n#bucket名称\naliyun.oss.file.bucketname=阿里云OSSBucketName\n#小程序appid和secret\nwechat.appid=微信appid\nwechat.secret=微信secret\nwechat.default-nickname=微信用户\nwechat.default-avatar-url=https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132', '1be7b46e42901d755f8449a65fa72781', '2024-10-12 11:02:53', '2024-10-12 12:25:32', NULL, '127.0.0.1', '', '', 'letaotao-trade-app-prod.properties', '', '', 'properties', '', '');

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'datum_id',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfoaggr_datagrouptenantdatum`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC, `datum_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '增加租户字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_aggr
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '密钥',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfobeta_datagrouptenant`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_beta' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfotag_datagrouptenanttag`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC, `tag_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_tag' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_tag
-- ----------------------------

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation`  (
  `id` bigint NOT NULL COMMENT 'id',
  `tag_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint NOT NULL AUTO_INCREMENT COMMENT 'nid, 自增长标识',
  PRIMARY KEY (`nid`) USING BTREE,
  UNIQUE INDEX `uk_configtagrelation_configidtag`(`id` ASC, `tag_name` ASC, `tag_type` ASC) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_tag_relation' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_group_id`(`group_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '集群、各Group容量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of group_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info`  (
  `id` bigint UNSIGNED NOT NULL COMMENT 'id',
  `nid` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'nid, 自增标识',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  `op_type` char(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'operation type',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '密钥',
  PRIMARY KEY (`nid`) USING BTREE,
  INDEX `idx_gmt_create`(`gmt_create` ASC) USING BTREE,
  INDEX `idx_gmt_modified`(`gmt_modified` ASC) USING BTREE,
  INDEX `idx_did`(`data_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '多租户改造' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`  (
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'role',
  `resource` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'resource',
  `action` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'action',
  UNIQUE INDEX `uk_role_permission`(`role` ASC, `resource` ASC, `action` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permissions
-- ----------------------------

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'username',
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'role',
  UNIQUE INDEX `idx_user_role`(`username` ASC, `role` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roles
-- ----------------------------

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数',
  `max_aggr_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '租户容量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_info_kptenantid`(`kp` ASC, `tenant_id` ASC) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'tenant_info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tenant_info
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'username',
  `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'password',
  `enabled` tinyint(1) NOT NULL COMMENT 'enabled',
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
