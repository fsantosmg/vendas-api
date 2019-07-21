package net.valorweb.recource.exceptions;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class StandardError implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter
	private Long timeStamp;
	
	@Getter @Setter
	private Integer status;
	
	@Getter @Setter
	private String error;
	
	@Getter @Setter
	private String message;
	
	@Getter @Setter
	private String path;


}
