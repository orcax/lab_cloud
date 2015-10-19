package com.prj.dao;

import java.util.Date;
import java.util.List;

import com.prj.entity.LabPlan;
import com.prj.entity.LabPlan.Weekday;
import com.prj.entity.Reservation.SlotNo;
import com.prj.entity.StudentReservation;

public interface LabPlanDao {
	LabPlan updateLabPlan(LabPlan lp);
	
	List<LabPlan> getLabPlanByWeekByLab(int weekid,int labid, Date fromdate);
	
	LabPlan getLabPlanByStudentReservation(StudentReservation sr);
	
	LabPlan getLabPlanByDate(int labid, Date date);
	
	LabPlan findLabPlanById(int id);
	
	List<LabPlan> getLabPlanByWeek(int weekid, Date fromdate);
	
	List<String> getDateByWeek(int weekid, Date fromdate);

	int getSlotOccupiedNum(int id, SlotNo slot);

	int closeLabSlot(int id, SlotNo slot);

	int getSlotOccupiedNum(int id, SlotNo slot, Weekday weekday);

	int closeLabSlot(int id, SlotNo slot, Weekday weekday);
}
