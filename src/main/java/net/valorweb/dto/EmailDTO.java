package net.valorweb.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data

public class EmailDTO implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotEmpty(message="Preenchimento obrigatório")
	@Email(message="E-mail invalido")
	private String email;
}
