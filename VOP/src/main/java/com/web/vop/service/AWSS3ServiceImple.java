package com.web.vop.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.migrationhubstrategyrecommendations.model.S3Object;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.web.vop.domain.ImageVO;
import com.web.vop.persistence.Constant;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class AWSS3ServiceImple implements AWSS3Service {

	@Autowired
	private AmazonS3 awsS3Client;
	
	@Autowired
	private ImageService imageService;
	
	private String bucketName = "vop-s3-bucket";
	
	@Override
	public String uploadImage(MultipartFile file, ImageVO imageVO) throws IOException {
		log.info("s3�� �̹��� ����");
		
		String imgUrl = uploadResizedImage(file, imageVO, 300, 300);
		return imgUrl;
	} // end uploadImage

	@Override
	public String uploadIcon(MultipartFile file, ImageVO imageVO) throws IOException {
		log.info("s3�� ����Ϸ� ����");
		
		String imgUrl = uploadResizedImage(file, imageVO, 120, 120);
		return imgUrl;
	} //end uploadIcon

	@Override
	public String uploadResizedImage(MultipartFile file, ImageVO imageVO, int width, int height) throws IOException {
		log.info("s3�� �̹��� ũ�⸦ �缳���Ͽ� ����");
		
		BufferedImage bi = ImageIO.read(file.getInputStream()); // ����� ������ �о�´�
		BufferedImage icon = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR); // icon ������ ���� ����
		icon.createGraphics().drawImage(bi, 0, 0, width, height, null); // icon �׸���
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(icon, "jpg", baos); // �׸� icon ����
		
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
		ImageVO imageVO = imageService.getImageById(imgId);
		
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

	

}