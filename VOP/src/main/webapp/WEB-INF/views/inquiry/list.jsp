<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>문의 리스트</title>
</head>
<body>
	<table>
	  <tbody>
	  	<c:if test="${empty listInquiry}">
         <c:forEach var="InquiryVO" items="${listInquiry}">
            <tr>
               <td>${InquiryVO.productId}</td>
               <td>${InquiryVO.inquiryContent}</td>
               <td>${InquiryVO.memberId}</td>
               <!-- boardDateCreated 데이터 포멧 변경 -->
               <fmt:formatDate value="${InquiryVO.inquiryDateCreated}"
                  pattern="yyyy-MM-dd HH:mm:ss" var="inquiryDateCreated" />
               <td>${inquiryDateCreated}</td>
            </tr>
         </c:forEach>
        </c:if>
        <c:otherwise>
        	<p>문의 사항이 없습니다.</p>
    	</c:otherwise>
      </tbody>
    </table>
    
    
</body>
</html>
