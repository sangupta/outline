package com.sangupta.outline.exceptions;

public class OutlineInvalidArgumentException extends OutlineException {

	/**
	 * Generated via Eclipse
	 */
	private static final long serialVersionUID = -1976856970473649140L;

	public final String argument;
	
	public final int positionIndex;
	
	public OutlineInvalidArgumentException(String argument, int positionIndex) {
		this.argument = argument;
		this.positionIndex = positionIndex;
	}
	
}
