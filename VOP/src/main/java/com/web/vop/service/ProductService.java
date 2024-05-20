package com.web.vop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.web.vop.domain.ImageVO;
import com.web.vop.domain.ProductDetailsDTO;
import com.web.vop.domain.ProductVO;
import com.web.vop.util.PageMaker;
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
	int registerProduct(ProductVO productVO, ImageVO thumbnail, List<ImageVO> details);
	
	// 카테고리로 상품 검색
	List<ProductVO> searchByCategory(String category, PageMaker pageMaker);
	
	// 이름으로 상품 검색
	List<ProductVO> searchByName(String productName, PageMaker pageMaker);
	
	// 카테고리 안에서 이름으로 검색
	List<ProductVO> searchByNameInCategory(String category, String productName,
			PageMaker pageMaker);
	
	// memberId로 상품 조회
	List<ProductVO> searchByMemberId(String memberId, PageMaker pageMaker);
	
	// productId로 상품 상태 변경
	int setProductState(String productState, int productId);
	
	// productId로 상품 상태 검색
	String selectStateByProductId(int productId);
		
	// productId로 상품 삭제
	List<ImageVO> deleteProduct(int productId);
	
	// 카테고리를 지정하여, 리뷰 수가 가장 많은 5개의 상품 검색
	List<ProductVO> getTopProductInCategory(String category);
	
	// 최근 등록된 상품 5개 조회
	List<ProductVO> getRecent5();
	
	// 상태가 ?? 대기중인 상품 조회
	List<ProductVO> searchByState(String productState, PageMaker pageMaker);
	
	// productId로 상세 정보 조회
	ProductDetailsDTO getDetails(int productId);
	
	// 상품 정보 변경
	int updateProduct(ProductVO productVO, ImageVO thumbnail, List<ImageVO> details);
	
	// 상품 삭제 요청
	public int deleteProductRequest(int productId);
}
