<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<title>관리자 페이지</title>
</head>
<body>
	<div>
		<h2>관리자 페이지</h2>
	</div>
	
	<div>
		<div>
			<h3>사업자 등록 요청</h3>
		</div>
		<div>
			<h3>상품 등록 요청</h3>
		</div>
		<div>
			<h3>상품 삭제 요청</h3>
		</div>
	</div>
	
	<div>
		
		<!-- 판매자 등록 심사 목록 -->
		
		<!-- 상품 등록 심사 목록 -->
		
		<!-- 상품 삭제 요청 -->
	</div>
	
	
	<script type="text/javascript">
		let tagSellerReqList = $('seller_req_list');
		
		
		
		function showSellerRequest(){
			let form = '<div><table><tbody id="seller_req_list">' + '</tbody></table></div>';
			
			//$.ajax({});
			
		} // end showSellerRequest
	
		
	</script>
	
</body>
</html>