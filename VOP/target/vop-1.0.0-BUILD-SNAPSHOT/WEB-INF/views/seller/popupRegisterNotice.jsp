<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="${_csrf.parameterName }" content="${_csrf.token }">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<title>공지사항 등록</title>
</head>
<body>
	<h2>공지사항 등록</h2>
	<div>
		<div>
			제목 <input type="text" id="title" name="title">
		</div>
		<div>
			<div>내용</div> 
			<textarea rows="30" cols="30" id="content" name="content"></textarea>
		</div>
		<div>
			<input type="button" id="btnSubmit" value="등록">
		</div>
	</div>
	
	<script type="text/javascript">
		
		$(document).ready(function(){
			$('#btnSubmit').click(function(){
				let title = $('#title').val();
				let content = $('#content').val();
				
				if(title.length == 0 || content.length == 0){
					alert('입력되지 않은 항목이 있습니다.');
					return;
				}
				
				$.ajax({
					method : 'POST',
					url : 'notice',
					headers : {
						'Content-Type' : 'application/json',
						'X-CSRF-TOKEN' : $('meta[name="${_csrf.parameterName }"]').attr('content')
					},
					data : JSON.stringify({
						'title' : title,
						'content' : content
					}),
					success : function(result){
						if(result == 1){
							alert('공지사항 등록 성공');
							window.close();
						}else{
							alert('공지사항 등록 실패');
						}	
					}
				}); // end ajax
				
			}); // end btnSubmit.click
		}); // end document.ready
	
	</script>
	
</body>
</html>