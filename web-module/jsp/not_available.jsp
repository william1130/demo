<%--
 * ---------------------------------------------------------------------------------------------------
 * $Id: not_available.jsp,v 1.1 2014/01/02 02:54:49 asiapacific\hungmike Exp $
 * ---------------------------------------------------------------------------------------------------
--%>
<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/directive.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
	<title>系統暫停服務</title></head>
	<%@include file="/jsp/header.jsp"%>
	<body>
		<p id="errorPageTitle">系統暫停服務</p>
		<br>
		<div id="errorPageMessage">
			<fieldset>
				<legend>錯誤訊息</legend>
				系統啟動過程中發生以下, 暫時無法提供服務, 請洽系統管理人員檢查.<br/>
				錯誤類別 : <c:out value="${systemFailCause.class.name}"/><br/>
				錯誤訊息 : <c:out value="${systemFailCause.message}"/>
			</fieldset>
			<fieldset class="stackTrace">
				<legend>詳細訊息</legend>
				<c:forEach items="${systemFailCause.stackTrace}" var="trace">
					<c:out value='${trace.className}.${trace.methodName}():${trace.lineNumber}'/><br>
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
