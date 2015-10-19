package com.prj.util;

public class PathEnum {
	enum AccountRole {
		ANY,
		ADMINISTRATOR,
		ALL_TEACHER, NOR_TEACHER, LAB_TEACHER,
		STUDENT
	}
	
	public enum ReservationType {
		classReservation, studentReservation
	}
	
	public enum ReservationStatus {
		ALL,
		PENDING, APPROVED, REJECTED
	}
	
}
