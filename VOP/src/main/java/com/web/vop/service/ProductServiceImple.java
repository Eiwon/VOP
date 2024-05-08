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
	
	// 상품 상세 정보 검색
	@Override
	public ProductVO getProductById(int productId) {
		log.info("getProductById()");
		ProductVO result = productMapper.selectProduct(productId);
		log.info("상품 상세 정보 : " + result.toString());
		return result;
	} // end getProductById()
	
	// 댓글 총 갯수 검색
	@Override
	public int selectReviewByCount(int productId) {
		log.info("selectReviewByCount()");
		int res = productMapper.selectReviewCount(productId);
		return res;
	}
	
	// 상품 리뷰 총 합 검색
	@Override
	public int selectReviewByStar(int productId) {
		log.info("selectReviewByStar()");
		int res = productMapper.selectReviewStar(productId);
		return res;
	}
	
//	// 이미지 상세 정보 검색
//	@Override
//	public ProductVO selectByMainImg(int productId) {
//		log.info("selectByMainImg()");
//		ProductVO result = productMapper.selectByMainImg(productId);
//		log.info("이미지 검색 : " + result.toString()); //우선 로그 만들었습니다.
//		return result;
//	}// end getImageById()

	@Override
	public int registerProduct(ProductVO productVO) {
		log.info("registerProduct : " + productVO);
		int res = productMapper.insertProduct(productVO);
		log.info(res + "행 추가 성공");
		return res;
	} // end registerProduct

	@Override
	public List<ProductVO> selectByCategory(String category, Pagination pagination) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductVO> selectByName(String productName, Pagination pagination) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductVO> selectByNameInCategory(String category, String productName, Pagination pagination) {
		// TODO Auto-generated method stub
		return null;
	}

	

	
	
}
