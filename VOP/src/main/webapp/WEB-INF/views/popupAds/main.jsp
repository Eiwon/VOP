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
.messageId {
	width: 100px;
}
.type {
	width: 100px;
}
.title {
	width : 200px;
}
.content {
	width : 200px;
}
.dateCreated{
	width : 200px;
}
.selected {
	background-color: red;
}
.unselected {
	background-color: white;
}
</style>
<title>팝업 광고 관리</title>
</head>
<body>
	<div class="formContainer">
		<div>
			<h2>등록된 팝업 광고 목록</h2>
		</div>
		<div>
			<div class="popupAdsHeader tableList">
				<div class="messageId">팝업 코드</div>
				<div class="type">분류</div>
				<div class="title">제목</div>
				<div class="content">내용</div>
				<div class="dateCreated">등록일</div>
			</div>
			<div class="popupAdsList"></div>
			<div class="popupAdsPage"></div>
		</div>
		<div>
			<input type="button" value="등록" onclick="registerPopupAds()">
			<input type="button" value="삭제" onclick="deletePopupAds()">
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
						'<span class="messageId">' + list[x].messageId + '</span>' + 
						'<span class="type">' + list[x].type + '</span>' +
						'<span class="title">' + list[x].title + '</span>' +
						'<span class="content">' + list[x].content + '</span>' +
						'<span class="dateCreated">' + toDate(list[x].dateCreated) + '</span>' +
					'</div>';
			}
			return listForm;
		} // end makeListForm
		
		
		function makePageForm(pageMaker, listener){
			const startNum = pageMaker.startNum;
			const endNum = pageMaker.endNum;
			
			let pageForm = $('<ul class="pageList"></ul>');
			let numForm;
			if(pageMaker.prev){
				numForm = $('<li>이전&nbsp&nbsp</li>').click(function() {
					listener(startNum -1);
				});
				pageForm.append(numForm);
			}
			for(let x = startNum; x <= endNum; x++){
				numForm = $('<li>' + x + '&nbsp&nbsp</li>').click(function(){
					listener(x);
				});
				pageForm.append(numForm);
			}
			if(pageMaker.next){
				numForm = $('<li>다음</li>').click(function(){
					listener(endNum +1);
				});
				pageForm.append(numForm);
			}
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
						'Content-type' : 'application/json'
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