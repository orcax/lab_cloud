package com.prj.daoImpl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.prj.dao.AbstractHibernateDao;
import com.prj.dao.FileRecordDao;
import com.prj.entity.FileRecord;
import com.prj.entity.FileRecord.Type;

@Service("FileRecordDaoImpl")
public class FileRecordDaoImpl extends AbstractHibernateDao<FileRecord, Integer>
		implements FileRecordDao {
	protected FileRecordDaoImpl() {
		super(FileRecord.class);
	}

	@Override
	public String addFileRecord(Integer srId, FileRecord fileRecord) {
		String fileToRemove = null;
		FileRecord fr = getFileRecordByPath(fileRecord.getPath());
		if (fileRecord.getType() == Type.expData) {
			if (fr == null) {
				super.add(fileRecord);
			}
		} else {
			fr = getFileRecordBySRType(srId, fileRecord.getType());
			if (fr == null) {
				super.add(fileRecord);
			} else if (fr != null && !fr.getPath().equals(fileRecord.getPath())) {
				fileToRemove = fr.getPath();
				fr.setPath(fileRecord.getPath());
				super.saveOrUpdate(fr);
			} 
		}
		return fileToRemove;
	}

	@Override
	public FileRecord getFileRecordByPath(String path) {
		return (FileRecord) super.createCriteria()
				.add(Restrictions.eq("path", path))
				.uniqueResult();
	}

	@Override
	public FileRecord getFileRecordBySRType(Integer srId, Type type) {
		return (FileRecord) super.createCriteria()
				.add(Restrictions.and(
						Restrictions.eq("studentRecord.id", srId),
						Restrictions.eq("type", type)))
				.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FileRecord> getFileRecordsBySR(Integer srId) {
		return super.createCriteria()
				.add(Restrictions.eq("studentRecord.id", srId))
				.list();
	}

	@Override
	public void removeFileRecord(FileRecord fileRecord) {
		super.delete(fileRecord);
	}
	
}
