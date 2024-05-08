package com.web.vop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.vop.domain.ProductVO;
import com.web.vop.service.ProductService;
import com.web.vop.util.PageMaker;
import com.web.vop.util.Pagination;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/seller")
@Log4j
public class SellerController {

	@Autowired
	private ProductService productService;
	
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
	
}
