package com.web.vop.service;

import java.text.DecimalFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.vop.domain.ProductVO;
import com.web.vop.domain.ReviewVO;
import com.web.vop.persistence.ProductMapper;
import com.web.vop.persistence.ReviewMapper;

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
		int insertRes = reviewMapper.insertReview(reviewVO);
		
		log.info("reviewVO : " + reviewVO);
		
		// �Ҽ��� ù ° �ڸ������� ���
		DecimalFormat df = new DecimalFormat("#.#");
		
		int productId = reviewVO.getProductId();
		

		// ����� �Ϸ� �Ǿ����� ��� �� ���� ����
		if (insertRes == 1) {

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
//			if (productVO.getReviewNum() != 0) { // 0 ������ �� ������ ������ ���Ϳ´�.
				// ���� �� ��
				res = productMapper.selectReviewStar(productId);
				log.info("����(��) : " + res);

				// ���� ��� �� reviewStar
				reviewAvg = df.format((float) res / productVO.getReviewNum());

				log.info("res : " + res);
				log.info("reviewAvg : " + reviewAvg);

				// ���� ��հ� ������Ʈ
				updateRes = productMapper.updateReviewAvg(productId, reviewAvg);

				log.info("updateRes : " + updateRes);
//			}
		  }
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
	public int updateReview(int reviewId, String reviewContent, float reviewStar, int productId) {
		log.info("updateReview()");
		
		// �Ҽ��� ù ° �ڸ������� ���
		DecimalFormat df = new DecimalFormat("#.#");
				
		ReviewVO reviewVO = new ReviewVO();
		// reviewVO�� �� ������� ������ ����
		reviewVO.setReviewId(reviewId);
		reviewVO.setReviewContent(reviewContent);
		reviewVO.setReviewStar(reviewStar);
		int updateRes = reviewMapper.updateReview(reviewVO);

		// productId�� �ش��ϴ� ��ǰ ��ȸ // ���׷��̵� �� ����
		  ProductVO productVO = productMapper.selectProduct(productId);
		  
	      int res = 0; // ��� �Է½� �Ҽ��� �Է� �Ұ�
		  String reviewAvg = "0";
	      
	      if(updateRes == 1) {
	    	    // ���� �� ��
				res = productMapper.selectReviewStar(productId);
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
				res = productMapper.selectReviewStar(productId);
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

}
