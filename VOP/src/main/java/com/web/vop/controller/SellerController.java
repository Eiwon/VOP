package com.web.vop.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.vop.domain.ImageVO;
import com.web.vop.domain.MemberVO;
import com.web.vop.domain.ProductDetailsDTO;
import com.web.vop.domain.ProductVO;
import com.web.vop.domain.SellerVO;
import com.web.vop.service.ImageService;
import com.web.vop.service.MemberService;
import com.web.vop.service.ProductService;
import com.web.vop.service.SellerService;
import com.web.vop.util.FileUploadUtil;
import com.web.vop.util.PageMaker;
import com.web.vop.util.Pagination;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/seller")
@Log4j
public class SellerController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private SellerService sellerService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private String thumbnailUploadPath;
	
	@Autowired
	private String uploadPath;
	
	@GetMapping("sellerRequest")
	public void sellerRequestGET() {
		log.info("�Ǹ��� ���� ��û �������� �̵�");
	} // end sellerRequestGET
	
	@GetMapping("registerProduct")
	public String registerProductGET() {
		log.info("��ǰ ��� �������� �̵�");
		return "redirect:../product/register";
	} // end registerProductGET
	
	@GetMapping("/admin")
	public void adminGET() {
		log.info("������ �������� �̵�");
	} // end myInfoGet
	
	@GetMapping("/myProduct")
	public void listProductGET() {
		log.info("��ǰ ��ȸ ������ �̵�");
	} // end listProductGET
	
	// �ش� ������ ����� ��ǰ �˻�
	@GetMapping("/productList")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> productList(Pagination pagination, HttpServletRequest request){
		String memberId = (String)request.getSession().getAttribute("memberId");
		log.info(memberId + "�� ����� ��ǰ �˻�");
		
		Map<String, Object> resultMap = new HashMap<>();
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		
		List<ProductVO> productList = productService.searchByMemberId(memberId, pageMaker);
		
		if(productList != null) {
			log.info(productList.size() + "�� ������ �˻� ����");
		}
		
		resultMap.put("productList", productList);
		resultMap.put("pageMaker", pageMaker);
		
		return new ResponseEntity<Map<String,Object>>(resultMap, HttpStatus.OK);
	} // end productList
	
	// ��ǰ ���� ����
	@PostMapping("/updateProduct")
	public void updateProduct(ProductVO productVO, MultipartFile thumbnail, MultipartFile[] details) {
		log.info("----------��ǰ ����---------------------");
		log.info("��ǰ ���� : " + productVO + ", ����� ���� : " + !thumbnail.isEmpty());
		ImageVO thumbnailVO = null;
		List<ImageVO> detailsList = new ArrayList<>();
		
		// DB ����
		// ������ �̹����� �ִٸ�, DB�� �����ϱ� ���� VO�� ��ȯ
		if(!thumbnail.isEmpty()) {  
			thumbnailVO = FileUploadUtil.toImageVO(thumbnail, thumbnailUploadPath);
		}
		if(!details[0].isEmpty()) {
			for(MultipartFile detail : details) {
				detailsList.add(FileUploadUtil.toImageVO(detail, uploadPath));
			}
		}
		
		// ������ ������ service�� ���� (transaction �ʿ�)
		int res = productService.updateProduct(productVO, thumbnailVO, detailsList);
		
		if(res == 1) { // ���� ������ ������ ���� ����
			if(!thumbnail.isEmpty()) {
				FileUploadUtil.saveIcon(thumbnailUploadPath, thumbnail, thumbnailVO.getImgChangeName());
			}
			if(!details[0].isEmpty()) {
				for(int i = 0; i < details.length; i++) {
					FileUploadUtil.saveFile(uploadPath, details[i], detailsList.get(i).getImgChangeName());
				}
			}
		}
	} // end updateProduct
	

	// �ڽ��� �Ǹ��� ���� ��û ��ȸ
	@GetMapping("/my/{memberId}")
	@ResponseBody
	public ResponseEntity<SellerVO> getMyRequest(@PathVariable("memberId") String memberId){
		log.info("�� ���ѿ�û ��ȸ");
		SellerVO result = sellerService.getMyRequest(memberId);
			
		return new ResponseEntity<SellerVO>(result, HttpStatus.OK);
	} // end getMyRequest
		
		
	// �Ǹ��� ���� ��û ���
	@PostMapping("/registerReq")
	@ResponseBody
	public ResponseEntity<Integer> registerRequest(@RequestBody SellerVO sellerVO) {
		log.info("��û ��� : " + sellerVO);
		int res = sellerService.registerRequest(sellerVO);

		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end registerRequest
		
	// �Ǹ��� ���� ��û ����(����)
	@PutMapping("/updateReq")
	@ResponseBody
	public ResponseEntity<Integer> updateRequest(@RequestBody SellerVO sellerVO) {
		log.info("��û ���� : " + sellerVO);
		int res = sellerService.updateMemberContent(sellerVO);

		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateRequest

	// �Ǹ��� ���� ��û ����/����(������)
	@PutMapping("/approval")
	@ResponseBody
	public ResponseEntity<Integer> approveRequest(@RequestBody SellerVO sellerVO) {
		log.info("��û ���� / ���� : " + sellerVO.getMemberId());
		int res = sellerService.approveRequest(sellerVO);

		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end refuseRequest
		
	// �Ǹ��� ���� ��û ����
	@DeleteMapping("/delete/{memberId}")
	@ResponseBody
	public ResponseEntity<Integer> deleteRequest(@PathVariable("memberId") String memberId) {
		log.info("��û ���� : " + memberId);
		int res = sellerService.deleteRequest(memberId);

		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end deleteRequest

	
	// �Ǹ��� ���� ��û ��ȸ
	@GetMapping("/wait")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getWaitRequest(Pagination pagination) {
		log.info("��� ���� ������� ���ѿ�û ��ȸ");
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		List<SellerVO> list = sellerService.getRequestByState("���� �����", pageMaker.getPagination());
		log.info(list);
		int requestCount = sellerService.getRequestByStateCnt("���� �����");
		pageMaker.setTotalCount(requestCount);
		pageMaker.update();
		
		log.info("pageMaker : " + pageMaker.getEndNum());
		Map<String, Object> resultMap = new HashMap<>(); // ��ȯ�� Ÿ���� 2���̹Ƿ� pageMaker�� list�� ���� �� ����
		resultMap.put("pageMaker", pageMaker);
		resultMap.put("list", list);

		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	} // end getWaitRequest

	// ��ϵ� �Ǹ��� ��ȸ
	@GetMapping("/approved")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getApprovedRequest(Pagination pagination) {
		log.info("��� ���ε� ��û ��ȸ");
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		List<SellerVO> list = sellerService.getRequestByState("����", pageMaker.getPagination());
		log.info(list);
		int requestCount = sellerService.getRequestByStateCnt("����");
		pageMaker.setTotalCount(requestCount);
		pageMaker.update();
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("pageMaker", pageMaker);
		resultMap.put("list", list);

		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	} // end getAllRequest
	
	// ��ǰ ��� ��û ��ȸ
	@GetMapping("/productReq")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getWaitProduct(Pagination pagination){
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
	
	// ��ǰ ���� ��û ��� (���� ������ ���¸� ����)
	@DeleteMapping("/productReq")
	@ResponseBody
	public ResponseEntity<Integer> deleteRequestProduct(@RequestBody int productId) {
		log.info("��ǰ ���� ��û : " + productId);
		int res = sellerService.deleteProductRequest(productId);

		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end deleteRequestProduct
	
	// ��ǰ ���� ��û ��ȸ
	@GetMapping("/productDeleteReq")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getDeleteProductReq(Pagination pagination){
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
	
	// �Ǹ��� ������ �˾����� �̵�
	@GetMapping("/popupSellerDetails")
	public void popupSellerDetailsGET(Model model, String memberId) {
		log.info("�Ǹ��� �� ���� �˾� ��û " + memberId);
		SellerVO sellerVO = sellerService.getMyRequest(memberId);
		MemberVO memberVO = memberService.getMemberInfo(memberId);
		model.addAttribute("sellerVO", sellerVO);
		model.addAttribute("memberVO", memberVO);
	} // end popupSellerReqGET
	
	// ��ǰ ������ �˾����� �̵�
	@GetMapping("/popupProductDetails")
	public void popupProductDetailsGET(Model model, int productId) {
		log.info("��ǰ �� ���� �˾� ��û " + productId);
		
		ProductDetailsDTO productDetails = productService.getDetails(productId);
		productDetails.setImgIdDetails(imageService.getImgId(productId));
		log.info("�� ���� �˻� ��� : " + productDetails);
		
		model.addAttribute("productDetails", productDetails);
	} // end popupProductDetailsGET
	
	// ��ǰ ���� ���� �˾����� �̵�
	@GetMapping("/popupProductUpdate")
	public void popupProductUpdateGET(Model model, int productId) {
		log.info("��ǰ ���� ���� �˾� ��û " + productId);
		
		ProductDetailsDTO productDetails = productService.getDetails(productId);
		productDetails.setImgIdDetails(imageService.getImgId(productId));
		log.info("�� ���� �˻� ��� : " + productDetails);
		
		try {
			model.addAttribute("productDetails", new ObjectMapper().writeValueAsString(productDetails));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
	} // end popupProductUpdateGET
	
	// ��ǰ ���� ����
	@PutMapping("/productState")
	@ResponseBody
	public ResponseEntity<Integer> updateProductState(@RequestBody ProductVO productVO){
		log.info("��ǰ ���� ���� : " + productVO.getProductId());
		int res = productService.setProductState(productVO.getProductState(), productVO.getProductId());
		log.info(res + "�� ���� ����");
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateProductState
	
	// ��ǰ ��� ����
	@DeleteMapping("/product")
	@ResponseBody
	public ResponseEntity<Integer> deleteProduct(@RequestBody ProductVO productVO){
		log.info("��ǰ ���� : " + productVO.getProductId());
		int res = productService.deleteProduct(productVO.getProductId());
		log.info(res + "�� ���� ����");
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateProductState
	
}
