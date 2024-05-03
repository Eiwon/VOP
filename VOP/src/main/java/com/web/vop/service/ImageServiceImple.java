package com.web.vop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.vop.domain.ImageVO;
import com.web.vop.persistence.ImageMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ImageServiceImple implements ImageService{
	
	@Autowired
	private ImageMapper imageMapper;
	
	// 이미지 상세 정보 검색
	@Override
	public ImageVO getImageById(int imgId) {
		log.info("getImageById()");
		ImageVO result = imageMapper.selectByImgId(imgId);
		log.info("이미지 검색 : " + result.toString()); //우선 로그 만들었습니다.
		return result;
	}// end getImageById()
	
}
