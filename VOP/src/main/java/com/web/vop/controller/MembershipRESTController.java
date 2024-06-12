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

import com.web.vop.domain.MembershipVO;
import com.web.vop.service.MembershipService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/membership")
@Log4j
public class MembershipRESTController {

	@Autowired
	MembershipService membershipService;
	
	//멤버십 등록하기
	@PostMapping("/membershipRegister")
	public ResponseEntity<Integer> membershipPOST(@RequestBody String memberId){
		log.info("membershipPOST " + memberId);
		// 멤버십 등록 
		int res = membershipService.registerMembership(memberId);
		log.info("멤버십 등록 결과 : " + res);
		
		// 멤버십 정보 전체 조회
		MembershipVO vo = membershipService.selectByMemberId(memberId);
		log.info("멤버십 전체 조회 = " + vo);
		
		if (res == 1) {
			return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            return ResponseEntity.status(500).body(res);
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
