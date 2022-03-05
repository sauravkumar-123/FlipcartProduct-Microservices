package com.flipcart.ExceptionHandle;

import org.springframework.http.HttpStatus;

public class FlipcartProductTypeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public HttpStatus httpStatus=null;
	
	public FlipcartProductTypeException() {
		super();
	}

	public FlipcartProductTypeException(String message, Throwable cause, boolean enableSuppression,boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FlipcartProductTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public FlipcartProductTypeException(String message) {
		super(message);
	}
	
	public FlipcartProductTypeException(String message,HttpStatus httpStatus) {
		super(message);
		this.httpStatus=httpStatus;
	}

	public FlipcartProductTypeException(Throwable cause) {
		super(cause);
	}
	
}
