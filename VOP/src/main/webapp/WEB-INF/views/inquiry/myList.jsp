<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!-- 시큐리티 관련코드 -->
<%@ taglib prefix="sec"
    uri="http://www.springframework.org/security/tags"%>
<sec:authorize access="isAuthenticated()">
    <sec:authentication var="memberDetails" property="principal" />
</sec:authorize>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<!-- 모바일 관련 코드라서 없어도 동작 가능 -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<jsp:include page="../include/header.jsp"></jsp:include>
<title>회원 문의 리스트</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style type="text/css">
.page_list {
            display: flex;
            flex-direction: row;
            list-style: none;
        }

        tbody {
            height: 250px;
        }

        tr {
            height: 50px;
        }

        td {
            width: 200px;
        }

        /* 좋아요 or 싫어요 css 코드 */
        .button-container {
            display: flex;
            gap: 20px;
        }

        .button {
            font-size: 24px;
            color: grey;
            cursor: pointer;
            transition: color 0.3s;
        }

        .button.liked {
            color: blue;
        }

        .button.disliked {
            color: red;
        }
</style>
</head>
<body>

    <div>
        <strong>${memberDetails.getUsername()}님 문의 목록</strong>
    </div>
    <table>
        <thead>
            <tr>
                <th>상품 번호</th>
                <th>문의 내용</th>
                <th>작성 일자</th>
            </tr>
        </thead>
        <tbody id="inquiry_list"></tbody>
        <tfoot id="inquiry_list_page"></tfoot>
    </table>

    <script type="text/javascript">
        let inquiryMap = {}; // 상품 목록과 페이지 정보를 저장할 객체 선언
        let memberId = '${memberDetails.getUsername()}';
       
        $(document).ready(function(){
            inquiryMap.show(1); // 상품 목록 출력
        });

        inquiryMap.show = function(page) {
        	
            let form = '';

            $.ajax({
                method: 'GET',
                url: '../inquiryRest/myList?pageNum=' + page,
                success: function(result) {
                    inquiryMap.list = result.listInquiry;
                    console.log("inquiryMap.list : " + inquiryMap.list);
                    inquiryMap.pageMaker = result.pageMaker;
                    console.log("inquiryMap.pageMaker : " + inquiryMap.pageMaker);

                    const list = inquiryMap.list;
                    console.log("list : " + list);
                    
                    for (let x in list) {
                        form += '<tr>' +
                                '<td class="productId">' + list[x].productId + '</td>' + 
                                '<td class="inquiryContent">' + list[x].inquiryContent + '</td>' + 
                                '<td class="inquiryDateCreated">' + toDate(list[x].inquiryDateCreated) + '</td>' +
                                '<td class="button-container"></td>' +
                                '</tr>';
                    }

                    $('#inquiry_list').html(form);
                    $('#inquiry_list_page').html(makePageForm(inquiryMap));
                } // end success
            }); // end ajax
        } // end show 
        
        /* inquiryMap.show = function(page) {
        	 $.ajax({
                 method: 'GET',
                 url: inquiryUrl,
                 success: function(data) {
                  	// reviewMap 초기화
                     inquiryMap.inquiryDTOList = data.listInquiry || [];
                     inquiryMap.pageMaker = data.pageMaker || null;
                     console.log("inquiryDTOList : " + inquiryDTOList);
                     console.log("inquiryNUM : " + inquiryNUM);

                     inquiryNUM = inquiryMap.inquiryDTOList;  // 성공적으로 데이터를 가져오면 inquiryNUM에 저장
                     console.log("inquiryNUM : " + inquiryNUM);
                     let matchingItems = printMatchingItems(inquiryNUM);// 그럼 여기서 함수가 실행 된 다음 변수에 저장?
                     $('#inquiry_list_page').html(makePageForm(inquiryMap));
                 },
             }); // ajax
        }// end inquiryMap.show()
        
        // 문의와 답변 데이터를 비교하여 일치하는 요소들을 반환하는 함수
        function printMatchingItems(inquiryNUM) {
            let result = [];
            // 모든 문의와 답변 데이터를 비교하여 일치하는 경우를 찾음
            for (let i = 0; i < inquiryNUM.length; i++) {
            	
                let matchingAnswers = []; // 현재 문의에 대한 일치하는 답변들을 저장할 배열

                        // 일치하는 경우 matchingAnswers 배열에 객체로 저장
                        matchingAnswers.push({
                            answerId: inquiryNUM[i].answerId,
                            answerMemberId: inquiryNUM[i].answerMemberId,
                            answerContent: inquiryNUM[i].answerContent,
                            answerDateCreated: inquiryNUM[i].answerDateCreated
                        });
            
                // 문의와 해당하는 모든 답변들을 result 배열에 객체로 저장
                result.push({
                    inquiryId: inquiryNUM[i].inquiryId,
                    inquiryMemberId: inquiryNUM[i].inquiryMemberId,
                    inquiryContent: inquiryNUM[i].inquiryContent,
                    inquiryDateCreated: inquiryNUM[i].inquiryDateCreated,
                    answers: matchingAnswers  // 일치하는 답변들 배열을 answers 필드로 저장
                });
            }
            
            console.log("result : " + result);
            return result;  // 일치하는 요소들을 담은 배열 반환
        }  
		
        
     // 일치하는 요소들을 HTML 테이블 형식으로 렌더링하여 출력하는 함수
        function renderComments(comments) {// comments변수 값은 따로 선언 하는것이 아니라 그 어떤값이 들어 가도 상관이없다.
        	// comments의 변수 값은 printMatchingItems함수를 통해 조건문에 맞게 정렬된 배열 형태의 값이다.
            let form = '';  // 출력할 HTML 문자열을 저장할 변수

            // 모든 일치하는 요소들을 테이블의 각 행으로 변환하여 form에 추가
            for (let i = 0; i < comments.length; i++) {
                // 문의 내용 행 추가
                form += '<tr>' +
                		'<td colspan="4">문의 내용</td>' +  
                		'</tr>' +
                		'<tr>' +
                        '<td class="inquiryId">' + comments[i].inquiryId + '</td>' +
                        '<td class="memberId">' + comments[i].memberId + '</td>' +
                        '<td class="inquiryContent">' + comments[i].inquiryContent + '</td>' + 
                        '<td class="inquiryDateCreated">' + toDate(comments[i].inquiryDateCreated) + '</td>' +
                        '</tr>';

                // 모든 일치하는 답변들에 대해 행 추가
                for (let j = 0; j < comments[i].answers.length; j++) {
                    form += '<tr>' +
                            '<td colspan="3">ㄴ답변 내용</td>' +  // 답변 내용 표시
                            '</tr>' +
                            '<tr>' +
                            '<td class="answerId">' + comments[i].answers[j].answerId + '</td>' +
                            '<td class="answerMemberId">' + comments[i].answers[j].memberId + '</td>' +
                            '<td class="answerContent">' + comments[i].answers[j].answerContent + '</td>' + 
                            '<td class="answerDateCreated">' + toDate(comments[i].answers[j].answerDateCreated) + '</td>' +
                            '</tr>';
                }
            }
            // 결과를 id가 'comments'인 요소에 HTML로 출력
            $('#comments').html(form);
        }// end renderComments() */


        function makePageForm(inquiryMap) {
            const pageMaker = inquiryMap.pageMaker;
            const startNum = pageMaker.startNum;
            const endNum = pageMaker.endNum;

            let pageForm = $('<ul class="page_list"></ul>');
            let numForm;
            if (pageMaker.prev) {
                numForm = $('<li>이전&nbsp&nbsp</li>').click(function() {
                    inquiryMap.show(startNum - 1);
                });
                pageForm.append(numForm);
            }
            for (let x = startNum; x <= endNum; x++) {
                numForm = $('<li>' + x + '&nbsp&nbsp</li>').click(function() {
                    inquiryMap.show(x);
                });
                pageForm.append(numForm);
            }
            if (pageMaker.next) {
                numForm = $('<li>다음</li>').click(function() {
                    inquiryMap.show(endNum + 1);
                });
                pageForm.append(numForm);
            }
            return pageForm;
        } // end makePageForm

        // 시간 변환 함수
        function toDate(timestamp) {
            let date = new Date(timestamp);
            let formatted = (date.getFullYear()) + '/' + (date.getMonth() + 1) + '/' + date.getDate() + ' ' +
                            date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds();
            return formatted;
        } // end toDate
    </script>
    
</body>
</html>
