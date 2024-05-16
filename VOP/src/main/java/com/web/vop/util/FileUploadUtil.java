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
    public static void deleteFile(ImageVO imageVO) {
        // 삭제할 파일의 전체 경로 생성
        String fullPath = imageVO.getImgPath() + File.separator + imageVO.getImgChangeName();
        
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
			BufferedImage icon = new BufferedImage(120, 120, BufferedImage.TYPE_3BYTE_BGR); // icon 저장할 공간 생성
			icon.createGraphics().drawImage(bi, 0, 0, 120, 120, null); // icon 그리기
			ImageIO.write(icon, "jpg", thumbnail); // 그린 icon 저장
			log.info("thumbnail 저장 성공");
		} catch (IOException e) {
			log.info("thumbnail 저장 실패");
			e.printStackTrace();
		}
    } // end saveIcon
    
    
    // @param imgPath : 파일 업로드 경로
    // @param changedName : 파일명
    // @param extension : 확장자 명
    // @GetMapping(value = "/요청 경로", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	// @ResponseBody
	// public ResponseEntity<Resource> 메소드 이름(){} 로 비동기 메소드 만들고, 이 메소드 실행 결과를 리턴하면 됩니다.
    // 클라이언트 측은 <img src="요청 경로?검색 대상 정보"> 로 비동기 요청 보내면 됩니다.
    public static ResponseEntity<Resource> getFile(String imgPath, String changedName, String extension) { // 저장된 파일 불러오기
    	String fullPath = imgPath + File.separator + changedName;
    	Resource resource = new FileSystemResource(fullPath);
        // 다운로드할 파일 이름을 헤더에 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" 
              + fullPath + "." + extension);
        log.info("이미지 완료");
        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
    } // end getFile
    

}
