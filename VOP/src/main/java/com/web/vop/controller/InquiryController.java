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
import com.web.vop.util.PageMaker;
import com.web.vop.util.Pagination;

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
	public void listInquiryGET(Model model, Integer productId, Pagination pagination) {
		log.info("listInquiryGET()");
		log.info("productId : " + productId);
		
		//페이지 메이커에 기본 쪽수값 저장
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		log.info("쪽수 기본값 : " + pageMaker.getPagination());
		
		// 페이징 처리용 productId사용해서 리스트 불려오는 코드
		List<InquiryVO> listInquiry = inquiryService.getAllInquiryPaging(productId, pageMaker);
		
		log.info("listInquiry : " + listInquiry);
		
		model.addAttribute("pageMaker", pageMaker);
		model.addAttribute("productId", productId);
		model.addAttribute("listInquiry", listInquiry);
	}
	
	@GetMapping("/myList")
	public void myListInquiryGET() {
		log.info("myListInquiryGET()");
	}
	
//	@GetMapping("/myList")
//	public void myListInquiryGET(Model model, String memberId, Pagination pagination) {
//		log.info("listInquiryGET()");
//		log.info("memberId : " + memberId);
//		
//		//페이지 메이커에 기본 쪽수값 저장
//		PageMaker pageMaker = new PageMaker();
//		pageMaker.setPagination(pagination);
//		log.info("쪽수 기본값 : " + pageMaker.getPagination());
//		
//		List<InquiryVO> listInquiry = inquiryService.getAllInquiryMemberIdPaging(memberId, pageMaker);
//		
//		log.info("listInquiry : " + listInquiry);
//		
//		model.addAttribute("pageMaker", pageMaker);
//		model.addAttribute("memberId", memberId);
//		model.addAttribute("listInquiry", listInquiry);
//	}
	
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
