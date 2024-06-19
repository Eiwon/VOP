package com.web.vop.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.vop.domain.LikesVO;
import com.web.vop.domain.ReviewVO;
import com.web.vop.service.LikesService;
import com.web.vop.socket.AlarmHandler;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/likes")
@Log4j
public class LikesRESTController {
	@Autowired
	private LikesService likesService;
	
	// 좋아요 or 싫어요 등록
	@PostMapping("/register") 
	public ResponseEntity<Integer> createLikesPOST(
			@RequestBody LikesVO likesVO) {
		log.info("createLikesDislikesPOST()");

		int res = likesService.createLikes(likesVO);
		
		// result 값을 전송하여 리턴하는 방식으로 성공하면 200 OK를 전송합니다.
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	// 좋아요 or 싫어요 수정
	@PutMapping("/modify")
	   public ResponseEntity<Integer> updateLikes(
			   @RequestBody LikesVO likesVO){
		log.info("updateLikesDislikes()");
		int res = likesService.updateLikes(likesVO);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	// 좋아요 or 싫어요 삭제
	 @DeleteMapping("/delete")
	 public ResponseEntity<Integer> deleteLikes(
			 @RequestBody LikesVO likesVO){
		 log.info("deleteReview()");
		 
		 int reviewId = likesVO.getReviewId();
		 String memberId = likesVO.getMemberId();
		 
		 int res = likesService.deleteLikes(reviewId, memberId);
		 return new ResponseEntity<>(res, HttpStatus.OK);
	 }
	
}
