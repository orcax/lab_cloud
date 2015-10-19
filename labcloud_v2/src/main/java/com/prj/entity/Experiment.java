package com.prj.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prj.util.CopyRequired;

@Entity
@Table(name = "experiment")
public class Experiment extends BaseEntity {

	private String number;
	private String name;
	private int minGrpStuCnt;
	private int maxGrpStuCnt;
	private String virtualExpLink;
	private boolean active = true;
	private Set<Lab> labs = new HashSet<Lab>();

	@ManyToMany
	@JoinTable(name = "lab_experiment", joinColumns = { @JoinColumn(name = "experiment_id") }, inverseJoinColumns = { @JoinColumn(name = "lab_id") })
	@JsonIgnore
	public Set<Lab> getLabs() {
		return labs;
	}

	public void setLabs(Set<Lab> labs) {
		this.labs = labs;
	}

	@CopyRequired
	@Column(nullable = false)
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@CopyRequired
	@Column(nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@CopyRequired
	@Column(name = "min_grp_stu_cnt", nullable = false)
	public int getMinGrpStuCnt() {
		return minGrpStuCnt;
	}

	public void setMinGrpStuCnt(int minGrpStuCnt) {
		this.minGrpStuCnt = minGrpStuCnt;
	}

	@CopyRequired
	@Column(name = "max_grp_stu_cnt", nullable = false)
	public int getMaxGrpStuCnt() {
		return maxGrpStuCnt;
	}

	public void setMaxGrpStuCnt(int maxGrpStuCnt) {
		this.maxGrpStuCnt = maxGrpStuCnt;
	}

	@Column(nullable = false)
	public boolean isActive() {
		return active;
	}

	@Column(nullable = false)
	public void setActive(boolean active) {
		this.active = active;
	}
	
	@CopyRequired
	@Column(nullable = true)
	public String getVirtualExpLink() {
		return virtualExpLink;
	}

	public void setVirtualExpLink(String virtualExpLink) {
		this.virtualExpLink = virtualExpLink;
	}

}
