create database if not exists remoteDocsDB;
use remoteDocsDB;

# SUBSTITUTE test FOR remoteDocsDB

DROP TABLE IF EXISTS `remoteDocsDB`.`users`;
DROP TABLE IF EXISTS `remoteDocsDB`.`docs`;
DROP TABLE IF EXISTS `remoteDocsDB`.`usersDocs`;

CREATE TABLE `remoteDocsDB`.`users` (
    `username` VARCHAR(64) NOT NULL,
    `password` VARCHAR(64) NOT NULL,
    PRIMARY KEY (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=73;

CREATE TABLE `remoteDocsDB`.`docs` (
    `doc_id` INT NOT NULL AUTO_INCREMENT,
    `content` VARCHAR(1000) NULL,
    PRIMARY KEY (`doc_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23;

INSERT INTO `remoteDocsDB`.`users`
    (`username`,
    `password`)
    VALUES
    ('diana',
     'password');

INSERT INTO `remoteDocsDB`.`users`
    (`username`,
    `password`)
    VALUES
    ('fatima',
     'password');

INSERT INTO `remoteDocsDB`.`users`
    (`username`,
     `password`)
    VALUES
    ('ielga',
     'password');

INSERT INTO `remoteDocsDB`.`docs`
    (`content`)
    VALUES
    ('this is the docs content');

INSERT INTO `remoteDocsDB`.`docs`
(`content`)
VALUES
    ('this is the others docs content');

SELECT * FROM remoteDocsDB.docs;