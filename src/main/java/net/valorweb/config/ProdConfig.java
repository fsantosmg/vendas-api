package net.valorweb.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import net.valorweb.services.DBService;
import net.valorweb.services.EmailService;
import net.valorweb.services.SmtpEmailService;

@Configuration
@Profile("prod")
public class ProdConfig {

	@Autowired
	private DBService dbService;
	
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String dbStrategy;
	
	@Bean
	public boolean instantiateDatabase() throws ParseException {

		if ("create".equals(dbStrategy)) {
			
			dbService.instantiateTestDataBase();
			return true;
		}
		
		return false;
	}
	
	@Bean
	public EmailService emailService() {
		return new SmtpEmailService();
	}

}
