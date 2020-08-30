package com.efs.cursomc.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.efs.cursomc.domain.Pedido;

public abstract class AbstractEmailService implements EmailServices {

	@Value("${default.sender}")
	private String sender;
	
	@Override
	public void sendOrderConfirmationEmail(Pedido pedido) {
		SimpleMailMessage msg = this.prepareSimpleMailMessageFromPedido(pedido);
		sendEmail(msg);
	}

	
	protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido pedido) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(pedido.getCliente().getEmail());
		msg.setFrom(sender);
		msg.setSubject("Pedido confirmado! Código: " + pedido.getId());
		msg.setSentDate(new Date(System.currentTimeMillis()));
		msg.setText(pedido.toString());
		return msg;
	}	
}