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

	// 댓글(리뷰) 등록
	@Transactional(value = "transactionManager")// 댓글 입력후 댓글 총 갯수 수정 및 리뷰의 평균값 수정
	@Override
	public int createReview(ReviewVO reviewVO) {
		log.info("createReview()");
		
		log.info("reviewVO : " + reviewVO);
		
		int productId = reviewVO.getProductId();
        String memberId = reviewVO.getMemberId();

        log.info("productId :" + productId);
        log.info("memberId : " + memberId);
        
        // 해당 상품에 회원이 리뷰를 작성 되어있는지 확인 하는코드(나중에 쿼리문에서 효율이게 수정가능)
        ReviewVO vo = reviewMapper.selectByReview(productId, memberId);
		
        log.info("vo : " + vo);
        // 
        int insertRes = 0;
        
        // 리뷰가 있는지 확인
        if(vo == null) {
        	
        	// 리뷰 등록 
    		insertRes = reviewMapper.insertReview(reviewVO);
        	
//    		// 상품에서 리뷰 총 개수 증가
//    		int update = productMapper.reviewNumUP(productId);
//    		log.info(update + "행 개수 증가");
    		
        	// 소수점 첫 째 자리까지만 출력
    		DecimalFormat df = new DecimalFormat("#.#");
    		
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
    				
    		// 리뷰 별 총 합
    		res = reviewMapper.selectReviewStar(productId);
    		log.info("리뷰(별) : " + res);

    		// 리뷰 평균 값 reviewStar
    		reviewAvg = df.format((float) res / productVO.getReviewNum());

    		log.info("res : " + res);
    		log.info("reviewAvg : " + reviewAvg);
    		
//    		// 리뷰 평균값 구하기
//    		float reviewAvg = reviewMapper.selectReviewAgv(productId);
//    		
//    		log.info("평균 값 :" + reviewAvg);
    		
//    		// 리뷰 평균값 업데이트
//    		int updateResNew = productMapper.updateReviewAvgNew(productId, reviewAvg);
//    		log.info("updateResNew : " + updateResNew);
    		
    		// 리뷰 평균값 업데이트
    		updateRes = productMapper.updateReviewAvg(productId, reviewAvg);
    		log.info("updateRes : " + updateRes);
    		
        } else {
            log.info(memberId + "님은 " + productId + "상품 번호에 이미 댓글(리뷰)를 등록 하였습니다.");
        }
        log.info("insertRes :" + insertRes);
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
	public int updateReview(String memberId, String reviewContent, float reviewStar, int productId) {
		log.info("updateReview()");
		
		// 소수점 첫 째 자리까지만 출력
		DecimalFormat df = new DecimalFormat("#.#");
				
		ReviewVO reviewVO = new ReviewVO();
		// reviewVO에 각 변경사항 변수들 저장
		reviewVO.setMemberId(memberId);
		reviewVO.setReviewContent(reviewContent);
		reviewVO.setReviewStar(reviewStar);
		reviewVO.setProductId(productId);
		log.info("reviewContent: " + reviewContent);
		int updateRes = reviewMapper.updateReview(reviewVO);

		// productId에 해당하는 상품 조회 // 업그레이드 된 상태
		  ProductVO productVO = productMapper.selectProduct(productId);
		  
	      int res = 0; // 댓글 입력시 소수점 입력 불가
		  String reviewAvg = "0";
	      
	      if(updateRes == 1) {
	    	    // 리뷰 총 합
				res = reviewMapper.selectReviewStar(productId);
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
				res = reviewMapper.selectReviewStar(productId);
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

	// 페이징 처리 리스트 검색
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
