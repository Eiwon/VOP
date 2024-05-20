package com.web.vop.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.vop.domain.BasketVO;
import com.web.vop.domain.ImageVO;
import com.web.vop.domain.ProductDetailsDTO;
import com.web.vop.domain.ProductVO;
import com.web.vop.service.ImageService;
import com.web.vop.service.ProductService;
import com.web.vop.util.FileUploadUtil;
import com.web.vop.util.PageMaker;
import com.web.vop.util.Pagination;


import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/product")
@Log4j
public class ProductController {
	
	@Autowired
	// ProductService 클래스에 있는 기능을 사용하기위해 생성
	private ProductService productService;
	
	@Autowired
	private String thumbnailUploadPath;
	
	@Autowired
	private String uploadPath;
	
	@Autowired
	private ImageService imageService;
	
	private static final String[] categoryList = {"여성패션", "남성패션", "남녀 공용 의류", "유아동 패션", "뷰티", "출산/유아동", 
 			"식품", "주방용품", "생활용품", "홈인테리어", "가전디지털", "스포츠/레저", "자동차 용품", "도서/음반/DVD", 
 			"완구/취미", "문구/오피스", "반려동물용품", "헬스/건강식품"};
	
	// 상품 상세 정보 조회
	@GetMapping("/detail")
	public void productDetailGET(Model model, Integer productId) {
		log.info("productDetailGET()");
		
		// 소수점 첫 째 자리까지만 출력
		DecimalFormat df = new DecimalFormat("#.#");//
		
		log.info("productId : " + productId);
		// productId에 해당하는 상품 조회 
		ProductVO productVO = productService.getProductById(productId);
		
		// 댓글 총 갯수 조회
//		int reviewCount = productService.selectReviewByCount(productId);
//		log.info("reviewCount" + reviewCount);
		
		// 리뷰 평균 값 코드
		int res = 0; // 댓글 입력시 소수점 입력 불가
		String reviewStar = "0";
		if(productVO.getReviewNum() != 0) { //0 이하일 때 무한의 에러가 나와온다.
			res = productService.selectReviewByStar(productId);
			log.info("리뷰(별) : " + res);
			// 리뷰 평균 값
			reviewStar = df.format((float)res / productVO.getReviewNum());
		}
		log.info("res : " + res);
		log.info("reviewStar : " + reviewStar);
		
		ImageVO imageVO = imageService.getImageById(productVO.getImgId());
		
		List<ImageVO> imageList = imageService.getByProductId(productId);
		for(ImageVO image  : imageList) {
			log.info(image);
		}
		
		// 댓글 갯수 정보
//		model.addAttribute("reviewCount", reviewCount);
		// 상품 조회 정보
		model.addAttribute("productVO", productVO);
		// 리뷰 평균 정보
		model.addAttribute("reviewStar", reviewStar);
		// 이미지 조회 정보
		model.addAttribute("imageVO", imageVO);
		// 상품 설명 이미지 조회 정보
		model.addAttribute("imageList", imageList);
		
		// 해당 경로
		log.info("/product/detail get");
	} // end productDetail()
	
	
	
	
	@GetMapping("/register")
	public void registerGET() {
		log.info("registerGET()");
	} // end productRegister()

	@GetMapping("/myProduct")
	public void myProductGET() {
		log.info("자신이 등록한 상품 목록 페이지 이동");
	} // end myProductGET
	
	@PostMapping("/register")
	public String registerPOST(ProductVO productVO,  MultipartFile thumbnail, MultipartFile[] details) {
		// DB 저장 실패시에는 이미지를 서버에 저장하면 안됨 => DB에 저장 성공을 확인한 후 서버에 저장
		log.info("registerPOST()");
		log.info(productVO);
		log.info("파일 명 : " + thumbnail.getOriginalFilename());
		ImageVO imgThumbnail = null;
		List<ImageVO> imgDetails = new ArrayList<>();
		
		
		// 모든 파일을 imageVO로 변환
		if (!thumbnail.isEmpty()) { // 파일이 있는 경우
			imgThumbnail = FileUploadUtil.toImageVO(thumbnail, thumbnailUploadPath);
		}
		if(!details[0].isEmpty()) {
			for (MultipartFile file : details) {
				imgDetails.add(FileUploadUtil.toImageVO(file, uploadPath));
			}
		}
		// DB에 상품 정보 등록
	    int res = productService.registerProduct(productVO, imgThumbnail, imgDetails);
	    log.info("상품 등록 결과 : " + res);
	    
	    if(res == 1) { // DB 저장 성공시 서버에 저장 
	    	if(imgThumbnail != null) {
	    		FileUploadUtil.saveIcon(thumbnailUploadPath, thumbnail, imgThumbnail.getImgChangeName());
	    	}
	    	if (!details[0].isEmpty()) {
	    		for(int i = 0; i < imgDetails.size(); i++) {
	    			FileUploadUtil.saveFile(uploadPath, details[i], imgDetails.get(i).getImgChangeName());	    			
	    		}
	    	}
	    }
	    return "redirect:../seller/sellerRequest";
	} // end registerPOST

	

	@GetMapping("search")
	public void search(Model model, String category, String word, Pagination pagination) {
		log.info("search category : " + category + ", word : " + word);
		List<ProductVO> productList = new ArrayList<>();
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		
		if(category.equals("전체")) { // 카테고리가 전체, 검색어가 있는 경우
			log.info("검색어 검색");
			productList = productService.searchByName(word, pageMaker);
		}else {
			if(word.length() > 0) { // 카테고리가 있고, 검색어도 있는 경우
				log.info("카테고리 + 검색어 검색");
				productList = productService.searchByNameInCategory(category, word, pageMaker);
			}else { // 카테고리가 있고, 검색어는 없는 경우
				log.info("카테고리 검색");
				productList = productService.searchByCategory(category, pageMaker);
			}
		}
		// 카테고리가 전체, 검색어도 없는 경우 -- 클라이언트 측에서 실행 X
		log.info("검색결과 = 총 " + pageMaker.getTotalCount() + "개 검색");
		model.addAttribute("productList", productList);
		model.addAttribute("pageMaker", pageMaker);
		model.addAttribute("category", category); // 검색결과 내에서 페이지 이동을 구현하기 위해, 기존 검색 조건 return
		model.addAttribute("word", word);
		
	} // end search
	
	// 해당 유저가 등록한 상품 검색
	@GetMapping("/myList")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> productList(Pagination pagination, HttpServletRequest request) {
		String memberId = (String) request.getSession().getAttribute("memberId");
		log.info(memberId + "가 등록한 상품 검색");

		Map<String, Object> resultMap = new HashMap<>();
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);

		List<ProductVO> productList = productService.searchByMemberId(memberId, pageMaker);

		if (productList != null) {
			log.info(productList.size() + "개 데이터 검색 성공");
		}

		resultMap.put("productList", productList);
		resultMap.put("pageMaker", pageMaker);

		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	} // end productList
	
	// 썸네일 이미지 파일 요청
	@GetMapping(value = "/showImg", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity<Resource> showImg(int imgId){
		log.info("showImg() : " + imgId);
		ImageVO imageVO = imageService.getImageById(imgId);
		if(imageVO == null) {
			return new ResponseEntity<Resource>(null, null, HttpStatus.OK);
		}
		String fullPath = imageVO.getImgPath() + File.separator + imageVO.getImgChangeName();
		HttpHeaders headers = new HttpHeaders();
		// 다운로드할 파일 이름을 헤더에 설정
		headers.add(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=" + fullPath + "." + imageVO.getImgExtension());

		Resource resource = FileUploadUtil.getFile(fullPath, imageVO.getImgExtension());
       
        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	} // end showImg
  
	
	@GetMapping("/bestReview")
	@ResponseBody
	public ResponseEntity<Map<String, List<ProductVO>>> getBestProductByCategory(){
		log.info("각 카테고리별 최고 리뷰 상품 5개 요청");
		Map<String, List<ProductVO>> resultMap = new HashMap<>();
		
		for(String category : categoryList) {
			List<ProductVO> list = productService.getTopProductInCategory(category);
			log.info(category + " 검색 결과 : " + list);
			resultMap.put(category, list);
		}
		return new ResponseEntity<Map<String, List<ProductVO>>>(resultMap, HttpStatus.OK);
	} // end getBestProductInCategory
	
	@GetMapping("/recent")
	@ResponseBody
	public ResponseEntity<List<ProductVO>> getRecent5(){
		log.info("최근 등록된 상품 5개 요청");
		
		List<ProductVO> list = productService.getRecent5();
		log.info("검색 결과 : " + list);
		return new ResponseEntity<List<ProductVO>>(list, HttpStatus.OK);
	} // end getRecent5
	
	
	@GetMapping("/searchProduct")
	@ResponseBody
	public ResponseEntity<?> searchProduct(@RequestParam("category") String category, @RequestParam("word") String word) {
        log.info("searchProduct()");
        
     // 검색 결과를 JSON 형태로 가공
        Map<String, Object> result = new HashMap<>();
        result.put("카테고리 : ", category);
        result.put("입력한 단어 : ", word);
        
        // ResponseEntity에 JSON 데이터를 담아서 반환
        return ResponseEntity.ok(result);
    }// end searchProduct()

	// 상품 정보 수정
	@PostMapping("/update")
	public String updateProduct(ProductVO productVO, MultipartFile thumbnail, MultipartFile[] details) {
		log.info("----------상품 수정---------------------");
		log.info("상품 정보 : " + productVO + ", 썸네일 유무 : " + !thumbnail.isEmpty());
		ImageVO thumbnailVO = null;
		List<ImageVO> detailsList = new ArrayList<>();

		// DB 변경
		// 변경할 이미지가 있다면, DB에 저장하기 위해 VO로 변환
		if (!thumbnail.isEmpty()) {
			thumbnailVO = FileUploadUtil.toImageVO(thumbnail, thumbnailUploadPath);
		}
		if (!details[0].isEmpty()) {
			for (MultipartFile detail : details) {
				detailsList.add(FileUploadUtil.toImageVO(detail, uploadPath));
			}
		}

		// 변경할 정보를 service에 전달 (transaction 필요)
		int res = productService.updateProduct(productVO, thumbnailVO, detailsList);

		if (res == 1) { // 저장 성공시 서버에 파일 저장
			if (!thumbnail.isEmpty()) {
				FileUploadUtil.saveIcon(thumbnailUploadPath, thumbnail, thumbnailVO.getImgChangeName());
			}
			if (!details[0].isEmpty()) {
				for (int i = 0; i < details.length; i++) {
					FileUploadUtil.saveFile(uploadPath, details[i], detailsList.get(i).getImgChangeName());
				}
			}
		}
		return "redirect:popupUpdate?productId=" + productVO.getProductId();
	} // end updateProduct

	
	// 상품 상세정보 팝업으로 이동
	@GetMapping("/popupDetails")
	public void popupDetailsGET(Model model, int productId) {
		log.info("상품 상세 정보 팝업 요청 " + productId);

		ProductDetailsDTO productDetails = productService.getDetails(productId);
		log.info("상세 정보 검색 결과 : " + productDetails);

		model.addAttribute("productDetails", productDetails);
	} // end popupProductDetailsGET

	// 상품 정보 수정 팝업으로 이동
	@GetMapping("/popupUpdate")
	public void popupUpdateGET(Model model, int productId) {
		log.info("상품 정보 수정 팝업 요청 " + productId);

		ProductDetailsDTO productDetails = productService.getDetails(productId);
		log.info("상세 정보 검색 결과 : " + productDetails);

		try { // 자바스크립트에서 사용하기 위해 JSON으로 변환 후 전송
			model.addAttribute("productDetails", new ObjectMapper().writeValueAsString(productDetails));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	} // end popupProductUpdateGET
	
	// 상품 상태 변경
	@PutMapping("/changeState")
	@ResponseBody
	public ResponseEntity<Integer> updateProductState(@RequestBody ProductVO productVO) {
		log.info("상품 상태 변경 : " + productVO.getProductId());
		int res = productService.setProductState(productVO.getProductState(), productVO.getProductId());
		log.info(res + "행 수정 성공");
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateProductState

	// 상품 즉시 삭제
	@DeleteMapping("/product")
	@ResponseBody
	public ResponseEntity<Integer> deleteProduct(@RequestBody ProductVO productVO) {
		log.info("상품 삭제 : " + productVO.getProductId());
		int res = delete(productVO.getProductId());
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateProductState
	
	// 상품 삭제 요청 등록 (삭제 가능한 상태면 삭제)
	@DeleteMapping("/request")
	@ResponseBody
	public ResponseEntity<Integer> deleteRequestProduct(@RequestBody int productId) {
		log.info("상품 삭제 요청 : " + productId);
		String productState = productService.selectStateByProductId(productId);
		
		int res = 0;
		if (productState.equals("판매중")) {
			res = productService.setProductState("삭제 대기중", productId);
		} else if (!productState.equals("삭제 대기중")) {
			res = delete(productId);
		}
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end deleteRequestProduct
	
	
	// 상품 등록 요청 조회
	@GetMapping("/registerRequest")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getRegisterRequest(Pagination pagination) {
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

	// 상품 삭제 요청 조회
	@GetMapping("/deleteRequest")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getdeleteRequest(Pagination pagination) {
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
	
	
	private int delete(int productId) {
		List<ImageVO> imageList = productService.deleteProduct(productId);
		
		if(imageList.size() > 0) { // 서버에 저장된 이미지 삭제
			log.info("관련 이미지 : " + imageList.size() + "건");
			for(ImageVO image : imageList) {
				FileUploadUtil.deleteFile(image);
			}
		}
		return imageList.size();
	} // end delete
} 
	
	

