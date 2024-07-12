<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="${_csrf.parameterName }" content="${_csrf.token }">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<style type="text/css">
.body_container{
	width: 65%;
	margin: auto;
	margin-top: 10%;
}
</style>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<title>쿠폰 등록</title>
</head>
<body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>

	<div class="body_container">
	<div>
		<div class="form-floating mb-3">
  			<input type="text" class="form-control" id="couponName" placeholder="쿠폰명">
  			<label for="couponName">쿠폰명</label>
		</div>
		<div class="form-floating mb-3">
  			<input type="text" class="form-control" id="discount" placeholder="할인률">
  			<label for="discount">할인률</label>
		</div>
	</div>
	<div>
		<input type="button" class="btn btn-outline-primary" value="등록" onclick="register()">
	</div>
	</div>
	<script type="text/javascript">
		
		let checkMap = {};
		checkMap.couponName = {
			exp : new RegExp('^[가-힣a-zA-Z0-9 %_\-]{1,50}$'),
			failMsg : '쿠폰명은 1~50자의 한글, 영어, 숫자, _, -만 입력 가능합니다',
			isValid : false
		};
		checkMap.discount = {
			exp : new RegExp('^[0-9]{1,2}$'),
			failMsg : '할인률은 100 미만의 자연수만 입력 가능합니다',
			isValid : false
		};
		
		function register() {
			checkMap.couponName.val = $('#couponName').val();
			checkMap.discount.val = $('#discount').val();
			
			// valid check
			for(x in checkMap){
				if(checkMap[x].val.length == 0){
					alert('모든 필드를 입력해주세요');
					return;
				}
				checkMap[x].isValid = checkMap[x].exp.test(checkMap[x].val);
				if(!checkMap[x].isValid){
					alert(checkMap[x].failMsg);
					return;
				}
			}
			
			// register request
			$.ajax({
				method : 'POST',
				url : 'register',
				headers : {
					'Content-type' : 'application/json',
					'X-CSRF-TOKEN' : $('meta[name="${_csrf.parameterName }"]').attr('content')
				},
				data : JSON.stringify({
					couponName : checkMap.couponName.val,
					discount : checkMap.discount.val
				}),
				success : function (result) {
					if(result == 1){
						alert('등록 성공');
						window.close();
					}else{
						alert('등록 실패');
					}
				}
			}); // end ajax
		
		} // end register 
		
	
	</script>
	
</body>
</html>