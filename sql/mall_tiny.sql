/*
Navicat MySQL Data Transfer

Source Server         : 阿里云轻量应用服务器
Source Server Version : 80042
Source Host           : 8.138.199.239:3306
Source Database       : mall_tiny

Target Server Type    : MYSQL
Target Server Version : 80042
File Encoding         : 65001

Date: 2025-10-26 17:18:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for oms_order
-- ----------------------------
DROP TABLE IF EXISTS `oms_order`;
CREATE TABLE `oms_order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_sn` varchar(64) DEFAULT NULL COMMENT '订单编号',
  `member_id` bigint DEFAULT NULL COMMENT '下单用户ID',
  `status` tinyint(1) DEFAULT '0' COMMENT '订单状态：0->待付款；1->已付款；2->已发货；3->已完成；4->已关闭',
  `total_amount` decimal(10,2) DEFAULT '0.00' COMMENT '订单总金额',
  `pay_amount` decimal(10,2) DEFAULT '0.00' COMMENT '实际支付金额',
  `pay_type` tinyint(1) DEFAULT '0' COMMENT '支付方式：0->未支付；1->支付宝；2->微信；3->银行卡',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `note` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='订单表';

-- ----------------------------
-- Records of oms_order
-- ----------------------------

-- ----------------------------
-- Table structure for oms_order_address
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_address`;
CREATE TABLE `oms_order_address` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint DEFAULT NULL COMMENT '所属订单ID',
  `receiver_name` varchar(255) DEFAULT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(255) DEFAULT NULL COMMENT '收货人电话',
  `province` varchar(255) DEFAULT NULL COMMENT '省',
  `city` varchar(255) DEFAULT NULL COMMENT '市',
  `district` varchar(255) DEFAULT NULL COMMENT '区',
  `detail_address` varchar(255) DEFAULT NULL COMMENT '详细地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='订单收货地址表';

-- ----------------------------
-- Records of oms_order_address
-- ----------------------------

-- ----------------------------
-- Table structure for oms_order_item
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_item`;
CREATE TABLE `oms_order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint DEFAULT NULL COMMENT '所属订单ID',
  `product_id` bigint DEFAULT NULL COMMENT '商品ID',
  `product_name` varchar(64) DEFAULT NULL COMMENT '商品名称',
  `product_image` varchar(500) DEFAULT NULL COMMENT '商品图片',
  `product_price` decimal(10,2) DEFAULT '0.00' COMMENT '商品单价',
  `quantity` int DEFAULT '1' COMMENT '购买数量',
  `total_price` decimal(10,2) DEFAULT '0.00' COMMENT '小计金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='订单商品项表';

-- ----------------------------
-- Records of oms_order_item
-- ----------------------------

-- ----------------------------
-- Table structure for oms_order_payment
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_payment`;
CREATE TABLE `oms_order_payment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint DEFAULT NULL COMMENT '所属订单ID',
  `pay_amount` decimal(10,2) DEFAULT '0.00' COMMENT '支付金额',
  `pay_type` tinyint(1) DEFAULT '0' COMMENT '支付方式：0->未支付；1->支付宝；2->微信；3->银行卡',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='订单支付记录表';

-- ----------------------------
-- Records of oms_order_payment
-- ----------------------------

-- ----------------------------
-- Table structure for oms_shopping_cart
-- ----------------------------
DROP TABLE IF EXISTS `oms_shopping_cart`;
CREATE TABLE `oms_shopping_cart` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `member_id` bigint DEFAULT NULL COMMENT '用户ID',
  `product_id` bigint DEFAULT NULL COMMENT '商品ID',
  `product_name` varchar(64) DEFAULT NULL COMMENT '商品名称',
  `product_image` varchar(500) DEFAULT NULL COMMENT '商品图片',
  `product_price` decimal(10,2) DEFAULT '0.00' COMMENT '商品单价',
  `quantity` int DEFAULT '1' COMMENT '购买数量',
  `create_time` datetime DEFAULT NULL COMMENT '加入购物车时间',
  PRIMARY KEY (`id`),
  KEY `member_id` (`member_id`,`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='购物车表';

-- ----------------------------
-- Records of oms_shopping_cart
-- ----------------------------

-- ----------------------------
-- Table structure for pms_product
-- ----------------------------
DROP TABLE IF EXISTS `pms_product`;
CREATE TABLE `pms_product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category_id` bigint DEFAULT NULL COMMENT '商品种类ID',
  `name` varchar(64) DEFAULT NULL COMMENT '商品名称',
  `image` varchar(500) DEFAULT NULL COMMENT '商品图片',
  `tags` varchar(255) DEFAULT NULL COMMENT '商品标签,json格式,例如[“新品”,“精选”]',
  `price` decimal(10,2) DEFAULT NULL COMMENT '商品价格',
  `description` varchar(255) DEFAULT NULL COMMENT '商品描述',
  `status` tinyint(1) DEFAULT NULL COMMENT '商品状态：0->下架；1->上架',
  `sort` int DEFAULT NULL COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 COMMENT='商品表';

-- ----------------------------
-- Records of pms_product
-- ----------------------------
INSERT INTO `pms_product` VALUES ('2', '3', '测试商品', 'http://8.138.199.239:9000/my-mall-bucket/images/product/2_1761236936170.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=devuser%2F20251023%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20251023T162856Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=844dfe6908dc4a10dc5ac3e3a9448c593795c4bc990d4a2a998c1d3c1c21eca8', '[]', '1999.00', '123', '1', '0', '2025-10-24 00:28:50', '2025-10-24 00:28:50');

-- ----------------------------
-- Table structure for pms_product_category
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_category`;
CREATE TABLE `pms_product_category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT NULL COMMENT '商品种类名称',
  `image` varchar(500) DEFAULT NULL COMMENT '商品种类图片',
  `description` varchar(255) DEFAULT NULL COMMENT '商品种类描述',
  `status` tinyint(1) DEFAULT NULL COMMENT '商品种类状态：0->下架；1->上架',
  `sort` int DEFAULT NULL COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 COMMENT='商品种类表';

-- ----------------------------
-- Records of pms_product_category
-- ----------------------------
INSERT INTO `pms_product_category` VALUES ('3', '其他', 'http://8.138.199.239:9000/my-mall-bucket/images/category/3_1761236903846.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=devuser%2F20251023%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20251023T162824Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=2825001775b693a291ae8fab3a3e8102a72bfd5917386e9b629a11396038a975', 'others', '1', '0', '2025-10-24 00:28:18', '2025-10-24 00:28:18');

-- ----------------------------
-- Table structure for ums_admin
-- ----------------------------
DROP TABLE IF EXISTS `ums_admin`;
CREATE TABLE `ums_admin` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(64) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `icon` varchar(500) DEFAULT NULL COMMENT '头像',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `nick_name` varchar(200) DEFAULT NULL COMMENT '昵称',
  `note` varchar(500) DEFAULT NULL COMMENT '备注信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `status` int DEFAULT '1' COMMENT '帐号启用状态：0->禁用；1->启用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb3 COMMENT='后台用户表';

-- ----------------------------
-- Records of ums_admin
-- ----------------------------
INSERT INTO `ums_admin` VALUES ('1', 'test', '$2a$10$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180607/timg.jpg', 'test@qq.com', '测试账号', null, '2018-09-29 13:55:30', '2018-09-29 13:55:39', '1');
INSERT INTO `ums_admin` VALUES ('3', 'admin', '$2a$10$.E1FokumK5GIXWgKlg.Hc.i/0/2.qdAwYFL1zc5QHdyzpXOr38RZO', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180607/timg.jpg', 'admin@163.com', '系统管理员', '系统管理员', '2018-10-08 13:32:47', '2019-04-20 12:45:16', '1');
INSERT INTO `ums_admin` VALUES ('4', 'macro', '$2a$10$Bx4jZPR7GhEpIQfefDQtVeS58GfT5n6mxs/b4nLLK65eMFa16topa', 'string', 'macro@qq.com', 'macro', 'macro专用', '2019-10-06 15:53:51', '2020-02-03 14:55:55', '1');
INSERT INTO `ums_admin` VALUES ('6', 'productAdmin', '$2a$10$6/.J.p.6Bhn7ic4GfoB5D.pGd7xSiD1a9M6ht6yO0fxzlKJPjRAGm', null, 'product@qq.com', '商品管理员', '只有商品权限', '2020-02-07 16:15:08', null, '1');
INSERT INTO `ums_admin` VALUES ('7', 'orderAdmin', '$2a$10$UqEhA9UZXjHHA3B.L9wNG.6aerrBjC6WHTtbv1FdvYPUI.7lkL6E.', null, 'order@qq.com', '订单管理员', '只有订单管理权限', '2020-02-07 16:15:50', null, '1');
INSERT INTO `ums_admin` VALUES ('10', 'ceshi', '$2a$10$RaaNo9CC0RSms8mc/gJpCuOWndDT4pHH0u5XgZdAAYFs1Uq4sOPRi', 'http://8.138.199.239:9000/my-mall-bucket/images/icon/10_1761223833769.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=devuser%2F20251023%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20251023T125033Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=c31dc277dca58d87b15e6fa023b430f57a0c003c6d60d7fe696fb2c9ca04b5ab', 'ceshi@qq.com', 'ceshi', null, '2020-03-13 16:23:30', null, '1');
INSERT INTO `ums_admin` VALUES ('16', 'admin2', '$2a$10$5R/PkpSQcg6.EGXhAAIYrOhfJZujmb8RYLu0qDQb9Q7/V962plalO', 'http://8.138.199.239:9000/my-mall-bucket/images/icon/16_1761209923442.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=devuser%2F20251023%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20251023T085843Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=0dc33dc364c6e53efda1ba2a9eec84b398eeaf0455ea289d38da4f911186bd74', '395773508@qq.com', 'admin2', null, '2025-10-23 16:58:02', null, '1');

-- ----------------------------
-- Table structure for ums_admin_login_log
-- ----------------------------
DROP TABLE IF EXISTS `ums_admin_login_log`;
CREATE TABLE `ums_admin_login_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `admin_id` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `ip` varchar(64) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `user_agent` varchar(100) DEFAULT NULL COMMENT '浏览器登录类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=289 DEFAULT CHARSET=utf8mb3 COMMENT='后台用户登录日志表';

-- ----------------------------
-- Records of ums_admin_login_log
-- ----------------------------
INSERT INTO `ums_admin_login_log` VALUES ('285', '3', '2020-08-24 14:05:21', '0:0:0:0:0:0:0:1', null, null);
INSERT INTO `ums_admin_login_log` VALUES ('286', '10', '2020-08-24 14:05:39', '0:0:0:0:0:0:0:1', null, null);
INSERT INTO `ums_admin_login_log` VALUES ('287', '16', '2025-10-23 16:58:11', '0:0:0:0:0:0:0:1', null, null);
INSERT INTO `ums_admin_login_log` VALUES ('288', '16', '2025-10-23 22:32:03', '0:0:0:0:0:0:0:1', null, null);

-- ----------------------------
-- Table structure for ums_admin_role_relation
-- ----------------------------
DROP TABLE IF EXISTS `ums_admin_role_relation`;
CREATE TABLE `ums_admin_role_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `admin_id` bigint DEFAULT NULL,
  `role_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb3 COMMENT='后台用户和角色关系表';

-- ----------------------------
-- Records of ums_admin_role_relation
-- ----------------------------
INSERT INTO `ums_admin_role_relation` VALUES ('26', '3', '5');
INSERT INTO `ums_admin_role_relation` VALUES ('27', '6', '1');
INSERT INTO `ums_admin_role_relation` VALUES ('28', '7', '2');
INSERT INTO `ums_admin_role_relation` VALUES ('29', '1', '5');
INSERT INTO `ums_admin_role_relation` VALUES ('30', '4', '5');
INSERT INTO `ums_admin_role_relation` VALUES ('31', '8', '5');
INSERT INTO `ums_admin_role_relation` VALUES ('34', '12', '6');
INSERT INTO `ums_admin_role_relation` VALUES ('38', '13', '5');
INSERT INTO `ums_admin_role_relation` VALUES ('39', '10', '8');
INSERT INTO `ums_admin_role_relation` VALUES ('41', '16', '5');
INSERT INTO `ums_admin_role_relation` VALUES ('42', '16', '1');
INSERT INTO `ums_admin_role_relation` VALUES ('43', '16', '2');
INSERT INTO `ums_admin_role_relation` VALUES ('44', '16', '8');

-- ----------------------------
-- Table structure for ums_menu
-- ----------------------------
DROP TABLE IF EXISTS `ums_menu`;
CREATE TABLE `ums_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint DEFAULT NULL COMMENT '父级ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `title` varchar(100) DEFAULT NULL COMMENT '菜单名称',
  `level` int DEFAULT NULL COMMENT '菜单级数',
  `sort` int DEFAULT NULL COMMENT '菜单排序',
  `name` varchar(100) DEFAULT NULL COMMENT '前端名称',
  `icon` varchar(200) DEFAULT NULL COMMENT '前端图标',
  `hidden` int DEFAULT NULL COMMENT '前端隐藏',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb3 COMMENT='后台菜单表';

-- ----------------------------
-- Records of ums_menu
-- ----------------------------
INSERT INTO `ums_menu` VALUES ('1', '0', '2020-02-02 14:50:36', '商品', '0', '0', 'pms', 'product', '1');
INSERT INTO `ums_menu` VALUES ('2', '1', '2020-02-02 14:51:50', '商品列表', '1', '0', 'product', 'product-list', '0');
INSERT INTO `ums_menu` VALUES ('3', '1', '2020-02-02 14:52:44', '添加商品', '1', '0', 'addProduct', 'product-add', '0');
INSERT INTO `ums_menu` VALUES ('4', '1', '2020-02-02 14:53:51', '商品分类', '1', '0', 'productCate', 'product-cate', '0');
INSERT INTO `ums_menu` VALUES ('5', '1', '2020-02-02 14:54:51', '商品类型', '1', '0', 'productAttr', 'product-attr', '0');
INSERT INTO `ums_menu` VALUES ('6', '1', '2020-02-02 14:56:29', '品牌管理', '1', '0', 'brand', 'product-brand', '0');
INSERT INTO `ums_menu` VALUES ('7', '0', '2020-02-02 16:54:07', '订单', '0', '0', 'oms', 'order', '1');
INSERT INTO `ums_menu` VALUES ('8', '7', '2020-02-02 16:55:18', '订单列表', '1', '0', 'order', 'product-list', '0');
INSERT INTO `ums_menu` VALUES ('9', '7', '2020-02-02 16:56:46', '订单设置', '1', '0', 'orderSetting', 'order-setting', '0');
INSERT INTO `ums_menu` VALUES ('10', '7', '2020-02-02 16:57:39', '退货申请处理', '1', '0', 'returnApply', 'order-return', '0');
INSERT INTO `ums_menu` VALUES ('11', '7', '2020-02-02 16:59:40', '退货原因设置', '1', '0', 'returnReason', 'order-return-reason', '0');
INSERT INTO `ums_menu` VALUES ('12', '0', '2020-02-04 16:18:00', '营销', '0', '0', 'sms', 'sms', '1');
INSERT INTO `ums_menu` VALUES ('13', '12', '2020-02-04 16:19:22', '秒杀活动列表', '1', '0', 'flash', 'sms-flash', '0');
INSERT INTO `ums_menu` VALUES ('14', '12', '2020-02-04 16:20:16', '优惠券列表', '1', '0', 'coupon', 'sms-coupon', '0');
INSERT INTO `ums_menu` VALUES ('16', '12', '2020-02-07 16:22:38', '品牌推荐', '1', '0', 'homeBrand', 'product-brand', '0');
INSERT INTO `ums_menu` VALUES ('17', '12', '2020-02-07 16:23:14', '新品推荐', '1', '0', 'homeNew', 'sms-new', '0');
INSERT INTO `ums_menu` VALUES ('18', '12', '2020-02-07 16:26:38', '人气推荐', '1', '0', 'homeHot', 'sms-hot', '0');
INSERT INTO `ums_menu` VALUES ('19', '12', '2020-02-07 16:28:16', '专题推荐', '1', '0', 'homeSubject', 'sms-subject', '0');
INSERT INTO `ums_menu` VALUES ('20', '12', '2020-02-07 16:28:42', '广告列表', '1', '0', 'homeAdvertise', 'sms-ad', '0');
INSERT INTO `ums_menu` VALUES ('21', '0', '2020-02-07 16:29:13', '权限', '0', '0', 'ums', 'ums', '0');
INSERT INTO `ums_menu` VALUES ('22', '21', '2020-02-07 16:29:51', '用户列表', '1', '0', 'admin', 'ums-admin', '0');
INSERT INTO `ums_menu` VALUES ('23', '21', '2020-02-07 16:30:13', '角色列表', '1', '0', 'role', 'ums-role', '0');
INSERT INTO `ums_menu` VALUES ('24', '21', '2020-02-07 16:30:53', '菜单列表', '1', '0', 'menu', 'ums-menu', '0');
INSERT INTO `ums_menu` VALUES ('25', '21', '2020-02-07 16:31:13', '资源列表', '1', '0', 'resource', 'ums-resource', '0');

-- ----------------------------
-- Table structure for ums_resource
-- ----------------------------
DROP TABLE IF EXISTS `ums_resource`;
CREATE TABLE `ums_resource` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `name` varchar(200) DEFAULT NULL COMMENT '资源名称',
  `url` varchar(200) DEFAULT NULL COMMENT '资源URL',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `category_id` bigint DEFAULT NULL COMMENT '资源分类ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb3 COMMENT='后台资源表';

-- ----------------------------
-- Records of ums_resource
-- ----------------------------
INSERT INTO `ums_resource` VALUES ('25', '2020-02-07 16:47:34', '后台用户管理', '/admin/**', '', '4');
INSERT INTO `ums_resource` VALUES ('26', '2020-02-07 16:48:24', '后台用户角色管理', '/role/**', '', '4');
INSERT INTO `ums_resource` VALUES ('27', '2020-02-07 16:48:48', '后台菜单管理', '/menu/**', '', '4');
INSERT INTO `ums_resource` VALUES ('28', '2020-02-07 16:49:18', '后台资源分类管理', '/resourceCategory/**', '', '4');
INSERT INTO `ums_resource` VALUES ('29', '2020-02-07 16:49:45', '后台资源管理', '/resource/**', '', '4');
INSERT INTO `ums_resource` VALUES ('32', '2025-10-23 17:35:11', '商品-创建', '/pms/product/create', '创建商品', '9');
INSERT INTO `ums_resource` VALUES ('33', '2025-10-23 17:35:11', '商品-更新', '/pms/product/update/**', '更新商品（含路径变量）', '9');
INSERT INTO `ums_resource` VALUES ('34', '2025-10-23 17:35:11', '商品-批量删除', '/pms/product/delete', '批量删除商品', '9');
INSERT INTO `ums_resource` VALUES ('35', '2025-10-23 17:35:11', '商品-分页列表', '/pms/product/list', '按名称/分类/状态/价格区间查询', '9');
INSERT INTO `ums_resource` VALUES ('36', '2025-10-23 17:35:11', '商品-详情', '/pms/product/item/**', '获取商品详情（含路径变量）', '9');
INSERT INTO `ums_resource` VALUES ('37', '2025-10-23 17:35:11', '商品-修改状态', '/pms/product/updateStatus/**', '修改商品状态（含路径变量）', '9');
INSERT INTO `ums_resource` VALUES ('38', '2025-10-23 17:35:11', '商品-按分类分页', '/pms/product/listByCategory/**', '按分类分页查询（含路径变量）', '9');
INSERT INTO `ums_resource` VALUES ('39', '2025-10-23 17:35:11', '商品-全部列表', '/pms/product/listAll', '获取所有商品', '9');
INSERT INTO `ums_resource` VALUES ('40', '2025-10-23 17:35:11', '商品分类-创建', '/pms/productCategory/create', '创建商品分类', '9');
INSERT INTO `ums_resource` VALUES ('41', '2025-10-23 17:35:11', '商品分类-更新', '/pms/productCategory/update/**', '更新商品分类（含路径变量）', '9');
INSERT INTO `ums_resource` VALUES ('42', '2025-10-23 17:35:11', '商品分类-批量删除', '/pms/productCategory/delete', '批量删除商品分类', '9');
INSERT INTO `ums_resource` VALUES ('43', '2025-10-23 17:35:12', '商品分类-全部列表', '/pms/productCategory/listAll', '获取所有商品分类', '9');
INSERT INTO `ums_resource` VALUES ('44', '2025-10-23 17:35:12', '商品分类-分页列表', '/pms/productCategory/list', '按名称分页查询', '9');
INSERT INTO `ums_resource` VALUES ('45', '2025-10-23 17:35:12', '商品分类-详情', '/pms/productCategory/item/**', '获取分类详情（含路径变量）', '9');
INSERT INTO `ums_resource` VALUES ('46', '2025-10-23 17:35:12', '商品分类-修改状态', '/pms/productCategory/updateStatus/**', '修改分类状态（含路径变量）', '9');

-- ----------------------------
-- Table structure for ums_resource_category
-- ----------------------------
DROP TABLE IF EXISTS `ums_resource_category`;
CREATE TABLE `ums_resource_category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `name` varchar(200) DEFAULT NULL COMMENT '分类名称',
  `sort` int DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb3 COMMENT='资源分类表';

-- ----------------------------
-- Records of ums_resource_category
-- ----------------------------
INSERT INTO `ums_resource_category` VALUES ('4', '2020-02-05 10:23:04', '权限模块', '0');
INSERT INTO `ums_resource_category` VALUES ('9', '2025-10-23 17:35:10', '商品管理模块（PMS）', '0');

-- ----------------------------
-- Table structure for ums_role
-- ----------------------------
DROP TABLE IF EXISTS `ums_role`;
CREATE TABLE `ums_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `admin_count` int DEFAULT NULL COMMENT '后台用户数量',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `status` int DEFAULT '1' COMMENT '启用状态：0->禁用；1->启用',
  `sort` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3 COMMENT='后台用户角色表';

-- ----------------------------
-- Records of ums_role
-- ----------------------------
INSERT INTO `ums_role` VALUES ('1', '商品管理员', '只能查看及操作商品', '0', '2020-02-03 16:50:37', '1', '0');
INSERT INTO `ums_role` VALUES ('2', '订单管理员', '只能查看及操作订单', '0', '2018-09-30 15:53:45', '1', '0');
INSERT INTO `ums_role` VALUES ('5', '超级管理员', '拥有所有查看和操作功能', '0', '2020-02-02 15:11:05', '1', '0');
INSERT INTO `ums_role` VALUES ('8', '权限管理员', '用于权限模块所有操作功能', '0', '2020-08-24 10:57:35', '1', '0');

-- ----------------------------
-- Table structure for ums_role_menu_relation
-- ----------------------------
DROP TABLE IF EXISTS `ums_role_menu_relation`;
CREATE TABLE `ums_role_menu_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=111 DEFAULT CHARSET=utf8mb3 COMMENT='后台角色菜单关系表';

-- ----------------------------
-- Records of ums_role_menu_relation
-- ----------------------------
INSERT INTO `ums_role_menu_relation` VALUES ('33', '1', '1');
INSERT INTO `ums_role_menu_relation` VALUES ('34', '1', '2');
INSERT INTO `ums_role_menu_relation` VALUES ('35', '1', '3');
INSERT INTO `ums_role_menu_relation` VALUES ('36', '1', '4');
INSERT INTO `ums_role_menu_relation` VALUES ('37', '1', '5');
INSERT INTO `ums_role_menu_relation` VALUES ('38', '1', '6');
INSERT INTO `ums_role_menu_relation` VALUES ('53', '2', '7');
INSERT INTO `ums_role_menu_relation` VALUES ('54', '2', '8');
INSERT INTO `ums_role_menu_relation` VALUES ('55', '2', '9');
INSERT INTO `ums_role_menu_relation` VALUES ('56', '2', '10');
INSERT INTO `ums_role_menu_relation` VALUES ('57', '2', '11');
INSERT INTO `ums_role_menu_relation` VALUES ('72', '5', '1');
INSERT INTO `ums_role_menu_relation` VALUES ('73', '5', '2');
INSERT INTO `ums_role_menu_relation` VALUES ('74', '5', '3');
INSERT INTO `ums_role_menu_relation` VALUES ('75', '5', '4');
INSERT INTO `ums_role_menu_relation` VALUES ('76', '5', '5');
INSERT INTO `ums_role_menu_relation` VALUES ('77', '5', '6');
INSERT INTO `ums_role_menu_relation` VALUES ('78', '5', '7');
INSERT INTO `ums_role_menu_relation` VALUES ('79', '5', '8');
INSERT INTO `ums_role_menu_relation` VALUES ('80', '5', '9');
INSERT INTO `ums_role_menu_relation` VALUES ('81', '5', '10');
INSERT INTO `ums_role_menu_relation` VALUES ('82', '5', '11');
INSERT INTO `ums_role_menu_relation` VALUES ('83', '5', '12');
INSERT INTO `ums_role_menu_relation` VALUES ('84', '5', '13');
INSERT INTO `ums_role_menu_relation` VALUES ('85', '5', '14');
INSERT INTO `ums_role_menu_relation` VALUES ('86', '5', '16');
INSERT INTO `ums_role_menu_relation` VALUES ('87', '5', '17');
INSERT INTO `ums_role_menu_relation` VALUES ('88', '5', '18');
INSERT INTO `ums_role_menu_relation` VALUES ('89', '5', '19');
INSERT INTO `ums_role_menu_relation` VALUES ('90', '5', '20');
INSERT INTO `ums_role_menu_relation` VALUES ('91', '5', '21');
INSERT INTO `ums_role_menu_relation` VALUES ('92', '5', '22');
INSERT INTO `ums_role_menu_relation` VALUES ('93', '5', '23');
INSERT INTO `ums_role_menu_relation` VALUES ('94', '5', '24');
INSERT INTO `ums_role_menu_relation` VALUES ('95', '5', '25');
INSERT INTO `ums_role_menu_relation` VALUES ('96', '6', '21');
INSERT INTO `ums_role_menu_relation` VALUES ('97', '6', '22');
INSERT INTO `ums_role_menu_relation` VALUES ('98', '6', '23');
INSERT INTO `ums_role_menu_relation` VALUES ('99', '6', '24');
INSERT INTO `ums_role_menu_relation` VALUES ('100', '6', '25');
INSERT INTO `ums_role_menu_relation` VALUES ('101', '7', '21');
INSERT INTO `ums_role_menu_relation` VALUES ('102', '7', '22');
INSERT INTO `ums_role_menu_relation` VALUES ('103', '7', '23');
INSERT INTO `ums_role_menu_relation` VALUES ('104', '7', '24');
INSERT INTO `ums_role_menu_relation` VALUES ('105', '7', '25');
INSERT INTO `ums_role_menu_relation` VALUES ('106', '8', '21');
INSERT INTO `ums_role_menu_relation` VALUES ('107', '8', '22');
INSERT INTO `ums_role_menu_relation` VALUES ('108', '8', '23');
INSERT INTO `ums_role_menu_relation` VALUES ('109', '8', '24');
INSERT INTO `ums_role_menu_relation` VALUES ('110', '8', '25');

-- ----------------------------
-- Table structure for ums_role_resource_relation
-- ----------------------------
DROP TABLE IF EXISTS `ums_role_resource_relation`;
CREATE TABLE `ums_role_resource_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint DEFAULT NULL COMMENT '角色ID',
  `resource_id` bigint DEFAULT NULL COMMENT '资源ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=234 DEFAULT CHARSET=utf8mb3 COMMENT='后台角色资源关系表';

-- ----------------------------
-- Records of ums_role_resource_relation
-- ----------------------------
INSERT INTO `ums_role_resource_relation` VALUES ('103', '2', '8');
INSERT INTO `ums_role_resource_relation` VALUES ('104', '2', '9');
INSERT INTO `ums_role_resource_relation` VALUES ('105', '2', '10');
INSERT INTO `ums_role_resource_relation` VALUES ('106', '2', '11');
INSERT INTO `ums_role_resource_relation` VALUES ('107', '2', '12');
INSERT INTO `ums_role_resource_relation` VALUES ('170', '1', '1');
INSERT INTO `ums_role_resource_relation` VALUES ('171', '1', '2');
INSERT INTO `ums_role_resource_relation` VALUES ('172', '1', '3');
INSERT INTO `ums_role_resource_relation` VALUES ('173', '1', '4');
INSERT INTO `ums_role_resource_relation` VALUES ('174', '1', '5');
INSERT INTO `ums_role_resource_relation` VALUES ('175', '1', '6');
INSERT INTO `ums_role_resource_relation` VALUES ('176', '1', '23');
INSERT INTO `ums_role_resource_relation` VALUES ('177', '1', '24');
INSERT INTO `ums_role_resource_relation` VALUES ('178', '6', '25');
INSERT INTO `ums_role_resource_relation` VALUES ('179', '6', '26');
INSERT INTO `ums_role_resource_relation` VALUES ('180', '6', '27');
INSERT INTO `ums_role_resource_relation` VALUES ('181', '6', '28');
INSERT INTO `ums_role_resource_relation` VALUES ('182', '6', '29');
INSERT INTO `ums_role_resource_relation` VALUES ('205', '7', '25');
INSERT INTO `ums_role_resource_relation` VALUES ('206', '7', '26');
INSERT INTO `ums_role_resource_relation` VALUES ('207', '7', '27');
INSERT INTO `ums_role_resource_relation` VALUES ('208', '7', '28');
INSERT INTO `ums_role_resource_relation` VALUES ('209', '7', '29');
INSERT INTO `ums_role_resource_relation` VALUES ('210', '7', '31');
INSERT INTO `ums_role_resource_relation` VALUES ('211', '8', '25');
INSERT INTO `ums_role_resource_relation` VALUES ('212', '8', '26');
INSERT INTO `ums_role_resource_relation` VALUES ('213', '8', '27');
INSERT INTO `ums_role_resource_relation` VALUES ('214', '8', '28');
INSERT INTO `ums_role_resource_relation` VALUES ('215', '8', '29');
INSERT INTO `ums_role_resource_relation` VALUES ('216', '5', '25');
INSERT INTO `ums_role_resource_relation` VALUES ('217', '5', '26');
INSERT INTO `ums_role_resource_relation` VALUES ('218', '5', '27');
INSERT INTO `ums_role_resource_relation` VALUES ('219', '5', '28');
INSERT INTO `ums_role_resource_relation` VALUES ('220', '5', '29');
INSERT INTO `ums_role_resource_relation` VALUES ('221', '5', '32');
INSERT INTO `ums_role_resource_relation` VALUES ('222', '5', '33');
INSERT INTO `ums_role_resource_relation` VALUES ('223', '5', '34');
INSERT INTO `ums_role_resource_relation` VALUES ('224', '5', '35');
INSERT INTO `ums_role_resource_relation` VALUES ('225', '5', '36');
INSERT INTO `ums_role_resource_relation` VALUES ('226', '5', '46');
INSERT INTO `ums_role_resource_relation` VALUES ('227', '5', '45');
INSERT INTO `ums_role_resource_relation` VALUES ('228', '5', '44');
INSERT INTO `ums_role_resource_relation` VALUES ('229', '5', '43');
INSERT INTO `ums_role_resource_relation` VALUES ('230', '5', '42');
INSERT INTO `ums_role_resource_relation` VALUES ('231', '5', '41');
INSERT INTO `ums_role_resource_relation` VALUES ('232', '5', '40');
INSERT INTO `ums_role_resource_relation` VALUES ('233', '5', '39');
