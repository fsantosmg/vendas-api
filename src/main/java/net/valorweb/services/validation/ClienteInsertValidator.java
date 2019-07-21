package net.valorweb.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import net.valorweb.domain.Cliente;
import net.valorweb.domain.enums.TipoCliente;
import net.valorweb.dto.ClienteNewDTO;
import net.valorweb.recource.exceptions.FieldMessage;
import net.valorweb.repositories.ClienteRepository;
import net.valorweb.services.validation.utils.CpfCnpj;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Override
	public void initialize(ClienteInsert clienteInsert) {
	}

	@Override
	public boolean isValid(ClienteNewDTO clienteNewDTO, ConstraintValidatorContext context) {
		
		List<FieldMessage> list = new ArrayList<>();
		
		if(clienteNewDTO.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !CpfCnpj.isValidCPF(clienteNewDTO.getCpfCnpj()))
		{
			list.add(new FieldMessage("cpfCnpj", "CPF inválido"));
		}
		
		if(clienteNewDTO.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !CpfCnpj.isValidCNPJ(clienteNewDTO.getCpfCnpj()))
		{
			list.add(new FieldMessage("cpfCnpj", "CNPJ inválido"));
		}
		
		Cliente cliente =  clienteRepository.findByEmail(clienteNewDTO.getEmail());
		if(cliente!=null) {
			list.add(new FieldMessage("email", "E-mail já cadastrado"));
		}
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFildName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}
