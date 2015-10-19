package com.prj.controller;

import com.prj.entity.Account.Role;
import com.prj.entity.FileRecord;
import com.prj.util.DataWrapper;
import com.prj.util.ErrorCode;
import com.prj.util.RequiredRole;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController extends BaseController {

  @RequestMapping(value = "/file/experiment", method = RequestMethod.POST)
  @RequiredRole(Role.STUDENT)
  DataWrapper add(@RequestParam MultipartFile file,
                  @RequestParam FileRecord.Type fileType,
                  @RequestParam(value = "classReservation", required = false) Long clResId,
                  @RequestParam(value = "studentReservation", required = false) Long stuResId,
//                  @RequestParam(value = "class", required = false) Long clazzId,
//                  @RequestParam(value = "experiment", required = false) Long expId,
                  @RequestParam(value = "mac") String mac) {
    DataWrapper ret = new DataWrapper();
    if (clResId != null) {
      fs.saveBySCM(currentAccount().getId(), clResId, mac, file, fileType);
      return DataWrapper.voidSuccessRet;
    } else if (stuResId != null) {
      fs.saveBySSM(currentAccount().getId(), stuResId, mac, file, fileType);
      return DataWrapper.voidSuccessRet;
    }
    ret.setErrorCode(ErrorCode.WRONG_PARAM);
    return ret;
  }

  @RequestMapping(value = "/file/experiment/homework", method = RequestMethod.POST)
  @RequiredRole(Role.STUDENT)
  DataWrapper add(@RequestParam MultipartFile file, @RequestParam(value="clazzId", required = false) Long clazzId,
                  @RequestParam(value = "expId") Long expId,
                  @RequestParam(value = "stuResId") Long stuResId){
    DataWrapper ret = new DataWrapper();
    if (stuResId!=null){
      System.out.println(stuResId);
      fs.saveHomework(currentAccount().getId(), clazzId, expId, stuResId, file);
    }
    return ret;
  }

  @RequestMapping(value = "/report/generate/studentRecord/{srId}", method = RequestMethod.POST)
  @ResponseBody
  public DataWrapper generateFile(
    @PathVariable Integer srId) {
    return fs.genReportTemplate(srId);
  }
}
