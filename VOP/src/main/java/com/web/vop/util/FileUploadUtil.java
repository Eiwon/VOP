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
     * 파일 이름에서 확장자를 제외한 실제 파일 이름을 추출
     * 
     * @param fileName 파일 이름
     * @return 실제 파일 이름
     */
    public static String subStrName(String fileName) {
       // FilenameUtils.normalize() : 파일 이름 정규화 메서드
        String normalizeName = FilenameUtils.normalize(fileName);
        int dotIndex = normalizeName.lastIndexOf('.');

        String realName = normalizeName.substring(0, dotIndex);
        return realName;
    }
    
    /**
     * 파일 이름에서 확장자를 추출
     * 
     * @param fileName 파일 이름
     * @return 확장자
     */
    public static String subStrExtension(String fileName) {
        // 파일 이름에서 마지막 '.'의 인덱스를 찾습니다.
        int dotIndex = fileName.lastIndexOf('.');

        // '.' 이후의 문자열을 확장자로 추출합니다.
        String extension = fileName.substring(dotIndex + 1);

        return extension;
    }
    
    
    /**
     * 파일을 저장
     * 
     * @param uploadPath 파일 업로드 경로
     * @param file 업로드된 파일
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
     * 파일을 삭제
     * 
     * @param uploadPath 파일 업로드 경로
     * @param path 파일이 저장된 날짜 경로
     * @param chgName 저장된 파일 이름
     */
    public static void deleteFile(String uploadPath, String path, String chgName) {
        // 삭제할 파일의 전체 경로 생성
        String fullPath = uploadPath + File.separator + path + File.separator + chgName;
        
        // 파일 객체 생성
        File file = new File(fullPath);
        
        // 파일이 존재하는지 확인하고 삭제
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
    
    
    // file을 아이콘으로 변환하여 저장
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
			BufferedImage bi = ImageIO.read(file.getInputStream()); // 저장된 파일을 읽어온다
			BufferedImage icon = new BufferedImage(95, 95, BufferedImage.TYPE_3BYTE_BGR); // icon 저장할 공간 생성
			icon.createGraphics().drawImage(bi, 0, 0, 95, 95, null); // icon 그리기
			ImageIO.write(icon, "jpg", thumbnail); // 그린 icon 저장
			log.info("thumbnail 저장 성공");
		} catch (IOException e) {
			log.info("thumbnail 저장 실패");
			e.printStackTrace();
		}
    } // end saveIcon
    
    public static File getFile(String imgPath, String changedName, String extension) { // 저장된 파일 불러오기
    	String fullPath = imgPath + File.pathSeparator + changedName + File.pathSeparator + extension;
    	File file = new File(fullPath);
    	return file;
    } // end getFile
    
}
