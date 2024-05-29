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
<meta charset="UTF-8">
<style>
	/* 로그인 , 회원가입 링크 -> 우측 정렬 안됨. */
		.container2 {
		    background-color: #f0f0f0;
		    width: 1800px;
		    padding: 30px;
		    margin: 5px;
		    
		}

		.container2.user-links2 {
		    margin-top: 20px;
		}
		
		.container2.user-links2 a {
		    margin-left: 10px;
		    margin-right: 0px; /*우측 여백 제거*/
		    text-align: right; /* 로그인과 회원가입 text를 우측 정렬 */
		    color: black;
		    text-decoration: none;
		    margin-left: 320px; /* 왼쪽 여백 추가 */
		}
		
		.container2.user-links2 a:hover {
		    color: black;
		}
		.container {
            text-align: center; /* 중앙 정렬 */
        }
	
        /*카테고리*/
        .category {
            position: relative;
            display: inline-block;
            border: 1px solid black; /*박스 추가*/
            width: 70px; /* 박스 너비 */
    		height: 20px; /* 박스 높이 */
            padding: 10px; /*박스 내부 padding 추가*/
            background-color: lightgreen; /*박스 색상 추가*/
            margin-top: 80px; /* 상단 여백 추가*/
            margin-bottom: 20px; /* 위 아래 여백 추가 */
            margin-right: 50px; /* 오른쪽 여백 추가 */
            margin-left: 320px; /* 왼쪽 여백 추가 */
        }
        
        .category:hover .subcategory {
            display: block; /* 부모 요소에 마우스를 올리면 서브 카테고리 표시 */
        }
        
        .subcategory {
           	display: none; /* 마우스 up 아닐땐 숨김 */
            position: absolute;
            top: 100%;
            left: 0;
            right: 10;
            background-color: #f9f9f9;
            padding: 10px;
            border: 1px solid #ccc;
            z-index: 1;
            font-size: 14px; 
        }
        
        .subcategory a {
            display: block;
            color: #333;
            text-decoration: none;
            padding: 5px 0;
        }
        
        .subcategory a:hover {
            background-color: #ddd;
        }
        
        
        
        
         /* VOP 링크 스타일링 */
        .vop-link {
            
            margin-right: 50px;
            margin-bottom: 30px; /* 위 아래 여백 추가 */
            color: blue; /* 링크 색상 지정 */
            text-decoration: none; /* 밑줄 제거 */
        }

        /* VOP 링크 호버 스타일링 */
        .vop-link:hover {
            color: darkblue; /* 호버 시 색상 변경 */
        }
        
         /* 검색창 스타일링 */
        .search-container {
            display: inline-block;
            margin-bottom: 20px; /* 위 아래 여백 추가 */
            
        }

        /* 검색 입력창 스타일링 */
        .search-input {
            padding: 8px 16px; /* 내부 여백 설정 */
            margin-right: 10px; /* 오른쪽 여백 설정 */
            border: 1px solid #ccc; /* 테두리 설정 */
            border-radius: 4px; /* 테두리 둥글게 설정 */
        }
        
        /* 검색 버튼 스타일링 */
        .search-button {
            padding: 8px 16px; /* 내부 여백 설정 */
            margin-right: 50px;
            background-color: #007bff; /* 배경색 지정 */
            color: #fff; /* 글자색 지정 */
            border: none; /* 테두리 없애기 */
            border-radius: 4px; /* 테두리 둥글게 설정 */
            cursor: pointer; /* 포인터 커서 설정 */
        }

        /* 검색 버튼 호버 스타일링 */
        .search-button:hover {
            background-color: #0056b3; /* 호버 시 배경색 변경 */
        }
        
        /* 마이페이지, 장바구니 링크 스타일링 */
	    .user-links {
	        display: inline-block;
	        margin-left: 20px; /* 검색창과 간격 조정 */
	    }
	
	    .user-links a {
	        margin-left: 10px; /* 링크들 간의 간격 조정 */
	        margin-right: 20px;
	        color: black;
	        text-decoration: none;
	    }
	
	    .user-links a:hover {
	        color: black;
	    }
        
        .flex_list {
        	display: flex;
    		flex-direction: row;
    		list-style: none;
        }
        
        .product_box {
        	border: 1px solid black;
        	width: 200px;
        }
        
        /* 마이페이지 - 하위 메뉴 숨김 상태로 설정*/
        
        /* 부모 링크에 position: relative; 스타일 적용 */
		.user-links {
		    position: relative;
		}
		
		/* 서브 메뉴에 position: absolute; 스타일 적용 */
		.submenu {
		    display: block; /* 줄 바꿈 */
		    position: absolute;
		    top: 100%;
		    left: 0;
		    background-color: #fff; /* 배경색 */
		    border: 1px solid #ccc; /* 테두리 */
		    padding: 10px;
		    box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2); /* 그림자 효과 */
		}
		
		/* 부모 링크에 호버 시 서브 메뉴 표시 */
		.user-links:hover .submenu {
		    display: block;
		}
		
		.user-links .submenu {
		    display: none;
		    position: absolute;
		    /* 다른 스타일 속성들 */
		    /* 필요한 경우에 추가적인 스타일 지정 */
		}
		
		/* 서브 메뉴 항목 스타일링 */
		.submenu a {
		    display: block;
		    margin-bottom: 5px; /* 항목 간격 조정 */
		    text-decoration: none; /* 밑줄 제거 */
		    color: #333; /* 글자색 지정 */
		}
		
		/* 서브 메뉴 항목 호버 스타일링 */
		.submenu a:hover {
		    background-color: #f0f0f0; /* 호버 시 배경색 변경 */
		}
		
    </style>
</head>
<body>
<div class="container2">
    <div class="user-links2" id="box_login">
        <a href="../member/login">로그인</a>
        <a href="../member/register">회원가입</a>
    </div>
	
	<div class="category">
        <span>카테고리</span>
        <div class="subcategory">
            <a href="#">패션의류/잡화</a>
            <a href="#">뷰티</a>
            <a href="#">출산/유아동</a>
            <a href="#">식품</a>
            <a href="#">주방용품</a>
            <a href="#">생활용품</a>
            <a href="#">홈인테리어</a>
            <a href="#">가전디지털</a>
            <a href="#">스포츠/레저</a>
            <a href="#">자동차 용품</a>
            <a href="#">도서/음반/DVD</a>
            <a href="#">완구/취미</a>
            <a href="#">문구/오피스</a>
            <a href="#">반려동물용품</a>
            <a href="#">헬스/건강식품</a>
        </div>
	</div>
		
	<!-- VOP 링크 추가 -->
    <a href="../board/main" class="vop-link">VOP</a>
    
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
        <input type="text" id="searchInput" class="search-input" placeholder="검색어를 입력하세요">
        <!-- 검색 버튼 -->
        <button id="searchButton" class="search-button">검색</button>
		
		<!-- 페이지 맨 위로 가기 버튼 -->
		<button id="btnTop" style="position: fixed; bottom: 20px; right: 20px; width: 80px; height: 40px; font-size: 16px;">TOP</button>
		
    </div>
    
	<!-- 마이페이지(mypage), 장바구니(basket) 링크 -->
    <div class="user-links">
        	<a href="../board/mypage">마이페이지</a>
        	<div class="submenu">
        		<a href="../order/orderlist">주문목록</a>
        		<a href="#">취소/반품</a>
        		<a href="#">찜리스트</a>
        	</div>	
        	<a href="../board/basket">장바구니</a>
    </div>
</div>

<script>

	document.querySelector('.vop-link').addEventListener('click', function() {
		location.reload();
	});

    $(document).ready(function(){
    	setLoginBox();
    	
        // 페이지 맨 위로 가기 버튼 클릭 이벤트
        $('#btnTop').click(function(){
            $('html, body').animate({scrollTop: 0}, 'slow'); // 스크롤을 맨 위로 부드럽게 이동
        });
        
        $('.search-button').click(function(){ // 검색 리스너 등록
		    location.href = "../product/search?category=" + $('#boxCategory').val() + "&word=" + $('#searchInput').val();
		}); // end btnSearch.click
        
		
    }); // end document.ready
    
    function setLoginBox(){
		let memberDetails = '${memberDetails}';
		console.log('현재 로그인 memberDetails : ' + memberDetails);
		let memberId = null;
		let memberAuth = null;
		
		if(memberDetails != 'anonymousUser'){
			memberId = '${memberDetails.memberVO.memberId }';
			memberAuth = '${memberDetails.memberVO.memberAuth}';
		}
		
		console.log('security memberId : ' + memberId);
		console.log('security memberAuth : ' + memberAuth);
		let form = '';
		
		if(memberId == '') { // 로그인 상태가 아닐 경우
			form = '<a href="../member/login">로그인</a>&nbsp&nbsp&nbsp' + 
				'<a href="../member/register">회원가입</a>';
		}else {
			form = '<form action="../member/logout" method="POST"><input type="submit" value="로그아웃"></form>';
		}
		$('#box_login').html(form);
	} // end setLoginBox
</script>
</body>
</html>