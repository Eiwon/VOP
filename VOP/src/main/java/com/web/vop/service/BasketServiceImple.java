package com.web.vop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.vop.domain.BasketDTO;
import com.web.vop.domain.BasketVO;
import com.web.vop.persistence.BasketMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class BasketServiceImple implements BasketService {

	@Autowired
	BasketMapper basketMapper;
	
	@Override
	public List<BasketDTO> getMyBasket(String memberId) {
		log.info("getMyBasket()");
		return basketMapper.selectByMemberId(memberId);
	} // end getMyBasket

	@Override
	public int getMyBasketCnt(String memberId) {
		log.info("getMyBasketCnt()");
		return basketMapper.selectByMemberIdCnt(memberId);
	} // end getMyBasketCnt

	@Override
	public int addToBasket(BasketVO basketVO) {
		log.info("addToBasket()");
		return basketMapper.insertToBasket(basketVO);
	} // end addToBasket

	@Override
	public int updateProductNum(BasketVO basketVO) {
		log.info("updateProductNum()");
		return basketMapper.updateProductNum(basketVO);
	} // end updateProductNum

	@Override
	public int removeFromBasket(int productId, String memberId) {
		log.info("removeFromBasket()");
		return basketMapper.deleteFromBasket(productId, memberId);
	} // end removeFromBasket

	@Override
	public int clear(String memberId) {
		log.info("clear()");
		return basketMapper.deleteAll(memberId);
	} // end clear
	
	// ��ٱ��� �����͸� ��ȸ// �ϴ� ��� ����
	@Override
	public BasketVO getMyBasketList(int productId, String memberId) {
		log.info("getMyBasketList()");
		return basketMapper.selectByMemberIdList(productId, memberId);
	}
	
	// ��ٱ��� ��� or ����
	@Override
	public int createBasket(BasketVO basketVO) {
		// ��ٱ��� ����
		int res = basketMapper.updateExistProductNum(basketVO);
		
		// ��ٱ��� ������ �ȵǾ����� ���
		if(res == 0) {
			res = basketMapper.insertToBasket(basketVO);
			log.info(res + "�� ���");
		} else {
			log.info(res + "�� ����");
		}
		return res;
	}

}
