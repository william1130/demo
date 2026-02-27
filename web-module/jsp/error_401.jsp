<%--
 * ---------------------------------------------------------------------------------------------------
 * $Id: error_401.jsp,v 1.1 2014/01/02 02:54:48 asiapacific\hungmike Exp $
 * ---------------------------------------------------------------------------------------------------
--%>
<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/directive.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>Status - 401</title>
		<%@include file="/jsp/header.jsp"%>
	</head>
	<body>
		<p id="errorPageTitle">Not Authorized!</p>
		<br/>
		<div id="errorPageMessage">
			<fieldset>
				<legend>Message</legend>
				<c:if test="${sessionScope.userProfile.userAuthorization.notAuthorized}">
					<fmt:message key="security.proj.notAuthorized"/>
				</c:if>
				<c:if test="${not sessionScope.userProfile.userAuthorization.notAuthorized}">
					<fmt:message key="security.notAuthorized"/>
				</c:if>
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
