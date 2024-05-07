package com.web.vop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.vop.domain.ImageVO;
import com.web.vop.domain.ProductVO;
import com.web.vop.persistence.ProductMapper;
import com.web.vop.util.Pagination;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ProductServiceImple implements ProductService{
	
	@Autowired
	private ProductMapper productMapper;
	
	// ��ǰ �� ���� �˻�
	@Override
	public ProductVO getProductById(int productId) {
		log.info("getProductById()");
		ProductVO result = productMapper.selectProduct(productId);
		log.info("��ǰ �� ���� : " + result.toString());
		return result;
	} // end getProductById()
	
	// ��� �� ���� �˻�
	@Override
	public int selectReviewByCount(int productId) {
		log.info("selectReviewByCount()");
		int res = productMapper.selectReviewCount(productId);
		return res;
	}
	
	// ��ǰ ���� �� �� �˻�
	@Override
	public int selectReviewByStar(int productId) {
		log.info("selectReviewByCount()");
		int res = productMapper.selectReviewStar(productId);
		return res;
	}
	
//	// �̹��� �� ���� �˻�
//	@Override
//	public ProductVO selectByMainImg(int productId) {
//		log.info("selectByMainImg()");
//		ProductVO result = productMapper.selectByMainImg(productId);
//		log.info("�̹��� �˻� : " + result.toString()); //�켱 �α� ��������ϴ�.
//		return result;
//	}// end getImageById()

	@Override
	public int registerProduct(ProductVO productVO) {
		log.info("registerProduct : " + productVO);
		int res = productMapper.insertProduct(productVO);
		log.info(res + "�� �߰� ����" + productVO.getProductId());
		return res;
	} // end registerProduct

	@Override
	public int getRecentProductId() {
		log.info("getRecentProductId()");
		int res = productMapper.selectLastInsertId();
		return res;
	} // end getRecentProductId
	
	@Override
	public List<ProductVO> selectByCategory(String category, Pagination pagination) {
		log.info("selectByCategory()");
		return productMapper.selectByCategory(category, pagination);
	} // end selectByCategory

	@Override
	public List<ProductVO> selectByName(String productName, Pagination pagination) {
		log.info("selectByName()");
		String includeName = '%' + productName + '%';
		return productMapper.selectByName(includeName, pagination);
	} // end selectByName

	@Override
	public List<ProductVO> selectByNameInCategory(String category, String productName, Pagination pagination) {
		log.info("selectByNameInCategory");
		String includeName = '%' + productName + '%';
		return productMapper.selectByNameInCategory(category, includeName, pagination);
	} // end selectByNameInCategory

	@Override
	public List<ProductVO> selectByMemberId(String memberId, Pagination pagination) {
		log.info("selectByMemberId()");
		return productMapper.selectByMemberId(memberId, pagination);
	} // end selectByMemberId

	@Override
	public int getCntByMemberId(String memberId) {
		log.info("getCntByMemberId()");
		return productMapper.selectByMemberIdCnt(memberId);
	} // end getCntByMemberId
	
	@Override
	public int setProductState(String productState, int productId) {
		log.info("setProductState");
		return productMapper.updateState(productState, productId);
	} // end setProductState

	@Override
	public String selectStateByProductId(int productId) {
		log.info("selectStateByProductId");
		return productMapper.selectStateByProductId(productId);
	} // end selectStateByProductId

	@Override
	public int deleteProduct(int productId) {
		log.info("deleteProduct");
		return productMapper.deleteProduct(productId);
	} // end deleteProduct

	
}
