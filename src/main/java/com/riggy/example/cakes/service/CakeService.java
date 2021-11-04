package com.riggy.example.cakes.service;

import java.util.List;

import com.riggy.example.cakes.model.dto.CakeDTO;

public interface CakeService {

	
	public List<CakeDTO> findCakes();
	
	public void createCake(CakeDTO cake);
}
