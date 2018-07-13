package com.syscom.xls_tool.exception;

// Make this class abstract so that developers are forced to create
// suitable exception types only
public abstract class BaseException extends Exception {
	/** */
	private static final long serialVersionUID = 1L;
	
	// Each exception message will be hold here
	private String message;

	public BaseException(String msg) {
		this.message = msg;
	}

	// Message can be retrieved using this accessor method
	public String getMessage() {
		return message;
	}
}