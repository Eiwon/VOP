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
		
		// ��� �� ���� ��ȸ
//		int reviewCount = productService.selectReviewByCount(productId);
//		log.info("reviewCount" + reviewCount);
		
		// ���� ��� �� �ڵ�
		int res = 0; // ��� �Է½� �Ҽ��� �Է� �Ұ�
		String reviewStar = "0";
		if(productVO.getReviewNum() != 0) { //0 ������ �� ������ ������ ���Ϳ´�.
			res = productService.selectReviewByStar(productId);
			log.info("����(��) : " + res);
			// ���� ��� ��
			reviewStar = df.format((float)res / productVO.getReviewNum());
		}
		log.info("res : " + res);
		log.info("reviewStar : " + reviewStar);
		
		ImageVO imageVO = imageService.getImageById(productVO.getImgId());
		
		List<ImageVO> imageList = imageService.getByProductId(productId);
		for(ImageVO image  : imageList) {
			log.info(image);
		}
		
		// ��� ���� ����
//		model.addAttribute("reviewCount", reviewCount);
		// ��ǰ ��ȸ ����
		model.addAttribute("productVO", productVO);
		// ���� ��� ����
		model.addAttribute("reviewStar", reviewStar);
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

	@GetMapping("/myProduct")
	public void myProductGET() {
		log.info("�ڽ��� ����� ��ǰ ��� ������ �̵�");
	} // end myProductGET
	
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
	
	// �ش� ������ ����� ��ǰ �˻�
	@GetMapping("/myList")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> productList(Pagination pagination, HttpServletRequest request) {
		String memberId = (String) request.getSession().getAttribute("memberId");
		log.info(memberId + "�� ����� ��ǰ �˻�");

		Map<String, Object> resultMap = new HashMap<>();
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);

		List<ProductVO> productList = productService.searchByMemberId(memberId, pageMaker);

		if (productList != null) {
			log.info(productList.size() + "�� ������ �˻� ����");
		}

		resultMap.put("productList", productList);
		resultMap.put("pageMaker", pageMaker);

		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	} // end productList
	
	// ����� �̹��� ���� ��û
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
		// �ٿ�ε��� ���� �̸��� ����� ����
		headers.add(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=" + fullPath + "." + imageVO.getImgExtension());

		Resource resource = FileUploadUtil.getFile(fullPath, imageVO.getImgExtension());
       
        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
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

	// ��ǰ ���� ����
	@PostMapping("/update")
	public String updateProduct(ProductVO productVO, MultipartFile thumbnail, MultipartFile[] details) {
		log.info("----------��ǰ ����---------------------");
		log.info("��ǰ ���� : " + productVO + ", ����� ���� : " + !thumbnail.isEmpty());
		ImageVO thumbnailVO = null;
		List<ImageVO> detailsList = new ArrayList<>();

		// DB ����
		// ������ �̹����� �ִٸ�, DB�� �����ϱ� ���� VO�� ��ȯ
		if (!thumbnail.isEmpty()) {
			thumbnailVO = FileUploadUtil.toImageVO(thumbnail, thumbnailUploadPath);
		}
		if (!details[0].isEmpty()) {
			for (MultipartFile detail : details) {
				detailsList.add(FileUploadUtil.toImageVO(detail, uploadPath));
			}
		}

		// ������ ������ service�� ���� (transaction �ʿ�)
		int res = productService.updateProduct(productVO, thumbnailVO, detailsList);

		if (res == 1) { // ���� ������ ������ ���� ����
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

	
	// ��ǰ ������ �˾����� �̵�
	@GetMapping("/popupDetails")
	public void popupDetailsGET(Model model, int productId) {
		log.info("��ǰ �� ���� �˾� ��û " + productId);

		ProductDetailsDTO productDetails = productService.getDetails(productId);
		log.info("�� ���� �˻� ��� : " + productDetails);

		model.addAttribute("productDetails", productDetails);
	} // end popupProductDetailsGET

	// ��ǰ ���� ���� �˾����� �̵�
	@GetMapping("/popupUpdate")
	public void popupUpdateGET(Model model, int productId) {
		log.info("��ǰ ���� ���� �˾� ��û " + productId);

		ProductDetailsDTO productDetails = productService.getDetails(productId);
		log.info("�� ���� �˻� ��� : " + productDetails);

		try { // �ڹٽ�ũ��Ʈ���� ����ϱ� ���� JSON���� ��ȯ �� ����
			model.addAttribute("productDetails", new ObjectMapper().writeValueAsString(productDetails));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	} // end popupProductUpdateGET
	
	// ��ǰ ���� ����
	@PutMapping("/changeState")
	@ResponseBody
	public ResponseEntity<Integer> updateProductState(@RequestBody ProductVO productVO) {
		log.info("��ǰ ���� ���� : " + productVO.getProductId());
		int res = productService.setProductState(productVO.getProductState(), productVO.getProductId());
		log.info(res + "�� ���� ����");
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateProductState

	// ��ǰ ��� ����
	@DeleteMapping("/product")
	@ResponseBody
	public ResponseEntity<Integer> deleteProduct(@RequestBody ProductVO productVO) {
		log.info("��ǰ ���� : " + productVO.getProductId());
		int res = delete(productVO.getProductId());
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateProductState
	
	// ��ǰ ���� ��û ��� (���� ������ ���¸� ����)
	@DeleteMapping("/request")
	@ResponseBody
	public ResponseEntity<Integer> deleteRequestProduct(@RequestBody int productId) {
		log.info("��ǰ ���� ��û : " + productId);
		String productState = productService.selectStateByProductId(productId);
		
		int res = 0;
		if (productState.equals("�Ǹ���")) {
			res = productService.setProductState("���� �����", productId);
		} else if (!productState.equals("���� �����")) {
			res = delete(productId);
		}
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end deleteRequestProduct
	
	
	// ��ǰ ��� ��û ��ȸ
	@GetMapping("/registerRequest")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getRegisterRequest(Pagination pagination) {
		log.info("��� ��ǰ ��� ��û ��ȸ");
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		List<ProductVO> list = productService.searchByState("���� �����", pageMaker);
		log.info(list);

		pageMaker.update();
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("pageMaker", pageMaker);
		resultMap.put("list", list);

		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	} // end getWaitProduct

	// ��ǰ ���� ��û ��ȸ
	@GetMapping("/deleteRequest")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getdeleteRequest(Pagination pagination) {
		log.info("��ǰ ���� ��û ��ȸ");
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		List<ProductVO> list = productService.searchByState("���� �����", pageMaker);
		log.info(list);

		pageMaker.update();
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("pageMaker", pageMaker);
		resultMap.put("list", list);

		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	} // end getWaitProduct
	
	
	private int delete(int productId) {
		List<ImageVO> imageList = productService.deleteProduct(productId);
		
		if(imageList.size() > 0) { // ������ ����� �̹��� ����
			log.info("���� �̹��� : " + imageList.size() + "��");
			for(ImageVO image : imageList) {
				FileUploadUtil.deleteFile(image);
			}
		}
		return imageList.size();
	} // end delete
} 
	
	

