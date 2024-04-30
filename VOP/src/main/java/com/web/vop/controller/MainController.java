package com.web.vop.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.log4j.Log4j;


@Controller
@Log4j
public class MainController {// ���� ������ ���� ��Ʈ�ѷ�

	@GetMapping("/main") // main.jsp �� ������ �̵�
	public void mainGET() {
		System.out.println("main.jsp�� �̵�");
		log.info("mainGET()");
	}//end mainGET()
	
	
	
	
}//end MainController
