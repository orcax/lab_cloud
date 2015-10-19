package com.prj.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
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
import com.prj.entity.Lab;
import com.prj.entity.Reservation;
import com.prj.entity.Reservation.Status;
import com.prj.entity.Slot;
import com.prj.service.ClazzReservationService;
import com.prj.util.DataWrapper;
import com.prj.util.ErrorCode;
import com.prj.util.PathEnum;
import com.prj.util.PathEnum.ReservationStatus;

@Service
public class ClazzReservationServiceImpl extends BaseServiceImpl<ClazzReservation, Long> implements
                                                                                        ClazzReservationService {
    private static Logger logger = Logger.getLogger(ClazzReservationServiceImpl.class);

    ClazzReservationDao   clresd;

    @Autowired
    StudentReservationDao stresd;

    @Autowired
    SemesterDao           semesterDao;

    @Autowired
    AccountDao            accountDao;

    @Autowired
    ClazzDao              clazzDao;

    @Autowired
    public ClazzReservationServiceImpl(ClazzReservationDao clresd) {
        super(ClazzReservation.class, clresd);
        this.clresd = clresd;
    }

    public DataWrapper create(ClazzReservation entity) {
        return null;
    }

    public DataWrapper findByLDS(long lab, Date date, long slot) {
        return null;
    }

    public DataWrapper handle(long id, long[] labTeacherIds) {
        return clresd.addTeachers(id, labTeacherIds);
    }

    public DataWrapper addClazzReservation(ClazzReservation classRes, long clazzId,
                                           long experimentId, long slotId, long labId,
                                           long reserverId) {
        ArrayList<Criterion> conditions = new ArrayList<Criterion>();
        conditions.add(Restrictions.eq("slot", new Slot(slotId)));
        conditions.add(Restrictions.eq("applyDate", classRes.getApplyDate()));
        conditions.add(Restrictions.eq("lab", new Lab(labId)));
        conditions.add(Restrictions.ne("status", Reservation.Status.REJECTED));
        int count = clresd.countByConditions(conditions);
        int st_count = stresd.countByConditions(conditions);
        logger.info("Get duplicative class reservation:" + count);
        logger.info("Get duplicative student reservation:" + st_count);

        if (count == 0 && st_count == 0) {
            return clresd.addClazzReservation(classRes, clazzId, experimentId, slotId, labId,
                reserverId);
        } else {
            //~ 如果同一个教师的不同班级，可以重复预约
            List<ClazzReservation> duplicateReservations = clresd.getAllByConditions(conditions);
            boolean needBlock = false;
            if (duplicateReservations.size() == 0) {
                needBlock = true;
            }
            for (ClazzReservation clazzReservation : duplicateReservations) {
                //同一个教师，不同班级
                if (clazzReservation.getReserver().getId() == reserverId
                    && clazzId != clazzReservation.getClazz().getId()) {
                    continue;
                } else {
                    //遇到不满足条件的情况，终止，并拦截请求
                    needBlock = true;
                    break;
                }
            }
            if (needBlock) {
                DataWrapper dw = new DataWrapper();
                dw.setErrorCode(ErrorCode.DUPLICATION);
                return dw;
            } else {
                return clresd.addClazzReservation(classRes, clazzId, experimentId, slotId, labId,
                    reserverId);
            }
        }
    }

    public DataWrapper listClazzReservationBySWS(long id, Integer weekIndex,
                                                 ReservationStatus status) {
        return null;
    }

    public DataWrapper listClazzReservationByTWS(long id, Integer weekIndex,
                                                 ReservationStatus status) {
        return null;
    }

    public DataWrapper deleteClazzReservation(long id) {
        if (clresd.getTeachers(id).size() == 0) {
            return super.delete(id);
        } else {
            return new DataWrapper(ErrorCode.INTERNAL_ERROR, null);
        }
    }

    public DataWrapper cancelClazzReservation(long id) {
        ClazzReservation reservation = clresd.read(id);
        if (reservation == null) {
            return new DataWrapper(ErrorCode.NOT_FOUND);
        }
        if (reservation.getLabTeachers().size() > 0) {
            reservation.getLabTeachers().clear();
        }
        reservation.setStatus(Status.END);
        try {
            clresd.merge(reservation);
            clresd.delete(reservation);
            return new DataWrapper();
        } catch (Exception e) {
            return new DataWrapper(ErrorCode.INTERNAL_ERROR);
        }
    }

    public DataWrapper getReservationsByCondition(Long semesterId, long teacherId) {
        return clresd.getClazzesBySemesterAndTeacher(semesterId, teacherId);
    }

    public DataWrapper getApprovedReservationsByCondition(Long semesterId, long labTeacherId) {
        return clresd.getClazzesBySemesterAndLabteacher(semesterId, labTeacherId);
    }

    public DataWrapper getReservationsBySemAndLab(Long semesterId, long labId) {
        return clresd.getClazzesBySemesterAndLabId(semesterId, labId);
    }

    public DataWrapper getAllReservationBySemester(long semesterId, Status status) {
        ArrayList<Criterion> conditions = resercationStatusConditions(semesterId, status);
        return new DataWrapper(clresd.getAllByConditions(conditions));
    }

    public DataWrapper getAllReservationByStatusPage(long semesterId, Status status, int pageSize,
                                                     int pageNumber) {
        ArrayList<Criterion> conditions = resercationStatusConditions(semesterId, status);
        return clresd.getPageByConditions(pageNumber, pageSize, conditions, reservationOrder());
    }

    public DataWrapper getTeacherReservationByPage(long semesterId, Status status, long account,
                                                   int pageSize, int pageNumber) {
        ArrayList<Criterion> conditions = new ArrayList<Criterion>();
        ArrayList<Criterion> clazzCriterions = new ArrayList<Criterion>();
        clazzCriterions.add(Restrictions.eq("semester", semesterDao.read(semesterId)));
        clazzCriterions.add(Restrictions.eq("teacher", accountDao.read(account)));
        List<Clazz> clazzs = clazzDao.getAllByConditions(clazzCriterions);
        if (clazzs.size() > 0) {
            conditions.add(Restrictions.in("clazz", clazzs));
        } else {
            return new DataWrapper(new ArrayList<String>());
        }
        conditions.add(Restrictions.eq("status", status));
        return clresd.getPageByConditions(pageNumber, pageSize, conditions);
    }

    private ArrayList<Criterion> resercationStatusConditions(long semesterId, Status status) {
        ArrayList<Criterion> conditions = new ArrayList<Criterion>();
        ArrayList<Criterion> clazzCondition = new ArrayList<Criterion>();
        clazzCondition.add(Restrictions.eq("semester", semesterDao.read(semesterId)));
        List<Clazz> clazzs = clazzDao.getAllByConditions(clazzCondition);
        conditions.add(Restrictions.in("clazz", clazzs));
        if (status != null) {
            conditions.add(Restrictions.eq("status", status));
        }
        return conditions;
    }

    private ArrayList<Order> reservationOrder() {
        ArrayList<Order> orders = new ArrayList<Order>();
        orders.add(Order.desc("applyDate"));
        return orders;
    }

    public DataWrapper getStudentReservationByPage(long semesterId, long account, int pageSize,
                                                   int pageNumber) {
        // 获取该学生本学期的所有班级
        Account student = accountDao.read(account);
        Set<Clazz> clazzs = student.getStudentClazzs();
        ArrayList<Criterion> conditions = new ArrayList<Criterion>();
        if (clazzs.size() == 0) {
            DataWrapper dw = new DataWrapper(new ArrayList<String>());
            dw.setCurPageNum(1);
            return dw;
        } else {
            conditions.add(Restrictions.eq("status", Status.APPROVED));
            conditions.add(Restrictions.in("clazz", clazzs));
            return clresd.getPageByConditions(pageNumber, pageSize, conditions);
        }
    }

    public List<ClazzReservation> getReservationByDateSpan(long semesterId, Date startDate,
                                                           Date endDate) {
        ArrayList<Criterion> conditions = resercationStatusConditions(semesterId, null);
        conditions.add(Restrictions.between("applyDate", startDate, endDate));
        conditions.add(Restrictions.ne("status", Status.REJECTED));
        return clresd.getAllByConditions(conditions);
    }

    public DataWrapper getClassReservationByTime(long stuId, java.sql.Date date, int slot,
                                                 long labid) {
        DataWrapper ret = new DataWrapper();
        Account student = accountDao.read(stuId);
        List<ClazzReservation> reservations = clresd.getClazzReservationByTime(date, slot, labid);
        if (reservations != null && reservations.size() != 0) {
          for (ClazzReservation reservation : reservations) {
            for (Account a : reservation.getClazz().getStudents()) {
              if (a.getId() == student.getId()) {
                reservation.setType(PathEnum.ReservationType.classReservation);
                ret.setData(reservation);
                return ret;
              }
            }
          }
            ret.setErrorCode(ErrorCode.NOT_FOUND);
        } else {
            ret.setErrorCode(ErrorCode.NOT_FOUND);
        }
        return ret;
    }

    public List<ClazzReservation> getLabTeacherReservationByDateSpan(long semesterId,
                                                                     long labTeacherId,
                                                                     Date startDate, Date endDate) {
        ArrayList<Criterion> conditions = resercationStatusConditions(semesterId, null);
        conditions.add(Restrictions.between("applyDate", startDate, endDate));
        conditions.add(Restrictions.eq("status", Status.APPROVED));
        Account ta = accountDao.read(labTeacherId);

        return null;
    }

    public DataWrapper getClazzReservationByAccountAndSemester(long accountId, long semesterId) {
        return new DataWrapper(
            clresd.getClazzReservationByAccountAndSemester(accountId, semesterId));
    }

    public DataWrapper getLabTeachersByReservation(long reservationId) {
        return new DataWrapper(clresd.getLabTeachersByReservation(reservationId));
    }

    public DataWrapper getLabTeacherReservations(long accountId, long semesterId) {
        ArrayList<Criterion> conditions = resercationStatusConditions(semesterId, null);
        return new DataWrapper(clresd.getLabTeacherReservations(accountId, conditions));
    }

    public DataWrapper getLabTeacherReservationByPage(long semesterId, Status status, long account,
                                                      int pageSize, int pageNumber) {
        DataWrapper dw = new DataWrapper();
        Criteria crt = clresd.createCriteria().createAlias("labTeachers", "lt")
            .add(Restrictions.eq("lt.id", account)).add(Restrictions.eq("status", status))
            .createAlias("clazz", "c").add(Restrictions.eq("c.semester.id", semesterId));
        // 查询总数
        int totalCount = Integer.parseInt(crt.setProjection(Projections.rowCount()).uniqueResult()
            .toString());
        dw.setTotalItemNum(totalCount);

        crt.setProjection(null);
        crt.setFirstResult((pageNumber - 1) * pageSize);
        crt.setMaxResults(pageSize);
        Disjunction re = Restrictions.or();
        crt.add(re);
        crt.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        @SuppressWarnings("unchecked")
        List list = crt.list();
        dw.setCurPageNum(pageNumber);
        dw.setNumPerPage(pageSize);
        if (totalCount % pageSize == 0)
            dw.setTotalPageNum(totalCount / pageSize);
        else
            dw.setTotalPageNum(totalCount / pageSize + 1);

        dw.setData(list);
        return dw;
    }

}
