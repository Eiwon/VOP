package com.web.vop.persistence;

import org.apache.ibatis.annotations.Mapper;

import com.web.vop.domain.ProductVO;

@Mapper
// ������ ��ǰ�� productId�� ��ȸ�ϴ� Mapper �޼ҵ�
public interface ProductMapper {
	ProductVO productSelect(int productId);
}
