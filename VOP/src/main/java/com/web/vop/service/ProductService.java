package com.web.vop.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.web.vop.domain.ProductVO;
import com.web.vop.util.Pagination;

@Service
public interface ProductService {
	
	// 상품 상세 조회
	ProductVO getProductById(int productId);
	
	// 상품 등록
	int registerProduct(ProductVO productVO);
	
	// 카테고리로 상품 검색
	List<ProductVO> selectByCategory(String category, Pagination pagination);
	
	// 이름으로 상품 검색
	List<ProductVO> selectByName(String productName, Pagination pagination);
	
	// 카테고리 안에서 이름으로 검색
	List<ProductVO> selectByNameInCategory(String category, String productName,
			Pagination pagination);
	
}
