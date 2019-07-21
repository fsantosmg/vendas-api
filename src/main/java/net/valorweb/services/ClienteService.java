package net.valorweb.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import net.valorweb.domain.Cliente;
import net.valorweb.domain.Endereco;
import net.valorweb.domain.enums.Perfil;
import net.valorweb.domain.enums.TipoCliente;
import net.valorweb.dto.ClienteDTO;
import net.valorweb.dto.ClienteNewDTO;
import net.valorweb.repositories.CidadeRepository;
import net.valorweb.repositories.ClienteRepository;
import net.valorweb.repositories.EnderecoRepository;
import net.valorweb.security.UserSecurity;
import net.valorweb.services.exception.AuthorizationExeption;
import net.valorweb.services.exception.DataIntegrityException;
import net.valorweb.services.exception.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repository;

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private S3Service s3Service;

	@Autowired
	private ImageService imageService;

	@Value("${img.prefix.cliente.profile}")
	private String prefix;

	@Value("${img.profile.size}")
	private Integer size;

	public Cliente findById(Integer id) {

		UserSecurity user = UserService.authenticated();

		if (user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationExeption("Acesso negado!");
		}

		Optional<Cliente> cliente = repository.findById(id);
		return cliente.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}

	public List<Cliente> findAll() {

		return repository.findAll();
	}

	public Cliente find(Integer id) {
		Optional<Cliente> cliente = repository.findById(id);
		return cliente.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}

	@Transactional
	public Cliente insert(Cliente cliente) {

		cliente.setId(null);
		cliente = repository.save(cliente);
		enderecoRepository.saveAll(cliente.getEnderecos());

		return cliente;
	}

	public Cliente update(Cliente cliente) {

		Cliente clienteDb = find(cliente.getId());
		updateData(cliente, clienteDb);

		return repository.save(clienteDb);
	}

	private void updateData(Cliente cliente, Cliente clienteDb) {
		clienteDb.setEmail(cliente.getEmail());
		clienteDb.setNome(cliente.getNome());
	}

	public void delete(Integer id) {

		find(id);
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir um cliente que possui pedidos!");
		}
	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {

		return repository.findAll(PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy));

	}

	public Cliente fromDTO(ClienteDTO clienteDTO) {
		return new Cliente(clienteDTO.getNome(), clienteDTO.getEmail(), null, null, null);
	}

	public Cliente fromDTO(@Valid ClienteNewDTO clienteDTO) {
		Cliente cli = new Cliente(clienteDTO.getNome(), clienteDTO.getEmail(), clienteDTO.getCpfCnpj(),
				TipoCliente.toEnum(clienteDTO.getTipo()), bCryptPasswordEncoder.encode(clienteDTO.getSenha()));
		Endereco endereco = new Endereco(clienteDTO.getLogradouro(), clienteDTO.getNumero(),
				clienteDTO.getComplemento(), clienteDTO.getBairro(), clienteDTO.getCep(), cli,
				cidadeRepository.getOne(clienteDTO.getCidadeId()));
		cli.getEnderecos().add(endereco);
		cli.getTelefones().add(clienteDTO.getTelefone1());
		if (clienteDTO.getTelefone2() != null) {
			cli.getTelefones().add(clienteDTO.getTelefone2());
		}
		if (clienteDTO.getTelefone3() != null) {
			cli.getTelefones().add(clienteDTO.getTelefone3());
		}
		return cli;
	}

	public Cliente findByEmail(String email) {

		UserSecurity user = UserService.authenticated();

		if (user == null || !user.hasRole(Perfil.ADMIN) && !email.equals(user.getUsername())) {
			throw new AuthorizationExeption("Acesso negado!");
		}
		Cliente cliente = repository.findByEmail(email);
		if (cliente == null) {
			throw new ObjectNotFoundException(
					"Email não encontrado! Id: " + user.getId() + ", Tipo: " + Cliente.class.getName());
		}

		return cliente;

	}

	public void save(Cliente cliente) {

		repository.save(cliente);

	}

	public URI uploadProfilePicture(MultipartFile multipartFile) {

		UserSecurity user = UserService.authenticated();

		if (user == null) {
			throw new AuthorizationExeption("Acesso negado!");
		}

		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);

		jpgImage = imageService.cropSquare(jpgImage);
		jpgImage = imageService.resize(jpgImage, size);

		String fileName = prefix + user.getId() + ".jpg";

		return s3Service.uploadFile(imageService.getImputStream(jpgImage, "jpg"), fileName, "image");

	}

}
