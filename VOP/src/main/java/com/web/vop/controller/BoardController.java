package com.web.vop.controller;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.vop.service.MemberService;

import lombok.extern.log4j.Log4j;

@RequestMapping("/board")
@Controller
@Log4j
public class BoardController {// 메인 페이지 구현 컨트롤러

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
	public String mypageGET(HttpServletRequest request) {
		System.out.println("mypage.jsp 이동");
		String path = "";
		log.info("mypageGET()");
		
		String memberId = (String)request.getSession().getAttribute("memberId");
		log.info("유저 아이디 : " + memberId);
		
		if(memberId == null) {
			path = "redirect:../member/login";
		}else {
			path = "board/mypage";
		}
		return path;
	}//end mypageGET()
	
	
	@Autowired
	private MemberService memberService;
	
	@PostMapping("/mypage")
	public String mypagePOST() {
		log.info("mypagePOST()");
		//if () {
			
		//}else {			
			return "redirect:/board/register";
		//}
	}
	
	
	// 장바구니 호출하기
	@GetMapping("/basket") 
	public void basketGET() {
		System.out.println("basket.jsp 이동");
		log.info("basketGET()");
	}//end basketGET()
	
	
	@PostMapping("/basket")
	public String basketPOST() {
		log.info("basketPOST()");
		
		return "redirect:/board/basket";
	}
	
	
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
	
	@GetMapping("/searchProduct")
	public String searchProductGET(String category, String word) {
		log.info("product/search로 redirect");
		log.info("카테고리 : " + category + " 검색어 : " + word);
		String path = "";
		
		try {
			path = URLEncoder.encode("redirect:../product/search?category=" + category + "&word=" + word, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return path;
	} // end searchProductGET
	
}//end MainController
