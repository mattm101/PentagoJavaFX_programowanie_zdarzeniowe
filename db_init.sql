DROP USER IF EXISTS 'javafxtest'@'localhost';

FLUSH PRIVILEGES;

CREATE USER 'javafxtest'@'localhost' IDENTIFIED BY 'javafxtest';

GRANT ALL PRIVILEGES ON * . * TO 'javafxtest'@'localhost';

CREATE DATABASE  IF NOT EXISTS `javafx_test_database`;
USE `javafx_test_database`;

DROP TABLE IF EXISTS `fxmatch`;

DROP TABLE IF EXISTS `fxuser`;

CREATE TABLE `fxuser` (
  `fxuser_id` int(11) NOT NULL AUTO_INCREMENT,
  `fxuser_username` varchar(45) DEFAULT NULL,
  `fxuser_password` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`fxuser_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


CREATE TABLE `fxmatch` (
  `fxmatch_id` int(11) NOT NULL AUTO_INCREMENT,
  `fxmatch_status` int(11) DEFAULT NULL,
  `fxmatch_fxuser_id` int(11) DEFAULT NULL,
  
  PRIMARY KEY (`fxmatch_id`),
  
  
  KEY `FK_FXUSER_idx` (`fxmatch_fxuser_id`),
  
  CONSTRAINT `FK_FXUSER` 
  FOREIGN KEY (`fxmatch_fxuser_id`) 
  REFERENCES `fxuser` (`fxuser_id`) 
  
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

INSERT INTO fxuser (fxuser_username, fxuser_password) VALUES ('KyleBroflovski','a');
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'KyleBroflovski'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'KyleBroflovski'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'KyleBroflovski'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'KyleBroflovski'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'KyleBroflovski'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'KyleBroflovski'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'KyleBroflovski'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'KyleBroflovski'));
INSERT INTO fxuser (fxuser_username, fxuser_password) VALUES ('SheldorDestroyer','a');
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'SheldorDestroyer'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'SheldorDestroyer'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'SheldorDestroyer'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'SheldorDestroyer'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'SheldorDestroyer'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'SheldorDestroyer'));
INSERT INTO fxuser (fxuser_username, fxuser_password) VALUES ('BigLebowski','a');
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'BigLebowski'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'BigLebowski'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'BigLebowski'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'BigLebowski'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'BigLebowski'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'BigLebowski'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'BigLebowski'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'BigLebowski'));
INSERT INTO fxuser (fxuser_username, fxuser_password) VALUES ('NaNaBaNaNa','a');
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'NaNaBaNaNa'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'NaNaBaNaNa'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'NaNaBaNaNa'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'NaNaBaNaNa'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'NaNaBaNaNa'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'NaNaBaNaNa'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'NaNaBaNaNa'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'NaNaBaNaNa'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'NaNaBaNaNa'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'NaNaBaNaNa'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'NaNaBaNaNa'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'NaNaBaNaNa'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'NaNaBaNaNa'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'NaNaBaNaNa'));
INSERT INTO fxuser (fxuser_username, fxuser_password) VALUES ('GodDestruction','a');
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'GodDestruction'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'GodDestruction'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'GodDestruction'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'GodDestruction'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'GodDestruction'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'GodDestruction'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'GodDestruction'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'GodDestruction'));
INSERT INTO fxuser (fxuser_username, fxuser_password) VALUES ('PentagoMaster','a');
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'PentagoMaster'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'PentagoMaster'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'PentagoMaster'));
INSERT INTO fxuser (fxuser_username, fxuser_password) VALUES ('Bulba','a');
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Bulba'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Bulba'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Bulba'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Bulba'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Bulba'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Bulba'));
INSERT INTO fxuser (fxuser_username, fxuser_password) VALUES ('Armageddon','a');
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Armageddon'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Armageddon'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Armageddon'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Armageddon'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Armageddon'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Armageddon'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Armageddon'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Armageddon'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Armageddon'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Armageddon'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Armageddon'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Armageddon'));
INSERT INTO fxuser (fxuser_username, fxuser_password) VALUES ('Malkovich','a');
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Malkovich'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Malkovich'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Malkovich'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Malkovich'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Malkovich'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Malkovich'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Malkovich'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Malkovich'));
INSERT INTO fxuser (fxuser_username, fxuser_password) VALUES ('Mclovin','a');
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Mclovin'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Mclovin'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Mclovin'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Mclovin'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Mclovin'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Mclovin'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Mclovin'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Mclovin'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Mclovin'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'Mclovin'));
INSERT INTO fxuser (fxuser_username, fxuser_password) VALUES ('TonySoprano','a');
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'TonySoprano'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'TonySoprano'));
INSERT INTO fxmatch (fxmatch_status, fxmatch_fxuser_id) VALUES (0, (SELECT fxuser_id FROM fxuser WHERE fxuser_username = 'TonySoprano'));
