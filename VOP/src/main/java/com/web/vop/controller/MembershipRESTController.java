package com.web.vop.controller;




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

import org.springframework.web.socket.WebSocketHandler;

import com.web.vop.domain.MemberIdRequest;
import com.web.vop.domain.MembershipVO;

import com.web.vop.domain.PaymentWrapper;
import com.web.vop.service.MembershipService;
import com.web.vop.service.PaymentService;
import com.web.vop.socket.AlarmHandler;
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
	
	@Autowired
	private WebSocketHandler alarmHandler;
	
	//���� ��ȸ
	@GetMapping("/getId")
	public ResponseEntity<Integer> getNextMembershipId(){
		log.info("���ο� paymentId �߱� ��û");
		int paymentId = paymentService.getNewPaymentId();
		log.info("paymentId : " + paymentId);
		return new ResponseEntity<Integer>(paymentId, HttpStatus.OK);
	}//end getNextMembershipId()
	
	
	// ����� ���� �� ����� ���� ��������
	@GetMapping("getMembership/{memberId}")
	public ResponseEntity<MembershipVO> membershipGET(@PathVariable String memberId){
		log.info("membershipGET() : " + memberId);
			
		// ����� ���� ��ü ��ȸ
		MembershipVO membershipVO = membershipService.selectByMemberId(memberId);
		log.info("����� ��ü ��ȸ = " + membershipVO);
			
		if (membershipVO == null) {
		     return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		 } else {
		     return new ResponseEntity<>(membershipVO, HttpStatus.OK);
		 }
	}//end membershipGET()
	
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
			// ������ ����Ǹ� �������� �˶��� �����ϴ�.
            // �˶��� ���� Ŭ���̾�Ʈ�� �ڵ����� ������ ������ ��û�� �����ϴ�.
            // - �ڽ��� userDetails(=principal) ������ �ٽ� �ε�
            // ��α������� �ʾƵ� ����� ������ ����˴ϴ�.
            ((AlarmHandler)alarmHandler).sendAuthUpdateAlarm(memberId, "������� ��ϵǾ����ϴ�.");
			return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return ResponseEntity.status(500).body(result);
        }
	}//end membershipPOST()
	
	
	// ����� ���� (�������)
	//@PreAuthorize("#request.memberId == authentication.principal.username")
	@PostMapping("/cancelPayment")
	public ResponseEntity<Integer> cancelPayment(@RequestBody MemberIdRequest request) {
			log.info("cancelPayment : " + request.getMemberId());
		try {
	        // memberId�� chargeId ��ȸ
	        String impUid = membershipService.getChargeIdByMemberId(request.getMemberId());
	        log.info("ȯ�Ҿ��̵� : " + impUid);
	        
	        // ���� ���� ��� ������ ȣ���ϴ� �κ�
	        paymentAPIUtil.cancelPayment(impUid);

	        // ���������� ���� ��ҵǾ��ٰ� �����ϰ� Ŭ���̾�Ʈ���� ���� ������ ����
	        return ResponseEntity.ok().body(HttpStatus.OK.value());
	    } catch (Exception e) {
	        // ���� ��� �� ���� �߻� ��, Ŭ���̾�Ʈ���� ���� ������ ����
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(HttpStatus.INTERNAL_SERVER_ERROR.value());
	    }
	}//end cancelPayment()
	
	
	
//	@PreAuthorize("#memberId == authentication.principal.username")
//	// ����� ����(�����) ����
//	@PutMapping("/updateAuth/{memberId}")
//	public ResponseEntity<Void> MembershipAuthPUTOnInsert(@PathVariable("memberId") String memberId){
//		log.info("����� ���� ����(�����) " + memberId);
//		 try {
//	            membershipService.updateMemberAuth(memberId);
//	            return ResponseEntity.noContent().build();  // 204 No Content ��ȯ
//	        } catch (Exception e) {
//	            log.error("����� ���� ������Ʈ ����: " + e.getMessage());
//	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//	        }
//	}//end membershipPUT()
	
	
	
	
	
	// ����� ����(����)
	@DeleteMapping("deleteMembership/{memberId}")
	public ResponseEntity<Integer> membershipDELETE(@PathVariable String memberId){
		log.info("membershipDELETE()" + memberId);
		int res = membershipService.deleteMembership(memberId);
		
		if(res == 1) {
			((AlarmHandler)alarmHandler).sendAuthUpdateAlarm(memberId, "������� �����Ǿ����ϴ�.");
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
