<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authorize access="isAuthenticated()">
    <sec:authentication var="memberDetails" property="principal"/>
</sec:authorize>
<jsp:include page="../include/header.jsp"></jsp:include>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <!-- <meta name="viewport" content="width=device-width, initial-scale=1.0"> 모달일 코드 사용 안해서 주석 석리 했습니다. -->
    <title>배송지 관리</title>
    <!-- 부트스트랩 CSS -->
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <!-- 부가적인 사용자 지정 CSS -->
    <style>
    	.container {
    		width: 75%;
    		margin: auto;
    	}
    	.custom-badge {
		    font-size: 0.9rem; /* 원하는 크기로 조정 */
		    padding: 0.3rem 0.5rem; /* 패딩을 조절하여 크기를 조정할 수도 있습니다 */
		}
    
        .delivery-box {
            border: 1px solid #ccc;
            padding: 10px;
            margin-bottom: 20px;
        }
        .delivery-details {
            display: flex;
            align-items: center;
            margin-bottom: 10px;
        }
        .delivery-details img {
            margin-right: 10px;
            max-width: 100px;
        }
        .delivery-buttons {
            margin-left: auto;
        }
        .delivery-details p {
            margin-bottom: 10px;
        }
        .delivery-details label {
            display: inline-block;
            width: 150px;
            font-weight: bold;
        }
        .default-checkbox {
            margin-top: 10px;
        }

        .default-delivery {
            background-color: #e6f7ff; /* 배경색 변경 */
            border-color: blue; /* 테두리 색상 변경 */
            border-width: 1px; /* 테두리 두께 설정 */
            border-style: solid; /* 테두리 스타일 설정 */
            font-weight: bold; /* 글씨 강조 */
            /* 기본 배송지에 대한 추가적인 스타일 지정 */
        }
    </style>
</head>
<body>
<jsp:include page="../include/sideBar.jsp"/>
<div class="container text-center"><br>
    <h2 class="mt-4 mb-4">${memberDetails.getUsername()}님의 배송지 관리</h2>
    <br>

    <table class="table table-bordered">
        <thead class="thead-dark">
            <tr>
                <th scope="col">받는 사람</th>
                <th scope="col">최종 배송지</th>
                <th scope="col">휴대폰 번호</th>
                <th scope="col">배송 요청 사항</th>
                <th scope="col">기본 배송지 설정</th>
                <th scope="col">수정</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${deliveryList}" var="delivery">
                <tr class="${delivery.isDefault == 1 ? 'default-delivery' : ''}">
                    <td>${delivery.receiverName}</td>
                    <td>${delivery.receiverAddress} ${delivery.deliveryAddressDetails}</td>
                    <td>${delivery.receiverPhone}</td>
                    <td>${delivery.requirement}</td>
                    <td>
                        <c:if test="${delivery.isDefault eq 1}">
                            <span class="badge badge-primary custom-badge" style="background-color: #ffea59; border-color: #ffea59; color: black;">기본 배송지</span>
                        </c:if>
                    </td>
                    <td>
                        <a href="../Delivery/deliveryUpdate?deliveryId=${delivery.deliveryId}" class="btn btn-sm btn-primary" style="background-color: #837cb6; border-color: #837cb6;">수정</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <!-- 배송지 목록이 비어있을 때 -->
    <c:if test="${empty deliveryList}">
        <div class="alert alert-info mt-3">
            배송지 목록이 비어 있습니다.
        </div>
    </c:if>

    <a href="../Delivery/deliveryRegister" class="btn btn-primary mt-3" style="background-color: #4f4c70; border-color: #4f4c70;">배송지 추가</a>
</div>

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<!-- 부트스트랩 JS -->
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
