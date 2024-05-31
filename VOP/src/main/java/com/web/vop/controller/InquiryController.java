package com.web.vop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.vop.domain.InquiryVO;
import com.web.vop.service.InquiryService;


import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/inquiry")
@Log4j
public class InquiryController {

	@Autowired
	private InquiryService inquiryService;
	
	// 문의 전체 검색 코드
	// 해당 상품id에 있는 문의 검색
	// 동기 방식으로 만들었습니다. 근데 상품 상세정보에 비동기로 불려왔습니다.
	@GetMapping("/list")
	public void listInquiryGET(Model model, Integer productId) {
		log.info("listInquiryGET()");
		log.info("productId : " + productId);
		List<InquiryVO> listInquiry = inquiryService.getAllInquiry(productId);
		
		log.info("listInquiry : " + listInquiry);
		
		model.addAttribute("listInquiry", listInquiry);
//		model.addAttribute("listInquiry", productName);
	}
	
//	@PostMapping("/delete") // DELETE : 댓글(리뷰) 삭제 // 나중에 데이터 받는 거에 따라 달라짐
//	public String deleteInquiry(Integer productId, String memberId){
//		log.info("deleteInquiry()");
//		
//		int result = inquiryService.deleteInquiry(productId, memberId);
//				
//		log.info(result + "행 삭제 완료");
//		return "redirect:../board/orderlist";
//	}   
//	
//	@DeleteMapping("/delete") // DELETE : 댓글(리뷰) 삭제 // 나중에 데이터 받는 거에 따라 달라짐
//	   public ResponseEntity<Integer> deleteInquiry(
//			   @RequestBody InquiryVO inquiryVO) {
//	      log.info("deleteInquiry()");
//	      
//	      log.info("productId : " + inquiryVO.getProductId());
//	      log.info("memberId : " + inquiryVO.getMemberId());
//	      
//	      // productId에 해당하는 reviewId의 댓글(리뷰)
//	      int result = inquiryService.deleteInquiry(inquiryVO.getProductId(), inquiryVO.getMemberId());
//	
//	      log.info(result + "행 문의 삭제");
//	      
//	      // result값을 전송하고 리턴하는 방식으로 성공하면 200 ok를 갔습니다.
//	      return new ResponseEntity<Integer>(result, HttpStatus.OK);
//	   }// end deleteInquiry()
}
