-- --------------------------------------------------------
-- Host:                         mysql.studev.groept.be
-- Server version:               8.0.28 - MySQL Community Server - GPL
-- Server OS:                    Linux
-- HeidiSQL Version:             11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for a21pt104
CREATE DATABASE IF NOT EXISTS `a21pt104` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `a21pt104`;

-- Dumping structure for table a21pt104.bike
CREATE TABLE IF NOT EXISTS `bike` (
  `id` varchar(64) NOT NULL DEFAULT (uuid()),
  `model` varchar(256) NOT NULL,
  `brand_id` varchar(64) NOT NULL,
  `material_id` varchar(64) NOT NULL,
  `category_id` varchar(64) NOT NULL,
  `image_url` varchar(256) DEFAULT NULL,
  `is_for_rent` tinyint(1) NOT NULL,
  `price` decimal(7,2) NOT NULL,
  `is_taken` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_brand` (`brand_id`),
  KEY `fk_material` (`material_id`),
  KEY `fk_category` (`category_id`),
  CONSTRAINT `fk_brand` FOREIGN KEY (`brand_id`) REFERENCES `bike_brand` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_category` FOREIGN KEY (`category_id`) REFERENCES `bike_category` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_material` FOREIGN KEY (`material_id`) REFERENCES `bike_material` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table a21pt104.bike_brand
CREATE TABLE IF NOT EXISTS `bike_brand` (
  `id` varchar(64) NOT NULL DEFAULT (uuid()),
  `name` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table a21pt104.bike_category
CREATE TABLE IF NOT EXISTS `bike_category` (
  `id` varchar(64) NOT NULL DEFAULT (uuid()),
  `name` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table a21pt104.bike_material
CREATE TABLE IF NOT EXISTS `bike_material` (
  `id` varchar(64) NOT NULL DEFAULT (uuid()),
  `name` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table a21pt104.user
CREATE TABLE IF NOT EXISTS `user` (
  `id` varchar(64) NOT NULL DEFAULT (uuid()),
  `email` varchar(256) NOT NULL,
  `password` varchar(256) NOT NULL,
  `role_id` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uc_email` (`email`),
  KEY `fk_roles` (`role_id`),
  CONSTRAINT `fk_roles` FOREIGN KEY (`role_id`) REFERENCES `user_role` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table a21pt104.user_bike_mapping
CREATE TABLE IF NOT EXISTS `user_bike_mapping` (
  `id` varchar(64) NOT NULL DEFAULT (uuid()),
  `user_id` varchar(64) NOT NULL,
  `bike_id` varchar(64) NOT NULL,
  `rent_start_date` datetime DEFAULT NULL,
  `rent_end_date` datetime DEFAULT NULL,
  `buy_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`bike_id`),
  KEY `fk_bike` (`bike_id`),
  CONSTRAINT `fk_bike` FOREIGN KEY (`bike_id`) REFERENCES `bike` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table a21pt104.user_role
CREATE TABLE IF NOT EXISTS `user_role` (
  `id` varchar(64) NOT NULL DEFAULT (uuid()),
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
