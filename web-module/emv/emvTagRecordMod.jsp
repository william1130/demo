
<%@page import="proj.nccc.logsearch.ProjConstants"%>
<%@page import="proj.nccc.logsearch.beans.Util"%>
<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/directive.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title><c:out value='${currentProgramObj.name}'/></title>
		<%@include file="/jsp/header.jsp"%>
		<style>
		.noBg{background:transparent;}
		.noBg tr{background:transparent;}
		.noBg td{background:transparent;}

		</style>	
	</head>
	<body>
	<s:set var="pagingInfo" value="%{entity.pagingInfo}" /> 
	<s:set var="resultList" value="%{pagingInfo.resultList}"/>
	<s:set var="resultTempList" value="%{pagingInfo.resultTempList}"/>
	<c:set var="cardTypeList" value="${paramBean.emvCardTypeList}" />
	<c:set var="string2" value="${fn:split( entity.cardType, ',')}" />
	<c:set var="cardTypeSzie" value="1" />
		<div id="mainContentBlock" style="width:70%">
			<div id="progHeader">
				<div id="progTitle" title="程式代碼 : <c:out value='${currentFunctionObj.id}'/>"><c:out value='EMV維護-修改'/></div>
			</div>
			<jsp:include page="/jsp/messages.jsp"/>
			<div id="dataBlock">
				<s:form action="modifyEmvTagRecord">
					<s:hidden name="entity.uiLogAction" value="MODIDY"/>
					<s:hidden name="entity.uiLogFunctionName" value="EMV_PARA"/>
					<s:hidden name="entity.uiLogOther" value=""/>
					
					<fieldset>
						<legend>修改</legend>
						<table border="0" cellpadding="4" cellspacing="1" class="listTable" align="center" style="width:90%">
							<tr class="oddRow">
								<th>*卡別</th>
								<td>
								<s:hidden name="entity.cardType"/>
								<input type="hidden" name="entity.oriCardType" value="${entity.cardType}" />
								<c:forEach var="ct" items="${cardTypeList}">
									<c:set var="boxFlag" value="N" />
									<c:forEach var="cts" items="${string2}" varStatus="num">
										<c:if test="${cts eq ct.cardType }">
											<c:set var="boxFlag" value="Y" />
											<input type="hidden" name="oriCardType" value="${ct.cardType }" />
											<input type="checkbox" name="cardType" value="${ct.cardType }" checked readonly  /><c:out value="${ct.cardTypeName }" />
										</c:if>
									</c:forEach>
									<c:if test="${boxFlag ne 'Y' }">
										<input type="checkbox" name="cardType" value="${ct.cardType }" /><c:out value="${ct.cardTypeName }" />
										
									</c:if>
								</c:forEach>
								<%--
									<c:forEach var="ct" items="${string2}" varStatus="num">
										<c:set var="cardTypeSzie" value="${num.index + 1}" />
										<input type="radio" name="cardType" id="cardType${num.index}" value="${ct}" /><c:out value="${paramBean.emvCardTypeNameMap[ct]}"/>
									</c:forEach>--%>
									 <input type="hidden" name="entity.oriSameValueFlag" value="${entity.sameValueFlag }" />
									 <s:hidden name="entity.sameValueFlag"/>
									 <c:if test="${entity.sameValueFlag eq 'Y' || entity.sameValueFlag.length() > 1 }" > 
										&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;					
										<input type="checkbox" name="entity.nowSameValueFlag" id="nowSameValueFlag" />取消同值關係
									 </c:if>
									<!-- <input type="checkbox" id="ne" name="ne" onclick="changeMod()" /> -->

								<%-- </c:if> --%>
								<%-- <c:if test="${entity.sameValueFlag ne 'Y' }" >
									<input type="hidden" name="cardType" value="${entity.cardType }" />
									<s:hidden name="entity.sameValueFlag"/>
									<c:out value="${paramBean.emvCardTypeNameMap[entity.cardType]}"/>
								</c:if> 
								<input type="hidden" name="cardTypeSize" id="cardTypeSize" value="${cardTypeSzie}" />
								--%>
								</td>
							</tr>
							<tr class="evenRow">
								<th>*TAG名稱</th>
								<td>
									<s:hidden name="entity.emvTag"/>
									<c:out value="${entity.emvTag }" />
								</td>
							</tr>
							<tr class="oddRow">
								<th>*全名</th>
								<td>
									<s:textfield name="entity.emvName" size="40" maxlength="50"/>
								</td>
							</tr>
							<tr class="evenRow">
								<th>*縮寫</th>
								<td>
									<s:textfield name="entity.emvAbbr"/>
								</td>
							</tr>
							<tr class="oddRow">
								<th>*描述</th>
								<td>
									<s:textarea name="entity.emvDesc" cols="80" rows="8"/>
								</td>
							</tr>		
							<tr class="evenRow">
								<th>*長度</th>
								<td>
									<s:hidden name="entity.emvLen"/>
									<c:out value="${entity.emvLen }" /> Bytes
								</td>
							</tr>
							<tr class="oddRow">
								<th>*Value</th>
								<td>
								<c:set var="bytePos" value="" />
									<table class="noBg">
										<c:forEach var="item" items="${resultTempList}" varStatus="status">
											<c:if test="${bytePos ne  item.posByte}" >
											<c:set var="bytePos" value="${ item.posByte }" />
												<tr>
													<th colspan=2>
														<input type="hidden" name="posByte" id="posByte" value="${ item.posByte}"  size="60" />
														Byte<c:out value="${item.posByte }" />
													</th>
													<td><input type="text" name="valueByte" id="valueByte" value="${ item.valueByte}" size="60" /></td>
												</tr>
											</c:if>
												<tr>
													<td width=10><input type="hidden" name="posByte" id="posByte" value="${ item.posByte}" /></td>
													<th>Bit<c:out value="${item.posBit }" /></th>
													<td><input type="text" name="valueBit${item.posBit }" id="valueBit${item.posBit }" value="${ item.valueBit}"  size="60" /></td>
												</tr>
										</c:forEach>
									</table>
								</td>
							</tr>		
							<tr>
								<td colspan="2" class="buttonCell">
									<input type="submit" id="defaultSubmitButton" name="submitButton" value="確定" class="button">
									
								</td>
							</tr>
						</table>
					</fieldset>
				</s:form>
				<script type="text/javascript">
					var form = document.forms[0];

					
					function checkForm( event )
					{
						var msg = "資料錯誤:\n\n";
						var warnMsg = "注意:\n\n";
						var len = 0;
						var aa = "1"; 
						var bb = "1"; 
						var allCardType = "";
						for(var a=0;a<form.cardType.length;a++) 
						{
					    	if(document.forms[0].cardType[a].checked){
					    		allCardType = allCardType + document.forms[0].cardType[a].value;
					    		len = len +1;
					      	}
					    }
						for(var b = 0; b< form.oriCardType.length;b++){
							if(allCardType.indexOf(form.oriCardType[b].value) < 0){
								aa = "0";
								break;
							}
						}
						
						if(len == 0) {
							msg = msg + "需至少選擇一種卡別!!";
						}
						
						if($("nowSameValueFlag").checked && len > 1){
							msg = msg + "僅能針對一種卡別取消同值關係!!";	
						}
						var oriCard = form["entity.oriCardType"].value;
						if(oriCard.indexOf(allCardType) < 0){
							bb = "0"
						}
						if($("nowSameValueFlag").checked && bb == '0' ){
							msg = msg + "非同值關係需選擇原本的卡別中的其中一個!!";	
						}
						
						if(!$("nowSameValueFlag").checked && aa == '0'){
							msg = msg + "同值關係需包含原本的卡別!!";
						}

						if( msg.length > 12 )
						{
							alert( msg );
							event.stop();
							return false;
						}
						
						var keyTitle = "TAG名稱:"+form['entity.emvTag'].value+"";
						$j("input[name='entity.uiLogOther']").val(keyTitle);
						showSubmitMessage();
						return true;
					}
					function initialForm()
					{
						$(form).observe("submit", checkForm);
						$('defaultSubmitButton').focus();
						//changeMod();
					}
			
					EdsEvent.addOnLoad( initialForm );
					
//					function changeMod()
//					{
						
//					}
					
				</script>
			</div>
		</div>
	</body>
</html>
