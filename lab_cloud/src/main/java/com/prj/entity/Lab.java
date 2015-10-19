package com.prj.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "lab")
//@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property="@id")
//@JsonIdentityInfo(generator=JSOGGenerator.class)
public class Lab extends BaseEntity {
	public enum Status {
		OPEN,
		CLOSED
	}
	
	private String labNumber;
	private String labName;
	private String description;
	private Boolean isActive = true;
	private Integer capacity;
	private Integer machineNumber;
//	private Boolean usage = false;
	private Status status = Status.OPEN;
	private Administrator creator;
	private Administrator lastUpdater;
//	private Date creationDate;
	private Set<Experiment> experiments = new HashSet<Experiment>(0);
//	private Set<LabPlan> labPlans = new HashSet<LabPlan>(0);

	@Column(nullable = false, name="lab_number")
	public String getLabNumber() {
		return labNumber;
	}
	public void setLabNumber(String labNumber) {
		this.labNumber = labNumber;
	}
	
	@Column(nullable = false, name="labname")
	public String getLabName() {
		return labName;
	}
	public void setLabName(String labName) {
		this.labName = labName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Column(nullable = false)
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	@Column(nullable = false)
	public Integer getCapacity() {
		return capacity;
	}
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
	
	@Column(nullable = false)
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	
	@ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	@JoinTable(	name = "lab_experiment",
				joinColumns = {@JoinColumn(name = "labId")},
				inverseJoinColumns = {@JoinColumn(name = "experimentId")})
//	@JsonManagedReference
	public Set<Experiment> getExperiments() {
		return experiments;
	}
	public void setExperiments(Set<Experiment> experiments) {
		this.experiments = experiments;
	}
	
//	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "lab")
//	@JsonIgnore	
//	public Set<LabPlan> getLabPlans() {
//		return labPlans;
//	}
//	public void setLabPlans(Set<LabPlan> labPlans) {
//		this.labPlans = labPlans;
//	}
	public Integer getMachineNumber() {
		return machineNumber;
	}
	public void setMachineNumber(Integer machineNumber) {
		this.machineNumber = machineNumber;
	}
	
//	@Column(name = "usage_status")
//	public Boolean getUsage() {
//		return usage;
//	}
//	
//	public void setUsage(Boolean usage) {
//		this.usage = usage;
//	}
	
	@ManyToOne
	@JoinColumn(name = "creator")
	public Administrator getCreator() {
		return creator;
	}
	public void setCreator(Administrator creator) {
		this.creator = creator;
	}
	
	@ManyToOne
	@JoinColumn(name = "last_updated_by")
	public Administrator getLastUpdater() {
		return lastUpdater;
	}
	public void setLastUpdater(Administrator lastUpdater) {
		this.lastUpdater = lastUpdater;
	}
	
//	@Column(nullable=false)
//	@Type(type = "date")
//	public Date getCreationDate() {
//		return creationDate;
//	}
//	
//	public void setCreationDate(Date creationDate) {
//		this.creationDate = creationDate;
//	}
	

}
