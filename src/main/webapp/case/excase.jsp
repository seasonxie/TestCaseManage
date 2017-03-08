<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title>执行用例管理</title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript" src="${pageContext.request.contextPath}/jslib/treegrid-dnd.js" charset="utf-8"></script>
<c:if test="${fn:contains(sessionInfo.resourceList, '/excaseController/editPage')}">
	<script type="text/javascript">
		$.canEdit = true;
	</script>
</c:if>
<c:if test="${fn:contains(sessionInfo.resourceList, '/excaseController/delete')}">
	<script type="text/javascript">
		$.canDelete = true;
	</script>
</c:if>
<script type="text/javascript">
	var treeGrid;
	var closeArray;
	$(function() {
		var Address = [{ "id": "P1", "text": "P1" }, { "id": "P2", "text": "P2" }, { "id": "P3", "text": "P3" }, { "id": "P4", "text": "P4" }, { "id": "P5", "text": "P5" }];
		$('#searchop').combobox({ 
			data:Address, 
			valueField:'id', 
			textField:'text',
		    panelHeight:'auto',
		    editable:true,//不可编辑，只能选择
		    onSelect:function(record){  
		    	searchFun();
		    }
			}); 
		
		var Status = [{ "id": "P", "text": "P" }, { "id": "F", "text": "F" }, { "id": "未执行", "text": "未执行" }];
		$('#searchstatus').combobox({ 
			data:Status, 
			valueField:'id', 
			textField:'text',
		    panelHeight:'auto',
		    editable:true,//不可编辑，只能选择
		    onSelect:function(record){  
		    	searchFun();
		    }
			});  

			
			$('#searchname').keyup(function(event) {
				if (event.keyCode == '13') {
					searchFun();
				}
			});
			$('#searchstep').keyup(function(event) {
				if (event.keyCode == '13') {
					searchFun();
				}
			});
			$('#searchupdater').keyup(function(event) {
				if (event.keyCode == '13') {
					searchFun();
				}
			});
		
		
		 var te=parent.$('#layout_west_tree2').tree('getSelected')
		 tryy('${excasenode}');
		 fetchData(te.template,'${excasenode}',te.id); 
		 setTotal('${excasenode}','',te.id)
	});
	
	function setTotal(project,search,treeid){
		 $.post('${pageContext.request.contextPath}/excaseController/total?data='+project+"&treeid="+treeid,search,function(data){	
				var p = $('#treeGrid').treegrid('getPager'); 
				 $(p).pagination({ 
					    total:data, 
				    /*     pageSize: 200,//每页显示的记录条数，默认为10 
				        pageList: [25,30,45],//可以设置每页记录条数的列表  */
				        beforePageText: '第',//页数文本框前显示的汉字 
				        afterPageText: '页    共 {pages} 页', 
				        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录', 
				    }); 
				 });   
	}
	
	function fetchData(nj,data,treeid) {  
	    var s = "";  
	    s = "[[";  
	    s = s + "{field : 'ck',title : '选择',checkbox:true}, {field : 'project',title : '项目用例',width : 70}," 
	    options={};  
	      
	    //lu todo 列的定义可从服务器获得  
	    if (nj=='2') {  
	    	options.treeField='fristpage'
	    	  a1=etemplate1
	    	s = s + a1
	      
	    }else if (nj=='3'){  
	    	  a1=etemplate2
	    	  s = s + a1 
	  } else{
		   a1=etemplate
    	  s = s + a1  
	  } 
	    s = s+ "]]";  
	   
	    options.columns = eval(s);  
	    options.url = '${pageContext.request.contextPath}/excaseController/treeGrid?data='+data+"&treeid="+treeid;
	   // options.fitColumns=true;
	   // options.fit=false;
	   // options.striped=false;
	    //lu 增加一列  
	   options.columns[0].push(  
			   {
					field : 'action',
					title : '操作',
					width : 70,
					formatter : function(value, row, index) {
						var str = '';
						if (row.iscase=='1') {
							str += $.formatString('<img onclick="doP(\'{0}\');" src="{1}" title="P"/>', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/P1.png');
					
							if(row.status!='F'){
						str += '&nbsp;';
						
							str += $.formatString('<img onclick="doF(\'{0}\');" src="{1}" title="F"/>', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/F3.png');
							}
							if(row.status=='F'){
							str += '&nbsp;';
							str += '&nbsp;';
							str += $.formatString('<img onclick="doR(\'{0}\');" src="{1}" title="R"/>', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/R.png');
							}
						}
						return str;
					}
				} ,
				{
					field : 'remark',
					title : '备注',
					width : 100,
					formatter : function(value, row, index) {
						//
						var str = value;
				
						try{
						if (value.indexOf('#') != -1) {
							var bug=value.substring(value.indexOf('#')+1,value.indexOf('#')+7)
							
					 var url='http:\\\\redmine.meizu.com/issues/'+ bug 
							if(value.trim().length==7){
						str='<a href='+url+'>'+'#'+bug+'</a>'
							}else{
						str='<a href='+url+'>'+'#'+bug+'</a>'+'<br>'+value
							}
						}else{
							str=value
						}
						}catch(err){console.log(err)}
						
						return str;
					}
				} 
	    );      
	      
	    $('#treeGrid').treegrid(options);  
	    $('#treeGrid').treegrid('reload');     
	      
	}  
	
	function tryy(data){
	
		treeGrid =$('#treeGrid').treegrid({
			idField : 'id',
			treeField : 'module',
			parentField : 'pid',
			fit : true,
			//nowrap: false,
			pagination:true, //显示分页
			pageSize : 300,//默认选择的分页是每页5行数据
            pageList : [300,500,1000],//可以选择的分页集合  
			singleSelect:false, 
			fitColumns : true,
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
				field : 'ck',
				title : '选择',
				checkbox:true
			},{
				field : 'project',
				title : '项目用例',
				width : 80
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
				field : 'module',
				title : '测试模块',
				width : 160
			},{
				field : 'modulechild',
				title : '子模块',
				width : 80
			},  {
				field : 'name',
				title : '测试项目',
				width : 100
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
				width : 320
			}, {
				field : 'expire',
				title : '预期结果',
				width : 320
			}, {
				field : 'status',
				title : '状态',
				width : 40,
		/* 		 styler: function(value,row,index){
				        
				      
				              if('P'.indexOf(value) != -1){
					             return 'background-color:#337AB7;color:#fff;';
				              }else if('F'.indexOf(value) != -1){
					             return 'background-color:red;color:#fff;';
				              }else if(  ''.indexOf(value) != -1){
					             return 'background-color:blue;color:#fff;';
				              }
				             
				        
			      } ,*/
			      /*	formatter : function(value, row, index) {
					var str = '';
				
					if (row.status=='P'&&row.iscase=='1') {
						str = '<font color="green">'+row.status+'</font>'
					}else if(row.status=='F'&&row.iscase=='1'){
						str ='<font color="red">'+row.status+'</font>'
					}else{
						if(row.iscase=='1')
						str='未执行'
					}
					return str;
				} 
			}, {
				field : 'tester',
				title : '执行人',
				width : 60
			}, {
				field : 'exdate',
				title : '执行日期',
				width : 100
			}, {
				field : 'action',
				title : '操作',
				width : 70,
				formatter : function(value, row, index) {
					var str = '';
					if (row.iscase=='1') {
						str += $.formatString('<img onclick="doP(\'{0}\');" src="{1}" title="P"/>', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/P1.png');
				
						if(row.status!='F'){
					str += '&nbsp;';
					
						str += $.formatString('<img onclick="doF(\'{0}\');" src="{1}" title="F"/>', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/F3.png');
						}
						if(row.status=='F'){
						str += '&nbsp;';
						str += '&nbsp;';
						str += $.formatString('<img onclick="doR(\'{0}\');" src="{1}" title="R"/>', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/R.png');
						}
					}
					return str;
				}
			},{
				field : 'remark',
				title : '备注',
				width : 100,
				formatter : function(value, row, index) {
					//
					var str = '';
					//console.log(value)
					//console.log("--"+value.indexOf('#'))
					if (value.indexOf('#') != -1) {
						console.log(value.indexOf('#'))
						var bug=value.substring(value.indexOf('#')+1,value.indexOf('#')+7)
						
				/* 		var l=0
						var a='';
						while(true){
							l=value.indexOf('#',l)
							if(l==-1){
								break;
							}else{
								a+=l+'*'
								l++
							}
						}
						console.log(a)
						var strs= new Array(); //定义一数组 
                          strs=a.split("*");
						console.log(strs.length)
						for(var i=0;i<strs.length-1;i++){
							console.log(value.substring(parseInt(strs[i])+1,parseInt(strs[i])+7))
						}
						 */
						
						 /*		 var url='http:\\\\redmine.meizu.com/issues/'+ bug 
						if(value.trim().length==7){
					str='<a href='+url+'>'+'#'+bug+'</a>'
						}else{
					str='<a href='+url+'>'+'#'+bug+'</a>'+'<br>'+value
						}
					}else{
						str=value
					}
					
					return str;
				}
			}
			 ] ], */
			toolbar : '#tb',
			onDblClickRow:function(row){//运用双击事件实现对一行的编辑  
				treeGrid.treegrid('unselectAll');
				if ($.canEdit) {
				editFun(row.id)
				}	
				    },
				    onBeforeLoad:function(row){
						closeArray=getClosed();
					},
			onContextMenu : function(e, row) {
				e.preventDefault();
				$(this).treegrid('unselectAll');
				$(this).treegrid('select', row.id);
				$('#menu').menu('show', {
					left : e.pageX,
					top : e.pageY
				});
			},
			onLoadSuccess : function(row) {
				for(var i=0;i<closeArray.length;i++){
		            treeGrid.treegrid('collapseAll', closeArray[i]);
		            }
		
				//$(this).treegrid('tooltip');
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
		        	var name="";
		        	$.each(node,function(i,n){
		        		if(i==0) {
		        			
		        			ps +=n.id+"*";
		        		  name=n.project;
		        		
		        		}else{
		        			ps +=n.id+"*";
		        	
		        		}
		        	});				
					$.post('${pageContext.request.contextPath}/excaseController/delete?&project='+name,{"ps":ps}, function(result) {
						if (result.success) {
							 $.messager.show({
									title : '信息提示',
									timeout:20000,
									iconCls: 'icon-info',
									msg : result.msg
								});	
							 treeGrid.treegrid('reload')
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
	
	function getClosed(){
		var closes=new Array()
		try{
		var num=0
		treeGrid.treegrid('selectAll');
		var dd=treeGrid.treegrid('getSelections');
			treeGrid.treegrid('unselectAll');
			for(var i=0;i<dd.length;i++){
				if(dd[i].children){
					if(dd[i].state=='closed'){
						closes[num]=dd[i].id
						num++
					}
				}
			}
		}catch(err){}
			return closes
	}

	function editFun(id) {
		if (id != undefined) {
			treeGrid.treegrid('select', id);
		}
		var node = treeGrid.treegrid('getSelected');
		console.log(node)
		treeGrid.treegrid('unselectAll');
		if (node) {
			parent.$.modalDialog({
				title : '编辑用例',
				width : 500,
				height : 600,
				href : '${pageContext.request.contextPath}/excaseController/editPage?id=' + node.id+'&name=' + node.project,
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
	
	function doPOnce() {
		
		var node = treeGrid.treegrid('getSelections');	
		if (node.length>0) {	
					
			        	
					parent.$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍后....'
					});	                
		        	var ps = "";
		        	var name="";
		        	$.each(node,function(i,n){
		        		if(i==0) {
		        			
		        			ps +=n.id+"*";
		        		  name=n.project;
		        		
		        		}else{
		        			ps +=n.id+"*";
		        	
		        		}
		        	});				
					$.post('${pageContext.request.contextPath}/excaseController/doPOnce?&project='+name,{"ps":ps}, function(result) {
			
						treeGrid.treegrid('reload');
						parent.$.messager.progress('close');
			 			 
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
	
	function doP(id) {
		if (id != undefined) {
			treeGrid.treegrid('select', id);
		}
		var node = treeGrid.treegrid('getSelected');   
		if (node) {
		var now=getNowFormatDate()
			var f='P'
			 $.post('${pageContext.request.contextPath}/excaseController/edit?&idd='+node.id+'&statuss='+f+'&namee='+node.project,function(data){	
				 
			        treeGrid.treegrid('update',{
                         id: node.id,
                       row: {
                        status: 'P',
                        tester:'${sessionInfo.name}',
                        exdate:now
                    
                     }
                         });
				// treeGrid.treegrid('reload');				 
			 });
		}
	}
	
	function doF(id) {
		if (id != undefined) {
			treeGrid.treegrid('select', id);
		}	
		var node = treeGrid.treegrid('getSelected');		
		if (node) {
			var now=getNowFormatDate()
			var p='F'
			 $.post('${pageContext.request.contextPath}/excaseController/edit?&idd='+node.id+'&statuss='+p+'&namee='+node.project,function(data){	
				 treeGrid.treegrid('update',{
                     id: node.id,
                   row: {
                    status: 'F',
                    tester:'${sessionInfo.name}',
                    exdate:now
                 }
                     });
			// treeGrid.treegrid('reload');			
			 });
		}
	}
	
	function doR(id) {
		if (id != undefined) {
			treeGrid.treegrid('select', id);
		}
		var projectt=parent.$('#layout_west_tree2').tree('getSelected')
		var cproject=projectt.text
		var project=parent.$('#layout_west_tree2').tree('getParent',projectt.target).text
		var node = treeGrid.treegrid('getSelected');
		
		var url='http://redmine.meizu.com/my/page'
		window.location.href=url
/* 		if (node) {
			
			parent.$.modalDialog({
				title : '提交bug',
				width : 500,
				height :500,
				href : '${pageContext.request.contextPath}/excaseController/redminePage?&id='+node.id+'&project='+project+'&cproject='+cproject,
				 onLoad:function() {
					 parent.$.modalDialog.openner_treeGrid = treeGrid;
			        }
	
			});
		} */
	}

	function addFun() {
		var projectt=parent.$('#layout_west_tree2').tree('getSelected')
		var project=parent.$('#layout_west_tree2').tree('getParent',projectt.target).text
			//var project=parent.$('#layout_west_tree2').tree('getSelected').text
			parent.$.modalDialog({
				title : '添加执行集',
				width : 500,
				height :250,
				href : '${pageContext.request.contextPath}/excaseController/addPage?project='+project,
				 onLoad:function() {
					 parent.$.modalDialog.openner_treeGrid = treeGrid;
			        }
		/* 		buttons : [ {
					text : '生成',
					handler : function() {
						parent.$.modalDialog.openner_treeGrid = treeGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
						var f = parent.$.modalDialog.handler.find('#form');
						f.submit();
					}
				} ] */
			});
	}
	
	function generateReportPage() {
	treeGrid.treegrid('selectAll');
	var dd=treeGrid.treegrid('getSelections');
		treeGrid.treegrid('unselectAll');
		var projectt=parent.$('#layout_west_tree2').tree('getSelected')
		var project=projectt.text
		var parentproject=parent.$('#layout_west_tree2').tree('getParent',projectt.target).text
		var faileddetail=''
		var failed=0
		
		var undo=0
		
		var casesnum=0
		for(var i=0;i<dd.length;i++){
			if(dd[i].iscase!="0"){
				casesnum++;
				//console.log(casesnum)
				}
			if(dd[i].status==undefined&&dd[i].iscase=="1"){
				undo++;
				}
			if(dd[i].status==''&&dd[i].iscase=="1"){
				undo++;
				}
			if(dd[i].status=='F'&&dd[i].iscase=="1"){
				faileddetail+=dd[i].exdate+'*'
				failed++;
				}
		/* 	
			if(dd[i].status=='F'&&dd[i].iscase=="1"&&dd[i].remark.indexOf("#")!=-1){
				faileddetail+=dd[i].exdate+'*'
			
				} */
			//console.log(dd[i].status +" ---  "+dd[i].iscase)
			
		}
		
		//console.log(faileddetail)

		
		var failedv=((failed/casesnum).toFixed(2)*10000)/100
		var undovv=((1-(undo/casesnum)).toFixed(2))
		var undov=(undovv*10000)/100
		 parent.$.modalDialog({
			title : '用例情况',
			width : 500,
			height :300,
			href : '${pageContext.request.contextPath}/excaseController/reportPage?project='+project+"&faileddetail="+faileddetail+"&casesnum="+casesnum+"&failed="+failed+"&failedv="+failedv+"&undo="+undo+"&undov="+undov+"&parentproject="+parentproject,
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
	
	function clearFun() {
		 var te=parent.$('#layout_west_tree2').tree('getSelected')
		$('#searchCondition').form('clear');
		treeGrid.treegrid('load', {});
		setTotal('${excasenode}','',te.id)
	}
	


	function searchFun() {
		 var te=parent.$('#layout_west_tree2').tree('getSelected')
		treeGrid.treegrid('load',
				serializeObject($('#searchCondition')));
		setTotal('${excasenode}',$('#searchCondition').serializeArray(),te.id)
	}

	function serializeObject(form) {
		var o = {};
		$.each(form.serializeArray(), function(index) {
			var value = this['value'], name = this['name'];
			if (value != "" || value != undefined || value != null) {
				if (o[name]) {
					o[name] = o[name] + "," + value;
				} else {
					o[name] = value;
				}
			}
		});
		return o;
	}
	
	

</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
		<div class="easyui-panel" style="padding: 0px;" fit="true">
			<table id="treeGrid"></table>
			</div>
			<div id="tb" style="padding:5px;height:auto">
			<form id="searchCondition">
			<table class="table table-hover table-condensed" style="width:80%">
					<tr>
						<th>测试描述/名称</th>
						<td><input id="searchname" name="name" class="span2" /></td>
						<th>操作步骤</th>
						<td><input id="searchstep" name="step" class="span2" /></td>
						<th>优先级</th>
						<td><input id="searchop" name="op" class="span2" /></td>
						<th>执行人</th>
						<td><input id="searchupdater" name="tester" class="span2" /></td>
						<th>状态</th>
						<td><input id="searchstatus" name="status" class="span2" /></td>
						
				<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'brick_add',plain:true" onclick="searchFun();">过滤条件</a></td>
               <td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'brick_add',plain:true" onclick="clearFun();">清除条件</a></td>
				</tr>
				</table>
			</form> 
				<div id="toolbar">
		<c:if test="${fn:contains(sessionInfo.resourceList, '/excaseController/addPage')}">
			<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'pencil_add'">添加</a>
		</c:if>
	
		<a onclick="redo();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'resultset_next'">展开</a> <a onclick="undo();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'resultset_previous'">折叠</a> <a onclick="treeGrid.treegrid('reload');" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'custer_shuaxin'">刷新</a>
	<%-- 	&nbsp;<img src="${pageContext.request.contextPath}/style/images/extjs_icons/cut_red.png" />&nbsp;选择过滤：<input id="pp" name="pp" style="width:120px" class="easyui-combobox" data-options="iconCls:'resultset_next'"> --%>


	
		<c:if test="${fn:contains(sessionInfo.resourceList, '/excaseController/delete')}">
		<a onclick="deleteFun()" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'package_delete'">批量删除</a>
	   </c:if>
	   <c:if test="${fn:contains(sessionInfo.resourceList, '/excaseController/reportPage')}">
		<a onclick="generateReportPage()" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'custer_baobiao1'">执行状况</a>
	   </c:if>
	    <c:if test="${fn:contains(sessionInfo.resourceList, '/excaseController/reportPage')}">
		<a onclick="doPOnce()" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'heart_add'">一键P</a>
	   </c:if>
<!-- 		 <a href="javascript:void(0);" class="easyui-menubutton" data-options="menu:'#layout_drop',iconCls:'plugin_link'">拖动模式</a> -->
				<!-- <a id="opendrop" onclick="dropt()" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'ruby_get'">开启拖动模式</a>
		        <a id="closedrop" onclick="dropf()" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'ruby_get'">关闭拖动模式</a> -->
	</div>
			</div>
		</div>
	</div>


	<div id="menu" class="easyui-menu" style="width: 120px; display: none;">
		<c:if test="${fn:contains(sessionInfo.resourceList, '/excaseController/delete')}">
			<div onclick="deleteFun();" data-options="iconCls:'pencil_delete'">删除</div>
		</c:if>
		<c:if test="${fn:contains(sessionInfo.resourceList, '/excaseController/editPage')}">
			<div onclick="editFun();" data-options="iconCls:'pencil'">编辑</div>
		</c:if>
		
		
	</div>
	
<!-- 	<div id="layout_drop" style="width: 100px; display: none;">
	<div id="opendrop" onclick="dropt()" onclick="editCurrentUserPwd();">开启</div>
	<div id="closedrop" onclick="dropf()" onclick="currentUserRole();">关闭</div>

</div> -->
</body>
</html>