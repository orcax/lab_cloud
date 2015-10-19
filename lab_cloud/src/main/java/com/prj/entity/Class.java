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

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name = "class")
public class Class extends BaseEntity {
	
	private String classNumber;
	private Semester semester;
	private Teacher teacher;
	private Course course;
	private String classHour;
	private String classRoom;
	private Set<StudentClass> studentClass = new HashSet<StudentClass>(0);
//	private Set<LabPlan> labPlans = new HashSet<LabPlan>(0);
	
	public String getClassNumber() {
		return classNumber;
	}
	public void setClassNumber(String classNumber) {
		this.classNumber = classNumber;
	}
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
	@JoinColumn(name = "semesterId")
//	@JsonIgnore
	public Semester getSemester() {
		return semester;
	}
	public void setSemester(Semester semester) {
		this.semester = semester;
	}
	
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
	@JoinColumn(name = "teacherId")
//	@JsonIgnore
	public Teacher getTeacher() {
		return teacher;
	}
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
	@JoinColumn(name = "courseId")
//	@JsonIgnore
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}

	public String getClassHour() {
		return classHour;
	}
	public void setClassHour(String classHour) {
		this.classHour = classHour;
	}
	public String getClassRoom() {
		return classRoom;
	}
	public void setClassRoom(String classRoom) {
		this.classRoom = classRoom;
	}
	
	@OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER,mappedBy = "clazz")
	@JsonIgnore
	public Set<StudentClass> getStudentClass() {
		return studentClass;
	}
	public void setStudentClass(Set<StudentClass> studentClass) {
		this.studentClass = studentClass;
	}

//	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "clazz")
//	public Set<LabPlan> getLabPlans() {
//		return labPlans;
//	}
//	public void setLabPlans(Set<LabPlan> labPlans) {
//		this.labPlans = labPlans;
//	}
}
