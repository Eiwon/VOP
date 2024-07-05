package com.web.vop.controller;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.vop.domain.AlertVO;
import com.web.vop.domain.MemberVO;
import com.web.vop.service.JwtTokenProvider;
import com.web.vop.service.MemberService;
import com.web.vop.service.UserDetailsServiceImple;
import com.web.vop.util.Constant;
import com.web.vop.util.MailAuthenticationUtil;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/member")
@Log4j
public class MemberController {

	@Autowired
	public MemberService memberService;
	
	@Autowired
	public UserDetailsServiceImple UserDetailsService;
	
	@Autowired
	public MailAuthenticationUtil mailAuthenticationUtil;
	
	@Autowired
	public JwtTokenProvider jwtTokenProvider;
	
	@GetMapping("/register")
	public void registerGET() {
		log.info("회원 가입 페이지 요청");
	} // end registerGET
	
	@GetMapping("/login")
	public void loginGET(HttpServletRequest request) {
		log.info("login 페이지 이동 요청");
		String prevPage = request.getHeader("Referer");
		request.getSession().setAttribute("prevPage", prevPage);
	} // end loginGET
	
//	@PostMapping("/login")
//	public String loginPOST(Model model, HttpServletResponse response, MemberVO memberVO) {
//		AlertVO alertVO = new AlertVO();
//		UserDetails memberDetails = memberService.authentication(memberVO.getMemberId(), memberVO.getMemberPw());
//		String token = null;
//		log.info(memberDetails);
//		if(memberDetails != null) {
//			token = jwtTokenProvider.createToken(memberDetails);
//			log.info(token);
//			
//			try {
//				token = URLEncoder.encode("Bearer " + token, "UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//			Cookie accessCookie = new Cookie("access_token", token);
//			accessCookie.setHttpOnly(true);
//			accessCookie.setDomain("localhost");
//			accessCookie.setPath("/");
//			accessCookie.setMaxAge(60 * 60* 1000);
//			accessCookie.setSecure(true);
//			response.addCookie(accessCookie);
//			alertVO.setAlertMsg("로그인 성공");
//			alertVO.setRedirectUri("board/main");
//		}else {
//			alertVO.setAlertMsg("잘못된 아이디 또는 비밀번호입니다");
//			alertVO.setRedirectUri("member/login");
//		}
//		model.addAttribute("alertVO", alertVO);
//		return Constant.ALERT_PATH;
//	}
	
	@PostMapping("/logout")
	public String logoutPOST(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals("access_token")) {
				cookie.setMaxAge(1);
			}
		}
		return "redirect:../board/main";
	}
	
	@GetMapping("/loginSuccess")
	public void loginSuccessGET() {
		log.info("login success get");
	}
	
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
		int resultCode = mailAuthenticationUtil.verifyAuthCode(memberEmail, authCode);
		
		switch (resultCode) {
		case 100:
			memberIdList = memberService.getIdByEmail(memberEmail);
			log.info("검색된 id : " + memberIdList);
			model.addAttribute("memberIdList", memberIdList);
			returnPath = "member/findAccountResult";
			// 인증 코드 일치
			break;
		case 101 : 
			// 유효기간 만료
			alertVO.setAlertMsg("만료된 인증 번호 입니다.");
			alertVO.setRedirectUri("member/findAccount");
			model.addAttribute("alertVO", alertVO);
			returnPath = Constant.ALERT_PATH;
			break;
		case 102 :
			// 인증 코드 불일치
			alertVO.setAlertMsg("인증코드 불일치");
			alertVO.setRedirectUri("member/findAccount");
			model.addAttribute("alertVO", alertVO);
			returnPath = Constant.ALERT_PATH;
			break;
		case 103 :
			alertVO.setAlertMsg("인증 번호 발송을 먼저 해주세요");
			alertVO.setRedirectUri("member/findAccount");
			model.addAttribute("alertVO", alertVO);
			returnPath = Constant.ALERT_PATH;
			// 인증 정보 없음
		default:
			alertVO.setAlertMsg("알 수 없는 오류.");
			alertVO.setRedirectUri("member/findAccount");
			model.addAttribute("alertVO", alertVO);
			returnPath = Constant.ALERT_PATH;
			break;
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
			mailAuthenticationUtil.sendAuthEmail(memberEmail);
			
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
		int resultCode = mailAuthenticationUtil.verifyAuthCode(memberEmail, authCode);
		
		switch (resultCode) {
		case 100:
			// 인증 코드 일치
			memberService.updatePw(memberId, memberPw);
			alertVO.setAlertMsg("비밀번호 변경 성공!");
			alertVO.setRedirectUri("member/login");
			break;
		case 101 : 
			// 유효기간 만료
			alertVO.setAlertMsg("만료된 인증 번호 입니다.");
			alertVO.setRedirectUri("member/findPassword");
			break;
		case 102 :
			// 인증 코드 불일치
			alertVO.setAlertMsg("인증 코드가 일치하지 않습니다.");
			alertVO.setRedirectUri("member/findPassword");
			break;
		case 103 :
			// 인증 정보 없음
			alertVO.setAlertMsg("인증 번호 발송을 먼저 해주세요.");
			alertVO.setRedirectUri("member/findPassword");
		default:
			alertVO.setAlertMsg("알 수 없는 오류.");
			alertVO.setRedirectUri("member/findPassword");
			break;
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
	public void modifyGET(Model model, @AuthenticationPrincipal UserDetails memberDetails) {
		log.info("내 정보 수정 페이지 요청");
		String memberId = memberDetails.getUsername();
		model.addAttribute("memberVO", memberService.getMemberInfo(memberId));
	} // end modifyGET

	@PreAuthorize("#memberVO.memberId == authentication.principal.username")
	@PostMapping("/modify")
	public String modifyPOST(Model model, MemberVO memberVO) {
		log.info("회원 정보 수정 : " + memberVO);
		AlertVO alertVO = new AlertVO();
		String newPw = memberVO.getMemberPw();
		
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
	
	@GetMapping("/withdrawal")
	public String withdrawGET(Model model, @AuthenticationPrincipal UserDetails memberDetails) {
		log.info("회원 탈퇴 페이지 이동");
		String memberId = memberDetails.getUsername();
		// 탈퇴 가능 여부 검사
		int res = memberService.isWithdrawable(memberId);
		
		if(res > 0) {
			AlertVO alertVO = new AlertVO();
			alertVO.setAlertMsg("등록된 상품이 있으면 탈퇴할 수 없습니다.");
			alertVO.setRedirectUri("product/myProduct");
			model.addAttribute("alertVO", alertVO);
			return Constant.ALERT_PATH;
		}
		
		// 이메일 검색
		String memberEmail = memberService.getEmailById(memberId);
		model.addAttribute("memberEmail", memberEmail);
		return "member/withdrawal";
	} // end withdrawGET
	
	@PostMapping("/withdrawal")
	public String withdrawPOST(Model model, String authCode, @AuthenticationPrincipal UserDetails memberDetails,
			HttpServletRequest request) {
		log.info("회원 탈퇴 신청");
				
		AlertVO alertVO = new AlertVO();
		String memberEmail = memberService.getEmailById(memberDetails.getUsername());
		int resultCode = mailAuthenticationUtil.verifyAuthCode(memberEmail, authCode);
		
		switch (resultCode) {
		case 100:
			// 인증 코드 일치
			memberService.deleteMember(memberDetails.getUsername());
			alertVO.setAlertMsg("회원 탈퇴되었습니다.");
			alertVO.setRedirectUri("board/main");
			request.getSession().invalidate();
			break;
		case 101 : 
			// 유효기간 만료
			alertVO.setAlertMsg("만료된 인증 번호 입니다.");
			alertVO.setRedirectUri("member/withdrawal");
			break;
		case 102 :
			// 인증 코드 불일치
			alertVO.setAlertMsg("인증 코드가 일치하지 않습니다.");
			alertVO.setRedirectUri("member/withdrawal");
			break;
		case 103 :
			// 인증 정보 없음
			alertVO.setAlertMsg("인증 번호 발송을 먼저 해주세요.");
			alertVO.setRedirectUri("member/withdrawal");
			break;
		default:
			alertVO.setAlertMsg("알 수 없는 오류.");
			alertVO.setRedirectUri("member/withdrawal");
			break;
		}
		
		model.addAttribute("alertVO", alertVO);
		
		return Constant.ALERT_PATH;
	} // end withdrawPOST
	
}
