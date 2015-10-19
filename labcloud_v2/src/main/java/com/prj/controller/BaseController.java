package com.prj.controller;

import com.prj.entity.Account;
import com.prj.entity.Account.Role;
import com.prj.exception.AppException;
import com.prj.security.AccountUserDetails;
import com.prj.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api")
public class BaseController {
  /**
   * for convince of dependency configuration change to full name and move to
   * inherited controller finally
   */
  @Autowired
  AccountService as;
  @Autowired
  LabService ls;
  @Autowired
  ExperimentService es;
  @Autowired
  CourseService cs;
  @Autowired
  ClazzService cls;
  @Autowired
  SemesterService ss;
  @Autowired
  ClazzReservationService clress;
  @Autowired
  StudentReservationService sress;
  @Autowired
  StudentRecordService srecs;
  @Autowired
  FileService fs;
  @Autowired
  MachineService ms;
  @Autowired
  MessageService mes;

  protected void assertOwner(long id) {
    Account a = currentAccount();
    if (id != a.getId()) {
      throw AppException.newInstanceOfForbiddenException(a.getName());
    }
  }

  protected boolean isAdmin() {
    return currentAccount().getRole() == Role.ADMINISTRATOR;
  }

  /**
   * Get current account fetch from token.
   * <p/>
   * Note that password property is already erased.
   *
   * @return current account logged in
   */
  protected Account currentAccount() {
    return ((AccountUserDetails) SecurityContextHolder.getContext()
      .getAuthentication().getPrincipal()).getAccount();
  }

}
