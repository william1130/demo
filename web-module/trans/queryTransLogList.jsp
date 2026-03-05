<%@page pageEncoding="UTF-8"%>
<%-- 此為AJAX查詢結果的頁面, 必須額外設定contentType --%>
<%@include file="/jsp/content_type_ajax.jsp"%>
<%@include file="/jsp/directive.jsp"%>
<jsp:include page="/jsp/messages.jsp"/>
<%-- <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> --%>
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
			<th colspan="16" style="text-align: left;">
				<input type="Button" name="reportButton" value="產生報表" class="button"
					onclick="createReport();">
			</th>
		</tr>
		<tr>
			<th rowspan="2">選取<br>
				<input type="checkbox" id="selAll" onclick="selectAll();"/>
			</th>
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
				style="text-decoration: underline; color: blue;">B24<br />MTI</a>
			</th>
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
			<th>EDC<br />MTI</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="item" items="${resultList}" varStatus="status">
			<tr class="<edstw:rowStyle value='${status.index}'/>">
				<td class="alignCenter" rowspan=2>
					<input type="checkbox" id="row_<c:out value="${status.index}"/>"
						name="seqIds"
						value="<c:out value="${item.seqId}"/>"
						onclick="checkSelectAll(this);"/>	
				</td>
				<td class="alignCenter">
					<c:if test="${item.showCardIso == 'Y'}">
						<c:if test="${item.cardType eq 'EZ' || item.cardType eq 'IC' || item.cardType eq 'IP' || item.cardType eq 'HC'}">
						<a href="#"
							onclick="showIFrameDialog('<c:url value="/trans/queryTransLogISO.do?entity.seqId=${item.seqId}&entity.maskCardNo=${item.maskCardNo}&entity.merchantId=${item.merchantId}&entity.approveCode=${item.approveCode}"/>',{width:1080, height:'520',title: 'ISO內容'});return false;"
							style="text-decoration: underline; color: blue;">
							<c:out value="${item.cardNum}"/></a>
						</c:if>
						<c:if test="${item.cardType ne 'EZ' && item.cardType ne 'IC' && item.cardType ne 'IP' && item.cardType ne 'HC'}">
						<a href="#"
							onclick="showIFrameDialog('<c:url value="/trans/queryTransLogISO.do?entity.seqId=${item.seqId}&entity.maskCardNo=${item.maskCardNo}&entity.merchantId=${item.merchantId}&entity.approveCode=${item.approveCode}"/>',{width:1080, height:'520',title: 'ISO內容'});return false;"
							style="text-decoration: underline; color: blue;">
							<c:out value="${item.cardNumMask}"/></a>
						</c:if>	
					</c:if>
					<c:if test="${item.showCardIso != 'Y'}">
						<c:if test="${item.cardType eq 'EZ' || item.cardType eq 'IC' || item.cardType eq 'IP' || item.cardType eq 'HC'}">
							<c:out value="${item.cardNum}"/>
						</c:if>
						<c:if test="${item.cardType ne 'EZ' && item.cardType ne 'IC' && item.cardType ne 'IP' && item.cardType ne 'HC'}">
							<c:out value="${item.cardNumMask}"/>
						</c:if>	
					</c:if>
				</td>
				<td class="alignCenter">		    
				<c:if test="${item.cardType ne 'EZ' && item.cardType ne 'IC' && item.cardType ne 'IP' && item.cardType ne 'HC'}">
					<c:out value="${paramBean.bankNameMap[item.bankId]}"/>
				</c:if>
				</td>
				<td class="alignCenter" rowspan="2"><u
					onmouseover="addToolTip(this, '特店名稱', '<c:out value="${item.merchantChinName}"/>');"
					style="color: blue;"><c:out value="${item.merchantId}"/></u></td>
				<!-- 20170120 M2016144 電子票證 start -->
				<td class="alignCenter" rowspan="2">
				<c:if test="${item.cardType eq 'EZ'}">
					<u onmouseover="addToolTip(this, '設備交易序號', '<c:out value="${item.tmSerialNum}"/>');" style="color: blue;">
						<c:out value="${item.termId}"/>
					</u>
				</c:if>
				<c:if test="${item.cardType ne 'EZ'}">					
					<c:out value="${item.termId}"/>
				</c:if>
				</td>			
				<td class="alignRight" rowspan="2">
				<c:if test="${item.upPreferAmt == null || item.upPreferAmt eq '' || item.upPreferAmt eq '0'}">
					<c:if test="${item.cardType eq 'EZ' || item.cardType eq 'IC' || item.cardType eq 'IP' || item.cardType eq 'HC'}">
						<u onmouseover="addToolTip(this, '餘額', '交易前金額：<edstw:formatNumber
								value="${item.tranAmountBf}" pattern="#,##0"/>\n交易後餘額：<edstw:formatNumber
								value="${item.tranAmountAf}" pattern="#,##0"/>');"
								style="color: blue;">
							<edstw:formatNumber value="${item.tranAmount}" pattern="#,##0.###"/>
						</u>
					</c:if>
					<c:if test="${item.cardType ne 'EZ' && item.cardType ne 'IC' && item.cardType ne 'IP' && item.cardType ne 'HC'}">
						<edstw:formatNumber value="${item.tranAmount}" pattern="#,##0.###"/>
					</c:if>
				</c:if>
				<%-- UPLAN-M2018187--%>
				<c:if test="${item.upPreferAmt != null && item.upPreferAmt ne '' && item.upPreferAmt ne '0'}">
					<u onmouseover="addToolTip(this, '---優惠內容----', '優惠前金額：<edstw:formatNumber
								value="${item.tranAmount}" pattern="#,##0.00"/>\n優惠金額　：<edstw:formatNumber
								value="${item.upPreferAmtD}" pattern="#,##0.00"/>\n優惠後金額：<edstw:formatNumber
								value="${item.upDiscountAmtD}" pattern="#,##0.00"/>');"
								style="color: blue;">
							<edstw:formatNumber value="${item.tranAmount}" pattern="#,##0.###"/>
						</u>
				</c:if>
				</td>
				<%-- 20170120 M2016144 電子票證 end --%>
				<td class="alignCenter" rowspan="2">
					<c:if test="${item.approveCodeName == null}">
						<c:out value="${item.approveCode}"/>
					</c:if>
					<c:if test="${item.approveCodeName != null}">
						<u onmouseover="addToolTip(this, '授權碼', '<c:out value="${item.approveCodeName}"/>');"
							style="color: blue;"><c:out value="${item.approveCode}"/></u>
					</c:if>
				</td>
				<td class="alignCenter">
					<c:out value="${item.tranYear}"/><c:out value="${item.tranDate}"/>
				</td>
				<td class="alignCenter"><c:out value="${item.tranTime}"/></td>
				<td class="alignCenter">
				<c:if test="${item.cardType ne 'EZ' && item.cardType ne 'IC' && item.cardType ne 'IP' && item.cardType ne 'HC'}">
					<c:out value="${paramBean.transTypeNameMap[item.tranType]}"/>
				</c:if>
				<c:if test="${item.cardType eq 'EZ' || item.cardType eq 'IC' || item.cardType eq 'IP' || item.cardType eq 'HC'}">
					<c:out value="${item.tksTranTypeName}"/>
				</c:if>
				<c:if test="${item.atsReversal eq 'Y'}">
					<br/><span style="color:red">人工沖銷</span>
				</c:if>
				<%-- UPLAN-M2018187 --%>
				<c:if test="${item.uplanFlag eq 'Y'}">
				(優惠)
				</c:if>
				</td>
				<td class="alignCenter"><c:out value="${item.batchNum}"/></td>
				<td class="alignCenter">聯卡中心</td>
				<td class="alignCenter"><c:out value="${paramBean.responseCodeNameMap[item.respCode]}"/></td>
				<td class="alignCenter" rowspan="2">
					<c:out value="${item.mtiStatus}"/>
				</td>
				<td class="alignCenter" rowspan="2"><c:out value="${item.standInFlag}"/></td>
				<td class="alignCenter"><c:out value="${item.b24Mti}"/></td>
			</tr>
			
			<tr class="<edstw:rowStyle value='${status.index}'/>">
				<td class="alignCenter"><c:out value="${item.expDate}"/></td>
				<td class="alignCenter">
				<c:if test="${ item.edcProcCode ne '519200' && item.edcProcCode ne '519600' }" >
					<c:out value="${paramBean.cardNameMap[item.cardType]}"/>
				</c:if>
				<c:if test="${ item.edcProcCode eq '519200' || item.edcProcCode eq '519600' }" >
					電子票證
				</c:if>
				<c:if test="${ paramBean.cardNameMap[item.cardType] eq null }" >
					<c:out value="${item.cardType }" />
				</c:if>
				</td>
				<td class="alignCenter">
					<c:if test="${item.payTypeInd eq '1' || item.payTypeInd eq '2'}">
						<u onmouseover="addToolTip(this, '紅利', '紅利扣抵點數：<edstw:formatNumber
							value="${item.redeemPoint}" pattern="#,##0"/>\n紅利剩餘點數：<edstw:formatNumber
							value="${item.balancePoint}" pattern="#,##0"/>\n現金繳款金額：<edstw:formatNumber
							value="${item.payAmount}" pattern="#,##0.###"/>');"
							style="color: blue;">					
							<c:if test="${item.mpasFlag eq 'Y'}">小額</c:if>紅利</u>
					</c:if>
					<c:if test="${item.payTypeInd eq 'I' || item.payTypeInd eq 'E'}">
						<u onmouseover="addToolTip(this, '分期', '期數：<c:out
							value="${item.installmentPeriod}"/>期\n首期金額：<edstw:formatNumber
							value="${item.downPayment}" pattern="#,##0.###"/>\n每期金額：<edstw:formatNumber
							value="${item.installmentPay}" pattern="#,##0.###"/>\n手續費:<edstw:formatNumber
							value="${item.formalityFee}" pattern="#,##0.##"/>');"
							style="color: blue;"><c:if test="${item.mpasFlag eq 'Y'}">小額</c:if>分期</u>
					</c:if>
					<c:if test="${item.payTypeInd ne '1' && item.payTypeInd ne '2' && item.payTypeInd ne 'I' && item.payTypeInd ne 'E' && item.mpasFlag eq 'Y'}">
					小額
					</c:if>					
				</td>
				<td class="alignCenter"><a href="#"
					onclick="showIFrameDialog('<c:url value="/trans/queryTransLogIC.do?entity.seqId=${item.seqId}&entity.maskCardNo=${item.maskCardNo}&entity.merchantId=${item.merchantId}&entity.approveCode=${item.approveCode}"/>',{width:1080, height:'520',title: 'IC'}); return false;"
					style="text-decoration: underline; color: blue;"><c:out value="${item.icTran}"/></a></td>
				<td class="alignCenter"><c:out value="${item.cardVeryFlag}"/></td>
				<td class="alignCenter"><c:out value="${item.seqNum}"/></td>
				<td class="alignCenter">
					<c:if test="${item.cupHotKeyInd eq '1'}">Y</c:if>
				</td>
				<td class="alignCenter">
				<c:if test="${item.cardType == 'S' }">
					<c:if test="${fn:length(item.edcPosEntryMode) >= 1}"> 
						<c:out value="${fn:substring(item.edcPosEntryMode,1,-1)}" />
					</c:if>
					<c:if test="${fn:length(item.edcPosEntryMode) < 1}"> 
						<c:out value="${item.edcPosEntryMode}"/>
					</c:if>
				</c:if>
				<c:if test="${item.cardType != 'S' }">
					<c:out value="${item.b24PosEntryMode}"/>
				</c:if>
				</td>
				<td class="alignCenter"><c:out value="${item.edcMti}"/></td>		
		</c:forEach>
	</tbody>
</table>


<script type='text/javascript'>

	function selectAll() {
		
		for(var i = 0; i < 100; i++) {
			
			var cbox = $("row_" + i);
			
			if (cbox == undefined)
				break;
			
			cbox.checked = $("selAll").checked;
		}
	}
	
	function checkSelectAll(item) {
		
		if (!item.check) {
			
			$("selAll").checked = false;
		}
	}
	
	function createReport(event) {

		var selCount = 0;
		
		if ($("selAll").checked) {
			
			try {
				checkFormRpt();
			}
			catch (e) {
				alert(e);
			}
			
		}
		else {

			for(var i = 0; i < 100; i++) {
				
				var cbox = $("row_" + i);
				
				if (cbox == undefined)
					break;
				
				if (cbox.checked)
					selCount++;
			}
			
			if (selCount <= 0) {
				
				alert("請先選取資料.");
			}
			else {
			
				try {
					form['maskCardNo'].value = form['entity.maskCardNo'].value;
					form['showCardIso'].value = form['entity.showCardIso'].value;

				}
				catch (e) {
					alert(e);
				}
				form.target = "_blank";
				form.action = "<c:url value="/trans/queryTransLogRpt.do"/>";
				form.submit();
			}
		}
	}

</script>
</c:if>