package com.jarno.imagedownloader.exceptions;

public class ImageCreationException extends BaseMyException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6782736686594249016L;

	public ImageCreationException() {
		super();
	}

	public ImageCreationException(String mistake) {
		super(mistake);
	}

	public ImageCreationException(Throwable e, String mistake) {
		super(e, mistake);
	}

	public ImageCreationException(Throwable e) {
		super(e);
	}
	
}
