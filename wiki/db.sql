-- phpMyAdmin SQL Dump
-- version 4.0.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 24, 2015 at 07:20 PM
-- Server version: 5.6.12-log
-- PHP Version: 5.4.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `magazinedb`
--


CREATE DATABASE IF NOT EXISTS `magazinedb`
  DEFAULT CHARACTER SET utf8
  COLLATE utf8_general_ci;
USE `magazinedb`;

-- --------------------------------------------------------

--
-- Table structure for table `categories_tbl`
--

CREATE TABLE `categories_tbl` (
  `cat_id`   INT(11)    NOT NULL AUTO_INCREMENT,
  `category` VARCHAR(45)         DEFAULT NULL,
  `sug`      TINYINT(1) NOT NULL,
  `img_path` VARCHAR(200)        DEFAULT NULL,
  PRIMARY KEY (`cat_id`),
  UNIQUE KEY `category` (`category`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 21
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `customerownedissues`
--

CREATE TABLE IF NOT EXISTS `customerownedissues` (
  `user_id`     INT(11)     NOT NULL,
  `mag_id`      INT(11)     NOT NULL,
  `title_id`    INT(3)      NOT NULL,
  `pub_id`      INT(11)     NOT NULL,
  `buy_date`    VARCHAR(20) NOT NULL,
  `payment`     DOUBLE      NOT NULL,
  `favorite`    TINYINT(1)  NOT NULL,
  `chargestart` VARCHAR(20) NOT NULL,
  `chargend`    VARCHAR(20) NOT NULL,
  PRIMARY KEY (`user_id`, `mag_id`),
  KEY `customerownedissues_ibfk_2` (`mag_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `gcm_registration_id`
--

CREATE TABLE IF NOT EXISTS `gcm_registration_id` (
  `id`              INT(11) NOT NULL AUTO_INCREMENT,
  `user_id`         INT(10) NOT NULL,
  `Registration_id` TEXT    NOT NULL,
  PRIMARY KEY (`id`, `user_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 3;

-- --------------------------------------------------------

--
-- Table structure for table `issuepagebookmark`
--

CREATE TABLE `issuepagebookmark` (
  `user_id`     INT(11) NOT NULL,
  `mag_id`      INT(11) NOT NULL,
  `page`        INT(3)  NOT NULL,
  `description` TEXT,
  PRIMARY KEY (`user_id`, `mag_id`, `page`),
  KEY `issuepagebookmark_ibfk_2` (`mag_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `issuepagerequest`
--

CREATE TABLE IF NOT EXISTS `issuepagerequest` (
  `user_id`     INT(11)     NOT NULL,
  `mag_id`      INT(11)     NOT NULL,
  `page`        INT(3)      NOT NULL,
  `title_id`    INT(3)      NOT NULL,
  `page_type`   INT(1)      NOT NULL,
  `requestTime` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`user_id`, `mag_id`, `page`),
  KEY `issuepagerequest_ibfk_2` (`mag_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `issuepagesession`
--

CREATE TABLE IF NOT EXISTS `issuepagesession` (
  `user_id`            INT(11)     NOT NULL,
  `mag_id`             INT(11)     NOT NULL,
  `sessionstarttime`   VARCHAR(20) NOT NULL,
  `sessionduration`    INT(5)      NOT NULL,
  `page`               INT(3)      NOT NULL,
  `timeelapsed`        INT(11)     NOT NULL,
  `clicktoenlargcount` INT(11)     NOT NULL,
  PRIMARY KEY (`user_id`, `mag_id`, `sessionstarttime`, `page`),
  KEY `issuepagesession_ibfk_1` (`mag_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `magazine_tbl`
--
CREATE TABLE `magazine_tbl` (
  `magazine_id`     INT(11)      NOT NULL AUTO_INCREMENT,
  `user_id`         INT(11)      NOT NULL,
  `dir_path`        VARCHAR(1000)         DEFAULT NULL,
  `title`           VARCHAR(255)          DEFAULT NULL,
  `description`     TEXT,
  `category`        VARCHAR(255) NOT NULL DEFAULT '',
  `issue_num`       INT(3)                DEFAULT NULL,
  `issue_year`      VARCHAR(4)            DEFAULT NULL,
  `appertype`       VARCHAR(30)           DEFAULT NULL,
  `orig_issue_date` VARCHAR(10)           DEFAULT NULL,
  `issue_appear`    VARCHAR(20)           DEFAULT NULL,
  `date_time`       VARCHAR(19)           DEFAULT NULL,
  `lang`            VARCHAR(15)           DEFAULT 'English',
  `constatus`       INT(1)       NOT NULL DEFAULT '0',
  `dlcount`         INT(10)      NOT NULL DEFAULT '0',
  `favorcount`      INT(11)      NOT NULL DEFAULT '0',
  `pubstatus`       INT(1)       NOT NULL DEFAULT '0',
  `price`           DOUBLE       NOT NULL,
  `sumOfBookmark`   BIGINT(20)   NOT NULL DEFAULT '0',
  `mag_keywords`    VARCHAR(1000)         DEFAULT NULL,
  `mag_type`        VARCHAR(100) NOT NULL,
  PRIMARY KEY (`magazine_id`),
  UNIQUE KEY `magazine_id_UNIQUE` (`magazine_id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 10
  DEFAULT CHARSET = utf8
  COLLATE = utf8_general_ci;


-- --------------------------------------------------------
CREATE TABLE `banners_tbl` (
  `id`          INT(11)      NOT NULL,
  `magazine_id` INT(11)      NOT NULL DEFAULT '0',
  `img_path`    VARCHAR(500) NOT NULL,
  `description` VARCHAR(500)          DEFAULT NULL,
  `active`      INT(1)                DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


-- --------------------------------------------------------

--
-- Table structure for table `magazine_title`
--

CREATE TABLE IF NOT EXISTS `magazine_title` (
  `title_id` INT(11)                 NOT NULL AUTO_INCREMENT,
  `pub_id`   INT(11)                 NOT NULL,
  `title`    VARCHAR(150)
             COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`title_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_general_ci
  AUTO_INCREMENT = 17;

-- --------------------------------------------------------

--
-- Table structure for table `mobileuser_tbl`
--
CREATE TABLE `mobileuser_tbl` (
  `user_id`           INT(11)     NOT NULL,
  `bdate`             VARCHAR(10)          DEFAULT NULL,
  `postcode`          VARCHAR(10)          DEFAULT NULL,
  `fname`             VARCHAR(30)          DEFAULT NULL,
  `lname`             VARCHAR(50)          DEFAULT NULL,
  `job`               VARCHAR(100)         DEFAULT NULL,
  `gener`             VARCHAR(1)           DEFAULT NULL,
  `charge`            DOUBLE               DEFAULT '0',
  `chargestart`       VARCHAR(20)          DEFAULT '0',
  `chargeennd`        VARCHAR(20)          DEFAULT '0',
  `totalpremiummonth` INT(11)     NOT NULL DEFAULT '0',
  `regdate`           VARCHAR(20) NOT NULL,
  PRIMARY KEY (`user_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_general_ci;


-- --------------------------------------------------------

--
-- Table structure for table `mobileuserfavor`
--

CREATE TABLE IF NOT EXISTS `mobileuserfavor` (
  `user_id`  INT(11)    NOT NULL,
  `mag_id`   INT(11)    NOT NULL,
  `favorite` TINYINT(1) NOT NULL,
  PRIMARY KEY (`user_id`, `mag_id`),
  UNIQUE KEY `user_id` (`user_id`, `mag_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `pages`
--

CREATE TABLE IF NOT EXISTS `pages` (
  `page_number` INT(3)  NOT NULL,
  `mag_id`      INT(11) NOT NULL,
  `pageType`    INT(1)  NOT NULL DEFAULT '0',
  PRIMARY KEY (`page_number`, `mag_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `pubbillt`
--

CREATE TABLE IF NOT EXISTS `pubbillt` (
  `id`         INT(11) NOT NULL DEFAULT '0',
  `bankaccnum` VARCHAR(20)      DEFAULT NULL,
  `bankcode`   VARCHAR(15)      DEFAULT NULL,
  `bankname`   VARCHAR(40)      DEFAULT NULL,
  `recipname`  VARCHAR(100)     DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_2` (`id`),
  KEY `id` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `pubcompany`
--

CREATE TABLE IF NOT EXISTS `pubcompany` (
  `id`           INT(11) NOT NULL DEFAULT '0',
  `compname`     VARCHAR(100)     DEFAULT NULL,
  `corpform`     VARCHAR(100)     DEFAULT NULL,
  `streetname`   VARCHAR(100)     DEFAULT NULL,
  `streetnum`    VARCHAR(20)      DEFAULT NULL,
  `cityname`     VARCHAR(50)      DEFAULT NULL,
  `citypostcode` VARCHAR(20)      DEFAULT NULL,
  `country`      VARCHAR(100)     DEFAULT NULL,
  `taxnum`       VARCHAR(50)      DEFAULT NULL,
  `corpregnum`   VARCHAR(100)     DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_2` (`id`),
  KEY `id` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `pubusers`
--

CREATE TABLE IF NOT EXISTS `pubusers` (
  `id`         INT(11) NOT NULL DEFAULT '0',
  `firstname`  VARCHAR(30)      DEFAULT NULL,
  `lastname`   VARCHAR(50)      DEFAULT NULL,
  `email`      VARCHAR(100)     DEFAULT NULL,
  `phonecode`  VARCHAR(15)      DEFAULT NULL,
  `phonenum`   VARCHAR(15)      DEFAULT NULL,
  `streetname` VARCHAR(50)      DEFAULT NULL,
  `streetnum`  VARCHAR(15)      DEFAULT NULL,
  `cityname`   VARCHAR(50)      DEFAULT NULL,
  `citynum`    VARCHAR(15)      DEFAULT NULL,
  `country`    VARCHAR(100)     DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE IF NOT EXISTS `roles` (
  `rolename` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`rolename`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id`     INT(11)      NOT NULL AUTO_INCREMENT,
  `username`    VARCHAR(255) NOT NULL,
  `password`    VARCHAR(20)  NOT NULL,
  `isPublisher` TINYINT(1)   NOT NULL,
  `uuid`        CHAR(36)              DEFAULT NULL,
  PRIMARY KEY (`user_id`, `username`),
  UNIQUE KEY `userscol_UNIQUE` (`user_id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 50
  DEFAULT CHARSET = utf8;


-- --------------------------------------------------------

--
-- Table structure for table `users_roles`
--

CREATE TABLE IF NOT EXISTS `users_roles` (
  `user_id`  INT(11)     NOT NULL,
  `username` VARCHAR(40) NOT NULL,
  `rolename` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`username`, `rolename`),
  KEY `users_roles_fk2` (`rolename`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;

-- Insert partition

INSERT INTO `roles` (`rolename`) VALUES
  ('adminrole'),
  ('publisherrole'),
  ('userrole');


INSERT INTO `users` (`user_id`, `username`, `password`, `isPublisher`) VALUES
  (1, 'magpack', 'magpack', 0);


INSERT INTO `users_roles` (`user_id`, `username`, `rolename`) VALUES
  (1, 'magpack', 'adminrole');


INSERT INTO `categories_tbl` (`cat_id`, `category`, `sug`, `img_path`) VALUES
  (1, 'cultural.and.artistic', 1, NULL),
  (2, 'science.and.technology', 1, NULL),
  (3, 'health.and.medicine', 1, NULL),
  (4, 'fun.and.games', 0, NULL),
  (5, 'athletic', 1, NULL),
  (6, 'political.and.social', 0, NULL),
  (7, 'economic', 1, NULL),
  (8, 'religious', 1, NULL),
  (9, 'historical.and.literary', 0, NULL),
  (10, 'architecture.and.decoration', 1, NULL),
  (11, 'life.style', 0, NULL),
  (12, 'organizational', 0, NULL),
  (13, 'collegiate', 0, NULL),
  (14, 'foreign', 1, NULL);
INSERT INTO `magazinedb`.`banners_tbl`
(`id`,
 `magazine_id`,
 `img_path`,
 `description`,
 `active`)
VALUES
  (1, 0, '', '', 1);

INSERT INTO `magazinedb`.`banners_tbl`
(`id`,
 `magazine_id`,
 `img_path`,
 `description`,
 `active`)
VALUES
  (2, 0, '', '', 1);

DROP USER 'apache'@'localhost';

CREATE USER 'apache'@'localhost'
  IDENTIFIED BY 'apache123';
GRANT SELECT ON magazinedb.users TO 'apache'@'localhost';
GRANT SELECT ON magazinedb.roles TO 'apache'@'localhost';
GRANT SELECT ON magazinedb.users_roles TO 'apache'@'localhost';
SHOW GRANTS FOR 'apache'@'localhost';


DROP USER 'magazineAdmin'@'localhost';

CREATE USER 'magazineAdmin'@'localhost'
  IDENTIFIED BY 'magazineAdmin';
GRANT ALL ON magazinedb.* TO 'magazineAdmin'@'localhost';
SHOW GRANTS FOR 'magazineAdmin'@'localhost';
COMMIT;
FLUSH PRIVILEGES;
COMMIT;


