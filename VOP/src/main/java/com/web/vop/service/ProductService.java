package com.web.vop.service;

import com.web.vop.domain.ProductVO;

//������ ��ǰ�� productId�� ��ȸ�ϴ� Mapper �޼ҵ�
public interface ProductService {
	ProductVO getProductById(int productId);
}
