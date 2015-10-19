package com.prj.service;

import org.springframework.web.multipart.MultipartFile;

import com.prj.entity.Account;
import com.prj.entity.Account.Role;
import com.prj.util.DataWrapper;

public interface AccountService extends BaseService<Account, Long> {

    DataWrapper updatePassword(long id, String oldPassword, String newPassword);

    DataWrapper updatePasswordByAdmin(long id, String newPassword);

    DataWrapper updateIcon(long id, MultipartFile file);

    DataWrapper addStudents(long classId, String path, MultipartFile stuList);

    DataWrapper getAccountByPage(int pageNum, int pageSize, Role role);

    DataWrapper getAllByUserType(Role role);

    DataWrapper searchByNameAndNumber(Role role, String value);

    DataWrapper removeStudentFromClass(long classId, long studentId);

    DataWrapper addStudentToClass(long classId, Account ao);

    DataWrapper clearAllStudentOfClass(long classId);
}
