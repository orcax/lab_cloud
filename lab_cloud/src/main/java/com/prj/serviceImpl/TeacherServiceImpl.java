package com.prj.serviceImpl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.criterion.SimpleExpression;
import org.springframework.stereotype.Service;

import com.prj.dao.AdministratorDao;
import com.prj.dao.ClassDao;
import com.prj.dao.ClassReservationDao;
import com.prj.dao.ExperimentDao;
import com.prj.dao.LabDao;
import com.prj.dao.LabPlanDao;
import com.prj.dao.SemesterDao;
import com.prj.dao.StudentRecordDao;
import com.prj.dao.TeacherDao;
import com.prj.entity.Account;
import com.prj.entity.Account.Status;
import com.prj.entity.Class;
import com.prj.entity.ClassReservation;
import com.prj.entity.Experiment;
import com.prj.entity.InfoEntry;
import com.prj.entity.Lab;
import com.prj.entity.LabPlan;
import com.prj.entity.LabPlan.ApprovalStatus;
import com.prj.entity.LabPlan.SlotType;
import com.prj.entity.Reservation.SlotNo;
import com.prj.entity.StudentClass;
import com.prj.entity.StudentRecord;
import com.prj.entity.StudentReservation;
import com.prj.entity.Teacher;
import com.prj.entity.Teacher.Role;
import com.prj.service.TeacherService;
import com.prj.util.DataWrapper;
import com.prj.util.ErrorCodeEnum;
import com.prj.util.MD5Tool;
import com.prj.util.Page;
import com.prj.util.PasswordReset;

@Service("TeacherServiceImpl")
public class TeacherServiceImpl implements TeacherService {

	@Resource(name = "TeacherDaoImpl")
	TeacherDao dao;
	@Resource(name = "LabPlanDaoImpl")
	LabPlanDao lpdao;
	@Resource(name = "ClassReservationDaoImpl")
	ClassReservationDao crdao;
	@Resource(name = "ExperimentDaoImpl")
	ExperimentDao expdao;
	@Resource(name = "AdministratorDaoImpl")
	AdministratorDao admindao;
	@Resource(name = "ClassDaoImpl")
	ClassDao classdao;
	@Resource(name = "LabDaoImpl")
	LabDao labdao;
	@Resource(name = "StudentRecordDaoImpl")
	StudentRecordDao srDao;
	@Resource(name = "SemesterDaoImpl")
	SemesterDao semesterDao;
	
//	private static final String INITIAL_PASSWORD = "qazwsxedc";

	public DataWrapper<Teacher> addTeacher(Teacher teacher) {
		DataWrapper<Teacher> ret = new DataWrapper<Teacher>();
		Teacher a = dao.getTeacherByNumber(teacher.getNumber());
		teacher.setPassword(MD5Tool.GetMd5(teacher.getInitialPassword()));
		if (teacher.getRole() == null) {
			teacher.setRole(Role.NORMAL);
		}
		if (a != null) {
			ret.setErrorCode(ErrorCodeEnum.Account_Exist);
		} else if (dao.addTeacher(teacher) != null) {
			ret.setData(teacher);
		} else {
			ret.setErrorCode(ErrorCodeEnum.Unknown_Error);
		}
		return ret;
	}

	public DataWrapper<Teacher> disableTeacherById(Integer id) {
		Teacher a = dao.findTeacherById(id);
		DataWrapper<Teacher> ret = new DataWrapper<Teacher>();
		if (a == null) {
			ret.setErrorCode(ErrorCodeEnum.Account_Not_Exist);
		} else {
			a.setStatus(Account.Status.INACTIVE);
			a.setLoginToken(null);
			ret.setData(dao.updateTeacher(a));
		}
		return ret;
	}

	public DataWrapper<List<Teacher>> getAllTeacher() {
		return dao.getAllTeacher();
	}

	public DataWrapper<Set<Class>> getTeacherClasses(int id) {
		DataWrapper<Set<Class>> ret = new DataWrapper<Set<Class>>();
		Teacher teacher = dao.findTeacherById(id);
		ret.setData(teacher.getClasses());
		return ret;
	}

	public DataWrapper<Teacher> getTeacherById(int id) {
		DataWrapper<Teacher> ret = new DataWrapper<Teacher>();
		Teacher a = dao.findTeacherById(id);
		ret.setData(a);
		if (a == null) {
			ret.setErrorCode(ErrorCodeEnum.Account_Not_Exist);
		}
		return ret;
	}

	@Override
	public DataWrapper<Teacher> updateTeacher(Integer id, Teacher v,
			boolean isAdmin) {
		// Only set attributes allowed
		Teacher t = dao.findTeacherById(id);
		DataWrapper<Teacher> ret = new DataWrapper<Teacher>();
		if (t == null) {
			ret.setErrorCode(ErrorCodeEnum.Account_Not_Exist);
		} else {
			t.setGender(v.getGender());
			// t.setIconPath(v.getIconPath());
			t.setName(v.getName());
			t.setTitle(v.getTitle());
			t.setRole(v.getRole());
			if (isAdmin) {
				if (!t.getNumber().equals(v.getNumber())
						&& dao.getTeacherByNumber(v.getNumber()) != null) {
					ret.setErrorCode(ErrorCodeEnum.Number_Exist);
				} else {
					t.setNumber(v.getNumber());
				}
			}
			t = dao.updateTeacher(t);
			ret.setData(t);
		}
		return ret;
	}

	public DataWrapper<Teacher> reset(PasswordReset reset) {
		Teacher a = dao.findTeacherById(reset.getId());
		DataWrapper<Teacher> ret = new DataWrapper<Teacher>();
		if (a == null) {
			ret.setErrorCode(ErrorCodeEnum.Account_Not_Exist);
		} else if (!a.getPassword().equals(
				MD5Tool.GetMd5(reset.getOldPassword()))) {
			ret.setErrorCode(ErrorCodeEnum.Password_Wrong);
		} else {
			a.setPassword(MD5Tool.GetMd5(reset.getNewPassword()));
			a.setLoginToken(null);
			a.setStatus(Status.ACTIVE);
			dao.updateTeacher(a);
		}
		return ret;
	}

	public DataWrapper<List<Teacher>> getTeacherByRolePage(Role role,
			int pagenumber, int pagesize) {
		return dao.getTeacherByRolePage(role, pagenumber, pagesize);
	}

	// Methods Following Are Not Checked... YET!
	public Page<Teacher> searchTeacher(int pagenumber, int pagesize, String name) {
		// return dao.searchTeacher(pagenumber, pagesize, name);
		return null;
	}

	public Page<Teacher> getByPageWithConditions(int pagenumber, int pagesize,
			List<SimpleExpression> list) {
		return dao.getByPageWithConditions(pagenumber, pagesize, list);
	}

	// FIXME TOKEN
	@Override
	public DataWrapper<List<LabPlan>> getPlanByWeekIndexByLab(String token,
			int index, int labid) {
		DataWrapper<List<LabPlan>> ret = new DataWrapper<List<LabPlan>>();
		List<LabPlan> list = lpdao.getLabPlanByWeekByLab(index, labid,semesterDao.getCurSemester().getStartDate());
		// Teacher teacher = dao.findTeacherByToken(token);

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

	// TODO CHECK
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

	// //TODO CHECK
	// @Override
	// public DataWrapper<LabPlan> getPlanByDate(String token, Date date) {
	// DataWrapper<LabPlan> ret = new DataWrapper<LabPlan>();
	// LabPlan lp = lpdao.getLabPlanByDate(date);
	// Teacher teacher = dao.findTeacherByToken(token);
	//
	// if(lp!=null)
	// {
	// SimplifyLabPlan(lp,teacher);
	// ret.setData(lp);
	// }
	// else
	// {
	// ret.setErrorCode(ErrorCodeEnum.LabPlan_Not_Exist);
	// }
	// return ret;
	// }

	// FIXME TOKEN
	// TODO CHECK class完整性检查 去除labplan的exper occupied设置
	// 自动生成classReservationNumber
	@Override
	public DataWrapper<ClassReservation> classReserver(Integer teacherId,
			ClassReservation cr, int labid, int expid, int classid) {
		DataWrapper<ClassReservation> ret = new DataWrapper<ClassReservation>();
		System.out.println(cr.getDate());
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
		// else if (clazz.getCourse() == null) {
		// ret.setErrorCode(ErrorCodeEnum.Course_Not_Exist);
		// return ret;
		// } else if (clazz.getSemester() == null) {
		// ret.setErrorCode(ErrorCodeEnum.Semester_Not_Exist);
		// return ret;
		// } else if (clazz.getStudentClass() == null
		// || clazz.getStudentClass().isEmpty()) {
		// ret.setErrorCode(ErrorCodeEnum.Students_Not_Added);
		// return ret;
		// } else if (clazz.getTeacher() == null) {
		// ret.setErrorCode(ErrorCodeEnum.Teacher_Not_Exist);
		// return ret;
		// }
		cr.setClazz(clazz);
		Lab lab = labdao.findLabById(labid);
		if (lab == null) {
			ret.setErrorCode(ErrorCodeEnum.Lab_Not_Exist);
			return ret;
		}
		cr.setLab(lab);
		if (cr.getCount() > lab.getCapacity()) {
			ret.setErrorCode(ErrorCodeEnum.Reservation_Count_Out_Of_Range);
			return ret;
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
			Teacher teacher = dao.findTeacherById(teacherId);
			cr.setReserver(teacher);
			if (crdao.addClassReservation(cr) == null)
				ret.setErrorCode(ErrorCodeEnum.Unknown_Error);

			ret.setData(cr);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			ret.setErrorCode(ErrorCodeEnum.Unknown_Error);
			return ret;
		}
		// if(crdao.addClassReservation(cr)!=0){
		// LabPlan newlp = lpdao.findLabPlanById(lp.getId());
		// if(cr.getSlot()==Slot.ONE){
		// // newlp.setSlot1Experiment(exp);
		// // newlp.setSlot1OccupiedNumber(number);
		// lpdao.updateLabPlan(newlp);
		// }
		// else if(cr.getSlot()==Slot.TWO){
		// // newlp.setSlot2Experiment(exp);
		// // newlp.setSlot2OccupiedNumber(number);
		// lpdao.updateLabPlan(newlp);
		// }
		// else if(cr.getSlot()==Slot.THREE){
		// // newlp.setSlot3Experiment(exp);
		// // newlp.setSlot3OccupiedNumber(number);
		// lpdao.updateLabPlan(newlp);
		// }
		// else if(cr.getSlot()==Slot.FOUR){
		// // newlp.setSlot4Experiment(exp);
		// // newlp.setSlot4OccupiedNumber(number);
		// lpdao.updateLabPlan(newlp);
		// }
		// else if(cr.getSlot()==Slot.FIVE){
		// newlp.setSlot5Experiment(exp);
		// // newlp.setSlot5OccupiedNumber(number);
		// lpdao.updateLabPlan(newlp);
		// }
		// else if(cr.getSlot()==Slot.SIX){
		// // newlp.setSlot6Experiment(exp);
		// // newlp.setSlot6OccupiedNumber(number);
		// lpdao.updateLabPlan(newlp);
		// }
		// }
		// else
		// ret.setErrorCode(ErrorCodeEnum.Unknown_Error);
		//
		// return ret;
	}

	private String generateReservationNumber(Integer classId, Date date,
			SlotNo slot) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return String.format("1%d%02d%02d%d%06d", cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH),
				slot.ordinal() + 1, classId);
		// StringBuffer buf = new StringBuffer();
		// buf.append('1');
		// buf.append(classId);
		// buf.append();
		// buf.append(cal.get(Calendar.MONTH)+1);
		// buf.append(cal.get(Calendar.DAY_OF_MONTH));
		// buf.append(slot.ordinal());
		// return buf.toString();
	}

	@Override
	public DataWrapper<List<ClassReservation>> getClassReservationByPageAS(
			int pagenumber, int pagesize, int id, ApprovalStatus as) {
		DataWrapper<List<ClassReservation>> ret = new DataWrapper<List<ClassReservation>>();

		Teacher teacher = dao.findTeacherById(id);
		if (teacher == null) {
			ret.setErrorCode(ErrorCodeEnum.Teacher_Not_Exist);
			return ret;
		}

		ret = crdao.getClassReservationByTeacherByPageAS(pagenumber, pagesize,
				teacher, as);
		return ret;
	}

	@Override
	public DataWrapper<ClassReservation> deleteClassReservationById(int id) {
		DataWrapper<ClassReservation> ret = new DataWrapper<ClassReservation>();
		ClassReservation cr = crdao.findClassReservationById(id);

		if (cr == null) {
			ret.setErrorCode(ErrorCodeEnum.Reservation_Not_Exist);
			return ret;
		}
		if (cr.getApprovalStatus() != ApprovalStatus.PENDING) {
			ret.setErrorCode(ErrorCodeEnum.ApprovalStatus_Not_Pending);
			return ret;
		}
		crdao.deleteClassReservationById(id);
		return ret;
	}

	@Override
	public DataWrapper<ClassReservation> getClassReservationById(int id) {
		// TODO Auto-generated method stub
		DataWrapper<ClassReservation> ret = new DataWrapper<ClassReservation>();
		ClassReservation cr = crdao.findClassReservationById(id);
		if (cr == null) {
			ret.setErrorCode(ErrorCodeEnum.Reservation_Not_Exist);
			return ret;
		}
		ret.setData(cr);
		return ret;
	}

	@Override
	public DataWrapper<ClassReservation> updateClassReservationById(int id,
			ClassReservation cr, int labid, int expid, int classid) {
		// TODO Auto-generated method stub
		DataWrapper<ClassReservation> ret = new DataWrapper<ClassReservation>();
		ClassReservation classreservation = crdao.findClassReservationById(id);
		if (classreservation == null) {
			ret.setErrorCode(ErrorCodeEnum.Reservation_Not_Exist);
			return ret;
		}
		Experiment exp = expdao.findExperimentById(expid);
		if (exp == null) {
			ret.setErrorCode(ErrorCodeEnum.Experiment_Not_Exist);
			return ret;
		}
		classreservation.setExperiment(exp);
		Class clazz = classdao.findClassById(classid);
		if (clazz == null) {
			ret.setErrorCode(ErrorCodeEnum.Class_Not_Exist);
			return ret;
		} else if (clazz.getCourse() == null) {
			ret.setErrorCode(ErrorCodeEnum.Course_Not_Exist);
			return ret;
		} else if (clazz.getClass() == null) {
			ret.setErrorCode(ErrorCodeEnum.Class_Not_Exist);
			return ret;
		} else if (clazz.getSemester() == null) {
			ret.setErrorCode(ErrorCodeEnum.Semester_Not_Exist);
			return ret;
		} else if (clazz.getStudentClass() == null
				|| clazz.getStudentClass().isEmpty()) {
			ret.setErrorCode(ErrorCodeEnum.Students_Not_Added);
			return ret;
		} else if (clazz.getTeacher() == null) {
			ret.setErrorCode(ErrorCodeEnum.Teacher_Not_Exist);
			return ret;
		}
		classreservation.setClazz(clazz);

		classreservation.setApprovalStatus(ApprovalStatus.PENDING);

		if (crdao.updateClassReservation(classreservation) == null)
			ret.setErrorCode(ErrorCodeEnum.Unknown_Error);

		ret.setData(cr);
		return ret;
	}

	@Override
	public DataWrapper<List<Class>> getAllClass(int accountid) {
		// TODO Auto-generated method stub
		DataWrapper<List<Class>> ret = new DataWrapper<List<Class>>();
		Teacher teacher = dao.findTeacherById(accountid);
		if (teacher == null) {
			ret.setErrorCode(ErrorCodeEnum.Teacher_Not_Exist);
			return ret;
		}
		ret.setData(new ArrayList<Class>(teacher.getClasses()));
		return ret;
	}

	@Override
	public DataWrapper<List<LabPlan>> getPlanByWeekIndex(String token, int index) {
		// TODO Auto-generated method stub
		DataWrapper<List<LabPlan>> ret = new DataWrapper<List<LabPlan>>();
		List<LabPlan> list = lpdao.getLabPlanByWeek(index,semesterDao.getCurSemester().getStartDate());
		// Teacher teacher = dao.findTeacherByToken(token);

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
	public DataWrapper<List<Class>> getClassesInLabByTeacher(Integer accountId,
			Integer labId) {
		return new DataWrapper<List<Class>>(classdao.getClassesInLabByTeacher(
				accountId, labId));
	}

	@Override
	public DataWrapper<List<InfoEntry>> getExpInfoListByClassLab(
			Integer courseId, Integer labId) {
		return new DataWrapper<List<InfoEntry>>(
				expdao.getExpInfoListByClassLab(courseId, labId));
	}

	@Override
	public DataWrapper<List<ClassReservation>> getAllClassReservation(
			Integer labTeaId) {
		return new DataWrapper<List<ClassReservation>>(
				crdao.getAllClassReservationByLabTeacher(labTeaId));
	}

	@Override
	public DataWrapper<List<Teacher>> getAvailableTeachers(Integer reservationId) {
		return new DataWrapper<List<Teacher>>(
				dao.getAvailableTeachersByCall(reservationId));
	}

	@Override
	public DataWrapper<StudentRecord> updateStudentRecord(Integer recordId,
			StudentRecord stuRec) {
		DataWrapper<StudentRecord> ret = new DataWrapper<StudentRecord>();
		StudentRecord sr = srDao.findStudentRecordById(recordId);
		if (sr == null) {
			ret.setErrorCode(ErrorCodeEnum.StudentRecord_Not_Exist);
		} else {
			sr.setExperimentRecord(stuRec.getExperimentRecord());
			sr.setExperimentComment(stuRec.getExperimentComment());
			ret.setData(srDao.updateStudentRecord(sr));
		}
		return ret;
	}

	@Override
	public DataWrapper<List<Teacher>> getTeacherList(Integer labId) {
		return new DataWrapper<List<Teacher>>(dao.getTeacherList(labId));
	}
}
