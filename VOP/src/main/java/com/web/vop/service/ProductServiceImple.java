package com.web.vop.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.web.vop.domain.ImageVO;
import com.web.vop.domain.ProductDetailsDTO;
import com.web.vop.domain.ProductPreviewDTO;
import com.web.vop.domain.ProductVO;
import com.web.vop.persistence.ImageMapper;
import com.web.vop.persistence.ProductMapper;
import com.web.vop.util.Constant;
import com.web.vop.util.FileAnalyzerUtil;
import com.web.vop.util.PageMaker;
import com.web.vop.util.Pagination;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ProductServiceImple implements ProductService{
	
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private ImageMapper imageMapper;
	
	// ��ǰ �� ���� �˻�
	@Override
	public ProductVO getProductById(int productId) {
		log.info("getProductById()");
		ProductVO result = productMapper.selectProduct(productId);
		return result;
	} // end getProductById()
	
	// ��� �� ���� �˻�
	@Override
	public int selectReviewByCount(int productId) {
		log.info("selectReviewByCount()");
		int res = productMapper.selectReviewCount(productId);
		return res;
	}
	
	// ��� �� ���� ����
	@Override
	public int reviewNumUP(int productId) {
		log.info("reviewNumUP()");
		log.info("productId : " + productId);
		int res = productMapper.reviewNumUP(productId);
		return res;
	}
			
	// ��� �� ���� ����
	@Override
	public int reviewNumDown(int productId) {
		log.info("reviewNumDown()");
		int res = productMapper.reviewNumDown(productId);
		return res;
	}
	
	// ��ǰ ���� ��հ� ����
	@Override
	public int updateReviewAvg(int productId, String reviewAvg) {
		log.info("updateReviewAvg()");
		int res = productMapper.updateReviewAvg(productId, reviewAvg);
		return res;
	}
	
	
	// ���(����) ī����
	@Override
	public int updateReviewNum(int productId, int reviewNum) {
		log.info("updateReviewNum()");
		int updateRes = productMapper.updateReviewNum(productId, reviewNum);
		log.info(updateRes + "�� ���(����) ī����");
		return updateRes;
	}

	@Transactional(value = "transactionManager")
	@Override
	public int registerProduct(ProductVO productVO, ImageVO thumbnail, List<ImageVO> details) throws IOException { // ��� ������, ����� ��ǰ id ��ȯ
		log.info("registerProduct : " + productVO);
		int res = 0;
		productVO.setProductState(Constant.STATE_APPROVAL_WAIT);

		// DB�� ��ǰ ���� ���
		if(thumbnail == null) { 
			productMapper.insertProduct(productVO);						
		}else {
			imageMapper.insertImg(thumbnail);
			productMapper.insertProductWithThumbnail(productVO);
		}
		
		for(ImageVO detail : details) {
			res = imageMapper.insertProductDetailsImg(detail);
		}
		return res;
	} // end registerProduct
	
	@Override
	public List<ProductPreviewDTO> search(PageMaker pageMaker) {
		log.info("search()");
		Pagination pagination = pageMaker.getPagination();
		String category = pagination.getCategory();
		String word = pagination.getWord();
		int totalCnt;
		List<ProductPreviewDTO> result;
		String searchWord = '%' + word + '%';
		pagination.setWord(searchWord);
		
		if(category.equals("��ü")) { // ���õ� ī�װ��� ���� ���
			totalCnt = productMapper.selectByNameCnt(pagination, Constant.STATE_SELL);
			pageMaker.setTotalCount(totalCnt);
			result = productMapper.selectByName(pagination, Constant.STATE_SELL);
		}else { // ���õ� ī�װ��� �ִ� ���
			totalCnt = productMapper.selectByNameInCategoryCnt(pagination, Constant.STATE_SELL);
			pageMaker.setTotalCount(totalCnt);
			result = productMapper.selectByNameInCategory(pagination, Constant.STATE_SELL);
		}
		
		pagination.setWord(word);
		return result;
	} // end search
	
//	@Override
//	public List<ProductPreviewDTO> searchByCategory(String category, PageMaker pageMaker) {
//		log.info("searchByCategory()");
//		int totalCnt = productMapper.selectByCategoryCnt(category, Constant.STATE_SELL);
//		pageMaker.setTotalCount(totalCnt);
//		return productMapper.selectByCategory(category, pageMaker.getPagination(), Constant.STATE_SELL);
//	} // end selectByCategory
//	
//	@Override
//	public List<ProductPreviewDTO> searchByName(String productName, PageMaker pageMaker) {
//		log.info("searchByName()");
//		String includeName = '%' + productName + '%';
//		int totalCnt = productMapper.selectByNameCnt(includeName, Constant.STATE_SELL);
//		pageMaker.setTotalCount(totalCnt);
//		return productMapper.selectByName(includeName, pageMaker.getPagination(), Constant.STATE_SELL);
//	} // end selectByName
//	
//	@Override
//	public List<ProductPreviewDTO> searchByNameInCategory(String category, String productName, PageMaker pageMaker) {
//		log.info("searchByNameInCategory()");
//		String includeName = '%' + productName + '%';
//		int totalCnt = productMapper.selectByNameInCategoryCnt(category, includeName, Constant.STATE_SELL);
//		pageMaker.setTotalCount(totalCnt);
//		return productMapper.selectByNameInCategory(category, includeName, pageMaker.getPagination(), Constant.STATE_SELL);
//	} // end selectByNameInCategory
	
	@Override
	public List<ProductPreviewDTO> searchByMemberId(String memberId, PageMaker pageMaker) {
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

	@Transactional(value = "transactionManager")
	@Override
	public int deleteProduct(int productId) {
		log.info("deleteProduct()");
		ProductVO target = productMapper.selectProduct(productId);
		int imgId = target.getImgId();
		
		// ��ǰ �̹����� �����ؾ���
		List<ImageVO> imageList = imageMapper.selectByProductId(productId);
		ImageVO imageVO = imageMapper.selectByImgId(imgId);
		if(imageVO != null) {
			imageList.add(imageVO);
		}
		// DB�� ����� �̹��� ���� ����
		imageMapper.deleteById(imgId);
		productMapper.deleteProduct(productId);
		int res = imageMapper.deleteByProductId(productId);
		
		return res;
	} // end deleteProduct

	@Override
	public List<ProductPreviewDTO> getTopProductByCategory() {
		log.info("getTopProductByCategory()");
		return productMapper.selectTopProductByCategory();
	} // end getTopProductByCategory
	
	@Override
	public List<ProductPreviewDTO> getRecent5() {
		log.info("getRecent5()");
		return productMapper.selectRecent5();
	} // end getRecent5

	@Override
	public List<ProductPreviewDTO> searchByState(String productState, PageMaker pageMaker) {
		log.info("searchByState()");
		int totalCnt = productMapper.selectStateIsCnt(productState);
		pageMaker.setTotalCount(totalCnt);
		return productMapper.selectStateIs(productState, pageMaker.getPagination());
	} // end searchByState

	@Override
	public ProductDetailsDTO getDetails(int productId) {
		log.info("getDetails()");
		ProductDetailsDTO details = productMapper.selectDetails(productId);
		log.info(details);
		List<ImageVO> imgIds = imageMapper.selectByProductId(productId);
		if(imgIds != null) {
			log.info(imgIds);
			details.setDetails(imgIds);			
		}
		return details;
	} // end getDetails

	@Transactional(value = "transactionManager")
	@Override
	public int updateProduct(ProductVO productVO, ImageVO newThumbnail, List<ImageVO> newDetails) throws IOException {
		log.info("updateProduct()");
		int productId = productVO.getProductId();
		int oldThumbnailId = productVO.getImgId();
		
		// thumbnail �̹��� ����
		if(newThumbnail != null) {// �̹����� ����Ǿ��ٸ�, ���� �̹��� ����, �� �̹��� �߰�
			if(productVO.getImgId() > 0) {
				imageMapper.deleteById(oldThumbnailId);
			}
			imageMapper.insertImg(newThumbnail);
			int newImgId = imageMapper.selectRecentImgId();
			log.info(newImgId);
			productVO.setImgId(newImgId);
		}
		
		// details �̹��� ����
		if(newDetails.size() > 0) {
			imageMapper.deleteByProductId(productId);
			for(ImageVO detail : newDetails) {
				detail.setProductId(productId);
				imageMapper.insertImg(detail);
			}
		}
		
		// ��ǰ ������ ����
		productMapper.updateProduct(productVO);
		int res = productMapper.updateState(productVO.getProductState(), productId);
		
		return res;
	} // end updateProduct

	
	@Override
	public int deleteProductRequest(int productId) {
		log.info("��ǰ ���� ��û");
		int res = productMapper.updateState("���� �����", productId);
				
		return res;
	} // end deleteProductRequest

	@Override
	public List<ImageVO> getAllProductImg(int productId) {
		log.info("��ǰ�� ��� �̹��� �˻�");
		return imageMapper.selectAllbyProductId(productId);
	} // end getAllProductImg

	@Override
	public ImageVO getProductThumbnail(int imgId) {
		log.info("��ǰ�� ����� �̹��� �˻�");
		return imageMapper.selectByImgId(imgId);
	} // end getProductThumbnail

	@Override
	public List<ImageVO> getProductDetails(int productId) {
		log.info("��ǰ�� ���� �̹��� �˻�");
		return imageMapper.selectByProductId(productId);
	} // end getProductDetails


	
}
