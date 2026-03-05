<%--
 * ==============================================================================================
 * $Id$
 * ==============================================================================================
--%>
<%@page import="proj.nccc.logsearch.persist.EmvCardType"%>
<%@page import="proj.nccc.logsearch.ProjConstants"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date" %>
<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/directive.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title><c:out value='${currentProgramObj.name}'/></title>
		<%@include file="/jsp/header.jsp"%>	
	</head>
	<body>
		<div id="mainContentBlock" style="width:70%;">
			<div id="progHeader">
				<div id="progTitle" title="程式代碼 : <c:out value='${currentFunctionObj.id}'/>"><c:out value='參考規格${currentProgramObj.name}'/></div>
			</div>
			<jsp:include page="/jsp/messages.jsp"/>
			<div id="dataBlock" >
				<s:form action="createEmvRefSpec">
					
					<s:hidden name="entity.uiLogAction" value="CREATE"/>
					<s:hidden name="entity.uiLogFunctionName" value="EMV_SPEC"/>
					<s:hidden name="entity.uiLogOther" value=""/>
					<%
					Date today = new Date();
					SimpleDateFormat f = new SimpleDateFormat("yyyyMMddhhmmss");
					String specID = f.format(today);
					pageContext.setAttribute("specID", specID);
					%>
					<s:hidden name="entity.specID" value="#{specID}" />
					<fieldset>
						<legend>新增</legend>
						<table border="0" cellpadding="4" cellspacing="0">
							<tr>
								<td class="require">*參考規格</td>
								<td>
									<s:textfield name="entity.specName" size="90"/>
								</td>
							</tr>
													
							<tr>
								<td colspan="2" class="buttonCell">
									<input type="submit" id="defaultSubmitButton" name="submitButton" value="確定" class="button">
								</td>
							</tr>
						</table>
					</fieldset>
				</s:form>
				<script type="text/javascript">
					var form = document.forms[0];

					
					function checkForm( event )
					{
						var msg = "資料錯誤:\n\n";
						var warnMsg = "注意:\n\n";

						if( form['entity.specName'].value == "" )
							msg = msg + "參考規格必須輸入.\n";
						
						
						if( msg.length > 12 )
						{
							alert( msg );
							event.stop();
							return false;
						}
						
						keyTitle = "參考規格["+form['entity.specName'].value+"]";
						$j("input[name='entity.uiLogOther']").val(keyTitle);	
						showSubmitMessage();
						return true;
					}
					function initialForm()
					{
						$(form).observe("submit", checkForm);
						$('defaultSubmitButton').focus();
						
					}
			
					EdsEvent.addOnLoad( initialForm );
				</script>
				<script type="text/javascript" src="paraModify.js"></script>
			</div>
		</div>
	</body>
</html>
