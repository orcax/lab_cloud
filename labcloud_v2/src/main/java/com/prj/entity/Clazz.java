package com.prj.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prj.util.CopyRequired;

@Entity
@Table(name = "clazz")
public class Clazz extends BaseEntity {
	
	private String number;
	private String clazzHour;
	private String clazzroom;
	private String description;

	private Semester semester;
	private Course course;
	private Account teacher;
	private Set<Account> students = new HashSet<Account>(0);
	
	@CopyRequired
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	@ManyToOne
	@JoinColumn(name = "semester_id")
	public Semester getSemester() {
		return semester;
	}
	public void setSemester(Semester semester) {
		this.semester = semester;
	}
	
	@ManyToOne
	@JoinColumn(name = "teacher_id")
	public Account getTeacher() {
		return teacher;
	}
	public void setTeacher(Account teacher) {
		this.teacher = teacher;
		
	}
	
	@ManyToOne
	@JoinColumn(name = "course_id")
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	
	@CopyRequired
	@Column(name = "clazz_hour")
	public String getClazzHour() {
		return clazzHour;
	}
	public void setClazzHour(String clazzHour) {
		this.clazzHour = clazzHour;
	}
	
	@CopyRequired
	@Column(name = "clazzroom")
	public String getClazzroom() {
		return clazzroom;
	}
	public void setClazzroom(String clazzroom) {
		this.clazzroom = clazzroom;
	}
	
	@ManyToMany
	@JsonIgnore
	@JoinTable(	name = "clazz_student",
				joinColumns = {@JoinColumn(name = "clazz_id")},
				inverseJoinColumns = {@JoinColumn(name = "student_id")})
	public Set<Account> getStudents() {
		return students;
	}
	public void setStudents(Set<Account> students) {
		this.students = students;
	}
	
	@CopyRequired
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
