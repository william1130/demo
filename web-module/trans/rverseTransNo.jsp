<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/directive.jsp"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<%@include file="/jsp/header.jsp"%>
	
</head>

<body>
	<div id="mainContentBlock">
		<div id="progHeader">
			<div id="progTitle">交易編號還原</div>
		</div>
		<jsp:include page="/jsp/messages.jsp" />
		<s:form action="rverseTransNo" style="text-align: center;">
			<edstw:folding foldingId="criteriaBlock" description="查詢條件" style="width: 75%;">
				<fieldset>
					<legend>輸入</legend>
					<table border="0" align="center" cellpadding="4" cellspacing="0">
						<tr>
							<td class="optional">交易編號</td>
							<td>
								<s:textfield name="entity.transNo" size="36" maxlength="23"/>
								<input id="defaultSubmitButton" type="submit" name="submitButton"
									value="確定" class="button">
							</td>
						</tr>
						<tr>
							<td class="optional">還原卡號</td>
							<td>
								<c:out value="${entity.cardNo}"/>
							</td>
						</tr>
					</table>
				</fieldset>
			</edstw:folding>
		</s:form>
	</div>
</body>
</html>