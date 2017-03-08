<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript">
    var loadSuite=0;
	var layout_west_tree0;
	var layout_west_tree2;
	var layout_west_tree_url = '';
	var layout_west_tree_url2 = '';
	var sessionInfo_userId = '${sessionInfo.id}';
	var sessionInfo_dept = '${sessionInfo.groupn}';
	

	if (sessionInfo_userId) {
		layout_west_tree_url = '${pageContext.request.contextPath}/resourceController/tree?type=0';
		layout_west_tree_url2 = '${pageContext.request.contextPath}/resourceController/tree?type=2';
	}
	$(function() {
		
		layout_west_tree0 = $('#layout_west_tree0').tree({
			
			url : layout_west_tree_url,
			parentField : 'pid',
			//lines : true,
			onClick : function(node) {
		
               
               
				if (node.attributes && node.attributes.url) {
					var url;
					if (node.attributes.url.indexOf('/') == 0) {/*如果url第一位字符是"/"，那么代表打开的是本地的资源*/
							 url = '${pageContext.request.contextPath}' + node.attributes.url; 						
						//console.log(url.indexOf('/druidController') == -1)
						if (url.indexOf('/druidController') == -1) {/*如果不是druid相关的控制器连接，那么进行遮罩层屏蔽*/
							/* parent.$.messager.progress({
								title : '提示',
								text : '数据处理中，请稍后....'
							}); */
						}
					} else {/*打开跨域资源*/
						url = node.attributes.url;
					}
					addTab({
						url : url,
						title : node.text,
						iconCls : node.iconCls
					});
				}
                
			},
			onBeforeLoad : function(node, param) {
				if (layout_west_tree_url) {//只有刷新页面才会执行这个方法
					parent.$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍后....'
					});
				}
			},
			onLoadSuccess : function(node, data) {
				parent.$.messager.progress('close');
			}
		});
		
		
layout_west_tree2 = $('#layout_west_tree2').tree({
			
			url : layout_west_tree_url2,
			parentField : 'pid',
			animate:true,
    		//lines:true,
    		formatter:function(node){
                var s = node.text;
               
                return '<span style="font-size:15px;font-family: 黑体, 宋体(GB)">'+s+'</span>';
            },	 
			//lines : true,
			onClick : function(node) {         
				if (node.attributes && node.attributes.url) {
					var url;
					var project='';
					if (node.attributes.url.indexOf('/') == 0) {/*如果url第一位字符是"/"，那么代表打开的是本地的资源*/
                        
						//业务用例
						if(node.attributes.url=='/buscaseController/manager'){
							
						
					    var te=$('#layout_west_tree2')
						var num=getNodeNum(te,te.tree('getSelected'));						
						console.log(num)					
													
							 try{
						 var aa=te.tree('getChildren',te.tree('getSelected').target)
						project+=node.id+','
							 for(var i=0;i<aa.length;i++){								
									if(aa.length-i==1){
										project+=aa[i].id
									}else{
										project+=aa[i].id+','
									}
								}
							 
						 }catch(err){
							 project=node.id
						 }
						



						}else{
							project=node.id
						}
					
					//公共用例
					if(node.attributes.url=='/caseController/manager'){
						project=node.text
					}
					
					//project还是空
					if(project==''){ 
					project=node.id						
					}
					   console.log(project)
                     

					url = '${pageContext.request.contextPath}' + node.attributes.url+'?node='+project;

			    	if (url.indexOf('/druidController') == -1) {/*如果不是druid相关的控制器连接，那么进行遮罩层屏蔽*/
				
					}
			    	
			    	
					} else {/*打开跨域资源*/
						url = node.attributes.url;
					}
					addTab({
						url : url,
						title : node.text,
						iconCls : node.iconCls
					});
				}
                 
			},
			onBeforeLoad : function(node, param) {
			/* 	opened=getOpen() */
				if (layout_west_tree_url2) {//只有刷新页面才会执行这个方法
					parent.$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍后....'
					});
				}
			},
			 onContextMenu: function (e,node) {
                 e.preventDefault();
				$(this).tree('select', node.target); 
                 $("#tabsMenu").menu('show', {
                     left: e.pageX,
                     top: e.pageY
                 })
             },
      
			onLoadSuccess : function(node, data) {
				console.log(!contains(sessionInfo_dept,'内部项目'))
					var node = layout_west_tree2.tree('getRoots');
					
					  for(var i=0;i<node.length;i++){
					  var nodechild=layout_west_tree2.tree('getChildren',node[i].target);
					   for(var j=0;j<nodechild.length;j++){
							 try{
								    if(!contains(sessionInfo_dept,nodechild[j].text))
									 layout_west_tree2.tree('collapseAll', nodechild[j].target);	 
								
								
								 
							 }catch(err){}
					   }
					  }
				
				parent.$.messager.progress('close');
			}
		});
		
	});
	
	var opened
	function getOpen(){
		console.log('sss')
		var opens=new Array()
		try{
		var num=0
		var node = layout_west_tree2.tree('getRoots');	
		console.log(node)
		  for(var i=0;i<node.length;i++){
		  var nodechild=layout_west_tree2.tree('getChildren',node[i].target);
		 
		   for(var j=0;j<nodechild.length;j++){
			   try{
			      if(nodechild[j].state=='open'&&nodechild[j].children)
			    	 
			    	  opens[num]=nodechild[j]
			    	  num++
			   }catch(err){}
		   }
		  }
		}catch(err){
			
		}
			return opens
	}

	function addTab(params) {
		
		var iframe = '<iframe id="currentFrame" name="currentFrame" src="' + params.url + '" frameborder="0" style="border:0;width:100%;height:98%;"></iframe>';
		var t = $('#index_tabs');
		var bb=t.tabs('tabs')
		if(bb.length>1){
			t.tabs('close', bb[1].panel('options').title);
		}
		var opts = {
			title : params.title,
			closable : true,
			iconCls : params.iconCls,
			content : iframe,
			border : false,
			fit : true
		};
		if (t.tabs('exists', opts.title)) {
			t.tabs('select', opts.title); //refresh
			  var tab = t.tabs('getSelected');
			  //tab.panel('refresh');
			  
			 t.tabs('update', {  
		            tab: tab,  
		            options: {  
		                    content:iframe,  
		            }  
		        });  
			parent.$.messager.progress('close');
		} else {
			t.tabs('add', opts);
		}
	}
	
	function addRFun() {
		var num=getNodeNum($('#layout_west_tree2'),$('#layout_west_tree2').tree('getSelected'));
		
		
		if(num==3&&$('#layout_west_tree2').tree('getSelected').template!='2'){
			 $.messager.show({
					title : '信息提示',
					timeout:20000, 
					iconCls: 'icon-info',
					msg : '最底层节点不能为父节点'
				});
		}else{
		parent.$.modalDialog({
			title : '添加资源',
			width : 500,
			height : 300,
			href : '${pageContext.request.contextPath}/resourceController/addPage',
			buttons : [ {
				text : '添加',
				handler : function() {
					
					//parent.$.modalDialog.openner_treeGrid = treeGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
					var f = parent.$.modalDialog.handler.find('#form');
					f.submit();
				}
			} ],
			onLoad: function() {
				var id=$('#layout_west_tree2').tree('getSelected');	
				$('#pid').combotree({
					parentField : 'pid',
					lines : true,
					value : id.id
				});
			}
		});
		}
	}
	
	function editRFun(){
		var node=$('#layout_west_tree2').tree('getSelected')
		parent.$.modalDialog({
			title : '编辑资源',
			width : 500,
			height : 300,
			href : '${pageContext.request.contextPath}/resourceController/editPage?id=' + node.id,
			buttons : [ {
				text : '编辑',
				handler : function() {
					//parent.$.modalDialog.openner_treeGrid = treeGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
					var f = parent.$.modalDialog.handler.find('#form');
					f.submit();
				}
			} ]
		});
	}
	
	function deleteRFun(){
		var node=$('#layout_west_tree2').tree('getSelected')
		try{
			if(node.children.length>0){
				parent.$.messager.alert('提示', '请不要删除有子节点的导航', 'info');
				return
			}}catch(err){
			}
		try{
		var t=document.currentFrame.treeGrid;
		t.treegrid('selectAll');
		var dd=t.treegrid('getSelections');
		t.treegrid('unselectAll');
		if(dd.length>0){
			parent.$.messager.alert('提示', '被删除导航下用例数有: '+dd.length+" 条，请谨慎删除这些用例后再做导航删除操作", 'info');
			return
		}
		
		}catch(err){
			parent.$.messager.alert('提示', '请点击进去该导航下面查看已存在的用例', 'info');
			return
		}
		
		
		
		parent.$.messager.confirm('询问', '您是否要删除当前资源？', function(b) {
			if (b) {
				parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				$.post('${pageContext.request.contextPath}/resourceController/delete', {
					id : node.id
				}, function(result) {
					if (result.success) {
						parent.$.messager.alert('提示', result.msg, 'info');
						parent.layout_west_tree2.tree('reload');
					}
					parent.$.messager.progress('close');
				}, 'JSON');
			}
		});
	}
	
    function getNodeNum(t,n){
		var check=0
		var treenode
		 while(true){
			if(check==0){
		   treenode=n				 
			}else{
				 try{
					  treenode=t.tree('getParent',treenode.target)
					    }catch(err){}				 
			}
		   var root=isParent(t,treenode.target)
			if(root=='false'){
				break
				}

		   check++
		}
	return check
}
    
    function isParent(t,target){
   	 try{
			    text=t.tree('getParent',target)
			    tt= text.text
			    return tt
			    }catch(err){
			    	return 'false'
			    }
    }
</script>
<div class="easyui-accordion" data-options="fit:true,border:false">

 <div title="用例管理" data-options="border:false,iconCls:'anchor'">
	
	<div class="well well-small">

			<ul id="layout_west_tree2"></ul>
		</div>
	</div>  
	<div title="系统管理" style="padding: 5px;" data-options="border:false,isonCls:'anchor',tools : [ {
				iconCls : 'database_refresh',
				handler : function() {
					$('#layout_west_tree0').tree('reload');
				}
			}, {
				iconCls : 'resultset_next',
				handler : function() {
					var node = $('#layout_west_tree0').tree('getSelected');
					if (node) {
						$('#layout_west_tree0').tree('expandAll', node.target);
					} else {
						$('#layout_west_tree0').tree('expandAll');
					}
				}
			}, {
				iconCls : 'resultset_previous',
				handler : function() {
					var node = $('#layout_west_tree0').tree('getSelected');
					if (node) {
						$('#layout_west_tree0').tree('collapseAll', node.target);
					} else {
						$('#layout_west_tree0').tree('collapseAll');
					}
				}
			} ]">
		<div class="well well-small">
			<ul id="layout_west_tree0"></ul>
		</div>
	</div>
	 <div id="tabsMenu" class="easyui-menu" style="width:120px;">  
	 <div id="addresource" onclick="addRFun();" data-options="iconCls:'pencil_add'">增加</div>
	 <div id="editresource" onclick="editRFun();" data-options="iconCls:'pencil'">编辑</div>
	 <div id="deresource" onclick="deleteRFun();" data-options="iconCls:'pencil_delete'">删除</div>

  </div>  
</div>