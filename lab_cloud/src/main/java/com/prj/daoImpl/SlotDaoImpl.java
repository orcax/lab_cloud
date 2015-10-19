package com.prj.daoImpl;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.prj.dao.AbstractHibernateDao;
import com.prj.dao.SlotDao;
import com.prj.entity.Reservation.SlotNo;
import com.prj.entity.Slot;

@Service("SlotDaoImpl")
public class SlotDaoImpl extends AbstractHibernateDao<Slot,Integer> implements SlotDao{
	
	protected SlotDaoImpl(){
		super(Slot.class);
	}

	@Override
	public Slot updateSlot(Slot c) {
		return saveOrUpdate(c);
	}

	@Override
	public Slot getSlot(SlotNo slotNo) {
		return (Slot) super.createCriteria()
				.add(Restrictions.and(
						Restrictions.eq("slotNo", slotNo),
						Restrictions.eq("isActive", true)))
				.uniqueResult();
	}
}
