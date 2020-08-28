package com.efs.cursomc.services.validation;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

public class UtilValidator {

	@Autowired
	private HttpServletRequest request; //pega os parametros da URI, queremos o id
	
	public String getParametro(String chave) {
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		return map.get("id");
	}
}