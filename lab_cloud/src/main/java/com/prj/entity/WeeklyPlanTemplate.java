package com.prj.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.prj.entity.LabPlan.SlotType;
import com.prj.entity.LabPlan.Weekday;

@Entity
@Table(name = "weekly_plan_template")
public class WeeklyPlanTemplate{
	public enum Status {
		OPEN,
		CLOSED
	}
	
	private Weekday weekday;
	private SlotType slot1Type;
	private Status slot1Status;
	private SlotType slot2Type;
	private Status slot2Status;
	private SlotType slot3Type;
	private Status slot3Status;
	private SlotType slot4Type;
	private Status slot4Status;
	private SlotType slot5Type;
	private Status slot5Status;
	private SlotType slot6Type;
	private Status slot6Status;
	private SlotType slot7Type;
	private Status slot7Status;
//	private SlotType slot8Type;
//	private Status slot8Status;
//	private SlotType slot9Type;
//	private Status slot9Status;

	@Id
	public Weekday getWeekday() {
		return weekday;
	}
	public void setWeekday(Weekday weekday) {
		this.weekday = weekday;
	}
	public SlotType getSlot1Type() {
		return slot1Type;
	}
	public void setSlot1Type(SlotType slot1Type) {
		this.slot1Type = slot1Type;
	}
	public Status getSlot1Status() {
		return slot1Status;
	}
	public void setSlot1Status(Status slot1Status) {
		this.slot1Status = slot1Status;
	}
	public SlotType getSlot2Type() {
		return slot2Type;
	}
	public void setSlot2Type(SlotType slot2Type) {
		this.slot2Type = slot2Type;
	}
	public Status getSlot2Status() {
		return slot2Status;
	}
	public void setSlot2Status(Status slot2Status) {
		this.slot2Status = slot2Status;
	}
	public SlotType getSlot3Type() {
		return slot3Type;
	}
	public void setSlot3Type(SlotType slot3Type) {
		this.slot3Type = slot3Type;
	}
	public Status getSlot3Status() {
		return slot3Status;
	}
	public void setSlot3Status(Status slot3Status) {
		this.slot3Status = slot3Status;
	}
	public SlotType getSlot4Type() {
		return slot4Type;
	}
	public void setSlot4Type(SlotType slot4Type) {
		this.slot4Type = slot4Type;
	}
	public Status getSlot4Status() {
		return slot4Status;
	}
	public void setSlot4Status(Status slot4Status) {
		this.slot4Status = slot4Status;
	}
	public SlotType getSlot5Type() {
		return slot5Type;
	}
	public void setSlot5Type(SlotType slot5Type) {
		this.slot5Type = slot5Type;
	}
	public Status getSlot5Status() {
		return slot5Status;
	}
	public void setSlot5Status(Status slot5Status) {
		this.slot5Status = slot5Status;
	}
	public SlotType getSlot6Type() {
		return slot6Type;
	}
	public void setSlot6Type(SlotType slot6Type) {
		this.slot6Type = slot6Type;
	}
	public Status getSlot6Status() {
		return slot6Status;
	}
	public void setSlot6Status(Status slot6Status) {
		this.slot6Status = slot6Status;
	}
	public SlotType getSlot7Type() {
		return slot7Type;
	}
	public void setSlot7Type(SlotType slot7Type) {
		this.slot7Type = slot7Type;
	}
	public Status getSlot7Status() {
		return slot7Status;
	}
	public void setSlot7Status(Status slot7Status) {
		this.slot7Status = slot7Status;
	}
//	public SlotType getSlot8Type() {
//		return slot8Type;
//	}
//	public void setSlot8Type(SlotType slot8Type) {
//		this.slot8Type = slot8Type;
//	}
//	public Status getSlot8Status() {
//		return slot8Status;
//	}
//	public void setSlot8Status(Status slot8Status) {
//		this.slot8Status = slot8Status;
//	}
//	public SlotType getSlot9Type() {
//		return slot9Type;
//	}
//	public void setSlot9Type(SlotType slot9Type) {
//		this.slot9Type = slot9Type;
//	}
//	public Status getSlot9Status() {
//		return slot9Status;
//	}
//	public void setSlot9Status(Status slot9Status) {
//		this.slot9Status = slot9Status;
//	}
}
