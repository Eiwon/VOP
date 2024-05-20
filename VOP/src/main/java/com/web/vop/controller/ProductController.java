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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.web.vop.domain.BasketVO;
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
	
	private static final String[] categoryList = {"�����м�", "�����м�", "���� ���� �Ƿ�", "���Ƶ� �м�", "��Ƽ", "���/���Ƶ�", 
 			"��ǰ", "�ֹ��ǰ", "��Ȱ��ǰ", "Ȩ���׸���", "����������", "������/����", "�ڵ��� ��ǰ", "����/����/DVD", 
 			"�ϱ�/���", "����/���ǽ�", "�ݷ�������ǰ", "�ｺ/�ǰ���ǰ"};
	
	// ��ǰ �� ���� ��ȸ
	@GetMapping("/detail")
	public void productDetailGET(Model model, Integer productId) {
		log.info("productDetailGET()");
		
		// �Ҽ��� ù ° �ڸ������� ���
		DecimalFormat df = new DecimalFormat("#.#");//
		
		log.info("productId : " + productId);
		// productId�� �ش��ϴ� ��ǰ ��ȸ 
		ProductVO productVO = productService.getProductById(productId);	
		
		// �̹��� �ڵ�
		ImageVO imageVO = imageService.getImageById(productVO.getImgId());
		
		List<ImageVO> imageList = imageService.getByProductId(productId);
		for(ImageVO image  : imageList) {
			log.info(image);
		}
		
		// ��ǰ ��ȸ ����
		model.addAttribute("productVO", productVO);
		// �̹��� ��ȸ ����
		model.addAttribute("imageVO", imageVO);
		// ��ǰ ���� �̹��� ��ȸ ����
		model.addAttribute("imageList", imageList);
		
		// �ش� ���
		log.info("/product/detail get");
	} // end productDetail()
	
	
	
	
	@GetMapping("/register")
	public void registerGET() {
		log.info("registerGET()");
	} // end productRegister()

	@PostMapping("/register")
	public String registerPOST(ProductVO productVO,  MultipartFile thumbnail, MultipartFile[] details) {
		// DB ���� ���нÿ��� �̹����� ������ �����ϸ� �ȵ� => DB�� ���� ������ Ȯ���� �� ������ ����
		log.info("registerPOST()");
		log.info(productVO);
		log.info("���� �� : " + thumbnail.getOriginalFilename());
		ImageVO imgThumbnail = null;
		List<ImageVO> imgDetails = new ArrayList<>();
		
		
		// ��� ������ imageVO�� ��ȯ
		if (!thumbnail.isEmpty()) { // ������ �ִ� ���
			imgThumbnail = FileUploadUtil.toImageVO(thumbnail, thumbnailUploadPath);
		}
		if(!details[0].isEmpty()) {
			for (MultipartFile file : details) {
				imgDetails.add(FileUploadUtil.toImageVO(file, uploadPath));
			}
		}
		// DB�� ��ǰ ���� ���
	    int res = productService.registerProduct(productVO, imgThumbnail, imgDetails);
	    log.info("��ǰ ��� ��� : " + res);
	    
	    if(res == 1) { // DB ���� ������ ������ ���� 
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
		
		if(category.equals("��ü")) { // ī�װ��� ��ü, �˻�� �ִ� ���
			log.info("�˻��� �˻�");
			productList = productService.searchByName(word, pageMaker);
		}else {
			if(word.length() > 0) { // ī�װ��� �ְ�, �˻�� �ִ� ���
				log.info("ī�װ� + �˻��� �˻�");
				productList = productService.searchByNameInCategory(category, word, pageMaker);
			}else { // ī�װ��� �ְ�, �˻���� ���� ���
				log.info("ī�װ� �˻�");
				productList = productService.searchByCategory(category, pageMaker);
			}
		}
		// ī�װ��� ��ü, �˻�� ���� ��� -- Ŭ���̾�Ʈ ������ ���� X
		log.info("�˻���� = �� " + pageMaker.getTotalCount() + "�� �˻�");
		model.addAttribute("productList", productList);
		model.addAttribute("pageMaker", pageMaker);
		model.addAttribute("category", category); // �˻���� ������ ������ �̵��� �����ϱ� ����, ���� �˻� ���� return
		model.addAttribute("word", word);
		
	} // end search
	
	// ����� �̹��� ���� ��û
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
		log.info("�� ī�װ��� �ְ� ���� ��ǰ 5�� ��û");
		Map<String, List<ProductVO>> resultMap = new HashMap<>();
		
		for(String category : categoryList) {
			List<ProductVO> list = productService.getTopProductInCategory(category);
			log.info(category + " �˻� ��� : " + list);
			resultMap.put(category, list);
		}
		return new ResponseEntity<Map<String, List<ProductVO>>>(resultMap, HttpStatus.OK);
	} // end getBestProductInCategory
	
	@GetMapping("/recent")
	@ResponseBody
	public ResponseEntity<List<ProductVO>> getRecent5(){
		log.info("�ֱ� ��ϵ� ��ǰ 5�� ��û");
		
		List<ProductVO> list = productService.getRecent5();
		log.info("�˻� ��� : " + list);
		return new ResponseEntity<List<ProductVO>>(list, HttpStatus.OK);
	} // end getRecent5
	
	
	@GetMapping("/searchProduct")
	@ResponseBody
	public ResponseEntity<?> searchProduct(@RequestParam("category") String category, @RequestParam("word") String word) {
        log.info("searchProduct()");
        
     // �˻� ����� JSON ���·� ����
        Map<String, Object> result = new HashMap<>();
        result.put("ī�װ� : ", category);
        result.put("�Է��� �ܾ� : ", word);
        
        // ResponseEntity�� JSON �����͸� ��Ƽ� ��ȯ
        return ResponseEntity.ok(result);
    }// end searchProduct()


} 
	
	

