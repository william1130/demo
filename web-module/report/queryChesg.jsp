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
			<div id="progTitle">數位化簽帳單查詢</div>
		</div>
		<jsp:include page="/jsp/messages.jsp" />
		<s:form action="queryChesg" style="text-align: center;">
			<%-- 設定開啟分頁功能 --%>
			<s:hidden name="entity.pagingInfo.enablePaging" value="true"/>
			<s:hidden name="entity.pagingInfo.currentPage" value="1"/>
			<s:hidden name="entity.checkStates" value="1"/>
			<edstw:folding foldingId="criteriaBlock" description="查詢條件" >
				<fieldset>
					<legend>查詢條件</legend>
					<table border="0" align="center" cellpadding="4" cellspacing="0">
						<tr>
							<td class="require">日期區間</td>
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
						</tr>
						<tr>
							<td class="optional" align="left">特店代號</td>
							<td>
								<s:textfield name="entity.merchantId" size="20" maxlength="15"/>
							</td>
						</tr>
						<tr>
							<td class="optional">端末機代號</td>
							<td>
								<s:textfield name="entity.termId" size="10" maxlength="8"/>
							</td>
						</tr>
						<tr>
							<td colspan="2" class="buttonCell">
								<input name="submitButton" type="submit" value="確定" id="defaultSubmitButton" class="button">
								<input name="submitButton" type="button" value="產生EXCEL報表" onclick="genReport();" class="button">
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
		msg += validateDateRange(form);
		
		if (msg != "") {
			
			msg = "以下查詢條件必需符合:\n" + msg + "\n";
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
			form.action = "<c:url value="/report/queryChesg.do"/>"
			return true;
		} else {
			event.stop();
			return false;
		}
	}
	
	function initialForm() {

		$(form).observe("submit", AjaxUtil.ajaxSubmitHandler.bindAsEventListener(form, checkForm));

		$('defaultSubmitButton').focus();
	}
	
	function submitPage(pageIdx) {
		form['entity.pagingInfo.currentPage'].value = pageIdx;
		ajaxFormSubmit( {form: form, checkForm: checkForm} );
		form['entity.pagingInfo.currentPage'].value = '1';
	}
	
	EdsEvent.addOnLoad( initialForm );
	
	function changeAction( type )
	{
		if( type == 1 )
		{
			form.action = '<c:url value="/report/queryChesg.do"/>';		
		}
	}
    
    function genReport()
    {
	   document.forms[0].action="<c:url value='/report/queryChesgRpt.do'/>";
       document.forms[0].submit();
       document.forms[0].action="<c:url value='/report/queryChesg.do'/>";
    }
    
    const MAX_DAYS = 93;

    function validateDateRange(form) {
        var dateFromStr = form['entity.tranDateFrom'].value;
        var dateToStr = form['entity.tranDateTo'].value;

        function parseDate(dateStr) {
            var year = parseInt(dateStr.substring(0, 4), 10);
            var month = parseInt(dateStr.substring(4, 6), 10) - 1;
            var day = parseInt(dateStr.substring(6, 8), 10);
            return new Date(year, month, day);
        }

        var dateFrom = parseDate(dateFromStr);
        var dateTo = parseDate(dateToStr);
        var timeDifference = dateTo.getTime() - dateFrom.getTime();
        var dayDifference = Math.ceil(timeDifference / (1000 * 60 * 60 * 24));
        
        if (dayDifference > MAX_DAYS) {
        	return "日期區間不可大於 " + MAX_DAYS + " 天。\n目前間隔約為 " + dayDifference + " 天。";
        }

        return "";
    }
</script>
	
</body>
</html>