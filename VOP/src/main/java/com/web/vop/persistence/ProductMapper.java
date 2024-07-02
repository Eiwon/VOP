package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.OrderViewDTO;
import com.web.vop.domain.ProductDetailsDTO;
import com.web.vop.domain.ProductPreviewDTO;
import com.web.vop.domain.ProductVO;
import com.web.vop.util.Pagination;

@Mapper
public interface ProductMapper {
	
	// ��ǰ �� ���� �˻�
	ProductVO selectProduct(int productId);
	
	// ��ǰ �̸����� �˻�
	ProductPreviewDTO selectPreviewById(int productId);
	
	// ��� �� ���� �˻�
	int selectReviewCount(int productId);
	
	// ��� �� ���� ����
	int reviewNumUP(int productId);
			
	// ��� �� ���� ����
	int reviewNumDown(int productId);
		
	// ��ǰ ����(��) ���� �˻� // ������
//	int selectReviewStar(int productId);
	
	// ��ǰ ����(��) ��հ� ���
	int updateReviewAvg(@Param("productId")int productId, @Param("reviewAvg")String reviewAvg);
	
	// ��ǰ ����(��) ��հ� ����
	int updateReviewAvgNew(@Param("productId")int productId, @Param("reviewAvg")float reviewAvg);
	
	// ���(����) ī����
	int updateReviewNum(@Param("productId")int productId, @Param("reviewNum")int reviewNum);
	
	// ��ǰ ���
	int insertProduct(ProductVO productVO);
	
	// ������� �ִ� ��ǰ ���
	int insertProductWithThumbnail(ProductVO productVO);
	
	// �˻��� �Ǵ� ī�װ� �˻� (����¡)
	List<ProductPreviewDTO> selectByNameNCategory(Pagination pagination);
	
	// �˻��� �Ǵ� ī�װ� �˻� ��� ��
	int selectByNameNCategoryCnt(Pagination pagination);
	
	// memberId�� ��ǰ ��ȸ
	List<ProductPreviewDTO> selectByMemberId(@Param("memberId") String memberId, @Param("pagination") Pagination pagination);
	
	// memberId�� ��ǰ ���� ��ȸ
	int selectByMemberIdCnt(String memberId);
	
	// productId�� ��ǰ ���� ����
	int updateState(@Param("productState") String productState, @Param("productId") int productId);
	
	// productId�� ��ǰ ���� �˻�
	String selectStateByProductId(int productId);
	
	// productId�� ��ǰ ����
	int deleteProduct(int productId);
	
	// ī�װ��� ���� ���� ���� ���� 5���� ��ǰ �˻�
	List<ProductPreviewDTO> selectTopProductByCategory();
	
	// �ֱ� ��ϵ� ��ǰ 5�� ��ȸ
	List<ProductPreviewDTO> selectRecent5();
	
	// ���°� ??�� ��ǰ ��ȸ
	List<ProductPreviewDTO> selectStateIs(@Param("productState") String productState, @Param("pagination") Pagination pagination);
	
	// ���°� ??�� ��ǰ �� ��ȸ
	int selectStateIsCnt(String productState);
	
	// productId�� �� ���� ��ȸ
	ProductDetailsDTO selectDetails(int productId);
	
	// ��ǰ ���� ����
	int updateProduct(ProductVO productVO);
	
	// productId�� �Ǹ��� memberId �˻�
	String selectMemberIdById(int productId);
	
	// ��ǰ ��� ����
	int updateRemains(@Param("productId") int productId, @Param("increaseNum") int increaseNum);
	
	// ��ǰ ��� �˻�
	int selectRemainsById(int productId);
	
	// ��ǰ �� ������ �˻��Ͽ� OrderViewDTO�� ��ȯ 
	List<OrderViewDTO> selectToOrderById(int[] productIds);
	
	
}
