package com.prj.entity;

import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.prj.entity.Reservation.SlotNo;

@Entity
@Table(name = "slot")
public class Slot extends BaseEntity {
	private SlotNo slotNo;
	private Time startTime;
	private Time endTime;
	private Administrator updater;
	private boolean isActive;
	
	public SlotNo getSlotNo() {
		return slotNo;
	}
	public void setSlotNo(SlotNo slotNo) {
		this.slotNo = slotNo;
	}
	public Time getStartTime() {
		return startTime;
	}
	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}
	public Time getEndTime() {
		return endTime;
	}
	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}
	
	@ManyToOne
	@JoinColumn(name = "updaterId")
	public Administrator getUpdater() {
		return updater;
	}
	public void setUpdater(Administrator updater) {
		this.updater = updater;
	}
	public boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
}
