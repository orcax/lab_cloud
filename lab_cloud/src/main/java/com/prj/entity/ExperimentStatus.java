package com.prj.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "experiment_status")
public class ExperimentStatus {
	private Integer experimentId;
	private String experimentName;
	private Integer totalNumber;
	private Integer submittedNumber;
	private Integer completedNumber;
	private Integer classReservationCount;
	
	@Id
	public Integer getExperimentId() {
		return experimentId;
	}
	public void setExperimentId(Integer experimentId) {
		this.experimentId = experimentId;
	}
	public String getExperimentName() {
		return experimentName;
	}
	public void setExperimentName(String experimentName) {
		this.experimentName = experimentName;
	}
	public Integer getTotalNumber() {
		return totalNumber;
	}
	public void setTotalNumber(Integer totalNumber) {
		this.totalNumber = totalNumber;
	}
	public Integer getSubmittedNumber() {
		return submittedNumber;
	}
	public void setSubmittedNumber(Integer submittedNumber) {
		this.submittedNumber = submittedNumber;
	}
	public Integer getCompletedNumber() {
		return completedNumber;
	}
	public void setCompletedNumber(Integer completedNumber) {
		this.completedNumber = completedNumber;
	}
	public Integer getClassReservationCount() {
		return classReservationCount;
	}
	public void setClassReservationCount(Integer classReservationCount) {
		this.classReservationCount = classReservationCount;
	}
}
