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
var copyData=""
function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
            + " " + date.getHours() + seperator2 + date.getMinutes()
            + seperator2 + date.getSeconds();
    return currentdate;
}


$.extend($.fn.datagrid.defaults.editors, {   
	 
    textarea: {   
init: function(container, options){  
	console.log(container)
           var input = $('<textarea class="datagrid-editable-input" cols="220" rows="7"></textarea>').appendTo(container);  
           return input;   
        },   
getValue: function(target){  
	     
           return $(target).val();   
       },   
setValue: function(target, value){   
           $(target).val(value);   
      },   
resize: function(target, width){   

           var input = $(target);   
           if ($.boxModel == true){   
               input.width(width - (input.outerWidth() - input.width()));   
           } else {   
               input.width(width);   
           }   
       }   
   }   
}); 

//业务用例  默认模板用例
var template="{field : 'module',"
	+"title : '测试模块',"
	+"editor:'text',"
	+"width : 170,"
	
	+"formatter : function(value, row, index) {"
	+"var s = row.module;"
	+"if (row.children){"
	+"s += '&nbsp;<span style=\"color:#F02749\">(' + row.children.length + ')</span>';"
	+"}"
	+"return s;"	
	+"}"
	
	+"},{"
	+"field : 'modulechild',"
	+"title : '子模块',"
	+"editor:'text',"
	+"width : 70"
	+"},  {"
	+"field : 'name',"
	+"title : '测试项目',"
	+"editor:'text',"
	+"width : 100"
	+"}, {"
	+"	field : 'op',"
	+"title : '优先级',"
	+"editor:{ type:'combobox',options:{valueField:'id', textField:'text',data:Address,panelHeight:'auto',}},"
	+"width : 40"
	+"}, {"
	+"	field : 'pre',"
	+"	title : '前置条件',"
	+"editor:{ type:'textarea'},"
	+"	width : 120,"
	
	+"formatter : function(value, row, index) {"
	+"if(value!=undefined)"
	+"var s = value.replace(new RegExp(\"\\n\",\"gm\"),\"<br>\");"
	+"return s;"	
	+"}"
	
	
	
	+"}, {"
	+"	field : 'step',"
	+"	title : '操作步骤',"
	+"editor:'textarea',"
	+"	width : 320,"
	
	+"formatter : function(value, row, index) {"
	+"if(value!=undefined)"
	+"var s = value.replace(new RegExp(\"\\n\",\"gm\"),\"<br>\");"
	+"return s;"	
	+"}"
	
	
	+"	}, {"
	+"	field : 'expire',"
	+"	title : '预期结果',"
	+"editor:'textarea',"
	+"	width : 320,"
	
	+"formatter : function(value, row, index) {"
	+"if(value!=undefined)"
	+"var s = value.replace(new RegExp(\"\\n\",\"gm\"),\"<br>\");"
	+"return s;"	
	+"}"
	
	
	+"}, {"
	+"		field : 'remark',"
	+"	title : '备注',"
	+"editor:'textarea',"
	+"	width : 100,"
	+"}, {"
	+"		field : 'seq',"
	+"		title : '排序',"
	+"editor:'text',"
	+"		width : 30"
	+"	}, {"
	+"		field : 'version',"
	+"		title : '版本',"
	+"editor:'text',"
	+"		width : 50"
	+"	}, {"
	+"		field : 'updater',"
	+"		title : '更新人',"
	+"		width : 50"
	+"	}, {"
	+"		field : 'updatetime',"
	+"		title : '更新时间',"
	+"		width : 80"
	+"	}"

	//业务用例  数据采集模板用例
var template1="{field : 'casenum',"
	+"title : '测试编号',"
	+"editor:'text',"
	+"width : 100"
	+"},{"
	+"field : 'casestep',"
	+"title : '测试项目',"
	+"editor:'text',"
	+"width : 100"
	+"},{"
	+"field : 'name',"
	+"title : '测试描述',"
	+"editor:'text',"
	+"width : 100"
	+"}, {"
	+"	field : 'op',"
	+"title : '优先级',"
	+"editor:{ type:'combobox',options:{valueField:'id', textField:'text',data:Address,panelHeight:'auto',}},"
	+"width : 50"
	+"},{"
	+"field : 'fristpage',"
	+"title : '一级页面',"
	+"editor:'text',"
	+"width : 150,"
	
	+"formatter : function(value, row, index) {"
	+"var s = row.fristpage;"
	+"if (row.children){"
	+"s += '&nbsp;<span style=\"color:#F02749\">(' + row.children.length + ')</span>';"
	+"}"
	+"return s;"	
	+"}"
	
	+"},{"
	+"field : 'secpage',"
	+"title : '二级页面',"
	+"editor:'text',"
	+"width : 100"
	+"},{"
	+"field : 'thrpage',"
	+"title : '三级页面',"
	+"editor:'text',"
	+"width : 100"
	+"},{"
	+"field : 'fourpage',"
	+"title : '四级页面',"
	+"editor:'text',"
	+"width : 100"
	+"},{"
	+"field : 'fivepage',"
	+"title : '五级页面',"
	+"editor:'text',"
	+"width : 100"
	+"}, {"
	+"	field : 'pre',"
	+"	title : '前置条件',"
	+"editor:'textarea',"
	+"	width : 120,"
	+"formatter : function(value, row, index) {"
	+"if(value!=undefined)"
	+"var s = value.replace(new RegExp(\"\\n\",\"gm\"),\"<br>\");"
	+"return s;"	
	+"}"
	+"}, {"
	+"	field : 'step',"
	+"	title : '操作步骤',"
	+"editor:'textarea',"
	+"	width : 320,"
	+"formatter : function(value, row, index) {"
	+"if(value!=undefined)"
	+"var s = value.replace(new RegExp(\"\\n\",\"gm\"),\"<br>\");"
	+"return s;"	
	+"}"
	+"	}, {"
	+"	field : 'expire',"
	+"	title : '预期结果',"
	+"editor:'textarea',"
	+"	width : 320,"
	+"formatter : function(value, row, index) {"
	+"if(value!=undefined)"
	+"var s = value.replace(new RegExp(\"\\n\",\"gm\"),\"<br>\");"
	+"return s;"	
	+"}"
	+"}, {"
	+"		field : 'remark',"
	+"	title : '备注',"
	+"editor:'textarea',"
	+"	width : 100,"
	+"}, {"
	+"		field : 'seq',"
	+"		title : '排序',"
	+"editor:'text',"
	+"		width : 30"
	+"	}, {"
	+"		field : 'version',"
	+"		title : '版本',"
	+"editor:'text',"
	+"		width : 40"

	+"	}, {"
	+"		field : 'updater',"
	+"		title : '更新人',"
	+"		width : 50"
	+"	}, {"
	+"		field : 'updatetime',"
	+"		title : '更新时间',"
	+"		width : 80"
	+"	}"

//业务用例  多媒体模板
var template2="{field : 'module',"
	+"title : '测试模块',"
	+"editor:'text',"
	+"width : 170"
	+"},{"
	+"field : 'modulechild',"
	+"title : '子模块',"
	+"editor:'text',"
	+"width : 70"
	+"},{field : 'casenum',"
	+"title : '测试编号',"
	+"editor:'text',"
	+"width : 100"
	+"},  {"
	+"field : 'name',"
	+"title : '用例名称',"
	+"editor:'text',"
	+"width : 100"
	+"}, {"
	+"	field : 'op',"
	+"title : '优先级',"
	+"editor:{ type:'combobox',options:{valueField:'id', textField:'text',data:Address,panelHeight:'auto',}},"
	+"width : 40"
	+"}, {"
	+"	field : 'pre',"
	+"	title : '前置条件',"
	+"editor:'textarea',"
	+"	width : 120,"
	+"formatter : function(value, row, index) {"
	+"if(value!=undefined)"
	+"var s = value.replace(new RegExp(\"\\n\",\"gm\"),\"<br>\");"
	+"return s;"	
	+"}"
	+"},  {"
	+"field : 'casestep',"
	+"title : '测试步号',"
	+"editor:'text',"
	+"width : 60"
	+"}, {"
	+"	field : 'step',"
	+"	title : '操作步骤',"
	+"editor:'textarea',"
	+"	width : 320,"
	+"formatter : function(value, row, index) {"
	+"if(value!=undefined)"
	+"var s = value.replace(new RegExp(\"\\n\",\"gm\"),\"<br>\");"
	+"return s;"	
	+"}"
	+"	}, {"
	+"	field : 'expire',"
	+"	title : '预期结果',"
	+"editor:'textarea',"
	+"	width : 320,"
	+"formatter : function(value, row, index) {"
	+"if(value!=undefined)"
	+"var s = value.replace(new RegExp(\"\\n\",\"gm\"),\"<br>\");"
	+"return s;"	
	+"}"
	+"}, {"
	+"		field : 'remark',"
	+"	title : '备注',"
	+"editor:'textarea',"
	+"	width : 100,"
	+"}, {"
	+"		field : 'seq',"
	+"		title : '排序',"
	+"editor:'text',"
	+"		width : 30"
	+"	}, {"
	+"		field : 'version',"
	+"		title : '版本',"
	+"editor:'text',"
	+"		width : 40"
	+"	}, {"
	+"		field : 'updater',"
	+"		title : '更新人',"
	+"		width : 50"
	+"	}, {"
	+"		field : 'updatetime',"
	+"		title : '更新时间',"
	+"		width : 80"
	+"	}"

	
//C开头的为  执行集用例	
	var ctemplate="{field : 'module',"
		+"title : '测试模块',"
		+"editor:'text',"
		+"width : 170"
		+"},{"
		+"field : 'modulechild',"
		+"title : '子模块',"
		+"editor:'text',"
		+"width : 70"
		+"},  {"
		+"field : 'name',"
		+"title : '测试项目',"
		+"editor:'text',"
		+"width : 100"
		+"}, {"
		+"	field : 'op',"
		+"title : '优先级',"
		+"width : 40"
		+"}, {"
		+"	field : 'pre',"
		+"	title : '前置条件',"
		+"editor:'text',"
		+"	width : 120,"
		+"}, {"
		+"	field : 'step',"
		+"	title : '操作步骤',"
		+"editor:'text',"
		+"	width : 320"
		+"	}, {"
		+"	field : 'expire',"
		+"	title : '预期结果',"
		+"editor:'text',"
		+"	width : 320"
		+"}, {"
		+"		field : 'remark',"
		+"	title : '备注',"
		+"editor:'text',"
		+"	width : 100,"
		+"}, {"
		+"		field : 'seq',"
		+"		title : '排序',"
		+"editor:'text',"
		+"		width : 30"
		+"	}, {"
		+"		field : 'version',"
		+"		title : '版本',"
		+"editor:'text',"
		+"		width : 50"
		+"	}, {"
		+"		field : 'updater',"
		+"		title : '更新人',"
		+"		width : 50"
		+"	}, {"
		+"		field : 'updatetime',"
		+"		title : '更新时间',"
		+"		width : 80"
		+"	}"

	var ctemplate1="{field : 'casenum',"
		+"title : '测试编号',"
		+"editor:'text',"
		+"width : 100"
		+"},{"
	+"field : 'casestep',"
	+"title : '测试项目',"
	+"editor:'text',"
	+"width : 100"
	+"},{"
	+"field : 'name',"
	+"title : '测试描述',"
	+"editor:'text',"
	+"width : 100"
	+"}, {"
		+"	field : 'op',"
		+"title : '优先级',"
		+"width : 40"
		+"},{"
		+"field : 'fristpage',"
		+"title : '一级页面',"
		+"editor:'text',"
		+"width : 100"
		+"},{"
		+"field : 'secpage',"
		+"title : '二级页面',"
		+"editor:'text',"
		+"width : 100"
		+"},{"
		+"field : 'thrpage',"
		+"title : '三级页面',"
		+"editor:'text',"
		+"width : 100"
		+"},{"
		+"field : 'fourpage',"
		+"title : '四级页面',"
		+"editor:'text',"
		+"width : 100"
		+"},{"
		+"field : 'fivepage',"
		+"title : '五级页面',"
		+"editor:'text',"
		+"width : 100"
		+"}, {"
		+"	field : 'op',"
		+"title : '优先级',"
		+"width : 40"
		+"}, {"
		+"	field : 'pre',"
		+"	title : '前置条件',"
		+"editor:'text',"
		+"	width : 120,"
		+"}, {"
		+"	field : 'step',"
		+"	title : '操作步骤',"
		+"editor:'text',"
		+"	width : 320"
		+"	}, {"
		+"	field : 'expire',"
		+"	title : '预期结果',"
		+"editor:'text',"
		+"	width : 320"
		+"}, {"
		+"		field : 'remark',"
		+"	title : '备注',"
		+"editor:'text',"
		+"	width : 100,"
		+"}, {"
		+"		field : 'seq',"
		+"		title : '排序',"
		+"editor:'text',"
		+"		width : 30"
		+"	}, {"
		+"		field : 'version',"
		+"		title : '版本',"
		+"editor:'text',"
		+"		width : 50"

		+"	}, {"
		+"		field : 'updater',"
		+"		title : '更新人',"
		+"		width : 50"
		+"	}, {"
		+"		field : 'updatetime',"
		+"		title : '更新时间',"
		+"		width : 80"
		+"	}"


	var ctemplate2="{field : 'module',"
		+"title : '测试模块',"
		+"editor:'text',"
		+"width : 170"
		+"},{"
		+"field : 'modulechild',"
		+"title : '子模块',"
		+"editor:'text',"
		+"width : 70"
		+"},{field : 'casenum',"
		+"title : '测试编号',"
		+"editor:'text',"
		+"width : 100"
		+"},  {"
		+"field : 'name',"
		+"title : '用例名称',"
		+"editor:'text',"
		+"width : 100"
		+"}, {"
		+"	field : 'op',"
		+"title : '优先级',"
		+"width : 40"
		+"}, {"
		+"	field : 'pre',"
		+"	title : '前置条件',"
		+"editor:'text',"
		+"	width : 120,"
		+"},  {"
		+"field : 'casestep',"
		+"title : '测试步号',"
		+"editor:'text',"
		+"width : 50"
		+"}, {"
		+"	field : 'step',"
		+"	title : '操作步骤',"
		+"editor:'text',"
		+"	width : 320"
		+"	}, {"
		+"	field : 'expire',"
		+"	title : '预期结果',"
		+"editor:'text',"
		+"	width : 320"
		+"}, {"
		+"		field : 'remark',"
		+"	title : '备注',"
		+"editor:'text',"
		+"	width : 100,"
		+"}, {"
		+"		field : 'seq',"
		+"		title : '排序',"
		+"editor:'text',"
		+"		width : 30"
		+"	}, {"
		+"		field : 'version',"
		+"		title : '版本',"
		+"editor:'text',"
		+"		width : 50"
		+"	}, {"
		+"		field : 'updater',"
		+"		title : '更新人',"
		+"		width : 50"
		+"	}, {"
		+"		field : 'updatetime',"
		+"		title : '更新时间',"
		+"		width : 80"
		+"	}"

		var etemplate="{field : 'module',"
			+"title : '测试模块',"
			+"editor:'text',"
			+"width : 170"
			+"},{"
			+"field : 'modulechild',"
			+"title : '子模块',"
			+"editor:'text',"
			+"width : 70"
			+"},  {"
			+"field : 'name',"
			+"title : '测试项目',"
			+"editor:'text',"
			+"width : 100"
			+"}, {"
			+"	field : 'op',"
			+"title : '优先级',"
			+"width : 40"
			+"}, {"
			+"	field : 'pre',"
			+"	title : '前置条件',"
			+"editor:'text',"
			+"	width : 120,"
			+"}, {"
			+"	field : 'step',"
			+"	title : '操作步骤',"
			+"editor:'text',"
			+"	width : 320"
			+"	}, {"
			+"	field : 'expire',"
			+"	title : '预期结果',"
			+"editor:'text',"
			+"	width : 320"
			+"}, {"
			+"field : 'tester',"
			+"title : '执行人',"
			+"width : 60"
			+"}, {"
			+"field : 'exdate',"
			+"title : '执行日期',"
			+"width : 100"
			+"}, {"
			+"field : 'status',"
			+"title : '状态',"
			+"width : 40,"
			+"  formatter : function(value, row, index) {"
			+"	var str = '';			"
			+"	if (row.status=='P'&&row.iscase=='1') {"
			+"	str = '<font color=\"green\">'+row.status+'</font>'"
			+"	}else if(row.status=='F'&&row.iscase=='1'){"
			+"	str ='<font color=\"red\">'+row.status+'</font>'"
			+"	}else{"
			+"	if(row.iscase=='1')"
			+"	str='未执行'"
			+"	}"
			+"	return str;"
			+"} "
			+"}, {"
			+"		field : 'seq',"
			+"		title : '排序',"
			+"editor:'text',"
			+"		width : 30"
			+"	}, {"
			+"		field : 'version',"
			+"		title : '版本',"
			+"editor:'text',"
			+"		width : 50"
			+"	}"

		var etemplate1="{field : 'casenum',"
		+"title : '测试编号',"
		+"editor:'text',"
		+"width : 100"
		+"},{"
	+"field : 'casestep',"
	+"title : '测试项目',"
	+"editor:'text',"
	+"width : 100"
	+"},{"
	+"field : 'name',"
	+"title : '测试描述',"
	+"editor:'text',"
	+"width : 100"

			+"},{"
		+"field : 'fristpage',"
		+"title : '一级页面',"
		+"editor:'text',"
		+"width : 100"
		+"},{"
		+"field : 'secpage',"
		+"title : '二级页面',"
		+"editor:'text',"
		+"width : 100"
		+"},{"
		+"field : 'thrpage',"
		+"title : '三级页面',"
		+"editor:'text',"
		+"width : 100"
		+"},{"
		+"field : 'fourpage',"
		+"title : '四级页面',"
		+"editor:'text',"
		+"width : 100"
		+"},{"
		+"field : 'fivepage',"
		+"title : '五级页面',"
		+"editor:'text',"
		+"width : 100"
		+"}, {"
			+"	field : 'op',"
			+"title : '优先级',"
			+"width : 40"
			+"}, {"
			+"	field : 'pre',"
			+"	title : '前置条件',"
			+"editor:'text',"
			+"	width : 120,"
			+"}, {"
			+"	field : 'step',"
			+"	title : '操作步骤',"
			+"editor:'text',"
			+"	width : 320"
			+"	}, {"
			+"	field : 'expire',"
			+"	title : '预期结果',"
			+"editor:'text',"
			+"	width : 320"
			+"}, {"
			+"field : 'tester',"
			+"title : '执行人',"
			+"width : 60"
			+"}, {"
			+"field : 'exdate',"
			+"title : '执行日期',"
			+"width : 100"
			+"}, {"
			+"field : 'status',"
			+"title : '状态',"
			+"width : 40,"
			+"  formatter : function(value, row, index) {"
			+"	var str = '';			"
			+"	if (row.status=='P'&&row.iscase=='1') {"
			+"	str = '<font color=\"green\">'+row.status+'</font>'"
			+"	}else if(row.status=='F'&&row.iscase=='1'){"
			+"	str ='<font color=\"red\">'+row.status+'</font>'"
			+"	}else{"
			+"	if(row.iscase=='1')"
			+"	str='未执行'"
			+"	}"
			+"	return str;"
			+"} "
			+"}, {"
			+"		field : 'seq',"
			+"		title : '排序',"
			+"editor:'text',"
			+"		width : 30"
			+"	}, {"
			+"		field : 'version',"
			+"		title : '版本',"
			+"editor:'text',"
			+"		width : 50"
			+"	}"


		var etemplate2="{field : 'module',"
			+"title : '测试模块',"
			+"editor:'text',"
			+"width : 170"
			+"},{"
			+"field : 'modulechild',"
			+"title : '子模块',"
			+"editor:'text',"
			+"width : 70"
			+"}  ,{field : 'casenum',"
		+"title : '测试编号',"
		+"editor:'text',"
		+"width : 100"
		+"},  {"
		+"field : 'name',"
		+"title : '用例名称',"
		+"editor:'text',"
		+"width : 100"
		+"}, {"
			+"	field : 'op',"
			+"title : '优先级',"
			+"width : 40"
			+"}, {"
			+"	field : 'pre',"
			+"	title : '前置条件',"
			+"editor:'text',"
			+"	width : 120,"
			+"},  {"
		+"field : 'casestep',"
		+"title : '测试步号',"
		+"editor:'text',"
		+"width : 50"
		+"}, {"
			+"	field : 'step',"
			+"	title : '操作步骤',"
			+"editor:'text',"
			+"	width : 320"
			+"	}, {"
			+"	field : 'expire',"
			+"	title : '预期结果',"
			+"editor:'text',"
			+"	width : 320"
			+"}, {"
			+"field : 'tester',"
			+"title : '执行人',"
			+"width : 60"
			+"}, {"
			+"field : 'exdate',"
			+"title : '执行日期',"
			+"width : 100"
			+"}, {"
			+"field : 'status',"
			+"title : '状态',"
			+"width : 40,"
			+"  formatter : function(value, row, index) {"
			+"	var str = '';			"
			+"	if (row.status=='P'&&row.iscase=='1') {"
			+"	str = '<font color=\"green\">'+row.status+'</font>'"
			+"	}else if(row.status=='F'&&row.iscase=='1'){"
			+"	str ='<font color=\"red\">'+row.status+'</font>'"
			+"	}else{"
			+"	if(row.iscase=='1')"
			+"	str='未执行'"
			+"	}"
			+"	return str;"
			+"} "
			+"}, {"
			+"		field : 'seq',"
			+"		title : '排序',"
			+"editor:'text',"
			+"		width : 30"
			+"	}, {"
			+"		field : 'version',"
			+"		title : '版本',"
			+"editor:'text',"
			+"		width : 50"
			+"	}"