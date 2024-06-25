package com.web.vop.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.web.vop.domain.ImageVO;

import lombok.extern.log4j.Log4j;

@Log4j
public class FileAnalyzerUtil {
	 
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
//    public static void saveFile(String uploadPath, MultipartFile file, String uuid) {
//        
//        File realUploadPath = new File(uploadPath);
//        if (!realUploadPath.exists()) {
//            realUploadPath.mkdirs();
//            log.info(realUploadPath.getPath() + " successfully created.");
//        } else {
//            log.info(realUploadPath.getPath() + " already exists.");
//        }
//        
//        File saveFile = new File(realUploadPath, uuid);
//        try {
//            file.transferTo(saveFile);
//            log.info("file upload success");
//        } catch (IllegalStateException e) {
//            log.error(e.getMessage());
//        } catch (IOException e) {
//            log.error(e.getMessage());
//        } 
//        
//    } // end saveFile
    
    /**
     * ������ ����
     * 
     * @param uploadPath ���� ���ε� ���
     * @param path ������ ����� ��¥ ���
     * @param chgName ����� ���� �̸�
     */
//    public static void deleteFile(ImageVO imageVO) {
//        // ������ ������ ��ü ��� ����
//        String fullPath = imageVO.getImgPath() + File.separator + imageVO.getImgChangeName();
//        
//        // ���� ��ü ����
//        File file = new File(fullPath);
//        
//        // ������ �����ϴ��� Ȯ���ϰ� ����
//        if(file.exists()) {
//            if(file.delete()) {
//                System.out.println(fullPath + " file delete success.");
//            } else {
//                System.out.println(fullPath + " file delete failed.");
//            }
//        } else {
//            System.out.println(fullPath + " file not found.");
//        }
//    }
    
    
//    // file�� ���������� ��ȯ�Ͽ� ����
//    public static void saveIcon(String uploadPath, MultipartFile file, String uuid) {
//        
//    	File realUploadPath = new File(uploadPath);
//        if (!realUploadPath.exists()) {
//            realUploadPath.mkdirs();
//            log.info(realUploadPath.getPath() + " successfully created.");
//        } else {
//            log.info(realUploadPath.getPath() + " already exists.");
//        }
//        
//        File thumbnail = new File(realUploadPath, uuid);
//        try {
//			BufferedImage bi = ImageIO.read(file.getInputStream()); // ����� ������ �о�´�
//			BufferedImage icon = new BufferedImage(120, 120, BufferedImage.TYPE_3BYTE_BGR); // icon ������ ���� ����
//			icon.createGraphics().drawImage(bi, 0, 0, 120, 120, null); // icon �׸���
//			ImageIO.write(icon, "jpg", thumbnail); // �׸� icon ����
//			log.info("thumbnail ���� ����");
//		} catch (IOException e) {
//			log.info("thumbnail ���� ����");
//			e.printStackTrace();
//		}
//    } // end saveIcon
    
    public static ImageVO toImageVO(MultipartFile file, String uploadPath) {
    	String imgChangedName = UUID.randomUUID().toString();
    	String imgRealName = file.getOriginalFilename();
    	return new ImageVO(0, 0, uploadPath, subStrName(imgRealName), imgChangedName, subStrExtension(imgRealName));
    } // end toImageVO
 
//    public static Resource getFile(String fullPath, String extension) { // ����� ���� �ҷ�����
//    	Resource resource = new FileSystemResource(fullPath);
// 
//        log.info("�̹��� �ҷ����� �Ϸ�");
//        return resource;
//    } // end getFile
    

}
