package com.web.vop.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.vop.service.MembershipService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/membership")
@Log4j
public class MembershipRESTController {

	@Autowired
	MembershipService membershipService;
	
	@PostMapping("/membershipRegisterr")
	public ResponseEntity<Integer> membershipPOST(@RequestBody String memberId){
		log.info("membershipPOST " + memberId);
		// 멤버십 등록 
		int res = membershipService.registerMembership(memberId);
		if (res == 1) {
			return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            return ResponseEntity.status(500).body(res);
        }
	}//end membershipPOST()
	
	
	@PutMapping("/updateAuth/{memberId}")
	public ResponseEntity<Void> membershipPUT(@PathVariable("memberId") String memberId){
		log.info("멤버십 권한 변경(멤버십) " + memberId);
		 try {
	            membershipService.updateMemberAuth(memberId);
	            return ResponseEntity.noContent().build();  // 204 No Content 반환
	        } catch (Exception e) {
	            log.error("멤버십 권한 업데이트 실패: " + e.getMessage());
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	        }
	    }//end membershipPUT()
	
	
	
	
	@GetMapping("getMembership/{memberId}")
	public ResponseEntity<Date> membershipGET(@PathVariable String memberId){
		log.info("membershipGET()" + memberId);
		Date expirydate = membershipService.getExpiryDate(memberId);
		return new ResponseEntity<Date>(expirydate, HttpStatus.OK);
	}//end membershipGET()
	
	
}
