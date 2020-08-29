package com.efs.cursomc.resources;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.efs.cursomc.domain.BeanID;

public class DefaultRestController {

	public ResponseEntity<Void> respostaPadrao(BeanID obj) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
}