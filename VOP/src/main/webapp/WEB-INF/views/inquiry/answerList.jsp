<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<sec:authorize access="isAuthenticated()">
    <sec:authentication var="memberDetails" property="principal" />
</sec:authorize>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="${_csrf.parameterName}" content="${_csrf.token}">
<title>답변 리스트</title>
<jsp:include page="../include/header.jsp"></jsp:include>
<jsp:include page="../include/sideBar.jsp"></jsp:include>
</head>
<body>

	<h3>답변 내용</h3>
       <div class="content-center">
            <div id="comments"></div>
       </div>
     <div class="pagination" id="comments_list_page"></div>
        
<script type="text/javascript">
$(document).ready(function() { 
	
	// 문의(대댓글) 
    inquiryMap.show = function(page) {
        let inquiryUrl = '../inquiryRest/list/' + productId + '/' + page;// 문의 데이터 API 엔드포인트
        let answerUrl = '../answer/list/' + productId; // 답변 데이터 API 엔드포인트
        
        let inquiryNUM = []; // 문의 데이터 배열
        let answerNUM = []; // 답변 데이터 배열
        
        // inquiry 데이터 가져오기
        $.ajax({
            method: 'GET',
            url: inquiryUrl,
            success: function(data) {
             	// reviewMap 초기화
                inquiryMap.inquiryList = data.inquiryList || [];
                inquiryMap.pageMaker = data.pageMaker || null;

                inquiryNUM = inquiryMap.inquiryList;  // 성공적으로 데이터를 가져오면 inquiryNUM에 저장
                console.log("inquiryNUM : " + inquiryNUM);
                processComments();  // 데이터를 가져왔으므로 처리 함수 호출
                $('#comments_list_page').html(inquiryMakePageForm(inquiryMap));
            },
        });
       
        // answer 데이터 가져오기
        $.ajax({
            method: 'GET',
            url: answerUrl,
            success: function(data) {
                answerNUM = data;   // 성공적으로 데이터를 가져오면 answerNUM에 저장
                console.log("answerNUM : " + answerNUM);
                processComments();  // 데이터를 가져왔으므로 처리 함수 호출
            },
        });

        // 문의와 답변 데이터를 처리하는 함수
        function processComments() {// 여기 비동기 동작 어떻게 처리 하는지 잘모르겠음
            // inquiryNUM과 answerNUM이 모두 데이터를 가져온 경우에만 처리
            // 비동기로 리스트를 가져왔을때 두 데이터가 있는지 확인 하는 코드이다.
            /* if (inquiryNUM.length > 0 && answerNUM.length > 0) { */
            if (inquiryNUM.length > 0) {
                // 데이터를 비교하여 일치하는 요소들을 찾음
                let matchingItems = printMatchingItems(inquiryNUM, answerNUM);// 그럼 여기서 함수가 실행 된 다음 변수에 저장?
                renderComments(matchingItems);  // 일치하는 요소들을 렌더링 함수로 전달하여 출력
            }
        }

        // 문의와 답변 데이터를 비교하여 일치하는 요소들을 반환하는 함수
        function printMatchingItems(inquiryNUM, answerNUM) {
            let result = [];
            // 모든 문의와 답변 데이터를 비교하여 일치하는 경우를 찾음
            for (let i = 0; i < inquiryNUM.length; i++) {// 모든 데이터를 꺼내는 작업
                let matchingAnswers = []; // 현재 문의에 대한 일치하는 답변들을 저장할 배열

                for (let j = 0; j < answerNUM.length; j++) {// 모든 데이터를 꺼내는 작업
                    if (inquiryNUM[i].inquiryId === answerNUM[j].inquiryId) {// 여기서 페이징 한 정보에 맞는 정보만 불려온다. 
                        // 일치하는 경우 matchingAnswers 배열에 객체로 저장
                        matchingAnswers.push({
                            answerId: answerNUM[j].answerId,
                            memberId: answerNUM[j].memberId,
                            answerContent: answerNUM[j].answerContent,
                            answerDateCreated: answerNUM[j].answerDateCreated
                        });
                    }
                }

                // 문의와 해당하는 모든 답변들을 result 배열에 객체로 저장
                result.push({//그냥 페이지 정보 저장
                    inquiryId: inquiryNUM[i].inquiryId,
                    memberId: inquiryNUM[i].memberId,
                    inquiryContent: inquiryNUM[i].inquiryContent,
                    inquiryDateCreated: inquiryNUM[i].inquiryDateCreated,
                    answers: matchingAnswers  // 일치하는 답변들 배열을 answers 필드로 저장
                });
            }
            return result;  // 일치하는 요소들을 담은 배열 반환
        }

        // 일치하는 요소들을 HTML 테이블 형식으로 렌더링하여 출력하는 함수
        function renderComments(comments) {// comments변수 값은 따로 선언 하는것이 아니라 그 어떤값이 들어 가도 상관이없다.
        	// comments의 변수 값은 printMatchingItems함수를 통해 조건문에 맞게 정렬된 배열 형태의 값이다.
            let form = '';  // 출력할 HTML 문자열을 저장할 변수

            // 모든 일치하는 요소들을 테이블의 각 행으로 변환하여 form에 추가
            for (let i = 0; i < comments.length; i++) {
                // 문의 내용 행 추가
                form += 
                		'<tr>' +
                		'<td>문의 내용</td>' +
                        '<td class="inquiryId">' + comments[i].inquiryId + '</td>' +
                        '<td class="memberId">' + comments[i].memberId + '</td>' +
                        '<td class="inquiryContent">' + comments[i].inquiryContent + '</td>' + 
                        '<td class="inquiryDateCreated">' + toDate(comments[i].inquiryDateCreated) + '</td>' +
                        '</tr>';

                // 모든 일치하는 답변들에 대해 행 추가
                for (let j = 0; j < comments[i].answers.length; j++) {
                    form += 
                            '<tr>' +
                            '<td>ㄴ답변 내용</td>' +
                            '<td class="answerId">' + comments[i].answers[j].answerId + '</td>' +
                            '<td class="answerMemberId">' + comments[i].answers[j].memberId + '</td>' +
                            '<td class="answerContent">' + comments[i].answers[j].answerContent + '</td>' + 
                            '<td class="answerDateCreated">' + toDate(comments[i].answers[j].answerDateCreated) + '</td>' +
                            '</tr>';
                }
            }
            // 결과를 id가 'comments'인 요소에 HTML로 출력
            $('#comments').html(form);
        }// end renderComments()
    }// end getAllComments()
    
});// end (document).ready

</script>

</body>
</html>