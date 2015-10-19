package com.prj.serviceImpl;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.criterion.SimpleExpression;
import org.springframework.stereotype.Service;

import com.prj.dao.AdministratorDao;
import com.prj.dao.ExperimentDao;
import com.prj.dao.LabDao;
import com.prj.dao.LabPlanDao;
import com.prj.entity.Administrator;
import com.prj.entity.Experiment;
import com.prj.entity.Lab;
import com.prj.entity.LabPlan;
import com.prj.entity.LabPlan.OpenStatus;
import com.prj.entity.LabPlan.SlotType;
import com.prj.entity.LabPlan.Weekday;
import com.prj.entity.Reservation.SlotNo;
import com.prj.service.LabService;
import com.prj.util.DataWrapper;
import com.prj.util.ErrorCodeEnum;
import com.prj.util.Page;

@Service("LabServiceImpl")
public class LabServiceImpl implements LabService {
	@Resource(name = "LabDaoImpl")
	LabDao labDao;
	@Resource(name = "AdministratorDaoImpl")
	AdministratorDao adminDao;
	@Resource(name = "ExperimentDaoImpl")
	ExperimentDao experimentdao;
	@Resource(name = "LabPlanDaoImpl")
	LabPlanDao labPlanDao;

	public DataWrapper<Lab> addLab(Integer id, Lab lab) {
		DataWrapper<Lab> ret = new DataWrapper<Lab>();
		Lab l = labDao.getActiveLabByNumber(lab.getLabNumber());
		if (l != null) {
			ret.setErrorCode(ErrorCodeEnum.Lab_Exist);
		} else if (labDao.isFull()) {
			ret.setErrorCode(ErrorCodeEnum.Reach_Lab_Limit);
		} else {
			Administrator a = adminDao.findAdministratorById(id);
			lab.setCreator(a);
			lab.setLastUpdater(a);		
			lab.setIsActive(true);
			lab.setStatus(Lab.Status.OPEN);
			
			if (labDao.addLab(lab) == null) {
				ret.setErrorCode(ErrorCodeEnum.Unknown_Error);
			} else {
				ret.setData(lab);
			}
		} 
		return ret;
	}

	public DataWrapper<Lab> deleteLabById(Integer id, Integer updaterId) {
		Lab l = labDao.findLabById(id);
		DataWrapper<Lab> ret = new DataWrapper<Lab>(l);
		if (l == null) {
			ret.setErrorCode(ErrorCodeEnum.Lab_Not_Exist);
		} else {
			l.setLastUpdater(adminDao.findAdministratorById(updaterId));
			l.setIsActive(false);
			l.setStatus(Lab.Status.CLOSED);
			labDao.updateLab(l);
//			labDao.deleteLabById(id);
		}
		return ret;
	}

	public DataWrapper<List<Lab>> getAllLab() {
		return labDao.getAllLab();
	}

	public DataWrapper<List<Lab>> getAllActiveLab() {
		return labDao.getAllActiveLab();
	}

	public DataWrapper<Lab> getLabById(int id) {
		DataWrapper<Lab> ret = new DataWrapper<Lab>();
		Lab a = labDao.findLabById(id);
		ret.setData(a);
		if (a == null) {
			ret.setErrorCode(ErrorCodeEnum.Lab_Not_Exist);
		}
		return ret;
	}

	public DataWrapper<Lab> updateLab(Integer id, Lab v, Integer adminId) {
		Lab a = labDao.findLabById(id);
		DataWrapper<Lab> ret = new DataWrapper<Lab>(a);
		if (a == null) {
			ret.setErrorCode(ErrorCodeEnum.Lab_Not_Exist);
		} else {
			a.setLabNumber(v.getLabNumber());
			a.setCapacity(v.getCapacity());
			a.setDescription(v.getDescription());
			a.setLabName(v.getLabName());
			a.setMachineNumber(v.getMachineNumber());
			a.setStatus(v.getStatus());
			a.setLastUpdater(adminDao.findAdministratorById(adminId));
			labDao.updateLab(a);
		}
		return ret;
	}

	public Page<Lab> getByPageWithConditions(int pagenumber,
			int pagesize, List<SimpleExpression> list) {
		return labDao.getByPageWithConditions(pagenumber, pagesize, list);
	}

	@Override
	public DataWrapper<Lab> addExperiment(int labid, int experimentid) {
		DataWrapper<Lab> ret =  new DataWrapper<Lab>();
		Experiment e = experimentdao.findExperimentById(experimentid);
		if (e == null || e.getIsActive() == false) {
			ret.setErrorCode(ErrorCodeEnum.Experiment_Not_Exist);
			return ret;
		}
		Lab l = labDao.findLabById(labid);
		if (l == null) {
			ret.setErrorCode(ErrorCodeEnum.Lab_Not_Exist);
			//FIXME
		} else if (Lab.contains(l.getExperiments(), e)) {
			ret.setErrorCode(ErrorCodeEnum.Experiment_Exist);
		} else {
			if (e.getMaximumStudent() > l.getCapacity())
				return ret.setErrorCode(ErrorCodeEnum.Experiment_Max_Student_Out_Of_Range);
			l.getExperiments().add(e);
			ret.setData(labDao.updateLab(l));
		}
		return ret;
	}

	@Override
	public DataWrapper<Lab> deleteExperiment(int labid, int experimentid) {
		// TODO Auto-generated method stub
		DataWrapper<Lab> ret = new DataWrapper<Lab>();
		Lab l = labDao.findLabById(labid);
		if(l == null||l.getIsActive() == false)
		{
			ret.setErrorCode(ErrorCodeEnum.Lab_Not_Exist);
			return ret;
		}
		Set<Experiment> es = l.getExperiments();
		boolean isExist = false;
		for(Experiment e : es)
		{
			if(e.getId() == experimentid)
				isExist = true;
		}
		if(!isExist)
		{
			ret.setErrorCode(ErrorCodeEnum.Experiment_Not_Included);
			return ret;
		}
		Experiment experiment = experimentdao.findExperimentById(experimentid);
		if(experiment == null||experiment.getIsActive() == false)
			ret.setErrorCode(ErrorCodeEnum.Experiment_Not_Exist);
		else
			if(!labDao.deleteExperiment(labid, experiment))
				ret.setErrorCode(ErrorCodeEnum.Unknown_Error);
				
		return ret;
	}

	@Override
	public DataWrapper<List<Experiment>> getExperimentList(int labid) {
		// TODO Auto-generated method stub
		DataWrapper<List<Experiment>> ret = new DataWrapper<List<Experiment>>();
		List<Experiment> list = labDao.getExperimentsOfLab(labid);
		
		if(list == null)
			ret.setErrorCode(ErrorCodeEnum.Lab_Not_Exist);
		else
		{
			for(Experiment e : list)
			{
				e.setLabs(null);
			}
			ret.setData(list);
		}
		return ret;
	}
	
	// Methods Following Are Not Checked... YET!
	public Page<Lab> getLabByPage(int pagenumber, int pagesize) {
		return labDao.getLabByPage(pagenumber, pagesize);
	}

	public Page<Lab> searchLab(int pagenumber, int pagesize,
			String name) {
		//return dao.searchLab(pagenumber, pagesize, name);
		return null;
	}

	@Override
	public DataWrapper<LabPlan> setLabSlotStatus(int labId, Date date, SlotNo slot, OpenStatus openStatus) {
		DataWrapper<LabPlan> ret = new DataWrapper<LabPlan>();
		LabPlan lp = labPlanDao.getLabPlanByDate(labId, date);
		if (lp == null) return ret.setErrorCode(ErrorCodeEnum.LabPlan_Not_Exist);
		try {
			Method m = LabPlan.class.getMethod("getSlot"+(slot.ordinal()+1)+"OpenStatus");
			OpenStatus os = (OpenStatus)m.invoke(lp);
			m = LabPlan.class.getMethod("setSlot"+(slot.ordinal()+1)+"OpenStatus", OpenStatus.class);
			if (os == OpenStatus.OPEN && openStatus == OpenStatus.CLOSED
					|| os == OpenStatus.CLOSED && openStatus == OpenStatus.OPEN) {
				m.invoke(lp, openStatus);
				ret.setData(labPlanDao.updateLabPlan(lp));
			} else {
				ret.setErrorCode(ErrorCodeEnum.Status_Cannot_Set);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	@Override
	public DataWrapper<LabPlan> setLabSlotType(int labId, Date date, SlotNo slot, SlotType slotType, Integer stuReservationMax) {
		DataWrapper<LabPlan> ret = new DataWrapper<LabPlan>();
		LabPlan lp = labPlanDao.getLabPlanByDate(labId, date);
		if (lp == null) return ret.setErrorCode(ErrorCodeEnum.LabPlan_Not_Exist);
		try {
			Method m = LabPlan.class.getMethod("getSlot"+(slot.ordinal()+1)+"OpenStatus");
			OpenStatus os = (OpenStatus)m.invoke(lp);
			if (os == OpenStatus.OPEN || os == OpenStatus.CLOSED) {
				m = LabPlan.class.getMethod("setSlot"+(slot.ordinal()+1)+"SlotType", SlotType.class);
				m.invoke(lp, slotType);
				if (slotType == SlotType.STUDENT) {
					m = LabPlan.class.getMethod("setSlot"+(slot.ordinal()+1)+"StudentReservationMax", Integer.class);
					m.invoke(lp, stuReservationMax);
				}
			} else {
				return ret.setErrorCode(ErrorCodeEnum.LabPlan_Slot_Occupied);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public DataWrapper<Void> closeLabSlot(int id, SlotNo slot) {
		DataWrapper<Void> ret = new DataWrapper<Void>();
		if (labPlanDao.getSlotOccupiedNum(id, slot) == 0) {
			labPlanDao.closeLabSlot(id, slot);
		} else {
			ret.setErrorCode(ErrorCodeEnum.Slot_Occupied);
		}
		return ret;
	}

	@Override
	public DataWrapper<Void> closeLabSlot(int id, SlotNo slot, Weekday weekday) {
		DataWrapper<Void> ret = new DataWrapper<Void>();
		if (labPlanDao.getSlotOccupiedNum(id, slot, weekday) == 0) {
			labPlanDao.closeLabSlot(id, slot, weekday);
		} else {
			ret.setErrorCode(ErrorCodeEnum.Slot_Occupied);
		}
		return ret;
	}
}
