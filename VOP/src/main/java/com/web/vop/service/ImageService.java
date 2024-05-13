package com.web.vop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.web.vop.domain.ImageVO;

@Service
public interface ImageService {
	
	// �̹��� �˻� (��ǰ ����)
	ImageVO getImageById(int imgId);
	
	// �̹��� ���
	int registerImage(ImageVO imageVO);
	
	// �ֱٿ� ����� �̹��� id �˻�   
	int getRecentImgId();
		
	// productId�� �̹��� �˻�
	List<ImageVO> getByProductId(int productId);
	
	// imgId�� �̹��� ��� ��ȸ
	String getImgPathByImgId(int imgId);
}
