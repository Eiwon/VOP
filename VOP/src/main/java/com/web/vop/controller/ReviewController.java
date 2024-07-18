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
	
	// ��Ʈ�ѷ��� ���� ���� �ϳ���
	// ImageService�� �޼ҵ� �� �ϳ� �ְ�, �� �޼ҵ带 ���⿡���� ���� ���� ���� �� ���� �ٶ�
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private AWSS3Service awsS3Service;
	
	
//	// ��� �˸��� ������ ���� �˶��ڵ鷯
//	@Autowired
//	public WebSocketHandler alarmHandler;
	
	// ���(����) ��� GET �̵�
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
	    	return "/review/register"; // ��� ��ȯ
	    } else {	
	    	log.info("������ ��ǰ�Դϴ�.");
	        return "/board/main"; // �̵��� ��� ��ȯ
	    }
	}
	
	// ���(����) ���� GET �̵�
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
	    	return "/review/modify"; // ��� ��ȯ
	    } else {	
	    	log.info("������ ��ǰ�Դϴ�.");
	        return "/board/main"; // �̵��� ��� ��ȯ
	    }
	} // end loginGET
	
	// ���(����) ���� ������ �˻� GET
	@PreAuthorize("#memberDetails.username == authentication.principal.username")
	@GetMapping("/list") // GET : ���(����) ����(all)
	public void readAllReviewMemberId(Model model, @AuthenticationPrincipal MemberDetails memberDetails){
		log.info("readAllReviewMemberId()");
		
		String memberId = memberDetails.getUsername();
		
		// memberId Ȯ�� �α�
		log.info("memberId = " + memberId);
		
		// memberId�� �ش��ϴ� ���(����) list�� ��ü �˻�
		List<ReviewVO> reviewList = reviewService.getAllReviewMemberId(memberId);
		
		// ��ǰ ����Ʈ ����
		List<ProductPreviewDTO> productList = new ArrayList<>(); // productList �ʱ�ȭ
		
		// ȸ���� �ۼ� �� ��� ����Ʈ���� �ش� ��ǰ ��ȸ
		for (ReviewVO vo : reviewList) {
			ProductPreviewDTO product = reviewService.getProductPreview(vo.getProductId()); // ���� ProductVO ��ȯ
			 if(product != null) {
				product.setImgUrl(
			    awsS3Service.toImageUrl(product.getImgPath(), product.getImgChangeName())
			    );
			 }
		    productList.add(product); // productList�� product �߰�
		}
		
		log.info("productList : " + productList);
		log.info("reviewList : " + reviewList);
		
		// ȸ���� ���� �ۼ� �� ����Ʈ
		model.addAttribute("productList", productList);
		
		// ȸ���� �ۼ��� ����
		model.addAttribute("reviewList", reviewList);

	}// end readAllReview()

}
