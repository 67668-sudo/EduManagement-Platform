-- ==========================================================
-- 🚀 EduManagement-Platform 数据库初始化脚本
-- 包含部门、员工、班级、学员、操作日志等核心业务表及测试数据
-- ==========================================================

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `tlias` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `tlias`;

-- ------------------------------------------------------
-- 1. 班级信息表
-- ------------------------------------------------------
DROP TABLE IF EXISTS `clazz`;
CREATE TABLE `clazz` (
                         `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                         `name` varchar(30) NOT NULL COMMENT '班级名称',
                         `room` varchar(20) DEFAULT NULL COMMENT '班级教室',
                         `begin_date` date NOT NULL COMMENT '开课时间',
                         `end_date` date NOT NULL COMMENT '结课时间',
                         `master_id` int unsigned DEFAULT NULL COMMENT '班主任ID, 关联员工表ID',
                         `subject` tinyint unsigned NOT NULL COMMENT '学科: 1-Java, 2-前端, 3-大数据, 4-Python, 5-Go, 6-嵌入式',
                         `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                         `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='班级信息表';

LOCK TABLES `clazz` WRITE;
INSERT INTO `clazz` (`id`, `name`, `room`, `begin_date`, `end_date`, `master_id`, `subject`, `create_time`, `update_time`) VALUES
                                                                                                                               (1,'JavaEE就业163期','212','2023-04-30','2023-06-29',10,1,'2023-06-01 17:08:23','2023-06-01 17:39:58'),
                                                                                                                               (4,'前端就业90期','210','2023-07-10','2024-01-20',3,2,'2023-06-01 17:45:12','2023-06-01 17:45:12'),
                                                                                                                               (5,'JavaEE就业165期','108','2023-06-15','2023-12-25',6,1,'2023-06-01 17:45:40','2023-06-01 17:45:40'),
                                                                                                                               (6,'JavaEE就业166期','105','2023-07-20','2024-02-20',20,1,'2023-06-01 17:46:10','2023-06-01 17:46:10'),
                                                                                                                               (7,'大数据就业58期','209','2023-08-01','2024-02-15',7,3,'2023-06-01 17:51:21','2023-06-01 17:51:21'),
                                                                                                                               (8,'JavaEE就业167期','325','2023-11-20','2024-05-10',36,1,'2023-11-15 11:35:46','2023-12-13 14:31:24');
UNLOCK TABLES;

-- ------------------------------------------------------
-- 2. 部门信息表
-- ------------------------------------------------------
DROP TABLE IF EXISTS `dept`;
CREATE TABLE `dept` (
                        `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                        `name` varchar(10) NOT NULL COMMENT '部门名称',
                        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                        `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部门信息表';

LOCK TABLES `dept` WRITE;
INSERT INTO `dept` (`id`, `name`, `create_time`, `update_time`) VALUES
                                                                    (1,'学工部','2023-09-25 09:47:40','2024-08-24 16:54:35'),
                                                                    (2,'教研部','2023-09-25 09:47:40','2024-08-09 15:17:04'),
                                                                    (3,'咨询部','2023-09-25 09:47:40','2024-07-30 21:26:24'),
                                                                    (16,'人事部','2024-08-20 09:50:42','2024-08-20 09:50:42'),
                                                                    (32,'财务部','2024-08-25 16:38:38','2024-08-25 16:38:38');
UNLOCK TABLES;

-- ------------------------------------------------------
-- 3. 员工信息表
-- ------------------------------------------------------
DROP TABLE IF EXISTS `emp`;
CREATE TABLE `emp` (
                       `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                       `username` varchar(20) NOT NULL COMMENT '登录用户名',
                       `password` varchar(50) DEFAULT '123456' COMMENT '密码(测试环境默认123456)',
                       `name` varchar(10) NOT NULL COMMENT '真实姓名',
                       `gender` tinyint unsigned NOT NULL COMMENT '性别: 1-男, 2-女',
                       `phone` char(11) NOT NULL COMMENT '手机号',
                       `job` tinyint unsigned DEFAULT NULL COMMENT '职位: 1-班主任, 2-讲师, 3-学工主管, 4-教研主管, 5-咨询师',
                       `salary` int unsigned DEFAULT NULL COMMENT '薪资',
                       `image` varchar(300) DEFAULT NULL COMMENT '头像URL',
                       `entry_date` date DEFAULT NULL COMMENT '入职日期',
                       `dept_id` int unsigned DEFAULT NULL COMMENT '关联部门ID',
                       `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                       `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                       PRIMARY KEY (`id`),
                       UNIQUE KEY `username` (`username`),
                       UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='员工信息表';

LOCK TABLES `emp` WRITE;
INSERT INTO `emp` (`id`, `username`, `password`, `name`, `gender`, `phone`, `job`, `salary`, `image`, `entry_date`, `dept_id`, `create_time`, `update_time`) VALUES
                                                                                                                                                                 (1,'shinaian','123456','施耐庵',1,'13309090001',4,NULL,'https://web-framework.oss-cn-hangzhou.aliyuncs.com/2023/1.jpg','2000-01-01',2,'2023-10-20 16:35:33','2023-11-16 16:11:26'),
                                                                                                                                                                 (2,'songjiang','123456','宋江',1,'13309090002',2,8600,'https://web-framework.oss-cn-hangzhou.aliyuncs.com/2023/1.jpg','2015-01-01',2,'2023-10-20 16:35:33','2023-10-20 16:35:37'),
                                                                                                                                                                 (3,'lujunyi','123456','卢俊义',1,'13309090003',2,8900,'https://web-framework.oss-cn-hangzhou.aliyuncs.com/2023/1.jpg','2008-05-01',2,'2023-10-20 16:35:33','2023-10-20 16:35:39'),
                                                                                                                                                                 (4,'wuyong','123456','吴用',1,'13309090004',2,9200,'https://web-framework.oss-cn-hangzhou.aliyuncs.com/2023/1.jpg','2007-01-01',2,'2023-10-20 16:35:33','2023-10-20 16:35:41'),
                                                                                                                                                                 (5,'gongsunsheng','123456','公孙胜',1,'13309090005',2,9500,'https://web-framework.oss-cn-hangzhou.aliyuncs.com/2023/1.jpg','2012-12-05',2,'2023-10-20 16:35:33','2023-10-20 16:35:43');
UNLOCK TABLES;

-- ------------------------------------------------------
-- 4. 员工工作经历表 (一对多)
-- ------------------------------------------------------
DROP TABLE IF EXISTS `emp_expr`;
CREATE TABLE `emp_expr` (
                            `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                            `emp_id` int unsigned DEFAULT NULL COMMENT '关联员工ID',
                            `begin` date DEFAULT NULL COMMENT '开始时间',
                            `end` date DEFAULT NULL COMMENT '结束时间',
                            `company` varchar(50) DEFAULT NULL COMMENT '公司名称',
                            `job` varchar(50) DEFAULT NULL COMMENT '担任职位',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='员工工作经历表';

LOCK TABLES `emp_expr` WRITE;
INSERT INTO `emp_expr` (`id`, `emp_id`, `begin`, `end`, `company`, `job`) VALUES
                                                                              (46,47,'2024-08-01','2024-08-03','百度','Java开发工程师'),
                                                                              (47,47,'2024-08-05','2024-08-10','字节跳动','后端架构师'),
                                                                              (48,47,'2024-07-01','2024-07-31','某科技公司','核心研发'),
                                                                              (49,30,'2024-08-02','2024-08-17','某互联网公司','Java开发');
UNLOCK TABLES;

-- ------------------------------------------------------
-- 5. 系统操作审计日志表 (AOP切面记录)
-- ------------------------------------------------------
DROP TABLE IF EXISTS `operate_log`;
CREATE TABLE `operate_log` (
                               `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                               `operate_emp_id` int unsigned DEFAULT NULL COMMENT '操作人ID',
                               `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
                               `class_name` varchar(100) DEFAULT NULL COMMENT '目标类名',
                               `method_name` varchar(100) DEFAULT NULL COMMENT '目标方法名',
                               `method_params` varchar(1000) DEFAULT NULL COMMENT '请求参数',
                               `return_value` varchar(2000) DEFAULT NULL COMMENT '返回值(JSON格式)',
                               `cost_time` int DEFAULT NULL COMMENT '耗时(ms)',
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统全局操作日志表';

LOCK TABLES `operate_log` WRITE;

INSERT INTO `operate_log` (`id`, `operate_emp_id`, `operate_time`, `class_name`, `method_name`, `method_params`, `return_value`, `cost_time`) VALUES
                                                                                                                                                  (1,1,'2024-08-24 15:33:04','org.example.controller.DeptController','delete','[20]','Result(code=1, msg=success, data=null)',41278),
                                                                                                                                                  (2,1,'2024-08-24 15:32:22','org.example.controller.DeptController','delete','[20]','Result(code=1, msg=success, data=null)',10046),
                                                                                                                                                  (3,1,'2024-08-24 15:33:37','org.example.controller.DeptController','add','[Dept(id=null, name=456, createTime=2024-08-24T15:33:37.472459700, updateTime=2024-08-24T15:33:37.472459700)]','Result(code=1, msg=success, data=null)',2596);
UNLOCK TABLES;

-- ------------------------------------------------------
-- 6. 学员信息表
-- ------------------------------------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
                           `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                           `name` varchar(10) NOT NULL COMMENT '姓名',
                           `no` char(10) NOT NULL COMMENT '学号',
                           `gender` tinyint unsigned NOT NULL COMMENT '性别: 1-男, 2-女',
                           `phone` varchar(11) NOT NULL COMMENT '手机号',
                           `id_card` char(18) NOT NULL COMMENT '身份证号',
                           `is_college` tinyint unsigned NOT NULL COMMENT '是否来自院校: 1-是, 0-否',
                           `address` varchar(100) DEFAULT NULL COMMENT '联系地址',
                           `degree` tinyint unsigned DEFAULT NULL COMMENT '最高学历: 1-初中, 2-高中, 3-大专, 4-本科, 5-硕士, 6-博士',
                           `graduation_date` date DEFAULT NULL COMMENT '毕业时间',
                           `clazz_id` int unsigned NOT NULL COMMENT '关联班级ID',
                           `violation_count` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '违纪次数',
                           `violation_score` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '违纪扣分',
                           `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                           `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `no` (`no`),
                           UNIQUE KEY `phone` (`phone`),
                           UNIQUE KEY `id_card` (`id_card`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学员信息表';

LOCK TABLES `student` WRITE;
INSERT INTO `student` (`id`, `name`, `no`, `gender`, `phone`, `id_card`, `is_college`, `address`, `degree`, `graduation_date`, `clazz_id`, `violation_count`, `violation_score`, `create_time`, `update_time`) VALUES
                                                                                                                                                                                                                   (1,'段誉','2022000001',1,'18800000001','110120000300200001',1,'北京市昌平区建材城西路1号',1,'2021-07-01',11,2,8,'2023-11-14 21:22:19','2024-08-20 09:52:34'),
                                                                                                                                                                                                                   (2,'萧峰','2022000002',1,'18800210003','110120000300200002',1,'北京市昌平区建材城西路2号',2,'2022-07-01',1,0,0,'2023-11-14 21:22:19','2023-11-14 21:22:19'),
                                                                                                                                                                                                                   (3,'虚竹','2022000003',1,'18800013001','110120000300200003',1,'北京市昌平区建材城西路3号',2,'2023-07-01',1,0,0,'2023-11-14 21:22:19','2023-11-14 21:22:19');
UNLOCK TABLES;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed by EduManagement-Platform