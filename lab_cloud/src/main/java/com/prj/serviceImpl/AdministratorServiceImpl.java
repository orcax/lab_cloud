package com.prj.serviceImpl;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.criterion.SimpleExpression;
import org.springframework.stereotype.Service;

import com.prj.dao.AdministratorDao;
import com.prj.dao.AdministratorLabReserveDao;
import com.prj.dao.ClassDao;
import com.prj.dao.ClassReservationDao;
import com.prj.dao.ExperimentDao;
import com.prj.dao.LabDao;
import com.prj.dao.LabPlanDao;
import com.prj.dao.SemesterDao;
import com.prj.dao.SlotDao;
import com.prj.dao.SlotReservationDao;
import com.prj.dao.StudentDao;
import com.prj.dao.StudentReservationDao;
import com.prj.dao.TeacherDao;
import com.prj.entity.Account;
import com.prj.entity.Account.Status;
import com.prj.entity.Administrator;
import com.prj.entity.Class;
import com.prj.entity.ClassReservation;
import com.prj.entity.Experiment;
import com.prj.entity.InfoEntry;
import com.prj.entity.Lab;
import com.prj.entity.LabPlan;
import com.prj.entity.Slot;
import com.prj.entity.LabPlan.ApprovalStatus;
import com.prj.entity.LabPlan.OpenStatus;
import com.prj.entity.LabPlan.SlotType;
import com.prj.entity.Reservation;
import com.prj.entity.Reservation.SlotNo;
import com.prj.entity.Reservation.Type;
import com.prj.entity.SlotReservation;
import com.prj.entity.StudentClass;
import com.prj.entity.StudentReservation;
import com.prj.entity.Teacher;
import com.prj.entity.TeacherLabReserve;
import com.prj.service.AccountService;
import com.prj.service.AdministratorService;
import com.prj.util.AccountCharacter;
import com.prj.util.AdministratorApproval;
import com.prj.util.AdministratorApproval.ApprovalResult;
import com.prj.util.DataWrapper;
import com.prj.util.DateUtils;
import com.prj.util.ErrorCodeEnum;
import com.prj.util.MD5Tool;
import com.prj.util.Page;
import com.prj.util.PasswordReset;

@Service("AdministratorServiceImpl")
public class AdministratorServiceImpl implements AdministratorService {
	@Resource(name = "AccountServiceImpl")
	AccountService as;
	@Resource(name = "AdministratorDaoImpl")
	AdministratorDao administratorDao;
	@Resource(name = "TeacherDaoImpl")
	TeacherDao teacherDao;
	@Resource(name = "StudentDaoImpl")
	StudentDao studentDao;
	@Resource(name = "AdministratorLabReserveDaoImpl")
	AdministratorLabReserveDao adminLabReserveDao;
	@Resource(name = "SemesterDaoImpl")
	SemesterDao semesterDao;
	@Resource(name = "ExperimentDaoImpl")
	ExperimentDao expdao;
	@Resource(name = "ClassDaoImpl")
	ClassDao classdao;
	@Resource(name = "LabDaoImpl")
	LabDao labdao;
	@Resource(name = "LabPlanDaoImpl")
	LabPlanDao lpdao;
	@Resource(name = "ClassReservationDaoImpl")
	ClassReservationDao crDao;
	@Resource(name = "StudentReservationDaoImpl")
	StudentReservationDao srdao;
	@Resource(name = "SlotReservationDaoImpl")
	SlotReservationDao slrdao;
	@Resource(name = "SlotDaoImpl")
	SlotDao sldao;
	
//	private static final String INITIAL_PASSWORD = "poiuhjklmnbv";
	
	public DataWrapper<Administrator> addAdministrator(Administrator administrator) {
		DataWrapper<Administrator> ret = new DataWrapper<Administrator>();
		Administrator a = administratorDao.getAdministratorByNumber(administrator.getNumber());
		administrator.setPassword(MD5Tool.GetMd5(administrator.getInitialPassword()));
		if (a != null) {
			ret.setErrorCode(ErrorCodeEnum.Account_Exist);
		} else if (administratorDao.addAdministrator(administrator) != null) {
			ret.setData(administrator);
		} else {
			ret.setErrorCode(ErrorCodeEnum.Unknown_Error);
		}
		return ret;
	}

	public DataWrapper<Administrator> disableAdministratorById(Integer id) {
		Administrator a = administratorDao.findAdministratorById(id);
		DataWrapper<Administrator> ret = new DataWrapper<Administrator>();
		if (a == null) {
			ret.setErrorCode(ErrorCodeEnum.Account_Not_Exist);
		} else {
			a.setStatus(Account.Status.INACTIVE);
			a.setLoginToken(null);
			ret.setData(administratorDao.updateAdministrator(a));
		}
		return ret;
	}

	public DataWrapper<List<Administrator>> getAllAdministrator() {
		return administratorDao.getAllAdministrator();
	}

	public DataWrapper<Administrator> getAdministratorById(int id) {
		DataWrapper<Administrator> ret = new DataWrapper<Administrator>();
		Administrator a = administratorDao.findAdministratorById(id);
		ret.setData(a);
		if (a == null) {
			ret.setErrorCode(ErrorCodeEnum.Account_Not_Exist);
		}
		return ret;
	}

	public DataWrapper<Administrator> updateAdministrator(Integer id, Administrator v, boolean isAdmin) {
		// Only set attributes allowed
		Administrator a = administratorDao.findAdministratorById(id);
		DataWrapper<Administrator> ret = new DataWrapper<Administrator>();
		if (a == null) {
			ret.setErrorCode(ErrorCodeEnum.Account_Not_Exist);
		} else {
//			a.setIconPath(v.getIconPath());
			a.setName(v.getName());
			a.setTitle(v.getTitle());
			if (isAdmin) {
				if (administratorDao.getAdministratorByNumber(v.getNumber()) != null) {
					ret.setErrorCode(ErrorCodeEnum.Number_Exist);
				} else {
					a.setNumber(v.getNumber());
				}
			}
			a = administratorDao.updateAdministrator(a);
			ret.setData(a);
		}
		return ret;
	}

	public DataWrapper<Administrator> reset(PasswordReset reset) {
		Administrator a = administratorDao.findAdministratorById(reset.getId());
		DataWrapper<Administrator> ret = new DataWrapper<Administrator>();
		if (a == null) {
			ret.setErrorCode(ErrorCodeEnum.Account_Not_Exist);
		} else if (!a.getPassword().equals(
				MD5Tool.GetMd5(reset.getOldPassword()))) {
			ret.setErrorCode(ErrorCodeEnum.Password_Wrong);
		} else {
			a.setPassword(MD5Tool.GetMd5(reset.getNewPassword()));
			a.setLoginToken(null);
			a.setStatus(Status.ACTIVE);
			administratorDao.updateAdministrator(a);
		}
		return ret;
	}

	public DataWrapper<List<Administrator>> getAdministratorbyPage(int pagenumber, int pagesize) {
		return administratorDao.getAdministratorbyPage(pagenumber, pagesize);
	}

	private Reservation findReservation(int resId, Type type) {
		switch (type) {
		case Class:
			return crDao.findClassReservationById(resId);
		case Slot:
			return slrdao.findSlotReservationById(resId);
		default:
			return null;
		}
	}
	
	private Reservation updateReservation(Reservation res, Type type) {
		switch (type) {
		case Class:
			return crDao.updateClassReservation((ClassReservation) res);
		case Slot:
			return slrdao.updateSlotReservation((SlotReservation) res);
		default:
			return null;
		}
	}
	
	@Override
	public DataWrapper<TeacherLabReserve> approveReservation(
			Integer reservationId, Type resType, AdministratorApproval administratorApproval) {
		DataWrapper<TeacherLabReserve> ret = new DataWrapper<TeacherLabReserve>();
		Reservation res = findReservation(reservationId, resType);
		if (res == null) {
			ret.setErrorCode(ErrorCodeEnum.ClassReservation_Not_Exist);
			return ret;
		} else if (res.getApprovalStatus().equals(ApprovalStatus.APPROVED)) {
			ret.setErrorCode(ErrorCodeEnum.Already_Approved);
			return ret;
		} else if (res.getApprovalStatus().equals(ApprovalStatus.REJECTED)) {
			ret.setErrorCode(ErrorCodeEnum.Already_Rejected);
			return ret;
		}
		if (administratorApproval.getApprovalResult().equals(ApprovalResult.APPROVED)) {
			res.setApprovalStatus(ApprovalStatus.APPROVED);
			res = updateReservation(res, resType);

			TeacherLabReserve adminLabReserve = new TeacherLabReserve();
			adminLabReserve.setTeachers(teacherDao.getTeachersByIds(administratorApproval.getLabTeaIds()));
			
			switch (resType) {
			case Class:
				adminLabReserve.setClassReservation((ClassReservation) res);
				break;
			case Slot:
				adminLabReserve.setSlotReservation((SlotReservation) res);
				break;
			default:
				return ret.setErrorCode(ErrorCodeEnum.Unknown_Error);
			}
			adminLabReserveDao.addAdministratorLabReserve(adminLabReserve);
			ret.setData(adminLabReserve);
		} else {
			res.setApprovalStatus(ApprovalStatus.REJECTED);
			updateReservation(res, resType);
		}
		return ret;
	}
	
	@Override
	public DataWrapper<ClassReservation> getClassReservation(
			Integer reservationId) {
		DataWrapper<ClassReservation> ret = new DataWrapper<ClassReservation>();
		ret.setData(crDao.findClassReservationById(reservationId));
		if (ret.getData() == null) {
			ret.setErrorCode(ErrorCodeEnum.ClassReservation_Not_Exist);
		}
		return ret;
	}
	
//	@Override
//	public DataWrapper<List<Administrator>> getAvailableAdministrators(
//			Integer reservationId) {
//		DataWrapper<List<Administrator>> ret = new DataWrapper<List<Administrator>>();
//		ClassReservation cr = crDao.findClassReservationById(reservationId);
//		if (cr == null) {
//			ret.setErrorCode(ErrorCodeEnum.ClassReservation_Not_Exist);
//		} else {
//			ret.setData(administratorDao.getAvailableAdministratorsByCall(reservationId));
////			List<ClassReservation> crs = crDao.getClassReservationsByDateSlot(cr.getDate(), cr.getSlot());
////			Set<Integer> adminIds = new HashSet<Integer>(); 
////			for (ClassReservation item : crs) {
////				AdministratorLabReserve labReserve = item.getAdministratorLabReserve();
////				if (labReserve != null) {
////					for (Administrator a : labReserve.getAdministrators()) {
////						if (!adminIds.contains(a.getId())) {
////							adminIds.add(a.getId());
////						}
////					}
////				}
////			}
////			ret.setData(adminDao.getAvailableAdministrators(adminIds));
//		}
//		return ret;
//	}

	@Override
	public DataWrapper<List<ClassReservation>> getClassReservationByPageDur(
			ApprovalStatus approvaltatus, Integer pageSize, Integer pageNumber, String startDate, String endDate) {
		return crDao.getClassReservationByPageDur(approvaltatus, pageSize, pageNumber, startDate, endDate);
	}
	
	@Override
	public DataWrapper<List<SlotReservation>> getSlotReservationByPageDur(
			Integer pageSize, Integer pageNumber, String startDate, String endDate) {
		return slrdao.getSlotReservationByPageDur(pageSize, pageNumber, startDate, endDate);
	}
	
//	@Override
//	public DataWrapper<List<ClassReservation>> getPendingClassReservationByPageDur(
//			Integer pageSize, Integer pageNumber, String startDate, String endDate) {
//		return crDao.getPendingClassReservationByPageDur(pageSize, pageNumber, startDate, endDate);
//	}
	
//	@Override
//	public DataWrapper<Semester> getCurSemester() {
//		DataWrapper<Semester> ret = new DataWrapper<Semester>();
//		ret.setData(semesterDao.getCurSemester());
//		if (ret.getData() == null) {
//			ret.setErrorCode(ErrorCodeEnum.Current_Semester_Not_Set);
//		}
//		return ret;
//	}

	@Override
	public DataWrapper<Account> resetById(PasswordReset reset) {
		DataWrapper<Account> ret = new DataWrapper<Account>();
		try {
			AccountCharacter ac = reset.getAccountCharacter();
			Object dao = this.getClass()
					.getDeclaredField(ac.getLowerCaseLabel()+"Dao")
					.get(this);
			Method method = dao.getClass()
					.getMethod("find"+ac.getCapitalizedLabel()+"ById", Integer.class);
			Account a = (Account)method.invoke(dao, reset.getId());
			if (a == null) {
				ret.setErrorCode(ErrorCodeEnum.Account_Not_Exist);
			} else {
				a.setPassword(MD5Tool.GetMd5(reset.getNewPassword()));
				a.setLoginToken(null);
				a.setStatus(Status.ACTIVE);
				ret = as.updateAccount(a, ac);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ret.setErrorCode(ErrorCodeEnum.Unknown_Error);
		}
		return ret;
	}

	// Methods Following Are Not Checked... YET!
	public Page<Administrator> searchAdministrator(int pagenumber, int pagesize, String name) {
		// return dao.searchAdministrator(pagenumber, pagesize, name);
		return null;
	}

	public Page<Administrator> getByPageWithConditions(int pagenumber, int pagesize,
			List<SimpleExpression> list) {
		return administratorDao.getByPageWithConditions(pagenumber, pagesize, list);
	}

	@Override
	public DataWrapper<ClassReservation> classReserver(
			ClassReservation cr, int labid, int expid, int classid) {
		DataWrapper<ClassReservation> ret = new DataWrapper<ClassReservation>();
		Slot sl = sldao.getSlot(cr.getSlot());
		if (sl == null)
			return ret.setErrorCode(ErrorCodeEnum.Slot_Not_Exist);
		if (new Date().getTime() >= cr.getDate().getTime()+DateUtils.timeStr2Long(sl.getStartTime().toString()))
			return ret.setErrorCode(ErrorCodeEnum.Reservation_Expired);
		
		Experiment exp = expdao.findExperimentById(expid);
		if (exp == null) {
			ret.setErrorCode(ErrorCodeEnum.Experiment_Not_Exist);
			return ret;
		}
		cr.setExperiment(exp);

		Class clazz = classdao.findClassById(classid);
		if (clazz == null) {
			ret.setErrorCode(ErrorCodeEnum.Class_Not_Exist);
			return ret;
		}
//		 else if (clazz.getCourse() == null) {
//			ret.setErrorCode(ErrorCodeEnum.Course_Not_Exist);
//			return ret;
//		} else if (clazz.getSemester() == null) {
//			ret.setErrorCode(ErrorCodeEnum.Semester_Not_Exist);
//			return ret;
//		} else if (clazz.getStudentClass() == null
//				|| clazz.getStudentClass().isEmpty()) {
//			ret.setErrorCode(ErrorCodeEnum.Students_Not_Added);
//			return ret;
//		} else if (clazz.getTeacher() == null) {
//			ret.setErrorCode(ErrorCodeEnum.Teacher_Not_Exist);
//			return ret;
//		}
		cr.setClazz(clazz);
		Lab lab = labdao.findLabById(labid);
		if (lab == null) {
			ret.setErrorCode(ErrorCodeEnum.Lab_Not_Exist);
			return ret;
		}
		cr.setLab(lab);
		if (cr.getCount() > lab.getCapacity()) {
			ret.setErrorCode(ErrorCodeEnum.Reservation_Count_Out_Of_Range);
			//return ret;
		}
		cr.setClassReservationNumber(generateReservationNumber(clazz.getId(),
				cr.getDate(), cr.getSlot()));
		LabPlan lp = lpdao.getLabPlanByDate(labid, (cr.getDate()));
		if (lp == null) {
			ret.setErrorCode(ErrorCodeEnum.LabPlan_Not_Exist);
			return ret;
		}
		Method method;
		try {
			method = lp.getClass().getMethod(
					"getSlot" + (cr.getSlot().ordinal() + 1)
							+ "ClassReservation");
			if (method.invoke(lp) != null) {
				ret.setErrorCode(ErrorCodeEnum.LabPlan_Slot_Occupied);
				return ret;
			}
			method = lp.getClass().getMethod(
					"getSlot" + (cr.getSlot().ordinal() + 1) + "Type");
			if (method.invoke(lp) != SlotType.CLASS) {
				ret.setErrorCode(ErrorCodeEnum.Slot_For_Student);
				return ret;
			}
			cr.setLabPlan(lp);
			cr.setApprovalStatus(ApprovalStatus.PENDING);
			Teacher teacher = clazz.getTeacher();
			cr.setReserver(teacher);
			if (crDao.addClassReservation(cr) == null)
				ret.setErrorCode(ErrorCodeEnum.Unknown_Error);

			ret.setData(cr);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			ret.setErrorCode(ErrorCodeEnum.Unknown_Error);
			return ret;
		}
	}
	
	@Override
	public DataWrapper<List<LabPlan>> getPlanByWeekIndex(int index) {
		DataWrapper<List<LabPlan>> ret = new DataWrapper<List<LabPlan>>();
		
		List<LabPlan> list = lpdao.getLabPlanByWeek(index,semesterDao.getCurSemester().getStartDate());

		if (list == null)
			ret.setErrorCode(ErrorCodeEnum.Out_Of_Bound);
		else {
			for (LabPlan lp : list) {
				SimplifyLabPlan(lp);
			}
			ret.setData(list);
		}
		return ret;
	}
	
	@Override
	public DataWrapper<List<LabPlan>> getPlanByWeekIndexByLab(
			int index, int labid) {
		DataWrapper<List<LabPlan>> ret = new DataWrapper<List<LabPlan>>();
		List<LabPlan> list = lpdao.getLabPlanByWeekByLab(index, labid,semesterDao.getCurSemester().getStartDate());

		if (list == null)
			ret.setErrorCode(ErrorCodeEnum.Out_Of_Bound);
		else {
			for (LabPlan lp : list) {
				SimplifyLabPlan(lp);
			}
			ret.setData(list);
		}
		return ret;
	}
	
	private void SimplifyLabPlan(LabPlan lp) {

		Set<StudentReservation> slot1sr = lp.getSlot1StudentReservations();
		Iterator<StudentReservation> it1 = slot1sr.iterator();
		while (it1.hasNext()) {
			StudentReservation sr = it1.next();
			sr.getStudent().setStudentReservation(null);
			if (sr.getSlot() != SlotNo.ONE) {
				it1.remove();
			}
		}

		if (lp.getSlot1ClassReservation() != null) {
			Set<StudentClass> slot1sc = lp.getSlot1ClassReservation()
					.getClazz().getStudentClass();
			Iterator<StudentClass> scit1 = slot1sc.iterator();
			while (scit1.hasNext()) {
				StudentClass sc = scit1.next();
				sc.getStudent().setStudentReservation(null);
			}
		}

		if (lp.getSlot1Experiment() != null)
			lp.getSlot1Experiment().setLabs(null);

		Set<StudentReservation> slot2sr = lp.getSlot2StudentReservations();
		Iterator<StudentReservation> it2 = slot2sr.iterator();
		while (it2.hasNext()) {
			StudentReservation sr = it2.next();
			sr.getStudent().setStudentReservation(null);
			if (sr.getSlot() != SlotNo.TWO) {
				it2.remove();
			}
		}
		if (lp.getSlot2ClassReservation() != null) {
			Set<StudentClass> slot2sc = lp.getSlot2ClassReservation()
					.getClazz().getStudentClass();
			Iterator<StudentClass> scit2 = slot2sc.iterator();
			while (scit2.hasNext()) {
				StudentClass sc = scit2.next();
				sc.getStudent().setStudentReservation(null);
			}
		}

		if (lp.getSlot2Experiment() != null)
			lp.getSlot2Experiment().setLabs(null);

		Set<StudentReservation> slot3sr = lp.getSlot3StudentReservations();
		Iterator<StudentReservation> it3 = slot3sr.iterator();
		while (it3.hasNext()) {
			StudentReservation sr = it3.next();
			sr.getStudent().setStudentReservation(null);
			if (sr.getSlot() != SlotNo.THREE) {
				it3.remove();
			}
		}

		if (lp.getSlot3ClassReservation() != null) {
			Set<StudentClass> slot3sc = lp.getSlot3ClassReservation()
					.getClazz().getStudentClass();
			Iterator<StudentClass> scit3 = slot3sc.iterator();
			while (scit3.hasNext()) {
				StudentClass sc = scit3.next();
				sc.getStudent().setStudentReservation(null);
			}
		}

		if (lp.getSlot3Experiment() != null)
			lp.getSlot3Experiment().setLabs(null);

		Set<StudentReservation> slot4sr = lp.getSlot4StudentReservations();
		Iterator<StudentReservation> it4 = slot4sr.iterator();
		while (it4.hasNext()) {
			StudentReservation sr = it4.next();
			sr.getStudent().setStudentReservation(null);
			if (sr.getSlot() != SlotNo.FOUR) {
				it4.remove();
			}
		}

		if (lp.getSlot4ClassReservation() != null) {
			Set<StudentClass> slot4sc = lp.getSlot4ClassReservation()
					.getClazz().getStudentClass();
			Iterator<StudentClass> scit4 = slot4sc.iterator();
			while (scit4.hasNext()) {
				StudentClass sc = scit4.next();
				sc.getStudent().setStudentReservation(null);
			}
		}

		if (lp.getSlot4Experiment() != null)
			lp.getSlot4Experiment().setLabs(null);

		Set<StudentReservation> slot5sr = lp.getSlot5StudentReservations();
		Iterator<StudentReservation> it5 = slot5sr.iterator();
		while (it5.hasNext()) {
			StudentReservation sr = it5.next();
			sr.getStudent().setStudentReservation(null);
			if (sr.getSlot() != SlotNo.FIVE) {
				it5.remove();
			}
		}

		if (lp.getSlot5ClassReservation() != null) {
			Set<StudentClass> slot5sc = lp.getSlot5ClassReservation()
					.getClazz().getStudentClass();
			Iterator<StudentClass> scit5 = slot5sc.iterator();
			while (scit5.hasNext()) {
				StudentClass sc = scit5.next();
				sc.getStudent().setStudentReservation(null);
			}
		}

		if (lp.getSlot5Experiment() != null)
			lp.getSlot5Experiment().setLabs(null);

		Set<StudentReservation> slot6sr = lp.getSlot6StudentReservations();
		Iterator<StudentReservation> it6 = slot6sr.iterator();
		while (it6.hasNext()) {
			StudentReservation sr = it6.next();
			sr.getStudent().setStudentReservation(null);
			if (sr.getSlot() != SlotNo.SIX) {
				it6.remove();
			}
		}

		if (lp.getSlot6ClassReservation() != null) {
			Set<StudentClass> slot6sc = lp.getSlot6ClassReservation()
					.getClazz().getStudentClass();
			Iterator<StudentClass> scit6 = slot6sc.iterator();
			while (scit6.hasNext()) {
				StudentClass sc = scit6.next();
				sc.getStudent().setStudentReservation(null);
			}
		}

		if (lp.getSlot6Experiment() != null)
			lp.getSlot6Experiment().setLabs(null);
	}
	
	private String generateReservationNumber(Integer classId, Date date,
			SlotNo slot) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return String.format("1%d%02d%02d%d%06d", cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH),
				slot.ordinal() + 1, classId);
	}
	
	@Override
	public DataWrapper<List<InfoEntry>> getExpInfoListByClassLab(
			Integer courseId, Integer labId) {
		return new DataWrapper<List<InfoEntry>>(
				expdao.getExpInfoListByClassLab(courseId, labId));
	}

	@Override
	public DataWrapper<List<StudentReservation>> getStudentReservationByPageDur(
			ApprovalStatus approvaltatus, 
			Integer pageSize, Integer pageNumber, 
			String startDate, String endDate) {
		return srdao.getClassReservationByPageDur(approvaltatus, pageSize, pageNumber, startDate, endDate);
	}

	@Override
	public DataWrapper<SlotReservation> addSlotReservation(Integer adminId,
			int labId, int expId, SlotReservation slr) {
		DataWrapper<SlotReservation> ret = new DataWrapper<SlotReservation>();
		Slot sl = sldao.getSlot(slr.getSlot());
		if (sl == null)
			return ret.setErrorCode(ErrorCodeEnum.Slot_Not_Exist);
		if (new Date().getTime() >= slr.getDate().getTime()+DateUtils.timeStr2Long(sl.getStartTime().toString()))
			return ret.setErrorCode(ErrorCodeEnum.Reservation_Expired);
		
		try {
			LabPlan lp = lpdao.getLabPlanByDate(labId, slr.getDate());
			if (lp == null)
				return ret.setErrorCode(ErrorCodeEnum.LabPlan_Not_Exist);
			Method m = lp.getClass().getMethod("getSlot"+(slr.getSlot().ordinal()+1)+"OpenStatus");
			OpenStatus os = (OpenStatus) m.invoke(lp);
			if (os != OpenStatus.OPEN)
				return ret.setErrorCode(ErrorCodeEnum.Slot_Unavailable);
//			m = lp.getClass().getMethod("getSlot"+(slot.ordinal()+1)+"Type");
//			SlotType st = (SlotType) m.invoke(lp);
//			if (st == SlotType.CLASS)
//				return ret.setErrorCode(ErrorCodeEnum.Slot_Occupied);
//			m = lp.getClass().getMethod("setSlot"+(slot.ordinal()+1)+"Type", SlotType.class);
//			m.invoke(lp, SlotType.CLASS);
//			lpdao.updateLabPlan(lp);
			
			Administrator a = administratorDao.findAdministratorById(adminId);
			if (a == null)
				return ret.setErrorCode(ErrorCodeEnum.Account_Not_Exist);
			Lab l = labdao.findLabById(labId);
			if (l == null)
				return ret.setErrorCode(ErrorCodeEnum.Lab_Not_Exist);
			if (slr.getMax() > l.getCapacity())
				ret.setErrorCode(ErrorCodeEnum.Reservation_Count_Out_Of_Range);
			Experiment e = expdao.findExperimentById(expId);
			if (e == null)
				return ret.setErrorCode(ErrorCodeEnum.Experiment_Not_Exist);
			if (!Experiment.contains(l.getExperiments(), e))
				return ret.setErrorCode(ErrorCodeEnum.Experiment_Not_Included);
			
			slr.setApprovalStatus(ApprovalStatus.APPROVED);
			slr.setOccupiedNumber(0);
			slr.setReserver(a);
			slr.setLab(l);
			slr.setExperiment(e);
			slr.setLabPlan(lp);
			slrdao.addSlotReservation(slr);
			return ret.setData(slr);
		} catch (Exception e) {
			e.printStackTrace();
			return ret.setErrorCode(ErrorCodeEnum.Unknown_Error);
		}
	}

	@Override
	public DataWrapper<List<SlotReservation>> getSlotReservationPage(
			Integer pageSize, Integer pageNumber) {
		return slrdao.getSlotReservationPage(pageSize, pageNumber);
	}
}
