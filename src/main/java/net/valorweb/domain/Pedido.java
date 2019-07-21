package net.valorweb.domain;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonFormat;

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
public class Pedido implements Serializable {
	
	
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
	@JsonFormat(pattern="dd/MM/yyyy HH:mm")
	private Date instante;
	

	@Getter
	@Setter
	@OneToOne(cascade=CascadeType.ALL, mappedBy="pedido")
	private Pagamento pagamento;
	
	@NonNull
	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name="cliente_id")
	private Cliente cliente;
	
	@NonNull
	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name="endereco_de_entrega_id")
	private Endereco enderecoDeEntrega;
	
	@Getter
	@Setter
	@OneToMany(mappedBy="id.pedido")
	private Set<ItemPedido> itens = new HashSet<>();
	
	
	public Double getValorTotal() {
		
		Double soma = 0.0;
		for (ItemPedido itemPedido : itens) {
			soma = soma + itemPedido.getSubTotal();
		}
		return soma;
	}


	@Override
	public String toString() {
		NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		StringBuilder builder = new StringBuilder();
		builder.append("Pedido número: ");
		builder.append(id);
		builder.append(", Instante: ");
		builder.append(sdf.format(instante));

		builder.append(", Cliente: ");
		builder.append(cliente.getNome());
		builder.append(", Situação do pagamento: ");
		builder.append(pagamento.getEstadoPagamento().getDescricao());
		builder.append("\nDetalhes: \n");
		
		for(ItemPedido itemPedido : itens) {
			builder.append(itemPedido.toString());
		}
		
		builder.append("Valor total: ");
		builder.append(nf.format(getValorTotal()));
	

		return builder.toString();
	}

	
	
	
}
