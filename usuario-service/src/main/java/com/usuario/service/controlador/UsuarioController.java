package com.usuario.service.controlador;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usuario.service.entidades.Usuario;
import com.usuario.service.modelos.Carro;
import com.usuario.service.modelos.Moto;
import com.usuario.service.servicios.UsuarioService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
	
	@Autowired
	private UsuarioService servicio;
	
	@GetMapping
	public ResponseEntity<List<Usuario>> listarUsuarios() {
		
		List<Usuario> usuarios = servicio.getAll();
		
		if(usuarios.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		else {
			return ResponseEntity.ok(usuarios);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Usuario> obtenerUsuario(@PathVariable("id") int id) {
	
		Usuario usuario = servicio.getUsuarioById(id);
		
		if(usuario == null) {
			return ResponseEntity.notFound().build();
		}
		else {
			return ResponseEntity.ok(usuario);
		}
	}
	
	@PostMapping
	public ResponseEntity<Usuario> guardarUsuario(@RequestBody Usuario usuario) {
		
		Usuario usuarioNuevo = servicio.save(usuario);
		
		return ResponseEntity.ok(usuarioNuevo);	
	}
	
	@CircuitBreaker(name = "carrosCB", fallbackMethod = "fallbackGetCarros")
	@GetMapping("/carro/{usuarioId}")
	public ResponseEntity<List<Carro>> listarCarros(@PathVariable("usuarioId") int usuarioId) {
		
		//Primero comprobar si el usuario existe
		Usuario usuario = servicio.getUsuarioById(usuarioId);
		
		if(usuario == null)
			return ResponseEntity.notFound().build();
		
		//Si existe obtener sus carros
		List<Carro> lista = servicio.getCarros(usuarioId);
		
		if(lista.isEmpty())
			return ResponseEntity.noContent().build();		
		else
			return ResponseEntity.ok(lista);
	}
	
	@CircuitBreaker(name = "motosCB", fallbackMethod = "fallbackGetMotos")
	@GetMapping("/moto/{usuarioId}")
	public ResponseEntity<List<Moto>> listarMotos(@PathVariable("usuarioId") int usuarioId) {
		
		//Primero comprobar si el usuario existe
		Usuario usuario = servicio.getUsuarioById(usuarioId);
		
		if(usuario == null)
			return ResponseEntity.notFound().build();
		
		//Si existe obtener sus motos
		List<Moto> lista = servicio.getMotos(usuarioId);
		
		if(lista.isEmpty())
			return ResponseEntity.noContent().build();		
		else
			return ResponseEntity.ok(lista);
	}
	
	@CircuitBreaker(name = "carrosCB", fallbackMethod = "fallbackSaveCarro")
	@PostMapping("/carro/{usuarioId}")
	public ResponseEntity<Carro> guardarCarroUsuarioFeign(@PathVariable("usuarioId") int usuarioId, @RequestBody Carro carro) {
		
		Carro carroNuevo = servicio.saveCarroConFeign(usuarioId, carro);
		
		return ResponseEntity.ok(carroNuevo);	
	}
	
	@GetMapping("/carros/{usuarioId}")
	public ResponseEntity<List<Carro>> listarCarrosUsuarioFeign(@PathVariable("usuarioId") int usuarioId) {
		
		List<Carro> lista = servicio.getCarrosUsuarioConFeign(usuarioId);
		
		if(lista.isEmpty())
			return ResponseEntity.noContent().build();		
		else
			return ResponseEntity.ok(lista);
	}
	
	@CircuitBreaker(name = "motosCB", fallbackMethod = "fallbackSaveMoto")
	@PostMapping("/moto/{usuarioId}")
	public ResponseEntity<Moto> guardarMotoUsuarioFeign(@PathVariable("usuarioId") int usuarioId, @RequestBody Moto moto) {
		
		Moto motoNueva = servicio.saveMotoConFeign(usuarioId, moto);
		
		return ResponseEntity.ok(motoNueva);	
	}
	
	@GetMapping("/motos/{usuarioId}")
	public ResponseEntity<List<Moto>> listarMotosUsuarioFeign(@PathVariable("usuarioId") int usuarioId) {
		
		List<Moto> lista = servicio.getMotosUsuarioConFeign(usuarioId);
		
		if(lista.isEmpty())
			return ResponseEntity.noContent().build();		
		else
			return ResponseEntity.ok(lista);
	}
	
	@CircuitBreaker(name = "todosCB", fallbackMethod = "fallbackGetTodos")
	@GetMapping("/todos/{usuarioId}")
	public ResponseEntity<Map<String, Object>> listarTodosLosVehiculosFeign(@PathVariable("usuarioId") int usuarioId) {
		
		Map<String, Object> resultado = servicio.getUsuarioAndVehiculos(usuarioId);
		
		return ResponseEntity.ok(resultado);
	}
	
	
	//Metodos de tipo fallBack para Circuit Breaker
	public ResponseEntity<List<Carro>> fallbackGetCarros(@PathVariable("usuarioId") int usuarioId, RuntimeException exception) {
		
		return new ResponseEntity("Hubo un problema con el servicio que obtiene los carros, usuarioId=" + usuarioId, HttpStatus.OK);
	}	

	public ResponseEntity<List<Moto>> fallbackGetMotos(@PathVariable("usuarioId") int usuarioId, RuntimeException exception) {
		
		return new ResponseEntity("Hubo un problema con el servicio que obtiene las motos, usuarioId=" + usuarioId, HttpStatus.OK);
	}

	public ResponseEntity<Carro> fallbackSaveCarro(@PathVariable("usuarioId") int usuarioId, @RequestBody Carro carro, RuntimeException exception) {
		
		return new ResponseEntity("Hubo un problema con el servicio que guarda el carro, usuarioId=" + usuarioId, HttpStatus.OK);
	}		
	
	public ResponseEntity<Moto> fallbackSaveMoto(@PathVariable("usuarioId") int usuarioId, @RequestBody Moto moto, RuntimeException exception) {
		
		return new ResponseEntity("Hubo un problema con el servicio que guarda la moto, usuarioId=" + usuarioId, HttpStatus.OK);
	}
	
	public ResponseEntity<Map<String, Object>> fallbackGetTodos(@PathVariable("usuarioId") int usuarioId, RuntimeException exception) {
		
		return new ResponseEntity("Hubo un problema con el servicio que obtiene todos los vehiculos, usuarioId=" + usuarioId, HttpStatus.OK);
	}

}
