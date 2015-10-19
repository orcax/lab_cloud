package com.prj.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prj.util.CopyRequired;

@Entity
@Table(name = "student_reservation")
public class StudentReservation extends Reservation {

	private int occupiedCount;
	private int maxCount;

	private Set<Account> students = new HashSet<Account>();
	private Set<Account> labTeachers = new HashSet<Account>();

	private Semester semester;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "semester_id")
	public Semester getSemester() {
		return semester;
	}

	public void setSemester(Semester semester) {
		this.semester = semester;
	}

	@CopyRequired
	@Column(name = "occupied_count")
	public int getOccupiedCount() {
		return occupiedCount;
	}

	public void setOccupiedCount(int occupiedCount) {
		this.occupiedCount = occupiedCount;
	}

	@CopyRequired
	@Column(name = "max_count")
	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	@JsonIgnore
	@ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	@JoinTable(name = "student_reservation_student", joinColumns = { @JoinColumn(name = "student_reservation_id") }, inverseJoinColumns = { @JoinColumn(name = "student_id") })
	public Set<Account> getStudents() {
		return students;
	}

	public void setStudents(Set<Account> students) {
		this.students = students;
	}

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "student_reservation_teacher", joinColumns = { @JoinColumn(name = "student_reservation_id") }, inverseJoinColumns = { @JoinColumn(name = "lab_teacher_id") })
	public Set<Account> getLabTeachers() {
		return labTeachers;
	}

	public void setLabTeachers(Set<Account> labTeachers) {
		this.labTeachers = labTeachers;
	}

}
