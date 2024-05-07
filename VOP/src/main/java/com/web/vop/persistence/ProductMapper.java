package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import com.web.vop.domain.ProductVO;
import com.web.vop.util.Pagination;

@Mapper
public interface ProductMapper {
	
	// ��ǰ �� ���� �˻�
	ProductVO selectProduct(int productId);
	
	// ��� �� ���� �˻�
	int selectReviewCount(int productId);
	
//	// ��ǰ ���� �̹��� �˻� 
//	ProductVO selectByMainImg(int productId);
	
	// ��ǰ ����(��) ���� �˻�
	int selectReviewStar(int productId);
	
	int insertProduct(ProductVO productVO);
	
	List<ProductVO> selectByCategory(
			@Param("category") String category, Pagination pagination);
	
	List<ProductVO> selectByName(
			@Param("productName") String productName, Pagination pagination);
	
	List<ProductVO> selectByNameInCategory(
			@Param("category") String category, @Param("productName") String productName,
			Pagination pagination);
}
