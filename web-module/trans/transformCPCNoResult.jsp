<%@page pageEncoding="UTF-8"%>
<%-- 此為AJAX查詢結果的頁面, 必須額外設定contentType --%>
<%@include file="/jsp/directive.jsp"%>
<jsp:include page="/jsp/messages.jsp"/>
<%@taglib uri="http://hp.com.tw/nccc/customtag" prefix="util"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<c:set var="entity" value="${transLogForm.entity}" />

<c:if test="${entity.ctcbMid ne '' }">
	<table border="0" cellpadding="4" cellspacing="1" class="listTable">
			<tr>
				<th>非聯合信用卡中心商店代號</th>
				<th>聯合信用卡中心商店代號</th>
			</tr>
			<tr>
				<td class="alignCenter"><c:out value="${entity.ctcbMid}"/></td>
				<td class="alignCenter">
				<c:if test="${entity.ncccMid eq '查無資料' }">
					<span style="color:red;font-weight:bold;"><c:out value="${entity.ncccMid}"/></span>
				</c:if>
				<c:if test="${entity.ncccMid ne '查無資料' }">
					<span style="color:blue;font-weight:bold;"><c:out value="${entity.ncccMid}"/></span>
				</c:if>
				</td>
			</tr>
	</table>
</c:if>
<br />
<c:if test="${ entity.ctcbTid ne '' }">
<table border="0" cellpadding="4" cellspacing="1" class="listTable">
		<tr>
			<th>非聯合信用卡中心端末機代號</th>
			<th>聯合信用卡中心端末機代號</th>
		</tr>
		<tr>
			<td class="alignCenter"><c:out value="${entity.ctcbTid}"/></td>
			<td class="alignCenter">
				<c:if test="${entity.ncccTid eq '查無資料' }">
					<span style="color:red;font-weight:bold;"><c:out value="${entity.ncccTid}"/></span>
				</c:if>
				<c:if test="${entity.ncccTid ne '查無資料' }">
					<span style="color:blue;font-weight:bold;"><c:out value="${entity.ncccTid}"/></span>
				</c:if>
				</td>
		</tr>
</table>
</c:if>