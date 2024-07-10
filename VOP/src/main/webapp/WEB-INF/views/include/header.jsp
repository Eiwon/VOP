<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="isAuthenticated()">
	<sec:authentication var="memberDetails" property="principal"/>
	<jsp:include page="../include/alarm.jsp"></jsp:include>
</sec:authorize>
<!DOCTYPE html>
<html>
<head>
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<meta charset="UTF-8">
<style>
	/* 로그인 , 회원가입 링크 -> 우측 정렬 안됨. */
/* 전체적인 스타일링 */
        * {
    margin: 0; /* 마진 초기화 */
    padding: 0; /* 패딩 초기화 */
    box-sizing: border-box; /* 상자 모델 설정 */
}

/* 바디 설정 */
body {
    font-family: Arial, sans-serif; /* 폰트 설정 */
    background-color: #f8f9fa; /* 배경색 지정 */
}

/* 컨테이너 설정 */
.container2 {
    background-color: #5cb8ff; /* 배경색 설정 */
    padding: 30px; /* 안쪽 여백 설정 */
    margin: 5px; /* 바깥 여백 설정 */
}

/* 사용자 링크 컨테이너 설정 */
.container2.user-links2 {
    text-align: right; /* 텍스트 우측 정렬 */
    margin-top: 20px; /* 위 여백 설정 */
    display: flex; /* Flexbox 사용 */
    justify-content: flex-end; /* 아이템 우측 정렬 */
    align-items: center; /* 수직 정렬 */
}

/* 링크 설정 */
.container2.user-links2 a {
    margin-left: 10px; /* 왼쪽 여백 설정 */
    color: black; /* 글자 색상 설정 */
    text-decoration: none; /* 밑줄 제거 */
}

/* 링크 호버 설정 */
.container2.user-links2 a:hover {
    color: black; /* 호버 시 글자 색상 설정 */
}
.user_container {
	display: flex;
	justify-content: flex-end;
}
/* 검색 라인 설정 */
.line_search {
    width: 100%; /* 너비 설정 */
    margin: auto; /* 가운데 정렬 */
}

/* VOP 링크 설정 */
.vop-link {
    margin-right: 50px; /* 오른쪽 여백 설정 */
    margin-bottom: 30px; /* 아래 여백 설정 */
    color: blue; /* 글자 색상 설정 */
    text-decoration: none; /* 밑줄 제거 */
}

/* VOP 링크 호버 설정 */
.vop-link:hover {
    color: darkblue; /* 호버 시 글자 색상 설정 */
}

/* 검색창 컨테이너 설정 */
.search-container {
    display: inline-block; /* 인라인 블록 설정 */
    margin-bottom: 20px; /* 아래 여백 설정 */
}

/* 검색 입력창 설정 */
.search-input {
    padding: 8px 16px; /* 안쪽 여백 설정 */
    margin-right: 10px; /* 오른쪽 여백 설정 */
    border: 1px solid #ccc; /* 테두리 설정 */
    border-radius: 4px; /* 테두리 둥글기 설정 */
}

/* 검색 버튼 설정 */
.search-button {
    padding: 8px 16px; /* 안쪽 여백 설정 */
    margin-right: 50px; /* 오른쪽 여백 설정 */
    background-color: #007bff; /* 배경색 설정 */
    color: #fff; /* 글자 색상 설정 */
    border: none; /* 테두리 없애기 */
    border-radius: 4px; /* 테두리 둥글기 설정 */
    cursor: pointer; /* 커서 설정 */
}

/* 검색 버튼 호버 설정 */
.search-button:hover {
    background-color: #0056b3; /* 호버 시 배경색 변경 */
}

/* 사용자 링크 설정 */
.user-links {
    position: relative; /* 상대적 위치 설정 */
    display: inline-block; /* 인라인 블록 설정 */
    margin-left: 20px; /* 왼쪽 여백 설정 */
}

/* 사용자 링크 아이템 설정 */
.user-links a {
    margin-left: 10px; /* 왼쪽 여백 설정 */
    margin-right: 20px; /* 오른쪽 여백 설정 */
    color: black; /* 글자 색상 설정 */
    text-decoration: none; /* 밑줄 제거 */
}

/* 사용자 링크 아이템 호버 설정 */
.user-links a:hover {
    color: black; /* 호버 시 글자 색상 설정 */
}

/* 서브메뉴 설정 */
.user-links .submenu {
    display: none; /* 표시 안 함 */
    position: absolute; /* 절대 위치 설정 */
    top: 100%; /* 위쪽 여백 설정 */
    left: 0; /* 왼쪽 여백 설정 */
    background-color: #fff; /* 배경색 설정 */
    border: 1px solid #ccc; /* 테두리 설정 */
    padding: 10px; /* 안쪽 여백 설정 */
    box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2); /* 그림자 설정 */
}

/* 서브메뉴 호버 설정 */
.user-links:hover .submenu {
    display: block; /* 표시 설정 */
}

/* 서브메뉴 링크 설정 */
.submenu a {
    display: block; /* 블록 설정 */
    margin-bottom: 5px; /* 아래 여백 설정 */
    text-decoration: none; /* 밑줄 제거 */
    color: #333; /* 글자 색상 설정 */
}

/* 서브메뉴 링크 호버 설정 */
.submenu a:hover {
    background-color: #f0f0f0; /* 호버 시 배경색 변경 */
}

/* 맨 위로 버튼 설정 */
#btnTop {
    position: fixed; /* 고정 위치 설정 */
    bottom: 20px; /* 아래 여백 설정 */
    right: 20px; /* 오른쪽 여백 설정 */
    width: 80px; /* 너비 설정 */
    height: 40px; /* 높이 설정 */
    font-size: 16px; /* 글꼴 크기 설정 */
    cursor: pointer; /* 커서 설정 */
}

/* 주요 버튼 설정 */
.btn-primary {
    background-color: #007bff; /* 배경색 설정 */
    color: #fff; /* 글자 색상 설정 */
    border: none; /* 테두리 없애기 */
    border-radius: 4px; /* 테두리 둥글기 설정 */
}

/* 주요 버튼 호버 설정 */
.btn-primary:hover {
    background-color: #0056b3; /* 호버 시 배경색 변경 */
}

/* 카테고리 서브메뉴 설정 */
.subcategory {
    display: none; /* 표시 안 함 */
    position: absolute; /* 절대 위치 설정 */
    background-color: #fff; /* 배경색 설정 */
    box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2); /* 그림자 설정 */
    z-index: 1; /* Z 인덱스 설정 */
}

/* 버튼 그룹 호버 설정 */
.btn-group:hover .subcategory {
    display: block; /* 표시 설정 */
}

/* 드롭다운 아이템 설정 */
.dropdown-item {
    display: block; /* 블록 설정 */
    width: 100%; /* 너비 설정 */
    padding: .5rem 1rem; /* 안쪽 여백 설정 */
    clear: both; /* 모두 지우기 */
    font-weight: 400; /* 글꼴 두께 설정 */
    color: #212529; /* 글자 색상 설정 */
    text-align: inherit; /* 텍스트 정렬 설정 */
    white-space: nowrap; /* 공백 처리 방법 설정 */
    background-color: transparent; /* 배경색 설정 */
    border: 0; /* 테두리 없애기 */
}

/* 드롭다운 아이템 호버 및 포커스 설정 */
.dropdown-item:hover,
.dropdown-item:focus {
    color: #16181b; /* 호버 시 글자 색상 설정 */
    text-decoration: none; /* 밑줄 제거 */
    background-color: #f8f9fa; /* 호버 시 배경색 변경 */
}

/* 상품 박스 설정 */
.product_box {
    border: 1px solid black; /* 테두리 설정 */
    width: 200px; /* 너비 설정 */
}

    </style>
</head>
<body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<div class="container2">

    <div class="user_container" id="box_login">
    	<sec:authorize access="isAuthenticated()">
    		<div style="font-size: 10px;">
    			${memberDetails.username } 님 환영합니다<br>
    			권한 : ${memberDetails.authorities[0].authority.substring(5) }
    		</div>
    		<form action="../member/logout" method="POST">
    			<input type="submit" value="로그아웃">
    			<sec:csrfInput/>
    		</form>
    	</sec:authorize>
    	<sec:authorize access="isAnonymous()">
    		<a href="../member/login" style="margin-right: 10px;">로그인</a>
        	<a href="../member/register">회원가입</a>
    	</sec:authorize>
    </div>
	<div class="line_search">
	
	<!-- VOP 링크 추가 -->
    <a href="../board/main" class="vop-link">
    <img src="${pageContext.request.contextPath}/resources/vop.png">
	</a>
	
	<div class="btn-group maincategory" role="group">
    	<button type="button" class="btn btn-primary dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
      		카테고리
   	 	</button>
    	<ul class="subcategory dropdown-menu">
     	 	<li><a class="dropdown-item" href="">여성패션</a></li>
            <li><a class="dropdown-item" href="">남성패션</a></li>
            <li><a class="dropdown-item" href="">남녀 공용 의류</a></li>
            <li><a class="dropdown-item" href="">유아동 패션</a></li>
            <li><a class="dropdown-item" href="">뷰티</a></li>
            <li><a class="dropdown-item" href="">출산/유아동</a></li>
            <li><a class="dropdown-item" href="">식품</a></li>
            <li><a class="dropdown-item" href="">주방용품</a></li>
            <li><a class="dropdown-item" href="">생활용품</a></li>
            <li><a class="dropdown-item" href="">홈인테리어</a></li>
            <li><a class="dropdown-item" href="">가전디지털</a></li>
            <li><a class="dropdown-item" href="">스포츠/레저</a></li>
            <li><a class="dropdown-item" href="">자동차 용품</a></li>
            <li><a class="dropdown-item" href="">도서/음반/DVD</a></li>
            <li><a class="dropdown-item" href="">완구/취미</a></li>
            <li><a class="dropdown-item" href="">문구/오피스</a></li>
            <li><a class="dropdown-item" href="">반려동물용품</a></li>
            <li><a class="dropdown-item" href="">헬스/건강식품</a></li>
   	 	</ul>
    </div>
	
    <!-- 카테고리 드롭다운 -->
    <div class="search-container">
          <!-- 카테고리 선택 드롭다운 -->
          <select id="boxCategory">
            <option value="전체">전체</option>
            <option value="여성패션">여성패션</option>
            <option value="남성패션">남성패션</option>
            <option value="남녀 공용 의류">남녀 공용 의류</option>
            <option value="유아동 패션">유아동 패션</option>
            <option value="뷰티">뷰티</option>
            <option value="출산/유아동">출산/유아동</option>
            <option value="식품">식품</option>
            <option value="주방용품">주방용품</option>
            <option value="생활용품">생활용품</option>
            <option value="홈인테리어">홈인테리어</option>
            <option value="가전디지털">가전디지털</option>
            <option value="스포츠/레저">스포츠/레저</option>
            <option value="자동차 용품">자동차 용품</option>
            <option value="도서/음반/DVD">도서/음반/DVD</option>
            <option value="완구/취미">완구/취미</option>
            <option value="문구/오피스">문구/오피스</option>
            <option value="반려동물용품">반려동물용품</option>
            <option value="헬스/건강식품">헬스/건강식품</option>
          </select> 
          
        <!-- 검색 입력창 -->
        <input type="text"class="search-input" placeholder="검색어를 입력하세요">
        <!-- 검색 버튼 -->
        <button class="search-button">검색</button>
		
		<!-- 페이지 맨 위로 가기 버튼 -->
		<button id="btnTop" style="position: fixed; bottom: 20px; right: 20px; width: 80px; height: 40px; font-size: 16px;">TOP</button>
		
    </div>
    
	<!-- 마이페이지(mypage), 장바구니(basket) 링크 -->
		<div class="user-links">
    		<a href="../board/mypage"><i class="fas fa-user-circle"></i> 마이페이지</a>
    		<div class="submenu">
        		<a href="../order/orderlist"><i class="fas fa-list-alt"></i> 주문목록</a>
    		</div>
    		<a href="../board/basket"><i class="fas fa-shopping-cart"></i> 장바구니</a>
		</div>
    </div>
</div>

<script>

	document.querySelector('.vop-link').addEventListener('click', function() {
		location.reload();
	});

    $(document).ready(function(){
    	
    	// 페이지 맨 위로 가기 버튼 클릭 이벤트
        $('#btnTop').click(function() {
            window.scrollTo(0, 0); // 페이지 맨 위로 즉시 이동
        });

        // 검색 버튼 클릭 이벤트
        $('.search-button').click(function() {
            search(); // search 함수 호출
        });

        // 검색 입력창에서 엔터 키 이벤트
        $('.search-input').keydown(function(event) {
            if (event.keyCode == 13) { // Enter 키의 keyCode는 13
                search(); // search 함수 호출
            }
        });

        // 카테고리 드롭다운 메뉴 항목 처리
        $('.subcategory').find('.dropdown-item').each(function() {
            let link = "../product/search?category=" + $(this).text() + "&word=";
            $(this).attr('href', link); // 각 항목의 href 속성에 링크 추가
        });
		
    }); // end document.ready
    
    function search(){
		let category = $('#boxCategory').val();
		let word = $('.search-input').val();
		if(category == '전체' && word.length == 0){
			return;
		}
		location.href = "../product/search?category=" + category + "&word=" + word;
	} // end search
</script>
</body>
</html>