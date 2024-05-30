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
	
	// 댓글(리뷰) 등록 GET 이동
	@GetMapping("/register")
	public String createReviewGET(Model model, Integer productId, Integer imgId) {
	    log.info("createReviewGET()");

	    log.info("productId : " + productId);
	    log.info("imgId : " + imgId);

	    ProductVO productVO = productService.getProductById(productId);

	    if (productVO != null) {
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
	        
	        return "/review/register"; // 경로 반환
	    } else {	
	    	log.info("삭제된 상품입니다.");
	        return "/board/main"; // 이동할 경로 반환
	    }
	}
	
	
	// 댓글(리뷰) 수정 GET 이동
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
	
	@PostMapping("/delete") // DELETE : 댓글(리뷰) 삭제 
	   public String deleteReview(Integer productId, @AuthenticationPrincipal MemberDetails memberDetails){
	      log.info("deleteReview()");
	      
	      String memberId = memberDetails.getUsername();
			
		  // memberId 확인 로그
		  log.info("memberId = " + memberId);
	      
	      // productId와 memberId 해당하는 댓글(리뷰) 삭제
	      int result = reviewService.deleteReview(productId, memberId);

	      log.info(result + "행 댓글 삭제");
	      
	      // result값을 전송하고 리턴하는 방식으로 성공하면 200 ok를 갔습니다.
	      return "redirect:../review/list";
	   }// end deleteReview()

}
