package com.icrane.quickmode.http;

/**
 * 请求错误信息
 * 
 * @author gujiwen
 * 
 */
public enum HttpError {
	ERROR_NONE, ERROR_EXCEPTION, ERROR_STR;

	private String errorMessage;
	private Exception exception;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

}
