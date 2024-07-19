package com.web.vop.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.web.vop.domain.ImageVO;
import com.web.vop.domain.ProductPreviewDTO;

public interface AWSS3Service {

	public String uploadImage(MultipartFile file, ImageVO imageVO) throws IOException;
	
	public String uploadIcon(MultipartFile file, ImageVO imageVO) throws IOException;
	
	public String uploadResizedImage(MultipartFile file, ImageVO imageVO, int width, int height) throws IOException;
	
	public String getImageUrl(int imgId);
	
	public int removeImage(ImageVO imageVO);
	
	public String toImageUrl(String imgPath, String imgChangeName);
	
	public void toImageUrl(List<ProductPreviewDTO> list);
}
