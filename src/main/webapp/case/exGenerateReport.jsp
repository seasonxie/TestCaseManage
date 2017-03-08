<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	$(function() {
		
		parent.$.messager.progress('close');
		
	});

	function sent(){
		var project=parent.$('#layout_west_tree2').tree('getSelected')
		console.log(project.template)
		parent.$.messager.progress({
			title : '提示',
			text : '数据处理中，请稍后....'
		});
		 $.post('${pageContext.request.contextPath}/excaseController/generateReport',{"treetemplate":project.template,"treeid":project.id,"faileddetail":'${faileddetail}',"parentproject":'${parentproject}',"failed":'${failed}',"caseproject":'${caseproject}',"failedv":'${failedv}',"casesnum":'${casesnum}',"undo":'${undo}',"undov":'${undov}'},function(data){	
				parent.$.messager.progress('close');
			 var myobj=eval(data);  			
			 parent.$.modalDialog.handler.dialog('close');
			
		
			 var path=myobj[0].url
			 
			 var uuu='${pageContext.request.contextPath}'+path+'.html'
			 window.open(uuu)
			// parent.$.messager.alert('提示', myobj[0].uri, 'info');
			// window.location.href=uuu
		
		 });
		 
		 
	}
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
		<form id="form" method="post">
			<table class="table table-hover table-condensed">
				<tr>
					<th>执行用例名称:</th>
					<td>${parentproject} -- ${caseproject}</td>		
					<th>总用例数:</th>
				    <td>${casesnum}</td>	
				</tr>	
				
				<tr>
				<th>失败用例数:</th>
				<td>${failed}</td>
				<th>失败率:</th>
				<td>${failedv}</td>
				</tr>	
					
					<tr>
					<th>未执行用例数:</th>
					<td>${undo}</td>
				    <th>完成率:</th>
					<td>${undov}</td>
				   </tr>	
				   	
				
		
			</table>
			
			<br><br><br><br>
			
			<span style="float:right;">
			【请注意报告是否被浏览器拦截弹出】
			<a onclick="sent();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'custer_baobiao'">生成静态报告</a></span>
		</form>
	</div>
</div>