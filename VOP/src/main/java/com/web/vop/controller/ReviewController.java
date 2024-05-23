package com.web.vop.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.web.vop.domain.ImageVO;
import com.web.vop.domain.MemberDetails;
import com.web.vop.domain.ProductVO;
import com.web.vop.domain.ReviewVO;
import com.web.vop.service.ImageService;
import com.web.vop.service.ProductService;
import com.web.vop.service.ReviewService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/review")
@Log4j
public class ReviewController {
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private ProductService productService;
	
	// 댓글(리뷰) 등록 GET
	@GetMapping("/register")
	public void createReviewGET(Model model, Integer productId, Integer imgId) {
		log.info("createReviewGET()");
		
		log.info("productId : " + productId);
		log.info("imgId : " + imgId);

		// imgId통해 이미지 조회
		ImageVO imageVO = imageService.getImageById(imgId);
		
		String imgRealName = imageVO.getImgRealName();
		String imgExtension = imageVO.getImgExtension();
		
		log.info("imageVO : " + imageVO);
		
		log.info("imgRealName : " + imgRealName);
		log.info("imgExtension : " + imgExtension);
		
		model.addAttribute("imgRealName", imgRealName);
		model.addAttribute("productId", productId);
		model.addAttribute("imgId", imgId);
		model.addAttribute("imgExtension", imgExtension);
	} // end loginGET
	
	
	// 댓글(리뷰) 수정 GET
	@GetMapping("/modify")
	public void updateReviewGET(Model model, Integer productId, Integer imgId, @AuthenticationPrincipal MemberDetails memberDetails) {
		log.info("updateReviewGET()");
		
		String memberId = memberDetails.getUsername();
		
		log.info("memberId = " + memberId);
		log.info("productId : " + productId);
		log.info("imgId : " + imgId);
		
		// productId, memberId를 통행 reviewVO 조회
		ReviewVO reviewVO = reviewService.selectByReview(productId, memberId);
		int reviewId = ((ReviewVO) reviewVO).getReviewId();
		
		// imgId통해 이미지 조회
		ImageVO imageVO = imageService.getImageById(imgId);
		
		String imgRealName = imageVO.getImgRealName();
		String imgExtension = imageVO.getImgExtension();
		
		log.info("imageVO : " + imageVO);
		
		log.info("imgRealName : " + imgRealName);
		log.info("imgExtension : " + imgExtension);
		
		model.addAttribute("productId", productId);
		model.addAttribute("imgRealName", imgRealName);
		model.addAttribute("reviewId", reviewId);
		model.addAttribute("imgId", imgId);
		model.addAttribute("imgExtension", imgExtension);
	} // end loginGET
	
//	// 댓글(리뷰) 전체 검색 GET
	@GetMapping("/list") // GET : 댓글(리뷰) 선택(all)
	public void readAllReviewMemberId(Model model, @AuthenticationPrincipal MemberDetails memberDetails){
		log.info("readAllReviewMemberId()");
		
		String memberId = memberDetails.getUsername();
		
		// memberId 확인 로그
		log.info("memberId = " + memberId);
		
		// memberId에 해당하는 댓글(리뷰) list을 전체 검색
		List<ReviewVO> reviewList = reviewService.getAllReviewMemberId(memberId);
		
		// 상품 리스트 정의
		List<ProductVO> productList = new ArrayList<>(); // productList 초기화
		
		// 회원이 작성 한 댓글 리스트에서 해당 상품 조회
		for (ReviewVO vo : reviewList) {
		    ProductVO product = productService.getProductById(vo.getProductId()); // 단일 ProductVO 반환
		    productList.add(product); // productList에 product 추가
		}
		
		log.info("productList : " + productList);
		log.info("reviewList" + reviewList);
		
		// 회원이 리뷰 작성 한 리스트
		model.addAttribute("productList", productList);
		
		// 회원이 작성한 리뷰
		model.addAttribute("reviewList", reviewList);
	}// end readAllReview()
	
	@Transactional(value = "transactionManager") // 리뷰 삭제 후 상품의 리뷰 총 갯수 검색 후 다시 상품 총 갯수 컬럼 등록
	@PostMapping("/delete") // DELETE : 댓글(리뷰) 삭제 
	   public String deleteReview(Integer productId, @AuthenticationPrincipal MemberDetails memberDetails){
	      log.info("deleteReview()");
	      
	      String memberId = memberDetails.getUsername();
			
		  // memberId 확인 로그
		  log.info("memberId = " + memberId);
	      
	      // 소수점 첫 째 자리까지만 출력
	      DecimalFormat df = new DecimalFormat("#.#");
	      
	      // productId에 해당하는 reviewId의 댓글(리뷰)
	      int result = reviewService.deleteReview(productId, memberId);
   
	   			if(result == 1) {
	   				
	   				// 현재 상품 댓글 카운터 
	   				int reviewNum =  productService.selectReviewByCount(productId);
	   				
	   				// 댓글 총 갯수 로그
	   				log.info("reviewNum : " + reviewNum);
	   				
	   				// 상품 댓글 카운터 수정
	   				int updateRes = productService.updateReviewNum(productId, reviewNum);
	   				
	   				// 리뷰 평균 관련 코드
	   				// productId에 해당하는 상품 조회 // 업그레이드 된 상태
	   				ProductVO productVO = productService.getProductById(productId);
	   				
	   				int res = 0; // 댓글 입력시 소수점 입력 불가
	   				String reviewAvg = "0";
	   				if(productVO.getReviewNum() != 0) { //0 이하일 때 무한의 에러가 나와온다.
	   					// 리뷰 총 합
	   					res = productService.selectReviewStar(productId);
	   					log.info("리뷰(별) : " + res);
	   					
	   					// 리뷰 평균 값 reviewStar
	   					reviewAvg = df.format((float)res / productVO.getReviewNum());
	   					
	   					log.info("res : " + res);
	   					log.info("reviewAvg : " + reviewAvg);
	   					
	   					// 리뷰 평균값 업데이트
	   					updateRes = productService.updateReviewAvg(productId, reviewAvg);

	   					log.info("updateRes : " + updateRes);
	   				}
	   			}

	      log.info(result + "행 댓글 삭제");
	      
	      // result값을 전송하고 리턴하는 방식으로 성공하면 200 ok를 갔습니다.
	      return "redirect:../board/orderlist";
	   }// end deleteReview()

}
