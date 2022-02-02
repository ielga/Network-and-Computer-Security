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
INSERT INTO `docs` VALUES ('diana','f1.txt','this is the docs content one',NULL,NULL),('dianapm','file1','ed5Zzmjjf6hjDuogzjcAiGUtJNCpXBPtOSYcQqWb95oygl6YjsgSvW1pkW7SzIAJufQJit2WqbJ+xs7H41JByA==',_binary 'Ou@\ïF\Ü[UDÀ&¶•M\Æ{\ËS&@_a\×lJ¡E¿,&rv\ôrJÍ¼Œ0e”\Ô_¹…®á¬˜Z¶8Oy2sºRù¸\óm|\Û\Z{¾\êW‹GœµE²\å\İá¸Š\Î\Ğ¹\Äè”»•=Ë–Ã›šn/\óz#£l)=S¥i7’\ïe\ğ£.\Ä|½\ó\\Aa§\ñ\Æ\ãn\ïj\İ\á¢MJˆP/~i\Í\Ï\ØEZ)\Ø`´¹\ïHß«\n6D\Ì!\ÙDs}?pz„c‰5k+*\Õ91‘\ÒB,q\ôj!»FV¶N\ŞD9‡·°:u±0¦\Ğ[z=\Û¬J¤C‚œ‡+„r\İHJD…®0\ğ4+¥\ôr‰3w{µ- \nD—”“H¢\á,aW\Üqf—`GO`¹º™ok\ë4’š°y\Ã7œ\Î.º4\ÆJ@w\à½\Øúeœ°Š¾›É‡5ÿ…–·_\Ì\õ»‡y²ü}£\ç$^³\ĞVÔ¬`!Å§1;%5\'D‘\È\ò3–r˜–ŒM”;ošÀ\õü“‹ş \Ï3ª\ÏYY$`\ğã†™\Ğ\'\ÑL¤q\rd\Ş\Ó\'\ê=ªmú©o\õ7ƒ»d\ö\Ê.<Á\ÕU5\Ôu\Ğ~\Ì\ñ}¹Z2u»¬¤±L³ºfR,5™I=\è£c/¬˜g´{Q;I\ó¤/|«[4\Îh9c\İK8\Ô7°\Zo\éP\Óu¥Fˆ‘',_binary 'Y\'™ˆG:\Í®\İrZv½¾\Ø\ĞzÊ‡\'¸%â“’y\n\ö‹¢y¹6Ó«$+“±h„¼¶._hXØŠ™›+øZÅ¤\Öox°?Í½#\\¡†_)I2q×Œ?¤¡Ë–S†\Ü]CŠ\Şv\ãƒq£wü\ïv!ª\Èqh\Ó\ÊC›#Î°£ZÜ¶¥Fú\é\r\ç}£Ç”d\ÉB7Û h\âg/\Z\ï\æš\Û;ÀG\"\Ãzûl\ğ9\à¥fH®ŒBa-e\Ç&O´9³M\à\âŸZ;£p\ÙÔˆ\Û\äR\ê*Z\\BK*\Îj’\Ş\öx\ÊÛ—5¿\"\ö\Ë\ï¤\à‰#æ¢‘\Ìm\ÆkL±s\rü>¥>„µ£¤ù5‰.	fv£Z™¯u¾\ã#\ÎLÕ±\Ù²E\Ü6\Äo²¬¿ş”<¦\Şfü\\b\ïÿ~±”ºÃ‡Õ¶\r\ÂüMvù\Ö[\\N@‹g}e\ñ²¤a\÷mìª2m\àp\Ã`EÀª\ÛV\',\nldV_f\ãµvhy†X\ô•\á`‰f\0GtkF¯”\Å\Õ#À\İÈ‡§1NE–O¦Ú”­¢\Âb~\Ä\Õ[5­-\Ø\æ…+<¯‚\÷F—go€‹5\İ\ôÁ\í\Ú3¯â¼¾ÿ\È\ÕA\Õ+°n\áÃ´V{;ØŸĞ¡ffBür\Ú+\n\0\í0V+\÷šT\É0¤&\ò$\Éf\Ş\ÌşH×š›!­†f\íª¦\ôZ\éıƒøL~\÷A™£d¹\Ñ0M\Ú'),('ielga','f2.txt','this is the docs content two',NULL,NULL);
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
INSERT INTO `users` VALUES ('diana','password',NULL),('dianapm','bfb19ff9a68c28666a1eb2867147a058f450b9ce508557276031cf6f9ce4e500',_binary '0‚\"0\r	*†H†\÷\r\0‚\00‚\n‚\0\ÄN.ùû”¦E¥x@¨BkgF´)\Õ\ÚF\ò’`\Ô(<É‹\ÄI\î\Ñ\ï\í>¢“\ò~7:9\"\Ûs\är\ö%v4Û¥V‡\Ü\õşK=\Ê\Å\Ú\÷º—°«tQ\òÿ\õ\nÅ´P€H7©aur{/\ì\Ø`€n\Ä\ó[8l»ù\Üù‰-.\à•r2\ãC\Î{¥?\Ë\r\Ö;ƒ4wşVP‘<7gh@\ëT\ÄÀy\Õ0»„»—~\ï?\Z»K±=Œ[#\É¬Œ|m6\Ú\õ\ÍBü\Ç_1\ğx¸I\Ğ \Ù#mü\é<\Û5[8\"{n\ÂrüR\ó\'Ù¢®e{¹zO‰#–’À\ô”,\nü&\ôj\r¤ıC²\÷¨J¤t%d3\Ì\ç}Jş\à¦ú_\ñ\ÇY[k\×W´pú\Ù\Íd6™„^I\â§C\Ö\ß6ƒdŞ²m\Ë\Ä(d¸\î.\èc=´Xş´w¨ë‘m\n\ñlo\\#®b\ÂÁP«GJ\í/2\ëI_‰/¡\õ)\Ï\Î^ü\å\÷z™3rƒ\ç\×-!\æc\Ş%¾ |C½D”‘\Ç\êû\Å²q\'›dª\Şÿ»*_\Çüf \ÌsP\àw\Üb	¿\0ú•Ÿ«\Õû!dqbN•\÷q8{e’A˜{°|aº\ñ#û§%œb’Àø:M|\íco\ôG«û¹\äP-Ta­\ÃY\ä#\ÛoU°6\ã¥]\0'),('fatima','password2',NULL),('ielga','password3',NULL);
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
