package com.web.vop.persistence;

import org.apache.ibatis.annotations.Mapper;

import com.web.vop.domain.ProductVO;

@Mapper
// 선택한 상품을 productId로 조회하는 Mapper 메소드
public interface ProductMapper {
	ProductVO productSelect(int productId);
}
