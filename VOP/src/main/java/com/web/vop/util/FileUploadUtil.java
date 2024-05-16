package com.web.vop.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.web.vop.domain.ImageVO;

import lombok.extern.log4j.Log4j;

@Log4j
public class FileUploadUtil {
	 
    /**
     * ���� �̸����� Ȯ���ڸ� ������ ���� ���� �̸��� ����
     * 
     * @param fileName ���� �̸�
     * @return ���� ���� �̸�
     */
    public static String subStrName(String fileName) {
       // FilenameUtils.normalize() : ���� �̸� ����ȭ �޼���
        String normalizeName = FilenameUtils.normalize(fileName);
        log.info("fileName : " + fileName + ", normalizeName : " + normalizeName);
        int dotIndex = normalizeName.lastIndexOf('.');

        String realName = normalizeName.substring(0, dotIndex);
        return realName;
    }
    
    /**
     * ���� �̸����� Ȯ���ڸ� ����
     * 
     * @param fileName ���� �̸�
     * @return Ȯ����
     */
    public static String subStrExtension(String fileName) {
        // ���� �̸����� ������ '.'�� �ε����� ã���ϴ�.
        int dotIndex = fileName.lastIndexOf('.');

        // '.' ������ ���ڿ��� Ȯ���ڷ� �����մϴ�.
        String extension = fileName.substring(dotIndex + 1);

        return extension;
    }
    
    
    /**
     * ������ ����
     * 
     * @param uploadPath ���� ���ε� ���
     * @param file ���ε�� ����
     * @param uuid UUID
     */
    public static void saveFile(String uploadPath, MultipartFile file, String uuid) {
        
        File realUploadPath = new File(uploadPath);
        if (!realUploadPath.exists()) {
            realUploadPath.mkdirs();
            log.info(realUploadPath.getPath() + " successfully created.");
        } else {
            log.info(realUploadPath.getPath() + " already exists.");
        }
        
        File saveFile = new File(realUploadPath, uuid);
        try {
            file.transferTo(saveFile);
            log.info("file upload success");
        } catch (IllegalStateException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        } 
        
    } // end saveFile
    
    /**
     * ������ ����
     * 
     * @param uploadPath ���� ���ε� ���
     * @param path ������ ����� ��¥ ���
     * @param chgName ����� ���� �̸�
     */
    public static void deleteFile(ImageVO imageVO) {
        // ������ ������ ��ü ��� ����
        String fullPath = imageVO.getImgPath() + File.separator + imageVO.getImgChangeName();
        
        // ���� ��ü ����
        File file = new File(fullPath);
        
        // ������ �����ϴ��� Ȯ���ϰ� ����
        if(file.exists()) {
            if(file.delete()) {
                System.out.println(fullPath + " file delete success.");
            } else {
                System.out.println(fullPath + " file delete failed.");
            }
        } else {
            System.out.println(fullPath + " file not found.");
        }
    }
    
    
    // file�� ���������� ��ȯ�Ͽ� ����
    public static void saveIcon(String uploadPath, MultipartFile file, String uuid) {
        
    	File realUploadPath = new File(uploadPath);
        if (!realUploadPath.exists()) {
            realUploadPath.mkdirs();
            log.info(realUploadPath.getPath() + " successfully created.");
        } else {
            log.info(realUploadPath.getPath() + " already exists.");
        }
        
        File thumbnail = new File(realUploadPath, uuid);
        
        try {
			BufferedImage bi = ImageIO.read(file.getInputStream()); // ����� ������ �о�´�
			BufferedImage icon = new BufferedImage(120, 120, BufferedImage.TYPE_3BYTE_BGR); // icon ������ ���� ����
			icon.createGraphics().drawImage(bi, 0, 0, 120, 120, null); // icon �׸���
			ImageIO.write(icon, "jpg", thumbnail); // �׸� icon ����
			log.info("thumbnail ���� ����");
		} catch (IOException e) {
			log.info("thumbnail ���� ����");
			e.printStackTrace();
		}
    } // end saveIcon
    
    
    // @param imgPath : ���� ���ε� ���
    // @param changedName : ���ϸ�
    // @param extension : Ȯ���� ��
    // @GetMapping(value = "/��û ���", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	// @ResponseBody
	// public ResponseEntity<Resource> �޼ҵ� �̸�(){} �� �񵿱� �޼ҵ� �����, �� �޼ҵ� ���� ����� �����ϸ� �˴ϴ�.
    // Ŭ���̾�Ʈ ���� <img src="��û ���?�˻� ��� ����"> �� �񵿱� ��û ������ �˴ϴ�.
    public static ResponseEntity<Resource> getFile(String imgPath, String changedName, String extension) { // ����� ���� �ҷ�����
    	String fullPath = imgPath + File.separator + changedName;
    	Resource resource = new FileSystemResource(fullPath);
        // �ٿ�ε��� ���� �̸��� ����� ����
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" 
              + fullPath + "." + extension);
        log.info("�̹��� �Ϸ�");
        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
    } // end getFile
    

}
