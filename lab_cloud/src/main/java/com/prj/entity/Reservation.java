package com.prj.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prj.entity.LabPlan.ApprovalStatus;

@MappedSuperclass
public class Reservation extends BaseEntity{
	public enum SlotNo {
		ONE,
		TWO,
		THREE,
		FOUR,
		FIVE,
		SIX,
		SEVEN
	}
	
	public enum Type {
		Class, Slot
	}
	
	private Date date;
	private SlotNo slot;
	private Experiment experiment;
	private Lab lab;
	private LabPlan labPlan;
	private ApprovalStatus approvalStatus;
	private TeacherLabReserve teacherLabReserve; // 实验教师教师与班级预约关系表
	
	@org.hibernate.annotations.Type(type = "date")
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public SlotNo getSlot() {
		return slot;
	}
	public void setSlot(SlotNo slot) {
		this.slot = slot;
	}
	
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "experimentId")
	public Experiment getExperiment() {
		return experiment;
	}
	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}
	
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "labId")
	public Lab getLab() {
		return lab;
	}
	public void setLab(Lab lab) {
		this.lab = lab;
	}
	
	@OneToOne
	@JoinColumn(name = "labPlanId")
	@JsonIgnore	
	public LabPlan getLabPlan() {
		return labPlan;
	}
	public void setLabPlan(LabPlan labPlan) {
		this.labPlan = labPlan;
	}
	
	public ApprovalStatus getApprovalStatus() {
		return approvalStatus;
	}
	public void setApprovalStatus(ApprovalStatus approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	
	@OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, mappedBy = "classReservation")
	public TeacherLabReserve getTeacherLabReserve() {
		return teacherLabReserve;
	}
	public void setTeacherLabReserve(TeacherLabReserve teacherLabReserve) {
		this.teacherLabReserve = teacherLabReserve;
	}
}
