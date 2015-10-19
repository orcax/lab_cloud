package com.prj.serviceImpl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.prj.dao.AdministratorDao;
import com.prj.dao.LabPlanDao;
import com.prj.dao.SemesterDao;
import com.prj.entity.Administrator;
import com.prj.entity.Semester;
import com.prj.entity.Semester.Status;
import com.prj.service.SemesterService;
import com.prj.util.DataWrapper;
import com.prj.util.ErrorCodeEnum;

@Service("SemesterServiceImpl")
public class SemesterServiceImpl implements SemesterService{

	@Resource(name="SemesterDaoImpl")
	SemesterDao semesterdao;
	@Resource(name = "AdministratorDaoImpl")
	AdministratorDao adminDao;
	@Resource(name = "LabPlanDaoImpl")
	LabPlanDao lpdao;
	
	@Override
	public DataWrapper<Semester> addSemester(Semester s, Integer adminId) {
		DataWrapper<Semester> ret = new DataWrapper<Semester>();
		Date startdate = s.getStartDate();
		Semester lastsemester = semesterdao.getLastSemester();
		Administrator a = adminDao.findAdministratorById(adminId);
		if(lastsemester != null &&(lastsemester.getEndDate().after(startdate)||s.getEndDate().before(startdate)))
			ret.setErrorCode(ErrorCodeEnum.Date_Error);
		else
		{
			s.setUpdater(a);
			s.setStatus(Status.FUTURE);
			semesterdao.addSemester(s);
		}
		return ret;
	}

	@Override
	public DataWrapper<Semester> getCurSemester() {
		DataWrapper<Semester> ret = new DataWrapper<Semester>();
		ret.setData(semesterdao.getCurSemester().generateWeekInfo());
		if (ret.getData() == null) {
			ret.setErrorCode(ErrorCodeEnum.Current_Semester_Not_Set);
		}
		return ret;
	}

	@Override
	public DataWrapper<Semester> setCurSemester(Integer id) {
		DataWrapper<Semester> ret = new DataWrapper<Semester>();
		Semester curS = semesterdao.getCurSemester();
		Semester s = semesterdao.findSemesterById(id);
		if (curS != null) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			if (curS.getEndDate().before(cal.getTime())) {
				if (s == null) {
					ret.setErrorCode(ErrorCodeEnum.Semester_Not_Exist);
				} else {
					switch (s.getStatus()) {
					case CURRENT:
//						ret.setErrorCode(ErrorCodeEnum.Already_Current);
						break;
					case FUTURE:
						curS.setStatus(Status.PASSED);
						s.setStatus(Status.CURRENT);
						semesterdao.updateSemester(curS);
						semesterdao.updateSemester(s);
						ret.setData(s.generateWeekInfo());
						break;
					case PASSED:
						ret.setErrorCode(ErrorCodeEnum.Already_Passed);
						break;
					default:
						break;
					}
				}
			} else {
				ret.setErrorCode(ErrorCodeEnum.Current_Semester_Not_Passed);
			}
		} else {
			if (s == null) {
				ret.setErrorCode(ErrorCodeEnum.Semester_Not_Exist);
			} else {
				s.setStatus(Status.CURRENT);
				semesterdao.updateSemester(s);
				ret.setData(s.generateWeekInfo());
			}
		}
		return ret;
	}

	@Override
	public DataWrapper<List<Semester>> getAllSemester() {
		DataWrapper<List<Semester>> ret = new DataWrapper<List<Semester>>();
		ret.setData(semesterdao.getAllSemesters());
		return ret;
	}

	@Override
	public DataWrapper<List<String>> getDateByWeekIndex(int index) {
		// TODO Auto-generated method stub
		DataWrapper<List<String>> ret = new DataWrapper<List<String>>();
		List<String> list = lpdao.getDateByWeek(index,semesterdao.getCurSemester().getStartDate());

		if (list == null)
			ret.setErrorCode(ErrorCodeEnum.Out_Of_Bound);
		else {
			ret.setData(list);
		}
		return ret;
	}
	
}
