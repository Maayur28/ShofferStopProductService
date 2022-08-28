package com.prodservice.utils;

public enum ErrorMessages {

	AUTHENTICATION_FAILED("Invalid username or password"), USER_ALREADY_EXISTS("User already exists"),
	INVALID_REQUEST("Invalid request");

	private String errorMessage;

	ErrorMessages(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
