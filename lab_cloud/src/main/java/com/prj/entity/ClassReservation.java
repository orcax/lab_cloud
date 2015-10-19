package com.prj.entity;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "class_reservation")
public class ClassReservation extends Reservation {
//	public enum Slot {
//		ONE,
//		TWO,
//		THREE,
//		FOUR,
//		FIVE,
//		SIX
//	}
	private String classReservationNumber;
	private Teacher reserver;
	private Integer count; 	// 预定人数
	private String remark;
	
	private Class clazz;
//	private Experiment experiment;
//	private Lab lab;
//	private Slot slot;
//	private LabPlan labPlan;
//	private ApprovalStatus approvalStatus;
	
	
	public String getClassReservationNumber() {
		return classReservationNumber;
	}
	public void setClassReservationNumber(String classReservationNumber) {
		this.classReservationNumber = classReservationNumber;
	}
	
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "reserverId")
	public Teacher getReserver() {
		return reserver;
	}
	public void setReserver(Teacher reserver) {
		this.reserver = reserver;
	}
	
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
	@Lob
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "classId")
	public Class getClazz() {
		return clazz;
	}
	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}

//	public Slot getSlot() {
//		return slot;
//	}
//	public void setSlot(Slot slot) {
//		this.slot = slot;
//	}
	

	

//	public ApprovalStatus getApprovalStatus() {
//		return approvalStatus;
//	}
//	public void setApprovalStatus(ApprovalStatus approvalStatus) {
//		this.approvalStatus = approvalStatus;
//	}

}
