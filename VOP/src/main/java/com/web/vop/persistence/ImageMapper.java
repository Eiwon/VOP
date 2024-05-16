package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.ImageVO;
import com.web.vop.domain.ProductVO;

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
	
	// �̹��� ��� ��ȸ
	String selectImgPathByImgId(int imgId);
	
	// productId�� �̹��� id �˻�
	List<Integer> selectImgIdByProductId(int productId);
	
	// ��ǰ�� ���õ� ��� �̹��� �˻�
	List<ImageVO> selectAllbyProductId(int productId);
	
	// ��ǰ�� ���õ� ��� �̹��� ����
	int deleteProductImage(int productId);
}
