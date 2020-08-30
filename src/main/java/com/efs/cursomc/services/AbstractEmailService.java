package com.efs.cursomc.services;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.efs.cursomc.domain.Pedido;

public abstract class AbstractEmailService implements EmailServices {

	@Value("${default.sender}")
	private String sender;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@Autowired
	protected JavaMailSender javaMailSender; 
	
	@Override
	public void sendOrderConfirmationEmail(Pedido pedido) {
		SimpleMailMessage msg = this.prepareSimpleMailMessageFromPedido(pedido);
		sendEmail(msg);
	}

	@Override
	public void sendOrderConfirmationHtmlEmail(Pedido pedido) {
		try {
			MimeMessage msg = this.prepareMimeMessageFromPedido(pedido);
			sendHtmlEmail(msg);
		} catch (MessagingException e) {
			this.sendOrderConfirmationEmail(pedido);
		}
	}

	protected String htmlFromTemplatePedido(Pedido pedido) {
		Context context = new Context();
		//enviando o objeto pedido para o templat
		context.setVariable("pedido", pedido);
		
		//ele ja assume a pasta /temlates/
		return templateEngine.process("email/confirmacaoPedido", context);
	}
	
	private SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido pedido) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(pedido.getCliente().getEmail());
		msg.setFrom(sender);
		msg.setSubject("Pedido confirmado! Código: " + pedido.getId());
		msg.setSentDate(new Date(System.currentTimeMillis()));
		msg.setText(pedido.toString());
		return msg;
	}
	
	private MimeMessage prepareMimeMessageFromPedido(Pedido pedido) throws MessagingException {
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