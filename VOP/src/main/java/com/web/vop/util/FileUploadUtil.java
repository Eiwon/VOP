package com.web.vop.util;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;

import javax.swing.ImageIcon;

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
            log.info("file upload scuccess");
        } catch (IllegalStateException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        } 
        
    }
    
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
    
    public ImageIcon convToIcon(File imageFile) { // �̹��� ������ ���������� ��ȯ
		System.out.println("convToIcon()");
		ImageIcon icon = new ImageIcon(imageFile.getPath());
		icon = new ImageIcon(icon.getImage().getScaledInstance(95, 95, 100));
		return icon;
	} // end convToIcon
}
