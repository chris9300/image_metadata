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
INSERT INTO `image_metadata` VALUES ('custALL',2,10043,NULL,'{\"Make\": \"HUAWEI\", \"Flash\": \"24\", \"Model\": \"EVA-L09\", \"FNumber\": \"220/100 (2,2)\", \"Contrast\": \"0\", \"DateTime\": \"2017:09:26 11:31:41\", \"Software\": \"EVA-L09C109B170\", \"MakerNote\": [\"65, 117, 116, 111, 0\", \"0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0... (3600)\", \"35, 42, 35, 42, 50, 48, 49, 53, 48, 57, 51, 48, 49, 48, 50, 54, 0, -24, 33, 0, 30, 0, 0, 0, 75, 10, 44, 0, 41, 0, -24, 3, -24, 3, -24, 3, -24, 3, 10, 0, -36, 0, 51, 0, 0, 0, 0, 0, 0, 0, 0... (4096)\", \"27, 2, 0, 0\"], \"SceneType\": \"1\", \"Sharpness\": \"0\", \"ColorSpace\": \"1\", \"ExifOffset\": \"268\", \"FileSource\": \"3\", \"ImageWidth\": [\"2240\", \"288\"], \"Saturation\": \"0\", \"SubSecTime\": \"073622\", \"Compression\": \"6\", \"ExifVersion\": \"48, 50, 49, 48\", \"FocalLength\": \"4500/1000 (4,5)\", \"GainControl\": \"0\", \"ImageLength\": [\"3968\", \"512\"], \"LightSource\": \"1\", \"Orientation\": [\"0\", \"0\"], \"XResolution\": [\"72\", \"72\"], \"YResolution\": [\"72\", \"72\"], \"ExposureMode\": \"0\", \"ExposureTime\": \"16667000/1000000000 (0,017)\", \"MeteringMode\": \"5\", \"WhiteBalance\": \"0\", \"ApertureValue\": \"227/100 (2,27)\", \"BitsPerSample\": \"8, 8, 8\", \"InteropOffset\": \"8620\", \"SensingMethod\": \"2\", \"CustomRendered\": \"1\", \"ExifImageWidth\": \"2240\", \"ResolutionUnit\": [\"2\", \"2\"], \"BrightnessValue\": \"0\", \"ExifImageLength\": \"3968\", \"ExposureProgram\": \"2\", \"FlashpixVersion\": \"48, 49, 48, 48\", \"JpgFromRawStart\": \"8780\", \"DateTimeOriginal\": \"2017:09:26 11:31:41\", \"DigitalZoomRatio\": \"1\", \"ImageDescription\": \"cof\", \"JpgFromRawLength\": \"18693\", \"SceneCaptureType\": \"0\", \"YCbCrPositioning\": \"1\", \"DateTimeDigitized\": \"2017:09:26 11:31:41\", \"ShutterSpeedValue\": \"298973/10000 (29,897)\", \"SubSecTimeOriginal\": \"073622\", \"SubSecTimeDigitized\": \"073622\", \"Unknown Tag (0x10d)\": \"\", \"ExposureCompensation\": \"0\", \"SubjectDistanceRange\": \"0\", \"InteroperabilityIndex\": \"R98\", \"ComponentsConfiguration\": \"1, 2, 3, 0\", \"FocalLengthIn35mmFormat\": \"27\", \"InteroperabilityVersion\": \"48, 49, 48, 48\", \"PhotographicSensitivity\": \"125\", \"DeviceSettingDescription\": \"105, 112, 112, 0\"}','uid1'),('custTest2',1,566,NULL,'{\"City\": \"City (Core) (ref2017.1)\", \"Artist\": \"Creator1 (ref2017.1)\", \"Credit\": \"Credit Line (ref2017.1)\", \"Source\": \"Source (ref2017.1)\", \"By-line\": \"Creator1 (ref2017.1)\", \"Headline\": \"The Headline (ref2017.1)\", \"Keywords\": [\"Keyword1ref2017.1\", \"Keyword2ref2017.1\", \"Keyword3ref2017.1\"], \"Copyright\": \"Copyright (Notice) 2017.1 IPTC - www.iptc.org  (ref2017.1)\", \"Object Name\": \"The Title (ref2017.1)\", \"Sublocation\": \"Sublocation (Core) (ref2017.1)\", \"XResolution\": \"72\", \"YResolution\": \"72\", \"Date Created\": \"20170713\", \"Time Created\": \"170100+0000\", \"By-line Title\": \"Creators Job Title  (ref2017.1)\", \"Writer/Editor\": \"Description Writer (ref2017.1)\", \"Province/State\": \"Province/State (Core) (ref2017.1\", \"ResolutionUnit\": \"2\", \"Caption/Abstract\": \"The description aka caption (ref2017.1)\", \"Copyright Notice\": \"Copyright (Notice) 2017.1 IPTC - www.iptc.org  (ref2017.1)\", \"ImageDescription\": \"The description aka caption (ref2017.1)\", \"YCbCrPositioning\": \"1\", \"Subject Reference\": [\"IPTC:1ref2017.1\", \"IPTC:2ref2017.1\", \"IPTC:3ref2017.1\"], \"Special Instructions\": \"An Instruction (ref2017.1)\", \"Object Attribute Reference\": \"A Genre (ref2017.1)\", \"Country/Primary Location Code\": \"R17\", \"Country/Primary Location Name\": \"Country (Core) (ref2017.1)\", \"Original Transmission, Reference\": \"Job Id (ref2017.1)\"}','uid123'),('custTest3',1,566,NULL,'{\"City\": \"City (Core) (ref2017.1)\", \"Artist\": \"Creator1 (ref2017.1)\", \"Credit\": \"Credit Line (ref2017.1)\", \"Source\": \"Source (ref2017.1)\", \"By-line\": \"Creator1 (ref2017.1)\", \"Headline\": \"The Headline (ref2017.1)\", \"Keywords\": [\"Keyword1ref2017.1\", \"Keyword2ref2017.1\", \"Keyword3ref2017.1\"], \"Copyright\": \"Copyright (Notice) 2017.1 IPTC - www.iptc.org  (ref2017.1)\", \"Object Name\": \"The Title (ref2017.1)\", \"Sublocation\": \"Sublocation (Core) (ref2017.1)\", \"XResolution\": \"72\", \"YResolution\": \"72\", \"Date Created\": \"20170713\", \"Time Created\": \"170100+0000\", \"By-line Title\": \"Creators Job Title  (ref2017.1)\", \"Writer/Editor\": \"Description Writer (ref2017.1)\", \"Province/State\": \"Province/State (Core) (ref2017.1\", \"ResolutionUnit\": \"2\", \"Caption/Abstract\": \"The description aka caption (ref2017.1)\", \"Copyright Notice\": \"Copyright (Notice) 2017.1 IPTC - www.iptc.org  (ref2017.1)\", \"ImageDescription\": \"The description aka caption (ref2017.1)\", \"YCbCrPositioning\": \"1\", \"Subject Reference\": [\"IPTC:1ref2017.1\", \"IPTC:2ref2017.1\", \"IPTC:3ref2017.1\"], \"Special Instructions\": \"An Instruction (ref2017.1)\", \"Object Attribute Reference\": \"A Genre (ref2017.1)\", \"Country/Primary Location Code\": \"R17\", \"Country/Primary Location Name\": \"Country (Core) (ref2017.1)\", \"Original Transmission, Reference\": \"Job Id (ref2017.1)\"}','uid_test'),('custTest847',2,10043,NULL,'{\"Make\": \"HUAWEI\", \"Flash\": \"24\", \"Model\": \"EVA-L09\", \"FNumber\": \"220/100 (2,2)\", \"Padding\": [\"28, -22, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0... (2060)\", \"28, -22, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0... (2060)\"], \"Contrast\": \"0\", \"DateTime\": \"2018:10:26 22:37:47\", \"Keywords\": [\"2nd_picasa\", \"own_Tag\", \"picasa_tag\"], \"Software\": \"EVA-L09C109B170\", \"SceneType\": \"1\", \"Sharpness\": \"0\", \"XPComment\": \"own_Comment\", \"ColorSpace\": \"1\", \"ExifOffset\": \"2404\", \"FileSource\": \"3\", \"ImageWidth\": [\"2240\", \"288\"], \"Saturation\": \"0\", \"SubSecTime\": \"073622\", \"XPKeywords\": \"own_Tag\", \"Compression\": \"6\", \"ExifVersion\": \"48, 50, 49, 48\", \"FocalLength\": \"4500/1000 (4,5)\", \"GainControl\": \"0\", \"ImageLength\": [\"3968\", \"512\"], \"LightSource\": \"1\", \"Orientation\": [\"0\", \"0\"], \"XResolution\": [\"72\", \"72\"], \"YResolution\": [\"72\", \"72\"], \"ExposureMode\": \"0\", \"ExposureTime\": \"16667000/1000000000 (0,017)\", \"MeteringMode\": \"5\", \"OffsetSchema\": \"-3504\", \"WhiteBalance\": \"0\", \"ApertureValue\": \"227/100 (2,27)\", \"BitsPerSample\": \"8, 8, 8\", \"ImageUniqueID\": \"706de7bf9e909e9d8270aa088360a90d\", \"SensingMethod\": \"2\", \"CustomRendered\": \"1\", \"ExifImageWidth\": \"2240\", \"ResolutionUnit\": [\"2\", \"2\"], \"BrightnessValue\": \"0\", \"ExifImageLength\": \"3968\", \"ExposureProgram\": \"2\", \"FlashpixVersion\": \"48, 49, 48, 48\", \"JpgFromRawStart\": \"5238\", \"DateTimeOriginal\": \"2017:09:26 11:31:41\", \"DigitalZoomRatio\": \"1\", \"ImageDescription\": \"cof\", \"JpgFromRawLength\": \"19027\", \"SceneCaptureType\": \"0\", \"YCbCrPositioning\": \"1\", \"DateTimeDigitized\": \"2017:09:26 11:31:41\", \"ShutterSpeedValue\": \"298973/10000 (29,897)\", \"SubSecTimeOriginal\": \"073622\", \"SubSecTimeDigitized\": \"073622\", \"ExposureCompensation\": \"0\", \"SubjectDistanceRange\": \"0\", \"ComponentsConfiguration\": \"1, 2, 3, 0\", \"FocalLengthIn35mmFormat\": \"27\", \"PhotographicSensitivity\": \"125\", \"DeviceSettingDescription\": \"105, 112, 112, 0\"}','uid_test');
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

-- Dump completed on 2018-11-10 11:50:51
