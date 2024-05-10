package com.web.vop.controller;

import java.util.UUID;



import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.web.vop.domain.ImageVO;
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
		DecimalFormat df = new DecimalFormat("#.#");
		
		log.info("productId : " + productId);
		// productId에 해당하는 상품 조회 
		ProductVO productVO = productService.getProductById(productId);
		// 댓글 총 갯수 조회
		int reviewCount = productService.selectReviewByCount(productId);
		log.info("reviewCount" + reviewCount);
		// 리뷰 평균 값 코드
		int res = 0;
		String reviewStar = "0";
		if(reviewCount != 0) {
			res = productService.selectReviewByStar(productId);
			// 리뷰 평균 값
			reviewStar = df.format((float)res / reviewCount);
		}
		log.info("res : " + res);	
		log.info("reviewStar : " + reviewStar);
		
		ImageVO imageVO = imageService.getImageById(productVO.getImgId());
		
		List<ImageVO> imageList = imageService.getByProductId(productId);
		for(ImageVO image  : imageList) {
			log.info(image);
		}
		
		
		// 상품 조회 정보
		model.addAttribute("productVO", productVO);
		// 댓글 갯수 정보
		model.addAttribute("reviewCount", reviewCount);
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

	@PostMapping("/register")
	public String registerPOST(ProductVO productVO,  MultipartFile thumbnail, MultipartFile[] details) {
		log.info("registerPOST()");
		log.info(productVO);
		log.info("파일 명 : " + thumbnail.getOriginalFilename());
		
		// UUID 생성
	    String thumbnailName = UUID.randomUUID().toString();
	    FileUploadUtil.saveIcon(thumbnailUploadPath, thumbnail, thumbnailName);
	    
	    ImageVO imageVO = new ImageVO(0, 0,
	    		thumbnailUploadPath, FileUploadUtil.subStrName(thumbnail.getOriginalFilename()),
	    		thumbnailName, FileUploadUtil.subStrExtension(thumbnail.getOriginalFilename()));
	    
	    // thumbnail 이미지 등록
	    int res = imageService.registerImage(imageVO);
	    log.info("image " + res + "행 추가 성공");
	    
	    // 등록한 이미지 id 불러오기 
	    int recentImageId = imageService.getRecentImgId();
	    log.info("추가된 이미지 id : " + recentImageId);
	    
	    productVO.setImgId(recentImageId);
	    // 상품 등록
	    res = productService.registerProduct(productVO);
	    log.info("product " + res + "행 추가 성공");
	    
	    // 등록한 상품 id 불러오기
	    int recentProductId = productService.getRecentProductId(); 
	    log.info("추가된 상품 id : " + recentProductId);
	    
	    
	    log.info("details 파일 수 : " + details.length);
	    
	    // details 이미지들 저장 후 IMAGE 테이블에 추가
	    String[] detailsNames = new String[details.length];
	    
	    for(int i = 0; i < details.length; i++) {
	    	detailsNames[i] = UUID.randomUUID().toString();
	    	FileUploadUtil.saveFile(uploadPath, details[i], detailsNames[i]);
	    	// 파일 저장
	    	ImageVO vo = new ImageVO(
	    			0, recentProductId, uploadPath, FileUploadUtil.subStrName(details[i].getOriginalFilename()),
	    			detailsNames[i], FileUploadUtil.subStrExtension(details[i].getOriginalFilename())
	    			); 
	    	res = imageService.registerImage(vo);
	    	log.info(res + "행 추가 성공");
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
			productList = productService.selectByName(word, pagination);
			pageMaker.setTotalCount(productService.selectByNameCnt(word));
		}else {
			if(word.length() > 0) { // 카테고리가 있고, 검색어도 있는 경우
				log.info("카테고리 + 검색어 검색");
				productList = productService.selectByNameInCategory(category, word, pagination);
				pageMaker.setTotalCount(productService.selectByNameInCategoryCnt(category, word));
			}else { // 카테고리가 있고, 검색어는 없는 경우
				log.info("카테고리 검색");
				productList = productService.selectByCategory(category, pagination);
				pageMaker.setTotalCount(productService.selectByCategoryCnt(category));
			}
		}
		// 카테고리가 전체, 검색어도 없는 경우 -- 클라이언트 측에서 실행 X
		log.info("검색결과 = 총 " + pageMaker.getTotalCount() + "개 검색");
		model.addAttribute("productList", productList);
		model.addAttribute("pageMaker", pageMaker);
		model.addAttribute("category", category); // 검색결과 내에서 페이지 이동을 구현하기 위해, 기존 검색 조건 return
		model.addAttribute("word", word);
		
	} // end search
	
	// 썸네일 이미지 파일 요청
	@GetMapping(value = "/showImg", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity<Resource> showImg(int imgId){
		log.info("showImg() : " + imgId);
		ImageVO imageVO = imageService.getImageById(imgId);
		
		if(imageVO == null) return null;
		
		return FileUploadUtil.getFile(imageVO.getImgPath(), imageVO.getImgChangeName(), imageVO.getImgExtension());
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
	
}
