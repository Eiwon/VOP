package com.web.vop.controller;

import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import org.springframework.web.socket.WebSocketHandler;

import com.web.vop.domain.ImageVO;
import com.web.vop.domain.MemberDetails;
import com.web.vop.domain.MessageVO;
import com.web.vop.domain.ProductVO;
import com.web.vop.domain.ReviewVO;
import com.web.vop.service.ImageService;
import com.web.vop.service.ProductService;
import com.web.vop.service.ReviewService;
import com.web.vop.socket.AlarmHandler;

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
	
//	// ��� �˸��� ������ ���� �˶��ڵ鷯
//	@Autowired
//	public WebSocketHandler alarmHandler;
	
	// ���(����) ��� GET �̵�
	@GetMapping("/register")
	public String createReviewGET(Model model, Integer productId, Integer imgId) {
	    log.info("createReviewGET()");

	    log.info("productId : " + productId);
	    log.info("imgId : " + imgId);
	    
	    
	    
	    // �ش� ��ǰ�� �ִ��� Ȯ�� �ϴ� �ڵ�(��ǰ�� �����Ǿ �ֹ� ��Ͽ� ���� �ֱ� ������ ���� ��)
	    ProductVO productVO = productService.getProductById(productId);
	    
	    // �ش� ��ǰ�� �ִ��� Ȯ��
	    if (productVO != null) {
	        // imgId���� �̹��� ��ȸ
	        ImageVO imageVO = imageService.getImageById(imgId);

	        String imgRealName = imageVO.getImgRealName();
	        String imgExtension = imageVO.getImgExtension();

	        log.info("imageVO : " + imageVO);

//	        // ��� �˶� �۽�
//	        ((AlarmHandler)alarmHandler).sendReplyAlarm(productId);

	        model.addAttribute("imgRealName", imgRealName);
	        model.addAttribute("productId", productId);
	        model.addAttribute("imgId", imgId);
	        model.addAttribute("imgExtension", imgExtension);
	        
	        return "/review/register"; // ��� ��ȯ
	    } else {	
	    	log.info("������ ��ǰ�Դϴ�.");
	        return "/board/main"; // �̵��� ��� ��ȯ
	    }
	}
	
	// ���(����) ���� GET �̵�
	@PreAuthorize("#memberDetails.username == authentication.principal.username")
	@GetMapping("/modify")
	public void updateReviewGET(Model model, Integer productId, Integer imgId, @AuthenticationPrincipal MemberDetails memberDetails) {
		log.info("updateReviewGET()");
		
		String memberId = memberDetails.getUsername();
		
		log.info("memberId = " + memberId);
		log.info("productId : " + productId);
		log.info("imgId : " + imgId);
		
		// productId, memberId�� ���� reviewVO ��ȸ
		ReviewVO reviewVO = reviewService.selectByReview(productId, memberId);
		int reviewId = ((ReviewVO) reviewVO).getReviewId();
		
		// imgId���� �̹��� ��ȸ
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
		List<ProductVO> productList = new ArrayList<>(); // productList �ʱ�ȭ
		
		// ȸ���� �ۼ� �� ��� ����Ʈ���� �ش� ��ǰ ��ȸ
		for (ReviewVO vo : reviewList) {
		    ProductVO product = productService.getProductById(vo.getProductId()); // ���� ProductVO ��ȯ
		    productList.add(product); // productList�� product �߰�
		}
		
		log.info("productList : " + productList);
		log.info("reviewList : " + reviewList);
		
		// ȸ���� ���� �ۼ� �� ����Ʈ
		model.addAttribute("productList", productList);
		
		// ȸ���� �ۼ��� ����
		model.addAttribute("reviewList", reviewList);
	}// end readAllReview()
	
//  ���� ���� �ڵ� �� ��������� ����� �ȳ���~ ��� �ǿ�
//	@PostMapping("/delete") // DELETE : ���(����) ���� 
//	   public String deleteReview(Integer productId, @AuthenticationPrincipal MemberDetails memberDetails){
//	      log.info("deleteReview()");
//	      
//	      String memberId = memberDetails.getUsername();
//			
//		  // memberId Ȯ�� �α�
//		  log.info("memberId = " + memberId);
//	      
//	      // productId�� memberId �ش��ϴ� ���(����) ����
//	      int result = reviewService.deleteReview(productId, memberId);
//
//	      log.info(result + "�� ��� ����");
//	      
//	      // result���� �����ϰ� �����ϴ� ������� �����ϸ� 200 ok�� �����ϴ�.
//	      return "redirect:../review/list";
//	   }// end deleteReview()

}
