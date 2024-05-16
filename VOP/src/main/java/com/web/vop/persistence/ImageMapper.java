package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.ImageVO;
import com.web.vop.domain.ProductVO;

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
	
	// 이미지 경로 조회
	String selectImgPathByImgId(int imgId);
	
	// productId로 이미지 id 검색
	List<Integer> selectImgIdByProductId(int productId);
	
	// 상품과 관련된 모든 이미지 검색
	List<ImageVO> selectAllbyProductId(int productId);
	
	// 상품과 관련된 모든 이미지 삭제
	int deleteProductImage(int productId);
}
