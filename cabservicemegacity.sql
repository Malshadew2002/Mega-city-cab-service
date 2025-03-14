-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Mar 14, 2025 at 02:21 AM
-- Server version: 9.1.0
-- PHP Version: 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `cabservicemegacity`
--

-- --------------------------------------------------------

--
-- Table structure for table `amount`
--

DROP TABLE IF EXISTS `amount`;
CREATE TABLE IF NOT EXISTS `amount` (
  `id` int NOT NULL,
  `driver_amount` decimal(10,2) NOT NULL,
  `tax` decimal(10,2) NOT NULL,
  `discount` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `amount`
--

INSERT INTO `amount` (`id`, `driver_amount`, `tax`, `discount`) VALUES
(1, 2500.00, 5.55, 5.55);

-- --------------------------------------------------------

--
-- Table structure for table `booking`
--

DROP TABLE IF EXISTS `booking`;
CREATE TABLE IF NOT EXISTS `booking` (
  `bookID` int NOT NULL AUTO_INCREMENT,
  `userID` int DEFAULT NULL,
  `cabID` int DEFAULT NULL,
  `userFullName` varchar(255) DEFAULT NULL,
  `userContactNumber` varchar(20) DEFAULT NULL,
  `driverOption` varchar(255) DEFAULT NULL,
  `Dates` varchar(255) DEFAULT NULL,
  `bookingDate` date DEFAULT NULL,
  `totalAmount` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`bookID`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `booking`
--

INSERT INTO `booking` (`bookID`, `userID`, `cabID`, `userFullName`, `userContactNumber`, `driverOption`, `Dates`, `bookingDate`, `totalAmount`) VALUES
(7, 3, 6, 'malsha dewmini', '02708099', 'With Driver', '2025-03-28', '2025-03-14', 2944.00),
(5, 1, 6, 'malshaEdit', '443333', 'With Driver', '2025-03-21, 2025-03-22', '2025-03-14', 4888.00);

-- --------------------------------------------------------

--
-- Table structure for table `cabs`
--

DROP TABLE IF EXISTS `cabs`;
CREATE TABLE IF NOT EXISTS `cabs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cabNumber` varchar(20) NOT NULL,
  `model` varchar(50) NOT NULL,
  `seats` int NOT NULL,
  `category` varchar(50) NOT NULL,
  `ownerContact` varchar(15) NOT NULL,
  `perDayAmount` decimal(10,2) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cabNumber` (`cabNumber`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `cabs`
--

INSERT INTO `cabs` (`id`, `cabNumber`, `model`, `seats`, `category`, `ownerContact`, `perDayAmount`, `image`) VALUES
(6, 'CAB8076', 'rr', 3, 'van', '4444', 444.00, 'Tel.png'),
(5, 'CAB8075', 'rrqqq', 4, 'van', '4444', 444.00, 'Screenshot 2025-01-25 094940.png'),
(7, 'CBL7070', 'honda civic', 5, 'car', '0702483999', 1000.00, 'car-removebg-preview.png');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `nic` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `address`, `nic`, `email`, `password`, `username`) VALUES
(3, 'malsha dewmini', 'weligama', '0233555V', 'dew@gmail.com', '1234', 'malsha');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
