<%--
 * ==============================================================================================
 * $Id$
 * ==============================================================================================
--%>
<%@page import="proj.nccc.logsearch.ProjConstants"%>
<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/directive.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>EMV TAG修改查詢</title>
		<%@include file="/jsp/header.jsp"%>	
	</head>
	<body onload="showPage();">
	<div id="page">
		<div id="mainContentBlock" style="width: 80%">
			<div id="progHeader">
				<div id="progTitle" title="程式代碼 : <c:out value='${currentFunctionObj.id}'/>">EMV TAG修改查詢</div>
			</div>
			<jsp:include page="/jsp/messages.jsp"/>
			<c:set var="cardTypeList" value="${paramBean.emvCardTypeList}" />
			<s:form action="queryEmvTagRecordMod">
				<%-- 設定開啟分頁功能 --%>
				<s:hidden name="entity.pagingInfo.enablePaging" value="true"/>
				<s:hidden name="entity.pagingInfo.currentPage" value="1"/>
				<edstw:folding foldingId="criteriaBlock" description="查詢條件" style="width: 500px">
					<fieldset>
						<legend>查詢條件</legend>
						<table cellpadding="4" cellspacing="0">							
							<tr>
								<td class="optional">卡別</td>
								<td>
								<s:select name="entity.cardType" headerKey="" headerValue="全部" list="#attr.paramBean.emvCardTypeOptions" listKey="value" listValue="label"/>
									<%-- <s:select name="entity.cardType" emptyOption="true" list="#{'%{cardTypeList }':'<c:out value="%{ct.cardTypeName }" />'}" />		 --%>							
								</td>
							</tr>							
							<tr>								
								<td class="optional">TAG名稱</td>
								<td>
									<s:textfield name="entity.emvTag"></s:textfield>																		
								</td>								
							</tr>						
							<tr>
								<td colspan="4" class="buttonCell">
									<input type="submit" id="defaultSubmitButton" name="submitButton" value="確定" class="button" disabled="disabled" />
								</td>
							</tr>
						</table>
					</fieldset>
				</edstw:folding>
			</s:form>
			<edstw:folding foldingId="ajaxResultBlock" description="查詢結果">
				<fieldset>
					<legend>查詢結果</legend>
					<div id="ajaxResultBlockContent">
					</div>
				</fieldset>
			</edstw:folding>
		</div>
	</div>
	<div id="loading">&nbsp;</div>	
		<script type='text/javascript'>
			var form = document.forms[0];
			//var queryAction = form.action;

			function initialForm() {
				
				$(form).observe("submit", AjaxUtil.ajaxSubmitHandler.bindAsEventListener(form, checkForm) );
				$('defaultSubmitButton').focus();					
				/* $('defaultSubmitButton').click();
				$('criteriaBlock').hide(); */
			}
		    
		    EdsEvent.addOnLoad( initialForm );
			
		    function showPage(){
				show('page', true);
				show('loading', false);
			}
		    
			function show(id, value) {
				document.getElementById("defaultSubmitButton").disabled=false;
			    document.getElementById(id).style.display = value ? 'block' : 'none';
			}

		    function checkForm( ) {
		    	var msg = "資料錯誤:\n\n";
				
		    	//msg = msg + checkDateRange(form["entity.dateFrom"].value, form["entity.dateTo"].value, "日期區間");
		    	
		    	if(msg.length > 12){
		    		alert(msg);
					return false;
		    	} 
		    	
		    	return true;
		    }
		    
		    function submitPage(pageIdx) {
				
				form["entity.pagingInfo.currentPage"].value = pageIdx;
				ajaxFormSubmit( {form: form, checkForm: checkForm} );
				form["entity.pagingInfo.currentPage"].value = "1";
			}
		</script>
	</body>
</html>
