<%--
 * ==============================================================================================
 * $Id: index_tab.jsp,v 1.1 2014/11/14 03:14:30 asiapacific\hsiehes Exp $
 * ==============================================================================================
--%>
<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/directive.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/jsp/header.jsp"%>	
		<title><fmt:message key="title"/></title>
		<script type="text/javascript" src="<c:url value="/scripts/ui_style.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/scripts/menu.js"/>"></script>
		<script type="text/javascript">
		UIStyle.checkUIStyle('tab');
		</script>
	</head>
	<body class="layoutPage">
		<div widgettype="LayoutContainer" id="layoutContainer" layoutstyle='top-bottom' style="width: 100%; height: 100%;">
			<div widgettype="ContentPane" id="topContentPane" layoutalign="top">
				<h1 class="title" title='<fmt:message key="title"/>'><fmt:message key="title"/></h1>
				<h1 class="titleShadow"><fmt:message key="title"/></h1>
				<div id="userInfo">
					<table width="100%" border="0" cellspacing="0" cellpadding="2">
						<tr>
							<td class="nowrap"><fmt:message key="label.login.user"/></td>
							<td class="nowrap">
								${sessionScope.userProfile.userInfo.signature}
							</td>
						</tr>
						<tr>
							<td class="nowrap"><fmt:message key="label.login.timestamp"/></td>
							<td class="nowrap">				
								<fmt:formatDate value="${sessionScope.userProfile.userInfo.loginTimestamp}" pattern="${dateUtil.fullPattern}"/>
							</td>
							
						</tr>
					</table>
				</div>
			</div>
			<div widgettype="LayoutContainer" id="splitContainerH" layoutalign="bottom" layoutstyle='left-right' toggleposition="left" splitsize="3" activesizing="true">
				<!-- 
				sizershare用來設定ContentPane之寬或高度, 若一個LayoutContainer下的ContentPane均有設定sizeshare時,
				會以比例去計算寬或高, 若只有一個有設定sizeshare時, 則以該值為其寬或高, 然後計算出另一個的寬或高.
				-->
				<div widgettype="ContentPane" id="menuContentPane" sizeshare="170">
					<edstw:menu openFirst="false"/>
				</div>
				<div widgettype="TabContainer" id="mainContentPane">
				</div>
			</div>
		</div>
		<script type="text/javascript">
		var layoutContainer;
		//必須將round動作放在onload的event裡完成, 否則在IE裡會出錯.
		function initialLayout()
		{
			layoutContainer = Widget.parseWidget( $('layoutContainer') );
			if( PreferenceConfig.roundBlocks )
				doRounded()
		}
		function doRounded()
		{
			Rico.Corner.round( $('userInfo'),{color: 'transparent'} );
		}
		EdsEvent.addOnLoad( initialLayout );
		
		function preloadImages()
		{
			BrowserUtils.preloadImages("images/folder_closed.gif", "images/folder_opened.gif", 
				"images/link.gif", "images/tri_down.gif", "images/tri_up.gif", "images/tri_right.gif", 
				"images/collapse_mark.gif", "images/collapse_mark_bk.gif", 
				"images/dialog_close.gif", "images/dialog_close_over.gif", 
				"images/expand_mark.gif", "images/expand_mark_bk.gif", "images/indicator.gif");
        }
		EdsEvent.addOnLoad( preloadImages );
		</script>
		
	</body>
</html>
