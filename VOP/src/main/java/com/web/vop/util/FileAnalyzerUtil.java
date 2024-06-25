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
     * 파일 이름에서 확장자를 제외한 실제 파일 이름을 추출
     * 
     * @param fileName 파일 이름
     * @return 실제 파일 이름
     */
    public static String subStrName(String fileName) {
       // FilenameUtils.normalize() : 파일 이름 정규화 메서드
        String normalizeName = FilenameUtils.normalize(fileName);
        log.info("fileName : " + fileName + ", normalizeName : " + normalizeName);
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
     * 파일을 삭제
     * 
     * @param uploadPath 파일 업로드 경로
     * @param path 파일이 저장된 날짜 경로
     * @param chgName 저장된 파일 이름
     */
//    public static void deleteFile(ImageVO imageVO) {
//        // 삭제할 파일의 전체 경로 생성
//        String fullPath = imageVO.getImgPath() + File.separator + imageVO.getImgChangeName();
//        
//        // 파일 객체 생성
//        File file = new File(fullPath);
//        
//        // 파일이 존재하는지 확인하고 삭제
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
    
    
//    // file을 아이콘으로 변환하여 저장
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
//			BufferedImage bi = ImageIO.read(file.getInputStream()); // 저장된 파일을 읽어온다
//			BufferedImage icon = new BufferedImage(120, 120, BufferedImage.TYPE_3BYTE_BGR); // icon 저장할 공간 생성
//			icon.createGraphics().drawImage(bi, 0, 0, 120, 120, null); // icon 그리기
//			ImageIO.write(icon, "jpg", thumbnail); // 그린 icon 저장
//			log.info("thumbnail 저장 성공");
//		} catch (IOException e) {
//			log.info("thumbnail 저장 실패");
//			e.printStackTrace();
//		}
//    } // end saveIcon
    
    public static ImageVO toImageVO(MultipartFile file, String uploadPath) {
    	String imgChangedName = UUID.randomUUID().toString();
    	String imgRealName = file.getOriginalFilename();
    	return new ImageVO(0, 0, uploadPath, subStrName(imgRealName), imgChangedName, subStrExtension(imgRealName));
    } // end toImageVO
 
//    public static Resource getFile(String fullPath, String extension) { // 저장된 파일 불러오기
//    	Resource resource = new FileSystemResource(fullPath);
// 
//        log.info("이미지 불러오기 완료");
//        return resource;
//    } // end getFile
    

}
