create database if not exists remoteDocsDB;
use remoteDocsDB;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `remoteDocsDB`.`users`;
DROP TABLE IF EXISTS `remoteDocsDB`.`docs`;
DROP TABLE IF EXISTS `remoteDocsDB`.`usersDocs`;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE `remoteDocsDB`.`users` (
                                        `username` VARCHAR(64) NOT NULL,
                                        `password` VARCHAR(64) NOT NULL,
                                        `publicKey` BLOB,

                                        PRIMARY KEY (`username`)
) ENGINE=InnoDB;

CREATE TABLE `remoteDocsDB`.`docs` (
           `owner`  VARCHAR(64) NOT NULL,
           `filename` VARCHAR(64) NOT NULL ,
           `content` VARCHAR(4000),
           `readKey` BLOB,
           `writeKey` BLOB,

           FOREIGN KEY (`owner`) REFERENCES users(`username`),
           PRIMARY KEY (`owner`, `filename`)

) ENGINE=InnoDB;

CREATE TABLE `remoteDocsDB`.`usersDocs` (
                `contributor` VARCHAR(64) NOT NULL,
                `owner` VARCHAR(64) NOT NULL,
                `filename` VARCHAR(64) NOT NULL,
                `permission` VARCHAR(64) NOT NULL,
                `readKey` BLOB,
                `writeKey` BLOB,

                FOREIGN KEY (`contributor`) REFERENCES users(`username`),
                FOREIGN KEY (`owner`, `filename`) REFERENCES docs (`owner`, `filename`),
                PRIMARY KEY (`contributor`, `owner`, `filename`)
) ENGINE=InnoDB;
