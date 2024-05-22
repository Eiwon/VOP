package com.web.vop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;

import com.web.vop.service.InquiryService;


import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/inquiry")
@Log4j
public class InquiryController {

	@Autowired
	private InquiryService inquiryService;
	
	@PostMapping("/delete") // DELETE : ���(����) ���� // ���߿� ������ �޴� �ſ� ���� �޶���
	public String deleteInquiry(Integer productId, String memberId){
		log.info("deleteInquiry()");
		
		int result = inquiryService.deleteInquiry(productId, memberId);
				
		log.info(result + "�� ���� �Ϸ�");
		return "redirect:../board/orderlist";
	}   
}
