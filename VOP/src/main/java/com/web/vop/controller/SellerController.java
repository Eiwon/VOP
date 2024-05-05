package com.web.vop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/seller")
@Log4j
public class SellerController {

	@GetMapping("sellerRequest")
	public void sellerRequestGET() {
		log.info("�Ǹ��� ���� ��û �������� �̵�");
	} // end sellerRequestGET
	
	@GetMapping("registerProduct")
	public String registerProductGET() {
		log.info("��ǰ ��� �������� �̵�");
		return "redirect:../product/register";
	} // end registerProductGET
}
