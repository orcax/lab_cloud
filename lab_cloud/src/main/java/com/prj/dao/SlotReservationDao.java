package com.prj.dao;

import java.util.List;

import com.prj.entity.SlotReservation;
import com.prj.util.DataWrapper;

public interface SlotReservationDao {

	Integer addSlotReservation(SlotReservation slr);

	SlotReservation findSlotReservationById(Integer slotResId);

	boolean checkStudentByExperiment(Integer studentId, Integer id);

	SlotReservation updateSlotReservation(SlotReservation slr);

	DataWrapper<List<SlotReservation>> getAvailableSlotReservationPage(
			Integer stuId, Integer pageSize, Integer pageNumber);

	DataWrapper<List<SlotReservation>> getSlotReservationPage(Integer pageSize,
			Integer pageNumber);

	DataWrapper<List<SlotReservation>> getApprovedSlotReservationPage(Integer stuId,
			Integer pageSize, Integer pageNumber);

	DataWrapper<List<SlotReservation>> getSlotReservationByPageDur(
			Integer pageSize, Integer pageNumber,
			String startDate, String endDate);
	
}
