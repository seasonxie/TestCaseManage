<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
   var treeselect
	$(function() {
		
		parent.$.messager.progress('close');
		
		var te=$('#layout_west_tree2')
		treeselect=te.tree('getSelected')
		var num=getNodeNum(te,te.tree('getSelected'));
		
		var project='';
	    
		//第二层
				
		try{
		var aa=te.tree('getChildren',te.tree('getSelected').target)
		project+=treeselect.id+'*'
			 for(var i=0;i<aa.length;i++){
				 if(aa.length-i==1){
						project+=aa[i].id
					}else{
						project+=aa[i].id+'*'
					}
				}
			 
		 }catch(err){
			 project=treeselect.id
		 }
	
		
		
		if(project==''){
			project=treeselect.id	
		}
		
		console.log(project)
		
	 $('#projecttree').combotree({
			url : '${pageContext.request.contextPath}/buscaseController/allTree?project='+project+"&treeid="+treeselect.id,
			parentField : 'pid',
			lines : true,
			multiple: true,
		    checkbox: true,
			panelHeight : 500,
			onLoadSuccess : function() {
				parent.$.messager.progress('close');
			}
		});  
		
/* 
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
	 */
		
	});
	
	function send(){

	        
		
		
		var t=$("#projecttree").combotree("tree")
        var n = t.tree('getChecked'); // 得到选择的节点  
       
		var oid=treeselect.id
	    var oname=treeselect.text
		
	    var ps=''
        for(var i=0;i<n.length;i++){
        	if(n.length-i==1){
        		ps+=n[i].id
        	}else{ps+=n[i].id+'*'}
        	
        }
      
/*     	var rowstrr = JSON.stringify(n);
		var rowstr=encodeURIComponent(rowstrr) */
		var name=$("#runname").val();
		var template=treeselect.template
		
		var nodes =t.tree('getChecked','indeterminate');
		  for(var i=0;i<nodes.length;i++){
	        	ps+='*'+nodes[i].id      	
	        }
		  
		
		  var isValid = $("#form").form('validate');
			if(!isValid){
				return
			}

			if($("#projecttree").combotree("getValues").length==0){
				parent.$.messager.alert('提示','执行集不能为空', 'info');
				parent.$.messager.progress('close');
				return false
			}

		parent.$.messager.progress({
			title : '提示',
			text : '数据处理中，请稍后....'
		});
		
		$.ajax({
			type : 'post',
			dataType : 'json',
			url :'${pageContext.request.contextPath}/excaseController/add',
			data : {"oid":oid,"oname":oname,"rowstr":ps,"name":name,"template":template},
			success : function(r) {
				var datas=eval(r)
				 if(contains(datas[0],'成功')){
					 parent.$.messager.alert('提示', datas[0]+' <br>请到用例集管理列表查看用例');
					 var treenode =$('#layout_west_tree2').tree('find',datas[3]);
				$('#layout_west_tree2').tree('append', {
					parent :treenode.target,
	                data: [{
	                	id: datas[1],
	                 	text: datas[2],
	                 	template:template,
	                 	attributes:{ url:'/excaseController/manager'}
	                   
	                  }]
                   });
				
				
		
				 parent.$.modalDialog.handler.dialog('close');
		
				 var uri='${pageContext.request.contextPath}' + '/excaseController/manager'+'?node='+datas[2]
				 addTab({
						url :uri ,
						title :datas[2]
					});
				 }else{
					 parent.$.messager.alert('提示', datas[0]);
				 }
				
				 parent.$.messager.progress('close');
				
			}
		});
	
		/*  $.post('${pageContext.request.contextPath}/excaseController/add',{"oid":oid,"oname":oname,"rowstr":ps,"name":name,"template":template},function(data){	
	 	 $.messager.show({
					title : '信息提示',
					timeout:20000,
					iconCls: 'icon-info',
					msg : data
				}); 
				console.log(treeselect)
			var datas=eval(data)
			 if(contains(datas[0],'成功')){
				// parent.$.messager.alert('提示', eval(data)+' <br>请到用例集管理列表查看用例');
			 ///console.log(rrr)
			 $('#layout_west_tree2').tree('reload')
			 parent.$.modalDialog.handler.dialog('close');
			 var id=datas[1]
			 console.log(id)
			 var treenode =$('#layout_west_tree2').tree('find',id);
			 console.log(treeselect)
			 $('#layout_west_tree2').tree('check',treeselect.target)
			 var uri='${pageContext.request.contextPath}' + '/excaseController/manager'+'?node='+datas[2]
			 console.log(uri)
			 addTab({
					url :uri ,
					title :datas[2]
				});
			 }else{
				 parent.$.messager.alert('提示', datas[0]);
			 }
			
			 parent.$.messager.progress('close');
		 }); */
	}
	
	function selectAll(){
		var t=$("#projecttree").combotree("tree")
		var r=t.tree('getRoots')
		for(var i=0;i<r.length;i++){
			t.tree('check',r[i].target)
		}
	
		
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
					<th>用例:</th>
					<td>${caseproject}</td>
			
					
				</tr>	<tr>
						<th>总用例数:</th>
					<td>${num}</td>
					</tr>	
					<tr>
					<th>执行集用例名称:</th>
					<td><input name="runname" id="runname" type="text" placeholder="请输入执行集名字" data-options="required:true" class="easyui-validatebox span2" value=""></td>

				   </tr>		
				<tr>
					<th>选择执行用例</th>
					<td colspan="3"><input id="projecttree" name="projecttree" style="width: 375px; height: 29px;" class="easyui-combotree" data-options="editable:false" /></td>
				</tr>
		
			</table>
			<span style="float:right;"><a onclick="selectAll();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'shading'">全选</a></span>
			<br><br><br><br>
			<span style="float:right;"><a onclick="send();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'mouse_add'">生成用例</a></span>
		</form>
	</div>
</div>