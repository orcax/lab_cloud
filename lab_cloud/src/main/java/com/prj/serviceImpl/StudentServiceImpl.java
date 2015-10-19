package com.prj.serviceImpl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.prj.dao.ClassDao;
import com.prj.dao.ClassReservationDao;
import com.prj.dao.ExperimentDao;
import com.prj.dao.FileRecordDao;
import com.prj.dao.LabDao;
import com.prj.dao.LabPlanDao;
import com.prj.dao.SemesterDao;
import com.prj.dao.SlotDao;
import com.prj.dao.SlotReservationDao;
import com.prj.dao.StudentDao;
import com.prj.dao.StudentRecordDao;
import com.prj.dao.StudentReservationDao;
import com.prj.entity.Account;
import com.prj.entity.Account.Status;
import com.prj.entity.Class;
import com.prj.entity.ClassReservation;
import com.prj.entity.Experiment;
import com.prj.entity.FileRecord;
import com.prj.entity.Lab;
import com.prj.entity.LabPlan;
import com.prj.entity.LabPlan.ApprovalStatus;
import com.prj.entity.LabPlan.OpenStatus;
import com.prj.entity.LabPlan.SlotType;
import com.prj.entity.Reservation;
import com.prj.entity.Reservation.SlotNo;
import com.prj.entity.Slot;
import com.prj.entity.SlotReservation;
import com.prj.entity.Student;
import com.prj.entity.StudentClass;
import com.prj.entity.StudentRecord;
import com.prj.entity.StudentReservation;
import com.prj.service.StudentService;
import com.prj.util.DataWrapper;
import com.prj.util.DateUtils;
import com.prj.util.ErrorCodeEnum;
import com.prj.util.MD5Tool;
import com.prj.util.PasswordReset;

@Service("StudentServiceImpl")
public class StudentServiceImpl implements StudentService {

	@Resource(name = "StudentDaoImpl")
	StudentDao dao;
	@Resource(name = "LabPlanDaoImpl")
	LabPlanDao lpdao;
	@Resource(name = "StudentReservationDaoImpl")
	StudentReservationDao srdao;
	@Resource(name = "ClassReservationDaoImpl")
	ClassReservationDao crdao;
	@Resource(name = "ClassDaoImpl")
	ClassDao classDao;
	@Resource(name = "StudentRecordDaoImpl")
	StudentRecordDao stuRecDao;
	@Resource(name = "FileRecordDaoImpl")
	FileRecordDao frDao;
	@Resource(name = "LabDaoImpl")
	LabDao labDao;
	@Resource(name = "ExperimentDaoImpl")
	ExperimentDao expDao;
	@Resource(name = "SlotReservationDaoImpl")
	SlotReservationDao slrDao;
	@Resource(name = "SemesterDaoImpl")
	SemesterDao semesterDao;
	@Resource(name = "SlotDaoImpl")
	SlotDao sld;
	
	private static final String INITIAL_PASSWORD = "222222";

	public DataWrapper<Student> addStudent(Student student) {
		DataWrapper<Student> ret = new DataWrapper<Student>();
		Student a = dao.getStudentByNumber(student.getNumber());
		student.setPassword(MD5Tool.GetMd5(student.getInitialPassword()));
		if (a != null) {
			ret.setErrorCode(ErrorCodeEnum.Account_Exist);
			ret.setData(a);
		} else if (dao.addStudent(student) != null) {
			ret.setData(student);
		} else {
			ret.setErrorCode(ErrorCodeEnum.Unknown_Error);
		}
		return ret;
	}

	public DataWrapper<Student> disableStudentById(Integer id) {
		Student a = dao.findStudentById(id);
		DataWrapper<Student> ret = new DataWrapper<Student>();
		if (a == null) {
			ret.setErrorCode(ErrorCodeEnum.Account_Not_Exist);
		} else {
			a.setStatus(Account.Status.INACTIVE);
			a.setLoginToken(null);
			ret.setData(dao.updateStudent(a));
		}
		return ret;
	}

	public DataWrapper<List<Student>> getAllStudent() {
		return dao.getAllStudent();
	}

	public DataWrapper<Student> getStudentById(int id) {
		DataWrapper<Student> ret = new DataWrapper<Student>();
		Student a = dao.findStudentById(id);
		ret.setData(a);
		if (a == null) {
			ret.setErrorCode(ErrorCodeEnum.Account_Not_Exist);
		}
		return ret;
	}

	@Override
	public DataWrapper<Student> updateStudent(Integer id, Student v,
			boolean isAdmin) {
		// Only set attributes allowed
		Student s = dao.findStudentById(id);
		DataWrapper<Student> ret = new DataWrapper<Student>();
		if (s == null) {
			ret.setErrorCode(ErrorCodeEnum.Account_Not_Exist);
		} else {
			s.setEmail(v.getEmail());
			s.setGrade(v.getGrade());
			// s.setIconPath(v.getIconPath());
			s.setMajor(v.getMajor());
			s.setName(v.getName());
			if (isAdmin) {
				if (!v.getNumber().equals(s.getNumber())
						&& dao.getStudentByNumber(v.getNumber()) != null) {
					ret.setErrorCode(ErrorCodeEnum.Number_Exist);
				} else {
					s.setNumber(v.getNumber());
				}
			}
			s = dao.updateStudent(s);
			ret.setData(s);
		}
		return ret;
	}

	public DataWrapper<Student> reset(PasswordReset reset) {
		Student a = dao.findStudentById(reset.getId());
		DataWrapper<Student> ret = new DataWrapper<Student>();
		if (a == null) {
			ret.setErrorCode(ErrorCodeEnum.Account_Not_Exist);
		} else if (!a.getPassword().equals(
				MD5Tool.GetMd5(reset.getOldPassword()))) {
			ret.setErrorCode(ErrorCodeEnum.Password_Wrong);
		} else {
			a.setPassword(MD5Tool.GetMd5(reset.getNewPassword()));
			a.setLoginToken(null);
			a.setStatus(Status.ACTIVE);
			dao.updateStudent(a);
		}
		return ret;
	}

	public DataWrapper<List<Student>> getStudentbyPage(int pagenumber,
			int pagesize) {
		return dao.getStudentbyPage(pagenumber, pagesize);
	}

	@Override
	public DataWrapper<List<Student>> addStudents(List<Student> list,
			Integer classId) {
		DataWrapper<List<Student>> ret = new DataWrapper<List<Student>>(
				new ArrayList<Student>());
		for (Student student : list) {
			student.setPassword(MD5Tool.GetMd5(INITIAL_PASSWORD));
		}
		ret.setData(dao.addStudents(list));
		return ret;
	}

	@Override
	public DataWrapper<List<LabPlan>> getPlanByWeekIndexByLab(String token,
			int index, int labid) {
		DataWrapper<List<LabPlan>> ret = new DataWrapper<List<LabPlan>>();
		List<LabPlan> list = lpdao.getLabPlanByWeekByLab(index, labid,semesterDao.getCurSemester().getStartDate());
		Student student = dao.findStudentByToken(token);

		if (list == null)
			ret.setErrorCode(ErrorCodeEnum.Out_Of_Bound);
		else {
			for (LabPlan lp : list) {
				SimplifyLabPlan(lp, student);
			}
			ret.setData(list);
		}
		return ret;
	}

	// 去除非本人的预约信息，以及去除各个slot中slotid不对应的预约，将Experiment中的labs设为null，不然会返回labplan整张表,
	// 将班级中每个学生的预约信息设置为null，这个冗余了
	private void SimplifyLabPlan(LabPlan lp, Student student) {
		Set<StudentReservation> slot1sr = lp.getSlot1StudentReservations();
		Iterator<StudentReservation> it1 = slot1sr.iterator();
		while (it1.hasNext()) {
			StudentReservation sr = it1.next();
			sr.getStudent().setStudentReservation(null);
			if (sr.getStudent().getId() != student.getId()
					|| sr.getSlot() != SlotNo.ONE) {
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
			if (sr.getStudent().getId() != student.getId()
					|| sr.getSlot() != SlotNo.TWO) {
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
			if (sr.getStudent().getId() != student.getId()
					|| sr.getSlot() != SlotNo.THREE) {
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
			if (sr.getStudent().getId() != student.getId()
					|| sr.getSlot() != SlotNo.FOUR) {
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
			if (sr.getStudent().getId() != student.getId()
					|| sr.getSlot() != SlotNo.FIVE) {
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
			if (sr.getStudent().getId() != student.getId()
					|| sr.getSlot() != SlotNo.SIX) {
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

	@Override
	public DataWrapper<List<? extends Reservation>> getallReservation(
			int accountid) {
		// TODO Auto-generated method stub
		DataWrapper<List<? extends Reservation>> ret = new DataWrapper<List<? extends Reservation>>();
		List<Reservation> data = new ArrayList<Reservation>();
		Student student = dao.findStudentById(accountid);
		if (student == null) {
			ret.setErrorCode(ErrorCodeEnum.Account_Not_Exist);
			return ret;
		}
		List<StudentReservation> srs = srdao.getAllStudentReservation();
		// data.addAll();
		for (StudentReservation sr : srs) {
			if (sr.getStudent().getId() == student.getId()) {
				data.add(sr);
			}
		}
		List<ClassReservation> crs = crdao.getAllClassReservation();
		// data.addAll();
		for (ClassReservation cr : crs) {
			boolean ifexist = false;
			Set<StudentClass> scs = cr.getClazz().getStudentClass();
			Iterator<StudentClass> it = scs.iterator();
			while (it.hasNext()) {
				StudentClass sc = it.next();
				if (sc.getStudent().getId() == student.getId()) {
					ifexist = true;
					break;
				}
			}
			if (ifexist)
				data.add(cr);
		}
		ret.setData(data);
		return ret;
	}

	@Override
	public DataWrapper<List<Class>> getAllClass(int accountid) {
		// TODO Auto-generated method stub
		DataWrapper<List<Class>> ret = new DataWrapper<List<Class>>();
		Student stu = dao.findStudentById(accountid);
		if (stu == null) {
			ret.setErrorCode(ErrorCodeEnum.Account_Not_Exist);
		}
		Set<StudentClass> scs = stu.getStudentClass();
		Iterator<StudentClass> it = scs.iterator();
		List<Class> data = new ArrayList<Class>();
		while (it.hasNext()) {
			StudentClass cs = it.next();
			data.add(cs.getClazz());
		}
		ret.setData(data);
		return ret;
	}

	@Override
	public DataWrapper<List<LabPlan>> getPlanByWeekIndex(String token, int index) {
		// TODO Auto-generated method stub
		DataWrapper<List<LabPlan>> ret = new DataWrapper<List<LabPlan>>();
		List<LabPlan> list = lpdao.getLabPlanByWeek(index,semesterDao.getCurSemester().getStartDate());
		Student student = dao.findStudentByToken(token);

		if (list == null)
			ret.setErrorCode(ErrorCodeEnum.Out_Of_Bound);
		else {
			for (LabPlan lp : list) {
				SimplifyLabPlan(lp, student);
			}
			ret.setData(list);
		}
		return ret;
	}

	@Override
	public DataWrapper<String> getReservationByTime(int id, Date date,
			SlotNo slot, int labid) {
		// TODO Auto-generated method stub
		DataWrapper<String> ret = new DataWrapper<String>();
		Student stu = dao.findStudentById(id);
		// ClassReservation cr = crdao.getClassReservationsByDateSlot(date,
		// slot);
		LabPlan lp = lpdao.getLabPlanByDate(labid, date);
		Boolean exist = false;
		int reserveid = 0;
		int type = 0;
		String expname ="";
		if (lp != null) {
			// cr.getReserver().setClasses(null);
			if (slot == SlotNo.ONE) {
				if (lp.getSlot1Type() == SlotType.CLASS
						&& lp.getSlot1OpenStatus() == OpenStatus.APPROVED
						&& lp.getSlot1ClassReservation().getApprovalStatus() == ApprovalStatus.APPROVED) {
					Set<StudentClass> ssc = lp.getSlot1ClassReservation()
							.getClazz().getStudentClass();
					Iterator<StudentClass> it = ssc.iterator();
					while (it.hasNext()) {
						StudentClass sc = it.next();
						if (sc.getStudent().getId() == stu.getId()) {
							exist = true;
							reserveid = lp.getSlot1ClassReservation().getId();
							type = 1;
							expname = lp.getSlot1Experiment().getExperimentName();
							break;
						}
					}
				}
				else if(lp.getSlot1Type() == SlotType.STUDENT
						&& lp.getSlot1OpenStatus() == OpenStatus.APPROVED
						){
					Set<Student> ss = lp.getSlot1SlotReservation().getStudents();
					Iterator<Student> it = ss.iterator();
					while(it.hasNext()){
						Student s = it.next();
						if(s.getId() == stu.getId()){
							exist = true;
							reserveid = lp.getSlot1SlotReservation().getId();
							type = 2;
							expname = lp.getSlot1Experiment().getExperimentName();
							break;
						}
					}
				}
			}
			if (slot == SlotNo.TWO) {
				if (lp.getSlot2Type() == SlotType.CLASS
						&& lp.getSlot2OpenStatus() == OpenStatus.APPROVED
						&&lp.getSlot2ClassReservation().getApprovalStatus() == ApprovalStatus.APPROVED) {
					Set<StudentClass> ssc = lp.getSlot2ClassReservation()
							.getClazz().getStudentClass();
					Iterator<StudentClass> it = ssc.iterator();
					while (it.hasNext()) {
						StudentClass sc = it.next();
						if (sc.getStudent().getId() == stu.getId()) {
							exist = true;
							reserveid = lp.getSlot2ClassReservation().getId();
							type = 1;
							expname = lp.getSlot2Experiment().getExperimentName();
							break;
						}
					}
				}
				else if(lp.getSlot2Type() == SlotType.STUDENT
						&& lp.getSlot2OpenStatus() == OpenStatus.APPROVED
						){
					Set<Student> ss = lp.getSlot2SlotReservation().getStudents();
					Iterator<Student> it = ss.iterator();
					while(it.hasNext()){
						Student s = it.next();
						if(s.getId() == stu.getId()){
							exist = true;
							reserveid = lp.getSlot2SlotReservation().getId();
							type = 2;
							expname = lp.getSlot2Experiment().getExperimentName();
							break;
						}
					}
				}

			}
			if (slot == SlotNo.THREE) {
				if (lp.getSlot3Type() == SlotType.CLASS
						&& lp.getSlot3OpenStatus() == OpenStatus.APPROVED
						&& lp.getSlot3ClassReservation().getApprovalStatus() == ApprovalStatus.APPROVED) {
					Set<StudentClass> ssc = lp.getSlot3ClassReservation()
							.getClazz().getStudentClass();
					Iterator<StudentClass> it = ssc.iterator();
					while (it.hasNext()) {
						StudentClass sc = it.next();
						if (sc.getStudent().getId() == stu.getId()) {
							exist = true;
							reserveid = lp.getSlot3ClassReservation().getId();
							type = 1;
							expname = lp.getSlot3Experiment().getExperimentName();
							break;
						}
					}
				}
				else if(lp.getSlot3Type() == SlotType.STUDENT
						&& lp.getSlot3OpenStatus() == OpenStatus.APPROVED
						){
					Set<Student> ss = lp.getSlot3SlotReservation().getStudents();
					Iterator<Student> it = ss.iterator();
					while(it.hasNext()){
						Student s = it.next();
						if(s.getId() == stu.getId()){
							exist = true;
							reserveid = lp.getSlot3SlotReservation().getId();
							type = 2;
							expname = lp.getSlot3Experiment().getExperimentName();
							break;
						}
					}
				}

			}
			if (slot == SlotNo.FOUR) {
				if (lp.getSlot4Type() == SlotType.CLASS
						&& lp.getSlot4OpenStatus() == OpenStatus.APPROVED
						&& lp.getSlot4ClassReservation().getApprovalStatus() == ApprovalStatus.APPROVED) {
					Set<StudentClass> ssc = lp.getSlot4ClassReservation()
							.getClazz().getStudentClass();
					Iterator<StudentClass> it = ssc.iterator();
					while (it.hasNext()) {
						StudentClass sc = it.next();
						if (sc.getStudent().getId() == stu.getId()) {
							exist = true;
							reserveid = lp.getSlot4ClassReservation().getId();
							type = 1;
							expname = lp.getSlot4Experiment().getExperimentName();
							break;
						}
					}
				}
				else if(lp.getSlot4Type() == SlotType.STUDENT
						&& lp.getSlot4OpenStatus() == OpenStatus.APPROVED
						){
					Set<Student> ss = lp.getSlot4SlotReservation().getStudents();
					Iterator<Student> it = ss.iterator();
					while(it.hasNext()){
						Student s = it.next();
						if(s.getId() == stu.getId()){
							exist = true;
							reserveid = lp.getSlot4SlotReservation().getId();
							type = 2;
							expname = lp.getSlot4Experiment().getExperimentName();
							break;
						}
					}
				}

			}
			if (slot == SlotNo.FIVE) {
				if (lp.getSlot5Type() == SlotType.CLASS
						&& lp.getSlot5OpenStatus() == OpenStatus.APPROVED
						&& lp.getSlot5ClassReservation().getApprovalStatus() == ApprovalStatus.APPROVED) {
					Set<StudentClass> ssc = lp.getSlot5ClassReservation()
							.getClazz().getStudentClass();
					Iterator<StudentClass> it = ssc.iterator();
					while (it.hasNext()) {
						StudentClass sc = it.next();
						if (sc.getStudent().getId() == stu.getId()) {
							exist = true;
							reserveid = lp.getSlot5ClassReservation().getId();
							type = 1;
							expname = lp.getSlot5Experiment().getExperimentName();
							break;
						}
					}
				}
				else if(lp.getSlot5Type() == SlotType.STUDENT
						&& lp.getSlot5OpenStatus() == OpenStatus.APPROVED
						){
					Set<Student> ss = lp.getSlot5SlotReservation().getStudents();
					Iterator<Student> it = ss.iterator();
					while(it.hasNext()){
						Student s = it.next();
						if(s.getId() == stu.getId()){
							exist = true;
							reserveid = lp.getSlot5SlotReservation().getId();
							type = 2;
							expname = lp.getSlot5Experiment().getExperimentName();
							break;
						}
					}
				}

			}
			if (slot == SlotNo.SIX) {
				if (lp.getSlot6Type() == SlotType.CLASS
						&& lp.getSlot6OpenStatus() == OpenStatus.APPROVED
						&& lp.getSlot6ClassReservation().getApprovalStatus() == ApprovalStatus.APPROVED) {
					Set<StudentClass> ssc = lp.getSlot6ClassReservation()
							.getClazz().getStudentClass();
					Iterator<StudentClass> it = ssc.iterator();
					while (it.hasNext()) {
						StudentClass sc = it.next();
						if (sc.getStudent().getId() == stu.getId()) {
							exist = true;
							reserveid = lp.getSlot6ClassReservation().getId();
							type = 1;
							expname = lp.getSlot6Experiment().getExperimentName();
							break;
						}
					}
				}
				else if(lp.getSlot6Type() == SlotType.STUDENT
						&& lp.getSlot6OpenStatus() == OpenStatus.APPROVED
						){
					Set<Student> ss = lp.getSlot6SlotReservation().getStudents();
					Iterator<Student> it = ss.iterator();
					while(it.hasNext()){
						Student s = it.next();
						if(s.getId() == stu.getId()){
							exist = true;
							reserveid = lp.getSlot6SlotReservation().getId();
							type = 2;
							expname = lp.getSlot6Experiment().getExperimentName();
							break;
						}
					}
				}

			}
			if (slot == SlotNo.SEVEN) {
				if (lp.getSlot7Type() == SlotType.CLASS
						&& lp.getSlot7OpenStatus() == OpenStatus.APPROVED
						&& lp.getSlot7ClassReservation().getApprovalStatus() == ApprovalStatus.APPROVED) {
					Set<StudentClass> ssc = lp.getSlot7ClassReservation()
							.getClazz().getStudentClass();
					Iterator<StudentClass> it = ssc.iterator();
					while (it.hasNext()) {
						StudentClass sc = it.next();
						if (sc.getStudent().getId() == stu.getId()) {
							exist = true;
							reserveid = lp.getSlot7ClassReservation().getId();
							type = 1;
							expname = lp.getSlot7Experiment().getExperimentName();
							break;
						}
					}
				}
				else if(lp.getSlot7Type() == SlotType.STUDENT
						&& lp.getSlot7OpenStatus() == OpenStatus.APPROVED
						){
					Set<Student> ss = lp.getSlot7SlotReservation().getStudents();
					Iterator<Student> it = ss.iterator();
					while(it.hasNext()){
						Student s = it.next();
						if(s.getId() == stu.getId()){
							exist = true;
							reserveid = lp.getSlot7SlotReservation().getId();
							type = 2;
							expname = lp.getSlot7Experiment().getExperimentName();
							break;
						}
					}
				}

			}
			ret.setData(exist + "_" + reserveid+"_" + type+"_"+expname);
			return ret;
		} else {
			ret.setErrorCode(ErrorCodeEnum.Reservation_Not_Exist);
			return ret;
		}
	}

	@Override
	public DataWrapper<Integer> getMaxStuOfExperiments(Date date, SlotNo slot,
			int labid) {
		// TODO Auto-generated method stub
		DataWrapper<Integer> ret = new DataWrapper<Integer>();
		LabPlan lp = lpdao.getLabPlanByDate(labid, date);
		if (slot == SlotNo.ONE) {
			ret.setData(lp.getSlot1Experiment().getMaximumStudent());
		}
		if (slot == SlotNo.TWO) {
			ret.setData(lp.getSlot2Experiment().getMaximumStudent());
		}
		if (slot == SlotNo.THREE) {
			ret.setData(lp.getSlot3Experiment().getMaximumStudent());
		}
		if (slot == SlotNo.FOUR) {
			ret.setData(lp.getSlot4Experiment().getMaximumStudent());
		}
		if (slot == SlotNo.FIVE) {
			ret.setData(lp.getSlot5Experiment().getMaximumStudent());
		}
		if (slot == SlotNo.SIX) {
			ret.setData(lp.getSlot6Experiment().getMaximumStudent());
		}
		return ret;
	}

	@Override
	public DataWrapper<List<StudentRecord>> getRecordByStuClass(Integer stuId,
			Integer classId) {
		return new DataWrapper<List<StudentRecord>>(
				stuRecDao.getRecordByStuClass(stuId, classId));
	}

	@Override
	public DataWrapper<List<FileRecord>> getFileRecordList(Integer srId) {
		return new DataWrapper<List<FileRecord>>(frDao.getFileRecordsBySR(srId));
	}

	@Override
	public DataWrapper<StudentReservation> addStudentReservation(
			Integer studentId, Integer labId, Integer expId, StudentReservation studentReservation) {
		DataWrapper<StudentReservation> ret = new DataWrapper<StudentReservation>(); 
		try {
			if (srdao.getStudentReservationBySLE(studentId, labId, expId).size()>0) {
				return ret.setErrorCode(ErrorCodeEnum.Reservation_Exist);
			}
			
			LabPlan lp = lpdao.getLabPlanByDate(labId, studentReservation.getDate());
			Method m = lp.getClass().getMethod("getSlot"+(studentReservation.getSlot().ordinal()+1)+"Type");
			SlotType st = (SlotType) m.invoke(lp);
			if (st == SlotType.CLASS)
				return ret.setErrorCode(ErrorCodeEnum.Slot_For_Class);
			
			m = lp.getClass().getMethod("getSlot"+(studentReservation.getSlot().ordinal()+1)+"OpenStatus");
			OpenStatus os = (OpenStatus) m.invoke(lp);
			if (os != OpenStatus.OPEN)
				return ret.setErrorCode(ErrorCodeEnum.Slot_Unavailable);
			Student s = dao.findStudentById(studentId);
			if (s == null)
				return ret.setErrorCode(ErrorCodeEnum.Account_Not_Exist);
			Lab l = labDao.findLabById(labId);
			if (l == null)
				return ret.setErrorCode(ErrorCodeEnum.Lab_Not_Exist);
			studentReservation.setExperiment(null);
			List<Experiment> list = expDao.getExperimentByStudent(studentId);
			for (Experiment e : list) {
				if (e.getId() == expId) {
					studentReservation.setExperiment(e);
				}
			}
			if (studentReservation.getExperiment() == null) {
				return ret.setErrorCode(ErrorCodeEnum.Experiment_Not_Included);
			}
			studentReservation.setLabPlan(lp);
			studentReservation.setStudent(s);
			studentReservation.setLab(l);
			srdao.addStudentReservation(studentReservation);
			return ret.setData(studentReservation);
		} catch (Exception e) {
			e.printStackTrace();
			return ret.setErrorCode(ErrorCodeEnum.Unknown_Error);
		}
	}

	@Override
	public DataWrapper<List<StudentReservation>> getStudentReservationPage(
			Integer stuId, ApprovalStatus as, Integer pageSize,
			Integer pageNumber) {
		return srdao.getStudentReservationPage(stuId, as, pageSize, pageNumber);
	}

	@Override
	public DataWrapper<List<SlotReservation>> getAvailableSlotReservationPage(
			Integer stuId, Integer pageSize, Integer pageNumber) {
		return slrDao.getAvailableSlotReservationPage(stuId, pageSize, pageNumber);
	}
	
	@Override
	public DataWrapper<SlotReservation> applySlotReservation(Integer studentId,
			Integer slotResId) {
		DataWrapper<SlotReservation> ret = new DataWrapper<SlotReservation>();
		SlotReservation slr = slrDao.findSlotReservationById(slotResId);
		if (slr == null)
			return ret.setErrorCode(ErrorCodeEnum.SlotReservation_Not_Exist);
		Slot sl = sld.getSlot(slr.getSlot());
		if (sl == null)
			return ret.setErrorCode(ErrorCodeEnum.Slot_Not_Exist);
		if (new Date().getTime() >= slr.getDate().getTime()+DateUtils.timeStr2Long(sl.getStartTime().toString()))
			return ret.setErrorCode(ErrorCodeEnum.Reservation_Expired);
		if (slr.getOccupiedNumber() >= slr.getMax())
			return ret.setErrorCode(ErrorCodeEnum.Slot_Full);
		Student s = dao.findStudentById(studentId);
		if (s == null)
			return ret.setErrorCode(ErrorCodeEnum.Account_Not_Exist);
		Experiment e = slr.getExperiment();
		if (slrDao.checkStudentByExperiment(studentId, e.getId())) {
			return ret.setErrorCode(ErrorCodeEnum.Already_Approved);
		}
		List<Class> cs = classDao.getClassesBySE(studentId, e.getId());
		for (Class c : cs) {
			StudentRecord sr = stuRecDao.getStudentRecordByClassStuExp(c.getId(), studentId, e.getId());
			if (sr == null) {
				sr = new StudentRecord();
				sr.setClazz(c);
				sr.setExperiment(e);
				sr.setStatus(StudentRecord.Status.NOT_STARTED);
				sr.setStudent(s);
				stuRecDao.addStudentRecord(sr);
			}
		}
		slr.getStudents().add(s);
		slr.setOccupiedNumber(slr.getOccupiedNumber()+1);
		slrDao.updateSlotReservation(slr);
		return ret.setData(slr);
	}

	@Override
	public DataWrapper<List<Student>> getStudentPageBySlotRes(
			Integer slotResId, Integer pageSize, Integer pageNumber) {
		return dao.getStudentPageBySlotRes(slotResId, pageSize, pageNumber);
	}

	@Override
	public DataWrapper<List<SlotReservation>> getSlotReservationPageByStu(
			Integer stuId, Integer pageSize, Integer pageNumber) {
		return slrDao.getApprovedSlotReservationPage(stuId, pageSize, pageNumber);
	}

	// @Override
	// public DataWrapper<LabPlan> getPlanByDate(String token, Date date) {
	// // TODO Auto-generated method stub
	// DataWrapper<LabPlan> ret = new DataWrapper<LabPlan>();
	// LabPlan lp = lpdao.getLabPlanByDate(date);
	// Student student = dao.findStudentByToken(token);
	// if(lp!=null)
	// {
	// SimplifyLabPlan(lp,student);
	// ret.setData(lp);
	// }
	// else
	// {
	// ret.setErrorCode(ErrorCodeEnum.LabPlan_Not_Exist);
	// }
	// return ret;
	// }

}
