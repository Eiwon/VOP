package com.web.vop.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.log4j.Log4j;


@Controller
@Log4j
public class MainController {// 메인 페이지 구현 컨트롤러

	@GetMapping("/main") // main.jsp 로 페이지 이동
	public void mainGET() {
		System.out.println("main.jsp로 이동");
		log.info("mainGET()");
	}//end mainGET()
	
	
	
	
}//end MainController
