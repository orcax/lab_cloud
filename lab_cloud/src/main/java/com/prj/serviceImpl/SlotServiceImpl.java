package com.prj.serviceImpl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.prj.dao.AdministratorDao;
import com.prj.dao.SlotDao;
import com.prj.entity.Administrator;
import com.prj.entity.Reservation.SlotNo;
import com.prj.entity.Slot;
import com.prj.service.SlotService;
import com.prj.util.DataWrapper;
import com.prj.util.ErrorCodeEnum;

@Service("SlotServiceImpl")
public class SlotServiceImpl implements SlotService {
	@Resource(name = "AdministratorDaoImpl")
	AdministratorDao ad;
	@Resource(name = "SlotDaoImpl")
	SlotDao sld;
	
	@Override
	public DataWrapper<Slot> setSlotTime(Integer adminId, Slot slot) {
		DataWrapper<Slot> ret = new DataWrapper<Slot>();
		Slot sl = sld.getSlot(slot.getSlotNo());
		if (sl == null) {
			return ret.setErrorCode(ErrorCodeEnum.Slot_Not_Exist);
		}
		Administrator a = ad.findAdministratorById(adminId);
		if (a == null) {
			return ret.setErrorCode(ErrorCodeEnum.Account_Not_Exist);
		}
		sl.setStartTime(slot.getStartTime());
		sl.setEndTime(slot.getEndTime());
		sl.setUpdater(a);
		sl.setIsActive(slot.getIsActive());
		return ret.setData(sld.updateSlot(sl));
	}

	@Override
	public DataWrapper<Slot> getSlot(SlotNo slotNo) {
		DataWrapper<Slot> ret = new DataWrapper<Slot>();
		Slot sl = sld.getSlot(slotNo);
		if (sl == null) {
			return ret.setErrorCode(ErrorCodeEnum.Slot_Not_Exist);
		}
		return ret.setData(sl);
	}
}
