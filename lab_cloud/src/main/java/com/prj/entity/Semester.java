package com.prj.entity;

import java.util.Date;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "semester")
public class Semester extends BaseEntity {
	public enum Status {
		CURRENT,
		PASSED,
		FUTURE
	}
	private String semesterName;
	private Date startDate;
	private Date endDate;
	private Status status;
	private Administrator updater;
	private Set<Class> classes = new HashSet<Class>(0);
	
	private Date currentDate;
	private Integer weekIndex;
	private Integer totalWeekNumber;
	
	public String getSemesterName() {
		return semesterName;
	}
	public void setSemesterName(String semesterName) {
		this.semesterName = semesterName;
	}
	
	@Column(nullable = false)
	@Type(type = "date")
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@Column(nullable = false)
	@Type(type = "date")
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "update_id")
	public Administrator getUpdater() {
		return updater;
	}
	public void setUpdater(Administrator updater) {
		this.updater = updater;
	}
	
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "semester")
	public Set<Class> getClasses() {
		return classes;
	}
	public void setClasses(Set<Class> classes) {
		this.classes = classes;
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
		this.currentDate = cal.getTime();
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
