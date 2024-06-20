<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<style type="text/css">
.selectCoupon {
	width: 600px;
}
</style>
<title>쿠폰 등록</title>
</head>
<body>

	<form action="register" method="POST">
		<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
		<div>
			<span>분류</span>
			<select name="type" class="type">
				<option value="notice">공지사항</option>
				<option value="coupon">쿠폰 팝업</option>
			</select>
		</div>
		<div>
			<span>제목</span>
			<input type="text" name="title" class="title">
		</div>
		<div>
			<span>내용</span>
			<textarea rows="4" cols="25" name="content" class="content">
			</textarea>
		</div>
		<div>
			<select class="selectCoupon" name="callbackInfo" hidden="hidden">
				
			</select>
		</div>
		<div>
			<input type="submit" value="등록">
		</div>
	</form>
	<script type="text/javascript">
		let tagType = $('.type');
		let tagSelectCoupon = $('.selectCoupon');
		let couponList;
		
		$(document).ready(function(){
			
			tagType.change(changeForm);
			
		}); // end document.ready
		
		$('form').submit(function(event){
			let type = tagType.val();
			let title = $('.title').val();
			let content = $('.content').val();
			let selectCoupon = tagSelectCoupon.val();
			
			if(title.length == 0 || content.length == 0){
				alert('제목과 내용을 모두 입력해주세요');
				event.preventDefault();
				return;
			}
			
			if(title.length > 30){
				alert('제목은 20자까지 가능합니다');
				event.preventDefault();
				return;
			}
			
			if(content.length > 100){
				alert('내용은 100자까지 가능합니다.');
				event.preventDefault();
				return;
			}
			
			if(type == 'coupon' && selectCoupon == undefined){
				alert('쿠폰을 선택해주세요');
				event.preventDefault();
				return;
			}
		}); // end form.submit
		
		function changeForm(){
			let type = tagType.val();
			console.log('type 변경 : ' + type);
			
			if(type == 'coupon'){
				tagSelectCoupon.attr('hidden', false);
				if(couponList == undefined){
					$.ajax({
						method : 'GET',
						url : '../coupon/publish',
						success : function(result){
							couponList = result;
							setCouponList(couponList);
						}
					}); // end ajax
				}
			}else{
				tagSelectCoupon.attr('hidden', true);
				tagSelectCoupon.val('');
			}
			
		} // end changeForm
	
		function setCouponList(list){
			let optionForm = '';
			
			for(x in list){
				optionForm += '<option value="' + list[x].couponId + '">' + 
						'쿠폰 코드 : ' + list[x].couponId + 
						' / 쿠폰명 : ' + list[x].couponName + 
						' / 할인률 : ' + list[x].discount + '</option>';
			}
			tagSelectCoupon.append(optionForm);
		} // end setCouponList
		
	</script>
	
</body>
</html>