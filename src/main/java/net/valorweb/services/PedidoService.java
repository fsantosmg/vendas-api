package net.valorweb.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.valorweb.domain.Categoria;
import net.valorweb.domain.Cliente;
import net.valorweb.domain.ItemPedido;
import net.valorweb.domain.PagamentoComBoleto;
import net.valorweb.domain.Pedido;
import net.valorweb.domain.enums.EstadoPagamento;
import net.valorweb.repositories.ItemPedidoRepository;
import net.valorweb.repositories.PagamentoRepository;
import net.valorweb.repositories.PedidoRepository;
import net.valorweb.security.UserSecurity;
import net.valorweb.services.exception.AuthorizationExeption;
import net.valorweb.services.exception.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repository;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;

	public Pedido findById(Integer id) {
		Optional<Pedido> pedido = repository.findById(id);
		return pedido.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}

	@Transactional
	public Pedido insert( Pedido pedido) {
		pedido.setId(null);
		pedido.setCliente(clienteService.find(pedido.getCliente().getId()));
		pedido.setInstante(new Date());
		pedido.getPagamento().setEstadoPagamento(EstadoPagamento.PENDENTE);
		pedido.getPagamento().setPedido(pedido);
		if(pedido.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto =  (PagamentoComBoleto) pedido.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, pedido.getInstante());
		}
		
		pedido = repository.save(pedido);
		
		pagamentoRepository.save(pedido.getPagamento());
		
		for (ItemPedido itemPedido : pedido.getItens()) {
			itemPedido.setDesconto(0.0);
			itemPedido.setProduto(produtoService.findById(itemPedido.getProduto().getId()));
			itemPedido.setPreco(itemPedido.getProduto().getPreco());
			itemPedido.setPedido(pedido);
		}
		itemPedidoRepository.saveAll(pedido.getItens());
		
		emailService.sendOrderConfirmationHtmlEmail(pedido);
		return pedido;
	}
	
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		
		UserSecurity user = UserService.authenticated();
		if (user == null) {
			throw new AuthorizationExeption("Acesso negado");
		}
		Cliente cliente = clienteService.find(user.getId());

		return repository.findByCliente(cliente, PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy));
		

	}





}
