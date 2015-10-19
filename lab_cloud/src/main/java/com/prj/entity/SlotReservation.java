package com.prj.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "slot_reservation")
public class SlotReservation extends Reservation {
	private Administrator reserver;
	private Integer occupiedNumber;
	private Integer max;
	private Set<Student> students = new HashSet<Student>();
	
	public Integer getOccupiedNumber() {
		return occupiedNumber;
	}
	public void setOccupiedNumber(Integer occupiedNumber) {
		this.occupiedNumber = occupiedNumber;
	}
	public Integer getMax() {
		return max;
	}
	public void setMax(Integer max) {
		this.max = max;
	}
	
	@ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	@JoinTable(	name = "slot_reservation_student",
				joinColumns = {@JoinColumn(name = "slotReservationId")},
				inverseJoinColumns = {@JoinColumn(name = "studentId")})
	public Set<Student> getStudents() {
		return students;
	}
	public void setStudents(Set<Student> students) {
		this.students = students;
	}
	
	@ManyToOne
	@JoinColumn(name = "reserverId")
	public Administrator getReserver() {
		return reserver;
	}
	public void setReserver(Administrator reserver) {
		this.reserver = reserver;
	}
}
