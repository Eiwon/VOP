package com.web.vop.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.web.vop.domain.ProductVO;
import com.web.vop.util.Pagination;

@Service
public interface ProductService {
	
	// ��ǰ �� ��ȸ
	ProductVO getProductById(int productId);
	
	// ��ǰ ���
	int registerProduct(ProductVO productVO);
	
	// ī�װ��� ��ǰ �˻�
	List<ProductVO> selectByCategory(String category, Pagination pagination);
	
	// �̸����� ��ǰ �˻�
	List<ProductVO> selectByName(String productName, Pagination pagination);
	
	// ī�װ� �ȿ��� �̸����� �˻�
	List<ProductVO> selectByNameInCategory(String category, String productName,
			Pagination pagination);
	
}
