package com.prj.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "lab_plan")
public class LabPlan extends BaseEntity {
	public enum OpenStatus {
		OPEN, 
		CLOSED,
		PENDING,
		APPROVED
	}

	// 在lab_plan中未使用
	public enum ApprovalStatus {
		PENDING,
		APPROVED,
		REJECTED,
		ALL
	}
	
	public enum SlotType {
		CLASS, 
		STUDENT
	}
	
	public enum Weekday {
		SUNDAY,
		MONDAY,
		TUESDAY,
		WEDNESDAY,
		THURSDAY,
		FRIDAY,
		SATURDAY
	}

//	public static Integer MAX_SLOT_ID = 7;
	
	private Date date;
	private Weekday weekday;
	private Lab Lab;
	//TODO CHECK
	private SlotType slot1Type;
	private OpenStatus slot1OpenStatus;
//	private ApprovalStatus slot1ApprovalStatus;
//	private Class slot1Class;
	private ClassReservation slot1ClassReservation;
	private Integer slot1OccupiedNumber;
	private Experiment slot1Experiment;
	private Integer slot1ChapterNumber;
//	private ClassReservation slot1ClassReservation;
	private Set<StudentReservation> slot1StudentReservations;


	private SlotType slot2Type;
	private OpenStatus slot2OpenStatus;
//	private ApprovalStatus slot2ApprovalStatus;
//	private Class slot2Class;
	private ClassReservation slot2ClassReservation;
	private Integer slot2OccupiedNumber;
	private Experiment slot2Experiment;
	private Integer slot2ChapterNumber;
//	private ClassReservation slot2ClassReservation;
	private Set<StudentReservation> slot2StudentReservations;
//	private Integer slot2studentReservationMax;
	
	private SlotType slot3Type;
	private OpenStatus slot3OpenStatus;
//	private ApprovalStatus slot3ApprovalStatus;
//	private Class slot3Class;
	private ClassReservation slot3ClassReservation;
	private Integer slot3OccupiedNumber;
	private Experiment slot3Experiment;
	private Integer slot3ChapterNumber;
//	private ClassReservation slot3ClassReservation;
	private Set<StudentReservation> slot3StudentReservations;
//	private Integer slot3studentReservationMax;
	
	private SlotType slot4Type;
	private OpenStatus slot4OpenStatus;
//	private ApprovalStatus slot4ApprovalStatus;
//	private Class slot4Class;
	private ClassReservation slot4ClassReservation;
	private Integer slot4OccupiedNumber;
	private Experiment slot4Experiment;
	private Integer slot4ChapterNumber;
//	private ClassReservation slot4ClassReservation;
	private Set<StudentReservation> slot4StudentReservations;
//	private Integer slot4studentReservationMax;
	
	private SlotType slot5Type;
	private OpenStatus slot5OpenStatus;
//	private ApprovalStatus slot5ApprovalStatus;
//	private Class slot5Class;
	private ClassReservation slot5ClassReservation;
	private Integer slot5OccupiedNumber;
	private Experiment slot5Experiment;
	private Integer slot5ChapterNumber;
//	private ClassReservation slot5ClassReservation;
	private Set<StudentReservation> slot5StudentReservations;
//	private Integer slot5studentReservationMax;
	
	private SlotType slot6Type;
	private OpenStatus slot6OpenStatus;
//	private ApprovalStatus slot6ApprovalStatus;
//	private Class slot6Class;
	private ClassReservation slot6ClassReservation;
	private Integer slot6OccupiedNumber;
	private Experiment slot6Experiment;
	private Integer slot6ChapterNumber;
//	private ClassReservation slot6ClassReservation;
	private Set<StudentReservation> slot6StudentReservations;
//	private Integer slot6studentReservationMax;
	
	private SlotType slot7Type;
	private OpenStatus slot7OpenStatus;
//	private ApprovalStatus slot7ApprovalStatus;
//	private Class slot7Class;
	private ClassReservation slot7ClassReservation;
	private Integer slot7OccupiedNumber;
	private Experiment slot7Experiment;
	private Integer slot7ChapterNumber;
//	private ClassReservation slot7ClassReservation;
	private Set<StudentReservation> slot7StudentReservations;
//	private Integer slot7studentReservationMax;
	
	private SlotReservation slot1SlotReservation;
	private SlotReservation slot2SlotReservation;
	private SlotReservation slot3SlotReservation;
	private SlotReservation slot4SlotReservation;
	private SlotReservation slot5SlotReservation;
	private SlotReservation slot6SlotReservation;
	private SlotReservation slot7SlotReservation;
	
	private Integer slot1SlotReservationMax;
	private Integer slot2SlotReservationMax;
	private Integer slot3SlotReservationMax;
	private Integer slot4SlotReservationMax;
	private Integer slot5SlotReservationMax;
	private Integer slot6SlotReservationMax;
	private Integer slot7SlotReservationMax;
	
//	private SlotType slot8Type;
//	private OpenStatus slot8OpenStatus;
////	private ApprovalStatus slot8ApprovalStatus;
////	private Class slot8Class;
//	private ClassReservation slot8ClassReservation;
//	private Integer slot8OccupiedNumber;
//	private Experiment slot8Experiment;
//	private Integer slot8ChapterNumber;
////	private ClassReservation slot8ClassReservation;
//	private Set<StudentReservation> slot8StudentReservations;
//	
//	private SlotType slot9Type;
//	private OpenStatus slot9OpenStatus;
////	private ApprovalStatus slot9ApprovalStatus;
////	private Class slot9Class;
//	private ClassReservation slot9ClassReservation;
//	private Integer slot9OccupiedNumber;
//	private Experiment slot9Experiment;
//	private Integer slot9ChapterNumber;
////	private ClassReservation slot9ClassReservation;
//	private Set<StudentReservation> slot9StudentReservations;
	
	@Column(nullable = false)
	@Type(type = "date")
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Weekday getWeekday() {
		return weekday;
	}

	public void setWeekday(Weekday weekday) {
		this.weekday = weekday;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "labId")
	public Lab getLab() {
		return Lab;
	}

	public void setLab(Lab lab) {
		Lab = lab;
	}

	//slot1
	public SlotType getSlot1Type() {
		return slot1Type;
	}

	public void setSlot1Type(SlotType slot1Type) {
		this.slot1Type = slot1Type;
	}

	public OpenStatus getSlot1OpenStatus() {
		return slot1OpenStatus;
	}

	public void setSlot1OpenStatus(OpenStatus slot1OpenStatus) {
		this.slot1OpenStatus = slot1OpenStatus;
	}

//	public ApprovalStatus getSlot1ApprovalStatus() {
//		return slot1ApprovalStatus;
//	}
//
//	public void setSlot1ApprovalStatus(ApprovalStatus slot1ApprovalStatus) {
//		this.slot1ApprovalStatus = slot1ApprovalStatus;
//	}
//
//	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
//	@JoinColumn(name = "slot1ClassId")
//	public Class getSlot1Class() {
//		return slot1Class;
//	}
//
//	public void setSlot1Class(Class slot1Class) {
//		this.slot1Class = slot1Class;
//	}

	@OneToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "slot1ClassReservationId")
	public ClassReservation getSlot1ClassReservation() {
		return slot1ClassReservation;
	}

	public void setSlot1ClassReservation(ClassReservation slot1ClassReservation) {
		this.slot1ClassReservation = slot1ClassReservation;
	}

	public Integer getSlot1OccupiedNumber() {
		return slot1OccupiedNumber;
	}

	public void setSlot1OccupiedNumber(Integer slot1OccupiedNumber) {
		this.slot1OccupiedNumber = slot1OccupiedNumber;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "slot1ExperimentId")
	public Experiment getSlot1Experiment() {
		return slot1Experiment;
	}

	public void setSlot1Experiment(Experiment slot1Experiment) {
		this.slot1Experiment = slot1Experiment;
	}

	public Integer getSlot1ChapterNumber() {
		return slot1ChapterNumber;
	}

	public void setSlot1ChapterNumber(Integer slot1ChapterNumber) {
		this.slot1ChapterNumber = slot1ChapterNumber;
	}

	@OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, mappedBy = "labPlan")
	public Set<StudentReservation> getSlot1StudentReservations() {
		return slot1StudentReservations;
	}

	public void setSlot1StudentReservations(
			Set<StudentReservation> slot1StudentReservations) {
		this.slot1StudentReservations = slot1StudentReservations;
	}

	
	//slot2
	public SlotType getSlot2Type() {
		return slot2Type;
	}

	public void setSlot2Type(SlotType slot2Type) {
		this.slot2Type = slot2Type;
	}

	public OpenStatus getSlot2OpenStatus() {
		return slot2OpenStatus;
	}

	public void setSlot2OpenStatus(OpenStatus slot2OpenStatus) {
		this.slot2OpenStatus = slot2OpenStatus;
	}

//	public ApprovalStatus getSlot2ApprovalStatus() {
//		return slot2ApprovalStatus;
//	}
//
//	public void setSlot2ApprovalStatus(ApprovalStatus slot2ApprovalStatus) {
//		this.slot2ApprovalStatus = slot2ApprovalStatus;
//	}
//
//	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
//	@JoinColumn(name = "slot2ClassId")
//	public Class getSlot2Class() {
//		return slot2Class;
//	}
//
//	public void setSlot2Class(Class slot2Class) {
//		this.slot2Class = slot2Class;
//	}

	@OneToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "slot2ClassReservationId")
	public ClassReservation getSlot2ClassReservation() {
		return slot2ClassReservation;
	}

	public void setSlot2ClassReservation(ClassReservation slot2ClassReservation) {
		this.slot2ClassReservation = slot2ClassReservation;
	}

	public Integer getSlot2OccupiedNumber() {
		return slot2OccupiedNumber;
	}

	public void setSlot2OccupiedNumber(Integer slot2OccupiedNumber) {
		this.slot2OccupiedNumber = slot2OccupiedNumber;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "slot2ExperimentId")
	public Experiment getSlot2Experiment() {
		return slot2Experiment;
	}

	public void setSlot2Experiment(Experiment slot2Experiment) {
		this.slot2Experiment = slot2Experiment;
	}

	public Integer getSlot2ChapterNumber() {
		return slot2ChapterNumber;
	}

	public void setSlot2ChapterNumber(Integer slot2ChapterNumber) {
		this.slot2ChapterNumber = slot2ChapterNumber;
	}

	@OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, mappedBy = "labPlan")
	public Set<StudentReservation> getSlot2StudentReservations() {
		return slot2StudentReservations;
	}

	public void setSlot2StudentReservations(
			Set<StudentReservation> slot2StudentReservations) {
		this.slot2StudentReservations = slot2StudentReservations;
	}

	
	//slot3
	public SlotType getSlot3Type() {
		return slot3Type;
	}

	public void setSlot3Type(SlotType slot3Type) {
		this.slot3Type = slot3Type;
	}

	public OpenStatus getSlot3OpenStatus() {
		return slot3OpenStatus;
	}

	public void setSlot3OpenStatus(OpenStatus slot3OpenStatus) {
		this.slot3OpenStatus = slot3OpenStatus;
	}

//	public ApprovalStatus getSlot3ApprovalStatus() {
//		return slot3ApprovalStatus;
//	}
//
//	public void setSlot3ApprovalStatus(ApprovalStatus slot3ApprovalStatus) {
//		this.slot3ApprovalStatus = slot3ApprovalStatus;
//	}
//
//	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
//	@JoinColumn(name = "slot3ClassId")
//	public Class getSlot3Class() {
//		return slot3Class;
//	}
//
//	public void setSlot3Class(Class slot3Class) {
//		this.slot3Class = slot3Class;
//	}

	@OneToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "slot3ClassReservationId")
	public ClassReservation getSlot3ClassReservation() {
		return slot3ClassReservation;
	}

	public void setSlot3ClassReservation(ClassReservation slot3ClassReservation) {
		this.slot3ClassReservation = slot3ClassReservation;
	}

	public Integer getSlot3OccupiedNumber() {
		return slot3OccupiedNumber;
	}

	public void setSlot3OccupiedNumber(Integer slot3OccupiedNumber) {
		this.slot3OccupiedNumber = slot3OccupiedNumber;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "slot3ExperimentId")
	public Experiment getSlot3Experiment() {
		return slot3Experiment;
	}

	public void setSlot3Experiment(Experiment slot3Experiment) {
		this.slot3Experiment = slot3Experiment;
	}

	public Integer getSlot3ChapterNumber() {
		return slot3ChapterNumber;
	}

	public void setSlot3ChapterNumber(Integer slot3ChapterNumber) {
		this.slot3ChapterNumber = slot3ChapterNumber;
	}

	@OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, mappedBy = "labPlan")
	public Set<StudentReservation> getSlot3StudentReservations() {
		return slot3StudentReservations;
	}

	public void setSlot3StudentReservations(
			Set<StudentReservation> slot3StudentReservations) {
		this.slot3StudentReservations = slot3StudentReservations;
	}

	
	//slot4
	public SlotType getSlot4Type() {
		return slot4Type;
	}

	public void setSlot4Type(SlotType slot4Type) {
		this.slot4Type = slot4Type;
	}

	public OpenStatus getSlot4OpenStatus() {
		return slot4OpenStatus;
	}

	public void setSlot4OpenStatus(OpenStatus slot4OpenStatus) {
		this.slot4OpenStatus = slot4OpenStatus;
	}

//	public ApprovalStatus getSlot4ApprovalStatus() {
//		return slot4ApprovalStatus;
//	}
//
//	public void setSlot4ApprovalStatus(ApprovalStatus slot4ApprovalStatus) {
//		this.slot4ApprovalStatus = slot4ApprovalStatus;
//	}
//
//	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
//	@JoinColumn(name = "slot4ClassId")
//	public Class getSlot4Class() {
//		return slot4Class;
//	}
//
//	public void setSlot4Class(Class slot4Class) {
//		this.slot4Class = slot4Class;
//	}

	@OneToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "slot4ClassReservationId")
	public ClassReservation getSlot4ClassReservation() {
		return slot4ClassReservation;
	}

	public void setSlot4ClassReservation(ClassReservation slot4ClassReservation) {
		this.slot4ClassReservation = slot4ClassReservation;
	}

	public Integer getSlot4OccupiedNumber() {
		return slot4OccupiedNumber;
	}

	public void setSlot4OccupiedNumber(Integer slot4OccupiedNumber) {
		this.slot4OccupiedNumber = slot4OccupiedNumber;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "slot4ExperimentId")
	public Experiment getSlot4Experiment() {
		return slot4Experiment;
	}

	public void setSlot4Experiment(Experiment slot4Experiment) {
		this.slot4Experiment = slot4Experiment;
	}

	public Integer getSlot4ChapterNumber() {
		return slot4ChapterNumber;
	}

	public void setSlot4ChapterNumber(Integer slot4ChapterNumber) {
		this.slot4ChapterNumber = slot4ChapterNumber;
	}

	@OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, mappedBy = "labPlan")
	public Set<StudentReservation> getSlot4StudentReservations() {
		return slot4StudentReservations;
	}

	public void setSlot4StudentReservations(
			Set<StudentReservation> slot4StudentReservations) {
		this.slot4StudentReservations = slot4StudentReservations;
	}

	
	//slot5
	public SlotType getSlot5Type() {
		return slot5Type;
	}

	public void setSlot5Type(SlotType slot5Type) {
		this.slot5Type = slot5Type;
	}

	public OpenStatus getSlot5OpenStatus() {
		return slot5OpenStatus;
	}

	public void setSlot5OpenStatus(OpenStatus slot5OpenStatus) {
		this.slot5OpenStatus = slot5OpenStatus;
	}

//	public ApprovalStatus getSlot5ApprovalStatus() {
//		return slot5ApprovalStatus;
//	}
//
//	public void setSlot5ApprovalStatus(ApprovalStatus slot5ApprovalStatus) {
//		this.slot5ApprovalStatus = slot5ApprovalStatus;
//	}
//
//	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
//	@JoinColumn(name = "slot5ClassId")
//	public Class getSlot5Class() {
//		return slot5Class;
//	}
//
//	public void setSlot5Class(Class slot5Class) {
//		this.slot5Class = slot5Class;
//	}

	@OneToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "slot5ClassReservationId")
		public ClassReservation getSlot5ClassReservation() {
		return slot5ClassReservation;
	}

	public void setSlot5ClassReservation(ClassReservation slot5ClassReservation) {
		this.slot5ClassReservation = slot5ClassReservation;
	}

	public Integer getSlot5OccupiedNumber() {
		return slot5OccupiedNumber;
	}

	public void setSlot5OccupiedNumber(Integer slot5OccupiedNumber) {
		this.slot5OccupiedNumber = slot5OccupiedNumber;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "slot5ExperimentId")
	public Experiment getSlot5Experiment() {
		return slot5Experiment;
	}

	public void setSlot5Experiment(Experiment slot5Experiment) {
		this.slot5Experiment = slot5Experiment;
	}

	public Integer getSlot5ChapterNumber() {
		return slot5ChapterNumber;
	}

	public void setSlot5ChapterNumber(Integer slot5ChapterNumber) {
		this.slot5ChapterNumber = slot5ChapterNumber;
	}

	@OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, mappedBy = "labPlan")
	public Set<StudentReservation> getSlot5StudentReservations() {
		return slot5StudentReservations;
	}

	public void setSlot5StudentReservations(
			Set<StudentReservation> slot5StudentReservations) {
		this.slot5StudentReservations = slot5StudentReservations;
	}

	
	//slot6
	public SlotType getSlot6Type() {
		return slot6Type;
	}

	public void setSlot6Type(SlotType slot6Type) {
		this.slot6Type = slot6Type;
	}

	public OpenStatus getSlot6OpenStatus() {
		return slot6OpenStatus;
	}

	public void setSlot6OpenStatus(OpenStatus slot6OpenStatus) {
		this.slot6OpenStatus = slot6OpenStatus;
	}

//	public ApprovalStatus getSlot6ApprovalStatus() {
//		return slot6ApprovalStatus;
//	}
//
//	public void setSlot6ApprovalStatus(ApprovalStatus slot6ApprovalStatus) {
//		this.slot6ApprovalStatus = slot6ApprovalStatus;
//	}
//
//	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
//	@JoinColumn(name = "slot6ClassId")
//	public Class getSlot6Class() {
//		return slot6Class;
//	}
//
//	public void setSlot6Class(Class slot6Class) {
//		this.slot6Class = slot6Class;
//	}

	@OneToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "slot6ClassReservationId")
	public ClassReservation getSlot6ClassReservation() {
		return slot6ClassReservation;
	}

	public void setSlot6ClassReservation(ClassReservation slot6ClassReservation) {
		this.slot6ClassReservation = slot6ClassReservation;
	}

	public Integer getSlot6OccupiedNumber() {
		return slot6OccupiedNumber;
	}

	public void setSlot6OccupiedNumber(Integer slot6OccupiedNumber) {
		this.slot6OccupiedNumber = slot6OccupiedNumber;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "slot6ExperimentId")
	public Experiment getSlot6Experiment() {
		return slot6Experiment;
	}

	public void setSlot6Experiment(Experiment slot6Experiment) {
		this.slot6Experiment = slot6Experiment;
	}

	public Integer getSlot6ChapterNumber() {
		return slot6ChapterNumber;
	}

	public void setSlot6ChapterNumber(Integer slot6ChapterNumber) {
		this.slot6ChapterNumber = slot6ChapterNumber;
	}

	@OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, mappedBy = "labPlan")
	public Set<StudentReservation> getSlot6StudentReservations() {
		return slot6StudentReservations;
	}

	public void setSlot6StudentReservations(
			Set<StudentReservation> slot6StudentReservations) {
		this.slot6StudentReservations = slot6StudentReservations;
	}

	//slot7
	public SlotType getSlot7Type() {
		return slot7Type;
	}

	public void setSlot7Type(SlotType slot7Type) {
		this.slot7Type = slot7Type;
	}

	public OpenStatus getSlot7OpenStatus() {
		return slot7OpenStatus;
	}

	public void setSlot7OpenStatus(OpenStatus slot7OpenStatus) {
		this.slot7OpenStatus = slot7OpenStatus;
	}

	@OneToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "slot7ClassReservationId")
	public ClassReservation getSlot7ClassReservation() {
		return slot7ClassReservation;
	}

	public void setSlot7ClassReservation(ClassReservation slot7ClassReservation) {
		this.slot7ClassReservation = slot7ClassReservation;
	}

	public Integer getSlot7OccupiedNumber() {
		return slot7OccupiedNumber;
	}

	public void setSlot7OccupiedNumber(Integer slot7OccupiedNumber) {
		this.slot7OccupiedNumber = slot7OccupiedNumber;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "slot7ExperimentId")
	public Experiment getSlot7Experiment() {
		return slot7Experiment;
	}

	public void setSlot7Experiment(Experiment slot7Experiment) {
		this.slot7Experiment = slot7Experiment;
	}

	public Integer getSlot7ChapterNumber() {
		return slot7ChapterNumber;
	}

	public void setSlot7ChapterNumber(Integer slot7ChapterNumber) {
		this.slot7ChapterNumber = slot7ChapterNumber;
	}

	@OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, mappedBy = "labPlan")
	public Set<StudentReservation> getSlot7StudentReservations() {
		return slot7StudentReservations;
	}

	public void setSlot7StudentReservations(
			Set<StudentReservation> slot7StudentReservations) {
		this.slot7StudentReservations = slot7StudentReservations;
	}

	public Integer getSlot1SlotReservationMax() {
		return slot1SlotReservationMax;
	}

	public void setSlot1SlotReservationMax(Integer slot1SlotReservationMax) {
		this.slot1SlotReservationMax = slot1SlotReservationMax;
	}

	public Integer getSlot2SlotReservationMax() {
		return slot2SlotReservationMax;
	}

	public void setSlot2SlotReservationMax(Integer slot2SlotReservationMax) {
		this.slot2SlotReservationMax = slot2SlotReservationMax;
	}

	public Integer getSlot3SlotReservationMax() {
		return slot3SlotReservationMax;
	}

	public void setSlot3SlotReservationMax(Integer slot3SlotReservationMax) {
		this.slot3SlotReservationMax = slot3SlotReservationMax;
	}

	public Integer getSlot4SlotReservationMax() {
		return slot4SlotReservationMax;
	}

	public void setSlot4SlotReservationMax(Integer slot4SlotReservationMax) {
		this.slot4SlotReservationMax = slot4SlotReservationMax;
	}

	public Integer getSlot5SlotReservationMax() {
		return slot5SlotReservationMax;
	}

	public void setSlot5SlotReservationMax(Integer slot5SlotReservationMax) {
		this.slot5SlotReservationMax = slot5SlotReservationMax;
	}

	public Integer getSlot6SlotReservationMax() {
		return slot6SlotReservationMax;
	}

	public void setSlot6SlotReservationMax(Integer slot6SlotReservationMax) {
		this.slot6SlotReservationMax = slot6SlotReservationMax;
	}

	public Integer getSlot7SlotReservationMax() {
		return slot7SlotReservationMax;
	}

	public void setSlot7SlotReservationMax(Integer slot7SlotReservationMax) {
		this.slot7SlotReservationMax = slot7SlotReservationMax;
	}

	@OneToOne
	@JoinColumn(name = "slot1SlotReservationId")
	public SlotReservation getSlot1SlotReservation() {
		return slot1SlotReservation;
	}

	public void setSlot1SlotReservation(SlotReservation slot1SlotReservation) {
		this.slot1SlotReservation = slot1SlotReservation;
	}

	@OneToOne
	@JoinColumn(name = "slot2SlotReservationId")
	public SlotReservation getSlot2SlotReservation() {
		return slot2SlotReservation;
	}

	public void setSlot2SlotReservation(SlotReservation slot2SlotReservation) {
		this.slot2SlotReservation = slot2SlotReservation;
	}

	@OneToOne
	@JoinColumn(name = "slot3SlotReservationId")
	public SlotReservation getSlot3SlotReservation() {
		return slot3SlotReservation;
	}

	public void setSlot3SlotReservation(SlotReservation slot3SlotReservation) {
		this.slot3SlotReservation = slot3SlotReservation;
	}

	@OneToOne
	@JoinColumn(name = "slot4SlotReservationId")
	public SlotReservation getSlot4SlotReservation() {
		return slot4SlotReservation;
	}

	public void setSlot4SlotReservation(SlotReservation slot4SlotReservation) {
		this.slot4SlotReservation = slot4SlotReservation;
	}

	@OneToOne
	@JoinColumn(name = "slot5SlotReservationId")
	public SlotReservation getSlot5SlotReservation() {
		return slot5SlotReservation;
	}

	public void setSlot5SlotReservation(SlotReservation slot5SlotReservation) {
		this.slot5SlotReservation = slot5SlotReservation;
	}

	@OneToOne
	@JoinColumn(name = "slot6SlotReservationId")
	public SlotReservation getSlot6SlotReservation() {
		return slot6SlotReservation;
	}

	public void setSlot6SlotReservation(SlotReservation slot6SlotReservation) {
		this.slot6SlotReservation = slot6SlotReservation;
	}

	@OneToOne
	@JoinColumn(name = "slot7SlotReservationId")
	public SlotReservation getSlot7SlotReservation() {
		return slot7SlotReservation;
	}

	public void setSlot7SlotReservation(SlotReservation slot7SlotReservation) {
		this.slot7SlotReservation = slot7SlotReservation;
	}

	
	
//	//slot8
//	public SlotType getSlot8Type() {
//		return slot8Type;
//	}
//
//	public void setSlot8Type(SlotType slot8Type) {
//		this.slot8Type = slot8Type;
//	}
//
//	public OpenStatus getSlot8OpenStatus() {
//		return slot8OpenStatus;
//	}
//
//	public void setSlot8OpenStatus(OpenStatus slot8OpenStatus) {
//		this.slot8OpenStatus = slot8OpenStatus;
//	}
//
//	@OneToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
//	@JoinColumn(name = "slot8ClassReservationId")
//	public ClassReservation getSlot8ClassReservation() {
//		return slot8ClassReservation;
//	}
//
//	public void setSlot8ClassReservation(ClassReservation slot8ClassReservation) {
//		this.slot8ClassReservation = slot8ClassReservation;
//	}
//
//	public Integer getSlot8OccupiedNumber() {
//		return slot8OccupiedNumber;
//	}
//
//	public void setSlot8OccupiedNumber(Integer slot8OccupiedNumber) {
//		this.slot8OccupiedNumber = slot8OccupiedNumber;
//	}
//
//	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
//	@JoinColumn(name = "slot8ExperimentId")
//	public Experiment getSlot8Experiment() {
//		return slot8Experiment;
//	}
//
//	public void setSlot8Experiment(Experiment slot8Experiment) {
//		this.slot8Experiment = slot8Experiment;
//	}
//
//	public Integer getSlot8ChapterNumber() {
//		return slot8ChapterNumber;
//	}
//
//	public void setSlot8ChapterNumber(Integer slot8ChapterNumber) {
//		this.slot8ChapterNumber = slot8ChapterNumber;
//	}
//
//	@OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, mappedBy = "labPlan")
//	public Set<StudentReservation> getSlot8StudentReservations() {
//		return slot8StudentReservations;
//	}
//
//	public void setSlot8StudentReservations(
//			Set<StudentReservation> slot8StudentReservations) {
//		this.slot8StudentReservations = slot8StudentReservations;
//	}
//	
//	//slot9
//	public SlotType getSlot9Type() {
//		return slot9Type;
//	}
//
//	public void setSlot9Type(SlotType slot9Type) {
//		this.slot9Type = slot9Type;
//	}
//
//	public OpenStatus getSlot9OpenStatus() {
//		return slot9OpenStatus;
//	}
//
//	public void setSlot9OpenStatus(OpenStatus slot9OpenStatus) {
//		this.slot9OpenStatus = slot9OpenStatus;
//	}
//
//	@OneToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
//	@JoinColumn(name = "slot9ClassReservationId")
//	public ClassReservation getSlot9ClassReservation() {
//		return slot9ClassReservation;
//	}
//
//	public void setSlot9ClassReservation(ClassReservation slot9ClassReservation) {
//		this.slot9ClassReservation = slot9ClassReservation;
//	}
//
//	public Integer getSlot9OccupiedNumber() {
//		return slot9OccupiedNumber;
//	}
//
//	public void setSlot9OccupiedNumber(Integer slot9OccupiedNumber) {
//		this.slot9OccupiedNumber = slot9OccupiedNumber;
//	}
//
//	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
//	@JoinColumn(name = "slot9ExperimentId")
//	public Experiment getSlot9Experiment() {
//		return slot9Experiment;
//	}
//
//	public void setSlot9Experiment(Experiment slot9Experiment) {
//		this.slot9Experiment = slot9Experiment;
//	}
//
//	public Integer getSlot9ChapterNumber() {
//		return slot9ChapterNumber;
//	}
//
//	public void setSlot9ChapterNumber(Integer slot9ChapterNumber) {
//		this.slot9ChapterNumber = slot9ChapterNumber;
//	}
//
//	@OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, mappedBy = "labPlan")
//	public Set<StudentReservation> getSlot9StudentReservations() {
//		return slot9StudentReservations;
//	}
//
//	public void setSlot9StudentReservations(
//			Set<StudentReservation> slot9StudentReservations) {
//		this.slot9StudentReservations = slot9StudentReservations;
//	}
}
