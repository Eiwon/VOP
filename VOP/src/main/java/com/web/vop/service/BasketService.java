package com.web.vop.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.BasketDTO;
import com.web.vop.domain.BasketVO;
import com.web.vop.util.Pagination;

public interface BasketService {
	
	//내 장바구니 물품 목록 조회
	List<BasketDTO> getMyBasket(String memberId);
	
	//내 장바구니 조회 // 우제영 제작
	BasketVO getMyBasketList(int productId, String memberId);

	// 내 장바구기 물품 수 조회
	int getMyBasketCnt(String memberId);
	
	// 장바구니에 물품 추가
	int addToBasket(BasketVO basketVO);
	
	// 장바구니 물품 수량 변경
	int updateProductNum(BasketVO basketVO);
	
	// 장바구니 물품 삭제
	int removeFromBasket(int productId, String memberId);
	
	// 장바구니 비우기
	int clear(String memberId);
}
	