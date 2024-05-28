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

	// 댓글(리뷰) 등록
	@Transactional(value = "transactionManager")// 댓글 입력후 댓글 총 갯수 수정 및 리뷰의 평균값 수정
	@Override
	public int createReview(ReviewVO reviewVO) {
		log.info("createReview()");
		int insertRes = reviewMapper.insertReview(reviewVO);
		
		log.info("reviewVO : " + reviewVO);
		
		// 소수점 첫 째 자리까지만 출력
		DecimalFormat df = new DecimalFormat("#.#");
		
		int productId = reviewVO.getProductId();
		

		// 등록이 완료 되었을때 댓글 총 갯수 증가
		if (insertRes == 1) {

			// 현재 상품 댓글 카운터
			int reviewNum = productMapper.selectReviewCount(productId);

			// 댓글 총 갯수 로그
			log.info("reviewNum : " + reviewNum);

			// 상품 댓글 카운터 수정
			int updateRes = productMapper.updateReviewNum(productId, reviewNum);

			// 리뷰 평균 관련 코드
			// productId에 해당하는 상품 조회 // 업그레이드 된 상태
			ProductVO productVO = productMapper.selectProduct(productId);

			int res = 0; // 댓글 입력시 소수점 입력 불가
			String reviewAvg = "0";
//			if (productVO.getReviewNum() != 0) { // 0 이하일 때 무한의 에러가 나와온다.
				// 리뷰 총 합
				res = productMapper.selectReviewStar(productId);
				log.info("리뷰(별) : " + res);

				// 리뷰 평균 값 reviewStar
				reviewAvg = df.format((float) res / productVO.getReviewNum());

				log.info("res : " + res);
				log.info("reviewAvg : " + reviewAvg);

				// 리뷰 평균값 업데이트
				updateRes = productMapper.updateReviewAvg(productId, reviewAvg);

				log.info("updateRes : " + updateRes);
//			}
		  }
		return insertRes;
	}

	// 댓글(리뷰) 전체 검색
	@Override
	public List<ReviewVO> getAllReview(int productId) {
		log.info("getAllReview()");
		List<ReviewVO> result = reviewMapper.selectListByReview(productId);
		return result;
	}

	// 댓글(리뷰) memberId로 전체 검색
	@Override
	public List<ReviewVO> getAllReviewMemberId(String memberId) {
		log.info("getAllReviewMemberId()");
		List<ReviewVO> result = reviewMapper.selectListByReviewMemberId(memberId);
		return result;
	}

	// 댓글(리뷰) productId 그리고 memberId통해 검색
	@Override
	public ReviewVO selectByReview(int productId, String memberId) {
		log.info("selectByReview()");
		log.info("productId : " + productId);
		log.info("memberId : " + memberId);
		ReviewVO result = reviewMapper.selectByReview(productId, memberId);
		return result;
	}

	// 댓글(리뷰) 수정
	@Transactional(value = "transactionManager")// 댓글 수정 후 리뷰의 평균 점수 수정
	@Override
	public int updateReview(int reviewId, String reviewContent, float reviewStar, int productId) {
		log.info("updateReview()");
		
		// 소수점 첫 째 자리까지만 출력
		DecimalFormat df = new DecimalFormat("#.#");
				
		ReviewVO reviewVO = new ReviewVO();
		// reviewVO에 각 변경사항 변수들 저장
		reviewVO.setReviewId(reviewId);
		reviewVO.setReviewContent(reviewContent);
		reviewVO.setReviewStar(reviewStar);
		int updateRes = reviewMapper.updateReview(reviewVO);

		// productId에 해당하는 상품 조회 // 업그레이드 된 상태
		  ProductVO productVO = productMapper.selectProduct(productId);
		  
	      int res = 0; // 댓글 입력시 소수점 입력 불가
		  String reviewAvg = "0";
	      
	      if(updateRes == 1) {
	    	    // 리뷰 총 합
				res = productMapper.selectReviewStar(productId);
				log.info("리뷰(별) : " + res);
				
				// 리뷰 평균 값 reviewStar
				reviewAvg = df.format((float)res / productVO.getReviewNum());
				
				log.info("res : " + res);
				log.info("reviewAvg : " + reviewAvg);
				
				// 리뷰 평균값 업데이트
				int result = productMapper.updateReviewAvg(productId, reviewAvg);

				log.info("updateRes : " + result);
	      } 
	      
	      log.info(updateRes + "행 수정 되었습니다.");

		return updateRes;
	}

	// 댓글(리뷰) 삭제
	@Transactional(value = "transactionManager") // 리뷰 삭제 후 상품의 리뷰 총 갯수 수정, 리뷰 평균 값 수정
	@Override
	public int deleteReview(int productId, String memberId) {
		log.info("deleteReview()");
		int deleteRes = reviewMapper.deleteReview(productId, memberId);

		// 소수점 첫 째 자리까지만 출력
		DecimalFormat df = new DecimalFormat("#.#");

		if (deleteRes == 1) {

			// 현재 상품 댓글 총 갯수 조회
			int reviewNum = productMapper.selectReviewCount(productId);

			// 댓글 총 갯수 로그
			log.info("reviewNum : " + reviewNum);

			// 상품 댓글 카운터 수정
			int updateRes = productMapper.updateReviewNum(productId, reviewNum);

			// 리뷰 평균 관련 코드
			// productId에 해당하는 상품 조회 // 업그레이드 된 상태
			ProductVO productVO = productMapper.selectProduct(productId);

			int res = 0; // 댓글 입력시 소수점 입력 불가
			String reviewAvg = "0";
			if (productVO.getReviewNum() != 0) { // 0 이하일 때 무한의 에러가 나와온다.

				// 삭제 후 해당 상품의 리뷰 총 합
				res = productMapper.selectReviewStar(productId);
				log.info("리뷰(별) : " + res);

				// 리뷰 평균 값 reviewStar
				reviewAvg = df.format((float) res / productVO.getReviewNum());

				log.info("res : " + res);
				log.info("reviewAvg : " + reviewAvg);

				// 리뷰 평균값 업데이트
				updateRes = productMapper.updateReviewAvg(productId, reviewAvg);

				log.info("updateRes : " + updateRes);
			} else {
				log.info("작성된 리뷰가 없습니다.");
			}
		}

		log.info(deleteRes + "행 삭제");
		return deleteRes;
	}

}
