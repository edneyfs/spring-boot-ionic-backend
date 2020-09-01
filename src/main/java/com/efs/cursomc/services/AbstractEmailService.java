package com.efs.cursomc.services;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.efs.cursomc.domain.Cliente;
import com.efs.cursomc.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {

	@Value("${default.sender}")
	protected String sender;
	
	@Autowired
	private TemplateEngine templateEngine;
	
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

	protected abstract MimeMessage prepareMimeMessageFromPedido(Pedido pedido) throws MessagingException;

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

	@Override
	public void sendNewPasswordEmail(Cliente cliente, String newPass) {
		SimpleMailMessage msg = this.prepareNewPasswordEmail(cliente, newPass);
		sendEmail(msg);
	}

	private SimpleMailMessage prepareNewPasswordEmail(Cliente cliente, String newPass) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(cliente.getEmail());
		msg.setFrom(sender);
		msg.setSubject("Solicitação de nova senha");
		msg.setSentDate(new Date(System.currentTimeMillis()));
		msg.setText("Nova senha: " + newPass);
		return msg;
	}
}