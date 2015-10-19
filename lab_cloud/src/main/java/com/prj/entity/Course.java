package com.prj.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "course")
public class Course extends BaseEntity {
	
	private String courseNumber;
	private String courseName;
	private String department;
	private Date startYear;
	private Boolean isActive = true;
	private Set<Class> classes = new HashSet<Class>(0);
	private Set<CourseExperiment> courseExperiment = new HashSet<CourseExperiment>(0);
	
	@Column(nullable = false)
	public String getCourseNumber() {
		return courseNumber;
	}
	public void setCourseNumber(String courseNumber) {
		this.courseNumber = courseNumber;
	}
	@Column(nullable = false)
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	
	@Column(nullable = false)
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	
	@Column(nullable = false)
	@Type(type = "date")
	public Date getStartYear() {
		return startYear;
	}
	public void setStartYear(Date startYear) {
		this.startYear = startYear;
	}
//	public void setStartYear(String startYear) {
//		System.out.println(startYear);
//		this.startYear = DateUtils.getDateFormString(startYear + "-1-1 0:0:0");
//	}
	
	@Column(nullable = false)
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "course")
	public Set<Class> getClasses() {
		return classes;
	}
	public void setClasses(Set<Class> classes) {
		this.classes = classes;
	}
	
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER,mappedBy = "course")
	public Set<CourseExperiment> getCourseExperiment() {
		return courseExperiment;
	}
	public void setCourseExperiment(Set<CourseExperiment> courseExperiment) {
		this.courseExperiment = courseExperiment;
	}
	
}
