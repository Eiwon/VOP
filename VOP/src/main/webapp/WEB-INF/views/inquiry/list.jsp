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
<title>문의 리스트</title>
<jsp:include page="../include/header.jsp"></jsp:include>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<style type="text/css">
    body {
        font-family: Arial, sans-serif;
        background-color: #f8f9fa;
        padding: 20px;
    }

    h1 {
        font-size: 28px;
        margin-bottom: 20px;
        text-align: center;
    }

    table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 20px;
        background-color: #ffffff;
        border: 1px solid #dddddd;
        border-radius: 8px;
        box-shadow: 0px 0px 5px 0px rgba(0,0,0,0.1);
    }

    th, td {
        border: 1px solid #dddddd;
        text-align: left;
        padding: 12px;
    }

    th {
        background-color: #f2f2f2;
        font-size: 16px;
    }

    .btn-container {
        display: flex;
        justify-content: space-between;
        flex-wrap: wrap;
    }

    .btn {
        padding: 8px 16px;
        border: none;
        cursor: pointer;
        border-radius: 4px;
        transition: background-color 0.3s ease;
        margin-bottom: 5px;
        width: 100%; /* 각 버튼이 너비 100%를 차지하도록 설정 */
    }

    .btn-primary {
        background-color: #007bff;
        color: #ffffff;
    }

    .btn-primary:hover {
        background-color: #0056b3;
    }

    .btn-secondary {
        background-color: #6c757d;
        color: #ffffff;
    }

    .btn-secondary:hover {
        background-color: #5a6268;
    }

    .btn-danger {
        background-color: #dc3545;
        color: #ffffff;
    }

    .btn-danger:hover {
        background-color: #bd2130;
    }

    .btn-info {
        background-color: #17a2b8;
        color: #ffffff;
    }

    .btn-info:hover {
        background-color: #138496;
    }

    .btn-link {
        background-color: transparent;
        color: #007bff;
        padding: 0;
        width: auto;
    }

    .btn-link:hover {
        color: #0056b3;
        text-decoration: none;
    }

    .btn-icon {
        margin-right: 8px;
    }
</style>
</head>
<body>
    <div class="container">
        <h1>상품 문의 목록</h1>
        <table class="table">
            <thead>
                <tr>
                    <th style="width: 10%;">문의 ID</th>
                    <th style="width: 15%;">회원 ID</th>
                    <th style="width: 15%;">상품 ID</th>
                    <th style="width: 40%;">문의 내용</th>
                    <th style="width: 20%;">작성 일자</th>
                    <th style="width: 20%;">버튼</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty listInquiry}">
                        <c:forEach var="InquiryVO" items="${listInquiry}">
                            <tr>
                                <td>${InquiryVO.inquiryId}</td>
                                <td>${InquiryVO.memberId}</td>
                                <td>${InquiryVO.productId}</td>
                                <td>${InquiryVO.inquiryContent}</td>
                                <td><fmt:formatDate value="${InquiryVO.inquiryDateCreated}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                <td class="btn-container">
                                    <div>
                                        <button class="btn btn-primary btn-icon btnAdd" data-inquiryid="${InquiryVO.inquiryId}" data-productid="${InquiryVO.productId}">
                                            <i class="fas fa-reply"></i> 답글 작성
                                        </button>
                                    </div>
                                    <div>
                                        <button class="btn btn-secondary btn-icon btnModify" data-inquiryid="${InquiryVO.inquiryId}">
                                            <i class="fas fa-edit"></i> 답글 수정
                                        </button>
                                    </div>
                                    <div>
                                        <button class="btn btn-danger btn-icon btnDelete" data-inquiryid="${InquiryVO.inquiryId}">
                                            <i class="fas fa-trash-alt"></i> 답글 삭제
                                        </button>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="6">
                                    <div id="inputContainer_${InquiryVO.inquiryId}" class="inputContainer" style="text-align: center; margin-top: 10px; display: none;">
                                        <input type="text" id="replyAnswer_${InquiryVO.inquiryId}" placeholder="답글 작성">
                                        <button class="btn btn-info btnSubmit" data-inquiryid="${InquiryVO.inquiryId}" data-productid="${InquiryVO.productId}">
                                            작성
                                        </button>
                                    </div>
                                    <div id="modifyContainer_${InquiryVO.inquiryId}" class="modifyContainer" style="text-align: center; margin-top: 10px; display: none;">
                                        <input type="text" id="modifyAnswer_${InquiryVO.inquiryId}" placeholder="답글 수정">
                                        <button class="btn btn-info btnModifySubmit" data-inquiryid="${InquiryVO.inquiryId}">
                                            수정
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="6">
                                <p>문의 사항이 없습니다.</p>
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>

        <div id="pageSelector" class="mt-3 d-flex justify-content-center">
            <c:if test="${pageMaker.isPrev()}">
                <a href="../inquiry/list?productId=${productId}&pageNum=${pageMaker.startNum - 1}" class="btn btn-link">이전</a>
            </c:if>
            <c:forEach begin="${pageMaker.startNum}" end="${pageMaker.endNum}" var="num">
                <a href="../inquiry/list?productId=${productId}&pageNum=${num}" class="btn btn-link">${num}</a>
            </c:forEach>
            <c:if test="${pageMaker.isNext()}">
                <a href="../inquiry/list?productId=${productId}&pageNum=${pageMaker.endNum + 1}" class="btn btn-link">다음</a>
            </c:if>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.7.1.js" defer></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" defer></script>
    <script type="text/javascript">
    document.addEventListener('DOMContentLoaded', function() {
        const memberId = '${memberDetails.getUsername()}';

        // Add button event listeners
        var btnAdds = document.querySelectorAll('.btnAdd');
        for (var i = 0; i < btnAdds.length; i++) {
            btnAdds[i].addEventListener('click', function() {
                var inquiryId = this.getAttribute('data-inquiryid');
                document.getElementById('inputContainer_' + inquiryId).style.display = 'block';
                document.getElementById('modifyContainer_' + inquiryId).style.display = 'none';
                document.getElementById('replyAnswer_' + inquiryId).value = '';
                document.getElementById('modifyAnswer_' + inquiryId).value = ''; // 답글 수정 입력 필드 초기화
            });
        }

        var btnModifies = document.querySelectorAll('.btnModify');
        for (var i = 0; i < btnModifies.length; i++) {
            btnModifies[i].addEventListener('click', function() {
                var inquiryId = this.getAttribute('data-inquiryid');
                document.getElementById('modifyContainer_' + inquiryId).style.display = 'block';
                document.getElementById('inputContainer_' + inquiryId).style.display = 'none';
                document.getElementById('modifyAnswer_' + inquiryId).value = '';
                document.getElementById('replyAnswer_' + inquiryId).value = ''; // 답글 작성 입력 필드 초기화
            });
        }

        var btnSubmits = document.querySelectorAll('.btnSubmit');
        for (var i = 0; i < btnSubmits.length; i++) {
            btnSubmits[i].addEventListener('click', function() {
                var answerContent = document.getElementById('replyAnswer_' + this.getAttribute('data-inquiryid')).value;
                var inquiryId = this.getAttribute('data-inquiryid');
                var productId = this.getAttribute('data-productid');

                var obj = {
                    'inquiryId': inquiryId,
                    'productId': productId,
                    'memberId': memberId,
                    'answerContent': answerContent
                };

                fetch('../answer/register', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-CSRF-TOKEN': document.querySelector('meta[name="${_csrf.parameterName}"]').content
                    },
                    body: JSON.stringify(obj)
                })
                .then(function(response) {
                    return response.json();
                })
                .then(function(result) {
                    if (result === 1) {
                        alert('답변 성공');
                    } else {
                        alert('답변을 이미 작성 하셨습니다.');
                    }
                    document.getElementById('replyAnswer_' + inquiryId).value = ''; // 입력 필드 초기화
                })
                .catch(function(error) {
                    console.error('Error:', error);
                });
            });
        }

        var btnModifySubmits = document.querySelectorAll('.btnModifySubmit');
        for (var i = 0; i < btnModifySubmits.length; i++) {
            btnModifySubmits[i].addEventListener('click', function() {
                var answerContent = document.getElementById('modifyAnswer_' + this.getAttribute('data-inquiryid')).value;
                var inquiryId = this.getAttribute('data-inquiryid');

                var obj = {
                    'inquiryId': inquiryId,
                    'memberId': memberId,
                    'answerContent': answerContent
                };

                fetch('../answer/modify', {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-CSRF-TOKEN': document.querySelector('meta[name="${_csrf.parameterName}"]').content
                    },
                    body: JSON.stringify(obj)
                })
                .then(function(response) {
                    return response.json();
                })
                .then(function(result) {
                    if (result === 1) {
                        alert('답변 수정 성공!');
                    } else {
                        alert('답변이 작성 되어 있지 않습니다.');
                    }
                    document.getElementById('modifyAnswer_' + inquiryId).value = ''; // 입력 필드 초기화
                })
                .catch(function(error) {
                    console.error('Error:', error);
                });
            });
        }

        var btnDeletes = document.querySelectorAll('.btnDelete');
        for (var i = 0; i < btnDeletes.length; i++) {
            btnDeletes[i].addEventListener('click', function() {
                var inquiryId = this.getAttribute('data-inquiryid');

                var obj = {
                    'inquiryId': inquiryId,
                    'memberId': memberId
                };

                fetch('../answer/delete', {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-CSRF-TOKEN': document.querySelector('meta[name="${_csrf.parameterName}"]').content
                    },
                    body: JSON.stringify(obj)
                })
                .then(function(response) {
                    return response.json();
                })
                .then(function(result) {
                    if (result === 1) {
                        alert('답변 삭제 성공!');
                    } else {
                        alert('삭제할 답변이 없습니다.');
                    }
                })
                .catch(function(error) {
                    console.error('Error:', error);
                });
            });
        }
    });

    </script>
</body>
</html>
