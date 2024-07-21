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
	
	//�� ��ٱ��� ��ǰ ��� ��ȸ
	public void selectByMemberId(String memberId) {
		log.info(basketMapper.selectByMemberId(memberId));
	} // end selectByMemberId
		
	// �� ��ٱ��� ��ȸ
	public void selectByMemberIdList(int productId, String memberId) {
		log.info(basketMapper.selectByMemberIdList(productId, memberId));
	} // end selectByMemberIdList
		
	// �� ��ٱ��� ��ǰ �� ��ȸ
	public void selectByMemberIdCnt(String memberId) {
		log.info(basketMapper.selectByMemberIdCnt(memberId));
	} // end selectByMemberIdCnt
		
	// ��ٱ��Ͽ� ��ǰ �߰�
	public void insertToBasket(BasketVO basketVO) {
		log.info(basketMapper.insertToBasket(basketVO));
	} // end insertToBasket
		
	// ��ٱ��� ��ǰ ���� ����
	public void updateProductNum(BasketVO basketVO) {
		log.info(basketMapper.updateProductNum(basketVO));
	} // end updateProductNum
		
	// ��ٱ��� ��ǰ ����
	public void deleteFromBasket(int productId, String memberId) {
		log.info(basketMapper.deleteFromBasket(productId, memberId));
	} // end deleteFromBasket
		
	// ��ٱ��� ����
	public void deleteAll(String memberId) {
		log.info(basketMapper.deleteAll(memberId));
	} // end deleteAll
		
	// ���� ��ǰ + ���� ��� �� ��ǰ ����
	public void updateExistProductNum(BasketVO basketVO) {
		log.info(basketMapper.updateExistProductNum(basketVO));
	} // end updateExistProductNum
}
