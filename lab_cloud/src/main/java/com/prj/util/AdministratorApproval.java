package com.prj.util;

import java.util.List;

public class AdministratorApproval {
	public enum ApprovalResult {
		APPROVED,
		REJECTED
	}
	private ApprovalResult approvalResult;
	private List<Integer> labTeaIds;
	
	public ApprovalResult getApprovalResult() {
		return approvalResult;
	}
	public void setApprovalResult(ApprovalResult approvalResult) {
		this.approvalResult = approvalResult;
	}
	public List<Integer> getLabTeaIds() {
		return labTeaIds;
	}
	public void setLabTeaIds(List<Integer> labTeaIds) {
		this.labTeaIds = labTeaIds;
	}
}
