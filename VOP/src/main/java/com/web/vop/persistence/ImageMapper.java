package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.vop.domain.ImageVO;

@Mapper
public interface ImageMapper {
	
	// ��ǰ ���� �̹��� �˻� 
	ImageVO selectByImgId(int imgId);
	
	// �̹��� ���
	int insertImg(ImageVO imageVO);
	
	// �ֱٿ� ����� �̹��� id �˻�   
	int selectRecentImgId();
	
	// productId�� �̹��� �˻�
	List<ImageVO> selectByProductId(int productId);
	
}
