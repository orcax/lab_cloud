package com.prj.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.prj.dao.StudentReservationDao;
import com.prj.entity.Reservation;
import com.prj.entity.StudentReservation;
import com.prj.util.DataWrapper;

@Repository
public class StudentReservationDaoImpl extends
		BaseDaoHibernateImpl<StudentReservation, Long> implements
		StudentReservationDao {

	public StudentReservationDaoImpl() {
		super(StudentReservation.class);
	}

	public DataWrapper addStudentReservation(
			StudentReservation studentReservation, long semesterId,
			long experimentId, long labId, long reserverId, long slotId) {
		return new DataWrapper(
				getSession()
						.createSQLQuery(
								"insert into "
										+ "student_reservation(create_time,modify_time,number,status,max_count,experiment_id,lab_id,reserver_id,slot_id,applyDate, semester_id) "
										+ "value(?,?,?,?,?,?,?,?,?,?,?)")
						.setString(0,
								studentReservation.getCreateTime().toString())
						.setString(1,
								studentReservation.getModifyTime().toString())
						.setString(2, studentReservation.getNumber())
						.setString(3, Reservation.Status.APPROVED.toString())
						.setInteger(4, studentReservation.getMaxCount())
						.setLong(5, experimentId)
						.setLong(6, labId)
						.setLong(7, reserverId)
						.setLong(8, slotId)
						.setString(9,
								studentReservation.getApplyDate().toString())
						.setLong(10, semesterId).executeUpdate());
	}

	@SuppressWarnings("unchecked")
	public List<Long> getTeachers(long reservationId) {
		return getSession()
				.createSQLQuery(
						"select s.lab_teacher_id from student_reservation_teacher s where s.student_reservation_id = ?")
				.setLong(0, reservationId).list();
	}

	@SuppressWarnings("unchecked")
	public List<Long> getStudents(long reservationId) {
		return getSession()
				.createSQLQuery(
						"select s.student_id from student_reservation_student s where s.student_reservation_id = ?")
				.setLong(0, reservationId).list();
	}

	public DataWrapper getClazzesBySemesterAndLabId(long semesterId, long labId) {
		String hql = "from StudentReservation as sr  where sr.lab.id = ? and sr.semester.id=?";
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.setLong(0, labId);
		query.setLong(1, semesterId);

		return new DataWrapper(query.list());
	}

	@SuppressWarnings("unchecked")
	public List<Reservation> getStudentReservationByAccIdAndSemeId(
			long accountId, long semesterId) {
		String hql = "from StudentReservation sr where (from Account a where a.id = ?) in elements(sr.students) and sr.semester.id =?";
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.setLong(0, accountId);
		query.setLong(1, semesterId);

		return query.list();
	}

  public List<StudentReservation> getStudentReservationByTime(Date date, int slot, long labid) {
    return (List<StudentReservation>)getSession().createCriteria(StudentReservation.class)
      .add(Restrictions.eq("applyDate", date))
      .add(Restrictions.eq("lab.id", labid))
      .add(Restrictions.eq("status", Reservation.Status.APPROVED))
      .createCriteria("slot")
      .add(Restrictions.eq("slotNo",slot))
      .list();
  }

}
