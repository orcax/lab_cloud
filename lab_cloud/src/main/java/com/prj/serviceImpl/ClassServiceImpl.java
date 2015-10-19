package com.prj.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.prj.dao.ClassDao;
import com.prj.dao.CourseDao;
import com.prj.dao.ExperimentDao;
import com.prj.dao.SemesterDao;
import com.prj.dao.StudentClassDao;
import com.prj.dao.StudentRecordDao;
import com.prj.dao.TeacherDao;
import com.prj.entity.Class;
import com.prj.entity.Course;
import com.prj.entity.Experiment;
import com.prj.entity.ExperimentStatus;
import com.prj.entity.Semester;
import com.prj.entity.Student;
import com.prj.entity.StudentClass;
import com.prj.entity.StudentRecord;
import com.prj.entity.Teacher;
import com.prj.service.ClassService;
import com.prj.util.CallStatusEnum;
import com.prj.util.DataWrapper;
import com.prj.util.ErrorCodeEnum;

@Service("ClassServiceImpl")
public class ClassServiceImpl implements ClassService{
	
	@Resource(name="ClassDaoImpl")
	ClassDao classdao;
	
	@Resource(name="CourseDaoImpl")
	CourseDao coursedao;
	
	@Resource(name="TeacherDaoImpl")
	TeacherDao teacherdao;
	
	@Resource(name="SemesterDaoImpl")
	SemesterDao semesterdao;

	@Resource(name="StudentClassDaoImpl")
	StudentClassDao stuClassDao;
	@Resource(name="StudentRecordDaoImpl")
	StudentRecordDao stuRecDao;
	@Resource(name="ExperimentDaoImpl")
	ExperimentDao expDao;
	
	@Override
	public DataWrapper<Class> addClass(Class c, int semesterId, int courseId, int teacherId) {
		DataWrapper<Class> ret = new DataWrapper<Class>();
		if(classdao.getClassByNumber(c.getClassNumber()) != null) {
			ret.setErrorCode(ErrorCodeEnum.Class_Exist);
		} else {
			Semester s = semesterdao.findSemesterById(semesterId);
			if (s == null) {
				ret.setErrorCode(ErrorCodeEnum.Semester_Not_Exist);
			} else {
				Course co = coursedao.findCourseById(courseId);
				if (co == null) {
					ret.setErrorCode(ErrorCodeEnum.Course_Not_Exist);
				} else {
					Teacher t = teacherdao.findTeacherById(teacherId);
					if (t == null) {
						ret.setErrorCode(ErrorCodeEnum.Teacher_Not_Exist);
					} 
//					else if (classdao.getClassByComponentIds(semesterId, courseId, teacherId)!=null) {
//						ret.setErrorCode(ErrorCodeEnum.Class_Exist);
//					} 
					else {
						c.setSemester(s);
						c.setCourse(co);
						c.setTeacher(t);
						//c.setClassNumber(String.format("%02d%03d%03d", s.getId(), co.getId(), t.getId()));
						if (classdao.addClass(c) != null) {
							ret.setData(c);	
						} else {
							ret.setErrorCode(ErrorCodeEnum.Unknown_Error);
						}
					}
				}
			}
		}
		return ret;
	}

	@Override
	public DataWrapper<List<Class>> getAllClass() {
		return classdao.getAllClass();
	}

	@Override
	public DataWrapper<Class> setTeacher(int classid, int teacherid) {
		DataWrapper<Class> ret = new DataWrapper<Class>();
		Teacher t = teacherdao.findTeacherById(teacherid);
		Class c = classdao.findClassById(classid);
		if(t==null)
			ret.setErrorCode(ErrorCodeEnum.Teacher_Not_Exist);
		else if(c== null){
			ret.setErrorCode(ErrorCodeEnum.Class_Not_Exist);
		}
		else
		{
			c.setTeacher(t);
			if(classdao.updateClass(c)==null)
				ret.setErrorCode(ErrorCodeEnum.Unknown_Error);
		}
		return ret;
	}

	@Override
	public DataWrapper<Class> setCourse(int classid, int courseid) {
		DataWrapper<Class> ret = new DataWrapper<Class>();
		Course course = coursedao.findCourseById(courseid);
		Class clazz = classdao.findClassById(classid);
		if(course==null)
			ret.setErrorCode(ErrorCodeEnum.Course_Not_Exist);
		else if(clazz== null){
			ret.setErrorCode(ErrorCodeEnum.Class_Not_Exist);
		}
		else
		{
			clazz.setCourse(course);
			if(classdao.updateClass(clazz)==null)
				ret.setErrorCode(ErrorCodeEnum.Unknown_Error);
		}
		return ret;
	}

	@Override
	public DataWrapper<Class> setSemester(int classid, int semesterid) {
		DataWrapper<Class> ret = new DataWrapper<Class>();
		Semester s = semesterdao.findSemesterById(semesterid);
		Class clazz = classdao.findClassById(classid);
		if(s==null)
			ret.setErrorCode(ErrorCodeEnum.Semester_Not_Exist);
		else if(clazz== null){
			ret.setErrorCode(ErrorCodeEnum.Class_Not_Exist);
		}
		else
		{
			clazz.setSemester(s);
			if(classdao.updateClass(clazz)==null)
				ret.setErrorCode(ErrorCodeEnum.Unknown_Error);
		}
		return ret;
	}

	@Override
	public DataWrapper<Class> deleteClassById(int id) {
		DataWrapper<Class> ret = new DataWrapper<Class>();
		Class c = classdao.findClassById(id);
		if(c==null)
			ret.setErrorCode(ErrorCodeEnum.Class_Not_Exist);
		else
		{
			if(classdao.deleteClassById(id)==null)
				ret.setErrorCode(ErrorCodeEnum.Unknown_Error);
		}
		return ret;
	}

	@Override
	public DataWrapper<Class> updateClassById(Integer id, Class clazz) {
		// Only set attributes allowed
		Class c = classdao.findClassById(id);
		DataWrapper<Class> ret = new DataWrapper<Class>();
		if (c == null) {
			ret.setErrorCode(ErrorCodeEnum.Class_Not_Exist);
		} else {
			c.setClassNumber(clazz.getClassNumber());
			c.setClassHour(clazz.getClassHour());
			c.setClassRoom(clazz.getClassRoom());
			c = classdao.updateClass(c);
			ret.setData(c);
		}
		return ret;
	}

	@Override
	public DataWrapper<List<Student>> addStudents(List<Student> list,
			Integer classId) {
		Class c = findClassById(classId).getData();
		DataWrapper<List<Student>> ret = new DataWrapper<List<Student>>();
		if (c == null) {
			ret.setErrorCode(ErrorCodeEnum.Class_Not_Exist);
		} else {
			List<StudentClass> l = new ArrayList<StudentClass>();
			for (Student student : list) {
				StudentClass sc = new StudentClass();
				sc.setClazz(c);
				sc.setStudent(student);
				l.add(sc);
				student.getStudentClass().add(sc);
			}
			stuClassDao.addStudentClasses(l);
			ret.setData(list);
//			Set<StudentClass> scs = c.getStudentClass();
//			for (Student student : list) {
//				boolean found = false;
//				for (StudentClass sc: scs) {
//					if (sc.getStudent().getId() == student.getId()) {
//						found = true;
//					}
//				}
//				if (found == false) {
//					StudentClass sc = new StudentClass();
//					sc.setStudent(student);
//					sc.setClazz(c);
//					if (stuClassDao.addStudentClass(sc) == null) {
//						ret.setErrorCode(ErrorCodeEnum.Unknown_Error);
//						break;
//					}
//					student.getStudentClass().add(sc);
//				}
//			}
//			ret.setData(list);
		}
		return ret;
	}

	@Override
	public DataWrapper<Class> findClassById(int classId) {
		Class c = classdao.findClassById(classId);
		DataWrapper<Class> ret = new DataWrapper<Class>();
		if (c == null) {
			ret.setErrorCode(ErrorCodeEnum.Class_Not_Exist);
		} else {
			ret.setData(c);
		}
		return ret;
	}

	@Override
	public DataWrapper<List<Class>> getCurSemesterClassByPage(Integer pageSize,
			Integer pageNumber) {
		Semester s = semesterdao.getCurSemester();
		if (s == null) {
			return new DataWrapper<List<Class>>(ErrorCodeEnum.Current_Semester_Not_Set);
		}
		return classdao.getCurSemesterClassByPage(s, pageSize, pageNumber);
	}
	
	@Override
	public DataWrapper<List<Student>> getStudents(int id, int page, int pagesize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataWrapper<List<Student>> getStudentByClassByPage(int classid,
			int pagesize, int pagenumber) {
		// TODO Auto-generated method stub
		DataWrapper<List<Student>> ret = new DataWrapper<List<Student>>();
		Class c = classdao.findClassById(classid);
		if(c == null)
		{
			ret.setErrorCode(ErrorCodeEnum.Class_Not_Exist);
			return ret;
		}
		DataWrapper<List<StudentClass>> tempret = stuClassDao.getStudentByClassByPage(c, pagenumber, pagesize);
		if(tempret.getCallStatus()==CallStatusEnum.FAILED)
		{
			ret.setErrorCode(tempret.getErrorCode());
			return ret;
		}
		List<Student> data = new ArrayList<Student>();
		for(StudentClass sc : tempret.getData()){
			data.add(sc.getStudent());
		}
		ret.setCurrPageNum(tempret.getCurrPageNum());
		ret.setNumPerPage(tempret.getNumPerPage());
		ret.setTotalItemNum(tempret.getTotalItemNum());
		ret.setTotalPageNum(tempret.getTotalPageNum());
		ret.setData(data);
		return ret;
	}

	@Override
	public DataWrapper<List<ExperimentStatus>> experimentStatusList(Integer classId) {
		return new DataWrapper<List<ExperimentStatus>>(classdao.experimentStatusList(classId));
	}

	@Override
	public DataWrapper<List<StudentRecord>> getStudentRecordsByClassExpInPage(
			Integer classId, Integer expId, Integer pageSize, Integer pageNumber) {
		DataWrapper<List<StudentRecord>> ret = new DataWrapper<List<StudentRecord>>();
		Class c = classdao.findClassById(classId);
		Experiment e = expDao.findExperimentById(expId);
		if (c == null)
			ret.setErrorCode(ErrorCodeEnum.Class_Not_Exist);
		else if (e == null)
			ret.setErrorCode(ErrorCodeEnum.Experiment_Not_Exist);
		else
			ret = stuRecDao.getStudentRecordsByClassExpInPage(c, e, pageSize, pageNumber);
		return ret;
	}

	@Override
	public DataWrapper<List<Class>> getClassList(Integer labId,
			Integer teacherId) {
		return new DataWrapper<List<Class>>(classdao.getClassList(labId, teacherId));
	}
}
