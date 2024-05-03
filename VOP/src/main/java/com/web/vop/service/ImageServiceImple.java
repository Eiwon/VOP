package com.web.vop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.vop.domain.ImageVO;
import com.web.vop.persistence.ImageMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ImageServiceImple implements ImageService{
	
	@Autowired
	private ImageMapper imageMapper;
	
	// �̹��� �� ���� �˻�
	@Override
	public ImageVO getImageById(int imgId) {
		log.info("getImageById()");
		ImageVO result = imageMapper.selectByImgId(imgId);
		log.info("�̹��� �˻� : " + result.toString()); //�켱 �α� ��������ϴ�.
		return result;
	}// end getImageById()
	
}
