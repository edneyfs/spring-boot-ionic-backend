package com.efs.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.efs.cursomc.domain.Pedido;

public interface EmailServices {

	void sendOrderConfirmationEmail(Pedido pedido);
	
	void sendEmail(SimpleMailMessage msg);
}