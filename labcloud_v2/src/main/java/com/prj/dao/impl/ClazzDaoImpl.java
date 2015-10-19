package com.prj.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.prj.dao.ClazzDao;
import com.prj.entity.Clazz;
import com.prj.entity.Semester;
import com.prj.util.DataWrapper;

@Repository
public class ClazzDaoImpl extends BaseDaoHibernateImpl<Clazz, Long> implements ClazzDao {

    public ClazzDaoImpl() {
        super(Clazz.class);
    }

    public DataWrapper addClazz(Clazz clazz, long courseId, long teacherId, long semester) {
        return new DataWrapper(
            getSession()
                .createSQLQuery(
                    "insert into clazz(create_time,modify_time,clazz_hour,clazzroom,number,course_id,semester_id,teacher_id) value(?,?,?,?,?,?,?,?)")
                .setString(0, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .setString(1, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .setString(2, clazz.getClazzHour()).setString(3, clazz.getClazzroom())
                .setString(4, clazz.getNumber()).setLong(5, courseId).setLong(6, semester)
                .setLong(7, teacherId).executeUpdate());
    }

    public DataWrapper getClazzByTeacher(Long teaId) {
        return new DataWrapper(getSession().createCriteria(Clazz.class)
            .add(Restrictions.eq("teacher.id", teaId)).createCriteria("semester")
            .add(Restrictions.eq("status", Semester.Status.CURRENT)).list());
    }

    @SuppressWarnings("unchecked")
    public List<Clazz> listSutdentSemesterClazz(Long semesterId, Long accountId) {
        String hql = "from Clazz clz where (from Account a where a.id = ?) in elements(clz.students) and clz.semester.id =?";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setLong(0, accountId);
        query.setLong(1, semesterId);

        return query.list();
    }

    public boolean isStudentExistInClazz(long stuId, long clazzId) {
        List<Long> stuIds = new ArrayList<Long>();
        stuIds.add(stuId);
        List list = getSession().createCriteria(Clazz.class).add(Restrictions.eq("id", clazzId))
            .createAlias("students", "student").add(Restrictions.in("student.id", stuIds)).list();
        if (list.size() == 0)
            return false;
        else
            return true;
    }

    @SuppressWarnings("unchecked")
    public List<Clazz> getClazzByStudent(Long stuId) {
        List<Long> stuIds = new ArrayList<Long>();
        stuIds.add(stuId);
        return (List<Clazz>) getSession().createCriteria(Clazz.class)
            .createAlias("students", "student").add(Restrictions.in("student.id", stuIds))
            .createCriteria("semester").add(Restrictions.eq("status", Semester.Status.CURRENT))
            .list();
    }

    public DataWrapper getStudentListByPage(Long clazzId, int pageNum, int pageSize) {
        DataWrapper dw = new DataWrapper();
        Query q = getSession().createQuery(
            "select clazz.students from Clazz clazz where clazz.id=" + clazzId);
        q.setFirstResult((pageNum - 1) * pageSize);
        q.setMaxResults(pageSize);
        dw.setData(q.list());
        dw.setCurPageNum(pageNum);
        dw.setNumPerPage(pageSize);
        int totalsize = Integer.parseInt(getSession().createCriteria(Clazz.class)
            .add(Restrictions.eq("id", clazzId)).createCriteria("students")
            .setProjection(Projections.rowCount()).uniqueResult().toString());
        dw.setTotalItemNum(totalsize);
        dw.setTotalPageNum(totalsize % pageSize == 0 ? totalsize / pageSize : totalsize / pageSize
                                                                              + 1);
        return dw;
    }
    
}
