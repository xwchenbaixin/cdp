CREATE DATABASE  IF NOT EXISTS `springsecurity` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `springsecurity`;
-- MySQL dump 10.13  Distrib 8.0.16, for Win64 (x86_64)
--
-- Host: 192.168.80.242    Database: springsecurity
-- ------------------------------------------------------
-- Server version	5.7.27

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `url` varchar(100) DEFAULT NULL,
  `pid` int(11) DEFAULT NULL,
  `method` varchar(10) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
INSERT INTO `permission` VALUES (1,'STUDENT','TEST','/student/**',NULL,'get|post',1),(2,'TEACHER','TEST','/teacher/**',NULL,'get|post',1),(3,'DEAN','TEST','/dean/**',NULL,'get|post',1),(4,'所有课程','学生主界面','/student/index',NULL,'get|post',1),(5,'课程','学生课程界面','/student/course',NULL,'get|post',1),(6,'代码编辑器','学生代码编辑器','/student/codeEditor',NULL,'get|post',1),(7,'收件箱','学生收件箱','/student/inbox',NULL,'get|post',1);
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'ROLE_STUDENT',1),(2,'ROLE_TEACHER',1),(3,'ROLE_DEAN',1);
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_permission`
--

DROP TABLE IF EXISTS `role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `role_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT NULL,
  `permission_id` int(11) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_Reference_2` (`role_id`),
  CONSTRAINT `FK_Reference_2` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_permission`
--

LOCK TABLES `role_permission` WRITE;
/*!40000 ALTER TABLE `role_permission` DISABLE KEYS */;
INSERT INTO `role_permission` VALUES (1,1,1,1),(2,2,2,1);
/*!40000 ALTER TABLE `role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `sex` varchar(2) DEFAULT NULL,
  `work_no` varchar(20) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `class_id` int(11) DEFAULT NULL,
  `password` varchar(256) DEFAULT NULL,
  `avatar` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_Reference_1` (`role_id`),
  CONSTRAINT `FK_Reference_1` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=694 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'陈佰鑫','男','16204110','18160745110',1,1,1,'$2a$10$tmPnzqtDtMbW6YE3CT31YeX6J4MKR.cXJBEZ4p4zfgq5XLPKdhO2a','/images/title1.jpg'),(2,'黄川沂','男','16204111','18160745110',2,1,1,'$2a$10$tmPnzqtDtMbW6YE3CT31YeX6J4MKR.cXJBEZ4p4zfgq5XLPKdhO2a','/images/title2.jpg'),(3,'曾利平','男','16204125','18160745110',3,1,1,'$2a$10$tmPnzqtDtMbW6YE3CT31YeX6J4MKR.cXJBEZ4p4zfgq5XLPKdhO2a','/images/title3.jpg'),(4,'曾利平','男','16204126','18160745110',3,1,1,'$2a$10$tmPnzqtDtMbW6YE3CT31YeX6J4MKR.cXJBEZ4p4zfgq5XLPKdhO2a','/images/title3.jpg'),(5,'陈佰鑫','男','16204112','18160745110',1,1,1,'$2a$10$tmPnzqtDtMbW6YE3CT31YeX6J4MKR.cXJBEZ4p4zfgq5XLPKdhO2a','/images/title1.jpg'),(6,'陈佰鑫','男','16204113','18160745110',1,1,1,'$2a$10$tmPnzqtDtMbW6YE3CT31YeX6J4MKR.cXJBEZ4p4zfgq5XLPKdhO2a','/images/title1.jpg'),(7,'陈佰鑫','男','16204114','18160745110',1,1,1,'$2a$10$tmPnzqtDtMbW6YE3CT31YeX6J4MKR.cXJBEZ4p4zfgq5XLPKdhO2a','/images/title1.jpg'),(8,'陈佰鑫','男','16204115','18160745110',1,1,1,'$2a$10$tmPnzqtDtMbW6YE3CT31YeX6J4MKR.cXJBEZ4p4zfgq5XLPKdhO2a','/images/title1.jpg'),(9,'陈佰鑫','男','16204116','18160745110',1,1,1,'$2a$10$tmPnzqtDtMbW6YE3CT31YeX6J4MKR.cXJBEZ4p4zfgq5XLPKdhO2a','/images/title1.jpg'),(692,'金伟莉','男女','16204117',NULL,NULL,1,1,NULL,NULL),(693,'金伟莉二号','男','16204118',NULL,NULL,1,NULL,NULL,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-09-04 21:48:44
