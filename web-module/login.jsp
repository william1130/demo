<%--
 * ==============================================================================================
 * $Id: login.jsp,v 1.1 2014/11/14 03:14:31 asiapacific\hsiehes Exp $
 * ==============================================================================================
--%>
<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/directive.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html >
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<%@include file="/jsp/header.jsp"%>
		<title><fmt:message key="title"/></title>
		<script type="text/javascript">
			if( window.top != window.self )
				window.top.location.href = '<c:url value="/login.jsp"/>'
		</script>
	</head>
	<body>
		<div class="mainContentBlock">
			<div id="progHeader">
				<div id="progTitle">登入</div>
			</div>
			<c:import url="/jsp/messages.jsp"/>
			<div id="inputBlock" class="inputBlock" style="width: 500px; margin-top: 30px;">
				<s:form action="login" focus="entity.userNo">
					<c:set var="entity" value="${UserForm.entity}"/>
					<s:hidden name="entity.pagination.enablePaging" value="true"/>
					<fieldset>
						<legend>登入</legend>
						<table border="0" cellpadding="3" cellspacing="0" class="inputTable">
							<tr>
								<td class="require">帳號</td>
								<td class="alignLeft"><s:textfield name="entity.userNo" size="20" maxlength="15"/></td>
								<td class="require">密碼</td>
								<td class="alignLeft"><html:password property="entity.password" size="20" maxlength="15"/></td>
							</tr>
							<tr>
								<td class="buttonCell" colspan="4">
									<input type="submit" name="submitButton" value="登入"/>
								</td>
							</tr>
						</table>
					</fieldset>
				</s:form>
				<es2:javascript formName="/login" method="validateForm"/>
				<script type="text/javascript">

					EdsEvent.addOnLoad(
						function()
						{
							var form = document.forms[0];
							$(form).observe("submit", formHandler );
						}
					);

					function formHandler(event)
					{
						var form = document.forms[0];
						if( validateForm( form ) )
							showSubmitMessage();
						else
							Event.stop( event );
					}
				</script>
			</div>
		</div>
	</body>
</html>
