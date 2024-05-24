package com.web.vop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.web.vop.domain.ImageVO;
import com.web.vop.persistence.ImageMapper;
import com.web.vop.util.FileUploadUtil;

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
		log.info("�̹��� �˻� : " + result); //�켱 �α� ��������ϴ�.
		return result;
	}// end getImageById()

	@Override
	public int registerImage(ImageVO imageVO) { // �̹��� ��� �� id ��ȯ
		log.info("registerImage() : " + imageVO.getImgRealName());
		int res = imageMapper.insertImg(imageVO);
		return res;
	} // end registerImage
	
	// ���� �̹���(����Ʈ) �˻�
	@Override
	public List<ImageVO> getByProductId(int productId) {
		log.info("getByProductId()");
		return imageMapper.selectByProductId(productId);
	} // end getByProductId

	@Override
	public String getImgPathByImgId(int imgId) {// imgId�� �̹��� ��� ��ȸ
		log.info("getImgPathByImgId - imgId : " + imgId);
		String result = imageMapper.selectImgPathByImgId(imgId);
		return result;
	}

	@Override
	public List<Integer> getImgId(int productId) {
		log.info("getImgId()");
		return imageMapper.selectImgIdByProductId(productId);
	} // end getImgId

	@Override
	public int removeById(int imgId) { // ������ ����� �̹��� ���� �� DB ���� ����
		log.info("removeById() : " + imgId);
		ImageVO imageVO = imageMapper.selectByImgId(imgId);
		FileUploadUtil.deleteFile(imageVO);
		int res = imageMapper.deleteById(imgId);
		
		return res;
	} // end removeById

	@Override
	public int removeByProductId(int productId) {
		log.info("removeByProductId() : " + productId);
		return imageMapper.deleteByProductId(productId);
	} // end removeByProductId

	@Override
	public int updateImgById(ImageVO imageVO) {
		log.info("updateImgById()");
		return imageMapper.updateById(imageVO);
	} // end setImgById

	@Override
	public int getRecentImgId() {
		return imageMapper.selectRecentImgId();
	}
}
