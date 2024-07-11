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

.selected {
	background-color: red;
}
.unselected {
	background-color: white;
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
.popupAdsPage {
	display: flex;
	justify-content: center;
	margin-top: 4px;
}
</style>
<title>팝업 광고 관리</title>
</head>
<body>
	<jsp:include page="../include/sideBar.jsp"/>
	<div class="body_container">
		<div class="inner_header">
			<h2>등록된 팝업 광고 목록</h2>
		</div>
		<div style="height: 200px;">
			<div class="popupAdsHeader tableList">
				<div class="messageId col">팝업 코드</div>
				<div class="type col">분류</div>
				<div class="title col">제목</div>
				<div class="content col">내용</div>
				<div class="dateCreated col">등록일</div>
			</div>
			<div class="popupAdsList"></div>
		</div>
			<div class="popupAdsPage"></div>
		<div>
			<input type="button" class="btn btn-outline-primary" value="등록" onclick="registerPopupAds()">
			<input type="button" class="btn btn-outline-danger" value="삭제" onclick="deletePopupAds()">
		</div>
	</div>

	<script type="text/javascript">
		let popupAdsList;
		let pageMaker;
		let selectedList = [];
		
		$(document).ready(function(){
			loadPopupAdsList();
		}); // end document.ready
	
	
		function loadPopupAdsList(page) {
			if (page == undefined) {
				page = 1;
			}

			$.ajax({
				method : 'GET',
				url : 'list?pageNum=' + page,
				success : function(result) {
					console.log(result);
					popupAdsList = result.list;
					pageMaker = result.pageMaker;

					$('.popupAdsList').html(makeListForm(popupAdsList));
					$('.popupAdsPage').html(makePageForm(pageMaker, loadPopupAdsList));
				}
			});
		} // end loadCouponList
		
		function makeListForm(list) {
			let listForm = '';
			let isSelected
			//let isSelected;
			for(x in list){
				isSelected = selectedList.includes(list[x].couponId) ? "selected" : "unselected";
				listForm += 
					'<div class="popupAdsItem tableList ' + isSelected + '" onclick="select(this)">' +
						'<span class="messageId col">' + list[x].messageId + '</span>' + 
						'<span class="type col">' + list[x].type + '</span>' +
						'<span class="title col">' + list[x].title + '</span>' +
						'<span class="content col">' + list[x].content + '</span>' +
						'<span class="dateCreated col">' + toDate(list[x].dateCreated) + '</span>' +
					'</div>';
			}
			return listForm;
		} // end makeListForm
		
		function makePageForm(pageMaker){
			
			
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
		function makePageForm(pageMaker, listener){
			const startNum = pageMaker.startNum;
			const endNum = pageMaker.endNum;
			const curPage = pageMaker.curPage;
			
			let pageForm = $('<ul class="pageList pagination"></ul>');
			let numForm;
			
			numForm = $('<li class="page-item"><a class="page-link">&laquo;</a></li>');
			// 이전 페이지가 있으면 클릭 리스너 등록, 없으면 disabled
			if(pageMaker.prev){
				numForm.click(function() {
					listener(startNum -1);
				});	
			}else {
				numForm.addClass('disabled');
			}
			pageForm.append(numForm);
			
			for(let x = startNum; x <= endNum; x++){
				numForm = $('<li class="page-item"><a class="page-link">' + x + '</a></li>').click(function(){
					listener(x);
				});
				if(curPage == x) { // 현재 페이지 번호는 색 변경
					numForm.addClass('active');
				}
				pageForm.append(numForm);
			}
			
			numForm = $('<li class="page-item"><a class="page-link">&raquo;</a></li>');
			if(pageMaker.next){
				numForm.click(function(){
					listener(endNum +1);
				});
			}else{
				numForm.addClass('disabled');
			}
			pageForm.append(numForm);
			
			return pageForm;
		} // end makePageForm
		
		function toDate(timestamp){
			let date = new Date(timestamp);
			let formatted = (date.getYear() + 1900) + '/' + (date.getMonth() + 1) + '/' + date.getDate();
			return formatted;
		} // end toDate
		
		function select(input) {
			let selectedId = parseInt($(input).find('.messageId').text());
			
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
		
		function registerPopupAds(){
			
			const popupStat = {
					'url' : 'register',
					'name' : 'popupRegister',
					'option' : 'width=1000, height=600, top=50, left=400'
			};
			
			// 팝업 창 띄우기
			let popup = window.open(popupStat.url, popupStat.name, popupStat.option);
			popup.onbeforeunload = function(){
				// 팝업 닫힐 때 실행
				console.log("팝업 닫힘");
				loadPopupAdsList(pageMaker.pagination.pageNum);
			} // end popup.onbeforeunload
			
		} // end registerPopupAds
		
		
		function deletePopupAds() {
			if(selectedList.length == 0){
				alert('삭제할 팝업 광고를 선택해주세요');
				return;
			}
			let deleteCheck = confirm(selectedList.length + '개 항목이 삭제됩니다. 정말 삭제하시겠습니까?');
			
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
						alert(result + '개 항목이 삭제되었습니다.');
						loadPopupAdsList(pageMaker.pagination.pageNum);
					} // end success
				}); // end ajax
			}
		} // end deletePopupAds
		
		
	</script>

</body>
</html>