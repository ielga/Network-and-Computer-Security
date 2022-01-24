create database if not exists remoteDocsDB;
use remoteDocsDB;

# SUBSTITUTE test FOR remoteDocsDB

DROP TABLE IF EXISTS `remoteDocsDB`.`users`;
DROP TABLE IF EXISTS `remoteDocsDB`.`docs`;
DROP TABLE IF EXISTS `remoteDocsDB`.`usersDocs`;

CREATE TABLE `remoteDocsDB`.`users` (
    `username` VARCHAR(64) NOT NULL,
    `password` VARCHAR(64) NOT NULL,
    `logged` BOOL NOT NULL,

    PRIMARY KEY (`username`)
) ENGINE=InnoDB;

CREATE TABLE `remoteDocsDB`.`docs` (
   `owner`  VARCHAR(64) NOT NULL,
   `filename` VARCHAR(64) NOT NULL ,
   `content` VARCHAR(1000),

   FOREIGN KEY (`owner`) REFERENCES users(`username`),
   PRIMARY KEY (`owner`, `filename`)

) ENGINE=InnoDB;

CREATE TABLE `remotedocsdb`.`usersDocs` (
    `contributor` VARCHAR(64) NOT NULL,
    `owner` VARCHAR(64) NOT NULL,
    `filename` VARCHAR(64) NOT NULL,
    `permission` VARCHAR(64) NOT NULL,

    FOREIGN KEY (`contributor`) REFERENCES users(`username`),
    FOREIGN KEY (`owner`, `filename`) REFERENCES docs (`owner`, `filename`),
    PRIMARY KEY (`contributor`, `owner`, `filename`)
) ENGINE=InnoDB;

INSERT INTO `remoteDocsDB`.`users`
    (`username`,
     `password`,
     `logged`)
    VALUES
        ('diana',
         'password', false);

INSERT INTO `remoteDocsDB`.`users`
    (`username`,
     `password`,
     `logged`)
    VALUES
        ('fatima',
         'password2', false);

INSERT INTO `remoteDocsDB`.`users`
    (`username`,
     `password`,
     `logged`)
    VALUES
        ('ielga',
         'password3', false);


INSERT INTO `remoteDocsDB`.`docs`
    (`owner`,`filename`,`content`)
    VALUES
    ('diana','f1.txt','this is the docs content one');

INSERT INTO `remoteDocsDB`.`docs`
    (`owner`,`filename`,`content`)
    VALUES
    ('ielga','f2.txt', 'this is the docs content two');

INSERT INTO `remoteDocsDB`.`usersdocs`
    (`contributor`,`owner`,`filename`,`permission`)
    VALUES
    ('fatima','diana','f1.txt','w');

INSERT INTO `remoteDocsDB`.`usersdocs`
    (`contributor`,`owner`,`filename`,`permission`)
    VALUES
    ('diana','ielga','f2.txt','r/w');

SELECT * FROM remoteDocsDB.docs;