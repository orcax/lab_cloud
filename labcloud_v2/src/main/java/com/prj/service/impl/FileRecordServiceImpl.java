package com.prj.service.impl;

import com.prj.dao.FileRecordDao;
import com.prj.entity.FileRecord;
import com.prj.service.FileRecordService;
import com.prj.util.DataWrapper;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileRecordServiceImpl extends BaseServiceImpl<FileRecord, Long> implements FileRecordService{
	FileRecordDao frd;

	@Autowired
	public FileRecordServiceImpl(FileRecordDao cd) {
		super(FileRecord.class, cd);
		this.frd = cd;
	}



	public DataWrapper create(FileRecord entity) {
		return super.createEntity(entity);
	}

  public DataWrapper findByPath(String path) {
    return new DataWrapper(frd.createCriteria()
      .add(Restrictions.eq("path", path))
      .uniqueResult());
  }

  public DataWrapper removeBySRT(long stuRecId, FileRecord.Type fileType) {
    List l = frd.createCriteria()
      .add(Restrictions.and(
        Restrictions.eq("studentRecord.id", stuRecId),
        Restrictions.eq("type", fileType)
      ))
      .list();
    for (Object e : l) {
      frd.delete((FileRecord) e);
    }
    return DataWrapper.voidSuccessRet;
  }
}
