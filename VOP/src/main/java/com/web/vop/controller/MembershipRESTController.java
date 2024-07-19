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
	
	//결제 조회
	@GetMapping("/getId")
	public ResponseEntity<Integer> getNextMembershipId(){
		log.info("새로운 paymentId 발급 요청");
		int paymentId = paymentService.getNewPaymentId();
		log.info("paymentId : " + paymentId);
		return new ResponseEntity<Integer>(paymentId, HttpStatus.OK);
	}//end getNextMembershipId()
	
	
	// 멤버십 성공 시 멤버십 정보 가져오기
	@GetMapping("getMembership/{memberId}")
	public ResponseEntity<MembershipVO> membershipGET(@PathVariable String memberId){
		log.info("membershipGET() : " + memberId);
			
		// 멤버십 정보 전체 조회
		MembershipVO membershipVO = membershipService.selectByMemberId(memberId);
		log.info("멤버십 전체 조회 = " + membershipVO);
			
		if (membershipVO == null) {
		     return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		 } else {
		     return new ResponseEntity<>(membershipVO, HttpStatus.OK);
		 }
	}//end membershipGET()
	
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
			// 권한이 변경되면 유저에게 알람을 보냅니다.
            // 알람을 받은 클라이언트는 자동으로 서버에 다음의 요청을 보냅니다.
            // - 자신의 userDetails(=principal) 정보를 다시 로드
            // 재로그인하지 않아도 변경된 권한이 적용됩니다.
            ((AlarmHandler)alarmHandler).sendAuthUpdateAlarm(memberId, "멤버십이 등록되었습니다.");
			return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return ResponseEntity.status(500).body(result);
        }
	}//end membershipPOST()
	
	
	// 멤버십 해지 (결제취소)
	//@PreAuthorize("#request.memberId == authentication.principal.username")
	@PostMapping("/cancelPayment")
	public ResponseEntity<Integer> cancelPayment(@RequestBody MemberIdRequest request) {
			log.info("cancelPayment : " + request.getMemberId());
		try {
	        // memberId로 chargeId 조회
	        String impUid = membershipService.getChargeIdByMemberId(request.getMemberId());
	        log.info("환불아이디 : " + impUid);
	        
	        // 실제 결제 취소 로직을 호출하는 부분
	        paymentAPIUtil.cancelPayment(impUid);

	        // 성공적으로 결제 취소되었다고 가정하고 클라이언트에게 성공 응답을 보냄
	        return ResponseEntity.ok().body(HttpStatus.OK.value());
	    } catch (Exception e) {
	        // 결제 취소 중 예외 발생 시, 클라이언트에게 실패 응답을 보냄
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(HttpStatus.INTERNAL_SERVER_ERROR.value());
	    }
	}//end cancelPayment()
	
	
	
//	@PreAuthorize("#memberId == authentication.principal.username")
//	// 멤버십 권한(멤버십) 수정
//	@PutMapping("/updateAuth/{memberId}")
//	public ResponseEntity<Void> MembershipAuthPUTOnInsert(@PathVariable("memberId") String memberId){
//		log.info("멤버십 권한 변경(멤버십) " + memberId);
//		 try {
//	            membershipService.updateMemberAuth(memberId);
//	            return ResponseEntity.noContent().build();  // 204 No Content 반환
//	        } catch (Exception e) {
//	            log.error("멤버십 권한 업데이트 실패: " + e.getMessage());
//	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//	        }
//	}//end membershipPUT()
	
	
	
	
	
	// 멤버십 삭제(해지)
	@DeleteMapping("deleteMembership/{memberId}")
	public ResponseEntity<Integer> membershipDELETE(@PathVariable String memberId){
		log.info("membershipDELETE()" + memberId);
		int res = membershipService.deleteMembership(memberId);
		
		if(res == 1) {
			((AlarmHandler)alarmHandler).sendAuthUpdateAlarm(memberId, "멤버십이 해지되었습니다.");
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
