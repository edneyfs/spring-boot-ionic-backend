package com.efs.cursomc.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.efs.cursomc.security.JWTUtil;

@Component
public class HeaderExposureFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	/**
	 * Filtro que pega todas as requisições e expoe o header locatiom e depois segue o fluxo - desta forma a aplicação Angular poderá ler. 
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletResponse res = (HttpServletResponse) response;
		res.addHeader(JWTUtil.EXPOR_ACESSO_AO_HEADER, JWTUtil.LOCATION);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}
}