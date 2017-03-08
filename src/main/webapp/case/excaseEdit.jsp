<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');
		
		var te=parent.$('#layout_west_tree2').tree('getSelected')
		 if (te.template=='2') {  
				$('#moren').css("display", "none");
	    }else if (te.template=='3'){  
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
			value : '${excase.iconCls}'
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
			    url:'${pageContext.request.contextPath}/excaseController/project',
			    panelHeight:'auto',
			    value : '${excase.project}',
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
		    value : '${excase.op}',
		    editable:true,//不可编辑，只能选择
			});  
		
		var statust = [{ "id": "P", "text": "P" }, { "id": "F", "text": "F" }];
		$('#status').combobox({ 
			data:statust, 
			valueField:'id', 
			textField:'text',
		    panelHeight:'auto',
		    value : '${excase.status}',
		    editable:true,//不可编辑，只能选择
			});  

	

		$('#form').form({
			url : '${pageContext.request.contextPath}/excaseController/edit',
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
					<td><input name="id" type="text" class="span2" value="${excase.id}" readonly="readonly"></td>
					<th>控件名称</th>
					<td><select name="project" id="project" readonly="readonly" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'"></select></td>
				</tr>
					<tr>
					<th>测试模块</th>
					<td><input name="module" id="module" type="text" placeholder="请输入测试模块" readonly="readonly" class="easyui-validatebox span2" value="${excase.module}"></td>
					<th>子模块</th>
					<td><input name="modulechild" id="modulechild" type="text" placeholder="请输入子模块" readonly="readonly" class="easyui-validatebox span2" value="${excase.modulechild}"></td>
				</tr>
				<tr>
					<th>测试项目</th>
					<td><input name="name" id="name" type="text" placeholder="请输入测试项目" readonly="readonly" class="easyui-validatebox span2" value="${excase.name}"></td>
					<th>执行级</th>
					<td><select name="op" id="op" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
					</select></td>
				</tr>
				<tr>
					<th>排序</th>
					<td><input name="seq" value="${excase.seq}" class="easyui-numberspinner" style="width: 140px; height: 29px;" required="required" data-options="editable:true"></td>
					<th>执行人</th>
					<td><input name="tester" id="tester" type="text" placeholder="请输入执行人" class="easyui-validatebox span2" value="${excase.tester}"></td>	
					
					<!-- <th>上级目录</th>
					<td><select id="pid" name="pid" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" style="width: 140px; height: 29px;"></select></td> -->
				</tr>
				
						<tr>
					<th>状态</th>
					<td><select name="status" id="status" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
					</select></td>
					<th>操作时间</th>
					<td colspan="3"><input class="span2" name="exdate" value="${excase.exdate}" placeholder="点击选择时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" /></td>
				</tr>
				<!-- <tr>
					<th>菜单图标</th>
					<td colspan="3"><input id="iconCls" name="iconCls" style="width: 375px; height: 29px;" data-options="editable:false" /></td>
				</tr> -->
				<tr>
					<th>备注</th>
					<td colspan="3"><textarea name="remark" id="remark"  rows="" cols="" class="span5">${excase.remark}</textarea></td>
				</tr>
					<tr id="duomeiti">
					<th>用例编号</th>
					<td><input name="casenum" type="text" readonly="readonly" placeholder="请输入用例编号" class="easyui-validatebox span2" value="${excase.casenum}"></td>
					<th>用例步号</th>
					<td><input name="casestep" type="text" readonly="readonly" placeholder="请输入用例步号" class="easyui-validatebox span2" value="${excase.casestep}"></td>
				</tr>
				<tr id="shuju1">
					<th>一级页面</th>
					<td><input name="fristpage" type="text" readonly="readonly" placeholder="请输入一级页面" class="easyui-validatebox span2" value="${excase.fristpage}"></td>
					<th>二级页面</th>
					<td><input name="secpage" type="text" readonly="readonly" placeholder="请输入二级页面" class="easyui-validatebox span2" value="${excase.secpage}"></td>
				</tr>
				<tr id="shuju2">
					<th>三级页面</th>
					<td><input name="thrpage" type="text" readonly="readonly" placeholder="请输入三级页面" class="easyui-validatebox span2" value="${excase.thrpage}"></td>
					<th>四级页面</th>
					<td><input name="fourpage" type="text" readonly="readonly" placeholder="请输入四级页面" class="easyui-validatebox span2" value="${excase.fourpage}"></td>
				</tr>
				<tr id="shuju3">
					<th>五级页面</th>
					<td><input name="fivepage" type="text" readonly="readonly" placeholder="请输入五级页面" class="easyui-validatebox span2" value="${excase.fivepage}"></td>
					<th>版本</th>
					<td><input name="version" type="text" readonly="readonly" placeholder="请输入版本号" class="easyui-validatebox span2" value="${excase.version}"></td>
				</tr>
				<tr>
					<th>执行步骤</th>
					<td colspan="3"><textarea name="step" rows="" readonly="readonly" cols="" style="width: 367px; height: 70px;" class="span5">${excase.step}</textarea></td>
				</tr>
					<tr>
					<th>预期结果</th>
					<td colspan="3"><textarea name="expire" readonly="readonly" rows="" cols="" style="width: 367px; height: 70px;" class="span5">${excase.expire}</textarea></td>
					<td colspan="3"><input name="pre"  type='hidden' value="${excase.pre}" ></td>
					<td colspan="3"><input name="pid"  type='hidden' value="${excase.pid}" ></td>
					<td colspan="3"><input name="orgid"  type='hidden' value="${excase.orgid}" ></td>
					<td colspan="3"><input name="iscase"  type='hidden' value="${excase.iscase}" ></td>
					<td colspan="3"><input name="treeid"  type='hidden' value="${excase.treeid}" ></td>
				</tr>
				
			</table>
		</form>
	</div>
</div>
