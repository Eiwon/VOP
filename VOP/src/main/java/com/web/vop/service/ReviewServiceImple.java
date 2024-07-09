package com.web.vop.service;

import java.text.DecimalFormat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.vop.domain.ProductPreviewDTO;
import com.web.vop.domain.ProductVO;
import com.web.vop.domain.ReviewVO;
import com.web.vop.persistence.ProductMapper;
import com.web.vop.persistence.ReviewMapper;
import com.web.vop.util.PageMaker;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ReviewServiceImple implements ReviewService {

	@Autowired
	private ReviewMapper reviewMapper;

	@Autowired
	private ProductMapper productMapper;

	// ���(����) ���
	@Transactional(value = "transactionManager")// ��� �Է��� ��� �� ���� ���� �� ������ ��հ� ����
	@Override
	public int createReview(ReviewVO reviewVO) {
		log.info("createReview()");
		
		log.info("reviewVO : " + reviewVO);
		
		int productId = reviewVO.getProductId();
        String memberId = reviewVO.getMemberId();

        log.info("productId :" + productId);
        log.info("memberId : " + memberId);
        
        // �ش� ��ǰ�� ȸ���� ���並 �ۼ� �Ǿ��ִ��� Ȯ�� �ϴ��ڵ�(���߿� ���������� ȿ���̰� ��������)
        ReviewVO vo = reviewMapper.selectByReview(productId, memberId);
		
        log.info("vo : " + vo);
        // 
        int insertRes = 0;
        
        // ���䰡 �ִ��� Ȯ��
        if(vo == null) {
        	
        	// ���� ��� 
    		insertRes = reviewMapper.insertReview(reviewVO);
        	
//    		// ��ǰ���� ���� �� ���� ����
//    		int update = productMapper.reviewNumUP(productId);
//    		log.info(update + "�� ���� ����");
    		
        	// �Ҽ��� ù ° �ڸ������� ���
    		DecimalFormat df = new DecimalFormat("#.#");
    		
    		// ���� ��ǰ ��� ī����
    		int reviewNum = productMapper.selectReviewCount(productId);

    		// ��� �� ���� �α�
    		log.info("reviewNum : " + reviewNum);

    		// ��ǰ ��� ī���� ����
    		int updateRes = productMapper.updateReviewNum(productId, reviewNum);
    		
    		// ���� ��� ���� �ڵ�
    		// productId�� �ش��ϴ� ��ǰ ��ȸ // ���׷��̵� �� ����
    		ProductVO productVO = productMapper.selectProduct(productId);

    		int res = 0; // ��� �Է½� �Ҽ��� �Է� �Ұ�
    		String reviewAvg = "0";
    				
    		// ���� �� �� ��
    		res = reviewMapper.selectReviewStar(productId);
    		log.info("����(��) : " + res);

    		// ���� ��� �� reviewStar
    		reviewAvg = df.format((float) res / productVO.getReviewNum());

    		log.info("res : " + res);
    		log.info("reviewAvg : " + reviewAvg);
    		
//    		// ���� ��հ� ���ϱ�
//    		float reviewAvg = reviewMapper.selectReviewAgv(productId);
//    		
//    		log.info("��� �� :" + reviewAvg);
    		
//    		// ���� ��հ� ������Ʈ
//    		int updateResNew = productMapper.updateReviewAvgNew(productId, reviewAvg);
//    		log.info("updateResNew : " + updateResNew);
    		
    		// ���� ��հ� ������Ʈ
    		updateRes = productMapper.updateReviewAvg(productId, reviewAvg);
    		log.info("updateRes : " + updateRes);
    		
        } else {
            log.info(memberId + "���� " + productId + "��ǰ ��ȣ�� �̹� ���(����)�� ��� �Ͽ����ϴ�.");
        }
        log.info("insertRes :" + insertRes);
		return insertRes;
	}

	// ���(����) ��ü �˻�
	@Override
	public List<ReviewVO> getAllReview(int productId) {
		log.info("getAllReview()");
		List<ReviewVO> result = reviewMapper.selectListByReview(productId);
		return result;
	}

	// ���(����) memberId�� ��ü �˻�
	@Override
	public List<ReviewVO> getAllReviewMemberId(String memberId) {
		log.info("getAllReviewMemberId()");
		List<ReviewVO> result = reviewMapper.selectListByReviewMemberId(memberId);
		
		return result;
	}

	// ���(����) productId �׸��� memberId���� �˻�
	@Override
	public ReviewVO selectByReview(int productId, String memberId) {
		log.info("selectByReview()");
		log.info("productId : " + productId);
		log.info("memberId : " + memberId);
		ReviewVO result = reviewMapper.selectByReview(productId, memberId);
		return result;
	}

	// ���(����) ����
	@Transactional(value = "transactionManager")// ��� ���� �� ������ ��� ���� ����
	@Override
	public int updateReview(String memberId, String reviewContent, float reviewStar, int productId) {
		log.info("updateReview()");
		
		// �Ҽ��� ù ° �ڸ������� ���
		DecimalFormat df = new DecimalFormat("#.#");
				
		ReviewVO reviewVO = new ReviewVO();
		// reviewVO�� �� ������� ������ ����
		reviewVO.setMemberId(memberId);
		reviewVO.setReviewContent(reviewContent);
		reviewVO.setReviewStar(reviewStar);
		reviewVO.setProductId(productId);
		log.info("reviewContent: " + reviewContent);
		int updateRes = reviewMapper.updateReview(reviewVO);

		// productId�� �ش��ϴ� ��ǰ ��ȸ // ���׷��̵� �� ����
		  ProductVO productVO = productMapper.selectProduct(productId);
		  
	      int res = 0; // ��� �Է½� �Ҽ��� �Է� �Ұ�
		  String reviewAvg = "0";
	      
	      if(updateRes == 1) {
	    	    // ���� �� ��
				res = reviewMapper.selectReviewStar(productId);
				log.info("����(��) : " + res);
				
				// ���� ��� �� reviewStar
				reviewAvg = df.format((float)res / productVO.getReviewNum());
				
				log.info("res : " + res);
				log.info("reviewAvg : " + reviewAvg);
				
				// ���� ��հ� ������Ʈ
				int result = productMapper.updateReviewAvg(productId, reviewAvg);

				log.info("updateRes : " + result);
	      } 
	      
	      log.info(updateRes + "�� ���� �Ǿ����ϴ�.");

		return updateRes;
	}

	// ���(����) ����
	@Transactional(value = "transactionManager") // ���� ���� �� ��ǰ�� ���� �� ���� ����, ���� ��� �� ����
	@Override
	public int deleteReview(int productId, String memberId) {
		log.info("deleteReview()");
		int deleteRes = reviewMapper.deleteReview(productId, memberId);

		// �Ҽ��� ù ° �ڸ������� ���
		DecimalFormat df = new DecimalFormat("#.#");

		if (deleteRes == 1) {

			// ���� ��ǰ ��� �� ���� ��ȸ
			int reviewNum = productMapper.selectReviewCount(productId);

			// ��� �� ���� �α�
			log.info("reviewNum : " + reviewNum);

			// ��ǰ ��� ī���� ����
			int updateRes = productMapper.updateReviewNum(productId, reviewNum);

			// ���� ��� ���� �ڵ�
			// productId�� �ش��ϴ� ��ǰ ��ȸ // ���׷��̵� �� ����
			ProductVO productVO = productMapper.selectProduct(productId);

			int res = 0; // ��� �Է½� �Ҽ��� �Է� �Ұ�
			String reviewAvg = "0";
			if (productVO.getReviewNum() != 0) { // 0 ������ �� ������ ������ ���Ϳ´�.

				// ���� �� �ش� ��ǰ�� ���� �� ��
				res = reviewMapper.selectReviewStar(productId);
				log.info("����(��) : " + res);

				// ���� ��� �� reviewStar
				reviewAvg = df.format((float) res / productVO.getReviewNum());

				log.info("res : " + res);
				log.info("reviewAvg : " + reviewAvg);

				// ���� ��հ� ������Ʈ
				updateRes = productMapper.updateReviewAvg(productId, reviewAvg);

				log.info("updateRes : " + updateRes);
			} else {
				log.info("�ۼ��� ���䰡 �����ϴ�.");
			}
		}

		log.info(deleteRes + "�� ����");
		return deleteRes;
	}

	// ����¡ ó�� ����Ʈ �˻�
	@Override
	public List<ReviewVO> getAllReviewPaging(int productId, PageMaker pageMaker) {
		log.info("getAllReviewPaging()");
		int totalCnt = reviewMapper.selectListByReviewCnt(productId);
		log.info("totalCnt : " + totalCnt);
		pageMaker.setTotalCount(totalCnt);
		return reviewMapper.selectListByReviewPaging(productId, pageMaker.getPagination());
	}

	@Override
	public ProductPreviewDTO getProductPreview(int productId) {
		
		return productMapper.selectPreviewById(productId);
	}
}
