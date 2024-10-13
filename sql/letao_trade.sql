/*
 Navicat Premium Dump SQL

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80026 (8.0.26)
 Source Host           : localhost:3306
 Source Schema         : letao_trade

 Target Server Type    : MySQL
 Target Server Version : 80026 (8.0.26)
 File Encoding         : 65001

 Date: 24/09/2024 23:36:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
CREATE DATABASE IF NOT EXISTS letao_trade CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE letao_trade;

-- ----------------------------
-- Table structure for data_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `data_dictionary`;
CREATE TABLE `data_dictionary`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除(1:已删除，0:未删除)',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '上级ID',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `str_value` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字符串类型的值',
  `num_value` tinyint NULL DEFAULT NULL COMMENT '数值类型的值',
  `dict_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '编码',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_data_dictionary`(`id` ASC, `is_deleted` ASC, `name` ASC, `dict_code` ASC, `create_time` ASC, `update_time` ASC, `parent_id` ASC, `str_value` ASC, `num_value` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of data_dictionary
-- ----------------------------
INSERT INTO `data_dictionary` VALUES (1, '2024-09-15 16:57:18', '2024-09-15 16:57:49', 0, 0, '性别', NULL, NULL, 'gender');
INSERT INTO `data_dictionary` VALUES (2, '2024-09-15 16:58:16', '2024-09-15 16:58:22', 0, 0, '状态', NULL, NULL, 'status');
INSERT INTO `data_dictionary` VALUES (6, '2024-09-15 14:02:57', '2024-09-15 18:49:57', 0, 1, '男', NULL, 0, '');
INSERT INTO `data_dictionary` VALUES (7, '2024-09-15 14:02:57', '2024-09-15 18:49:59', 0, 1, '女', NULL, 1, '');
INSERT INTO `data_dictionary` VALUES (8, '2024-09-15 14:08:22', '2024-09-15 18:50:02', 0, 2, '启用', NULL, 1, '');
INSERT INTO `data_dictionary` VALUES (9, '2024-09-15 14:08:22', '2024-09-15 18:50:04', 0, 2, '停用', NULL, 0, '');
INSERT INTO `data_dictionary` VALUES (10, '2024-09-15 21:47:53', '2024-09-15 21:47:53', 0, 0, '商品状态', NULL, NULL, 'productStatus');
INSERT INTO `data_dictionary` VALUES (11, '2024-09-15 21:47:54', '2024-09-15 21:47:54', 0, 10, '草稿', NULL, 0, NULL);
INSERT INTO `data_dictionary` VALUES (12, '2024-09-15 21:47:56', '2024-09-15 21:47:56', 0, 10, '上架', NULL, 1, NULL);
INSERT INTO `data_dictionary` VALUES (13, '2024-09-15 21:47:57', '2024-09-15 21:47:57', 0, 10, '下架', NULL, 2, NULL);
INSERT INTO `data_dictionary` VALUES (14, '2024-09-15 21:48:00', '2024-09-15 21:48:00', 0, 10, '待审核', NULL, 3, NULL);
INSERT INTO `data_dictionary` VALUES (15, '2024-09-15 21:48:01', '2024-09-15 21:48:01', 0, 10, '已售出', NULL, 4, NULL);
INSERT INTO `data_dictionary` VALUES (16, '2024-09-15 21:53:21', '2024-09-15 21:53:21', 0, 0, '订单状态', NULL, NULL, 'orderStatus');
INSERT INTO `data_dictionary` VALUES (17, '2024-09-15 21:53:21', '2024-09-15 21:53:21', 0, 16, '待付款', NULL, 1, NULL);
INSERT INTO `data_dictionary` VALUES (18, '2024-09-15 21:53:21', '2024-09-15 21:53:21', 0, 16, '待发货', NULL, 2, NULL);
INSERT INTO `data_dictionary` VALUES (19, '2024-09-15 21:53:21', '2024-09-15 21:53:21', 0, 16, '待收货', NULL, 3, NULL);
INSERT INTO `data_dictionary` VALUES (20, '2024-09-15 21:53:21', '2024-09-15 21:53:21', 0, 16, '已完成', NULL, 4, NULL);
INSERT INTO `data_dictionary` VALUES (21, '2024-09-15 21:53:21', '2024-09-15 21:53:21', 0, 16, '已取消', NULL, 5, NULL);
INSERT INTO `data_dictionary` VALUES (22, '2024-09-15 21:56:16', '2024-09-15 21:56:16', 0, 0, '帖子状态', NULL, NULL, 'postStatus');
INSERT INTO `data_dictionary` VALUES (23, '2024-09-15 21:56:16', '2024-09-15 21:56:16', 0, 22, '草稿', NULL, 0, NULL);
INSERT INTO `data_dictionary` VALUES (24, '2024-09-15 21:56:16', '2024-09-15 21:56:16', 0, 22, '已发布', NULL, 1, NULL);
INSERT INTO `data_dictionary` VALUES (25, '2024-09-15 21:56:16', '2024-09-15 21:56:16', 0, 22, '待审核', NULL, 3, NULL);

-- ----------------------------
-- Table structure for lt_address
-- ----------------------------
DROP TABLE IF EXISTS `lt_address`;
CREATE TABLE `lt_address`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '地址唯一标识',
  `user_id` bigint NOT NULL COMMENT '用户编号',
  `contact_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '联系人姓名',
  `phone` char(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '联系电话',
  `region` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '地区',
  `detail_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '详细地址',
  `is_default` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_address`(`id` ASC, `user_id` ASC, `is_default` ASC, `contact_person` ASC, `phone` ASC, `region` ASC, `detail_address` ASC) USING BTREE,
  INDEX `lt_address_ibfk_1`(`user_id` ASC) USING BTREE,
  CONSTRAINT `lt_address_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `lt_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '收货地址表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lt_address
-- ----------------------------
INSERT INTO `lt_address` VALUES (1, 7, '张三', '13785695412', '广西壮族自治区-南宁市-西乡塘区-西乡塘街道', '朝阳路397号', 0);
INSERT INTO `lt_address` VALUES (2, 7, '张三', '13678456984', '广东省-广州市-海珠区', '新港中路397号', 1);
INSERT INTO `lt_address` VALUES (3, 7, '张三', '13846858754', '广东省-广州市-海珠区', '新港中路398号', 0);
INSERT INTO `lt_address` VALUES (4, 8, '夜半歌声', '13642587856', '广西壮族自治区-南宁市-西乡塘区-安宁街道', '安宁路111号', 1);
INSERT INTO `lt_address` VALUES (5, 8, '东冻', '13456892574', '广东省-广州市-荔湾区-岭南街道', '东冻街道111号', 0);

-- ----------------------------
-- Table structure for lt_category
-- ----------------------------
DROP TABLE IF EXISTS `lt_category`;
CREATE TABLE `lt_category`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '分类唯一标识',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分类名称',
  `parent_id` int NULL DEFAULT NULL COMMENT '父分类ID',
  `image_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类图片URL',
  `is_delete` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `parent_id`(`parent_id` ASC) USING BTREE,
  INDEX `idx_category`(`id` ASC, `name` ASC, `parent_id` ASC, `is_delete` ASC, `image_url` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lt_category
-- ----------------------------
INSERT INTO `lt_category` VALUES (1, '生活用品', 0, '', 0);
INSERT INTO `lt_category` VALUES (2, '服饰鞋帽', 0, '', 0);
INSERT INTO `lt_category` VALUES (3, '数码', 0, '', 0);
INSERT INTO `lt_category` VALUES (4, '运动户外', 0, '', 0);
INSERT INTO `lt_category` VALUES (5, '图书影像', 0, '', 0);
INSERT INTO `lt_category` VALUES (6, '手机', 0, '', 0);
INSERT INTO `lt_category` VALUES (7, '床上用品', 1, '', 0);
INSERT INTO `lt_category` VALUES (8, '日用', 1, '', 0);
INSERT INTO `lt_category` VALUES (9, '餐饮具', 1, '', 0);
INSERT INTO `lt_category` VALUES (10, '收纳整理', 1, '', 0);
INSERT INTO `lt_category` VALUES (11, '学习用品', 1, '', 0);
INSERT INTO `lt_category` VALUES (12, '男鞋', 2, '', 0);
INSERT INTO `lt_category` VALUES (13, '男装', 2, '', 0);
INSERT INTO `lt_category` VALUES (14, '服饰配饰', 2, '', 0);
INSERT INTO `lt_category` VALUES (15, '女鞋', 2, '', 0);
INSERT INTO `lt_category` VALUES (16, '女装', 2, '', 0);
INSERT INTO `lt_category` VALUES (17, '平板电脑', 3, '', 0);
INSERT INTO `lt_category` VALUES (18, '电脑整机', 3, '', 0);
INSERT INTO `lt_category` VALUES (19, '电脑外设', 3, '', 0);
INSERT INTO `lt_category` VALUES (20, '智能设备', 3, '', 0);
INSERT INTO `lt_category` VALUES (21, '影音娱乐', 3, '', 0);
INSERT INTO `lt_category` VALUES (22, '数码配件', 3, '', 0);
INSERT INTO `lt_category` VALUES (23, '电脑配件', 3, '', 0);
INSERT INTO `lt_category` VALUES (24, '运动穿搭', 4, '', 0);
INSERT INTO `lt_category` VALUES (25, '健身塑形', 4, '', 0);
INSERT INTO `lt_category` VALUES (26, '户外运动', 4, '', 0);
INSERT INTO `lt_category` VALUES (27, '图书教材', 5, '', 0);
INSERT INTO `lt_category` VALUES (28, '热门品牌', 6, '', 0);
INSERT INTO `lt_category` VALUES (29, '热门手机', 6, '', 0);
INSERT INTO `lt_category` VALUES (30, '笔记本电脑', 3, NULL, 0);
INSERT INTO `lt_category` VALUES (31, 'aweawweawe', 9, '', 1);
INSERT INTO `lt_category` VALUES (32, 'erwae', 20, '', 1);

-- ----------------------------
-- Table structure for lt_chat_relation
-- ----------------------------
DROP TABLE IF EXISTS `lt_chat_relation`;
CREATE TABLE `lt_chat_relation`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '关系唯一标识符',
  `buyer_id` bigint NOT NULL COMMENT '买家编号',
  `seller_id` bigint NOT NULL COMMENT '卖家编号',
  `product_id` int NOT NULL COMMENT '商品编号',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2102980611 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lt_chat_relation
-- ----------------------------
INSERT INTO `lt_chat_relation` VALUES (504991746, 8, 7, 2, '2024-04-08 20:34:06');
INSERT INTO `lt_chat_relation` VALUES (542769154, 8, 7, 9, '2024-09-24 18:47:05');
INSERT INTO `lt_chat_relation` VALUES (905981954, 7, 8, 6, '2024-09-12 16:32:11');
INSERT INTO `lt_chat_relation` VALUES (1423503362, 8, 7, 5, '2024-04-28 11:15:00');
INSERT INTO `lt_chat_relation` VALUES (1901649922, 7, 8, 7, '2024-05-11 18:44:08');
INSERT INTO `lt_chat_relation` VALUES (2102980610, 8, 7, 1, '2024-04-08 20:32:01');

-- ----------------------------
-- Table structure for lt_collection
-- ----------------------------
DROP TABLE IF EXISTS `lt_collection`;
CREATE TABLE `lt_collection`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '收藏唯一标识',
  `user_id` bigint NOT NULL COMMENT '收藏用户ID',
  `target_id` int NOT NULL COMMENT '收藏目标ID',
  `target_type` tinyint(1) NOT NULL COMMENT '收藏目标类型（1: 商品, 2: 帖子）',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  `is_active` tinyint(1) NOT NULL DEFAULT 1 COMMENT '收藏状态',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id` ASC, `target_id` ASC, `target_type` ASC) USING BTREE,
  INDEX `user_id_2`(`user_id` ASC, `target_id` ASC, `target_type` ASC) USING BTREE,
  INDEX `idx_collection`(`target_id` ASC, `target_type` ASC, `user_id` ASC, `is_active` ASC, `id` ASC, `create_time` ASC) USING BTREE,
  CONSTRAINT `lt_collection_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `lt_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lt_collection
-- ----------------------------
INSERT INTO `lt_collection` VALUES (4, 7, 2, 1, '2024-05-01 19:55:41', 1);
INSERT INTO `lt_collection` VALUES (7, 7, 6, 1, '2024-09-15 23:31:20', 0);
INSERT INTO `lt_collection` VALUES (5, 8, 6, 1, '2024-05-11 17:20:10', 1);
INSERT INTO `lt_collection` VALUES (10, 7, 9, 1, '2024-09-22 22:10:46', 0);
INSERT INTO `lt_collection` VALUES (11, 7, 12, 1, '2024-09-22 22:29:39', 0);
INSERT INTO `lt_collection` VALUES (12, 7, 15, 1, '2024-09-23 22:57:40', 0);
INSERT INTO `lt_collection` VALUES (13, 7, 21, 2, '2024-09-24 17:18:25', 0);
INSERT INTO `lt_collection` VALUES (8, 7, 33, 2, '2024-09-22 00:16:38', 0);
INSERT INTO `lt_collection` VALUES (6, 8, 33, 2, '2024-05-11 17:20:52', 1);
INSERT INTO `lt_collection` VALUES (9, 7, 35, 2, '2024-09-22 21:22:18', 0);

-- ----------------------------
-- Table structure for lt_comment
-- ----------------------------
DROP TABLE IF EXISTS `lt_comment`;
CREATE TABLE `lt_comment`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '留言评论唯一标识',
  `user_id` bigint NOT NULL COMMENT '留言评论用户ID',
  `reference_id` int NOT NULL COMMENT '相关实体ID',
  `parent_id` int NULL DEFAULT NULL COMMENT '父评论ID',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '留言评论内容',
  `comment_time` datetime NOT NULL COMMENT '留言评论时间',
  `type` tinyint(1) NOT NULL COMMENT '留言评论类型（1：留言，2：评论）',
  `like_count` int NULL DEFAULT 0 COMMENT '点赞数量',
  `is_delete` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC, `reference_id` ASC, `type` ASC) USING BTREE,
  INDEX `idx_comment`(`reference_id` ASC, `type` ASC, `id` ASC, `user_id` ASC, `parent_id` ASC, `content`(400) ASC, `comment_time` ASC, `is_delete` ASC) USING BTREE,
  CONSTRAINT `lt_comment_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `lt_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 80 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '留言/评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lt_comment
-- ----------------------------
INSERT INTO `lt_comment` VALUES (1, 7, 3, 0, '看看看看', '2024-03-14 13:02:39', 2, NULL, 0);
INSERT INTO `lt_comment` VALUES (2, 7, 3, 1, '加1', '2024-03-14 13:28:32', 2, NULL, 0);
INSERT INTO `lt_comment` VALUES (3, 7, 3, 1, '加1加1', '2024-03-15 04:03:31', 2, 0, 0);
INSERT INTO `lt_comment` VALUES (4, 7, 2, 0, '来看看', '2024-03-19 07:43:29', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (5, 8, 2, 4, '我也看看', '2024-04-20 15:43:19', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (6, 8, 2, 4, '111', '2024-04-20 15:47:34', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (7, 8, 2, 4, '2233', '2024-04-20 15:52:50', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (8, 7, 2, 7, '1246', '2024-04-21 21:53:26', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (9, 7, 3, 0, '来看看', '2024-04-26 19:58:29', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (10, 7, 3, 0, '111', '2024-04-26 20:05:33', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (11, 7, 3, 0, '123', '2024-04-26 20:19:27', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (12, 7, 4, 0, '123', '2024-04-26 21:35:11', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (13, 7, 4, 0, '456', '2024-04-26 22:38:58', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (14, 7, 4, 0, '789', '2024-04-26 22:44:03', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (15, 7, 4, 0, '457', '2024-04-26 22:48:48', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (16, 7, 4, 0, '4654', '2024-04-26 22:52:35', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (17, 7, 4, 0, '4548', '2024-04-26 23:00:02', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (18, 7, 4, 0, '8797', '2024-04-26 23:06:17', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (19, 7, 4, 0, '854', '2024-04-26 23:33:04', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (20, 7, 4, 0, '498', '2024-04-26 23:34:07', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (21, 7, 5, 0, '575', '2024-04-26 23:36:03', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (22, 7, 5, 0, '978', '2024-04-26 23:36:11', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (23, 7, 5, 0, '64', '2024-04-26 23:51:43', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (24, 7, 5, 0, '787', '2024-04-27 09:26:42', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (25, 7, 5, 0, '755', '2024-04-27 09:28:48', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (26, 7, 5, 0, '7878', '2024-04-27 09:34:44', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (27, 7, 5, 0, '8978', '2024-04-27 09:35:27', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (28, 7, 5, 0, '78797', '2024-04-27 09:35:58', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (29, 7, 5, 0, '4466', '2024-04-27 09:51:17', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (30, 7, 5, 0, '5786', '2024-04-27 09:54:18', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (31, 7, 5, 0, '7864', '2024-04-27 09:55:28', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (32, 7, 4, 0, '8762', '2024-04-27 09:57:58', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (33, 7, 4, 0, '57561', '2024-04-27 10:08:09', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (34, 7, 4, 0, '5464', '2024-04-27 10:18:55', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (35, 7, 4, 0, '867865', '2024-04-27 10:19:09', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (36, 7, 5, 0, '8965', '2024-04-28 09:22:37', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (37, 7, 2, 0, '123', '2024-04-28 10:19:05', 2, 0, 0);
INSERT INTO `lt_comment` VALUES (38, 7, 2, 0, '456', '2024-04-28 10:19:13', 2, 0, 0);
INSERT INTO `lt_comment` VALUES (39, 7, 2, 0, '789', '2024-04-28 10:19:16', 2, 0, 0);
INSERT INTO `lt_comment` VALUES (40, 7, 2, 0, '1024', '2024-04-28 10:19:21', 2, 0, 0);
INSERT INTO `lt_comment` VALUES (41, 7, 2, 0, '204', '2024-04-28 10:19:41', 2, 0, 0);
INSERT INTO `lt_comment` VALUES (42, 8, 3, 9, '125', '2024-05-11 17:10:53', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (43, 8, 4, 12, '252', '2024-05-11 17:17:21', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (44, 8, 4, 0, '142', '2024-05-11 17:17:35', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (45, 8, 4, 0, '136', '2024-05-11 17:17:55', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (46, 8, 6, 0, '一个留言。', '2024-05-11 17:20:00', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (47, 8, 33, 0, '一个评论。', '2024-05-11 17:20:28', 2, 0, 0);
INSERT INTO `lt_comment` VALUES (48, 8, 33, 47, '333', '2024-05-11 17:21:42', 2, 0, 0);
INSERT INTO `lt_comment` VALUES (49, 8, 33, 0, '11', '2024-05-11 17:21:53', 2, 0, 0);
INSERT INTO `lt_comment` VALUES (50, 8, 6, 0, '223', '2024-05-11 17:26:38', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (51, 8, 2, 0, '111', '2024-05-11 17:27:49', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (52, 8, 6, 0, '172', '2024-05-11 17:29:03', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (53, 7, 4, 0, '586', '2024-05-11 17:32:14', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (54, 7, 2, 0, '123', '2024-05-11 17:38:16', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (55, 7, 2, 0, '875', '2024-05-11 17:42:27', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (56, 7, 2, 0, '652', '2024-05-11 17:46:06', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (57, 7, 2, 0, '745', '2024-05-11 17:48:46', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (58, 8, 7, 0, '留言', '2024-05-11 17:56:21', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (59, 8, 7, 0, '123', '2024-05-11 17:56:29', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (60, 8, 7, 0, '456', '2024-05-11 17:56:37', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (61, 8, 7, 0, '725', '2024-05-11 17:56:42', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (62, 7, 3, 0, '11', '2024-09-12 16:49:56', 2, 0, 1);
INSERT INTO `lt_comment` VALUES (63, 7, 33, 0, '12', '2024-09-22 21:24:20', 2, 0, 0);
INSERT INTO `lt_comment` VALUES (64, 7, 35, 0, '11', '2024-09-22 21:26:11', 2, 0, 0);
INSERT INTO `lt_comment` VALUES (65, 7, 9, 0, '11', '2024-09-22 22:11:40', 1, 0, 0);
INSERT INTO `lt_comment` VALUES (66, 7, 24, 0, 'dd', '2024-09-23 23:26:19', 2, 0, 0);
INSERT INTO `lt_comment` VALUES (78, 7, 21, 0, '11', '2024-09-24 17:18:38', 2, 0, 0);
INSERT INTO `lt_comment` VALUES (79, 8, 9, 0, '13', '2024-09-24 18:46:51', 1, 0, 0);

-- ----------------------------
-- Table structure for lt_evaluate
-- ----------------------------
DROP TABLE IF EXISTS `lt_evaluate`;
CREATE TABLE `lt_evaluate`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '评价唯一标识符',
  `order_id` int NOT NULL COMMENT '订单编号',
  `user_id` bigint NOT NULL COMMENT '用户编号',
  `is_favor` tinyint(1) NOT NULL COMMENT '是否好评',
  `content` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单编号',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `order_id`(`order_id` ASC) USING BTREE,
  INDEX `lt_evaluate_ibfk_2`(`user_id` ASC) USING BTREE,
  CONSTRAINT `lt_evaluate_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `lt_order` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `lt_evaluate_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `lt_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lt_evaluate
-- ----------------------------
INSERT INTO `lt_evaluate` VALUES (3, 6, 8, 1, '非常愉快的交易。', '2024-04-30 11:18:42');
INSERT INTO `lt_evaluate` VALUES (5, 6, 7, 1, '一次愉快的交易。', '2024-04-30 14:14:36');
INSERT INTO `lt_evaluate` VALUES (6, 7, 8, 1, '很愉快的交易。', '2024-05-11 19:58:38');
INSERT INTO `lt_evaluate` VALUES (7, 7, 7, 1, '+1', '2024-05-11 20:04:07');
INSERT INTO `lt_evaluate` VALUES (8, 12, 7, 1, '111', '2024-09-24 22:59:16');
INSERT INTO `lt_evaluate` VALUES (9, 12, 8, 1, '112', '2024-09-24 23:00:47');

-- ----------------------------
-- Table structure for lt_images
-- ----------------------------
DROP TABLE IF EXISTS `lt_images`;
CREATE TABLE `lt_images`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '图片唯一标识',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '图片地址',
  `related_id` int NULL DEFAULT NULL COMMENT '关联编号',
  `type` enum('product','post','chat','user') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '类型',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 84 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lt_images
-- ----------------------------
INSERT INTO `lt_images` VALUES (1, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/03/12/0f5582ee11f449d69220d15676bc0200.png', 3, 'post', '2024-03-13 09:08:31');
INSERT INTO `lt_images` VALUES (2, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/03/12/23f2ce4dc8db442ba18a71eba5d5a139.png', 3, 'post', '2024-03-13 09:08:31');
INSERT INTO `lt_images` VALUES (17, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/03/13/4262d2c1cad4443c9096cc802d29c76b.png', 21, 'post', '2024-03-13 15:59:15');
INSERT INTO `lt_images` VALUES (18, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/03/13/396e8154b6834828b45221876ec11c5a.png', 22, 'post', '2024-03-13 16:00:54');
INSERT INTO `lt_images` VALUES (19, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/03/13/91ff16b8ffd64e1e804f418e5388c184.png', 23, 'post', '2024-03-13 16:03:16');
INSERT INTO `lt_images` VALUES (25, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/03/18/217051b5be414f5cb0274034c625562f.jpg', 2, 'product', '2024-03-18 00:00:00');
INSERT INTO `lt_images` VALUES (26, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/03/18/e592283c521e44768dabe75bf0bb334c.png', 3, 'product', '2024-03-18 00:00:00');
INSERT INTO `lt_images` VALUES (27, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/03/18/52bf8fdf42dd4834a4dca5584aaf96e0.jpg', 4, 'product', '2024-03-18 00:00:00');
INSERT INTO `lt_images` VALUES (28, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/03/18/0c55ff373e084104b7892bcc9e50d24d.jpg', 5, 'product', '2024-03-18 00:00:00');
INSERT INTO `lt_images` VALUES (29, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/03/18/58d0fea37ece43e2a413f7a7b62cf1d7.jpg', 5, 'product', '2024-03-18 00:00:00');
INSERT INTO `lt_images` VALUES (30, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/03/18/dfa7b396b97a4cb694117b561203ff3c.jpg', 5, 'product', '2024-03-18 00:00:00');
INSERT INTO `lt_images` VALUES (31, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/03/18/aab24b21e0b24d4abd3d5c6df0926a10.jpg', 5, 'product', '2024-03-18 00:00:00');
INSERT INTO `lt_images` VALUES (32, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/04/17/f0084924e76e4a0f8cca1caf90c98aea.jpg', 1, 'product', '2024-04-17 17:45:40');
INSERT INTO `lt_images` VALUES (37, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/04/17/3152e3b1ea3c4e179d6ea7ce83fb6b70.png', 1, 'product', '2024-04-17 18:53:35');
INSERT INTO `lt_images` VALUES (38, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/04/17/b36335dc4a0149baa054678ac43c0ce5.png', 1, 'product', '2024-04-17 18:57:20');
INSERT INTO `lt_images` VALUES (39, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/04/28/2d8a9386cc8f42079d1a8ca874261b03.jpg', 2, 'post', '2024-04-28 10:44:21');
INSERT INTO `lt_images` VALUES (40, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/04/28/c5e05caa24974799b6f23c27e0ae633a.png', 2, 'post', '2024-04-28 10:59:55');
INSERT INTO `lt_images` VALUES (41, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-user-images/2024/05/03/02d5682ed68d4151a02fb3ecfe24a82c.jpeg', 7, 'user', '2024-05-03 20:21:37');
INSERT INTO `lt_images` VALUES (44, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/05/11/b3bdce575de24eb1ac4f6c842bfc40f7.jpg', 6, 'product', '2024-05-11 16:18:19');
INSERT INTO `lt_images` VALUES (45, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/05/11/daad7acb23d646f4801a86efa9a5c639.jpg', 33, 'post', '2024-05-11 16:45:49');
INSERT INTO `lt_images` VALUES (46, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/05/11/669dd0d8e7e64fbcbbf806f7c99e1e3b.jpg', 7, 'product', '2024-05-11 17:51:17');
INSERT INTO `lt_images` VALUES (48, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/05/21/50fc569bcf26440e8f91b68a8dd9d734.png', 2102980610, 'chat', '2024-05-21 12:16:24');
INSERT INTO `lt_images` VALUES (49, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/05/24/c21f78447eea4547824995dea5bec9b4.jpg', 35, 'post', '2024-05-24 22:35:44');
INSERT INTO `lt_images` VALUES (50, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/09/15/e00d90baae29407e85ca56d062617a08.png', 9, 'product', '2024-09-15 23:50:12');
INSERT INTO `lt_images` VALUES (57, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/09/23/f880534bd20f4dc8bec7709dee808fb1.png', 15, 'product', '2024-09-23 21:38:26');
INSERT INTO `lt_images` VALUES (66, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/09/23/7a94db9323134db584a5a5f4ebe3dd96.png', 14, 'product', '2024-09-23 22:19:47');
INSERT INTO `lt_images` VALUES (67, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/09/23/cd19060b5476400593bf13fb7a7c7cbc.png', 14, 'product', '2024-09-23 22:21:19');
INSERT INTO `lt_images` VALUES (71, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/09/24/4bafb19ef6734b76b501d3b1e1a3ae70.png', 24, 'post', '2024-09-24 11:00:49');
INSERT INTO `lt_images` VALUES (79, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/09/24/614129337c17496fb6ff66e7ff7def50.png', 15, 'product', '2024-09-24 17:07:08');
INSERT INTO `lt_images` VALUES (80, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/09/24/9aef2dd7c4d84c2587243972a3f2fcc8.png', 24, 'post', '2024-09-24 17:17:40');
INSERT INTO `lt_images` VALUES (82, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-user-images/2024/09/24/18e5ade7687246be9d4bd1075054d179.jpg', 8, 'user', '2024-09-24 18:39:41');
INSERT INTO `lt_images` VALUES (83, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-user-images/2024/09/24/109e65bed81146e7bbb6e19370db9e31.jpg', 8, 'user', '2024-09-24 18:44:31');

-- ----------------------------
-- Table structure for lt_like
-- ----------------------------
DROP TABLE IF EXISTS `lt_like`;
CREATE TABLE `lt_like`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '点赞唯一标识',
  `user_id` bigint NOT NULL COMMENT '点赞用户ID',
  `target_id` int NOT NULL COMMENT '点赞目标ID',
  `target_type` tinyint(1) NOT NULL COMMENT '点赞目标类型（1: 帖子, 2: 评论，3：留言）',
  `is_active` tinyint(1) NOT NULL DEFAULT 1 COMMENT '点赞状态',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id_2`(`user_id` ASC, `target_id` ASC, `target_type` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC, `target_id` ASC, `target_type` ASC) USING BTREE,
  CONSTRAINT `lt_like_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `lt_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lt_like
-- ----------------------------
INSERT INTO `lt_like` VALUES (1, 7, 3, 1, 1, '2024-03-15 16:58:39');
INSERT INTO `lt_like` VALUES (3, 7, 5, 3, 1, '2024-04-21 13:05:34');
INSERT INTO `lt_like` VALUES (4, 7, 4, 3, 1, '2024-04-21 18:25:50');
INSERT INTO `lt_like` VALUES (5, 7, 8, 3, 0, '2024-04-21 22:03:34');
INSERT INTO `lt_like` VALUES (6, 7, 2, 1, 1, '2024-04-24 20:05:00');
INSERT INTO `lt_like` VALUES (7, 7, 1, 2, 1, '2024-04-28 09:49:53');
INSERT INTO `lt_like` VALUES (8, 7, 21, 1, 1, '2024-04-28 11:06:11');
INSERT INTO `lt_like` VALUES (9, 8, 33, 1, 1, '2024-05-11 17:20:57');
INSERT INTO `lt_like` VALUES (11, 7, 33, 1, 1, '2024-09-12 16:07:48');
INSERT INTO `lt_like` VALUES (12, 7, 35, 1, 0, '2024-09-22 21:22:16');
INSERT INTO `lt_like` VALUES (13, 7, 64, 2, 0, '2024-09-22 21:44:10');
INSERT INTO `lt_like` VALUES (14, 7, 65, 3, 1, '2024-09-22 22:11:56');
INSERT INTO `lt_like` VALUES (15, 7, 36, 1, 0, '2024-09-22 22:30:31');
INSERT INTO `lt_like` VALUES (16, 7, 24, 1, 1, '2024-09-23 23:26:05');
INSERT INTO `lt_like` VALUES (17, 7, 78, 2, 1, '2024-09-24 17:18:40');

-- ----------------------------
-- Table structure for lt_message
-- ----------------------------
DROP TABLE IF EXISTS `lt_message`;
CREATE TABLE `lt_message`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '消息唯一标识',
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息内容',
  `message_type` tinyint(1) NOT NULL COMMENT '消息类型（1：通知，2：消息）',
  `sender_id` bigint NOT NULL COMMENT '发送者ID',
  `receiver_id` bigint NOT NULL COMMENT '接收者ID',
  `send_time` datetime NOT NULL COMMENT '消息发送时间',
  `relation_id` int NULL DEFAULT NULL COMMENT '聊天关系编号',
  `is_image` tinyint(1) NULL DEFAULT NULL,
  `is_read` tinyint(1) NULL DEFAULT NULL,
  `is_order` tinyint(1) NULL DEFAULT NULL,
  `order_id` int NULL DEFAULT NULL,
  `is_delete` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `relation_id`(`relation_id` ASC) USING BTREE,
  INDEX `idx_message`(`id` ASC, `title` ASC, `content`(400) ASC, `message_type` ASC, `sender_id` ASC, `receiver_id` ASC, `is_delete` ASC, `send_time` ASC, `relation_id` ASC, `is_image` ASC, `is_read` ASC, `is_order` ASC, `order_id` ASC) USING BTREE,
  INDEX `lt_message_ibfk_1`(`sender_id` ASC) USING BTREE,
  INDEX `lt_message_ibfk_2`(`receiver_id` ASC) USING BTREE,
  CONSTRAINT `lt_message_ibfk_1` FOREIGN KEY (`sender_id`) REFERENCES `lt_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `lt_message_ibfk_2` FOREIGN KEY (`receiver_id`) REFERENCES `lt_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `lt_message_ibfk_3` FOREIGN KEY (`relation_id`) REFERENCES `lt_chat_relation` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 61 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '通知/消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lt_message
-- ----------------------------
INSERT INTO `lt_message` VALUES (1, NULL, '你好', 2, 8, 7, '2024-04-14 16:46:11', 2102980610, NULL, 1, NULL, NULL, 0);
INSERT INTO `lt_message` VALUES (2, NULL, '你好', 2, 7, 8, '2024-04-14 21:52:26', 2102980610, NULL, 1, NULL, NULL, 0);
INSERT INTO `lt_message` VALUES (3, NULL, '价格是这么多不会变了是吗？', 2, 8, 7, '2024-04-22 10:38:04', 2102980610, NULL, 1, NULL, NULL, 0);
INSERT INTO `lt_message` VALUES (4, NULL, '是的', 2, 7, 8, '2024-04-22 10:40:41', 2102980610, NULL, 1, NULL, NULL, 0);
INSERT INTO `lt_message` VALUES (5, NULL, '有什么问题吗？', 2, 7, 8, '2024-04-22 16:13:58', 2102980610, NULL, 1, NULL, NULL, 0);
INSERT INTO `lt_message` VALUES (6, NULL, '暂时没有', 2, 8, 7, '2024-04-22 16:38:04', 2102980610, NULL, 1, NULL, NULL, 0);
INSERT INTO `lt_message` VALUES (7, NULL, '好的', 2, 7, 8, '2024-04-22 19:38:32', 2102980610, NULL, 1, NULL, NULL, 0);
INSERT INTO `lt_message` VALUES (8, NULL, '111', 2, 8, 7, '2024-04-22 19:40:00', 2102980610, NULL, 1, NULL, NULL, 0);
INSERT INTO `lt_message` VALUES (9, NULL, '你好', 2, 8, 7, '2024-04-29 11:11:18', 1423503362, 0, 1, 0, NULL, 0);
INSERT INTO `lt_message` VALUES (10, NULL, '你好', 2, 7, 8, '2024-04-29 13:25:55', 1423503362, 0, 1, 0, NULL, 0);
INSERT INTO `lt_message` VALUES (11, '我已拍下，待付款', '我已下单，等待我的付款', 2, 8, 7, '2024-04-29 15:23:12', 1423503362, 0, 1, 1, 5, 0);
INSERT INTO `lt_message` VALUES (12, NULL, '消息消息', 2, 8, 7, '2024-04-29 18:08:22', 1423503362, 0, 1, 0, NULL, 0);
INSERT INTO `lt_message` VALUES (13, NULL, '12', 2, 8, 7, '2024-04-29 18:19:19', 1423503362, 0, 1, 0, NULL, 0);
INSERT INTO `lt_message` VALUES (14, NULL, '44', 2, 8, 7, '2024-04-29 18:22:58', 1423503362, 0, 1, 0, NULL, 0);
INSERT INTO `lt_message` VALUES (15, NULL, '25', 2, 8, 7, '2024-04-29 18:27:19', 1423503362, 0, 1, 0, NULL, 0);
INSERT INTO `lt_message` VALUES (16, NULL, '56', 2, 8, 7, '2024-04-29 19:04:15', 1423503362, 0, 1, 0, NULL, 0);
INSERT INTO `lt_message` VALUES (17, NULL, '78', 2, 8, 7, '2024-04-29 19:07:32', 1423503362, 0, 1, 0, NULL, 0);
INSERT INTO `lt_message` VALUES (18, NULL, '36', 2, 8, 7, '2024-04-29 19:08:38', 1423503362, 0, 1, 0, NULL, 0);
INSERT INTO `lt_message` VALUES (19, NULL, '63', 2, 8, 7, '2024-04-29 19:21:29', 1423503362, 0, 1, 0, NULL, 0);
INSERT INTO `lt_message` VALUES (20, NULL, '132', 2, 8, 7, '2024-04-29 19:55:23', 1423503362, 0, 1, 0, NULL, 0);
INSERT INTO `lt_message` VALUES (21, NULL, '235', 2, 8, 7, '2024-04-29 19:55:55', 1423503362, 0, 1, 0, NULL, 0);
INSERT INTO `lt_message` VALUES (22, NULL, '142', 2, 8, 7, '2024-04-29 20:00:41', 1423503362, 0, 1, 0, NULL, 0);
INSERT INTO `lt_message` VALUES (23, NULL, '25', 2, 8, 7, '2024-04-29 20:18:09', 1423503362, 0, 1, 0, NULL, 0);
INSERT INTO `lt_message` VALUES (24, NULL, '53', 2, 8, 7, '2024-04-29 20:35:04', 1423503362, 0, 1, 0, NULL, 0);
INSERT INTO `lt_message` VALUES (25, '我已拍下，待付款', '我已下单，等待我的付款', 2, 8, 7, '2024-04-29 21:09:29', 1423503362, 0, 1, 1, 6, 0);
INSERT INTO `lt_message` VALUES (26, '我已付款，等待你发货', '请带上物品，按照我提供的地址送货', 2, 8, 7, '2024-04-29 21:09:33', 1423503362, 0, 1, 1, 6, 0);
INSERT INTO `lt_message` VALUES (27, NULL, '23', 2, 8, 7, '2024-04-29 21:12:28', 1423503362, 0, 1, 0, NULL, 0);
INSERT INTO `lt_message` VALUES (28, '我已发货，等待你收货', '我正在送货路上，请等待收货', 2, 7, 8, '2024-04-29 21:43:27', 1423503362, 0, 1, 1, 6, 0);
INSERT INTO `lt_message` VALUES (29, '交易完成，等待评价', '双方交易完成，请及时完成评价', 2, 8, 7, '2024-04-29 21:47:19', 1423503362, 0, 1, 1, 6, 0);
INSERT INTO `lt_message` VALUES (30, '完成评价', '我已完成评价', 2, 8, 7, '2024-04-30 11:18:42', 1423503362, 0, 1, 1, 6, 0);
INSERT INTO `lt_message` VALUES (32, '完成评价', '我已完成评价', 2, 7, 8, '2024-04-30 14:14:36', 1423503362, 0, 1, 1, 6, 0);
INSERT INTO `lt_message` VALUES (33, NULL, '你好', 2, 7, 8, '2024-05-11 18:47:36', 1901649922, 0, 1, 0, NULL, 0);
INSERT INTO `lt_message` VALUES (34, NULL, '你好', 2, 8, 7, '2024-05-11 19:09:53', 1901649922, 0, 1, 0, NULL, 0);
INSERT INTO `lt_message` VALUES (35, '我已拍下，待付款', '我已下单，等待我的付款', 2, 7, 8, '2024-05-11 19:51:43', 1901649922, 0, 1, 1, 7, 0);
INSERT INTO `lt_message` VALUES (36, '我已付款，等待你发货', '请带上物品，按照我提供的地址送货', 2, 7, 8, '2024-05-11 19:51:51', 1901649922, 0, 1, 1, 7, 0);
INSERT INTO `lt_message` VALUES (39, '我已发货，等待你收货', '我正在送货路上，请等待收货', 2, 8, 7, '2024-05-11 19:58:11', 1901649922, 0, 1, 1, 7, 0);
INSERT INTO `lt_message` VALUES (40, '交易完成，等待评价', '双方交易完成，请及时完成评价', 2, 7, 8, '2024-05-11 19:58:18', 1901649922, 0, 1, 1, 7, 0);
INSERT INTO `lt_message` VALUES (41, '完成评价', '我已完成评价', 2, 8, 7, '2024-05-11 19:58:39', 1901649922, 0, 1, 1, 7, 0);
INSERT INTO `lt_message` VALUES (42, '完成评价', '我已完成评价', 2, 7, 8, '2024-05-11 20:04:07', 1901649922, 0, 1, 1, 7, 0);
INSERT INTO `lt_message` VALUES (43, NULL, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/05/21/50fc569bcf26440e8f91b68a8dd9d734.png', 2, 7, 8, '2024-05-21 12:16:24', 2102980610, 1, 1, 0, NULL, 0);
INSERT INTO `lt_message` VALUES (44, NULL, '222', 2, 7, 8, '2024-05-24 22:21:33', 2102980610, 0, 1, 0, NULL, 0);
INSERT INTO `lt_message` VALUES (45, NULL, '55', 2, 8, 7, '2024-05-24 22:22:15', 2102980610, 0, 1, 0, NULL, 0);
INSERT INTO `lt_message` VALUES (46, NULL, '56', 2, 7, 8, '2024-09-12 14:09:23', 2102980610, NULL, 1, NULL, NULL, 0);
INSERT INTO `lt_message` VALUES (47, NULL, '57', 2, 7, 8, '2024-09-12 15:47:44', 2102980610, NULL, 1, NULL, NULL, 0);
INSERT INTO `lt_message` VALUES (48, NULL, '11', 2, 7, 8, '2024-09-15 23:46:38', 2102980610, NULL, 1, NULL, NULL, 0);
INSERT INTO `lt_message` VALUES (49, NULL, '11', 2, 7, 8, '2024-09-22 00:17:18', 2102980610, NULL, 1, NULL, NULL, 0);
INSERT INTO `lt_message` VALUES (50, NULL, '12', 2, 8, 7, '2024-09-24 17:24:17', 2102980610, NULL, 1, NULL, NULL, 0);
INSERT INTO `lt_message` VALUES (51, NULL, '13', 2, 7, 8, '2024-09-24 17:24:38', 2102980610, NULL, 1, NULL, NULL, 0);
INSERT INTO `lt_message` VALUES (52, NULL, '14', 2, 8, 7, '2024-09-24 18:23:52', 2102980610, NULL, 1, NULL, NULL, 0);
INSERT INTO `lt_message` VALUES (53, NULL, '111', 2, 8, 7, '2024-09-24 18:47:25', 542769154, NULL, 1, NULL, NULL, 0);
INSERT INTO `lt_message` VALUES (54, NULL, '52', 2, 8, 7, '2024-09-24 22:52:41', 2102980610, NULL, 1, NULL, NULL, 0);
INSERT INTO `lt_message` VALUES (55, '我已拍下，待付款', '我已下单，等待我的付款', 2, 7, 8, '2024-09-24 22:58:40', 905981954, NULL, 1, 1, 12, 0);
INSERT INTO `lt_message` VALUES (56, '我已付款，等待你发货', '请带上物品，按照我提供的地址送货', 2, 7, 8, '2024-09-24 22:58:47', 905981954, NULL, 1, 1, 12, 0);
INSERT INTO `lt_message` VALUES (57, '我已发货，等待你收货', '我正在送货路上，请等待收货', 2, 8, 7, '2024-09-24 22:58:53', 905981954, NULL, 1, 1, 12, 0);
INSERT INTO `lt_message` VALUES (58, '交易完成，等待评价', '双方交易完成，请及时完成评价', 2, 7, 8, '2024-09-24 22:59:03', 905981954, NULL, 1, 1, 12, 0);
INSERT INTO `lt_message` VALUES (59, '完成评价', '我已完成评价', 2, 7, 8, '2024-09-24 22:59:16', 905981954, NULL, 1, 1, 12, 0);
INSERT INTO `lt_message` VALUES (60, '完成评价', '我已完成评价', 2, 8, 7, '2024-09-24 23:00:47', 905981954, NULL, 0, 1, 12, 0);

-- ----------------------------
-- Table structure for lt_order
-- ----------------------------
DROP TABLE IF EXISTS `lt_order`;
CREATE TABLE `lt_order`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '订单唯一标识',
  `order_num` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单号',
  `buyer_id` bigint NOT NULL COMMENT '买家ID',
  `seller_id` bigint NOT NULL COMMENT '卖家ID',
  `product_id` int NOT NULL COMMENT '商品ID',
  `price` decimal(10, 2) NOT NULL COMMENT '订单金额',
  `address_id` int NOT NULL COMMENT '收货地址ID',
  `status` tinyint(1) NOT NULL COMMENT '订单状态（待付款：1，待发货：2，待收货：3，已完成：4，已取消：5）',
  `create_time` timestamp NOT NULL COMMENT '下单时间',
  `pay_time` timestamp NULL DEFAULT NULL COMMENT '付款时间',
  `ship_time` timestamp NULL DEFAULT NULL COMMENT '发货时间',
  `finish_time` timestamp NULL DEFAULT NULL COMMENT '成交时间',
  `transaction_num` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信交易号',
  `is_delete` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_num`(`order_num` ASC) USING BTREE,
  INDEX `product_id`(`product_id` ASC) USING BTREE,
  INDEX `address_id`(`address_id` ASC) USING BTREE,
  INDEX `idx_order`(`status` ASC, `order_num` ASC, `buyer_id` ASC, `seller_id` ASC, `product_id` ASC, `is_delete` ASC, `id` ASC, `price` ASC, `address_id` ASC, `create_time` ASC, `pay_time` ASC, `ship_time` ASC, `finish_time` ASC, `transaction_num` ASC) USING BTREE,
  INDEX `lt_order_ibfk_1`(`buyer_id` ASC) USING BTREE,
  INDEX `lt_order_ibfk_2`(`seller_id` ASC) USING BTREE,
  CONSTRAINT `lt_order_ibfk_1` FOREIGN KEY (`buyer_id`) REFERENCES `lt_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `lt_order_ibfk_2` FOREIGN KEY (`seller_id`) REFERENCES `lt_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `lt_order_ibfk_3` FOREIGN KEY (`product_id`) REFERENCES `lt_product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `lt_order_ibfk_4` FOREIGN KEY (`address_id`) REFERENCES `lt_address` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lt_order
-- ----------------------------
INSERT INTO `lt_order` VALUES (3, '1713795587040871', 8, 7, 1, 10.00, 4, 2, '2024-04-22 22:19:47', '2024-04-22 22:32:34', NULL, NULL, NULL, 0);
INSERT INTO `lt_order` VALUES (6, '1714396168397875', 8, 7, 5, 20.00, 4, 4, '2024-04-29 21:09:28', '2024-04-29 21:09:33', '2024-04-29 21:43:27', '2024-04-29 21:47:19', NULL, 0);
INSERT INTO `lt_order` VALUES (7, '1715428302593787', 7, 8, 7, 2.00, 2, 4, '2024-05-11 19:51:42', '2024-05-11 19:51:51', '2024-05-11 19:58:11', '2024-05-11 19:58:18', NULL, 1);
INSERT INTO `lt_order` VALUES (12, '1727189919783786', 7, 8, 6, 9.00, 2, 4, '2024-09-24 22:58:40', '2024-09-24 22:58:47', '2024-09-24 22:58:53', '2024-09-24 22:59:03', NULL, 0);
INSERT INTO `lt_order` VALUES (1, '1713790168464871', 8, 7, 1, 10.00, 4, 5, '2024-04-22 20:49:28', NULL, NULL, NULL, NULL, 0);
INSERT INTO `lt_order` VALUES (2, '1713792859515871', 8, 7, 1, 10.00, 4, 5, '2024-04-22 21:34:20', NULL, NULL, NULL, NULL, 0);
INSERT INTO `lt_order` VALUES (8, '1715428422618787', 7, 8, 7, 2.00, 2, 5, '2024-05-11 19:53:42', NULL, NULL, NULL, NULL, 0);
INSERT INTO `lt_order` VALUES (9, '1727186113246786', 7, 8, 6, 9.00, 2, 5, '2024-09-24 21:55:13', '2024-09-24 22:21:48', NULL, NULL, NULL, 0);
INSERT INTO `lt_order` VALUES (10, '1727188367590786', 7, 8, 6, 9.00, 2, 5, '2024-09-24 22:32:47', '2024-09-24 22:36:58', NULL, NULL, NULL, 0);
INSERT INTO `lt_order` VALUES (11, '1727188854275786', 7, 8, 6, 9.00, 2, 5, '2024-09-24 22:40:54', '2024-09-24 22:43:37', NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for lt_post
-- ----------------------------
DROP TABLE IF EXISTS `lt_post`;
CREATE TABLE `lt_post`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '帖子唯一标识',
  `user_id` bigint NOT NULL COMMENT '发帖用户ID',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '帖子标题',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '帖子内容',
  `image_url` varchar(350) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '帖子图片URL',
  `status` tinyint(1) NOT NULL COMMENT '帖子状态（0：草稿，1：发布，3：待审核）',
  `post_time` datetime NOT NULL COMMENT '帖子发布时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '帖子更新时间',
  `like_count` int NULL DEFAULT 0 COMMENT '点赞数量',
  `comment_count` int NULL DEFAULT 0 COMMENT '评论数量',
  `collect_count` int NULL DEFAULT 0 COMMENT '收藏数量',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_post`(`status` ASC, `post_time` DESC, `user_id` ASC, `content`(400) ASC, `id` ASC, `image_url` ASC, `update_time` ASC, `like_count` ASC, `comment_count` ASC, `collect_count` ASC) USING BTREE,
  INDEX `lt_post_ibfk_1`(`user_id` ASC) USING BTREE,
  CONSTRAINT `lt_post_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `lt_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '帖子表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lt_post
-- ----------------------------
INSERT INTO `lt_post` VALUES (2, 7, 'yhrde', 'rfsee', NULL, 1, '2024-03-12 15:29:20', '2024-04-28 10:59:55', 1, 5, 0);
INSERT INTO `lt_post` VALUES (3, 7, 'dwaed', 'tat', 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/03/12/0f5582ee11f449d69220d15676bc0200.png,https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-images/2024/03/12/23f2ce4dc8db442ba18a71eba5d5a139.png', 1, '2024-03-12 16:14:48', NULL, 1, 3, 0);
INSERT INTO `lt_post` VALUES (21, 7, 'gsegse', 'waeawf', NULL, 0, '2024-03-13 15:59:15', NULL, 1, 0, 0);
INSERT INTO `lt_post` VALUES (22, 7, 'fgbdg', 'jfgjfg', NULL, 0, '2024-03-13 16:00:53', NULL, 0, 0, 0);
INSERT INTO `lt_post` VALUES (24, 7, 'tye4tewae', 'rwarawsy', NULL, 1, '2024-03-13 16:08:00', '2024-09-24 17:17:51', 0, 0, 0);
INSERT INTO `lt_post` VALUES (33, 8, '一个帖子', '一个帖子的描述。。。', NULL, 1, '2024-05-11 16:45:49', NULL, 0, 0, 0);
INSERT INTO `lt_post` VALUES (35, 8, '帖子贴', '帖子帖子。', NULL, 1, '2024-05-24 22:35:05', '2024-09-22 13:30:08', 0, 0, 0);
INSERT INTO `lt_post` VALUES (36, 7, 'rserr', 'rsers2w', NULL, 1, '2024-09-22 22:30:06', '2024-09-22 22:30:06', 0, 0, 0);

-- ----------------------------
-- Table structure for lt_product
-- ----------------------------
DROP TABLE IF EXISTS `lt_product`;
CREATE TABLE `lt_product`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '商品唯一标识',
  `publisher_id` bigint NOT NULL COMMENT '发布者ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品名称',
  `category_id` int NOT NULL COMMENT '商品分类',
  `description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '商品描述',
  `image_url` varchar(350) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品图片URL',
  `condition` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品成色',
  `price` decimal(10, 2) NOT NULL COMMENT '商品价格',
  `publish_time` datetime NOT NULL COMMENT '商品发布时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '商品更新时间',
  `status` tinyint(1) NOT NULL COMMENT '商品状态（0：草稿，1：上架，2：下架，3：待审核，4：已售出）',
  `is_recommended` tinyint NOT NULL DEFAULT 0 COMMENT '是否推荐',
  `is_delete` tinyint NULL DEFAULT 0,
  `is_lock` tinyint(1) NOT NULL DEFAULT 0,
  `lock_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `category_id`(`category_id` ASC) USING BTREE,
  INDEX `idx_product`(`status` ASC, `is_lock` ASC, `is_recommended` DESC, `publish_time` DESC, `price` DESC, `id` ASC, `publisher_id` ASC, `name` ASC, `category_id` ASC, `description`(400) ASC, `condition` ASC, `update_time` ASC) USING BTREE,
  INDEX `lt_product_ibfk_1`(`publisher_id` ASC) USING BTREE,
  CONSTRAINT `lt_product_ibfk_1` FOREIGN KEY (`publisher_id`) REFERENCES `lt_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `lt_product_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `lt_category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lt_product
-- ----------------------------
INSERT INTO `lt_product` VALUES (1, 7, '第一个商品', 27, '第一个商品', NULL, '轻微使用痕迹', 10.00, '2024-03-18 13:03:14', '2024-04-17 18:57:20', 4, 1, 0, 0, '');
INSERT INTO `lt_product` VALUES (2, 7, '第二个商品', 27, '第二个商品', NULL, '几乎全新', 10.00, '2024-03-18 16:34:44', NULL, 1, 0, 0, 0, NULL);
INSERT INTO `lt_product` VALUES (3, 7, '第三个商品', 27, '第三个商品', NULL, '几乎全新', 12.00, '2024-03-18 16:35:26', NULL, 1, 1, 0, 0, NULL);
INSERT INTO `lt_product` VALUES (4, 7, '第四个商品', 27, '第四个商品', NULL, '全新', 15.00, '2024-03-18 16:35:50', NULL, 1, 1, 0, 0, NULL);
INSERT INTO `lt_product` VALUES (5, 7, '第五个商品', 27, '第五个商品', NULL, '明显使用痕迹', 20.00, '2024-03-18 16:41:59', NULL, 4, 0, 0, 0, '');
INSERT INTO `lt_product` VALUES (6, 8, '一个商品', 8, '一个商品描述。', NULL, '轻微使用痕迹', 9.00, '2024-05-11 16:18:20', '2024-09-24 21:55:13', 4, 0, 0, 0, '');
INSERT INTO `lt_product` VALUES (7, 8, '一个商品一个商品', 8, '一个商品的描述。', NULL, '全新', 2.00, '2024-05-11 17:51:17', NULL, 4, 0, 0, 0, '');
INSERT INTO `lt_product` VALUES (9, 7, '商品33', 17, '商品333', NULL, '全新', 6.00, '2024-09-16 00:02:47', '2024-09-22 13:19:13', 1, 0, 0, 0, NULL);
INSERT INTO `lt_product` VALUES (12, 7, 'resres', 21, 'testtdr', NULL, '全新', 1.00, '2024-09-22 22:27:11', '2024-09-23 21:33:38', 2, 0, 1, 0, NULL);
INSERT INTO `lt_product` VALUES (13, 7, 'rawraw', 21, 'eawraw', NULL, '全新', 6.00, '2024-09-23 18:29:58', '2024-09-23 21:30:10', 2, 0, 1, 0, NULL);
INSERT INTO `lt_product` VALUES (14, 7, 'ydrtseraw', 24, 'eaweawraww', NULL, '全新', 5.00, '2024-09-23 18:30:51', '2024-09-23 21:37:13', 0, 0, 1, 0, NULL);
INSERT INTO `lt_product` VALUES (15, 7, 'rtsetes', 13, 'eawet', NULL, '全新', 6.00, '2024-09-23 21:38:26', '2024-09-24 17:07:08', 1, 0, 0, 0, NULL);

-- ----------------------------
-- Table structure for lt_user
-- ----------------------------
DROP TABLE IF EXISTS `lt_user`;
CREATE TABLE `lt_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户编号',
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录名称(用户名)',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录密码',
  `real_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `nick_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `gender` tinyint NOT NULL COMMENT '性别(0-男，1-女)',
  `phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `introduction` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '个人介绍',
  `birthday` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生日',
  `open_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信open_id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `is_delete` tinyint NULL DEFAULT 0 COMMENT '是否删除(0-未删除，1-已删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `open_id`(`open_id` ASC) USING BTREE,
  INDEX `sys_user_idx1`(`username` ASC, `real_name` ASC, `nick_name` ASC) USING BTREE,
  INDEX `sys_user_idx2`(`gender` ASC, `phone` ASC) USING BTREE,
  INDEX `idx_lt_user`(`id` ASC, `username` ASC, `real_name` ASC, `nick_name` ASC, `gender` ASC, `phone` ASC, `email` ASC, `avatar` ASC, `introduction`(200) ASC, `birthday` ASC, `open_id` ASC, `create_time` ASC, `update_time` ASC, `is_delete` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lt_user
-- ----------------------------
INSERT INTO `lt_user` VALUES (7, 'o7vvC4p2KtL', '$2a$10$lajCGSDoLqmbZLwPo7dcQukn79bxCBPOQo7vD7J8KhJ2jyyNYDFeW', NULL, '春夏秋冬', 0, NULL, NULL, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-user-images/2024/05/03/02d5682ed68d4151a02fb3ecfe24a82c.jpeg', '1', '2001-02-16', 'o7vvC4p2KtL', '2024-03-06 20:00:24', '2024-09-22 22:55:18', 0);
INSERT INTO `lt_user` VALUES (8, 'o7vvC4upE', '$2a$10$Jtxp4akheEK0mvwrD3mfR.jVQ0gULePAaUHalsAEIXtt7fwF.rSEO', NULL, '夜半歌声', 0, NULL, NULL, 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/letao-user-images/2024/09/24/109e65bed81146e7bbb6e19370db9e31.jpg', NULL, '2001-07-17', 'o7vvC4upE', '2024-04-01 15:42:36', '2024-09-24 18:44:31', 0);

-- ----------------------------
-- Table structure for lt_user_follow
-- ----------------------------
DROP TABLE IF EXISTS `lt_user_follow`;
CREATE TABLE `lt_user_follow`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '关注唯一标识符',
  `follower_id` bigint NOT NULL COMMENT '关注者',
  `followed_id` bigint NOT NULL COMMENT '被关注者',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '关注状态（0:未关注, 1:已关注）',
  `create_time` datetime NULL DEFAULT NULL COMMENT '关注时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_follower_followed`(`follower_id` ASC, `followed_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1783126963613388803 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lt_user_follow
-- ----------------------------
INSERT INTO `lt_user_follow` VALUES (1780606117544050689, 8, 7, 0, '2024-04-17 22:35:55', '2024-04-24 22:00:13');
INSERT INTO `lt_user_follow` VALUES (1783126963613388802, 7, 8, 1, '2024-04-24 21:32:52', '2024-09-22 00:16:45');

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '权限编号',
  `label` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限名称',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父权限ID',
  `parent_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父权限名称',
  `code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '授权标识符',
  `path` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路由地址',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路由名称',
  `url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '授权路径',
  `type` tinyint NULL DEFAULT NULL COMMENT '权限类型(0-目录 1-菜单 2-按钮)',
  `icon` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `order_num` int NULL DEFAULT NULL COMMENT '排序',
  `is_delete` tinyint NULL DEFAULT 0 COMMENT '是否删除(0-未删除，1-已删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 96 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (1, '系统管理', 0, '顶级菜单', 'sys:manager', '/system', 'system', '/system/system', 0, 'el-icon-menu', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (6, '用户管理', 1, '系统管理', 'sys:user', '/userList', 'userList', '/system/user/userList', 1, 'el-icon-s-custom', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (7, '新增', 6, '用户管理', 'sys:user:add', NULL, NULL, NULL, 2, 'el-icon-plus', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (8, '修改', 6, '用户管理', 'sys:user:edit', NULL, NULL, NULL, 2, 'el-icon-edit', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (9, '删除', 6, '用户管理', 'sys:user:delete', NULL, NULL, NULL, 2, 'el-icon-delete', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (10, '角色管理', 1, '系统管理', 'sys:role', '/roleList', 'roleList', '/system/role/roleList', 1, 'el-icon-s-tools', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (11, '新增', 10, '角色管理', 'sys:role:add', NULL, NULL, NULL, 2, 'el-icon-plus', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (12, '修改', 10, '角色管理', 'sys:role:edit', NULL, NULL, NULL, 2, 'el-icon-edit', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (13, '删除', 10, '角色管理', 'sys:role:delete', NULL, NULL, NULL, 2, 'el-icon-delete', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (14, '菜单管理', 1, '系统管理', 'sys:menu', '/menuList', 'menuList', '/system/menu/menuList', 1, 'el-icon-s-tools', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (15, '新增', 14, '权限管理', 'sys:menu:add', NULL, NULL, NULL, 2, 'el-icon-plus', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (16, '修改', 14, '权限管理', 'sys:menu:edit', NULL, NULL, NULL, 2, 'el-icon-edit', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (17, '删除', 14, '权限管理', 'sys:menu:delete', NULL, NULL, NULL, 2, 'el-icon-delete', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (23, '分配角色', 6, '用户管理', 'sys:user:assign', '', '', '', 2, 'el-icon-setting', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (24, '分配权限', 10, '角色管理', 'sys:role:assign', '', '', '', 2, 'el-icon-setting', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (26, '查询', 6, '用户管理', 'sys:user:select', '', '', '', 2, 'el-icon-search', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (27, '查询', 10, '角色管理', 'sys:role:select', '', '', '', 2, 'el-icon-search', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (28, '查询', 14, '菜单管理', 'sys:menu:select', '', '', '', 2, 'el-icon-search', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (51, '乐淘淘管理', 0, '顶级菜单', 'ltt:manager', '/letao', 'letao', '/letao/letao', 0, 'el-icon-menu', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (52, '商品管理', 51, '乐淘淘管理', 'ltt:product', '/productList', 'productList', '/letao/product/productList', 1, 'el-icon-s-shop', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (53, '商品分类管理', 51, '乐淘淘管理', 'ltt:product:category', '/productCategoryList', 'productCategoryList', '/letao/product-category/productCategoryList', 1, 'el-icon-setting', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (54, '留言/评论管理', 51, '乐淘淘管理', 'ltt:comment', '/commentList', 'commentList', '/letao/comment/commentList', 1, 'el-icon-s-comment', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (55, '通知/消息管理', 51, '乐淘淘管理', 'ltt:message', '/messageList', 'messageList', '/letao/message/messageList', 1, 'el-icon-message-solid', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (56, '订单管理', 51, '乐淘淘管理', 'ltt:order', '/orderList', 'orderList', '/letao/order/orderList', 1, 'el-icon-s-order', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (57, '新增', 56, '订单管理', 'ltt:order:add', NULL, NULL, NULL, 2, 'el-icon-plus', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (58, '修改', 56, '订单管理', 'ltt:order:edit', NULL, NULL, NULL, 2, 'el-icon-edit', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (59, '删除', 56, '订单管理', 'ltt:order:delete', NULL, NULL, NULL, 2, 'el-icon-delete', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (60, '查询', 56, '订单管理', 'ltt:order:select', NULL, NULL, NULL, 2, 'el-icon-search', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (61, '新增', 55, '通知/消息管理', 'ltt:message:add', NULL, NULL, NULL, 2, 'el-icon-plus', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (62, '修改', 55, '通知/消息管理', 'ltt:message:edit', NULL, NULL, NULL, 2, 'el-icon-edit', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (63, '删除', 55, '通知/消息管理', 'ltt:message:delete', NULL, NULL, NULL, 2, 'el-icon-delete', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (64, '查询', 55, '通知/消息管理', 'ltt:message:select', NULL, NULL, NULL, 2, 'el-icon-search', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (65, '新增', 54, '留言/评论管理', 'ltt:comment:add', NULL, NULL, NULL, 2, 'el-icon-plus', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (66, '修改', 54, '留言/评论管理', 'ltt:comment:edit', NULL, NULL, NULL, 2, 'el-icon-edit', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (67, '删除', 54, '留言/评论管理', 'ltt:comment:delete', NULL, NULL, NULL, 2, 'el-icon-delete', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (68, '查询', 54, '留言/评论管理', 'ltt:comment:select', NULL, NULL, NULL, 2, 'el-icon-search', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (69, '新增', 53, '商品分类管理', 'ltt:category:add', NULL, NULL, NULL, 2, 'el-icon-plus', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (70, '修改', 53, '商品分类管理', 'ltt:category:edit', NULL, NULL, NULL, 2, 'el-icon-edit', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (71, '删除', 53, '商品分类管理', 'ltt:category:delete', NULL, NULL, NULL, 2, 'el-icon-delete', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (72, '查询', 53, '商品分类管理', 'ltt:category:select', NULL, NULL, NULL, 2, 'el-icon-search', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (73, '新增', 52, '商品管理', 'ltt:product:add', NULL, NULL, NULL, 2, 'el-icon-plus', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (74, '修改', 52, '商品管理', 'ltt:product:edit', NULL, NULL, NULL, 2, 'el-icon-edit', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (75, '删除', 52, '商品管理', 'ltt:product:delete', NULL, NULL, NULL, 2, 'el-icon-delete', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (76, '查询', 52, '商品管理', 'ltt:product:select', NULL, NULL, NULL, 2, 'el-icon-search', '2022-04-25 14:40:32', '2022-04-25 14:40:32', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (77, '社区帖子管理', 51, '乐淘淘管理', 'ltt:post', '/postList', 'postList', '/letao/post/postList', 1, 'el-icon-discover', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (78, '帖子编辑', 77, '社区帖子管理', 'ltt:post:edit', '', '', '', 2, 'el-icon-circle-plus-outline', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (79, '帖子删除', 77, '社区帖子管理', 'ltt:post:delete', '', '', '', 2, 'el-icon-delete', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (80, '商品审核', 52, '商品管理', 'ltt:product:audit', '', '', '', 2, 'el-icon-circle-check', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (81, '商品推荐', 52, '商品管理', 'ltt:product:recommend', '', '', '', 2, 'el-icon-thumb', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (82, '商品下架', 52, '商品管理', 'ltt:product:soldOut', '', '', '', 2, 'el-icon-remove-outline', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (83, '帖子通过', 77, '社区帖子管理', 'ltt:post:audit', '', '', '', 2, 'el-icon-circle-check', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (84, '帖子不合格', 77, '社区帖子管理', 'ltt:post:out', '', '', '', 2, 'el-icon-remove-outline', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (85, '数据统计', 51, '乐淘淘管理', 'ltt:statistics', '/statistics', 'statistics', '/letao/statistic/overview', 1, 'el-icon-data-line', NULL, '2024-09-19 21:39:42', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (86, '乐淘淘用户管理', 51, '乐淘淘管理', 'letao:user', '/ltUserList', 'ltUserList', '/letao/user/userList', 1, 'UserFilled', NULL, '2024-09-19 15:27:48', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (87, '删除', 86, '乐淘淘用户管理', 'letao:user:delete', '', '', '', 2, 'el-icon-delete-solid', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (88, '数据字典', 1, '系统管理', 'sys:dict', '/dictList', 'dictList', '/system/dict/dictList', 1, 'el-icon-menu', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (89, '查询', 86, '乐淘淘用户管理', 'letao:user:search', '', '', '', 2, 'Search', '2024-09-19 17:40:09', '2024-09-19 18:18:02', NULL, NULL, 1);
INSERT INTO `sys_permission` VALUES (90, '修改', 86, '乐淘淘用户管理', 'letao:user:edit', '', '', '', 2, 'Edit', '2024-09-19 18:17:21', '2024-09-19 18:17:21', NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (91, '查询', 86, '乐淘淘用户管理', 'kltae', '', '', '', 2, 'AlarmClock', '2024-09-22 12:06:23', '2024-09-22 12:18:01', NULL, NULL, 1);
INSERT INTO `sys_permission` VALUES (92, '新增', 86, '乐淘淘用户管理', 'eawe', '', '', '', 2, 'ArrowLeftBold', '2024-09-22 12:16:05', '2024-09-22 12:16:13', NULL, NULL, 1);
INSERT INTO `sys_permission` VALUES (93, 'ewae', 86, '乐淘淘用户管理', 'eawe', '', '', '', 2, 'Apple', '2024-09-22 12:24:19', '2024-09-22 12:24:29', NULL, NULL, 1);
INSERT INTO `sys_permission` VALUES (94, 'ewae', 86, '乐淘淘用户管理', 'ewae', '', '', '', 2, 'Apple', '2024-09-22 12:25:52', '2024-09-22 12:26:02', NULL, NULL, 1);
INSERT INTO `sys_permission` VALUES (95, 'eaw', 86, '乐淘淘用户管理', 'ewae', '', '', '', 2, 'el-icon-phone', '2024-09-22 13:59:14', '2024-09-22 13:59:26', NULL, NULL, 1);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色编号',
  `role_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色编码',
  `role_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_delete` tinyint NULL DEFAULT 0 COMMENT '是否删除(0-未删除，1-已删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 'ROLE_SYSTEM', '超级管理员', 1, '2022-04-25 14:44:23', '2022-04-25 14:44:23', NULL, 0);
INSERT INTO `sys_role` VALUES (2, 'ROLE_SYSTEM', '系统管理员', 1, '2022-04-25 14:44:23', '2022-04-25 14:44:23', '拥有系统管理功能模块的权限', 0);
INSERT INTO `sys_role` VALUES (3, 'ROLE_RESOURCE', '乐淘淘管理员', 4, NULL, NULL, '拥有乐淘淘管理模块的功能权限', 0);
INSERT INTO `sys_role` VALUES (8, 'ewa', 'ewae', NULL, '2024-09-19 13:42:42', '2024-09-19 13:42:42', '', 0);
INSERT INTO `sys_role` VALUES (9, 'fes', 'fes', 1, NULL, '2024-09-19 13:39:26', '', 0);
INSERT INTO `sys_role` VALUES (10, 'rse', 'ydre', 1, NULL, '2024-09-22 13:07:07', '', 0);
INSERT INTO `sys_role` VALUES (11, 'dawwae', 'rse', 1, '2024-09-19 13:42:42', '2024-09-22 13:05:35', '', 1);
INSERT INTO `sys_role` VALUES (12, 'eawe', 'tdrod', 1, '2024-09-22 13:57:48', '2024-09-22 13:58:38', '', 0);

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `role_Id` bigint NOT NULL COMMENT '角色ID',
  `permission_Id` bigint NOT NULL COMMENT '权限ID'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES (2, 1);
INSERT INTO `sys_role_permission` VALUES (2, 2);
INSERT INTO `sys_role_permission` VALUES (2, 3);
INSERT INTO `sys_role_permission` VALUES (2, 4);
INSERT INTO `sys_role_permission` VALUES (2, 5);
INSERT INTO `sys_role_permission` VALUES (2, 25);
INSERT INTO `sys_role_permission` VALUES (2, 6);
INSERT INTO `sys_role_permission` VALUES (2, 7);
INSERT INTO `sys_role_permission` VALUES (2, 8);
INSERT INTO `sys_role_permission` VALUES (2, 9);
INSERT INTO `sys_role_permission` VALUES (2, 23);
INSERT INTO `sys_role_permission` VALUES (2, 26);
INSERT INTO `sys_role_permission` VALUES (2, 10);
INSERT INTO `sys_role_permission` VALUES (2, 11);
INSERT INTO `sys_role_permission` VALUES (2, 12);
INSERT INTO `sys_role_permission` VALUES (2, 13);
INSERT INTO `sys_role_permission` VALUES (2, 24);
INSERT INTO `sys_role_permission` VALUES (2, 27);
INSERT INTO `sys_role_permission` VALUES (2, 14);
INSERT INTO `sys_role_permission` VALUES (2, 15);
INSERT INTO `sys_role_permission` VALUES (2, 16);
INSERT INTO `sys_role_permission` VALUES (2, 17);
INSERT INTO `sys_role_permission` VALUES (2, 28);
INSERT INTO `sys_role_permission` VALUES (3, 51);
INSERT INTO `sys_role_permission` VALUES (3, 52);
INSERT INTO `sys_role_permission` VALUES (3, 73);
INSERT INTO `sys_role_permission` VALUES (3, 74);
INSERT INTO `sys_role_permission` VALUES (3, 75);
INSERT INTO `sys_role_permission` VALUES (3, 76);
INSERT INTO `sys_role_permission` VALUES (3, 80);
INSERT INTO `sys_role_permission` VALUES (3, 81);
INSERT INTO `sys_role_permission` VALUES (3, 82);
INSERT INTO `sys_role_permission` VALUES (3, 53);
INSERT INTO `sys_role_permission` VALUES (3, 69);
INSERT INTO `sys_role_permission` VALUES (3, 70);
INSERT INTO `sys_role_permission` VALUES (3, 71);
INSERT INTO `sys_role_permission` VALUES (3, 72);
INSERT INTO `sys_role_permission` VALUES (3, 54);
INSERT INTO `sys_role_permission` VALUES (3, 65);
INSERT INTO `sys_role_permission` VALUES (3, 66);
INSERT INTO `sys_role_permission` VALUES (3, 67);
INSERT INTO `sys_role_permission` VALUES (3, 68);
INSERT INTO `sys_role_permission` VALUES (3, 55);
INSERT INTO `sys_role_permission` VALUES (3, 61);
INSERT INTO `sys_role_permission` VALUES (3, 62);
INSERT INTO `sys_role_permission` VALUES (3, 63);
INSERT INTO `sys_role_permission` VALUES (3, 64);
INSERT INTO `sys_role_permission` VALUES (3, 56);
INSERT INTO `sys_role_permission` VALUES (3, 57);
INSERT INTO `sys_role_permission` VALUES (3, 58);
INSERT INTO `sys_role_permission` VALUES (3, 59);
INSERT INTO `sys_role_permission` VALUES (3, 60);
INSERT INTO `sys_role_permission` VALUES (3, 77);
INSERT INTO `sys_role_permission` VALUES (3, 78);
INSERT INTO `sys_role_permission` VALUES (3, 79);
INSERT INTO `sys_role_permission` VALUES (3, 83);
INSERT INTO `sys_role_permission` VALUES (3, 84);
INSERT INTO `sys_role_permission` VALUES (3, 85);
INSERT INTO `sys_role_permission` VALUES (1, 1);
INSERT INTO `sys_role_permission` VALUES (1, 6);
INSERT INTO `sys_role_permission` VALUES (1, 7);
INSERT INTO `sys_role_permission` VALUES (1, 8);
INSERT INTO `sys_role_permission` VALUES (1, 9);
INSERT INTO `sys_role_permission` VALUES (1, 23);
INSERT INTO `sys_role_permission` VALUES (1, 26);
INSERT INTO `sys_role_permission` VALUES (1, 10);
INSERT INTO `sys_role_permission` VALUES (1, 11);
INSERT INTO `sys_role_permission` VALUES (1, 12);
INSERT INTO `sys_role_permission` VALUES (1, 13);
INSERT INTO `sys_role_permission` VALUES (1, 24);
INSERT INTO `sys_role_permission` VALUES (1, 27);
INSERT INTO `sys_role_permission` VALUES (1, 14);
INSERT INTO `sys_role_permission` VALUES (1, 15);
INSERT INTO `sys_role_permission` VALUES (1, 16);
INSERT INTO `sys_role_permission` VALUES (1, 17);
INSERT INTO `sys_role_permission` VALUES (1, 28);
INSERT INTO `sys_role_permission` VALUES (1, 51);
INSERT INTO `sys_role_permission` VALUES (1, 52);
INSERT INTO `sys_role_permission` VALUES (1, 73);
INSERT INTO `sys_role_permission` VALUES (1, 74);
INSERT INTO `sys_role_permission` VALUES (1, 75);
INSERT INTO `sys_role_permission` VALUES (1, 76);
INSERT INTO `sys_role_permission` VALUES (1, 80);
INSERT INTO `sys_role_permission` VALUES (1, 81);
INSERT INTO `sys_role_permission` VALUES (1, 82);
INSERT INTO `sys_role_permission` VALUES (1, 53);
INSERT INTO `sys_role_permission` VALUES (1, 69);
INSERT INTO `sys_role_permission` VALUES (1, 70);
INSERT INTO `sys_role_permission` VALUES (1, 71);
INSERT INTO `sys_role_permission` VALUES (1, 72);
INSERT INTO `sys_role_permission` VALUES (1, 54);
INSERT INTO `sys_role_permission` VALUES (1, 65);
INSERT INTO `sys_role_permission` VALUES (1, 66);
INSERT INTO `sys_role_permission` VALUES (1, 67);
INSERT INTO `sys_role_permission` VALUES (1, 68);
INSERT INTO `sys_role_permission` VALUES (1, 55);
INSERT INTO `sys_role_permission` VALUES (1, 61);
INSERT INTO `sys_role_permission` VALUES (1, 62);
INSERT INTO `sys_role_permission` VALUES (1, 63);
INSERT INTO `sys_role_permission` VALUES (1, 64);
INSERT INTO `sys_role_permission` VALUES (1, 56);
INSERT INTO `sys_role_permission` VALUES (1, 57);
INSERT INTO `sys_role_permission` VALUES (1, 58);
INSERT INTO `sys_role_permission` VALUES (1, 59);
INSERT INTO `sys_role_permission` VALUES (1, 60);
INSERT INTO `sys_role_permission` VALUES (1, 77);
INSERT INTO `sys_role_permission` VALUES (1, 78);
INSERT INTO `sys_role_permission` VALUES (1, 79);
INSERT INTO `sys_role_permission` VALUES (1, 83);
INSERT INTO `sys_role_permission` VALUES (1, 84);
INSERT INTO `sys_role_permission` VALUES (1, 85);
INSERT INTO `sys_role_permission` VALUES (1, 86);
INSERT INTO `sys_role_permission` VALUES (1, 87);
INSERT INTO `sys_role_permission` VALUES (1, 88);
INSERT INTO `sys_role_permission` VALUES (8, 68);
INSERT INTO `sys_role_permission` VALUES (8, 64);
INSERT INTO `sys_role_permission` VALUES (8, 60);
INSERT INTO `sys_role_permission` VALUES (8, 51);
INSERT INTO `sys_role_permission` VALUES (8, 54);
INSERT INTO `sys_role_permission` VALUES (8, 55);
INSERT INTO `sys_role_permission` VALUES (8, 56);
INSERT INTO `sys_role_permission` VALUES (1, 89);
INSERT INTO `sys_role_permission` VALUES (1, 90);
INSERT INTO `sys_role_permission` VALUES (1, 91);
INSERT INTO `sys_role_permission` VALUES (1, 92);
INSERT INTO `sys_role_permission` VALUES (1, 93);
INSERT INTO `sys_role_permission` VALUES (1, 94);
INSERT INTO `sys_role_permission` VALUES (10, 85);
INSERT INTO `sys_role_permission` VALUES (10, 51);
INSERT INTO `sys_role_permission` VALUES (12, 85);
INSERT INTO `sys_role_permission` VALUES (12, 51);
INSERT INTO `sys_role_permission` VALUES (1, 95);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户编号',
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录名称(用户名)',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录密码',
  `is_account_non_expired` tinyint NOT NULL COMMENT '帐户是否过期(1-未过期，0-已过期)',
  `is_account_non_locked` tinyint NOT NULL COMMENT '帐户是否被锁定(1-未过期，0-已过期)',
  `is_credentials_non_expired` tinyint NOT NULL COMMENT '密码是否过期(1-未过期，0-已过期)',
  `is_enabled` tinyint NOT NULL COMMENT '帐户是否可用(1-可用，0-禁用)',
  `real_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `nick_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `gender` tinyint NOT NULL COMMENT '性别(0-男，1-女)',
  `phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'https://manong-authority.oss-cn-guangzhou.aliyuncs.com/avatar/default-avatar.gif' COMMENT '用户头像',
  `introduction` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '个人介绍',
  `birthday` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生日',
  `is_admin` tinyint NULL DEFAULT 0 COMMENT '是否是管理员(1-管理员)',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `is_delete` tinyint NULL DEFAULT 0 COMMENT '是否删除(0-未删除，1-已删除)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `sys_user_idx1`(`username` ASC, `real_name` ASC, `nick_name` ASC) USING BTREE,
  INDEX `sys_user_idx2`(`gender` ASC, `phone` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$TdEVQtGCkpo8L.jKjFB3/uxV5xkkDfiy0zoCa.ZS2yAXHe7H95OIC', 0, 0, 0, 0, '李明', '超级管理员', 0, '13242587415', 'liming@163.com', 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/avatar/2023/11/04/b16041b2e12241de8c76ccfcd0c24e5d.gif', NULL, NULL, 1, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (2, 'liming', '$2a$10$WwhJ8dBezfyMFIn19.ELru58K65k6N2tgewtv2sWdClKiRCjC55wG', 0, 0, 0, 0, '黎明', '黎明', 0, '13578956652', 'liming@qq.com', 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/avatar/2023/11/04/636b5e09f54c4e4b816f481ee6e83a1a.gif', '', '', 0, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (3, 'zhangsan', '$2a$10$iBQbmrAEBE5B84/U3RY7c.zhObI4aIpjl807FV4LzL/uay7arIcpu', 1, 1, 1, 1, '张三', '张三', 0, '13245678965', 'zhangsan@163.com', 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/avatar/2023/11/04/65044f82e8044c00bc7b1265cc809437.jpeg', NULL, NULL, 0, NULL, NULL, 1);
INSERT INTO `sys_user` VALUES (4, 'lisi', '$2a$10$QywHvELdRoFGiU6LKpd/X.LYpfaXETtS0pD4Nem2K3c0iMQwaZuAm', 0, 0, 0, 0, '李四', '李四', 0, '13754214568', '', 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/avatar/2023/11/04/65044f82e8044c00bc7b1265cc809437.jpeg', NULL, NULL, 0, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (5, 'wangwu', '$2a$10$O8uyPZFS9PLfR8JN.aMRi.l/YeykYYuKH.cg/HBAR.N4NJeNg8hQK', 0, 0, 0, 0, '王五', '王五', 0, '13212345678', '', 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/avatar/2023/11/04/ee3a4b3b622c4885b0a2e41e18556caf.jpg', NULL, NULL, 0, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (6, 'zhaoliu', '$2a$10$r45wkEYLHlteEr0KLI8y3.G506ylhQrEJkmGM.i2eHkcCnFvfbhCS', 0, 0, 0, 0, '赵六', '赵六', 0, '13212345676', '', 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/avatar/2023/11/04/1bad9ebcf66c454b9fc7b29855327399.jpeg', NULL, NULL, 0, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (9, 'eawe', '$2a$10$NAk62st6EQ3QS31NepumJeSQqQCBaVZi9HHtitnwhk.a0p3B8Ef1q', 0, 0, 0, 0, 'rwar', '', 0, '13579846513', '', 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/avatar/2024/09/19/9e8d421fb0f243738a6eb0c94e27595d.png', '', '', 0, NULL, '2024-09-19 18:24:53', 1);
INSERT INTO `sys_user` VALUES (10, 'eaw', '$2a$10$B32rFB1ckJ7.2y3AXZwSpO6/MVHX6RyLmzo6K4zIjkwAOTZ4XU.zq', 0, 0, 0, 0, 'yrdty', '', 0, '13678456978', '', 'https://xxsq-lean.oss-cn-shenzhen.aliyuncs.com/avatar/2024/09/22/7adde0b1b1d44fb8a8bdd924ada55d3e.png', '', '', 0, '2024-09-22 12:03:45', '2024-09-22 13:07:27', 0);
INSERT INTO `sys_user` VALUES (11, 'eawe', '$2a$10$70iX/DTfceTeJcjRTWYasuLFh150yfmg3khH0mHiIhrUmSs8rtfvC', 0, 0, 0, 0, 'tyaer', '', 0, '13645789754', '', '', '', '', 0, '2024-09-22 13:57:27', '2024-09-22 13:58:04', 0);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint NOT NULL COMMENT '用户编号',
  `role_id` bigint NOT NULL COMMENT '角色编号'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2);
INSERT INTO `sys_user_role` VALUES (4, 2);
INSERT INTO `sys_user_role` VALUES (5, 3);
INSERT INTO `sys_user_role` VALUES (6, 3);
INSERT INTO `sys_user_role` VALUES (10, 10);
INSERT INTO `sys_user_role` VALUES (11, 10);

SET FOREIGN_KEY_CHECKS = 1;
