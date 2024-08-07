package com.web.vop.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.vop.domain.MemberDetails;
import com.web.vop.domain.ProductPreviewDTO;
import com.web.vop.domain.ReviewVO;
import com.web.vop.service.AWSS3Service;
import com.web.vop.service.ReviewService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/review")
@Log4j
public class ReviewController {
	
	// 컨트롤러에 서비스 제발 하나만
	// ImageService에 메소드 단 하나 있고, 그 메소드를 여기에서만 쓰고 있음 수정 후 삭제 바람
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private AWSS3Service awsS3Service;
	
	
//	// 댓글 알림을 보내기 위한 알람핸들러
//	@Autowired
//	public WebSocketHandler alarmHandler;
	
	// 댓글(리뷰) 등록 GET 이동
	@GetMapping("/register")
	public String createReviewGET(Model model, Integer productId) {
	    log.info("createReviewGET()");

	    log.info("productId : " + productId);

	    ProductPreviewDTO productPreviewDTO = reviewService.getProductPreview(productId);
	    if(productPreviewDTO != null) {
	    	productPreviewDTO.setImgUrl(
	    	awsS3Service.toImageUrl(productPreviewDTO.getImgPath(), productPreviewDTO.getImgChangeName())
	    	);
	    	model.addAttribute("productPreviewDTO", productPreviewDTO);
	    	return "/review/register"; // 경로 반환
	    } else {	
	    	log.info("삭제된 상품입니다.");
	        return "/board/main"; // 이동할 경로 반환
	    }
	}
	
	// 댓글(리뷰) 수정 GET 이동
	@PreAuthorize("#memberDetails.username == authentication.principal.username")
	@GetMapping("/modify")
	public String updateReviewGET(Model model, Integer productId, Integer imgId, @AuthenticationPrincipal MemberDetails memberDetails) {
		log.info("updateReviewGET()");
		
		String memberId = memberDetails.getUsername();
		
		log.info("memberId = " + memberId);
		log.info("productId : " + productId);
		log.info("imgId : " + imgId);

		ProductPreviewDTO productPreviewDTO = reviewService.getProductPreview(productId);
	    if(productPreviewDTO != null) {
	    	productPreviewDTO.setImgUrl(
	    	awsS3Service.toImageUrl(productPreviewDTO.getImgPath(), productPreviewDTO.getImgChangeName())
	    	);
	    	model.addAttribute("productPreviewDTO", productPreviewDTO);
	    	return "/review/modify"; // 경로 반환
	    } else {	
	    	log.info("삭제된 상품입니다.");
	        return "/board/main"; // 이동할 경로 반환
	    }
	} // end loginGET
	
	// 댓글(리뷰) 마이 페이지 검색 GET
	@PreAuthorize("#memberDetails.username == authentication.principal.username")
	@GetMapping("/list") // GET : 댓글(리뷰) 선택(all)
	public void readAllReviewMemberId(Model model, @AuthenticationPrincipal MemberDetails memberDetails){
		log.info("readAllReviewMemberId()");
		
		String memberId = memberDetails.getUsername();
		
		// memberId 확인 로그
		log.info("memberId = " + memberId);
		
		// memberId에 해당하는 댓글(리뷰) list을 전체 검색
		List<ReviewVO> reviewList = reviewService.getAllReviewMemberId(memberId);
		
		// 상품 리스트 정의
		List<ProductPreviewDTO> productList = new ArrayList<>(); // productList 초기화
		
		// 회원이 작성 한 댓글 리스트에서 해당 상품 조회
		for (ReviewVO vo : reviewList) {
			ProductPreviewDTO product = reviewService.getProductPreview(vo.getProductId()); // 단일 ProductVO 반환
			 if(product != null) {
				product.setImgUrl(
			    awsS3Service.toImageUrl(product.getImgPath(), product.getImgChangeName())
			    );
			 }
		    productList.add(product); // productList에 product 추가
		}
		
		log.info("productList : " + productList);
		log.info("reviewList : " + reviewList);
		
		// 회원이 리뷰 작성 한 리스트
		model.addAttribute("productList", productList);
		
		// 회원이 작성한 리뷰
		model.addAttribute("reviewList", reviewList);

	}// end readAllReview()

}
