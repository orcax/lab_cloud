package com.prj.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "file_record")
public class FileRecord extends BaseEntity {
	public enum Type {
		expData("ExpData"),
		report("Report"),
		photo("Photo");
		
		private String label;
		
		Type(String label) {
			this.setLabel(label);
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}
	}
	
	String path;
	private Type type;
	StudentRecord studentRecord;
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	
	@ManyToOne//(fetch = FetchType.EAGER)
	@JoinColumn(name = "studentRecordId")
	public StudentRecord getStudentRecord() {
		return studentRecord;
	}
	public void setStudentRecord(StudentRecord studentRecord) {
		this.studentRecord = studentRecord;
	}
}
