<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	$(function() {
		
		parent.$.messager.progress('close');
		
	});
	
	
    function contains(string,substr) {     	
 	   var startChar=substr.substring(0,1); 
 	   var strLen=substr.length; 
 	   for(var j=0;j<string.length-strLen+1;j++) { 
 	   if(string.charAt(j)==startChar){ 
 	   if(string.substring(j,j+strLen)==substr){//如果从j开始的字符与str匹配，那ok { 
 	   return true; }   
 	    } } 
 	   return false; 
 	   }
	
	var Address = [{ "id": "Flyme4", "text": "Flyme4" }, { "id": "Flyme5", "text": "Flyme5" }, { "id": "Flyme5.1", "text": "Flyme5.1" }];
	$('#fversion').combobox({ 
		data:Address, 
		valueField:'id', 
		textField:'text',
	    panelHeight:'auto',
	    editable:true,//不可编辑，只能选择
		});  

	function sent(){
		
		var isValid = $('#form').form('validate');
		if (!isValid) {
			return isValid;
		}
		
		parent.$.messager.progress({
			title : '提示',
			text : '数据处理中，请稍后....'
		});
		
		$.post('${pageContext.request.contextPath}/excaseController/redmineDo',$("#form").serializeArray(),function(data){ 
			parent.$.messager.progress('close');
			var info=eval(data); 
			// console.log(info)
			 if(contains(info,'Failed')){
			 parent.$.messager.alert('提示', info, 'error');
			 }else{
				 parent.$.messager.alert('提示', '生成 bug id：  '+info, 'info');
			 }
			 
			parent.$.modalDialog.handler.dialog('close');
			parent.$.modalDialog.openner_treeGrid.treegrid('reload');
			 
		});
		
	
		 
		 
	}
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
		<form id="form" method="post">
			<table class="table table-hover table-condensed">
				<tr>
					<th>项目名称:</th>
					<td>${project}</td>		
					<th>Flyme版本:</th>
					<td><select id="fversion" name="fversion" class="easyui-combobox" data-options="required:true,width:140,height:29,editable:false,panelHeight:'auto'">
					</select></td>	
				</tr>	
				
				<tr>
				<th>创建者：</th>
					<td><input name="creater"  type="text" data-options="required:true" placeholder="请输入创建人"  class="easyui-validatebox span2" ></td>
				<th>指派人：</th>
					<td><input name="assiger" type="text" data-options="required:true" placeholder="请输入指派人"  class="easyui-validatebox span2" ></td>
				</tr>	
				
				
					
				<tr>
					<th>主题</th>
					<td colspan="3"><textarea name="subject"  data-options="required:true" class="easyui-validatebox span2" rows="" cols="" style="width: 367px; height: 40px;" class="span5"></textarea></td>
				</tr>
				<tr>
					<th>操作步骤</th>
					<td colspan="3"><textarea name="comment" rows="" data-options="required:true" class="easyui-validatebox span2" cols="" style="width: 367px; height: 200px;" class="span5"></textarea></td>
					<td colspan="3"><input name="caseid"  type='hidden' value="${id}" ></td>
					<td colspan="3"><input name="cproject"  type='hidden' value="${cproject}" ></td>
				</tr>
				   	
				
		
			</table>
	
			
			<span style="float:right;">
			<a onclick="sent();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'custer_redmine'">生成bug</a></span>
		</form>
	</div>
</div>