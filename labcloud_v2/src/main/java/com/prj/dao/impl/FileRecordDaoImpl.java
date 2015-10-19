package com.prj.dao.impl;

import org.springframework.stereotype.Repository;

import com.prj.dao.FileRecordDao;
import com.prj.entity.FileRecord;

@Repository
public class FileRecordDaoImpl extends BaseDaoHibernateImpl<FileRecord, Long> implements FileRecordDao {

	public FileRecordDaoImpl() {
		super(FileRecord.class);
	}


}
