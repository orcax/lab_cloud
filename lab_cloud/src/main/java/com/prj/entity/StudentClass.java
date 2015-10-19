package com.prj.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "student_class")
public class StudentClass extends BaseEntity {
	private Student student;
	private Class clazz;
//	private ClassReservation classReservation;
	
	@ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	@JoinColumn(name="studentid")
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	@ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	@JoinColumn(name="classid")
	public Class getClazz() {
		return clazz;
	}
	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}
	
	
	
	
	
//	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
//	@JoinColumn(name = "classReservationId")
//	@JsonIgnore
//	public ClassReservation getClassReservation() {
//		return classReservation;
//	}
//	public void setClassReservation(ClassReservation classReservation) {
//		this.classReservation = classReservation;
//	}
}
