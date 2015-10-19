package com.prj.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.prj.entity.Account;
import org.hibernate.criterion.Criterion;

import com.prj.entity.ClazzReservation;
import com.prj.entity.Reservation;
import com.prj.util.DataWrapper;

public interface ClazzReservationDao extends BaseDao<ClazzReservation, Long> {

    DataWrapper addClazzReservation(ClazzReservation classRes, long clazzId, long experimentId,
                                    long slotId, long labId, long reserverId);

    List<Long> getTeachers(long reservationId);

    DataWrapper addTeachers(long id, long[] teachers);

    DataWrapper getClazzesBySemesterAndTeacher(long semesterId, long teacherId);

    DataWrapper getClazzesBySemesterAndLabteacher(long semesterId, long labTeacherId);

    DataWrapper getClazzesBySemesterAndLabId(long semesterId, long labId);

    List<Reservation> getClazzReservationByAccountAndSemester(long accountId, long semesterId);

    List<ClazzReservation> getClazzReservationByTime(Date date, int slot, long labid);

    List<ClazzReservation> getLabTracherClazzReservationsByDateSpan(long semesterId,
                                                                    long labTeacherId,
                                                                    Date startDate, Date endDate);

    List<ClazzReservation> getLabTeacherReservations(long accountId, ArrayList<Criterion> conditions);

    List<Account> getLabTeachersByReservation(long reservationId);

    Integer countClazzReservtionStudent(long clazzId, long expId);
}
