package com.prj.entity;

import java.sql.Date;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prj.util.CopyRequired;

@Entity
@Table(name = "semester")
public class Semester extends BaseEntity {
	public enum Status {
		FUTURE,
		CURRENT, 
		PASSED
	}
	
	private String name;
	private Date startDate;
	private Date endDate;
	private Status status = Status.FUTURE;
	private Account updater;
	
	// Transient
	private Date currentDate;
	private Integer weekIndex;
	private Integer totalWeekNumber;
	
	@CopyRequired
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@CopyRequired
	@Column(name = "start_date", nullable = false)
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@CopyRequired
	@Column(name = "end_date", nullable = false)
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "update_id")
	public Account getUpdater() {
		return updater;
	}
	public void setUpdater(Account updater) {
		this.updater = updater;
	}
	
	@Transient
	public Date getCurrentDate() {
		return currentDate;
	}
	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}
	@Transient
	public Integer getWeekIndex() {
		return weekIndex;
	}
	public void setWeekIndex(Integer weekIndex) {
		this.weekIndex = weekIndex;
	}
	@Transient
	public Integer getTotalWeekNumber() {
		return totalWeekNumber;
	}
	public void setTotalWeekNumber(Integer totalWeekNumber) {
		this.totalWeekNumber = totalWeekNumber;
	}
	
	public Semester generateWeekInfo() {
		Calendar cal = Calendar.getInstance();
		this.currentDate = new Date(cal.getTime().getTime());
		this.totalWeekNumber = dateDiffWeekCnt(this.startDate, this.endDate);
		this.weekIndex = dateDiffWeekCnt(this.startDate, this.currentDate);
		return this;
	}
	
	private static int dateDiffWeekCnt(Date start, Date end) {
		Calendar startCal = getDateCal(start);
		Calendar endCal = getDateCal(end);
		long dayCnt = (endCal.getTimeInMillis() - startCal.getTimeInMillis())/86400000L + 1;
		int startIndex = day2index(startCal.get(Calendar.DAY_OF_WEEK));
		int endIndex = day2index(endCal.get(Calendar.DAY_OF_WEEK));
		if (startCal.get(Calendar.YEAR) == endCal.get(Calendar.YEAR)
				&& startCal.get(Calendar.WEEK_OF_YEAR) == endCal.get(Calendar.WEEK_OF_YEAR))
			return 1;
		else 
			return (int) ((dayCnt - (7-startIndex+1) - endIndex) / 7 + 2);
	}
	
	private static Calendar getDateCal(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.get(Calendar.DAY_OF_WEEK);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}
	
	private static int day2index(int dayOfWeek) {
		return (dayOfWeek+5)%7+1;
	}
}
