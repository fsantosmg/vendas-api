package net.valorweb.recource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.valorweb.domain.Categoria;
import net.valorweb.domain.Pedido;
import net.valorweb.domain.Produto;
import net.valorweb.dto.CategoriaDTO;
import net.valorweb.dto.ProdutoDTO;
import net.valorweb.recource.utils.Url;
import net.valorweb.services.ProdutoService;

@RestController
@RequestMapping(value = "produtos")
public class ProdutoResource {

	@Autowired
	ProdutoService service;

	@GetMapping("/{id}")
	public ResponseEntity<Produto> findPorId(@PathVariable Integer id) {

		return ResponseEntity.ok().body(service.findById(id));

	}

	@GetMapping
	public ResponseEntity<Page<ProdutoDTO>> findPage(
			@RequestParam(value = "nome", defaultValue = "") String nome,
			@RequestParam(value = "categorias", defaultValue = "") String categorias,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {

		
		Page<Produto> list = service.search(Url.decodeParam(nome), Url.decodeIntList(categorias), 
				
				page, linesPerPage, orderBy, direction);

		Page<ProdutoDTO> listDto = list.map(produto -> new ProdutoDTO(produto));

		return ResponseEntity.ok().body(listDto);

	}

}
