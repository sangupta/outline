package com.sangupta.outline.exceptions;

public class OutlineInvalidConfigurationException extends OutlineException {

	/**
	 * Generated via Eclipse
	 */
	private static final long serialVersionUID = 8565644830649924353L;
	
	public final String className;
	
	public final String field;
	
	public final String message;
	
	public OutlineInvalidConfigurationException(String className, String field, String message) {
		this.className = className;
		this.field = field;
		this.message = message;
	}

}
