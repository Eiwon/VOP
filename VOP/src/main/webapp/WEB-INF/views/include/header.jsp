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
		.line_search {
			width: 65%;
			margin: auto;
		}
    </style>
</head>
<body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<div class="container2">

    <div class="user-links2" id="box_login">
    	<sec:authorize access="isAuthenticated()">
    		<div>
    			${memberDetails.username } 님 환영합니다. <br>
    			${memberDetails.username } 님의 현재 권한 : ${memberDetails.authorities[0].authority.substring(5) }
    		</div>
    		<form action="../member/logout" method="POST">
    			<input type="submit" value="로그아웃">
    			<sec:csrfInput/>
    		</form>
    	</sec:authorize>
    	<sec:authorize access="isAnonymous()">
    		<a href="../member/login">로그인</a>
        	<a href="../member/register">회원가입</a>
    	</sec:authorize>
    </div>
	<div class="line_search">
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
        <input type="text"class="search-input" placeholder="검색어를 입력하세요">
        <!-- 검색 버튼 -->
        <button class="search-button">검색</button>
		
		<!-- 페이지 맨 위로 가기 버튼 -->
		<button id="btnTop" style="position: fixed; bottom: 20px; right: 20px; width: 80px; height: 40px; font-size: 16px;">TOP</button>
		
    </div>
    
	<!-- 마이페이지(mypage), 장바구니(basket) 링크 -->
    <div class="user-links">
        	<a href="../board/mypage">마이페이지</a>
        	<div class="submenu">
        		<a href="../order/orderlist">주문목록</a>
        		
        		<!-- <a href="#">취소/반품</a> -->
        		<!-- <a href="#">찜리스트</a> -->

        	</div>	
        	<a href="../board/basket">장바구니</a>
    </div>
    </div>
</div>

<script>

	document.querySelector('.vop-link').addEventListener('click', function() {
		location.reload();
	});

    $(document).ready(function(){
    	
        // 페이지 맨 위로 가기 버튼 클릭 이벤트
        $('#btnTop').click(function(){
            $('html, body').animate({scrollTop: 0}, 'slow'); // 스크롤을 맨 위로 부드럽게 이동
        });
        
        $('.search-button').click(function(){
        	search();
        }); // end btnSearch.click
		
		$('.search-input').keydown(function(event){
			if(event.keyCode == 13){ // enter 키 코드가 13
				search();
			}
		}); // end search.keydown
		
		$('.subcategory').find('.dropdown-item').each(function(){
			let link = "../product/search?category=" + $(this).text() + "&word=";
			$(this).attr('href', link);
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