package com.prj.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.criterion.SimpleExpression;

import com.prj.entity.Account.Status;
import com.prj.entity.Teacher;
import com.prj.entity.Teacher.Role;
import com.prj.util.DataWrapper;
import com.prj.util.Page;

public interface TeacherDao {
	Integer addTeacher(Teacher v);

//	Teacher disableTeacherById(Integer id);

	Teacher findTeacherById(Integer id);

	DataWrapper<List<Teacher>> getAllTeacher();

	Teacher updateTeacher(Teacher v);

	DataWrapper<List<Teacher>> getTeacherByRolePage(Role role, int pagenumber, int pagesize);

	List<Teacher> getByCondition(List<SimpleExpression> list);

	Page<Teacher> getByPageWithConditions(int pagenumber, int pagesize,
			List<SimpleExpression> list);

	Teacher getTeacherByNumber(String number);

	Teacher findTeacherByToken(String token);

	List<Teacher> getTeacherByStatus(Status as);
	
	Set<Teacher> getTeachersByIds(List<Integer> teacherIds);

	List<Teacher> getAvailableTeachersByCall(Integer classReservationId);

	List<Teacher> getTeacherList(Integer labId);
}
