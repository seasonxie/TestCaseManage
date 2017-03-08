<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript">
	
	$(function() {

		parent.$.messager.progress('close');
		var checkbox= $("input:checkbox")
		var checkboxs=new Array()
		checkboxs[0]='${TextBox.blank_q}'
		checkboxs[1]='${TextBox.blank_z}'
		checkboxs[2]='${TextBox.blank_h}'
		checkboxs[3]='${TextBox.letter_d}'
		checkboxs[4]='${TextBox.letter_x}'
		checkboxs[5]='${TextBox.letter_m}'
		checkboxs[6]='${TextBox.jh}'
		checkboxs[7]='${TextBox.sign}'
		checkboxs[8]='${TextBox.number_i}'
		checkboxs[9]='${TextBox.number_d}'
		checkboxs[10]='${TextBox.htmlencode}'
		checkboxs[11]='${TextBox.urlencode}'
		checkboxs[12]='${TextBox.mix}'
		
		for(var i=0;i<checkbox.length;i++){
			if(checkboxs[i]=='on')
			checkbox[i].checked=true
		}
		
		
		datatype = [{ "id": "文本输入框", "text": "文本输入框" }, { "id": "数字输入框", "text": "数字输入框" }, { "id": "HTTP校验", "text": "HTTP校验" }, { "id": "邮件校验", "text": "邮件校验" }];
		$('#type').combobox({ 
			data:datatype, 
			valueField:'id', 
			textField:'text',
			value:'${TextBox.type}',
		    panelHeight:'auto',
		    editable:true,//不可编辑，只能选择
		    required:true
			});  
		$('#form').form({
			url : '${pageContext.request.contextPath}/dataController/edit',
			onSubmit : function() {
				parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				var isValid = $(this).form('validate');
				if (!isValid) {
					parent.$.messager.progress('close');
				}
				return isValid;
			},
			success : function(result) {
				parent.$.messager.progress('close');
				result = $.parseJSON(result);
				if (result.success) {
					parent.$.modalDialog.openner_dataGrid.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
					parent.$.modalDialog.handler.dialog('close');
				} else {
					parent.$.messager.alert('错误', result.msg, 'error');
				}
			}
		});
	});

</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
		<form id="form" method="post">
			<table class="table table-hover table-condensed" width="60%" cellspacing="0" cellpadding="0" align="left" >  
   
     <tr>     
     <th>数据名字</th>
					<td><input name="name" id="name" type="text" placeholder="请输入名字"  data-options="required:true" class="easyui-validatebox span2" style="width:100px;" value="${TextBox.name}"></td>   
           
                 	<th>类型</th>
						<td><select name="type" id="type" class="easyui-combobox" style="width:100px;" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
					</select></td>
        </tr>  
     <tr>        
            <th>边界值</th>  
            <td><input type="text" name="boundary_down"  data-options="required:true" style="width:60px;" value="${TextBox.boundary_down}" class="easyui-validatebox span2"/></td>  
            <th>到</th>  
            <td><input type="text" name="boundary_up"   data-options="required:true" style="width:60px;" value="${TextBox.boundary_up}" class="easyui-validatebox span2"/></td>  
        </tr>  
        <tr>  
            <th>前置空格:</th>  
            <td><input type="checkbox" name="blank_q"/></td>  
        
            <th>中置空格:</th>  
             <td><input type="checkbox" name="blank_z"/></td>        
        </tr>  
          <tr>  
           <th>后置空格:</th>  
             <td><input type="checkbox" name="blank_h"/></td>        
             <th>大写字符:</th>  
             <td><input type="checkbox" name="letter_d"/></td>    
           
        </tr> 
          <tr>  
           <th>小写字符:</th>  
             <td><input type="checkbox" name="letter_x"/></td>        
             <th>大小混合:</th>  
             <td><input type="checkbox" name="letter_m"/></td>    
           
        </tr> 
         <tr>  
           <th>HTMLJS:</th>  
             <td><input type="checkbox" name="jh"/></td>        
             <th>特殊字符:</th>  
             <td><input type="checkbox" name="sign"/></td>    
           
        </tr> 
          <tr>  
           <th>整数:</th>  
             <td><input type="checkbox" name="number_i"/></td>        
             <th>小数:</th>  
             <td><input type="checkbox" name="number_d"/></td>    
           
        </tr> 
        
          <tr>  
           <th>HTMLENCODE:</th>  
             <td><input type="checkbox" name="htmlencode"/></td>        
             <th>URLENCODE:</th>  
             <td><input type="checkbox" name="urlencode"/></td>    
           
        </tr> 
        
          <tr>  
           <th>混合型:</th>  
             <td colspan="3"><input type="checkbox" name="mix"/></td>         
           <td><input name="id" type='hidden' value="${TextBox.id}"></td>
        </tr> 
        
        
       
    
</table>  
		</form>
	</div>
</div>