-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
-- Host: localhost    Database: specialcookdb
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Billing`
--

DROP TABLE IF EXISTS `Billing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Billing` (
  `billing_id` int NOT NULL AUTO_INCREMENT,
  `order_id` int DEFAULT NULL,
  `customer_id` int DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `payment_status` enum('Pending','Paid') DEFAULT 'Pending',
  PRIMARY KEY (`billing_id`),
  KEY `order_id` (`order_id`),
  KEY `billing_ibfk_2` (`customer_id`),
  CONSTRAINT `billing_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `Orders` (`order_id`),
  CONSTRAINT `billing_ibfk_2` FOREIGN KEY (`customer_id`) REFERENCES `Customers` (`customer_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Billing`
--

LOCK TABLES `Billing` WRITE;
/*!40000 ALTER TABLE `Billing` DISABLE KEYS */;
INSERT INTO `Billing` VALUES (3,3,3,200.00,'Paid'),(4,4,4,120.30,'Pending'),(5,5,5,99.99,'Paid'),(6,1,1,100.50,'Pending');
/*!40000 ALTER TABLE `Billing` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Chefs`
--

DROP TABLE IF EXISTS `Chefs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Chefs` (
  `chef_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `specialization` text,
  PRIMARY KEY (`chef_id`)
) ENGINE=InnoDB AUTO_INCREMENT=225 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Chefs`
--

LOCK TABLES `Chefs` WRITE;
/*!40000 ALTER TABLE `Chefs` DISABLE KEYS */;
INSERT INTO `Chefs` VALUES 
(1,'Chef Laila','Pastries and Baking'),
(3,'Chef Tom','Grilled Foods'),
(4,'Chef Fatima','Middle Eastern Dishes'),
(5,'Chef Robert','Healthy Cooking'),
(6,'Chef Ayman','Italian Cuisine'),
(7,'Chef Ayman','Italian Cuisine'),
(8,'Chef Ayman','Italian Cuisine'),
(9,'Chef Ayman','Italian Cuisine'),
(10,'Chef Ayman','Italian Cuisine'),
(11,'Chef Ayman','Italian Cuisine'),
(12,'Chef Ayman','Italian Cuisine'),
(13,'Chef Ayman','Italian Cuisine'),
(14,'Chef Ayman','Italian Cuisine'),
(15,'Chef Ayman','Italian Cuisine'),
(16,'Chef Ayman','Italian Cuisine'),
(17,'Chef Ayman','Italian Cuisine'),
(18,'Chef Ayman','Italian Cuisine'),
(19,'Chef Ayman','Italian Cuisine'),
(20,'Chef Ayman','Italian Cuisine'),
(21,'Chef Ayman','Italian Cuisine'),
(22,'Chef Ayman','Italian Cuisine'),
(23,'Chef Ayman','Italian Cuisine'),
(24,'Chef Ayman','Italian Cuisine'),
(25,'Chef Ayman','Italian Cuisine'),
(26,'Chef Ayman','Italian Cuisine'),
(27,'Chef Ayman','Italian Cuisine'),
(28,'Chef Ayman','Italian Cuisine'),
(29,'Chef Ayman','Italian Cuisine'),
(30,'Chef Ayman','Italian Cuisine'),
(31,'Chef Ayman','Italian Cuisine'),
(32,'Chef Ayman','Italian Cuisine'),
(33,'Chef Ayman','Italian Cuisine'),
(34,'Chef Ayman','Italian Cuisine'),
(35,'Chef Ayman','Italian Cuisine'),
(36,'Chef Ayman','Italian Cuisine'),
(37,'Chef Ayman','Italian Cuisine'),
(38,'Chef Ayman','Italian Cuisine'),
(39,'Chef Ayman','Italian Cuisine'),
(40,'Chef Ayman','Italian Cuisine'),
(41,'Chef Ayman','Italian Cuisine'),
(42,'Chef Ayman','Italian Cuisine'),
(43,'Chef Ayman','Italian Cuisine'),
(44,'Chef Ayman','Italian Cuisine'),
(45,'Chef Ayman','Italian Cuisine'),
(46,'Chef Ayman','Italian Cuisine'),
(47,'Chef Ayman','Italian Cuisine'),
(48,'Chef Ayman','Italian Cuisine'),
(49,'Chef Ayman','Italian Cuisine'),
(50,'Chef Ayman','Italian Cuisine'),
(51,'Chef Ayman','Italian Cuisine'),
(52,'Chef Ayman','Italian Cuisine'),
(53,'Chef Ayman','Italian Cuisine'),
(54,'Chef Ayman','Italian Cuisine'),
(55,'Chef Ayman','Italian Cuisine'),
(56,'Chef Ayman','Italian Cuisine'),
(57,'Chef Ayman','Italian Cuisine'),
(58,'Chef Ayman','Italian Cuisine'),
(59,'Chef Ayman','Italian Cuisine'),
(60,'Chef Ayman','Italian Cuisine'),
(61,'Chef Ayman','Italian Cuisine'),
(62,'Chef Ayman','Italian Cuisine'),
(63,'Chef Ayman','Italian Cuisine'),
(64,'Chef Ayman','Italian Cuisine'),
(65,'Chef Ayman','Italian Cuisine'),
(66,'Chef Ayman','Italian Cuisine'),
(67,'Chef Ayman','Italian Cuisine'),
(68,'Chef Ayman','Italian Cuisine'),
(69,'Chef Ayman','Italian Cuisine'),
(70,'Chef Ayman','Italian Cuisine'),
(71,'Chef Ayman','Italian Cuisine'),
(72,'Chef Ayman','Italian Cuisine'),
(73,'Chef Ayman','Italian Cuisine'),
(74,'Chef Ayman','Italian Cuisine'),
(75,'Chef Ayman','Italian Cuisine'),
(76,'Chef Ayman','Italian Cuisine'),
(77,'Chef Ayman','Italian Cuisine'),
(78,'Chef Ayman','Italian Cuisine'),
(79,'Chef Ayman','Italian Cuisine'),
(80,'Chef Ayman','Italian Cuisine'),
(81,'Chef Ayman','Italian Cuisine'),
(82,'Chef Ayman','Italian Cuisine'),
(83,'Chef Ayman','Italian Cuisine'),
(84,'Chef Ayman','Italian Cuisine'),
(85,'Chef Ayman','Italian Cuisine'),
(86,'Chef Ayman','Italian Cuisine'),
(87,'Chef Ayman','Italian Cuisine'),
(88,'Chef Ayman','Italian Cuisine'),
(89,'Chef Ayman','Italian Cuisine'),
(90,'Chef Ayman','Italian Cuisine'),
(91,'Chef Ayman','Italian Cuisine'),
(92,'Chef Ayman','Italian Cuisine'),
(93,'Chef Ayman','Italian Cuisine'),
(94,'Chef Ayman','Italian Cuisine'),
(95,'Chef Ayman','Italian Cuisine'),
(96,'Chef Ayman','Italian Cuisine'),
(97,'Chef Ayman','Italian Cuisine'),
(98,'Chef Ayman','Italian Cuisine'),
(99,'Chef Ayman','Italian Cuisine'),
(100,'Chef Ayman','Italian Cuisine'),
(101,'Chef Ayman','Italian Cuisine'),
(102,'Chef Ayman','Italian Cuisine'),
(103,'Chef Ayman','Italian Cuisine'),
(104,'Chef Ayman','Italian Cuisine'),
(105,'Chef Ayman','Italian Cuisine'),
(106,'Chef Ayman','Italian Cuisine'),
(107,'Chef Ayman','Italian Cuisine'),
(108,'Chef Ayman','Italian Cuisine'),
(109,'Chef Ayman','Italian Cuisine'),
(110,'Chef Ayman','Italian Cuisine'),
(111,'Chef Ayman','Italian Cuisine'),
(112,'Chef Ayman','Italian Cuisine'),
(113,'Chef Ayman','Italian Cuisine'),
(114,'Chef Ayman','Italian Cuisine'),
(115,'Chef Ayman','Italian Cuisine'),
(116,'Chef Ayman','Italian Cuisine'),
(117,'Chef Ayman','Italian Cuisine'),
(118,'Chef Ayman','Italian Cuisine'),
(119,'Chef Ayman','Italian Cuisine'),
(120,'Chef Ayman','Italian Cuisine'),
(121,'Chef Ayman','Italian Cuisine'),
(122,'Chef Ayman','Italian Cuisine'),
(123,'Chef Ayman','Italian Cuisine'),
(124,'Chef Ayman','Italian Cuisine'),
(125,'Chef Ayman','Italian Cuisine'),
(126,'Chef Ayman','Italian Cuisine'),
(127,'Chef Ayman','Italian Cuisine'),
(128,'Chef Ayman','Italian Cuisine'),
(129,'Chef Ayman','Italian Cuisine'),
(130,'Chef Ayman','Italian Cuisine'),
(131,'Chef Ayman','Italian Cuisine'),
(132,'Chef Ayman','Italian Cuisine'),
(133,'Chef Ayman','Italian Cuisine'),
(134,'Chef Ayman','Italian Cuisine'),
(135,'Chef Ayman','Italian Cuisine'),
(136,'Chef Ayman','Italian Cuisine'),
(137,'Chef Ayman','Italian Cuisine'),
(138,'Chef Ayman','Italian Cuisine'),
(139,'Chef Ayman','Italian Cuisine'),
(140,'Chef Ayman','Italian Cuisine'),
(141,'Chef Ayman','Italian Cuisine'),
(142,'Chef Ayman','Italian Cuisine'),
(143,'Chef Ayman','Italian Cuisine'),
(144,'Chef Ayman','Italian Cuisine'),
(145,'Chef Ayman','Italian Cuisine'),
(146,'Chef Ayman','Italian Cuisine'),
(147,'Chef Ayman','Italian Cuisine'),
(148,'Chef Ayman','Italian Cuisine'),
(149,'Chef Ayman','Italian Cuisine'),
(150,'Chef Ayman','Italian Cuisine'),
(151,'Chef Ayman','Italian Cuisine'),
(152,'Chef Ayman','Italian Cuisine'),
(153,'Chef Ayman','Italian Cuisine'),
(154,'Chef Ayman','Italian Cuisine'),
(155,'Chef Ayman','Italian Cuisine'),
(156,'Chef Ayman','Italian Cuisine'),
(157,'Chef Ayman','Italian Cuisine'),
(158,'Chef Ayman','Italian Cuisine'),
(159,'Chef Ayman','Italian Cuisine'),
(160,'Chef Ayman','Italian Cuisine'),
(161,'Chef Ayman','Italian Cuisine'),
(162,'Chef Ayman','Italian Cuisine'),
(163,'Chef Ayman','Italian Cuisine'),
(164,'Chef Ayman','Italian Cuisine'),
(165,'Chef Ayman','Italian Cuisine'),
(166,'Chef Ayman','Italian Cuisine'),
(167,'Chef Ayman','Italian Cuisine'),
(168,'Chef Ayman','Italian Cuisine'),
(169,'Chef Ayman','Italian Cuisine'),
(170,'Chef Ayman','Italian Cuisine'),
(171,'Chef Ayman','Italian Cuisine'),
(172,'Chef Ayman','Italian Cuisine'),
(173,'Chef Ayman','Italian Cuisine'),
(174,'Chef Ayman','Italian Cuisine'),
(175,'Chef Ayman','Italian Cuisine'),
(176,'Chef Ayman','Italian Cuisine'),
(177,'Chef Ayman','Italian Cuisine'),
(178,'Chef Ayman','Italian Cuisine'),
(179,'Chef Ayman','Italian Cuisine'),
(180,'Chef Ayman','Italian Cuisine'),
(181,'Chef Ayman','Italian Cuisine'),
(182,'Chef Ayman','Italian Cuisine'),
(183,'Chef Ayman','Italian Cuisine'),
(184,'Chef Ayman','Italian Cuisine'),
(185,'Chef Ayman','Italian Cuisine'),
(186,'Chef Ayman','Italian Cuisine'),
(187,'Chef Ayman','Italian Cuisine'),
(188,'Chef Ayman','Italian Cuisine'),
(189,'Chef Ayman','Italian Cuisine'),
(190,'Chef Ayman','Italian Cuisine'),
(191,'Chef Ayman','Italian Cuisine'),
(192,'Chef Ayman','Italian Cuisine'),
(193,'Chef Ayman','Italian Cuisine'),
(194,'Chef Ayman','Italian Cuisine'),
(195,'Chef Ayman','Italian Cuisine'),
(196,'Chef Ayman','Italian Cuisine'),
(197,'Chef Ayman','Italian Cuisine'),
(198,'Chef Ayman','Italian Cuisine'),
(199,'Chef Ayman','Italian Cuisine'),
(200,'Chef Ayman','Italian Cuisine'),
(201,'Chef Ayman','Italian Cuisine'),
(202,'Chef Ayman','Italian Cuisine'),
(203,'Chef Ayman','Italian Cuisine'),
(204,'Chef Ayman','Italian Cuisine'),
(205,'Chef Ayman','Italian Cuisine'),
(206,'Chef Ayman','Italian Cuisine'),
(207,'Chef Ayman','Italian Cuisine'),
(208,'Chef Ayman','Italian Cuisine'),
(209,'Chef Ayman','Italian Cuisine'),
(210,'Chef Ayman','Italian Cuisine'),
(211,'Chef Ayman','Italian Cuisine'),
(212,'Chef Ayman','Italian Cuisine'),
(213,'Chef Ayman','Italian Cuisine'),
(214,'Chef Ayman','Italian Cuisine'),
(215,'Chef Ayman','Italian Cuisine'),
(216,'Chef Ayman','Italian Cuisine'),
(217,'Chef Ayman','Italian Cuisine'),
(218,'Chef Ayman','Italian Cuisine'),
(219,'Chef Ayman','Italian Cuisine'),
(220,'Chef Ayman','Italian Cuisine'),
(221,'Chef Ayman','Italian Cuisine'),
(222,'Chef Ayman','Italian Cuisine'),
(223,'Chef Ayman','Italian Cuisine'),
(224,'Chef Ayman','Italian Cuisine');
/*!40000 ALTER TABLE `Chefs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Customers`
--

DROP TABLE IF EXISTS `Customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Customers` (
  `customer_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `dietary_preferences` text,
  `allergies` text,
  PRIMARY KEY (`customer_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=233 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Customers`
--

LOCK TABLES `Customers` WRITE;
/*!40000 ALTER TABLE `Customers` DISABLE KEYS */;
INSERT INTO `Customers` VALUES 
(1,'John Smith','${RANDOM_EMAIL2}','9876543210','Vegan','Dairy'),
(3,'joseph','joseph@gmail.com','05916667181','Vegan','Nuts'),
(4,'Mary Smith','mary@example.com','3334445555','Gluten-Free','Gluten'),
(5,'Omar Khalid','omar@example.com','1112223333','None','Shellfish'),
(6,'John Doe','jo2hn@example.com','1234567890','Vegan','None'),
(8,'Customer A','a@example.com','1234567890','None','None'),
(9,'Customer B','b@example.com','1234567890','None','None'),
(10,'Duplicate','duplicate@example.com','1234567890','None','None'),
(16,'John Doe','jo33hn@example.com','1234567890','Vegan','None'),
(21,'Jane Doe','jane@example.com','0987654321','Vegetarian','Peanuts'),
(25,'${RANDOM_NAME1}','${RANDOM_EMAIL1}','${RANDOM_PHONE1}','${RANDOM_DIET1}','${RANDOM_ALLERGY1}'),
(28,'Duplicate','${RANDOM_EMAIL3}','1234567890','None','None'),
(30,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(31,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(32,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(33,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(34,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(35,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(36,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(37,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(38,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(39,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(40,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(41,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(42,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(43,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(44,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(45,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(46,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(47,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(48,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(49,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(50,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(51,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(52,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(53,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(54,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(55,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(56,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(57,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(58,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(59,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(60,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(61,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(62,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(63,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(64,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(65,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(66,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(67,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(68,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(69,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(70,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(71,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(72,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(73,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(74,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(75,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(76,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(77,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(78,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(79,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(80,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(81,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(82,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(83,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(84,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(85,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(86,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(87,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(88,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(89,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(90,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(91,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(92,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(93,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(94,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(95,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(96,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(97,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(98,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(99,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(100,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(101,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(102,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(103,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(104,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(105,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(106,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(107,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(108,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(109,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(110,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(111,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(112,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(113,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(114,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(115,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(116,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(117,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(118,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(119,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(120,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(121,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(122,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(123,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(124,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(125,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(126,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(127,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(128,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(129,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(130,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(131,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(132,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(133,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(134,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(135,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(136,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(137,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(138,'hehe','a@b.com','3432424','Vegan','None'),
(139,'mustafa','haji@a.com','347487321','Vegan','None'),
(140,'mustafa al hajji','hajji@mustafa.com','3424278823','Vegan','None'),
(141,'gerald','gerald@ford.com','12015019001','Mediterranean','None'),
(142,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(143,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(144,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(145,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(146,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(147,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(148,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(149,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(150,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(151,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(152,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(153,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(154,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(155,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(156,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(157,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(158,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(159,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(160,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(161,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(162,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(163,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(164,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(165,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(166,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(167,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(168,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(169,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(170,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(171,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(172,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(173,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(174,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(175,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(176,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(177,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(178,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(179,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(180,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(181,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(182,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(183,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(184,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(185,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(186,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(187,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(188,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(189,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(190,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(191,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(192,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(193,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(194,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(195,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(196,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(197,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(198,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(199,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(200,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(201,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(202,'lidl von walmart','lidl@joe.com','1234599123','Vegan','None'),
(203,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(204,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(205,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(206,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(207,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(208,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(209,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(210,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(211,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(212,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(213,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(214,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(215,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(216,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(217,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(218,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(219,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(220,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(221,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(222,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(223,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(224,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(225,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(226,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(227,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(228,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(229,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(230,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(231,'John Doe',NULL,'1234567890','Vegetarian','Peanuts'),
(232,'John Doe',NULL,'1234567890','Vegetarian','Peanuts');
/*!40000 ALTER TABLE `Customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Ingredients`
--

DROP TABLE IF EXISTS `Ingredients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Ingredients` (
  `ingredient_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `stock_quantity` int NOT NULL,
  `unit` varchar(50) DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`ingredient_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Ingredients`
--

LOCK TABLES `Ingredients` WRITE;
/*!40000 ALTER TABLE `Ingredients` DISABLE KEYS */;
INSERT INTO `Ingredients` VALUES 
(2,'Olive Oil',20,'liters',10.00),
(3,'Chicken Breast',30,'kg',5.00),
(4,'Cheese',25,'kg',8.50),
(5,'Flour',100,'kg',1.20),
(6,'Tomato',50,'kg',5.00);
/*!40000 ALTER TABLE `Ingredients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Inventory`
--

DROP TABLE IF EXISTS `Inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Inventory` (
  `inventory_id` int NOT NULL AUTO_INCREMENT,
  `ingredient_id` int DEFAULT NULL,
  `supplier_id` int DEFAULT NULL,
  `stock_level` int DEFAULT NULL,
  `last_restocked` datetime DEFAULT NULL,
  PRIMARY KEY (`inventory_id`),
  KEY `ingredient_id` (`ingredient_id`),
  KEY `supplier_id` (`supplier_id`),
  CONSTRAINT `inventory_ibfk_1` FOREIGN KEY (`ingredient_id`) REFERENCES `Ingredients` (`ingredient_id`),
  CONSTRAINT `inventory_ibfk_2` FOREIGN KEY (`supplier_id`) REFERENCES `Suppliers` (`supplier_id`)
) ENGINE=InnoDB AUTO_INCREMENT=181 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Inventory`
--

LOCK TABLES `Inventory` WRITE;
/*!40000 ALTER TABLE `Inventory` DISABLE KEYS */;
INSERT INTO `Inventory` VALUES 
(3,3,3,30,'2025-02-14 22:54:05'),
(4,4,4,25,'2025-02-14 22:54:05'),
(5,5,5,100,'2025-02-14 22:54:05');
/*!40000 ALTER TABLE `Inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `KitchenManagers`
--

DROP TABLE IF EXISTS `KitchenManagers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `KitchenManagers` (
  `manager_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `contact_info` text,
  PRIMARY KEY (`manager_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `KitchenManagers`
--

LOCK TABLES `KitchenManagers` WRITE;
/*!40000 ALTER TABLE `KitchenManagers` DISABLE KEYS */;
INSERT INTO `KitchenManagers` VALUES (2,'John Doe','john.doe@example.com');
/*!40000 ALTER TABLE `KitchenManagers` ENABLE KEYS */;
UNLOCK TABLES;

-- Table structure for table `meal_ingredients`

DROP TABLE IF EXISTS `meal_ingredients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meal_ingredients` (
  `meal_id` int NOT NULL,
  `ingredient_id` int NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`meal_id`,`ingredient_id`),
  KEY `ingredient_id` (`ingredient_id`),
  CONSTRAINT `meal_ingredients_ibfk_1` FOREIGN KEY (`meal_id`) REFERENCES `meals` (`meal_id`) ON DELETE CASCADE,
  CONSTRAINT `meal_ingredients_ibfk_2` FOREIGN KEY (`ingredient_id`) REFERENCES `Ingredients` (`ingredient_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Dumping data for table `meal_ingredients`

LOCK TABLES `meal_ingredients` WRITE;
/*!40000 ALTER TABLE `meal_ingredients` DISABLE KEYS */;
INSERT INTO `meal_ingredients` VALUES 
(1,2,2),(1,3,1),(1,6,2),
(2,2,1),(2,5,1),(2,6,2),
(4,2,1),(4,6,1),
(7,3,5),(7,6,3);
/*!40000 ALTER TABLE `meal_ingredients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meals`
--

DROP TABLE IF EXISTS `meals`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meals` (
  `meal_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` text,
  `price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`meal_id`)
) ENGINE=InnoDB AUTO_INCREMENT=107 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meals`
--

LOCK TABLES `meals` WRITE;
/*!40000 ALTER TABLE `meals` DISABLE KEYS */;
INSERT INTO `meals` VALUES 
(1,'Chicken Salad','Grilled chicken with mixed greens and vinaigrette',12.50),
(2,'Pasta Primavera','Pasta with seasonal vegetables and tomato sauce',10.00),
(4,'Custom les','special',15.00),
(7,'jemapple zaki','very yumm',40.00),
(101,'Spaghetti','Delicious pasta',12.50),
(102,'Veggie Pizza','Cheesy veggie pizza',11.00),
(104,'Burger','Beef burger',9.50),
(105,'Fries','Crispy fries',4.50);
/*!40000 ALTER TABLE `meals` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Notifications`
--

DROP TABLE IF EXISTS `Notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Notifications` (
  `notification_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `message` text,
  `timestamp` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`notification_id`),
  KEY `notifications_ibfk_1` (`user_id`),
  CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `Customers` (`customer_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=340 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Notifications`
--

LOCK TABLES `Notifications` WRITE;
/*!40000 ALTER TABLE `Notifications` DISABLE KEYS */;
INSERT INTO `Notifications` VALUES 
(3,3,'Your order has been shipped.','2025-02-14 22:54:05'),
(4,4,'New promotion available for gluten-free items.','2025-02-14 22:54:05'),
(5,5,'Your loyalty points have been updated.','2025-02-14 22:54:05'),
(6,1,'Order confirmed','2025-02-15 12:12:07'),
(7,1,'Order confirmed','2025-02-15 12:13:55'),
(8,1,'Order confirmed','2025-02-15 12:14:37'),
(9,1,'Notification 1','2025-02-15 12:14:37'),
(10,1,'Notification 2','2025-02-15 12:14:37'),
(11,1,'Order confirmed','2025-02-15 12:15:19'),
(12,1,'Notification 1','2025-02-15 12:15:19'),
(13,1,'Notification 2','2025-02-15 12:15:19'),
(14,1,'Order confirmed','2025-02-15 12:15:37'),
(15,1,'Notification 1','2025-02-15 12:15:37'),
(16,1,'Notification 2','2025-02-15 12:15:37'),
(17,1,'Order confirmed','2025-02-15 12:28:13'),
(18,1,'Notification 1','2025-02-15 12:28:14'),
(19,1,'Notification 2','2025-02-15 12:28:14'),
(20,1,'Order confirmed','2025-02-15 12:34:07'),
(21,1,'Notification 1','2025-02-15 12:34:07'),
(22,1,'Notification 2','2025-02-15 12:34:07'),
(23,1,'Order confirmed','2025-02-15 12:34:32'),
(24,1,'Notification 1','2025-02-15 12:34:32'),
(25,1,'Notification 2','2025-02-15 12:34:32'),
(26,1,'Order confirmed','2025-02-15 12:38:42'),
(27,1,'Notification 1','2025-02-15 12:38:42'),
(28,1,'Notification 2','2025-02-15 12:38:42'),
(29,1,'Order confirmed','2025-02-15 12:39:10'),
(30,1,'Notification 1','2025-02-15 12:39:10'),
(31,1,'Notification 2','2025-02-15 12:39:10'),
(32,1,'Order confirmed','2025-02-15 12:39:41'),
(33,1,'Notification 1','2025-02-15 12:39:41'),
(34,1,'Notification 2','2025-02-15 12:39:41'),
(35,1,'Order confirmed','2025-02-15 12:40:09'),
(36,1,'Notification 1','2025-02-15 12:40:09'),
(37,1,'Notification 2','2025-02-15 12:40:09'),
(38,1,'Order confirmed','2025-02-15 12:40:31'),
(39,1,'Notification 1','2025-02-15 12:40:32'),
(40,1,'Notification 2','2025-02-15 12:40:32'),
(41,1,'Order confirmed','2025-02-15 12:40:52'),
(42,1,'Notification 1','2025-02-15 12:40:52'),
(43,1,'Notification 2','2025-02-15 12:40:53'),
(44,1,'Order confirmed','2025-02-15 12:42:00'),
(45,1,'Notification 1','2025-02-15 12:42:00'),
(46,1,'Notification 2','2025-02-15 12:42:00'),
(47,1,'Order confirmed','2025-02-15 12:42:36'),
(48,1,'Notification 1','2025-02-15 12:42:36'),
(49,1,'Notification 2','2025-02-15 12:42:36'),
(50,1,'Order confirmed','2025-02-15 12:51:09'),
(51,1,'Notification 1','2025-02-15 12:51:09'),
(52,1,'Notification 2','2025-02-15 12:51:09'),
(53,1,'Order confirmed','2025-02-15 13:00:21'),
(54,1,'Notification 1','2025-02-15 13:00:21'),
(55,1,'Notification 2','2025-02-15 13:00:21'),
(56,1,'Order confirmed','2025-02-15 13:06:32'),
(57,1,'Notification 1','2025-02-15 13:06:32'),
(58,1,'Notification 2','2025-02-15 13:06:32'),
(59,1,'Order confirmed','2025-02-15 13:06:52'),
(60,1,'Notification 1','2025-02-15 13:06:52'),
(61,1,'Notification 2','2025-02-15 13:06:52'),
(62,1,'Order confirmed','2025-02-15 13:07:14'),
(63,1,'Notification 1','2025-02-15 13:07:14'),
(64,1,'Notification 2','2025-02-15 13:07:14'),
(65,1,'Order confirmed','2025-02-15 13:07:44'),
(66,1,'Notification 1','2025-02-15 13:07:44'),
(67,1,'Notification 2','2025-02-15 13:07:44'),
(68,1,'Order confirmed','2025-02-20 14:07:27'),
(69,1,'Notification 1','2025-02-20 14:07:27'),
(70,1,'Notification 2','2025-02-20 14:07:27'),
(71,3,'Your order (Order ID: 3) is ready for pickup!','2025-03-09 06:33:03'),
(72,1,'Order confirmed','2025-03-10 10:11:47'),
(73,1,'Notification 1','2025-03-10 10:11:47'),
(74,1,'Notification 2','2025-03-10 10:11:47'),
(75,1,'Order confirmed','2025-03-10 10:12:12'),
(76,1,'Notification 1','2025-03-10 10:12:12'),
(77,1,'Notification 2','2025-03-10 10:12:12'),
(78,1,'Order confirmed','2025-03-10 10:13:26'),
(79,1,'Notification 1','2025-03-10 10:13:26'),
(80,1,'Notification 2','2025-03-10 10:13:26'),
(81,1,'Order confirmed','2025-03-10 11:46:29'),
(82,1,'Notification 1','2025-03-10 11:46:29'),
(83,1,'Notification 2','2025-03-10 11:46:29'),
(84,1,'Order confirmed','2025-03-10 11:51:27'),
(85,1,'Notification 1','2025-03-10 11:51:27'),
(86,1,'Notification 2','2025-03-10 11:51:27'),
(87,1,'Order confirmed','2025-03-10 12:08:12'),
(88,1,'Notification 1','2025-03-10 12:08:12'),
(89,1,'Notification 2','2025-03-10 12:08:12'),
(90,1,'Order confirmed','2025-03-10 12:10:08'),
(91,1,'Notification 1','2025-03-10 12:10:08'),
(92,1,'Notification 2','2025-03-10 12:10:08'),
(93,1,'Order confirmed','2025-03-10 12:10:58'),
(94,1,'Notification 1','2025-03-10 12:10:58'),
(95,1,'Notification 2','2025-03-10 12:10:58'),
(96,1,'Order confirmed','2025-03-11 05:48:14'),
(97,1,'Notification 1','2025-03-11 05:48:14'),
(98,1,'Notification 2','2025-03-11 05:48:14'),
(99,1,'Order confirmed','2025-03-11 05:59:44'),
(100,1,'Notification 1','2025-03-11 05:59:44'),
(101,1,'Notification 2','2025-03-11 05:59:44'),
(102,1,'Order confirmed','2025-03-11 06:00:07'),
(103,1,'Notification 1','2025-03-11 06:00:07'),
(104,1,'Notification 2','2025-03-11 06:00:07'),
(105,1,'Order confirmed','2025-03-11 06:02:01'),
(106,1,'Notification 1','2025-03-11 06:02:01'),
(107,1,'Notification 2','2025-03-11 06:02:01'),
(108,1,'Order confirmed','2025-03-11 06:03:03'),
(109,1,'Notification 1','2025-03-11 06:03:03'),
(110,1,'Notification 2','2025-03-11 06:03:03'),
(111,1,'Order confirmed','2025-03-11 06:03:46'),
(112,1,'Notification 1','2025-03-11 06:03:46'),
(113,1,'Notification 2','2025-03-11 06:03:46'),
(114,1,'Order confirmed','2025-03-11 06:04:48'),
(115,1,'Notification 1','2025-03-11 06:04:48'),
(116,1,'Notification 2','2025-03-11 06:04:48'),
(117,1,'Order confirmed','2025-03-11 06:06:47'),
(118,1,'Notification 1','2025-03-11 06:06:47'),
(119,1,'Notification 2','2025-03-11 06:06:47'),
(120,1,'Order confirmed','2025-03-11 06:07:53'),
(121,1,'Notification 1','2025-03-11 06:07:53'),
(122,1,'Notification 2','2025-03-11 06:07:53'),
(123,1,'Order confirmed','2025-03-11 06:08:27'),
(124,1,'Notification 1','2025-03-11 06:08:27'),
(125,1,'Notification 2','2025-03-11 06:08:27'),
(126,1,'Order confirmed','2025-03-11 06:08:47'),
(127,1,'Notification 1','2025-03-11 06:08:48'),
(128,1,'Notification 2','2025-03-11 06:08:48'),
(129,1,'Order confirmed','2025-03-11 06:10:15'),
(130,1,'Notification 1','2025-03-11 06:10:15'),
(131,1,'Notification 2','2025-03-11 06:10:15'),
(132,1,'Order confirmed','2025-03-11 06:11:06'),
(133,1,'Notification 1','2025-03-11 06:11:06'),
(134,1,'Notification 2','2025-03-11 06:11:06'),
(135,1,'Order confirmed','2025-03-11 06:20:24'),
(136,1,'Notification 1','2025-03-11 06:20:24'),
(137,1,'Notification 2','2025-03-11 06:20:24'),
(138,1,'Order confirmed','2025-03-11 06:20:52'),
(139,1,'Notification 1','2025-03-11 06:20:52'),
(140,1,'Notification 2','2025-03-11 06:20:52'),
(141,1,'Order confirmed','2025-03-11 06:21:09'),
(142,1,'Notification 1','2025-03-11 06:21:09'),
(143,1,'Notification 2','2025-03-11 06:21:09'),
(144,1,'Order confirmed','2025-03-11 06:21:26'),
(145,1,'Notification 1','2025-03-11 06:21:26'),
(146,1,'Notification 2','2025-03-11 06:21:26'),
(147,1,'Order confirmed','2025-03-11 06:23:00'),
(148,1,'Notification 1','2025-03-11 06:23:00'),
(149,1,'Notification 2','2025-03-11 06:23:00'),
(150,1,'Order confirmed','2025-03-11 06:23:55'),
(151,1,'Notification 1','2025-03-11 06:23:55'),
(152,1,'Notification 2','2025-03-11 06:23:55'),
(153,1,'Order confirmed','2025-03-11 06:24:51'),
(154,1,'Notification 1','2025-03-11 06:24:51'),
(155,1,'Notification 2','2025-03-11 06:24:51'),
(156,1,'Order confirmed','2025-03-11 06:28:20'),
(157,1,'Notification 1','2025-03-11 06:28:20'),
(158,1,'Notification 2','2025-03-11 06:28:20'),
(159,1,'Order confirmed','2025-03-11 06:29:18'),
(160,1,'Notification 1','2025-03-11 06:29:18'),
(161,1,'Notification 2','2025-03-11 06:29:18'),
(162,1,'Order confirmed','2025-03-11 06:31:40'),
(163,1,'Notification 1','2025-03-11 06:31:40'),
(164,1,'Notification 2','2025-03-11 06:31:40'),
(165,1,'Order confirmed','2025-03-11 06:33:26'),
(166,1,'Notification 1','2025-03-11 06:33:26'),
(167,1,'Notification 2','2025-03-11 06:33:26'),
(168,1,'Order confirmed','2025-03-11 06:34:46'),
(169,1,'Notification 1','2025-03-11 06:34:46'),
(170,1,'Notification 2','2025-03-11 06:34:46'),
(171,1,'Order confirmed','2025-03-11 06:36:32'),
(172,1,'Notification 1','2025-03-11 06:36:32'),
(173,1,'Notification 2','2025-03-11 06:36:32'),
(174,1,'Order confirmed','2025-03-11 06:36:32'),
(175,1,'Notification 1','2025-03-11 06:36:32'),
(176,1,'Notification 2','2025-03-11 06:36:32'),
(177,1,'Order confirmed','2025-03-11 06:39:51'),
(178,1,'Notification 1','2025-03-11 06:39:51'),
(179,1,'Notification 2','2025-03-11 06:39:51'),
(180,1,'Order confirmed','2025-03-11 06:40:07'),
(181,1,'Notification 1','2025-03-11 06:40:07'),
(182,1,'Notification 2','2025-03-11 06:40:07'),
(183,1,'Order confirmed','2025-03-11 06:41:22'),
(184,1,'Notification 1','2025-03-11 06:41:22'),
(185,1,'Notification 2','2025-03-11 06:41:22'),
(186,1,'Order confirmed','2025-03-11 06:42:15'),
(187,1,'Notification 1','2025-03-11 06:42:15'),
(188,1,'Notification 2','2025-03-11 06:42:15'),
(189,1,'Order confirmed','2025-03-11 06:43:08'),
(190,1,'Notification 1','2025-03-11 06:43:08'),
(191,1,'Notification 2','2025-03-11 06:43:08'),
(192,1,'Order confirmed','2025-03-11 06:43:36'),
(193,1,'Notification 1','2025-03-11 06:43:36'),
(194,1,'Notification 2','2025-03-11 06:43:36'),
(195,1,'Order confirmed','2025-03-11 06:45:04'),
(196,1,'Notification 1','2025-03-11 06:45:04'),
(197,1,'Notification 2','2025-03-11 06:45:04'),
(198,1,'Order confirmed','2025-03-11 06:46:12'),
(199,1,'Notification 1','2025-03-11 06:46:12'),
(200,1,'Notification 2','2025-03-11 06:46:12'),
(201,1,'Order confirmed','2025-03-11 06:48:47'),
(202,1,'Notification 1','2025-03-11 06:48:47'),
(203,1,'Notification 2','2025-03-11 06:48:47'),
(204,1,'Order confirmed','2025-03-11 06:49:03'),
(205,1,'Notification 1','2025-03-11 06:49:03'),
(206,1,'Notification 2','2025-03-11 06:49:03'),
(207,1,'Order confirmed','2025-03-11 06:53:31'),
(208,1,'Notification 1','2025-03-11 06:53:31'),
(209,1,'Notification 2','2025-03-11 06:53:31'),
(210,1,'Order confirmed','2025-03-11 06:54:10'),
(211,1,'Notification 1','2025-03-11 06:54:10'),
(212,1,'Notification 2','2025-03-11 06:54:10'),
(213,1,'Order confirmed','2025-03-11 06:54:39'),
(214,1,'Notification 1','2025-03-11 06:54:39'),
(215,1,'Notification 2','2025-03-11 06:54:39'),
(216,1,'Order confirmed','2025-03-11 06:54:54'),
(217,1,'Notification 1','2025-03-11 06:54:54'),
(218,1,'Notification 2','2025-03-11 06:54:54'),
(219,1,'Order confirmed','2025-03-11 07:01:38'),
(220,1,'Notification 1','2025-03-11 07:01:38'),
(221,1,'Notification 2','2025-03-11 07:01:38'),
(222,1,'Order confirmed','2025-03-11 07:02:10'),
(223,1,'Notification 1','2025-03-11 07:02:10'),
(224,1,'Notification 2','2025-03-11 07:02:10'),
(225,1,'Order confirmed','2025-03-11 07:03:13'),
(226,1,'Notification 1','2025-03-11 07:03:13'),
(227,1,'Notification 2','2025-03-11 07:03:13'),
(228,1,'Order confirmed','2025-03-11 07:14:20'),
(229,1,'Notification 1','2025-03-11 07:14:20'),
(230,1,'Notification 2','2025-03-11 07:14:20'),
(231,1,'Order confirmed','2025-03-11 07:31:55'),
(232,1,'Notification 1','2025-03-11 07:31:55'),
(233,1,'Notification 2','2025-03-11 07:31:55'),
(234,1,'Order confirmed','2025-03-11 07:32:10'),
(235,1,'Notification 1','2025-03-11 07:32:10'),
(236,1,'Notification 2','2025-03-11 07:32:10'),
(237,1,'Order confirmed','2025-03-11 07:35:14'),
(238,1,'Notification 1','2025-03-11 07:35:14'),
(239,1,'Notification 2','2025-03-11 07:35:14'),
(240,1,'Order confirmed','2025-03-11 07:36:48'),
(241,1,'Notification 1','2025-03-11 07:36:48'),
(242,1,'Notification 2','2025-03-11 07:36:48'),
(243,1,'Order confirmed','2025-03-11 07:39:29'),
(244,1,'Notification 1','2025-03-11 07:39:29'),
(245,1,'Notification 2','2025-03-11 07:39:29'),
(246,1,'Order confirmed','2025-03-11 07:41:25'),
(247,1,'Notification 1','2025-03-11 07:41:25'),
(248,1,'Notification 2','2025-03-11 07:41:25'),
(249,1,'Order confirmed','2025-03-11 07:42:08'),
(250,1,'Notification 1','2025-03-11 07:42:08'),
(251,1,'Notification 2','2025-03-11 07:42:08'),
(252,3,'Your order (Order ID: 27) is ready for pickup!','2025-03-11 08:02:55'),
(253,1,'Order confirmed','2025-03-11 08:04:50'),
(254,1,'Notification 1','2025-03-11 08:04:50'),
(255,1,'Notification 2','2025-03-11 08:04:50'),
(256,1,'Order confirmed','2025-03-12 07:39:22'),
(257,1,'Notification 1','2025-03-12 07:39:22'),
(258,1,'Notification 2','2025-03-12 07:39:22'),
(259,1,'Order confirmed','2025-03-12 07:44:37'),
(260,1,'Notification 1','2025-03-12 07:44:37'),
(261,1,'Notification 2','2025-03-12 07:44:37'),
(262,1,'Order confirmed','2025-03-12 07:45:36'),
(263,1,'Notification 1','2025-03-12 07:45:36'),
(264,1,'Notification 2','2025-03-12 07:45:36'),
(265,1,'Order confirmed','2025-03-12 07:46:57'),
(266,1,'Notification 1','2025-03-12 07:46:57'),
(267,1,'Notification 2','2025-03-12 07:46:57'),
(268,1,'Order confirmed','2025-03-12 07:47:26'),
(269,1,'Notification 1','2025-03-12 07:47:26'),
(270,1,'Notification 2','2025-03-12 07:47:26'),
(271,1,'Order confirmed','2025-03-12 07:49:36'),
(272,1,'Notification 1','2025-03-12 07:49:36'),
(273,1,'Notification 2','2025-03-12 07:49:36'),
(274,1,'Order confirmed','2025-03-12 07:53:35'),
(275,1,'Notification 1','2025-03-12 07:53:35'),
(276,1,'Notification 2','2025-03-12 07:53:35'),
(277,1,'Order confirmed','2025-03-12 07:57:33'),
(278,1,'Notification 1','2025-03-12 07:57:34'),
(279,1,'Notification 2','2025-03-12 07:57:34'),
(280,1,'Order confirmed','2025-03-12 18:14:27'),
(281,1,'Notification 1','2025-03-12 18:14:27'),
(282,1,'Notification 2','2025-03-12 18:14:27'),
(283,1,'Order confirmed','2025-03-12 18:15:05'),
(284,1,'Notification 1','2025-03-12 18:15:05'),
(285,1,'Notification 2','2025-03-12 18:15:05'),
(286,1,'Order confirmed','2025-03-12 18:16:24'),
(287,1,'Notification 1','2025-03-12 18:16:24'),
(288,1,'Notification 2','2025-03-12 18:16:24'),
(289,1,'Order confirmed','2025-03-12 18:17:13'),
(290,1,'Notification 1','2025-03-12 18:17:13'),
(291,1,'Notification 2','2025-03-12 18:17:13'),
(292,1,'Order confirmed','2025-03-12 18:17:38'),
(293,1,'Notification 1','2025-03-12 18:17:38'),
(294,1,'Notification 2','2025-03-12 18:17:38'),
(295,1,'Order confirmed','2025-03-12 18:22:59'),
(296,1,'Notification 1','2025-03-12 18:22:59'),
(297,1,'Notification 2','2025-03-12 18:22:59'),
(298,1,'Order confirmed','2025-03-12 18:26:49'),
(299,1,'Notification 1','2025-03-12 18:26:49'),
(300,1,'Notification 2','2025-03-12 18:26:49'),
(301,1,'Order confirmed','2025-03-12 18:27:05'),
(302,1,'Notification 1','2025-03-12 18:27:05'),
(303,1,'Notification 2','2025-03-12 18:27:05'),
(304,1,'Order confirmed','2025-03-12 18:28:13'),
(305,1,'Notification 1','2025-03-12 18:28:13'),
(306,1,'Notification 2','2025-03-12 18:28:13'),
(307,1,'Order confirmed','2025-03-12 18:28:33'),
(308,1,'Notification 1','2025-03-12 18:28:33'),
(309,1,'Notification 2','2025-03-12 18:28:33'),
(310,1,'Order confirmed','2025-03-12 18:29:50'),
(311,1,'Notification 1','2025-03-12 18:29:50'),
(312,1,'Notification 2','2025-03-12 18:29:50'),
(313,1,'Order confirmed','2025-03-12 18:31:22'),
(314,1,'Notification 1','2025-03-12 18:31:22'),
(315,1,'Notification 2','2025-03-12 18:31:22'),
(316,1,'Order confirmed','2025-03-12 18:32:15'),
(317,1,'Notification 1','2025-03-12 18:32:15'),
(318,1,'Notification 2','2025-03-12 18:32:15'),
(319,1,'Order confirmed','2025-03-12 18:33:23'),
(320,1,'Notification 1','2025-03-12 18:33:23'),
(321,1,'Notification 2','2025-03-12 18:33:23'),
(322,1,'Order confirmed','2025-03-12 18:33:53'),
(323,1,'Notification 1','2025-03-12 18:33:53'),
(324,1,'Notification 2','2025-03-12 18:33:53'),
(325,1,'Order confirmed','2025-03-12 18:34:12'),
(326,1,'Notification 1','2025-03-12 18:34:12'),
(327,1,'Notification 2','2025-03-12 18:34:12'),
(328,1,'Order confirmed','2025-03-12 18:34:54'),
(329,1,'Notification 1','2025-03-12 18:34:55'),
(330,1,'Notification 2','2025-03-12 18:34:55'),
(331,1,'Order confirmed','2025-03-12 18:36:24'),
(332,1,'Notification 1','2025-03-12 18:36:24'),
(333,1,'Notification 2','2025-03-12 18:36:24'),
(334,1,'Order confirmed','2025-03-12 18:36:38'),
(335,1,'Notification 1','2025-03-12 18:36:38'),
(336,1,'Notification 2','2025-03-12 18:36:38'),
(337,1,'Order confirmed','2025-03-12 18:39:01'),
(338,1,'Notification 1','2025-03-12 18:39:01'),
(339,1,'Notification 2','2025-03-12 18:39:01');
/*!40000 ALTER TABLE `Notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OrderDetails`
--

DROP TABLE IF EXISTS `OrderDetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `OrderDetails` (
  `detail_id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `meal_id` int NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`detail_id`),
  KEY `order_id` (`order_id`),
  KEY `meal_id` (`meal_id`),
  CONSTRAINT `orderdetails_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `Orders` (`order_id`) ON DELETE CASCADE,
  CONSTRAINT `orderdetails_ibfk_2` FOREIGN KEY (`meal_id`) REFERENCES `meals` (`meal_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OrderDetails`
--

LOCK TABLES `OrderDetails` WRITE;
/*!40000 ALTER TABLE `OrderDetails` DISABLE KEYS */;
INSERT INTO `OrderDetails` VALUES 
(2,1,2,1),
(3,11,1,1),
(4,11,2,1),
(6,18,2,1),
(7,18,1,1),
(10,20,2,1),
(11,20,1,1),
(13,21,2,2),
(14,22,1,1),
(15,23,7,1),
(16,25,7,1),
(18,27,2,1),
(19,88,104,1);
/*!40000 ALTER TABLE `OrderDetails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Orders`
--

DROP TABLE IF EXISTS `Orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Orders` (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int DEFAULT NULL,
  `order_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `total_price` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`order_id`),
  KEY `orders_ibfk_1` (`customer_id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `Customers` (`customer_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Orders`
--

LOCK TABLES `Orders` WRITE;
/*!40000 ALTER TABLE `Orders` DISABLE KEYS */;
INSERT INTO `Orders` VALUES 
(3,3,'2025-02-14 22:54:05',200.00),
(4,4,'2025-02-14 22:54:05',120.30),
(5,5,'2025-02-14 22:54:05',99.99),
(6,3,'2025-03-05 21:05:04',0.00),
(7,3,'2025-03-05 21:07:42',0.00),
(8,3,'2025-03-05 21:13:55',0.00),
(9,3,'2025-03-05 21:17:03',0.00),
(10,3,'2025-03-05 21:23:45',0.00),
(11,3,'2025-03-05 21:27:18',22.50),
(12,3,'2025-03-05 21:27:57',0.00),
(13,3,'2025-03-05 21:33:41',0.00),
(14,3,'2025-03-05 21:36:59',0.00),
(15,3,'2025-03-05 21:40:24',0.00),
(16,3,'2025-03-05 21:42:08',5.00),
(17,3,'2025-03-05 21:55:22',0.00),
(18,3,'2025-03-07 22:01:35',36.00),
(19,3,'2025-03-07 22:02:38',18.50),
(20,3,'2025-03-07 22:12:30',22.50),
(21,3,'2025-03-07 22:18:05',38.50),
(22,3,'2025-03-07 22:20:21',12.50),
(23,3,'2025-03-07 22:20:35',40.00),
(24,3,'2025-03-08 08:02:55',0.00),
(25,3,'2025-03-08 20:59:10',40.00),
(26,3,'2025-03-09 05:44:41',5.00),
(27,3,'2025-03-10 05:52:57',10.00),
(28,1,'2025-03-10 10:11:46',100.00),
(29,1,'2025-03-10 10:12:12',100.00),
(30,1,'2025-03-10 10:13:26',100.00),
(31,1,'2025-03-10 11:46:29',100.00),
(32,1,'2025-03-10 11:51:27',100.00),
(33,1,'2025-03-10 12:08:12',100.00),
(34,1,'2025-03-10 12:10:08',100.00),
(35,1,'2025-03-10 12:10:57',100.00),
(36,1,'2025-03-11 05:48:14',100.00),
(37,1,'2025-03-11 05:59:44',100.00),
(38,1,'2025-03-11 06:00:07',100.00),
(39,1,'2025-03-11 06:02:00',100.00),
(40,1,'2025-03-11 06:03:03',100.00),
(41,1,'2025-03-11 06:03:45',100.00),
(42,1,'2025-03-11 06:04:48',100.00),
(43,1,'2025-03-11 06:06:47',100.00),
(44,1,'2025-03-11 06:07:53',100.00),
(45,1,'2025-03-11 06:08:27',100.00),
(46,1,'2025-03-11 06:08:47',100.00),
(47,1,'2025-03-11 06:10:15',100.00),
(48,1,'2025-03-11 06:11:06',100.00),
(49,1,'2025-03-11 06:20:23',100.00),
(50,1,'2025-03-11 06:20:52',100.00),
(51,1,'2025-03-11 06:21:09',100.00),
(52,1,'2025-03-11 06:21:26',100.00),
(53,1,'2025-03-11 06:23:00',100.00),
(54,1,'2025-03-11 06:23:55',100.00),
(55,1,'2025-03-11 06:24:51',100.00),
(56,1,'2025-03-11 06:28:19',100.00),
(57,1,'2025-03-11 06:29:18',100.00),
(58,1,'2025-03-11 06:31:40',100.00),
(59,1,'2025-03-11 06:33:25',100.00),
(60,1,'2025-03-11 06:34:46',100.00),
(61,1,'2025-03-11 06:36:31',100.00),
(62,1,'2025-03-11 06:36:31',100.00),
(63,1,'2025-03-11 06:39:50',100.00),
(64,1,'2025-03-11 06:40:06',100.00),
(65,1,'2025-03-11 06:41:22',100.00),
(66,1,'2025-03-11 06:42:14',100.00),
(67,1,'2025-03-11 06:43:08',100.00),
(68,1,'2025-03-11 06:43:36',100.00);
/*!40000 ALTER TABLE `Orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SupplierPayments`
--

DROP TABLE IF EXISTS `SupplierPayments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SupplierPayments` (
  `payment_id` int NOT NULL AUTO_INCREMENT,
  `supplier_id` int DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `payment_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `status` enum('Pending','Paid') DEFAULT 'Pending',
  PRIMARY KEY (`payment_id`),
  KEY `supplier_id` (`supplier_id`),
  CONSTRAINT `supplierpayments_ibfk_1` FOREIGN KEY (`supplier_id`) REFERENCES `Suppliers` (`supplier_id`)
) ENGINE=InnoDB AUTO_INCREMENT=107 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SupplierPayments`
--

LOCK TABLES `SupplierPayments` WRITE;
/*!40000 ALTER TABLE `SupplierPayments` DISABLE KEYS */;
INSERT INTO `SupplierPayments` VALUES (3,3,750.00,'2025-02-14 22:54:05','Paid'),(4,4,400.00,'2025-02-14 22:54:05','Paid');
/*!40000 ALTER TABLE `SupplierPayments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Suppliers`
--

DROP TABLE IF EXISTS `Suppliers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Suppliers` (
  `supplier_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `contact_info` text,
  PRIMARY KEY (`supplier_id`),
  UNIQUE KEY `unique_supplier_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Suppliers`
--

LOCK TABLES `Suppliers` WRITE;
/*!40000 ALTER TABLE `Suppliers` DISABLE KEYS */;
INSERT INTO `Suppliers` VALUES 
(3,'Poultry Direct','sales@poultrydirect.com'),
(4,'Dairy Best','support@dairybest.com'),
(5,'Baking Essentials','info@bakingessentials.com'),
(6,'Updated Supplier','111-222-3333'),
(7,'XYZ Supplies','987-654-3210'),
(8,'XYZ Supplies1','987-654-32101');
/*!40000 ALTER TABLE `Suppliers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Tasks`
--

DROP TABLE IF EXISTS `Tasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Tasks` (
  `task_id` int NOT NULL AUTO_INCREMENT,
  `chef_id` int DEFAULT NULL,
  `order_id` int DEFAULT NULL,
  `task_description` text,
  `due_time` datetime DEFAULT NULL,
  `status` enum('Pending','Completed') DEFAULT 'Pending',
  PRIMARY KEY (`task_id`),
  KEY `tasks_ibfk_1` (`chef_id`),
  KEY `tasks_ibfk_2` (`order_id`),
  CONSTRAINT `tasks_ibfk_1` FOREIGN KEY (`chef_id`) REFERENCES `Chefs` (`chef_id`) ON DELETE CASCADE,
  CONSTRAINT `tasks_ibfk_2` FOREIGN KEY (`order_id`) REFERENCES `Orders` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Tasks`
--

LOCK TABLES `Tasks` WRITE;
/*!40000 ALTER TABLE `Tasks` DISABLE KEYS */;
INSERT INTO `Tasks` VALUES 
(3,3,3,'Grill chicken breast with seasoning','2025-02-14 22:54:05','Completed'),
(4,4,4,'Make gluten-free pizza','2025-02-14 22:54:05','Completed'),
(5,5,5,'Cook seafood-free meal','2025-02-14 22:54:05','Completed'),
(21,1,101,'Prepare pasta','2025-02-20 12:00:00','Pending'),
(22,1,101,'Prepare pasta',NULL,'Pending'),
(23,1,101,'Prepare pizza',NULL,'Pending'),
(24,0,0,'Prepare pasta',NULL,'Pending'),
(25,0,0,'Prepare pasta',NULL,'Pending'),
(27,5,27,'Prepare Order #27','2025-03-10 07:08:59','Completed'),
(28,1,88,'Prepare Order #88','2025-03-11 08:50:23','Pending'),
(29,1,89,'Prepare Custom Order #89','2025-03-11 08:51:30','Pending');
/*!40000 ALTER TABLE `Tasks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Users`
--

DROP TABLE IF EXISTS `Users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('Customer','Chef','Kitchen Manager','Admin') NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Users`
--

LOCK TABLES `Users` WRITE;
/*!40000 ALTER TABLE `Users` DISABLE KEYS */;
INSERT INTO `Users` VALUES 
(3,'john_doe','password123','Customer'),
(4,'admin_12','a1','Admin'),
(5,'ayman_chef','wow','Chef'),
(6,'man_joe','p1','Kitchen Manager');
/*!40000 ALTER TABLE `Users` ENABLE KEYS */;
UNLOCK TABLES;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-16 22:31:07
