package net.valorweb.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.valorweb.domain.Estado;
import net.valorweb.repositories.EstadoRepository;

@Service
public class EstadoService {

	@Autowired 
	private EstadoRepository estadoRepository;
	
	public List<Estado> findAll(){
		return estadoRepository.findAllByOrderByNome();
	}
}
