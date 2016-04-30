package com.jarno.imagedownloader.exceptions;

public class BaseMyException extends Exception {

	private String mistake;
	
	public BaseMyException(String mistake){
		super();
		this.mistake = mistake;
	}
	
	public BaseMyException() {
		this("");
	}
	
	public BaseMyException(Throwable e, String mistake) {
		super(e);
		this.mistake = mistake;
	}
	
	public BaseMyException(Throwable e) {
		this(e, "");
	}
	
	public String getError() {
		return mistake;
	}

}
