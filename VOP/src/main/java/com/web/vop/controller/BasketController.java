package com.web.vop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.vop.domain.BasketDTO;
import com.web.vop.domain.BasketVO;
import com.web.vop.domain.MemberDetails;
import com.web.vop.service.AWSS3Service;
import com.web.vop.service.BasketService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/basket")
@Log4j
public class BasketController {

	@Autowired
	BasketService basketService;
	
	@Autowired
	AWSS3Service awsS3Service;
	
	@GetMapping("/main")
	public void basketMainGET() {
		log.info("basket/main.jsp 이동");
	} // end basketMainGET
	
	
	// 내 장바구니 물품 목록 조회
	// 입력값 : memberId, pageNum
	// return : 장바구니 물품 리스트
	@GetMapping("/myBasket")
	@ResponseBody
	public ResponseEntity<List<BasketDTO>> getMyBasket(@AuthenticationPrincipal MemberDetails memberDetails){
		log.info("getMyBasket()");
		List<BasketDTO> basketList = basketService.getMyBasket(memberDetails.getUsername());
		for(BasketDTO basketDTO : basketList) {
			basketDTO.getProductPreviewDTO().setImgUrl(
					awsS3Service.toImageUrl(basketDTO.getProductPreviewDTO().getImgPath(), basketDTO.getProductPreviewDTO().getImgChangeName())
					); 
		}
		
		log.info(basketList);
		return new ResponseEntity<List<BasketDTO>>(basketList, HttpStatus.OK);
	} // end getMyBasket
	
	// 장바구니에 물품 추가
	@PostMapping("/myBasket")
	@ResponseBody
	public ResponseEntity<Integer> addToBasket(@RequestBody BasketVO basketVO, @AuthenticationPrincipal MemberDetails memberDetails){
		basketVO.setMemberId(memberDetails.getUsername());
		log.info("addToBasket() : " + basketVO);
		int res = basketService.addToBasket(basketVO);
		log.info(res + "행 추가 성공");
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end addToBasket
	
	
	// 장바구니 물품 수량 변경
	@PutMapping("/myBasket")
	@ResponseBody
	public ResponseEntity<Integer> updateProductNum(@RequestBody BasketVO basketVO, @AuthenticationPrincipal MemberDetails memberDetails){
		basketVO.setMemberId(memberDetails.getUsername());
		log.info("updateProductNum() : " + basketVO);
		int res = basketService.updateProductNum(basketVO);
		log.info(res + "행 변경 성공");
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateProductNum
	
	// 장바구니 물품 삭제
	@DeleteMapping("/myBasket")
	@ResponseBody
	public ResponseEntity<Integer> deleteFromBasket(@RequestBody BasketVO basketVO, @AuthenticationPrincipal MemberDetails memberDetails){
		basketVO.setMemberId(memberDetails.getUsername());
		log.info("deleteFromBasket() : " + basketVO);
		int res = basketService.removeFromBasket(basketVO.getProductId(), basketVO.getMemberId());
		log.info(res + "행 변경 성공");
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateProductNum
	
	// 장바구니 비우기
	@DeleteMapping("/clear")
	@ResponseBody
	public ResponseEntity<Integer> clearBasket(@AuthenticationPrincipal MemberDetails memberDetails){
		log.info("clearBasket()");
		int res = basketService.clear(memberDetails.getUsername());
		log.info(res + "행 삭제 : 장바구니 초기화");
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end clearBasket
	
	// 장바구니 물품 다중 삭제
	@DeleteMapping("/multi")
	@ResponseBody
	public ResponseEntity<Integer> deleteMulti(@RequestBody int[] targetList, @AuthenticationPrincipal MemberDetails memberDetails){
		log.info("deleteMulti() : " + targetList[0]);
		int res = 0;
		
		for(int productId : targetList) {
			res += basketService.removeFromBasket(productId, memberDetails.getUsername());
		}
		log.info(res + "행 삭제 성공");
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateProductNum
	
	// 상품상세 정보에서 버튼 클릭시 동작되는 코드// 우제영 제작 함
	@ResponseBody
	@PostMapping("/myBasketDate")
	public ResponseEntity<Integer> registerBasket(@RequestBody BasketVO basketVO){
		log.info("registerBasketPOST()");
		log.info("basketVO : " + basketVO);
		int res = basketService.createBasket(basketVO);

		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	}// end registerBasket
	
}



