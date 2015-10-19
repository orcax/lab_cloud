-- set plan template
call set_plan_template();
call set_slot_default();

-- root administrator
INSERT INTO `administrator`
(`id`,
`create_time`,
`modify_time`,
`iconPath`,
`loginToken`,
`number`,
`password`,
`status`,
`name`,
`title`)
VALUES
(0,
current_timestamp,
current_timestamp,
null,
null,
'000000',
'1f24f5fbfca88db3ba2330afd2f0dcdb',
1,
'root',
'root_admin');

-- constant
INSERT INTO `constant`
(`create_time`, `modify_time`, `doulbeValue`, `intValue`, `name`, `strValue`)
VALUES
(current_timestamp, current_timestamp, null, 20, 'LAB_MAX_NUM', null);
INSERT INTO `constant`
(`create_time`, `modify_time`, `doulbeValue`, `intValue`, `name`, `strValue`)
VALUES
(current_timestamp, current_timestamp, null, null, 'CUR_SEMESTER', null);

-- ONLY FOR TEST
/*!40000 ALTER TABLE `lab` DISABLE KEYS */;
INSERT INTO `lab` VALUES 
(1,'2015-03-05 16:55:06','2015-03-24 11:02:26',60,'','','本部101','001',NULL,0,1,1),
(2,'2015-03-05 16:55:19','2015-03-24 11:01:39',60,'','','本部103','002',NULL,0,1,1),
(3,'2015-03-05 16:55:25','2015-03-24 11:01:43',60,'','','本部105','003',NULL,0,1,1),
(4,'2015-03-05 16:55:30','2015-03-24 10:52:58',60,'','','本部114','004',NULL,0,1,1),
(5,'2015-03-05 16:55:35','2015-03-24 10:53:36',60,'','','本部115','005',NULL,0,1,1),
(6,'2015-03-05 16:55:40','2015-03-24 10:16:18',61,'','','本部117','006',NULL,0,1,1),
(7,'2015-03-05 16:55:45','2015-03-24 11:06:00',60,'','','本部118','007',NULL,0,1,1),
(8,'2015-03-05 16:55:06','2015-03-24 10:38:34',101,'','','本部213','008',NULL,0,1,1),
(9,'2015-03-05 16:55:19','2015-03-24 10:54:39',100,'','','本部215','009',NULL,0,1,1),
(10,'2015-03-05 16:55:25','2015-03-24 11:02:48',100,'','','本部218','010',NULL,0,1,1),
(11,'2015-03-05 16:55:30','2015-03-24 11:01:18',100,'','','嘉定H314','011',NULL,0,1,1),
(12,'2015-03-05 16:55:35','2015-03-24 09:29:03',100,'','','嘉定H315','012',NULL,0,1,1),
(13,'2015-03-05 16:55:40','2015-03-24 11:01:52',100,'','','嘉定H316','013',NULL,0,1,1),
(14,'2015-03-05 16:55:45','2015-03-24 11:02:53',100,'','','嘉定H317','014',NULL,0,1,1);
/*!40000 ALTER TABLE `lab` ENABLE KEYS */;

INSERT INTO `semester`
(
`create_time`,
`modify_time`,
`endDate`,
`semesterName`,
`startDate`,
`status`,
`update_id`)
VALUES
(current_timestamp,
current_timestamp,
'2015-06-30',
'2014学年第二学期',
'2015-03-02',
0,
1);

-- SET SQL_SAFE_UPDATES = 0;