
<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/directive.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title><c:out value='參考規格維護'/></title>
		<%@include file="/jsp/header.jsp"%>	
	</head>
	<body onload="showPage();">
	<div id="page">
		<div id="mainContentBlock" style="width:70%;margin:0 auto;">
			<div id="progHeader">
				<div id="progTitle" title="程式代碼 : <c:out value='${currentFunctionObj.id}'/>"><c:out value='參考規格維護'/></div>
			</div>
			<jsp:include page="/jsp/messages.jsp"/>
			<s:form action="queryEmvRefSpec">
				<s:hidden name="entity.pagingInfo.enablePaging" value="true"/>
				<!-- <input type="hidden" name="entity.pagingInfo.currentPage" value="1"/> -->
				<edstw:folding foldingId="criteriaBlock" description="查詢條件">
					<fieldset>
						<legend>查詢條件</legend>
						<table cellpadding="4" cellspacing="0">							
							<tr>
								<td colspan="2" class="buttonCell">
									<input type="submit" id="defaultSubmitButton" name="submitButton" value="確定"  class="button" />									
								</td>
							</tr>
						</table>
					</fieldset>						
				</edstw:folding>
			</s:form>
			<edstw:folding foldingId="ajaxResultBlock" >
					<div id="ajaxResultBlockContent" style="width:95%">
					</div>	
			</edstw:folding>
		</div>
	</div>
	<div id="loading">&nbsp;</div>	
		<script type='text/javascript'>
		    function initialForm() {
				var form = document.forms[0];
				$(form).observe("submit", AjaxUtil.ajaxSubmitHandler.bindAsEventListener(form) );
				$('defaultSubmitButton').focus();					
				$('defaultSubmitButton').click();
				$('criteriaBlock').hide();
		    }
		    
		    EdsEvent.addOnLoad( initialForm );
			function showPage(){
				show('page', true);
				show('loading', false);
			}
			function show(id, value) {
				//document.getElementById("defaultSubmitButton").disabled=false;
			    document.getElementById(id).style.display = value ? 'block' : 'none';
			}
		</script>
	</body>
</html>



