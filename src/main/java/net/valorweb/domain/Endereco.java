package net.valorweb.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Endereco implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	@Setter
	private Integer id;

	@NonNull
	@Getter
	@Setter
	private String logradouro;

	@NonNull
	@Getter
	@Setter
	private String numero;

	@NonNull
	@Getter
	@Setter
	private String complemento;

	@NonNull
	@Getter
	@Setter
	private String bairro;

	@NonNull
	@Getter
	@Setter
	private String cep;

	@NonNull
	@Getter
	@Setter
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="cliente_id")
	private Cliente cliente;

	@NonNull
	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name="cidade_id")
	private Cidade cidade;
}
