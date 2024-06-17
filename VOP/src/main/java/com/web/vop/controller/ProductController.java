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
	// ProductService Ŭ������ �ִ� ����� ����ϱ����� ����
	private ProductService productService;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private AWSS3Service awsS3Service;
	
	@Autowired
	private String thumbnailUploadPath;
	
	@Autowired
	private String uploadPath;
	
	private static final String[] categoryList = {"�����м�", "�����м�", "���� ���� �Ƿ�", "���Ƶ� �м�", "��Ƽ", "���/���Ƶ�", 
 			"��ǰ", "�ֹ��ǰ", "��Ȱ��ǰ", "Ȩ���׸���", "����������", "������/����", "�ڵ��� ��ǰ", "����/����/DVD", 
 			"�ϱ�/���", "����/���ǽ�", "�ݷ�������ǰ", "�ｺ/�ǰ���ǰ"};
	
	// ��ǰ �� ���� ��ȸ
	@GetMapping("/detail")
	public void productDetailGET(Model model, Integer productId) {
		log.info("productDetailGET()");
		
		log.info("productId : " + productId);
		// productId�� �ش��ϴ� ��ǰ ��ȸ 
		ProductVO productVO = productService.getProductById(productId);	
		
//		// �̹��� �ڵ� ��ȸ
//		ImageVO imageVO = imageService.getImageById(productVO.getImgId());
		
		// �� �̹��� ��ȸ
		List<ImageVO> imageList = imageService.getByProductId(productId);
		for(ImageVO image  : imageList) {
			log.info(image);
		}
		
		// ��ǰ ��ȸ ����
		model.addAttribute("productVO", productVO);
		// �̹��� ��ȸ ����
//		model.addAttribute("imageVO", imageVO);
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
	public String registerPOST(
			ProductVO productVO,  MultipartFile thumbnail, MultipartFile[] details,
			@AuthenticationPrincipal MemberDetails memberDetails) {
		// DB ���� ���нÿ��� �̹����� ������ �����ϸ� �ȵ� => DB�� ���� ������ Ȯ���� �� ������ ����
		log.info("registerPOST()");
		productVO.setMemberId(memberDetails.getUsername());
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
		
		try {
			int res = productService.registerProduct(productVO, imgThumbnail, imgDetails);
			if(res == 1) { // DB ��� ������ S3 ������ �̹��� ����
		    	if(imgThumbnail != null) { // ����� ���
					awsS3Service.uploadIcon(thumbnail, imgThumbnail);
			    }
		    	if (!details[0].isEmpty()) { // �������� �̹��� ���
			    	for(int i = 0; i < imgDetails.size(); i++) {
		    		awsS3Service.uploadImage(details[i], imgDetails.get(i));	    			
		    		}
			    }
		    }
			log.info("��ǰ ��� ��� : " + res);
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
			category = "��ü";
		}
		if(word == null) {
			word = "";
		}
		
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
		awsS3Service.toImageUrl(productList);
		model.addAttribute("productList", productList);
		model.addAttribute("pageMaker", pageMaker);
		model.addAttribute("category", category); // �˻���� ������ ������ �̵��� �����ϱ� ����, ���� �˻� ���� return
		model.addAttribute("word", word);
		
	} // end search
	
	// �ش� ������ ����� ��ǰ �˻�
	@GetMapping("/myList")
	@ResponseBody
	public ResponseEntity<PagingListDTO<ProductPreviewDTO>> productList(
			Pagination pagination, @AuthenticationPrincipal MemberDetails memberDetails) {
		String memberId = memberDetails.getUsername();
		log.info(memberId + "�� ����� ��ǰ �˻�");

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
	
//	// ����� �̹��� ���� ��û
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
//		// �ٿ�ε��� ���� �̸��� ����� ����
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
		log.info("�� ī�װ��� �ְ� ���� ��ǰ 5�� ��û");
		List<ProductPreviewDTO> list = productService.getTopProductByCategory();
		awsS3Service.toImageUrl(list);
		
		// key : ī�װ� �̸�, value : ��ǰ ����Ʈ ���·� ��ȯ
		Map<String, List<ProductPreviewDTO>> resultMap = new HashMap<>();
		String category;
		for(ProductPreviewDTO product : list) {
			category = product.getProductVO().getCategory();
			if(!resultMap.containsKey(category)) { // �ʿ� �ش� ī�װ��� ���ٸ� ���� ����
				resultMap.put(category, new LinkedList<>());
			}
			resultMap.get(category).add(product);
		}
		return new ResponseEntity<Map<String, List<ProductPreviewDTO>>>(resultMap, HttpStatus.OK);
	} // end getBestProductInCategory
	
	@GetMapping("/recent")
	@ResponseBody
	public ResponseEntity<List<ProductPreviewDTO>> getRecent5(){
		log.info("�ֱ� ��ϵ� ��ǰ 5�� ��û");
		
		List<ProductPreviewDTO> list = productService.getRecent5();
		log.info("�˻� ��� : " + list);
		
		awsS3Service.toImageUrl(list);
		
		return new ResponseEntity<List<ProductPreviewDTO>>(list, HttpStatus.OK);
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
		
		ImageVO newThumbnail = null;
		ImageVO oldThumbnail = null;
		List<ImageVO> newDetails = new ArrayList<>();
		List<ImageVO> oldDetails = new ArrayList<>();
		
		// ������ �̹����� �ִٸ� DB�� �����ϱ� ���� VO�� ��ȯ, ���� �̹����� S3 �������� �����ϱ� ���� �̹��� ������ �ҷ���
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
		
		// ������ ������ service�� ����
		try {
			int res = productService.updateProduct(productVO, newThumbnail, newDetails);
			if (res == 1) { // ���� ������ ������ ���� ����
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
		
		// ������ ��ǰ�� img ���� �ҷ�����
		List<ImageVO> imgList = productService.getAllProductImg(productVO.getProductId());
		
		// ��ǰ ����
		int res = productService.deleteProduct(productVO.getProductId());
		
		// �������� ��� ���� �̹��� ����
		if(res == 1) {
			for(ImageVO image : imgList) {
			awsS3Service.removeImage(image);
			}
		}
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateProductState
	
	// ��ǰ ���� ��û ��� (���� ������ ���¸� ����)
	@DeleteMapping("/request")
	@ResponseBody
	public ResponseEntity<Integer> deleteRequestProduct(@RequestBody int productId) {
		log.info("��ǰ ���� ��û : " + productId);
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
	
	
	// ��ǰ ��� ��û ��ȸ
	@GetMapping("/registerRequest")
	@ResponseBody
	public ResponseEntity<PagingListDTO<ProductVO>> getRegisterRequest(Pagination pagination) {
		log.info("��� ��ǰ ��� ��û ��ȸ");
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

	// ��ǰ ���� ��û ��ȸ
	@GetMapping("/deleteRequest")
	@ResponseBody
	public ResponseEntity<PagingListDTO<ProductVO>> getdeleteRequest(Pagination pagination) {
		log.info("��ǰ ���� ��û ��ȸ");
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
	
	

