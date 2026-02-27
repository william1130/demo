<%@page import="proj.nccc.logsearch.ProjConstants"%>
<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/directive.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>NCCC Log查詢系統</title>
		<meta http-equiv="Content-Type" content="text/html; charset=big5">
		<meta http-equiv="Context-Script-Type" content="text/javascript">
		<link href='css/common.css' rel="stylesheet" type="text/css">
		<link href='css/widget.css' rel="stylesheet" type="text/css">
		<link href='css/pane.css' rel="stylesheet" type="text/css">
		<link href='css/folding.css' rel='stylesheet' type='text/css'>
		<link href='css/project.css' rel='stylesheet' type='text/css'>
		<script type="text/javascript" src="scripts/firebug/firebug.js"></script>
		<script type="text/javascript" src="scripts/prototype.js"></script>
		<script type="text/javascript" src="scripts/common.js"></script>
		<script type="text/javascript" src="scripts/widget.js"></script>
		<script type="text/javascript" src="scripts/project.js"></script>
		<script type="text/javascript" src="scripts/cookie.js"></script>
		<script type="text/javascript" src="scripts/ajax_function.js"></script>
		<script type="text/javascript" src="scripts/effect.js"></script>
		<script type="text/javascript" src="scripts/folding.js"></script>
		<script type="text/javascript" src="scripts/validate.js"></script>
		<script type="text/javascript" src="scripts/rico/rico.js"></script>
		<script type="text/javascript" src="scripts/rico/ricoStyles.js"></script>
		<script type="text/javascript" src="scripts/scriptaculous/scriptaculous.js"></script>
		<script type="text/javascript" src="scripts/scriptaculous/effects.js"></script>
		
		<script type="text/javascript" src="scripts/ui_style.js"></script>
		<script type="text/javascript" src="scripts/menu.js"></script>
		<script type="text/javascript">
		//當session timeout或使用者登出再登入, 要判斷首頁視窗是否被包含在頁框裡, 避免畫面顯示不正確.
		if( window.top != self )
			window.top.location.href = self.location.href;
		UIStyle.checkUIStyle('tab');
		</script>
	</head>
	<body class="layoutPage">
		<div widgettype="LayoutContainer" id="layoutContainer" layoutstyle='top-bottom' style="width: 100%; height: 100%;">
			<div widgettype="ContentPane" id="topContentPane" layoutalign="top">
				<h1 class="title" title='NCCC Log查詢系統'>NCCC Log查詢系統</h1>
				<h1 class="titleShadow">NCCC Log查詢系統</h1>
				<div id="userInfo">
					<table width="100%" border="0" cellspacing="0" cellpadding="2">
						<tr>
							<td class="nowrap">登入人員：</td>
							<td class="nowrap">[test]Test</td>
						</tr>
						<tr>
							<td class="nowrap">登入時間：</td>
							<td class="nowrap">20130927 14:37:49</td>
						</tr>
					</table>
				</div>
			</div>
			<div widgettype="LayoutContainer" id="splitContainerH" layoutalign="bottom" layoutstyle='left-right' toggleposition="left" splitsize="3" activesizing="true">
				<!-- 
				sizershare用來設定ContentPane之寬或高度, 若一個LayoutContainer下的ContentPane均有設定sizeshare時,
				會以比例去計算寬或高, 若只有一個有設定sizeshare時, 則以該值為其寬或高, 然後計算出另一個的寬或高.
				-->
				<div widgettype="ContentPane" id="menuContentPane" sizeshare="2">
					<script type="text/javascript" src="prototype_menu.js"></script>
				</div>
				<div widgettype="TabContainer" id="mainContentPane" sizeshare="8">
					<!--
					<div id="test2" widgettype="ContentPane" style="width: 100%; height: 100%;" closable="true">
						<iframe src="ui_config.html" frameborder="0" style="width: 100%; height: 100%;"></iframe>
					</div>
					<div id="test1" widgettype="ContentPane" style="width: 100%; height: 100%;" closable="true">
						<iframe src="templates/tab_1.html" frameborder="0" style="width: 100%; height: 100%;"></iframe>
					</div>
					<div id="test3" widgettype="ContentPane" style="width: 100%; height: 100%;" selected="true" closable="true">
						<iframe src="templates/tab_2.html" frameborder="0" style="width: 100%; height: 100%;"></iframe>
					</div>
					-->
				</div>
			</div>
		</div>
		<script type="text/javascript">
		var layoutContainer;
		//必須將round動作放在onload的event裡完成, 否則在IE裡會出錯.
		function initialLayout()
		{
			layoutContainer = Widget.parseWidget( $('layoutContainer') );
		}
		function doRounded()
		{
			Rico.Corner.round( $('userInfo'),{color: 'transparent'} );
		}
		EdsEvent.addOnLoad( initialLayout );
		if( PreferenceConfig.roundBlocks )
		{
			EdsEvent.addOnLoad( doRounded );
		}
		</script>
	</body>
</html>
