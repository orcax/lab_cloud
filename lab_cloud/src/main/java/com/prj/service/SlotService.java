package com.prj.service;

import com.prj.entity.Reservation.SlotNo;
import com.prj.entity.Slot;
import com.prj.util.DataWrapper;

public interface SlotService {
	DataWrapper<Slot> setSlotTime(Integer adminId, Slot slot);
	DataWrapper<Slot> getSlot(SlotNo slotNo);
}
