package com.efs.cursomc.services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

import com.efs.cursomc.domain.Pedido;

public class MockEmailService extends AbstractEmailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MockEmailService.class);
	
	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOGGER.info("Simulando o envio de email...");
		LOGGER.info(msg.toString());
		LOGGER.info("Email enviado");
	}

	@Override
	public void sendHtmlEmail(MimeMessage msg) {
		LOGGER.info("Simulando o envio de email HTML...");
		LOGGER.info(msg.toString());
		LOGGER.info("Email enviado");		
	}

	@Override
	protected MimeMessage prepareMimeMessageFromPedido(Pedido pedido) throws MessagingException {
		throw new MessagingException();
	}
}