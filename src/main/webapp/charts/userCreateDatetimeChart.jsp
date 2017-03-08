<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	var userCreateDatetimeChart = '${userCreateDatetimeChart}';
	var data = eval("(" + userCreateDatetimeChart + ")");
	var countUser = 0;
	for ( var i = 0; i < data.length; i++) {
		countUser += data[i];
	}
	$(function() {
		$.post('${pageContext.request.contextPath}/bugController/reclo', function(result) {

			
					
					var datas = eval('(' + result + ')');
		
			
			$('#container').highcharts({
				credits : {
					enabled : false
				},
				chart : {
					type : 'column',
					margin : [ 50, 50, 100, 80 ]
				},
				title : {
					text : '活跃用户数(总用户数：' + countUser + ')'
				},
				xAxis : {
					categories : datas.users,
					labels : {
						rotation : -45,
						align : 'right',
						style : {
							fontSize : '13px',
							fontFamily : 'Verdana, sans-serif'
						}
					}
				},
				yAxis : {
					min : 0,
					title : {
						text : '操作数量'
					}
				},
				legend : {
					enabled : false
				},
				tooltip : {
					formatter : function() {
						return '<b>' + this.x + ' 童鞋</b><br/>' + '总共的操作行为数量有: ' + this.y + ' 次';
					}
				},
				series : [ {
					data :datas.nums,
					dataLabels : {
						enabled : true,
						rotation : -90,
						color : '#FFFFFF',
						align : 'right',
						x : 4,
						y : 10,
						style : {
							fontSize : '13px',
							fontFamily : 'Verdana, sans-serif'
						}
					}
				} ]
			});

			parent.$.messager.progress('close');
		});
			
		}); 
		
</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit : true,border : false">
		<div data-options="region:'center',border:false">
			<div id="container"></div>
		</div>
	</div>
</body>
</html>