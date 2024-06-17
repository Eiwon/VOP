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

import com.web.vop.domain.LikesDislikesVO;
import com.web.vop.domain.ReviewVO;
import com.web.vop.service.LikesDislikesService;
import com.web.vop.socket.AlarmHandler;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/likes")
@Log4j
public class LikesDislikesRESTController {
	@Autowired
	private LikesDislikesService likesDislikesService;
	
	@PostMapping("/register") // POST : 댓글(리뷰) 입력
	public ResponseEntity<Integer> createLikesDislikesPOST(
			@RequestBody LikesDislikesVO likesDislikesVO) {
		log.info("createLikesDislikesPOST()");

		int res = likesDislikesService.createLikesDislikes(likesDislikesVO);
		
		// result 값을 전송하여 리턴하는 방식으로 성공하면 200 OK를 전송합니다.
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	@PutMapping("/modify") // PUT : 댓글(리뷰) 수정 
	   public ResponseEntity<Integer> updateLikesDislikes(
			   @RequestBody LikesDislikesVO likesDislikesVO){
		log.info("updateLikesDislikes()");
		int res = likesDislikesService.updateLikesDislikes(likesDislikesVO);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	 @DeleteMapping("/delete")
	 public ResponseEntity<Integer> deleteReview(
			 @RequestBody LikesDislikesVO likesDislikesVO){
		 log.info("deleteReview()");
		 
		 int reviewId = likesDislikesVO.getReviewId();
		 String memberId = likesDislikesVO.getMemberId();
		 
		 int res = likesDislikesService.deleteLikesDislikes(reviewId, memberId);
		 return new ResponseEntity<>(res, HttpStatus.OK);
	 }
	
}
