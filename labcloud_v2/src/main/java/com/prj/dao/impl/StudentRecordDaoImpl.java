package com.prj.dao.impl;

import com.prj.entity.Clazz;
import com.prj.entity.Experiment;
import com.prj.entity.FileRecord;
import com.prj.util.DataWrapper;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.prj.dao.StudentRecordDao;
import com.prj.entity.StudentRecord;

import java.math.BigInteger;
import java.util.List;

@Repository
public class StudentRecordDaoImpl extends BaseDaoHibernateImpl<StudentRecord, Long> implements StudentRecordDao {

	public StudentRecordDaoImpl() {
		super(StudentRecord.class);
	}


  public List<StudentRecord> getRecordByStuClass(long stuId, long classId) {
    return (List<StudentRecord>)getSession().createCriteria(StudentRecord.class)
      .add(Restrictions.and(
        Restrictions.eq("student.id", stuId),
        Restrictions.eq("clazz.id", classId)))
      .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
      .list();
  }

  public List<FileRecord> getFileListByRecord(long recordId) {
    return (List<FileRecord>)getSession().createCriteria(FileRecord.class)
      .add(Restrictions.eq("studentRecord.id",recordId))
      .list();
  }

  public List<StudentRecord> getRecordsByClazzExp(long clazzId, long expId) {
    return (List<StudentRecord>)getSession().createCriteria(StudentRecord.class)
      .add(Restrictions.and(
        Restrictions.eq("clazz.id", clazzId),
        Restrictions.eq("experiment.id", expId)))
      .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
      .list();
  }

  public DataWrapper getRecordByClazzExpByPage(Clazz clazz, Experiment experiment, int pageSize, int pageNumber) {
    DataWrapper ret = new DataWrapper();

    String hql = "from StudentRecord sr where sr.clazz.id = "+clazz.getId()+" and sr.experiment.id="+experiment.getId();

    Query query = getSession().createQuery(hql);

    //query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);

    List list = query.list();

    String count_sql = "select count(*) from student_record where class_id = "+clazz.getId()+" and experiment_id="+experiment.getId();

    SQLQuery count_query = getSession().createSQLQuery(count_sql);

    BigInteger count = (BigInteger)count_query.uniqueResult();

    ret.setData(list);

    ret.setTotalItemNum(count.intValue());

    ret.setCurPageNum(pageNumber);
    ret.setNumPerPage(pageSize);

    if (count.intValue() % pageSize == 0)
      ret.setTotalPageNum(count.intValue() / pageSize);
    else
      ret.setTotalPageNum(count.intValue() / pageSize + 1);


    return ret;
  }
}
