package com.prj.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.prj.entity.ExperimentStatus;

import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.prj.dao.ExperimentDao;
import com.prj.entity.Experiment;
import com.prj.entity.Lab;
import com.prj.util.DataWrapper;

@Repository
public class ExperimentDaoImpl extends BaseDaoHibernateImpl<Experiment, Long>
		implements ExperimentDao {

	public ExperimentDaoImpl() {
		super(Experiment.class);
	}

	public DataWrapper listLabsByExp(long expId) {
		Experiment experiment = (Experiment) getSession().get(Experiment.class,
				expId);
		ArrayList<Lab> labs = new ArrayList<Lab>();
		for (Lab lab : experiment.getLabs()) {
			labs.add(lab);
		}
		return new DataWrapper(labs);
	}

	public DataWrapper addLabExp(long labId, long expId) {
		return new DataWrapper(
				getSession()
						.createSQLQuery(
								"INSERT INTO lab_experiment(lab_experiment.lab_id,lab_experiment.experiment_id) VALUE(?,?)")
						.setLong(0, labId).setLong(1, expId).executeUpdate());
	}

	public DataWrapper deleteLabExp(long labId, long expId) {
		return new DataWrapper(
				getSession()
						.createSQLQuery(
								"DELETE FROM lab_experiment WHERE lab_experiment.lab_id = ? AND lab_experiment.experiment_id = ?")
						.setLong(0, labId).setLong(1, expId).executeUpdate());
	}

	public DataWrapper statusListByExpIds(List<Long> expIds, int totalNumber, long clazzId) {

    String expidstr = "";
    for (long l : expIds){
      expidstr += l+",";
    }
    expidstr = expidstr.substring(0,expidstr.length()-1);

		String sql = "select experimentId, experimentName, submittedNumber, totalNumber, completedNumber, status from (SELECT e.name as experimentName, e.id as experimentId,"+clazzId+" as classId, count(student_id) as submittedNumber,"+totalNumber+" as totalNumber, count(CASE WHEN `experiment_record` >0 THEN 1 ELSE NULL END) as completedNumber FROM (select * from student_record where class_id = "+clazzId+") sr right join experiment e\n" +
      "on sr.experiment_id = e.id\n" +
      " where (e.id in ("+expidstr+") and (sr.class_id = "+clazzId+" or sr.class_id is null)) group by e.id) experimentStatus\n" +
 " left join class_reservation cr\n" +
      "on cr.experiment_id = experimentStatus.experimentId and experimentStatus.classId = cr.class_id\n";
    SQLQuery query = getSession().createSQLQuery(sql);
    query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);

    List<Map<String, Object>> templist = query.list();

    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    String lastExpId = "";
    Map<String, Object> lastExp = new HashMap<String, Object>();
    if (templist.size()!=0){
      lastExpId = templist.get(0).get("experimentId").toString();
    }


    boolean exist = false;

    for (Map<String, Object> map: templist){
      if (!map.get("experimentId").toString().equals(lastExpId)){
        if (!exist){
          list.add(lastExp);
        }else {
          exist = false;
        }
      }
      if(map.get("status")!=null&&map.get("status").toString().equals("APPROVED")&&!exist){
        exist = true;
        list.add(map);
      }
      lastExpId = map.get("experimentId").toString();
      lastExp = map;
    }

    if (!exist&&templist.size()!=0){
      list.add(lastExp);
    }

    String countSql = "select sum(cr.count) as count, cr.experiment_id as expId from class_reservation as cr where cr.experiment_id in("+ expidstr+") and cr.class_id=" + clazzId + " group by cr.experiment_id";
    SQLQuery countQuery = getSession().createSQLQuery(countSql);
    countQuery.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
    List<Map<String, Object>> countList = countQuery.list();

    Map<String, Integer> staticticsMap = new HashMap<String, Integer>();
    for(Map<String, Object> map : countList){
        staticticsMap.put(map.get("expId").toString(), Integer.parseInt(map.get("count").toString()));
    }

    for(Map<String, Object> map : list){
        String expId = map.get("experimentId").toString();
        if(staticticsMap.containsKey(expId.toString())){
            map.put("reservCount",staticticsMap.get(expId));
        }else{
            map.put("reservCount",0l);
        }
        if (map.get("status")==null||!map.get("status").toString().equals("APPROVED")){
            map.put("reservCount",0l);
        }
    }
    return new DataWrapper(list);
	}

	public List<Experiment> getStudentAvailableExperimentOfSermester(
			long accountId, long semesterId) {
		return getSession()
				.createQuery(
						"select clz.course.experiments form Clazz clz where (from Account a where a.id = ?) in elements(clz.students) and clz.semester.id =?")
				.setLong(0, accountId).setLong(1, semesterId).list();

	}

}
