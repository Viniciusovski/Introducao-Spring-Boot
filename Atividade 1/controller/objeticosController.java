package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/obj")
public class objeticosController {
	
	@GetMapping
	public String mostrarObjetivos() {
		return "Objetivos de aprendizagem nessa semana são: Spring Boot e Rest";
	}
}
