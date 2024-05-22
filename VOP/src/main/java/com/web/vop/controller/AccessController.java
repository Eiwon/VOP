package com.web.vop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/access")
@Log4j
public class AccessController {
	
	
	@GetMapping("/denied")
	public void accessDeniedGET() {
		log.info("액세스 거부");
	} // end accessDeniedGET
	
	
}
