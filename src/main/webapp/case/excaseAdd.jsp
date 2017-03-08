<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	$(function() {
		
		parent.$.messager.progress('close');
		
		var te=parent.$('#layout_west_tree2')
		var num=getNodeNum(te,te.tree('getSelected'));
		var selected=te.tree('getSelected')
		var project='';
		var projectid=''
	
			
		try{
		 var a=te.tree('getParent',selected.target)
		 var aa=te.tree('getChildren',a.target)
		project+=a.text+'*'
		projectid+=a.id+'*'
			 for(var i=0;i<aa.length;i++){
				 if(aa.length-i==1){
						project+=aa[i].text
						projectid+=aa[i].id
					}else{
						project+=aa[i].text+'*'
						projectid+=aa[i].id+'*'
					}
				}
			 
		 }catch(err){
			 project=te.tree('getParent',selected.target).text
			 projectid=te.tree('getParent',selected.target).id
		 }
	
		
		if(project==''){
			 project=te.tree('getParent',selected.target).text
			 projectid=te.tree('getParent',selected.target).id
		}
		
		console.log(project)
		console.log(projectid)
		 $('#projecttree').combotree({
			url : '${pageContext.request.contextPath}/buscaseController/allTree?project='+project+"&casetype="+"excase"+"&projectid="+projectid,
			parentField : 'pid',
			lines : true,
			multiple: true,
		    checkbox: true,
			panelHeight : 500,
			onLoadSuccess : function() {
				parent.$.messager.progress('close');
			}
		}); 
		

		 $.extend($.fn.tree.methods,{
			 getCheckedExt: function(jq){//扩展getChecked方法,使其能实心节点也一起返回
			 var checked = $(jq).tree("getChecked");
			 var checkbox2 = $(jq).find("span.tree-checkbox2").parent();
			 $.each(checkbox2,function(){
			 var node = $.extend({}, $.data(this, "tree-node"), {
			 target : this
			 });
			 checked.push(node);
			 });
			 return checked;
			 },
			 getSolidExt:function(jq){//扩展一个能返回实心节点的方法
			 var checked =[];
			 var checkbox2 = $(jq).find("span.tree-checkbox2").parent();
			 $.each(checkbox2,function(){
			 var node = $.extend({}, $.data(this, "tree-node"), {
			 target : this
			 });
			 checked.push(node);
			 });
			 return checked;
			 }
			 });
	
		
	});
	
	function send(){
	
		
		var isValid = $("#form").form('validate');
		if(!isValid){
			return
		}
		
		
		parent.$.modalDialog.openner_treeGrid.treegrid('selectAll');
		var dd=parent.$.modalDialog.openner_treeGrid.treegrid('getSelections');
		parent.$.modalDialog.openner_treeGrid.treegrid('unselectAll');
		
		if($("#projecttree").combotree("getValues").length==0){
			parent.$.messager.alert('提示','执行集不能为空', 'info');
			return false
		}
	
		var t=$("#projecttree").combotree("tree")
        var n = t.tree('getChecked'); // 得到选择的节点  
		//console.log(n[0].text)
		
	    var ntree=$('#layout_west_tree2').tree('getSelected')
	    var name=ntree.text
		var nodeid=''
    for(var i=0;i<dd.length;i++){
	nodeid+=dd[i].orgid
		}
    
		 console.log(nodeid)
	for(var i=0;i<n.length;i++){
		 if(contains(nodeid,n[i].id))	{
			 parent.$.messager.alert('提示', '已存在用例:  '+n[i].text, 'info'); 
			 return
		 }
		}

       
		  var ps=''
		        for(var i=0;i<n.length;i++){
		        	ps+=n[i].id+'*'
		        }
		     
		
		parent.$.messager.progress({
			title : '提示',
			text : '数据处理中，请稍后....'
		});
		 $.post('${pageContext.request.contextPath}/excaseController/addEx',{"rowstr":ps,"name":name,"treeid":ntree.id},function(data){	
			 $.messager.show({
					title : '信息提示',
					timeout:20000,
					iconCls: 'icon-info',
					msg : eval(data)
				});
			 parent.$.modalDialog.handler.dialog('close');
			 parent.$.messager.progress('close');
			 parent.$.modalDialog.openner_treeGrid.treegrid('reload');
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
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
		<form id="form" method="post">
			<table class="table table-hover table-condensed">
		
				<tr>
					<th>选择执行用例</th>
					<td colspan="3"><input id="projecttree"  name="projecttree" style="width: 375px; height: 29px;" class="easyui-combotree" data-options="editable:false" /></td>
				</tr>
		
			</table>
			<br><br><br><br>
			<span style="float:right;"><a onclick="send();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'mouse_add'">生成用例</a></span>
		</form>
	</div>
</div>