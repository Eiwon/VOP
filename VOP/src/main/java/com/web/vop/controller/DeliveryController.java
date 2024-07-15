package com.web.vop.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.web.vop.domain.DeliveryVO;
import com.web.vop.domain.MemberDetails;
import com.web.vop.service.DeliveryService;
import com.web.vop.service.OrderService;
import com.web.vop.service.PaymentService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/Delivery")
@Log4j
public class DeliveryController {
    
    @Autowired
    DeliveryService deliveryService;
    
    @Autowired
    OrderService orderService;
    
    @Autowired
    PaymentService paymentService;
    
    // 마이페이지에서 주문목록 > 배송조회 페이지로 이동
    @GetMapping("/delivery")
    public String deliveryGET(int paymentId, int orderId, Model model) {
        log.info("deliveryGET()");
        
        Date now = new Date(); // 현재날짜
        // 배송 예정일 조회
    	String expectedDate = orderService.getExpectDateByPaymentId(orderId);
    	log.info("배송 예정일 : " + expectedDate);
    	SimpleDateFormat formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    	String formattedNow = formatted.format(now);
    	log.info("현재날짜 : " + formattedNow);
    	
        model.addAttribute("formattedNow", formattedNow);
        model.addAttribute("expectedDate", expectedDate);
        
    	// paymentId를 모델에 담아서 delivery.jsp로 전달
    	model.addAttribute("paymentId", paymentId);
    	
        return "/Delivery/delivery";  // delivery.jsp 로 이동
    }
    
    
    // 배송지 관리 페이지로 이동
    @GetMapping("/deliveryAddressList")
    public String deliveryAddressListGET(Model model, @AuthenticationPrincipal MemberDetails memberDetails) {
        log.info("deliveryAddressListGET() 페이지 이동 요청");
        
            // 배송지 상세 조회
        	String memberId = memberDetails.getUsername();
        	log.info("memberId : " + memberId);
            List<DeliveryVO> deliveryList = deliveryService.getMemberId(memberId);
            log.info(deliveryList.toString());
            
            model.addAttribute("deliveryList", deliveryList);
            // 기본 배송지 등록을 위해 memberId 보내기
            model.addAttribute("memberId", memberId);
            
        return "/Delivery/deliveryAddressList";
    }//end deliveryAddressListGET()
    
    
    // 배송지 등록 페이지로 이동
    @GetMapping("/deliveryRegister")
    public String deliveryRegisterGET(Model model, @AuthenticationPrincipal MemberDetails memberDetails) {
        log.info("deliveryRegister 페이지 이동 요청");
        
        	String memberId = memberDetails.getUsername();
        			
            model.addAttribute("memberId", memberId);
           
        return "/Delivery/deliveryRegister";
    }//end deliveryRegisterGET()

    // 배송지 등록 처리
    @PostMapping("/register")
    public String registerPOST(DeliveryVO deliveryVo, @AuthenticationPrincipal MemberDetails memberDetails) {
        log.info("배송 정보 등록");
        
        deliveryVo.setMemberId(memberDetails.getUsername());
        
        int res = deliveryService.registerDelivery(deliveryVo);
        log.info(res + "행 등록 성공");
        
        return "redirect:deliveryAddressList";
    }

    // 배송지 수정 페이지로 이동
    @GetMapping("/deliveryUpdate")
    public String deliveryUpdateGET(@RequestParam("deliveryId") int deliveryId, 
    		Model model, @AuthenticationPrincipal MemberDetails memberDetails) {
        log.info("deliveryUpdate 페이지 이동 요청");
        	
        // 배송지 전체 조회(by deliveryId)하기
        DeliveryVO delivery = deliveryService.getDeliveryById(deliveryId, memberDetails.getUsername());
        log.info("delivery" + delivery);
        
        // 사용자가 선택한 배송지 선택 정보 View로 전달
        model.addAttribute("delivery", delivery);
        
        // 사용자가 선택한 배송지의 deliveryId View로 전달
        model.addAttribute("deliveryId", deliveryId);
        
        // 사용자 아이디만 전달 
        model.addAttribute("memberId", memberDetails.getUsername());
        
	    return "/Delivery/deliveryUpdate";
    }//end deliveryUpdateGET()
    
    
    // 배송지 수정 처리
    @PostMapping("/update")
    public String updatePOST(DeliveryVO deliveryVo,
    		@AuthenticationPrincipal MemberDetails memberDetails, RedirectAttributes reAttr) {
        log.info("배송 정보 수정");
        
        // deliveryVo 객체에 memberId 설정
        deliveryVo.setMemberId(memberDetails.getUsername());
        
        int res = deliveryService.updateDelivery(deliveryVo);
        log.info(res + "행 수정 성공");
        
        return "redirect:deliveryAddressList";
    }//end updatePOST()
    
    
    @GetMapping("/popupSelect")
    public void popupSelectGET() {
    	log.info("배송지 선택 팝업 요청 ");
    } // end popupSelectGET
    
    
    @GetMapping("/popupRegister")
	public void popupRegisterGET() {
		log.info("배송지 등록 팝업 이동");
	} // end popupRegisterGET
    
    @GetMapping("/popupUpdate")
    public String popupUpdateGET(Model model, int deliveryId, @AuthenticationPrincipal UserDetails memberDetails) {
    	log.info("배송지 수정 팝업 이동");
    	String memberId = memberDetails.getUsername();
    	DeliveryVO deliveryVO = deliveryService.getDeliveryById(deliveryId, memberId);
    	model.addAttribute("deliveryVO", deliveryVO);
    	return "Delivery/popupRegister";
    } // end popupUpdateGEt
    
}//end DeliveryController
