package com.web.vop.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.web.vop.domain.MemberDetails;
import com.web.vop.domain.MemberVO;
import com.web.vop.domain.OrderVO;
import com.web.vop.domain.OrderViewDTO;
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
import com.web.vop.util.PaymentAPIUtil;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/payment")
@Log4j
public class PaymentController {
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private PaymentAPIUtil paymentAPIUtil;
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/checkout")
	public void makePayment(
			Model model, int[] productIds, int[] productNums, @AuthenticationPrincipal MemberDetails memberDetails) {
		log.info("makePayment()");
		log.info("������ ��ǰ ���� : " + productIds.length);
		
		PaymentWrapper payment = paymentService.makePaymentForm(productIds, productNums, memberDetails.getUsername());
		
		try { // �ڹٽ�ũ��Ʈ���� ���� ���� json ���� ���ڿ��� ��ȯ
			model.addAttribute("paymentWrapper", new ObjectMapper().writeValueAsString(payment));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	} // end makePayment
	
	
	@GetMapping("/paymentResult")
	public void paymentResultGET(Model model, int paymentId , @AuthenticationPrincipal MemberDetails memberDetails) {
		log.info("paymentResult.jsp �̵� ��û");
		String memberId = memberDetails.getUsername();
		// �� �ֹ������� ��ü ���� ������ �ѹ��� ������ ���� ����
		PaymentWrapper paymentWrapper = paymentService.getPayment(memberId, paymentId);
		
		model.addAttribute("paymentWrapper", paymentWrapper);
		
	} // end paymentResultGET
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/getId")
	@ResponseBody
	public ResponseEntity<Integer> getNewPaymentId(){
		log.info("���ο� paymentId �߱� ��û");
		int paymentId = paymentService.getNewPaymentId();
		return new ResponseEntity<Integer>(paymentId, HttpStatus.OK);
	} // end getNewPaymentId
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/apply")
	@ResponseBody
	public ResponseEntity<Integer> savePaymentResult(@RequestBody PaymentWrapper paymentResult){
		log.info("---------���� ��� ����--------");
		log.info("���� ���� : " + paymentResult.getPaymentVO());
		log.info("�ֹ� ��� : " + paymentResult.getOrderList());
		log.info("���� ��� ���� : " + paymentResult.getMyCouponVO());
		
		PaymentVO paymentVO = paymentResult.getPaymentVO();
		String impUid = paymentVO.getChargeId();
		int chargePrice = paymentVO.getChargePrice();
		int res = 0;
		// ���� ������ ��ȿ���� �˻�
		// �ֹ� ��� �Ѿ��� �����ݾװ� ��ġ�ϴ��� Ȯ��
		int total = 0;
		for(OrderViewDTO orderDTO : paymentResult.getOrderList()) {
			total += orderDTO.getOrderVO().getProductPrice() * orderDTO.getOrderVO().getPurchaseNum();
		}
		total = (total + paymentVO.getDeliveryPrice()) * 
				(100 - paymentVO.getMembershipDiscount() - paymentVO.getCouponDiscount()) / 100;
				
		if(total != chargePrice) {
			// ���� ����
			log.error("���Ź�ǰ �Ѿ� : ������ ����ġ");
			paymentAPIUtil.cancelPayment(impUid); // ���� ���
			return new ResponseEntity<Integer>(res, HttpStatus.OK);
		}
		
		// ������ ���޵� �ݾ��� ���� ���� �ݾװ� ������ Ȯ��
		int realChargePrice = paymentAPIUtil.getPaymentAmount(impUid);
		if(realChargePrice != chargePrice) {
			// ���� ����
			log.error("�ǰ����� : ���� ���� ������ ����ġ");
			paymentAPIUtil.cancelPayment(impUid); // ���� ���
			return new ResponseEntity<Integer>(res, HttpStatus.OK);
		}
		
		try {
			res = paymentService.registerPayment(paymentResult); // ���� ��� ���
		}catch(DataIntegrityViolationException e) {
			log.error("DB ���� ���� : ��� ����");
			paymentAPIUtil.cancelPayment(impUid); // ���� ���
			res = -1;
		}
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end savePaymentResult
	
	@GetMapping("/popupDeliverySelect")
	public String popupDeliverySelect() {
		log.info("����� ���� �˾� ȣ��");
		return "redirect:../Delivery/popupSelect";
	} // end popupDeliverySelect
	
	
}
