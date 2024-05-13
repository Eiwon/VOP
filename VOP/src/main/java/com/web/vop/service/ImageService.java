package com.web.vop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.web.vop.domain.ImageVO;

@Service
public interface ImageService {
	
	// 이미지 검색 (상품 메인)
	ImageVO getImageById(int imgId);
	
	// 이미지 등록
	int registerImage(ImageVO imageVO);
	
	// 최근에 등록한 이미지 id 검색   
	int getRecentImgId();
		
	// productId로 이미지 검색
	List<ImageVO> getByProductId(int productId);
	
	// imgId로 이미지 경로 조회
	String getImgPathByImgId(int imgId);
}
