<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<title>상품 검색</title>
</head>
<body>
	
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
    
    
	<div id="searchResult">
		<c:forEach var="productVO" items="${productList}">
			<div>
					<div class="productId">${productVO.productId }</div>
					<div>${productVO.productName }</div>
					<div>${productVO.productPrice }</div>
					<div>${productVO.reviewNum }</div>
					<div>${productVO.productRemains }</div>
					<div>${productVO.productPlace }</div>
					<div>${productVO.category }</div>
					<img alt="등록된 이미지가 없습니다." 
					src="showImg?productId=${productVO.productId }">
			</div>
		
		</c:forEach>
	</div>   
	<script type="text/javascript">
		let boxCategory = $('#boxCategory');
		let inputSearch = $('.search-input');
		let btnSearch = $('.search-button');
		
		btnSearch.click(function(){
			console.log("선택된 카테고리 : " + boxCategory.val());
			console.log("입력된 검색어 : " + inputSearch.val());
			
			location.href = "search?category=" + boxCategory.val() + "&word=" + inputSearch.val();
			
		}); // end btnSearch.click
	
	</script>
</body>
</html>