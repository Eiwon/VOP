package com.web.vop.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.BasketDTO;
import com.web.vop.domain.BasketVO;
import com.web.vop.util.Pagination;

public interface BasketService {
	
	//�� ��ٱ��� ��ǰ ��� ��ȸ
	List<BasketDTO> getMyBasket(String memberId);
	
	//�� ��ٱ��� ��ȸ // ������ ����
	BasketVO getMyBasketList(int productId, String memberId);

	// �� ��ٱ��� ��ǰ �� ��ȸ
	int getMyBasketCnt(String memberId);
	
	// ��ٱ��Ͽ� ��ǰ �߰�
	int addToBasket(BasketVO basketVO);
	
	// ��ٱ��� ��ǰ ���� ����
	int updateProductNum(BasketVO basketVO);
	
	// ��ٱ��� ��ǰ ����
	int removeFromBasket(int productId, String memberId);
	
	// ��ٱ��� ����
	int clear(String memberId);
}
	