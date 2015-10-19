package com.prj.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.prj.entity.LabPlan.ApprovalStatus;

@Entity
@Table(name = "student_request")
public class StudentRequest extends BaseEntity{
	private ApprovalStatus approvalStatus;
}
