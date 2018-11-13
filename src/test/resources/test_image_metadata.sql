-- MySQL dump 10.13  Distrib 8.0.13, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: test
-- ------------------------------------------------------
-- Server version	8.0.13

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
-- Table structure for table `image_metadata`
--

DROP TABLE IF EXISTS `image_metadata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `image_metadata` (
  `customer_id` varchar(255) NOT NULL,
  `image_version` int(11) NOT NULL,
  `rspace_image_id` bigint(20) NOT NULL,
  `id` int(11) DEFAULT NULL,
  `metadata` json DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`customer_id`,`image_version`,`rspace_image_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image_metadata`
--

LOCK TABLES `image_metadata` WRITE;
/*!40000 ALTER TABLE `image_metadata` DISABLE KEYS */;
INSERT INTO `image_metadata` VALUES ('cust_test_1',1,1000,NULL,'{\"make\": \"HUAWEI\", \"flash\": \"24\", \"model\": \"EVA-L09\", \"fnumber\": \"220/100 (2,2)\", \"padding\": [\"28, -22, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0... (2060)\", \"28, -22, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0... (2060)\"], \"contrast\": \"0\", \"datetime\": \"2018:10:26 22:37:47\", \"keywords\": [\"2nd_picasa\", \"own_Tag\", \"picasa_tag\"], \"software\": \"EVA-L09C109B170\", \"scenetype\": \"1\", \"sharpness\": \"0\", \"xpcomment\": \"own_Comment\", \"colorspace\": \"1\", \"exifoffset\": \"2404\", \"filesource\": \"3\", \"imagewidth\": [\"2240\", \"288\"], \"saturation\": \"0\", \"subsectime\": \"073622\", \"xpkeywords\": \"own_Tag\", \"compression\": \"6\", \"exifversion\": \"48, 50, 49, 48\", \"focallength\": \"4500/1000 (4,5)\", \"gaincontrol\": \"0\", \"imagelength\": [\"3968\", \"512\"], \"lightsource\": \"1\", \"orientation\": [\"0\", \"0\"], \"xresolution\": [\"72\", \"72\"], \"yresolution\": [\"72\", \"72\"], \"exposuremode\": \"0\", \"exposuretime\": \"16667000/1000000000 (0,017)\", \"meteringmode\": \"5\", \"offsetschema\": \"-3504\", \"whitebalance\": \"0\", \"aperturevalue\": \"227/100 (2,27)\", \"bitspersample\": \"8, 8, 8\", \"imageuniqueid\": \"706de7bf9e909e9d8270aa088360a90d\", \"sensingmethod\": \"2\", \"customrendered\": \"1\", \"exifimagewidth\": \"2240\", \"resolutionunit\": [\"2\", \"2\"], \"brightnessvalue\": \"0\", \"exifimagelength\": \"3968\", \"exposureprogram\": \"2\", \"flashpixversion\": \"48, 49, 48, 48\", \"jpgfromrawstart\": \"5238\", \"datetimeoriginal\": \"2017:09:26 11:31:41\", \"digitalzoomratio\": \"1\", \"imagedescription\": \"cof\", \"jpgfromrawlength\": \"19027\", \"scenecapturetype\": \"0\", \"ycbcrpositioning\": \"1\", \"datetimedigitized\": \"2017:09:26 11:31:41\", \"shutterspeedvalue\": \"298973/10000 (29,897)\", \"subsectimeoriginal\": \"073622\", \"subsectimedigitized\": \"073622\", \"exposurecompensation\": \"0\", \"subjectdistancerange\": \"0\", \"componentsconfiguration\": \"1, 2, 3, 0\", \"focallengthin35mmformat\": \"27\", \"photographicsensitivity\": \"125\", \"devicesettingdescription\": \"105, 112, 112, 0\"}','uid_test_1'),('cust_test_2',1,1000,NULL,'{\"city\": \"City (Core) (ref2017.1)\", \"artist\": \"Creator1 (ref2017.1)\", \"credit\": \"Credit Line (ref2017.1)\", \"source\": \"Source (ref2017.1)\", \"by-line\": \"Creator1 (ref2017.1)\", \"headline\": \"The Headline (ref2017.1)\", \"keywords\": [\"Keyword1ref2017.1\", \"Keyword2ref2017.1\", \"Keyword3ref2017.1\"], \"copyright\": \"Copyright (Notice) 2017.1 IPTC - www.iptc.org  (ref2017.1)\", \"object name\": \"The Title (ref2017.1)\", \"sublocation\": \"Sublocation (Core) (ref2017.1)\", \"xresolution\": \"72\", \"yresolution\": \"72\", \"date created\": \"20170713\", \"time created\": \"170100+0000\", \"by-line title\": \"Creators Job Title  (ref2017.1)\", \"writer/editor\": \"Description Writer (ref2017.1)\", \"province/state\": \"Province/State (Core) (ref2017.1\", \"resolutionunit\": \"2\", \"caption/abstract\": \"The description aka caption (ref2017.1)\", \"copyright notice\": \"Copyright (Notice) 2017.1 IPTC - www.iptc.org  (ref2017.1)\", \"imagedescription\": \"The description aka caption (ref2017.1)\", \"ycbcrpositioning\": \"1\", \"subject reference\": [\"IPTC:1ref2017.1\", \"IPTC:2ref2017.1\", \"IPTC:3ref2017.1\"], \"special instructions\": \"An Instruction (ref2017.1)\", \"object attribute reference\": \"A Genre (ref2017.1)\", \"country/primary location code\": \"R17\", \"country/primary location name\": \"Country (Core) (ref2017.1)\", \"original transmission, reference\": \"Job Id (ref2017.1)\"}','uid_test_2'),('cust_test_3',1,1000,NULL,'{\"city\": \"City (Core) (ref2017.1)\", \"artist\": \"Creator1 (ref2017.1)\", \"credit\": \"Credit Line (ref2017.1)\", \"source\": \"Source (ref2017.1)\", \"by-line\": \"Creator1 (ref2017.1)\", \"headline\": \"The Headline (ref2017.1)\", \"keywords\": [\"Keyword1ref2017.1\", \"Keyword2ref2017.1\", \"Keyword3ref2017.1\"], \"copyright\": \"Copyright (Notice) 2017.1 IPTC - www.iptc.org  (ref2017.1)\", \"object name\": \"The Title (ref2017.1)\", \"sublocation\": \"Sublocation (Core) (ref2017.1)\", \"xresolution\": \"72\", \"yresolution\": \"72\", \"date created\": \"20170713\", \"time created\": \"170100+0000\", \"by-line title\": \"Creators Job Title  (ref2017.1)\", \"writer/editor\": \"Description Writer (ref2017.1)\", \"province/state\": \"Province/State (Core) (ref2017.1\", \"resolutionunit\": \"2\", \"caption/abstract\": \"The description aka caption (ref2017.1)\", \"copyright notice\": \"Copyright (Notice) 2017.1 IPTC - www.iptc.org  (ref2017.1)\", \"imagedescription\": \"The description aka caption (ref2017.1)\", \"ycbcrpositioning\": \"1\", \"subject reference\": [\"IPTC:1ref2017.1\", \"IPTC:2ref2017.1\", \"IPTC:3ref2017.1\"], \"special instructions\": \"An Instruction (ref2017.1)\", \"object attribute reference\": \"A Genre (ref2017.1)\", \"country/primary location code\": \"R17\", \"country/primary location name\": \"Country (Core) (ref2017.1)\", \"original transmission, reference\": \"Job Id (ref2017.1)\"}','uid_test_3'),('cust_test_4',1,1000,NULL,'{\"make\": \"HUAWEI\", \"flash\": \"24\", \"model\": \"EVA-L09\", \"fnumber\": \"220/100 (2,2)\", \"padding\": [\"28, -22, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0... (2060)\", \"28, -22, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0... (2060)\"], \"contrast\": \"0\", \"datetime\": \"2018:10:26 22:37:47\", \"keywords\": [\"2nd_picasa\", \"own_Tag\", \"picasa_tag\"], \"software\": \"EVA-L09C109B170\", \"scenetype\": \"1\", \"sharpness\": \"0\", \"xpcomment\": \"own_Comment\", \"colorspace\": \"1\", \"exifoffset\": \"2404\", \"filesource\": \"3\", \"imagewidth\": [\"2240\", \"288\"], \"saturation\": \"0\", \"subsectime\": \"073622\", \"xpkeywords\": \"own_Tag\", \"compression\": \"6\", \"exifversion\": \"48, 50, 49, 48\", \"focallength\": \"4500/1000 (4,5)\", \"gaincontrol\": \"0\", \"imagelength\": [\"3968\", \"512\"], \"lightsource\": \"1\", \"orientation\": [\"0\", \"0\"], \"xresolution\": [\"72\", \"72\"], \"yresolution\": [\"72\", \"72\"], \"exposuremode\": \"0\", \"exposuretime\": \"16667000/1000000000 (0,017)\", \"meteringmode\": \"5\", \"offsetschema\": \"-3504\", \"whitebalance\": \"0\", \"aperturevalue\": \"227/100 (2,27)\", \"bitspersample\": \"8, 8, 8\", \"imageuniqueid\": \"706de7bf9e909e9d8270aa088360a90d\", \"sensingmethod\": \"2\", \"customrendered\": \"1\", \"exifimagewidth\": \"2240\", \"resolutionunit\": [\"2\", \"2\"], \"brightnessvalue\": \"0\", \"exifimagelength\": \"3968\", \"exposureprogram\": \"2\", \"flashpixversion\": \"48, 49, 48, 48\", \"jpgfromrawstart\": \"5238\", \"datetimeoriginal\": \"2017:09:26 11:31:41\", \"digitalzoomratio\": \"1\", \"imagedescription\": \"cof\", \"jpgfromrawlength\": \"19027\", \"scenecapturetype\": \"0\", \"ycbcrpositioning\": \"1\", \"datetimedigitized\": \"2017:09:26 11:31:41\", \"shutterspeedvalue\": \"298973/10000 (29,897)\", \"subsectimeoriginal\": \"073622\", \"subsectimedigitized\": \"073622\", \"exposurecompensation\": \"0\", \"subjectdistancerange\": \"0\", \"componentsconfiguration\": \"1, 2, 3, 0\", \"focallengthin35mmformat\": \"27\", \"photographicsensitivity\": \"125\", \"devicesettingdescription\": \"105, 112, 112, 0\"}','uid_test_3');
/*!40000 ALTER TABLE `image_metadata` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-11-13 16:59:21
