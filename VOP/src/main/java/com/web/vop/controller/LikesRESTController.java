package com.web.vop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.vop.domain.LikesVO;
import com.web.vop.service.LikesService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/likes")
@Log4j
public class LikesRESTController {
	@Autowired
	private LikesService likesService;
	
	// 좋아요 or 싫어요 등록
	@PreAuthorize("#likesVO.memberId == authentication.principal.username")
	@PostMapping("/register") 
	public ResponseEntity<Integer> createLikesPOST(
			@RequestBody LikesVO likesVO) {
		log.info("createLikesDislikesPOST()");
		log.info("likesVO : " + likesVO);
		
		int res = likesService.createLikes(likesVO);
		
		// result 값을 전송하여 리턴하는 방식으로 성공하면 200 OK를 전송합니다.
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	}
	
	// 좋아요 or 싫어요 수정
	@PreAuthorize("#likesVO.memberId == authentication.principal.username")
	@PutMapping("/modify")
	   public ResponseEntity<Integer> updateLikes(
			   @RequestBody LikesVO likesVO){
		log.info("updateLikes()");
		log.info("likesVO : " + likesVO);
		int res = likesService.updateLikes(likesVO);
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	}
	
	// 좋아요 or 싫어요 삭제
	 @PreAuthorize("#likesVO.memberId == authentication.principal.username")
	 @DeleteMapping("/delete")
	 public ResponseEntity<Integer> deleteLikes(
			 @RequestBody LikesVO likesVO){
		 log.info("deleteLikes()");
		 log.info("likesVO : " + likesVO);
		 int reviewId = likesVO.getReviewId();
		 String memberId = likesVO.getMemberId();
		 int likesType = likesVO.getLikesType();
		
		 int res = likesService.deleteLikes(reviewId, memberId, likesType);
		 return new ResponseEntity<>(res, HttpStatus.OK);

	 }
	
	@PreAuthorize("#memberId == authentication.principal.username")// 이런식으로 코드 작성 하는지 확인 하기
	@GetMapping("/list/{productId}/{memberId}") // GET : 댓글(문의) 선택(all)  // 나중에 데이터 받는 거에 따라 달라짐
	public ResponseEntity<List<LikesVO>> readAllLikes(
			@PathVariable("productId") int productId,
			@PathVariable("memberId") String memberId){
		 log.info("readAllLikes()");
		 
		 List<LikesVO> listLikes = likesService.getAllLikesPaging(productId, memberId);
		 
		 return new ResponseEntity<List<LikesVO>>(listLikes, HttpStatus.OK);
	 }
	 
	
}
