package com.prj.service;

import com.prj.entity.FileRecord;
import com.prj.util.DataWrapper;

public interface FileRecordService extends BaseService<FileRecord, Long> {

  DataWrapper findByPath(String path);

  DataWrapper removeBySRT(long stuRecId, FileRecord.Type fileType);
}
