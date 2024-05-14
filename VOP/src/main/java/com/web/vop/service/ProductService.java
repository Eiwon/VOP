package com.web.vop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.web.vop.domain.ProductVO;
import com.web.vop.util.Pagination;

@Service
public interface ProductService {
	
	// 상품 상세 조회
	ProductVO getProductById(int productId);
	
	// 댓글 총 갯수 조회 
	int selectReviewByCount(int productId);
	
	// 상품 리뷰(별) 총합 조회
	int selectReviewByStar(int productId);
	
	// 상품 등록
	int registerProduct(ProductVO productVO);
	
	// 방금 등록한 상품 id 조회
	int getRecentProductId();
	
	// 카테고리로 상품 검색
	List<ProductVO> selectByCategory(String category, Pagination pagination);
	
	// 카테고리로 상품 검색 결과 수량
	int selectByCategoryCnt(String category);
	
	// 이름으로 상품 검색
	List<ProductVO> selectByName(String productName, Pagination pagination);
	
	// 이름으로 상품 검색 결과 수량
	int selectByNameCnt(String productName);
	
	// 카테고리 안에서 이름으로 검색
	List<ProductVO> selectByNameInCategory(String category, String productName,
			Pagination pagination);
	
	// 카테고리 안에서 이름으로 검색 결과 수량
	int selectByNameInCategoryCnt(String category, String productName);
	
	// memberId로 상품 조회
	List<ProductVO> selectByMemberId(String memberId, Pagination pagination);
	
	// memberId로 상품 갯수 조회
	int getCntByMemberId(String memberId);
	
	// productId로 상품 상태 변경
	int setProductState(String productState, int productId);
	
	// productId로 상품 상태 검색
	String selectStateByProductId(int productId);
		
	// productId로 상품 삭제
	int deleteProduct(int productId);
	
	// 카테고리를 지정하여, 리뷰 수가 가장 많은 5개의 상품 검색
	List<ProductVO> getTopProductInCategory(String category);
	
	// 최근 등록된 상품 5개 조회
	List<ProductVO> getRecent5();
	
	// 상태가 ?? 대기중인 상품 조회
	List<ProductVO> getStateIs(String productState, Pagination pagination);
	
	// 상태가 ?? 대기중인 상품 수 조회
	int getStateIsCnt(String productState);
	
}
