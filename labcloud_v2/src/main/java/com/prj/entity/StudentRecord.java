package com.prj.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prj.util.CopyRequired;

@Entity
@Table(name = "student_record")
public class StudentRecord extends BaseEntity {
  private Clazz clazz;
  private Account student;
  private Experiment experiment;
  private Status status;
  private int experimentRecord;
  private String experimentComment;
  private Set<FileRecord> fileRecords;
  private Machine machine;
  private Date date;
  private Slot slot;
  private Lab lab;

  @ManyToOne
  @JoinColumn(name = "class_id")
  @CopyRequired(update = false)
  public Clazz getClazz() {
    return clazz;
  }

  public void setClazz(Clazz clazz) {
    this.clazz = clazz;
  }

  @ManyToOne
  @JoinColumn(name = "student_id")
  @CopyRequired(update = false)
  public Account getStudent() {
    return student;
  }

  public void setStudent(Account student) {
    this.student = student;
  }

  @ManyToOne
  @JoinColumn(name = "experiment_id")
  @CopyRequired(update = false)
  public Experiment getExperiment() {
    return experiment;
  }

  public void setExperiment(Experiment experiment) {
    this.experiment = experiment;
  }

  @Enumerated(value = EnumType.STRING)
  @CopyRequired(update = false)
  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  @Column(name = "experiment_record")
  @CopyRequired
  public int getExperimentRecord() {
    return experimentRecord;
  }

  public void setExperimentRecord(int experimentRecord) {
    this.experimentRecord = experimentRecord;
  }

  @Column(name = "experiment_comment")
  @CopyRequired
  public String getExperimentComment() {
    return experimentComment;
  }

  public void setExperimentComment(String experimentComment) {
    this.experimentComment = experimentComment;
  }

  @OneToMany(mappedBy = "studentRecord", fetch = FetchType.EAGER)
  @JsonIgnore
  public Set<FileRecord> getFileRecords() {
    return fileRecords;
  }

  public void setFileRecords(Set<FileRecord> fileRecords) {
    this.fileRecords = fileRecords;
  }

  @CopyRequired
  @ManyToOne
  @JoinColumn(name = "machine_id")
  public Machine getMachine() {
    return machine;
  }

  public void setMachine(Machine machine) {
    this.machine = machine;
  }

  @CopyRequired
  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  @CopyRequired
  @ManyToOne
  @JoinColumn(name = "slot_id")
  public Slot getSlot() {
    return slot;
  }

  public void setSlot(Slot slot) {
    this.slot = slot;
  }

  @CopyRequired
  @ManyToOne
  @JoinColumn(name = "lab_id")
  public Lab getLab() {
    return lab;
  }

  public void setLab(Lab lab) {
    this.lab = lab;
  }

  public enum Status {
    NOT_STARTED,
    IN_PROGRESS, SUBMITTED, COMPLETED
  }
}
