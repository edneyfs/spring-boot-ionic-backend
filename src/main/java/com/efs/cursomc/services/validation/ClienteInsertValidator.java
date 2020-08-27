package com.efs.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.efs.cursomc.domain.enums.TipoCliente;
import com.efs.cursomc.dto.ClienteNewDTO;
import com.efs.cursomc.resources.exception.FieldMessage;
import com.efs.cursomc.services.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
	
	@Override
	public void initialize(ClienteInsert ann) {
	}

	@Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();
		
		if (TipoCliente.PESSOAFISICA.getCodigo().equals(objDto.getTipoCliente()) &&
			!BR.isValidCPF(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CPF Inválido"));
		}
		
		if (TipoCliente.PESSOAJURIDICA.getCodigo().equals(objDto.getTipoCliente()) &&
				!BR.isValidCNPJ(objDto.getCpfOuCnpj())) {
				list.add(new FieldMessage("cpfOuCnpj", "CNPJ Inválido"));
			}
		
		for (FieldMessage e : list) {
			//setando o erro no framework
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFielName()).addConstraintViolation();
		}
		return list.isEmpty();
	}
}