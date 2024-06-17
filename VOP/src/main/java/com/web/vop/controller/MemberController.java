package com.web.vop.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.vop.domain.AlertVO;
import com.web.vop.domain.MemberDetails;
import com.web.vop.domain.MemberVO;
import com.web.vop.service.MailAuthenticationService;
import com.web.vop.service.MemberService;
import com.web.vop.service.UserDetailsServiceImple;
import com.web.vop.util.Constant;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/member")
@Log4j
public class MemberController {

	@Autowired
	MemberService memberService;
	
	@Autowired
	UserDetailsServiceImple UserDetailsService;
	
	@Autowired
	MailAuthenticationService mailAuthService;
	
	@GetMapping("/register")
	public void registerGET() {
		log.info("회원 가입 페이지 요청");
	} // end registerGET
	
	@GetMapping("/login")
	public void loginGET() {
		log.info("login 페이지 이동 요청");
	} // end loginGET
	
	
	@GetMapping("/loginFail")
	public String loginFailGET(Model model) {
		log.info("login 실패");
		AlertVO alertVO = new AlertVO();
		alertVO.setAlertMsg("잘못된 아이디 또는 비밀번호 입니다.");
		alertVO.setRedirectUri("member/login");
		model.addAttribute("alertVO", alertVO);
		return Constant.ALERT_PATH;
	} // end loginFailGET
	
	@GetMapping("/findAccount")
	public void findAccount() {
		log.info("아이디, 비밀번호 찾기 페이지 요청");
	} // end findAccount
	
	@PostMapping("/findAccount")
	public String mailAuthenticationPOST(Model model, String memberEmail, String authCode){
		log.info("아이디 찾기 인증 코드 :" + authCode);
		AlertVO alertVO = new AlertVO();
		String returnPath = null;
		List<String> memberIdList = null;
		boolean res = mailAuthService.verifyAuthCode(memberEmail, authCode);
		
		if(res) {
			memberIdList = memberService.getIdByEmail(memberEmail);
			log.info("검색된 id : " + memberIdList);
			model.addAttribute("memberIdList", memberIdList);
			returnPath = "member/findAccountResult";
		}else {
			alertVO.setAlertMsg("인증코드 불일치");
			alertVO.setRedirectUri("member/findAccount");
			model.addAttribute("alertVO", alertVO);
			returnPath = Constant.ALERT_PATH;
		}
		
		return returnPath;
	} // end mailAuthenticationPOST
	
	@GetMapping("/findPassword")
	public void findPassword() {
		log.info("비밀번호 찾기 페이지 요청");
	} // end findPassword
	
	@PostMapping("/findPassword")
	public String findPasswordPOST(Model model, String memberId) {
		log.info("비밀번호 찾기 요청");
		
		String memberEmail = null;
		// 입력된 아이디가 유효한지 확인
		MemberVO memberVO = memberService.getMemberInfo(memberId);
		if(memberVO != null) {
			// 유효하면 해당 아이디의 email로 인증번호 전송
			memberEmail = memberVO.getMemberEmail();
			mailAuthService.sendAuthEmail(memberEmail);
			
			String secretEmail = ""; // 이메일 일부를 *로 가리기
			secretEmail += memberEmail.charAt(0);
			for(int i = 1; i < memberEmail.indexOf("@") - 1; i++) {
				secretEmail += "*";
			}
			secretEmail += memberEmail.subSequence(memberEmail.indexOf("@") - 1, memberEmail.length());
			
			model.addAttribute("memberId", memberId);
			model.addAttribute("memberEmail", secretEmail);
		}
		
		return "member/findPasswordResult";
	} // end findPassword
	
	@PostMapping("/changePw")
	public String changePw(Model model, String memberId, String memberPw, String authCode) {
		log.info("비밀번호 재설정");
		AlertVO alertVO = new AlertVO();
		String memberEmail = memberService.getEmailById(memberId);
		boolean isCorrect = mailAuthService.verifyAuthCode(memberEmail, authCode);
		
		if(isCorrect) {
			// 인증 코드 일치
			memberService.updatePw(memberId, memberPw);
			alertVO.setAlertMsg("비밀번호 변경 성공!");
			alertVO.setRedirectUri("member/login");
		}else {
			// 인증 코드 불일치
			alertVO.setAlertMsg("인증 코드가 일치하지 않습니다.");
			alertVO.setRedirectUri("member/findPassword");
		}
		model.addAttribute("alertVO", alertVO);
		
		return Constant.ALERT_PATH;
	} // end changePw
	
	@PostMapping("/register") // 회원가입 요청
	public String registerPOST(Model model, MemberVO memberVO) {
		log.info("회원 가입 요청 : " + memberVO);
		AlertVO alertVO = new AlertVO();
		int res = memberService.registerMember(memberVO);
		log.info("회원 가입 결과 : " + res);
		
		if(res == 1) {
			alertVO.setAlertMsg("회원가입 성공!");
			alertVO.setRedirectUri("member/login");
		}else {
			alertVO.setAlertMsg("회원가입 실패");
			alertVO.setRedirectUri("member/register");
		}
		
		model.addAttribute("alertVO", alertVO);
		
		return Constant.ALERT_PATH;
	} // end registerPOST
	
	@GetMapping("/modify")
	public void modifyGET(Model model, @AuthenticationPrincipal MemberDetails memberDetails) {
		log.info("내 정보 수정 페이지 요청");
		String memberId = memberDetails.getUsername();
		model.addAttribute("memberVO", memberService.getMemberInfo(memberId));
	} // end modifyGET

	@PostMapping("/modify")
	public String modifyPOST(Model model, MemberVO memberVO, @AuthenticationPrincipal MemberDetails memberDetails) {
		log.info("회원 정보 수정 : " + memberVO);
		AlertVO alertVO = new AlertVO();
		String newPw = memberVO.getMemberPw();
		if(newPw.length() == 0) {
			newPw = null;
		}
		
		memberVO.setMemberId(memberDetails.getUsername());
		int res = memberService.updateMember(memberVO);
		
		if(res == 1) {
			alertVO.setAlertMsg("수정 성공");
			alertVO.setRedirectUri("board/mypage");
		}else {
			alertVO.setAlertMsg("수정 실패");
			alertVO.setRedirectUri("member/modify");
		}
		
		model.addAttribute("alertVO", alertVO);
		
		return Constant.ALERT_PATH;
	} // end modifyPOST
	
	
}
