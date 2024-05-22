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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.vop.domain.ImageVO;
import com.web.vop.service.ImageService;
import com.web.vop.util.FileUploadUtil;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/image")
@Log4j
public class ImageController {
	
	@Autowired
	ImageService imageService;
	
	// 이미지 파일 요청
	@GetMapping(value = "/show", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity<Resource> showImg(int imgId){
		log.info("showImg() : " + imgId);
		ImageVO imageVO = imageService.getImageById(imgId);
		if(imageVO == null) {
			return new ResponseEntity<Resource>(null, null, HttpStatus.OK);
		}
		
		String fullPath = imageVO.getImgPath() + File.separator + imageVO.getImgChangeName();
			
		HttpHeaders headers = new HttpHeaders();
		// 다운로드할 파일 이름을 헤더에 설정
		headers.add(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=" + fullPath + "." + imageVO.getImgExtension());

		Resource resource = FileUploadUtil.getFile(fullPath, imageVO.getImgExtension());
	       
	    return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	} // end showImg
	
	
	
}
