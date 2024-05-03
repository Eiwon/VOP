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
	// ProductService 클래스에 있는 기능을 사용하기위해 생성
	private ProductService productService;
	
	@GetMapping("/detail")
	public void productDetail(Model model, Integer productId) {
		log.info("productDetail()");
		
		// productId에 해당하는 상품 조회 
		ProductVO productVO = productService.getProductById(productId);
		log.info("/product/detail get");
		model.addAttribute("productVO", productVO);
	} // end productDetail()
	
	
	
}
