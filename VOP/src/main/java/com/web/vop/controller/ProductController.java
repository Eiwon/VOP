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
import org.springframework.security.access.prepost.PreAuthorize;
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
import org.springframework.web.socket.WebSocketHandler;

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
import com.web.vop.socket.AlarmHandler;
import com.web.vop.util.Constant;
import com.web.vop.util.FileAnalyzerUtil;
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
	private AWSS3Service awsS3Service;
	
	@Autowired
	private String thumbnailUploadPath;
	
	@Autowired
	private String uploadPath;
	
	//@Autowired
	//private WebSocketHandler alarmHandler;
	
	// 상품 상세 정보 조회
	@GetMapping("/detail")
	public void productDetailGET(Model model, Integer productId) {
		log.info("productDetailGET()");
		
		// 상품, 이미지 테이블 정보 검색 일부 컬럼 조인 해서 조인
		ProductDetailsDTO productDetails = productService.getDetails(productId);
		log.info("상세 정보 검색 결과 : " + productDetails);
		
		// 썸네일 이미지의 경로와 변경된 이름을 이용하여 AWS S3의 URL을 생성하고 설정합니다.
		productDetails.setThumbnailUrl(
				awsS3Service.toImageUrl(productDetails.getThumbnail().getImgPath(), productDetails.getThumbnail().getImgChangeName())
				);
		// 상세 이미지 리스트를 가져옵니다.
		List<ImageVO> list = productDetails.getDetails();
		
		// 상세 이미지 URL을 저장할 리스트를 초기화합니다.
		productDetails.setDetailsUrl(new ArrayList<>());
		
		// 상세 이미지 리스트를 순회하면서 각 이미지의 경로와 변경된 이름을 이용하여 AWS S3의 URL을 생성하고 추가합니다.
		for(ImageVO image : list) {
			productDetails.getDetailsUrl().add(awsS3Service.toImageUrl(image.getImgPath(), image.getImgChangeName()));
		}
		
		// model 객체에 productDetails를 추가하여 뷰에서 사용할 수 있도록 합니다.
		model.addAttribute("productDetails", productDetails);
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
	public String registerPOST(Model model,
			ProductVO productVO,  MultipartFile thumbnail, MultipartFile[] details,
			@AuthenticationPrincipal MemberDetails memberDetails) {
		// DB 저장 실패시에는 이미지를 서버에 저장하면 안됨 => DB에 저장 성공을 확인한 후 서버에 저장
		log.info("registerPOST()");
		productVO.setMemberId(memberDetails.getUsername());
		log.info(productVO);
		log.info("파일 명 : " + thumbnail.getOriginalFilename());
	
		ImageVO imgThumbnail = null;
		List<ImageVO> imgDetails = new ArrayList<>();
		int res = 0;
		
		// 모든 파일을 imageVO로 변환
		if (!thumbnail.isEmpty()) { // 파일이 있는 경우
			imgThumbnail = FileAnalyzerUtil.toImageVO(thumbnail, thumbnailUploadPath);
		}
		if(!details[0].isEmpty()) {
			for (MultipartFile file : details) {
				imgDetails.add(FileAnalyzerUtil.toImageVO(file, uploadPath));
			}
		}
		
		try {
			res = productService.registerProduct(productVO, imgThumbnail, imgDetails);
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
			res = 0;
		}
		
		String resultMsg = (res == 1) ? "상품이 등록되었습니다. 관리자의 승인 후 판매 가능합니다." : "등록 실패";
		
	    AlertVO alertVO = new AlertVO();
	    alertVO.setAlertMsg(resultMsg);
	    alertVO.setRedirectUri("product/myProduct");
	    model.addAttribute("alertVO", alertVO);
	    return Constant.ALERT_PATH;
	} // end registerPOST

	@GetMapping("search")
	public void search(Model model, Pagination pagination) {
		log.info("search category : " + pagination.getCategory() + ", word : " + pagination.getWord());
		List<ProductPreviewDTO> productList;
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		
//		
		productList = productService.search(pageMaker);
//		
		pageMaker.update();
		awsS3Service.toImageUrl(productList);
		
		model.addAttribute("productList", productList);
		model.addAttribute("pageMaker", pageMaker);
		//model.addAttribute("category", category); // 검색결과 내에서 페이지 이동을 구현하기 위해, 기존 검색 조건 return
		//model.addAttribute("word", word);
		
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
	

	// 상품 정보 수정
	@PreAuthorize("#productVO.memberId == authentication.principal.username")
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
			newThumbnail = FileAnalyzerUtil.toImageVO(thumbnail, thumbnailUploadPath);
			oldThumbnail = productService.getProductThumbnail(productVO.getImgId());
		}
		if (!details[0].isEmpty()) {
			oldDetails = productService.getProductDetails(productVO.getProductId());
			for (MultipartFile detail : details) {
				newDetails.add(FileAnalyzerUtil.toImageVO(detail, uploadPath));
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
		
		productDetails.setThumbnailUrl(
				awsS3Service.toImageUrl(productDetails.getThumbnail().getImgPath(), productDetails.getThumbnail().getImgChangeName())
				);
		List<ImageVO> list = productDetails.getDetails();
		productDetails.setDetailsUrl(new ArrayList<>());
		for(ImageVO image : list) {
			productDetails.getDetailsUrl().add(awsS3Service.toImageUrl(image.getImgPath(), image.getImgChangeName()));
		}
		model.addAttribute("productDetails", productDetails);
	} // end popupProductDetailsGET

	// 상품 정보 수정 팝업으로 이동
	@GetMapping("/popupUpdate")
	public void popupUpdateGET(Model model, int productId) {
		log.info("상품 정보 수정 팝업 요청 " + productId);

		ProductDetailsDTO productDetails = productService.getDetails(productId);
		log.info("상세 정보 검색 결과 : " + productDetails);

		productDetails.setThumbnailUrl(
				awsS3Service.toImageUrl(productDetails.getThumbnail().getImgPath(), productDetails.getThumbnail().getImgChangeName())
				);
		List<ImageVO> list = productDetails.getDetails();
		productDetails.setDetailsUrl(new ArrayList<>());
		for(ImageVO image : list) {
			productDetails.getDetailsUrl().add(awsS3Service.toImageUrl(image.getImgPath(), image.getImgChangeName()));
		}
		
		model.addAttribute("productDetailsDTO", productDetails);
	} // end popupProductUpdateGET
	
	// 상품 상태 변경
	// 상품 등록한 사람 또는 관리자만 변경 가능
	@PreAuthorize("#productVO.memberId == authentication.principal.username || hasRole('관리자')")
	@PutMapping("/changeState")
	@ResponseBody
	public ResponseEntity<Integer> updateProductState(@RequestBody ProductVO productVO) {
		log.info("상품 상태 변경 : " + productVO.getProductId() + ", " + productVO.getProductState());
		int res = productService.setProductState(productVO.getProductState(), productVO.getProductId());
		log.info(res + "행 수정 성공");
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateProductState

	// 상품 즉시 삭제
	@DeleteMapping("/delete")
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
	
	// 상품 등록한 사람만 요청 가능
	@PreAuthorize("#productVO.memberId == authentication.principal.username")
	// 상품 삭제 요청 등록 (삭제 가능한 상태면 삭제)
	@DeleteMapping("/request")
	@ResponseBody
	public ResponseEntity<Integer> deleteRequestProduct(@RequestBody ProductVO productVO) {
		int productId = productVO.getProductId();
		log.info("상품 삭제 요청 : " + productId);
		String productState = productService.selectStateByProductId(productId);
		
		int res = 0;
		if (productState.equals(Constant.STATE_SELL)) {
			res = productService.setProductState(Constant.STATE_REMOVE_WAIT, productId) > 0 ? 201 : 200;
		} else if (!productState.equals(Constant.STATE_REMOVE_WAIT)) {
			res = productService.deleteProduct(productId) > 0 ? 101 : 100;
		}
		// res 코드 100 = 삭제 실패, 101 = 삭제 성공, 201 = 삭제요청 성공, 200 = 삭제요청 실패
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end deleteRequestProduct
	
	
	// 상품 등록 요청 조회
	@GetMapping("/registerRequest")
	@ResponseBody
	public ResponseEntity<PagingListDTO<ProductPreviewDTO>> getRegisterRequest(Pagination pagination) {
		log.info("모든 상품 등록 요청 조회");
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		List<ProductPreviewDTO> list = productService.searchByState(Constant.STATE_APPROVAL_WAIT, pageMaker);
		log.info(list);

		awsS3Service.toImageUrl(list);
		pageMaker.update();

		PagingListDTO<ProductPreviewDTO> pagingList = new PagingListDTO<>();
		pagingList.setList(list);
		pagingList.setPageMaker(pageMaker);

		return new ResponseEntity<PagingListDTO<ProductPreviewDTO>>(pagingList, HttpStatus.OK);
	} // end getWaitProduct

	// 상품 삭제 요청 조회
	@GetMapping("/deleteRequest")
	@ResponseBody
	public ResponseEntity<PagingListDTO<ProductPreviewDTO>> getdeleteRequest(Pagination pagination) {
		log.info("상품 삭제 요청 조회");
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		List<ProductPreviewDTO> list = productService.searchByState(Constant.STATE_REMOVE_WAIT, pageMaker);
		log.info(list);

		awsS3Service.toImageUrl(list);
		pageMaker.update();
		
		PagingListDTO<ProductPreviewDTO> pagingList = new PagingListDTO<>();
		pagingList.setList(list);
		pagingList.setPageMaker(pageMaker);

		return new ResponseEntity<PagingListDTO<ProductPreviewDTO>>(pagingList, HttpStatus.OK);
	} // end getWaitProduct
	
} 
	
	

