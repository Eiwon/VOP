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
    <title>리뷰 작성</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <jsp:include page="../include/header.jsp"></jsp:include>
    <style>
        /* 전체 페이지의 기본 스타일 */
        body {
            font-family: 'Noto Sans KR', sans-serif; /* 폰트 설정 */
            background-color: #f8f9fa; /* 배경색 설정 */
        }

        /* 주요 컨테이너 스타일 */
        .container {
            background-color: #ffffff; /* 배경색 설정 */
            border-radius: 8px; /* 테두리 모서리 둥글기 설정 */
            box-shadow: 0 0 10px rgba(0,0,0,0.1); /* 그림자 설정 */
            padding: 30px; /* 안쪽 여백 설정 */
            margin-bottom: 50px; /* 하단 여백 설정 */
        }

        /* 폼 요소 그룹 스타일 */
        .form-group {
            margin-bottom: 20px; /* 하단 여백 설정 */
        }

        /* 입력 컨트롤(텍스트 박스 등) 스타일 */
        .form-control {
            height: calc(2.25rem + 2px); /* 높이 설정 */
            border-radius: 4px; /* 테두리 둥글기 설정 */
        }

        /* 등록 버튼 스타일 */
        .btn-submit {
            background-color: #ffa000; /* 배경색 설정 */
            color: #ffffff; /* 글자색 설정 */
            border: none; /* 테두리 없음 */
            padding: 10px 20px; /* 안쪽 여백 설정 */
            border-radius: 4px; /* 테두리 둥글기 설정 */
            cursor: pointer; /* 마우스 커서 설정 */
            width: 100px; /* 너비 설정 */
        }

        /* 등록 버튼 호버 시 스타일 */
        .btn-submit:hover {
            background-color: #ffca28; /* 호버 배경색 설정 */
        }

        #starFieldset input[type=radio] {
            display: none; /* 라디오 버튼 숨김 */
        }

        /* 별점 필드셋 스타일 */
        #starFieldset {
            direction: rtl; /* 오른쪽에서 왼쪽으로 표시 */
            margin-bottom: 10px; /* 하단 여백 설정 */
        }

        /* 별점 라벨 스타일 */
        #starFieldset label {
            font-size: 1.5em; /* 폰트 크기 설정 */
            color: #e4e5e9; /* 기본 별 색상 설정 */
            cursor: pointer; /* 마우스 커서 설정 */
            margin-right: 5px; /* 오른쪽 여백 설정 */
        }

        /* 별점 라벨 호버 시 스타일 */
        #starFieldset label:hover,
        #starFieldset label:hover ~ label {
            color: #fada00; /* 호버 시 별 색상 변경 */
        }

        /* 선택된 별점 라벨 스타일 */
        #starFieldset input[type=radio]:checked ~ label {
            color: #fada00; /* 선택된 별 색상 설정 */
        }

        /* textarea의 크기를 조절하지 못하도록 설정 */
        textarea.form-control {
            resize: none;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1 class="mb-4">리뷰 작성</h1>
        
        <h3>${memberDetails.getUsername() } 님의 리뷰 작성</h3>
        
        <div class="mb-4">
            <p>상품 번호: ${productPreviewDTO.productVO.productId}</p>
        </div>
        
        <div>
            <img src="${productPreviewDTO.imgUrl}" style="max-width: 100%; height: auto;" class="mb-4">
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
            <input type="hidden" id="productId" value="${productPreviewDTO.productVO.productId}">
            <div class="form-group">
                <label for="reviewContent">리뷰 내용</label>
                <textarea class="form-control" id="reviewContent" rows="6" maxlength="450"></textarea>
                <small id="charCount" class="form-text text-muted">0/450</small>
            </div>
            <button type="button" id="btnAdd" class="btn btn-submit btn-block">등록</button>
        </form>
    </div>

    <script>
        $(document).ready(function() {
            let selectedStar; // 전역 변수로 selectedStar 선언
            let memberId = "${memberDetails.getUsername()}";

            // 라디오 버튼 클릭 이벤트 핸들러
            $('#starFieldset input[type="radio"]').click(function() {
                // 선택된 별의 값 가져오기
                selectedStar = $(this).val();
                console.log(selectedStar);
            });

            // textarea의 입력 길이를 확인하는 이벤트 핸들러
            $('#reviewContent').on('input', function() {
                let charCount = $(this).val().length;
                $('#charCount').text(charCount + '/450');
            });

            // 댓글 입력 코드
            $('#btnAdd').click(function(event) {
                let productId = $("#productId").val(); 
                let reviewStar = selectedStar;
                let reviewContent = $('#reviewContent').val();

                if (reviewStar && reviewContent) {
                    let obj = {
                        'memberId': memberId,
                        'productId': productId,
                        'reviewStar': reviewStar,
                        'reviewContent': reviewContent
                    };

                    $.ajax({
                        type: 'POST',
                        url: '../reviewRest/register',
                        headers: {
                            'Content-Type': 'application/json',
                            'X-CSRF-TOKEN': $('meta[name="${_csrf.parameterName }"]').attr('content')
                        },
                        data: JSON.stringify(obj),
                        success: function(result) {
                            console.log(result);
                            if (result == 1) {
                                alert('리뷰 등록 성공');
                                window.location.href = '../review/list?memberId=' + memberId;
                            } else {
                                alert('이미 등록된 리뷰입니다.');
                                window.location.href = '../review/list?memberId=' + memberId;
                            }
                        }
                    });
                } else {
                    alert('별점과 리뷰 내용을 모두 입력해주세요.');
                }
            });// end ajax()
        });// end $(document).ready()
    </script>
</body>
</html>
