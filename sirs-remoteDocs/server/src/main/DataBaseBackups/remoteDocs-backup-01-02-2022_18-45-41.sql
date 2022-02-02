-- MySQL dump 10.13  Distrib 8.0.27, for Linux (x86_64)
--
-- Host: localhost    Database: remoteDocsDB
-- ------------------------------------------------------
-- Server version	8.0.27-0ubuntu0.20.04.1

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

--
-- Table structure for table `docs`
--

DROP TABLE IF EXISTS `docs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `docs` (
  `owner` varchar(64) NOT NULL,
  `filename` varchar(64) NOT NULL,
  `content` varchar(4000) DEFAULT NULL,
  `readKey` blob,
  `writeKey` blob,
  PRIMARY KEY (`owner`,`filename`),
  CONSTRAINT `docs_ibfk_1` FOREIGN KEY (`owner`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `docs`
--

LOCK TABLES `docs` WRITE;
/*!40000 ALTER TABLE `docs` DISABLE KEYS */;
INSERT INTO `docs` VALUES ('diana','f1.txt','this is the docs content one',NULL,NULL),('dianapm','file1','ed5Zzmjjf6hjDuogzjcAiGUtJNCpXBPtOSYcQqWb95oygl6YjsgSvW1pkW7SzIAJufQJit2WqbJ+xs7H41JByA==',_binary 'Ou@\�F\�[UD�&��M\�{\�S&@_a\�lJ�E�,&rv\�rJͼ�0�e�\�_���ᬘZ�8Oy2s�R��\�m|\�\Z{�\�W�G��E�\�\�Ḋ\�\��\�蔻�=˖�Û�n/�\�z#�l)=S�i7��\�e\�.\�|�\�\\Aa�\�\�\�n\�j\�\��MJ�P/~i\�\�\�EZ)\�`��\�H߫\n6D\�!\�Ds}?pz�c�5k+*\�91�\�B,q\�j!�FV�N\�D9���:u�0�\�[z=\��J�C���+�r\�HJD��0\�4+�\�r�3w{�-��\nD���H�\�,aW\�qf�`GO`����ok\�4����y\�7�\�.�4\�J@w\���\��e�����ɇ5�����_\�\���y��}�\�$^�\�VԬ`!ŧ1;%5\'D�\�\�3�r���M�;o���\����� \�3�\�YY$`\�㆙\�\'\�L�q\rd\�\�\'\�=�m��o\�7��d\�\�.<�\�U5\�u\�~\�\�}�Z2u����L���fR,5�I=\�c/��g�{Q;I\�/|�[4\�h9c\�K8\�7�\Zo\�P\�u�F��',_binary 'Y\'��G:\��\�rZv��\�\�zʇ\'�%ⓒy\n\���y�6ӫ$+��h����._hX؊��+�ZŤ\�ox�?ͽ#\\���_)I2q׌?��˖S��\�]C�\�v\�q�w�\�v!�\�qh\�\�C�#ΰ�Zܶ�F�\�\r\�}�ǔd\�B7۠h\�g/\Z\�\�\�;�G\"\�z�l�\�9\�fH��Ba-e\�&O�9���M\�\�Z;�p\�ԝ�\�\�R\�*Z\\BK*\�j�\�\�x\�ۗ5�\"\�\�\�\���#梑\�m\�kL�s\r�>�>�����5�.	fv�Z��u�\�#\�Lձ\��E\�6\�o�����<�\�f�\\b\���~���Çն\r\��Mv�\�[\\N@�g}e\�a\�m쪏2m\�p\�`E��\�V\',\nldV_f\�vhy�X\��\�`�f\0GtkF��\�\�#�\�ȇ�1NE�O�ڔ��\�b~\�\�[5�-\�\�+<��\�F�go��5\�\��\�\�3�⼾�\�\�A\�+�n\�ôV{;؟СffB�r\�+\n\0\�0V+\��T\�0�&\�$�\�f\�\��Hך�!��f\�\�Z\����L~\�A��d�\�0M\�'),('ielga','f2.txt','this is the docs content two',NULL,NULL);
/*!40000 ALTER TABLE `docs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `username` varchar(64) NOT NULL,
  `password` varchar(64) NOT NULL,
  `publicKey` blob,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('diana','password',NULL),('dianapm','bfb19ff9a68c28666a1eb2867147a058f450b9ce508557276031cf6f9ce4e500',_binary '0�\"0\r	*�H�\�\r\0�\00�\n�\0\�N.����E�x@�BkgF�)\�\�F\�`\�(<ɋ\�I\�\�\�\�>��\�~7:9\"\�s\�r\�%v4۝�V��\�\��K�=\�\�\�\�����tQ\��\�\nŴP�H7�aur�{/\�\�`�n\�\�[8l��\���-.\��r2\�C\�{��?\�\r\�;�4w�VP�<7gh@\�T\��y\�0����~\�?\Z�K�=�[#\���|m6\�\�\�B�\�_1\�x�I\� \�#m�\�<\�5[8\"{n\�r�R\�\'٢�e�{�zO�#���\��,\n�&\�j\r��C�\���J�t%d3\�\�}J��\��_\�\�Y[k\�W�p�\�\�d6���^I\�C\�\�6��d޲m\�\�(d�\�.\�c=�X��w�띑m\n\�lo\\#�b\��P�GJ\�/2\�I_��/�\�)\�\�^��\�\�z�3r��\�\�-!\�c\�%� |C�D��\�\��\��q\'�d�\���*_\��f \�sP\�w\�b	�\0����\��!dqbN�\�q8{e��A�{�|a�\�#���%�b���:M|\�co\�G����\�P-�Ta�\�Y\�#\�oU�6\�]\0'),('fatima','password2',NULL),('ielga','password3',NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usersDocs`
--

DROP TABLE IF EXISTS `usersDocs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usersDocs` (
  `contributor` varchar(64) NOT NULL,
  `owner` varchar(64) NOT NULL,
  `filename` varchar(64) NOT NULL,
  `permission` varchar(64) NOT NULL,
  `readKey` blob,
  `writeKey` blob,
  PRIMARY KEY (`contributor`,`owner`,`filename`),
  KEY `owner` (`owner`,`filename`),
  CONSTRAINT `usersDocs_ibfk_1` FOREIGN KEY (`contributor`) REFERENCES `users` (`username`),
  CONSTRAINT `usersDocs_ibfk_2` FOREIGN KEY (`owner`, `filename`) REFERENCES `docs` (`owner`, `filename`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usersDocs`
--

LOCK TABLES `usersDocs` WRITE;
/*!40000 ALTER TABLE `usersDocs` DISABLE KEYS */;
INSERT INTO `usersDocs` VALUES ('diana','ielga','f2.txt','w',NULL,NULL),('fatima','diana','f1.txt','w',NULL,NULL);
/*!40000 ALTER TABLE `usersDocs` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-02-01 18:45:41
