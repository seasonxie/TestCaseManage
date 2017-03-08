<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title>BUG管理</title>
<jsp:include page="../inc.jsp"></jsp:include>
<c:if test="${fn:contains(sessionInfo.resourceList, '/dataController/editPage')}">
	<script type="text/javascript">
		$.canEdit = true;
	</script>
</c:if>
<c:if test="${fn:contains(sessionInfo.resourceList, '/dataController/delete')}">
	<script type="text/javascript">
		$.canDelete = true;
	</script>
</c:if>
<c:if test="${fn:contains(sessionInfo.resourceList, '/dataController/download')}">
	<script type="text/javascript">
		$.canDownload = true;
	</script>
</c:if>

<script type="text/javascript">
	var dataGrid;
	var datatype;
	$(function() {
		dataGrid = $('#dataGrid').datagrid({
			url : '${pageContext.request.contextPath}/dataController/dataGrid',
			fit : true,
			fitColumns : true,
			border : false,
			pagination : true,
			idField : 'id',
			pageSize : 10,
			pageList : [ 10, 20, 30, 40, 50 ],
			sortName : 'updatetime',
			sortOrder : 'desc',
			checkOnSelect : false,
			selectOnCheck : false,
			nowrap : false,
			striped : true,
			rownumbers : true,
			singleSelect : true,
			frozenColumns : [ [ {
				field : 'id',
				title : '编号',
				width : 150,
				hidden : true
			}, {
				field : 'type',
				title : '类型',
				width : 80,
				sortable : true
			} ] ],
			columns : [ [ {
				field : 'name',
				title : '数据名称',
				width : 150,
				sortable : true
			}, {
				field : 'updater',
				title : '更新人',
				width : 150,
				sortable : true
			}, {
				field : 'updatetime',
				title : '更新时间',
				width : 150,
				sortable : true
			}, {
				field : 'action',
				title : '下载测试数据',
				width : 100,
				formatter : function(value, row, index) {
					var str = '';
					if ($.canEdit) {
						str += $.formatString('<img onclick="editFun(\'{0}\');" src="{1}" title="编辑"/>', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/bug/bug_edit.png');
					}
					str += '&nbsp;';
					if ($.canDelete) {
						str += $.formatString('<img onclick="deleteFun(\'{0}\');" src="{1}" title="删除"/>', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/bug/bug_delete.png');
					}
					str += '&nbsp;';
					if ($.canDownload) {
						str += $.formatString('<img onclick="downloadFun(\'{0}\');" src="{1}" title="下载"/>', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/plugin_link.png');
					}
					return str;
				}
			} ] ],
			toolbar : '#tb',
			onLoadSuccess : function() {
				$('#searchForm table').show();
				parent.$.messager.progress('close');

				$(this).datagrid('tooltip');
			},
			onRowContextMenu : function(e, rowIndex, rowData) {
				e.preventDefault();
				$(this).datagrid('unselectAll');
				$(this).datagrid('selectRow', rowIndex);
				$('#menu').menu('show', {
					left : e.pageX,
					top : e.pageY
				});
			}
		});
		
		datatype = [{ "id": "文本输入框", "text": "文本输入框" }, { "id": "数字输入框", "text": "数字输入框" }, { "id": "HTTP校验", "text": "HTTP校验" }, { "id": "邮件校验", "text": "邮件校验" }];
		$('#type').combobox({ 
			data:datatype, 
			valueField:'id', 
			textField:'text',
		    panelHeight:'auto',
		    editable:true,//不可编辑，只能选择
			});  

	});

	function deleteFun(id) {
		if (id == undefined) {
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].id;
		}
		parent.$.messager.confirm('询问', '您是否要删除当前BUG?', function(b) {
			if (b) {
				parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				$.post('${pageContext.request.contextPath}/dataController/delete', {
					id : id
				}, function(result) {
					if (result.success) {
						parent.$.messager.alert('提示', result.msg, 'info');
						dataGrid.datagrid('reload');
					}
					parent.$.messager.progress('close');
				}, 'JSON');
			}
		});
	}

	function editFun(id) {
		if (id == undefined) {
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].id;
		}
		parent.$.modalDialog({
			title : '修改数据',
			width : 500,
			height : 500,
			href : '${pageContext.request.contextPath}/dataController/editPage?id=' + id,
			buttons : [ {
				text : '编辑',
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
					var f = parent.$.modalDialog.handler.find('#form');
					f.submit();
				}
			} ]
		});
	}
	
	function downloadFun(id) {
		if (id == undefined) {
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].id;
		}
		
		 var url = '${pageContext.request.contextPath}/dataController/download?id='+id;
        window.location.href=url; 
	}


	function addFun() {
		parent.$.modalDialog({
			title : '添加数据',
			width : 500,
			height : 500,
			href : '${pageContext.request.contextPath}/dataController/addPage',
			buttons : [ {
				text : '添加',
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
					var f = parent.$.modalDialog.handler.find('#form');
					f.submit();
				}
			},{
				text : '全选',
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;//因添加成功之后，需要刷新这个dataGrid，所以先预定义好
					//parent.$.modalDialog.handler.find('#blank_q').attr("checked",'checked'); 
					
					var checkbox=parent.$.modalDialog.handler.find('input:checkbox')
				
					for(var i=0;i<checkbox.length;i++){
						checkbox[i].checked=true
					}
					
					
				}
			} ]
		});
	}

	function searchFun() {
		dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
	}
	function cleanFun() {
		$('#searchForm input').val('');
		dataGrid.datagrid('load', {});
	}
</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit : true,border : false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
			
			 <div class="easyui-panel" style="padding: 0px;" fit="true">
			<table id="dataGrid"></table>
		</div>
		
		<div id="tb" style="padding:5px;height:auto">
			<form id="searchForm">
				<table class="table table-hover table-condensed" style="width:60%">
					<tr>
						<th>数据名称</th>
						<td><input name="name" placeholder="可以模糊查询" class="span2" /></td>
						<th>类型</th>
						<td><select name="type" id="type" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
					</select></td>
					<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'brick_add',plain:true" onclick="searchFun();">过滤条件</a></td>
					<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'brick_delete',plain:true" onclick="cleanFun();">清空条件</a></td>
					</tr>
				<!-- 	<tr>
						<th>最后修改时间</th>
						<td colspan="3"><input class="span2" name="updatetime" placeholder="点击选择时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" />至<input class="span2" name="modifydatetimeEnd" placeholder="点击选择时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" /></td>
					</tr> -->
				</table>
			</form>
				<div id="toolbar">
		<c:if test="${fn:contains(sessionInfo.resourceList, '/dataController/addPage')}">
			<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'bug_add'">添加</a>
		</c:if>
		
	</div>
      </div>
		</div>
		
	</div>


	<div id="menu" class="easyui-menu" style="width: 120px; display: none;">
		<c:if test="${fn:contains(sessionInfo.resourceList, '/dataController/addPage')}">
			<div onclick="addFun();" data-options="iconCls:'pencil_add'">增加</div>
		</c:if>
		<c:if test="${fn:contains(sessionInfo.resourceList, '/dataController/delete')}">
			<div onclick="deleteFun();" data-options="iconCls:'pencil_delete'">删除</div>
		</c:if>
		<c:if test="${fn:contains(sessionInfo.resourceList, '/dataController/editPage')}">
			<div onclick="editFun();" data-options="iconCls:'pencil'">编辑</div>
		</c:if>
	</div>
</body>
</html>