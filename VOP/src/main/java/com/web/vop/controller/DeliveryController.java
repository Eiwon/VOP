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
	
	// 마이페이지에서 주문목록 > 배송조회 페이지로 이동
	@GetMapping("/delivery")
	public void deliveryGET() {
		log.info("delivery 페이지 이동 요청");
	}//end deliveryGET()
	
	@Autowired
	MemberService memberService;
	
	// 배송지 관리 페이지로 이동
		@GetMapping("/deliveryAddressList")
		public String deliveryAddressListGET(Model model, HttpServletRequest request) { // 배송지 목록 불러오기
			log.info("deliveryAddressListGET() 페이지 이동 요청");
			
			String path = "";
			String memberId = (String)request.getSession().getAttribute("memberId");
			log.info("memberId : " + memberId);
			
			if(memberId == null) {
				path = "redirect:../member/login";
			}else {
				// 배송지 상세 조회 (받는 사람 , 받는 주소 , 상세 주소 ,배송 요청 사항 )
				List<DeliveryVO> deliveryList = orderService.getMemberId(memberId);
				log.info(deliveryList.toString());
				
				model.addAttribute("deliveryList", deliveryList);
				
				String memberAuth = memberService.getMemberAuth(memberId);
				model.addAttribute("memberAuth", memberAuth);
				
				path = "/Delivery/deliveryAddressList";
			}
			return path;
		}//end deliveryAddressListGET()
	
		
	// 배송지 등록 페이지로 이동
	@GetMapping("/deliveryRegister")
	public void deliveryRegisterGET() {
		log.info("deliveryRegister 페이지 이동 요청");
		
	}//end deliveryRegisterGET()
	
	/*
	 * @GetMapping("/register") public void registerGET() {
	 * log.info("배송 정보 등록 페이지 이동"); } // end registerGET
	 * 
	 * @PostMapping("/register") public String registerPOST(DeliveryVO deliveryVO,
	 * HttpServletRequest request) { log.info("배송 정보 등록"); log.info("등록 정보 : " +
	 * deliveryVO);
	 * 
	 * deliveryVO.setMemberId((String)request.getSession().getAttribute("memberId"))
	 * ;
	 * 
	 * int res = deliveryService.registerDelivery(deliveryVO); log.info(res +
	 * "행 등록 성공");
	 * 
	 * return "redirect:delivery"; } // end registerPOST
	 */	
}
