package com.moto.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moto.service.entity.Moto;
import com.moto.service.service.MotoService;

@RestController
@RequestMapping("/moto")
public class MotoController {
	
	@Autowired
	private MotoService servicio;
	
	@GetMapping
	public ResponseEntity<List<Moto>> listarMotos() {
		
		List<Moto> lista = servicio.getAll();
		
		if(lista.isEmpty())
			return ResponseEntity.noContent().build();
		else
			return ResponseEntity.ok(lista);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Moto> obtenerMoto(@PathVariable("id") int id) {
	
		Moto moto = servicio.getMotoById(id);
		
		if(moto == null)
			return ResponseEntity.notFound().build();
		else 
			return ResponseEntity.ok(moto);
	}
	
	@GetMapping("/usuario/{usuarioId}")
	public ResponseEntity<List<Moto>> listarMotosPorUsuarioId(@PathVariable("usuarioId") int usuarioId) {
		
		List<Moto> lista = servicio.getMotosByUsuarioId(usuarioId);
		
		if(lista.isEmpty())
			return ResponseEntity.noContent().build();
		else
			return ResponseEntity.ok(lista);
	}
	
	@PostMapping
	public ResponseEntity<Moto> guardarMoto(@RequestBody Moto moto) {
		
		Moto nuevaMoto = servicio.save(moto);
		return ResponseEntity.ok(nuevaMoto);
	}
	

}
