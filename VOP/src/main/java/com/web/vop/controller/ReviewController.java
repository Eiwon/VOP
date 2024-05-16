package com.web.vop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/review")
@Log4j
public class ReviewController {
	
	@GetMapping("/register")
	public void loginGET() {
		log.info("register 페이지 이동 요청");
		
	} // end loginGET
}
