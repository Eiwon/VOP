package com.web.vop.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.web.vop.domain.ImageVO;
import com.web.vop.domain.ProductPreviewDTO;
import com.web.vop.persistence.ImageMapper;
import com.web.vop.util.Constant;

import lombok.extern.log4j.Log4j;


@Service
@Log4j
public class AWSS3ServiceImple implements AWSS3Service {

	@Autowired
	private AmazonS3 awsS3Client;
	
	@Autowired
	private ImageMapper imageMapper;
	
	private String bucketName = "vop-s3-bucket";
	
	@Autowired
	private String cloudFrontUrl;
	
	@Override
	public String uploadImage(MultipartFile file, ImageVO imageVO) throws IOException {
		log.info("s3�� �̹��� ����");
		
		String imgUrl = uploadResizedImage(file, imageVO, 300, 300);
		return imgUrl;
	} // end uploadImage

	@Override
	public String uploadIcon(MultipartFile file, ImageVO imageVO) throws IOException {
		log.info("s3�� ����Ϸ� ����");
		
		String imgUrl = uploadResizedImage(file, imageVO, 140, 140);
		return imgUrl;
	} //end uploadIcon

	@Override
	public String uploadResizedImage(MultipartFile file, ImageVO imageVO, int width, int height) throws IOException {
		log.info("s3�� �̹��� ũ�⸦ �缳���Ͽ� ����");
		BufferedImage icon = null;
		BufferedImage bi = ImageIO.read(file.getInputStream()); // ����� ������ �о�´�
		if(bi.getWidth() > width && bi.getHeight() > height) {
			icon = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR); // icon ������ ���� ����
			icon.createGraphics().drawImage(bi, 0, 0, width, height, null); // icon �׸���
		}else {
			icon = bi;
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(icon, imageVO.getImgExtension(), baos); // �׸� icon ����
		
		baos.flush();
		MultipartFile changedFile = new MockMultipartFile(imageVO.getImgChangeName(), baos.toByteArray());
			
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(changedFile.getInputStream().available());
        String fullPath = imageVO.getImgPath() + imageVO.getImgChangeName();
		awsS3Client.putObject(new PutObjectRequest(bucketName, fullPath, changedFile.getInputStream(), objectMetadata));
		
		String imgUrl = awsS3Client.getUrl(bucketName, fullPath).toString();
		
		return imgUrl;
	} // end uploadResizedImage
	
	@Override
	public String getImageUrl(int imgId) {
		log.info("�̹��� �ҷ�����");
		ImageVO imageVO = imageMapper.selectByImgId(imgId);
		
		String fullPath = 
				(imageVO == null) ? Constant.DEFAULT_IMG_PATH : imageVO.getImgPath() + imageVO.getImgChangeName();
		
		return awsS3Client.getUrl(bucketName, fullPath).toString();
	} // end getImageUrl

	@Override
	public int removeImage(ImageVO imageVO) {
		log.info("s3���� �̹��� ����");
		String fullPath = imageVO.getImgPath() + imageVO.getImgChangeName();
		
		awsS3Client.deleteObject(bucketName, fullPath);
		
		return 1;
	} // end removeImage

	@Override
	public String toImageUrl(String imgPath, String imgChangeName) {
		String fullPath = 
				(imgChangeName == null) ? Constant.DEFAULT_IMG_PATH : imgPath + imgChangeName;
		
		return cloudFrontUrl + fullPath;
		//return awsS3Client.getUrl(bucketName, fullPath).toString();
	} // end toImageUrl

	@Override
	public void toImageUrl(List<ProductPreviewDTO> list) {
		String fullPath;
		for(ProductPreviewDTO item : list) {
			fullPath = (item.getImgChangeName() == null) ? Constant.DEFAULT_IMG_PATH : item.getImgPath() + item.getImgChangeName();
			item.setImgUrl(cloudFrontUrl + fullPath);
			//item.setImgUrl(awsS3Client.getUrl(bucketName, fullPath).toString());
		}
		
	} // end toImageUrl

	

}