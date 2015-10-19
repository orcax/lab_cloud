package com.prj.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prj.dao.AccountDao;
import com.prj.dao.ClazzDao;
import com.prj.dao.ClazzReservationDao;
import com.prj.dao.SemesterDao;
import com.prj.dao.StudentReservationDao;
import com.prj.entity.Account;
import com.prj.entity.Clazz;
import com.prj.entity.ClazzReservation;
import com.prj.entity.Experiment;
import com.prj.entity.Reservation;
import com.prj.entity.Reservation.Status;
import com.prj.entity.StudentReservation;
import com.prj.service.StudentReservationService;
import com.prj.util.DataWrapper;
import com.prj.util.ErrorCode;
import com.prj.util.PathEnum;

@Service
public class StudentReservationServiceImpl extends BaseServiceImpl<StudentReservation, Long>
                                                                                            implements
                                                                                            StudentReservationService {
    StudentReservationDao sresd;

    @Autowired
    AccountDao            accountDao;

    @Autowired
    SemesterDao           semesterDao;

    @Autowired
    ClazzDao              clazzDao;

    @Autowired
    ClazzReservationDao   clazzReservationDao;

    @Autowired
    public StudentReservationServiceImpl(StudentReservationDao sresd) {
        super(StudentReservation.class, sresd);
        this.sresd = sresd;
    }

    public DataWrapper create(StudentReservation entity) {
        assertUniqueNumber(entity.getNumber());
        return super.createEntity(entity);
    }

    public DataWrapper findByLDS(long lab, Date date, long slot) {
        // TODO Auto-generated method stub
        return null;
    }

    public DataWrapper cancelStudentReservationApplication(long id, Account account) {
        StudentReservation sr = sresd.read(id);
        for (Account count : sr.getStudents()) {
            if (count.getId() == account.getId()) {
                sr.getStudents().remove(count);
                break;
            }
        }
        sresd.update(sr);
        return DataWrapper.voidSuccessRet;
    }

    public DataWrapper listAllReservationStudents(long id) {
        StudentReservation sr = sresd.read(id);
        return new DataWrapper(sr.getStudents());
    }

    /** 添加学生自行预约 **/
    public DataWrapper addStudentReservationApplication(long id, Account account) {
        StudentReservation sr = sresd.read(id);
        Account uAccount = accountDao.read(account.getId());
        uAccount.getStudentReservation().add(sr);
        accountDao.update(uAccount);
        return DataWrapper.voidSuccessRet;
    }

    public DataWrapper handle(long id, long[] labTeacherIds) {
        // TODO Auto-generated method stub
        return null;
    }

    public DataWrapper add(StudentReservation stuResProfile, long lab, Date date, int slotId) {
        // TODO Auto-generated method stub
        return null;
    }

    public DataWrapper addStuReservation(StudentReservation studentReservation, long semesterId,
                                         long experimentId, long labId, long slotId, long reserverId) {
        //判断是否已经存在

        return sresd.addStudentReservation(studentReservation, semesterId, experimentId, labId,
            reserverId, slotId);
    }

    public DataWrapper deleteStuReservation(long id) {
        if (sresd.getStudents(id).size() == 0 && sresd.getTeachers(id).size() == 0) {
            return super.delete(id);
        } else {
            return new DataWrapper(ErrorCode.INTERNAL_ERROR, null);
        }
    }

    public DataWrapper cancelStudentReservation(long id) {
        StudentReservation reservation = sresd.read(id);
        if (reservation == null) {
            return new DataWrapper(ErrorCode.NOT_FOUND);
        }
        if (reservation.getLabTeachers().size() > 0) {
            reservation.getLabTeachers().clear();
        }
        reservation.getStudents().clear();
        reservation.setStatus(Status.END);
        try {
            sresd.merge(reservation);
            sresd.delete(reservation);
            return new DataWrapper();
        } catch (Exception e) {
            return new DataWrapper(ErrorCode.INTERNAL_ERROR);
        }
    }

    public DataWrapper getReservationsBySemAndLab(Long semesterId, long labId) {
        // TODO Auto-generated method stub
        return sresd.getClazzesBySemesterAndLabId(semesterId, labId);
    }

    public DataWrapper getStudentReservationBySemsterAndAccount(long semesterId, long accountId) {
        List<Reservation> stuReservations = sresd.getStudentReservationByAccIdAndSemeId(accountId,
            semesterId);
        return new DataWrapper(stuReservations);
    }

    public DataWrapper getStudentReservationByAccountPage(long semesterId, long accountId,
                                                          int pageSize, int pageNumber) {
        List<Reservation> stuReservations = sresd.getStudentReservationByAccIdAndSemeId(accountId,
            semesterId);
        ArrayList<Long> ids = new ArrayList<Long>();
        if (stuReservations.size() == 0) {
            DataWrapper dw = new DataWrapper(new ArrayList<String>());
            dw.setCurPageNum(1);
            return dw;
        }
        for (Reservation res : stuReservations) {
            ids.add(res.getId());
        }
        ArrayList<Criterion> conditions = new ArrayList<Criterion>();
        conditions.add(Restrictions.in("id", ids));
        return sresd.getPageByConditions(pageNumber, pageSize, conditions);
    }

    // 实现非常取巧，未来需要优化
    public DataWrapper getAvailableStudentReservation(long semesterId, long accountId,
                                                      int pageSize, int pageNumber) {
        ArrayList<Experiment> clist = getAvailableExperiments(semesterId, accountId);
        ArrayList<Criterion> conditions = new ArrayList<Criterion>();
        if (clist.size() == 0) {
            DataWrapper dw = new DataWrapper(new ArrayList<String>());
            dw.setCurPageNum(1);
            return dw;
        }
        conditions.add(Restrictions.in("experiment", clist));
        conditions.add(Restrictions.eq("semester", semesterDao.read(semesterId)));
        return sresd.getPageByConditions(pageNumber, pageSize, conditions);
    }

    private ArrayList<Experiment> getAvailableExperiments(long semesterId, long accountId) {
        List<Clazz> clazzs = clazzDao.listSutdentSemesterClazz(semesterId, accountId);
        LinkedList<Experiment> list = new LinkedList<Experiment>();
        for (Clazz clz : clazzs) {
            for (Experiment exp : clz.getCourse().getExperiments()) {
                list.add(exp);
            }
        }

        Set<StudentReservation> reservations = accountDao.read(accountId).getStudentReservation();

        ArrayList<Experiment> clist = new ArrayList<Experiment>();
        for (Experiment exp : list) {
            boolean in = false;
            for (StudentReservation str : reservations) {
                if (str.getExperiment().getId() == exp.getId()) {
                    in = true;
                }
            }
            if (!in) {
                clist.add(exp);
            }
        }
        return clist;
    }

    private ArrayList<Criterion> resercationStatusConditions(long semesterId, Status status) {
        ArrayList<Criterion> conditions = new ArrayList<Criterion>();
        conditions.add(Restrictions.eq("semester", semesterDao.read(semesterId)));
        if (status != null) {
            conditions.add(Restrictions.eq("status", status));
        }
        return conditions;
    }

    public List<StudentReservation> getReservationByDateSpan(long semesterId, Date startDate,
                                                             Date endDate) {
        ArrayList<Criterion> conditions = resercationStatusConditions(semesterId, null);
        conditions.add(Restrictions.between("applyDate", startDate, endDate));
        conditions.add(Restrictions.ne("status", Status.REJECTED));
        return sresd.getAllByConditions(conditions);
    }

    public DataWrapper getStudentReservationByTime(long stuId, Date date, int slot, long labid) {
        DataWrapper ret = new DataWrapper();
        Account student = accountDao.read(stuId);
        List<StudentReservation> reservations = sresd
            .getStudentReservationByTime(date, slot, labid);
        if (reservations != null && reservations.size() != 0) {
            for (Account a : reservations.get(0).getStudents()) {
                if (a.getId() == student.getId()) {
                    reservations.get(0).setType(PathEnum.ReservationType.studentReservation);
                    ret.setData(reservations.get(0));
                    return ret;
                }
            }
            ret.setErrorCode(ErrorCode.NOT_FOUND);
        } else {
            ret.setErrorCode(ErrorCode.NOT_FOUND);
        }
        return ret;
    }

}
