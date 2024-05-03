package com.web.vop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.web.vop.domain.SellerVO;
import com.web.vop.service.SellerService;
import com.web.vop.util.PageMaker;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/seller")
@Log4j
public class SellerRESTController {

	@Autowired
	SellerService sellerService;
	
	// �Ǹ��� ���� ��û ��ȸ
	@GetMapping("/all/{page}")
	public ResponseEntity<Map<String, Object>> getAllRequest(@PathVariable("page") int page){
		log.info("��� ���ѿ�û ��ȸ");
		PageMaker pageMaker = new PageMaker();
		pageMaker.getPagination().setPageNum(page);
		List<SellerVO> list = sellerService.getAllRequest(pageMaker.getPagination());
		log.info(list);
		int requestCount = sellerService.getRequestCount();
		pageMaker.setPageCount(requestCount);
		
		Map<String, Object> resultMap = new HashMap<>(); // ��ȯ�� Ÿ���� 2���̹Ƿ� pageMaker�� list�� ���� �� ����
		resultMap.put("pageMaker", pageMaker);
		resultMap.put("list", list);
		
		return new ResponseEntity<Map<String,Object>>(resultMap, HttpStatus.OK);
	} // end getAllRequest
	
	
	// �ڽ��� ���� ��û ��ȸ
	@GetMapping("/my/{memberId}")
	public ResponseEntity<SellerVO> getMyRequest(@PathVariable("memberId") String memberId){
		log.info("�� ���ѿ�û ��ȸ");
		SellerVO result = sellerService.getMyRequest(memberId);
		
		return new ResponseEntity<SellerVO>(result, HttpStatus.OK);
	} // end getMyRequest
	
	
	// ��û ���
	@PostMapping("/register")
	public ResponseEntity<Integer> registerRequest(@RequestBody SellerVO sellerVO){
		log.info("��û ��� : " + sellerVO);
		int res = sellerService.registerRequest(sellerVO);
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end registerRequest
	
	// ��û ����(����)
	@PutMapping("/update")
	public ResponseEntity<Integer> updateRequest(@RequestBody SellerVO sellerVO){
		log.info("��û ���� : " + sellerVO);
		int res = sellerService.updateMemberContent(sellerVO);
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateRequest
	
	// ��û ����(������)
	@PutMapping("/refuse")
	public ResponseEntity<Integer> refuseRequest(@RequestBody SellerVO sellerVO){
		log.info("��û ���� : " + sellerVO.getMemberId());
		int res = sellerService.refuseRequest(sellerVO.getMemberId(), sellerVO.getRefuseMsg());
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end refuseRequest
	
	// ��û ����
	@DeleteMapping("/delete/{memberId}")
	public ResponseEntity<Integer> deleteRequest(@PathVariable("memberId") String memberId){
		log.info("��û ���� : " + memberId);
		int res = sellerService.deleteRequest(memberId);
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end deleteRequest
	
}
