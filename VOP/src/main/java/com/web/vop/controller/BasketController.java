package com.web.vop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.vop.domain.BasketDTO;
import com.web.vop.domain.BasketVO;
import com.web.vop.service.BasketService;
import com.web.vop.util.PageMaker;
import com.web.vop.util.Pagination;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/basket")
@Log4j
public class BasketController {

	@Autowired
	BasketService basketService;
	
	@GetMapping("/main")
	public void basketMainGET() {
		log.info("basket/main.jsp �̵�");
	} // end basketMainGET
	
	
	// �� ��ٱ��� ��ǰ ��� ��ȸ
	// �Է°� : memberId, pageNum
	// return : ��ٱ��� ��ǰ ����Ʈ, ������ ����Ŀ
	@GetMapping("/myBasket")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getMyBasket(String memberId, Pagination pagination){
		log.info("getMyBasket() : " + memberId);
		
		// ����¡ ó���� ���� pageMaker ����
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		pageMaker.setTotalCount(basketService.getMyBasketCnt(memberId));
		
		List<BasketDTO> basketList = basketService.getMyBasket(memberId, pagination);
		
		// �ΰ��� Ÿ���� �ѹ��� ��ȯ�ϱ� ���� �ϳ��� ��ü(Map)�� ����
		Map<String, Object> resultMap = new HashMap<>();
		
		resultMap.put("basketList", basketList);
		resultMap.put("pageMaker", pageMaker);
		
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	} // end getMyBasket
	
	// ��ٱ��Ͽ� ��ǰ �߰�
	@PostMapping("/myBasket")
	@ResponseBody
	public ResponseEntity<Integer> addToBasket(BasketVO basketVO){
		log.info("addToBasket() : " + basketVO);
		int res = basketService.addToBasket(basketVO);
		log.info(res + "�� �߰� ����");
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end addToBasket
	
	
	// ��ٱ��� ��ǰ ���� ����
	@PutMapping("/myBasket")
	@ResponseBody
	public ResponseEntity<Integer> updateProductNum(BasketVO basketVO){
		log.info("updateProductNum() : " + basketVO);
		int res = basketService.updateProductNum(basketVO);
		log.info(res + "�� ���� ����");
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateProductNum
	
	// ��ٱ��� ��ǰ ����
	@DeleteMapping("/myBasket")
	@ResponseBody
	public ResponseEntity<Integer> deleteFromBasket(BasketVO basketVO){
		log.info("deleteFromBasket() : " + basketVO);
		int res = basketService.removeFromBasket(basketVO.getProductId(), basketVO.getMemberId());
		log.info(res + "�� ���� ����");
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateProductNum
	
	// ��ٱ��� ����
	@DeleteMapping("/clear")
	@ResponseBody
	public ResponseEntity<Integer> clearBasket(String memberId){
		log.info("clearBasket() : " + memberId);
		int res = basketService.clear(memberId);
		log.info(res + "�� ��ٱ��� �ʱ�ȭ");
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end clearBasket
	
	
	
	
	
}



