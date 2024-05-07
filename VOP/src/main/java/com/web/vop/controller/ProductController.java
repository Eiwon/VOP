package com.web.vop.controller;

import java.util.UUID;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.web.vop.domain.ImageVO;
import com.web.vop.domain.ProductVO;
import com.web.vop.service.ImageService;
import com.web.vop.service.ProductService;
import com.web.vop.util.FileUploadUtil;
import com.web.vop.util.PageMaker;
import com.web.vop.util.Pagination;

import com.web.vop.domain.ReviewVO;
import com.web.vop.service.ReviewService;

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
	
	// ReviewService 클래스에 있는 기능을 사용하기위해 생성
	private ReviewService reviewService;
	
	// 상품 상세 정보 조회
	@GetMapping("/detail")
	public void productDetailGET(Model model, Integer productId) {
		log.info("productDetailGET()");
		log.info("productId : " + productId);
		
		// productId에 해당하는 상품 조회 
		ProductVO productVO = productService.getProductById(productId);
		log.info("/product/detail get");
		model.addAttribute("productVO", productVO);
	} // end productDetail()
	
//	// 첨부 파일 이미지 상세 정보 조회(GET)
//    //@GetMapping("/product/detail")
//    public void detailGET(int productId, Model model) {
//        log.info("detailGET()");
//        log.info("productId : " + productId);
//        
//        // imgId로 상세 정보 조회
//        ProductVO ProductVO = productService.selectByMainImg(productId);
//        
//        log.info("/product/detail get");
//        // 조회된 상세 정보를 Model에 추가하여 전달
//        model.addAttribute("ProductVO", ProductVO);
//    } // end detail()
    
	// 댓글 총 갯수 조회
	// @GetMapping("/detail") -- 경로 똑같이 하면 에러난다고 바로 위에서 말했는데...
	public void reviewCountGET(Model model, Integer productId) {
		log.info("reviewCountGET()");
		int reviewCount = productService.selectReviewByCount(productId);
		log.info("reviewCount : " + reviewCount);
		model.addAttribute("reviewCount", reviewCount);
	}
	
	// 상품 리뷰(별) 총 합 검색
	public void reviewStarGET(Model model, Integer productId) {
		log.info("reviewStarGET()");
		int res = productService.selectReviewByStar(productId);
		int reviewCount = productService.selectReviewByCount(productId);
		float reviewStar = res / reviewCount;
		model.addAttribute("reviewStar", reviewStar);
	}
	
	 // 댓글 전체 조회
	 @GetMapping("/all/{productId}") // GET : 댓글(리뷰) 선택(all)
	 public ResponseEntity<List<ReviewVO>> readAllReview(
	 		@PathVariable("productId") int productId){
	 	log.info("readAllReview()");
	 		
	 	// productId 확인 로그
	 	log.info("productId = " + productId);
	 		
	 	// productId에 해당하는 댓글(리뷰) list을 전체 검색
	 	List<ReviewVO> list = reviewService.getAllReview(productId);
	 		
	 	// list값을 전송하고 리턴하는 방식으로 성공하면 200 ok를 갔습니다.
	 	return new ResponseEntity<List<ReviewVO>>(list, HttpStatus.OK);
	 }
	
	
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
	    
	    productVO.setImgPath(thumbnailUploadPath);
	    productVO.setImgRealName(FileUploadUtil.subStrName(thumbnail.getOriginalFilename()));
	    productVO.setImgChangeName(thumbnailName);
	    productVO.setImgExtension(FileUploadUtil.subStrExtension(thumbnail.getOriginalFilename()));
	    
	    int res = productService.registerProduct(productVO);
	    log.info("product " + res + "행 추가 성공");
	    int productId = productService.getRecentProductId(); 
	    log.info("추가된 상품 id : " + productId);
	    
	    
	    // details 이미지들 저장 후 IMAGE 테이블에 추가
	    String[] detailsNames = new String[details.length];
	    
	    for(int i = 0; i < details.length; i++) {
	    	detailsNames[i] = UUID.randomUUID().toString();
	    	FileUploadUtil.saveFile(uploadPath, details[i], detailsNames[i]);
	    	ImageVO imageVO = new ImageVO(
	    			0, productId, uploadPath, FileUploadUtil.subStrName(details[i].getOriginalFilename()),
	    			detailsNames[i], FileUploadUtil.subStrExtension(details[i].getOriginalFilename()), null
	    			); 
	    	int imgRes = imageService.registerImage(imageVO);
	    	log.info(imgRes + "행 추가 성공");
	    }
	    
	    return "redirect:../seller/sellerRequest";
	} // end registerPOST
	
	@GetMapping("search")
	public void search(Model model, String category, String word, Pagination pagination) {
		log.info("search category : " + category + ", word : " + word);
		
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		
		if(word.length() > 0) {
			
		}
		
		
		if(category.equals("전체")) { // 카테고리가 전체, 검색어가 있는 경우
			productService.selectByName(word, pagination);
		}else {
			if(word.length() > 0) { // 카테고리가 있고, 검색어도 있는 경우
				productService.selectByNameInCategory(category, word, pagination);
			}else { // 카테고리가 있고, 검색어는 없는 경우
				productService.selectByCategory(category, pagination);
			}
		}
		// 카테고리가 전체, 검색어도 없는 경우 -- 검색 실행 X
		
	} // end search
	
  
	
}
