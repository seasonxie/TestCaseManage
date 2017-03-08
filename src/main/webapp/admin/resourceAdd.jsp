<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	$(function() {
		$('#iconCls').combobox({
			data : $.iconData,
			formatter : function(v) {
				return $.formatString('<span class="{0}" style="display:inline-block;vertical-align:middle;width:16px;height:16px;"></span>{1}', v.value, v.value);
			}
		});
		
		var moban = [{ "id": "1", "text": "默认模板" }, { "id": "2", "text": "数据采集模板" }, { "id": "3", "text": "多媒体模板" }];
		$('#template').combobox({ 
			data:moban, 
			valueField:'id', 
			textField:'text',
		    panelHeight:'auto',
		    required:true
		    //editable:true,//不可编辑，只能选择
			});  

		$('#pid').combotree({
			url : '${pageContext.request.contextPath}/resourceController/tree?type=all',
			parentField : 'pid',
			lines : true,
			panelHeight : 'auto',
			onLoadSuccess : function() {
				
				
				parent.$.messager.progress('close');
				$('#pid').combotree('tree').tree('collapseAll');
				
			},
			onSelect:function(record){  
				var t = $("#pid").combotree('tree'); // 得到树对象  
			
				var n = t.tree('getSelected');
			
				
			    var name=''
			    name=getName(t,n)
			  
	
			    if(name=="公共用例"&&$("#resourcetype").combobox("getValue")==2){
			    document.getElementById("url").value='/caseController/manager';
			    return;
			    }
			    if(name=="业务用例"&&$("#resourcetype").combobox("getValue")==2){
				    document.getElementById("url").value='/buscaseController/manager';
				    return;
				    }
			    if(name=="执行集管理"&&$("#resourcetype").combobox("getValue")==2){
			    	parent.$.messager.alert('提示','执行集目录请在业务用例中生成执行集', 'info');
					return;
				    }
			    
			    
		}  
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
	    	   
	     function isParent(target){
	    	 var t = $("#pid").combotree('tree');
	    	 try{
				    text=t.tree('getParent',target)
				    tt= text.text
				    return tt
				    }catch(err){
				    	return 'false'
				    }
	     }
	     
	     function getName(t,n){
	    		var name=''
					var check=0
					var treenode
					 while(true){
						if(check==0){
					   treenode=n				 
						}else{
							 try{
								  treenode=t.tree('getParent',treenode.target)
								    }catch(err){
								  
								    }				 
						}
					   var root=isParent(treenode.target)
						if(root!='false'){
							name=root
						}else{
							if(check==0){
								name=treenode.text
							}
							break
						}
					   check++
					}
	    		return name
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
					   var root=isParent(treenode.target)
						if(root=='false'){
							break}
		
					   check++
					}
	    		return check
	     }

		$('#form').form({
			url : '${pageContext.request.contextPath}/resourceController/add',
			onSubmit : function() {
				parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				var isValid = $(this).form('validate');
				if (!isValid) {
					parent.$.messager.progress('close');
				}
				if($("#pid").combotree("getValue").length==0){
					parent.$.messager.alert('提示','上级资源不能为空', 'info');
					parent.$.messager.progress('close');
					return false
				}
			
				var t = $("#pid").combotree('tree');
				var n = t.tree('getSelected');
				
				   var name=''
					    name=getName(t,n)
					   
					    if(name=='执行集管理'&&$("#resourcetype").combobox("getValue")==2){
					    	parent.$.messager.alert('提示','执行集目录请在业务用例中生成执行集', 'info');
							parent.$.messager.progress('close');
							return false
						    }
				   
				/*    if( getNodeNum(t,n)==3&&$("#resourcetype").combobox("getValue")==2){
				    	parent.$.messager.alert('提示','最多只允许4层子目录,请不要选择该层级新建目录', 'info');
						parent.$.messager.progress('close');
						return false
					    } */
				return isValid;
			},
			success : function(result) {
				parent.$.messager.progress('close');
				result = $.parseJSON(result);
				if (result.success) {
					try{
					parent.layout_west_tree0.tree('reload');
					var node=parent.layout_west_tree2.tree('getSelected');
					parent.layout_west_tree2.tree('append', {
						parent: node.target,
						data: [{
							id:result.id,
							text:result.name,
							state:'open',
							attributes:{url:'/buscaseController/manager'}
						}]
					});
					}catch(err){}
				
					
					//parent.layout_west_tree2.tree('reload');
					parent.$.modalDialog.handler.dialog('close');
					parent.$.modalDialog.openner_treeGrid.treegrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_treeGrid这个对象，是因为resource.jsp页面预定义好了
				}else{					
					parent.$.messager.alert('提示', result.msg, 'info');
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
					<td><input name="id" type="text" class="span2" value="${resource.id}" readonly="readonly"></td>
					<th>资源名称</th>
					<td><input name="name" type="text" placeholder="请输入资源名称" class="easyui-validatebox span2" data-options="required:true" value=""></td>
				</tr>
				<tr>
					<th>资源路径</th>
					<td><input name="url" id="url" type="text" placeholder="请输入资源路径" class="easyui-validatebox span2" value=""></td>
					<th>资源类型</th>
					<td><select id="resourcetype" name="typeId" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
							
							<c:choose>  
  
                           <c:when test="${sessionInfo.name=='admin'}">   
                           <c:forEach items="${resourceTypeList}" var="resourceType"> 
                           <option value="${resourceType.id}">${resourceType.name}</option>
                           </c:forEach>
                             </c:when>  
     
                           <c:otherwise> 
                            <c:forEach items="${resourceTypeList}" var="resourceType">  
                            <c:if test="${resourceType.id=='2'}"> 
								<option value="${resourceType.id}">${resourceType.name}</option>
								</c:if> 
								 </c:forEach>
                          </c:otherwise>  
                          </c:choose>  
						     
							
					</select></td>
				</tr>
				<tr>
					<th>排序</th>
					<td><input name="seq" value="100" class="easyui-numberspinner" style="width: 140px; height: 29px;" required="required" data-options="editable:false,min:100"></td>
					<th>上级资源</th>
					<td><select id="pid" name="pid" style="width: 140px; height: 29px;"></select><img src="${pageContext.request.contextPath}/style/images/extjs_icons/cut_red.png" onclick="$('#pid').combotree('clear');" /></td>
				</tr>
				
				<tr>
					<th>备注</th>
					<td><input name="remark" type="text" placeholder="请输入备注" class="easyui-validatebox span2" value=""></td>
				<th>用例模板</th>
					<td><select name="template" id="template" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
					</select></td>
				</tr>
				<tr>
					<th>菜单图标</th>
					<td colspan="3"><input id="iconCls" name="iconCls" style="width: 375px; height: 29px;" data-options="editable:false" /></td>
				</tr>
			</table>
		</form>
	</div>
</div>