package com.prj.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "teacher_labreserve")
public class TeacherLabReserve extends BaseEntity {	
	//private Date date;
	//private Slot slot;
	private String serial;
	private Set<Teacher> teachers;
	private ClassReservation classReservation;
	private SlotReservation slotReservation;
	
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	
	@ManyToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinTable(name="teacher_labreserve_teacher",
		joinColumns={@JoinColumn(name="teacherLabReserveId")},
		inverseJoinColumns={@JoinColumn(name="teacherId")})
	public Set<Teacher> getTeachers() {
		return teachers;
	}
	public void setTeachers(Set<Teacher> teachers) {
		this.teachers = teachers;
	}
	
	@OneToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "classReservatoinId")
	public ClassReservation getClassReservation() {
		return classReservation;
	}
	public void setClassReservation(ClassReservation classReservation) {
		this.classReservation = classReservation;
	}
	
	@OneToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "slotReservationId")
	public SlotReservation getSlotReservation() {
		return slotReservation;
	}
	public void setSlotReservation(SlotReservation slotReservation) {
		this.slotReservation = slotReservation;
	}
	
}
