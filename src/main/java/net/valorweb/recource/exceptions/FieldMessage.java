package net.valorweb.recource.exceptions;

import java.io.Serializable;

public class FieldMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FieldMessage() {
		// TODO Auto-generated constructor stub
	}

	public FieldMessage(String fieldName, String message) {
		super();
		this.fieldName = fieldName;
		this.message = message;
	}

	private String fieldName;
	private String message;

	public String getFildName() {
		return fieldName;
	}

	public void setFildName(String fildName) {
		this.fieldName = fildName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
