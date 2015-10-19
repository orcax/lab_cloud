package com.prj.dao;

import java.util.List;

import com.prj.entity.FileRecord;
import com.prj.entity.FileRecord.Type;

public interface FileRecordDao {
	FileRecord getFileRecordByPath(String path);

	FileRecord getFileRecordBySRType(Integer srId, Type type);

	String addFileRecord(Integer srId, FileRecord fileRecord);

	List<FileRecord> getFileRecordsBySR(Integer srId);

	void removeFileRecord(FileRecord fileRecord);
}
