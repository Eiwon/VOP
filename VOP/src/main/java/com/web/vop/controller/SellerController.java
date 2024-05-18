package com.web.vop.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.vop.domain.ImageVO;
import com.web.vop.domain.MemberVO;
import com.web.vop.domain.ProductDetailsDTO;
import com.web.vop.domain.ProductVO;
import com.web.vop.domain.SellerVO;
import com.web.vop.service.ImageService;
import com.web.vop.service.MemberService;
import com.web.vop.service.ProductService;
import com.web.vop.service.SellerService;
import com.web.vop.util.FileUploadUtil;
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
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private String thumbnailUploadPath;
	
	@Autowired
	private String uploadPath;
	
	@GetMapping("sellerRequest")
	public void sellerRequestGET() {
		log.info("판매자 권한 신청 페이지로 이동");
	} // end sellerRequestGET
	
	@GetMapping("registerProduct")
	public String registerProductGET() {
		log.info("상품 등록 페이지로 이동");
		return "redirect:../product/register";
	} // end registerProductGET
	
	@GetMapping("/admin")
	public void adminGET() {
		log.info("관리자 페이지로 이동");
	} // end myInfoGet
	
	@GetMapping("/myProduct")
	public void listProductGET() {
		log.info("상품 조회 페이지 이동");
	} // end listProductGET
	
	// 해당 유저가 등록한 상품 검색
	@GetMapping("/productList")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> productList(Pagination pagination, HttpServletRequest request){
		String memberId = (String)request.getSession().getAttribute("memberId");
		log.info(memberId + "가 등록한 상품 검색");
		
		Map<String, Object> resultMap = new HashMap<>();
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		
		List<ProductVO> productList = productService.searchByMemberId(memberId, pageMaker);
		
		if(productList != null) {
			log.info(productList.size() + "개 데이터 검색 성공");
		}
		
		resultMap.put("productList", productList);
		resultMap.put("pageMaker", pageMaker);
		
		return new ResponseEntity<Map<String,Object>>(resultMap, HttpStatus.OK);
	} // end productList
	
	// 상품 정보 수정
	@PostMapping("/updateProduct")
	public void updateProduct(ProductVO productVO, MultipartFile thumbnail, MultipartFile[] details) {
		log.info("----------상품 수정---------------------");
		log.info("상품 정보 : " + productVO + ", 썸네일 유무 : " + !thumbnail.isEmpty());
		ImageVO thumbnailVO = null;
		List<ImageVO> detailsList = new ArrayList<>();
		
		// DB 변경
		// 변경할 이미지가 있다면, DB에 저장하기 위해 VO로 변환
		if(!thumbnail.isEmpty()) {  
			thumbnailVO = FileUploadUtil.toImageVO(thumbnail, thumbnailUploadPath);
		}
		if(!details[0].isEmpty()) {
			for(MultipartFile detail : details) {
				detailsList.add(FileUploadUtil.toImageVO(detail, uploadPath));
			}
		}
		
		// 변경할 정보를 service에 전달 (transaction 필요)
		int res = productService.updateProduct(productVO, thumbnailVO, detailsList);
		
		if(res == 1) { // 저장 성공시 서버에 파일 저장
			if(!thumbnail.isEmpty()) {
				FileUploadUtil.saveIcon(thumbnailUploadPath, thumbnail, thumbnailVO.getImgChangeName());
			}
			if(!details[0].isEmpty()) {
				for(int i = 0; i < details.length; i++) {
					FileUploadUtil.saveFile(uploadPath, details[i], detailsList.get(i).getImgChangeName());
				}
			}
		}
	} // end updateProduct
	

	// 자신의 판매자 권한 요청 조회
	@GetMapping("/my/{memberId}")
	@ResponseBody
	public ResponseEntity<SellerVO> getMyRequest(@PathVariable("memberId") String memberId){
		log.info("내 권한요청 조회");
		SellerVO result = sellerService.getMyRequest(memberId);
			
		return new ResponseEntity<SellerVO>(result, HttpStatus.OK);
	} // end getMyRequest
		
		
	// 판매자 권한 요청 등록
	@PostMapping("/registerReq")
	@ResponseBody
	public ResponseEntity<Integer> registerRequest(@RequestBody SellerVO sellerVO) {
		log.info("요청 등록 : " + sellerVO);
		int res = sellerService.registerRequest(sellerVO);

		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end registerRequest
		
	// 판매자 권한 요청 수정(유저)
	@PutMapping("/updateReq")
	@ResponseBody
	public ResponseEntity<Integer> updateRequest(@RequestBody SellerVO sellerVO) {
		log.info("요청 수정 : " + sellerVO);
		int res = sellerService.updateMemberContent(sellerVO);

		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateRequest

	// 판매자 권한 요청 승인/거절(관리자)
	@PutMapping("/approval")
	@ResponseBody
	public ResponseEntity<Integer> approveRequest(@RequestBody SellerVO sellerVO) {
		log.info("요청 승인 / 거절 : " + sellerVO.getMemberId());
		int res = sellerService.approveRequest(sellerVO);

		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end refuseRequest
		
	// 판매자 권한 요청 삭제
	@DeleteMapping("/delete/{memberId}")
	@ResponseBody
	public ResponseEntity<Integer> deleteRequest(@PathVariable("memberId") String memberId) {
		log.info("요청 삭제 : " + memberId);
		int res = sellerService.deleteRequest(memberId);

		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end deleteRequest

	
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
	
	// 상품 등록 요청 조회
	@GetMapping("/productReq")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getWaitProduct(Pagination pagination){
		log.info("모든 상품 등록 요청 조회");
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		List<ProductVO> list = productService.searchByState("승인 대기중", pageMaker);
		log.info(list);
		pageMaker.update();
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("pageMaker", pageMaker);
		resultMap.put("list", list);

		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	} // end getWaitProduct
	
	// 상품 삭제 요청 등록 (삭제 가능한 상태면 삭제)
	@DeleteMapping("/productReq")
	@ResponseBody
	public ResponseEntity<Integer> deleteRequestProduct(@RequestBody int productId) {
		log.info("상품 삭제 요청 : " + productId);
		int res = sellerService.deleteProductRequest(productId);

		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end deleteRequestProduct
	
	// 상품 삭제 요청 조회
	@GetMapping("/productDeleteReq")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getDeleteProductReq(Pagination pagination){
		log.info("상품 삭제 요청 조회");
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		List<ProductVO> list = productService.searchByState("삭제 대기중", pageMaker);
		log.info(list);
		pageMaker.update();
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("pageMaker", pageMaker);
		resultMap.put("list", list);

		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	} // end getWaitProduct
	
	// 판매자 상세정보 팝업으로 이동
	@GetMapping("/popupSellerDetails")
	public void popupSellerDetailsGET(Model model, String memberId) {
		log.info("판매자 상세 정보 팝업 요청 " + memberId);
		SellerVO sellerVO = sellerService.getMyRequest(memberId);
		MemberVO memberVO = memberService.getMemberInfo(memberId);
		model.addAttribute("sellerVO", sellerVO);
		model.addAttribute("memberVO", memberVO);
	} // end popupSellerReqGET
	
	// 상품 상세정보 팝업으로 이동
	@GetMapping("/popupProductDetails")
	public void popupProductDetailsGET(Model model, int productId) {
		log.info("상품 상세 정보 팝업 요청 " + productId);
		
		ProductDetailsDTO productDetails = productService.getDetails(productId);
		productDetails.setImgIdDetails(imageService.getImgId(productId));
		log.info("상세 정보 검색 결과 : " + productDetails);
		
		model.addAttribute("productDetails", productDetails);
	} // end popupProductDetailsGET
	
	// 상품 정보 수정 팝업으로 이동
	@GetMapping("/popupProductUpdate")
	public void popupProductUpdateGET(Model model, int productId) {
		log.info("상품 정보 수정 팝업 요청 " + productId);
		
		ProductDetailsDTO productDetails = productService.getDetails(productId);
		productDetails.setImgIdDetails(imageService.getImgId(productId));
		log.info("상세 정보 검색 결과 : " + productDetails);
		
		try {
			model.addAttribute("productDetails", new ObjectMapper().writeValueAsString(productDetails));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
	} // end popupProductUpdateGET
	
	// 상품 상태 변경
	@PutMapping("/productState")
	@ResponseBody
	public ResponseEntity<Integer> updateProductState(@RequestBody ProductVO productVO){
		log.info("상품 상태 변경 : " + productVO.getProductId());
		int res = productService.setProductState(productVO.getProductState(), productVO.getProductId());
		log.info(res + "행 수정 성공");
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateProductState
	
	// 상품 즉시 삭제
	@DeleteMapping("/product")
	@ResponseBody
	public ResponseEntity<Integer> deleteProduct(@RequestBody ProductVO productVO){
		log.info("상품 삭제 : " + productVO.getProductId());
		int res = productService.deleteProduct(productVO.getProductId());
		log.info(res + "행 삭제 성공");
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateProductState
	
}
