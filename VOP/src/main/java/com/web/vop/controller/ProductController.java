package com.web.vop.controller;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import javax.swing.ImageIcon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	@GetMapping("/detail")
	public void productDetail(Model model, Integer productId) {
		log.info("productDetail()");
		
		// productId에 해당하는 상품 조회 
		ProductVO productVO = productService.getProductById(productId);
		log.info("/product/detail get");
		model.addAttribute("productVO", productVO);
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
