package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.vop.domain.ImageVO;

@Mapper
public interface ImageMapper {
	
	// 상품 메인 이미지 검색 
	ImageVO selectByImgId(int imgId);
	
	// 이미지 등록
	int insertImg(ImageVO imageVO);
	
	// 최근에 등록한 이미지 id 검색   
	int selectRecentImgId();
	
	// productId로 이미지 검색
	List<ImageVO> selectByProductId(int productId);
	
}
