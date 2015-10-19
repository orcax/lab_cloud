package com.prj.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.prj.entity.FileRecord.Type;
import com.prj.entity.Student;
import com.prj.util.AccountCharacter;
import com.prj.util.DataWrapper;

public interface FileUploadService {
	DataWrapper<String> saveFile(String path, MultipartFile file, String fileName);
	
	DataWrapper<String> saveFileById(String path, MultipartFile file, int id);

	DataWrapper<String> saveIcon(MultipartFile file,
			Integer accountId, AccountCharacter ac, String rootPath);

	DataWrapper<String> saveStudentListFile(String path, MultipartFile file, String prefix);
	
	DataWrapper<List<Student>> getStudentsFromFile(String path);
	
//	DataWrapper<String> saveExpData(MultipartFile file, Integer reservationId, Integer studentId, 
//			String rootPath);
//
//	DataWrapper<String> saveReport(MultipartFile file, Integer classId,
//			Integer expId, Integer stuId, String rootPath);
//
//	DataWrapper<String> savePhoto(MultipartFile file, Integer reservationId,
//			Integer studentId, String rootPath);

	DataWrapper<String> saveFileRecord(MultipartFile file, Type fileType,
			Integer reservationId, Integer stuId, String rootPath);
	
	DataWrapper<String> saveFileRecord(MultipartFile file, Type fileType, 
			Integer classId, Integer expId, Integer stuId, String rootPath);
	
	DataWrapper<String> genReportTemplate(Integer stuRecId, String rootPath);

	DataWrapper<String> saveFileRecordBySlotRes(MultipartFile data,
			Type fileType, Integer reservationId, Integer stuId,
			String rootPath);
	
	DataWrapper<String> saveFlashFile(String path, MultipartFile file);
	
	DataWrapper<String> deleteFileRecord(Type fileType, String fileName,
			Integer classId, Integer expId, Integer accountId, String rootPath);

	DataWrapper<String> deleteFileRecordBySlotRes(Type fileType,
			String fileName, Integer reservationId, Integer accountId,
			String rootPath);

	DataWrapper<String> deleteFileRecord(Type fileType, String fileName,
			Integer reservationId, Integer accountId, String rootPath);
}
