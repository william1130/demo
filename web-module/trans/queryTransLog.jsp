<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/directive.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title><c:out value='${currentProgramObj.name}'/></title>
	<%@include file="/jsp/header.jsp"%>
	
	<script type="text/javascript" src="../scripts/addToolTip.js"></script>
	<style>
		.edsToolTip {
			background-color: #CCCCFF;
			opacity: 0.9;
		}
		
	</style>
</head>

<body>
	<div id="mainContentBlock" style="margin:0 auto;width:100%;">
		<div id="progHeader">
			<div id="progTitle">交易Log查詢</div>
		</div>
		<jsp:include page="/jsp/messages.jsp" />
		<s:form action="queryTransLog" style="text-align: center;">
			<%-- 設定開啟分頁功能 --%>
			<s:hidden name="entity.pagingInfo.enablePaging" value="true"/>
			<s:hidden name="entity.pagingInfo.currentPage" value="1"/>
			<s:hidden name="entity.checkStates" value="1"/>
			<edstw:folding foldingId="criteriaBlock" description="查詢條件" >
				<fieldset>
					<legend>查詢條件</legend>
					<table border="0" align="center" cellpadding="4" cellspacing="0">
						<tr>
							<td class="require">交易日期起迄</td>
							<td colspan="3">
									<s:textfield name="entity.tranDateFrom" maxlength="8" size="10" title="請輸入西元年(YYYYMMDD)" value="%{#attr.dateUtil.sysDateString.inputString}"/>
									<img id="_tranDateFrom" class="datePickerImage" src="../images/dateIcon.gif" border="0" align="bottom" width="19px" height="19px" title="選擇日期" alt="選擇日期"/>
									<script type='text/javascript' language='javascript'>
										var form = document.forms[0];
										new DatePicker( {imageId:'_tranDateFrom', dateField:form['entity.tranDateFrom']} );
									</script>
									-
          							<s:textfield name="entity.tranDateTo" maxlength="8" size="10" title="請輸入西元年(YYYYMMDD)" value="%{#attr.dateUtil.sysDateString.inputString}"/>
									<img id="_tranDateTo" class="datePickerImage" src="../images/dateIcon.gif" border="0" align="bottom" width="19px" height="19px" title="選擇日期" alt="選擇日期"/>
									<script type='text/javascript' language='javascript'>
										var form = document.forms[0];
										new DatePicker( {imageId:'_tranDateTo', dateField:form['entity.tranDateTo']} );
									</script>(YYYYMMDD)
								</td>

							<td class="optional">起訖時間</td>
                            <td>
                                <s:textfield name="entity.tranTimeFrom" size="8" maxlength="6" title="請輸入時間(hhmmss)" value="000000" />
                                 -
                                <s:textfield name="entity.tranTimeTo" size="8" maxlength="6" title="請輸入時間(hhmmss)" value="235959" />
                                (hhmmss)
                            </td>
						</tr>
						<tr>
							<td class="optional">卡號</td>
							<td>
								<s:textfield name="entity.cardNum" size="24" maxlength="19"/>
								<s:hidden name="entity.maskCardNo" value="Y"/>
								<s:hidden name="entity.showCardIso" value="Y"/>
							</td>
							<td class="optional">檢核卡號</td>
							<td>
								<input type="checkbox" id="checkCardNum" name="entity.checkCardNum" value="Y" /><span style="color:red">(CUP、SP無需檢核)</span>
							</td>
							<td class="optional" align="left">特店代號</td>
							<td>
								<s:textfield name="entity.merchantId" size="20" maxlength="15"  />
							</td>
						</tr>
						<tr>
							<td class="optional">端末機代號</td>
							<td>
								<s:textfield name="entity.termId" size="10" maxlength="8"/>
							</td>
							<td class="optional">批次號碼</td>
							<td>
								<s:textfield name="entity.batchNum" size="10" maxlength="6"/>
							</td>
							<td class="optional" align="left">授權碼</td>
							<td>
								<s:textfield name="entity.approveCode" size="10" maxlength="6"/>
							</td>
						</tr>
						<tr>
							<td class="optional">EDC MTI</td>
							<td>
								<s:textfield name="entity.edcMti" size="10" maxlength="4"/>
							</td>
							<td class="optional">B24 MTI</td>
							<td>
								<s:textfield name="entity.b24Mti" size="10" maxlength="4"/>
							</td>
							<td class="optional">顯示全部MTI</td>
							<td colspan="2">
								<input type="checkbox" name="entity.defaultMti" id = "defaultMti" value="true" />
                            </td>
						</tr>
						<tr>
							<td class="optional">金額起迄</td>
							<td colspan="3">
								<s:textfield name="entity.tranAmountFrom" size="12" maxlength="10"/> -
								<s:textfield name="entity.tranAmountTo" size="12" maxlength="10"/>
							</td>
							<td class="optional">交易編號</td>
							<td>
								<s:textfield name="entity.transNo" size="30" maxlength="23"/>
							</td>
						</tr>
						<tr>
							<td class="optional">Uny 交易碼</td>
							<td>
								<s:textfield name="entity.unyTranCode" size="24" maxlength="20"/>
							</td>
							<td class="optional">信託資訊平台序號</td>
							<td>
								<s:textfield name="entity.trustNum" size="15" maxlength="12"/>
							</td>
							<td class="optional">信託銀行代碼</td>
							<td>
								<s:textfield name="entity.trustBankId" size="12" maxlength="7"/>
							</td>
						</tr>
						<tr>
							<td class="optional">CHESG FLAG</td>
							<td colspan="5">
								<input type="checkbox" name="entity.chesgFlag" id = "chesgFlag" value="true" />
							</td>
						</tr>
						<tr>
							<td class="optional">查詢交易類型</td>
							<td colspan="5">
								<s:radio name="entity.cardType"
									list="#{'All':'全部', 'credit':'信用卡', 'S':'SmartPay', 'EZ':'悠遊卡', 'IP':'一卡通', 'IC':'愛金卡', 'HC':'遠鑫', '21':'LINE Pay', '23':'iCash Pay', '22':'悠遊付', '26':'Pi', '24':'全盈', '25':'全支付', '0957':'信託資訊交換平台'}"
									value="'All'" />
							</td>
						</tr>
						<tr>
							<td class="optional">排序選擇</td>
							<td colspan="5">
								<s:radio name="entity.sortFields"
									list="#{'tran_date':'交易日期', 'tran_time':'交易時間', 'card_num':'卡號', 'tran_amount':'交易金額', 'bank_id':'金融機構名稱', 'approve_code':'授權碼', 'resp_code':'回應碼', 'merchant_id':'特店代號', 'term_id':'端末代號'}" />
							</td>
						</tr>
						<tr>
							<td colspan="6" class="buttonCell">
								<input id="defaultSubmitButton" type="submit" name="submitButton" value="確定" class="button">
								<input name="resetButton" type="button" value="重設" class="button" onclick="reloadPage();">
							</td>
						</tr>
					</table>
				</fieldset>
			</edstw:folding>
	
		<edstw:folding foldingId="ajaxResultBlock" description="查詢結果">
			<fieldset>
				<legend>查詢結果</legend>
				<div id="ajaxResultBlockContent">
				</div>
			</fieldset>
		</edstw:folding>
	</s:form>	
	</div>

	<script type='text/javascript'>
	var form = document.forms[0];

	function validateCardNo(no) {
		
		if (!no.match("^[0-9]+$")) {	
			return false;
		}		
	/*	var len = no.length;
		var odd = true;
		var sum = 0;
		
		for (var i = len - 1; i >= 0; i--) {
			
			var ch = no.substring(i, i + 1);
			var num = odd ? ch * 1 : ch * 2;
			odd = !odd;
			
			if (num >= 10) {
				num = (num % 10) + 1;
			}
			
			sum += num;
		}
		
		if ((sum % 10) != 0)
			return false;
		*/
		return true;
	}
	
	function validateForm(form) {
		var checkRequireds = [
		 	["entity.tranDateFrom", "交易日期起"],
		 	["entity.tranDateTo", "交易日期迄"]
		];
		
		var msg = "";
		
		if (form[checkRequireds[0][0]].value != "" && form[checkRequireds[1][0]].value == ""){
            form[checkRequireds[1][0]].value = form[checkRequireds[0][0]].value;
        }else if (form[checkRequireds[1][0]].value != "" && form[checkRequireds[0][0]].value == ""){
            form[checkRequireds[0][0]].value = form[checkRequireds[1][0]].value;
        }
		
		for (var i = 0; i < checkRequireds.length; i++) {
			
			var val = form[checkRequireds[i][0]].value;
			
			if (val == "") {
				msg += "  *" + checkRequireds[i][1] + "\n";
			}
		}
		
		if (msg != "") {
			
			msg = "以下查詢條件必需輸入:\n" + msg + "\n";
		}
		
		var cardNumField = form['entity.cardNum'];
		
		if (form['entity.checkCardNum'].checked) {
			
			var result = validateCardNo(cardNumField.value);
			
			if (!result) {
				
				msg += "卡號檢查錯誤!\n";
				cardNumField["style"]["borderColor"] = "#FF0000";
			}
			else {

				cardNumField["style"]["borderColor"] = "";
			}
		}
		else {

			cardNumField["style"]["borderColor"] = "";
		}
		
		if (form['entity.transNo'].value != "" &&
				form['entity.termId'].value == "") {

			msg += "已輸入交易編號, 端末機代號必需輸入!\n";
		}
		
		if (msg != "") {
			
			alert(msg);
			return false;
		}
		
		return true;
	}
	
	function checkForm(event) {
		var now = (+new Date());
		form['entity.checkStates'].value = now;
		
		if (validateForm(form)) {
			//form.target = undefined;
			form.action = "<c:url value="/trans/queryTransLog.do"/>"
			//form.action = '<c:url value="/trans/queryTransLog.do"/>';
			return true;
		} else {
			event.stop();
			return false;
		}
	}
	
	function checkFormRpt() {
		if (validateForm(form)) {
			form.target = "_blank";
			form.action = "<c:url value="/trans/queryTransLogRptAll.do"/>";
			form.submit();
			return true;
		}
		else {		
			return false;
		}
	}
	
	function initialForm() {
		$("defaultMti").checked = true;

		checkUrl("<c:url value="/trans/checkingShowCardNo.do"/>",
			function() {
				if (arguments[0]) {
					form['entity.maskCardNo'].value = "N";
				}
				else {
					form['entity.maskCardNo'].value = "Y";
				}
			}
		);
		checkUrl("<c:url value="/trans/checkingShowISO.do"/>",
			function() {
				if (arguments[0]) {
					form['entity.showCardIso'].value = "Y";
				}
				else {
					form['entity.showCardIso'].value = "N";
				}
			}
		);

		$(form).observe("submit", AjaxUtil.ajaxSubmitHandler.bindAsEventListener(form, checkForm));
		//$(form).observe("submit", checkForm);
		//$(form).observe("submit", AjaxUtil.ajaxSubmitHandler.bindAsEventListener(form, checkForm));

		$('defaultSubmitButton').focus();
	}
	
	function submitPage(pageIdx) {
		form['entity.pagingInfo.currentPage'].value = pageIdx;
		ajaxFormSubmit( {form: form, checkForm: checkForm} );
		form['entity.pagingInfo.currentPage'].value = '1';
	}
	
	function reloadPage(){
		location.href = '<c:url value="/trans/toQueryTransLog.do"/>';
	}
	
	function checkUrl(url, targetFunc) {
		try {
			
			var xmlhttp = null;
			
			if (window.XMLHttpRequest) {
				xmlhttp = new XMLHttpRequest;
			}
			else if (window.ActiveXObject) {
				xmlhttp = new ActiveXObject("Microsoft.XMLHttp");
			}
			
			if (xmlhttp) {
				
				xmlhttp.onreadystatechange = function() {
					
					if (xmlhttp.readyState == 4) {
						if (xmlhttp.status == 200) {
							targetFunc(true);
						}
						else {
							targetFunc(false);
						}
					}
				};
				
				xmlhttp.open("HEAD", url, false);
				xmlhttp.send();
			}
		}
		catch (e) {
			
			alert(e);
		}
	}
	
	EdsEvent.addOnLoad( initialForm );
	
	function changeAction( type )
	{
		if( type == 1 )
		{
			form.action = '<c:url value="/trans/queryTransLog.do"/>';		
		}
		else
		{
			form.action = '<c:url value="/trans/queryTransLogRpt.do"/>';	
		}
	}
	
			
</script>
	
</body>
</html>

