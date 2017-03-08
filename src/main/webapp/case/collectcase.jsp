<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html>
<head>
<title>业务用例管理</title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">

	var treeGridleft;
	$(function() {
		tryy('${buscasenode}');

		$('#pp').combobox({
			valueField : 'project', //值字段
			textField : 'project', //显示的字段
			url : '${pageContext.request.contextPath}/caseController/project',
			panelHeight : 300,
			editable : true,//不可编辑，只能选择
			onSelect : function(record) {
				//treeGrid.treegrid('reload');
				var value = $('#pp').combobox('getValue');
				tryy(value)

			}
		});
	});

	function tryy(data) {
		var uri='${pageContext.request.contextPath}/caseController/ctreeGrid?data='+data
		if(data!=""){
			uri='${pageContext.request.contextPath}/caseController/treeGrid?data='+data
		}
		treeGridleft = $('#treeGridleft')
				.treegrid(
						{
							url : uri,
							idField : 'id',
							treeField : 'info',
							parentField : 'pid',
							fit : true,
							nowrap : false,
							fitColumns : true,
							border : false,
							//rownumbers:true, //显示行号
							striped : true, //奇偶行颜色不同
							frozenColumns : [ [ {
								title : '编号',
								field : 'id',
								width : 150,
								hidden : true
							} ] ],
							columns : [ [ {
								field : 'info',
								title : '公共用例',
								width : 250,
								formatter : function(value, row, index) {
									var str = row.project;
									var name = row.name;
									var r
									//  console.log(row.name)
									if (row.pid == null) {
										r = str;
									} else if(row.pid==1||row.pid==2||row.pid==3) {
										r = str;
									}else{
										r = name;
									}

									return r;
								}
							}, {
								field : 'step',
								title : '操作步骤',
								width : 200
							}, {
								field : 'expire',
								title : '预期结果',
								width : 200
							} ] ],
							toolbar : '#toolbarleft',
							onDblClickRow:function(row){//运用双击事件实现对一行的编辑  
								var aa=$("#treeGridleft").treegrid('getChildren',row.id)
								if(aa.length==0){
								var rowstrr = JSON.stringify(row);
							var rowstr=encodeURIComponent(rowstrr)
								 $.post('${pageContext.request.contextPath}/buscaseController/add?data='+rowstr+"&pid="+'${collectpid}'+"&module="+'${collectmodule}'+"&modulechild="+'${collectmodulechild}'+"&project="+'${collectproject}'+"&projectid="+'${collectprojectid}',function(data){	
									 $.messager.show({
											title : '信息提示',
											timeout:20000,
											iconCls: 'icon-info',
											msg : '成功添加用例'
										});
										$("#treeGrid").treegrid('reload'); 					
									}); 
								}else{
									 $.messager.show({
											title : '信息提示',
											timeout:20000,
											iconCls: 'icon-info',
											msg : '请不要添加父节点用例'
										});
								}
								
							    }  ,
							onLoadSuccess : function() {
								var value = $('#pp').combobox('getValue');
								if (value.length == 0) {
									undoleft()
								}
								parent.$.messager.progress('close');
								$(this).treegrid('tooltip');
							}
						});
	}

	function redoleft() {
		var node = treeGridleft.treegrid('getSelected');
		if (node) {
			treeGridleft.treegrid('expandAll', node.id);
		} else {
			treeGridleft.treegrid('expandAll');
		}
	}

	function undoleft() {
		var node = treeGridleft.treegrid('getSelected');
		if (node) {
			treeGridleft.treegrid('collapseAll', node.id);
		} else {
			treeGridleft.treegrid('collapseAll');
		}
	}

	function filterleft() {
		tryy('')
		$('#pp').combobox('clear');
	}
	</script>
	
<c:if test="${fn:contains(sessionInfo.resourceList, '/buscaseController/editPage')}">
	<script type="text/javascript">
		$.canEdit = true;
	</script>
</c:if>
<c:if test="${fn:contains(sessionInfo.resourceList, '/buscaseController/delete')}">
	<script type="text/javascript">
		$.canDelete = true;
	</script>
</c:if>
<script type="text/javascript">
	var treeGrid;
	
	$(function() {
	tryyy('${collectpid}');
	var te=parent.$('#layout_west_tree2').tree('getSelected')
	fetchData(te.template); 
	});
	
	function fetchData(nj) {  
	    var s = "";  
	    s = "[[";  
	    s = s + "{field : 'ck',title : '选择',checkbox:true}, {field : 'project',title : '项目用例',width : 70}," 

	      
	    //lu todo 列的定义可从服务器获得  
	    if (nj=='2') {  
	    	  a1=ctemplate1
	    	s = s + a1
	      
	    }else if (nj=='3'){  
	    	  a1=ctemplate2
	    	  s = s + a1
	  } else{
		  a1=ctemplate
    	  s = s + a1 
	  } 
	    s = s+ "]]";  
	    options={};  
	    options.columns = eval(s);  
	    options.columns[0].push(  
				   {
						field : 'action',
						title : '操作',
						width : 40,
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
					})  
	      
	    $('#treeGrid').treegrid(options);  
	    $('#treeGrid').treegrid('reload');     
	      
	}  
	
	function tryyy(data){
		treeGrid =$('#treeGrid').treegrid({
			url : '${pageContext.request.contextPath}/buscaseController/collecttreeGrid?pid='+data,
			idField : 'id',
			treeField : 'module',
			parentField : 'pid',
			fit : true,
			nowrap: false,
			fitColumns : false,
			border : false,
			rownumbers:true, //显示行号
			striped: true, //奇偶行颜色不同
			frozenColumns : [ [ {
				title : '编号',
				field : 'id',
				width : 150,
				hidden : true
			} ] ],
			/* columns : [ [{
				field : 'project',
				title : '项目用例',
				width : 70,
				hidden : true
			},{
				field : 'casenum',
				title : '用例编号',
				width : 70
			},{
				field : 'casestep',
				title : '用例步号',
				width : 70
			},{
				field : 'version',
				title : '用例版本',
				width : 70
			},{
				field : 'updater',
				title : '更新人',
				width : 70
			},{
				field : 'updatetime',
				title : '更新时间',
				width : 70
			},{
				field : 'fristpage',
				title : '一级页面',
				width : 70
			},{
				field : 'secpage',
				title : '二级页面',
				width : 70
			},{
				field : 'thrpage',
				title : '三级页面',
				width : 70
			},{
				field : 'fourpage',
				title : '四级页面',
				width : 70
			},{
				field : 'fivepage',
				title : '五级页面',
				width : 70
			},{
				field : 'appstatusb',
				title : '用例状态',
				width : 70
			},{
				field : 'module',
				title : '测试模块',
				width : 120
			},{
				field : 'modulechild',
				title : '子模块',
				width : 70
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
			} ] ], */
			toolbar : '#toolbarcenter',
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
				
			
				parent.$.messager.confirm('询问', '您是否要删除当前资源？', function(b) {
			         if (b) {
						
						parent.$.messager.progress({
							title : '提示',
							text : '数据处理中，请稍后....'
						});
		                
			        	var ps = "";
			        	$.each(node,function(i,n){
	                            if(i==0) {
			        			
			        			ps +=n.id+"*";
			        	
			        		}else{
			        			ps +=n.id+"*";
			        		}
			        	});
						
						$.post('${pageContext.request.contextPath}/buscaseController/delete',{"ps":ps}, function(result) {
							if (result.success) {
								 $.messager.show({
										title : '信息提示',
										timeout:20000,
										iconCls: 'icon-info',
										msg : result.msg
									});	
						
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
				height : 650,
				href : '${pageContext.request.contextPath}/buscaseController/editPage?id='+node.id,
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
		var seq=0
		treeGrid.treegrid('selectAll');
		var dd=treeGrid.treegrid('getSelections');
	    treeGrid.treegrid('unselectAll');
		
		
		for(var i=0;i<dd.length;i++){			
		if(dd[i].seq>seq)
			seq=dd[i].seq
		}
		 
		var project=parent.$('#layout_west_tree2').tree('getSelected').text
		parent.$.modalDialog({
			title : '添加用例',
			width : 500,
			height :650,
			href : '${pageContext.request.contextPath}/buscaseController/addPage?project='+project+'&pid='+'${collectpid}'+"&seq="+seq,
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
	
	function upload() {
		parent.$.modalDialog({
			title : '导入',
			width : 220,
			height :'auto',
			href : '${pageContext.request.contextPath}/buscaseController/uploadPage',
			onLoad:function() {
				parent.$.modalDialog.openner_treeGrid = treeGrid; 
	        }
			
		/* 	buttons : [ {
				text : '上传',
				handler : function() {
			       
			      
				}
			} ] */
		});
	}
	
	function editCaseFun(){
		var project=parent.$('#layout_west_tree2').tree('getSelected').text
		var node = treeGrid.treegrid('getSelected');
		if(node){
			window.location.href='${pageContext.request.contextPath}/buscaseController/collectcase?pid='+node.id+"&module="+node.module+"&modulechild="+node.modulechild+"&project="+project
		}else{
			 $.messager.show({
					title : '信息提示',
					timeout:20000,
					iconCls: 'icon-info',
					msg : '请选择要拼装的用例级'
				});
		}
	}
	
</script>
</head>
<body>
	<!-- 	<div class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'west',border:false" title="" style="overflow: hidden;">
			<table id="treeGrid"></table>
		</div>
	</div>	
	 -->
	<div class="easyui-layout" data-options="fit:true,border:false">
	
	
		<div data-options="region:'west',split:true" title="公共用例"
			style="width: 500px; overflow: hidden;">
				<div id="toolbar">
			<a onclick="redoleft();" href="javascript:void(0);"
				class="easyui-linkbutton"
				data-options="plain:true,iconCls:'resultset_next'">展开</a> <a
				onclick="undoleft();" href="javascript:void(0);"
				class="easyui-linkbutton"
				data-options="plain:true,iconCls:'resultset_previous'">折叠</a> <a
				onclick="filterleft()" href="javascript:void(0);"
				class="easyui-linkbutton"
				data-options="plain:true,iconCls:'custer_shuaxin'">刷新</a> &nbsp;<img
				src="${pageContext.request.contextPath}/style/images/extjs_icons/cut_red.png" />&nbsp;选择过滤：<input
				id="pp" name="pp" style="width: 120px" class="easyui-combobox"
				data-options="iconCls:'resultset_next'">

		</div>
			<table id="treeGridleft"></table>
		</div>
		<div data-options="region:'center'" title="用例拼装"
			style="overflow: hidden;">
			
			<div id="toolbarcenter" style="display: none;">
		<c:if test="${fn:contains(sessionInfo.resourceList, '/buscaseController/addPage')}">
			<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'pencil_add'">添加</a>
		</c:if>
	
		<a onclick="redo();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'resultset_next'">展开</a> <a onclick="undo();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'resultset_previous'">折叠</a> <a onclick="treeGrid.treegrid('reload');" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'custer_shuaxin'">刷新</a>
	<%-- 	&nbsp;<img src="${pageContext.request.contextPath}/style/images/extjs_icons/cut_red.png" />&nbsp;选择过滤：<input id="pp" name="pp" style="width:120px" class="easyui-combobox" data-options="iconCls:'resultset_next'"> --%>
		<a onclick="upload()" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'custer_daoru'">导入</a>
		<c:if test="${fn:contains(sessionInfo.resourceList, '/buscaseController/editCase')}">
			<a onclick="editCaseFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'mouse_add'">拼装用例</a>
		</c:if>
	</div>
			<table id="treeGrid"></table>


		</div>
	</div>
</body>
</html>