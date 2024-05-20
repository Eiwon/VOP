<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<title>배송지 등록</title>
<!-- 우편번호 API 스크립트 -->
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
</head>
<body>
<h2>배송지 등록</h2>
<form id="deliveryForm" action="register" method="post">
    <label for="receiverName">받는 사람:</label>
    <input type="text" id="receiverName" name="receiverName" required><br><br>

    <!-- 우편번호 찾기 관련 코드 -->
    <input type="text" id="sample6_postcode" placeholder="우편번호">
    <input type="button" onclick="sample6_execDaumPostcode()" value="우편번호 찾기"><br>
    <input type="text" id="receiverAddress" name="receiverAddress" placeholder="주소"><br>
    <input type="text" id="deliveryAddressDetails" name="deliveryAddressDetails" placeholder="상세주소"><br><br>

    <label for="receiverPhone">휴대폰 번호:</label>
    <input type="text" id="receiverPhone" name="receiverPhone" placeholder="010-1234-5678" required><br><br>

    <label for="requirement">배송 요청사항:</label><br>
    <textarea id="requirement" name="requirement" rows="4" cols="50">일반 : 문앞</textarea><br><br>

    <input type="checkbox" id="isDefault" name="isDefault" value="1" checked>
    <label for="isDefault">기본 배송지로 설정</label><br><br>

    <button type="submit">저장</button>

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
<script>
	//저장 버튼 클릭 시 폼 유효성 검사 후 제출 및 페이지 이동
	document.getElementById("deliveryForm").addEventListener("submit", function(event) {
    	var receiverName = document.getElementById("receiverName").value;
    	var receiverPhone = document.getElementById("receiverPhone").value;

        // 받는 사람, 휴대폰 번호가 비어있는지 확인
        if (!receiverName || !receiverPhone) {
            alert("받는 사람, 휴대폰 번호는 필수 입력 사항입니다.");
            event.preventDefault(); // 폼 제출 막기
        }

        // 휴대폰 번호 형식 체크
        if (!checkPhoneNumber(receiverPhone)) {
            alert("올바른 휴대폰 번호 형식이 아닙니다. 국내 휴대번호만 입력 가능합니다.");
            event.preventDefault(); // 폼 제출 막기
        }

        // 기본 배송지로 설정되었는지 확인
        var isDefault = document.getElementById("isDefault").checked;
        if (isDefault && !receiverName) {
            alert("기본 배송지로 설정하려면 받는 사람 정보가 필요합니다.");
            event.preventDefault(); // 폼 제출 막기
        }
    });

    // 휴대폰 번호 형식 체크 함수
    function checkPhoneNumber(receiverPhone) {
    var phoneRegex = /^010-\d{4}-\d{4}$/;
    return phoneRegex.test(receiverPhone);
}
</script>
</body>
</html>           