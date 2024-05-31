<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<sec:authorize access="isAuthenticated()">
	<sec:authentication var="memberDetails" property="principal" />
</sec:authorize>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<jsp:include page="../include/header.jsp"></jsp:include>
<title>리뷰 전체 검색</title>
<style>
/* 박스 스타일 */
.product-box {
	padding: 20px;
	margin: 20px 0;
	border-radius: 10px;
	background-color: #f9f9f9;
}

.product-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
}

.product-info {
	flex-grow: 1;
}

.product-image {
	max-width: 150px;
	margin-right: 20px;
}

.review-content {
	margin-top: 10px;
	padding-top: 10px;
	border-top: 1px solid #ccc;
}

.review-content p {
	margin: 5px 0;
}

/* 리뷰 별 폼 스타일 */
.stars {
	display: inline-block;
	direction: ltr;
	border: 0;
}

/* 별표시 스타일 */
.stars label {
	font-size: 1em;
	color: transparent;
	text-shadow: 0 0 0 #f0f0f0;
}
</style>
</head>
<body>

	<p>댓글 리스트</p>

	<c:forEach var="ProductVO" items="${productList}">
		<div class="product-box">
			<div class="product-header">
				<div class="product-info">
					<!-- 리뷰 수정 코드 -->
					<form action="../review/modify" method="get">
						<input type="hidden" name="productId"
							value="${ProductVO.productId}"> <input type="hidden"
							name="imgId" value="${ProductVO.imgId}"> <input
							type="hidden" name="memberId"
							value="${memberDetails.getUsername()}">
						<button type="submit">리뷰 수정</button>
					</form>
					<!-- 리뷰 삭제 동기 코드 -->
					<!--<form action="../review/delete" method="POST">
						<input type="hidden" name="productId"
							value="${ProductVO.productId}"> <input type="hidden"
							name="memberId" value="${memberDetails.getUsername()}">
						<button type="submit">리뷰 동기 삭제</button>
					</form> -->
					
					<button class="btnDelete" data-productid="${ProductVO.productId}">리뷰 삭제</button>
					
					<h2>상품 이름 : ${ProductVO.productName}</h2>
				</div>
				<img alt="${ProductVO.imgId}">
			</div>
			<div class="review-content">
				<c:forEach var="ReviewVO" items="${reviewList}">
					<c:if test="${ReviewVO.productId == ProductVO.productId}">
						<p>리뷰 : ${ReviewVO.reviewStar}</p>
						<!-- 리뷰 별점 표시 -->
						<div class="stars" data-reviewavg="${ReviewVO.reviewStar}">
							<fieldset>
								<label for="star5">&#9733;</label> <label for="star4">&#9733;</label>
								<label for="star3">&#9733;</label> <label for="star2">&#9733;</label>
								<label for="star1">&#9733;</label>
							</fieldset>
						</div>
						<p>리뷰 내용 : ${ReviewVO.reviewContent}</p>
					</c:if>
				</c:forEach>
			</div>
		</div>
	</c:forEach>

	<script type="text/javascript">
	
		let memberId = "${memberDetails.getUsername()}";
	
		$(document).ready(function() {
			// 이미지 로드 함수 호출
			loadImg();

			// 별표시를 업데이트하는 함수 호출
			displayStars();

			// 이미지가 없을 때 이름과 확장명을 보여주는 코드
			function loadImg() {
				$('img').each(function() {
					let target = $(this);
					let imgId = target.attr("alt");
					$.ajax({
						method : 'GET',
						url : '../image/' + imgId,
						success : function(result) {
							target.attr('src', result);
						}
					}); // end ajax
				});
			}// end loadImg

			// 별표시를 업데이트하는 함수
			function displayStars() {
				$('.stars').each(function() {
					let value = parseInt($(this).attr('data-reviewavg')); // 리뷰 별점을 정수 형으로 변환
					let stars = $(this).find('label'); // 별 표시 요소 가져오기
					for (let i = 0; i < stars.length; i++) {
						if (i < value) {
							stars.eq(i).css('color', '#f0d000'); // 선택된 별보다 작은 값의 별은 노란색으로 표시
						} else {
							stars.eq(i).css('color', 'transparent'); // 선택된 별보다 큰 값의 별은 투명하게 표시
						}
					}
				});
			}// end displayStars()
			
			// .btnDelete 클릭 이벤트를 동적으로 바인딩
			$(document).on('click', '.btnDelete', function() {
		        let productId = $(this).data('productid'); // productId 가져오기

		        let obj = {
		            'productId': productId,
		            'memberId': memberId
		        }
		        
		        // ajax 요청
		        $.ajax({
		           type : 'DELETE', // 메서드 타입
		           url : '../review/delete',// 경로 
		           headers : {
		              'Content-Type' : 'application/json' // json content-type 설정
		           }, // 'Content - Type' : application/json; 헤더 정보가 안들어가면 405 에러가 나온다.
		           data : JSON.stringify(obj), // JSON으로 변환
		           success : function(result) { // 전송 성공 시 서버에서 result 값 전송
		              if(result == 1) {
		                 alert('댓글 삭제 성공!');
		              } else {
		            	alert('댓글 삭제 실패!');
		              }
		           }
		        });// end ajax()
		    }); // end .btnDelete click

		 }); // end document.ready()
			
	</script>

</body>
</html>
