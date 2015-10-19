package com.prj.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.prj.dao.StudentRecordDao;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.prj.entity.Account;
import com.prj.entity.Clazz;
import com.prj.entity.ClazzReservation;
import com.prj.entity.FileRecord;
import com.prj.entity.Machine;
import com.prj.entity.StudentRecord;
import com.prj.entity.StudentReservation;
import com.prj.exception.AppException;
import com.prj.service.ClazzReservationService;
import com.prj.service.ClazzService;
import com.prj.service.FileRecordService;
import com.prj.service.FileService;
import com.prj.service.MachineService;
import com.prj.service.StudentRecordService;
import com.prj.service.StudentReservationService;
import com.prj.util.DataWrapper;
import com.prj.util.DateUtils;
import com.prj.util.DocumentGenerator;
import com.prj.util.ErrorCode;
import com.prj.util.RootPath;

@Service
public class FileServiceImpl implements FileService {
  @Autowired
  ClazzReservationService clress;
  @Autowired
  StudentReservationService sress;
  @Autowired
  ClazzService cs;
  @Autowired
  FileRecordService frs;
  @Autowired
  StudentRecordService srecs;
  @Autowired
  MachineService ms;
  @Autowired
  StudentRecordDao srd;

//	@Override
//	public String save(MultipartFile file) {
//		// TODO Auto-generated method stub
//		return null;
//	}

  public List<Account> readStudents(String path, MultipartFile stuList) {
    int i = stuList.getOriginalFilename().lastIndexOf(".");
    String fileName = null;
    if (i != -1) {
      fileName = stuList.getOriginalFilename();
    }
    this.save("files/student/", fileName, stuList);
    DataWrapper get_stu_ret = getStudentsFromFile(path + "/" + fileName);
    if (get_stu_ret.getErrorCode().equals(ErrorCode.NOT_FOUND)) {
      return null;
    }
    return (List<Account>) get_stu_ret.getData();
  }

  @Transactional
  synchronized public void saveHomework(long stuId, long clazzId, long expId, long stuResId, MultipartFile file){
    Clazz clazz = (Clazz)cs.read(clazzId).getData();
    String relativePath = "files/student/" + stuId + '/'
      + clazz.getSemester().getId() + '/' + clazzId
      + '/' + expId + '/'
      + FileRecord.Type.REPORT;

    String path = this.save(relativePath, null, file);

    StudentRecord srec = srd.read(stuResId);
    srec.setStatus(StudentRecord.Status.SUBMITTED);
    srd.update(srec);


    FileRecord fr = new FileRecord();
    fr.setPath(path);
    fr.setType(FileRecord.Type.REPORT);
    // TODO batch creation of student record when class reservation approved

    fr.setStudentRecord(srec);
    frs.create(fr);

  }

  @Transactional
  synchronized public void saveBySCM(long stuId, long clResId, String mac, MultipartFile file, FileRecord.Type fileType) {
    ClazzReservation clr = (ClazzReservation) clress.read(clResId).getData();
    Assert.notNull(clr);
    long clazzId = clr.getClazz().getId();
    String relativePath = "files/student/" + stuId + '/'
      + clr.getClazz().getSemester().getId() + '/' + clazzId
      + '/' + clr.getExperiment().getId() + '/'
      + fileType;

    String path = this.save(relativePath, null, file);
//    FileRecord fr = (FileRecord) frs.findByPath(path).getData();
//    if (fr == null) {
    StudentRecord srec = (StudentRecord) srecs.findByCES(clazzId, clr.getExperiment().getId(), stuId).getData();
    if (srec == null) {
      srec = new StudentRecord();
      srec.setExperiment(clr.getExperiment());
      srec.setClazz(clr.getClazz());
      Account s = new Account();
      s.setId(stuId);
      srec.setStudent(s);
      srec.setStatus(StudentRecord.Status.IN_PROGRESS);
      srec.setDate(new Date(new java.util.Date().getTime()));
      srec.setSlot(clr.getSlot());
      srec.setLab(clr.getLab());
      Machine m = (Machine) ms.getByMac(mac).getData();
      Assert.notNull(m);
      srec.setMachine(m);
      srec = (StudentRecord) srecs.create(srec).getData();
    } else {
      if(fileType != FileRecord.Type.EXPDATA&&fileType != FileRecord.Type.PHOTO)
        frs.removeBySRT(srec.getId(), fileType);
    }
      FileRecord fr = new FileRecord();
      fr.setPath(path);
      fr.setType(fileType);
      // TODO batch creation of student record when class reservation approved

      fr.setStudentRecord(srec);
      frs.create(fr);
//    }
  }

  @Transactional
  synchronized public void saveBySSM(long stuId, long stuResId, String mac, MultipartFile file, FileRecord.Type fileType) {
    StudentReservation sr = (StudentReservation) sress.read(stuResId).getData();
    Assert.notNull(sr);

    List<Clazz> cll = (List<Clazz>) cs.getClazzBySE(stuId, sr.getExperiment().getId()).getData();

    for (Clazz cl : cll) {
      long clazzId = cl.getId();
      Assert.notNull(cl);

      String relativePath = "files/student/" + stuId + '/'
        + cl.getSemester().getId() + '/' + clazzId
        + '/' + sr.getExperiment().getId() + '/'
        + fileType;

      String path = this.save(relativePath, null, file);

      StudentRecord srec = (StudentRecord) srecs.findByCES(clazzId, sr.getExperiment().getId(), stuId).getData();
      if (srec == null) {
        srec = new StudentRecord();
//      Experiment e = new Experiment();
//      e.setId(expId);
        srec.setExperiment(sr.getExperiment());
        Clazz c = new Clazz();
        c.setId(clazzId);
        srec.setClazz(c);
        Account s = new Account();
        s.setId(stuId);
        srec.setStudent(s);
        srec.setStatus(StudentRecord.Status.IN_PROGRESS);
        srec.setDate(new Date(new java.util.Date().getTime()));
        srec.setSlot(sr.getSlot());
        srec.setLab(sr.getLab());
        Machine m = (Machine) ms.getByMac(mac).getData();
        Assert.notNull(m);
        srec.setMachine(m);
        srec = (StudentRecord) srecs.create(srec).getData();
      } else {
        if (fileType != FileRecord.Type.EXPDATA&&fileType != FileRecord.Type.PHOTO)
          frs.removeBySRT(srec.getId(), fileType);
      }
//    FileRecord fr = (FileRecord) frs.findByPath(path).getData();
//    if (fr == null) {
      FileRecord fr = new FileRecord();
      fr.setPath(path);
      fr.setType(fileType);

      fr.setStudentRecord(srec);
      frs.create(fr);
//    }
    }
  }

  public String save(String relativePath, String fileName, MultipartFile file) {
    if (file == null) {
      throw AppException.newInstanceOfInternalException("Null File.");
    }
    try {
      String path = RootPath.value + "/" + relativePath;
      if (!new File(path).isDirectory()) {
        new File(path).mkdirs();
      }
      if (fileName == null)
        fileName = file.getOriginalFilename();
      fileName = URLDecoder.decode(file.getOriginalFilename(),
        "UTF-8");
      FileOutputStream fos = new FileOutputStream(path + "/" + fileName);
      System.out.println("Create File: " + path + "/" + fileName);
      InputStream is = file.getInputStream();
      byte[] buffer = new byte[1024 * 1024];
      int byteread = 0;
      while ((byteread = is.read(buffer)) != -1) {
        fos.write(buffer, 0, byteread);
        fos.flush();
      }
      fos.close();
      is.close();
      return relativePath + "/" + fileName;

    } catch (Exception e) {
      e.printStackTrace();
      throw AppException.newInstanceOfInternalException("File creation error.");
    }
  }
  
  /**
   * 从文件中获取学生
   * 
   * @param path
   * @return
   */
  private DataWrapper getStudentsFromFile(String path) {
    DataWrapper ret = new DataWrapper();
    try {
      InputStream inp;
      inp = new FileInputStream(path);

      Workbook wb = WorkbookFactory.create(inp);
      Sheet sheet = wb.getSheetAt(0);
      int i = -1;
      List<Account> l = new ArrayList<Account>();

      Row row;
      Cell cell;
      do {
        row = sheet.getRow(++i);
        cell = row.getCell(0);
      } while (!isNo(cell.toString()));

      while (cell != null && !cell.toString().equals("")) {
        Account student = new Account();
        cell = row.getCell(0);
        if (cell != null) {
          student.setNumber(getIntStr(row.getCell(0)));
        }
        cell = row.getCell(1);
        if (cell != null)
          student.setName(row.getCell(1).toString());
        cell = row.getCell(2);
        if (cell != null) {
          // 性别
          if (getIntStr(row.getCell(2)).equals("男"))
            student.setGender(Account.Gender.MALE);
          else
            student.setGender(Account.Gender.FEMALE);
        }
        cell = row.getCell(3);
        if (cell != null)
          student.setMajor(row.getCell(3).toString());
        cell = row.getCell(4);
        if (cell != null)
          student.setEmail(row.getCell(4).toString());
        student.setRole(Account.Role.STUDENT);
        l.add(student);
        row = sheet.getRow(++i);
        if (row == null) {
          break;
        }
        cell = row.getCell(0);
      }
      ret.setData(l);
      return ret;

    } catch (Exception e) {
      e.printStackTrace();
      ret.setErrorCode(ErrorCode.NOT_FOUND);
      return ret;
    }
  }

  private boolean isNo(String s) {
    //1. 去除掉无关符号
    s = s.replace("*", "");
    //2. 返回结果
    return s.matches("\\*{0,1}[0-9]{0,}\\.[0-9]{0,}$");
  }

  private String getIntStr(Cell cell) {
    String tmp = cell.toString();
    if (tmp.startsWith("*")) {
      tmp = tmp.substring(1);
    }
    if (tmp.endsWith(".0"))
      tmp = tmp.substring(0, tmp.length() - 2);
    return tmp;
  }


  public DataWrapper genReportTemplate(long stuRecId) {
    String rootPath = RootPath.value;
    DataWrapper ret = new DataWrapper();
    StudentRecord sr = (StudentRecord) srecs.read(stuRecId).getData();
    Assert.notNull(sr);
    Set<FileRecord> frs = sr.getFileRecords();
    String[] paths = new String[2];
    java.util.Date max0 = null, max1 = null;
    for (FileRecord fr : frs) {
      if (fr.getType() == FileRecord.Type.EXPDATA) {
        String[] strs = fr.getPath().split("[_.]");
        if (strs[strs.length-1].equals("TXT")) {
          java.util.Date temp = DateUtils.getDateTimeFromString(strs[strs.length-2]);
          if (max0 == null || max0.before(temp)) {
            max0 = temp;
            // table
            paths[0] = rootPath + "/" + fr.getPath();
          }
        } else if (strs[strs.length-3].endsWith("-1") && strs[strs.length-1].equals("txt")) {
          java.util.Date temp = DateUtils.getDateTimeFromString(strs[strs.length-2]);
          if (max1 == null || max1.before(temp)) {
            max1 = temp;
            // graph
            paths[1] = rootPath + "/" + fr.getPath();
          }
        } else {
          System.out.println("File Unexpected: " + fr.getPath());
        }
      }
    }
    // if (paths[0] == null || paths[1] == null) {
    // return ret.setErrorCode(ErrorCodeEnum.EXPDATA_FILE_REQUIRED);
    // }
    String relativePath = "files/student/" + sr.getStudent().getId()
      + '/' + sr.getClazz().getSemester().getId() + '/'
      + sr.getClazz().getId() + '/' + sr.getExperiment().getId()
      + '/' + FileRecord.Type.REPORT;
    DocumentGenerator.generate(rootPath, relativePath, "report_template",
      "report", paths);
    ret.setData(relativePath + "/report_template.doc");
    return ret;
  }
}
