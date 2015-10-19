package com.prj.dao;

import com.prj.entity.Reservation.SlotNo;
import com.prj.entity.Slot;

public interface SlotDao {
	Slot getSlot(SlotNo slotNo);
	Slot updateSlot(Slot sl);
	
}
