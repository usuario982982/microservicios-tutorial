package com.moto.service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moto.service.entity.Moto;
import com.moto.service.repository.MotoRepository;

@Service
public class MotoService {
	
	@Autowired
	private MotoRepository repositorio;
	
	public List<Moto> getAll() {
		
		return repositorio.findAll();
	}
	
	public Moto getMotoById(int id) {
		
		return repositorio.findById(id).orElse(null);
	}
	
	public List<Moto> getMotosByUsuarioId(int usuarioId) {
		
		List<Moto> listaMotos = repositorio.findByUsuarioId(usuarioId);
		
		if(listaMotos == null || listaMotos.isEmpty())
			return null;
		else
			return listaMotos;
	}
	
	public Moto save(Moto moto) {
		
		return repositorio.save(moto);
	}

}
