package com.prj.service;

import java.util.Date;
import java.util.List;

import com.prj.entity.ClazzReservation;
import com.prj.entity.Reservation.Status;
import com.prj.util.DataWrapper;
import com.prj.util.PathEnum.ReservationStatus;

public interface ClazzReservationService extends BaseService<ClazzReservation, Long> {

    DataWrapper handle(long id, long[] labTeacherIds);

    DataWrapper addClazzReservation(ClazzReservation classRes, long clazzId, long experimentId,
                                    long slotId, long labId, long reserverId);

    DataWrapper deleteClazzReservation(long id);

    DataWrapper cancelClazzReservation(long id);

    DataWrapper listClazzReservationBySWS(long id, Integer weekIndex, ReservationStatus status);

    DataWrapper listClazzReservationByTWS(long id, Integer weekIndex, ReservationStatus status);

    DataWrapper getReservationsByCondition(Long semesterId, long teacherId);

    DataWrapper getApprovedReservationsByCondition(Long semesterId, long labTeacherId);

    DataWrapper getReservationsBySemAndLab(Long semesterId, long labId);

    DataWrapper getAllReservationBySemester(long semesterId, Status status);

    DataWrapper getAllReservationByStatusPage(long semesterId, Status status, int pageSize,
                                              int pageNumber);

    DataWrapper getTeacherReservationByPage(long semesterId, Status status, long account,
                                            int pageSize, int pageNumber);

    DataWrapper getStudentReservationByPage(long semesterId, long account, int pageSize,
                                            int pageNumber);

    List<ClazzReservation> getReservationByDateSpan(long semesterId, Date startDate, Date endDate);

    DataWrapper getClassReservationByTime(long stuId, java.sql.Date date, int slot, long labid);

    List<ClazzReservation> getLabTeacherReservationByDateSpan(long semesterId, long labTeacherId,
                                                              Date startDate, Date endDate);

    DataWrapper getLabTeacherReservations(long accountId, long semesterId);

    DataWrapper getLabTeacherReservationByPage(long semesterId, Status status, long account,
                                               int pageSize, int pageNumber);

    DataWrapper getClazzReservationByAccountAndSemester(long accountId, long semesterId);

    DataWrapper getLabTeachersByReservation(long reservationId);
}
