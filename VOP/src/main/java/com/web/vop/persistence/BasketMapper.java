package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.BasketDTO;
import com.web.vop.domain.BasketVO;
import com.web.vop.util.Pagination;

@Mapper
public interface BasketMapper {
	
	//내 장바구니 물품 목록 조회
	List<BasketDTO> selectByMemberId(@Param("memberId") String memberId, @Param("pagination") Pagination pagination);
	
	// 내 장바구기 물품 수 조회
	int selectByMemberIdCnt(String memberId);
	
	// 장바구니에 물품 추가
	int insertToBasket(BasketVO basketVO);
	
	// 장바구니 물품 수량 변경
	int updateProductNum(BasketVO basketVO);
	
	// 장바구니 물품 삭제
	int deleteFromBasket(@Param("productId") int productId, @Param("memberId") String memberId);
	
	// 장바구니 비우기
	int deleteAll(String memberId);
	
	
	
}