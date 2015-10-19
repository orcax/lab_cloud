package com.prj.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prj.util.CopyRequired;

@Entity
@Table(name = "class_reservation")
public class ClazzReservation extends Reservation {

	private Integer count; // 预定人数
	private String remark;

	private Clazz clazz;
	
	private Set<Account> labTeachers;

    @CopyRequired
	@Column(nullable = false)
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@CopyRequired
	@Lob
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@ManyToOne
	@JoinColumn(name = "class_id")
	public Clazz getClazz() {
		return clazz;
	}

	public void setClazz(Clazz clazz) {
		this.clazz = clazz;
	}

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "class_reservation_teacher", joinColumns = { @JoinColumn(name = "class_reservation_id") }, inverseJoinColumns = { @JoinColumn(name = "lab_teacher_id") })
	public Set<Account> getLabTeachers() {
		return labTeachers;
	}

	public void setLabTeachers(Set<Account> labTeachers) {
		this.labTeachers = labTeachers;
	}

}