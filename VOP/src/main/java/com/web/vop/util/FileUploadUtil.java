package com.web.vop.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

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
    public static void deleteFile(String uploadPath, String path, String chgName) {
        // ������ ������ ��ü ��� ����
        String fullPath = uploadPath + File.separator + path + File.separator + chgName;
        
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
			BufferedImage icon = new BufferedImage(95, 95, BufferedImage.TYPE_3BYTE_BGR); // icon ������ ���� ����
			icon.createGraphics().drawImage(bi, 0, 0, 95, 95, null); // icon �׸���
			ImageIO.write(icon, "jpg", thumbnail); // �׸� icon ����
			log.info("thumbnail ���� ����");
		} catch (IOException e) {
			log.info("thumbnail ���� ����");
			e.printStackTrace();
		}
    } // end saveIcon
    
    public static File getFile(String imgPath, String changedName, String extension) { // ����� ���� �ҷ�����
    	String fullPath = imgPath + File.pathSeparator + changedName + File.pathSeparator + extension;
    	File file = new File(fullPath);
    	return file;
    } // end getFile
    
}
