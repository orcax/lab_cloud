package com.prj.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "student_reservation")
public class StudentReservation extends Reservation {
//	private Set<Student> students = new HashSet<Student>(0);
//	private Set<LabPlan> labPlans = new HashSet<LabPlan>(0);
	private Student student;
//	private Lab lab;
//	private LabPlan labPlan;
//	private Experiment experiment;
//	private Slot slot;
//	private ApprovalStatus approvalStatus;
//	private TeacherLabReserve teacherLabReserve; 
	
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "studentId")
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
//	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
//	@JoinColumn(name = "labId")
//	public Lab getLab() {
//		return lab;
//	}
//	public void setLab(Lab lab) {
//		this.lab = lab;
//	}
//	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
//	@JoinColumn(name = "labPlanId")
//	@JsonIgnore	
//	public LabPlan getLabPlan() {
//		return labPlan;
//	}
//	public void setLabPlan(LabPlan labPlan) {
//		this.labPlan = labPlan;
//	}
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
	
//	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
//	@JoinColumn(name = "experimentId")
//	public Experiment getExperiment() {
//		return experiment;
//	}
//	public void setExperiment(Experiment experiment) {
//		this.experiment = experiment;
//	}
//	@OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, mappedBy = "studentReservation")
//	public TeacherLabReserve getTeacherLabReserve() {
//		return teacherLabReserve;
//	}
//	public void setTeacherLabReserve(TeacherLabReserve teacherLabReserve) {
//		this.teacherLabReserve = teacherLabReserve;
//	}
	
	
	
//	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "studentReservation")
//	public Set<Student> getStudents() {
//		return students;
//	}
//	public void setStudents(Set<Student> students) {
//		this.students = students;
//	}
//	
//	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "studentReservation")
//	public Set<LabPlan> getLabPlans() {
//		return labPlans;
//	}
//	public void setLabPlans(Set<LabPlan> labPlans) {
//		this.labPlans = labPlans;
//	}
}
