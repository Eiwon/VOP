package com.web.vop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.vop.domain.ProductVO;
import com.web.vop.service.ProductService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/product")
@Log4j
public class ProducController {
	
	@Autowired
	// ProductService Ŭ������ �ִ� ����� ����ϱ����� ����
	private ProductService productService;
	
	@GetMapping("/detail")
	public void productDetail(Model model, Integer productId) {
		log.info("productDetail()");
		
		// productId�� �ش��ϴ� ��ǰ ��ȸ 
		ProductVO productVO = productService.getProductById(productId);
		log.info("/product/detail get");
		model.addAttribute("productVO", productVO);
	} // end productDetail()
	
	
	
}
