package com.web.vop.service;

import org.springframework.stereotype.Service;

import com.web.vop.domain.ProductVO;

@Service
public interface ProductService {
	
	// 상품 상세 조회
	ProductVO getProductById(int productId);
}
