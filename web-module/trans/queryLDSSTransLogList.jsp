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

	<s:hidden name="maskCardNo" />
	<s:hidden name="showCardIso" />

<table border="0" cellpadding="4" cellspacing="1"
	class="listTable">
	<thead>

		<tr>
			<th rowspan="2">序號</th>
			<th rowspan="2">LDSS主機</th>
			<th>卡號</th>
			<th rowspan="2">商店代碼</th>
			<th rowspan="2">端末機代碼</th>
			<th rowspan="2">金額</th>
			<th rowspan="2">授權碼</th>
			<th rowspan=2>交易日期</th>
			<th rowspan=2>交易時間</th>
			<th>對應主機</th>
			<th>交易類別</th>
			<th>批號</th>
			<th>回應碼</th>
		</tr>
		<tr>
			<th>有效期</th>
			<th>MTI</th>
			<th>Process Code</th>
			<th>序號</th>			
			<th><u
				onmouseover="addToolTip(this, 'POS Entry Mode', 'tooltipPOSEntryMode', true);"
				style="color: blue;">Entry Mode</u></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="item" items="${resultList}" varStatus="status">
			<tr class="<edstw:rowStyle value='${status.index}'/>">
				<td class="alignCenter" rowspan="2"><c:out value="${status.index + 1}"/></td>
				<td class="alignCenter" rowspan="2"><c:out value="${item.hostName}"/></td>
				<td class="alignCenter">
					<c:if test="${item.showCardIso == 'Y'}">
						<a href="#"
							onclick="showIFrameDialog('<c:url value="/trans/queryLDSSTransLogISO.do?entity.seqId=${item.seqId}&entity.maskCardNo=${item.maskCardNo}&entity.merchantId=${item.merchantId}&entity.approveCode=${item.approveCode}"/>',{width:1080, height:'520',title: 'ISO內容'});return false;"
							style="text-decoration: underline; color: blue;"><c:out value="${item.cardNumMask}"/></a>
					</c:if>
					<c:if test="${item.showCardIso != 'Y'}">
						<c:out value="${item.cardNumMask}"/>
					</c:if>
				</td>
				<td class="alignCenter" rowspan="2"><u
					onmouseover="addToolTip(this, '特店名稱', '<c:out value="${item.merchantChinName}"/>');"
					style="color: blue;"><c:out value="${item.merchantId}"/></u></td>
				<td class="alignCenter" rowspan="2"><c:out value="${item.termId}"/></td>
				<td class="alignRight" rowspan="2">
					<edstw:formatNumber value="${item.tranAmount}" pattern="#,##0.###"/>
				</td>
				<td class="alignCenter" rowspan="2">
					<c:if test="${item.approveCodeName == null}">
						<c:out value="${item.approveCode}"/>
					</c:if>
					<c:if test="${item.approveCodeName != null}">
						<u onmouseover="addToolTip(this, '授權碼', '<c:out value="${item.approveCodeName}"/>');"
							style="color: blue;"><c:out value="${item.approveCode}"/></u>
					</c:if>
				</td>
				<td class="alignCenter" rowspan="2">
					<c:out value="${item.tranYear}"/><c:out value="${item.tranDate}"/>
				</td>
				<td class="alignCenter" rowspan="2"><c:out value="${item.tranTime}"/></td>
				
				<td class="alignCenter"><c:out value="${item.hostAccord}"/></td>
				<td class="alignCenter"><c:out value="${item.tranType}"/></td>
				<td class="alignCenter"><c:out value="${item.batchNum}"/></td>
				<td class="alignCenter"><c:out value="${item.respCode }"/>
				<c:out value="${paramBean.responseCodeNameMap[item.respCode]}"/>
				</td>
			</tr>
			<tr class="<edstw:rowStyle value='${status.index}'/>">
			
				<td class="alignCenter"><c:out value="${item.expDate}"/></td>
				<td class="alignCenter"><c:out value="${item.mti}"/></td>
				<td class="alignCenter"><c:out value="${item.procCode}"/></td>
				<td class="alignCenter"><c:out value="${item.seqNum}"/></td>
				<td class="alignCenter"><c:out value="${item.posEntryMode}"/></td>	
		</c:forEach>
	</tbody>
</table>



</c:if>