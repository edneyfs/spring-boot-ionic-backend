package com.efs.cursomc.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {
	
	@Value("${jwt.secret}")
	private String secret;
			
	@Value("${jwt.expiration}")
	private Long expiration;
	
	public String generateToken(String username) {
		//gera nosso token
		return Jwts.builder()
				.setSubject(username)
				// horario atual + o tempo de expiration
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				// dizer como vai assinar o token 0 qual o algotirmo e o segredo
				.signWith(SignatureAlgorithm.HS512, secret.getBytes())
				.compact();
	}
}