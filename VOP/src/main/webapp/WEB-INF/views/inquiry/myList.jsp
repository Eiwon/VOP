<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<jsp:include page="../include/header.jsp"></jsp:include>
<jsp:include page="../include/sideBar.jsp"/>
<title>회원 문의 리스트</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style type="text/css">
    body {
        font-family: Arial, sans-serif;
        background-color: #f8f8f8;
        margin: 0;
        padding: 0;
    }
    .container {
        max-width: 800px;
        margin: 20px auto;
        padding: 20px;
        background-color: #fff;
        border: 1px solid #ddd;
        border-radius: 5px;
    }
    .header {
        margin-bottom: 20px;
    }
    .header strong {
        font-size: 1.5em;
    }
    .inquiry-list {
        list-style: none;
        padding: 0;
        margin: 0;
    }
    .inquiry-item {
        padding: 15px;
        border-bottom: 1px solid #ddd;
    }
    .inquiry-header {
        display: flex;
        justify-content: space-between;
        margin-bottom: 5px;
    }
    .inquiry-content {
        margin-bottom: 10px;
    }
    .inquiry-meta {
        font-size: 0.9em;
        color: #888;
    }
    .answer-list {
        list-style: none;
        padding-left: 20px;
        margin-top: 10px;
        border-left: 2px solid #ddd;
    }
    .answer-item {
        padding: 10px 0;
        border-bottom: 1px solid #eee;
    }
    .page_list {
        display: flex;
        justify-content: center;
        list-style: none;
        padding: 10px 0;
    }
    .page_list li {
        margin: 0 5px;
        cursor: pointer;
    }
    .page_list li:hover {
        text-decoration: underline;
    }
    @media (max-width: 600px) {
        .container {
            padding: 10px;
        }
        .header strong {
            font-size: 1.2em;
        }
        .inquiry-item, .answer-item {
            padding: 10px 0;
        }
        .page_list {
            flex-direction: column;
            align-items: center;
        }
        .page_list li {
            margin: 5px 0;
        }
    }
</style>
</head>
<body>
<div class="container">
    <div class="header">
        <strong>${memberDetails.getUsername()}님 문의 목록</strong>
    </div>
    <ul id="inquiry_list" class="inquiry-list"></ul>
    <div id="inquiry_list_page"></div>
</div>
<script type="text/javascript">
    let inquiryMap = {}; 
    let memberId = '${memberDetails.getUsername()}';

    $(document).ready(function() {
        inquiryMap.show(1);
    });

    inquiryMap.show = function(page) {
        $.ajax({
            method: 'GET',
            url: '../inquiryRest/myList?pageNum=' + page,
            success: function(data) {
                inquiryMap.listInquiry = data.listInquiry || [];
                inquiryMap.pageMaker = data.pageMaker || null;

                let matchingItems = printMatchingItems(inquiryMap.listInquiry);
                renderComments(matchingItems);
                $('#inquiry_list_page').html(makePageForm(inquiryMap));
            }
        });
    }

    function printMatchingItems(inquiryNUM) {
        let result = [];
        for (let i = 0; i < inquiryNUM.length; i++) {
            let matchingAnswers = [];
            if (inquiryNUM[i].inquiryId === inquiryNUM[i].answerInquiryId) {
                matchingAnswers.push({
                    answerId: inquiryNUM[i].answerId,
                    answerMemberId: inquiryNUM[i].answerMemberId,
                    answerContent: inquiryNUM[i].answerContent,
                    answerDateCreated: inquiryNUM[i].answerDateCreated
                });
            }
            result.push({
                inquiryId: inquiryNUM[i].inquiryId,
                inquiryMemberId: inquiryNUM[i].inquiryMemberId,
                productId: inquiryNUM[i].productId,
                inquiryContent: inquiryNUM[i].inquiryContent,
                inquiryDateCreated: inquiryNUM[i].inquiryDateCreated,
                answers: matchingAnswers
            });
        }
        return result;
    }

    function renderComments(comments) {
        let form = '';
        for (let i = 0; i < comments.length; i++) {
            form += '<li class="inquiry-item">' +
                    '<div class="inquiry-header">' +
                        '<span>문의 내용</span>' +
                        '<span class="inquiry-meta">' + toDate(comments[i].inquiryDateCreated) + '</span>' +
                    '</div>' +
                    '<div class="inquiry-content">' + comments[i].inquiryContent + '</div>' +
                    '<div class="inquiry-meta">작성자: ' + comments[i].inquiryMemberId + ' | 상품 번호: ' + comments[i].productId + '</div>';

            if (comments[i].answers.length > 0) {
                form += '<ul class="answer-list">';
                for (let j = 0; j < comments[i].answers.length; j++) {
                    form += '<li class="answer-item">' +
                            '<div class="inquiry-header">' +
                                '<span>답변 내용</span>' +
                                '<span class="inquiry-meta">' + toDate(comments[i].answers[j].answerDateCreated) + '</span>' +
                            '</div>' +
                            '<div class="inquiry-content">' + comments[i].answers[j].answerContent + '</div>' +
                            '<div class="inquiry-meta">작성자: ' + comments[i].answers[j].answerMemberId + '</div>' +
                            '</li>';
                }
                form += '</ul>';
            }
            form += '</li>';
        }
        $('#inquiry_list').html(form);
    }

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
    }

    function toDate(timestamp) {
        let date = new Date(timestamp);
        let formatted = date.getFullYear() + '/' + (date.getMonth() + 1) + '/' + date.getDate() + ' ' +
                        date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds();
        return formatted;
    }
</script>
</body>
</html>
