package com.web.vop.persistence;

import org.apache.ibatis.annotations.Mapper;

import com.web.vop.domain.ImageVO;

@Mapper
public interface ImageMapper {
	
	// 상품 메인 이미지 검색 
	ImageVO selectByImgId(int imgId);
}
