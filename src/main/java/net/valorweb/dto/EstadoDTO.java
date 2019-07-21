package net.valorweb.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.valorweb.domain.Estado;

@Data
@NoArgsConstructor
public class EstadoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public EstadoDTO(Estado estado) {
		id = estado.getId();
		nome = estado.getNome();
	}
	
	private Integer id;
	
	private String nome;
	
	

}
