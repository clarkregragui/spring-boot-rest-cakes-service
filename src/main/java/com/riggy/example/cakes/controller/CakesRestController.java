package com.riggy.example.cakes.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.riggy.example.cakes.model.dto.CakeDTO;
import com.riggy.example.cakes.service.CakeService;

@RestController
@Validated
public class CakesRestController {

	@Autowired
	private CakeService cakeService;
	
	
	@GetMapping(value = "/cakes")
	public ResponseEntity<List<CakeDTO>> getCakes(Model model) {
		
		List<CakeDTO> cakes = cakeService.findCakes();
		
		return new ResponseEntity<>(cakes, HttpStatus.OK);

	}
	
	@PostMapping(value = "/cakes")
	public ResponseEntity<CakeDTO> createCake(@Valid @RequestBody CakeDTO cakeDto) {
		
		try {
			cakeService.createCake(cakeDto);
			return new ResponseEntity<>(cakeDto, HttpStatus.CREATED);
			
		} catch(DataIntegrityViolationException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Integrity constraint violated", e);
		}
	}
}
