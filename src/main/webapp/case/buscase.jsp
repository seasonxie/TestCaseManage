<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title>业务用例管理</title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript" src="${pageContext.request.contextPath}/jslib/treegrid-dnd.js" charset="utf-8"></script>
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
	var lastIndex;
	var editingRow;
	var closeArray;
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
		tryy('${buscasenode}');
		
		fetchData(te.template,'${buscasenode}',te.id);
		
		setTotal('${buscasenode}','',te.id)
		
		var s=''
		$(window).keypress( function(event){
			   
			
				if(event.keyCode == '13')
				s+='a'
				
				if(String.fromCharCode( event.which )=='p')
					s+='p'
				
					if(s.length>2){
						s=''
					}
					if(s=='ap'||s=='pa'){
						$('#treeGrid').treegrid('endEdit', lastIndex);
						s=''
					}
		} );

	});
	
	function setTotal(project,search,treeid){
		 $.post('${pageContext.request.contextPath}/buscaseController/total?data='+project+"&treeid="+treeid,search,function(data){	
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
	    	  a1=template1
	    	s = s + a1
	    	$('#template').css("display", "none");
	    	$('#template3').css("display", "none");
	    }else if (nj=='3'){  
	    	  a1=template2
	    	  s = s + a1
	    		$('#template').css("display", "none");
		    	$('#template2').css("display", "none");
	  } else{
		  a1=template
    	  s = s + a1 
    		$('#template2').css("display", "none");
	    	$('#template3').css("display", "none");
	  } 
	    s = s+ "]]";  
	    
	    options.columns = eval(s);  
	    options.url = '${pageContext.request.contextPath}/buscaseController/treeGrid?data='+data+"&treeid="+treeid
	    //options.pagination=true
	    //options.parentField='pid'
	  
	   // options.fitColumns=true;
	   // options.fit=false;
	   // options.striped=false;
	    //lu 增加一列  
	  /*  options.columns[0].push(  
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
				}  
	    );      */
	      
	    $('#treeGrid').treegrid(options);  
	    $('#treeGrid').treegrid('reload');     
	      
	}  
	
	function doSelectChild(row){
		for(var i=0;i<row.length;i++){
			$('#treeGrid').treegrid('select',row[i].id);
			try{
				if(row[i].children.length>0){
					doSelectChild(row[i].children);
				}
				}catch(err){}
		}
	}
	

	var Address = [{ "id": "P1", "text": "P1" }, { "id": "P2", "text": "P2" }, { "id": "P3", "text": "P3" }, { "id": "P4", "text": "P4" }, { "id": "P5", "text": "P5" }];
	function tryy(data){
		
		parent.$.messager.progress({
			title : '提示',
			text : '数据处理中，请稍后....'
		});
	
		treeGrid =$('#treeGrid').treegrid({
			//url : '${pageContext.request.contextPath}/buscaseController/treeGrid?data='+data,
			idField : 'id',
			treeField : 'module',
			parentField : 'pid',
			fit : true,
			nowrap: false,
			pagination:true, //显示分页
			pageSize : 300,//默认选择的分页是每页5行数据
            pageList : [300,500,1000,5000],//可以选择的分页集合  
            loadMsg : '正在加载数据....请稍候....', 
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
		/* 	columns : [ [{
				field : 'ck',
				title : '选择',
				checkbox:true
			},{
				field : 'project',
				title : '项目用例',
				width : 70
			},{
				field : 'casenum',
				title : '用例编号',
				editor:'text',
				width : 70
			},{
				field : 'casestep',
				title : '用例步号',
				editor:'text',
				width : 70
			},{
				field : 'version',
				title : '用例版本',
				editor:'text',
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
				editor:'text',
				width : 70
			},{
				field : 'secpage',
				title : '二级页面',
				editor:'text',
				width : 70
			},{
				field : 'thrpage',
				title : '三级页面',
				editor:'text',
				width : 70
			},{
				field : 'fourpage',
				title : '四级页面',
				editor:'text',
				width : 70
			},{
				field : 'fivepage',
				title : '五级页面',
				editor:'text',
				width : 70
			},{
				field : 'appstatusb',
				title : '用例状态',
				width : 70
			},{
				field : 'module',
				editor:'text',
				title : '测试模块',
				width : 170
			},{
				field : 'modulechild',
				editor:'text',
				title : '子模块',
				width : 70
			},  {
				field : 'name',
				editor:'text',
				title : '测试项目',
				width : 100
			}, {
				field : 'op',
				title : '优先级',
				 editor:{
                     type:'combobox',
                     options:{
                         valueField:'id',
                         textField:'text',
                         data:Address,
                         panelHeight:'auto',
                     }},
				width : 40
			}, {
				field : 'pre',
				editor:'text',
				title : '前置条件',
				width : 120,
			}, {
				field : 'step',
				editor:'text',
				title : '操作步骤',
				width : 320
			}, {
				field : 'expire',
				editor:'text',
				title : '预期结果',
				width : 320
			}, {
				field : 'remark',
				editor:'text',
				title : '备注',
				width : 100,
			}, {
				field : 'seq',
				editor:'text',
				title : '排序',
				width : 30
			}, {
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
			} ] ], */
			toolbar : '#tb',
			onClickRow : function(row) {    
				if (lastIndex != row.id) {
					//否则开始用户点击行编辑,结束上一行编辑
					$('#treeGrid').treegrid('endEdit', lastIndex);					
				}
			/* 	for(var i=0;i<row.children.length;i++){
					$('#treeGrid').treegrid('select',row.children[i].id);
					try{
					if(row.children[i].children.length>0){
						doSelectChild(row.children[i].children);
					}
					}catch(err){}
				} */				
			},
			onDblClickRow:function(row){//运用双击事件实现对一行的编辑  
				if(dropopen==true)
				dropf()
				 editingRow = row.id
				$('#treeGrid').treegrid('beginEdit', editingRow);
				lastIndex = editingRow;  
				    },
				    onAfterEdit : function(row, changes) {
						if (!$.isEmptyObject(changes)) {
							roweidtFun(row);
						}
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
			onBeforeLoad:function(row){
				closeArray=getClosed();
			},
			onLoadSuccess : function(row) {
				for(var i=0;i<closeArray.length;i++){
		            treeGrid.treegrid('collapseAll', closeArray[i]);
		            }
				parent.$.messager.progress('close');
			if(dropopen==true){		
				$('#closedrop').css("display", "block");
				$('#opendrop').css("display", "none");
				enableDnd($('#treeGrid'));
			}else{
				$('#closedrop').css("display", "none");
				$('#opendrop').css("display", "block");
			}
			//$(this).treegrid('tooltip');
			}
		});
		
		
	}
	
	//获取当前被关闭的节点-treegrid
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

	
	var dropopen;
	function dropt(){
		
		dropopen=true;
		//treeGrid.treegrid('unselectAll');
		$('#opendrop').css("display", "none");
		$('#closedrop').css("display", "block");
		enableDnd($('#treeGrid'));
		 $.messager.show({
				title : '信息提示',
				timeout:20000,
				iconCls: 'icon-info',
				msg : '已经入拖动模式,可以操作多条'
			});		
	}
	
	function dropf(){
		dropopen=false;
		$('#opendrop').css("display", "block");
		treeGrid.treegrid('reload');
		 $.messager.show({
				title : '信息提示',
				timeout:20000,
				iconCls: 'icon-info',
				msg : '关闭拖动模式'
			});		
	}
	
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
	
	function enableDnd(t) {
        var nodes = t.treegrid('getPanel').find('tr[node-id]');
        nodes.find('span.tree-hit').bind('mousedown.treegrid', function() {
            return false;
        });
        nodes.draggable({
            disabled: false,
            revert: true,
            cursor: 'pointer',
            proxy: function(source) {
                 var p = $('<div class="tree-node-proxy tree-dnd-no"></div>').appendTo('body');
                p.html($(source).find('.tree-title').html());
                p.hide();
                return p; 
            },
            deltaX: 15,
            deltaY: 15, 
             onBeforeDrag: function() {
            	
                $(this).next('tr.treegrid-tr-tree').find('tr[node-id]').droppable({ accept: 'no-accept' });
               
               
            }, 
            onStartDrag: function() {
                 $(this).draggable('proxy').css({
                    left: -10000,
                    top: -10000
                }); 
                 var node = treeGrid.treegrid('getSelections');
                 var pid=''
         		if (node.length>0) {
         			
         			for(var i=0;i<node.length;i++){
         				pid+=node[i].id
         			}
         			
         			for(var i=0;i<node.length;i++){
         				if(node[i].pid!=undefined&&node[i].pid!=''){
         				if(contains(pid,node[i].pid)){   
         					//console.log(treeGrid.treegrid('getChildren',node[i].id))
                			 $.messager.show({
             					title : '信息提示',
             					timeout:20000,
             					iconCls: 'icon-info',
             					msg : '请不要选择不同级别的用例做拖动'
             				});	
                			treeGrid.treegrid('unselectAll');
                	
                		}
         				}
         			}
         		}
            }, 
            onDrag: function(e) {
            	
       
            	
            	$(this).draggable('proxy').show();
                this.pageY = e.pageY;
            } , 
            onStopDrag: function() {
                $(this).next('tr.treegrid-tr-tree').find('tr[node-id]').droppable({ accept: 'tr[node-id]' });
            } 
        }).droppable({
            accept: 'tr[node-id]',
            onDragOver: function(e, source) {
                var pageY = source.pageY;
                var top = $(this).offset().top;
                var bottom = top + $(this).outerHeight();
                $(source).draggable('proxy').removeClass('tree-dnd-no').addClass('tree-dnd-yes');
                $(this).removeClass('row-append row-top row-bottom');
                if (pageY > top + (bottom - top) / 2) {
                    if (bottom - pageY < 5) {
                        $(this).addClass('row-bottom');
                    } else {
                        $(this).addClass('row-append');
                    }
                } else {
                    if (pageY - top < 5) {
                        $(this).addClass('row-top');
                    } else {
                        $(this).addClass('row-append');
                    }
                }
            },
            onDragLeave: function(e, source) {
                $(source).draggable('proxy').removeClass('tree-dnd-yes').addClass('tree-dnd-no');
                $(this).removeClass('row-append row-top row-bottom');
            },
            onDrop: function(e, source) {
           
               var action, point;
                if ($(this).hasClass('row-append')) {
                    action = 'append';
                } else {
                    action = 'insert';
                    point = $(this).hasClass('row-top') ? 'top' : 'bottom';
                }
                $(this).removeClass('row-append row-top row-bottom');
               // alert(action + ":" + point);
                // your logic code here
                // do append or insert action and reload the treegrid data<br>                 //==================================<br>                 //做自己的逻辑处理
                var src  = $('#treeGrid').treegrid('find', $(source).attr('node-id'));
                
                var dest = $('#treeGrid').treegrid('find', $(this).attr('node-id'));
                
                var nodedrop = $('#treeGrid').treegrid('getSelections');
                
                var oid='';
                if(nodedrop.length>0){
                for(var i=0;i<nodedrop.length;i++){
                	 oid+=nodedrop[i].id+"*"
                }
                }else{
                	oid=src.id
                }             
                 $.post('${pageContext.request.contextPath}/buscaseController/edit',{'oid':oid,'nid':dest.id},function(data){	
									 $.messager.show({
											title : '信息提示',
											timeout:20000, 
											iconCls: 'icon-info',
											msg : '修改成功'
										});
										$("#treeGrid").treegrid('reload'); 					
									}); 
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
					var reload=false;
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
	
	function paseFun() {
	   if(copyData.length>0){
		var node = treeGrid.treegrid('getSelections');
		if (node.length==1) {
			 $.post('${pageContext.request.contextPath}/buscaseController/copy',{"ps":copyData,"pid":node[0].id}, function(result) {
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
			copyData=""	
		}else if(node.length>1){
			 $.messager.show({
					title : '信息提示',
					timeout:20000,
					iconCls: 'icon-info',
					msg : '只能选择一条记录作为上级'
				});	
			 treeGrid.treegrid('clearChecked')
		}else{
			parent.$.messager.confirm('询问', '没有选择父节点用例，将直接黏贴做为根节点，请确认？', function(b) {
		         if (b) {
		        	 $.post('${pageContext.request.contextPath}/buscaseController/copy',{"ps":copyData,"pid":node.id}, function(result) {
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
		        	 copyData=""
		         }
			});	 
		}
	   }else{
		   $.messager.show({
				title : '信息提示',
				timeout:20000,
				iconCls: 'icon-info',
				msg : '请先复制用例'
			});	 
	   }
		
	}
	
	function copyFun() {

		var node = treeGrid.treegrid('getSelections');
		if (node.length>0) {
		
	                
					copyData= "";
		        	$.each(node,function(i,n){
                            if(i==0) {
		        			
                            	copyData +=n.id;
		        	
		        		}else{
		        			copyData +="*"+n.id;
		        		}
		        	});
		        	
		        	 treeGrid.treegrid('clearChecked')
					 $.messager.show({
					title : '信息提示',
					timeout:20000,
					iconCls: 'icon-info',
					msg : '复制成功'+node.length+'条记录'
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
		treeGrid.treegrid('unselectAll');
		if (node) {
			parent.$.modalDialog({
				title : '编辑用例',
				width : 500,
				height :650,
				href : '${pageContext.request.contextPath}/buscaseController/editPage?id=' + node.id,
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
	
	function insertFun() {
		var node = treeGrid.treegrid('getSelections');
		if (node.length==1) {
			$.post('${pageContext.request.contextPath}/buscaseController/insert',{"id":node[0].id,"pid":node[0].pid}, function(result) {
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
		}else if(node.length>1){
			 $.messager.show({
					title : '信息提示',
					timeout:20000,
					iconCls: 'icon-info',
					msg : '只能选择一条记录'
				});	
			 treeGrid.treegrid('clearChecked')
		}else{
			 $.messager.show({
					title : '信息提示',
					timeout:20000,
					iconCls: 'icon-info',
					msg : '请选择一条记录'
				});	
		}
	}
	
	function copyInsertFun() {
		 if(copyData.length>0){
				var node = treeGrid.treegrid('getSelections');
				if (node.length==1) {
					 $.post('${pageContext.request.contextPath}/buscaseController/copyInsert',{"id":copyData,"targetId":node[0].id}, function(result) {
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
					copyData=""	
				}else if(node.length>1){
					 $.messager.show({
							title : '信息提示',
							timeout:20000,
							iconCls: 'icon-info',
							msg : '只能选择一条用例做前置'
						});	
					 treeGrid.treegrid('clearChecked')
				}else{
					 $.messager.show({
							title : '信息提示',
							timeout:20000,
							iconCls: 'icon-info',
							msg : '请至少选择一条用例'
						});	
				}
			   }else{
				   $.messager.show({
						title : '信息提示',
						timeout:20000,
						iconCls: 'icon-info',
						msg : '请先复制用例'
					});	 
			   }
	}
	
	function repl(str){
       try{
		return str.replace(new RegExp("&quot;","gm"),"\"").replace(new RegExp("script","gm"),"sc**pt").replace(new RegExp("\n","gm"),"<br>").replace(new RegExp("&lt;br&gt;","gm"),"<br>")
/* 		.replace(new RegExp("1.","gm"),"<br>1.").replace(new RegExp("2.","gm"),"<br>2.").replace(new RegExp("3.","gm"),"<br>3.") */
       }catch(err){
    	   return str
       }
	}
	
	function roweidtFun(row) {
		var id
		var project
		var module
		var modulechild
		var name
		var op
		var seq
		var remark
		var iconCls
		var pre
		var step
		var expire
		var pid
		
		var casenum
		var casestep
		var version
		var fristpage
		var secpage
		var thrpage
		var fourpage
		var fivepage
	
		$.ajax({
			type : 'post',
			dataType : 'json',
			url : '${pageContext.request.contextPath}/buscaseController/edit',
			data : {
				id : row.id,
				project : row.project,
				module : repl(row.module),
				modulechild : repl(row.modulechild),
				name : repl(row.name),
				op : row.op,
				//seq : parseInt(row.seq),
				treeid:row.treeid,
				seq :row.seq,
				remark : repl(row.remark),
				iconCls : row.iconCls,
				pre : repl(row.pre),
				step : repl(row.step),
				expire : repl(row.expire),
				casenum : repl(row.casenum),				
				casestep : repl(row.casestep),
				version : row.version,
				fristpage : repl(row.fristpage),
				secpage : repl(row.secpage),
				thrpage : repl(row.thrpage),
				fourpage : repl(row.fourpage),
				fivepage : repl(row.fivepage),
				pid : row.pid,
			},
			success : function(r) {
				treeGrid.treegrid('reload');
			
				$.messager.show({
					title : '信息提示',
					iconCls : 'icon-info',
					timeout : 20000,
					msg : r.msg
				});
				
			}
		});
	
	}
	

	function addFun() {
		
       
		var project=parent.$('#layout_west_tree2').tree('getSelected')
		var node = treeGrid.treegrid('getSelected');
		var pid=''
		var seq=0
		try{
		pid=node.id	
		seq=node.seq
		var childs=treeGrid.treegrid('getChildren',pid);
		seq=childs[childs.length-1].seq
		
		}catch(err){}
		
		if(pid==''){
			 $.messager.show({
					title : '信息提示',
					timeout:10000,
					iconCls: 'icon-info',
					msg : '选中列表中的用例，点击添加，添加的用例就会生成在选中节点下面了'
				}); 
		
		treeGrid.treegrid('selectAll');
		var dd=treeGrid.treegrid('getSelections');
	    treeGrid.treegrid('unselectAll');
			
		for(var i=0;i<dd.length;i++){			
		if(dd[i].seq>seq)
			seq=dd[i].seq
		}
		} 

		parent.$.modalDialog({
			title : '添加用例',
			width : 500,
			height :650,
			href : '${pageContext.request.contextPath}/buscaseController/addPage?project='+project.text+"&pid="+pid+"&seq="+seq+"&treeid="+project.id,
			buttons : [ {
				text : '添加',
				handler : function() {
					parent.$.modalDialog.openner_handle='添加' 
					parent.$.modalDialog.openner_treeGrid = treeGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
					var f = parent.$.modalDialog.handler.find('#form');
					f.submit();
					
				}
			} , {
				text : '继续添加',
				handler : function() {
					parent.$.modalDialog.openner_handle='继续添加' 
					parent.$.modalDialog.openner_treeGrid = treeGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
					var f = parent.$.modalDialog.handler.find('#form');
					f.submit();
					
				}
			}]
		});

	}
	
	
	
	function generateCaseFun() {
		var project='请在不要再项目目录生成执行用例'
			var projects=''
	   treeGrid.treegrid('selectAll');
	   var dd=treeGrid.treegrid('getSelections');
		treeGrid.treegrid('unselectAll');
		
		
		
        try{
        	var te=parent.$('#layout_west_tree2')
			project=te.tree('getSelected').text	
        }catch(err){
        
			project=dd[0].project
        }
			
		
			if(dd.length==0){
			project='【warning】 : 当前项目没有用例'
		}
			
	
			
		//var project=parent.$('#layout_west_tree2').tree('getSelected').text
		parent.$.modalDialog({
			title : '生成执行集',
			width : 500,
			height :320,
			href : '${pageContext.request.contextPath}/buscaseController/generateCase?project='+project+"&num="+dd.length,
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
	
	function outputFun(){
		var node = treeGrid.treegrid('getSelections');
		if (node.length>0) {
		
					parent.$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍后....'
					});
	                
		        	var ps = "";
		        	$.each(node,function(i,n){
                            if(i==0) {
		        			
		        			ps +=n.id;
		        	
		        		}else{
		        			ps +="*"+n.id;
		        		}
		        	});
		        	
		            $.ajax({
		                type: "POST",
		                url: "${pageContext.request.contextPath}/buscaseController/output",
		                data:{"ps":ps},
		                
		              
		                success: function (data) {
		                	 var te=parent.$('#layout_west_tree2').tree('getSelected')
		                    var template="1"
		                    template=te.template
		                	var url ='${pageContext.request.contextPath}/buscaseController/output?do='+template;			        		
			                window.location.href=url;  
		                },
		                error: function () {
		                   
		                }
		            });
					
		        /* 	var url ='${pageContext.request.contextPath}/buscaseController/output?ps='+ps;

		        		
		                window.location.href=url;  */
		                parent.$.messager.progress('close');
			
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
	
	function downloadd(template) {

		var url = ''
		if(template=='1'){
			url='${pageContext.request.contextPath}/buscaseController/download';
		}else if(template=='2'){
			url='${pageContext.request.contextPath}/buscaseController/download2';
		}else if(template=='3'){
			url='${pageContext.request.contextPath}/buscaseController/download3';
		}
		
        window.location.href=url; 
	}
	
	//拼装公共用例
	function editCaseFun(){
		var node = treeGrid.treegrid('getSelected');
		if(node){
			window.location.href='${pageContext.request.contextPath}/buscaseController/collectcase?pid='+node.id+"&module="+node.module+"&modulechild="+node.modulechild+"&project="+node.project+"&treeid="+node.treeid
		}else{
			 $.messager.show({
					title : '信息提示',
					timeout:20000,
					iconCls: 'icon-info',
					msg : '请选择要拼装的用例级'
				});
		}
	}
	
	function clearFun() {
		$('#searchCondition').form('clear');
		treeGrid.treegrid('load', {});
		setTotal('${buscasenode}')
	}
	


	function searchFun() {
		treeGrid.treegrid('load',
				serializeObject($('#searchCondition')));
		setTotal('${buscasenode}',$('#searchCondition').serializeArray())
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
	
	
	//右键粘贴进入剪切板
	function copyInnerHtml()
	{
	var node = treeGrid.treegrid('getSelected');	
/* 	ZeroClipboard.setMoviePath( '${pageContext.request.contextPath}/jslib/ZeroClipboard.swf' )
	clip = new ZeroClipboard.Client(); 
	clip.setText( '' );
	 clip.setCSSEffects( true );
	    clip.setText(node.id);  //复制的文本

	    clip.glue( 'copy_button'); //注册id按钮，可以是其它非input元素
	    var clip = new ZeroClipboard( document.getElementById("copy_button"), {
	    	  moviePath: "${pageContext.request.contextPath}/jslib/ZeroClipboard.swf"
	    	} );
	    clip.setText(node.id); */
	   $('#copy_button').zclip({
	    	path:'${pageContext.request.contextPath}/jslib/ZeroClipboard.swf',
	    	copy:function(){
	    	return node.id;
	    	}
	    	});
	   
	}
	
	//一键排序
	function setOrder()
	{
		parent.$.messager.confirm('询问', '是否要对当前用例做重新排序，会按现有排序重组排序数字，请放心？', function(b) {
	   if(b){
		var project=parent.$('#layout_west_tree2').tree('getSelected').id

		parent.$.messager.progress({
			title : '提示',
			text : '数据处理中，请稍后....'
		});
        $.ajax({
        	type : 'post',
			dataType : 'json',
            url: "${pageContext.request.contextPath}/buscaseController/setOrder",
            data:{"project":project},
            
          
            success: function (data) {
            	parent.$.messager.progress('close');
            },
            error: function () {
            	parent.$.messager.progress('close');
 
               treeGrid.treegrid('reload')
               $.messager.show({
   				title : '信息提示',
   				timeout:20000,
   				iconCls: 'icon-info',
   				msg : '排序完成'
   			});
            }
        });
		
		
       
	   }
		 });
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
						<th>更新人</th>
						<td><input id="searchupdater" name="updater" class="span2" /></td>
						
				<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'brick_add',plain:true" onclick="searchFun();">过滤条件</a></td>
               <td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'brick_add',plain:true" onclick="clearFun();">清除条件</a></td>
				</tr>
				</table>
			</form> 
	
	<div id="toolbar">
		<c:if test="${fn:contains(sessionInfo.resourceList, '/buscaseController/addPage')}">
			<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'pencil_add'">添加</a>
		</c:if>
		   <c:if test="${fn:contains(sessionInfo.resourceList, '/buscaseController/insert')}">

						<a onclick="insertFun()" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'disconnect'">插入用例</a>
	   </c:if>
	
		<a onclick="redo();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'resultset_next'">展开</a> <a onclick="undo();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'resultset_previous'">折叠</a> <a onclick="treeGrid.treegrid('reload');" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'custer_shuaxin'">刷新</a>
	<%-- 	&nbsp;<img src="${pageContext.request.contextPath}/style/images/extjs_icons/cut_red.png" />&nbsp;选择过滤：<input id="pp" name="pp" style="width:120px" class="easyui-combobox" data-options="iconCls:'resultset_next'"> --%>
		
		<c:if test="${fn:contains(sessionInfo.resourceList, '/buscaseController/uploadPage')}">
			 <a href="javascript:void(0);" class="easyui-menubutton" data-options="menu:'#layout_drop',iconCls:'plugin_link'">拖动模式</a>

		<a onclick="upload()" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'custer_daoru'">导入</a>
        </c:if>
		<c:if test="${fn:contains(sessionInfo.resourceList, '/buscaseController/editCase')}">
			<a onclick="editCaseFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'mouse_add'">拼装用例</a>
		</c:if>
		<c:if test="${fn:contains(sessionInfo.resourceList, '/buscaseController/generateCase')}">
			<a onclick="generateCaseFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'building_add'">生成执行集</a>
		</c:if>
		<c:if test="${fn:contains(sessionInfo.resourceList, '/buscaseController/delete')}">
		<a onclick="deleteFun()" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'package_delete'">批量删除</a>
	   </c:if>
	  
	   <c:if test="${fn:contains(sessionInfo.resourceList, '/buscaseController/copy')}">
	  
	   <a onclick="copyFun()" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'cake'">复制用例</a>
		
	   </c:if>
	    	     <c:if test="${fn:contains(sessionInfo.resourceList, '/buscaseController/copyInsert')}">
	    	       <a href="javascript:void(0);" class="easyui-menubutton" data-options="menu:'#paseCase',iconCls:'cut'">粘贴</a>

	   </c:if>
	   
	  
	   
	      
	
	   
	    <c:if test="${fn:contains(sessionInfo.resourceList, '/buscaseController/download')}">

					  <a href="javascript:void(0);" class="easyui-menubutton" data-options="menu:'#layout_template',iconCls:'ruby_put'">模板/用例下载</a>
	   </c:if>
	   
	    <c:if test="${fn:contains(sessionInfo.resourceList, '/buscaseController/setOrder')}">

						<a onclick="setOrder()" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'bullet_key'">一键排序</a>
	   </c:if>
	   
	  
	
				<!-- <a id="opendrop" onclick="dropt()" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'ruby_get'">开启拖动模式</a>
		        <a id="closedrop" onclick="dropf()" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'ruby_get'">关闭拖动模式</a> -->
	</div>
		</div>
	</div>
	
	</div>
	<div id="menu" class="easyui-menu" style="width: 120px; display: none;">
		<c:if test="${fn:contains(sessionInfo.resourceList, '/buscaseController/addPage')}">
			<div onclick="addFun();" data-options="iconCls:'pencil_add'">增加</div>
		</c:if>
		<c:if test="${fn:contains(sessionInfo.resourceList, '/buscaseController/delete')}">
			<div onclick="deleteFun();" data-options="iconCls:'pencil_delete'">删除</div>
		</c:if>
		<c:if test="${fn:contains(sessionInfo.resourceList, '/buscaseController/editPage')}">
			<div onclick="editFun();" data-options="iconCls:'pencil'">编辑</div>
		</c:if>
		<c:if test="${fn:contains(sessionInfo.resourceList, '/buscaseController/copy')}">
		<div onclick="copyFun();" data-options="iconCls:'cake'">复制</div>	
	   </c:if>
	   	 <c:if test="${fn:contains(sessionInfo.resourceList, '/buscaseController/copyInsert')}">
	    	<div onclick="paseFun();" data-options="iconCls:'joystick'">粘贴用例</div>	    
            <div onclick="copyInsertFun();" data-options="iconCls:'joystick_add'">粘贴插入</div>	
	   </c:if>
		<!-- <div id="copy_button" onclick="copyInnerHtml();" data-options="iconCls:'pencil'">复制ID</div> -->
		
	</div>
	
	<div id="layout_drop" style="width: 100px; display: none;">
	<div id="opendrop" onclick="dropt()" onclick="editCurrentUserPwd();">开启</div>
	<div id="closedrop" onclick="dropf()" onclick="currentUserRole();">关闭</div>
    </div>
    
    <div id="layout_template" style="width: 100px; display: none;">
	<div id="template" onclick="downloadd('1')">模板下载</div>
	<div id="template2" onclick="downloadd('2')">模板下载</div>
	<div id="template3" onclick="downloadd('3')">模板下载</div>
	   <c:if test="${fn:contains(sessionInfo.resourceList, '/buscaseController/output')}">
          <div onclick="outputFun()">导出用例</div>
						<!-- <a onclick="outputFun()" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'anchor'">导出</a> -->
	   </c:if>
    </div>
    
     <div id="paseCase" style="width: 100px; display: none;">
	<div onclick="paseFun()">粘贴用例</div>
	<div onclick="copyInsertFun()">粘贴插入</div>
	
    </div>
</body>
</html>