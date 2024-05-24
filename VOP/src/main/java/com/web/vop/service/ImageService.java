package com.web.vop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.web.vop.domain.ImageVO;

public interface ImageService {
	
	// 이미지 검색 (상품 메인)
	ImageVO getImageById(int imgId);
	
	// 이미지 등록
	int registerImage(ImageVO imageVO);
		
	// productId로 이미지 검색
	List<ImageVO> getByProductId(int productId);
	
	// imgId로 이미지 경로 조회
	String getImgPathByImgId(int imgId);
	
	// productId로 이미지 id 검색
	List<Integer> getImgId(int productId);

	// imgId로 이미지 삭제
	int removeById(int imgId);
		
	// productId로 이미지 삭제
	int removeByProductId(int productId);
	
	// imgId로 업데이트
	int updateImgById(ImageVO imageVO);
	
	// 최근 등록한 이미지 검색
	int getRecentImgId();
	
}
