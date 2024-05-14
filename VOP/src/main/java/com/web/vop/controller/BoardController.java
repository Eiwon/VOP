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
public class BoardController {// ���� ������ ���� ��Ʈ�ѷ�

	@Autowired
	private MemberService memberService;
	
	@GetMapping("/main") 
	public void mainGET() {
		System.out.println("main.jsp �̵�");
		log.info("mainGET()");
	}//end mainGET()
	
	
	@PostMapping("/main")
	public String mainPOST() {
		log.info("mainPOST()");
		
		return "redirect:/board/main";
	}
	
	// ���������� ȣ���ϱ� -> ������ ���� ��� �̵�, ������ �α��� �������� �̵�
	@GetMapping("/mypage") 
	public String mypageGET(Model model, HttpServletRequest request) {
		System.out.println("mypage.jsp �̵�");
		String path = "";
		log.info("mypageGET()");
		
		String memberId = (String)request.getSession().getAttribute("memberId");
		log.info("���� ���̵� : " + memberId);
		
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
	public String orderlistGET(Model model, HttpServletRequest request) { // �ֹ���� ������ �ҷ�����
		System.out.println("orderlist.jsp �̵�");
		log.info("orderlistGET()");
		
		String path = "";
		String memberId = (String)request.getSession().getAttribute("memberId");
		log.info("memberId : "+memberId);
		
		if(memberId == null) {
			path = "redirect:../member/login";
		}else {
			// �ֹ� ��� �������� 
			List<OrderVO> orderList = orderService.getOrderListByMemberId(memberId);
			log.info("orderList : " + orderList);
			
			model.addAttribute("orderList", orderList);
			
			String memberAuth = memberService.getMemberAuth(memberId);
			model.addAttribute("memberAuth", memberAuth);
			
			path = "/board/orderlist";
		}
		return path;
	}//end orderlistGET()
	
	
	// ������ ȣ���ϱ�
	@GetMapping("inquiry") 
	public void inquiryGET() {
		System.out.println("inquiry.jsp �̵�");
		log.info("inquiryGET()");
	}//end inquiryGET()
	
	
	@PostMapping("/inquiry")
	public String inquiryPOST() {
		log.info("inquiryPOST()");
		
		return "redirect:/board/inquiry";
	}
	
	@GetMapping("/myInfo")
	public String myInfoGET() {
		log.info("member/modify�� redirect");
		return "redirect:../member/modify";
	} // end myInfoGet
	
	@GetMapping("/seller")
	public String sellerGET() {
		log.info("seller/sellerRequest�� redirect");
		return "redirect:../seller/sellerRequest";
	} // end myInfoGet
	
	@GetMapping("/admin")
	public void adminGET() {
		log.info("������ �������� �̵�");
	} // end myInfoGet
	
	@GetMapping("basket")
	public String basketGET() {
		log.info("��ٱ��� ������ �̵�");
		return "redirect:../basket/main";
	} // end basketGET
	
	
	
	
	
}//end MainController
