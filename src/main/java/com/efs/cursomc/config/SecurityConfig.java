package com.efs.cursomc.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.efs.cursomc.security.JWTAuthenticationFilter;
import com.efs.cursomc.security.JWTAuthorizationFilter;
import com.efs.cursomc.security.JWTUtil;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // permite auterizar para perfis diferentes
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private Environment env;

	/**
	 * o Spring procura no projeto uma implementacao desta classe, nosso caso UserDetailsServiceImpl
	 */
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	private static final String[] PUBLIC_MATCHERS = { 
			"/h2-console/**"};
	
	private static final String[] PUBLIC_MATCHERS_GET = {
			"/produtos/**",
			"/categorias/**"};
	
	//permitir só o POST (para a pessoa poder se cadastrar)
	private static final String[] PUBLIC_MATCHERS_POST = {
			"/clientes/**"};

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//se tiver no profile de teste, precisa usar o H2.
		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		
		http.cors().and().csrf().disable();
		http.authorizeRequests()
				.antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
				
				
				
				//TODO remover
				.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_POST).permitAll()
				//TODO remover
				
				
				
				// não precisa de autencicação - apenas metodos GETs
				.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
				// não precisa de autencicação
				.antMatchers(PUBLIC_MATCHERS).permitAll()
				// pra todo o resto, precisa de autencicação
				.anyRequest().authenticated();
		
		
		http.addFilter(new JWTAuthenticationFilter(this.authenticationManager(), jwtUtil));
		http.addFilter(new JWTAuthorizationFilter(this.authenticationManager(), jwtUtil, userDetailsService));
		
		//nosso back-and não ira criar sessao de usuario
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	/**
	 * necessário para usar a autenticacao do framework
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(this.bCryptPasswordEncoder());
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		//permite acesso aos enpoint com as configurações basicas de varias origens. 
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}