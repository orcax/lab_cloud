package com.prj.dao;

import java.util.List;

import com.prj.entity.Account;
import com.prj.entity.Student;
import com.prj.util.DataWrapper;

public interface StudentDao {
	Integer addStudent(Student v);

//	Student disableStudentById(Integer id);

	Student findStudentById(Integer id);

	DataWrapper<List<Student>> getAllStudent();

	Student updateStudent(Student v);

	DataWrapper<List<Student>> getStudentbyPage(int pagenumber, int pagesize);

	Student getStudentByNumber(String number);

	Student findStudentByToken(String token);
	
	List<Student> getStudentByStatus(Account.Status as);

	List<Student> addStudents(List<Student> list);

	DataWrapper<List<Student>> getStudentPageBySlotRes(Integer slotResId,
			Integer pageSize, Integer pageNumber);
	
}
