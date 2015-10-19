package com.prj.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "experiment")
//@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property="@id")
//@JsonIdentityInfo(generator=JSOGGenerator.class)
public class Experiment extends BaseEntity {
	
	private String experimentNumber;
	private String experimentName;
	private Integer minimumStudent;
	private Integer maximumStudent;
	private Boolean isActive = true;
	private Set<CourseExperiment> courseExperiment = new HashSet<CourseExperiment>(0);
	private Set<Lab> labs = new HashSet<Lab>(0);
	//private ExperimentLab experimentLab;
	
	@Column(nullable = false)
	public String getExperimentNumber() {
		return experimentNumber;
	}
	public void setExperimentNumber(String experimentNumber) {
		this.experimentNumber = experimentNumber;
	}
	@Column(nullable = false)
	public String getExperimentName() {
		return experimentName;
	}
	public void setExperimentName(String experimentName) {
		this.experimentName = experimentName;
	}
	
	@Column(nullable = false)
	public Integer getMinimumStudent() {
		return minimumStudent;
	}
	public void setMinimumStudent(Integer minimumStudent) {
		this.minimumStudent = minimumStudent;
	}
	
	@Column(nullable = false)
	public Integer getMaximumStudent() {
		return maximumStudent;
	}
	public void setMaximumStudent(Integer maximumStudent) {
		this.maximumStudent = maximumStudent;
	}
	@Column(nullable = false)
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER,mappedBy = "experiment")
//	@JsonIgnore
	public Set<CourseExperiment> getCourseExperiment() {
		return courseExperiment;
	}
	public void setCourseExperiment(Set<CourseExperiment> courseExperiment) {
		this.courseExperiment = courseExperiment;
	}
	
	@ManyToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, mappedBy = "experiments")
//	@JsonIgnore	
//	@JsonBackReference
	public Set<Lab> getLabs() {
		return labs;
	}
	public void setLabs(Set<Lab> labs) {
		this.labs = labs;
	}
	
//	@OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
//	@JsonIgnore	
//	public ExperimentLab getExperimentLab() {
//		return experimentLab;
//	}
//	public void setExperimentLab(ExperimentLab experimentLab) {
//		this.experimentLab = experimentLab;
//	}
}
