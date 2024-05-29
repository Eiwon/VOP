package com.web.vop.controller;


import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.vop.domain.MemberDetails;
import com.web.vop.domain.MessageVO;
import com.web.vop.domain.OrderVO;
import com.web.vop.service.MemberService;
import com.web.vop.service.MessageService;
import com.web.vop.service.OrderService;

import lombok.extern.log4j.Log4j;

@RequestMapping("/board")
@Controller
@Log4j
public class BoardController {// 메인 페이지 구현 컨트롤러

	@Autowired
	private MessageService messageService;
	
	@GetMapping("/main") 
	public void mainGET() {
		System.out.println("main.jsp 이동");
		log.info("mainGET()");
	}//end mainGET()
	
	
	@PostMapping("/main")
	public String mainPOST() {
		log.info("mainPOST()");
		
		return "redirect:/board/main";
	}
	
	// 마이페이지 호출하기 -> 세션이 있을 경우 이동, 없으면 로그인 페이지로 이동
	@GetMapping("/mypage") 
	public void mypageGET(Model model) {
		System.out.println("mypage.jsp 이동");
		log.info("mypageGET()");
	}//end mypageGET()
	
	
	// 고객센터 호출하기
	@GetMapping("inquiry") 
	public void inquiryGET() {
		System.out.println("inquiry.jsp 이동");
		log.info("inquiryGET()");
	}//end inquiryGET()
	
	
	@PostMapping("/inquiry")
	public String inquiryPOST() {
		log.info("inquiryPOST()");
		
		return "redirect:/board/inquiry";
	}
	
	@GetMapping("/myInfo")
	public String myInfoGET() {
		log.info("member/modify로 redirect");
		return "redirect:../member/modify";
	} // end myInfoGet
	
	@GetMapping("/seller")
	public String sellerGET() {
		log.info("seller/sellerRequest로 redirect");
		return "redirect:../seller/sellerRequest";
	} // end myInfoGet
	
	@GetMapping("/admin")
	public String adminGET() {
		log.info("관리자 페이지로 이동");
		return "redirect:../seller/admin";
	} // end myInfoGet
	
	@GetMapping("/basket")
	public String basketGET() {
		log.info("장바구니 페이지 이동");
		return "redirect:../basket/main";
	} // end basketGET
	
	@GetMapping("/delivery")
	public String deliveryGET() {
		log.info("delivery controller로 redirection");
		return "redirect:../Delivery/delivery";
	} // end deliveryGET
	
	
	@GetMapping("/notice")
	@ResponseBody
	public ResponseEntity<List<MessageVO>> noticeGET() {
		log.info("모든 공지사항 요청");
		List<MessageVO> result = messageService.getNotice();
		return new ResponseEntity<>(result, HttpStatus.OK);
	} // end noticeGET
	
	@GetMapping("/popupNotice")
	public void popupNoticeGET(Model model, int messageId) {
		log.info("공지사항 정보 요청 : " + messageId);
		MessageVO message = messageService.getById(messageId);
		model.addAttribute("messageVO", message);
	} // end popupNoticeGET
	
}//end MainController
