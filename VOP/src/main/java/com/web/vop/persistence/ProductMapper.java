package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import com.web.vop.domain.ProductVO;
import com.web.vop.util.Pagination;

@Mapper
public interface ProductMapper {
	
	// 상품 상세 정보 검색
	ProductVO selectProduct(int productId);
	
	// 댓글 총 갯수 검색
	int selectReviewCount(int productId);
	
//	// 상품 메인 이미지 검색 
//	ProductVO selectByMainImg(int productId);
	
	// 상품 리뷰(별) 총합 검색
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
