package com.efs.cursomc.resources;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.efs.cursomc.dto.EmailDTO;
import com.efs.cursomc.security.JWTUtil;
import com.efs.cursomc.security.UserSS;
import com.efs.cursomc.services.AuthService;
import com.efs.cursomc.services.UserService;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private AuthService service;

	@RequestMapping(value = "/refresh_token", method = RequestMethod.POST) // o usuário tem que estar logado para acessar
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		UserSS user = UserService.authenticated();
		String token = jwtUtil.generateToken(user.getUsername());
		response.addHeader(JWTUtil.AUTHORIZATION, JWTUtil.BEARER + token);
		response.addHeader(JWTUtil.EXPOR_ACESSO_AO_HEADER, JWTUtil.AUTHORIZATION);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value = "/forgot", method = RequestMethod.POST) // o usuário tem que estar logado para acessar
	public ResponseEntity<Void> forgot(@Valid @RequestBody EmailDTO objDTO) {
		service.sendNewPassword(objDTO.getEmail());
		return ResponseEntity.noContent().build();
	}
}