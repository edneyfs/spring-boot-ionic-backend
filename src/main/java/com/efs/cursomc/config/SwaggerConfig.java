package com.efs.cursomc.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Header;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * http://localhost:8080/swagger-ui.html
 * 
 * @author edney.siqueira
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	private final ResponseMessage ERRO_201 = customMessage1();
	private final ResponseMessage ERRO_204_PUT = simpleMessage(204, "Atualização ok");
	private final ResponseMessage ERRO_204_DEL = simpleMessage(204, "Deleção ok");
	private final ResponseMessage ERRO_403 = simpleMessage(403, "Não autorizado");
	private final ResponseMessage ERRO_404 = simpleMessage(404, "Não encontrado");
	private final ResponseMessage ERRO_422 = simpleMessage(422, "Erro de validação");
	private final ResponseMessage ERRO_500 = simpleMessage(500, "Erro inesperado");
	
	@Bean
	public Docket api() {
		return 
			new Docket(DocumentationType.SWAGGER_2)
				//configurar as mensagems com nossas respostas
				.useDefaultResponseMessages(false)
				.globalResponseMessage(RequestMethod.GET, Arrays.asList(ERRO_403, ERRO_404, ERRO_500))
				.globalResponseMessage(RequestMethod.POST, Arrays.asList(ERRO_201, ERRO_403, ERRO_422, ERRO_500))
				.globalResponseMessage(RequestMethod.PUT, Arrays.asList(ERRO_204_PUT, ERRO_403, ERRO_404, ERRO_422, ERRO_500))
				.globalResponseMessage(RequestMethod.DELETE, Arrays.asList(ERRO_204_DEL, ERRO_403, ERRO_404, ERRO_500))
				
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.efs.cursomc.resources"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(this.apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfo("API do curso Spring Boot",
				"Esta API é utilizada no curso de Spring Boot do prof. Nelio Alves", "Versão 1.0",
				"https://www.udemy.com/terms",
				new Contact("Edney Siqueira", "www.efs.com", "edneyfs@gmail.com"),
				"Permitido uso para estudantes", 
				"https://www.udemy.com/terms", 
				Collections.emptyList() // Vendor // Extensions
		);
	}
	
	private ResponseMessage simpleMessage(int code, String msg) {
		return new ResponseMessageBuilder().code(code).message(msg).build();
	}
	
	private ResponseMessage customMessage1() {
		Map<String, Header> map = new HashMap<>();
		map.put("location", 
				new Header("location", "URI do novo recurso", new ModelRef("string")));
		
		return 
				new ResponseMessageBuilder()
					.code(201)
					.message("Recurso criado")
					.headersWithDescription(map)
					.build();
	}
}