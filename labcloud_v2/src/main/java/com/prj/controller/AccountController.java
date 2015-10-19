package com.prj.controller;

import com.prj.entity.Account;
import com.prj.entity.Account.Role;
import com.prj.util.DataWrapper;
import com.prj.util.RequiredRole;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;

@RestController
public class AccountController extends BaseController {

    // ~ Private Account Management
    // ===================================================================================

    /**
     * PUT /account/{id}
     * <p>
     * BODY Account
     *
     * @param id
     * @param data
     * @return
     */
    @RequestMapping(value = "/account/{id}", method = RequestMethod.PUT)
    DataWrapper updateProfile(@PathVariable long id, @RequestBody Account data) {
        if (!isAdmin())
            assertOwner(id);
        return as.update(id, data);
    }

    /**
     * PUT /account/{id}/password
     * <p>
     * BODY {oldPassword, newPassword}
     * <p>
     * Reset password by oneself or administrator
     *
     * @param id
     * @param data
     * @return
     */
    @RequestMapping(value = "/account/{id}/password", method = RequestMethod.PUT)
    DataWrapper updatePassword(@PathVariable long id, @RequestBody Map<String, String> data) {
        if (!isAdmin())
            assertOwner(id);
        return as.updatePassword(id, data.get("oldPassword"), data.get("newPassword"));
    }

    /**
     * PUT /account/{id}/passwordByAdmin
     * <p>
     * BODY { newPassword }
     *
     * @param id
     * @param data
     * @return
     */
    @RequestMapping(value = "/account/{id}/passwordByAdmin", method = RequestMethod.PUT)
    @RequiredRole(Role.ADMINISTRATOR)
    DataWrapper updatePasswordByAdmin(@PathVariable long id, @RequestBody Map<String, String> data) {
        return as.updatePasswordByAdmin(id, data.get("newPassword"));
    }

    /**
     * POST /accounts/{id}/icon
     * <p>
     * BODY [FormData] file
     * <p>
     * Should be <b>PUT</b> But
     * <p>
     * "Commons FileUpload is hard coded to only accept POST requests for files."
     * <p>
     * http://stackoverflow.com/questions/15058548/spring-security-multipart-
     * requests
     *
     * @param id
     * @param file
     * @return
     */
    @RequestMapping(value = "/account/{id}/icon", method = RequestMethod.POST)
    DataWrapper updateIcon(@PathVariable long id, @RequestParam MultipartFile file) {
        assertOwner(id);
        return as.updateIcon(id, file);
    }

    // ~ Administrator Account Management
    // ===================================================================================

    /**
     * POST /account
     * <p>
     * BODY Account
     *
     * @param data
     * @return
     */
    @RequestMapping(value = "/account", method = RequestMethod.POST)
    @RequiredRole(Role.ADMINISTRATOR)
    DataWrapper add(@RequestBody Account data) {
        return as.create(data);
    }

    /**
     * POST /accounts/listFile?class={classId}
     * <p>
     * BODY [FormData] file 
     * 上传学生名单
     *
     * @param classId
     * @param file
     * @return
     */
    @RequestMapping(value = "/account/list", method = RequestMethod.POST)
    @RequiredRole({ Role.ADMINISTRATOR, Role.ALL_TEACHER, Role.NOR_TEACHER })
    DataWrapper addStudents(@RequestParam("class") long classId, @RequestParam MultipartFile file,
                            HttpServletRequest request) {
        String path = request.getSession().getServletContext().getRealPath("/files/student");
        return as.addStudents(classId, path, file);
    }

    /**
     * DELETE 从一个班级移除学生
     */
    @RequestMapping(value = "/account/class/{classId}/st/{studentId}", method = RequestMethod.DELETE)
    @RequiredRole({ Role.ADMINISTRATOR, Role.ALL_TEACHER, Role.NOR_TEACHER })
    DataWrapper removeStudent(@PathVariable("classId") long classId,
                              @PathVariable("studentId") long studentId) {
        return as.removeStudentFromClass(classId, studentId);
    }

    /**
     * POST 为一个班级添加一个学生
     */
    @RequestMapping(value = "/account/class/{classId}/st", method = RequestMethod.POST)
    @RequiredRole({ Role.ADMINISTRATOR, Role.ALL_TEACHER, Role.NOR_TEACHER })
    DataWrapper addStudent(@PathVariable("classId") long classId, @RequestBody Account data) {
        return as.addStudentToClass(classId, data);
    }

    /**
     * DELETE 一键clear所有学生
     */
    @RequestMapping(value = "/account/class/{classId}/st/clear", method = RequestMethod.DELETE)
    @RequiredRole({ Role.ADMINISTRATOR, Role.ALL_TEACHER, Role.NOR_TEACHER })
    DataWrapper removeAllStudent(@PathVariable("classId") long classId) {
        return as.clearAllStudentOfClass(classId);
    }

    /**
     * DELETE /account/{id}
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/account/{id}", method = RequestMethod.DELETE)
    @RequiredRole(Role.ADMINISTRATOR)
    DataWrapper delete(@PathVariable long id) {
        return as.delete(id);
    }

    /**
     * GET /accounts/{id}
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/account/{id}", method = RequestMethod.GET)
    DataWrapper get(@PathVariable long id) {
        if (!isAdmin())
            assertOwner(id);
        return as.read(id);
    }

    /**
     * GET /accounts?number={number}
     *
     * @param number
     * @return
     */
    @RequestMapping(value = "/account", method = RequestMethod.GET)
    @RequiredRole(Role.ADMINISTRATOR)
    DataWrapper search(@RequestParam String number) {
        return as.findByNumber(number);
    }

    /**
     * GET /account/list/all
     *
     * @param number
     * @return
     */
    @RequestMapping(value = "/account/list/all", params = { "userType" }, method = RequestMethod.GET)
    @RequiredRole(Role.ADMINISTRATOR)
    DataWrapper getAllAccount(@RequestParam(value = "userType") String userType) {
        Role role = Role.valueOf(userType);
        return as.getAllByUserType(role);
    }

    /**
     * GET /account/list/page/{pageSize}/{pageNumber}
     *
     * @param number
     * @return
     */
    @RequestMapping(value = "/account/list/page/{pageSize}/{pageNumber}", params = { "userType" }, method = RequestMethod.GET)
    @RequiredRole(Role.ADMINISTRATOR)
    DataWrapper getPage(@PathVariable int pageSize, @PathVariable int pageNumber,
                        @RequestParam(value = "userType") String userType) {
        Role role = Role.valueOf(userType);
        return as.getAccountByPage(pageNumber, pageSize, role);
    }

    /**
     * GET /account/search/name?userType&value 閫氳繃宸ュ彿鎴栬�呭悕瀛楁悳绱㈢敤鎴�
     */
    @RequestMapping(value = "/account/searchByNameAndNumber", params = { "userType", "value" }, method = RequestMethod.GET)
    @RequiredRole({ Role.ADMINISTRATOR, Role.ALL_TEACHER, Role.NOR_TEACHER })
    DataWrapper searchByNameAndNumber(@RequestParam(value = "userType") String userType,
                                      @RequestParam(value = "value") String value) {
        Role role = Role.valueOf(userType);
        return as.searchByNameAndNumber(role, value);
    }
}
