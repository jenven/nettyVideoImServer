/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50717
 Source Host           : localhost:3306
 Source Schema         : chat

 Target Server Type    : MySQL
 Target Server Version : 50717
 File Encoding         : 65001

 Date: 09/04/2021 16:10:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `deptid` bigint(11) NULL DEFAULT NULL COMMENT '部门',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `nickname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `sex` int(11) NULL DEFAULT NULL COMMENT '性别（0女 1男）',
  `birthday` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生日',
  `cardid` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证',
  `signature` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '签名',
  `school` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '毕业院校',
  `education` int(11) NULL DEFAULT NULL COMMENT '学历',
  `address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '现居住地址',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `remark` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '备注',
  `profilephoto` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '个人头像',
  `createdate` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `createuser` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `updatedate` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `updateuser` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES (1, 3, 1, '张三', NULL, NULL, NULL, NULL, '我的签名', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2017-11-27 15:08:41', 3, '2017-11-27 15:08:41', 3);
INSERT INTO `user_info` VALUES (2, 4, 4, '李四', NULL, NULL, NULL, NULL, 'Ta的签名', NULL, NULL, NULL, NULL, NULL, NULL, 'layui/images/0.jpg', '2017-11-27 18:00:14', 4, '2017-11-27 18:00:14', 4);
INSERT INTO `user_info` VALUES (3, 5, 4, '王五', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'layui/images/0.jpg', '2017-11-27 18:06:20', 5, '2017-11-27 18:06:20', 5);
INSERT INTO `user_info` VALUES (4, 6, 3, '赵六', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'layui/images/0.jpg', '2017-11-27 18:12:11', 6, '2017-11-27 18:12:11', 6);
INSERT INTO `user_info` VALUES (5, 7, 3, '孙七', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'layui/images/0.jpg', '2017-11-27 18:13:18', 7, '2017-11-27 18:13:18', 7);
INSERT INTO `user_info` VALUES (6, 8, 1, '周八', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'layui/images/0.jpg', '2017-11-27 18:13:58', 8, '2017-11-27 18:13:58', 8);
INSERT INTO `user_info` VALUES (7, 9, 1, '吴九', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'layui/images/0.jpg', '2017-11-27 18:14:24', 9, '2017-11-27 18:14:24', 9);

-- ----------------------------
-- Table structure for user_message
-- ----------------------------
DROP TABLE IF EXISTS `user_message`;
CREATE TABLE `user_message`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `senduser` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发送人',
  `receiveuser` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接收人',
  `groupid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '群ID',
  `isread` int(11) NULL DEFAULT NULL COMMENT '是否已读 0 未读  1 已读',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型 0 单聊消息  1 群消息',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '消息内容',
  `createuser` bigint(20) NULL DEFAULT NULL,
  `createdate` datetime(0) NULL DEFAULT NULL,
  `updatedate` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 131 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_message
-- ----------------------------
INSERT INTO `user_message` VALUES (29, 'DA0875E968961ED8E516B9209AEF424C', NULL, '0', 0, 1, '测试', NULL, '2017-11-24 10:58:33', '2017-11-24 10:58:33');
INSERT INTO `user_message` VALUES (30, 'DA0875E968961ED8E516B9209AEF424C', NULL, '0', 0, 1, '测试', NULL, '2017-11-24 10:58:56', '2017-11-24 10:58:56');
INSERT INTO `user_message` VALUES (31, '269A734E0AC76F7BE6262124BE104BCC', NULL, '0', 0, 1, '测试什么？\n', NULL, '2017-11-24 10:59:06', '2017-11-24 10:59:06');
INSERT INTO `user_message` VALUES (112, '3', '8', '', 1, 0, '你好face[微笑] ', NULL, '2017-11-29 11:34:39', '2017-11-29 11:34:39');
INSERT INTO `user_message` VALUES (113, '8', '3', '', 1, 0, '你好，在干什么呢face[疑问] ', NULL, '2017-11-29 11:35:31', '2017-11-29 11:35:31');
INSERT INTO `user_message` VALUES (114, '3', '8', '', 1, 0, 'img[/upload/img/temp/3ed0460ec-82fa-4f46-8373-ccfa1742bf89.jpg?1511926548410]', NULL, '2017-11-29 11:35:48', '2017-11-29 11:35:48');
INSERT INTO `user_message` VALUES (115, '8', '3', '', 1, 0, '你发的这个图好挫face[偷笑] ', NULL, '2017-11-29 11:36:30', '2017-11-29 11:36:30');
INSERT INTO `user_message` VALUES (116, 'F7AE5C1AD6078C8B0F032D7174D539DE', '114B45C5C0009277BD78FD0C796F0D27', '', 1, 0, '你好啊', NULL, '2020-09-02 09:34:40', '2020-09-02 09:34:40');
INSERT INTO `user_message` VALUES (117, '114B45C5C0009277BD78FD0C796F0D27', 'F7AE5C1AD6078C8B0F032D7174D539DE', '', 1, 0, '好啊', NULL, '2020-09-02 09:35:12', '2020-09-02 09:35:12');
INSERT INTO `user_message` VALUES (118, '8C2E525FA2130BE139DDABA47BF880B7', '7ee722577fae49d2b75aac2020573e2b', '', 1, 0, '1<div><br></div>', NULL, '2020-09-18 16:01:28', '2020-09-18 16:01:28');
INSERT INTO `user_message` VALUES (119, '8C2E525FA2130BE139DDABA47BF880B7', '7ee722577fae49d2b75aac2020573e2b', '', 1, 0, '32', NULL, '2020-09-18 16:01:37', '2020-09-18 16:01:37');
INSERT INTO `user_message` VALUES (120, '8C2E525FA2130BE139DDABA47BF880B7', '7ee722577fae49d2b75aac2020573e2b', '', 1, 0, '1', NULL, '2020-09-18 16:01:40', '2020-09-18 16:01:40');
INSERT INTO `user_message` VALUES (121, '8C2E525FA2130BE139DDABA47BF880B7', '7ee722577fae49d2b75aac2020573e2b', '', 1, 0, '哈哈', NULL, '2020-09-18 16:02:29', '2020-09-18 16:02:29');
INSERT INTO `user_message` VALUES (122, '8C2E525FA2130BE139DDABA47BF880B7', '7ee722577fae49d2b75aac2020573e2b', '', 1, 0, '111', NULL, '2020-09-18 16:10:05', '2020-09-18 16:10:05');
INSERT INTO `user_message` VALUES (123, '166CDC20801FA711A6FA09908AB37918', 'E6686FD77FDA9318A105ABB15A9FB16C', '', 1, 0, '123', NULL, '2021-02-05 14:44:24', '2021-02-05 14:44:24');
INSERT INTO `user_message` VALUES (124, 'E6686FD77FDA9318A105ABB15A9FB16C', '166CDC20801FA711A6FA09908AB37918', '', 1, 0, 'etrreter', NULL, '2021-02-05 14:44:37', '2021-02-05 14:44:37');
INSERT INTO `user_message` VALUES (125, '564538E365F69E5F736BB19A15E57FC5', NULL, '0', 1, 1, '324', NULL, '2021-03-27 14:11:49', '2021-03-27 14:11:49');
INSERT INTO `user_message` VALUES (126, 'A4A9A021EC24E523FBAE051DFA3AEF15', 'E2D1A6CC2A3BA73F83E29FDED3B6DA3F', '', 1, 0, '43', NULL, '2021-03-27 15:22:36', '2021-03-27 15:22:36');
INSERT INTO `user_message` VALUES (127, 'E2D1A6CC2A3BA73F83E29FDED3B6DA3F', 'A4A9A021EC24E523FBAE051DFA3AEF15', '', 1, 0, '234', NULL, '2021-03-27 15:22:42', '2021-03-27 15:22:42');
INSERT INTO `user_message` VALUES (128, '324E6924824F4D6552DF73758832315F', 'AD5E53431338C1F58E47E653D8326981', '', 1, 0, '6578', NULL, '2021-03-27 15:30:50', '2021-03-27 15:48:55');
INSERT INTO `user_message` VALUES (129, 'F328F50AC1C35BE6DA25A8FA9026CE87', '3D4BDCB59C8B20094006BAD419CDFC9D', '', 1, 0, '111', NULL, '2021-04-09 15:04:53', '2021-04-09 15:04:53');
INSERT INTO `user_message` VALUES (130, '123456789', '987654321', '', 0, 0, '111444', NULL, '2021-04-09 16:08:46', '2021-04-09 16:08:46');

SET FOREIGN_KEY_CHECKS = 1;
