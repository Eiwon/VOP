
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!-- 시큐리티 회원id 관련 코드 -->
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
<title>문의 리스트</title>
<style type="text/css">
#pageSelector {
		list-style: none;
		flex-direction: row;
		display: flex;
	}
</style>
</head>
<body>
	<table>
		<tbody>
			<c:choose>
				<c:when test="${not empty listInquiry}">
					<c:forEach var="InquiryVO" items="${listInquiry}">
						<tr>
							<td>문의ID : ${InquiryVO.inquiryId}</td>
							<td>회원ID : ${InquiryVO.memberId}</td>
							<td>상품ID : ${InquiryVO.productId}</td>
							<td>문의 내용 : ${InquiryVO.inquiryContent}</td>
							<td><fmt:formatDate value="${InquiryVO.inquiryDateCreated}"
									pattern="yyyy-MM-dd HH:mm:ss" var="inquiryDateCreated" />
								${inquiryDateCreated}</td>
							<td>
								<!-- 수정: 클래스 이름을 btnAdd로 변경 -->
								<button class="btnAdd" data-inquiryid="${InquiryVO.inquiryId}"
									data-productid="${InquiryVO.productId}">답글 작성</button> <!-- 수정: 클래스 이름을 btnModify로 변경 -->
								<button class="btnModify"
									data-inquiryid="${InquiryVO.inquiryId}">답글 수정</button>
								<!-- style="display:none;" 화면에서 감추는 코드 --> <!-- 수정: 클래스 이름을 btnDelete로 변경 -->
								<button class="btnDelete"
									data-inquiryid="${InquiryVO.inquiryId}">답글 삭제</button>
							</td>
						</tr>
						<tr>
							<td colspan="5">
								<!-- 답글 작성 -->
								<div id="inputContainer_${InquiryVO.inquiryId}"
									class="inputContainer"
									style="text-align: center; margin-top: 10px; display: none;">
									<!-- 입력 필드와 제출 버튼이 추가될 위치 -->
									<input type="text" id="replyAnswer_${InquiryVO.inquiryId}"
										placeholder="답글 작성">
									<button class="btnSubmit"
										data-inquiryid="${InquiryVO.inquiryId}"
										data-productid="${InquiryVO.productId}">작성</button>
								</div> <!-- 답글 수정 -->
								<div id="modifyContainer_${InquiryVO.inquiryId}"
									class="modifyContainer"
									style="text-align: center; margin-top: 10px; display: none;">
									<!-- 수정 필드와 제출 버튼이 추가될 위치 -->
									<input type="text" id="modifyAnswer_${InquiryVO.inquiryId}"
										placeholder="답글 수정">
									<button class="btnModifySubmit"
										data-inquiryid="${InquiryVO.inquiryId}">수정</button>
								</div>
							</td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr>
						<td colspan="5"><p>문의 사항이 없습니다.</p></td>
					</tr>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>
	
	<!-- 페이징 처리 코드 -->
	<div id="pageSelector">
		<c:if test="${pageMaker.isPrev() }">
			<li><a
				href="../inquiry/list?productId=${productId }&pageNum=${pageMaker.startNum - 1}">이전</a></li>
		</c:if>
		<c:forEach begin="${pageMaker.startNum }" end="${pageMaker.endNum }"
			var="num">
			<li><a
				href="../inquiry/list?productId=${productId }&pageNum=${num }">${num }</a></li>
		</c:forEach>
		<c:if test="${pageMaker.isNext() }">
			<li><a
				href="../inquiry/list?productId=${productId }&pageNum=${pageMaker.endNum + 1}">다음</a></li>
		</c:if>
	</div>

	<script type="text/javascript">
		// 코드 작성 예정
		$(document).ready(
				
				function() {
					
					let productId = ${productId};

					const memberId = '${memberDetails.getUsername()}';

					// 답글 작성 클릭 했을 때 화면띄어주는 역할
					$('.btnAdd').click(function() {
						let inquiryId = $(this).data('inquiryid');

						// 해당 inquiryId에 대한 inputContainer를 toggle합니다.
						$('#inputContainer_' + inquiryId).toggle();
						// 해당 inquiryId에 대한 modifyContainer와 deleteContainer는 숨깁니다.
						$('#modifyContainer_' + inquiryId).hide();
					});

					// 답글 작성 클릭 했을 때 화면띄어주는 역할
					$('.btnModify').click(function() {
						let inquiryId = $(this).data('inquiryid');

						// 해당 inquiryId에 대한 inputContainer를 toggle합니다.
						$('#modifyContainer_' + inquiryId).toggle();
						$('#inputContainer_' + inquiryId).hide();
					});

					// 답글 작성 비동기 코드
					$('.btnSubmit').click(
							function() {
								let answerContent = $(
										'#replyAnswer_'
												+ $(this).data('inquiryid'))
										.val();
								let inquiryId = $(this).data('inquiryid');
								let productId = $(this).data('productid');

								console.log("productId : " + productId);

								let obj = {
									'inquiryId' : inquiryId,
									'productId' : productId,
									'memberId' : memberId,
									'answerContent' : answerContent
								}
								console.log(obj);

								// ajax 요청
								$.ajax({
									type : 'POST',
									url : '../answer/register',
									headers : {
										'Content-Type' : 'application/json'
									},
									data : JSON.stringify(obj),
									success : function(result) {
										if (result == 1) {
											alert('답변 성공');
										} else {
											alert('답변을 이미 작성 하셨습니다.');
										}
									}
								});
							});

					// 답변 수정 비동기 코드
					$('.btnModifySubmit').click(
							function() {
								let answerContent = $(
										'#modifyAnswer_'
												+ $(this).data('inquiryid'))
										.val();
								let inquiryId = $(this).data('inquiryid');

								let obj = {
									'inquiryId' : inquiryId,
									'memberId' : memberId,
									'answerContent' : answerContent
								}
								console.log(obj);

								// ajax 요청
								$.ajax({
									type : 'PUT',
									url : '../answer/modify',
									headers : {
										'Content-Type' : 'application/json'
									},
									data : JSON.stringify(obj),
									success : function(result) {
										if (result == 1) {
											alert('답변 수정 성공!');
										} else {
											alert('답변이 작성 되어 있지 않습니다.');
										}
									}
								});
							});

					// 답글 삭제 비동기 코드
					$('.btnDelete').click(function() {
						let inquiryId = $(this).data('inquiryid');
						let obj = {
							'inquiryId' : inquiryId,
							'memberId' : memberId
						}

						// ajax 요청
						$.ajax({
							type : 'DELETE',
							url : '../answer/delete',
							headers : {
								'Content-Type' : 'application/json'
							},
							data : JSON.stringify(obj),
							success : function(result) {
								if (result == 1) {
									alert('답변 삭제 성공!');
								} else {
									alert('답변이 삭제 실패!');
								}
							}
						});
					});
				}); // end document
	</script>
</body>
</html>
