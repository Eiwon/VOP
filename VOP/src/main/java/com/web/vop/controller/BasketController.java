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
		log.info("basket/main.jsp �̵�");
	} // end basketMainGET
	
	
	// �� ��ٱ��� ��ǰ ��� ��ȸ
	// �Է°� : memberId, pageNum
	// return : ��ٱ��� ��ǰ ����Ʈ, ������ ����Ŀ
	@GetMapping("/myBasket")
	@ResponseBody
	public ResponseEntity<List<BasketDTO>> getMyBasket(String memberId){
		log.info("getMyBasket() : " + memberId);
		
		List<BasketDTO> basketList = basketService.getMyBasket(memberId);
		
		return new ResponseEntity<List<BasketDTO>>(basketList, HttpStatus.OK);
	} // end getMyBasket
	
	// ��ٱ��Ͽ� ��ǰ �߰�
	@PostMapping("/myBasket")
	@ResponseBody
	public ResponseEntity<Integer> addToBasket(@RequestBody BasketVO basketVO){
		log.info("addToBasket() : " + basketVO);
		int res = basketService.addToBasket(basketVO);
		log.info(res + "�� �߰� ����");
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end addToBasket
	
	
	// ��ٱ��� ��ǰ ���� ����
	@PutMapping("/myBasket")
	@ResponseBody
	public ResponseEntity<Integer> updateProductNum(@RequestBody BasketVO basketVO){
		log.info("updateProductNum() : " + basketVO);
		int res = basketService.updateProductNum(basketVO);
		log.info(res + "�� ���� ����");
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateProductNum
	
	// ��ٱ��� ��ǰ ����
	@DeleteMapping("/myBasket")
	@ResponseBody
	public ResponseEntity<Integer> deleteFromBasket(@RequestBody BasketVO basketVO){
		log.info("deleteFromBasket() : " + basketVO);
		int res = basketService.removeFromBasket(basketVO.getProductId(), basketVO.getMemberId());
		log.info(res + "�� ���� ����");
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateProductNum
	
	// ��ٱ��� ����
	@DeleteMapping("/clear")
	@ResponseBody
	public ResponseEntity<Integer> clearBasket(@RequestBody String memberId){
		log.info("clearBasket() : " + memberId);
		int res = basketService.clear(memberId);
		log.info(res + "�� ���� : ��ٱ��� �ʱ�ȭ");
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end clearBasket
	
	// ��ٱ��� ��ǰ ���� ����
	@DeleteMapping("/multi/{memberId}")
	@ResponseBody
	public ResponseEntity<Integer> deleteMulti(@RequestBody int[] targetList, @PathVariable("memberId") String memberId){
		log.info("deleteMulti() : " + targetList[0]);
		int res = 0;
		
		for(int productId : targetList) {
			res += basketService.removeFromBasket(productId, memberId);
		}
		log.info(res + "�� ���� ����");
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateProductNum
	
	// ��ǰ�� �������� ��ư Ŭ���� ���۵Ǵ� �ڵ�// ������ ���� ��
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
		
		// ��ٱ��� ����Ʈ �˻�
		BasketVO basketVOList = basketService.getMyBasketList(dbProductId, dbMemberId);
		log.info("basketVOList" + basketVOList);
		
		int res = 0;
		
		if(basketVOList == null) {
			log.info("��ٱ��� ���");
			res = basketService.addToBasket(basketVO);
		} else {
			basketVO.setProductNum(dbProductNum + basketVOList.getProductNum());
			log.info("ProductNum : " + basketVO.getProductNum());
			log.info("��ٱ��� ����");// ������ ����
			res = basketService.updateProductNum(basketVO);
		}
		log.info(res + "�� ����");
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	}// end registerBasket
	
}



