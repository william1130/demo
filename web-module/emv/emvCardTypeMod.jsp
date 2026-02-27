<%--
 * ==============================================================================================
 * $Id$
 * ==============================================================================================
--%>
<%@page import="proj.nccc.logsearch.persist.EmvCardType"%>
<%@page import="proj.nccc.logsearch.ProjConstants"%>
<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/directive.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title><c:out value='EMC CARD TYPE 修改'/></title>
		<%@include file="/jsp/header.jsp"%>	
	</head>
	<body>
		<div id="mainContentBlock">
			<div id="progHeader">
				<div id="progTitle" title="程式代碼 : <c:out value='${currentFunctionObj.id}'/>"><c:out value='EMC CARD TYPE 修改'/></div>
			</div>
			<jsp:include page="/jsp/messages.jsp"/>
			<div id="dataBlock">
				<s:form action="modifyEmvCardType">
				<s:hidden name="entity.cardType"/>
					<s:hidden name="entity.uiLogAction" value="MODIDY"/>
					<s:hidden name="entity.uiLogFunctionName" value="EMV_CARD_TYPE"/>
					<s:hidden name="entity.uiLogOther" value="卡別[%{entity.cardType }]"/> 
					<fieldset>
						<legend>修改</legend>
						<table border="0" cellpadding="4" cellspacing="0">
						<tr>
								<td class="require">*卡別</td>
								<td>
									<c:out value="${entity.cardType}" />
								</td>
							</tr>
							<tr>
								<td class="require">*卡別名稱</td>
								<td>
									<s:textfield name="entity.cardTypeName" size="15" maxlength="20"/>
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

						if( form['entity.cardTypeName'].value == "" )
							msg = msg + "卡別名稱必須輸入.\n";
						
						var rv = $revisedMonitor.result();

						if (rv == "" && msg.length <= 12 )
							msg = warnMsg + "未修改任何欄位，表單無法送出";
						
						if( msg.length > 12 )
						{
							alert( msg );
							event.stop();
							return false;
						}
						
						$j("input[name='entity.uiLogOther']").val("卡別["+form['entity.cardType'].value +"]");
						
						showSubmitMessage();
						return true;
					}
					function initialForm()
					{
						$(form).observe("submit", checkForm);
						$('defaultSubmitButton').focus();
						$revisedMonitor.addField($j("input[name='entity.cardTypeName']"),"卡別名稱");

					}
					
					//logOtherTitle = form['entity.description'].value;
					
					EdsEvent.addOnLoad( initialForm );
				</script>
				<script type="text/javascript" src="paraModify.js"></script>
			</div>
		</div>
	</body>
</html>
