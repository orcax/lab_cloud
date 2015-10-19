-- USE `labcloud`;
DROP TRIGGER if exists STUDENT_RESERVATION_LABPLAN_INSERT; 
delimiter //
CREATE TRIGGER STUDENT_RESERVATION_LABPLAN_INSERT after insert ON student_reservation
FOR EACH ROW 
BEGIN
	declare numbers int;
	if new.slot = 0 then
		select slot1OccupiedNumber into @numbers from lab_plan where id=NEW.labPlanId;
        if ifnull(@numbers,'null')='null'  then
			update lab_plan set slot1OccupiedNumber = 0 where id=NEW.labPlanId;
		end if;
		update lab_plan set slot1OccupiedNumber = slot1OccupiedNumber+1 where id=NEW.labPlanId;
        #update lab_plan set slot1ApprovalStatus = NEW.approvalStatus;
	elseif new.slot = 1 then
		select slot2OccupiedNumber into @numbers from lab_plan where id=NEW.labPlanId;
        if ifnull(@numbers,'null')='null' then
			update lab_plan set slot2OccupiedNumber = 0 where id=NEW.labPlanId;
		end if;
		update lab_plan set slot2OccupiedNumber = slot2OccupiedNumber+1 where id=NEW.labPlanId;
        #update lab_plan set slot2ApprovalStatus = NEW.approvalStatus;
	elseif new.slot = 2 then
		select slot3OccupiedNumber into @numbers from lab_plan where id=NEW.labPlanId;
        if ifnull(@numbers,'null')='null' then
			update lab_plan set slot3OccupiedNumber = 0 where id=NEW.labPlanId;
		end if;
		update lab_plan set slot3OccupiedNumber = slot3OccupiedNumber+1 where id=NEW.labPlanId;
        #update lab_plan set slot3ApprovalStatus = NEW.approvalStatus;
	elseif new.slot = 3 then
		select slot4OccupiedNumber into @numbers from lab_plan where id=NEW.labPlanId;
        if ifnull(@numbers,'null')='null' then
			update lab_plan set slot4OccupiedNumber = 0 where id=NEW.labPlanId;
		end if;
		update lab_plan set slot4OccupiedNumber = slot4OccupiedNumber+1 where id=NEW.labPlanId;
        #update lab_plan set slot4ApprovalStatus = NEW.approvalStatus;
	elseif new.slot = 4 then
		select slot5OccupiedNumber into @numbers from lab_plan where id=NEW.labPlanId;
        if ifnull(@numbers,'null')='null' then
			update lab_plan set slot5OccupiedNumber = 0 where id=NEW.labPlanId;
		end if;
		update lab_plan set slot5OccupiedNumber = slot5OccupiedNumber+1 where id=NEW.labPlanId;
        #update lab_plan set slot5ApprovalStatus = NEW.approvalStatus;
	elseif new.slot = 5 then
		select slot6OccupiedNumber into @numbers from lab_plan where id=NEW.labPlanId;
        if ifnull(@numbers,'null')='null' then
			update lab_plan set slot6OccupiedNumber = 0 where id=NEW.labPlanId;
		end if;
		update lab_plan set slot6OccupiedNumber = slot6OccupiedNumber+1 where id=NEW.labPlanId;
        #update lab_plan set slot6ApprovalStatus = NEW.approvalStatus;
	elseif new.slot = 6 then
		select slot7OccupiedNumber into @numbers from lab_plan where id=NEW.labPlanId;
        if ifnull(@numbers,'null')='null' then
			update lab_plan set slot7OccupiedNumber = 0 where id=NEW.labPlanId;
		end if;
		update lab_plan set slot7OccupiedNumber = slot7OccupiedNumber+1 where id=NEW.labPlanId;
        #update lab_plan set slot7ApprovalStatus = NEW.approvalStatus;
	end if;
END; //
delimiter ; 


DROP TRIGGER if exists CLASS_RESERVATION_LABPLAN_INSERT; 
delimiter //
CREATE TRIGGER CLASS_RESERVATION_LABPLAN_INSERT after insert ON class_reservation
FOR EACH ROW 
BEGIN
	declare stu_id, stu_cnt, stu_index, stu_rec_cnt int;
    -- declare done int default false;
    
    declare stu_cur cursor for 
    select s.id 
    from student s
    inner join student_class sc on s.id=sc.studentId
    inner join class c on sc.classId=c.id
    where c.id=new.classId;

    -- declare continue handler for not found set done = true;
    
	if new.slot = 0 then
		update lab_plan set slot1OpenStatus=2, slot1ClassReservationId=new.id,
			slot1ExperimentId=new.experimentId where id = new.labPlanId and `date`=new.`date`;
	elseif new.slot = 1 then
		update lab_plan set slot2OpenStatus=2, slot2ClassReservationId=new.id,
			slot2ExperimentId=new.experimentId where id = new.labPlanId and `date`=new.`date`;
    elseif new.slot = 2 then
		update lab_plan set slot3OpenStatus=2, slot3ClassReservationId=new.id,
			slot3ExperimentId=new.experimentId where id = new.labPlanId and `date`=new.`date`;
	elseif new.slot = 3 then
		update lab_plan set slot4OpenStatus=2, slot4ClassReservationId=new.id,
			slot4ExperimentId=new.experimentId where id = new.labPlanId and `date`=new.`date`;
	elseif new.slot = 4 then
		update lab_plan set slot5OpenStatus=2, slot5ClassReservationId=new.id,
			slot5ExperimentId=new.experimentId where id = new.labPlanId and `date`=new.`date`;
	elseif new.slot = 5 then
		update lab_plan set slot6OpenStatus=2, slot6ClassReservationId=new.id,
			slot6ExperimentId=new.experimentId where id = new.labPlanId and `date`=new.`date`;
	elseif new.slot = 6 then
		update lab_plan set slot7OpenStatus=2, slot7ClassReservationId=new.id,
			slot7ExperimentId=new.experimentId where id = new.labPlanId and `date`=new.`date`;
	end if;
    
    select count(*)
    from student_record sr
    where sr.classId=new.classId and sr.experimentId=new.experimentId
    into stu_rec_cnt;
    
    if stu_rec_cnt = 0 then
		open stu_cur;
		set stu_index = 0;
		select count(*) 
		from student s
		inner join student_class sc on s.id=sc.studentId
		inner join class c on sc.classId=c.id
		where c.id=new.classId
		into stu_cnt;
		-- stu_loop: loop
		while stu_index < stu_cnt do
			set stu_index = stu_index + 1;
			fetch stu_cur into stu_id;
		--    if done then
		-- 		leave stu_loop;
		-- 	end if;

			insert into student_record 
			set create_time=current_timestamp, modify_time=current_timestamp,
				experimentId=new.experimentId, studentId=stu_id, classId=new.classId, status=0;
		-- end loop;
		end while;
    end if;
END; //
delimiter ; 


DROP TRIGGER if exists CLASS_RESERVATION_LABPLAN_DELETE; 
delimiter //
CREATE TRIGGER CLASS_RESERVATION_LABPLAN_DELETE after delete ON class_reservation
FOR EACH ROW 
BEGIN    
	if old.slot=0 then
		update lab_plan set slot1openstatus = 0 where id = old.labplanid;
        update lab_plan set slot1classreservationid = null where id = old.labplanid;
	elseif old.slot = 1 then
		update lab_plan set slot2openstatus = 0 where id = old.labplanid;
        update lab_plan set slot2classreservationid = null where id = old.labplanid;
	elseif old.slot = 2 then
		update lab_plan set slot3openstatus = 0 where id = old.labplanid;
        update lab_plan set slot3classreservationid = null where id = old.labplanid;
    elseif old.slot = 3 then
		update lab_plan set slot4openstatus = 0 where id = old.labplanid;
        update lab_plan set slot4classreservationid = null where id = old.labplanid;
    elseif old.slot = 4 then
		update lab_plan set slot5openstatus = 0 where id = old.labplanid;
        update lab_plan set slot5classreservationid = null where id = old.labplanid;
    elseif old.slot = 5 then
		update lab_plan set slot6openstatus = 0 where id = old.labplanid;
        update lab_plan set slot6classreservationid = null where id = old.labplanid;
	elseif old.slot = 6 then
		update lab_plan set slot7openstatus = 0 where id = old.labplanid;
        update lab_plan set slot7classreservationid = null where id = old.labplanid;
    end if;
    
    delete from student_record where experimentId=old.experimentId and classId=old.classId;
END; //
delimiter ; 


DROP TRIGGER if exists CLASS_RESERVATION_LABPLAN_UPDATE; 
delimiter //
CREATE TRIGGER CLASS_RESERVATION_LABPLAN_UPDATE after update ON class_reservation
FOR EACH ROW 
BEGIN
	-- if new.slot = 0 then
-- 		update lab_plan set slot1OpenStatus=2, slot1ExperimentId=new.experimentId where id = NEW.labplanid and `date`=new.`date`;
-- 	elseif new.slot = 1 then
-- 		update lab_plan set slot2OpenStatus=2, slot2ExperimentId=new.experimentId where id = NEW.labplanid and `date`=new.`date`;
-- 	elseif new.slot = 2 then
-- 		update lab_plan set slot3OpenStatus=2, slot3ExperimentId=new.experimentId where id = NEW.labplanid and `date`=new.`date`;
--     elseif new.slot = 3 then
-- 		update lab_plan set slot4OpenStatus=2, slot4ExperimentId=new.experimentId where id = NEW.labplanid and `date`=new.`date`;
--     elseif new.slot = 4 then
-- 		update lab_plan set slot5OpenStatus=2, slot5ExperimentId=new.experimentId where id = NEW.labplanid and `date`=new.`date`;
--     elseif new.slot = 5 then
-- 		update lab_plan set slot6OpenStatus=2, slot6ExperimentId=new.experimentId where id = NEW.labplanid and `date`=new.`date`;
-- 	END IF;

	if old.slot = 0 then
		if 0 = new.approvalStatus then
			update lab_plan set slot1OpenStatus=2, slot1ExperimentId=new.experimentId where id = NEW.labplanid and `date`=new.`date`;
			
		else
			if new.approvalStatus=1 then
				update lab_plan set slot1OpenStatus=3 where id=new.labPlanId;
			else
				update lab_plan set slot1OpenStatus=0, slot1ExperimentId=null, slot1ClassReservationId=null where id=new.labPlanId;
			end if;
		end if;
	elseif old.slot = 1 then
		if 0 = new.approvalStatus then
			update lab_plan set slot2OpenStatus=2, slot2ExperimentId=new.experimentId where id = NEW.labplanid and `date`=new.`date`;
        else
			if new.approvalStatus=1 then
				update lab_plan set slot2OpenStatus=3 where id=new.labPlanId;
			else
				update lab_plan set slot2OpenStatus=0, slot2ExperimentId=null, slot2ClassReservationId=null where id=new.labPlanId;
			end if;
		end if;
	elseif old.slot = 2 then
		if 0 = new.approvalStatus then
			update lab_plan set slot3OpenStatus=2, slot3ExperimentId=new.experimentId where id = NEW.labplanid and `date`=new.`date`;
			
        else
			if new.approvalStatus=1 then
				update lab_plan set slot3OpenStatus=3 where id=new.labPlanId;
			else
				update lab_plan set slot3OpenStatus=0, slot3ExperimentId=null, slot3ClassReservationId=null where id=new.labPlanId;
			end if;  
		end if;
	elseif old.slot = 3 then
		if 0 = new.approvalStatus then
			update lab_plan set slot4OpenStatus=2, slot4ExperimentId=new.experimentId where id = NEW.labplanid and `date`=new.`date`;
			
        else
			if new.approvalStatus=1 then
				update lab_plan set slot4OpenStatus=3 where id=new.labPlanId;
			else
				update lab_plan set slot4OpenStatus=0, slot4ExperimentId=null, slot4ClassReservationId=null where id=new.labPlanId;
			end if;
		end if;
    elseif old.slot = 4 then
		if 0 = new.approvalStatus then
			update lab_plan set slot5OpenStatus=2, slot5ExperimentId=new.experimentId where id = NEW.labplanid and `date`=new.`date`;
			
        else
			if new.approvalStatus=1 then
				update lab_plan set slot5OpenStatus=3 where id=new.labPlanId;
			else
				update lab_plan set slot5OpenStatus=0, slot5ExperimentId=null, slot5ClassReservationId=null where id=new.labPlanId;
			end if;
		end if;
    elseif old.slot = 5 then
		if 0 = new.approvalStatus then
			update lab_plan set slot6OpenStatus=2, slot6ExperimentId=new.experimentId where id = NEW.labplanid and `date`=new.`date`;
			
        else
			if new.approvalStatus=1 then
				update lab_plan set slot6OpenStatus=3 where id=new.labPlanId;
			else
				update lab_plan set slot6OpenStatus=0, slot6ExperimentId=null, slot6ClassReservationId=null where id=new.labPlanId;
			end if;
		end if;
	elseif old.slot = 6 then
		if 0 = new.approvalStatus then
			update lab_plan set slot7OpenStatus=2, slot7ExperimentId=new.experimentId where id = NEW.labplanid and `date`=new.`date`;
			
        else
			if new.approvalStatus=1 then
				update lab_plan set slot7OpenStatus=3 where id=new.labPlanId;
			else
				update lab_plan set slot7OpenStatus=0, slot7ExperimentId=null, slot7ClassReservationId=null where id=new.labPlanId;
			end if;
		end if;
    end if;
END;//
delimiter ; 

-- semester trigger
DROP TRIGGER if exists SEMESTER_LABPLAN_INSERT; 
delimiter //
CREATE TRIGGER SEMESTER_LABPLAN_INSERT after insert ON semester
FOR EACH ROW 
BEGIN
	call roll_semester(new.startDate, new.endDate); 
	if new.`status`=0 then
		update constant set intValue=new.id where `name`='CUR_SEMESTER' and id<>-1;
	end if;
END; //
delimiter ; 

DROP TRIGGER if exists SEMESTER_LABPLAN_UPDATE; 
delimiter //
CREATE TRIGGER SEMESTER_LABPLAN_UPDATE after update ON semester
FOR EACH ROW 
BEGIN
	if new.`status`=0 then
		update constant set intValue=new.id where `name`='CUR_SEMESTER' and id<>-1;
	end if;
END; //
delimiter ; 


-- DROP TRIGGER if exists STUDENT_RESERVATIOIN_INSERT; 
-- delimiter //
-- CREATE TRIGGER STUDENT_RESERVATIOIN_INSERT after insert ON student_reservation
-- FOR EACH ROW 
-- BEGIN
-- 	call update_lab_plan_occupied_num(new.slot, new.date, new.labId);
-- END; //
-- delimiter ; 

DROP TRIGGER if exists SLOT_RESERVATION_INSERT; 
delimiter //
CREATE TRIGGER SLOT_RESERVATION_INSERT after insert ON slot_reservation
FOR EACH ROW 
BEGIN
	call update_labplan_slot(new.labId, new.date, new.slot, new.max, 0, new.id, new.experimentId);
END; //
delimiter ; 

DROP TRIGGER if exists SLOT_RESERVATION_UPDATE; 
delimiter //
CREATE TRIGGER SLOT_RESERVATION_UPDATE after update ON slot_reservation
FOR EACH ROW 
BEGIN
	call update_labplan_slot(new.labId, new.date, new.slot, new.max, new.occupiedNumber, new.id, new.experimentId);
END; //
delimiter ; 