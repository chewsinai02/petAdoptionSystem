-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jun 27, 2025 at 09:53 AM
-- Server version: 8.0.30
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `adopt`
--

-- --------------------------------------------------------

--
-- Table structure for table `admins`
--

CREATE TABLE `admins` (
  `id` int NOT NULL,
  `adminName` varchar(20) NOT NULL COMMENT 'Admin''s name',
  `adminPwd` varchar(20) NOT NULL COMMENT 'Password',
  `realName` varchar(20) NOT NULL COMMENT 'Real name',
  `telephone` varchar(20) NOT NULL COMMENT 'Telephone',
  `Email` varchar(20) NOT NULL,
  `birthday` date NOT NULL,
  `sex` varchar(3) NOT NULL,
  `pic` varchar(100) DEFAULT 'a.png',
  `remark` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- Dumping data for table `admins`
--

INSERT INTO `admins` (`id`, `adminName`, `adminPwd`, `realName`, `telephone`, `Email`, `birthday`, `sex`, `pic`, `remark`) VALUES
(1, 'Zidane', 'yi123', 'Yang Yi', '13902193927', '2425902914@qq.com', '1993-09-10', 'Mal', 'a1.png', 'Nickname: Xuanzong. He is the representative of metaphysics in our team. With him, it seems that any difficulty can be solved.'),
(2, 'Carvajal', 'wemz123', 'Wang Er Mazi', '14402193927', '2425902016@qq.com', '1994-09-12', 'Mal', 'a2.png', 'Nickname: Da Sha; He is the most enthusiastic person in our team. He actively participates in team activities and always does his best when the team needs him.'),
(3, 'Varane', 'zs123', 'Zhang San', '14495893927', '2917902016@qq.com', '1994-10-12', 'Mal', 'a3.png', 'Nickname: Xueba. This is a real scholar. He is enthusiastic about team activities and is responsible for the financial management of the team. He is serious and rigorous.'),
(4, 'Ramos', 'ls123', 'Li Si', '14495893012', '2107902016@qq.com', '1994-06-09', 'Mal', 'a4.png', 'Nickname: Shui Ye. Although the nickname of this team has water, it is not watery at all. He is our captain. He has devoted the most energy to the team, and he often protects the safety of the team members.'),
(5, 'Nacho', 'ww123', 'Wang Wu', '14495890112', '2992902016@qq.com', '1995-06-12', 'Fem', 'a5.png', 'Nickname: Wan Jin You. This team member is just like his name. He is the jack-of-all-trades of our team. When our team members have something urgent, he can always step up. He is a very reliable person.'),
(6, 'Marcelo', 'zl123', 'Zhao Liu', '13195890112', '2992909126@qq.com', '1995-09-12', 'Mal', 'a6.png', 'Nickname: Team Pet. This is our vice-captain. He is the happy fruit of our team. In normal activities, he can always bring us happiness.'),
(7, 'Hazard', 'sql1234', 'Sun Qi', '13195890081', '2992909823@qq.com', '1995-10-12', 'Mal', 'a7.png', 'Nickname: Yang Kun. He is not only the face value representative of the team, but also the technical representative. He is an expert in the protection of small animals. He can always give correct advice on protecting small animals.'),
(9, 'Benzema', '342', '342', '342', '342', '2019-08-20', 'Fem', 'a9.png', 'Nickname: Scapegoat. This is the vanguard of our team, brave. But because of some things, the name of the scapegoat was created.'),
(10, 'Modric', '111', 'Luka', '15797959509', '2425549281@qq.com', '2019-08-05', 'Mal', 'a10.png', 'Nickname: Magic Flute. He and Yang Kun in our team are representatives of technology and appearance, and he always keeps a calm heart. When facing difficulties, he is always calm and fearless.'),
(11, 'Bale', '1111', 'Sun Wukong', '15797959509', '2425549281@qq.com', '1990-01-30', 'Fem', 'a11.png', 'Known as Sun Wukong, an invincible existence!!');

-- --------------------------------------------------------

--
-- Table structure for table `adoptanimal`
--

CREATE TABLE `adoptanimal` (
  `id` int NOT NULL,
  `userId` int NOT NULL COMMENT 'Foreign key for user table id',
  `petId` int NOT NULL COMMENT 'Foreign key for pet table id',
  `adoptTime` date NOT NULL,
  `state` int DEFAULT '1' COMMENT 'Whether to agree to be adopted 0 is disagree 1 is still under review 2 is agree'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- Dumping data for table `adoptanimal`
--

INSERT INTO `adoptanimal` (`id`, `userId`, `petId`, `adoptTime`, `state`) VALUES
(1, 1, 1, '2019-08-21', 2),
(2, 5, 2, '2019-08-20', 0),
(3, 3, 3, '2019-08-19', 0),
(4, 4, 4, '2019-08-18', 0),
(5, 2, 5, '2019-08-17', 0),
(6, 6, 6, '2019-08-16', 2),
(7, 7, 7, '2019-08-15', 2),
(8, 2, 8, '2019-08-14', 0),
(9, 9, 9, '2019-08-13', 2),
(10, 9, 1, '2019-08-09', 0),
(13, 1, 5, '2019-09-01', 0),
(14, 30, 5, '2019-09-09', 0),
(15, 30, 2, '2019-09-09', 0),
(16, 30, 2, '2019-09-09', 0),
(17, 30, 10, '2019-09-09', 0),
(18, 1, 2, '2025-06-16', 0),
(19, 1, 2, '2025-06-16', 0),
(21, 1, 2, '2025-06-16', 2),
(22, 1, 2, '2025-06-16', 0),
(23, 1, 3, '2025-06-16', 0),
(24, 5, 3, '2025-06-25', 0),
(25, 2, 10, '2025-06-27', 2),
(26, 2, 5, '2025-06-27', 1),
(27, 2, 12, '2025-06-27', 1);

-- --------------------------------------------------------

--
-- Table structure for table `notification`
--

CREATE TABLE `notification` (
  `id` int NOT NULL,
  `userId` int NOT NULL,
  `petId` int DEFAULT NULL,
  `createdAt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `notification`
--

INSERT INTO `notification` (`id`, `userId`, `petId`, `createdAt`) VALUES
(1, 1, 1, '2025-06-27 01:11:32'),
(2, 6, 6, '2025-06-27 01:17:19'),
(3, 7, 7, '2025-06-27 02:22:11'),
(4, 1, 2, '2025-06-27 15:23:05');

-- --------------------------------------------------------

--
-- Table structure for table `pet`
--

CREATE TABLE `pet` (
  `id` int NOT NULL,
  `petName` varchar(20) NOT NULL,
  `petType` varchar(20) NOT NULL COMMENT 'Pet type',
  `sex` varchar(6) DEFAULT NULL,
  `birthday` date NOT NULL,
  `pic` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'Avatar',
  `state` int NOT NULL DEFAULT '1' COMMENT 'Current status 0 not applied for adoption 1 applied for adoption 2 has been adopted',
  `remark` varchar(100) DEFAULT NULL COMMENT 'Remark'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- Dumping data for table `pet`
--

INSERT INTO `pet` (`id`, `petName`, `petType`, `sex`, `birthday`, `pic`, `state`, `remark`) VALUES
(1, 'kitty', 'Persian Cat', 'male', '2018-09-10', 'cat1_1.jpg,cat1_2.jpg,cat1_3.jpg', 2, 'kitty is a shy, warm, gentle, and friendly Persian cat.'),
(2, 'Vito', 'Persian Cat', 'male', '2018-09-21', 'cat2_1.jpg,cat2_2.jpg,cat2_3.jpg', 2, 'Vito is an energetic cat who loves to play more than other cats.'),
(3, 'kena', 'Bobtail Cat', 'female', '2018-01-21', 'cat3_1.jpg,cat3_2.jpg,cat3_3.jpg,cat3_4.jpg', 0, 'Kena is a cute and lively little princess. She has a persistence for food. She yearns for freedom an'),
(4, 'Vamp', 'Persian Cat', 'female', '2018-11-21', 'cat4_1.jpg,cat4_2.jpg,cat4_3.jpg,cat4_4.jpg', 0, 'Vamp is a female blue Persian cat. She is quiet and does not like to play.'),
(5, 'Simba', 'Shepherd Dog', 'male', '2018-11-21', 'dog1_1.jpg,dog1_2.jpg,dog1_3.jpg,dog1_4.jpg', 1, 'Simba is lively and curious. He especially likes to play with other dogs and loves outdoor activitie'),
(6, 'glery', 'Tibetan Mastiff', 'male', '2019-01-21', 'dog2_1.jpg,dog2_2.jpg,dog2_3.jpg,dog2_4.jpg', 2, 'kito is a curious, energetic, intelligent, and enthusiastic Tibetan Mastiff.'),
(7, 'Soju', 'Tibetan Mastiff', 'male', '2019-01-21', 'dog3_1.jpg,dog3_2.jpg,dog3_3.jpg', 2, 'Soju is a curious, energetic, intelligent, and enthusiastic Tibetan Mastiff.'),
(8, 'Minnie', 'Orange Cat', 'female', '2018-11-21', 'cat5_1.jpg,cat5_2.jpg,cat5_3.jpg,cat5_4.jpg', 0, 'Minnie is a shy, timid, and super-eating orange cat.'),
(9, 'Kena', 'Tabby Cat', 'female', '2018-11-21', 'cat6_1.jpg,cat6_2.jpg,cat6_3.jpg,cat6_4.jpg', 2, 'Kena is a shy, warm, gentle, and friendly tabby cat.'),
(10, 'Pigge', 'Shepherd Dog', 'female', '2018-03-19', 'dog4_1.jpg,dog4_2.jpg,dog4_3.jpg', 3, 'Pigge is a playful shepherd dog, but he is always conscientious when his owner needs him.'),
(11, 'Tommy', 'Akita', 'male', '2019-07-12', 'dog5_1.jpg,dog5_2.jpg,dog5_3.jpg', 0, 'Tommy is a playful shepherd dog, but he is always conscientious when his owner needs him.'),
(12, 'James', 'Bobtail Cat', 'male', '2019-05-12', 'cat7_1.jpg,cat7_2.jpg,cat7_3.jpg', 1, 'James is an energetic cat who loves to play more than other cats.'),
(13, 'Snow', 'Sled Dog', 'male', '2019-06-12', 'dog6_1.jpg,dog6_2.jpg,dog6_3.jpg', 0, 'Snow looks like a quiet dog, but when he moves, he loves to play more than any other dog.'),
(14, 'Isio', 'Shepherd Dog', 'female', '2019-06-29', 'dog7_1.jpg,dog7_2.jpg,dog7_3.jpg', 0, ''),
(16, 'Jellyfish', 'Akita', 'male', '2019-08-31', 'dog8_1.jpg,dog8_2.jpg,dog8_3.jpg', 0, 'ewew'),
(17, 'Fat', 'Cat', 'female', '2024-06-16', 'cat1_1.jpg', 0, 'yellow, like to eat'),
(19, 'Tom', 'cat', 'male', '2020-06-04', 'IMG_5507_1.jpg,IMG_5508_3.JPG', 0, '11');

-- --------------------------------------------------------

--
-- Table structure for table `petupdate`
--

CREATE TABLE `petupdate` (
  `id` int NOT NULL,
  `adoptId` int NOT NULL,
  `updateTime` date NOT NULL,
  `updateContent` text NOT NULL,
  `updatePic` varchar(255) DEFAULT NULL,
  `remark` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `petupdate`
--

INSERT INTO `petupdate` (`id`, `adoptId`, `updateTime`, `updateContent`, `updatePic`, `remark`) VALUES
(1, 1, '2025-06-16', 'aaaa', 'th.jpeg', NULL),
(2, 1, '2025-06-27', 'just vaccinate. look good so far', 'th.jpeg', NULL),
(3, 21, '2025-06-27', 'sick now, already visit doctor. going to recover.', 'th.jpeg', NULL),
(4, 6, '2025-06-27', 'grown up in well', 'th.jpeg', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int NOT NULL,
  `realName` varchar(255) NOT NULL,
  `userName` varchar(20) NOT NULL,
  `password` varchar(30) NOT NULL,
  `sex` varchar(6) DEFAULT NULL,
  `age` int DEFAULT NULL COMMENT 'Age',
  `telephone` varchar(20) DEFAULT NULL COMMENT 'Telephone',
  `Email` varchar(30) DEFAULT NULL COMMENT 'Email',
  `address` varchar(50) DEFAULT NULL COMMENT 'Address',
  `pic` varchar(100) DEFAULT 't0.jpg',
  `state` int DEFAULT '0' COMMENT 'Have you ever adopted a pet? 0 means no, 1 means yes',
  `petHave` int NOT NULL COMMENT 'number of pet current have',
  `experience` int NOT NULL COMMENT 'experience in caring a pets(years)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `realName`, `userName`, `password`, `sex`, `age`, `telephone`, `Email`, `address`, `pic`, `state`, `petHave`, `experience`) VALUES
(1, 'Zhang Sanfeng', 'Zhang Sanfeng', 'zsf123', 'male', 100, '13809182091', '2425549281@qq.com', 'Hubei Wudang Mountain', 't1.jpg', 0, 2, 5),
(2, 'Song Yuanqiao', 'Song Yuanqiao', 'syq123', 'male', 50, '13908457344', '2425549281@qq.com', 'Hubei Wudang Mountain', 't2.jpg', 0, 0, 0),
(3, 'Yu Lianzhou', 'Yu Lianzhou', 'ylz123', 'male', 45, '13903827601', '2425549281@qq.com', 'Hubei Wudang Mountain', 't3.jpg', 1, 0, 0),
(4, 'Yu Daiyan', 'Yu Daiyan', 'ydy123', 'male', 43, '13903822001', '2425549281@qq.com', 'Hubei Wudang Mountain', 't4.jpg', 1, 0, 0),
(5, 'Zhao Min', 'Zhao Min', 'zm123', 'female', 23, '13903810621', '2425549281@qq.com', 'Mongolia Horqin', 't5.jpg', 1, 0, 0),
(6, 'Zhang Songxi', 'Zhang Songxi', 'zsx123', 'male', 40, '13903819146', '2425549281@qq.com', 'Hubei Wudang Mountain', 't6.jpg', 0, 0, 0),
(7, 'Zhang Cuishan', 'Zhang Cuishan', 'zcs123', 'male', 38, '13903819301', '2425549281@qq.com', 'Hubei Wudang Mountain', 't7.jpg', 0, 0, 0),
(8, 'Yin Susu', 'Yin Susu', 'yss123', 'female', 35, '13123819301', '2425549281@qq.com', 'Guangmingding', 't8.jpg', 0, 0, 0),
(9, 'Yin Liting', 'Yin Liting', 'ylt123', 'male', 35, '13123249301', '2425549281@qq.com', 'Hubei Wudang Mountain', 't9.jpg', 1, 0, 0),
(10, 'Mo Shenggu', 'Mo Shenggu', 'msg123', 'male', 32, '13123249892', '2425549281@qq.com', 'Hubei Wudang Mountain', 't10.jpg', 1, 0, 0),
(11, 'Zhang Wuji', 'Zhang Wuji', 'zwj123', 'male', 21, '15797959509', '2425549281@qq.com', 'Guangmingding', 't11.jpg', 1, 0, 0),
(12, 'Yang Xiao', 'Yang Xiao', '123', 'male', 45, '15797959509', '2425549281@qq.com', 'Guangmingding', 't12.jpg', 0, 0, 0),
(13, 'White-browed Eagle K', 'White-browed Eagle K', '8888', 'male', 75, '15797959509', '2425549281@qq.com', 'Guangmingding', 't13.jpg', 0, 0, 0),
(14, 'Master Jueyuan', 'Master Jueyuan', '8888', 'male', 145, '15797959509', '2425549281@qq.com', 'Songshan Shaolin Temple', 't14.jpg', 1, 0, 0),
(15, 'Bao Zheng', 'Bao Zheng', '1111', 'male', 19, '15797959509', '2425549281@qq.com', 'Kaifeng', 't15.jpg', 0, 0, 0),
(16, 'Zhan Zhao', 'Zhan Zhao', '2222', 'male', 31, '15797959509', '2425549281@qq.com', 'Kaifeng', 't16.jpg', 0, 0, 0),
(17, 'Xiaolongnü', 'Xiaolongnü', '4444', 'female', 32, '15797959509', '2425549281@qq.com', 'Ancient Tomb', 't17.jpg', 0, 0, 0),
(18, 'Wang Yuyan', 'Wang Yuyan', '7777', 'female', 45, '15797959509', '2425549281@qq.com', 'Dali, Yunnan', 't18.jpg', 0, 0, 0),
(19, 'Duan Yu', 'Duan Yu', '1111', 'male', 26, '15797959509', '2425549281@qq.com', 'Nanchang City, Jiangxi Province', 't19.jpg', 1, 0, 0),
(33, 'admin', 'admin', 'admin', 'male', 30, '0177423008', 'chewsinai2002@gmail.com', '6,Jalan 4/2, Taman Sri Kluang,', 't1.jpg', 0, 0, 0),
(34, 'admin', 'admin', 'admin', 'male', 30, '0177423008', 'chewsinai2002@gmail.com', '6,Jalan 4/2, Taman Sri Kluang,', 't0.jpg', 0, 0, 0),
(35, 'admin', 'admin', 'admin', 'male', 30, '0177423008', 'chewsinai2002@gmail.com', '6,Jalan 4/2, Taman Sri Kluang,', 't0.jpg', 0, 0, 0),
(36, 'admin', 'admin', 'admin', 'male', 30, '0177423008', 'chewsinai2002@gmail.com', '6,Jalan 4/2, Taman Sri Kluang,', 't0.jpg', 0, 0, 0),
(37, 'sinai02', 'sinai02', '123456789', 'female', 23, '0177423008', 'chewsinai2002@gmail.com', '6, taman sri kluang', 't0.jpg', 0, 0, 0),
(38, 'Jason Lee', 'Jason', 'jason123', 'male', 25, '0175542653', 'jason@gmail.com', 'swdknj cenbhjbbcfsujidnhjukbs', 'Screenshot 2023-09-30 225547.png', 0, 2, 5),
(39, 'Pizza', 'pizza', 'pizza123', 'female', 30, '01115155556', 'pizza@gmail.com', 'dtwtsdf', 'th.jpg', 0, 0, 2);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admins`
--
ALTER TABLE `admins`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `adoptanimal`
--
ALTER TABLE `adoptanimal`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk3` (`userId`),
  ADD KEY `fk4` (`petId`);

--
-- Indexes for table `notification`
--
ALTER TABLE `notification`
  ADD PRIMARY KEY (`id`),
  ADD KEY `userId` (`userId`),
  ADD KEY `petId` (`petId`);

--
-- Indexes for table `pet`
--
ALTER TABLE `pet`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `petupdate`
--
ALTER TABLE `petupdate`
  ADD PRIMARY KEY (`id`),
  ADD KEY `adoptId` (`adoptId`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admins`
--
ALTER TABLE `admins`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `adoptanimal`
--
ALTER TABLE `adoptanimal`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT for table `notification`
--
ALTER TABLE `notification`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `pet`
--
ALTER TABLE `pet`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `petupdate`
--
ALTER TABLE `petupdate`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `notification`
--
ALTER TABLE `notification`
  ADD CONSTRAINT `notification_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `notification_ibfk_2` FOREIGN KEY (`petId`) REFERENCES `pet` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
