package com.prj.controller;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prj.entity.Account;
import com.prj.entity.Account.Role;
import com.prj.entity.ClazzReservation;
import com.prj.entity.Reservation;
import com.prj.entity.Reservation.Status;
import com.prj.entity.StudentReservation;
import com.prj.exception.AppException;
import com.prj.util.DataWrapper;
import com.prj.util.ErrorCode;
import com.prj.util.PathEnum.ReservationStatus;
import com.prj.util.PathEnum.ReservationType;
import com.prj.util.RequiredRole;

@RestController
public class ReservationController extends BaseController {

    /**
     * GET /classReservation/{id}
     * <p>
     * Get class reservation by id.
     * <p>
     * GET /classReservation/{id}
     * <p>
     * Get student reservation by id.
     *
     * @param resType
     * @param id
     * @return
     */
    @RequestMapping(value = "/reservation/{resType}/{id}", method = RequestMethod.GET)
    @RequiredRole(Role.ADMINISTRATOR)
    DataWrapper find(@PathVariable ReservationType resType, @PathVariable long id) {
        switch (resType) {
            case classReservation:
                return clress.read(id);
            case studentReservation:
                return sress.read(id);
            default:
                throw AppException.newInstanceOfWrongParameterException();
        }
    }

    @RequestMapping(value = "/reservation/{resType}/{id}", method = RequestMethod.PUT)
    @RequiredRole(Role.ADMINISTRATOR)
    DataWrapper update(@PathVariable ReservationType resType, @PathVariable long id,
                       @RequestBody Reservation info) {
        switch (resType) {
            case classReservation:
                return clress.update(id, (ClazzReservation) info);
            case studentReservation:
                return sress.update(id, (StudentReservation) info);
            default:
                throw AppException.newInstanceOfWrongParameterException();
        }
    }

    @RequestMapping(value = "/reservation/{resType}/{id}", method = RequestMethod.DELETE)
    @RequiredRole(Role.ADMINISTRATOR)
    DataWrapper delete(@PathVariable ReservationType resType, @PathVariable long id) {
        switch (resType) {
            case classReservation:
                return clress.deleteClazzReservation(id);
            case studentReservation:
                return sress.deleteStuReservation(id);
            default:
                throw AppException.newInstanceOfWrongParameterException();
        }
    }

    /**
     * 取消一个预约
     */
    @RequestMapping(value = "/reservation/{resType}/{id}/cancel", method = RequestMethod.DELETE)
    @RequiredRole({ Role.ADMINISTRATOR, Role.ALL_TEACHER, Role.NOR_TEACHER })
    DataWrapper cancelReservation(@PathVariable ReservationType resType, @PathVariable long id) {
        switch (resType) {
            case classReservation:
                //支持删除
                return clress.cancelClazzReservation(id);
            case studentReservation:
                //支持删除
                return sress.cancelStudentReservation(id);
            default:
                throw AppException.newInstanceOfWrongParameterException();
        }
    }

    /**
     * POST /studentReservations?lab={labId}&date={date}&slot={slotId}
     * <p>
     * BODY StudentReservation stuResProfile
     *
     * @param lab
     * @param date
     * @param slotId
     * @return
     */
    @RequestMapping(value = "/reservation/studentReservations", method = RequestMethod.POST)
    @RequiredRole(Role.ADMINISTRATOR)
    DataWrapper addStudentReservation(@RequestParam("experiment") long experimentId,
                                      @RequestParam("lab") long lab,
                                      @RequestParam("slot") int slotId,
                                      @RequestParam("semester") long semester,
                                      @RequestBody StudentReservation stuResProfile) {
        return sress.addStuReservation(stuResProfile, semester, experimentId, lab, slotId,
            currentAccount().getId());
    }

    /**
     * POST /classReservations?lab={labId}&date={date}&slot={slotId}
     * <p>
     * BODY ClazzReservation classResProfile
     *
     * @param lab
     * @param date
     * @param slotId
     * @return
     */
    @RequestMapping(value = "/reservation/classReservation", method = RequestMethod.POST)
    @RequiredRole({ Role.ALL_TEACHER, Role.NOR_TEACHER, Role.ADMINISTRATOR })
    DataWrapper addClazzReservation(@RequestBody ClazzReservation classResProfile,
                                    @RequestParam("clazz") long clazz,
                                    @RequestParam("experiment") long experiment,
                                    @RequestParam("lab") long lab, @RequestParam("slot") int slot) {
        return clress.addClazzReservation(classResProfile, clazz, experiment, slot, lab,
            currentAccount().getId());
    }

    /**
     * GET /classReservations?status={status}&week={weekIndex} 获取某一周的所有
     * reservation
     *
     * @param stuId
     * @param teaId
     * @param weekIndex
     * @param status
     * @return
     */
    @RequestMapping(value = "/reservation/classReservations", method = RequestMethod.GET)
    @RequiredRole({ Role.STUDENT, Role.ALL_TEACHER, Role.NOR_TEACHER })
    DataWrapper listClazzReservation(@RequestParam(value = "week", required = false) Integer weekIndex,
                                     @RequestParam ReservationStatus status) {
        Account a = currentAccount();
        if (a.getRole() == Role.STUDENT)
            return clress.listClazzReservationBySWS(a.getId(), weekIndex, status);
        else
            return clress.listClazzReservationByTWS(a.getId(), weekIndex, status);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @RequestMapping(value = "/reservation/semester/{semester}/list/all", params = { "startDate",
            "endDate" }, method = RequestMethod.GET)
    @RequiredRole({ Role.ADMINISTRATOR })
    DataWrapper listAllAvailableReservation(@PathVariable("semester") long semester,
                                            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        ArrayList list = new ArrayList();
        list.addAll(clress.getReservationByDateSpan(semester, startDate, endDate));
        list.addAll(sress.getReservationByDateSpan(semester, startDate, endDate));
        return new DataWrapper(list);
    }

    /**
     * POST /studentReservations/student/{id} student grab reservation
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/reservation/studentReservation/{id}/student/apply", method = RequestMethod.POST)
    @RequiredRole(Role.STUDENT)
    DataWrapper addStudentReservationApplication(@PathVariable long id) {
        Account account = currentAccount();
        return sress.addStudentReservationApplication(id, account);
    }

    /**
     * DELETE /studentReservations/student/{id}
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/reservation/studentReservation/{id}/student/apply", method = RequestMethod.DELETE)
    @RequiredRole(Role.STUDENT)
    DataWrapper cancelStudentReservationApplication(@PathVariable long id) {
        Account account = currentAccount();
        return sress.cancelStudentReservationApplication(id, account);
    }

    /**
     * GET Student list of student reservations
     */
    @RequestMapping(value = "/reservation/{id}/students/all", method = RequestMethod.GET)
    @RequiredRole(Role.ADMINISTRATOR)
    DataWrapper listReservationStudentsByPage(@PathVariable long id) {
        return sress.listAllReservationStudents(id);
    }

    /**
     * POST /reservation/{id}/verify
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/reservation/{id}/verify", method = RequestMethod.POST)
    @RequiredRole(Role.ADMINISTRATOR)
    DataWrapper verify(@PathVariable long id, @RequestParam("status") Status status,
                       @RequestParam long[] teacherIds) {
        if (clress.update(id, "status", status.toString()).getErrorCode() == ErrorCode.NO_ERROR) {
            if (status == Status.REJECTED
                || clress.handle(id, teacherIds).getErrorCode() == ErrorCode.NO_ERROR) {
                return DataWrapper.voidSuccessRet;
            } else {
                return new DataWrapper(ErrorCode.INTERNAL_ERROR, null);
            }
        } else {
            return new DataWrapper(ErrorCode.INTERNAL_ERROR, null);
        }
    }

    /**
     * GET /reservation/semester/{semesterId}/lab/{labId}
     *
     * @param lab
     * @param date
     * @param slot
     * @return student or class reservation in specified slot
     */
    @RequestMapping(value = "/reservation/{resType}/semester/{semesterId}/lab/{labId}", method = RequestMethod.GET)
    @RequiredRole(Role.ADMINISTRATOR)
    DataWrapper find(@PathVariable ReservationType resType, @PathVariable long semesterId,
                     @PathVariable long labId) {
        switch (resType) {
            case classReservation:
                return clress.getReservationsBySemAndLab(semesterId, labId);
            case studentReservation:
                return sress.getReservationsBySemAndLab(semesterId, labId);
            default:
                throw AppException.newInstanceOfWrongParameterException();
        }

    }

    /**
     * GET 获取特定状态的预约
     *
     * @param status
     * @param semesterId
     * @return
     */
    @RequestMapping(value = "/reservation/semester/{semesterId}/reservations", method = RequestMethod.GET)
    @RequiredRole(Role.ADMINISTRATOR)
    DataWrapper getReservationByStatus(@RequestParam("status") Status status,
                                       @PathVariable long semesterId) {
        return clress.getAllReservationBySemester(semesterId, status);
    }

    /**
     * GET 分页获取特定状态的预约
     *
     * @param status
     * @param semesterId
     * @return
     */
    @RequestMapping(value = "/reservation/semester/{semesterId}/reservations/page/{pageSize}/{pageNumber}", method = RequestMethod.GET)
    @RequiredRole(Role.ADMINISTRATOR)
    DataWrapper getReservationByStatusPage(@RequestParam("status") Status status,
                                           @PathVariable long semesterId,
                                           @PathVariable int pageSize, @PathVariable int pageNumber) {
        return clress.getAllReservationByStatusPage(semesterId, status, pageSize, pageNumber);
    }

    /**
     * GET 获取教师的预约列表
     *
     * @param status
     * @param semester
     * @param pageSize
     * @param pageNumber
     * @return
     */
    @RequestMapping(value = "/reservation/teacher/{accountId}/page/{pageSize}/{pageNumber}", method = RequestMethod.GET)
    @RequiredRole({ Role.ALL_TEACHER, Role.NOR_TEACHER, Role.ADMINISTRATOR })
    DataWrapper getTeacherReservationByPage(@RequestParam("status") Status status,
                                            @RequestParam("semester") long semester,
                                            @PathVariable long accountId,
                                            @PathVariable int pageSize, @PathVariable int pageNumber) {
        return clress
            .getTeacherReservationByPage(semester, status, accountId, pageSize, pageNumber);
    }

    /**
     * GET 获取学生的预约列表
     *
     * @param status
     * @param semester
     * @param pageSize
     * @param pageNumber
     * @return
     */
    @RequestMapping(value = "/reservation/student/{accountId}/page/{pageSize}/{pageNumber}", method = RequestMethod.GET)
    @RequiredRole({ Role.STUDENT, Role.ADMINISTRATOR })
    DataWrapper getStudentReservationByPage(@RequestParam("semester") long semester,
                                            @PathVariable long accountId,
                                            @RequestParam("type") String type,
                                            @PathVariable int pageSize, @PathVariable int pageNumber) {
        if (type.equals("clazz")) {
            return clress.getStudentReservationByPage(semester, accountId, pageSize, pageNumber);
        } else if (type.equals("student")) {
            return sress.getStudentReservationByAccountPage(semester, accountId, pageSize,
                pageNumber);
        } else {
            throw AppException.newInstanceOfWrongParameterException();
        }
    }

    /**
     * GET 获取学可以自行预约的列表
     *
     * @param status
     * @param semester
     * @param pageSize
     * @param pageNumber
     * @return
     */
    @RequestMapping(value = "/reservation/studentAvailable/{accountId}/page/{pageSize}/{pageNumber}", method = RequestMethod.GET)
    @RequiredRole({ Role.STUDENT, Role.ADMINISTRATOR })
    DataWrapper getStudentAvailableReservationByPage(@RequestParam("semester") long semester,
                                                     @PathVariable long accountId,
                                                     @PathVariable int pageSize,
                                                     @PathVariable int pageNumber) {
        return sress.getAvailableStudentReservation(semester, accountId, pageSize, pageNumber);
    }

    /**
     * GET 获取实验教师的预约列表
     *
     * @param status
     * @param semester
     * @param pageSize
     * @param pageNumber
     * @return
     */
    @RequestMapping(value = "/reservation/labTeacher/{accountId}/page/{pageSize}/{pageNumber}", method = RequestMethod.GET)
    @RequiredRole({ Role.ALL_TEACHER, Role.NOR_TEACHER, Role.ADMINISTRATOR })
    DataWrapper getLabTeacherReservationByPage(@RequestParam("status") Status status,
                                               @RequestParam("semester") long semester,
                                               @PathVariable long accountId,
                                               @PathVariable int pageSize,
                                               @PathVariable int pageNumber) {
        return clress.getLabTeacherReservationByPage(semester, status, accountId, pageSize,
            pageNumber);
    }

    /**
     * GET /reservation/labTeacher/{teacherId}/semester/{semesterId}/list
     */
    @RequestMapping(value = "/reservation/labTeacher/{teacherId}/semester/{semesterId}/list", method = RequestMethod.GET)
    @RequiredRole({ Role.ALL_TEACHER, Role.LAB_TEACHER })
    DataWrapper getAllLabTeacherReservations(@PathVariable("teacherId") long teacherId,
                                             @PathVariable("semesterId") long semesterId) {
        return clress.getLabTeacherReservations(teacherId, semesterId);
    }

    /**
     * GET 获取教师本学期所有通过的预约
     */
    @RequestMapping(value = "/reservation/semester/{semesterId}/teacher/{teacherId}/list", method = RequestMethod.GET)
    @RequiredRole({ Role.ALL_TEACHER, Role.NOR_TEACHER })
    DataWrapper getTeacherReservations(@PathVariable("semesterId") long semesterId,
                                       @PathVariable("teacherId") long teacherId) {
        return clress.getClazzReservationByAccountAndSemester(teacherId, semesterId);
    }

    /**
     * GET 根据获取日期获取预约
     *
     * @param date
     * @param slot
     * @return
     */
    @RequestMapping(value = "/reservation/time/{date}/{slot}", method = RequestMethod.GET)
    DataWrapper getReservertionByTime(@PathVariable java.sql.Date date, @PathVariable int slot,
                                      @RequestParam("labId") long labid) {
        System.out.println(date);
        DataWrapper ret = new DataWrapper();
        ret = clress.getClassReservationByTime(currentAccount().getId(), date, slot, labid);
        if (ret.getErrorCode() != ErrorCode.NOT_FOUND) {
            return ret;
        } else {
            ret = sress.getStudentReservationByTime(currentAccount().getId(), date, slot, labid);
            if (ret.getErrorCode() != ErrorCode.NOT_FOUND) {
                return ret;
            }
        }
        ret.setErrorCode(ErrorCode.NOT_FOUND);
        return ret;
    }

    /**
     * GET 根据预约获取实验老师
     *
     * @param reservationId
     * @return
     */
    @RequestMapping(value = "/reservation/{reservationId}/labteachers", method = RequestMethod.GET)
    DataWrapper getLabTeachersByReservation(@PathVariable long reservationId) {
        return clress.getLabTeachersByReservation(reservationId);
    }

}
