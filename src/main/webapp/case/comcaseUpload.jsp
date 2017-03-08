<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 <script src="${pageContext.request.contextPath}/jslib/jquery-form.js"></script>
<script type="text/javascript">
$(function() {
	parent.$.messager.progress('close');
})

function checkupload(){
    var filename=$("#uploadExcel").val();
    console.log(filename)
 
     if (filename == undefined) { 
      	parent.$.messager.alert('提示','请选择上传文件！','info');
     	}else{
      var mime=filename.toLowerCase().substr(filename.lastIndexOf("."));
     		if(mime==".xls"){ 
     			parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
     		 $("#questionTypesManage").ajaxSubmit({  
                  url:'${pageContext.request.contextPath}/caseController/upload',  
                  type:"post",  
                  enctype:"multipart/form-data",  
                  contentType: "application/x-www-form-urlencoded; charset=utf-8",  
                  //dataType:"json", 
                  
                  success: function(data){ 
                	  var dataa=eval(data)
                	  parent.$.messager.progress('close');
                	  if(contains(dataa,'false')){
                		  $('#dd').dialog({  
 							 title: '错误信息提醒',  
 							   
 							 width: 350,  
 							   
 							 height: 'auto',  
 							   
 							 closed: false,  
 							   
 							 cache: false,  
 							   
 							 content:dataa,  
 							   
 							 modal: true  
 							 }); 
                	  }else{
                 	  $.messager.show({
							title : '信息提示',
							timeout:20000,
							iconCls: 'icon-info',
							msg : dataa
						}); 
                	  }
                 	parent.$.modalDialog.openner_treeGrid.treegrid('reload');
                 	parent.$.modalDialog.handler.dialog('close');
                  },  
                  error: function() {  
                	  parent.$.messager.progress('close');
                	  parent.$.messager.alert('提示','导入失败，请联系管理员'); 
                  }  
              }) ;  
     		 return false; //不刷新页面  
        }else{
     	   parent.$.messager.alert('提示','请选择excel(.xls)文件上传');    
        }
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

<form id="questionTypesManage"  method="post" enctype="multipart/form-data">  
<input id="uploadExcel" type="file" name="uploadExcel" class="easyui-filebox" style="width:200px" data-options="prompt:'请选择文件...'">  
<span style="float:right;"><a onclick="checkupload()" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'transmit'">确定</a>　</span>  
</form>  
<div id="dd"> </div> 