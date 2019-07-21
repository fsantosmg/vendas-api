package net.valorweb.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import net.valorweb.domain.Categoria;
import net.valorweb.dto.CategoriaDTO;
import net.valorweb.repositories.CategoriaRepository;
import net.valorweb.services.exception.DataIntegrityException;
import net.valorweb.services.exception.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repository;

	public Categoria find(Integer id) {
		Optional<Categoria> categoria = repository.findById(id);
		return categoria.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}

	public Categoria insert(Categoria categoria) {

		categoria.setId(null);
		return repository.save(categoria);
	}

	public Categoria update(Categoria categoria) {
		Categoria categoriaDb = find(categoria.getId());
		updateData(categoria, categoriaDb);
		return repository.save(categoria);
	}

	private void updateData(Categoria categoria, Categoria categoriaDb) {
		categoriaDb.setNome(categoria.getNome());
	}

	public void delete(Integer id) {

		find(id);
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos!");
		}
	}

	public List<Categoria> findAll() {

		return repository.findAll();
	}

	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {

		return repository.findAll(PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy));

	}

	public Categoria fromDTO(CategoriaDTO categoriaDTO) {
		return new Categoria(categoriaDTO.getId(), categoriaDTO.getNome());
	}

}
