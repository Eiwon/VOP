package com.web.vop.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.web.vop.domain.ImageVO;
import com.web.vop.domain.ProductDetailsDTO;
import com.web.vop.domain.ProductVO;
import com.web.vop.util.PageMaker;
import com.web.vop.util.Pagination;

@Service
public interface ProductService {
	
	// ��ǰ �� ��ȸ
	ProductVO getProductById(int productId);
	
	// ��� �� ���� ��ȸ 
	int selectReviewByCount(int productId);
	
	// ��� �� ���� ����
	int reviewNumUP(int productId);
			
	// ��� �� ���� ����
	int reviewNumDown(int productId);
		
	// ��ǰ ����(��) ���� �˻�
	int selectReviewStar(int productId);
	
	// ��ǰ ����(��) ��հ� ����
	int updateReviewAvg(int productId, String reviewAvg);
	
	// ���(����) ī����
	int updateReviewNum(int productId, int reviewNum);
	
	// ��ǰ ���
	int registerProduct(ProductVO productVO, ImageVO thumbnail, List<ImageVO> details) throws IOException;
	
	// ī�װ��� ��ǰ �˻�
	List<ProductVO> searchByCategory(String category, PageMaker pageMaker);
	
	// �̸����� ��ǰ �˻�
	List<ProductVO> searchByName(String productName, PageMaker pageMaker);
	
	// ī�װ� �ȿ��� �̸����� �˻�
	List<ProductVO> searchByNameInCategory(String category, String productName,
			PageMaker pageMaker);
	
	// memberId�� ��ǰ ��ȸ
	List<ProductVO> searchByMemberId(String memberId, PageMaker pageMaker);
	
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
	List<ProductVO> searchByState(String productState, PageMaker pageMaker);
	
	// productId�� �� ���� ��ȸ
	ProductDetailsDTO getDetails(int productId);
	
	// ��ǰ ���� ����
	int updateProduct(ProductVO productVO, ImageVO newThumbnail, List<ImageVO> newDetails) throws IOException;
	
	// ��ǰ ���� ��û
	public int deleteProductRequest(int productId);
	
	// ��ǰ�� ���õ� ��� �̹��� �˻�
	List<ImageVO> getAllProductImg(int productId);
	
	// ��ǰ�� ����� �˻�
	ImageVO getProductThumbnail(int imgId);
	
	// ��ǰ�� ���� �̹��� �˻�
	List<ImageVO> getProductDetails(int productId);
	
	
}
