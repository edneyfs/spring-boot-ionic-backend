package com.efs.cursomc.services;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.efs.cursomc.domain.Pedido;

/**
 * @author edney.siqueira
 *
 */
public class SmtpEmailService extends AbstractEmailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SmtpEmailService.class);
	
	@Autowired
	protected JavaMailSender javaMailSender;

	/*
	 * o framewwork instancia esta classe com todos os dados definido no application.properties
	 */
	@Autowired
	private MailSender mailSender;

	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOGGER.info("Enviando email...");
		mailSender.send(msg);
		LOGGER.info("Email enviado");
		
	}

	@Override
	public void sendHtmlEmail(MimeMessage msg) {
		LOGGER.info("Enviando email...");
		javaMailSender.send(msg);
		LOGGER.info("Email enviado");		
	}
	
	protected MimeMessage prepareMimeMessageFromPedido(Pedido pedido) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage, true);
		
		mmh.setTo(pedido.getCliente().getEmail());
		mmh.setFrom(sender);
		mmh.setSubject("Pedido confirmado! Código: " + pedido.getId());
		mmh.setSentDate(new Date(System.currentTimeMillis()));
		mmh.setText(this.htmlFromTemplatePedido(pedido), true);		
		
		return mimeMessage;
	}
}
