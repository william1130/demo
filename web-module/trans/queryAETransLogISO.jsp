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
	</style>
</head>

<c:set var="entity" value="${transLogForm.entity}" />
<c:set var="respRole" value="" />
<body class="ieVersion">
	<div id="mainContentBlock" style="text-align: center">

		<jsp:include page="/jsp/messages.jsp" />
		<div id="progTitle" style="width: 75%">選擇ISO資料</div>
		<div id='criteriaBlock' class='foldingBlock' style="width: 75%">

			<table border="0" cellpadding="2" cellspacing="1" class="listTable"
				align="center">
				<thead>
					<tr>
						<th width="30%" rowspan="2">系統</th>
						<th width="35%" colspan="3">From</th>
						<th width="35%" colspan="3">To</th>
					</tr>
					<tr>
						<th width="12%">日期</th>
						<th width="12%">時間</th>
						<th width="12%">訊息</th>
						<th width="12%">日期</th>
						<th width="12%">時間</th>
						<th width="12%">訊息</th>
					</tr>
				</thead>
				<tbody>
					<tr class="oddRow">
						<td align="center">AE EDC</td>
						<td align="center"><c:out value="${entity.fromEdcDate}" /></td>
						<td align="center"><c:out value="${entity.fromEdcTime}" /></td>
						<td align="center"><c:if test="${entity.fromEdcDate != null && entity.fromEdcDate != '' && entity.fromEdcDate != '        '}"><button type="button" class="button"
								onclick="showIFrameDialog('<c:url value="/trans/parseAEISOCode.do?rawType=FROM%20EDC&seqId=${entity.seqId}&maskCardNo=${entity.maskCardNo}&edcSpec=${entity.edcSpec}&cardType=${entity.cardType}"/>',{width:1040, height:'480',title: 'ISO'});return false;">ISO</button></c:if></td>
						<td align="center"><c:out value="${entity.toEdcDate}" /></td>
						<td align="center"><c:out value="${entity.toEdcTime}" /></td>
						<td align="center"><c:if test="${entity.toEdcDate != null && entity.toEdcDate != '' && entity.toEdcDate != '        '}"><button type="button" class="button"
								onclick="showIFrameDialog('<c:url value="/trans/parseAEISOCode.do?rawType=TO%20EDC&seqId=${entity.seqId}&maskCardNo=${entity.maskCardNo}&edcSpec=${entity.edcSpec}"/>',{width:1040, height:'480',title: 'ISO'});return false;">ISO</button></c:if></td>
					</tr>

					<tr class="evenRow">
							<td align="center">BASE24</td>
							<td align="center"><c:out value="${entity.fromBase24Date}" /></td>
							<td align="center"><c:out value="${entity.fromBase24Time}" /></td>
							<td align="center"><c:if test="${entity.fromBase24Date != null && entity.fromBase24Date != '        ' && entity.fromBase24Date !='' }"><button type="button" class="button"
									onclick="showIFrameDialog('<c:url value="/trans/parseAEISOCode.do?rawType=FROM%20B24&seqId=${entity.seqId}&maskCardNo=${entity.maskCardNo}&edcSpec=${entity.edcSpec}"/>',{width:1040, height:'480',title: 'ISO'});return false;">ISO</button></c:if></td>
							<td align="center"><c:out value="${entity.toBase24Date}" /></td>
							<td align="center"><c:out value="${entity.toBase24Time}" /></td>
							<td align="center"><c:if test="${entity.toBase24Date != null && entity.toBase24Date != '        ' && entity.toBase24Date != ''}"><button type="button" class="button"
									onclick="showIFrameDialog('<c:url value="/trans/parseAEISOCode.do?rawType=TO%20B24&seqId=${entity.seqId}&maskCardNo=${entity.maskCardNo}&edcSpec=${entity.edcSpec}"/>',{width:1040, height:'480',title: 'ISO'});return false;">ISO</button></c:if></td>
						</tr>

					<tr class="oddRow">
						<td align="center">ASM</td>
						<td align="center"><c:out value="${entity.fromAsmDate}" /></td>
						<td align="center"><c:out value="${entity.fromAsmTime}" /></td>
						<td align="center"><c:if test="${entity.fromAsmDate != null && entity.fromAsmDate != '        ' && entity.fromAsmDate !='' }"><button type="button" class="button"
								onclick="showIFrameDialog('<c:url value="/trans/parseAEISOCode.do?rawType=FROM%20ASM&seqId=${entity.seqId}&maskCardNo=${entity.maskCardNo}&edcSpec=${entity.edcSpec}"/>',{width:1040, height:'480',title: 'ISO'});return false;">ISO</button></c:if></td>
						<td align="center"><c:out value="${entity.toAsmDate}" /></td>
						<td align="center"><c:out value="${entity.toAsmTime}" /></td>
						<td align="center"><c:if test="${entity.toAsmDate != null && entity.toAsmDate != '        ' && entity.toAsmDate !='' }"><button type="button" class="button"
								onclick="showIFrameDialog('<c:url value="/trans/parseAEISOCode.do?rawType=TO%20ASM&seqId=${entity.seqId}&maskCardNo=${entity.maskCardNo}&edcSpec=${entity.edcSpec}"/>',{width:1040, height:'480',title: 'ISO'});return false;">ISO</button></c:if></td>
					</tr>
					<tr class="evenRow">
						<td align="center">TMS</td>
						<td align="center"><c:out value="${entity.fromTmsDate}" /></td>
						<td align="center"><c:out value="${entity.fromTmsTime}" /></td>
						<td align="center"><c:if test="${entity.fromTmsDate != null && entity.fromTmsDate != '        ' && entity.fromTmsDate != ''}"><button type="button" class="button"
								onclick="showIFrameDialog('<c:url value="/trans/parseAEISOCode.do?rawType=FROM%20TMS&seqId=${entity.seqId}&maskCardNo=${entity.maskCardNo}&edcSpec=${entity.edcSpec}"/>',{width:1040, height:'480',title: 'ISO'});return false;">ISO</button></c:if></td>
						<td align="center"><c:out value="${entity.toTmsDate}" /></td>
						<td align="center"><c:out value="${entity.toTmsTime}" /></td>
						<td align="center"><c:if test="${entity.toTmsDate != null && entity.toTmsDate != '        ' && entity.toTmsDate !='' }"><button type="button" class="button"
								onclick="showIFrameDialog('<c:url value="/trans/parseAEISOCode.do?rawType=TO%20TMS&seqId=${entity.seqId}&maskCardNo=${entity.maskCardNo}&edcSpec=${entity.edcSpec}"/>',{width:1040, height:'480',title: 'ISO'});return false;">ISO</button></c:if></td>
					</tr>
					<c:if test="${'S' eq entity.cardType}">
						<tr class="oddRow">
							<td align="center">EDC Confirm</td>
							<td align="center"><c:out value="${entity.fromEdcConfDate}" /></td>
							<td align="center"><c:out value="${entity.fromEdcConfTime}" /></td>
							<td align="center"><c:if test="${entity.fromEdcConfDate != null && entity.fromEdcConfDate != '' && entity.fromEdcConfDate != '        '}"><button type="button" class="button"
									onclick="showIFrameDialog('<c:url value="/trans/parseAEISOCode.do?rawType=FROM%20EDC%20CONFIRM&seqId=${entity.seqId}&maskCardNo=${entity.maskCardNo}&edcSpec=${entity.edcSpec}"/>',{width:1040, height:'480',title: 'ISO'});return false;">ISO</button></c:if></td>
							<td align="center"><c:out value="${entity.toEdcConfDate}" /></td>
							<td align="center"><c:out value="${entity.toEdcConfTime}" /></td>
							<td align="center"><c:if test="${entity.toEdcConfDate != null && entity.toEdcConfDate != '' && entity.toEdcConfDate != '        '}"><button type="button" class="button"
									onclick="showIFrameDialog('<c:url value="/trans/parseAEISOCode.do?rawType=TO%20EDC%20CONFIRM&seqId=${entity.seqId}&maskCardNo=${entity.maskCardNo}&edcSpec=${entity.edcSpec}"/>',{width:1040, height:'480',title: 'ISO'});return false;">ISO</button></c:if></td>
						</tr>
						<tr class="evenRow">
							<td align="center">SPSW Confirm</td>
							<td align="center" style="background:#bbbbbb"></td>
							<td align="center" style="background:#bbbbbb"></td>
							<td align="center" style="background:#bbbbbb"></td>
							<td align="center"><c:out value="${entity.toSpswConfDate}" /></td>
							<td align="center"><c:out value="${entity.toSpswConfTime}" /></td>
							<td align="center"><c:if test="${entity.toSpswConfDate != null && entity.toSpswConfDate != '        ' && entity.toSpswConfDate != '' }"><button type="button" class="button"
									onclick="showIFrameDialog('<c:url value="/trans/parseAEISOCode.do?rawType=TO%20SP%20SWITCH%20CONFIRM&seqId=${entity.seqId}&maskCardNo=${entity.maskCardNo}&edcSpec=${entity.edcSpec}"/>',{width:1040, height:'480',title: 'ISO'});return false;">ISO</button></c:if></td>
						</tr>
					</c:if>
									
					<tr class="oddRow">
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
					<th>卡號</th>
					<th>金融機構名稱</th>
					<th rowspan="2">商店代碼</th>
					<th rowspan="2">端末機代碼</th>
					<th rowspan="2">金額</th>
					<th rowspan="2">授權碼</th>
					<th>交易日期</th>
					<th>交易時間</th>
					<th>交易類別</th>
					<th>批號</th>
					<th>收單行</th>
					<th>回應碼</th>
					<th rowspan="2">狀況</th>
					<th rowspan="2"><u
						onmouseover="addToolTip(this, '代行', 'tooltipStandIn', true);"
						style="color: blue;">代行</u></th>
					<th><a href="#"
						onclick="showIFrameDialog('<c:url value="/trans/parseAEFixedCode.do?fixedCodeType=MTI"/>', {width:1080, height:'480',title: 'MTI'});return false;"
						style="text-decoration: underline; color: blue;">B24<br />MTI
					</a></th>
				</tr>
				<tr>
					<th>有效期</th>
					<th>卡別</th>
					<th>分期紅利</th>
					<th><u
						onmouseover="addToolTip(this, '分期 交易明細', 'tooltipIC', true);"
						style="color: blue;">IC</u></th>
					<th><u
						onmouseover="addToolTip(this, '驗證結果', 'tooltipCrdVefyFlg', true);"
						style="color: blue;">驗證結果</u></th>
					<th>序號</th>
					<th>銀聯卡<br />Hot key
					</th>
					<th><u
						onmouseover="addToolTip(this, 'POS Entry Mode', 'tooltipPOSEntryMode', true);"
						style="color: blue;">Entry Mode</u></th>
					<th>EDC<br />MTI
					</th>
				</tr>
			</thead>
			<tbody>
				<tr class="<edstw:rowStyle value='0'/>">
					<td class="alignCenter"><c:if
							test="${entity.showCardIso == 'Y'}">
							<c:out value="${entity.cardNumMask}" />
						</c:if> <c:if test="${entity.showCardIso != 'Y'}">
							<c:out value="${entity.cardNumMask}" />
						</c:if></td>
					<td class="alignCenter"><c:choose>
                                <c:when test="${entity.mtic eq '05' || entity.mtic eq '08'}">
                                </c:when>
                                <c:otherwise>
                                    <c:out value="${paramBean.bankNameMap[entity.bankId]}" />
                                </c:otherwise>
                            </c:choose></td>
					<td class="alignCenter" rowspan="2"><u
						onmouseover="addToolTip(this, '特店名稱', '<c:out value="${entity.merchantChinName}"/>');"
						style="color: blue;"><c:out value="${entity.merchantId}" /></u></td>
					<td class="alignCenter" rowspan="2"><c:out
							value="${entity.termId}" /></td>
					<td class="alignRight" rowspan="2"><edstw:formatNumber
							value="${entity.tranAmount}" pattern="#,##0.###" /></td>
					<td class="alignCenter" rowspan="2"><c:if
							test="${entity.approveCodeName == null}">
							<c:out value="${entity.approveCode}" />
						</c:if> <c:if test="${entity.approveCodeName != null}">
							<u
								onmouseover="addToolTip(this, '授權碼', '<c:out value="${entity.approveCodeName}"/>');"
								style="color: blue;"><c:out value="${entity.approveCode}" /></u>
						</c:if></td>
					<td class="alignCenter"><c:out value="${entity.tranYear}" />
						<c:out value="${entity.tranDate}" /></td>
					<td class="alignCenter"><c:out value="${entity.tranTime}" /></td>
					<td class="alignCenter"><c:out
							value="${paramBean.transTypeNameMap[entity.tranType]}" /></td>
					<td class="alignCenter"><c:out value="${entity.batchNum}" /></td>
					<td class="alignCenter"><c:out
							value="${paramBean.bankNameMap[entity.acqId]}" /> <%--聯信中心--%></td>
					<td class="alignCenter"><c:out
							value="${paramBean.responseCodeNameMap[entity.respCode]}" /></td>
					<td class="alignCenter" rowspan="2"><c:out
							value="${entity.mtiStatus}" /></td>
					<td class="alignCenter" rowspan="2"><c:out
							value="${entity.standInFlag}" /></td>
					<td class="alignCenter"><c:out value="${entity.b24Mti}" /></td>
				</tr>

				<tr class="<edstw:rowStyle value='1'/>">
					<td class="alignCenter"><c:out value="${entity.expDate}" /></td>
					<td class="alignCenter"><c:out
							value="${paramBean.cardNameMap[entity.cardType]}" /></td>
					<td class="alignCenter"><c:if
							test="${entity.payTypeInd eq '1' || entity.payTypeInd eq '2'}">
							<u
								onmouseover="addToolTip(this, '紅利', '紅利扣抵點數：<edstw:formatNumber
                            value="${entity.redeemPoint}" pattern="#,##0"/>\n紅利剩餘點數：<edstw:formatNumber
                            value="${entity.balancePoint}" pattern="#,##0"/>\n現金繳款金額：<edstw:formatNumber
                            value="${entity.payAmount}" pattern="#,##0.###"/>');"
								style="color: blue;">紅利</u>
						</c:if> <c:if
							test="${entity.payTypeInd eq 'I' || entity.payTypeInd eq 'E'}">
							<u
								onmouseover="addToolTip(this, '分期', '期數：<c:out
                            value="${entity.installmentPeriod}"/>期\n首期金額：<edstw:formatNumber
                            value="${entity.downPayment}" pattern="#,##0.###"/>\n每期金額：<edstw:formatNumber
                            value="${entity.installmentPay}" pattern="#,##0.###"/>\n手續費:<edstw:formatNumber
                            value="${entity.formalityFee}" pattern="#,##0.##"/>');"
								style="color: blue;">分期</u>
						</c:if></td>
					<td class="alignCenter"><c:out value="${entity.icTran}" /></td>
					<td class="alignCenter"><c:out value="${entity.cardVeryFlag}" /></td>
					<td class="alignCenter"><c:out value="${entity.seqNum}" /></td>
					<td class="alignCenter"><c:if
							test="${entity.cupHotKeyInd eq '1'}">Y</c:if></td>
					<td class="alignCenter">
					<c:if test="${entity.cardType == 'S' }">
					<c:if test="${fn:length(entity.edcPosEntryMode) >= 1}"> 
						<c:out value="${fn:substring(entity.edcPosEntryMode,1,-1)}" />
					</c:if>
					<c:if test="${fn:length(entity.edcPosEntryMode) < 1}"> 
						<c:out value="${entity.edcPosEntryMode}"/>
					</c:if>
					</c:if>
					<c:if test="${entity.cardType != 'S' }">
						<c:out value="${entity.b24PosEntryMode}"/>
					</c:if>
					</td>
					<td class="alignCenter"><c:out value="${entity.edcMti}" /></td>
			</tbody>
		</table>
	</div>
	<br />
	<div id='criteriaBlock' class='foldingBlock'
		style="text-align: center; width: 100%;">
		General Transaction Data

		<table border="0" cellpadding="4" cellspacing="1" class="listTable"
			align="center">
			<tr class="oddRow">
				<td class="optional" width="15%">LOG Date</td>
				<td width="18%"><c:out value="${entity.logDate}" /></td>
				<td class="optional" width="15%">LOG Time</td>
				<td width="18%"><c:out value="${entity.logTime}" /></td>
				<td class="optional" width="15%">System Trace Number</td>
				<td width="18%"><c:out value="${entity.sysTraceNum}" /></td>
			</tr>
			<tr class="evenRow">
				<td class="optional"><a href="#"
					onclick="showIFrameDialog('<c:url value="/trans/parseAEFixedCode.do?fixedCodeType=ProcessingCode"/>',{width:1000, height:'400',title: 'Processing Code'});return false;"
					style="text-decoration: underline; color: blue;">EDC Processing
						Code</a></td>
				<td width="18%"><c:out value="${entity.edcProcCode}" /></td>
				<td class="optional"><a href="#"
					onclick="showIFrameDialog('<c:url value="/trans/parseAEFixedCode.do?fixedCodeType=ProcessingCode"/>',{width:1000, height:'400',title: 'B24 Processing Code'});return false;"
					style="text-decoration: underline; color: blue;"><c:if test="${'S' ne entity.cardType}">B24</c:if><c:if test="${'S' eq entity.cardType}">FISC</c:if> Processing
						Code</a></td>
				<td width="18%"><c:out value="${entity.b24ProcCode}" /></td>
				<td class="optional">NII</td>
				<td width="18%"><c:out value="${entity.nii}" /></td>
			</tr>
			<tr class="oddRow">
				<td class="optional"><a href="#"
					onclick="showIFrameDialog('<c:url value="/trans/parseAEFixedCode.do?fixedCodeType=EDCPOSEntryMode"/>',{width:1000, height:'400',title: 'EDC POS Entry Mode'});return false;"
					style="text-decoration: underline; color: blue;">EDC POS Entry
						Mode</a></td>
				<td width="18%"><c:out value="${entity.edcPosEntryMode}" /></td>
				<td class="optional">Settlement flag</td>
				<td width="18%"><c:out value="${entity.settleFlag}" /></td>
				<td class="optional">Responder</td>
				<td width="18%"><c:out value="${entity.responder}" /></td>
			</tr>
			<tr class="evenRow">
				<td class="optional">EDC POS Condition Code</td>
				<td width="18%"><c:out value="${entity.posCondCode}" /></td>
				<td class="optional">
					<c:if test="${'S' ne entity.cardType}">B24 POS Condition Code</c:if>
					<c:if test="${'S' eq entity.cardType}"><a href="#"
					onclick="showIFrameDialog('<c:url value="/trans/parseAEFixedCode.do?fixedCodeType=FiscConditionCode"/>',{width:1000, height:'400',title: 'FISC POS Condition Code'});return false;"
					style="text-decoration: underline; color: blue;">FISC POS Condition Code</a></c:if>
				</td>
				<td width="18%"><c:out value="${entity.b24PosCondCode}" /></td>
				<td class="optional">EDC Spec.</td>
				<td width="18%"><c:out value="${entity.edcSpec}" /></td>
			</tr>
			<tr class="oddRow">
				<td class="optional">Service Code</td>
				<td width="18%"><c:out value="${entity.serviceCode}" /></td>
				<td class="optional">EDC RRN</td>
				<td width="18%"><c:out value="${entity.edcRrn}" /></td>
				<td class="optional">Auth With PIN</td>
				<td width="18%"><c:out value="${entity.authWithPin}" /></td>
			</tr>
			<tr class="evenRow">
				<td class="optional">Additional Amount</td>
				<td width="18%"><edstw:formatNumber value="${entity.addAmount}"
						pattern="#,##0.###" /></td>
				<td class="optional">小費交易原始金額</td>
				<td width="18%"><edstw:formatNumber
						value="${entity.tipsOrigAmount}" pattern="#,##0.###" /></td>
				<td class="optional">郵遞區號</td>
				<td width="18%"><c:out value="${entity.postalCode}" /></td>
			</tr>
			<tr class="oddRow">
				<td class="optional">CUP Trace Number</td>
				<td width="18%"><c:out value="${entity.cupTraceNum}" /></td>
				<td class="optional">CUP Transaction Date</td>
				<td width="18%"><c:out value="${entity.cupTranDate}" /></td>
				<td class="optional">CUP Transaction Time</td>
				<td width="18%"><c:out value="${entity.cupTranTime}" /></td>
			</tr>
			<tr class="evenRow">
				<td class="optional">CUP RRN</td>
				<td width="18%"><c:out value="${entity.cupRrn}" /></td>
				<td class="optional">CUP Settlement date</td>
				<td width="18%"><c:out value="${entity.cupSettleDate}" /></td>
				<td class="optional"><c:if test="${'S' ne entity.cardType}">B24</c:if><c:if test="${'S' eq entity.cardType}">FISC</c:if>收單行代號</td>
				<td width="18%"><c:out value="${entity.b24AcqId}" /></td>
			</tr>
			<tr class="oddRow">
				<td class="optional">MO/TO Flag</td>
				<td width="18%"><c:out value="${entity.moToFlag}" /></td>
				<td class="optional">有人或無人端末機</td>
				<td width="18%"><c:out
						value="${transLogForm.termAttendedIndLabel}" /></td>
				<td class="optional">Transaction ID</td>
				<td width="18%"><c:out value="${entity.transactionId}" /></td>
			</tr>
			<tr class="evenRow">
				<td class="optional"><a href="#"
					onclick="showIFrameDialog('<c:url value="/trans/parseAEFixedCode.do?fixedCodeType=CardholderPresentIndicator"/>',{width:1000, height:'320',title: 'Cardholder Present Indicator'});return false;"
					style="text-decoration: underline; color: blue;">Cardholder
						Present Indicator</a></td>
				<td width="18%"><c:out value="${entity.chPresentInd}" /></td>
				<td class="optional"><a href="#"
					onclick="showIFrameDialog('<c:url value="/trans/parseAEFixedCode.do?fixedCodeType=CardPresentIndicator"/>',{width:1000, height:'400',title: 'Card Present Indicator'});return false;"
					style="text-decoration: underline; color: blue;">Card Present
						Indicator</a></td>
				<td width="18%"><c:out value="${entity.cardPresentInd}" /></td>
				<td class="optional"><a href="#"
					onclick="showIFrameDialog('<c:url value="/trans/parseAEFixedCode.do?fixedCodeType=TransactionStatusIndicator"/>',{width:1000, height:'400',title: 'Transaction Status Indicator'});return false;"
					style="text-decoration: underline; color: blue;">Transaction
						Status Indicator</a></td>
				<td width="18%"><c:out value="${entity.tranStatusInd}" /></td>
			</tr>
			<tr class="oddRow">
				<td class="optional"><a href="#"
					onclick="showIFrameDialog('<c:url value="/trans/parseAEFixedCode.do?fixedCodeType=TransactionSecurityIndicator"/>',{width:1000, height:'400',title: 'Transaction Security Indicator'});return false;"
					style="text-decoration: underline; color: blue;">Transaction
						Security Indicator</a></td>
				<td width="18%"><c:out value="${entity.tranSecInd}" /></td>
				<td class="optional"><a href="#"
					onclick="showIFrameDialog('<c:url value="/trans/parseAEFixedCode.do?fixedCodeType=CATIndicator"/>',{width:1000, height:'400',title: 'CAT Indicator'});return false;"
					style="text-decoration: underline; color: blue;">CAT Indicator</a></td>
				<td width="18%"><c:out value="${entity.chActTermInd}" /></td>
				<td class="optional">FISC Resp. Reason code</td>
				<td width="18%">		
					<c:if test="${fn:length(entity.fiscRespReasonCode) >= 1	}">
						<c:if test="${fn:contains(entity.fiscRespReasonCode, 'I')}"> 
							<c:set var="respRole" value="發卡機構回覆"/>
						</c:if>
						<c:if test="${fn:contains(entity.fiscRespReasonCode, 'A')}"> 
							<c:set var="respRole" value="收單機構回覆"/>
						</c:if>
					<u onmouseover="addToolTip(this, 'Reason code', '<c:out value="${respRole }" />:<c:out value="${paramBean.logReasonCodeNameMap[fn:substring(entity.fiscRespReasonCode,1,-1)]}"/>');" style="color: blue;">
						<c:out value="${entity.fiscRespReasonCode }" />
					</u>
					</c:if>
				</td>
			</tr>
			<tr class="evenRow">
				<td class="optional"><a href="#"
					onclick="showIFrameDialog('<c:url value="/trans/parseAEFixedCode.do?fixedCodeType=TerminalInputCapabilityIndicator"/>',{width:1000, height:'400',title: 'Terminal Input Capability Indicator'});return false;"
					style="text-decoration: underline; color: blue;">Terminal Input
						Capability Indicator</a></td>
				<td width="18%"><c:out value="${entity.termCapInd}" /></td>
				<td class="optional">ATS主機</td>
				<td width="18%"><c:out value="${entity.atsHost}" /></td>
				<td class="optional"><c:if test="${'S' ne entity.cardType}">BICI </c:if><c:if test="${'S' eq entity.cardType}">SPSW </c:if>Port</td>
				<td width="18%"><c:out value="${entity.biciPort}" /></td>
			</tr>
			<tr class="oddRow">
				<td class="optional"><c:if test="${'S' ne entity.cardType}">ATS RRN</c:if><c:if test="${'S' eq entity.cardType}">FISC RRN</c:if></td>
				<td width="18%"><c:out value="${entity.atsRrn}" /></td>
				<td class="optional">FISC Resp. Code</td>
				<td width="18%">
				<u onmouseover="addToolTip(this, 'Fisc Resp. code', '<c:out value="${paramBean.logRespCodeNameMap[entity.fiscRespCode]}"/>');" style="color: blue;">
						<c:out value="${entity.fiscRespCode}" />
					</u>
				
				</td>
				<td class="optional">是否免簽名 </td>
				<td width="18%"><c:out value="${entity.quickPayFlag}" /></td>
				
			</tr>
			<tr class="evenRow">
				<td class="optional">TO HSM MAC Date Time</td>
				<td width="18%"><c:out value="${entity.toHsmMacDate}" />
					<c:out value="${entity.toHsmMacTime}" /></td>
				<td class="optional">From HSM MAC Date Time</td>
				<td width="18%"><c:out value="${entity.fromHsmMacDate}" />
					<c:out value="${entity.fromHsmMacTime}" /></td>
				<td class="optional">連線主機名稱</td>
				<td width="18%"><c:out value="${entity.destinationId}" /></td>
			</tr>
			<tr class="oddRow">
				<td class="optional">TO HSM Key Date Time</td>
				<td width="18%"><c:out value="${entity.toHsmKeyDate}" />
					<c:out value="${entity.toHsmKeyTime}" /></td>
				<td class="optional">From HSM Key Date Time</td>
				<td width="18%"><c:out value="${entity.fromHsmKeyDate}" />
					<c:out value="${entity.fromHsmKeyTime}" /></td>
				<td class="optional">TMK Index</td>
				<td width="18%"><c:out value="${entity.tmkIndex}" /></td>
			</tr>
			<tr class="evenRow">
				<td class="optional">優惠數</td>
				<td width="18%"><c:out value="${entity.asmCouponNumber}" /></td>
				<td class="optional"><a href="#"
					onclick="showIFrameDialog('<c:url value="/trans/parseAEFixedCode.do?fixedCodeType=ExchangeMode"/>',{width:1000, height:'400',title: '優惠兌換方式'});return false;"
					style="text-decoration: underline; color: blue;">優惠兌換方式</a></td>
				<td width="18%"><c:out value="${entity.asmRedeemMethod}" /></td>
				<td class="optional">DCC轉台幣Flag</td>
				<td width="18%"><c:out value="${entity.dccToNtdFlag}" /></td>
			</tr>
			<tr class="oddRow">
				<td class="optional">On Us Role</td>
				<td width="18%">
				<c:if test="${'COMM' eq entity.onUsRole}">一般特店</c:if>
				<c:if test="${'GOV' eq entity.onUsRole}">公務特店</c:if>
				</td>
				<td class="optional"></td>
				<td width="18%"></td>
				<td class="optional"></td>
				<td width="18%"></td>
			</tr>
		</table>

	</div>
	<br />

</body>
</html>