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
		log.info("s3에 이미지 저장");
		String fullPath = imageVO.getImgPath() + imageVO.getImgChangeName();
		
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(file.getInputStream().available());
		
		awsS3Client.putObject(new PutObjectRequest(bucketName, fullPath, file.getInputStream(), objectMetadata));
		String imgUrl = awsS3Client.getUrl(bucketName, fullPath).toString();
		log.info(imgUrl);
		return imgUrl;
	} // end uploadImage

	@Override
	public String uploadIcon(MultipartFile file, ImageVO imageVO) throws IOException {
		log.info("s3에 이미지 크기를 줄여서 저장");
		BufferedImage bi = ImageIO.read(file.getInputStream()); // 저장된 파일을 읽어온다
		BufferedImage icon = new BufferedImage(120, 120, BufferedImage.TYPE_3BYTE_BGR); // icon 저장할 공간 생성
		icon.createGraphics().drawImage(bi, 0, 0, 120, 120, null); // icon 그리기
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(icon, "jpg", baos); // 그린 icon 저장
	
        baos.flush();
		MultipartFile changedFile = new MockMultipartFile(imageVO.getImgChangeName(), baos.toByteArray());
		
		
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(changedFile.getInputStream().available());
        String fullPath = imageVO.getImgPath() + imageVO.getImgChangeName();
		awsS3Client.putObject(new PutObjectRequest(bucketName, fullPath, changedFile.getInputStream(), objectMetadata));
		
		String imgUrl = awsS3Client.getUrl(bucketName, fullPath).toString();
		
		return imgUrl;
	} //end uploadIcon

	@Override
	public String getImageUrl(int imgId) {
		log.info("이미지 불러오기");
		ImageVO imageVO = imageService.getImageById(imgId);
		if(imageVO == null) {
			return null;
		}
		String fullPath = imageVO.getImgPath() + imageVO.getImgChangeName();
		
		return awsS3Client.getUrl(bucketName, fullPath).toString();
	} // end getImageUrl

	@Override
	public int removeImage(ImageVO imageVO) {
		log.info("s3에서 이미지 삭제");
		String fullPath = imageVO.getImgPath() + imageVO.getImgChangeName();
		
		awsS3Client.deleteObject(bucketName, fullPath);
		
		return 1;
	} // end removeImage


	
	
	
	
	

}



