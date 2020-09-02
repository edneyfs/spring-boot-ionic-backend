package com.efs.cursomc.security;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JWTUtil.class);
	
	public static final String AUTHORIZATION = "Authorization";
	public static final String BEARER = "Bearer ";
	public static final String LOCATION = "location";

	public static final String EXPOR_ACESSO_AO_HEADER = "access-control-expose-headers";

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private Long expiration;

	public String generateToken(String username) {
		// gera nosso token
		return Jwts.builder().setSubject(username)
				// horario atual + o tempo de expiration
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				// dizer como vai assinar o token e qual o algoritmo e o segredo
				.signWith(SignatureAlgorithm.HS512, secret.getBytes()).compact();
	}

	public boolean tokenValido(String token) {
		// tem as reinvidicações do token
		Claims claims = this.getClaims(token);
		if (claims != null) {
			String username = claims.getSubject();
			Date expirationDate = claims.getExpiration();
			Date now = new Date(System.currentTimeMillis());
			if (username != null && expirationDate != null && now.before(expirationDate)) {
				return true;
			}
		}
		return false;
	}

	private Claims getClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			LOGGER.debug(e.getMessage(), e);
			return null;
		}
	}

	public String getUsername(String token) {
		Claims claims = this.getClaims(token);
		if (claims != null) {
			return claims.getSubject();
		}
		return null;
	}
}