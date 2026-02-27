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
<c:set var="nameMap" value="${paramBean.emvChipDataNameMap}" />

<body class="ieVersion">
	<div id="mainContentBlock" style="text-align: center; width: 100%;">
		<div id='criteriaBlock' class='foldingBlock' style="width: 90%">
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
							onclick="showIFrameDialog('<c:url value="/trans/parseFixedCode.do?fixedCodeType=MTI"/>', {width:1080, height:'480',title: 'MTI'});return false;"
							style="text-decoration: underline; color: blue;">B24<br />MTI
						</a></th>
					</tr>
					<tr>
						<th>有效期</th>
						<th>卡別</th>
						<th>分期/紅利/小額</th>
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
					<td class="alignCenter">
					<c:if test="${entity.cardType eq 'EZ' || entity.cardType eq 'IC' || entity.cardType eq 'IP' || entity.cardType eq 'HC'}">
						<c:out value="${entity.cardNum}"/>
					</c:if>
					<c:if test="${entity.cardType ne 'EZ' && entity.cardType ne 'IC' && entity.cardType ne 'IP' && entity.cardType ne 'HC'}">
						<c:out value="${entity.cardNumMask}"/>
					</c:if>	
					
					</td>
					<td class="alignCenter">
					<c:if test="${entity.cardType ne 'EZ' && entity.cardType ne 'IC' && entity.cardType ne 'IP' && entity.cardType ne 'HC'}">
						<c:out value="${paramBean.bankNameMap[entity.bankId]}" />
					</c:if>
					</td>
					<td class="alignCenter" rowspan="2"><u
						onmouseover="addToolTip(this, '特店名稱', '<c:out value="${entity.merchantChinName}"/>');"
						style="color: blue;"><c:out value="${entity.merchantId}" /></u></td>
					<td class="alignCenter" rowspan="2"><c:out
							value="${entity.termId}" /></td>
					<td class="alignRight" rowspan="2">
					 <c:if test="${entity.upPreferAmt == null || entity.upPreferAmt eq '' || entity.upPreferAmt eq '0'}">
						<c:if test="${entity.cardType eq 'EZ' || entity.cardType eq 'IC' || entity.cardType eq 'IP' || entity.cardType eq 'HC'}">
						<u onmouseover="addToolTip(this, '餘額', '交易前金額：<edstw:formatNumber
								value="${entity.tranAmountBf}" pattern="#,##0"/>\n交易後餘額：<edstw:formatNumber
								value="${entity.tranAmountAf}" pattern="#,##0"/>');"
								style="color: blue;">
							<edstw:formatNumber value="${entity.tranAmount}" pattern="#,##0.###"/>
						</u>
						</c:if>
						<c:if test="${entity.cardType ne 'EZ' && entity.cardType ne 'IC' && entity.cardType ne 'IP' && entity.cardType ne 'HC'}">
							<edstw:formatNumber value="${entity.tranAmount}" pattern="#,##0.###"/>
						</c:if>
					 </c:if>
					<%-- UPLAN-M2018187 --%>
					<c:if test="${entity.upPreferAmt != null && entity.upPreferAmt ne '' && entity.upPreferAmt ne '0'}">
						<u onmouseover="addToolTip(this, '---優惠內容----', '優惠前金額：<edstw:formatNumber
								value="${entity.tranAmount}" pattern="#,##0.00"/>\n優惠金額　：<edstw:formatNumber
								value="${entity.upPreferAmtD}" pattern="#,##0.00"/>\n優惠後金額：<edstw:formatNumber
								value="${entity.upDiscountAmtD}" pattern="#,##0.00"/>');"
								style="color: blue;">
							<edstw:formatNumber value="${entity.tranAmount}" pattern="#,##0.###"/>
						</u>
					</c:if>
					</td>
					<td class="alignCenter" rowspan="2">
						<c:if test="${entity.approveCodeName == null && (entity.approveCode != null && entity.approveCode != '' )}">
							[<c:out value="${entity.approveCode}" />]
						</c:if>
						<c:if test="${entity.approveCodeName == null && (entity.approveCode == null || entity.approveCode == '' )}">
							無
						</c:if>
						<c:if test="${entity.approveCodeName != null}">
							<u onmouseover="addToolTip(this, '授權碼', '<c:out value="${entity.approveCodeName}"/>');"
								style="color: blue;"><c:out value="${entity.approveCode}" /></u>
						</c:if></td>
					<td class="alignCenter"><c:out value="${entity.tranYear}" /><c:out value="${entity.tranDate}" /></td>
					<td class="alignCenter"><c:out value="${entity.tranTime}" /></td>
					<td class="alignCenter">
					<c:if test="${entity.cardType eq 'EZ' || entity.cardType eq 'IC' || entity.cardType eq 'IP' || entity.cardType eq 'HC'}">
						<c:out value="${entity.tksTranTypeName}" />
					</c:if>
					<c:if test="${entity.cardType ne 'EZ' && entity.cardType ne 'IC' && entity.cardType ne 'IP' && entity.cardType ne 'HC'}">
						<c:out value="${paramBean.transTypeNameMap[entity.tranType]}" />
					</c:if>
					<c:if test="${entity.atsReversal eq 'Y'}">
						<br/><span style="color:red">人工沖銷</span>
					</c:if>
					<%-- UPLAN-M2018187  --%>
					<c:if test="${entity.uplanFlag eq 'Y'}">
					(優惠)
					</c:if>
					</td>
					<td class="alignCenter"><c:out value="${entity.batchNum}" /></td>
					<td class="alignCenter">聯卡中心</td>
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
					<td class="alignCenter">
					<c:if test="${ entity.edcProcCode ne '519200' && entity.edcProcCode ne '519600' }" >
						<c:out value="${paramBean.cardNameMap[entity.cardType]}"/>
					</c:if>
					<c:if test="${ entity.edcProcCode eq '519200' || entity.edcProcCode eq '519600' }" >
						電子票證
					</c:if>
					<c:if test="${ paramBean.cardNameMap[entity.cardType] eq null }" >
						<c:out value="${entity.cardType }" />
					</c:if>
					</td>
					<td class="alignCenter"><c:if
							test="${entity.payTypeInd eq '1' || entity.payTypeInd eq '2'}">
							<u
								onmouseover="addToolTip(this, '紅利', '紅利扣抵點數：<edstw:formatNumber
                            value="${entity.redeemPoint}" pattern="#,##0"/>\n紅利剩餘點數：<edstw:formatNumber
                            value="${entity.balancePoint}" pattern="#,##0"/>\n現金繳款金額：<edstw:formatNumber
                            value="${entity.payAmount}" pattern="#,##0.###"/>');"
								style="color: blue;"><c:if test="${entity.mpasFlag eq 'Y'}">小額</c:if>紅利</u>
						</c:if> <c:if
							test="${entity.payTypeInd eq 'I' || entity.payTypeInd eq 'E'}">
							<u
								onmouseover="addToolTip(this, '分期', '期數：<c:out
                            value="${entity.installmentPeriod}"/>期\n首期金額：<edstw:formatNumber
                            value="${entity.downPayment}" pattern="#,##0.###"/>\n每期金額：<edstw:formatNumber
                            value="${entity.installmentPay}" pattern="#,##0.###"/>\n手續費:<edstw:formatNumber
                            value="${entity.formalityFee}" pattern="#,##0.##"/>');"
								style="color: blue;"><c:if test="${entity.mpasFlag eq 'Y'}">小額</c:if>分期</u>
						</c:if>
						<c:if test="${entity.payTypeInd ne '1' && entity.payTypeInd ne '2' && entity.payTypeInd ne 'I' && entity.payTypeInd ne 'E' && entity.mpasFlag eq 'Y'}">
						小額
						</c:if>
						</td>
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
		<div id='criteriaBlock' class='foldingBlock' style="width: 90%">

			<table border="0" cellpadding="2" cellspacing="1" class="listTable"
				align="center">
				<thead>
					<tr>
						<th width="20%">Tag</th>
						<th width="60%">Tag說明</th>
						<th width="40%">Value</th>
					</tr>
				</thead>
				
				<c:if test="${entity.cardType != 'S' }">
				<tbody>
					<tr class="oddRow">
						<td align="center">5F2A</td>
						<td align="center"><c:out value="${nameMap['5F2A']}" /> <%--Transaction Currency Code--%></td>
						<td align="center"><c:out
								value="${entity.tag5f2aTranCurrCode}" /></td>
					</tr>
					<tr class="evenRow">
						<td align="center">5F34</td>
						<td align="center"><c:out value="${nameMap['5F34']}" /> <%--Application PAN Sequence Number--%></td>
						<td align="center"><c:out value="${entity.tag5f34PanSeqNum}" /></td>
					</tr>
					<!-- //M2017080 -->
					<tr class="oddRow">
						<td align="center">71</td>
						<td align="center"><c:out value="${nameMap['71']}" /> <%--Application Interchange Profile--%></td>
						<td align="center"><c:out value="${entity.tag71}" /></td>
					</tr>
					<tr class="evenRow">
						<td align="center">72</td>
						<td align="center"><c:out value="${nameMap['72']}" /> <%--Dedicated file name(AID)--%></td>
						<td align="center"><c:out value="${entity.tag72}" /></td>
					</tr>
					<tr class="oddRow">
						<td align="center">82</td>
						<td align="center"><c:out value="${nameMap['82']}" /> <%--Application Interchange Profile--%></td>
						<td align="center"><a href="#"
							onclick="showIFrameDialog('<c:url value="/trans/parseICCode.do?tagName=82&cardType=&chipType=&code=${entity.tag82Aip}"/>',{width:1040, height:'480',title: 'AIP'});return false;"
							style="text-decoration: underline; color: blue;"><c:out
									value="${entity.tag82Aip}" /></a></td>
					</tr>
					<tr class="evenRow">
						<td align="center">84</td>
						<td align="center"><c:out value="${nameMap['84']}" /> <%--Dedicated file name(AID)--%></td>
						<td align="center"><c:out value="${entity.tag84DfName}" /></td>
					</tr>
					<tr class="oddRow">
						<td align="center">91</td>
						<td align="center"><c:out value="${nameMap['91_1']}" /> <%--Issuer Authentication Data--ARPC--%></td>
						<td align="center"><c:out value="${entity.tag91Arpc}" /></td>
					</tr>
					<tr class="evenRow">
						<td align="center">91</td>
						<td align="center"><c:out value="${nameMap['91_2']}" /> <%--Issuer Authentication Data--ARC--%></td>
						<td align="center"><c:out value="${entity.tag91Arc}" /></td>
					</tr>
					<tr class="oddRow">
						<td align="center">95</td>
						<td align="center"><c:out value="${nameMap['95']}" /> <%--Terminal Verification Results--%></td>
						<td align="center"><a href="#"
							onclick="showIFrameDialog('<c:url value="/trans/parseICCode.do?tagName=95&cardType=&chipType=&code=${entity.tag95Tvr}"/>',{width:1040, height:'480',title: 'Terminal Verification Results'});return false;"
							style="text-decoration: underline; color: blue;"><c:out
									value="${entity.tag95Tvr}" /></a></td>
					</tr>
					<tr class="evenRow">
						<td align="center">9A</td>
						<td align="center"><c:out value="${nameMap['9A']}" /> <%--Transaction Date--%></td>
						<td align="center"><c:out value="${entity.tag9aTranDate}" /></td>
					</tr>
					<tr class="oddRow">
						<td align="center">9B</td>
						<td align="center"><c:out value="${nameMap['9B']}" /> <%--Transaction Status Information--%></td>
						<td align="center"><a href="#"
							onclick="showIFrameDialog('<c:url value="/trans/parseICCode.do?tagName=9B&cardType=&chipType=&code=${entity.tag9bTsi}"/>',{width:1040, height:'480',title: 'Transaction Status Information'});return false;"
							style="text-decoration: underline; color: blue;"><c:out
									value="${entity.tag9bTsi}" /></a></td>
					</tr>
					<tr class="evenRow">
						<td align="center">9C</td>
						<td align="center"><c:out value="${nameMap['9C']}" /> <%--Transaction Type--%></td>
						<td align="center"><c:out value="${entity.tag9cTranType}" /></td>
					</tr>
					<tr class="oddRow">
						<td align="center">9F02</td>
						<td align="center"><c:out value="${nameMap['9F02']}" /> <%--Amount, Authorized--%></td>
						<td align="center"><edstw:formatNumber
								value="${entity.tag9f02AmountAuth}" pattern="#,##0.###" /></td>
					</tr>
					<tr class="evenRow">
						<td align="center">9F03</td>
						<td align="center"><c:out value="${nameMap['9F03']}" /> <%--Amount, Other--%></td>
						<td align="center"><edstw:formatNumber
								value="${entity.tag9f03AmountOther}" pattern="#,##0.###" /></td>
					</tr>
					<tr class="oddRow">
						<td align="center">9F08</td>
						<td align="center"><c:out value="${nameMap['9F08']}" /> <%--Application version number in terminal--%></td>
						<td align="center"><c:out value="${entity.tag9f08CardAvn}" /></td>
					</tr>
					<tr class="evenRow">
						<td align="center">9F09</td>
						<td align="center"><c:out value="${nameMap['9F09']}" /> <%--Application version number in terminal--%></td>
						<td align="center"><c:out value="${entity.tag9f09TermAvn}" /></td>
					</tr>
					<tr class="oddRow">
						<td align="center">9F10</td>
						<td align="center"><c:out value="${nameMap['9F10']}" /> <%--Issuer Application Data--%></td>
						<td align="center"><a href="#"
							onclick="showIFrameDialog('<c:url value="/trans/parseICCode.do?tagName=9F10&cardType=${entity.cardType}&chipType=${entity.chipType9F10}&code=${entity.tag9f10Iad}"/>',{width:1040, height:'480',title: 'Issuer Application Data'});return false;"
							style="text-decoration: underline; color: blue;"><c:out
									value="${entity.tag9f10Iad}" /></a></td>
					</tr>
					<tr class="evenRow">
						<td align="center">9F1A</td>
						<td align="center"><c:out value="${nameMap['9F1A']}" /> <%--Terminal Country Code--%></td>
						<td align="center"><c:out
								value="${entity.tag9f1aTermCountryCode}" /></td>
					</tr>
					<tr class="oddRow">
						<td align="center">9F1E</td>
						<td align="center"><c:out value="${nameMap['9F1E']}" /> <%--Interface Device (IFD) Serial Number--%></td>
						<td align="center"><c:out
								value="${entity.tag9f1eIfdSerialNum}" /></td>
					</tr>
					<tr class="evenRow">
						<td align="center">9F26</td>
						<td align="center"><c:out value="${nameMap['9F26']}" /> <%--Application Cryptogram--%></td>
						<td align="center"><c:out value="${entity.tag9f26Ac}" /></td>
					</tr>
					<tr class="oddRow">
						<td align="center">9F27</td>
						<td align="center"><c:out value="${nameMap['9F27']}" /> <%--Cryptogram Information Data--%></td>
						<td align="center"><a href="#"
							onclick="showIFrameDialog('<c:url value="/trans/parseICCode.do?tagName=9F27&cardType=&chipType=&code=${entity.tag9f27Cid}"/>',{width:1040, height:'480',title: 'Cryptogram Information Data'});return false;"
							style="text-decoration: underline; color: blue;"><c:out
									value="${entity.tag9f27Cid}" /></a></td>
					</tr>
					<tr class="evenRow">
						<td align="center">9F33</td>
						<td align="center"><c:out value="${nameMap['9F33']}" /> <%--Terminal Capability Profile--%></td>
						<td align="center"><a href="#"
							onclick="showIFrameDialog('<c:url value="/trans/parseICCode.do?tagName=9F33&cardType=&chipType=&code=${entity.tag9f33TermCap}"/>',{width:1040, height:'480',title: 'Terminal Capability Profile'});return false;"
							style="text-decoration: underline; color: blue;"><c:out
									value="${entity.tag9f33TermCap}" /></a></td>
					</tr>
					<tr class="oddRow">
						<td align="center">9F34</td>
						<td align="center"><c:out value="${nameMap['9F34']}" /> <%--Cardholder Verification Method Results--%></td>
						<td align="center"><a href="#"
							onclick="showIFrameDialog('<c:url value="/trans/parseICCode.do?tagName=9F34&cardType=&chipType=&code=${entity.tag9f34CvnResult}"/>',{width:1040, height:'480',title: 'Cardholder Verification Method Results'});return false;"
							style="text-decoration: underline; color: blue;"><c:out
									value="${entity.tag9f34CvnResult}" /></a></td>
					</tr>
					<tr class="evenRow">
						<td align="center">9F35</td>
						<td align="center"><c:out value="${nameMap['9F35']}" /> <%--Terminal Type--%></td>
						<td align="center"><c:out value="${entity.tag9f35TermType}" /></td>
					</tr>
					<tr class="oddRow">
						<td align="center">9F36</td>
						<td align="center"><c:out value="${nameMap['9F36']}" /> <%--Application Transaction Counter--%></td>
						<td align="center"><c:out value="${entity.tag9f36Atc}" /></td>
					</tr>
					<tr class="evenRow">
						<td align="center">9F37</td>
						<td align="center"><c:out value="${nameMap['9F37']}" /> <%--Unpredictable Number--%></td>
						<td align="center"><c:out value="${entity.tag9f37Un}" /></td>
					</tr>
					<tr class="oddRow">
						<td align="center">9F41</td>
						<td align="center"><c:out value="${nameMap['9F41']}" /> <%--Transaction sequence counter--%></td>
						<td align="center"><c:out value="${entity.tag9f41TranSeqNum}" /></td>
					</tr>
					<tr class="evenRow">
						<td align="center">9F5B</td>
						<td align="center"><c:out value="${nameMap['9F5B']}" /> <%--Issuer Script Results--%></td>
						<td align="center"><a href="#"
							onclick="showIFrameDialog('<c:url value="/trans/parseICCode.do?tagName=9F5B&cardType=&chipType=&code=${entity.tag9f5bIsr}"/>',{width:1040, height:'480',title: 'ISR'});return false;"
							style="text-decoration: underline; color: blue;"><c:out
									value="${entity.tag9f5bIsr}" /></a></td>
					</tr>
					<tr class="oddRow"><%-- UPLAN-M2018187--%>
						<td align="center">9F60</td>
						<td align="center"><c:out value="${nameMap['9F60']}" /> //Coupon Information</td>
						<td align="center"><c:out
								value="${entity.tag9f60CouponInfo}" /></td>
					</tr> 
					<tr class="evenRow">
						<td align="center">9F63</td>
						<td align="center"><c:out value="${nameMap['9F63']}" /> <%--Card Product Label Information(卡產品標識信息)--%></td>
						<td align="center"><a href="#"
							onclick="showIFrameDialog('<c:url value="/trans/parseICCode.do?tagName=9F63&cardType=&chipType=&code=${entity.tag9f63CardProductInfo}"/>',{width:1040, height:'480',title: 'Card Product Label Information'});return false;"
							style="text-decoration: underline; color: blue;"><c:out
									value="${entity.tag9f63CardProductInfo}" /></a></td>
					</tr>
				 	<tr class="oddRow">
						<td align="center">9F6E</td>
						<td align="center"><c:out value="${nameMap['9F6E']}" /></td>
						<td align="center"><a href="#"
							onclick="showIFrameDialog('<c:url value="/trans/parseICCode.do?tagName=9F6E&cardType=&chipType=&code=${entity.tag9f6eFFI}"/>',{width:1040, height:'480',title: 'Form Factor Indicator'});return false;"
							style="text-decoration: underline; color: blue;"><c:out
									value="${entity.tag9f6eFFI}" /></a></td>
					</tr>
					<tr class="evenRow"><%-- M2021165_9F7C Partner Discretionary Data--%>
						<td align="center">9F7C</td>
						<td align="center"><c:out value="${nameMap['9F7C']}" /></td>
						<td align="center"><c:out value="${entity.tag9f7c}" /></td>
					</tr>
					<tr class="oddRow">
						<td align="center">DFED</td>
						<td align="center"><c:out value="${nameMap['DFED']}" /> <%--Chip Condition Code--%></td>
						<td align="center"><c:out value="${entity.tagDfedChipCondCode}" /></td>
					</tr>
					<tr class="evenRow">
						<td align="center">DFEE</td>
						<td align="center"><c:out value="${nameMap['DFEE']}" /> <%--Terminal Entry Capability--%></td>
						<td align="center"><c:out
								value="${entity.tagDfeeTermEntryCap}" /></td>
					</tr>
					<tr class="oddRow">
						<td align="center">DFEF</td>
						<td align="center"><c:out value="${nameMap['DFEF']}" /> <%--Reason Online Code (BASE24 use)--%></td>
						<td align="center"><c:out
								value="${entity.tagDfefRsnOnlineCode}" /></td>
					</tr>
					
				</tbody>
				</c:if>
				<c:if test="${entity.cardType == 'S' }">
				<tbody>
					<tr class="evenRow">
						<td align="center">S1</td>
						<td align="center"><c:out value="${nameMap['S1']}" /> <%-- Smart Pay Serial Number --%></td>
						<td align="center"><c:out value="${entity.tagS1}" /></td>
					</tr>
					<tr class="oddRow">
						<td align="center">S2</td>
						<td align="center"><c:out value="${nameMap['S2']}" /> <%-- Smart Pay TAC --%></td>
						<td align="center"><c:out value="${entity.tagS2}" /></td>
					</tr>
					<tr class="evenRow">
						<td align="center">S3</td>
						<td align="center"><c:out value="${nameMap['S3']}" /> <%-- MCC/BranchBank ID --%></td>
						<td align="center"><c:out value="${entity.tagS3}" /></td>
					</tr>
					<tr class="oddRow">
						<td align="center">S4</td>
						<td align="center"><c:out value="${nameMap['S4']}" /> <%-- Terminal Check Code --%></td>
						<td align="center"><c:out value="${entity.tagS4}" /></td>
					</tr>
					<tr class="evenRow">
						<td align="center">S5</td>
						<td align="center"><c:out value="${nameMap['S5']}" /> <%-- Smart Pay RRN --%></td>
						<td align="center"><c:out value="${entity.tagS5}" /></td>
					</tr>
					<tr class="oddRow">
						<td align="center">S6</td>
						<td align="center"><c:out value="${nameMap['S6']}" /> <%-- Original Transaction Date --%></td>
						<td align="center"><c:out value="${entity.tagS6}" /></td>
					</tr>
					<tr class="evenRow">
						<td align="center">S7</td>
						<td align="center"><c:out value="${nameMap['S7']}" /> <%-- Transaction Date & Time --%></td>
						<td align="center"><c:out value="${entity.tagS7}" /></td>
					</tr>
					</tbody>	
				</c:if>
			</table>
		</div>

	</div>

</body>
</html>