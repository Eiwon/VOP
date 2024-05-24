package com.web.vop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.web.vop.domain.ImageVO;

public interface ImageService {
	
	// �̹��� �˻� (��ǰ ����)
	ImageVO getImageById(int imgId);
	
	// �̹��� ���
	int registerImage(ImageVO imageVO);
		
	// productId�� �̹��� �˻�
	List<ImageVO> getByProductId(int productId);
	
	// imgId�� �̹��� ��� ��ȸ
	String getImgPathByImgId(int imgId);
	
	// productId�� �̹��� id �˻�
	List<Integer> getImgId(int productId);

	// imgId�� �̹��� ����
	int removeById(int imgId);
		
	// productId�� �̹��� ����
	int removeByProductId(int productId);
	
	// imgId�� ������Ʈ
	int updateImgById(ImageVO imageVO);
	
	// �ֱ� ����� �̹��� �˻�
	int getRecentImgId();
	
}
