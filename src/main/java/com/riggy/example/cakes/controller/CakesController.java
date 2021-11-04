package com.riggy.example.cakes.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.riggy.example.cakes.model.dto.CakeDTO;
import com.riggy.example.cakes.service.CakeService;


@Controller
public class CakesController {

	@Autowired
	private CakeService cakeService;
	
	
	@GetMapping(value = "/")
	public String cakes(Model model) {
		
		List<CakeDTO> cakes = cakeService.findCakes();
		
		model.addAttribute("cakes", cakes);

	    return "cakes-view";
	}

}
