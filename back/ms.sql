/*
Navicat MySQL Data Transfer

Source Server         : xjyc-svr2
Source Server Version : 50722
Source Host           : xjyc-svr2:3306
Source Database       : ms

Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001

Date: 2018-07-26 15:26:25
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for T_MENU
-- ----------------------------
DROP TABLE IF EXISTS `T_MENU`;
CREATE TABLE `T_MENU` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `m_code` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `m_name` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `m_icon` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '图标',
  `m_url` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `m_order` int(11) DEFAULT NULL COMMENT '菜单顺序',
  `desc` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of T_MENU
-- ----------------------------
INSERT INTO `T_MENU` VALUES ('1', 'loan', '合同', 'tree', '', null, '1', '顶层菜单需要指定绝对路径', '2018-01-30 14:58:26', null);
INSERT INTO `T_MENU` VALUES ('2', 'spv', 'SPV放款', 'money', 'loan/spv', '1', '1', null, '2018-01-30 15:06:29', null);
INSERT INTO `T_MENU` VALUES ('3', 'system', '系统设置', 'table', '', null, '2', '顶层菜单需要指定绝对路径', '2018-01-30 15:08:22', null);
INSERT INTO `T_MENU` VALUES ('4', 'user', '用户管理', 'user', 'system/user', '3', '1', null, '2018-01-30 15:08:59', null);
INSERT INTO `T_MENU` VALUES ('5', 'role', '角色管理', 'role', 'system/role', '3', '2', null, '2018-01-30 15:09:39', null);
INSERT INTO `T_MENU` VALUES ('6', 'permission', '权限管理', 'eye', 'system/permission', '3', '3', null, '2018-02-01 21:50:01', null);
INSERT INTO `T_MENU` VALUES ('7', 'menu', '菜单管理', 'tree', 'system/menu', '3', '4', null, '2018-02-01 21:50:09', null);
INSERT INTO `T_MENU` VALUES ('10', 'public', '公共模块', 'example', '', null, '1', '', '2018-02-07 14:20:40', null);
INSERT INTO `T_MENU` VALUES ('11', 'config', '系统配置', 'password', 'public/config', '10', '1', '理享派公共服务系统配置', '2018-02-07 14:33:30', null);
INSERT INTO `T_MENU` VALUES ('12', 'clientLog', '客户端日志', 'table', 'public/clientLog', '10', '2', '', '2018-03-05 13:48:12', null);
INSERT INTO `T_MENU` VALUES ('13', 'file', '文件管理', 'role', 'public/file', '10', '3', '', '2018-04-02 17:40:38', null);
INSERT INTO `T_MENU` VALUES ('14', 'user', '用户模块', 'user', '', '0', '1', '', '2018-04-03 17:35:10', null);
INSERT INTO `T_MENU` VALUES ('15', 'opLog', '操作日志', 'role', 'user/opLog', '14', '1', '', '2018-04-03 17:37:37', null);

-- ----------------------------
-- Table structure for T_PERMISSION
-- ----------------------------
DROP TABLE IF EXISTS `T_PERMISSION`;
CREATE TABLE `T_PERMISSION` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `p_code` varchar(32) DEFAULT NULL COMMENT '权限code',
  `parentId` bigint(20) DEFAULT NULL COMMENT '对应菜单',
  `p_name` varchar(32) DEFAULT NULL COMMENT '权限名',
  `desc` varchar(500) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 COMMENT='权限表';

-- ----------------------------
-- Records of T_PERMISSION
-- ----------------------------
INSERT INTO `T_PERMISSION` VALUES ('1', 'user:list', '5', '用户列表', null, '2018-01-31 14:37:54', null);
INSERT INTO `T_PERMISSION` VALUES ('2', 'user:add', '1', '新增用户', null, '2018-01-31 14:38:06', null);
INSERT INTO `T_PERMISSION` VALUES ('3', 'user:delete', '1', '删除用户', null, '2018-01-31 14:38:22', null);
INSERT INTO `T_PERMISSION` VALUES ('4', 'user:update', '1', '更新用户', null, '2018-01-31 14:38:32', null);
INSERT INTO `T_PERMISSION` VALUES ('5', 'system:list', null, '系统设置', null, '2018-02-01 11:17:53', null);
INSERT INTO `T_PERMISSION` VALUES ('6', 'role:list', '5', '角色列表', null, '2018-02-01 21:45:43', null);
INSERT INTO `T_PERMISSION` VALUES ('7', 'loan:list', null, '合同模块', null, '2018-02-01 21:46:09', null);
INSERT INTO `T_PERMISSION` VALUES ('8', 'spv:list', '7', 'spv列表', null, '2018-02-01 21:46:35', null);
INSERT INTO `T_PERMISSION` VALUES ('9', 'permission:list', '5', '权限列表', null, '2018-02-01 21:56:43', null);
INSERT INTO `T_PERMISSION` VALUES ('10', 'menu:list', '5', '菜单列表', null, '2018-02-01 21:57:00', null);
INSERT INTO `T_PERMISSION` VALUES ('11', 'permission:add', '9', '添加权限', null, '2018-02-02 13:44:48', null);
INSERT INTO `T_PERMISSION` VALUES ('12', 'spv:down', '8', 'spv放款', null, '2018-02-02 16:42:10', null);
INSERT INTO `T_PERMISSION` VALUES ('14', 'role:add', '6', '添加角色', '', '2018-02-03 23:30:37', null);
INSERT INTO `T_PERMISSION` VALUES ('15', 'menu:add', '10', '添加菜单', null, '2018-02-04 00:28:11', null);
INSERT INTO `T_PERMISSION` VALUES ('16', 'menu:update', '10', '修改菜单', null, '2018-02-04 00:28:36', null);
INSERT INTO `T_PERMISSION` VALUES ('17', 'menu:delete', '10', '删除菜单', null, '2018-02-04 00:29:04', null);
INSERT INTO `T_PERMISSION` VALUES ('18', 'role:delete', '6', '删除角色', null, '2018-02-04 00:30:06', null);
INSERT INTO `T_PERMISSION` VALUES ('19', 'role:update', '6', '修改角色', null, '2018-02-04 00:30:18', null);
INSERT INTO `T_PERMISSION` VALUES ('20', 'permission:delete', '9', '删除权限', null, '2018-02-04 00:30:55', null);
INSERT INTO `T_PERMISSION` VALUES ('21', 'permission:update', '9', '修改权限', null, '2018-02-04 00:31:05', null);
INSERT INTO `T_PERMISSION` VALUES ('22', 'public:list', '0', '公共模块', '查看公共模块菜单', '2018-02-07 15:20:33', null);
INSERT INTO `T_PERMISSION` VALUES ('23', 'config:list', '22', '系统配置列表', '', '2018-02-07 15:21:30', null);
INSERT INTO `T_PERMISSION` VALUES ('24', 'config:update', '22', '修改配置', '', '2018-02-07 16:51:18', null);
INSERT INTO `T_PERMISSION` VALUES ('25', 'clientLog:list', '22', '客户端日志列表', '', '2018-03-05 13:50:13', null);
INSERT INTO `T_PERMISSION` VALUES ('26', 'file:list', '22', '显示文件列表', '', '2018-04-02 17:43:36', null);
INSERT INTO `T_PERMISSION` VALUES ('27', 'file:upload', '26', '文件上传', '', '2018-04-02 17:44:36', null);
INSERT INTO `T_PERMISSION` VALUES ('28', 'file:delete', '26', '文件删除', '', '2018-04-02 17:45:09', null);
INSERT INTO `T_PERMISSION` VALUES ('29', 'file:download', '26', '文件下载', '', '2018-04-03 14:59:30', null);
INSERT INTO `T_PERMISSION` VALUES ('30', 'user:list', '0', '用户模块', '', '2018-04-03 17:40:03', null);
INSERT INTO `T_PERMISSION` VALUES ('31', 'opLog:list', '30', '用户操作日志列表', '', '2018-04-03 17:41:14', null);

-- ----------------------------
-- Table structure for T_ROLE
-- ----------------------------
DROP TABLE IF EXISTS `T_ROLE`;
CREATE TABLE `T_ROLE` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(32) DEFAULT NULL,
  `role_name` varchar(32) NOT NULL COMMENT '角色名',
  `status` char(1) DEFAULT '0' COMMENT '是否禁用',
  `desc` varchar(500) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `INDEX_ROLE_NAME` (`role_name`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Records of T_ROLE
-- ----------------------------
INSERT INTO `T_ROLE` VALUES ('1', 'admin', '管理员', '1', null, '2018-01-31 12:36:58', null);
INSERT INTO `T_ROLE` VALUES ('3', 'tester', '测试人员', '1', null, '2018-02-01 22:24:24', null);
INSERT INTO `T_ROLE` VALUES ('13', 'dashuaibi', '大帅比', '1', '', '2018-02-06 18:01:52', null);
INSERT INTO `T_ROLE` VALUES ('14', 'developer', '开发人员', '1', '', '2018-02-07 16:52:19', null);

-- ----------------------------
-- Table structure for T_ROLE_PERMISSION
-- ----------------------------
DROP TABLE IF EXISTS `T_ROLE_PERMISSION`;
CREATE TABLE `T_ROLE_PERMISSION` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL,
  `permission_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `INDEX_ROLE_PERMISSION_RID` (`role_id`),
  KEY `INDEX_ROLE_PERMISSION_PID` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=218 DEFAULT CHARSET=utf8 COMMENT='角色权限表';

-- ----------------------------
-- Records of T_ROLE_PERMISSION
-- ----------------------------
INSERT INTO `T_ROLE_PERMISSION` VALUES ('19', '3', '7');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('20', '3', '12');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('24', '3', '8');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('28', '13', '1');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('29', '13', '2');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('30', '13', '3');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('31', '13', '4');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('32', '13', '5');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('33', '13', '6');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('34', '13', '7');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('35', '13', '8');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('36', '13', '14');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('37', '13', '18');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('38', '13', '19');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('81', '14', '1');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('82', '14', '5');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('83', '14', '22');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('84', '14', '23');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('85', '14', '24');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('188', '1', '1');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('189', '1', '2');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('190', '1', '3');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('191', '1', '4');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('192', '1', '5');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('193', '1', '6');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('194', '1', '7');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('195', '1', '8');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('196', '1', '9');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('197', '1', '10');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('198', '1', '11');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('199', '1', '12');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('200', '1', '14');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('201', '1', '15');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('202', '1', '16');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('203', '1', '17');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('204', '1', '18');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('205', '1', '19');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('206', '1', '20');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('207', '1', '21');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('208', '1', '22');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('209', '1', '23');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('210', '1', '24');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('211', '1', '25');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('212', '1', '26');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('213', '1', '27');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('214', '1', '28');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('215', '1', '29');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('216', '1', '30');
INSERT INTO `T_ROLE_PERMISSION` VALUES ('217', '1', '31');

-- ----------------------------
-- Table structure for T_USER
-- ----------------------------
DROP TABLE IF EXISTS `T_USER`;
CREATE TABLE `T_USER` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_code` varchar(32) DEFAULT NULL,
  `username` varchar(32) DEFAULT NULL,
  `phoneno` varchar(32) DEFAULT NULL,
  `email` varchar(32) DEFAULT NULL,
  `pwd` varchar(64) DEFAULT NULL,
  `nick_name` varchar(64) DEFAULT NULL,
  `avatar` varchar(100) DEFAULT NULL COMMENT '头像',
  `status` char(1) DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL,
  `salt` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `U_INDEX_USER_PHONENO` (`phoneno`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of T_USER
-- ----------------------------
INSERT INTO `T_USER` VALUES ('1', 'admin', 'admin', '13148899469', 'liaoxudong@dafy.com', '1f3e1c35fe33f6905696e7f8e891c2d5', '大帅比', null, '1', '2018-01-30 17:13:57', null, 'admin');
INSERT INTO `T_USER` VALUES ('18', null, 'test', '18888888888', 'test@dafy.com', '03698509376aacc7c2c4c7a8e4e77ea2', '测试账号', null, '1', '2018-02-04 12:31:25', null, 'test');
INSERT INTO `T_USER` VALUES ('19', null, 'liaoxudong', '15243884137', 'k42jc@hotmail.com', '0cd76924e808cd70204bfb63bcf9f06a', '大帅比呀', null, '1', '2018-02-06 10:31:53', null, null);
INSERT INTO `T_USER` VALUES ('20', null, 'chenshenjian', '15986638227', 'chenshenjian@dafy.com', '5ec38eccab7df47ba5d25932c040290c', '陈申建', null, '1', '2018-02-07 16:53:34', null, null);
INSERT INTO `T_USER` VALUES ('21', null, '大帅比', '18711858115', '635684783@qq.com', 'da1b05c2f5f0049b54c8008fd11695fb', '大帅比', null, '1', '2018-04-13 16:51:25', null, null);
INSERT INTO `T_USER` VALUES ('22', null, '大帅比2', '18711858116', '635684783@qq.com', 'ece14b8858e14706e98c93ca35c83be1', '8fb1ae10a8b042c18a8325dc4a5bb19e', null, '0', '2018-04-13 16:57:17', null, '大帅比');
INSERT INTO `T_USER` VALUES ('24', null, '大帅比3', '18711858117', '635684783@qq.com', 'b343faa7cfd44f970dea32a6147e947c', '大帅比', null, '1', '2018-04-13 17:01:23', null, '75dc3c666bfe4674b78abfc1e9d040cb');

-- ----------------------------
-- Table structure for T_USER_ROLE
-- ----------------------------
DROP TABLE IF EXISTS `T_USER_ROLE`;
CREATE TABLE `T_USER_ROLE` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `INDEX_USER_ROLE_UID` (`user_id`),
  KEY `INDEX_USER_ROLE_RID` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='用户角色表';

-- ----------------------------
-- Records of T_USER_ROLE
-- ----------------------------
INSERT INTO `T_USER_ROLE` VALUES ('1', '1', '1');
INSERT INTO `T_USER_ROLE` VALUES ('11', '18', '3');
INSERT INTO `T_USER_ROLE` VALUES ('12', '19', '13');
INSERT INTO `T_USER_ROLE` VALUES ('13', '20', '14');
INSERT INTO `T_USER_ROLE` VALUES ('14', '21', '13');
INSERT INTO `T_USER_ROLE` VALUES ('15', '22', '13');
INSERT INTO `T_USER_ROLE` VALUES ('16', '24', '13');
