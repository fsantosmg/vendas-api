package net.valorweb.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import net.valorweb.domain.Cliente;
import net.valorweb.services.exception.ObjectNotFoundException;

@Service
public class AuthService {
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	
	@Autowired
	private EmailService emailService;
	
	private Random random = new Random();
	
	public void sendNewPassword(String email) {
		
		Cliente cliente = clienteService.findByEmail(email);
		
		if( cliente== null) {
			throw new ObjectNotFoundException("Email não encontrado");
			
		}
		
		String newPass = newPassword();
		cliente.setSenha(bCryptPasswordEncoder.encode(newPass));
		
		clienteService.save(cliente);
		emailService.sendNewPasswordEmail(cliente, newPass);
		
	}

	private String newPassword() {
		
		char[] vet = new char[10];
		for(int i=0; i<10 ; i++) {
			vet[i] = randoChar();
		}
		return new String(vet);
	}

	private char randoChar() {
		int opt = random.nextInt(3);
		if(opt == 0) { //gera um digito
			
			return (char) (random.nextInt(10) + 48);
			
		}else if(opt == 1) { //gera letra maiuscula
			
			return (char) (random.nextInt(26) + 65);
			
		}else {//gera letra minuscula
			return (char) (random.nextInt(26) + 97);
		}
		
	}
}
