<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/directive.jsp"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@include file="/jsp/header.jsp"%>
<jsp:include page="../parseCode/tooltipIC.jsp" />
<jsp:include page="../parseCode/tooltipCrdVefyFlg.jsp" />
<jsp:include page="../parseCode/tooltipPOSEntryMode.jsp" />
<jsp:include page="../parseCode/tooltipStandIn.jsp" />
<script type="text/javascript" src="../scripts/addToolTip.js"></script>
<style>
.edsToolTip {
	background-color: #CCCCFF;
	opacity: 0.9;
}

.preClass {
  width:auto;
  max-width: 950px;
  white-space: pre-wrap; /* css-3 */
  white-space: -moz-pre-wrap; /* Mozilla, since 1999 */
  white-space: -pre-wrap; /* Opera 4-6 */
  white-space: -o-pre-wrap; /* Opera 7 */
  word-wrap: break-word; /* Internet Explorer 5.5+ */
  padding: 10px;
  word-break: break-all;
}	
		</style>
</head>

<c:set var="respRole" value="" />
<body class="ieVersion">
	
	<c:if test="${entity.hostAccord != 'DCC' && entity.hostAccord != 'ESC'}">
		<div  style="text-align: center;width: 90%;margin:0 auto">
	
			<jsp:include page="/jsp/messages.jsp" />
			<div id='criteriaBlock' class='foldingBlock' style="width: 100%">
			<c:forEach var="item" items="${entity.rawList}" varStatus="status">
			<table border="0" cellpadding="4" cellspacing="1" class="listTable">
				<thead>
					<tr>
						<th width="100%" style="text-align:left;">
							<c:out value="${item.rawType}"/> (<c:out value="${item.logDate }" /> <c:out value="${item.logTime }" />)
						</th>
						
					</tr>
				</thead>
				<tbody>
					<tr class="oddRow">
						<td class="alignLeft">
						<div class="preClass">
<c:out value="${item.rawData}"/>
						</div>
						</td>
					</tr>
				</tbody>
			</table>
			<br />
			</c:forEach>
			</div>
		</div>
	</c:if>
	
	<c:if test="${entity.hostAccord == 'DCC' || entity.hostAccord == 'ESC'}">
		<div id="mainContentBlock" style="text-align: center">
	
			<jsp:include page="/jsp/messages.jsp" />
			<div id="progTitle" style="width: 75%">選擇ISO資料</div>
			
			<div id='criteriaBlock' class='foldingBlock' style="width: 75%">
				<table border="0" cellpadding="4" cellspacing="1" class="listTable"
					align="center">
					<thead>
						<tr>
							<th>序號</th>
							<th>交易類別</th>
							<th>發送時間</th>
							<th>原始電文</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="item" items="${entity.rawList}" varStatus="status">
							<tr class="<edstw:rowStyle value='${status.index}'/>">
								<td class="alignCenter"><c:out value="${status.index + 1}"/></td>
								<td class="alignCenter"><c:out value="${item.rawType}"/></td>
								<td class="alignCenter"><c:out value="${item.logDate}" /> <c:out value="${item.logTime}" /></td>
								<td align="center">
									<c:if test="${item.rawType != null && item.rawType != '        ' && item.rawType !='' }">
										<button type="button" class="button" onclick="showIFrameDialog('<c:url value="/trans/parseLDSSData.do?rawType=${item.rawType}&seqId=${item.seqId}&logTime=${item.logTime}&maskCardNo=${entity.maskCardNo}&hostAccord=${entity.hostAccord}"/>',{width:1040, height:'780',title: 'ISO'});return false;">
											ISO
										</button>
									</c:if>
								</td>
							</tr>
						</c:forEach>
						<tr>
							<td align="center" colspan="7"><button type="button"
									class="button" onclick="parent.closeIFrameDialog();">關閉</button></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<br />
		<div id='criteriaBlock' class='foldingBlock' style="text-align: center; width: 100%;">
			<table border="0" cellpadding="4" cellspacing="1" class="listTable">
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
					<tr class="<edstw:rowStyle value='${status.index}'/>">
						<td class="alignCenter" rowspan="2"><c:out value="${status.index + 1}"/></td>
						<td class="alignCenter" rowspan="2"><c:out value="${entity.hostName}"/></td>
						<td class="alignCenter">
							<c:out value="${entity.cardNumMask}"/>
						</td>
						<td class="alignCenter" rowspan="2"><u
							onmouseover="addToolTip(this, '特店名稱', '<c:out value="${entity.merchantChinName}"/>');"
							style="color: blue;"><c:out value="${entity.merchantId}"/></u></td>
						<td class="alignCenter" rowspan="2"><c:out value="${entity.termId}"/></td>
						<td class="alignRight" rowspan="2">
							<edstw:formatNumber value="${entity.tranAmount}" pattern="#,##0.###"/>
						</td>
						<td class="alignCenter" rowspan="2">
							<c:if test="${entity.approveCodeName == null}">
								<c:out value="${entity.approveCode}"/>
							</c:if>
							<c:if test="${entity.approveCodeName != null}">
								<u onmouseover="addToolTip(this, '授權碼', '<c:out value="${entity.approveCodeName}"/>');"
									style="color: blue;"><c:out value="${entity.approveCode}"/></u>
							</c:if>
						</td>
						<td class="alignCenter" rowspan="2">
							<c:out value="${entity.tranYear}"/><c:out value="${entity.tranDate}"/>
						</td>
						<td class="alignCenter" rowspan="2"><c:out value="${entity.tranTime}"/></td>
						
						<td class="alignCenter"><c:out value="${entity.hostAccord}"/></td>
						<td class="alignCenter"><c:out value="${entity.tranType}"/></td>
						<td class="alignCenter"><c:out value="${entity.batchNum}"/></td>
						<td class="alignCenter"><c:out value="${entity.respCode }"/>
						<c:out value="${paramBean.responseCodeNameMap[entity.respCode]}"/>
						</td>
					</tr>
					<tr class="<edstw:rowStyle value='${status.index}'/>">
					
						<td class="alignCenter"><c:out value="${entity.expDate}"/></td>
						<td class="alignCenter"><c:out value="${entity.mti}"/></td>
						<td class="alignCenter"><c:out value="${entity.procCode}"/></td>
						<td class="alignCenter"><c:out value="${entity.seqNum}"/></td>
						<td class="alignCenter"><c:out value="${entity.posEntryMode}"/></td>
					</tr>
				</tbody>
			</table>
		</div>
	</c:if>
	<br />
</body>
</html>