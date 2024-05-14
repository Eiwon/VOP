package com.web.vop.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.vop.domain.OrderVO;
import com.web.vop.domain.SellerVO;
import com.web.vop.service.MemberService;
import com.web.vop.service.OrderService;
import com.web.vop.util.PageMaker;
import com.web.vop.util.Pagination;

import lombok.extern.log4j.Log4j;

@RequestMapping("/board")
@Controller
@Log4j
public class BoardController {// 메인 페이지 구현 컨트롤러

	@Autowired
	private MemberService memberService;
	
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
	public String mypageGET(Model model, HttpServletRequest request) {
		System.out.println("mypage.jsp 이동");
		String path = "";
		log.info("mypageGET()");
		
		String memberId = (String)request.getSession().getAttribute("memberId");
		log.info("유저 아이디 : " + memberId);
		
		if(memberId == null) {
			path = "redirect:../member/login";
		}else {
			String memberAuth = memberService.getMemberAuth(memberId);
			model.addAttribute("memberAuth", memberAuth);
			path = "board/mypage";
		}
		return path;
	}//end mypageGET()
	
	
	@PostMapping("/mypage")
	public String mypagePOST() {
		log.info("mypagePOST()");
		//if () {
			
		//}else {			
			return "redirect:/board/register";
		//}
	}
	
	@Autowired
	OrderService orderService;
	
	@GetMapping("/orderlist") 
	public String orderlistGET(Model model, HttpServletRequest request) { // 주문목록 페이지 불러오기
		System.out.println("orderlist.jsp 이동");
		log.info("orderlistGET()");
		
		String path = "";
		String memberId = (String)request.getSession().getAttribute("memberId");
		log.info("memberId : "+memberId);
		
		if(memberId == null) {
			path = "redirect:../member/login";
		}else {
			// 주문 목록 가져오기 
			List<OrderVO> orderList = orderService.getOrderListByMemberId(memberId);
			log.info("orderList : " + orderList);
			
			model.addAttribute("orderList", orderList);
			
			String memberAuth = memberService.getMemberAuth(memberId);
			model.addAttribute("memberAuth", memberAuth);
			
			path = "/board/orderlist";
		}
		return path;
	}//end orderlistGET()
	
	
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
	public void adminGET() {
		log.info("관리자 페이지로 이동");
	} // end myInfoGet
	
	@GetMapping("basket")
	public String basketGET() {
		log.info("장바구니 페이지 이동");
		return "redirect:../basket/main";
	} // end basketGET
	
	
	
	
	
}//end MainController
