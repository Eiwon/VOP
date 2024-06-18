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
	
	// ���ƿ� or �Ⱦ�� ���
	@PostMapping("/register") 
	public ResponseEntity<Integer> createLikesPOST(
			@RequestBody LikesVO likesVO) {
		log.info("createLikesDislikesPOST()");

		int res = likesService.createLikes(likesVO);
		
		// result ���� �����Ͽ� �����ϴ� ������� �����ϸ� 200 OK�� �����մϴ�.
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	// ���ƿ� or �Ⱦ�� ����
	@PutMapping("/modify")
	   public ResponseEntity<Integer> updateLikes(
			   @RequestBody LikesVO likesVO){
		log.info("updateLikesDislikes()");
		int res = likesService.updateLikes(likesVO);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	// ���ƿ� or �Ⱦ�� ����
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
