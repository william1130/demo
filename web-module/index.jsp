<%--
 * ==============================================================================================
 * $Id: index.jsp,v 1.1 2014/11/14 03:14:30 asiapacific\hsiehes Exp $
 * ==============================================================================================
--%>
<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/directive.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html >
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta http-equiv="Context-Script-Type" content="text/javascript"/>
	<meta http-equiv="pragma" content="no-cache"/>
	<title><fmt:message key="title"/></title>
	<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/scripts/common.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/scripts/project.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/scripts/menu.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/scripts/ui_style.js'/>"></script>
	<script type="text/javascript">
	//設定最上層window的name, 以便回報系統廠商在登出後, 登入畫面能正確出現在最上層, 但又不會破壞NCCC的portal畫面.
	window.name = '<fmt:message key="title"/>';
	UIStyle.getUIStyle().linkToHomePage();
	</script>
</head>
<body>
</body>
</html>
