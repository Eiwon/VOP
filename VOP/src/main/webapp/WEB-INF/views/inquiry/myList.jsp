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

        $(document).ready(function(){
            inquiryMap.show(1); // 상품 목록 출력
        });

        inquiryMap.show = function(page) {
            let form = '';
            let memberId = '${memberDetails.getUsername()}';

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
