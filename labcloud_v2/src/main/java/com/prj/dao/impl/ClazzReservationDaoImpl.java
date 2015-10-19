package com.prj.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.prj.entity.Account;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.prj.dao.ClazzReservationDao;
import com.prj.entity.ClazzReservation;
import com.prj.entity.Reservation;
import com.prj.entity.Reservation.Status;
import com.prj.util.DataWrapper;

@Repository
public class ClazzReservationDaoImpl extends BaseDaoHibernateImpl<ClazzReservation, Long> implements
                                                                                         ClazzReservationDao {

    public ClazzReservationDaoImpl() {
        super(ClazzReservation.class);
    }

    public DataWrapper addClazzReservation(ClazzReservation classRes, long clazzId,
                                           long experimentId, long slotId, long labId,
                                           long reserverId) {
        if (classRes != null) {
            String sql = "insert into class_reservation(create_time,modify_time,number,status,count,experiment_id,lab_id,reserver_id,slot_id,class_id,applyDate) "
                         + "value(?,?,?,?,?,?,?,?,?,?,?)";
            Session session = getSession();
            Query query = session.createSQLQuery(sql);
            query.setString(0, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            query.setString(1, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            query.setString(2, classRes.getNumber());
            query.setString(3, Status.PENDING.toString());
            query.setLong(4, classRes.getCount());
            query.setLong(5, experimentId);
            query.setLong(6, labId);
            query.setLong(7, reserverId);
            query.setLong(8, slotId);
            query.setLong(9, clazzId);
            query.setString(10, classRes.getApplyDate().toString());

            int recordNum = query.executeUpdate();
        }
        System.out.println(classRes.getId());
        return DataWrapper.voidSuccessRet;
    }

    public List<Long> getTeachers(long reservationId) {
        return getSession()
            .createSQLQuery(
                "select c.lab_teacher_id from class_reservation_teacher c where c.class_reservation_id = ?")
            .setLong(0, reservationId).list();
    }

    public DataWrapper addTeachers(long id, long[] teachers) {
        Session session = getSession();
        String sql = "insert into class_reservation_teacher(lab_teacher_id,class_reservation_id) values ";
        for (Long teacherId : teachers) {
            sql = sql + "(" + teacherId + "," + id + "),";
        }
        sql = sql.substring(0, sql.length() - 1);
        Query query = session.createSQLQuery(sql);
        return new DataWrapper(query.executeUpdate());
    }

    public DataWrapper getClazzesBySemesterAndTeacher(long semesterId, long teacherId) {
        String hql = "from ClazzReservation as cr where cr.clazz.id in (select cz.id from Clazz as cz where cz.semester.id = ? and cz.teacher.id = ?)";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setLong(0, semesterId);
        query.setLong(1, teacherId);

        return new DataWrapper(query.list());
    }

    public DataWrapper getClazzesBySemesterAndLabteacher(long semesterId, long labTeacherId) {
        String hql = "select cr from ClazzReservation as cr , Clazz as cz where cz.teacher in elements(cr.labTeachers) and cz.teacher.id = ? and cz.semester.id=?";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setLong(0, semesterId);
        query.setLong(1, labTeacherId);

        return new DataWrapper(query.list());
    }

    public DataWrapper getClazzesBySemesterAndLabId(long semesterId, long labId) {
        String hql = "select cr from ClazzReservation as cr , Clazz as cz where cr.lab.id = ? and cz.semester.id=?";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setLong(0, labId);
        query.setLong(1, semesterId);

        return new DataWrapper(query.list());
    }

    public List<Reservation> getClazzReservationByAccountAndSemester(long accountId, long semesterId) {
        String hql = "from ClazzReservation as cr where cr.clazz.teacher.id=? and cr.clazz.semester.id = ?)";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setLong(0, accountId);
        query.setLong(1, semesterId);

        return query.list();
    }

    public List<ClazzReservation> getClazzReservationByTime(Date date, int slot, long labid) {
        return (List<ClazzReservation>) getSession().createCriteria(ClazzReservation.class)
            .add(Restrictions.eq("applyDate", date)).add(Restrictions.eq("lab.id", labid))
            .add(Restrictions.eq("status", Status.APPROVED)).createCriteria("slot")
            .add(Restrictions.eq("slotNo", slot)).list();
    }

    public List<ClazzReservation> getLabTracherClazzReservationsByDateSpan(long semesterId,
                                                                           long labTeacherId,
                                                                           Date startDate,
                                                                           Date endDate) {

        return null;
    }

    public List<ClazzReservation> getLabTeacherReservations(long accountId,
                                                            ArrayList<Criterion> conditions) {
        Criteria crt = getSession().createCriteria(ClazzReservation.class);
        for (Criterion cr : conditions) {
            crt.add(cr);
        }
        crt.createCriteria("labTeachers").add(Restrictions.eq("id", accountId));
        return crt.list();
    }

    public List<Account> getLabTeachersByReservation(long reservationId) {
        ClazzReservation clazzReservation = read(reservationId);
        return new ArrayList<Account>(clazzReservation.getLabTeachers());
    }

    /** 
     * @see com.prj.dao.ClazzReservationDao#countClazzReservtionStudent(long, long)
     */
    public Integer countClazzReservtionStudent(long clazzId, long expId) {
        String hql = "select sum(cr.count) from ClazzReservation as cr where cr.clazz.id = ? and cr.experiment.id=?";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setLong(0, clazzId);
        query.setLong(1, expId);
        int result =  query.getFirstResult();
        return result;
    }

}
