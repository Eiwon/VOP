package com.web.vop.service;

import org.springframework.stereotype.Service;

import com.web.vop.domain.ProductVO;

@Service
public interface ProductService {
	
	// ��ǰ �� ��ȸ
	ProductVO getProductById(int productId);
}
