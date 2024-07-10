<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="isAuthenticated()">
	<sec:authentication var="memberDetails" property="principal"/>
</sec:authorize> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="${_csrf.token }" content="${_csrf.token }">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<title>배송지 수정/삭제</title>
<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">

<!-- 우편번호 API 스크립트 -->
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<style>

        body {
            padding: 20px; /* Add padding for visual appeal */
        }
        
         .form-container {
            max-width: 600px; /* 원하는 폭으로 설정 */
            margin: auto; /* 가운데 정렬 */
            padding: 20px; /* 내용과의 간격 */
        }

        /* 예시: 입력란 너비 조절 */
		.form-control {
		    width: 100%; /* 입력 요소 너비 100% */
		    max-width: 600px; /* 최대 너비 설정 (예: 400px) */
		}
    </style>
</head>
<body>


<div class="container-fluid">
<div class="form-container">
	<h2 class="text-center mt-5">배송지 수정</h2><br>


	<form id="delivery" action="update" method="post" class="mt-4 text-center" >
		<input type="hidden" class="form-control" name="${_csrf.parameterName }" value="${_csrf.token }">
		<input type="hidden" id="deliveryId" name="deliveryId" value="${delivery.deliveryId}">
	
    <div class="form-group text-center">
	    <label for="receiverName">받는 사람:</label><br>
	    <input type="text" class="form-control" id="receiverName" name="receiverName" onblur="checkValid(this)" required >
		<div></div> <!-- 유효성 메시지 출력을 위한 빈 div -->
	</div>

    
    	<div class="form-row justify-content-center">
            <div class="form-group text-center">
                <label for="sample6_postcode">우편번호</label>
                <div class="input-group">
                    <input type="text" class="form-control" id="sample6_postcode" placeholder="우편번호">
                    <div class="input-group-append">
                        <button class="btn btn-outline-secondary" type="button" onclick="sample6_execDaumPostcode()">우편번호 찾기</button>
                    </div>
                </div>
            </div>
        </div>
        
         <div class="form-group text-center">
            <label for="receiverAddress">주소:</label>
            <input type="text" class="form-control" id="receiverAddress" name="receiverAddress" placeholder="주소">
        </div>
        
     	<div class="form-group text-center">
            <label for="deliveryAddressDetails">상세주소:</label>
            <input type="text" class="form-control" id="deliveryAddressDetails" name="deliveryAddressDetails" placeholder="상세주소">
        </div>
        
		<div class="form-group text-center">
		    <label for="receiverPhone">휴대폰 번호:</label>
		    <input type="text" class="form-control" id="receiverPhone" name="receiverPhone" placeholder="010-1234-5678 형식으로 입력하세요." onblur="checkValid(this)" required>
		    <div></div> <!-- 유효성 메시지 출력을 위한 빈 div -->
	    </div>
	    <br><br>
	    
		<div class="form-group text-center">
		    <label for="requirement">배송 요청사항:</label><br>
		    <textarea id="requirement" name="requirement" rows="4" cols="50">일반 : 문앞</textarea><br><br>
		</div>
		
		<div class="form-check text-center">
    		<input type="checkbox" id="isDefault" name="isDefault" value="1" checked>
    		<label for="isDefault">기본 배송지로 설정</label><br><br>
		</div>

	    <!-- 수정 버튼 -->
	    <button type="submit" class="btn btn-success mt-3" id="updateButton">수정</button>
    </form>
    
		 <!-- 삭제 버튼 -->
        <form id="deleteForm" action="delete" method="post" class="text-center">
            <input type="hidden" id="deliveryId" name="deliveryId" value="${delivery.deliveryId}">
            <button type="button" class="btn btn-success mt-3" id="deleteButton">삭제</button>
        </form>
    </div>
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
    // 삭제 버튼 클릭 시 삭제 동작 실행
    $(document).ready(function() {
        $("#deleteButton").click(function() {
            var deliveryId = $("#deliveryId").val();
         
            // 배송지 삭제를 위한 Ajax 요청
            $.ajax({
                url: "delete/" + deliveryId,
                headers : {
                	'X-CSRF-TOKEN' : '${_csrf.token }' 
    			},
                type: 'DELETE',
                success: function(response) {
                	
                	if(response === 1){
                    	// 삭제 성공 시 페이지 새로고침 또는 다른 동작 수행
                		window.location.href = "../Delivery/deliveryAddressList";               		
                	}else{
                		// 삭제 실패 시 에러 메시지 출력
                        console.error("배송지 삭제 실패");
                        alert("배송지 삭제 실패");
                	}
                },
                error: function(xhr, status, error) {
                	// Ajax 요청 실패 시 에러 처리
                    console.error("배송지 삭제 실패:", error);
                    alert("배송지 삭제 중 오류가 발생했습니다.");
                }
            });//end ajax
        }); //end click event handler
    }); //end document.ready function
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
    
    let checkMap = {
    		receiverName : {
				exp : new RegExp("^[a-zA-Z가-힣]{2,20}$"),
				success : "올바른 입력 형식 입니다.",
				fail : "이름은 2~20자의 한글 또는 알파벳으로 입력하세요.",
				isValid : false
			},
			
			receiverPhone: {
	            exp: new RegExp("^010-\\d{4}-\\d{4}$"),
	            success: "올바른 입력 형식 입니다.",
	            fail: "휴대폰 번호는 010-1234-5678 형식으로 입력하세요.",
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
    
</script>

<script>

const memberId = '${memberDetails.getUsername()}';
console.log('member ID:', memberId);

$(document).ready(function() {
	
	 // 페이지 로드 시 기본 배송지 여부 확인
    checkDefaultAddress().then(function(isExistingDefault) {
        
    	 if (isExistingDefault) {
    	        alert("기본 배송지가 설정되어 있습니다.");
    	        
    	        // 기본 배송지가 있는 경우
    	        // 체크박스 상태에 따라 수정 버튼 활성화/비활성화 설정
    	        $('#isDefault').change(function() {
    	            if ($(this).prop('checked')) {
    	                // 체크박스가 선택되면 수정 버튼을 비활성화
    	                $('#updateButton').prop('disabled', true);
    	            } else {
    	                // 체크박스가 비선택되면 수정 버튼을 활성화
    	                $('#updateButton').prop('disabled', false);
    	            }
    	        });
    	        
    	        // 초기 체크박스 상태에 따라 수정 버튼 활성화/비활성화 설정
    	        if ($('#isDefault').prop('checked')) {
    	            $('#updateButton').prop('disabled', true);
    	        } else {
    	            $('#updateButton').prop('disabled', false);
    	        }
    	    } else {
    	        console.log("기본 배송지가 없습니다.");
    	        
    	        // 기본 배송지가 없는 경우 수정 버튼을 항상 활성화
    	        $('#updateButton').prop('disabled', false);
    	    }
    });
});// end document.ready()
	
	
	// 수정 동작을 수행하는 함수 예제
	function performUpdate() {
	    // 여기에 수정 동작을 수행하는 코드를 작성하세요.
	    console.log("수정 동작이 수행되었습니다.");
	}

	// 수정 버튼 클릭 이벤트 핸들러 설정
	$('#updateButton').click(function() {
	    // 수정 동작 수행
	    if (!$(this).prop('disabled')) { // 기본 배송지 설정이 되어있지 않을 때 
	        
	        
	    } else {
	        alert("기본 배송지를 체크한 경우 수정할 수 없습니다.");
	    }
	});
	

	   
    // 페이지 로드 시 기본 배송지 여부 확인
    checkDefaultAddress().then(function(isExistingDefault) {
        
        if (isExistingDefault) {
              
               // 기본 배송지가 있는 경우
               // 체크박스 상태에 따라 수정 버튼 활성화/비활성화 설정
               $('#isDefault').change(function() {
                   if ($(this).prop('checked')) {
                       // 체크박스가 선택되면 수정 버튼을 비활성화
                       $('#updateButton').prop('disabled', true);
                   } else {
                       // 체크박스가 비선택되면 수정 버튼을 활성화
                       $('#updateButton').prop('disabled', false);
                   }
               });
               
               // 초기 체크박스 상태에 따라 수정 버튼 활성화/비활성화 설정
               if ($('#isDefault').prop('checked')) {
                   $('#updateButton').prop('disabled', true);
               } else {
                   $('#updateButton').prop('disabled', false);
               }
           } else {
               console.log("기본 배송지가 없습니다.");
               
               // 기본 배송지가 없는 경우 수정 버튼을 항상 활성화
               $('#updateButton').prop('disabled', false);
           }
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



	//수정 버튼 클릭 시 폼 유효성 검사 후 제출 및 페이지 이동
	
	 $("#updateButton").click(function(event) {
        var receiverName = $("#receiverName").val();
        var receiverPhone = $("#receiverPhone").val();

        // 받는 사람, 휴대폰 번호가 비어있는지 확인
        if (!receiverName || !receiverPhone) {
            alert("받는 사람, 휴대폰 번호는 필수 입력 사항입니다.");
            event.preventDefault(); // 폼 제출 막기
            return;
        }


        // 기본 배송지로 설정되었는지 확인
        var isDefault = $("#isDefault").prop("checked");
        if (isDefault && !receiverName) {
            alert("기본 배송지로 설정하려면 받는 사람 정보가 필요합니다.");
            event.preventDefault(); // 폼 제출 막기
            return;
        }

        for (let x in checkMap) {
            if (!checkMap[x].isValid) {
                alert('유효하지 않은 입력 : ' + x);
                event.preventDefault();
                return;
            }
        }
        
    });


	
	
</script>

</body>
</html>
