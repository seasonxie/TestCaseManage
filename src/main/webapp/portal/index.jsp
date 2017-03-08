<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript" charset="utf-8">
	var portalLayout;
	var portal;
	$(function() {
		portalLayout = $('#portalLayout').layout({
			fit : true
		});
		$(window).resize(function() {
			portalLayout.layout('panel', 'center').panel('resize', {
				width : 1,
				height : 1
			});
		});

		panels = [  {
			id : 'p4',
			title : '报表分析',
			height : 400,
			collapsible : true,
			href : '${pageContext.request.contextPath}/baseController/portal/seq'
		}, {
			id : 'p2',
			title : 'Redmine做交互',
			height : 350,
			collapsible : true,
			href : '${pageContext.request.contextPath}/baseController/portal/link'
		} /*  , {
			id : 'p3',
			title : '修复数据库',
			height : 300,
			collapsible : true,
			href : '${pageContext.request.contextPath}/baseController/portal/repair'
		} */, {
			id : 'p1',
			title : '更新日志',
			height : 400,
			collapsible : true,
			href : '${pageContext.request.contextPath}/baseController/portal/about2'
		}, {
			id : 'p5',
			title : '用例分层',
			height : 350,
			collapsible : true,
			href : '${pageContext.request.contextPath}/baseController/portal/about'
		} /* , {
			id : 'p6',
			title : 'EasyUI的QQ群',
			height : 300,
			collapsible : true,
			href : '${pageContext.request.contextPath}/baseController/portal/qun'
		} */ ];

		portal = $('#portal').portal({
			border : false,
			fit : true,
			onStateChange : function() {
				$.cookie('portal-state', getPortalState(), {
					expires : 7
				});
			}
		});
		var state = $.cookie('portal-state');
		//if (!state) {
			state = 'p1,p2,p3:p4,p5,p6';/*冒号代表列，逗号代表行*/
		//}
		addPortalPanels(state);
		portal.portal('resize');

	});

	function getPanelOptions(id) {
		for ( var i = 0; i < panels.length; i++) {
			if (panels[i].id == id) {
				return panels[i];
			}
		}
		return undefined;
	}
	function getPortalState() {
		var aa = [];
		for ( var columnIndex = 0; columnIndex < 2; columnIndex++) {
			var cc = [];
			var panels = portal.portal('getPanels', columnIndex);
			for ( var i = 0; i < panels.length; i++) {
				cc.push(panels[i].attr('id'));
			}
			aa.push(cc.join(','));
		}
		return aa.join(':');
	}
	function addPortalPanels(portalState) {
		var columns = portalState.split(':');
		console.log(columns.length)
		
		for ( var columnIndex = 0; columnIndex < columns.length; columnIndex++) {
			var cc = columns[columnIndex].split(',');
			console.log(columns[columnIndex])
			console.log(cc.length)
			for ( var j = 0; j < cc.length; j++) {
				var options = getPanelOptions(cc[j]);
				if (options) {
					var p = $('<div/>').attr('id', options.id).appendTo('body');
					p.panel(options);
					portal.portal('add', {
						panel : p,
						columnIndex : columnIndex
					});
				}
			}
		}
	}
</script>
</head>
<body>
	<div id="portalLayout">
		<div data-options="region:'center',border:false">
			<div id="portal" style="position: relative">
				<div></div>
				<div></div>
			</div>
		</div>
	</div>
</body>
</html>