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
import com.web.vop.util.PageMaker;
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
	public List<ProductVO> searchByCategory(String category, PageMaker pageMaker) {
		log.info("searchByCategory()");
		int totalCnt = productMapper.selectByCategoryCnt(category);
		pageMaker.setTotalCount(totalCnt);
		return productMapper.selectByCategory(category, pageMaker.getPagination());
	} // end selectByCategory
	
	@Override
	public List<ProductVO> searchByName(String productName, PageMaker pageMaker) {
		log.info("searchByName()");
		String includeName = '%' + productName + '%';
		int totalCnt = productMapper.selectByNameCnt(includeName);
		pageMaker.setTotalCount(totalCnt);
		return productMapper.selectByName(includeName, pageMaker.getPagination());
	} // end selectByName
	
	@Override
	public List<ProductVO> searchByNameInCategory(String category, String productName, PageMaker pageMaker) {
		log.info("searchByNameInCategory()");
		String includeName = '%' + productName + '%';
		int totalCnt = productMapper.selectByNameInCategoryCnt(category, includeName);
		pageMaker.setTotalCount(totalCnt);
		return productMapper.selectByNameInCategory(category, includeName, pageMaker.getPagination());
	} // end selectByNameInCategory
	
	@Override
	public List<ProductVO> searchByMemberId(String memberId, PageMaker pageMaker) {
		log.info("selectByMemberId()");
		int totalCnt = productMapper.selectByMemberIdCnt(memberId);
		pageMaker.setTotalCount(totalCnt);
		return productMapper.selectByMemberId(memberId, pageMaker.getPagination());
	} // end selectByMemberId
	
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
	public List<ProductVO> searchByState(String productState, PageMaker pageMaker) {
		log.info("searchByState()");
		int totalCnt = productMapper.selectStateIsCnt(productState);
		pageMaker.setTotalCount(totalCnt);
		return productMapper.selectStateIs(productState, pageMaker.getPagination());
	} // end searchByState

	@Override
	public ProductDetailsDTO getDetails(int productId) {
		log.info("getDetails()");
		ProductDetailsDTO details = productMapper.selectDetails(productId);
		details.setImgIdDetails(imageService.getImgId(productId));
		return details;
	} // end getDetails

	@Transactional(value = "transactionManager")
	@Override
	public int updateProduct(ProductVO productVO, ImageVO thumbnail, List<ImageVO> details) {
		log.info("updateProduct()");
		int productId = productVO.getProductId();
		int oldThumbnailId = productVO.getImgId();
		// thumbnail 이미지 변경
		if(thumbnail != null) {// 이미지가 변경되었다면, 기존 이미지 삭제, 새 이미지 추가
			if(productVO.getImgId() > 0) {
				imageMapper.deleteById(oldThumbnailId);
			}
			imageMapper.insertImg(thumbnail);
			int newImgId = imageMapper.selectRecentImgId();
			productVO.setImgId(newImgId);
		}
		
		// details 이미지 변경
		if(details.size() > 0) {
			imageMapper.deleteByProductId(productId);
			for(ImageVO detail : details) {
				detail.setProductId(productId);
				imageMapper.insertImg(detail);
			}
		}
		// 상품 변경점 저장
		productMapper.updateProduct(productVO);
		int res = productMapper.updateState(productVO.getProductState(), productId);
		
		return res;
	} // end updateProduct

	

	
}
