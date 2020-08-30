package com.efs.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.efs.cursomc.services.DBService;
import com.efs.cursomc.services.EmailServices;
import com.efs.cursomc.services.MockEmailService;

@Configuration
@Profile("test") // serão ativados apenas quando o profile estiver definido com este name
public class TesteConfig {

	@Autowired
	private DBService dbService;
	
	@Bean
	public boolean instantieteDatabase() throws ParseException {
		dbService.instantiateTestDatabese();
		
		System.out.println("");
		System.out.println("");
		System.out.println("******RODANDO****");
		return true;
	}
	
	@Bean
	public EmailServices emailServices() {
		return new MockEmailService();
	}
}