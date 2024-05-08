package com.web.vop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
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

import com.web.vop.domain.ProductVO;
import com.web.vop.domain.SellerVO;
import com.web.vop.service.ProductService;
import com.web.vop.service.SellerService;
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
	SellerService sellerService;
	
	@GetMapping("sellerRequest")
	public void sellerRequestGET() {
		log.info("�Ǹ��� ���� ��û �������� �̵�");
	} // end sellerRequestGET
	
	@GetMapping("registerProduct")
	public String registerProductGET() {
		log.info("��ǰ ��� �������� �̵�");
		return "redirect:../product/register";
	} // end registerProductGET
	
	@GetMapping("listProduct")
	public void listProductGET(Model model, String memberId, Pagination pagination) {
		log.info("��ǰ ��ȸ ������ �̵� by " + memberId);
		
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		pageMaker.setTotalCount(productService.getCntByMemberId(memberId));
		List<ProductVO> productList = productService.selectByMemberId(memberId, pageMaker.getPagination());
		
		if(productList != null) {
			log.info(productList.size() + "�� ������ �˻� ����");
		}
		model.addAttribute("pageMaker", pageMaker);
		model.addAttribute("productList", productList);
	} // end listProductGET
	
	@GetMapping("updateProduct")
	public void updateProductGET(Model model, String productId) {
		log.info("��ǰ ���� ������ ��û"); // �� �˻� ��� �ʿ�
		
	} // end updateProductGET
	
	
	// �Ǹ��� ���� ��û ��ȸ
	@GetMapping("/all/{page}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getAllRequest(@PathVariable("page") int page){
		log.info("��� ���ѿ�û ��ȸ");
		PageMaker pageMaker = new PageMaker();
		pageMaker.getPagination().setPageNum(page);
		List<SellerVO> list = sellerService.getAllRequest(pageMaker.getPagination());
		log.info(list);
		int requestCount = sellerService.getRequestCount();
		pageMaker.setPageCount(requestCount);
			
		Map<String, Object> resultMap = new HashMap<>(); // ��ȯ�� Ÿ���� 2���̹Ƿ� pageMaker�� list�� ���� �� ����
		resultMap.put("pageMaker", pageMaker);
		resultMap.put("list", list);
			
		return new ResponseEntity<Map<String,Object>>(resultMap, HttpStatus.OK);
	} // end getAllRequest
	
	// �ڽ��� ���� ��û ��ȸ
	@GetMapping("/my/{memberId}")
	@ResponseBody
	public ResponseEntity<SellerVO> getMyRequest(@PathVariable("memberId") String memberId){
		log.info("�� ���ѿ�û ��ȸ");
		SellerVO result = sellerService.getMyRequest(memberId);
			
		return new ResponseEntity<SellerVO>(result, HttpStatus.OK);
	} // end getMyRequest
		
		
	// ��û ���
	@PostMapping("/register")
	@ResponseBody
	public ResponseEntity<Integer> registerRequest(@RequestBody SellerVO sellerVO) {
		log.info("��û ��� : " + sellerVO);
		int res = sellerService.registerRequest(sellerVO);

		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end registerRequest
		
	// ��û ����(����)
	@PutMapping("/update")
	@ResponseBody
	public ResponseEntity<Integer> updateRequest(@RequestBody SellerVO sellerVO) {
		log.info("��û ���� : " + sellerVO);
		int res = sellerService.updateMemberContent(sellerVO);

		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateRequest

	// ��û ����(������)
	@PutMapping("/refuse")
	@ResponseBody
	public ResponseEntity<Integer> refuseRequest(@RequestBody SellerVO sellerVO) {
		log.info("��û ���� : " + sellerVO.getMemberId());
		int res = sellerService.refuseRequest(sellerVO.getMemberId(), sellerVO.getRefuseMsg());

		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end refuseRequest
		
	// ��û ����
	@DeleteMapping("/delete/{memberId}")
	@ResponseBody
	public ResponseEntity<Integer> deleteRequest(@PathVariable("memberId") String memberId) {
		log.info("��û ���� : " + memberId);
		int res = sellerService.deleteRequest(memberId);

		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end deleteRequest

	// ��ǰ ���� ��û
	@PostMapping("/delReqProduct")
	@ResponseBody
	public ResponseEntity<Integer> deleteRequestProduct(String productId) {
		log.info("��ǰ ���� ��û : " + productId);
		int pid = Integer.parseInt(productId);
		// �Ǹ� ���� ��ǰ�̸� ���� ����� ���·� ��ȯ, (�Ǹ� ��, ���� �����) ���°� �ƴ϶�� ��� ����
		String productState = productService.selectStateByProductId(pid);
		log.info("���� ���� : " + productState);

		int res = 0;
		if (productState.equals("�Ǹ���")) {
			if (productService.setProductState("���� �����", pid) == 1)
				res = 1;
		} else if (!productState.equals("���� �����")) {
			if (productService.deleteProduct(pid) == 1)
				res = 2;
		}

		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end deleteRequestProduct
	
}
