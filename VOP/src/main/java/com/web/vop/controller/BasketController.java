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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	public ResponseEntity<List<BasketDTO>> getMyBasket(String memberId){
		log.info("getMyBasket() : " + memberId);
		
		List<BasketDTO> basketList = basketService.getMyBasket(memberId);
		
		return new ResponseEntity<List<BasketDTO>>(basketList, HttpStatus.OK);
	} // end getMyBasket
	
	// 장바구니에 물품 추가
	@PostMapping("/myBasket")
	@ResponseBody
	public ResponseEntity<Integer> addToBasket(@RequestBody BasketVO basketVO){
		log.info("addToBasket() : " + basketVO);
		int res = basketService.addToBasket(basketVO);
		log.info(res + "행 추가 성공");
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end addToBasket
	
	
	// 장바구니 물품 수량 변경
	@PutMapping("/myBasket")
	@ResponseBody
	public ResponseEntity<Integer> updateProductNum(@RequestBody BasketVO basketVO){
		log.info("updateProductNum() : " + basketVO);
		int res = basketService.updateProductNum(basketVO);
		log.info(res + "행 변경 성공");
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateProductNum
	
	// 장바구니 물품 삭제
	@DeleteMapping("/myBasket")
	@ResponseBody
	public ResponseEntity<Integer> deleteFromBasket(@RequestBody BasketVO basketVO){
		log.info("deleteFromBasket() : " + basketVO);
		int res = basketService.removeFromBasket(basketVO.getProductId(), basketVO.getMemberId());
		log.info(res + "행 변경 성공");
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateProductNum
	
	// 장바구니 비우기
	@DeleteMapping("/clear")
	@ResponseBody
	public ResponseEntity<Integer> clearBasket(@RequestBody String memberId){
		log.info("clearBasket() : " + memberId);
		int res = basketService.clear(memberId);
		log.info(res + "행 삭제 : 장바구니 초기화");
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end clearBasket
	
	// 장바구니 물품 다중 삭제
	@DeleteMapping("/multi/{memberId}")
	@ResponseBody
	public ResponseEntity<Integer> deleteMulti(@RequestBody int[] targetList, @PathVariable("memberId") String memberId){
		log.info("deleteMulti() : " + targetList[0]);
		int res = 0;
		
		for(int productId : targetList) {
			res += basketService.removeFromBasket(productId, memberId);
		}
		log.info(res + "행 삭제 성공");
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateProductNum
	
	// 상품상세 정보에서 버튼 클릭시 동작되는 코드// 우제영 제작 함
	@ResponseBody
	@PostMapping("/myBasketDate")
	public ResponseEntity<Integer> registerBasket(@RequestBody BasketVO basketVO){
		log.info("registerBasketPOST()");
		int dbProductId = basketVO.getProductId();
		String dbMemberId = basketVO.getMemberId();
		int dbProductNum =  basketVO.getProductNum();
		log.info("basketVO : " + basketVO);
		log.info("dbProductId : " + dbProductId);
		log.info("dbMemberId : " + dbMemberId);
		log.info("dbProductNum : " + dbProductNum);
		
		// 장바구니 리스트 검색
		BasketVO basketVOList = basketService.getMyBasketList(dbProductId, dbMemberId);
		log.info("basketVOList" + basketVOList);
		
		int res = 0;
		
		if(basketVOList == null) {
			log.info("장바구니 등록");
			res = basketService.addToBasket(basketVO);
		} else {
			basketVO.setProductNum(dbProductNum + basketVOList.getProductNum());
			log.info("ProductNum : " + basketVO.getProductNum());
			log.info("장바구니 수정");// 수량만 수정
			res = basketService.updateProductNum(basketVO);
		}
		log.info(res + "행 성공");
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	}// end registerBasket
	
}



