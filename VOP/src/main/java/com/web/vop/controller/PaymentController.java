package com.web.vop.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.vop.domain.MemberVO;
import com.web.vop.domain.OrderVO;
import com.web.vop.domain.ProductVO;
import com.web.vop.service.MemberService;
import com.web.vop.service.ProductService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/payment")
@Log4j
public class PaymentController {

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private ProductService productService;
	
	@PostMapping("/checkout")
	public void makeOrders(Model model, int[] productIds, int[] productNums, String memberId) {
		log.info("makeOrders() - memberId : " + memberId);
		log.info("productId : " + productIds[0]);
		
		List<OrderVO> orderList = new ArrayList<>();
		
		// 유저 정보 검색
		MemberVO memberVO = memberService.getMemberInfo(memberId);
		
		// 배송지 정보 (미구현)
		
		
		// 상품 정보 검색 => 주문 정보 형태로 변환
		for(int i = 0; i < productIds.length; i++) {
			ProductVO productVO = productService.getProductById(productIds[i]);
			orderList.add(new OrderVO(
					0, 0, productVO.getProductId(), productVO.getProductName(), productVO.getProductPrice(), 
					productNums[i], null)
					);
		}
		
		try { // 자바스크립트에서 쓰기 위해 json 형식 문자열로 변환
			model.addAttribute("orderList", new ObjectMapper().writeValueAsString(orderList));
			model.addAttribute("memberVO", new ObjectMapper().writeValueAsString(memberVO));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		 
		// return 값 : MemberVO(유저 정보), 배송지 정보, OrderVO[](주문 정보)
	} // end toCheckout
	
}
