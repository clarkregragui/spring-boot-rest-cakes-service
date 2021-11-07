package com.riggy.example.cakes.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.riggy.example.cakes.data.CakeRepository;
import com.riggy.example.cakes.model.dto.CakeDTO;
import com.riggy.example.cakes.model.entity.Cake;

@Service
public class CakeServiceImpl implements CakeService {

	@Value("classpath:cakes.json")
	Resource cakesJsonFileResource;
	
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private CakeRepository cakeRepository;
	
	@Override
	public List<CakeDTO> findCakes() {
		Iterable<Cake> cakes = cakeRepository.findAll();
		
//		List<CakeDTO> cakeDtoList = 
//				StreamSupport.stream(cakes.spliterator(), false)
//				  .map(cake -> modelMapper.map(cake, CakeDTO.class))
//				  .collect(Collectors.toList());

//Alternative ModelMapper mapping Lists option		
		java.lang.reflect.Type listType = new TypeToken<List<CakeDTO>>() {}.getType();
		List<CakeDTO> cakeDtoList = new ModelMapper().map(cakes, listType);
		
		return cakeDtoList;
	}

	@Override
	public void createCake(CakeDTO cakeDto) {
		Cake cake = modelMapper.map(cakeDto, Cake.class);
		cake.setCakeId(null);
		cakeRepository.save(cake);
	}
	
	@PostConstruct
	private void postConstruct() throws IOException {
		
		InputStream in = cakesJsonFileResource.getInputStream();
		ObjectMapper objectMapper = new ObjectMapper();

		Set<Cake> cakes = objectMapper.readValue(in, new TypeReference<Set<Cake>>(){});
		cakeRepository.saveAll(cakes);
	}

}
