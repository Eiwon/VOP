<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="${_csrf.parameterName }" content="${_csrf.token }">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<jsp:include page="../include/header.jsp"></jsp:include>
<style type="text/css">

.pageList {
	display: flex;
	flex-direction: row;
	list-style: none;
}

.tableList{
	display: flex;
	flex-direction: row;
}
.body_container{
	width: 65%;
	margin: auto;
	height: 500px;
}
.inner_header {
	margin: 40px;
}
.form_foot {
	display: flex;
	justify-content: center;
	margin-top: 4px;
}
.selected {
	background-color: red;
}
.unselected {
	background-color: white;
}
.couponPage {
	display: flex;
	justify-content: center;
	margin-top: 4px;
}
</style>
<title>쿠폰 관리</title>
</head>
<body>
	<jsp:include page="../include/sideBar.jsp"/>
	<div class="body_container ">
		<div class="inner_header">
			<h2>등록된 쿠폰 목록</h2>
		</div>
		<div style="height: 200px;">
			<div class="couponHeader tableList">
				<div class="couponId col">쿠폰 코드</div>
				<div class="couponName col">쿠폰명</div>
				<div class="discount col">할인률</div>
				<div class="dateCreated col">등록일</div>
				<div class="publishing col">배포 상태</div>
			</div>
			<div class="couponList"></div>
		</div>
		
		<div class="couponPage"></div>
		
		<div>
			<input type="button" class="btn btn-outline-primary" value="쿠폰 등록" onclick="registerCoupon()">
			<div class="btn-group" role="group">
			<input type="button" class="btn btn-outline-success" value="배포" onclick="publishCoupon(1)">
			<input type="button" class="btn btn-outline-success" value="배포 취소" onclick="publishCoupon(0)">
			</div>
			<input type="button" class="btn btn-outline-danger" value="삭제" onclick="deleteCoupon()">
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
						'<span class="couponId col">' + list[x].couponId + '</span>' + 
						'<span class="couponName col">' + list[x].couponName + '</span>' +
						'<span class="discount col">' + list[x].discount + '%</span>' +
						'<span class="dateCreated col">' + toDate(list[x].dateCreated) + '</span>' +
						'<span class="publishing col">' + isPublishing + '</span>' +
					'</div>';
			}
			return listForm;
		} // end makeListForm
		
		function makePageForm(pageMaker){
			const startNum = pageMaker.startNum;
			const endNum = pageMaker.endNum;
			const curPage = pageMaker.curPage;
			
			let pageForm = $('<ul class="pageList pagination"></ul>');
			let numForm;
			
			numForm = $('<li class="page-item"><a class="page-link">&laquo;</a></li>');
			// 이전 페이지가 있으면 클릭 리스너 등록, 없으면 disabled
			if(pageMaker.prev){
				numForm.click(function() {
					loadCouponList(startNum -1);
				});	
			}else {
				numForm.addClass('disabled');
			}
			pageForm.append(numForm);
			
			for(let x = startNum; x <= endNum; x++){
				numForm = $('<li class="page-item"><a class="page-link">' + x + '</a></li>').click(function(){
					loadCouponList(x);
				});
				if(curPage == x) { // 현재 페이지 번호는 색 변경
					numForm.addClass('active');
				}
				pageForm.append(numForm);
			}
			
			numForm = $('<li class="page-item"><a class="page-link">&raquo;</a></li>');
			if(pageMaker.next){
				numForm.click(function(){
					loadCouponList(endNum +1);
				});
			}else{
				numForm.addClass('disabled');
			}
			pageForm.append(numForm);
			
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
					'option' : 'width=600, height=300, top=50, left=400'
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
						'Content-type' : 'application/json',
						'X-CSRF-TOKEN' : $('meta[name="${_csrf.parameterName }"]').attr('content')
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
				headers : {
					'Content-type' : 'application/json',
					'X-CSRF-TOKEN' : $('meta[name="${_csrf.parameterName }"]').attr('content')
				},
				data : JSON.stringify(selectedList),
				success : function(result){
					if(result > 0){
						alert('변경 성공');
						loadCouponList(pageMaker.pagination.pageNum);
					}else {
						alert('변경 실패');
					}
				} // end success
			}); // end ajax
			
		} // end publishCoupon
		
		function toDate(timestamp){
			let date = new Date(timestamp);
			let formatted = (date.getYear() + 1900) + '/' + (date.getMonth() + 1) + '/' + date.getDate();
			return formatted;
		} // end toDate
		
		
	</script>

</body>
</html>