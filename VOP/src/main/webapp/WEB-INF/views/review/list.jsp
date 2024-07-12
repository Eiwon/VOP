<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<sec:authorize access="isAuthenticated()">
    <sec:authentication var="memberDetails" property="principal" />
</sec:authorize>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="${_csrf.parameterName}" content="${_csrf.token}">
    <title>리뷰 전체 검색</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <jsp:include page="../include/header.jsp"></jsp:include>
    <jsp:include page="../include/sideBar.jsp"/>
    <style>
        .product-box {
            padding: 20px;
            margin: 20px 0;
            border-radius: 10px;
            background-color: #f9f9f9;
        }
        .product-header {
            display: flex;
            flex-wrap: wrap;
            align-items: center;
            justify-content: space-between;
        }
        .product-info {
            display: flex;
            flex-wrap: wrap;
            align-items: center;
            justify-content: flex-start; /* 수정 및 삭제 버튼 왼쪽 정렬 */
            flex-grow: 1;
        }
        .product-image {
            max-width: 100px;
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
        .stars {
            display: inline-block;
            direction: ltr;
            border: 0;
        }
        .stars label {
            font-size: 1.5em;
            color: #ccc;
        }
        .btn-delete {
            background-color: #dc3545;
            color: #fff;
            border: none;
            padding: 5px 10px;
            border-radius: 4px;
            cursor: pointer;
        }
        .btn-delete:hover {
            background-color: #c82333;
        }
        @media (max-width: 576px) {
            .product-header {
                flex-direction: column;
                align-items: flex-start;
            }
            .product-info {
                flex-direction: column;
                align-items: flex-start;
                width: 100%;
            }
            .product-image {
                max-width: 100%;
                margin-bottom: 10px;
            }
        }
    </style>
</head>
<body>

<div class="container mt-5">
    <h1>댓글 리스트</h1>

    <c:forEach var="product" items="${productList}">
        <div class="product-box">
            <div class="product-header">
                <div class="product-info">
                    <h2>상품 이름 : ${product.productVO.productName}</h2>
                    <form action="../review/modify" method="get" class="mr-2">
                        <input type="hidden" name="productId" value="${product.productVO.productId}">
                        <input type="hidden" name="imgId" value="${product.productVO.imgId}">
                        <input type="hidden" name="memberId" value="${memberDetails.getUsername()}">
                        <button type="submit" class="btn btn-primary">리뷰 수정</button>
                    </form>
                    <button class="btn btn-delete" data-productid="${product.productVO.productId}">리뷰 삭제</button>
                </div>
                <img src="${product.imgUrl}" class="product-image img-fluid">
            </div>
            <div class="review-content">
                <c:forEach var="review" items="${reviewList}">
                    <c:if test="${review.productId == product.productVO.productId}">
                        <p>리뷰 : ${review.reviewStar}</p>
                        <div class="stars" data-reviewavg="${review.reviewStar}">
                            <fieldset>
                                <label for="star5">&#9733;</label>
                                <label for="star4">&#9733;</label>
                                <label for="star3">&#9733;</label>
                                <label for="star2">&#9733;</label>
                                <label for="star1">&#9733;</label>
                            </fieldset>
                        </div>
                        <p>리뷰 내용 : ${review.reviewContent}</p>
                    </c:if>
                </c:forEach>
            </div>
        </div>
    </c:forEach>
</div>

<script type="text/javascript">
    let memberId = "${memberDetails.getUsername()}";

    $(document).ready(function() {
        displayStars();

        function displayStars() {
            $('.stars').each(function() {
                let value = parseInt($(this).attr('data-reviewavg'));
                let stars = $(this).find('label');
                stars.slice(0, value).css('color', '#f0d000');
            });
        }

        $(document).on('click', '.btn-delete', function() {
            let productId = $(this).data('productid');

            let obj = {
                'productId': productId,
                'memberId': memberId
            };

            $.ajax({
                type : 'DELETE',
                url : '../reviewRest/delete',
                headers : {
                    'Content-Type' : 'application/json',
                    'X-CSRF-TOKEN' : $('meta[name="${_csrf.parameterName}"]').attr('content')
                },
                data : JSON.stringify(obj),
                success : function(result) {
                    if(result == 1) {
                        alert('댓글 삭제 성공!');
                        window.location.href = '../review/list?memberId=' + memberId;
                    } else {
                        alert('댓글 삭제 실패!');
                    }
                }
            });
        });
    });
</script>

</body>
</html>
