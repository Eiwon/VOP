package com.web.vop.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.vop.domain.DeliveryVO;
import com.web.vop.service.MemberService;
import com.web.vop.service.OrderService;

import lombok.extern.log4j.Log4j;


@Controller
@RequestMapping("/Delivery")
@Log4j
public class DeliveryController {
	
	@Autowired
	OrderService orderService;
	
	// �������������� �ֹ���� > �����ȸ �������� �̵�
	@GetMapping("/delivery")
	public void deliveryGET() {
		log.info("delivery ������ �̵� ��û");
	}//end deliveryGET()
	
	@Autowired
	MemberService memberService;
	
	// ����� ���� �������� �̵�
		@GetMapping("/deliveryAddressList")
		public String deliveryAddressListGET(Model model, HttpServletRequest request) { // ����� ��� �ҷ�����
			log.info("deliveryAddressListGET() ������ �̵� ��û");
			
			String path = "";
			String memberId = (String)request.getSession().getAttribute("memberId");
			log.info("memberId : " + memberId);
			
			if(memberId == null) {
				path = "redirect:../member/login";
			}else {
				// ����� �� ��ȸ (�޴� ��� , �޴� �ּ� , �� �ּ� ,��� ��û ���� )
				List<DeliveryVO> deliveryList = orderService.getMemberId(memberId);
				log.info(deliveryList.toString());
				
				model.addAttribute("deliveryList", deliveryList);
				
				String memberAuth = memberService.getMemberAuth(memberId);
				model.addAttribute("memberAuth", memberAuth);
				
				path = "/Delivery/deliveryAddressList";
			}
			return path;
		}//end deliveryAddressListGET()
	
		
	// ����� ��� �������� �̵�
	@GetMapping("/deliveryRegister")
	public void deliveryRegisterGET() {
		log.info("deliveryRegister ������ �̵� ��û");
		
	}//end deliveryRegisterGET()
	
	
	
}
