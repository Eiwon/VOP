package com.web.vop.service;

import org.springframework.stereotype.Service;

import com.web.vop.domain.ImageVO;

@Service
public interface ImageService {
	
	// 이미지 검색 (상품 메인)
	ImageVO getImageById(int imgId);
	
	// 이미지 등록
	int registerImage(ImageVO imageVO);
	
}
