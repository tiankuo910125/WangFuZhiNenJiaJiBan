package com.demo.smarthome.base.exception;

public class NetException extends Exception {

	public NetException() {
	}

	public NetException(String detailMessage) {
		super(detailMessage);
	}

	public NetException(Throwable throwable) {
		super(throwable);
	}

	public NetException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

}
