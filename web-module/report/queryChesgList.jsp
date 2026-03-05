<%@page pageEncoding="UTF-8"%>
<%-- 此為AJAX查詢結果的頁面, 必須額外設定contentType --%>
<%@include file="/jsp/content_type_ajax.jsp"%>
<%@include file="/jsp/directive.jsp"%>
<jsp:include page="/jsp/messages.jsp"/>
<c:if test="${not empty errorMsg}">

</c:if>
<c:if test="${empty errorMsg}">
<s:set var="pagingInfo" value="%{entity.pagingInfo}" /> 
<s:set var="resultList" value="%{pagingInfo.resultList}"/>

<div style="width: 100%; text-align: center;">
	<jsp:include page="/jsp/pagingInfo.jsp"/>
</div> 
<style>
	.displayRowspan{height:12px;line-height:12px;padding:1px;font-size:12px;}
		.displayRowspan a{font-size:12px;}
		.displayRowspan u{font-size:12px;}
		.displayRowspan2{height:12px;line-height:6px;padding:1px;font-size:12px;}
		.displayFontSize{font-size:12px;}
		.displayFontSize u{font-size:12px;}
		hr{border:0;height:1px;background-color:#C0C0C0;color:#C0C0C0}
</style>
<jsp:include page="../parseCode/tooltipIC.jsp"/>
<jsp:include page="../parseCode/tooltipCrdVefyFlg.jsp"/>
<jsp:include page="../parseCode/tooltipPOSEntryMode.jsp"/>
<jsp:include page="../parseCode/tooltipStandIn.jsp"/>

	<table border="0" cellpadding="4" cellspacing="1" class="listTable">
		<thead>
			<tr>
				<th>序號</th>
				<th>交易日期</th>
				<th>端未機代號</th>
				<th>特店代號</th>
				<th>特店名稱</th>
				<th>總交易筆數</th>
				<th>CHESG FLAG<br>交易筆數
				</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${resultList}" varStatus="status">
				<tr class="<edstw:rowStyle value='${status.index}'/>">
					<td class="alignCenter"><c:out value="${status.index + 1}" /></td>
					<td class="alignCenter"><c:out value="${item.logDate}" /></td>
					<td class="alignCenter"><c:out value="${item.termId}" /></td>
					<td class="alignCenter"><c:out value="${item.merchantId}" /></td>
					<td class="alignCenter"><c:out value="${item.merchantChinName}" /></td>
					<td class="alignCenter"><c:out value="${item.totalCount}" /></td>
					<td class="alignCenter"><c:out value="${item.chesgCount}" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</c:if>