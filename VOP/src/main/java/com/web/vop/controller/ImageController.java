package com.web.vop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.vop.service.AWSS3Service;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/image")
@Log4j
public class ImageController {
//	
//	
//	@Autowired
//	private AWSS3Service awsS3Service;
//	
//	
//	// 이미지 파일 요청
//	@GetMapping("/{imgId}")
//	@ResponseBody
//	public ResponseEntity<String> showImg(@PathVariable("imgId") int imgId){
//		log.info("showImg() : " + imgId);
//		String imgUrl = "";
//		
//		imgUrl = awsS3Service.getImageUrl(imgId);
//		log.info(imgUrl);
//		
//	    return new ResponseEntity<String>(imgUrl, HttpStatus.OK);
//	} // end showImg

}// end ImageController()
	 
	
	

