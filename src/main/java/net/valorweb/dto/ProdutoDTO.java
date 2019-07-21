package net.valorweb.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.valorweb.domain.Produto;

@NoArgsConstructor
public class ProdutoDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ProdutoDTO(Produto produto) {
		this.id = produto.getId();
		this.nome = produto.getNome();
		this.preco = produto.getPreco();
	}

	@Getter
	@Setter
	private Integer id;


	@Getter
	@Setter
	private String nome;


	@Getter
	@Setter
	private Double preco;

}
