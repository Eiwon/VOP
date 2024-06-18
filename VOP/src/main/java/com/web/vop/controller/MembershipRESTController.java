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
	
	
	//결제 조회
	@GetMapping("/getId")
	public ResponseEntity<Integer> getNextMembershipId(){
		log.info("새로운 paymentId 발급 요청");
		int paymentId = paymentService.getNewPaymentId();
		log.info("paymentId : " + paymentId);
		return new ResponseEntity<Integer>(paymentId, HttpStatus.OK);
	}//end getNextMembershipId()
	
	
	//멤버십 등록하기
	@PostMapping("/membershipRegister")
	public ResponseEntity<Integer> membershipPOST(@RequestBody PaymentWrapper membershipResult){
		log.info("membershipPOST");
		log.info("--------- 결제 결과 저장 --------");
		log.info("결제 내역 : " + membershipResult.toString());
		
		MembershipVO membershipVO = membershipResult.getMembershipVO();
		log.info(membershipVO);
		
		String memberId = membershipVO.getMemberId(); // 사용자 아이디
		log.info("사용자 아이디 :" + memberId);
		String impUid = membershipVO.getChargeId(); // 환불 아이디
		log.info("환불 아이디 :" + impUid);
		membershipVO.setChargeId(impUid);
		
		int chargePrice = membershipVO.getMembershipFee(); // 결제금액 
		log.info("결제 금액 :" + chargePrice);
		int res = 0;
		
		// membershipResult에서 MembershipVO 객체를 가져와 chargeId를 설정
	    // 이 줄은 실제 사용해야 하는 impUid 값을 MembershipVO 객체에 설정합니다.
		//membershipResult.getMembershipVO().setChargeId(impUid);
		
		if (membershipResult == null || membershipResult.getMembershipVO() == null) {
	        // 예외 처리 또는 오류 응답을 반환할 수 있습니다
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	    }
		
		if(chargePrice == res) {
			// 결제 에러
			log.error("결제 금액 == 0");
			paymentAPIUtil.cancelPayment(impUid); // 결제 취소
			return new ResponseEntity<Integer>(res, HttpStatus.OK);
		}
		
		// 멤버십 등록 
		int result = membershipService.registerMembership(membershipResult);
		log.info("멤버십 등록 결과 : " + result);
		
		// 멤버십 정보 전체 조회
		MembershipVO vo = membershipService.selectByMemberId(memberId);
		log.info("멤버십 전체 조회 = " + vo);
		
		if (result == 1) {
			return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return ResponseEntity.status(500).body(result);
        }
	}//end membershipPOST()
	
	
	// 멤버십 권한(멤버십) 수정
	@PutMapping("/updateAuth/{memberId}")
	public ResponseEntity<Void> MembershipAuthPUTOnInsert(@PathVariable("memberId") String memberId){
		log.info("멤버십 권한 변경(멤버십) " + memberId);
		 try {
	            membershipService.updateMemberAuth(memberId);
	            return ResponseEntity.noContent().build();  // 204 No Content 반환
	        } catch (Exception e) {
	            log.error("멤버십 권한 업데이트 실패: " + e.getMessage());
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	        }
	    }//end membershipPUT()
	
	
	// 멤버십 성공 시 만료일 조회
	@GetMapping("getMembership/{memberId}")
	public ResponseEntity<Date> membershipGET(@PathVariable String memberId){
		log.info("membershipGET() : " + memberId);
		
		// 멤버십 정보 전체 조회
		MembershipVO vo = membershipService.selectByMemberId(memberId);
		log.info("멤버십 전체 조회 = " + vo);
		
		Date expirydate = membershipService.getExpiryDate(memberId);
		log.info("멤버십 만료일 : " + expirydate);
		
		if (expirydate == null) {
	        // 만료일이 없는 경우
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    } else {
	        // 만료일이 있는 경우
	        return new ResponseEntity<>(expirydate, HttpStatus.OK);
	    }
	}//end membershipGET()
	
	
	// 멤버십 삭제(해지)
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
	
	
	// 멤버십 삭제 시 권한(일반) 수정 
	@PutMapping("updateAuthOnUser/{memberId}")
	public ResponseEntity<Void> MembershipAuthPUTOnDelete(@PathVariable("memberId") String memberId){
		log.info("멤버십 권한 변경(일반) " + memberId);
		 try {
	            membershipService.updateMemberAuthOnDelete(memberId);
	            return ResponseEntity.noContent().build();  // 204 No Content 반환
	        } catch (Exception e) {
	            log.error("멤버십 권한 업데이트 실패: " + e.getMessage());
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	        }
	}//end membershipUpdate
	
	
}//end MembershipRESTController()
