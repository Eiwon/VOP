package com.web.vop.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.vop.domain.CouponVO;
import com.web.vop.domain.DeliveryVO;
import com.web.vop.domain.MemberVO;
import com.web.vop.domain.OrderVO;
import com.web.vop.domain.PaymentVO;
import com.web.vop.domain.PaymentWrapper;
import com.web.vop.domain.ProductVO;
import com.web.vop.service.BasketService;
import com.web.vop.service.CouponService;
import com.web.vop.service.DeliveryService;
import com.web.vop.service.MemberService;
import com.web.vop.service.OrderService;
import com.web.vop.service.PaymentService;
import com.web.vop.service.ProductService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/payment")
@Log4j
public class PaymentController {
	
	@Autowired
	private PaymentService paymentService;
	
	@PostMapping("/checkout")
	public void makePayment(Model model, int[] productIds, int[] productNums, String memberId) {
		log.info("makePayment() - memberId : " + memberId);
		log.info("������ ��ǰ ���� : " + productIds.length);
		
		List<OrderVO> orderList = new ArrayList<>();
		
		PaymentWrapper payment = paymentService.makePaymentForm(productIds, productNums, memberId);
		
		try { // �ڹٽ�ũ��Ʈ���� ���� ���� json ���� ���ڿ��� ��ȯ
			model.addAttribute("paymentWrapper", new ObjectMapper().writeValueAsString(payment));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	} // end makePayment
	
	
	@GetMapping("/paymentResult")
	public void paymentResultGET() {
		log.info("paymentResult.jsp �̵� ��û");
	} // end paymentResultGET
	
	
	@GetMapping("/getId")
	@ResponseBody
	public ResponseEntity<Integer> getNewPaymentId(){
		log.info("���ο� paymentId �߱� ��û");
		int paymentId = paymentService.getNewPaymentId();
		return new ResponseEntity<Integer>(paymentId, HttpStatus.OK);
	} // end getNewPaymentId
	
	
	@PostMapping("/apply")
	@ResponseBody
	public ResponseEntity<Integer> savePaymentResult(@RequestBody PaymentWrapper paymentResult){
		log.info("---------���� ��� ����--------");
		log.info("���� ���� : " + paymentResult.getPaymentVO());
		log.info("�ֹ� ��� : " + paymentResult.getOrderList());
		log.info("���� ��� ���� : " + paymentResult.getCouponVO());
		
		int res = paymentService.registerPayment(paymentResult); // ���� ��� ���
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end savePaymentResult
	
	@GetMapping("/payment")
	@ResponseBody
	public ResponseEntity<PaymentWrapper> getPaymentResult(HttpServletRequest request){
		log.info("���� ��� ��ȸ ��û");
		String memberId = (String) request.getSession().getAttribute("memberId");// �� �ֹ������� ��ü ���� ������ �ѹ��� ������ ���� ����
		PaymentWrapper payment = paymentService.getRecentPayment(memberId);
		
		return new ResponseEntity<PaymentWrapper>(payment, HttpStatus.OK);
	} // end sendPaymentResult
	
	
	@GetMapping("/popupDeliverySelect")
	public String popupDeliverySelect() {
		log.info("����� ���� �˾� ȣ��");
		return "redirect:../Delivery/popupSelect";
	} // end popupDeliverySelect
	
	
}
