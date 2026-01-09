-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: hmshop
-- ------------------------------------------------------
-- Server version	8.0.35

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
-- Table structure for table `images`
--

DROP TABLE IF EXISTS `images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `images` (
  `id` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `link` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `size` bigint DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `created_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_4mgw71qgyeud96uf8kgiu9fsw` (`link`),
  KEY `FKp1m9f9rm7xy8nk7a820dvh6c4` (`created_by`),
  CONSTRAINT `FKp1m9f9rm7xy8nk7a820dvh6c4` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `images`
--

LOCK TABLES `images` WRITE;
/*!40000 ALTER TABLE `images` DISABLE KEYS */;
INSERT INTO `images` VALUES ('1fda1bdc-debd-49fd-97cd-e408be946050','2026-01-05 07:37:56.131000','/media/static/1fda1bdc-debd-49fd-97cd-e408be946050.jpg','file',389687,'jpg',1),('3fcdff4c-702f-4b65-a178-2c549502e41e','2026-01-05 07:42:22.468000','/media/static/3fcdff4c-702f-4b65-a178-2c549502e41e.jpg','file',783469,'jpg',1),('5cb8b95d-f7c2-47a9-bf22-9477aac5be72','2026-01-05 07:09:01.850000','/media/static/5cb8b95d-f7c2-47a9-bf22-9477aac5be72.jpg','file',3504,'jpg',1),('68545992-c339-452f-a445-955dfa13440d','2026-01-05 07:42:08.684000','/media/static/68545992-c339-452f-a445-955dfa13440d.jpg','file',520258,'jpg',1),('79855520-4525-40c2-b649-740bde48d567','2026-01-05 07:22:41.355000','/media/static/79855520-4525-40c2-b649-740bde48d567.png','file',174050,'png',1),('7f34fe7c-acd3-46f7-8013-a88b7f7c16db','2026-01-05 07:11:06.505000','/media/static/7f34fe7c-acd3-46f7-8013-a88b7f7c16db.png','file',256442,'png',1),('9b478433-80a0-4952-9373-2f7b3de2283b','2026-01-08 13:56:05.511000','/media/static/9b478433-80a0-4952-9373-2f7b3de2283b.png','file',256442,'png',1),('9b591eec-df5f-44f5-bf0f-92026ef94f4f','2026-01-05 07:42:16.430000','/media/static/9b591eec-df5f-44f5-bf0f-92026ef94f4f.jpg','file',284700,'jpg',1),('a3e54d13-87be-43af-b054-ef264b59b142','2026-01-05 07:16:12.960000','/media/static/a3e54d13-87be-43af-b054-ef264b59b142.png','file',368069,'png',1),('abfa8116-3b90-49b7-b9a4-1779f549c55a','2026-01-05 07:25:42.154000','/media/static/abfa8116-3b90-49b7-b9a4-1779f549c55a.jpg','file',29757,'jpg',1),('ae67ec0f-a8b5-4545-abf8-e2912c5e5ff1','2026-01-05 07:09:49.708000','/media/static/ae67ec0f-a8b5-4545-abf8-e2912c5e5ff1.png','file',3663,'png',1),('cb35769c-268d-4c2b-b91a-848307600dbf','2026-01-05 07:08:36.282000','/media/static/cb35769c-268d-4c2b-b91a-848307600dbf.png','file',2463,'png',1),('d1a2075a-3859-47d2-ad4a-15c5363676ca','2026-01-05 07:09:27.600000','/media/static/d1a2075a-3859-47d2-ad4a-15c5363676ca.png','file',3260,'png',1),('d6a6128e-42ad-4c77-9512-c7fbd250e8a1','2026-01-05 07:34:24.780000','/media/static/d6a6128e-42ad-4c77-9512-c7fbd250e8a1.jpg','file',426688,'jpg',1);
/*!40000 ALTER TABLE `images` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-10  3:54:08
