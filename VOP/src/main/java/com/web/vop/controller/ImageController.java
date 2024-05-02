package com.web.vop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.web.vop.domain.ImageVO;
import com.web.vop.service.ImageService;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
public class ImageController {
	
	@Autowired
	private ImageService imageService;
	
	// 첨부 파일 상세 정보 조회(GET)
    @GetMapping("/detail")
    public void detail(int imgId, Model model) {
        log.info("detail()");
        log.info("imgId : " + imgId);
        
        // imgId로 상세 정보 조회
        ImageVO imageVO = imageService.getImageById(imgId);
        
        log.info("/product/detail get");
        // 조회된 상세 정보를 Model에 추가하여 전달
        model.addAttribute("imageVO", imageVO);
    } // end detail()
}
