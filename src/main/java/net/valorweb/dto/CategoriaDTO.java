package net.valorweb.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.Data;
import net.valorweb.domain.Categoria;

@Data
public class CategoriaDTO implements Serializable {

	public CategoriaDTO() {

	}

	public CategoriaDTO(Categoria categoria) {
		id = categoria.getId();
		nome = categoria.getNome();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

	@NotEmpty(message="Preenchimento obrigatório")
	@Length(min=5, max=80, message="O tamanho dece ser entre 5 e 80 caracteres")
	private String nome;

}
