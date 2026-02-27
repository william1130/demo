<%--
 * ==============================================================================================
 * $Id: SC-UCA-W00145_QL1.jsp,v 1.4 2020/02/03 10:46:41 \jjih Exp $
 * ==============================================================================================
--%>
<%@page pageEncoding="UTF-8"%>
<%@page contentType="text/html"%>
<%@include file="/jsp/directive.jsp"%>
<jsp:include page="/jsp/messages.jsp"/>
<s:set var="resultList" value="%{resultList}" scope="request"/><c:if test="${not empty resultList}">
<s:set var="pagingInfo" value="%{entity.pagingInfo}"/>



<display:table uid="authLogData" cellpadding="4" cellspacing="1" sort="external" partialList="true" size="${pagingInfo.totalCount}" name="pagingInfo">
    <display:column title="序號" class="seqNo" value="${(pagingInfo.startIndex + authLogData_rowNum)-1}"/>
    
    <display:column title="卡號" class="alignCenter" style="width:64">        
        <c:out value="${authLogData.cardNo}"/></display:column>

    <display:column title="有效日期" class="alignCenter">
        <c:out value="${authLogData.expireDate}"/>
    </display:column>

    <display:column title="特店代號<hr>端末機代號<br>(操作 ID)" class="alignCenter" >
        <c:out value="${authLogData.merchantNo}"/>
        <hr>
        <c:choose>
        	<c:when test="${authLogData.transType=='AP' || authLogData.transType=='AC' || authLogData.transType=='AM'}">
        		<c:out value="${authLogData.updateId}"/>&nbsp;
        	</c:when>
        	<c:otherwise>
        		<c:out value="${authLogData.terminalNo}"/>&nbsp;
        	</c:otherwise>
        </c:choose>
    </display:column>
    <display:column title="消費金額" class="alignCenter" >
        <c:out value="${authLogData.purchaseAmt}"/>
    </display:column>
	<display:column title="商店附加費" class="alignRight" >
		<c:out value="${authLogData.surchgAmt}"/>&nbsp;
	</display:column>
    <display:column title="授權碼" class="alignCenter" >
      <!-- 20090211 fallback授權不成功交易,不顯示授權碼(移除authLogData.transType=='IP'判斷式) -->
      <c:if test="${authLogData.displayApproveNo=='Y' ||authLogData.manualEntryResult=='Y' || authLogData.manualEntryResult=='C' || authLogData.approvalNo=='AT0000' }">
         <c:out value="${authLogData.approvalNo}"/>   
      </c:if>   
    </display:column>
    <display:column title="狀況欄<hr>MCC" class="alignCenter" >
        <!--  20090204 改顯示為condition code   conditionCode  
        <c:out value="${authLogData.manualEntryResult}"/>&nbsp;
        -->
        <c:out value="${authLogData.conditionCode}"/>&nbsp;
        <hr>
        <c:out value="${authLogData.mccCode}"/>&nbsp;
    </display:column>
	<display:column title="消費日期<hr>交易型態" class="alignCenter" >
    <c:out value="${authLogData.purchaseDate}"/>&nbsp;
    <hr>
    <c:if test="${authLogData.loyaltyFlag == 'Y'}">
    <a onmouseover="ShowLoyaltyObj('<c:out value="${(pagingInfo.startIndex + authLogData_rowNum)-1}"/>')" onmouseout="HideObj('<c:out value="${(pagingInfo.startIndex + authLogData_rowNum)-1}"/>')" href="#"><c:out value="紅利"/></a>&nbsp;        
    </c:if>
    <c:if test="${authLogData.installmentFlag == 'Y'}">       
    <a onmouseover="ShowLoyaltyObj('<c:out value="${(pagingInfo.startIndex + authLogData_rowNum)-1}"/>')" onmouseout="HideObj('<c:out value="${(pagingInfo.startIndex + authLogData_rowNum)-1}"/>')" href="#"><c:out value="分期"/></a>&nbsp;        
    </c:if>        
    <c:if test="${authLogData.loyaltyFlag != 'Y' && authLogData.installmentFlag != 'Y'}">
        <c:out value="一般"/>&nbsp;
    </c:if>
    <div class="article" id="<c:out value="${(pagingInfo.startIndex + authLogData_rowNum)-1}"/>" style="display:none; width:200px; height:150px; ">
        <c:if test="${authLogData.loyaltyFlag == 'Y'}">
            <fieldset>
                <legend>紅利交易明細</legend>
                    <table border="0" cellpadding="4" cellspacing="0">
                        <tr>
                            <th class="field">原紅利點數</th><td><edstw:formatNumber value="${authLogData.loyaltyOriginalPoint}"/></td>
                        </tr>		                       
                        <tr>                        
                            <th class="field">扣抵點數</th><td><edstw:formatNumber value="${authLogData.loyaltyRedemptionPoint}"/></td>
                        </tr>		           
                        <tr>                        
                            <th class="field">扣抵金額</th><td><edstw:formatNumber value="${authLogData.loyaltyRedemptionAmt}"/></td>		
                        </tr>		  
                        <tr>                        
                            <th class="field">扣抵後金額</th><td><edstw:formatNumber value="${authLogData.loyaltyPaidCreditAmt}"/></td>						  	  	
                        </tr>		
            </table>
            </fieldset>   
        </c:if>
        <c:if test="${authLogData.installmentFlag == 'Y'}">
            <fieldset>
                <legend>分期交易明細</legend>                
                <table border="0" cellpadding="4" cellspacing="0">
                        <tr>
                            <th class="field">分期期數</th><td><edstw:formatNumber value="${authLogData.installPeriodNum}"/></td>
                        </tr>
                        <tr>
                            <th class="field">首期金額</th><td><edstw:formatNumber value="${authLogData.installFirstPayment}"/></td>
                        </tr>
                        <tr>
                            <th class="field">每期金額</th><td><edstw:formatNumber value="${authLogData.installStagesPayment}"/></td>						  	  	
                        </tr>
                        <tr>
                            <th class="field">手續費</th><td><edstw:formatNumber value="${authLogData.installFormalityFee}"/></td>						  	  	
                        </tr>
                </table>
            </fieldset>
        </c:if>        
        </div>
    </display:column>
    <display:column title="消費時間<hr>收單Bin" class="alignCenter" >
        <c:out value="${authLogData.purchaseTime}"/>&nbsp;
        <hr>
        <c:out value="${authLogData.acquireId}"/>&nbsp;
    </display:column>
    <display:column title="類別<hr>國別" class="alignCenter" >
        <c:out value="${authLogData.transType}"/>&nbsp;
        <hr>
        <c:out value="${authLogData.countryName}"/>&nbsp;
    </display:column>
    <display:column title="RESP主機<hr>Entry<br>Mode" class="alignCenter" >
        <c:out value="${authLogData.returnHostCode}"/>&nbsp;
        <hr>
        <c:out value="${authLogData.entryMode}"/>&nbsp;
    </display:column>

</display:table>

<script type="text/javascript">
	//必須提供本函式, 將分頁資訊傳回.
	function createDisplayPagingInfo()
	{
		var dpi = new DisplayPagingInfo();
		//設定執行查詢的form物件.
		dpi.form = document.forms[0];
		//設定使用ajax方式查詢
		dpi.enableAjaxSubmit = true;
		//設定原查詢條件所能查得的全部個數.
		dpi.totalCount = <c:out value='${pagingInfo.totalCount}'/>;
		//設定sever端用來放置分頁資訊的pagingInfo物件的屬性名稱.
		dpi.pagingInfoProperty = "entity.pagingInfo";
		return dpi;
	}
</script>
</c:if>

