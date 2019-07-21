package net.valorweb.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.valorweb.domain.Cidade;

@Data
@NoArgsConstructor
public class CidadeDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String nome;
	
	public CidadeDTO(Cidade cidade) {
		this.id = cidade.getId();
		this.nome = cidade.getNome();
	}
	
	

}
