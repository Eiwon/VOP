<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="isAuthenticated()">
	<sec:authentication var="memberDetails" property="principal"/>
</sec:authorize> 
<jsp:include page="../include/header.jsp"></jsp:include>
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
	<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
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

    <button type="submit" id="registerBtn">저장</button>

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
    const memberId = '${memberDetails.getUsername()}';
    console.log('member ID:', memberId);
        
    // 페이지 로드 시 실행되는 코드
    $(document).ready(function() {

        // 페이지 로드 시 기본 배송지 여부 확인
        checkDefaultAddress().then(function(isExistingDefault) {
            if (isExistingDefault) {
            	alert("기본 배송지가 설정되어 있습니다.");
            	// 기본 배송지가 설정되어 있으면 등록 버튼을 비활성화합니다.
                $('#registerBtn').prop('disabled', true);
            } else {
                console.log("기본 배송지가 없습니다.");
            }
        });
    });

    

 	

    // 기본 배송지 있는지 조회 
    function checkDefaultAddress() {
        return new Promise(function(resolve, reject) {
            $.ajax({
                url: 'checkDefaultAddress', // 서버 엔드포인트 URL
                method: 'GET', // GET 방식으로 요청
                data: { memberId: memberId },
                success: function(response) {
                    console.log('기본 배송지 확인 성공:', response); // true or false 
                    resolve(response.hasDefaultAddress); // 응답의 기본 배송지 여부를 반환
                },
                error: function(err) {
                    console.error("기본 배송지 확인 실패:", err);
                    resolve(false); // 오류가 발생하면 기본 배송지는 없다고 가정
                }
            });
        });
    }
    
 // 기본 배송지 설정 체크박스 변경 시 이벤트 핸들러
    $('#isDefault').change(function() {
        if ($(this).prop('checked')) {
            console.log("기본 배송지 설정됨");
            // 기본 배송지가 설정되면 등록 버튼을 비활성화합니다.
            $('#registerBtn').prop('disabled', true);
        } else {
            console.log("기본 배송지 해제됨");
            // 기본 배송지가 해제되면 등록 버튼을 활성화합니다.
            $('#registerBtn').prop('disabled', false);
        }
    });
    
 // 휴대폰 번호 형식 체크 함수
    function checkPhoneNumber(receiverPhone) {
        var phoneRegex = /^010-\d{4}-\d{4}$/;
        return phoneRegex.test(receiverPhone);
    }
 
 // 제출 버튼 클릭 시 실행되는 코드
    $('#deliveryForm').submit(function(event) {
        // 기본 제출 동작을 중지
        event.preventDefault();

        // 폼에서 휴대폰 번호 값을 가져옴
        var receiverPhone = $('#receiverPhone').val();

        // 휴대폰 번호 형식을 체크
        var isPhoneNumberValid = checkPhoneNumber(receiverPhone);

        // 휴대폰 번호 형식이 올바르지 않은 경우
        if (!isPhoneNumberValid) {
            // 사용자에게 알림
            alert('휴대폰 번호 형식이 올바르지 않습니다. (예: 010-1234-5678)');
            // 폼 제출을 중지
            return false;
        }

        // 휴대폰 번호 형식이 올바른 경우 폼을 제출
        this.submit();
    });
 
</script>

</body>
</html>           