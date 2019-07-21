package net.valorweb.recource;

import java.net.URI;
import java.util.List;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import net.valorweb.domain.Cliente;
import net.valorweb.domain.Cliente;
import net.valorweb.dto.ClienteDTO;
import net.valorweb.dto.ClienteNewDTO;
import net.valorweb.services.ClienteService;
import net.valorweb.services.S3Service;

@RestController
@RequestMapping(value = "clientes")
public class ClienteResource {

	@Autowired
	private ClienteService service;
	


	@GetMapping
	public ResponseEntity<List<Cliente>> listAll() {

		return ResponseEntity.ok().body(service.findAll());

	}

	@GetMapping("/{id}")
	public ResponseEntity<Cliente> findPorId(@PathVariable Integer id) {

		return ResponseEntity.ok().body(service.findById(id));

	}
	
	@GetMapping("/email")
	public ResponseEntity<Cliente> find(@RequestParam(value="value") String email ){
		return ResponseEntity.ok(service.findByEmail(email));
	}

	@PostMapping
	public ResponseEntity<Void> insert(@Valid @RequestBody ClienteNewDTO clienteDTO) {

		Cliente cliente = service.fromDTO(clienteDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(service.insert(cliente).getId()).toUri();
		return ResponseEntity.created(uri).build();
	}


	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO clienteDTO, @PathVariable Integer id) {

		Cliente cliente = service.fromDTO(clienteDTO);

		cliente.setId(id);

		cliente = service.update(cliente);

		return ResponseEntity.noContent().build();

	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {

		service.delete(id);

		return ResponseEntity.noContent().build();

	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/page")
	public ResponseEntity<Page<ClienteDTO>> findPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {

		Page<Cliente> list = service.findPage(page, linesPerPage, orderBy, direction);

		Page<ClienteDTO> listDto = list.map(cliente -> new ClienteDTO(cliente));

		return ResponseEntity.ok().body(listDto);

	}
	

	@PostMapping(value="/picture")
	public ResponseEntity<Void> uploadProfilePicture(@RequestParam(name="file") MultipartFile file) {

		URI uri =  service.uploadProfilePicture(file);
		
		return ResponseEntity.created(uri).build();
	}

}
