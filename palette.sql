/*
 Navicat Premium Data Transfer

 Source Server         : TS
 Source Server Type    : MySQL
 Source Server Version : 80012
 Source Host           : localhost:3306
 Source Schema         : sky_take_out

 Target Server Type    : MySQL
 Target Server Version : 80012
 File Encoding         : 65001

 Date: 26/04/2024 20:09:27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address_book
-- ----------------------------
DROP TABLE IF EXISTS `address_book`;
CREATE TABLE `address_book`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `user_id` bigint(20) NOT NULL COMMENT 'User id',
  `consignee` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'Recipient',
  `sex` varchar(2) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'Gender',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'Phone number',
  `province_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Province code',
  `province_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Province name',
  `city_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'City code',
  `city_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'City name',
  `district_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'District code',
  `district_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'District name',
  `detail` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Detailed address',
  `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Label',
  `is_default` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'Default 0 no 1 yes',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'Address book' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `type` int(11) NULL DEFAULT NULL COMMENT 'Type   1 dish category 2 setmeal category',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'Category name',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT 'Sort order',
  `status` int(11) NULL DEFAULT NULL COMMENT 'Category status 0: disabled, 1: enabled',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT 'Create time',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT 'Update time',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT 'Created by',
  `update_user` bigint(20) NULL DEFAULT NULL COMMENT 'Updated by',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_category_name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'Dish and setmeal category' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dish
-- ----------------------------
DROP TABLE IF EXISTS `dish`;
CREATE TABLE `dish`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'Dish name',
  `category_id` bigint(20) NOT NULL COMMENT 'Dish category id',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT 'Dish price',
  `image` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'Image',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'Description',
  `status` int(11) NULL DEFAULT 1 COMMENT '0 discontinued 1 on sale',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT 'Create time',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT 'Update time',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT 'Created by',
  `update_user` bigint(20) NULL DEFAULT NULL COMMENT 'Updated by',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_dish_name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 72 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'Dish' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dish_flavor
-- ----------------------------
DROP TABLE IF EXISTS `dish_flavor`;
CREATE TABLE `dish_flavor`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `dish_id` bigint(20) NOT NULL COMMENT 'Dish',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'Flavor name',
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'Flavor data list',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 120 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'Dish flavor relation table' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'Name',
  `username` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'Username',
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'Password',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'Phone number',
  `sex` varchar(2) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'Gender',
  `id_number` varchar(18) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'ID number',
  `status` int(11) NOT NULL DEFAULT 1 COMMENT 'Status 0: disabled, 1: enabled',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT 'Create time',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT 'Update time',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT 'Created by',
  `update_user` bigint(20) NULL DEFAULT NULL COMMENT 'Updated by',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'Employee info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_detail
-- ----------------------------
DROP TABLE IF EXISTS `order_detail`;
CREATE TABLE `order_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'Name',
  `image` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'Image',
  `order_id` bigint(20) NOT NULL COMMENT 'Order id',
  `dish_id` bigint(20) NULL DEFAULT NULL COMMENT 'Dish id',
  `setmeal_id` bigint(20) NULL DEFAULT NULL COMMENT 'Setmeal id',
  `dish_flavor` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'flavor',
  `number` int(11) NOT NULL DEFAULT 1 COMMENT 'Quantity',
  `amount` decimal(10, 2) NOT NULL COMMENT 'Amount',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'Order detail table' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `number` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'Order number',
  `status` int(11) NOT NULL DEFAULT 1 COMMENT 'Order status 1 pending payment 2 pending confirmation 3 confirmed 4 delivering 5 completed 6 cancelled 7 refunded',
  `user_id` bigint(20) NOT NULL COMMENT 'Ordering user',
  `address_book_id` bigint(20) NOT NULL COMMENT 'Address id',
  `order_time` datetime(0) NOT NULL COMMENT 'Order time',
  `checkout_time` datetime(0) NULL DEFAULT NULL COMMENT 'Checkout time',
  `pay_method` int(11) NOT NULL DEFAULT 1 COMMENT 'Payment method 1 WeChat, 2 Alipay',
  `pay_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT 'Payment status 0 unpaid 1 paid 2 refunded',
  `amount` decimal(10, 2) NOT NULL COMMENT 'Amount received',
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'Remark',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'Phone number',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'Address',
  `user_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'User name',
  `consignee` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'Recipient',
  `cancel_reason` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'Order cancellation reason',
  `rejection_reason` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'Order rejection reason',
  `cancel_time` datetime(0) NULL DEFAULT NULL COMMENT 'Order cancellation time',
  `estimated_delivery_time` datetime(0) NULL DEFAULT NULL COMMENT 'Estimated delivery time',
  `delivery_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT 'Delivery status  1 deliver immediately  0 select a specific time',
  `delivery_time` datetime(0) NULL DEFAULT NULL COMMENT 'Delivery time',
  `pack_amount` int(11) NULL DEFAULT NULL COMMENT 'Packing fee',
  `tableware_number` int(11) NULL DEFAULT NULL COMMENT 'Tableware quantity',
  `tableware_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT 'Tableware quantity status  1 provide based on order quantity  0 select a specific quantity',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'Order table' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for setmeal
-- ----------------------------
DROP TABLE IF EXISTS `setmeal`;
CREATE TABLE `setmeal`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `category_id` bigint(20) NOT NULL COMMENT 'Dish category id',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'Setmeal name',
  `price` decimal(10, 2) NOT NULL COMMENT 'Setmeal price',
  `status` int(11) NULL DEFAULT 1 COMMENT 'Sale status 0: discontinued 1: on sale',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'Description',
  `image` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'Image',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT 'Create time',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT 'Update time',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT 'Created by',
  `update_user` bigint(20) NULL DEFAULT NULL COMMENT 'Updated by',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_setmeal_name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'Setmeal' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for setmeal_dish
-- ----------------------------
DROP TABLE IF EXISTS `setmeal_dish`;
CREATE TABLE `setmeal_dish`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `setmeal_id` bigint(20) NULL DEFAULT NULL COMMENT 'Setmeal id',
  `dish_id` bigint(20) NULL DEFAULT NULL COMMENT 'Dish id',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'Dish name (redundant field)',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT 'Dish unit price (redundant field)',
  `copies` int(11) NULL DEFAULT NULL COMMENT 'Dish copies',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'Setmeal-dish relation' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for shopping_cart
-- ----------------------------
DROP TABLE IF EXISTS `shopping_cart`;
CREATE TABLE `shopping_cart`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'Item name',
  `image` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'Image',
  `user_id` bigint(20) NOT NULL COMMENT 'Primary key',
  `dish_id` bigint(20) NULL DEFAULT NULL COMMENT 'Dish id',
  `setmeal_id` bigint(20) NULL DEFAULT NULL COMMENT 'Setmeal id',
  `dish_flavor` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'flavor',
  `number` int(11) NOT NULL DEFAULT 1 COMMENT 'Quantity',
  `amount` decimal(10, 2) NOT NULL COMMENT 'Amount',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT 'Create time',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'Shopping cart' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `openid` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'WeChat user unique identifier',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'Name',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'Phone number',
  `sex` varchar(2) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'Gender',
  `id_number` varchar(18) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'ID number',
  `avatar` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'Avatar',
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'User info' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
