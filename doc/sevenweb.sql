/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50723
 Source Host           : localhost:3306
 Source Schema         : sevenweb

 Target Server Type    : MySQL
 Target Server Version : 50723
 File Encoding         : 65001

 Date: 22/10/2020 09:35:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for daily_plan_work
-- ----------------------------
DROP TABLE IF EXISTS `daily_plan_work`;
CREATE TABLE `daily_plan_work`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '键',
  `TIME` date NOT NULL COMMENT '完成该项的日期',
  `PLAN_ID` int(11) NULL DEFAULT NULL COMMENT '对应计划的id值',
  `PLAN_PERCENT` int(11) NULL DEFAULT NULL COMMENT '对应计划在今日完成的进度',
  `PLAN_FINISH` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '今日计划是否完成',
  `STUDY_ID` int(11) NULL DEFAULT NULL COMMENT '对应的学习id',
  `STUDY_FINISH` tinyint(1) NULL DEFAULT NULL COMMENT '对应的学习是否完成',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for plan_work
-- ----------------------------
DROP TABLE IF EXISTS `plan_work`;
CREATE TABLE `plan_work`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PROJECT` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `PERCENT` int(11) NULL DEFAULT 0,
  `START_TIME` date NULL DEFAULT NULL,
  `END_TIME` date NULL DEFAULT NULL,
  `DEPENDS_ON` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `STATUS` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `PRIORITY` int(11) NULL DEFAULT NULL,
  `REMARKS` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for study_work
-- ----------------------------
DROP TABLE IF EXISTS `study_work`;
CREATE TABLE `study_work`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `PROJECT` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `START_TIME` date NOT NULL COMMENT '启动时间',
  `ONE_DAY` tinyint(1) UNSIGNED ZEROFILL NOT NULL DEFAULT 0,
  `TWO_DAY` tinyint(1) UNSIGNED ZEROFILL NOT NULL DEFAULT 0,
  `FOUR_DAY` tinyint(1) UNSIGNED ZEROFILL NOT NULL DEFAULT 0,
  `SEVEN_DAY` tinyint(1) UNSIGNED ZEROFILL NOT NULL DEFAULT 0,
  `FIFTEEN_DAY` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
