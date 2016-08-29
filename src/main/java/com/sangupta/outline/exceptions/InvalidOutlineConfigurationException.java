package com.sangupta.outline.exceptions;

public class InvalidOutlineConfigurationException extends RuntimeException {

	/**
	 * Generated via Eclipse
	 */
	private static final long serialVersionUID = 8565644830649924353L;
	
	public final String className;
	
	public final String field;
	
	public final String message;
	
	public InvalidOutlineConfigurationException(String className, String field, String message) {
		this.className = className;
		this.field = field;
		this.message = message;
	}

}
