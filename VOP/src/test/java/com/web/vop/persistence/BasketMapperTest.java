package com.web.vop.persistence;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import com.web.vop.config.RootConfig;
import com.web.vop.config.ServletConfig;
import com.web.vop.config.WebConfig;
import com.web.vop.domain.BasketVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebConfig.class, RootConfig.class, ServletConfig.class})
@Log4j
public class BasketMapperTest {

	@Autowired
	private BasketMapper basketMapper;
	 
	@Test
	public void test() {
		String memberId = "test1234";
		int productId = 33;
		int productNum = 1;
		BasketVO basketVO = new BasketVO();
		basketVO.setMemberId(memberId);
		basketVO.setProductId(productId);
		basketVO.setProductNum(productNum);
		
//		selectByMemberId(memberId);
//	    selectByMemberIdList(productId, memberId);
//	    selectByMemberIdCnt(memberId);
//	    insertToBasket(basketVO);
//	    updateProductNum(basketVO);
//	    deleteFromBasket(productId, memberId);
//	    deleteAll(memberId);
	    updateExistProductNum(basketVO);
	} // end test
	
	//내 장바구니 물품 목록 조회
	public void selectByMemberId(String memberId) {
		log.info(basketMapper.selectByMemberId(memberId));
	} // end selectByMemberId
		
	// 내 장바구니 조회
	public void selectByMemberIdList(int productId, String memberId) {
		log.info(basketMapper.selectByMemberIdList(productId, memberId));
	} // end selectByMemberIdList
		
	// 내 장바구기 물품 수 조회
	public void selectByMemberIdCnt(String memberId) {
		log.info(basketMapper.selectByMemberIdCnt(memberId));
	} // end selectByMemberIdCnt
		
	// 장바구니에 물품 추가
	public void insertToBasket(BasketVO basketVO) {
		log.info(basketMapper.insertToBasket(basketVO));
	} // end insertToBasket
		
	// 장바구니 물품 수량 변경
	public void updateProductNum(BasketVO basketVO) {
		log.info(basketMapper.updateProductNum(basketVO));
	} // end updateProductNum
		
	// 장바구니 물품 삭제
	public void deleteFromBasket(int productId, String memberId) {
		log.info(basketMapper.deleteFromBasket(productId, memberId));
	} // end deleteFromBasket
		
	// 장바구니 비우기
	public void deleteAll(String memberId) {
		log.info(basketMapper.deleteAll(memberId));
	} // end deleteAll
		
	// 기존 상품 + 새로 등록 한 상품 갯수
	public void updateExistProductNum(BasketVO basketVO) {
		log.info(basketMapper.updateExistProductNum(basketVO));
	} // end updateExistProductNum
}
