package com.web.vop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.vop.service.MembershipService;

import lombok.extern.log4j.Log4j;

@RequestMapping("/membership")
@Controller
@Log4j
public class MembershipController {

	@Autowired
	MembershipService membershipService;
	
	@GetMapping("/register")
	public void registerGET() {
		log.info("멤버십 등록 페이지 이동");
	}
	
	
	
}
