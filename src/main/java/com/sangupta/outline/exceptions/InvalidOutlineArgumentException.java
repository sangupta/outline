package com.sangupta.outline.exceptions;

public class InvalidOutlineArgumentException extends RuntimeException {

	/**
	 * Generated via Eclipse
	 */
	private static final long serialVersionUID = -1976856970473649140L;

	public final String argument;
	
	public final int positionIndex;
	
	public InvalidOutlineArgumentException(String argument, int positionIndex) {
		this.argument = argument;
		this.positionIndex = positionIndex;
	}
	
}
