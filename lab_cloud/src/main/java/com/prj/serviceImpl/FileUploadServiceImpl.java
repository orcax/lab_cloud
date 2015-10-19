package com.prj.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.prj.dao.AdministratorDao;
import com.prj.dao.ClassDao;
import com.prj.dao.ClassReservationDao;
import com.prj.dao.ExperimentDao;
import com.prj.dao.FileRecordDao;
import com.prj.dao.SlotReservationDao;
import com.prj.dao.StudentClassDao;
import com.prj.dao.StudentDao;
import com.prj.dao.StudentRecordDao;
import com.prj.dao.TeacherDao;
import com.prj.entity.Account;
import com.prj.entity.ClassReservation;
import com.prj.entity.Experiment;
import com.prj.entity.FileRecord;
import com.prj.entity.FileRecord.Type;
import com.prj.entity.SlotReservation;
import com.prj.entity.Student;
import com.prj.entity.StudentRecord;
import com.prj.entity.StudentRecord.Status;
import com.prj.service.FileUploadService;
import com.prj.util.AccountCharacter;
import com.prj.util.DataWrapper;
import com.prj.util.DateUtils;
import com.prj.util.DocumentGenerator;
import com.prj.util.ErrorCodeEnum;

@Service("FileUploadServiceImpl")
public class FileUploadServiceImpl implements FileUploadService {
	@Resource(name = "AdministratorDaoImpl")
	AdministratorDao administratorDao;
	@Resource(name = "TeacherDaoImpl")
	TeacherDao teacherDao;
	@Resource(name = "StudentDaoImpl")
	StudentDao studentDao;
	@Resource(name = "ClassReservationDaoImpl")
	ClassReservationDao crDao;
	@Resource(name = "SlotReservationDaoImpl")
	SlotReservationDao slrDao;
	@Resource(name = "ClassDaoImpl")
	ClassDao classDao;
	@Resource(name = "ExperimentDaoImpl")
	ExperimentDao expDao;
	@Resource(name = "StudentRecordDaoImpl")
	StudentRecordDao srDao;
	@Resource(name = "StudentClassDaoImpl")
	StudentClassDao scDao;
	@Resource(name = "FileRecordDaoImpl")
	FileRecordDao frDao;

	@Override
	public DataWrapper<String> saveFileById(String path, MultipartFile file,
			int id) {
		if (file == null)
			return new DataWrapper<String>(ErrorCodeEnum.File_Null);
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append(path);
		strBuffer.append('/');
		strBuffer.append(id);
		strBuffer.append('/');
		strBuffer.append(DateUtils.getCurrentDateString());
		strBuffer.append('/');
		path = strBuffer.toString();
		return saveFile(path, file, null);
	}

	@Override
	public DataWrapper<String> saveFlashFile(String path, MultipartFile file) {
		if (file == null)
			return new DataWrapper<String>(ErrorCodeEnum.File_Null);
		return saveFile(path, file,
				new Random().nextInt(100) + file.getOriginalFilename());
	}

	@Override
	public DataWrapper<String> saveFile(String path, MultipartFile file,
			String fileName) {
		if (file == null)
			return new DataWrapper<String>(ErrorCodeEnum.File_Null);
		DataWrapper<String> ret = new DataWrapper<String>();
		try {

			if (!new File(path).isDirectory()) {
				new File(path).mkdirs();
			}
			if (fileName == null)
				fileName = file.getOriginalFilename();
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
			ret.setData(path + "/" + fileName);
			return ret;

		} catch (Exception e) {
			e.printStackTrace();
			ret.setErrorCode(ErrorCodeEnum.File_Creation_Error);
			return ret;
		}
	}

	// @Override
	// public DataWrapper<String> saveIcon(String path, MultipartFile file,
	// String iconName) {
	// if (file == null)
	// return new DataWrapper<String>(ErrorCodeEnum.File_Null);
	// int i = file.getOriginalFilename().lastIndexOf(".");
	// String fileName = null;
	// if (i != -1) {
	// fileName = iconName + file.getOriginalFilename().substring(i);
	// }
	// return saveFile(path, file, fileName);
	// }

	@Override
	public DataWrapper<String> saveStudentListFile(String path,
			MultipartFile file, String prefix) {
		if (file == null)
			return new DataWrapper<String>(ErrorCodeEnum.File_Null);
		int i = file.getOriginalFilename().lastIndexOf(".");
		String fileName = null;
		if (i != -1) {
			fileName = prefix + new Date().getTime()
					+ file.getOriginalFilename().substring(i);
		}
		return saveFile(path, file, fileName);
	}

	public boolean isNo(String s) {
		return s.matches("\\*{0,1}[0-9]+");
	}

	@Override
	public DataWrapper<List<Student>> getStudentsFromFile(String path) {
		DataWrapper<List<Student>> ret = new DataWrapper<List<Student>>();
		try {
			InputStream inp;
			inp = new FileInputStream(path);

			// InputStream inp = new FileInputStream("workbook.xlsx");

			Workbook wb = WorkbookFactory.create(inp);
			Sheet sheet = wb.getSheetAt(0);
			int i = -1;
			List<Student> l = new ArrayList<Student>();

			Row row;
			Cell cell;
			do {
				row = sheet.getRow(++i);
				cell = row.getCell(0);

			} while (!isNo(cell.toString()));

			// row = sheet.getRow(i);
			// cell = row.getCell(0);

			while (cell != null && !cell.toString().equals("")) {
				Student student = new Student();
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
					// student.setGrade(getIntStr(row.getCell(2)));
				}
				cell = row.getCell(3);
				if (cell != null)
					student.setMajor(row.getCell(3).toString());
				cell = row.getCell(4);
				if (cell != null)
					student.setEmail(row.getCell(4).toString());
				l.add(student);
				row = sheet.getRow(++i);
				if (row == null) {
					// System.out.println("empty row");
					break;
				}
				cell = row.getCell(0);
			}
			ret.setData(l);
			return ret;

		} catch (Exception e) {
			e.printStackTrace();
			ret.setErrorCode(ErrorCodeEnum.File_Error);
			return ret;
		}
	}

	// @Override
	// public DataWrapper<String> saveExpData(MultipartFile file,
	// Integer reservationId, Integer studentId, String rootPath) {
	// if (file == null)
	// return new DataWrapper<String>(ErrorCodeEnum.File_Null);
	// ClassReservation cr = crDao.findClassReservationById(reservationId);
	// Student s = studentDao.findStudentById(studentId);
	// DataWrapper<String> ret = new DataWrapper<String>();
	// if (cr == null) {
	// ret.setErrorCode(ErrorCodeEnum.ClassReservation_Not_Exist);
	// } else {
	// Integer classId = cr.getClazz().getId();
	// String relativePath = "files/student/"
	// + s.getNumber() + '/'
	// + cr.getClazz().getSemester().getId() + '/'
	// + classId + '/'
	// + cr.getExperiment().getId();
	// String path = rootPath + '/' + relativePath;
	// ret = saveFile(path, file, null);
	// StudentRecord sr = srDao.getStudentRecordByClassStuExp(classId,
	// studentId, cr.getExperiment().getId());
	// // if (sr == null) {
	// // sr = new StudentRecord();
	// // sr.setExperiment(cr.getExperiment());
	// // sr.setStudentClass(scDao.getStudentClassBySC(studentId, classId));
	// // }
	// sr.setDataPath(relativePath+'/'+file.getOriginalFilename());
	// sr.setStatus(Status.IN_PROGRESS);
	// srDao.updateStudentRecord(sr);
	// ret.setData(relativePath+'/'+file.getOriginalFilename());
	// }
	// return ret;
	// }

	private String getIntStr(Cell cell) {
		String tmp = cell.toString();
		if (tmp.startsWith("*")) {
			tmp = tmp.substring(1);
		}
		if (tmp.endsWith(".0"))
			tmp = tmp.substring(0, tmp.length() - 2);
		return tmp;
	}

	final public static String ICON_DEFAULT_NAME = "icon";

	public DataWrapper<String> saveIcon(MultipartFile file, Integer accountId,
			AccountCharacter ac, String rootPath) {
		DataWrapper<String> ret = new DataWrapper<String>();
		try {
			if (file == null) {
				ret.setErrorCode(ErrorCodeEnum.File_Null);
			} else {
				Object dao;
				dao = this.getClass()
						.getDeclaredField(ac.getLowerCaseLabel() + "Dao")
						.get(this);
				Method method = dao.getClass().getMethod(
						"find" + ac.getCapitalizedLabel() + "ById",
						Integer.class);
				Account account = (Account) method.invoke(dao, accountId);
				String number = account.getNumber();
				String relativePath = "images/" + ac.getLowerCaseLabel() + "/"
						+ number + "/icon";
				String path = rootPath + '/' + relativePath;
				String iconName = ICON_DEFAULT_NAME;
				int i = file.getOriginalFilename().lastIndexOf(".");
				String fileName = null;
				if (i != -1) {
					fileName = iconName
							+ file.getOriginalFilename().substring(i);
				} else {
					fileName = iconName;
				}
				ret = saveFile(path, file, fileName);
				ret.setData(relativePath + '/' + fileName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ret.setErrorCode(ErrorCodeEnum.Unknown_Error);
		}
		return ret;
	}

	// @Override
	// public DataWrapper<String> saveReport(MultipartFile file, Integer
	// classId,
	// Integer expId, Integer stuId, String rootPath) {
	// if (file == null)
	// return new DataWrapper<String>(ErrorCodeEnum.File_Null);
	// com.prj.entity.Class c = classDao.findClassById(classId);
	// if (c == null) {
	// return new DataWrapper<String>(ErrorCodeEnum.Class_Not_Exist);
	// }
	// Experiment e = expDao.findExperimentById(expId);
	// if (e == null) {
	// return new DataWrapper<String>(ErrorCodeEnum.Experiment_Not_Exist);
	// }
	// Student s = studentDao.findStudentById(stuId);
	// DataWrapper<String> ret = new DataWrapper<String>();
	//
	// String relativePath = "files/student/"
	// + s.getNumber() + '/'
	// + c.getSemester().getId() + '/'
	// + c.getId() + '/'
	// + e.getId();
	// String path = rootPath + '/' + relativePath;
	// ret = saveFile(path, file, file.getOriginalFilename());
	// StudentRecord sr = srDao.getStudentRecordByClassStuExp(classId, stuId,
	// expId);
	// // if (sr == null) {
	// // sr = new StudentRecord();
	// // sr.setExperiment(e);
	// // sr.setStudentClass(scDao.getStudentClassBySC(stuId, classId));
	// // }
	// sr.setReportPath(relativePath+'/'+file.getOriginalFilename());
	// sr.setStatus(Status.SUBMITTED);
	// srDao.updateStudentRecord(sr);
	// ret.setData(relativePath+'/'+file.getOriginalFilename());
	//
	// return ret;
	// }

	// @Override
	// public DataWrapper<String> savePhoto(MultipartFile file,
	// Integer reservationId, Integer studentId, String rootPath) {
	// if (file == null)
	// return new DataWrapper<String>(ErrorCodeEnum.File_Null);
	// ClassReservation cr = crDao.findClassReservationById(reservationId);
	// Student s = studentDao.findStudentById(studentId);
	// DataWrapper<String> ret = new DataWrapper<String>();
	// if (cr == null) {
	// ret.setErrorCode(ErrorCodeEnum.ClassReservation_Not_Exist);
	// } else {
	// Integer classId = cr.getClazz().getId();
	// String relativePath = "files/student/"
	// + s.getNumber() + '/'
	// + cr.getClazz().getSemester().getId() + '/'
	// + classId + '/'
	// + cr.getExperiment().getId();
	// String path = rootPath + '/' + relativePath;
	// ret = saveFile(path, file, null);
	// StudentRecord sr = srDao.getStudentRecordByClassStuExp(classId,
	// studentId, cr.getExperiment().getId());
	// // if (sr == null) {
	// // sr = new StudentRecord();
	// // sr.setExperiment(cr.getExperiment());
	// // sr.setStudentClass(scDao.getStudentClassBySC(studentId, classId));
	// // }
	// sr.setPhotoPath(relativePath+'/'+file.getOriginalFilename());
	// sr.setStatus(Status.IN_PROGRESS);
	// srDao.updateStudentRecord(sr);
	// ret.setData(relativePath+'/'+file.getOriginalFilename());
	// }
	// return ret;
	// }

	private static boolean deleteFile(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			System.out.println("Remove File：" + filePath + " Not Found");
			return false;
		} else {
			if (file.isFile()) {
				System.out.println("Remove File：" + filePath);
				return file.delete();
			} else {
				return false;
				// return deleteDirectory(filePath);
			}
		}
	}

	private static boolean deleteDirectory(String dir) {
		// 如果dir不以文件分隔符结尾，自动添加文件分隔符
		if (!dir.endsWith(File.separator)) {
			dir = dir + File.separator;
		}
		File dirFile = new File(dir);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			System.out.println("Delete Folder:" + dir + " Not Found");
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = files[i].delete();
				if (!flag) {
					break;
				}
			}
			// 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag) {
					break;
				}
			}
		}

		if (!flag) {
			// System.out.println("删除目录失败");
			System.out
					.println("Remove Folder:" + dir + " Remove SubFile Error");
			return false;
		}

		// 删除当前目录
		if (dirFile.delete()) {
			System.out.println("Remove Folder:" + dir);
			return true;
		} else {
			System.out.println("Remove Folder:" + dir + " Error");
			return false;
		}
	}

	@Override
	public DataWrapper<String> genReportTemplate(Integer stuRecId,
			String rootPath) {
		DataWrapper<String> ret = new DataWrapper<String>();
		StudentRecord sr = srDao.findStudentRecordById(stuRecId);
		if (sr == null)
			return ret.setErrorCode(ErrorCodeEnum.StudentRecord_Not_Exist);
		Set<FileRecord> frs = sr.getFileRecords();
		String[] paths = new String[2];
		Date max0 = null, max1 = null;
		for (FileRecord fr : frs) {
			if (fr.getType() == Type.expData) {
				String[] strs = fr.getPath().split("[_.]");
				if (strs.length != 3) {
					System.out.println("File Name Unexpected: " + fr.getPath());
				} else if (strs[2].equals("TXT")) {
					Date temp = DateUtils.getDateFromString(strs[1]);
					if (max0 == null || max0.before(temp)) {
						max0 = temp;
						// table
						paths[0] = rootPath + "/" + fr.getPath();						
					}
				} else if (strs[0].endsWith("-1") && strs[2].equals("txt")) {
					Date temp = DateUtils.getDateFromString(strs[1]);
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
		String relativePath = "files/student/" + sr.getStudent().getNumber()
				+ '/' + sr.getClazz().getSemester().getId() + '/'
				+ sr.getClazz().getId() + '/' + sr.getExperiment().getId()
				+ '/' + Type.report.getLabel();
		DocumentGenerator.generate(rootPath, relativePath, "report_template",
				"report", paths);
		return ret.setData(relativePath + "/report_template.doc");
	}

	// @Override
	// public DataWrapper<String> saveReport(MultipartFile file, Integer
	// classId,
	// Integer expId, Integer stuId, String rootPath) {
	// if (file == null)
	// return new DataWrapper<String>(ErrorCodeEnum.File_Null);
	// com.prj.entity.Class c = classDao.findClassById(classId);
	// if (c == null) {
	// return new DataWrapper<String>(ErrorCodeEnum.Class_Not_Exist);
	// }
	// Experiment e = expDao.findExperimentById(expId);
	// if (e == null) {
	// return new DataWrapper<String>(ErrorCodeEnum.Experiment_Not_Exist);
	// }
	// Student s = studentDao.findStudentById(stuId);
	// DataWrapper<String> ret = new DataWrapper<String>();
	//
	// String relativePath = "files/student/"
	// + s.getNumber() + '/'
	// + c.getSemester().getId() + '/'
	// + c.getId() + '/'
	// + e.getId();
	// String path = rootPath + '/' + relativePath;
	// ret = saveFile(path, file, file.getOriginalFilename());
	// StudentRecord sr = srDao.getStudentRecordByClassStuExp(classId, stuId,
	// expId);
	// // if (sr == null) {
	// // sr = new StudentRecord();
	// // sr.setExperiment(e);
	// // sr.setStudentClass(scDao.getStudentClassBySC(stuId, classId));
	// // }
	// sr.setReportPath(relativePath+'/'+file.getOriginalFilename());
	// sr.setStatus(Status.SUBMITTED);
	// srDao.updateStudentRecord(sr);
	// ret.setData(relativePath+'/'+file.getOriginalFilename());
	//
	// return ret;
	// }
	
	// @Override
	// public DataWrapper<String> savePhoto(MultipartFile file,
	// Integer reservationId, Integer studentId, String rootPath) {
	// if (file == null)
	// return new DataWrapper<String>(ErrorCodeEnum.File_Null);
	// ClassReservation cr = crDao.findClassReservationById(reservationId);
	// Student s = studentDao.findStudentById(studentId);
	// DataWrapper<String> ret = new DataWrapper<String>();
	// if (cr == null) {
	// ret.setErrorCode(ErrorCodeEnum.ClassReservation_Not_Exist);
	// } else {
	// Integer classId = cr.getClazz().getId();
	// String relativePath = "files/student/"
	// + s.getNumber() + '/'
	// + cr.getClazz().getSemester().getId() + '/'
	// + classId + '/'
	// + cr.getExperiment().getId();
	// String path = rootPath + '/' + relativePath;
	// ret = saveFile(path, file, null);
	// StudentRecord sr = srDao.getStudentRecordByClassStuExp(classId,
	// studentId, cr.getExperiment().getId());
	// // if (sr == null) {
	// // sr = new StudentRecord();
	// // sr.setExperiment(cr.getExperiment());
	// // sr.setStudentClass(scDao.getStudentClassBySC(studentId, classId));
	// // }
	// sr.setPhotoPath(relativePath+'/'+file.getOriginalFilename());
	// sr.setStatus(Status.IN_PROGRESS);
	// srDao.updateStudentRecord(sr);
	// ret.setData(relativePath+'/'+file.getOriginalFilename());
	// }
	// return ret;
	// }
	
	@Override
	public DataWrapper<String> saveFileRecord(MultipartFile file,
			Type fileType, Integer classId, Integer expId, Integer stuId,
			String rootPath) {
		DataWrapper<String> ret = new DataWrapper<String>();
		try {
			if (file == null)
				return new DataWrapper<String>(ErrorCodeEnum.File_Null);
			com.prj.entity.Class c = classDao.findClassById(classId);
			if (c == null) {
				return new DataWrapper<String>(ErrorCodeEnum.Class_Not_Exist);
			}
			Experiment e = expDao.findExperimentById(expId);
			if (e == null) {
				return new DataWrapper<String>(
						ErrorCodeEnum.Experiment_Not_Exist);
			}
			Student s = studentDao.findStudentById(stuId);
	
			String relativePath = "files/student/" + s.getNumber() + '/'
					+ c.getSemester().getId() + '/' + c.getId() + '/'
					+ e.getId() + '/' + fileType.getLabel();
			String path = rootPath + '/' + relativePath;
	
			String fileName = URLDecoder.decode(file.getOriginalFilename(),
					"UTF-8");
	
			ret = saveFile(path, file, fileName);
			StudentRecord sr = srDao.getStudentRecordByClassStuExp(classId,
					stuId, expId);
			FileRecord fr = new FileRecord();
			fr.setPath(relativePath + '/' + fileName);
			fr.setType(fileType);
			fr.setStudentRecord(sr);
			String rPath = frDao.addFileRecord(sr.getId(), fr);
			if (rPath != null) {
				FileRecord fr0 = frDao.getFileRecordByPath(relativePath + '/' + fileName);
				if (fr0 == null)
					return ret.setErrorCode(ErrorCodeEnum.File_Not_Exist);
				
				// TODO CHECK FILE CNT
				for (FileRecord f : sr.getFileRecords()) {
					if (f.getPath().equals(fr0.getPath())) {
						sr.getFileRecords().remove(f);
						break;
					}
				}
				srDao.updateStudentRecord(sr);
				frDao.removeFileRecord(fr0);
				deleteFile(rootPath + '/' + rPath);
			}
			// sr.getFileRecords().add(fr);
			sr.setStatus(Status.SUBMITTED);
			srDao.updateStudentRecord(sr);
			ret.setData(fr.getPath());
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			ret.setErrorCode(ErrorCodeEnum.Unknown_Error);
		}
		return ret;
	}

	@Override
	public DataWrapper<String> saveFileRecord(MultipartFile file,
			Type fileType, Integer reservationId, Integer studentId,
			String rootPath) {
		DataWrapper<String> ret = new DataWrapper<String>();
		try {
			if (file == null)
				return new DataWrapper<String>(ErrorCodeEnum.File_Null);
			ClassReservation cr = crDao.findClassReservationById(reservationId);
			Student s = studentDao.findStudentById(studentId);
			if (cr == null) {
				ret.setErrorCode(ErrorCodeEnum.ClassReservation_Not_Exist);
			} else {
				Integer classId = cr.getClazz().getId();
				String relativePath = "files/student/" + s.getNumber() + '/'
						+ cr.getClazz().getSemester().getId() + '/' + classId
						+ '/' + cr.getExperiment().getId() + '/'
						+ fileType.getLabel();
				String path = rootPath + '/' + relativePath;
				String fileName = URLDecoder.decode(file.getOriginalFilename(),
						"UTF-8");

				ret = saveFile(path, file, fileName);
				StudentRecord sr = srDao.getStudentRecordByClassStuExp(classId,
						studentId, cr.getExperiment().getId());
				FileRecord fr = new FileRecord();
				fr.setPath(relativePath + '/' + fileName);
				fr.setType(fileType);
				fr.setStudentRecord(sr);
				String rPath = frDao.addFileRecord(sr.getId(), fr);
				if (rPath != null) {
					FileRecord fr0 = frDao.getFileRecordByPath(relativePath + '/' + fileName);
					if (fr0 == null)
						return ret.setErrorCode(ErrorCodeEnum.File_Not_Exist);
					
					// TODO CHECK FILE CNT
					for (FileRecord f : sr.getFileRecords()) {
						if (f.getPath().equals(fr0.getPath())) {
							sr.getFileRecords().remove(f);
							break;
						}
					}
					srDao.updateStudentRecord(sr);
					frDao.removeFileRecord(fr0);
					deleteFile(rootPath + '/' + rPath);
				}
				// sr.getFileRecords().add(fr);
				sr.setStatus(Status.IN_PROGRESS);
				srDao.updateStudentRecord(sr);
				ret.setData(fr.getPath());
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			ret.setErrorCode(ErrorCodeEnum.Unknown_Error);
		}
		return ret;
	}

	@Override
	public DataWrapper<String> saveFileRecordBySlotRes(MultipartFile file,
			Type fileType, Integer reservationId, Integer studentId,
			String rootPath) {
		DataWrapper<String> ret = new DataWrapper<String>();
		try {
			if (file == null)
				return new DataWrapper<String>(ErrorCodeEnum.File_Null);
			SlotReservation slr = slrDao.findSlotReservationById(reservationId);
			Student s = studentDao.findStudentById(studentId);
			if (slr == null) {
				ret.setErrorCode(ErrorCodeEnum.ClassReservation_Not_Exist);
			} else {
				List<com.prj.entity.Class> cs = classDao.getClassesBySE(
						studentId, slr.getExperiment().getId());
				for (com.prj.entity.Class c : cs) {
					Integer classId = c.getId();
					String relativePath = "files/student/" + s.getNumber()
							+ '/' + c.getSemester().getId() + '/' + classId
							+ '/' + slr.getExperiment().getId() + '/'
							+ fileType.getLabel();
					String path = rootPath + '/' + relativePath;
					String fileName = URLDecoder.decode(
							file.getOriginalFilename(), "UTF-8");

					ret = saveFile(path, file, fileName);
					StudentRecord sr = srDao.getStudentRecordByClassStuExp(
							classId, studentId, slr.getExperiment().getId());
					FileRecord fr = new FileRecord();
					fr.setPath(relativePath + '/' + fileName);
					fr.setType(fileType);
					fr.setStudentRecord(sr);
					String rPath = frDao.addFileRecord(sr.getId(), fr);
					if (rPath != null) {
						FileRecord fr0 = frDao.getFileRecordByPath(relativePath + '/' + fileName);
						if (fr0 == null)
							return ret.setErrorCode(ErrorCodeEnum.File_Not_Exist);
						
						// TODO CHECK FILE CNT
						for (FileRecord f : sr.getFileRecords()) {
							if (f.getPath().equals(fr0.getPath())) {
								sr.getFileRecords().remove(f);
								break;
							}
						}
						srDao.updateStudentRecord(sr);
						frDao.removeFileRecord(fr0);
						deleteFile(rootPath + '/' + rPath);
					}
					// sr.getFileRecords().add(fr);
					sr.setStatus(Status.IN_PROGRESS);
					srDao.updateStudentRecord(sr);
					ret.setData(fr.getPath());
				}
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			ret.setErrorCode(ErrorCodeEnum.Unknown_Error);
		}
		return ret;
	}

	@Override
	public DataWrapper<String> deleteFileRecord(
			Type fileType, String fileName,
			Integer classId, Integer expId, Integer stuId,
			String rootPath) {
		DataWrapper<String> ret = new DataWrapper<String>();
//		try {
//			if (file == null)
//				return new DataWrapper<String>(ErrorCodeEnum.File_Null);
			com.prj.entity.Class c = classDao.findClassById(classId);
			if (c == null) {
				return new DataWrapper<String>(ErrorCodeEnum.Class_Not_Exist);
			}
			Experiment e = expDao.findExperimentById(expId);
			if (e == null) {
				return new DataWrapper<String>(
						ErrorCodeEnum.Experiment_Not_Exist);
			}
			Student s = studentDao.findStudentById(stuId);
	
			String relativePath = "files/student/" + s.getNumber() + '/'
					+ c.getSemester().getId() + '/' + c.getId() + '/'
					+ e.getId() + '/' + fileType.getLabel();
			String path = rootPath + '/' + relativePath;
	
//			String fileName = URLDecoder.decode(file.getOriginalFilename(),
//					"UTF-8");
	
			StudentRecord sr = srDao.getStudentRecordByClassStuExp(classId,
					stuId, expId);
			
			FileRecord fr = frDao.getFileRecordByPath(relativePath + '/' + fileName);
			if (fr == null)
				return ret.setErrorCode(ErrorCodeEnum.File_Not_Exist);
			
			// TODO CHECK FILE CNT
			for (FileRecord f : sr.getFileRecords()) {
				if (f.getPath().equals(fr.getPath())) {
					sr.getFileRecords().remove(f);
					break;
				}
			}
			srDao.updateStudentRecord(sr);
			frDao.removeFileRecord(fr);
			deleteFile(path + '/' + fileName);
			
			ret.setData(fr.getPath());
//		} catch (UnsupportedEncodingException e1) {
//			e1.printStackTrace();
//			ret.setErrorCode(ErrorCodeEnum.Unknown_Error);
//		}
		return ret;
	}
	
	@Override
	public DataWrapper<String> deleteFileRecord(
			Type fileType, String fileName,
			Integer reservationId, Integer studentId,
			String rootPath) {
		DataWrapper<String> ret = new DataWrapper<String>();
//		try {
//			if (file == null)
//				return new DataWrapper<String>(ErrorCodeEnum.File_Null);
			ClassReservation cr = crDao.findClassReservationById(reservationId);
			Student s = studentDao.findStudentById(studentId);
			if (cr == null) {
				ret.setErrorCode(ErrorCodeEnum.ClassReservation_Not_Exist);
			} else {
				Integer classId = cr.getClazz().getId();
				String relativePath = "files/student/" + s.getNumber() + '/'
						+ cr.getClazz().getSemester().getId() + '/' + classId
						+ '/' + cr.getExperiment().getId() + '/'
						+ fileType.getLabel();
				String path = rootPath + '/' + relativePath;
//				String fileName = URLDecoder.decode(file.getOriginalFilename(),
//						"UTF-8");

				StudentRecord sr = srDao.getStudentRecordByClassStuExp(classId,
						studentId, cr.getExperiment().getId());
				
				FileRecord fr = frDao.getFileRecordByPath(relativePath + '/' + fileName);
				if (fr == null)
					return ret.setErrorCode(ErrorCodeEnum.File_Not_Exist);
				
				// TODO CHECK FILE CNT
				for (FileRecord f : sr.getFileRecords()) {
					if (f.getPath().equals(fr.getPath())) {
						sr.getFileRecords().remove(f);
						break;
					}
				}
				srDao.updateStudentRecord(sr);
				frDao.removeFileRecord(fr);
				deleteFile(path + '/' + fileName);
				ret.setData(fr.getPath());
			}
//		} catch (UnsupportedEncodingException e1) {
//			e1.printStackTrace();
//			ret.setErrorCode(ErrorCodeEnum.Unknown_Error);
//		}
		return ret;
	}

	@Override
	public DataWrapper<String> deleteFileRecordBySlotRes(
			Type fileType, String fileName,
			Integer reservationId, Integer studentId,
			String rootPath) {
		DataWrapper<String> ret = new DataWrapper<String>();
//		try {
//			if (file == null)
//				return new DataWrapper<String>(ErrorCodeEnum.File_Null);
			SlotReservation slr = slrDao.findSlotReservationById(reservationId);
			Student s = studentDao.findStudentById(studentId);
			if (slr == null) {
				ret.setErrorCode(ErrorCodeEnum.ClassReservation_Not_Exist);
			} else {
				List<com.prj.entity.Class> cs = classDao.getClassesBySE(
						studentId, slr.getExperiment().getId());
				for (com.prj.entity.Class c : cs) {
					Integer classId = c.getId();
					String relativePath = "files/student/" + s.getNumber()
							+ '/' + c.getSemester().getId() + '/' + classId
							+ '/' + slr.getExperiment().getId() + '/'
							+ fileType.getLabel();
					String path = rootPath + '/' + relativePath;
//					String fileName = URLDecoder.decode(
//							file.getOriginalFilename(), "UTF-8");

//					ret = saveFile(path, file, fileName);
					StudentRecord sr = srDao.getStudentRecordByClassStuExp(
							classId, studentId, slr.getExperiment().getId());
					
					FileRecord fr = frDao.getFileRecordByPath(relativePath + '/' + fileName);
					if (fr == null)
						return ret.setErrorCode(ErrorCodeEnum.File_Not_Exist);
					
					// TODO CHECK FILE CNT
					for (FileRecord f : sr.getFileRecords()) {
						if (f.getPath().equals(fr.getPath())) {
							sr.getFileRecords().remove(f);
							break;
						}
					}
					srDao.updateStudentRecord(sr);
					frDao.removeFileRecord(fr);
					deleteFile(path + '/' + fileName);
					
					ret.setData(fr.getPath());
				}
			}
//		} catch (UnsupportedEncodingException e1) {
//			e1.printStackTrace();
//			ret.setErrorCode(ErrorCodeEnum.Unknown_Error);
//		}
		return ret;
	}
}
