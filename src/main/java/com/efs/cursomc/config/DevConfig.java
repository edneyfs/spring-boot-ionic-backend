package com.efs.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.efs.cursomc.services.DBService;
import com.efs.cursomc.services.EmailService;
import com.efs.cursomc.services.SmtpEmailService;

@Configuration
@Profile("dev") // serão ativados apenas quando o profile estiver definido com este name
public class DevConfig {

	@Autowired
	private DBService dbService;
	
	//pega o valor definido em application-*.properties
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;
	
	@Bean
	public boolean instantieteDatabase() throws ParseException {
		boolean retorno;
		System.out.println("spring.jpa.hibernate.ddl-auto: " + strategy);
		
		if ( ! "create".equals(strategy)) {
			retorno =  false;
		} else {
			dbService.instantiateTestDatabase();
			retorno = true;
		}
		
		System.out.println("");
		System.out.println("");
		System.out.println("******RODANDO****");
		
		return retorno;
	}
	
	@Bean
	public EmailService emailServices() {
		return new SmtpEmailService();
	}
}