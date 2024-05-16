package com.web.vop.controller;


<<<<<<< HEAD
import java.util.List;

=======
>>>>>>> 0d996a6246facfcd9fb033cc5a67cd9ba9379427
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.vop.domain.DeliveryVO;
<<<<<<< HEAD
import com.web.vop.service.MemberService;
=======
import com.web.vop.service.DeliveryService;
>>>>>>> 0d996a6246facfcd9fb033cc5a67cd9ba9379427
import com.web.vop.service.OrderService;

import lombok.extern.log4j.Log4j;


@Controller
@RequestMapping("/delivery")
@Log4j
public class DeliveryController {
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	private DeliveryService deliveryService;
	
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
	
	/*
	 * @GetMapping("/register") public void registerGET() {
	 * log.info("��� ���� ��� ������ �̵�"); } // end registerGET
	 * 
	 * @PostMapping("/register") public String registerPOST(DeliveryVO deliveryVO,
	 * HttpServletRequest request) { log.info("��� ���� ���"); log.info("��� ���� : " +
	 * deliveryVO);
	 * 
	 * deliveryVO.setMemberId((String)request.getSession().getAttribute("memberId"))
	 * ;
	 * 
	 * int res = deliveryService.registerDelivery(deliveryVO); log.info(res +
	 * "�� ��� ����");
	 * 
	 * return "redirect:delivery"; } // end registerPOST
	 */	
}
