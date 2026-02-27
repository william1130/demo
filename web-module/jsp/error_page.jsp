<%--
 * ---------------------------------------------------------------------------------------------------
 * $Id: error_page.jsp,v 1.1 2014/01/02 02:54:48 asiapacific\hungmike Exp $
 * ---------------------------------------------------------------------------------------------------
--%>
<%@page pageEncoding="UTF-8" isELIgnored="false" isErrorPage="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>系統-錯誤訊息</title>
	<%@include file="/jsp/header.jsp"%>
</head>
<body>
	<div id="errorPageTitle" title="程式代碼 : ${currentFunctionObj.id}">${currentProgramObj.name}</div>
	<br/>
	<div id="errorPageMessage">
		<fieldset>
			<legend>Message</legend>
			<c:set var="exception" value='${pageContext.exception}'/>
			${exception.message}
		</fieldset>
		<fieldset class="stackTrace">
			<legend>Stack Trace</legend>
			<c:forEach items="${exception.stackTrace}" var="e">
				${e.className}.${e.methodName}():${e.lineNumber}<br/>
			</c:forEach>
		</fieldset>
	</div>
	<script type="text/javascript">
	<!--//
	function doEffect()
	{
		Rico.Corner.round( $('errorPageTitle'),{color: 'transparent'} );
		Rico.Corner.round( $('errorPageMessage'),{color: 'transparent'} );
	}
	EdsEvent.addOnLoad( doEffect );
	//-->
	</script>
</body>
</html>
