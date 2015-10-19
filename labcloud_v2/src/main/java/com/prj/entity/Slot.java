package com.prj.entity;

import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prj.util.CopyRequired;

@Entity
@Table(name = "slot")
public class Slot extends BaseEntity {

    // public enum SlotNo {
    // ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN
    // }

    private int     slotNo;
    private Time    startTime;
    private Time    endTime;
    private Account updater;
    private boolean active;
    private String  title;

    public Slot() {

    }

    public Slot(long id) {
        this.id = id;
    }

    @CopyRequired
    @Column(nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @CopyRequired
    @Column(name = "slot_no", unique = true, nullable = false)
    public int getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(int slotNo) {
        this.slotNo = slotNo;
    }

    @CopyRequired
    @Column(name = "start_time")
    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    @CopyRequired
    @Column(name = "end_time")
    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "updater_id")
    public Account getUpdater() {
        return updater;
    }

    public void setUpdater(Account updater) {
        this.updater = updater;
    }

    @Column(nullable = false)
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
