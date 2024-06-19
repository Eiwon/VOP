package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.BasketDTO;
import com.web.vop.domain.BasketVO;

@Mapper
public interface BasketMapper {
	
	//�� ��ٱ��� ��ǰ ��� ��ȸ
	List<BasketDTO> selectByMemberId(String memberId);
	
	// �� ��ٱ��� ��ȸ
	BasketVO selectByMemberIdList(@Param("productId") int productId, @Param("memberId") String memberId);
	
	// �� ��ٱ��� ��ǰ �� ��ȸ
	int selectByMemberIdCnt(String memberId);
	
	// ��ٱ��Ͽ� ��ǰ �߰�
	int insertToBasket(BasketVO basketVO);
	
	// ��ٱ��� ��ǰ ���� ����
	int updateProductNum(BasketVO basketVO);
	
	// ��ٱ��� ��ǰ ����
	int deleteFromBasket(@Param("productId") int productId, @Param("memberId") String memberId);
	
	// ��ٱ��� ����
	int deleteAll(String memberId);
	
	// ���� ��ǰ + ���� ��� �� ��ǰ ����
	int updateExistProductNum(BasketVO basketVO);
	
	
	
}