package net.valorweb.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.valorweb.domain.enums.Perfil;
import net.valorweb.domain.enums.TipoCliente;

@Entity
@EqualsAndHashCode(of = "id")
public class Cliente implements Serializable {
	

	
	public Cliente() {
		addPerfil(Perfil.CLIENTE);
	}

	public Cliente(String nome, String email, String cpfCnpj, TipoCliente tipo, String senha) {
		super();

		this.nome = nome;
		this.email = email;
		this.cpfCnpj = cpfCnpj;
		this.tipo = (tipo == null) ? null : tipo.getCod();
		this.senha = senha;
		addPerfil(Perfil.CLIENTE);
	}

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	@Setter
	private Integer id;

	@Getter
	@Setter
	private String nome;

	@Getter
	@Setter
	@Column(unique = true)
	private String email;

	@Getter
	@Setter
	private String cpfCnpj;

	private Integer tipo;

	@Getter
	@Setter
	@JsonIgnore
	private String senha;

	public TipoCliente getTipo() {
		return TipoCliente.toEnum(tipo);
	}

	public void setTipo(TipoCliente tipo) {
		this.tipo = tipo.getCod();
	}

	@Getter
	@Setter
	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
	private List<Endereco> enderecos = new ArrayList<>();

	@Getter
	@Setter
	@ElementCollection
	@CollectionTable(name = "TELEFONE")
	private Set<String> telefones = new HashSet<>();

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "PERFIS")
	private Set<Integer> perfis = new HashSet<>();

	@Getter
	@Setter
	@JsonIgnore
	@OneToMany(mappedBy = "cliente")
	private List<Pedido> pedidos = new ArrayList<>();

	public Set<Perfil> getPerfis() {
		return perfis.stream().map(perfil -> Perfil.toEnum(perfil)).collect(Collectors.toSet());
	}
	
	public void addPerfil( Perfil perfil) {
		perfis.add(perfil.getCod());
	}

}
