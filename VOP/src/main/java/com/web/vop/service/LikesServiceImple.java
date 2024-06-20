package com.web.vop.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.web.vop.domain.LikesVO;
import com.web.vop.persistence.LikesMapper;
import lombok.extern.log4j.Log4j;


@Service
@Log4j
public class LikesServiceImple implements LikesService{
	
	@Autowired
	private LikesMapper likesMapper;
	
	@Override
	public int createLikes(LikesVO likesVO) {
		log.info("createLikesDislikes()");
		log.info("likesVO : " + likesVO);
		int insertRes = likesMapper.insertLikes(likesVO);
		log.info(insertRes + "행 등록");
		return insertRes;
	}
	
	@Override
	public List<LikesVO> getAllLikes(int reviewId, String memberId) {
		return null;
	}

	@Override
	public int updateLikes(LikesVO likesVO) {
		log.info("updateLikesDislikes()");
		int updateRes = likesMapper.updateLikes(likesVO);
		log.info(updateRes + "행 수정");
		return updateRes;
	}

	@Override
	public int deleteLikes(int reviewId, String memberId) {
		log.info("deleteLikesDislikes()");
		int deleteRes = likesMapper.deleteLikes(reviewId, memberId);
		log.info(deleteRes + "행 삭제");
		return deleteRes;
	}

	@Override
	public List<LikesVO> getAllLikesPaging(String memberId, List<String> reviewIds) {
		log.info("getAllLikesPaging()");
		List<LikesVO> list = likesMapper.selectByLikesPaging(memberId, reviewIds);
		log.info("list : " + list);
		return list;
	}
	
}
