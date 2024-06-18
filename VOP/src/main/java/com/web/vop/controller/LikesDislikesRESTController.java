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
	
	@PostMapping("/register") // POST : ���(����) �Է�
	public ResponseEntity<Integer> createLikesDislikesPOST(
			@RequestBody LikesDislikesVO likesDislikesVO) {
		log.info("createLikesDislikesPOST()");

		int res = likesDislikesService.createLikesDislikes(likesDislikesVO);
		
		// result ���� �����Ͽ� �����ϴ� ������� �����ϸ� 200 OK�� �����մϴ�.
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	@PutMapping("/modify") // PUT : ���(����) ���� 
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
