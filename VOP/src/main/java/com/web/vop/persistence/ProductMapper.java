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
	
	
	// 상품 등록
	int insertProduct(ProductVO productVO);
	
	// 방금 등록한 상품 id 검색
	int selectLastInsertId();
	
	// 카테고리로 검색
	List<ProductVO> selectByCategory(
			@Param("category") String category, @Param("pagination") Pagination pagination);
	
	// 카테고리로 검색 결과 수량
	int selectByCategoryCnt(String category);
	
	// 이름에 검색어가 포함된 상품 검색
	List<ProductVO> selectByName(
			@Param("productName") String productName, @Param("pagination") Pagination pagination);
	
	// 이름에 검색어가 포함된 상품 검색 결과 수량
	int selectByNameCnt(String productName);
	
	// 카테고리 내에서, 이름에 검색어가 포함된 상품 검색
	List<ProductVO> selectByNameInCategory(
			@Param("category") String category, @Param("productName") String productName,
			@Param("pagination") Pagination pagination);
	
	// 카테고리 내에서, 이름에 검색어가 포함된 상품 검색 결과 수량
	int selectByNameInCategoryCnt(@Param("category") String category, @Param("productName") String productName);
	
	// memberId로 상품 조회
	List<ProductVO> selectByMemberId(@Param("memberId") String memberId, @Param("pagination") Pagination pagination);
	
	// memberId로 상품 갯수 조회
	int selectByMemberIdCnt(String memberId);
	
	// productId로 상품 상태 변경
	int updateState(@Param("productState") String productState, @Param("productId") int productId);
	
	// productId로 상품 상태 검색
	String selectStateByProductId(int productId);
	
	// productId로 상품 삭제
	int deleteProduct(int productId);
	
	// 카테고리를 지정하여, 리뷰 수가 가장 많은 5개의 상품 검색
	List<ProductVO> selectTopProductInCategory(String category);
	
	// 최근 등록된 상품 5개 조회
	List<ProductVO> selectRecent5();
	
}
