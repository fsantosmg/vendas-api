package net.valorweb.recource;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.valorweb.domain.Estado;
import net.valorweb.dto.CidadeDTO;
import net.valorweb.dto.EstadoDTO;
import net.valorweb.services.CidadeService;
import net.valorweb.services.EstadoService;

@RestController
@RequestMapping(value = "estados")
public class EstadoResource {

	@Autowired
	private EstadoService estadoService;

	@Autowired
	private CidadeService cidadeService;

	@GetMapping
	public ResponseEntity<List<EstadoDTO>> findAll() {
		List<Estado> list = estadoService.findAll();
		List<EstadoDTO> listDto = list.stream().map(estado -> new EstadoDTO(estado)).collect(Collectors.toList());
		return ResponseEntity.ok(listDto);
	}

	@GetMapping("/{estadoId}/cidades")
	public ResponseEntity<List<CidadeDTO>> findCidades(@PathVariable Integer estadoId) {
		return ResponseEntity.ok(cidadeService.findByEstado(estadoId).stream().map(cidade -> new CidadeDTO(cidade))
				.collect(Collectors.toList()));
	}

}
