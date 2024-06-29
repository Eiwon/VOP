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
	// ProductService Ŭ������ �ִ� ����� ����ϱ����� ����
	private ProductService productService;
	
	@Autowired
	private AWSS3Service awsS3Service;
	
	@Autowired
	private String thumbnailUploadPath;
	
	@Autowired
	private String uploadPath;
	
	//@Autowired
	//private WebSocketHandler alarmHandler;
	
	// ��ǰ �� ���� ��ȸ
	@GetMapping("/detail")
	public void productDetailGET(Model model, Integer productId) {
		log.info("productDetailGET()");
		
		// ��ǰ, �̹��� ���̺� ���� �˻� �Ϻ� �÷� ���� �ؼ� ����
		ProductDetailsDTO productDetails = productService.getDetails(productId);
		log.info("�� ���� �˻� ��� : " + productDetails);
		
		// ����� �̹����� ��ο� ����� �̸��� �̿��Ͽ� AWS S3�� URL�� �����ϰ� �����մϴ�.
		productDetails.setThumbnailUrl(
				awsS3Service.toImageUrl(productDetails.getThumbnail().getImgPath(), productDetails.getThumbnail().getImgChangeName())
				);
		// �� �̹��� ����Ʈ�� �����ɴϴ�.
		List<ImageVO> list = productDetails.getDetails();
		
		// �� �̹��� URL�� ������ ����Ʈ�� �ʱ�ȭ�մϴ�.
		productDetails.setDetailsUrl(new ArrayList<>());
		
		// �� �̹��� ����Ʈ�� ��ȸ�ϸ鼭 �� �̹����� ��ο� ����� �̸��� �̿��Ͽ� AWS S3�� URL�� �����ϰ� �߰��մϴ�.
		for(ImageVO image : list) {
			productDetails.getDetailsUrl().add(awsS3Service.toImageUrl(image.getImgPath(), image.getImgChangeName()));
		}
		
		// model ��ü�� productDetails�� �߰��Ͽ� �信�� ����� �� �ֵ��� �մϴ�.
		model.addAttribute("productDetails", productDetails);
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
	public String registerPOST(Model model,
			ProductVO productVO,  MultipartFile thumbnail, MultipartFile[] details,
			@AuthenticationPrincipal MemberDetails memberDetails) {
		// DB ���� ���нÿ��� �̹����� ������ �����ϸ� �ȵ� => DB�� ���� ������ Ȯ���� �� ������ ����
		log.info("registerPOST()");
		productVO.setMemberId(memberDetails.getUsername());
		log.info(productVO);
		log.info("���� �� : " + thumbnail.getOriginalFilename());
	
		ImageVO imgThumbnail = null;
		List<ImageVO> imgDetails = new ArrayList<>();
		int res = 0;
		
		// ��� ������ imageVO�� ��ȯ
		if (!thumbnail.isEmpty()) { // ������ �ִ� ���
			imgThumbnail = FileAnalyzerUtil.toImageVO(thumbnail, thumbnailUploadPath);
		}
		if(!details[0].isEmpty()) {
			for (MultipartFile file : details) {
				imgDetails.add(FileAnalyzerUtil.toImageVO(file, uploadPath));
			}
		}
		
		try {
			res = productService.registerProduct(productVO, imgThumbnail, imgDetails);
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
			res = 0;
		}
		
		String resultMsg = (res == 1) ? "��ǰ�� ��ϵǾ����ϴ�. �������� ���� �� �Ǹ� �����մϴ�." : "��� ����";
		
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
		//model.addAttribute("category", category); // �˻���� ������ ������ �̵��� �����ϱ� ����, ���� �˻� ���� return
		//model.addAttribute("word", word);
		
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
	

	// ��ǰ ���� ����
	@PreAuthorize("#productVO.memberId == authentication.principal.username")
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
			newThumbnail = FileAnalyzerUtil.toImageVO(thumbnail, thumbnailUploadPath);
			oldThumbnail = productService.getProductThumbnail(productVO.getImgId());
		}
		if (!details[0].isEmpty()) {
			oldDetails = productService.getProductDetails(productVO.getProductId());
			for (MultipartFile detail : details) {
				newDetails.add(FileAnalyzerUtil.toImageVO(detail, uploadPath));
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

	// ��ǰ ���� ���� �˾����� �̵�
	@GetMapping("/popupUpdate")
	public void popupUpdateGET(Model model, int productId) {
		log.info("��ǰ ���� ���� �˾� ��û " + productId);

		ProductDetailsDTO productDetails = productService.getDetails(productId);
		log.info("�� ���� �˻� ��� : " + productDetails);

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
	
	// ��ǰ ���� ����
	// ��ǰ ����� ��� �Ǵ� �����ڸ� ���� ����
	@PreAuthorize("#productVO.memberId == authentication.principal.username || hasRole('������')")
	@PutMapping("/changeState")
	@ResponseBody
	public ResponseEntity<Integer> updateProductState(@RequestBody ProductVO productVO) {
		log.info("��ǰ ���� ���� : " + productVO.getProductId() + ", " + productVO.getProductState());
		int res = productService.setProductState(productVO.getProductState(), productVO.getProductId());
		log.info(res + "�� ���� ����");
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateProductState

	// ��ǰ ��� ����
	@DeleteMapping("/delete")
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
	
	// ��ǰ ����� ����� ��û ����
	@PreAuthorize("#productVO.memberId == authentication.principal.username")
	// ��ǰ ���� ��û ��� (���� ������ ���¸� ����)
	@DeleteMapping("/request")
	@ResponseBody
	public ResponseEntity<Integer> deleteRequestProduct(@RequestBody ProductVO productVO) {
		int productId = productVO.getProductId();
		log.info("��ǰ ���� ��û : " + productId);
		String productState = productService.selectStateByProductId(productId);
		
		int res = 0;
		if (productState.equals(Constant.STATE_SELL)) {
			res = productService.setProductState(Constant.STATE_REMOVE_WAIT, productId) > 0 ? 201 : 200;
		} else if (!productState.equals(Constant.STATE_REMOVE_WAIT)) {
			res = productService.deleteProduct(productId) > 0 ? 101 : 100;
		}
		// res �ڵ� 100 = ���� ����, 101 = ���� ����, 201 = ������û ����, 200 = ������û ����
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end deleteRequestProduct
	
	
	// ��ǰ ��� ��û ��ȸ
	@GetMapping("/registerRequest")
	@ResponseBody
	public ResponseEntity<PagingListDTO<ProductPreviewDTO>> getRegisterRequest(Pagination pagination) {
		log.info("��� ��ǰ ��� ��û ��ȸ");
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

	// ��ǰ ���� ��û ��ȸ
	@GetMapping("/deleteRequest")
	@ResponseBody
	public ResponseEntity<PagingListDTO<ProductPreviewDTO>> getdeleteRequest(Pagination pagination) {
		log.info("��ǰ ���� ��û ��ȸ");
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
	
	

