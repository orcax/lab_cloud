package com.prj.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "student_record")
public class StudentRecord extends BaseEntity {
	public enum Status {
		NOT_STARTED,
		IN_PROGRESS,
		SUBMITTED,
		COMPLETED
	}
	
	private Class clazz;
	private Student student;
	private Experiment experiment;
	private Status status;
	private Integer experimentRecord;
	private String experimentComment;
//	private String dataPath;
//	private String photoPath;
//	private String reportPath;
	private Set<FileRecord> fileRecords = new HashSet<FileRecord>(0);
	
//	private StudentLabPlan studentLabPlan;
//
//	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
//	@JoinColumn(name = "studentLabRecordId")
//	@JsonIgnore
//	public StudentLabPlan getStudentLabPlan() {
//		return studentLabPlan;
//	}
//
//	public void setStudentLabPlan(StudentLabPlan studentLabPlan) {
//		this.studentLabPlan = studentLabPlan;
//	}
	
//	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
//	@JoinColumn(name = "studentClassId")
//	public StudentClass getStudentClass() {
//		return studentClass;
//	}
//	public void setStudentClass(StudentClass studentClass) {
//		this.studentClass = studentClass;
//	}
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "classId")
	public Class getClazz() {
		return clazz;
	}
	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "studentId")
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "experimentId")
	public Experiment getExperiment() {
		return experiment;
	}
	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Integer getExperimentRecord() {
		return experimentRecord;
	}
	public void setExperimentRecord(Integer experimentRecord) {
		this.experimentRecord = experimentRecord;
	}
	public String getExperimentComment() {
		return experimentComment;
	}
	public void setExperimentComment(String experimentComment) {
		this.experimentComment = experimentComment;
	}
//	public String getDataPath() {
//		return dataPath;
//	}
//	public void setDataPath(String dataPath) {
//		this.dataPath = dataPath;
//	}
//	public String getPhotoPath() {
//		return photoPath;
//	}
//	public void setPhotoPath(String photoPath) {
//		this.photoPath = photoPath;
//	}
//	public String getReportPath() {
//		return reportPath;
//	}
//	public void setReportPath(String reportPath) {
//		this.reportPath = reportPath;
//	}
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "studentRecord")
	public Set<FileRecord> getFileRecords() {
		return fileRecords;
	}
	public void setFileRecords(Set<FileRecord> fileRecords) {
		this.fileRecords = fileRecords;
	}
	
}
