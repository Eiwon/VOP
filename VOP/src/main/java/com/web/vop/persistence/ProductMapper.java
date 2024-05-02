package com.web.vop.persistence;

import org.apache.ibatis.annotations.Mapper;

import com.web.vop.domain.ProductVO;

@Mapper
public interface ProductMapper {
	
	// 상품 상세 정보 검색
	ProductVO selectProduct(int productId);
	
}
