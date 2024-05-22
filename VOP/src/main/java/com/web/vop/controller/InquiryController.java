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
	
	@PostMapping("/delete") // DELETE : 댓글(리뷰) 삭제 // 나중에 데이터 받는 거에 따라 달라짐
	public String deleteInquiry(Integer productId, String memberId){
		log.info("deleteInquiry()");
		
		int result = inquiryService.deleteInquiry(productId, memberId);
				
		log.info(result + "행 삭제 완료");
		return "redirect:../board/orderlist";
	}   
}
