package com.prj.entity;

import java.sql.Date;

import javax.persistence.*;

import com.prj.util.CopyRequired;
import com.prj.util.PathEnum;

@MappedSuperclass
public class Reservation extends BaseEntity {

	public enum Status {
		PENDING, APPROVED, REJECTED, END
	}

	private String number;
	private Date applyDate;
	private Slot slot;
	private Experiment experiment;
	private Lab lab;
	private Status status;
	private Account reserver;

  private PathEnum.ReservationType type;

	@CopyRequired
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@ManyToOne
	@JoinColumn(name = "slot_id")
	public Slot getSlot() {
		return slot;
	}

	@CopyRequired
	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public void setSlot(Slot slot) {
		this.slot = slot;
	}

	@ManyToOne
	@JoinColumn(name = "experiment_id")
	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}

	@ManyToOne
	@JoinColumn(name = "lab_id")
	public Lab getLab() {
		return lab;
	}

	public void setLab(Lab lab) {
		this.lab = lab;
	}

	@Enumerated(value = EnumType.STRING)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@ManyToOne
	@JoinColumn(name = "reserver_id")
	public Account getReserver() {
		return reserver;
	}

	public void setReserver(Account reserver) {
		this.reserver = reserver;
	}

  @Transient
  public PathEnum.ReservationType getType(){
    return type;
  }
  public void setType(PathEnum.ReservationType type){
    this.type = type;
  }
}
