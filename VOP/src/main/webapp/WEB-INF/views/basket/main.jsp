<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="${_csrf.parameterName }" content="${_csrf.token }">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<style type="text/css">
.col_check{
	width: 50px;
}
.col_img{
	width: 100px;
}
.col_name{
	width: 150px;
}
.col_price{
	width: 100px;
}
.col_remains{
	width: 100px;
}
.col_total{
	width: 100px;
}
.col_num{
	width: 400px;
}
</style>
<title>장바구니</title>
</head>
<jsp:include page="../include/header.jsp"></jsp:include>
<body>
	
	<div id="basket_container">
	
	</div>
	
	<div id="product_container">
		<button id="btn_clear">장바구니 비우기</button>
		<table>
			<thead>
				<tr>
					<td class="col_check">선택</td>
					<td class="col_img">이미지</td>
					<td class="col_name">상품명</td>
					<td class="col_price">가격</td>
					<td class="col_remains">남은 수량</td>
					<td class="col_total">총액</td>
					<td class="col_num">선택 수량</td>
				</tr>
			</thead>
			<tbody id="basket_list"></tbody>
		</table>
		<input id="chk_select_all" type="checkbox">
		<input id="btn_delete" type="button" value="선택 삭제">
	</div>	
	
	<form action="../payment/checkout" method="POST" id="form_expense">
		<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
		<div id="order_list" hidden="hidden"></div>
		<div id="total_product_price"></div>
		<input type="button" value="결제하기" onclick="submitOrder()">
	</form>
	
	<script type="text/javascript">
		let pageNum = 1;
		let tagBasketList = $('#basket_list');
		let basketMap; // 데이터를 html과 분리해서 관리하기 위한 map
		let formExpense = $('#form_expense');
		
		$(document).ready(function(){
				printBasketList();

				$('#btn_clear').click(function(){
					clearBasket();
				}); // end btn_clear.click
				
				$('#chk_select_all').change(function(){ // "전체 선택" 체크 박스에 이벤트 등록(클릭시 모든 체크박스 상태를 자신과 같게 변경)
					let chkAll = $(this).prop("checked");
					
					$('.chk_product').each(function(){
						$(this).prop("checked", chkAll);
					});
					calcTotalExpense();
				}); // end chk_select_all.change
				$('#btn_delete').click(function(){
					multiDelete();
				}); // end btn_delete
		}); // end document.ready
		
		
		// 장바구니 물품 출력
		function printBasketList(){
			$.ajax({
				method : 'GET',
				url : 'myBasket',
				success : function(result){
					console.log(result);
					const basketList = result;
					initBasketMap(basketList);
					let str = "";
					for(x in basketMap){
						const productVO = basketMap[x];
						str += '<tr class="basket_item">' +
							'<td class="col_check"><input type="checkbox" class="chk_product"></td>' +
							'<td class="col_img"><img src="' + productVO.imgUrl + '"></td>' +
							'<td class="product_id" hidden="hidden">' + productVO.productId + '</td>' +
							'<td class="product_name col_name">' + productVO.productName + '</td>' +
							'<td class="product_price col_price">' + productVO.productPrice + '원</td>' +
							'<td class="product_remains col_remains">' + productVO.productRemains + '개</td>' +
							'<td class="total_price col_total">' + (productVO.productPrice * productVO.productNum) + '원</td>' +
							'<td class="col_num btn_num">' +
								'<button class="btnMinus">-</button>' +
								'<input class="product_num" type="number" value=' + productVO.productNum + '>' +
								'<button class="btnPlus">+</button>' +
							'</td>' +
							'<td><a class="btnDelete">삭제</a></td>' +
							'<td></td>' +
							'</tr>';
					} // end for
					tagBasketList.html(str);
					calcTotalExpense();
					
					$('.basket_item').each(function(){
						$(this).find('.btnMinus').click(function(){
							minusProductNum(this);
						});
						$(this).find('.btnPlus').click(function(){
							plusProductNum(this);
						});
						$(this).find('.product_num').blur(function(){
							changeProductNum(this);
						});
						$(this).find('.btnDelete').click(function(){
							deleteProduct(this);
						});
						$(this).find('.chk_product').change(function(){
							if(!$(this).prop("checked")){ // check를 해제하면 "전체 선택" 체크박스도 해제
								$('#chk_select_all').prop("checked", false);
							}
							calcTotalExpense();
						});
						$(this).find('.product_name').click(function(){
							let productId = getTargetId(this);
							location.href = '../product/detail?productId=' + productId;
						});
					});
				} // end success
			}); // end ajax
		} // end printBasketList
		
		function submitOrder(){
			let orderList = [];
			let tagOrderList = $('#order_list');
			let form = "";
			
			// 체크된 물품들의 id, 갯수를 가져온다.
			$('.chk_product').each(function(){
				if($(this).prop('checked') == true){
					const productId = getTargetId(this);
					const order = {
							'productId' : productId,
							'purchaseNum' : basketMap[productId].productNum
					}
					orderList.push(order);
					// 전달할 값을 form에 hidden으로 추가 
					form += '<input type="hidden" name="productIds" value="' + productId + '">';
					form += '<input type="hidden" name="productNums" value="' + basketMap[productId].productNum + '">';
				}
			});
			console.log(orderList);
			
			if(orderList.length == 0){
				alert("구매할 물품을 선택해주세요.");
				event.preventDefault();
				return;
			}
			
			tagOrderList.html(form);
			
			// productId[], productNum[] input 태그 생성 후 submit
			formExpense.submit();
			
		} // end submit
		
		
		
		// productNum -1이 가능한지 체크 후 update 요청 함수 호출
		function minusProductNum(input){
			let basketItem = $(input).parents('.basket_item');
			let targetId = getTargetId(input);
			let targetProductNum = basketMap[targetId].productNum;
			
			console.log("minusProductNum() - 클릭된 상품 id : " + targetId);
			
			if(targetProductNum == 1) return;
			
			setProductNum(basketItem, targetId, targetProductNum - 1);
		} // end downProductNum
		
		// productNum +1이 가능한지 체크 후 update 요청 함수 호출
		function plusProductNum(input){
			let basketItem = $(input).parents('.basket_item');
			let targetId = getTargetId(input);
			let targetProductNum = basketMap[targetId].productNum;
			let maxNum = basketMap[targetId].productRemains;
			
			console.log("downProductNum() - 클릭된 상품 id : " + targetId);
			
			if(maxNum == targetProductNum)	return;
			
			setProductNum(basketItem, targetId, targetProductNum + 1);
		} // end downProductNum
		
		// productNum 직접 수정시, 가능한 수인지 체크 후 update 요청 함수 호출
		function changeProductNum(input){
			let targetProductNum = $(input).val();
			let basketItem = $(input).parents('.basket_item');
			let targetId = getTargetId(input);
			let maxNum = basketMap[targetId].productRemains;
			
			if(targetProductNum < 1){
				targetProductNum = 1;
			}else if(targetProductNum > maxNum){
				targetProductNum = maxNum;
			}
			setProductNum(basketItem, targetId, targetProductNum);
		} // end changeProductNum
		
		// 클릭한 삭제 버튼이 속해있는 상품 삭제
		function deleteProduct(input){
			let targetId = getTargetId(input);
			
			$.ajax({
				method : 'DELETE',
				url : 'myBasket',
				headers : {
	                'Content-Type' : 'application/json',
	                'X-CSRF-TOKEN' : $('meta[name="${_csrf.parameterName }"]').attr('content')
	            },
				data : targetId,
				success : function(result){
					console.log(result);
					printBasketList();
				}
			});
			
		} // end deleteProduct
		
		// 선택된 모든 상품 삭제
		function multiDelete(){
			let targetList = [];
			
			$('.chk_product').each(function(){ // 모든 상품의 체크박스에 접근하여, 체크되어있으면 그 id를 targetList에 등록
				if($(this).prop("checked")){
					targetList.push(getTargetId(this));
				}
			});
			console.log("체크된 상품 : " + targetList);
			if(targetList.length == 0) return;
			
			$.ajax({
				method : 'DELETE',
				url : 'multi',
				headers : {
	                  'Content-Type' : 'application/json',
	                  'X-CSRF-TOKEN' : $('meta[name="${_csrf.parameterName }"]').attr('content')
	            },
	            data : JSON.stringify(targetList),
				success : function(result){
					console.log(result);
					printBasketList();
				}
			}); // end ajax
		} // end multiDelete
		
		// 장바구니 비우기
		function clearBasket(){
			$.ajax({
				method : 'DELETE',
				headers : {
					'X-CSRF-TOKEN' : $('meta[name="${_csrf.parameterName }"]').attr('content')
				},
				url : 'clear',
				success : function(result){
					console.log(result);
					printBasketList();
				}
			}); // end ajax
		} // end clearBasket
		
		// productNum update 요청 송신
		function setProductNum(basketItem, productId, productNum){
			const obj = {
				'productId' : productId,
				'productNum' : productNum
			};
			
			$.ajax({
				method : 'PUT',
				url : 'myBasket',
				headers : {
	                  'Content-Type' : 'application/json',
	                  'X-CSRF-TOKEN' : $('meta[name="${_csrf.parameterName }"]').attr('content')
	            }, 
				data : JSON.stringify(obj),
				success : function(result){
					basketMap[productId].productNum = productNum;
					basketItem.find('.product_num').val(basketMap[productId].productNum);
					basketItem.find('.total_price').text(basketMap[productId].productNum * basketMap[productId].productPrice);
					calcTotalExpense();
				} // end success
			});  // end ajax
		}// end setProductNum
		
		// 예상 주문 금액 계산
		function calcTotalExpense(){
			let totalExpense = 0;
			
			$('.chk_product').each(function(){
				if($(this).prop("checked")){
					let productId = getTargetId(this);
					totalExpense += basketMap[productId].productPrice * basketMap[productId].productNum;
				}
			});
			
			console.log("총 예상 금액 : " + totalExpense);
			let str = '<span>총 상품 가격 <strong>' + totalExpense + '</strong>원</span>';
			$('#total_product_price').html(str);
			
		} // end calcTotalExpense
		
		// 데이터 관리 맵 초기화 (key : productId, value : basketDTO)
		function initBasketMap(basketList){
			console.log("map init");
			basketMap = {};
			for(x in basketList){
				const productVO = basketList[x].productPreviewDTO.productVO;
				basketMap[productVO.productId] = productVO;
				basketMap[productVO.productId].imgUrl = basketList[x].productPreviewDTO.imgUrl;
				basketMap[productVO.productId].productNum = basketList[x].productNum;
			}
		} // end setBasketMap
		
		// 입력된 element가 속해있는 상품의 productId 리턴
		function getTargetId(input){
			return $(input).parents('.basket_item').find('.product_id').text();
		} // end getTargetId
		
	</script>
	
</body>
</html>



