package com.web.vop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.web.vop.domain.ProductVO;
import com.web.vop.util.Pagination;

@Service
public interface ProductService {
	
	// ��ǰ �� ��ȸ
	ProductVO getProductById(int productId);
	
	// ��� �� ���� ��ȸ 
	int selectReviewByCount(int productId);
	
	// ��ǰ ����(��) ���� ��ȸ
	int selectReviewByStar(int productId);
	
	// ��ǰ ���
	int registerProduct(ProductVO productVO);
	
	// ��� ����� ��ǰ id ��ȸ
	int getRecentProductId();
	
	// ī�װ��� ��ǰ �˻�
	List<ProductVO> selectByCategory(String category, Pagination pagination);
	
	// ī�װ��� ��ǰ �˻� ��� ����
	int selectByCategoryCnt(String category);
	
	// �̸����� ��ǰ �˻�
	List<ProductVO> selectByName(String productName, Pagination pagination);
	
	// �̸����� ��ǰ �˻� ��� ����
	int selectByNameCnt(String productName);
	
	// ī�װ� �ȿ��� �̸����� �˻�
	List<ProductVO> selectByNameInCategory(String category, String productName,
			Pagination pagination);
	
	// ī�װ� �ȿ��� �̸����� �˻� ��� ����
	int selectByNameInCategoryCnt(String category, String productName);
	
	// memberId�� ��ǰ ��ȸ
	List<ProductVO> selectByMemberId(String memberId, Pagination pagination);
	
	// memberId�� ��ǰ ���� ��ȸ
	int getCntByMemberId(String memberId);
	
	// productId�� ��ǰ ���� ����
	int setProductState(String productState, int productId);
	
	// productId�� ��ǰ ���� �˻�
	String selectStateByProductId(int productId);
		
	// productId�� ��ǰ ����
	int deleteProduct(int productId);
	
	// ī�װ��� �����Ͽ�, ���� ���� ���� ���� 5���� ��ǰ �˻�
	List<ProductVO> getTopProductInCategory(String category);
	
	// �ֱ� ��ϵ� ��ǰ 5�� ��ȸ
	List<ProductVO> getRecent5();
	
	// ���°� ?? ������� ��ǰ ��ȸ
	List<ProductVO> getStateIs(String productState, Pagination pagination);
	
	// ���°� ?? ������� ��ǰ �� ��ȸ
	int getStateIsCnt(String productState);
	
}
