-- USE `labcloud`;
DROP function IF EXISTS `current_semester`;
DELIMITER $$
CREATE FUNCTION `current_semester` ()
RETURNS INTEGER
BEGIN
	declare ret int;
	select id
    from semester
    where status=0
    into ret;
RETURN ret;
END
$$
DELIMITER ;

DROP function IF EXISTS `cur_before_date_time`;
DELIMITER $$
CREATE FUNCTION `cur_before_date_time` (d date, t time)
RETURNS INTEGER
BEGIN
	if d > curdate() then
		return true;
	elseif d = curdate() then
		return t > curtime();
	else
		return false;
	end if;
END
$$
DELIMITER ;


DROP procedure IF EXISTS `set_plan_template`;

DELIMITER $$
CREATE PROCEDURE `set_plan_template` ()
BEGIN
	truncate table weekly_plan_template;
    insert into weekly_plan_template values(6,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
    insert into weekly_plan_template values(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
    insert into weekly_plan_template values(1,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
    insert into weekly_plan_template values(2,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
    insert into weekly_plan_template values(3,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
    insert into weekly_plan_template values(4,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
    insert into weekly_plan_template values(5,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
END
$$

DELIMITER ;

DROP procedure IF EXISTS `set_slot_default`;
DELIMITER $$
CREATE PROCEDURE `set_slot_default` ()
BEGIN
	declare cur_time datetime;
	truncate table slot;
    insert into slot values(1, curtime(), curtime(), '10:00:00', 1, 0, '08:00:00', null);
    insert into slot values(2, curtime(), curtime(), '12:00:00', 1, 1, '10:00:00', null);
    insert into slot values(3, curtime(), curtime(), '13:00:00', 1, 2, '12:00:00', null);
    insert into slot values(4, curtime(), curtime(), '15:20:00', 1, 3, '13:00:00', null);
    insert into slot values(5, curtime(), curtime(), '18:00:00', 1, 4, '15:20:00', null);
    insert into slot values(6, curtime(), curtime(), '20:20:00', 1, 5, '18:20:00', null);
    insert into slot values(7, curtime(), curtime(), '23:00:00', 1, 6, '20:20:00', null);
END
$$
DELIMITER ;


DROP procedure IF EXISTS `roll_semester`;

DELIMITER $$
CREATE PROCEDURE `roll_semester` (in start_date date, in end_date date)
BEGIN
	declare done int default false;
    declare lab_id int;
    declare day_cnt, lab_cnt int;
    declare day_index, lab_index int;
    declare weekday int;
    declare date_temp date;
    declare slot1_type, slot1_status, slot2_type, slot2_status, slot3_type, slot3_status, slot4_type, slot4_status, slot5_type, slot5_status, slot6_type, slot6_status, slot7_type, slot7_status int;
    
    declare lab_cur cursor for select id from lab where isActive = true;
    
    -- declare continue handler for not found set done = true;
    set lab_cnt = (select count(*) from lab where isActive = true);
    open lab_cur;
    set lab_index = 0;
    while lab_index < lab_cnt do
		set lab_index = lab_index+1;
		fetch lab_cur into lab_id;

        set day_index = 0;
        set day_cnt = datediff(end_date, start_date) + 1;
        while day_index < day_cnt do
			set date_temp = DATE_ADD(start_date,INTERVAL day_index DAY);
			set weekday = dayofweek(date_temp) - 1;
            select t.slot1Type, t.slot1Status, t.slot2Type, t.slot2Status, t.slot3Type, t.slot3Status, t.slot4Type, t.slot4Status, t.slot5Type, t.slot5Status, t.slot6Type, t.slot6Status, t.slot7Type, t.slot7Status  
            into slot1_type, slot1_status, slot2_type, slot2_status, slot3_type, slot3_status, slot4_type, slot4_status, slot5_type, slot5_status, slot6_type, slot6_status, slot7_type, slot7_status 
			from weekly_plan_template t where t.weekday = weekday ; 
            insert into lab_plan 
            (create_time, modify_time, `date`, `weekday`, labId, slot1Type, slot1OpenStatus, slot2Type, slot2OpenStatus, slot3Type, slot3OpenStatus, slot4Type, slot4OpenStatus, slot5Type, slot5OpenStatus, slot6Type, slot6OpenStatus, slot7Type, slot7OpenStatus) 
            values (current_timestamp(),current_timestamp(), date_temp, weekday, lab_id, slot1_type, slot1_status, slot2_type, slot2_status, slot3_type, slot3_status, slot4_type, slot4_status, slot5_type, slot5_status, slot6_type, slot6_status, slot7_type, slot7_status);
            set day_index = day_index + 1;
        end while;
    end while;

END
$$
DELIMITER ;

DROP procedure IF EXISTS `get_ava_labteas`;
DELIMITER $$
CREATE PROCEDURE `get_ava_labteas` (in class_reservation_id int)
BEGIN
	select *
	from teacher
	where role=1 and id not in 
	(
		select t.id from class_reservation cr
		inner join class_reservation cr2 on cr.date=cr2.date and cr.slot=cr2.slot
		inner join teacher_labreserve tl on cr.id=tl.classReservatoinId
		inner join teacher_labreserve_teacher tlt on tl.id=tlt.teacherLabReserveId
		inner join teacher t on tlt.teacherId=t.id
		where cr2.id=class_reservation_id
	);
END
$$
DELIMITER ;

DROP procedure IF EXISTS `get_classes_in_lab_by_teacher`;
DELIMITER $$
CREATE PROCEDURE `get_classes_in_lab_by_teacher` (in teacher_id int, in lab_id int)
BEGIN
	select c.*
    from class c
    inner join semester s on c.semesterId=s.id
    inner join teacher t on c.teacherId=t.id
    inner join course co on c.courseId=co.id
    inner join course_experiment coe on co.id=coe.courseId
    inner join experiment e on coe.experimentId=e.id
    inner join lab_experiment le on e.id=le.experimentId
    inner join lab l on le.labId=l.id
    where s.status=0 and t.id=teacher_id and l.id=lab_id;
END
$$
DELIMITER ;

DROP procedure IF EXISTS `get_exp_infos_by_lab_class`;
DELIMITER $$
CREATE PROCEDURE `get_exp_infos_by_lab_class` (in class_id int, in lab_id int)
BEGIN
	select e.id as id, e.experimentName as strValue
    from experiment e
    inner join course_experiment coe on coe.experimentId=e.id
    inner join course co on coe.courseId=co.id
    inner join class c on co.id=c.courseId
    inner join lab_experiment le on e.id=le.experimentId
    inner join lab l on le.labId=l.id
    where c.id=class_id and l.id=lab_id;
END
$$
DELIMITER ;

DROP procedure IF EXISTS `get_cr_by_labtea`;
DELIMITER $$
CREATE PROCEDURE `get_cr_by_labtea` (in labtea_id int)
BEGIN
	select *
    from class_reservation cr
    inner join teacher_labreserve tl on cr.id=tl.classReservatoinId
    inner join teacher_labreserve_teacher tlt on tl.id=tlt.teacherLabReserveId
    where tlt.teacherId=labtea_id;
END
$$
DELIMITER ;

DROP procedure IF EXISTS `get_stu_record_by_class_stu_exp`;
DELIMITER $$
CREATE PROCEDURE `get_stu_record_by_class_stu_exp` (in class_id int, in stu_id int, in exp_id int)
BEGIN
	select *
    from student_record sr
    where sr.classId=class_id and sr.studentId=stu_id and sr.experimentId=exp_id;
END
$$
DELIMITER ;

DROP procedure IF EXISTS `get_exp_status_by_class`;
DELIMITER $$
CREATE PROCEDURE `get_exp_status_by_class` (in class_id int)
BEGIN
	select 
		e.id as experimentId,
        e.experimentName, 
		count(*) as totalNumber,
		sum(sr.status=2) as submittedNumber, 
        sum(sr.status=3) as completedNumber,
        crc.crCnt as classReservationCount
    from experiment e
    inner join course_experiment ce on ce.experimentId=e.id
    inner join class cl on cl.courseId=ce.courseId
    inner join student_class sc on sc.classId=cl.id
    left join student_record sr on sr.classid=cl.id and sr.experimentId=e.id and sr.studentId=sc.id
    left join (
		select sum(!isNull(cr.id)) as crCnt, e.id as expId
		from class_reservation cr
        right join experiment e on e.id=cr.experimentId
        inner join course_experiment ce on ce.experimentId=e.id
        inner join class cl on cl.courseId=ce.courseId
        where cl.id=1
        group by expId) as crc on crc.expId = e.id
    where cl.id=1
    group by e.id;
END
$$
DELIMITER ;

DROP procedure IF EXISTS `get_tea_list_by_lab`;
DELIMITER $$
CREATE PROCEDURE `get_tea_list_by_lab` (in lab_id int)
BEGIN
	select t.*
    from teacher t
    inner join class c on c.teacherId=t.id
    inner join course_experiment ce on ce.courseId=c.courseId
    inner join lab_experiment le on le.experimentId=ce.experimentId
    where le.labId=lab_id and c.semesterId=current_semester();
END
$$
DELIMITER ;

DROP procedure IF EXISTS `get_class_list_by_lab_tea`;
DELIMITER $$
CREATE PROCEDURE `get_class_list_by_lab_tea` (in lab_id int, in teacher_id int)
BEGIN
	select c.*
    from class c
    inner join course_experiment ce on ce.courseId=c.courseId
    inner join lab_experiment le on le.experimentId=ce.experimentId
    where le.labId=lab_id and c.teacherId=teacher_id and c.semesterId=current_semester();
END
$$
DELIMITER ;

DROP procedure IF EXISTS `get_exp_by_stu`;
DELIMITER $$
CREATE PROCEDURE `get_exp_by_stu` (in stu_id int)
BEGIN
	select e.*
    from experiment e
    inner join course_experiment ce on ce.experimentId=e.id
    inner join class c on c.courseId=ce.courseId
    inner join student_class sc on sc.classId=c.id
    inner join constant co on co.intValue=c.semesterId
    where sc.studentId=stu_id and co.`name`='CUR_SEMESTER';
END
$$
DELIMITER ;

DROP procedure IF EXISTS `get_ava_stu_resvation_by_stu`;
DELIMITER $$
CREATE PROCEDURE `get_ava_stu_resvation_by_stu` (in stu_id int)
BEGIN
	select sr.*
    from lab_plan lp
    inner join experiment e on e.id=sr.experimentId
    inner join course_experiment ce on ce.experimentId=e.id
    inner join class c on c.courseId=ce.courseId
    inner join student_class sc on sc.classId=c.id
    inner join constant co on co.intValue=c.semesterId
    where sc.studentId=stu_id and co.`name`='CUR_SEMESTER';
END
$$
DELIMITER ;

DROP procedure IF EXISTS `update_lab_plan_occupied_num`;
DELIMITER $$
CREATE PROCEDURE `update_lab_plan_occupied_num` (in slot int, in d date, in lab_id int)
BEGIN
	set @slot_num = slot+1;
    set @is_full = 0, @d=d, @lab_id=lab_id;
    prepare select_stmt 
    from 'select slot?OccupiedNumber>=slot?StudentReservationMax 
			from lab_plan 
            into @is_full
            where date=?, labId=?';
    execute select_stmt using @slot_num, @d, @lab_id;
    if @is_full = 0 then
		prepare stmt from 'update lab_plan set slot?occupiedNumber = slot?occupiedNumber+1 where date=?, labId=?';
		execute stmt using @slot_num, @d, @lab_id;
	end if;
END
$$
DELIMITER ;

DROP procedure IF EXISTS `check_stu_by_exp`;
DELIMITER $$
CREATE PROCEDURE `check_stu_by_exp` (in stu_id int, in exp_id int)
BEGIN
	select count(*)
    from student s
    inner join slot_reservation_student slrs on slrs.studentId=s.id
    inner join slot_reservation slr on slr.id=slrs.slotReservationId
    inner join semester sm on sm.startDate<=slr.date and sm.endDate>=slr.date
    inner join constant c on c.intValue=sm.id
    inner join slot sl on sl.slotNo=slr.slot
    where slr.experimentId=exp_id and cur_before_date_time(slr.date, sl.endTime) and s.id=stu_id and c.name='CUR_SEMESTER';
END
$$
DELIMITER ;

DROP procedure IF EXISTS `update_labplan_slot`;
DELIMITER $$
CREATE PROCEDURE `update_labplan_slot` (
	in lab_id int, in d date, in slot int, in max int, in occupied int, in res_id int, in exp_id int)
BEGIN
-- 	set @lab_id = lab_id, @date = date, @slot=slot, @max = max, @occupied = occupied;
--     prepare stmt from 'update labplan
--     set slot?SlotReservationMax=?, slot?OccupiedNumber=?
--     where labId=? and date=?';
--     execute stmt using @slot, @max, @slot, @occupied, @lab_id, @date;
--     deallocate prepare stmt;
	if slot=0 then
		update lab_plan
		set slot1SlotReservationMax=max, slot1OccupiedNumber=occupied, slot1OpenStatus=3, slot1Type=1, slot1SlotReservationId=res_id, slot1ExperimentId=exp_id
        where labId=lab_id and `date`=d;
	elseif slot=1 then
		update lab_plan
		set slot2SlotReservationMax=max, slot2OccupiedNumber=occupied, slot2OpenStatus=3, slot2Type=1, slot2SlotReservationId=res_id, slot2ExperimentId=exp_id
        where labId=lab_id and `date`=d;
	elseif slot=2 then
		update lab_plan
		set slot3SlotReservationMax=max, slot3OccupiedNumber=occupied, slot3OpenStatus=3, slot3Type=1, slot3SlotReservationId=res_id, slot3ExperimentId=exp_id
        where labId=lab_id and `date`=d;
	elseif slot=3 then
		update lab_plan
		set slot4SlotReservationMax=max, slot4OccupiedNumber=occupied, slot4OpenStatus=3, slot4Type=1, slot4SlotReservationId=res_id, slot4ExperimentId=exp_id
        where labId=lab_id and `date`=d;
	elseif slot=4 then
		update lab_plan
		set slot5SlotReservationMax=max, slot5OccupiedNumber=occupied, slot5OpenStatus=3, slot5Type=1, slot5SlotReservationId=res_id, slot5ExperimentId=exp_id
        where labId=lab_id and `date`=d;
	elseif slot=5 then
		update lab_plan
		set slot6SlotReservationMax=max, slot6OccupiedNumber=occupied, slot6OpenStatus=3, slot6Type=1, slot6SlotReservationId=res_id, slot6ExperimentId=exp_id
        where labId=lab_id and `date`=d;
	elseif slot=6 then
		update lab_plan
		set slot7SlotReservationMax=max, slot7OccupiedNumber=occupied, slot7OpenStatus=3, slot7Type=1, slot7SlotReservationId=res_id, slot7ExperimentId=exp_id
        where labId=lab_id and `date`=d;
	end if;	
END
$$
DELIMITER ;

DROP procedure IF EXISTS `get_ava_slot_resvation_by_stu`;
DELIMITER $$
CREATE PROCEDURE `get_ava_slot_resvation_by_stu` (in stu_id int)
BEGIN
	select DISTINCT slr.*
    from slot_reservation slr
    inner join course_experiment ce on ce.experimentId=slr.experimentId
    inner join class c on c.courseId=ce.courseId
    inner join student_class sc on sc.classId=c.id
    inner join semester s on s.id=c.semesterId
    inner join slot sl on sl.slotNo=slr.slot
    where sc.studentId=stu_id and s.status=0 and cur_before_date_time(slr.date, sl.startTime)
		and slr.experimentId not in (    
			select e.id
			from experiment e
			inner join slot_reservation slr on slr.experimentId=e.id
			inner join slot_reservation_student slrs on slrs.slotReservationId=slr.id
            inner join slot sl on sl.slotNo=slr.slot
			where slrs.studentId=stu_id and cur_before_date_time(slr.date, sl.endTime));
END
$$
DELIMITER ;

DROP procedure IF EXISTS `get_class_by_stu_exp`;
DELIMITER $$
CREATE PROCEDURE `get_class_by_stu_exp` (in stu_id int, in exp_id int)
BEGIN
	select c.*
    from class c
    INNER JOIN course_experiment ce on ce.courseId=c.courseId
    INNER JOIN student_class sc on sc.classId=c.id
    INNER JOIN constant con on con.intValue=c.semesterId
    where sc.studentId=stu_id and ce.experimentId=exp_id and con.`name` = 'CUR_SEMESTER'; 
END
$$
DELIMITER ;

DROP procedure IF EXISTS `get_slot_occupied_num`;
DELIMITER $$
CREATE PROCEDURE `get_slot_occupied_num` (in lab_id int, in slot int)
BEGIN
	select count(*)
    from class_reservation cr
    inner join semester s on s.startDate<=cr.`date` and s.endDate>=cr.`date`
    where s.`status` = 0 and cr.slot=slot and cr.date>=curdate() and cr.`approvalStatus`<>2 and cr.labId=lab_id;
END
$$
DELIMITER ;

DROP procedure IF EXISTS `close_lab_slot`;
DELIMITER $$
CREATE PROCEDURE `close_lab_slot` (in lab_id int, in slot int)
BEGIN
	set @slot=slot+1, @lab_id=lab_id,
		@q = concat(   'update lab_plan lp, semester s 
						set lp.slot', slot+1, 'OpenStatus=1
						where lp.`date`>curdate() and s.`status`=0 
							and s.startDate<=lp.`date` and s.endDate>=lp.`date`
							and lp.labId=?');
	prepare stmt from @q;
    SET SQL_SAFE_UPDATES = 0;
	execute stmt using @lab_id;
    SET SQL_SAFE_UPDATES = 1;
    DEALLOCATE PREPARE stmt;
END
$$
DELIMITER ;

DROP procedure IF EXISTS `get_slot_occupied_num_by_date`;
DELIMITER $$
CREATE PROCEDURE `get_slot_occupied_num_by_date` (in lab_id int, in slot int, in day_of_week int)
BEGIN
	select count(*)
    from class_reservation cr
    inner join semester s on s.startDate<=cr.`date` and s.endDate>=cr.`date`
    where s.`status` = 0 and cr.date>=curdate() and cr.approvalStatus<>2 
		and cr.labId=lab_id and cr.slot=slot and dayofweek(cr.`date`)=day_of_week;
END
$$
DELIMITER ;

DROP procedure IF EXISTS `close_lab_slot_by_date`;
DELIMITER $$
CREATE PROCEDURE `close_lab_slot_by_date` (in lab_id int, in slot int, in day_of_week int)
BEGIN
	set @slot=slot+1, @lab_id=lab_id, @day_of_week = day_of_week,
		@q = concat(   'update lab_plan lp, semester s 
						set lp.slot', slot+1, 'OpenStatus=1
						where lp.`date`>curdate() and s.`status`=0 
							and s.startDate<=lp.`date` and s.endDate>=lp.`date`
							and lp.labId=? and lp.weekday=?-1');
    
	prepare stmt from @q;
    SET SQL_SAFE_UPDATES = 0;
	execute stmt using @lab_id, @day_of_week;
    SET SQL_SAFE_UPDATES = 1;
    DEALLOCATE PREPARE stmt;
END
$$
DELIMITER ;


call set_plan_template();
call set_slot_default();
-- delete from lab_plan where id<>-1;
-- call roll_semester('2015-3-2','2015-6-30'); 
-- call get_classes_in_lab_by_teacher(2,2);
-- call get_exp_infos_by_lab_class(11,2)
-- call get_exp_status_by_class(3);
-- call get_tea_list_by_lab(2);
-- select current_semester();
-- call get_class_list_by_lab_tea(1,1);
-- call get_exp_by_stu(1);
-- call get_ava_slot_resvation_by_stu(1);
-- call get_class_by_stu_exp(1,1);
-- call get_ava_slot_resvation_by_stu(1);