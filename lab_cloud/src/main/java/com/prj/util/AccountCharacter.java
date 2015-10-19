package com.prj.util;

public enum AccountCharacter {
	ANY("any"),
	ADMINISTRATOR("administrator"),
	ALL_TEACHER("all_teacher"),
	TEACHER("teacher"),
	LAB_TEACHER("lab_teacher"),
	STUDENT("student");
	
	private String label;
	
	AccountCharacter() {}
	
	AccountCharacter(String label) {
		this.setLabel(label);
	}

	public String getLabel() {
		return label;
	}

	public String getUpperCaseLabel() {
		return label.toUpperCase();
	}
	
	public String getLowerCaseLabel() {
		return label.toLowerCase();
	}
	
	public String getCapitalizedLabel() {
		if (label.length() > 1) {
			return label.substring(0, 1).toUpperCase() + label.substring(1);
		}
		if (label.length() == 1){
			return label.toUpperCase();
		}
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
}
