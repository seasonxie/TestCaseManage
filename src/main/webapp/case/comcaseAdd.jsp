<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');
		$('#iconCls').combobox({
			data : $.iconData,
			formatter : function(v) {
				
				return $.formatString('<span class="{0}" style="display:inline-block;vertical-align:middle;width:16px;height:16px;"></span>{1}', v.value, v.value);
			}
		});

		/* $('#pid').combotree({
			url : '${pageContext.request.contextPath}/caseController/tree',
			parentField : 'pid',
			lines : true,
			panelHeight : 'auto',
			onLoadSuccess : function() {
				parent.$.messager.progress('close');
			}
		}); */
		$('#pid').combobox({
			valueField:'id', //值字段
			    textField:'name', //显示的字段
			    url:'${pageContext.request.contextPath}/caseController/projectchild?child='+'${case.project}',
			    panelHeight:'auto',
				editable:true//不可编辑，只能选择						
			});
		
		$('#project').combobox({
			valueField:'project', //值字段
			    textField:'project', //显示的字段
			    url:'${pageContext.request.contextPath}/caseController/project',
			    panelHeight:500,
				editable:true,//不可编辑，只能选择
				value:'${case.project}',
				onSelect:function(record){  
					var value = $('#project').combobox('getValue');
					$('#pid').combobox({
						valueField:'id', //值字段
						    textField:'name', //显示的字段
						    url:'${pageContext.request.contextPath}/caseController/projectchild?child='+value,
						    panelHeight:'auto',
							editable:true//不可编辑，只能选择						
						});
			     }  
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
			url : '${pageContext.request.contextPath}/caseController/add',
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
					parent.$.modalDialog.openner_treeGrid.treegrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_treeGrid这个对象，是因为case.jsp页面预定义好了
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
					<th>编号</th>
					<td><input name="id" type="text" class="span2" value="${case.id}" readonly="readonly"></td>
					<th>控件名称</th>
					<td><select name="project" id="project" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'"></select></td>
				</tr>
				<tr>
					<th>测试项目</th>
					<td><input name="name" id="name" type="text" placeholder="请输入测试项目" data-options="required:true" class="easyui-validatebox span2" value=""></td>
					<th>执行级</th>
					<td><select name="op" id="op" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
							<%-- <c:forEach items="${resourceTypeList}" var="resourceType">
								<option value="${resourceType.id}">${resourceType.name}</option>
							</c:forEach> --%>
					</select></td>
				</tr>
				<tr>
					<th>排序</th>
					<td><input name="seq" value="100" class="easyui-numberspinner" style="width: 140px; height: 29px;" required="required" data-options="editable:false,min:100"></td>
					<th>上级目录</th>
					<td><select id="pid" name="pid" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" style="width: 140px; height: 29px;"></select></td>
				</tr>
				<tr>
					<th>菜单图标</th>
					<td colspan="3"><input id="iconCls" name="iconCls" style="width: 375px; height: 29px;" data-options="editable:false" /></td>
				</tr>
				<tr>
					<th>前置条件</th>
					<td colspan="3"><textarea name="pre" rows="" cols="" class="span5"></textarea></td>
				</tr>
				<tr>
					<th>执行步骤</th>
					<td colspan="3"><textarea name="step" style="width: 367px; height: 70px;" rows="" cols="" class="span5"></textarea></td>
				</tr>
					<tr>
					<th>预期结果</th>
					<td colspan="3"><textarea name="expire" style="width: 367px; height: 70px;" rows="" cols="" class="span5"></textarea></td>
				</tr>
				<tr>
					<th>备注</th>
					<td colspan="3"><textarea name="remark" rows="" cols="" class="span5"></textarea></td>
				</tr>
			</table>
		</form>
	</div>
</div>