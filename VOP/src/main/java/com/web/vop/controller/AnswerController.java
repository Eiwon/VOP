package com.web.vop.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.vop.domain.MemberDetails;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/answerNew")
@Log4j
public class AnswerController {
	
	@PreAuthorize("#memberDetails.username == authentication.principal.username")
	@GetMapping("/register")
	public void readAllAnswer(Model model, Integer productId, @AuthenticationPrincipal MemberDetails memberDetails) {
		log.info("readAllAnswer()");
		String memberId = memberDetails.getUsername();
		log.info("memberId : " + memberId);
		
		// 회원이 리뷰 작성 한 리스트
		model.addAttribute("productId", productId);
		
		// 회원이 작성한 리뷰
		model.addAttribute("memberId", memberId);
	}
}
