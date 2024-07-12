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
<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">

<!-- 우편번호 API 스크립트 -->
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
	<style>
        
        body {
            padding: 20px; /* Add padding for visual appeal */
        }
        
        /* 예시: 입력란 너비 조절 */
		.form-control {
		    width: 100%; /* 또는 필요한 너비 값 설정 */
		    max-width: 400px; /* 최대 너비 설정 (예: 400px) */
		    margin: auto; /* 가운데 정렬 */
		}
    </style>
</head>
<body>
<jsp:include page="../include/sideBar.jsp"/>

<div class="container text-center">
<h2 class="mt-5">배송지 등록</h2><br>

<form id="deliveryForm" action="register" method="post" class="mt-4">
	<input type="hidden"  class="form-control" name="${_csrf.parameterName }" value="${_csrf.token }">
	
	<div class="form-group">
	    <label for="receiverName">받는 사람:</label><br>
	    <input type="text" class="form-control" id="receiverName" name="receiverName" onblur="checkValid(this)" required >
		<div></div> <!-- 유효성 메시지 출력을 위한 빈 div -->
	</div>
	
    <div class="form-row justify-content-center">
            <div class="form-group col-md-6">
                <label for="sample6_postcode">우편번호</label>
                <div class="input-group">
                    <input type="text" class="form-control" id="sample6_postcode" placeholder="우편번호">
                    <div class="input-group-append">
                        <button class="btn btn-outline-secondary" type="button" onclick="sample6_execDaumPostcode()">우편번호 찾기</button>
                    </div>
                </div>
            </div>
        </div>
        
         <div class="form-group">
            <label for="receiverAddress">주소:</label>
            <input type="text" class="form-control" id="receiverAddress" name="receiverAddress" placeholder="주소">
        </div>
        
     	<div class="form-group">
            <label for="deliveryAddressDetails">상세주소:</label>
            <input type="text" class="form-control" id="deliveryAddressDetails" name="deliveryAddressDetails" placeholder="상세주소">
        </div>
        
		<div class="form-group">
		    <label for="receiverPhone">휴대폰 번호:</label>
		    <input type="text" class="form-control" id="receiverPhone" name="receiverPhone" placeholder="01012345678 형식으로 입력하세요." onblur="checkValid(this)" required>
		    <div></div> <!-- 유효성 메시지 출력을 위한 빈 div -->
	    </div>
	    <br><br>
	    
		<div class="form-group">
		    <label for="requirement">배송 요청사항:</label><br>
		    <textarea id="requirement" name="requirement" rows="4" cols="50">일반 : 문앞</textarea><br><br>
		</div>
		
		<div class="form-check">
    		<input type="checkbox" id="isDefault" name="isDefault" value="1" checked>
    		<label for="isDefault">기본 배송지로 설정</label><br><br>
		</div>
		
    	<button type="submit" class="btn btn-success mt-3" id="registerBtn">저장</button>

</form>
</div> <!-- end container -->

<!-- Bootstrap JS -->
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>

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
    });//end document.ready()

    
    let checkMap = {
    		receiverName : {
				exp : new RegExp("^[a-zA-Z가-힣]{2,20}$"),
				success : "올바른 입력 형식 입니다.",
				fail : "이름은 2~20자의 한글 또는 알파벳으로 입력하세요.",
				isValid : false
			},
			
			receiverPhone: {
				exp: new RegExp("^010\\d{8}$"),
	            success: "올바른 입력 형식 입니다.",
	            fail: "휴대폰 번호는 01012345678 형식으로 입력하세요.",
	            isValid: false
	        }
    };
 	
 	// 입력값의 유효성을 체크하는 함수
    function checkValid(input){
		let type = $(input).attr('id'); // 입력 요소의 id를 가져옴
		let inputVal = $(input).val(); // 입력한 값
		
		console.log('Checking validity for:', type); // 디버깅을 위해 추가
	    
	    // checkMap에 type이 존재하는지 확인
	    if (!checkMap.hasOwnProperty(type)) {
	        console.error('checkMap에 존재하지 않는 type입니다:', type);
	        return;
	    }
		
		if(inputVal.length == 0){  // 입력값이 없을 경우
			checkMap[type].isValid = false;
			return;
		}
		
		let isValid = checkMap[type].exp.test(inputVal); // 정규표현식을 이용해 유효성 검사
		console.log(type + ' 유효성 확인 = ' + isValid);
		
		let msg = isValid ? checkMap[type].success : checkMap[type].fail; // 유효성에 따른 메시지 선택
		
		$(input).next().text(msg); // 입력 요소 다음에 메시지 출력
		checkMap[type].isValid = isValid; // 유효성 상태 저장
		
		if(!isValid) {  // 유효하지 않으면 종료
			return;
		}
			
	} // end checkValid
    
    
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
    

 
 // 제출 버튼 클릭 시 실행되는 코드
    $('#deliveryForm').submit(function(event) {

        for (let x in checkMap) {
            if (!checkMap[x].isValid) {
                alert('유효하지 않은 입력 : ' + x);
                event.preventDefault();
                return;
            }
        }
    });//end deliveryForm
 
	
</script>

</body>
</html>           