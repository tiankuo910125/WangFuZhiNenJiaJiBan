package com.demo.smarthome.base.exception;

public class ServerException extends Exception {
	public ServerException() {
	}

	public ServerException(String detailMessage) {
		super(detailMessage);
	}

	public ServerException(Throwable throwable) {
		super(throwable);
	}

	public ServerException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

}
