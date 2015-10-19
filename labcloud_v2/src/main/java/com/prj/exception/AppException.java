package com.prj.exception;

import javax.servlet.http.HttpServletResponse;

import com.prj.util.ErrorCode;


public class AppException extends RuntimeException {
	
	private static final long serialVersionUID = 4925495525292795163L;

	private int statusCode;
	private ErrorCode errorCode;
	
	public AppException(int statusCode, ErrorCode errorCode, String message) {
		super(message);
		this.statusCode = statusCode;
		this.errorCode = errorCode;
	}
	
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public static AppException newInstanceOfNotFoundException(String entity) {
		return new AppException(HttpServletResponse.SC_NOT_FOUND, ErrorCode.NOT_FOUND, entity);
	}
	
	public static AppException newInstanceOfDuplicationException(String property) {
		return new AppException(HttpServletResponse.SC_CONFLICT, ErrorCode.DUPLICATION, property);
	}
	
	public static AppException newInstanceOfWrongParameterException() {
		return new AppException(HttpServletResponse.SC_BAD_REQUEST,
				ErrorCode.WRONG_PARAM, "Parameters don't meet restrictions.");
	}
	
	public static AppException newInstanceOfForbiddenException(String accountName) {
		return new AppException(HttpServletResponse.SC_FORBIDDEN, 
				ErrorCode.FORBIDDEN, accountName+" is not Onwer");
	}
	
	public static AppException newInstanceOfPasswordException() {
		return new AppException(418, 
				ErrorCode.WRONG_PASSWD, "Wrong password");
	}
	
	public static AppException newInstanceOfInternalException(String msg) {
		return new AppException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
				ErrorCode.INTERNAL_ERROR, msg);
	}
}
