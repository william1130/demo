<%@page import="proj.nccc.logsearch.ProjConstants"%>
<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/directive.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title><c:out value='EMV TAG維護覆核'/></title>
		<%@include file="/jsp/header.jsp"%>	
	</head>
	<body onload="showPage();">
	<div id="page">
		<div id="mainContentBlock" style="width:80%;margin:0 auto;">
			<div id="progHeader">
				<div id="progTitle" title="程式代碼 : <c:out value='${currentFunctionObj.id}'/>"><c:out value='EMV TAG維護覆核'/></div>
			</div>
			<jsp:include page="/jsp/messages.jsp"/>
			
			<s:form action="queryEmvTagRecordTemp">
				<%-- 設定開啟分頁功能 --%>
				<s:hidden name="entity.pagingInfo.enablePaging" value="true"/>
				<s:hidden name="entity.uiLogAction" value=""/>
				<s:hidden name="entity.uiLogFunctionName" value="EMV晶片"/>
				<edstw:folding foldingId="criteriaBlock" description="查詢條件" style="width: 500px">
					<fieldset>
						<legend>查詢條件</legend>
						<table cellpadding="4" cellspacing="0">							
							<tr>
								<td class="require">日期區間</td>
								<td colspan="3">						
									
									<s:textfield name="entity.dateFrom" maxlength="8" size="10" title="請輸入西元年(YYYYMMDD)" value="%{#attr.dateUtil.sysDateString.inputString}"/>
									<img id="_dateFrom" class="datePickerImage" src="../images/dateIcon.gif" border="0" align="bottom" width="19px" height="19px" title="選擇日期" alt="選擇日期"/>
									<script type='text/javascript' language='javascript'>
										var form = document.forms[0];
										new DatePicker( {imageId:'_dateFrom', dateField:form['entity.dateFrom']} );
									</script>
									-
          							<s:textfield name="entity.dateTo" maxlength="8" size="10" title="請輸入西元年(YYYYMMDD)" value="%{#attr.dateUtil.sysDateString.inputString}"/>
									<img id="_dateTo" class="datePickerImage" src="../images/dateIcon.gif" border="0" align="bottom" width="19px" height="19px" title="選擇日期" alt="選擇日期"/>
									<script type='text/javascript' language='javascript'>
										var form = document.forms[0];
										new DatePicker( {imageId:'_dateTo', dateField:form['entity.dateTo']} );
									</script>(YYYYMMDD)
									
									
								</td>
							</tr>							
							<tr>								
								<td class="optional">使用者代號</td>
								<td>
									<s:textfield name="entity.userId"></s:textfield>																		
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
				
		    	msg = msg + checkDateRange(form["entity.dateFrom"].value, form["entity.dateTo"].value, "日期區間");
		    	
		    	if(msg.length > 12){
		    		alert(msg);
					return false;
		    	} 
		    	
		    	return true;
		    }
		</script>
	</body>
</html>



