<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/directive.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<%@include file="/jsp/header.jsp"%>

</head>

<body>
	<div id="mainContentBlock" style="margin:0 auto;">
		<div id="progHeader">
			<div id="progTitle">併機代號查詢</div>
		</div>
		<jsp:include page="/jsp/messages.jsp" />
		<s:form action="trans/transformCPCNo" style="text-align: center;">
			<edstw:folding foldingId="criteriaBlock" description="查詢條件">
				<fieldset>
					<legend>查詢條件</legend>
					<table border="0" align="center" cellpadding="4" cellspacing="0">
						<tr>
							<td class="optional">商店代號</td>
							<td>
								<s:textfield name="entity.ctcbMid" size="24" maxlength="19"/>
							</td>	
						</tr>
						<tr>
							<td class="optional">端末機代號</td>
							<td>
								<s:textfield name="entity.ctcbTid" size="24" maxlength="19"/>
							</td>	
						</tr>
						<tr>
							<td colspan="2" class="buttonCell">
								<input id="defaultSubmitButton" type="submit" name="submitButton" value="確定" class="button">
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
				<div id="ajaxResultBlockContent"></div>
			</fieldset>
		</edstw:folding>
	</div>
</body>
</html>

<script type='text/javascript'>
	var form = document.forms[0];
	
	function validateForm(form) {
		
		var msg = "";
		
		if (form["entity.ctcbMid"].value == "" && form["entity.ctcbTid"].value == "") {
			msg += "需至少輸入商店代號或端末機代號!\n";
		}
		
		if (msg != "") {
			
			alert(msg);
			return false;
		}
		
		return true;
	}
	
	function checkForm(event) {
		if (validateForm(form)) {
			form.target = undefined;
			form.action = "<c:url value="/trans/transformCPCNo.do"/>";
			return true;
		} else {
			event.stop();
			return false;
		}
	}

	function initialForm() {
			
		$("defaultSubmitButton").observe("click",
				AjaxUtil.ajaxSubmitHandler.bindAsEventListener(form, checkForm));

		$('defaultSubmitButton').focus();
	}

	function reloadPage(){
		location.href = '<c:url value="/trans/toTransformCPCNo.do"/>';
	}

	
	EdsEvent.addOnLoad(initialForm);				
</script>
	