package com.prj.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Repository;

import com.prj.dao.CourseDao;
import com.prj.entity.Course;
import com.prj.entity.Experiment;
import com.prj.util.DataWrapper;

@Repository
public class CourseDaoImpl extends BaseDaoHibernateImpl<Course, Long> implements CourseDao {

    public CourseDaoImpl() {
        super(Course.class);
    }

    public DataWrapper listCourseExps(long courseId) {
        Course course = read(courseId);
        ArrayList<Experiment> exps = new ArrayList<Experiment>();
        for (Experiment exp : course.getExperiments()) {
            exps.add(exp);
        }
        return new DataWrapper(exps);
    }

    public DataWrapper listExps(long classId, long stuId) {

        String sql = "select clazzId, expId, temp.id as recordId, e.name as expName, e.virtualExpLink as virtualExpLink, experiment_comment, experiment_record, status, lab_id, machine_id, slot_id from (select * from (select id as clazzId, experiment_id as expId from (select * from clazz where id = "
                     + classId
                     + " ) c left join course_experiment ce\n"
                     + "on ce.course_id = c.course_id) ce \n"
                     + "left join (select * from student_record where student_id ="
                     + stuId
                     + ") sr \n"
                     + "on ce.clazzId = sr.class_id and ce.expId = sr.experiment_id) temp\n"
                     + "left join experiment e\n" + "on temp.expId= e.id";

        SQLQuery query = getSession().createSQLQuery(sql);
        query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);

        List list = query.list();

        return new DataWrapper(list);
    }

    public DataWrapper addCourseExp(long courseId, long expId) {
        return new DataWrapper(
            getSession()
                .createSQLQuery(
                    "INSERT INTO course_experiment(course_experiment.course_id,course_experiment.experiment_id) VALUE(?,?)")
                .setLong(0, courseId).setLong(1, expId).executeUpdate());

    }

    public DataWrapper deleteCourseExp(long courseId, long expId) {
        return new DataWrapper(
            getSession()
                .createSQLQuery(
                    "DELETE FROM course_experiment WHERE course_experiment.course_id = ? AND course_experiment.experiment_id = ?")
                .setLong(0, courseId).setLong(1, expId).executeUpdate());
    }
}
