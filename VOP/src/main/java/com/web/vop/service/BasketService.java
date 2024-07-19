package com.web.vop.service;

import java.util.List;


import com.web.vop.domain.BasketDTO;
import com.web.vop.domain.BasketVO;

public interface BasketService {
	
	//�� ��ٱ��� ��ǰ ��� ��ȸ
	List<BasketDTO> getMyBasket(String memberId);
	
	//�� ��ٱ��� ��ȸ // ������ ����
	BasketVO getMyBasketList(int productId, String memberId);
	
	// ��ٱ��� ��� or ����
	int createBasket(BasketVO basketVO);

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
	