<%@page pageEncoding="UTF-8"%>
<%-- 此為AJAX查詢結果的頁面, 必須額外設定contentType --%>
<%@include file="/jsp/content_type_ajax.jsp"%>
<%@include file="/jsp/directive.jsp"%>
<jsp:include page="/jsp/messages.jsp"/>
<c:if test="${not empty errorMsg}">

</c:if>
<c:if test="${empty errorMsg}">
<s:set var="resultList" value="%{pagingInfo.resultList}"/>

<div style="width: 100%; text-align: center;">
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

<table border="0" cellpadding="4" cellspacing="1"
	class="listTable">
	<thead>
		<tr>
			<th>序號</th>
			<th>未結帳端末機代號</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="item" items="${resultList}" varStatus="status">
			<tr class="<edstw:rowStyle value='${status.index}'/>">
				<td class="alignCenter"><c:out value="${status.index + 1}"/></td>
				<td class="alignCenter"><c:out value="${item.applyCode}"/></td>
		</c:forEach>
	</tbody>
</table>



</c:if>