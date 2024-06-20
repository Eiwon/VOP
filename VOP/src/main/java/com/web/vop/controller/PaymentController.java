package com.web.vop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.vop.domain.MemberDetails;
import com.web.vop.domain.OrderViewDTO;
import com.web.vop.domain.PaymentVO;
import com.web.vop.domain.PaymentWrapper;
import com.web.vop.service.AWSS3Service;
import com.web.vop.service.PaymentService;
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
	
	@Autowired
	private AWSS3Service awsS3Service;
	
	@PostMapping("/checkout")
	public void makePayment(
			Model model, int[] productIds, int[] productNums, @AuthenticationPrincipal MemberDetails memberDetails) {
		log.info("makePayment()");
		log.info("������ ��ǰ ���� : " + productIds.length);
		
		PaymentWrapper payment = paymentService.makePaymentForm(productIds, productNums, memberDetails.getUsername());
		
		for(OrderViewDTO order : payment.getOrderList()) {
			order.setImgUrl(awsS3Service.toImageUrl(order.getImgPath(), order.getImgChangeName()));
		}
		log.info("���� ���� : " + payment);
		model.addAttribute("paymentWrapper", payment);
	} // end makePayment
	
	
	@GetMapping("/paymentResult")
	public void paymentResultGET(Model model, int paymentId , @AuthenticationPrincipal MemberDetails memberDetails) {
		log.info("paymentResult.jsp �̵� ��û");
		String memberId = memberDetails.getUsername();
		// �� �ֹ������� ��ü ���� ������ �ѹ��� ������ ���� ����
		PaymentWrapper paymentWrapper = paymentService.getPayment(memberId, paymentId);
		
		for(OrderViewDTO order : paymentWrapper.getOrderList()) {
			order.setImgUrl(awsS3Service.toImageUrl(order.getImgPath(), order.getImgChangeName()));
		}
		
		model.addAttribute("paymentWrapper", paymentWrapper);
		
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
	public ResponseEntity<Integer> savePaymentResult(@RequestBody PaymentWrapper paymentResult, @AuthenticationPrincipal UserDetails memberDetails){
		log.info("---------���� ��� ����--------");
		log.info("���� ���� : " + paymentResult.getPaymentVO());
		log.info("�ֹ� ��� : " + paymentResult.getOrderList());
		log.info("���� ��� ���� : " + paymentResult.getMyCouponVO());
		
		PaymentVO paymentVO = paymentResult.getPaymentVO();
		String impUid = paymentVO.getChargeId();
		String memberId = memberDetails.getUsername();
		int chargePrice = paymentVO.getChargePrice();
		int res = 0;
		// ���� ������ ��ȿ���� �˻�
		// �ֹ� ��� �Ѿ��� �����ݾװ� ��ġ�ϴ��� Ȯ��
		int total = 0;
		for(OrderViewDTO orderDTO : paymentResult.getOrderList()) {
			total += orderDTO.getOrderVO().getProductPrice() * orderDTO.getOrderVO().getPurchaseNum();
		}
		int totalDiscount = paymentVO.getMembershipDiscount() + paymentVO.getCouponDiscount();
		total = (totalDiscount >= 100) ? 0 : (total + paymentVO.getDeliveryPrice()) * (100 - totalDiscount) / 100;
		
		if(total != chargePrice) {
			// ���� ����
			log.error("���Ź�ǰ �Ѿ� " + total + "�� vs " + chargePrice + "�� : ������ ����ġ");
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
		paymentResult.getPaymentVO().setMemberId(memberId);
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
