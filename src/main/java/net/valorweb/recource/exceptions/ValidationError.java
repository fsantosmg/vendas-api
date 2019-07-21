package net.valorweb.recource.exceptions;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ValidationError extends StandardError {

	private static final long serialVersionUID = 1L;
	private List<FieldMessage> errors = new ArrayList<>();

	public ValidationError(Long timeStamp, Integer status, String error, String message, String path) {
		super(timeStamp, status, error, message, path);
		this.errors = errors;
	}

	public List<FieldMessage> getErrors() {
		return errors;
	}

		
	public void addError(String fieldName, String message) {
		this.errors.add(new FieldMessage(fieldName, message));
	}


}
