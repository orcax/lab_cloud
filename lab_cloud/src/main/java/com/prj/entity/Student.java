package com.prj.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "student")
public class Student extends Account {

	//	private String number;
//	private String password;
	private String name;
	private String email;
	private String grade;
	private String major;
//	private Status status = Status.NEW;

//	private String loginToken;

	private Set<StudentClass> studentClass = new HashSet<StudentClass>(0);
	private Set<StudentReservation> studentReservation = new HashSet<StudentReservation>(0);
	
//	public String getNumber() {
//		return number;
//	}
//	public void setNumber(String number) {
//		this.number = number;
//	}
//	public String getPassword() {
//		return password;
//	}
//	public void setPassword(String password) {
//		this.password = password;
//	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
//	public Status getStatus() {
//		return status;
//	}
//	public void setStatus(Status status) {
//		this.status = status;
//	}
//	public String getLoginToken() {
//		return loginToken;
//	}
//	public void setLoginToken(String loginToken) {
//		this.loginToken = loginToken;
//	}

	@OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER,mappedBy="student")
//	@JsonIgnore
	public Set<StudentClass> getStudentClass() {
		return studentClass;
	}
	public void setStudentClass(Set<StudentClass> studentClass) {
		this.studentClass = studentClass;
	}

	@OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER,mappedBy="student")
	//@JoinColumn(name = "studentReaservationId")
//	@JsonIgnore
	public Set<StudentReservation> getStudentReservation() {
		return studentReservation;
	}
	public void setStudentReservation(Set<StudentReservation> studentReservation) {
		this.studentReservation = studentReservation;
	}
}
