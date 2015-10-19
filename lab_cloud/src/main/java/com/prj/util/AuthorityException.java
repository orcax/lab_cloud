package com.prj.util;

public class AuthorityException extends RuntimeException {
	private static final long serialVersionUID = 5360637098284757279L;
	
	private ErrorCodeEnum errorCode;

	
	public AuthorityException(ErrorCodeEnum errorCode) {
		super();
		this.errorCode = errorCode;
	}

	public ErrorCodeEnum getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCodeEnum errorCode) {
		this.errorCode = errorCode;
	}
}
