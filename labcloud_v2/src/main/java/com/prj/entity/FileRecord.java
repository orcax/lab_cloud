package com.prj.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.prj.util.CopyRequired;

@Entity
@Table(name = "file_record")
public class FileRecord extends BaseEntity {
	public enum Type {
		EXPDATA,
		REPORT,
		PHOTO;
	}

	String path;
	private Type type;
	StudentRecord studentRecord;

	@CopyRequired(update = false)
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	@CopyRequired(update = false)
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}

	@ManyToOne
	@JoinColumn(name = "studentRecordId")
	@CopyRequired(update = false)
	public StudentRecord getStudentRecord() {
		return studentRecord;
	}
	public void setStudentRecord(StudentRecord studentRecord) {
		this.studentRecord = studentRecord;
	}
}
