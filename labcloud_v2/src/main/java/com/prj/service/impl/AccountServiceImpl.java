package com.prj.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.prj.dao.AccountDao;
import com.prj.dao.ClazzDao;
import com.prj.entity.Account;
import com.prj.entity.Account.Role;
import com.prj.entity.Clazz;
import com.prj.service.AccountService;
import com.prj.util.DataWrapper;
import com.prj.util.ErrorCode;
import com.prj.util.MD5Tool;

@PropertySource(value = { "/WEB-INF/spring-security.properties" })
@Service
public class AccountServiceImpl extends BaseServiceImpl<Account, Long> implements AccountService {
    @Autowired
    Environment env;

    AccountDao  ad;

    @Autowired
    ClazzDao    clazzDao;

    @Autowired
    public AccountServiceImpl(AccountDao ad) {
        super(Account.class, ad);
        this.ad = ad;
    }

    public DataWrapper create(Account a) {
        assertUniqueNumber(a.getNumber());
        a.setPassword(MD5Tool.getMd5(a.getInitialPassword()));
        return super.createEntity(a);
    }

    public DataWrapper updatePassword(long id, String oldPassword, String newPassword) {
        Account a = (Account) read(id).getData();
        if (MD5Tool.getMd5(oldPassword).equals(a.getPassword())) {
            a.setPassword(MD5Tool.getMd5(newPassword));
            return DataWrapper.voidSuccessRet;
        } else {
            return new DataWrapper(false);
        }
    }

    public DataWrapper updatePasswordByAdmin(long id, String newPassword) {
        Account a = (Account) read(id).getData();
        a.setPassword(MD5Tool.getMd5(newPassword));
        return DataWrapper.voidSuccessRet;
    }

    public DataWrapper updateIcon(long id, MultipartFile file) {
        Account a = (Account) read(id).getData();
        a.setIconPath(fs.save("files/accounts/" + id + "/icon", null, file));
        return new DataWrapper(a.getIconPath());
    }

    public DataWrapper addStudents(long classId, String path, MultipartFile stuList) {
        String passwd = env.getProperty("student.initialPassword");
        Assert.hasText(passwd, "Initial password of student must be set.");
        List<Account> students = fs.readStudents(path, stuList);
        
        Clazz clazz = clazzDao.read(classId);
        if (students == null || students.size() <= 0) {
            DataWrapper ret = new DataWrapper();
            ret.setErrorCode(ErrorCode.INTERNAL_ERROR);
            return ret;
        }
        for (Account student : students) {
            if (isUniqueNumber(student.getNumber())) {
                student.setInitialPassword(passwd);
                Account account = (Account) create(student).getData();
            } else {
                Account account = ad.findBy("number", student.getNumber());
                clazz.getStudents().add(account);
            }
        }
        try {
            clazzDao.merge(clazz);
        } catch (Exception e) {
            return new DataWrapper(ErrorCode.INTERNAL_ERROR);
        }
        DataWrapper dw = new DataWrapper(students);
        return dw;        
    }

    public DataWrapper getAllByUserType(Role role) {
        DataWrapper dw = new DataWrapper();
        dw.setData(ad.getAllByConditions(getRoleConditions(role)));
        return dw;
    }

    public DataWrapper getAccountByPage(int pageNum, int pageSize, Role role) {
        return ad.getPageByConditions(pageNum, pageSize, getRoleConditions(role));
    }

    private List<Criterion> getRoleConditions(Role role) {
        List<Criterion> conditions = new ArrayList<Criterion>();
        if (role == Role.ADMINISTRATOR || role == Role.STUDENT) {
            conditions.add(Restrictions.eq("role", role));
        } else {
            conditions.add(Restrictions.ne("role", Role.ADMINISTRATOR));
            conditions.add(Restrictions.ne("role", Role.STUDENT));
            if (role != Role.ALL_TEACHER) {
                conditions.add(Restrictions.eq("role", role));
            }
        }
        return conditions;
    }

    /**
     * 根据姓名或者账户号模糊查询用户
     */
    public DataWrapper searchByNameAndNumber(Role role, String value) {
        DataWrapper dw = new DataWrapper();
        List<Criterion> list = getRoleConditions(role);
        list.add(Restrictions.or(Restrictions.like("number", "%" + value + "%"),
            Restrictions.like("name", "%" + value + "%")));
        dw.setData(ad.getAllByConditions(list));
        return dw;
    }

    /** 
     * @see com.prj.service.AccountService#removeStudentFromClass(long, long)
     */
    public DataWrapper removeStudentFromClass(long classId, long studentId) {
        Clazz clazz = clazzDao.read(classId);
        Account st = (Account) this.read(studentId).getData();
        if (clazz == null || st == null) {
            return new DataWrapper(ErrorCode.NOT_FOUND);
        }
        clazz.getStudents().remove(st);
        try {
            clazzDao.merge(clazz);
        } catch (Exception e) {
            return new DataWrapper(ErrorCode.INTERNAL_ERROR);
        }
        return DataWrapper.voidSuccessRet;
    }

    /** 
     * @see com.prj.service.AccountService#addStudentToClass(long, com.prj.entity.Account)
     */
    public DataWrapper addStudentToClass(long classId, Account ao) {
        String passwd = env.getProperty("student.initialPassword");
        Assert.hasText(passwd, "Initial password of student must be set.");
        ao.setInitialPassword(passwd);
        ao.setRole(Role.STUDENT);
        Account account;
        if (isUniqueNumber(ao.getNumber())) {
            account = (Account) this.create(ao).getData();
        } else {
            account = ad.findBy("number", ao.getNumber());
        }
        Clazz clazz = clazzDao.read(classId);
        if (account == null || clazz == null) {
            return new DataWrapper(ErrorCode.INTERNAL_ERROR);
        }
        if (clazz.getStudents().contains(account)) {
            return new DataWrapper(ErrorCode.DUPLICATION);
        }
        clazz.getStudents().add(account);
        try {
            clazzDao.merge(clazz);
        } catch (Exception e) {
            return new DataWrapper(ErrorCode.INTERNAL_ERROR);
        }
        return DataWrapper.voidSuccessRet;
    }

    /** 
     * @see com.prj.service.AccountService#clearAllStudentOfClass(long)
     */
    public DataWrapper clearAllStudentOfClass(long classId) {
        Clazz clazz = clazzDao.read(classId);
        if (clazz == null) {
            return new DataWrapper(ErrorCode.INTERNAL_ERROR);
        }
        clazz.getStudents().clear();
        try {
            clazzDao.merge(clazz);
        } catch (Exception e) {
            return new DataWrapper(ErrorCode.INTERNAL_ERROR);
        }
        return DataWrapper.voidSuccessRet;
    }
}
