package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.ProductDetailsDTO;
import com.web.vop.domain.ProductVO;
import com.web.vop.util.Pagination;

@Mapper
public interface ProductMapper {
	
	// ��ǰ �� ���� �˻�
	ProductVO selectProduct(int productId);
	
	// ��� �� ���� �˻�
	int selectReviewCount(int productId);
	
	// ��� �� ���� ����
	int reviewNumUP(int productId);
			
	// ��� �� ���� ����
	int reviewNumDown(int productId);
		
	// ��ǰ ����(��) ���� �˻�
	int selectReviewStar(int productId);
	
	// ��ǰ ����(��) ��հ� ���
	int updateReviewAvg(@Param("productId")int productId, @Param("reviewAvg")String reviewAvg);
	
	// ���(����) ī����
	int updateReviewNum(@Param("productId")int productId, @Param("reviewNum")int reviewNum);
	
	// ��ǰ ���
	int insertProduct(ProductVO productVO);
	
	// ��� ����� ��ǰ id �˻�
	int selectLastInsertId();
	
	// ī�װ��� �˻�
	List<ProductVO> selectByCategory(
			@Param("category") String category, @Param("pagination") Pagination pagination);
	
	// ī�װ��� �˻� ��� ����
	int selectByCategoryCnt(String category);
	
	// �̸��� �˻�� ���Ե� ��ǰ �˻�
	List<ProductVO> selectByName(
			@Param("productName") String productName, @Param("pagination") Pagination pagination);
	
	// �̸��� �˻�� ���Ե� ��ǰ �˻� ��� ����
	int selectByNameCnt(String productName);
	
	// ī�װ� ������, �̸��� �˻�� ���Ե� ��ǰ �˻�
	List<ProductVO> selectByNameInCategory(
			@Param("category") String category, @Param("productName") String productName,
			@Param("pagination") Pagination pagination);
	
	// ī�װ� ������, �̸��� �˻�� ���Ե� ��ǰ �˻� ��� ����
	int selectByNameInCategoryCnt(@Param("category") String category, @Param("productName") String productName);
	
	// memberId�� ��ǰ ��ȸ
	List<ProductVO> selectByMemberId(@Param("memberId") String memberId, @Param("pagination") Pagination pagination);
	
	// memberId�� ��ǰ ���� ��ȸ
	int selectByMemberIdCnt(String memberId);
	
	// productId�� ��ǰ ���� ����
	int updateState(@Param("productState") String productState, @Param("productId") int productId);
	
	// productId�� ��ǰ ���� �˻�
	String selectStateByProductId(int productId);
	
	// productId�� ��ǰ ����
	int deleteProduct(int productId);
	
	// ī�װ��� �����Ͽ�, ���� ���� ���� ���� 5���� ��ǰ �˻�
	List<ProductVO> selectTopProductInCategory(String category);
	
	// �ֱ� ��ϵ� ��ǰ 5�� ��ȸ
	List<ProductVO> selectRecent5();
	
	// ���°� ??�� ��ǰ ��ȸ
	List<ProductVO> selectStateIs(@Param("productState") String productState, @Param("pagination") Pagination pagination);
	
	// ���°� ??�� ��ǰ �� ��ȸ
	int selectStateIsCnt(String productState);
	
	// productId�� �� ���� ��ȸ
	ProductDetailsDTO selectDetails(int productId);
	
	// ��ǰ ���� ����
	int updateProduct(ProductVO productVO);
}
