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
	
	//����� ����ϱ�
	@PostMapping("/membershipRegister")
	public ResponseEntity<Integer> membershipPOST(@RequestBody String memberId){
		log.info("membershipPOST " + memberId);
		// ����� ��� 
		int res = membershipService.registerMembership(memberId);
		log.info("����� ��� ��� : " + res);
		
		// ����� ���� ��ü ��ȸ
		MembershipVO vo = membershipService.selectByMemberId(memberId);
		log.info("����� ��ü ��ȸ = " + vo);
		
		if (res == 1) {
			return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            return ResponseEntity.status(500).body(res);
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
