package com.prj.util;

import java.io.Serializable;

public enum ErrorCodeEnum implements Serializable{
	No_Error("No Error", 0),
	Unknown_Error("Unknown Error", 1),

	Account_Not_Exist("Account Not Exist", 2),
	Password_Wrong("Password Wrong", 3),
	Account_Exist("Account Exist", 4),
	Account_Not_Active("Account Not Active", 5),
	Token_Expired("Token Expired", 6),
	Token_Invalid("Token Invalid", 7),
	Access_Denied("Access Denied", 8), 
	Lab_Not_Exist("Lab Not Exist", 9), 
	Lab_Exist("Lab Exist", 10), 
	Course_Not_Exist("Course Not Exist", 11),
	Course_Exist("Course Exist", 12),
	Experiment_Not_Exist("Experiment Not Exist", 13),
	Experiment_Exist("Course Exist", 14),
	Lab_Not_Active("Lab Not Active", 15),
	Experiment_Not_Active("Experiment Not Active", 16),
	File_Creation_Error("File Creation Error", 17),
	Account_Character_Null("Account Character Null", 18), 
	Reach_Lab_Limit("Reach Lab Limit", 19),
	Search_Character_Wrong("Search Character Wrong", 20),
	Search_Criteria_Null("Search Criteria Null", 21),
	Page_Number_Invaild("Page Number Invaild", 22), 
	Lab_Not_Included("Lab Not Included", 23),
	Out_Of_Bound("Out Of Bound", 24),
	Experiment_Not_Included("Experiment Not Included", 25),
	File_Error("File Error", 26),
	Class_Exist("Class Exist", 27),
	Semester_Not_Exist("Semester Not Exist", 28),
	Teacher_Not_Exist("Teacher Not Exist", 29),
	Class_Not_Exist("Class Not Exist", 30),
	Date_Error("Date Error", 31),
	LabPlan_Not_Exist("LabPlan Not Exist", 32), 
	Students_Not_Added("Students Not Added", 33), 
	Reservation_Not_Exist("Reservation Not Exist", 34),
	LabPlan_Slot_Occupied, Slot_For_Student, Slot_For_Class, 
	ClassReservation_Not_Exist, 
	Already_Approved, 
	Already_Rejected, 
	Current_Semester_Not_Set, 
	Current_Semester_Not_Passed, Already_Current, Already_Passed,
	ApprovalStatus_Not_Pending, 
	File_Null, 
	Number_Exist, 
	StudentRecord_Not_Exist, 
	Reservation_Count_Out_Of_Range, 
	Experiment_Max_Student_Out_Of_Range, 
	Slot_Pending, Slot_Approved, Status_Cannot_Set, Slot_Occupied, Slot_Closed, 
	EXPDATA_FILE_REQUIRED, StudentRecord_Exist, Slot_Unavailable, Reservation_Exist, SlotReservation_Not_Exist, Slot_Full, Page_Parameter_Wrong, Slot_Not_Exist, Reservation_Expired, File_Not_Exist;
	
	private String label;
	private Integer code;
	
	ErrorCodeEnum() {}
	
	ErrorCodeEnum(String label, Integer code) {
		this.setLabel(label);
		this.setCode(code);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	@Override
	public String toString() {
		return code.toString();
	}
	
	public static ErrorCodeEnum parse(int code) {
		for (ErrorCodeEnum theEnum : ErrorCodeEnum.values()) {
			if (theEnum.getCode() == code) {
				return theEnum;
			}
		}
		return No_Error;
	}
}
