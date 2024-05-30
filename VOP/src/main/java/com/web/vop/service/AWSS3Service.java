package com.web.vop.service;

import java.io.IOException;
import java.net.URL;

import org.springframework.web.multipart.MultipartFile;

import com.web.vop.domain.ImageVO;

public interface AWSS3Service {

	public String uploadImage(MultipartFile file, ImageVO imageVO) throws IOException;
	
	public String uploadIcon(MultipartFile file, ImageVO imageVO) throws IOException;
	
	public String uploadResizedImage(MultipartFile file, ImageVO imageVO, int width, int height) throws IOException;
	
	public String getImageUrl(int imgId);
	
	public int removeImage(ImageVO imageVO);
}
