<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
 <meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- jquery 라이브러리 import -->
<script src="https://code.jquery.com/jquery-3.7.1.js">
</script>
<title>VOP</title>

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
	</style>
	
	<style >
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
            display: block;
        }
        
        .subcategory {
            display: none;
            position: absolute;
            top: 100%;
            left: 0;
            right: 10;
            background-color: #f9f9f9;
            padding: 10px;
            border: 1px solid #ccc;
            z-index: 1;
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
        
        
        
    </style>
</head>
<body>

	<div class="container2">
    <div class="user-links2">
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
    <a href="main" class="vop-link">VOP</a>
    
    <!--  JavaScript를 사용하여 페이지를 새로고침 -->
    <script>
    	document.querySelector('.vop-link').addEventListener('click', function() {
        	location.reload();
   	 });
	</script>
    
    <!-- 검색창 추가 -->
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
        <input type="text" class="search-input" placeholder="검색어를 입력하세요">
        <!-- 검색 버튼 -->
        <button class="search-button">검색</button>
    </div>
		<!-- 마이페이지(mypage), 장바구니(basket) 링크 -->
    	<div class="user-links">
        	<a href="mypage">마이페이지</a>
        	<a href="basket">장바구니</a>
    </div>
</div>

	<script type="text/javascript">
		let boxCategory = $('#boxCategory');
		let inputSearch = $('.search-input');
		let btnSearch = $('.search-button');
		
		btnSearch.click(function(){
			console.log("선택된 카테고리 : " + boxCategory.val());
			console.log("입력된 검색어 : " + inputSearch.val());
			
			location.href = "../product/search?category=" + boxCategory.val() + "&word=" + inputSearch.val();
			
		}); // end btnSearch.click
	
	</script>
	

</body>
</html>