<%--
 * ==============================================================================================
 * $Id: queryMFESCheckoutLog.jsp,v 1.1 2018/01/26 08:06:25 linsteph2\cvsuser Exp $
 * ==============================================================================================
--%>
<%@page import="proj.nccc.logsearch.ProjConstants"%>
<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/directive.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>MFES未結帳端末機查詢</title>
		<%@include file="/jsp/header.jsp"%>	
	</head>
	<body onload="showPage();">
	<div id="page">
		<div id="mainContentBlock" style="width: 80%">
			<div id="progHeader">
				<div id="progTitle" title="程式代碼 : <c:out value='${currentFunctionObj.id}'/>">MFES未結帳端末機查詢</div>
			</div>
			<jsp:include page="/jsp/messages.jsp"/>
			<s:form action="queryMFESCheckout" style="text-align: center;">
				<%-- 設定開啟分頁功能 --%>
				<s:hidden name="entity.pagingInfo.enablePaging" value="true"/>
				<s:hidden name="entity.pagingInfo.currentPage" value="1"/>
				<edstw:folding foldingId="criteriaBlock" description="查詢條件" style="width: 500px">
					<fieldset>
						<legend class="alignCenter">查詢</legend>
						<table cellpadding="4" cellspacing="0" align="center" >													
							<tr>								
								<td class="optional">請款代碼</td>
								<td><s:textfield name="entity.applyCode" size="6" maxlength="6"></s:textfield></td>								
							</tr>						
							<tr>
								<td class="buttonCell" colspan="2">
									<input type="submit" id="defaultSubmitButton" name="submitButton" value="確認" class="button" disabled="disabled" />
									<input name="resetButton" type="button" value="重設" class="button" onclick="reloadPage();">
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

		    	if(msg.length > 12){
		    		alert(msg);
					return false;
		    	} 
		    	if(form["entity.applyCode"].value == "") {
		    		msg = "請輸入請款代碼";
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
		    
			function reloadPage() {
				location.href = '<c:url value="/trans/toQueryMFESCheckout.do"/>';
			}
		</script>
	</body>
</html>
