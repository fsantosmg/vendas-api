package net.valorweb.domain;

import java.util.Date;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.valorweb.domain.enums.EstadoPagamento;

@Entity
@NoArgsConstructor
@JsonTypeName("pagamentoComBoleto")
public class PagamentoComBoleto extends Pagamento {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PagamentoComBoleto( EstadoPagamento estado, Pedido pedido, Date dataVencimento, Date dataPagamento) {
		super(estado, pedido);

		this.dataPagamento = dataPagamento;
		this.dataVencimento = dataVencimento;

	}

	@Getter
	@Setter
	@JsonFormat(pattern="dd/MM/yyyy")
	private Date dataVencimento;

	@Getter
	@Setter
	@JsonFormat(pattern="dd/MM/yyyy")
	private Date dataPagamento;
	
	

}
