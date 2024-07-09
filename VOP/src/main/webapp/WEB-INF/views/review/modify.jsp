<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="isAuthenticated()">
    <sec:authentication var="memberDetails" property="principal"/>
</sec:authorize>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="${_csrf.parameterName }" content="${_csrf.token }">
    <title>리뷰 수정</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <jsp:include page="../include/header.jsp"></jsp:include>
    <style>
        body {
            font-family: 'Noto Sans KR', sans-serif;
            background-color: #f8f9fa;
            padding-top: 50px;
        }
        .container {
            background-color: #ffffff;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            padding: 30px;
            margin-bottom: 50px;
        }
        .form-group {
            margin-bottom: 20px;
        }
        .form-control {
            height: calc(2.25rem + 2px);
            border-radius: 4px;
        }
        .btn-submit {
            background-color: #ffa000;
            color: #ffffff;
            border: none;
            padding: 10px 20px;
            border-radius: 4px;
            cursor: pointer;
        }
        .btn-submit:hover {
            background-color: #ffca28;
        }
        #starFieldset {
            direction: rtl; /* 오른쪽에서 왼쪽으로 표시 */
            margin-bottom: 10px;
        }
        #starFieldset input[type=radio] {
            display: none; /* 라디오 버튼 숨김 */
        }
        #starFieldset label {
            font-size: 1.5em;
            color: #e4e5e9; /* 기본 별 색상 */
            cursor: pointer;
            margin-right: 5px;
        }
        #starFieldset label:hover,
        #starFieldset label:hover ~ label {
            color: #fada00; /* 호버 시 별 색상 변경 */
        }
        #starFieldset input[type=radio]:checked ~ label {
            color: #fada00; /* 선택된 별 색상 변경 */
        }
    </style>
</head>
<body>
    <div class="container">
        <c:set var="productVO" value="${productPreviewDTO.productVO }"></c:set>
        <h1>리뷰 수정</h1>
        
        <div>
            <p>상품 번호: ${productVO.productId}</p>
        </div>
        
        <p>이미지 썸네일</p>
        <div>
            <img src="${productPreviewDTO.imgUrl}" style="max-width: 300px; height: auto;" class="mb-4">
        </div>
        
        <form id="myform">
            <fieldset id="starFieldset">
                <!-- 별점 선택 라디오 버튼 -->
                <input type="radio" name="reviewStar" value="5" id="rate5"><label for="rate5">★</label>
                <input type="radio" name="reviewStar" value="4" id="rate4"><label for="rate4">★</label>
                <input type="radio" name="reviewStar" value="3" id="rate3"><label for="rate3">★</label>
                <input type="radio" name="reviewStar" value="2" id="rate2"><label for="rate2">★</label>
                <input type="radio" name="reviewStar" value="1" id="rate1"><label for="rate1">★</label>
            </fieldset>
            <div class="form-group">
                <label for="reviewContent">리뷰 내용</label>
                <textarea class="form-control" id="reviewContent" rows="3"></textarea>
            </div>
            <button type="button" id="btnUpdate" class="btn btn-submit">수정</button>
            <input type="hidden" id="productId" value="${productVO.productId}">
        </form>
    </div>

    <script>
        $(document).ready(function() {
            let memberId = "${memberDetails.getUsername()}";
            let selectedStar; // 전역 변수로 selectedStar 선언

            // 라디오 버튼 클릭 이벤트 핸들러
            $('#starFieldset input[type="radio"]').click(function() {
                // 선택된 별의 값 가져오기
                selectedStar = $(this).val();
                console.log(selectedStar);
            });

            // 수정 버튼 클릭 시
            $('#btnUpdate').click(function(event) {
                let productId = $("#productId").val(); 
                let reviewStar = selectedStar;
                let reviewContent = $('#reviewContent').val();

                if (reviewStar && reviewContent) {
                    let obj = {
                        'reviewStar': reviewStar,
                        'reviewContent': reviewContent,
                        'productId': productId,
                        'memberId': memberId
                    };

                    // ajax 요청
                    $.ajax({
                        type: 'PUT',
                        url: '../reviewRest/modify',
                        headers: {
                            'Content-Type': 'application/json',
                            'X-CSRF-TOKEN': $('meta[name="${_csrf.parameterName }"]').attr('content')
                        },
                        data: JSON.stringify(obj),
                        success: function(result) {
                            console.log(result);
                            if (result == 1) {
                                alert('리뷰 수정 성공');
                                window.location.href = '../review/list?memberId=' + memberId;
                            } else {
                                alert('리뷰 수정 실패');
                            }
                        }
                    });
                } else {
                    alert('별점과 리뷰 내용을 모두 입력해주세요.');
                }
            });
        });
    </script>
</body>
</html>
