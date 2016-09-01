package com.sangupta.outline.exceptions;

public class OutlineRequiredOptionMissingException extends RuntimeException {

	/**
	 * Generated via Eclipse
	 */
	private static final long serialVersionUID = 1645262045227379596L;
	
	public final String argument;
	
	public OutlineRequiredOptionMissingException(String argument) {
		this.argument = argument;
	}

}
