<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<jsp:include page="../include/header.jsp"></jsp:include>
<style type="text/css">
.formContainer{
	border: 1px solid black;
}

.pageList {
	display: flex;
	flex-direction: row;
	list-style: none;
}

.tableList{
	display: flex;
	flex-direction: row;
}
.couponId {
	width: 100px;
}
.couponName {
	width: 200px;
}
.discount {
	width : 100px;
}
.dateCreated {
	width : 200px;
}
.selected {
	background-color: red;
}
.unselected {
	background-color: white;
}
</style>
<title>쿠폰 관리</title>
</head>
<body>
	<div class="formContainer">
		<div>
			<h2>등록된 쿠폰 목록</h2>
		</div>
		<div>
			<div class="couponHeader tableList">
				<div class="couponId">쿠폰 코드</div>
				<div class="couponName">쿠폰명</div>
				<div class="discount">할인률</div>
				<div class="dateCreated">등록일</div>
				<div class="publishing">배포 상태</div>
			</div>
			<div class="couponList"></div>
			<div class="couponPage"></div>
		</div>
		<div>
			<input type="button" value="쿠폰 등록" onclick="registerCoupon()">
			<input type="button" value="삭제" onclick="deleteCoupon()">
			<input type="button" value="배포" onclick="publishCoupon(1)">
			<input type="button" value="배포 취소" onclick="publishCoupon(0)">
		</div>
	</div>
	<script type="text/javascript">
		let couponList;	
		let pageMaker;
		let selectedList = [];
		
		$(document).ready(function(){
			loadCouponList();
		}); // end document.ready
		
		
		function loadCouponList(page) {
			if(page == undefined){
				page = 1;
			}
			
			$.ajax({
				method : 'GET',
				url : 'list?pageNum=' + page,
				success : function(result) {
					console.log(result);
					couponList = result.list;
					pageMaker = result.pageMaker;
					
					$('.couponList').html(makeListForm(couponList));
					$('.couponPage').html(makePageForm(pageMaker));
				}
			});
		} // end loadCouponList
	

		function makeListForm(list) {
			let listForm = '';
			let isSelected;
			let isPublishing;
			for(x in list){
				isSelected = selectedList.includes(list[x].couponId) ? "selected" : "unselected";
				isPublishing = list[x].publishing == '0' ? '미배포' : '배포중';
				listForm += 
					'<div class="couponItem tableList ' + isSelected + '" onclick="select(this)">' +
						'<span class="couponId">' + list[x].couponId + '</span>' + 
						'<span class="couponName">' + list[x].couponName + '</span>' +
						'<span class="discount">' + list[x].discount + '%</span>' +
						'<span class="dateCreated">' + toDate(list[x].dateCreated) + '</span>' +
						'<span class="publishing">' + isPublishing + '%</span>' +
					'</div>';
			}
			return listForm;
		} // end makeListForm
		
		
		function makePageForm(pageMaker){
			const startNum = pageMaker.startNum;
			const endNum = pageMaker.endNum;
			
			let pageForm = $('<ul class="pageList"></ul>');
			let numForm;
			if(pageMaker.prev){
				numForm = $('<li>이전&nbsp&nbsp</li>').click(function() {
					loadCouponList(startNum -1);
				});
				pageForm.append(numForm);
			}
			for(let x = startNum; x <= endNum; x++){
				numForm = $('<li>' + x + '&nbsp&nbsp</li>').click(function(){
					loadCouponList(x);
				});
				pageForm.append(numForm);
			}
			if(pageMaker.next){
				numForm = $('<li>다음</li>').click(function(){
					loadCouponList(endNum +1);
				});
				pageForm.append(numForm);
			}
			return pageForm;
		} // end makePageForm
		

		function select(input) {
			let selectedId = parseInt($(input).find('.couponId').text());
			
			if(selectedList.includes(selectedId)){
				selectedList.splice(selectedList.indexOf(selectedId));
				$(input).removeClass('selected');
				$(input).addClass('unselected');
			}else{
				selectedList.push(selectedId);
				$(input).addClass('selected');
				$(input).removeClass('unselected');
			}
			
		} // end select
		
		function registerCoupon() {
			
			const popupStat = {
					'url' : 'register',
					'name' : 'popupCouponRegister',
					'option' : 'width=1000, height=600, top=50, left=400'
			};
			
			// 팝업 창 띄우기
			let popup = window.open(popupStat.url, popupStat.name, popupStat.option);
			popup.onbeforeunload = function(){
				// 팝업 닫힐 때 실행
				console.log("팝업 닫힘");
				loadCouponList(pageMaker.pagination.pageNum);
			} // end popup.onbeforeunload
			
		} // end registerCoupon
		
		function deleteCoupon() {
			if(selectedList.length == 0){
				alert('삭제할 쿠폰을 선택해주세요');
				return;
			}
			let deleteCheck = confirm(selectedList.length + '개 쿠폰이 삭제됩니다. 정말 삭제하시겠습니까?');
			
			if(deleteCheck){
				$.ajax({
					method : 'DELETE',
					url : 'delete',
					headers : {
						'Content-type' : 'application/json'
					},
					data : JSON.stringify(selectedList),
					success : function(result){
						selectedList = [];
						alert(result + '개 쿠폰이 삭제되었습니다.');
						loadCouponList(pageMaker.pagination.pageNum);
					} // end success
				}); // end ajax
			}
			
		} // end deleteCoupon
		
		function publishCoupon(publishing){
			if(selectedList.length == 0){
				alert('변경할 쿠폰을 선택해주세요');
				return;
			}
			
			$.ajax({
				method : 'PUT',
				url : 'publish/' + publishing,
				success : function(result){
					if(result == 1){
						alert('변경 성공');
						loadCouponList(pageMaker.pagination.pageNum);
					}else {
						alert('변경 실패');
					}
				}
			});
			
		} // end publishCoupon
		
		function toDate(timestamp){
			let date = new Date(timestamp);
			let formatted = (date.getYear() + 1900) + '/' + (date.getMonth() + 1) + '/' + date.getDate();
			return formatted;
		} // end toDate
		
		
	</script>

</body>
</html>