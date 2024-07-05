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
		log.info("ȸ�� ���� ������ ��û");
	} // end registerGET
	
	@GetMapping("/login")
	public void loginGET(HttpServletRequest request) {
		log.info("login ������ �̵� ��û");
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
//			alertVO.setAlertMsg("�α��� ����");
//			alertVO.setRedirectUri("board/main");
//		}else {
//			alertVO.setAlertMsg("�߸��� ���̵� �Ǵ� ��й�ȣ�Դϴ�");
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
		log.info("login ����");
		AlertVO alertVO = new AlertVO();
		alertVO.setAlertMsg("�߸��� ���̵� �Ǵ� ��й�ȣ �Դϴ�.");
		alertVO.setRedirectUri("member/login");
		model.addAttribute("alertVO", alertVO);
		return Constant.ALERT_PATH;
	} // end loginFailGET
	
	@GetMapping("/findAccount")
	public void findAccount() {
		log.info("���̵�, ��й�ȣ ã�� ������ ��û");
	} // end findAccount
	
	@PostMapping("/findAccount")
	public String mailAuthenticationPOST(Model model, String memberEmail, String authCode){
		log.info("���̵� ã�� ���� �ڵ� :" + authCode);
		AlertVO alertVO = new AlertVO();
		String returnPath = null;
		List<String> memberIdList = null;
		int resultCode = mailAuthenticationUtil.verifyAuthCode(memberEmail, authCode);
		
		switch (resultCode) {
		case 100:
			memberIdList = memberService.getIdByEmail(memberEmail);
			log.info("�˻��� id : " + memberIdList);
			model.addAttribute("memberIdList", memberIdList);
			returnPath = "member/findAccountResult";
			// ���� �ڵ� ��ġ
			break;
		case 101 : 
			// ��ȿ�Ⱓ ����
			alertVO.setAlertMsg("����� ���� ��ȣ �Դϴ�.");
			alertVO.setRedirectUri("member/findAccount");
			model.addAttribute("alertVO", alertVO);
			returnPath = Constant.ALERT_PATH;
			break;
		case 102 :
			// ���� �ڵ� ����ġ
			alertVO.setAlertMsg("�����ڵ� ����ġ");
			alertVO.setRedirectUri("member/findAccount");
			model.addAttribute("alertVO", alertVO);
			returnPath = Constant.ALERT_PATH;
			break;
		case 103 :
			alertVO.setAlertMsg("���� ��ȣ �߼��� ���� ���ּ���");
			alertVO.setRedirectUri("member/findAccount");
			model.addAttribute("alertVO", alertVO);
			returnPath = Constant.ALERT_PATH;
			// ���� ���� ����
		default:
			alertVO.setAlertMsg("�� �� ���� ����.");
			alertVO.setRedirectUri("member/findAccount");
			model.addAttribute("alertVO", alertVO);
			returnPath = Constant.ALERT_PATH;
			break;
		}
		
		return returnPath;
	} // end mailAuthenticationPOST
	
	@GetMapping("/findPassword")
	public void findPassword() {
		log.info("��й�ȣ ã�� ������ ��û");
	} // end findPassword
	
	@PostMapping("/findPassword")
	public String findPasswordPOST(Model model, String memberId) {
		log.info("��й�ȣ ã�� ��û");
		
		String memberEmail = null;
		// �Էµ� ���̵� ��ȿ���� Ȯ��
		MemberVO memberVO = memberService.getMemberInfo(memberId);
		if(memberVO != null) {
			// ��ȿ�ϸ� �ش� ���̵��� email�� ������ȣ ����
			memberEmail = memberVO.getMemberEmail();
			mailAuthenticationUtil.sendAuthEmail(memberEmail);
			
			String secretEmail = ""; // �̸��� �Ϻθ� *�� ������
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
		log.info("��й�ȣ �缳��");
		AlertVO alertVO = new AlertVO();
		String memberEmail = memberService.getEmailById(memberId);
		int resultCode = mailAuthenticationUtil.verifyAuthCode(memberEmail, authCode);
		
		switch (resultCode) {
		case 100:
			// ���� �ڵ� ��ġ
			memberService.updatePw(memberId, memberPw);
			alertVO.setAlertMsg("��й�ȣ ���� ����!");
			alertVO.setRedirectUri("member/login");
			break;
		case 101 : 
			// ��ȿ�Ⱓ ����
			alertVO.setAlertMsg("����� ���� ��ȣ �Դϴ�.");
			alertVO.setRedirectUri("member/findPassword");
			break;
		case 102 :
			// ���� �ڵ� ����ġ
			alertVO.setAlertMsg("���� �ڵ尡 ��ġ���� �ʽ��ϴ�.");
			alertVO.setRedirectUri("member/findPassword");
			break;
		case 103 :
			// ���� ���� ����
			alertVO.setAlertMsg("���� ��ȣ �߼��� ���� ���ּ���.");
			alertVO.setRedirectUri("member/findPassword");
		default:
			alertVO.setAlertMsg("�� �� ���� ����.");
			alertVO.setRedirectUri("member/findPassword");
			break;
		}
		model.addAttribute("alertVO", alertVO);
		
		return Constant.ALERT_PATH;
	} // end changePw
	
	@PostMapping("/register") // ȸ������ ��û
	public String registerPOST(Model model, MemberVO memberVO) {
		log.info("ȸ�� ���� ��û : " + memberVO);
		AlertVO alertVO = new AlertVO();
		int res = memberService.registerMember(memberVO);
		log.info("ȸ�� ���� ��� : " + res);
		
		if(res == 1) {
			alertVO.setAlertMsg("ȸ������ ����!");
			alertVO.setRedirectUri("member/login");
		}else {
			alertVO.setAlertMsg("ȸ������ ����");
			alertVO.setRedirectUri("member/register");
		}
		
		model.addAttribute("alertVO", alertVO);
		
		return Constant.ALERT_PATH;
	} // end registerPOST
	
	
	@GetMapping("/modify")
	public void modifyGET(Model model, @AuthenticationPrincipal UserDetails memberDetails) {
		log.info("�� ���� ���� ������ ��û");
		String memberId = memberDetails.getUsername();
		model.addAttribute("memberVO", memberService.getMemberInfo(memberId));
	} // end modifyGET

	@PreAuthorize("#memberVO.memberId == authentication.principal.username")
	@PostMapping("/modify")
	public String modifyPOST(Model model, MemberVO memberVO) {
		log.info("ȸ�� ���� ���� : " + memberVO);
		AlertVO alertVO = new AlertVO();
		String newPw = memberVO.getMemberPw();
		
		int res = memberService.updateMember(memberVO);
		
		if(res == 1) {
			alertVO.setAlertMsg("���� ����");
			alertVO.setRedirectUri("board/mypage");
		}else {
			alertVO.setAlertMsg("���� ����");
			alertVO.setRedirectUri("member/modify");
		}
		
		model.addAttribute("alertVO", alertVO);
		
		return Constant.ALERT_PATH;
	} // end modifyPOST
	
	@GetMapping("/withdrawal")
	public String withdrawGET(Model model, @AuthenticationPrincipal UserDetails memberDetails) {
		log.info("ȸ�� Ż�� ������ �̵�");
		String memberId = memberDetails.getUsername();
		// Ż�� ���� ���� �˻�
		int res = memberService.isWithdrawable(memberId);
		
		if(res > 0) {
			AlertVO alertVO = new AlertVO();
			alertVO.setAlertMsg("��ϵ� ��ǰ�� ������ Ż���� �� �����ϴ�.");
			alertVO.setRedirectUri("product/myProduct");
			model.addAttribute("alertVO", alertVO);
			return Constant.ALERT_PATH;
		}
		
		// �̸��� �˻�
		String memberEmail = memberService.getEmailById(memberId);
		model.addAttribute("memberEmail", memberEmail);
		return "member/withdrawal";
	} // end withdrawGET
	
	@PostMapping("/withdrawal")
	public String withdrawPOST(Model model, String authCode, @AuthenticationPrincipal UserDetails memberDetails,
			HttpServletRequest request) {
		log.info("ȸ�� Ż�� ��û");
				
		AlertVO alertVO = new AlertVO();
		String memberEmail = memberService.getEmailById(memberDetails.getUsername());
		int resultCode = mailAuthenticationUtil.verifyAuthCode(memberEmail, authCode);
		
		switch (resultCode) {
		case 100:
			// ���� �ڵ� ��ġ
			memberService.deleteMember(memberDetails.getUsername());
			alertVO.setAlertMsg("ȸ�� Ż��Ǿ����ϴ�.");
			alertVO.setRedirectUri("board/main");
			request.getSession().invalidate();
			break;
		case 101 : 
			// ��ȿ�Ⱓ ����
			alertVO.setAlertMsg("����� ���� ��ȣ �Դϴ�.");
			alertVO.setRedirectUri("member/withdrawal");
			break;
		case 102 :
			// ���� �ڵ� ����ġ
			alertVO.setAlertMsg("���� �ڵ尡 ��ġ���� �ʽ��ϴ�.");
			alertVO.setRedirectUri("member/withdrawal");
			break;
		case 103 :
			// ���� ���� ����
			alertVO.setAlertMsg("���� ��ȣ �߼��� ���� ���ּ���.");
			alertVO.setRedirectUri("member/withdrawal");
			break;
		default:
			alertVO.setAlertMsg("�� �� ���� ����.");
			alertVO.setRedirectUri("member/withdrawal");
			break;
		}
		
		model.addAttribute("alertVO", alertVO);
		
		return Constant.ALERT_PATH;
	} // end withdrawPOST
	
}
