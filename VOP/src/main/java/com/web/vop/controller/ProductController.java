package com.web.vop.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.web.vop.domain.AlertVO;
import com.web.vop.domain.BasketVO;
import com.web.vop.domain.ImageVO;
import com.web.vop.domain.MemberDetails;
import com.web.vop.domain.PagingListDTO;
import com.web.vop.domain.ProductDetailsDTO;
import com.web.vop.domain.ProductPreviewDTO;
import com.web.vop.domain.ProductVO;
import com.web.vop.domain.SellerVO;
import com.web.vop.service.AWSS3Service;
import com.web.vop.service.ImageService;
import com.web.vop.service.ProductService;
import com.web.vop.util.Constant;
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
	private ImageService imageService;
	
	@Autowired
	private AWSS3Service awsS3Service;
	
	@Autowired
	private String thumbnailUploadPath;
	
	@Autowired
	private String uploadPath;
	
	private static final String[] categoryList = {"여성패션", "남성패션", "남녀 공용 의류", "유아동 패션", "뷰티", "출산/유아동", 
 			"식품", "주방용품", "생활용품", "홈인테리어", "가전디지털", "스포츠/레저", "자동차 용품", "도서/음반/DVD", 
 			"완구/취미", "문구/오피스", "반려동물용품", "헬스/건강식품"};
	
	// 상품 상세 정보 조회
	@GetMapping("/detail")
	public void productDetailGET(Model model, Integer productId) {
		log.info("productDetailGET()");
		
		log.info("productId : " + productId);
		// productId에 해당하는 상품 조회 
		ProductVO productVO = productService.getProductById(productId);	
		
//		// 이미지 코드 조회
//		ImageVO imageVO = imageService.getImageById(productVO.getImgId());
		
		// 상세 이미지 조회
		List<ImageVO> imageList = imageService.getByProductId(productId);
		for(ImageVO image  : imageList) {
			log.info(image);
		}
		
		// 상품 조회 정보
		model.addAttribute("productVO", productVO);
		// 이미지 조회 정보
//		model.addAttribute("imageVO", imageVO);
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
	public String registerPOST(
			ProductVO productVO,  MultipartFile thumbnail, MultipartFile[] details,
			@AuthenticationPrincipal MemberDetails memberDetails) {
		// DB 저장 실패시에는 이미지를 서버에 저장하면 안됨 => DB에 저장 성공을 확인한 후 서버에 저장
		log.info("registerPOST()");
		productVO.setMemberId(memberDetails.getUsername());
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
		
		try {
			int res = productService.registerProduct(productVO, imgThumbnail, imgDetails);
			if(res == 1) { // DB 등록 성공시 S3 서버에 이미지 저장
		    	if(imgThumbnail != null) { // 썸네일 등록
					awsS3Service.uploadIcon(thumbnail, imgThumbnail);
			    }
		    	if (!details[0].isEmpty()) { // 세부정보 이미지 등록
			    	for(int i = 0; i < imgDetails.size(); i++) {
		    		awsS3Service.uploadImage(details[i], imgDetails.get(i));	    			
		    		}
			    }
		    }
			log.info("상품 등록 결과 : " + res);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    return "redirect:../seller/main";
	} // end registerPOST

	@GetMapping("search")
	public void search(Model model, String category, String word, Pagination pagination) {
		log.info("search category : " + category + ", word : " + word);
		List<ProductPreviewDTO> productList;
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		
		if(category == null) {
			category = "전체";
		}
		if(word == null) {
			word = "";
		}
		
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
		awsS3Service.toImageUrl(productList);
		model.addAttribute("productList", productList);
		model.addAttribute("pageMaker", pageMaker);
		model.addAttribute("category", category); // 검색결과 내에서 페이지 이동을 구현하기 위해, 기존 검색 조건 return
		model.addAttribute("word", word);
		
	} // end search
	
	// 해당 유저가 등록한 상품 검색
	@GetMapping("/myList")
	@ResponseBody
	public ResponseEntity<PagingListDTO<ProductPreviewDTO>> productList(
			Pagination pagination, @AuthenticationPrincipal MemberDetails memberDetails) {
		String memberId = memberDetails.getUsername();
		log.info(memberId + "가 등록한 상품 검색");

		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		
		List<ProductPreviewDTO> productPreviewList = productService.searchByMemberId(memberId, pageMaker);
		awsS3Service.toImageUrl(productPreviewList);
		
		pageMaker.update();
		
		PagingListDTO<ProductPreviewDTO> pagingList = new PagingListDTO<>();
		pagingList.setList(productPreviewList);
		pagingList.setPageMaker(pageMaker);
		
		return new ResponseEntity<PagingListDTO<ProductPreviewDTO>>(pagingList, HttpStatus.OK);
	} // end productList
	
//	// 썸네일 이미지 파일 요청
//	@GetMapping(value = "/showImg", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//	@ResponseBody
//	public ResponseEntity<Resource> showImg(int imgId){
//		log.info("showImg() : " + imgId);
//		ImageVO imageVO = imageService.getImageById(imgId);
//		if(imageVO == null) {
//			return new ResponseEntity<Resource>(null, null, HttpStatus.OK);
//		}
//		String fullPath = imageVO.getImgPath() + File.separator + imageVO.getImgChangeName();
//		HttpHeaders headers = new HttpHeaders();
//		// 다운로드할 파일 이름을 헤더에 설정
//		headers.add(HttpHeaders.CONTENT_DISPOSITION,
//				"attachment; filename=" + fullPath + "." + imageVO.getImgExtension());
//
//		Resource resource = FileUploadUtil.getFile(fullPath, imageVO.getImgExtension());
//       
//        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
//	} // end showImg
  
	
	@GetMapping("/bestReview")
	@ResponseBody
	public ResponseEntity<Map<String, List<ProductPreviewDTO>>> getBestProductByCategory(){
		log.info("각 카테고리별 최고 리뷰 상품 5개 요청");
		List<ProductPreviewDTO> list = productService.getTopProductByCategory();
		awsS3Service.toImageUrl(list);
		
		// key : 카테고리 이름, value : 상품 리스트 형태로 변환
		Map<String, List<ProductPreviewDTO>> resultMap = new HashMap<>();
		String category;
		for(ProductPreviewDTO product : list) {
			category = product.getProductVO().getCategory();
			if(!resultMap.containsKey(category)) { // 맵에 해당 카테고리가 없다면 공간 생성
				resultMap.put(category, new LinkedList<>());
			}
			resultMap.get(category).add(product);
		}
		return new ResponseEntity<Map<String, List<ProductPreviewDTO>>>(resultMap, HttpStatus.OK);
	} // end getBestProductInCategory
	
	@GetMapping("/recent")
	@ResponseBody
	public ResponseEntity<List<ProductPreviewDTO>> getRecent5(){
		log.info("최근 등록된 상품 5개 요청");
		
		List<ProductPreviewDTO> list = productService.getRecent5();
		log.info("검색 결과 : " + list);
		
		awsS3Service.toImageUrl(list);
		
		return new ResponseEntity<List<ProductPreviewDTO>>(list, HttpStatus.OK);
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
		
		ImageVO newThumbnail = null;
		ImageVO oldThumbnail = null;
		List<ImageVO> newDetails = new ArrayList<>();
		List<ImageVO> oldDetails = new ArrayList<>();
		
		// 변경할 이미지가 있다면 DB에 저장하기 위해 VO로 변환, 기존 이미지를 S3 서버에서 삭제하기 위해 이미지 정보를 불러옴
		if (!thumbnail.isEmpty()) {
			newThumbnail = FileUploadUtil.toImageVO(thumbnail, thumbnailUploadPath);
			oldThumbnail = productService.getProductThumbnail(productVO.getImgId());
		}
		if (!details[0].isEmpty()) {
			oldDetails = productService.getProductDetails(productVO.getProductId());
			for (MultipartFile detail : details) {
				newDetails.add(FileUploadUtil.toImageVO(detail, uploadPath));
			}
		}
		
		// 변경할 정보를 service에 전달
		try {
			int res = productService.updateProduct(productVO, newThumbnail, newDetails);
			if (res == 1) { // 저장 성공시 서버에 파일 저장
				if (!thumbnail.isEmpty()) {
					awsS3Service.removeImage(oldThumbnail);
					awsS3Service.uploadIcon(thumbnail, newThumbnail);
				}
				if (!details[0].isEmpty()) {
					for(ImageVO oldDetail : oldDetails) {
						awsS3Service.removeImage(oldDetail);
					}
					for (int i = 0; i < details.length; i++) {
						awsS3Service.uploadImage(details[i], newDetails.get(i));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
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
		
		// 삭제할 상품의 img 정보 불러오기
		List<ImageVO> imgList = productService.getAllProductImg(productVO.getProductId());
		
		// 상품 삭제
		int res = productService.deleteProduct(productVO.getProductId());
		
		// 서버에서 모든 관련 이미지 삭제
		if(res == 1) {
			for(ImageVO image : imgList) {
			awsS3Service.removeImage(image);
			}
		}
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateProductState
	
	// 상품 삭제 요청 등록 (삭제 가능한 상태면 삭제)
	@DeleteMapping("/request")
	@ResponseBody
	public ResponseEntity<Integer> deleteRequestProduct(@RequestBody int productId) {
		log.info("상품 삭제 요청 : " + productId);
		String productState = productService.selectStateByProductId(productId);
		
		int res = 0;
		if (productState.equals(Constant.STATE_SELL)) {
			res = productService.setProductState(Constant.STATE_REMOVE_WAIT, productId);
		} else if (!productState.equals(Constant.STATE_REMOVE_WAIT)) {
			productService.deleteProduct(productId);
			//res = delete(productId);
		}
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end deleteRequestProduct
	
	
	// 상품 등록 요청 조회
	@GetMapping("/registerRequest")
	@ResponseBody
	public ResponseEntity<PagingListDTO<ProductVO>> getRegisterRequest(Pagination pagination) {
		log.info("모든 상품 등록 요청 조회");
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		List<ProductVO> list = productService.searchByState(Constant.STATE_APPROVAL_WAIT, pageMaker);
		log.info(list);

		pageMaker.update();

		PagingListDTO<ProductVO> pagingList = new PagingListDTO<>();
		pagingList.setList(list);
		pagingList.setPageMaker(pageMaker);

		return new ResponseEntity<PagingListDTO<ProductVO>>(pagingList, HttpStatus.OK);
	} // end getWaitProduct

	// 상품 삭제 요청 조회
	@GetMapping("/deleteRequest")
	@ResponseBody
	public ResponseEntity<PagingListDTO<ProductVO>> getdeleteRequest(Pagination pagination) {
		log.info("상품 삭제 요청 조회");
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		List<ProductVO> list = productService.searchByState(Constant.STATE_REMOVE_WAIT, pageMaker);
		log.info(list);

		pageMaker.update();
		
		PagingListDTO<ProductVO> pagingList = new PagingListDTO<>();
		pagingList.setList(list);
		pagingList.setPageMaker(pageMaker);

		return new ResponseEntity<PagingListDTO<ProductVO>>(pagingList, HttpStatus.OK);
	} // end getWaitProduct
	
} 
	
	

