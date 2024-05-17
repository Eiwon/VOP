package com.web.vop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.vop.domain.ImageVO;
import com.web.vop.domain.ProductDetailsDTO;
import com.web.vop.domain.ProductVO;
import com.web.vop.persistence.ImageMapper;
import com.web.vop.persistence.ProductMapper;
import com.web.vop.util.FileUploadUtil;
import com.web.vop.util.Pagination;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ProductServiceImple implements ProductService{
	
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private ImageMapper imageMapper;
	
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

	@Transactional(value = "transactionManager")
	@Override
	public int registerProduct(ProductVO productVO, ImageVO thumbnail, List<ImageVO> details) { // 등록 성공시, 등록한 상품 id 반환
		log.info("registerProduct : " + productVO);
		int res = 0;
		if(thumbnail != null) {
			int imgId = imageService.registerImage(thumbnail);
			productVO.setImgId(imgId);
		}
		productMapper.insertProduct(productVO);
		int productId = productMapper.selectLastInsertId();
		
		for(ImageVO detail : details) {
			detail.setProductId(productId);
			res = imageMapper.insertImg(detail);
		}
		
		return res;
	} // end registerProduct
	
	@Override
	public List<ProductVO> selectByCategory(String category, Pagination pagination) {
		log.info("selectByCategory()");
		return productMapper.selectByCategory(category, pagination);
	} // end selectByCategory

	@Override
	public int selectByCategoryCnt(String category) {
		log.info("selectByCategoryCnt()");
		return productMapper.selectByCategoryCnt(category);
	} // end selectByCategoryCnt
	
	@Override
	public List<ProductVO> selectByName(String productName, Pagination pagination) {
		log.info("selectByName()");
		String includeName = '%' + productName + '%';
		return productMapper.selectByName(includeName, pagination);
	} // end selectByName

	@Override
	public int selectByNameCnt(String productName) {
		log.info("selectByNameCnt()");
		String includeName = '%' + productName + '%';
		return productMapper.selectByNameCnt(includeName);
	} // end selectByName
	
	@Override
	public List<ProductVO> selectByNameInCategory(String category, String productName, Pagination pagination) {
		log.info("selectByNameInCategory()");
		String includeName = '%' + productName + '%';
		return productMapper.selectByNameInCategory(category, includeName, pagination);
	} // end selectByNameInCategory

	@Override
	public int selectByNameInCategoryCnt(String category, String productName) {
		log.info("selectByNameInCategoryCnt()");
		String includeName = '%' + productName + '%';
		return productMapper.selectByNameInCategoryCnt(category, includeName);
	} // end selectByNameInCategoryCnt
	
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
		log.info("setProductState()");
		return productMapper.updateState(productState, productId);
	} // end setProductState

	@Override
	public String selectStateByProductId(int productId) {
		log.info("selectStateByProductId()");
		return productMapper.selectStateByProductId(productId);
	} // end selectStateByProductId

	@Override
	public int deleteProduct(int productId) {
		log.info("deleteProduct()");
		int res = 0;
		// 상품 이미지도 삭제해야함
		List<ImageVO> imageList = imageMapper.selectAllbyProductId(productId);
		
		if(imageList != null) { // 서버에 저장된 이미지 삭제
			log.info("관련 이미지 : " + imageList.size() + "건");
			for(ImageVO image : imageList) {
				FileUploadUtil.deleteFile(image);
			}
		}
		// DB에 저장된 이미지 정보 삭제
		res += imageMapper.deleteProductImage(productId);
		res += productMapper.deleteProduct(productId);
		log.info("DB 총 " + res + "건 삭제 완료");
		
		return res;
	} // end deleteProduct

	@Override
	public List<ProductVO> getTopProductInCategory(String category) {
		log.info("getTopProductInCategory()");
		return productMapper.selectTopProductInCategory(category);
	} // end getTopProductInCategory

	@Override
	public List<ProductVO> getRecent5() {
		log.info("getRecent5()");
		return productMapper.selectRecent5();
	} // end getRecent5

	@Override
	public List<ProductVO> getStateIs(String productState, Pagination pagination) {
		log.info("getStateIsWait()");
		return productMapper.selectStateIs(productState, pagination);
	} // end getStateIsWait

	@Override
	public int getStateIsCnt(String productState) {
		log.info("getStateIsWaitCnt()");
		return productMapper.selectStateIsCnt(productState);
	} // end getStateIsWaitCnt

	@Override
	public ProductDetailsDTO getDetails(int productId) {
		log.info("getDetails()");
		return productMapper.selectDetails(productId);
	} // end getDetails

	@Override
	public int updateProduct(ProductVO productVO) {
		log.info("updateProduct()");
		return productMapper.updateProduct(productVO);
	} // end updateProduct

	

	
}
