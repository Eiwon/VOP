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
<meta name="${_csrf.parameterName}" content="${_csrf.token}">
<title>답변 리스트</title>
<jsp:include page="../include/header.jsp"></jsp:include>
<jsp:include page="../include/sideBar.jsp"></jsp:include>
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<style>
body {
    font-family: Arial, sans-serif;
    background-color: #f8f8f8;
    margin: 0;
    padding: 0;
}

.container {
    max-width: 1200px;
    margin: 20px auto;
    padding: 20px;
    background-color: #fff;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.section {
    margin-bottom: 40px;
    padding: 20px;
    background-color: #ffffff;
    border: 1px solid #e0e0e0;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.section h3 {
    margin-bottom: 20px;
    font-size: 1.5em;
    text-align: center;
    color: #333;
}

.content-center {
    display: flex;
    justify-content: center;
}

.content-center div {
    width: 100%;
}

.comment {
    margin-bottom: 20px;
    padding: 15px;
    border: 1px solid #e0e0e0;
    border-radius: 8px;
    background-color: #f9f9f9;
}

.comment-header {
    display: flex;
    justify-content: space-between;
    margin-bottom: 10px;
    font-weight: bold;
}

.comment-body {
    margin-bottom: 10px;
}

.answer {
    margin-left: 20px;
    padding-left: 10px;
    border-left: 2px solid #e0e0e0;
    background-color: #f2f2f2;
    border-radius: 8px;
}

.pagination {
    display: flex;
    justify-content: center;
    margin-top: 20px;
}

.pagination ul {
    display: flex;
    list-style: none;
    padding: 0;
}

.pagination li {
    margin: 0 5px;
    padding: 10px 20px;
    background-color: #f0f0f0;
    border: 1px solid #ccc;
    border-radius: 4px;
    cursor: pointer;
}

.pagination li:hover {
    background-color: #e0e0e0;
}

@media (max-width: 768px) {
    .section {
        padding: 10px;
    }

    .section h3 {
        font-size: 1.2em;
    }

    .pagination li {
        padding: 5px 10px;
    }
}
</style>
</head>
<body>

<div class="container">
    <div class="section">
        <h3>답변 내용</h3>
        <div class="content-center">
            <div id="comments"></div>
        </div>
        <div class="pagination" id="comments_list_page"></div>
    </div>
</div>

<script type="text/javascript">
    let productId = ${productId};
    let inquiryMap = [];

    $(document).ready(function() { 
        inquiryMap.show(1);
    });

    inquiryMap.show = function(page) {
        console.log("productId : " + productId);
        let inquiryUrl = '../inquiryRest/list/' + productId + '/' + page;
        let answerUrl = '../answer/list/' + productId;

        let inquiryNUM = [];
        let answerNUM = [];

        $.ajax({
            method: 'GET',
            url: inquiryUrl,
            success: function(data) {
                inquiryMap.inquiryList = data.inquiryList || [];
                inquiryMap.pageMaker = data.pageMaker || null;

                inquiryNUM = inquiryMap.inquiryList;
                console.log("inquiryNUM : " + inquiryNUM);
                processComments();
                $('#comments_list_page').html(inquiryMakePageForm(inquiryMap));
            },
        });

        $.ajax({
            method: 'GET',
            url: answerUrl,
            success: function(data) {
                answerNUM = data;
                console.log("answerNUM : " + answerNUM);
                processComments();
            },
        });

        function processComments() {
            if (inquiryNUM.length > 0) {
                let matchingItems = printMatchingItems(inquiryNUM, answerNUM);
                renderComments(matchingItems);
            } else if (inquiryNUM.length == 0) {
            	let form =  
            	'<div class="comment">' +
                '<span>답변 내용이 없습니다.</span>' +
                '</div>';
            	 $('#comments').html(form);
            }
        }

        function printMatchingItems(inquiryNUM, answerNUM) {
            let result = [];
            for (let i = 0; i < inquiryNUM.length; i++) {
                let matchingAnswers = [];

                for (let j = 0; j < answerNUM.length; j++) {
                    if (inquiryNUM[i].inquiryId === answerNUM[j].inquiryId) {
                        matchingAnswers.push({
                            answerId: answerNUM[j].answerId,
                            memberId: answerNUM[j].memberId,
                            answerContent: answerNUM[j].answerContent,
                            answerDateCreated: answerNUM[j].answerDateCreated
                        });
                    }
                }

                result.push({
                    inquiryId: inquiryNUM[i].inquiryId,
                    memberId: inquiryNUM[i].memberId,
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
                form += 
                    '<div class="comment">' +
                    '<div class="comment-header">' +
                    '<span>문의 내용</span>' +
                    '<span>' + toDate(comments[i].inquiryDateCreated) + '</span>' +
                    '</div>' +
                    '<div class="comment-body">' +
                    '<p>' + comments[i].inquiryContent + '</p>' +
                    '</div>' +
                    '<div class="comment-footer">' +
                    '<span>작성자: ' + comments[i].memberId + '</span>' +
                    '</div>';

                for (let j = 0; j < comments[i].answers.length; j++) {
                    form += 
                        '<div class="comment answer">' +
                        '<div class="comment-header">' +
                        '<span>ㄴ답변 내용</span>' +
                        '<span>' + toDate(comments[i].answers[j].answerDateCreated) + '</span>' +
                        '</div>' +
                        '<div class="comment-body">' +
                        '<p>' + comments[i].answers[j].answerContent + '</p>' +
                        '</div>' +
                        '<div class="comment-footer">' +
                        '<span>작성자: ' + comments[i].answers[j].memberId + '</span>' +
                        '</div>' +
                        '</div>';
                }

                form += '</div>';
            }

            $('#comments').html(form);
        }

        function inquiryMakePageForm(inquiryMap) {
            const inquiryPageMaker = inquiryMap.pageMaker;
            const inquiryStartNum = inquiryPageMaker.startNum;
            const inquiryEndNum = inquiryPageMaker.endNum;
            
            let pageForm = $('<ul></ul>');
            let numForm;
            if (inquiryPageMaker.prev) {
                numForm = $('<li>이전&nbsp&nbsp</li>').click(function() {
                    inquiryMap.show(inquiryStartNum - 1);
                });
                pageForm.append(numForm);
            }
            for (let x = inquiryStartNum; x <= inquiryEndNum; x++) {
                numForm = $('<li>' + x + '&nbsp&nbsp</li>').click(function() {
                    inquiryMap.show(x);
                });
                pageForm.append(numForm);
            }
            if (inquiryPageMaker.next) {
                numForm = $('<li>다음</li>').click(function() {
                    inquiryMap.show(inquiryEndNum + 1);
                });
                pageForm.append(numForm);
            }
            return pageForm;
        }

        function toDate(timestamp) {
            let date = new Date(timestamp);
            let formatted = (date.getFullYear()) + '/' + (date.getMonth() + 1) + '/' + date.getDate() + ' ' +
                            date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds();
            return formatted;
        }
    }
</script>

</body>
</html>
