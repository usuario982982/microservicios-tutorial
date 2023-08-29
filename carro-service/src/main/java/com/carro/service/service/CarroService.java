package com.carro.service.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.carro.service.entity.Carro;
import com.carro.service.repository.CarroRepository;

@Service
public class CarroService {

	@Autowired
	private CarroRepository repositorio;
	
	public List<Carro> getAll() {
		
		return repositorio.findAll();
	}
	
	public Carro getCarroById(int id) {
		
		return repositorio.findById(id).orElse(null);
	}
	
	public List<Carro> getCarrosByUsuarioId(int usuarioId) {
		
		List<Carro> listaCarros = repositorio.findByUsuarioId(usuarioId);
		
		if(listaCarros == null || listaCarros.isEmpty())
			return null;
		else
			return listaCarros;
	}
	
	public Carro save(Carro carro) {
		
		return repositorio.save(carro);
	}
	
}
