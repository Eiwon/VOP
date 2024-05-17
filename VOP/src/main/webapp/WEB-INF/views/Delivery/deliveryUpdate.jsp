<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<title>배송지 수정/삭제</title>
<!-- 우편번호 API 스크립트 -->
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
</head>
<body>
<h2>배송지 수정</h2>

<%
	// 세션 객체 가져오기
	HttpSession sessionJSP = request.getSession();
	// 세션에 저장된 memberId 가져오기
	String memberId = (String) sessionJSP.getAttribute("memberId");
%>

<form id="delivery" action="update" method="post">

    <label for="receiverName">받는 사람:</label>
    <input type="text" id="receiverName" name="receiverName" value="${delivery.receiverName}" required><br><br>

    <!-- 우편번호 찾기 관련 코드 -->
    <input type="text" id="sample6_postcode" placeholder="우편번호">
    <input type="button" onclick="sample6_execDaumPostcode()" value="우편번호 찾기"><br>
    <input type="text" id="receiverAddress" name="receiverAddress" value="${delivery.receiverAddress}" placeholder="주소"><br>
    <input type="text" id="deliveryAddressDetails" name="deliveryAddressDetails" value="${delivery.deliveryAddressDetails}" placeholder="상세주소"><br><br>

    <label for="receiverPhone">휴대폰 번호:</label>
    <input type="text" id="receiverPhone" name="receiverPhone" value="${delivery.receiverPhone}" placeholder="010-1234-5678" required><br><br>

    <label for="requirement">배송 요청사항:</label><br>
    <textarea id="requirement" name="requirement" rows="4" cols="50">${delivery.requirement}</textarea><br><br>

    <input type="checkbox" id="isDefault" name="isDefault" value="1" ${delivery.isDefault == 1 ? 'checked' : ''}>
    <label for="isDefault">기본 배송지로 설정</label><br><br>

    <!-- 수정 버튼 -->
    <button type="submit">수정</button>
    
</form>


<script>
    function sample6_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var addr = ''; // 주소 변수

                // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    addr = data.roadAddress;
                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    addr = data.jibunAddress;
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('sample6_postcode').value = data.zonecode;
                document.getElementById("receiverAddress").value = addr;
                // 커서를 상세주소 필드로 이동한다.
                document.getElementById("deliveryAddressDetails").focus();
            }
        }).open();
    }
</script>

<!-- 삭제 버튼 -->
<form id="deleteForm" action="delete" method="post">
    <input type="hidden" id="deliveryId" name="deliveryId" value="${delivery.deliveryId}">
    <button type="button" id="deleteButton">삭제</button>
</form>

<script>
    // 삭제 버튼 클릭 시 삭제 동작 실행
    $(document).ready(function() {
        $("#deleteButton").click(function() {
            var deliveryId = $("#deliveryId").val();
            // 배송지 삭제를 위한 Ajax 요청
            $.ajax({
                url: "delete",
                type: "POST",
                data: { deliveryId: deliveryId },
                success: function(data) {
                    // 삭제 성공 시 페이지 새로고침 또는 다른 동작 수행
                    location.reload(); // 페이지 새로고침 예시
                },
                error: function(xhr, status, error) {
                    // 삭제 실패 시 에러 처리
                    console.error("배송지 삭제 실패:", error);
                }
            });
        });
    });
</script>

<script>
    
    // 배송지 정보를 폼에 입력하는 함수
    function populateForm(delivery) {
        document.getElementById("receiverName").value = delivery.receiverName;
        document.getElementById("receiverAddress").value = delivery.receiverAddress;
        document.getElementById("deliveryAddressDetails").value = delivery.deliveryAddressDetails;
        document.getElementById("receiverPhone").value = delivery.receiverPhone;
        document.getElementById("requirement").value = delivery.requirement;
        
        // 기본 배송지 설정
        document.getElementById("isDefault").checked = (delivery.isDefault == 1);
    }

    // 페이지 로딩 시 해당 배송지 정보 입력
    $(document).ready(function() {
    	
    	var deliveryId = $("#deliveryId").val(); // deliveryId가 페이지 로드 시 설정되어 있어야 한다
        
    	console.log('deliveryId : ' + deliveryId);
        // 서버에서 해당 배송지 정보를 받아오는 Ajax 요청 
        $.ajax({
        	url : "restDeliveryUpdate",
        	type : "GET",
        	data : { deliveryId: deliveryId}, // 배송지 Id를 서버에 전달
        	success : function(data) {
        		// 받아온 배송지 정보를 deliveryInfo에 할당
        		console.log(data);
        		var deliveryInfo = data; // 여기서 data는 서버로부터 받아온 JSON 형식의 배송지 
        		populateForm(deliveryInfo); // 폼에 배송지 정보 입력
        	},
        	error : function(xhr, status, error) {
        		console.error("배송지 정보 불러오기 실패 : ",error);
        	}
        });
    	
    });
</script>
</body>
</html>