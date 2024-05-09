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
		log.info("basket/main.jsp 이동");
	} // end basketMainGET
	
	
	// 내 장바구니 물품 목록 조회
	// 입력값 : memberId, pageNum
	// return : 장바구니 물품 리스트, 페이지 메이커
	@GetMapping("/myBasket")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getMyBasket(String memberId, Pagination pagination){
		log.info("getMyBasket() : " + memberId);
		
		// 페이징 처리를 위한 pageMaker 생성
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		pageMaker.setTotalCount(basketService.getMyBasketCnt(memberId));
		
		List<BasketDTO> basketList = basketService.getMyBasket(memberId, pagination);
		
		// 두가지 타입을 한번에 반환하기 위해 하나의 객체(Map)에 담음
		Map<String, Object> resultMap = new HashMap<>();
		
		resultMap.put("basketList", basketList);
		resultMap.put("pageMaker", pageMaker);
		
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	} // end getMyBasket
	
	// 장바구니에 물품 추가
	@PostMapping("/myBasket")
	@ResponseBody
	public ResponseEntity<Integer> addToBasket(BasketVO basketVO){
		log.info("addToBasket() : " + basketVO);
		int res = basketService.addToBasket(basketVO);
		log.info(res + "행 추가 성공");
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end addToBasket
	
	
	// 장바구니 물품 수량 변경
	@PutMapping("/myBasket")
	@ResponseBody
	public ResponseEntity<Integer> updateProductNum(BasketVO basketVO){
		log.info("updateProductNum() : " + basketVO);
		int res = basketService.updateProductNum(basketVO);
		log.info(res + "행 변경 성공");
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateProductNum
	
	// 장바구니 물품 삭제
	@DeleteMapping("/myBasket")
	@ResponseBody
	public ResponseEntity<Integer> deleteFromBasket(BasketVO basketVO){
		log.info("deleteFromBasket() : " + basketVO);
		int res = basketService.removeFromBasket(basketVO.getProductId(), basketVO.getMemberId());
		log.info(res + "행 변경 성공");
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateProductNum
	
	// 장바구니 비우기
	@DeleteMapping("/clear")
	@ResponseBody
	public ResponseEntity<Integer> clearBasket(String memberId){
		log.info("clearBasket() : " + memberId);
		int res = basketService.clear(memberId);
		log.info(res + "개 장바구니 초기화");
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end clearBasket
	
	
	
	
	
}



