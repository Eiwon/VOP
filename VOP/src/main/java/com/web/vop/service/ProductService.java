package com.web.vop.service;

import com.web.vop.domain.ProductVO;

//선택한 상품을 productId로 조회하는 Mapper 메소드
public interface ProductService {
	ProductVO getProductById(int productId);
}
