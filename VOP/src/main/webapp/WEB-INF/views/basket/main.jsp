<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<title>장바구니</title>
</head>
<body>
	
	<div id="basket_container">
	
	
	</div>
	
	<div class="product_container">
		<table>
			<tbody id="basket_list">
				<tr class="basket_item">
					<td><img src="../product/showImg?productId="></td>
					<td class="product_id" hidden="hidden"/>
					<td class="product_name"></td>
					<td class="product_num"></td>
					<td class="product_price"></td>
					<td>
						<button>-</button>
						<input type="number">
						<button>+</button>
					</td>
					<td><a>삭제</a></td>
					<td></td>
				</tr>
			</tbody>
		</table>
	</div>	
	
	<script type="text/javascript">
		const memberId = '<%= request.getSession().getAttribute("memberId")%>';
		let pageNum = 1;
		
		$(document).ready(function(){
				printBasketList();
			
		}); // end document.ready
		
		
		// 장바구니 물품 출력
		function printBasketList(){
			$.ajax({
				method : 'GET',
				url : 'myBasket?memberId=' + memberId + '&pageNum=' + pageNum,
				success : function(result){
					console.log(result);
					const basketList = result.basketList;
					let str = "";
					for(x in basketList){
						
					}
					
				}
			}); // end ajax
		} // end printBasketList
		
	</script>
	
</body>
</html>



