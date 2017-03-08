<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title>公共用例管理</title>
<jsp:include page="../inc.jsp"></jsp:include>
<c:if test="${fn:contains(sessionInfo.resourceList, '/caseController/editPage')}">
	<script type="text/javascript">
		$.canEdit = true;
	</script>
</c:if>
<c:if test="${fn:contains(sessionInfo.resourceList, '/caseController/delete')}">
	<script type="text/javascript">
		$.canDelete = true;
	</script>
</c:if>
<script type="text/javascript">
	var treeGrid;
	$(function() {
		
		/* $('#pp').combobox({
			valueField:'project', //值字段
			    textField:'project', //显示的字段
			    url:'${pageContext.request.contextPath}/caseController/project',
			    panelHeight:'auto',
				editable:true,//不可编辑，只能选择
				onSelect:function(record){  
					treeGrid.treegrid('reload');
					var value = $('#pp').combobox('getValue');
					//tryy(value)
					 $.post('${pageContext.request.contextPath}/caseController/treeGrid?data='+value,function(data){	
                        var d=data;  
                        console.log(d)
						$("#treeGrid").treegrid('loadData',d); 					
					}); 
					
			     }  
			});*/
		tryy('${casenode}');
	});
	
	function tryy(data){
		treeGrid =$('#treeGrid').treegrid({
			url : '${pageContext.request.contextPath}/caseController/treeGrid?data='+data,
			idField : 'id',
			treeField : 'name',
			parentField : 'pid',
			fit : true,
			nowrap: false,
			fitColumns : true,
			border : false,
			 pagination:true, //显示分页
				pageSize : 200,//默认选择的分页是每页5行数据
	            pageList : [200,500,1000],//可以选择的分页集合
			singleSelect:false, 
			rownumbers:true, //显示行号
			striped: true, //奇偶行颜色不同
			frozenColumns : [ [ {
				title : '编号',
				field : 'id',
				width : 150,
				hidden : true
			} ] ],
			columns : [ [{
				field : 'ck',
				title : '选择',
				checkbox:true
			}, {
				field : 'project',
				title : '控件类型',
				width : 70,	
			},  {
				field : 'name',
				title : '测试项目',
				width : 180
				
			
				
			}, {
				field : 'op',
				title : '优先级',
				width : 50
			}, {
				field : 'pre',
				title : '前置条件',
				width : 100,
			}, {
				field : 'step',
				title : '操作步骤',
				width : 300
			}, {
				field : 'expire',
				title : '预期结果',
				width : 300
			}, {
				field : 'remark',
				title : '备注',
				width : 100,
			}, {
				field : 'seq',
				title : '排序',
				width : 40
			}, {
				field : 'appstatusc',
				title : '用例状态',
				width : 40
			}, {
				field : 'action',
				title : '操作',
				width : 50,
				formatter : function(value, row, index) {
					var str = '';
					if ($.canEdit) {
						str += $.formatString('<img onclick="editFun(\'{0}\');" src="{1}" title="编辑"/>', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/pencil.png');
					}
					str += '&nbsp;';
					if ($.canDelete) {
						str += $.formatString('<img onclick="deleteFun(\'{0}\');" src="{1}" title="删除"/>', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/cancel.png');
					}
					return str;
				}
			} ] ],
			toolbar : '#toolbar',
			onDblClickRow:function(row){//运用双击事件实现对一行的编辑  
				treeGrid.treegrid('unselectAll');
				if ($.canEdit) {
				editFun(row.id)
				}
					
				    }  ,
			onContextMenu : function(e, row) {
				e.preventDefault();
				$(this).treegrid('unselectAll');
				$(this).treegrid('select', row.id);
				$('#menu').menu('show', {
					left : e.pageX,
					top : e.pageY
				});
			},
			onLoadSuccess : function() {
				parent.$.messager.progress('close');

				$(this).treegrid('tooltip');
			}
		});
	}

	function deleteFun(id) {
		if (id != undefined) {
			treeGrid.treegrid('select', id);
		}
		
		var node = treeGrid.treegrid('getSelections');
		
		if (node.length>0) {
			var go=true;
		
			for(var i=0;i<node.length;i++){
				if(treeGrid.treegrid('getChildren',node[i].id)!=""){      			
       			 $.messager.show({
    					title : '信息提示',
    					timeout:20000,
    					iconCls: 'icon-info',
    					msg : '请不要选择有子节点的用例做删除'
    				});	
       			 go=false;
       		
       		}
			}
			
			if(go!=true){
				return false;
			}
			parent.$.messager.confirm('询问', '您是否要删除当前资源？', function(b) {
				if (b) {
					
					parent.$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍后....'
					});
	                
		        	var ps = "";
		        	$.each(node,function(i,n){
		        		if(i==0) 
		        			ps += "?id="+n.id;
		        		else
		        			ps += "&id="+n.id;
		        	});
					
					$.post('${pageContext.request.contextPath}/caseController/delete'+ps, function(result) {
						if (result.success) {
							 $.messager.show({
									title : '信息提示',
									timeout:20000,
									iconCls: 'icon-info',
									msg : result.msg
								});	
/* 							 treeGrid.treegrid('clearSelections');
							 treeGrid.treegrid('unselectAll'); */
							// treeGrid.treegrid('uncheckedAll')
					
    						 treeGrid.treegrid('reload');
						
							 treeGrid.treegrid('clearChecked')
							
						}
						parent.$.messager.progress('close');
					}, 'JSON');
				}
			});
		}else{
			 $.messager.show({
					title : '信息提示',
					timeout:20000,
					iconCls: 'icon-info',
					msg : '请选择记录'
				});	
		}
	}

	function editFun(id) {
		if (id != undefined) {
			treeGrid.treegrid('select', id);
		}
		var node = treeGrid.treegrid('getSelected');
		if (node) {
			parent.$.modalDialog({
				title : '编辑用例',
				width : 500,
				height : 600,
				href : '${pageContext.request.contextPath}/caseController/editPage?id=' + node.id,
				buttons : [ {
					text : '编辑',
					handler : function() {
						parent.$.modalDialog.openner_treeGrid = treeGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
						var f = parent.$.modalDialog.handler.find('#form');
						f.submit();
					}
				} ]
			});
		}
	}

	function addFun() {
		var project=parent.$('#layout_west_tree2').tree('getSelected').text
		parent.$.modalDialog({
			title : '添加用例',
			width : 500,
			height :600,
			href : '${pageContext.request.contextPath}/caseController/addPage?project='+project,
			buttons : [ {
				text : '添加',
				handler : function() {
					parent.$.modalDialog.openner_treeGrid = treeGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
					var f = parent.$.modalDialog.handler.find('#form');
					f.submit();
				}
			} ]
		});
	}

	function redo() {
		var node = treeGrid.treegrid('getSelected');
		if (node) {
			treeGrid.treegrid('expandAll', node.id);
		} else {
			treeGrid.treegrid('expandAll');
		}
	}

	function undo() {
		var node = treeGrid.treegrid('getSelected');
		if (node) {
			treeGrid.treegrid('collapseAll', node.id);
		} else {
			treeGrid.treegrid('collapseAll');
		}
	}
	
	function filter() {
		var node = treeGrid.treegrid('getSelected');
		if (node) {
			treeGrid.treegrid('collapseAll', node.id);
		} else {
			treeGrid.treegrid('collapseAll');
		}
	}
	
	function bupdate() {
		var nodea = treeGrid.treegrid('getSelections');
		console.log(nodea.length)
		if (nodea.length>0) {	
		  	var ps = "";
        	$.each(nodea,function(i,n){
        		if(i==0) 
        			ps += "&id="+n.id;
        		else
        			ps += "&id="+n.id;
        	});
			parent.$.modalDialog({
				title : '编辑用例',
				width : 500,
				height : 200,
				href : '${pageContext.request.contextPath}/caseController/bupdatePage?name='+ nodea[0].project+ps,
				buttons : [ {
					text : '编辑',
					handler : function() {
						parent.$.modalDialog.openner_treeGrid = treeGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
						var f = parent.$.modalDialog.handler.find('#form');
						f.submit();
					}
				} ]
			});
		      
		}else{
			 $.messager.show({
					title : '信息提示',
					timeout:20000,
					iconCls: 'icon-info',
					msg : '请选择记录'
				});	
		}
	}
	
	function upload() {
		parent.$.modalDialog({
			title : '导入',
			width : 220,
			height :'auto',
			href : '${pageContext.request.contextPath}/caseController/uploadPage',
			onLoad:function() {
				var num=treeGrid.treegrid('getChecked');
				console.log(num.length)
				for(var i=0;i<num.length;i++){
					  console.log(num[i].pid)
				}
              
				parent.$.modalDialog.openner_treeGrid = treeGrid; 
	        }
			/* 	console.log($('#layout_west_tree2').tree('getParent',node.target).text)
			console.log($('#layout_west_tree2').tree('getSelected').text)
			console.log($('#layout_west_tree2').tree('getRoot').text) 
			var ta=$('#layout_west_tree2').tree('getSelected');
		    var aa=$('#layout_west_tree2').tree('getChildren',ta.target)
			console.log(aa[0].text)
			$('#layout_west_tree2').tree('getParent',ta.target).text
			$('#layout_west_tree2').tree('isLeaf',ta.target) //是否最后一个节点
			
			*/
		/* 	buttons : [ {
				text : '上传',
				handler : function() {
			       
			      
				}
			} ] */
		});
	}
</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
			<table id="treeGrid"></table>
		</div>
	</div>
	<div id="toolbar" style="display: none;">
		<c:if test="${fn:contains(sessionInfo.resourceList, '/caseController/addPage')}">
			<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'pencil_add'">添加</a>
		</c:if>
		<a onclick="redo();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'resultset_next'">展开</a> <a onclick="undo();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'resultset_previous'">折叠</a> <a onclick="treeGrid.treegrid('reload');" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'custer_shuaxin'">刷新</a>
	<%-- 	&nbsp;<img src="${pageContext.request.contextPath}/style/images/extjs_icons/cut_red.png" />&nbsp;选择过滤：<input id="pp" name="pp" style="width:120px" class="easyui-combobox" data-options="iconCls:'resultset_next'"> --%>
		<c:if test="${fn:contains(sessionInfo.resourceList, '/caseController/uploadPage')}">
		<a onclick="upload()" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'custer_daoru'">导入</a>
		</c:if>
	<c:if test="${fn:contains(sessionInfo.resourceList, '/caseController/delete')}">
		<a onclick="bupdate()" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'building_edit'">批量更新</a>
		</c:if>
		
		<c:if test="${fn:contains(sessionInfo.resourceList, '/caseController/delete')}">
		<a onclick="deleteFun()" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'package_delete'">批量删除</a>
	</c:if>
	</div>

	<div id="menu" class="easyui-menu" style="width: 120px; display: none;">
		<c:if test="${fn:contains(sessionInfo.resourceList, '/caseController/addPage')}">
			<div onclick="addFun();" data-options="iconCls:'pencil_add'">增加</div>
		</c:if>
		<c:if test="${fn:contains(sessionInfo.resourceList, '/caseController/delete')}">
			<div onclick="deleteFun();" data-options="iconCls:'pencil_delete'">删除</div>
		</c:if>
		<c:if test="${fn:contains(sessionInfo.resourceList, '/caseController/editPage')}">
			<div onclick="editFun();" data-options="iconCls:'pencil'">编辑</div>
		</c:if>
		
		
	</div>
</body>
</html>