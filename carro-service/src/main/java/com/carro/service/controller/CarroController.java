package com.carro.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carro.service.entity.Carro;
import com.carro.service.service.CarroService;

@RestController
@RequestMapping("/carro")
public class CarroController {
	
	
	@Autowired
	private CarroService servicio;

	@GetMapping
	public ResponseEntity<List<Carro>> listarCarros() {
		
		List<Carro> lista = servicio.getAll();
		
		if(lista.isEmpty())
			return ResponseEntity.noContent().build();
		else
			return ResponseEntity.ok(lista);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Carro> obtenerCarro(@PathVariable("id") int id) {
	
		Carro carro = servicio.getCarroById(id);
		
		if(carro == null)
			return ResponseEntity.notFound().build();
		else 
			return ResponseEntity.ok(carro);
	}
	
	@GetMapping("/usuario/{usuarioId}")
	public ResponseEntity<List<Carro>> listarCarrosPorUsuarioId(@PathVariable("usuarioId") int usuarioId) {
		
		List<Carro> lista = servicio.getCarrosByUsuarioId(usuarioId);
		
		if(lista == null || lista.isEmpty())
			return ResponseEntity.noContent().build();
		else
			return ResponseEntity.ok(lista);
	}
	
	@PostMapping
	public ResponseEntity<Carro> guardarCarro(@RequestBody Carro carro) {
		
		Carro nuevoCarro = servicio.save(carro);
		return ResponseEntity.ok(nuevoCarro);
	}
	
}
