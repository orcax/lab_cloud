package com.prj.service;

import java.util.List;
import java.util.Set;

import org.hibernate.criterion.SimpleExpression;

import com.prj.entity.Class;
import com.prj.entity.ClassReservation;
import com.prj.entity.InfoEntry;
import com.prj.entity.LabPlan;
import com.prj.entity.LabPlan.ApprovalStatus;
import com.prj.entity.StudentRecord;
import com.prj.entity.Teacher;
import com.prj.entity.Teacher.Role;
import com.prj.util.DataWrapper;
import com.prj.util.Page;
import com.prj.util.PasswordReset;

public interface TeacherService {
//	DataWrapper<Teacher> login(Teacher teacher);

//	void logout(Integer id);

	DataWrapper<Teacher> addTeacher(Teacher teacher);

	public DataWrapper<Teacher> disableTeacherById(Integer id);

	DataWrapper<List<Teacher>> getAllTeacher();

	DataWrapper<Teacher> getTeacherById(int id);
	
	DataWrapper<Set<Class>>  getTeacherClasses(int id);

	DataWrapper<Teacher> updateTeacher(Integer integer, Teacher entity, boolean isAdmin);

	DataWrapper<Teacher> reset(PasswordReset data);

	DataWrapper<List<Teacher>> getTeacherByRolePage(Role role, int pagenumber, int pagesize);

	Page<Teacher> searchTeacher(int pagenumber, int pagesize, String name);

	Page<Teacher> getByPageWithConditions(int pagenumber, int pagesize,
			List<SimpleExpression> list);

	DataWrapper<List<LabPlan>> getPlanByWeekIndexByLab(String token, int index, int labid);
	
//	DataWrapper<LabPlan> getPlanByDate(String token, Date date);
	
	DataWrapper<ClassReservation> classReserver(Integer teacherId, ClassReservation cr, int labid, int expid, int classid);

	DataWrapper<List<ClassReservation>> getClassReservationByPageAS(int pagenumber, int pagesize, int id, ApprovalStatus as);
	
	DataWrapper<ClassReservation> deleteClassReservationById(int id);
	
	DataWrapper<ClassReservation> getClassReservationById(int id);
	
	DataWrapper<ClassReservation> updateClassReservationById(int id, ClassReservation cr, int labid, int expid, int classid);

	DataWrapper<List<Class>> getAllClass(int accountid);
	
	DataWrapper<List<LabPlan>> getPlanByWeekIndex(String token, int index);

	DataWrapper<List<Class>> getClassesInLabByTeacher(Integer accountId,
			Integer expId);

	DataWrapper<List<InfoEntry>> getExpInfoListByClassLab(Integer courseId,
			Integer labId);

	DataWrapper<List<ClassReservation>> getAllClassReservation(Integer labTeaId);

	DataWrapper<List<Teacher>> getAvailableTeachers(Integer reservationId);

	DataWrapper<StudentRecord> updateStudentRecord(Integer recordId,
			StudentRecord stuRec);

	DataWrapper<List<Teacher>> getTeacherList(Integer labId);
}
