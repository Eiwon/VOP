package com.web.vop.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.apigateway.model.Model;
import com.web.vop.domain.MembershipVO;
import com.web.vop.domain.PaymentVO;
import com.web.vop.domain.PaymentWrapper;
import com.web.vop.service.MembershipService;
import com.web.vop.service.PaymentService;
import com.web.vop.util.PaymentAPIUtil;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/membership")
@Log4j
public class MembershipRESTController {

	@Autowired
	MembershipService membershipService;
	
	@Autowired
	PaymentService paymentService;
	
	@Autowired
	private PaymentAPIUtil paymentAPIUtil;
	
	
	//���� ��ȸ
	@GetMapping("/getId")
	public ResponseEntity<Integer> getNextMembershipId(){
		log.info("���ο� paymentId �߱� ��û");
		int paymentId = paymentService.getNewPaymentId();
		log.info("paymentId : " + paymentId);
		return new ResponseEntity<Integer>(paymentId, HttpStatus.OK);
	}//end getNextMembershipId()
	
	
	//����� ����ϱ�
	@PostMapping("/membershipRegister")
	public ResponseEntity<Integer> membershipPOST(@RequestBody PaymentWrapper membershipResult){
		log.info("membershipPOST");
		log.info("--------- ���� ��� ���� --------");
		log.info("���� ���� : " + membershipResult.toString());
		
		MembershipVO membershipVO = membershipResult.getMembershipVO();
		log.info(membershipVO);
		
		String memberId = membershipVO.getMemberId(); // ����� ���̵�
		log.info("����� ���̵� :" + memberId);
		String impUid = membershipVO.getChargeId(); // ȯ�� ���̵�
		log.info("ȯ�� ���̵� :" + impUid);
		membershipVO.setChargeId(impUid);
		
		int chargePrice = membershipVO.getMembershipFee(); // �����ݾ� 
		log.info("���� �ݾ� :" + chargePrice);
		int res = 0;
		
		// membershipResult���� MembershipVO ��ü�� ������ chargeId�� ����
	    // �� ���� ���� ����ؾ� �ϴ� impUid ���� MembershipVO ��ü�� �����մϴ�.
		//membershipResult.getMembershipVO().setChargeId(impUid);
		
		if (membershipResult == null || membershipResult.getMembershipVO() == null) {
	        // ���� ó�� �Ǵ� ���� ������ ��ȯ�� �� �ֽ��ϴ�
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	    }
		
		if(chargePrice == res) {
			// ���� ����
			log.error("���� �ݾ� == 0");
			paymentAPIUtil.cancelPayment(impUid); // ���� ���
			return new ResponseEntity<Integer>(res, HttpStatus.OK);
		}
		
		// ����� ��� 
		int result = membershipService.registerMembership(membershipResult);
		log.info("����� ��� ��� : " + result);
		
		// ����� ���� ��ü ��ȸ
		MembershipVO vo = membershipService.selectByMemberId(memberId);
		log.info("����� ��ü ��ȸ = " + vo);
		
		if (result == 1) {
			return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return ResponseEntity.status(500).body(result);
        }
	}//end membershipPOST()
	
	
	// ����� ����(�����) ����
	@PutMapping("/updateAuth/{memberId}")
	public ResponseEntity<Void> MembershipAuthPUTOnInsert(@PathVariable("memberId") String memberId){
		log.info("����� ���� ����(�����) " + memberId);
		 try {
	            membershipService.updateMemberAuth(memberId);
	            return ResponseEntity.noContent().build();  // 204 No Content ��ȯ
	        } catch (Exception e) {
	            log.error("����� ���� ������Ʈ ����: " + e.getMessage());
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	        }
	    }//end membershipPUT()
	
	
	// ����� ���� �� ������ ��ȸ
	@GetMapping("getMembership/{memberId}")
	public ResponseEntity<Date> membershipGET(@PathVariable String memberId){
		log.info("membershipGET() : " + memberId);
		
		// ����� ���� ��ü ��ȸ
		MembershipVO vo = membershipService.selectByMemberId(memberId);
		log.info("����� ��ü ��ȸ = " + vo);
		
		Date expirydate = membershipService.getExpiryDate(memberId);
		log.info("����� ������ : " + expirydate);
		
		if (expirydate == null) {
	        // �������� ���� ���
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    } else {
	        // �������� �ִ� ���
	        return new ResponseEntity<>(expirydate, HttpStatus.OK);
	    }
	}//end membershipGET()
	
	
	// ����� ����(����)
	@DeleteMapping("deleteMembership/{memberId}")
	public ResponseEntity<Integer> membershipDELETE(@PathVariable String memberId){
		log.info("membershipDELETE()" + memberId);
		int res = membershipService.deleteMembership(memberId);
		
		if(res == 1) {
			return new ResponseEntity<>(res, HttpStatus.OK);
		}else {
			return ResponseEntity.status(500).body(res);
		}
	}//end membershipDELETE()
	
	
	// ����� ���� �� ����(�Ϲ�) ���� 
	@PutMapping("updateAuthOnUser/{memberId}")
	public ResponseEntity<Void> MembershipAuthPUTOnDelete(@PathVariable("memberId") String memberId){
		log.info("����� ���� ����(�Ϲ�) " + memberId);
		 try {
	            membershipService.updateMemberAuthOnDelete(memberId);
	            return ResponseEntity.noContent().build();  // 204 No Content ��ȯ
	        } catch (Exception e) {
	            log.error("����� ���� ������Ʈ ����: " + e.getMessage());
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	        }
	}//end membershipUpdate
	
	
}//end MembershipRESTController()
