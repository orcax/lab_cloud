package com.prj.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//@Subselect("select item.name, max(bid.amount), count(*) "
//	    + "from item "
//	    + "join bid on bid.item_id = item.id "
//	    + "group by item.name")
//	@Synchronize( {"item", "bid"} ) //tables impacted

@Entity
@Table(name = "experiment_status")
public class ExperimentStatus {
	private Long experimentId;
	private String experimentName;
	private Integer totalNumber;
	private Integer submittedNumber;
	private Integer completedNumber;
	private Integer classReservationCount;

	@Id
	public Long getExperimentId() {
		return experimentId;
	}
	public void setExperimentId(Long experimentId) {
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
