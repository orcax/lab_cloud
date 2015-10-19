package com.prj.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prj.util.CopyRequired;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lab")
public class Lab extends BaseEntity {
	public enum Status {
		OPEN, CLOSED
	}

	private String number;
	private String name;
	private String description;
	private boolean active = true;
	private int capacity;
	private int machineCount;
	private Status status = Status.OPEN;
	private Account creator;
	private Account updater;
	private Set<Experiment> experiments = new HashSet<Experiment>(0);
	
	public Lab(){
	}
	
	public Lab(long id){
	    this.id = id;
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

	public void setName(String labName) {
		this.name = labName;
	}

	@CopyRequired
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(nullable = false)
	public Boolean isActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@CopyRequired
	@Column(nullable = false)
	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@ManyToMany
	@JoinTable(name = "lab_experiment", joinColumns = { @JoinColumn(name = "lab_id") }, inverseJoinColumns = { @JoinColumn(name = "experiment_id") })
	@JsonIgnore
	public Set<Experiment> getExperiments() {
		return experiments;
	}

	public void setExperiments(Set<Experiment> experiments) {
		this.experiments = experiments;
	}

	@CopyRequired
	@Column(name = "machine_count")
	public int getMachineCount() {
		return machineCount;
	}

	public void setMachineCount(int machineCount) {
		this.machineCount = machineCount;
	}

	@CopyRequired(update = false, create = false)
	@ManyToOne
	@JoinColumn(name = "creator_id")
	public Account getCreator() {
		return creator;
	}

	public void setCreator(Account creator) {
		this.creator = creator;
	}

	@ManyToOne
	@JoinColumn(name = "updater_id")
	public Account getUpdater() {
		return updater;
	}

	public void setUpdater(Account updater) {
		this.updater = updater;
	}
}
