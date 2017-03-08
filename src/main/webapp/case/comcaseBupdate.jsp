<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');
        var nodeee='${caseid}';

		
		
		$('#pid').combobox({
			valueField:'id', //值字段
			    textField:'name', //显示的字段
			    url:'${pageContext.request.contextPath}/caseController/projectchild?child='+'${case}',
			    panelHeight:'auto',
				editable:true,				
			});
		
		
		var Address = [{ "id": "P1", "text": "P1" }, { "id": "P2", "text": "P2" }, { "id": "P3", "text": "P3" }, { "id": "P4", "text": "P4" }, { "id": "P5", "text": "P5" }];
		$('#op').combobox({ 
			data:Address, 
			valueField:'id', 
			textField:'text',
		    panelHeight:'auto',
		    editable:true,//不可编辑，只能选择
			});  

	

		$('#form').form({
			url : '${pageContext.request.contextPath}/caseController/bedit?id='+nodeee,
			onSubmit : function() {
				parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				var isValid = $(this).form('validate');
				if (!isValid) {
					parent.$.messager.progress('close');
				}
				return isValid;
			},
			success : function(result) {
				parent.$.messager.progress('close');
				result = $.parseJSON(result);
				if (result.success) {
					parent.$.modalDialog.openner_treeGrid.treegrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_treeGrid这个对象，是因为resource.jsp页面预定义好了
					parent.$.modalDialog.openner_treeGrid.treegrid('clearSelections');
					parent.$.modalDialog.openner_treeGrid.treegrid('clearChecked')
					parent.$.modalDialog.handler.dialog('close');
				}
			}
		});
	});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
		<form id="form" method="post">
			<table class="table table-hover table-condensed">
				<tr>
					<th>执行级</th>
					<td><select name="op" id="op" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
							<%-- <c:forEach items="${resourceTypeList}" var="resourceType">
								<option value="${resourceType.id}">${resourceType.name}</option>
							</c:forEach> --%>
					</select></td>
					<th>上级目录</th>
					<td><select id="pid" name="pid" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" style="width: 140px; height: 29px;"></select></td>
				</tr>
			</table>
		</form>
	</div>
</div>
