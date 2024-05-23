package com.web.vop.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.web.vop.domain.ImageVO;
import com.web.vop.domain.ProductDetailsDTO;
import com.web.vop.domain.ProductVO;
import com.web.vop.persistence.Constant;
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
	
	@Autowired
	private String thumbnailUploadPath;
	
	@Autowired
	private String uploadPath;
	
	@Autowired
	private AWSS3Service awsS3Service; 
	
	// ��ǰ �� ���� �˻�
	@Override
	public ProductVO getProductById(int productId) {
		log.info("getProductById()");
		ProductVO result = productMapper.selectProduct(productId);
//		log.info("��ǰ �� ���� : " + result.toString());
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
	
	// productId �ش��ϴ� ���� �� �� 
	@Override
	public int selectReviewStar(int productId) {
		log.info("selectReviewByStar()");
		int res = productMapper.selectReviewStar(productId);
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
	public int registerProduct(ProductVO productVO, MultipartFile thumbnail, MultipartFile[] details) throws IOException { // ��� ������, ����� ��ǰ id ��ȯ
		log.info("registerProduct : " + productVO);
		int res = 0;
		
		ImageVO imgThumbnail = null;
		List<ImageVO> imgDetails = new ArrayList<>();
		
		// ��� ������ imageVO�� ��ȯ
		if (!thumbnail.isEmpty()) { // ������ �ִ� ���
			imgThumbnail = FileUploadUtil.toImageVO(thumbnail, thumbnailUploadPath);
		}
		if(!details[0].isEmpty()) {
			for (MultipartFile file : details) {
				imgDetails.add(FileUploadUtil.toImageVO(file, uploadPath));
			}
		}
		
		// DB�� ��ǰ ���� ���
		if(thumbnail != null) {
			int imgId = imageService.registerImage(imgThumbnail);
			productVO.setImgId(imgId);
		}
		productVO.setProductState(Constant.STATE_SELL);
		productMapper.insertProduct(productVO);
		int productId = productMapper.selectLastInsertId();
		
		for(ImageVO detail : imgDetails) {
			detail.setProductId(productId);
			res = imageMapper.insertImg(detail);
		}
		 
		if(res == 1) { // DB ���� ������ ������ ���� 
		    if(imgThumbnail != null) {
					awsS3Service.uploadIcon(thumbnail, imgThumbnail);
		    }
		    if (!details[0].isEmpty()) {
		    	for(int i = 0; i < imgDetails.size(); i++) {
		    		awsS3Service.uploadImage(details[i], imgDetails.get(i));	    			
		    	}
		    }
		}
		return res;
	} // end registerProduct
	
	@Override
	public List<ProductVO> searchByCategory(String category, PageMaker pageMaker) {
		log.info("searchByCategory()");
		int totalCnt = productMapper.selectByCategoryCnt(category, Constant.STATE_SELL);
		pageMaker.setTotalCount(totalCnt);
		return productMapper.selectByCategory(category, pageMaker.getPagination(), Constant.STATE_SELL);
	} // end selectByCategory
	
	@Override
	public List<ProductVO> searchByName(String productName, PageMaker pageMaker) {
		log.info("searchByName()");
		String includeName = '%' + productName + '%';
		int totalCnt = productMapper.selectByNameCnt(includeName, Constant.STATE_SELL);
		pageMaker.setTotalCount(totalCnt);
		return productMapper.selectByName(includeName, pageMaker.getPagination(), Constant.STATE_SELL);
	} // end selectByName
	
	@Override
	public List<ProductVO> searchByNameInCategory(String category, String productName, PageMaker pageMaker) {
		log.info("searchByNameInCategory()");
		String includeName = '%' + productName + '%';
		int totalCnt = productMapper.selectByNameInCategoryCnt(category, includeName, Constant.STATE_SELL);
		pageMaker.setTotalCount(totalCnt);
		return productMapper.selectByNameInCategory(category, includeName, pageMaker.getPagination(), Constant.STATE_SELL);
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

	@Transactional(value = "transactionManager")
	@Override
	public int deleteProduct(int productId) {
		log.info("deleteProduct()");
		ProductVO target = productMapper.selectProduct(productId);
		int imgId = target.getImgId();
		
		// ��ǰ �̹����� �����ؾ���
		List<ImageVO> imageList = imageMapper.selectAllbyProductId(productId);
		ImageVO imageVO = imageMapper.selectByImgId(imgId);
		if(imageVO != null) {
			imageList.add(imageVO);
		}
		// DB�� ����� �̹��� ���� ����
		imageMapper.deleteById(imgId);
		productMapper.deleteProduct(productId);
		imageMapper.deleteProductImage(productId);
		
		for(ImageVO image : imageList) {
			awsS3Service.removeImage(image);
		}
		
		return 1;
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
		log.info(details);
		List<Integer> imgIds = imageService.getImgId(productId);
		if(imgIds != null) {
			log.info(imgIds);
			details.setImgIdDetails(imgIds);			
		}
		return details;
	} // end getDetails

	@Transactional(value = "transactionManager")
	@Override
	public int updateProduct(ProductVO productVO, MultipartFile thumbnail, MultipartFile[] details) throws IOException {
		log.info("updateProduct()");
		int productId = productVO.getProductId();
		int oldThumbnailId = productVO.getImgId();
		
		ImageVO thumbnailVO = null;
		List<ImageVO> detailsList = new ArrayList<>();

		// DB ����
		// ������ �̹����� �ִٸ�, DB�� �����ϱ� ���� VO�� ��ȯ
		if (!thumbnail.isEmpty()) {
			thumbnailVO = FileUploadUtil.toImageVO(thumbnail, thumbnailUploadPath);
		}
		if (!details[0].isEmpty()) {
			for (MultipartFile detail : details) {
				detailsList.add(FileUploadUtil.toImageVO(detail, uploadPath));
			}
		}
		
		// thumbnail �̹��� ����
		if(thumbnailVO != null) {// �̹����� ����Ǿ��ٸ�, ���� �̹��� ����, �� �̹��� �߰�
			if(productVO.getImgId() > 0) {
				imageMapper.deleteById(oldThumbnailId);
			}
			imageMapper.insertImg(thumbnailVO);
			int newImgId = imageMapper.selectRecentImgId();
			log.info(newImgId);
			productVO.setImgId(newImgId);
		}
		
		// details �̹��� ����
		if(detailsList.size() > 0) {
			imageMapper.deleteByProductId(productId);
			for(ImageVO detail : detailsList) {
				detail.setProductId(productId);
				imageMapper.insertImg(detail);
			}
		}
		
		// ��ǰ ������ ����
		productMapper.updateProduct(productVO);
		int res = productMapper.updateState(productVO.getProductState(), productId);
		
		if (res == 1) { // ���� ������ ������ ���� ����
			if (!thumbnail.isEmpty()) {
				awsS3Service.uploadIcon(thumbnail, thumbnailVO);
			}
			if (!details[0].isEmpty()) {
				for (int i = 0; i < details.length; i++) {
					awsS3Service.uploadImage(details[i], detailsList.get(i));
				}
			}
		}
		
		return res;
	} // end updateProduct

	
	@Override
	public int deleteProductRequest(int productId) {
		log.info("��ǰ ���� ��û");
		int res = productMapper.updateState("���� �����", productId);
				
		return res;
	} // end deleteProductRequest
	

	
}
