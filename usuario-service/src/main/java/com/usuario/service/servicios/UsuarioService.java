package com.usuario.service.servicios;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.usuario.service.entidades.Usuario;
import com.usuario.service.feignclients.CarroFeignClient;
import com.usuario.service.feignclients.MotoFeignClient;
import com.usuario.service.modelos.Carro;
import com.usuario.service.modelos.Moto;
import com.usuario.service.repositorios.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private CarroFeignClient carroFeignclient;
	
	@Autowired
	private MotoFeignClient motoFeignclient;
	
	
	@Autowired
	private UsuarioRepository repositorio;
		
	public List<Usuario> getAll() {
		
		return repositorio.findAll();
	}
	
	public Usuario getUsuarioById(int id) {
		
		Optional<Usuario> oUsuario = repositorio.findById(id);
		
		if(oUsuario.isPresent())
			return repositorio.findById(id).get();
		else 
			return null;
	}
	
	public Usuario save(Usuario usuario) {
		
		return repositorio.save(usuario);
	}
	
	
	//Comunicacion con los otros microservicios usando RestTemplate
	
	public List<Carro> getCarros(int usuarioId) {
		
		//List<Carro> carros = restTemplate.getForObject("http://localhost:8002/carro/usuario/" + usuarioId, List.class);
		//Como ahora se usa el gateway y puertos dinamicos solo se debe colocar el nombre del servicio, ya no la IP y puerto.
		List<Carro> carros = restTemplate.getForObject("http://carro-service/carro/usuario/" + usuarioId, List.class);
		
		return carros;
	}
	
	public List<Moto> getMotos(int usuarioId) {
		
		//List<Moto> motos = restTemplate.getForObject("http://localhost:8003/moto/usuario/" + usuarioId, List.class);
		//Como ahora se usa el gateway y puertos dinamicos solo se debe colocar el nombre del servicio, ya no la IP y puerto.
		List<Moto> motos = restTemplate.getForObject("http://moto-service/moto/usuario/" + usuarioId, List.class);
		
		return motos;
	}
	
	public Carro saveCarroConFeign(int usuarioId, Carro carro) {
		
		carro.setUsuarioId(usuarioId);
		Carro carroNuevo = carroFeignclient.save(carro);
		
		return carroNuevo;
	}
	
	public List<Carro> getCarrosUsuarioConFeign(int usuarioId) {
		
		List<Carro> lista = carroFeignclient.getCarros(usuarioId);
		
		return lista;
	}
	
	
	public Moto saveMotoConFeign(int usuarioId, Moto moto) {
		
		moto.setUsuarioId(usuarioId);
		Moto motoNueva = motoFeignclient.save(moto);
		
		return motoNueva;
	}
	
	public List<Moto> getMotosUsuarioConFeign(int usuarioId) {
		
		List<Moto> lista = motoFeignclient.getMotos(usuarioId);
		
		return lista;
	}
	
	public Map<String, Object> getUsuarioAndVehiculos(int usuarioId) {
		
		Map<String, Object> resultado = new HashMap<String, Object>();
		
		Usuario usuario = repositorio.findById(usuarioId).orElse(null);
		
		if(usuario == null) {
			resultado.put("mensaje", "El usuario no existe.");
		}
		else {
			resultado.put("usuario", usuario);
			
			List<Carro> listaCarros = carroFeignclient.getCarros(usuarioId);			
			if(listaCarros == null || listaCarros.isEmpty()) {
				resultado.put("Carros", "El usuario no tiene carros.");
			}
			else {
				resultado.put("Carros", listaCarros);								
			}			
			
			List<Moto> listaMotos = motoFeignclient.getMotos(usuarioId);			
			if(listaMotos == null || listaMotos.isEmpty()) {
				resultado.put("Motos", "El usuario no tiene motos.");
			}
			else {
				resultado.put("Motos", listaMotos);								
			}			
		}
		
		return resultado;
	}
	
}
