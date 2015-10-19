package com.prj.service;

import com.prj.entity.Account;
import com.prj.entity.FileRecord;
import com.prj.util.DataWrapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

	String save(String path, String fileName, MultipartFile file);

	List<Account> readStudents(String path, MultipartFile stuList);

  void saveBySCM(long stuId, long clResId, String mac, MultipartFile file, FileRecord.Type fileType);

  void saveBySSM(long stuId, long stuResId, String mac, MultipartFile file, FileRecord.Type fileType);

  DataWrapper genReportTemplate(long srId);

  void saveHomework(long stuId, long clazzId, long expId, long stuRecId, MultipartFile file);
}
