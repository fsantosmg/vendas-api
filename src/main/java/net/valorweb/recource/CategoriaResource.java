package net.valorweb.recource;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import net.valorweb.domain.Categoria;
import net.valorweb.dto.CategoriaDTO;
import net.valorweb.services.CategoriaService;

@RestController
@RequestMapping(value = "categorias")
public class CategoriaResource {

	@Autowired
	CategoriaService service;

	@GetMapping
	public ResponseEntity<List<CategoriaDTO>> listAll() {

		List<Categoria> list = service.findAll();

		List<CategoriaDTO> listDto = list.stream().map(categoria -> new CategoriaDTO(categoria))
				.collect(Collectors.toList());

		return ResponseEntity.ok().body(listDto);

	}

	@GetMapping("/{id}")
	public ResponseEntity<Categoria> findById(@PathVariable Integer id) {

		return ResponseEntity.ok().body(service.find(id));

	}

	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDTO categoriaDTO) {

		Categoria categoria = service.fromDTO(categoriaDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(service.insert(categoria).getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDTO categoriaDTO, @PathVariable Integer id) {

		Categoria categoria = service.fromDTO(categoriaDTO);
		
		categoria.setId(id);

		categoria = service.update(categoria);

		return ResponseEntity.noContent().build();

	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {

		service.delete(id);

		return ResponseEntity.noContent().build();

	}

	@GetMapping("/page")
	public ResponseEntity<Page<CategoriaDTO>> findPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {

		Page<Categoria> list = service.findPage(page, linesPerPage, orderBy, direction);

		Page<CategoriaDTO> listDto = list.map(categoria -> new CategoriaDTO(categoria));

		return ResponseEntity.ok().body(listDto);

	}

}
