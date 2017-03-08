<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');
		var te=parent.$('#layout_west_tree2').tree('getSelected')
		 if (te.template=='2') {  
				$('#moren').css("display", "none");
				$('#d').css("display", "none");
	    }else if (te.template=='3'){  
	    	$('#s').css("display", "none");
	    	$('#shuju1').css("display", "none");
			$('#shuju2').css("display", "none");
	  } else{
			$('#duomeiti').css("display", "none");
			$('#shuju1').css("display", "none");
			$('#shuju2').css("display", "none");
	  } 
		$('#iconCls').combobox({
			data : $.iconData,
			formatter : function(v) {
				return $.formatString('<span class="{0}" style="display:inline-block;vertical-align:middle;width:16px;height:16px;"></span>{1}', v.value, v.value);
			},
			value : '${buscase.iconCls}'
		});
	 
/* 		$('#pid').combobox({
			valueField:'id', //值字段
			    textField:'name', //显示的字段
			    url:'${pageContext.request.contextPath}/buscaseController/projectchild?child='+'${buscase.project}',
			    panelHeight:'auto',
				editable:true,
				value : '${buscase.pid}'//不可编辑，只能选择						
			}); */
		
		$('#project').combobox({
			valueField:'project', //值字段
			    textField:'project', //显示的字段
			    url:'${pageContext.request.contextPath}/buscaseController/project',
			    panelHeight:500,
			    value : '${buscase.project}',
				editable:true,//不可编辑，只能选择
				onSelect:function(record){  
					/* var value = $('#project').combobox('getValue');
					$('#pid').combobox({
						valueField:'id', //值字段
						    textField:'name', //显示的字段
						    url:'${pageContext.request.contextPath}/buscaseController/projectchild?child='+value,
						    panelHeight:'auto',
							editable:true//不可编辑，只能选择						
						}); */
			     }  
			});
		
		var Address = [{ "id": "P1", "text": "P1" }, { "id": "P2", "text": "P2" }, { "id": "P3", "text": "P3" }, { "id": "P4", "text": "P4" }, { "id": "P5", "text": "P5" }];
		$('#op').combobox({ 
			data:Address, 
			valueField:'id', 
			textField:'text',
		    panelHeight:'auto',
		    value : '${buscase.op}',
		    editable:true,//不可编辑，只能选择
			});  

	

		$('#form').form({
			url : '${pageContext.request.contextPath}/buscaseController/edit',
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
				
					parent.$.modalDialog.openner_treeGrid.treegrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_treeGrid这个对象，是因为resource.jsp页面预定义好了
				
					parent.$.modalDialog.handler.dialog('close');
					
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
					<td><input name="id" type="text" class="span2" value="${buscase.id}" readonly="readonly"></td>
					<th>控件名称</th>
					<td><select name="project" id="project" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'"></select></td>
				</tr>
				<tr id="moren">
					<th>测试模块</th>
					<td><input name="module" id="module" type="text" placeholder="请输入测试模块" class="easyui-validatebox span2" value="${buscase.module}"></td>
					<th>子模块</th>
					<td><input name="modulechild" id="modulechild" type="text" placeholder="请输入子模块" class="easyui-validatebox span2" value="${buscase.modulechild}"></td>
				</tr>
					<tr id="duomeiti">
					<th>用例编号</th>
					<td><input name="casenum" type="text" placeholder="请输入用例编号" class="easyui-validatebox span2" value="${buscase.casenum}"></td>
					<th id="d">测试步号</th>
					<th id="s">测试项目</th>
					<td><input name="casestep" type="text" placeholder="请输入用例步号" class="easyui-validatebox span2" value="${buscase.casestep}"></td>
				</tr>
				<tr id="shuju1">
					<th>一级页面</th>
					<td><input name="fristpage" type="text" placeholder="请输入一级页面" class="easyui-validatebox span2" value="${buscase.fristpage}"></td>
					<th>二级页面</th>
					<td><input name="secpage" type="text" placeholder="请输入二级页面" class="easyui-validatebox span2" value="${buscase.secpage}"></td>
				</tr>
				<tr id="shuju2">
					<th>三级页面</th>
					<td><input name="thrpage" type="text" placeholder="请输入三级页面" class="easyui-validatebox span2" value="${buscase.thrpage}"></td>
					<th>四级页面</th>
					<td><input name="fourpage" type="text" placeholder="请输入四级页面" class="easyui-validatebox span2" value="${buscase.fourpage}"></td>
				</tr>
				<tr id="shuju3">
					<th>五级页面</th>
					<td><input name="fivepage" type="text" placeholder="请输入五级页面" class="easyui-validatebox span2" value="${buscase.fivepage}"></td>
					<th>版本</th>
					<td><input name="version" type="text" placeholder="请输入版本号" class="easyui-validatebox span2" value="${buscase.version}"></td>
				</tr>
					
				<tr>
					<th>测试描述</th>
					<td><input name="name" id="name" type="text" placeholder="请输入测试项目" class="easyui-validatebox span2" value="${buscase.name}"></td>
					<th>执行级</th>
					<td><select name="op" id="op" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
					</select></td>
				</tr>
				<tr>
					<th>排序</th>
					<td><input name="seq" value="${buscase.seq}" class="easyui-numberspinner" style="width: 140px; height: 29px;" required="required" data-options="editable:true"></td>
					<th>备注</th>
					<td><input name="remark" id="remark" type="text" placeholder="请输入备注" class="easyui-validatebox span2" value="${buscase.remark}"></td>	
					<!-- <th>上级目录</th>
					<td><select id="pid" name="pid" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" style="width: 140px; height: 29px;"></select></td> -->
				</tr>
				<!-- <tr>
					<th>菜单图标</th>
					<td colspan="3"><input id="iconCls" name="iconCls" style="width: 375px; height: 29px;" data-options="editable:false" /></td>
				</tr> -->
				<tr>
					<th>前置条件</th>
					<td colspan="3"><textarea name="pre" rows="" cols="" class="span5">${buscase.pre}</textarea></td>
				</tr>
				<tr>
					<th>执行步骤</th>
					<td colspan="3"><textarea name="step" rows="" cols="" style="width: 367px; height: 70px;" class="span5">${buscase.step}</textarea></td>
				</tr>
					<tr>
					<th>预期结果</th>
					<td colspan="3"><textarea name="expire" rows="" cols="" style="width: 367px; height: 70px;" class="span5">${buscase.expire}</textarea></td>
					<td colspan="3"><input name="pid"  type='hidden' value="${buscase.pid}" ></td>
					<td colspan="3"><input name="treeid"  type='hidden' value="${buscase.treeid}" ></td>
					<!-- <td colspan="3"><input id="iconCls" type='hidden' name="iconCls" style="width: 375px; height: 29px;" data-options="editable:false" /></td> -->
				</tr>
				
			</table>
		</form>
	</div>
</div>
