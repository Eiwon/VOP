package com.web.vop.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.vop.domain.ImageVO;
import com.web.vop.service.AWSS3Service;
import com.web.vop.service.ImageService;
import com.web.vop.util.FileUploadUtil;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/image")
@Log4j
public class ImageController {
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private AWSS3Service awsS3Service;
	
	
	// 이미지 파일 요청
	@GetMapping("/{imgId}")
	@ResponseBody
	public ResponseEntity<String> showImg(@PathVariable("imgId") int imgId){
		log.info("showImg() : " + imgId);
		String imgUrl = "";
		
		imgUrl = awsS3Service.getImageUrl(imgId);
		
		
	    return new ResponseEntity<String>(imgUrl, HttpStatus.OK);
	} // end showImg
	
	
	
	
}// end ImageController()
	 
	
	

