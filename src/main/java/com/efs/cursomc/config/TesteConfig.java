package com.efs.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.efs.cursomc.services.DBService;
import com.efs.cursomc.services.EmailService;
import com.efs.cursomc.services.MockEmailService;

@Configuration
@Profile("test") // serão ativados apenas quando o profile estiver definido com este name
public class TesteConfig {

	@Autowired
	private DBService dbService;
	
	@Bean
	public boolean instantieteDatabase() throws ParseException {
		System.out.println("******INICIANDO EM AMBIENTE DE TESTE ****");
		
		dbService.instantiateTestDatabase();
		
		System.out.println("");
		System.out.println("");
		System.out.println("******RODANDO****");
		return true;
	}
	
	@Bean
	public EmailService emailService() {
		return new MockEmailService();
	}
}