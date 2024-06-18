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
		log.info("ȸ�� ���� ������ ��û");
	} // end registerGET
	
	@GetMapping("/login")
	public void loginGET() {
		log.info("login ������ �̵� ��û");
	} // end loginGET
	
	
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
		boolean res = mailAuthService.verifyAuthCode(memberEmail, authCode);
		
		if(res) {
			memberIdList = memberService.getIdByEmail(memberEmail);
			log.info("�˻��� id : " + memberIdList);
			model.addAttribute("memberIdList", memberIdList);
			returnPath = "member/findAccountResult";
		}else {
			alertVO.setAlertMsg("�����ڵ� ����ġ");
			alertVO.setRedirectUri("member/findAccount");
			model.addAttribute("alertVO", alertVO);
			returnPath = Constant.ALERT_PATH;
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
			mailAuthService.sendAuthEmail(memberEmail);
			
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
		boolean isCorrect = mailAuthService.verifyAuthCode(memberEmail, authCode);
		
		if(isCorrect) {
			// ���� �ڵ� ��ġ
			memberService.updatePw(memberId, memberPw);
			alertVO.setAlertMsg("��й�ȣ ���� ����!");
			alertVO.setRedirectUri("member/login");
		}else {
			// ���� �ڵ� ����ġ
			alertVO.setAlertMsg("���� �ڵ尡 ��ġ���� �ʽ��ϴ�.");
			alertVO.setRedirectUri("member/findPassword");
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
	public void modifyGET(Model model, @AuthenticationPrincipal MemberDetails memberDetails) {
		log.info("�� ���� ���� ������ ��û");
		String memberId = memberDetails.getUsername();
		model.addAttribute("memberVO", memberService.getMemberInfo(memberId));
	} // end modifyGET

	@PostMapping("/modify")
	public String modifyPOST(Model model, MemberVO memberVO, @AuthenticationPrincipal MemberDetails memberDetails) {
		log.info("ȸ�� ���� ���� : " + memberVO);
		AlertVO alertVO = new AlertVO();
		String newPw = memberVO.getMemberPw();
		if(newPw.length() == 0) {
			newPw = null;
		}
		
		memberVO.setMemberId(memberDetails.getUsername());
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
	
	
}
