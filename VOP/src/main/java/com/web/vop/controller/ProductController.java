package com.web.vop.controller;

import java.util.UUID;
import java.io.File;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
	// ProductService Ŭ������ �ִ� ����� ����ϱ����� ����
	private ProductService productService;
	
	@Autowired
	private String thumbnailUploadPath;
	
	@Autowired
	private String uploadPath;
	
	@Autowired
	private ImageService imageService;
	
	
	// ��ǰ �� ���� ��ȸ
	@GetMapping("/detail")
	public void productDetailGET(Model model, Integer productId) {
		log.info("productDetailGET()");
		
		// �Ҽ��� ù ° �ڸ������� ���
		DecimalFormat df = new DecimalFormat("#.#");
		
		log.info("productId : " + productId);
		// productId�� �ش��ϴ� ��ǰ ��ȸ 
		ProductVO productVO = productService.getProductById(productId);
		int reviewCount = productService.selectReviewByCount(productId);
		log.info("reviewCount" + reviewCount);
		int res = 0;
		String reviewStar = "0";
		if(reviewCount != 0) {
			res = productService.selectReviewByStar(productId);
			// ���� ��� ��
			reviewStar = df.format((float)res / reviewCount);
		}
		log.info("res : " + res);	
		log.info("reviewStar : " + reviewStar);
		// ��ǰ ��ȸ ����
		model.addAttribute("productVO", productVO);
		// ��� ���� ����
		model.addAttribute("reviewCount", reviewCount);
		// ���� ��� ����
		model.addAttribute("reviewStar", reviewStar);
		// �ش� ���
		log.info("/product/detail get");
	} // end productDetail()
	
//	// ÷�� ���� �̹��� �� ���� ��ȸ(GET)
//    //@GetMapping("/product/detail")
//    public void detailGET(int productId, Model model) {
//        log.info("detailGET()");
//        log.info("productId : " + productId);
//        
//        // imgId�� �� ���� ��ȸ
//        ProductVO ProductVO = productService.selectByMainImg(productId);
//        
//        log.info("/product/detail get");
//        // ��ȸ�� �� ������ Model�� �߰��Ͽ� ����
//        model.addAttribute("ProductVO", ProductVO);
//    } // end detail()
    

	@GetMapping("/register")
	public void registerGET() {
		log.info("registerGET()");
	} // end productRegister()
	
//	@PostMapping("/register")
//	public String registerPOST(ProductVO productVO,  MultipartFile thumbnail, MultipartFile[] details) {
//		log.info("registerPOST()");
//		log.info(productVO);
//		log.info("���� �� : " + thumbnail.getOriginalFilename());
//		
//		// UUID ����
//	    String thumbnailName = UUID.randomUUID().toString();
//	    FileUploadUtil.saveIcon(thumbnailUploadPath, thumbnail, thumbnailName);
//	    
//	    productVO.setImgPath(thumbnailUploadPath);
//	    productVO.setImgRealName(FileUploadUtil.subStrName(thumbnail.getOriginalFilename()));
//	    productVO.setImgChangeName(thumbnailName);
//	    productVO.setImgExtension(FileUploadUtil.subStrExtension(thumbnail.getOriginalFilename()));
//	    
//	    int res = productService.registerProduct(productVO);
//	    log.info("product " + res + "�� �߰� ����");
//	    int productId = productService.getRecentProductId(); 
//	    log.info("�߰��� ��ǰ id : " + productId);
//	    
//	    log.info("details ���� �� : " + details.length);
//	    log.info("details : " + details);
//	    // details �̹����� ���� �� IMAGE ���̺� �߰�
//	    String[] detailsNames = new String[details.length];
//	    
//	    for(int i = 0; i < details.length; i++) {
//	    	detailsNames[i] = UUID.randomUUID().toString();
//	    	FileUploadUtil.saveFile(uploadPath, details[i], detailsNames[i]);
//	    	ImageVO imageVO = new ImageVO(
//	    			0, productId, uploadPath, FileUploadUtil.subStrName(details[i].getOriginalFilename()),
//	    			detailsNames[i], FileUploadUtil.subStrExtension(details[i].getOriginalFilename()), null
//	    			); 
//	    	int imgRes = imageService.registerImage(imageVO);
//	    	log.info(imgRes + "�� �߰� ����");
//	    }
//	    
//	    return "redirect:../seller/sellerRequest";
//	} // end registerPOST
	

	@GetMapping("search")
	public void search(Model model, String category, String word, Pagination pagination) {
		log.info("search category : " + category + ", word : " + word);
		List<ProductVO> productList = new ArrayList<>();
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		
		if(category.equals("��ü")) { // ī�װ��� ��ü, �˻�� �ִ� ���
			log.info("�˻��� �˻�");
			productList = productService.selectByName(word, pagination);
			pageMaker.setTotalCount(productService.selectByNameCnt(word));
		}else {
			if(word.length() > 0) { // ī�װ��� �ְ�, �˻�� �ִ� ���
				log.info("ī�װ� + �˻��� �˻�");
				productList = productService.selectByNameInCategory(category, word, pagination);
				pageMaker.setTotalCount(productService.selectByNameInCategoryCnt(category, word));
			}else { // ī�װ��� �ְ�, �˻���� ���� ���
				log.info("ī�װ� �˻�");
				productList = productService.selectByCategory(category, pagination);
				pageMaker.setTotalCount(productService.selectByCategoryCnt(category));
			}
		}
		// ī�װ��� ��ü, �˻�� ���� ��� -- Ŭ���̾�Ʈ ������ ���� X
		log.info("�˻���� = �� " + pageMaker.getTotalCount() + "�� �˻�");
		model.addAttribute("productList", productList);
		model.addAttribute("pageMaker", pageMaker);
		
	} // end search
	
//	@GetMapping(value = "/showImg", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//	@ResponseBody
//	public ResponseEntity<Resource> showImg(int productId){
//		log.info("showImg() : " + productId);
//		ProductVO productVO = productService.getProductById(productId);
//		
//		String imgPath = productVO.getImgPath() + File.separator + productVO.getImgChangeName();
//		// ���� ���ҽ� ����
//        Resource resource = new FileSystemResource(imgPath);
//        // �ٿ�ε��� ���� �̸��� ����� ����
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" 
//              + imgPath + "." + productVO.getImgExtension());
//        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
//	}
//  
	

}
