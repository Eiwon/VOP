<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>리뷰 전체 검색</title>
<!-- 외부 CSS 파일 링크 -->
<link href="/assets/css/star.css" rel="stylesheet"/>
<style>
/* 리뷰 별 폼 스타일 */
#myform fieldset {
    display: inline-block;
    direction: ltr; /* 텍스트 방향을 오른쪽에서 왼쪽으로 설정 */
    border: 0;
}

/* 라디오 버튼 숨김 */
#myform input[type=radio] {
    display: none;
}

/* 별표시 스타일 */
#myform label {
    font-size: 1em;
    color: transparent;
    text-shadow: 0 0 0 #f0f0f0;
    pointer-events: none; /* 별점 조작 비활성화 */
    cursor: default; /* 커서를 기본값으로 설정하여 클릭 이벤트 제거 */
    
}
</style>
</head>
<body>
	<!-- 제작 예정 -->



</body>
</html>