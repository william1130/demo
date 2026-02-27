
<%@page import="proj.nccc.logsearch.ProjConstants"%>
<%@page import="proj.nccc.logsearch.beans.Util"%>
<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/directive.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title><c:out value='${currentProgramObj.name}'/></title>
		<%@include file="/jsp/header.jsp"%>	
	</head>
	<body>
	<%
		pageContext.setAttribute("successFlag",  Util.checkBooleanValue(request.getParameter("success")));
	%>
		<div id="mainContentBlock" style="width:70%">
			<div id="progHeader">
				<div id="progTitle" title="程式代碼 : <c:out value='${currentFunctionObj.id}'/>"><c:out value='EMV維護-新增'/></div>
			</div>
			<c:if test="${successFlag}">
				<jsp:include page="/jsp/messages.jsp"/>
				<div id="toBeApprove">
					<a href="<c:url value='toEmvTagRecordCreate.do' />" class="button">新增EMV TAG</a>
				</div>
			</c:if>
			<c:if test="${!successFlag}">
	
			<jsp:include page="/jsp/messages.jsp"/>
			<c:set var="cardTypeList" value="${paramBean.emvCardTypeList}" />
			<div id="dataBlock">
				<s:form action="createEmvTagRecord">
					<s:hidden name="entity.uiLogAction" value="CREATE"/>
					<s:hidden name="entity.uiLogFunctionName" value="EMV_PARA"/>
					<s:hidden name="entity.uiLogOther" value=""/>
					<fieldset>
						<legend>新增</legend>
						<table border="0" cellpadding="4" cellspacing="1" class="listTable" align="center" style="width:90%">
							<tr class="oddRow">
								<th>*卡別</th>
								<td>
									<c:forEach var="ct" items="${cardTypeList}">
										<input type="checkbox" name="cardType" value="${ct.cardType }" /><c:out value="${ct.cardTypeName }" />
									</c:forEach>
									&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="checkbox" name="entity.sameValueFlag" />各卡別取消同值關係
								</td>
							</tr>
							<tr class="evenRow">
								<th>*TAG名稱</th>
								<td>
									<s:textfield name="entity.emvTag"/>
								</td>
							</tr>
							<tr class="oddRow">
								<th>*全名</th>
								<td>
									<s:textfield name="entity.emvName" size="60" maxlength="70"/>
								</td>
							</tr>
							<tr class="evenRow">
								<th>*縮寫</th>
								<td>
									<s:textfield name="entity.emvAbbr"/>
								</td>
							</tr>
							<tr class="oddRow">
								<th>*描述</th>
								<td>
									<s:textarea name="entity.emvDesc" cols="80" rows="8"/>
								</td>
							</tr>		
							<tr class="evenRow">
								<th>*長度</th>
								<td>
									<s:hidden name="entity.emvLen"/>
									<input type="text" name="emvLen" id="emvLen" onkeyup="buildField();" />Bytes
								</td>
							</tr>
							<tr class="oddRow">
								<th>*Value</th>
								<td>
									<div id="contentValue">
									</div>
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

						/* if( form['entity.cardType'].value == "" )
							msg = msg + "卡別必須輸入.\n";
						 */
						
						if( msg.length > 12 )
						{
							alert( msg );
							event.stop();
							return false;
						}
						
						 var keyTitle = "TAG:"+form['entity.emvTag'].value+"";
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
					
					function buildField() {
						var len = form['emvLen'].value;
						form['entity.emvLen'].value = len;
						if (isNaN(len)) {
							//form['entity.emvLen'].value; = '';
							document.getElementById("contentValue").innerHTML = inputContent;
							alert("請輸入數字");
							//form['entity.emvLen'].value.focus;

						} else {
							
							if (len > 99) {
								document.getElementById("contentValue").innerHTML = inputContent;
								alert("長度不可大於99!! ");
								//form["entity.emvLen"].value.focus;
							} else {
								
								 inputContent = "<table width=90% style='background:transparent;'>";
								var classType = 'style="background:transparent;"';
								for (var i = 1; i <= len; i++) {
									
									inputContent = inputContent
											+ "<tr "+classType+"><th colspan=2>Byte"
											+ i
											+ "</th><td><input type='text' name='byteValue'  size='60'  /></td></tr>"
									for (var j = 8; j >= 1; j--) {
										inputContent = inputContent
												+ "<tr "+classType+"><td width=10></td><th>Bit"
												+ j
												+ "</th><td><input type='text' name='bitValue"+j+"' size='70'  /></td></tr>";
									}
								}

								inputContent = inputContent + "</table>";
								
								document.getElementById("contentValue").innerHTML = inputContent;
							} 
						}
					}	
					
					
				</script>
			</div>
			</c:if>
		</div>
	</body>
</html>
