package com.web.vop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.vop.domain.MemberVO;
import com.web.vop.domain.ProductVO;
import com.web.vop.domain.SellerVO;
import com.web.vop.service.MemberService;
import com.web.vop.service.ProductService;
import com.web.vop.service.SellerService;
import com.web.vop.util.PageMaker;
import com.web.vop.util.Pagination;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/seller")
@Log4j
public class SellerController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private SellerService sellerService;
	
	@Autowired
	private MemberService memberService;
	
	@GetMapping("sellerRequest")
	public void sellerRequestGET() {
		log.info("판매자 권한 신청 페이지로 이동");
	} // end sellerRequestGET
	
	@GetMapping("registerProduct")
	public String registerProductGET() {
		log.info("상품 등록 페이지로 이동");
		return "redirect:../product/register";
	} // end registerProductGET
	
	@GetMapping("listProduct")
	public void listProductGET(Model model, String memberId, Pagination pagination) {
		log.info("상품 조회 페이지 이동 by " + memberId);
		
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		pageMaker.setTotalCount(productService.getCntByMemberId(memberId));
		List<ProductVO> productList = productService.selectByMemberId(memberId, pageMaker.getPagination());
		
		if(productList != null) {
			log.info(productList.size() + "개 데이터 검색 성공");
		}
		model.addAttribute("pageMaker", pageMaker);
		model.addAttribute("productList", productList);
	} // end listProductGET
	
	@GetMapping("updateProduct")
	public void updateProductGET(Model model, String productId) {
		log.info("상품 수정 페이지 요청"); // 상세 검색 기능 필요
		
	} // end updateProductGET
	

	// 자신의 권한 요청 조회
	@GetMapping("/my/{memberId}")
	@ResponseBody
	public ResponseEntity<SellerVO> getMyRequest(@PathVariable("memberId") String memberId){
		log.info("내 권한요청 조회");
		SellerVO result = sellerService.getMyRequest(memberId);
			
		return new ResponseEntity<SellerVO>(result, HttpStatus.OK);
	} // end getMyRequest
		
		
	// 요청 등록
	@PostMapping("/registerReq")
	@ResponseBody
	public ResponseEntity<Integer> registerRequest(@RequestBody SellerVO sellerVO) {
		log.info("요청 등록 : " + sellerVO);
		int res = sellerService.registerRequest(sellerVO);

		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end registerRequest
		
	// 요청 수정(유저)
	@PutMapping("/updateReq")
	@ResponseBody
	public ResponseEntity<Integer> updateRequest(@RequestBody SellerVO sellerVO) {
		log.info("요청 수정 : " + sellerVO);
		int res = sellerService.updateMemberContent(sellerVO);

		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateRequest

	// 요청 승인/거절(관리자)
	@PutMapping("/approval")
	@ResponseBody
	public ResponseEntity<Integer> approveRequest(@RequestBody SellerVO sellerVO) {
		log.info("요청 거절 : " + sellerVO.getMemberId());
		int res = sellerService.approveRequest(sellerVO);

		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end refuseRequest
		
	// 요청 삭제
	@DeleteMapping("/delete/{memberId}")
	@ResponseBody
	public ResponseEntity<Integer> deleteRequest(@PathVariable("memberId") String memberId) {
		log.info("요청 삭제 : " + memberId);
		int res = sellerService.deleteRequest(memberId);

		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end deleteRequest

	// 상품 삭제 요청
	@PostMapping("/delReqProduct")
	@ResponseBody
	public ResponseEntity<Integer> deleteRequestProduct(String productId) {
		log.info("상품 삭제 요청 : " + productId);
		int pid = Integer.parseInt(productId);
		// 판매 중인 상품이면 삭제 대기중 상태로 전환, (판매 중, 삭제 대기중) 상태가 아니라면 즉시 삭제
		String productState = productService.selectStateByProductId(pid);
		log.info("현재 상태 : " + productState);

		int res = 0;
		if (productState.equals("판매중")) {
			if (productService.setProductState("삭제 대기중", pid) == 1)
				res = 1;
		} else if (!productState.equals("삭제 대기중")) {
			if (productService.deleteProduct(pid) == 1)
				res = 2;
		}

		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end deleteRequestProduct
	
	// 판매자 권한 요청 조회
	@GetMapping("/wait")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getWaitRequest(Pagination pagination) {
		log.info("모든 승인 대기중인 권한요청 조회");
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		List<SellerVO> list = sellerService.getRequestByState("승인 대기중", pageMaker.getPagination());
		log.info(list);
		int requestCount = sellerService.getRequestByStateCnt("승인 대기중");
		pageMaker.setTotalCount(requestCount);
		pageMaker.update();
		
		log.info("pageMaker : " + pageMaker.getEndNum());
		Map<String, Object> resultMap = new HashMap<>(); // 반환할 타입이 2개이므로 pageMaker와 list를 담을 맵 생성
		resultMap.put("pageMaker", pageMaker);
		resultMap.put("list", list);

		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	} // end getWaitRequest

	// 등록된 판매자 조회
	@GetMapping("/approved")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getApprovedRequest(Pagination pagination) {
		log.info("모든 승인된 요청 조회");
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		List<SellerVO> list = sellerService.getRequestByState("승인", pageMaker.getPagination());
		log.info(list);
		int requestCount = sellerService.getRequestByStateCnt("승인");
		pageMaker.setTotalCount(requestCount);
		pageMaker.update();
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("pageMaker", pageMaker);
		resultMap.put("list", list);

		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	} // end getAllRequest
	
	@GetMapping("/productReq")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getWaitProduct(Pagination pagination){
		log.info("모든 상품 등록 요청 조회");
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		List<ProductVO> list = productService.getStateIs("승인 대기중", pageMaker.getPagination());
		log.info(list);
		int requestCount = productService.getStateIsCnt("승인 대기중");
		pageMaker.setTotalCount(requestCount);
		pageMaker.update();
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("pageMaker", pageMaker);
		resultMap.put("list", list);

		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	} // end getWaitProduct
	
	@GetMapping("/productDeleteReq")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getDeleteProductReq(Pagination pagination){
		log.info("상품 삭제 요청 조회");
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		List<ProductVO> list = productService.getStateIs("삭제 대기중", pageMaker.getPagination());
		log.info(list);
		int requestCount = productService.getStateIsCnt("삭제 대기중");
		pageMaker.setTotalCount(requestCount);
		pageMaker.update();
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("pageMaker", pageMaker);
		resultMap.put("list", list);

		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	} // end getWaitProduct
	
	@GetMapping("/popupSellerReg")
	public void popupSellerReqGET(Model model, String memberId) {
		log.info("판매자 등록 팝업 요청 " + memberId);
		SellerVO sellerVO = sellerService.getMyRequest(memberId);
		MemberVO memberVO = memberService.getMemberInfo(memberId);
		model.addAttribute("sellerVO", sellerVO);
		model.addAttribute("memberVO", memberVO);
	} // end popupSellerReqGET
	
	
}
