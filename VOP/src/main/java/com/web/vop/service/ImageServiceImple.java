package com.web.vop.service;

import java.util.List;

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
	
	// ������ �̹��� �� ���� �˻�
	@Override
	public ImageVO getImageById(int imgId) {
		log.info("getImageById()");
		log.info("imgId : "+ imgId);
		ImageVO result = imageMapper.selectByImgId(imgId);
		log.info("�̹��� �˻� : " + result.toString()); //�켱 �α� ��������ϴ�.
		return result;
	}// end getImageById()

	@Override
	public int registerImage(ImageVO imageVO) {
		log.info("registerImage() : " + imageVO.getImgRealName());
		int res = imageMapper.insertImg(imageVO);
		return res;
	} // end registerImage

	@Override
	public int getRecentImgId() {
		log.info("getRecentImgId()");
		return imageMapper.selectRecentImgId();
	} // end getRecentImgId
	
	// ���� �̹���(����Ʈ) �˻�
	@Override
	public List<ImageVO> getByProductId(int productId) {
		log.info("getByProductId()");
		return imageMapper.selectByProductId(productId);
	} // end getByProductId
}
