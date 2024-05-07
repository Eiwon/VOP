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
	// ProductService Ŭ������ �ִ� ����� ����ϱ����� ����
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
		
		// productId�� �ش��ϴ� ��ǰ ��ȸ 
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
		log.info("���� �� : " + thumbnail.getOriginalFilename());
		
		// UUID ����
	    String thumbnailName = UUID.randomUUID().toString();
	    FileUploadUtil.saveIcon(thumbnailUploadPath, thumbnail, thumbnailName);
	    
	    productVO.setImgPath(thumbnailUploadPath);
	    productVO.setImgRealName(FileUploadUtil.subStrName(thumbnail.getOriginalFilename()));
	    productVO.setImgChangeName(thumbnailName);
	    productVO.setImgExtension(FileUploadUtil.subStrExtension(thumbnail.getOriginalFilename()));
	    
	    int res = productService.registerProduct(productVO);
	    log.info("product " + res + "�� �߰� ����");
	    int productId = productService.getRecentProductId(); 
	    log.info("�߰��� ��ǰ id : " + productId);
	    
	    
	    // details �̹����� ���� �� IMAGE ���̺� �߰�
	    String[] detailsNames = new String[details.length];
	    
	    for(int i = 0; i < details.length; i++) {
	    	detailsNames[i] = UUID.randomUUID().toString();
	    	FileUploadUtil.saveFile(uploadPath, details[i], detailsNames[i]);
	    	ImageVO imageVO = new ImageVO(
	    			0, productId, uploadPath, FileUploadUtil.subStrName(details[i].getOriginalFilename()),
	    			detailsNames[i], FileUploadUtil.subStrExtension(details[i].getOriginalFilename()), null
	    			); 
	    	int imgRes = imageService.registerImage(imageVO);
	    	log.info(imgRes + "�� �߰� ����");
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
		
		
		if(category.equals("��ü")) { // ī�װ��� ��ü, �˻�� �ִ� ���
			productService.selectByName(word, pagination);
		}else {
			if(word.length() > 0) { // ī�װ��� �ְ�, �˻�� �ִ� ���
				productService.selectByNameInCategory(category, word, pagination);
			}else { // ī�װ��� �ְ�, �˻���� ���� ���
				productService.selectByCategory(category, pagination);
			}
		}
		// ī�װ��� ��ü, �˻�� ���� ��� -- �˻� ���� X
		
	} // end search
	
}
