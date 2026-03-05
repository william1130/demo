<%--
 * ==============================================================================================
 * $Id: SC-UCA-W00144_M01.jsp,v 1.3 2019/10/08 11:26:30 \jjih Exp $
 * ==============================================================================================
--%>
<%@page pageEncoding="UTF-8"%>
<%@page contentType="text/html"%>
<%@include file="/jsp/directive.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title><c:out value='${currentFunctionObj.name}'/></title>
		<%@include file="/jsp/header.jsp"%>	
	</head>
	<body>
		<div id="mainContentBlock" style="width: 750;">
			<div id="progHeader">
				<div id="progTitle" title="程式代碼 : <c:out value='${currentFunctionObj.id}'/>"><c:out value='${currentFunctionObj.name}'/></div>
			</div>
			<s:set var="mainEntity" value="%{entity}"/>                            
			<c:if test="${ not empty mainEntity }">
               <s:form action="mAuthorizerGetApproveNo">       
               <c:set var="entity" value="${mainEntity.creditInfoVO}"/>
               <c:set var="mEntity" value="${mainEntity.merchantVO}"/>   
               <div id='dataBlock' class='foldingBlock'>
               <table border='0' cellspacing='0' cellpadding='0' class='foldingTable'>
                 <tr>
                   <td class='foldingMark'>
                     <div name='foldingMark'>
                       <img name='foldingImage' border='0' onclick='autoFolding(dataBlock)' onmouseover='javascript:this.style.cursor="pointer";' src='dummy_for_firefox_bug'/>
                     </div>
                   </td>
                   <td class='foldingOpened'>
                     <div name='foldingArea'>
                     <fieldset>
                     <legend>特店及發卡銀行資訊</legend>
                     <table border="0" align="center" cellpadding="4" cellspacing="0">
                       <tr class="rowSeparator">
                         <td colspan="8">特店基本資料</td>
                       </tr>
                       <tr>
                         <td class="field">特店</td>
                         <td colspan="2">
                           <c:out value="${mEntity.mchtName}"/>
                           <s:hidden name="entity.merchantNo"/>
                         </td>
                         <td class="field">地址</td>
                         <td colspan="4">
                           <c:out value="${mEntity.zipCode3}"/>
                           <c:out value="${mEntity.zipCode2}"/>
                           <c:out value="${mEntity.chinZipCity}"/>
                           <c:out value="${mEntity.chinAddr}"/>                                                                                    
                         </td>
                       </tr>
                       <tr>
                         <td class="field">電話</td>
                         <td colspan="2"><c:out value="${mEntity.telNo1}"/></td>
                         <td class="field">收單會員名稱</td>
                         <td colspan="4"><c:out value="${mainEntity.acqName}"/></td>
                       </tr>
                       <tr>
						 <td class="field">簽約日期</td>
						 <td><c:out value="${mEntity.contractDate}"/></td>
						 <td class="field">解約日期</td>
	                     <td><c:out value="${mEntity.contractEndDate}"/></td>
					     <td class="field">風險等級</td>
				         <td><c:out value="${mEntity.trustClass}"/></td>
						 <td class="field">特店限額</td>
						 <td><c:out value="${mEntity.sepcialTreatyLimit}"/></td>
                       </tr>
                       <tr>
                         <td class="field">合約種類</td>
                         <td><c:out value="${mEntity.contractType}"/></td>
                         <td class="field">POS</td >
                         <td><c:out value="${mEntity.posFlag}"/></td>
                         <td class="field">EDC</td>
                         <td><c:out value="${mEntity.edcFlag}"/></td>
                         <td class="field">IC</td>
                         <td><c:out value="${mEntity.icFlag}"/></td>
                       </tr>
                       <tr class="rowSeparator">
                         <td colspan="8">發卡行資訊</td>
                       </tr>
                       <tr>                                
                         <td class="field">發卡銀行</td>
                         <td><c:out value="${mainEntity.cardBankNo}"/><c:out value="${mainEntity.cardBankName}"/></td>
                         <td class="field">是否為會員銀行</td>
                         <td>
	                       <c:if test="${mainEntity.memberBank=='Y'}" >是&nbsp;&nbsp;</c:if>
	                       <c:if test="${mainEntity.memberBank=='N'}" >否&nbsp;&nbsp;</c:if>
	                     </td>
                         <td class="field">授權專線</td>
                         <td colspan="3"><c:out value="${mainEntity.issueInfoTel}"/><br><c:out value="${mainEntity.issueInfoTel1}" /></td>
                       </tr>                                                                                    
                     </table>
                     </fieldset>
                     <script type='text/javascript' language='javascript'>
                        var dataBlock = new FoldingObj('dataBlock',false,'特店及發卡銀行資訊','../images/expand_mark.gif','../images/collapse_mark.gif');
                        autoFolding(dataBlock,false);
                     </script>
                     </div> 
                   </td>
                 </tr>
               </table>
               </div>   
               <div id='inputBlock' class='foldingBlock'>
                 <table border='0' cellspacing='0' cellpadding='0' class='foldingTable'>                        
                 <tr>
                   <td class='foldingMark'>&nbsp;&nbsp;</td>
                   <td class='foldingOpened'>
                     <div name='foldingArea'>
                     <fieldset>
                     <legend>授權資料</legend>
                     <table border="0" align="center" cellpadding="4" cellspacing="0">                                
                       <tr>
                         <td class="field">特店代號</td>
                         <td><c:out value="${mainEntity.realMerchantNo}"/></td>
                         <td class="field">卡號</td>
                         <td>
                           <c:out value="${mainEntity.cardNo}"/>
                           <s:hidden name="entity.cardNo1"/>
                           <s:hidden name="entity.cardNo2"/>
                           <s:hidden name="entity.cardNo3"/>
                           <s:hidden name="entity.cardNo4"/>
                           <s:hidden name="entity.cardNo5"/>
                         </td>
                         <td class="field">有效日期</td>
                         <td>
                           <c:out value="${mainEntity.expireDate}"/>
                           <s:hidden name="entity.expireDate"/>                                                                    
                           <input type="hidden" name="entity.recurringFlag" value='<c:out value="${mainEntity.recurringFlag}"/>' />
                         </td>
                       </tr>
                       <tr>
                         <td class="field" style="font-weight:bold; font-size:1.5em;">金額</td>
                         <td class="displayAmount" style="font-weight:bold; font-size:1.5em;">
                           <edstw:formatNumber value="${mainEntity.purchaseAmt}" />
                           <s:hidden name="entity.purchaseAmt"/>
                         </td>
                         <td class="field">交易日期</td>
                         <td>
                           <c:out value="${mainEntity.processDate}"/>
                           <s:hidden name="entity.processDate"/>
                         </td>
                         <td class="field">交易時間</td>
                         <td>
                           <c:out value="${mainEntity.processTime}"/>
                           <s:hidden name="entity.processTime"/>
                         </td>
                       </tr>
                       <tr>
                         <td class="field">補登交易</td>
                         <td>
                           <s:radio name="entity.addFlag" list="#{'Y':'是'}" disabled="true"/>&nbsp;&nbsp;
                           <s:radio name="entity.addFlag" list="#{'N':'否'}" disabled="true"/>
                         </td>
                         <td class="field">授權來源</td>
                         <td>
                           <c:if test="${mainEntity.addFlag == 'Y'}">
                              <c:set var="authorizeOptions" value="${paramBean.authorizeOptions}"/>
                              <s:select name="entity.authorizeReason" list="#attr.authorizeOptions != null ? #attr.authorizeOptions : #{'':''}" listKey="value" listValue="label"/>
                           </c:if>
                           <c:if test="${mainEntity.addFlag == 'N'}">
                              <s:hidden name="entity.authorizeReason"/>
                              <c:out value="${paramBean.authorizeNameMap[mainEntity.authorizeReason]}"/>
                           </c:if>
                         </td>
                         <td class="alignCenter" colspan="2">
                           <input name="Button4" type="button" class="button" value="授權交易查詢" onclick="goAuthLogQuery();"/>
                         </td>	                                                      
                       </tr>
                       <tr>
						 <td class="field">索取授權碼</td>
                         <td>
			               <input name="entity.getAuthCode" type="radio" value="Y" disabled <c:if test="${mainEntity.getAuthCode == 'Y'}">checked</c:if>/>是&nbsp;&nbsp;
                           <input name="entity.getAuthCode" type="radio" value="N" disabled <c:if test="${mainEntity.getAuthCode == 'N'}">checked</c:if>/>否
		                 </td>
                         <td class="field">強制授權</td>
                         <td>
                           <input type="hidden" name="entity.manualForcePostFlag" value='<c:out value="${mainEntity.manualForcePostFlag}"/>' />
                           <!-- 
                           <c:out value="${mainEntity.forceAuth}"/>
                           -->
                           <c:choose>
                             <c:when test="${mainEntity.addFlag == 'Y'}">
					            <input name="entity.forceAuth" type="radio" value="Y" disabled />是&nbsp;&nbsp;
		                        <input name="entity.forceAuth" type="radio" value="N" disabled checked/>否
                             </c:when>
                             <c:when test="${mainEntity.addFlag == 'N'}">
		                           <c:choose>
		                             <c:when test="${mainEntity.actionStep=='NONCENTERAUTH'}">
					                 	<input name="entity.forceAuth" type="radio" value="Y" onclick="clieckforce0();" <c:if test="${mainEntity.forceAuth == 'Y'}">checked</c:if> />是&nbsp;&nbsp;
		                                <input name="entity.forceAuth" type="radio" value="N" onclick="clieckforce1();" <c:if test="${mainEntity.forceAuth == 'N'}">checked</c:if> />否
		                             </c:when>		                             
		                             <c:otherwise>
							            <input name="entity.forceAuth" type="radio" value="Y" disabled <c:if test="${mainEntity.forceAuth == 'Y'}">checked="true"</c:if> />是&nbsp;&nbsp;
				                        <input name="entity.forceAuth" type="radio" value="N" disabled <c:if test="${mainEntity.forceAuth == 'N'}">checked="true"</c:if> />否
		                             </c:otherwise>	
		                           </c:choose>	
                             </c:when>
                           </c:choose>  
                         </td>	
                       </tr>
                       <tr>
                         <td class="field" style="font-weight:bold; font-size:1.5em;" >授權接受否</td>
                         <td colspan="5" style="font-weight:bold; font-size:1.5em;" >
                           <c:if test="${mainEntity.addFlag == 'N'}">
                              <c:if test="${mainEntity.actionStep=='APPROVED'}">
                                 <c:choose>
								    <c:when test="${mainEntity.returnHostCode=='00'}">
									   <input type="radio" name="entity.authManuEntry" value="Y" 
									    checked onclick="clickSureButton();" >接受&nbsp;
						               <input type="radio" name="entity.authManuEntry" value="C" 
										onclick="clickApproveButton();" >取消&nbsp;
								    </c:when>
									<c:otherwise>
									   <input type="radio" name="entity.authManuEntry" 
									    value='<c:out value="${mainEntity.authManuEntry}"/>' checked >
								       <c:choose>
										  <c:when test="${mainEntity.authManuEntry=='P'}">沒收</c:when>
										  <c:when test="${mainEntity.authManuEntry=='R'}">請聯絡發卡銀行</c:when>
										  <c:when test="${mainEntity.authManuEntry=='N'}">拒絕</c:when>
										  <c:otherwise>其他</c:otherwise>
									   </c:choose>
									</c:otherwise>
								  </c:choose>
						      </c:if>
							  <c:if test="${mainEntity.actionStep=='CANCELAPPROVED'}">
								 <input type="hidden" name="entity.authManuEntry" value='C' />
						      </c:if>					
                           </c:if>            
                           <c:if test="${mainEntity.addFlag == 'Y'}">
							    <c:if test="${(mainEntity.pureCupCard != 'Y' && mainEntity.tokenFlag == false) && mainEntity.cardType != 'D' && mainEntity.qrMcht != 'Y'}">
							          <input type="radio" name="entity.authManuEntry" value="Y" checked >接受&nbsp;   
							    </c:if>   
							    <!--    
							    <c:out value="${mainEntity.pureCupCard}"/>                                                         
							    -->
							   <input type="radio" name="entity.authManuEntry" value="N"  >拒絕&nbsp;                                                                    
							   <input type="radio" name="entity.authManuEntry" value="C"  >取消&nbsp;                                                                                                                                        
							   <input type="radio" name="entity.authManuEntry" value="U"  >不處理&nbsp;                                                                    
							   <input type="radio" name="entity.authManuEntry" value="A"  >確認&nbsp;                                                                    
							   <input type="radio" name="entity.authManuEntry" value="D"  >未先刷卡&nbsp;                                                                    
							   <input type="radio" name="entity.authManuEntry" value="P"  >沒收&nbsp;
                           </c:if>
                           <!--    
                           [<c:out value="${mainEntity.cardType}"/>]
                           -->                             
                         </td> 
                       </tr>
                         <td class="field" style="font-weight:bold; font-size:1.5em;">授權碼</td>
                         <td style="font-weight:bold; font-size:1.5em;">
                          <!--
	                      <c:if test="${mainEntity.addFlag == 'Y'&& mainEntity.cardType != 'D'&& mainEntity.qrMcht != 'Y'}">
	                          <input name="entity.approvalNo" type="text" value='<c:out value="${mainEntity.approvalNo}"/>' size="8" maxlength="6"/>
	                       </c:if>	
                          <c:if test="${mainEntity.addFlag == 'Y'&&( mainEntity.cardType == 'D' || mainEntity.qrMcht == 'Y')}"> 
	                         <input type="hidden" name="entity.approvalNo" value='<c:out value="${mainEntity.approvalNo}"/>' />
	                      </c:if>	
	                      -->
	                       <c:if test="${mainEntity.addFlag == 'Y'}">
	                          <input name="entity.approvalNo" type="text" value='<c:out value="${mainEntity.approvalNo}"/>' size="8" maxlength="6"/>
	                       </c:if>	
	                       <c:if test="${mainEntity.addFlag == 'N'}">
	                          <c:out value="${mainEntity.approvalNo}"/>
	                          <input type="hidden" name="entity.approvalNo" value='<c:out value="${mainEntity.approvalNo}"/>' />
	                       </c:if>	
                         </td>
                         <td class="field" >授權回應碼</td>
                         <td colspan="3">
                             <c:choose>
                               <c:when test="${mainEntity.returnIsoCode==null}">&nbsp;</c:when>
                               <c:otherwise>
                                 <c:out value="${mainEntity.returnIsoCode}"></c:out>&nbsp;-&nbsp;
                                 <edstw:out value="${paramBean.responseCodeNameMap[mainEntity.returnIsoCode]}" default="DECLINE"/>
                               </c:otherwise>
                             </c:choose>
                             <input type="hidden" name="entity.returnIsoCode" value="<c:out value="${mainEntity.returnIsoCode}"/>"/>
                         </td>

                       <tr>
                         <td colspan="6">
                           <jsp:include page="/jsp/messages.jsp"/>
                         </td>
                       </tr>
                       <tr>
						 <td colspan="6"  class="buttonCell">
						   <!--  
						   <c:out value="${mainEntity.actionStep}"></c:out>
						  -->
						   <input name="goGetApproveButton" type="button" value="連線授權" onclick="goGetApproveNo();"/>
						   <input name="goSureButton" type="button" value="確　　定" onclick="goConfirmAuth();"/>
						   <input name="goHomeButton" type="button" value="更　　正" onclick="goHome();"/>
						 </td>
						 <c:if test="${mainEntity.actionStep=='PREAPPROVE'}">
						   <script>
						      document.forms[0].goGetApproveButton.disabled=false;
						  	  document.forms[0].goSureButton.disabled=true;
						   </script>
						 </c:if>
					     <c:if test="${mainEntity.actionStep=='APPROVED'}">
						   <script>
							  document.forms[0].goGetApproveButton.disabled=true;
							  document.forms[0].goSureButton.disabled=false;
							  document.forms[0].goHomeButton.disabled=true;
						   </script>
					     </c:if>
						 <c:if test="${mainEntity.actionStep=='ADDFLAG'}">
						   <script>
							  document.forms[0].goGetApproveButton.disabled=true;
							  document.forms[0].goSureButton.disabled=false;
							  document.forms[0].goHomeButton.disabled=false;
						   </script>
						 </c:if>
						 <c:if test="${mainEntity.actionStep=='CANCELAPPROVED'}">
						   <script>
							  document.forms[0].goGetApproveButton.disabled=true;
							  document.forms[0].goSureButton.disabled=false;
							  document.forms[0].goHomeButton.disabled=true;
						   </script>
						 </c:if>
						 <c:if test="${mainEntity.actionStep=='ATSERROR'}">
						   <script>
							  document.forms[0].goGetApproveButton.disabled=true;
							  document.forms[0].goSureButton.disabled=true;
							  document.forms[0].goHomeButton.disabled=false;
						   </script>
						 </c:if>
						 <c:if test="${mainEntity.actionStep=='NONCENTERAUTH'}">
						   <script>
							  document.forms[0].goGetApproveButton.disabled=true;
							  document.forms[0].goSureButton.disabled=true;
							  document.forms[0].goHomeButton.disabled=false;
						   </script>
						 </c:if>
						 <script>
						    var form = document.forms[0];
							function clickApproveButton()
						    {
							   document.forms[0].goGetApproveButton.disabled=false;
							   document.forms[0].goSureButton.disabled=true;							   
							}
							function clickSureButton()
							{
							   document.forms[0].goGetApproveButton.disabled=true;
							   document.forms[0].goSureButton.disabled=false;
					        }
							function clieckforce0()
							{
							   alert("強制授權, 中心需承擔風險!!");
                               form['entity.manualForcePostFlag'].value="Y";
							   document.forms[0].goGetApproveButton.disabled=false;
							}
							function clieckforce1()
							{							   
                               form['entity.manualForcePostFlag'].value="N";   
							   document.forms[0].goGetApproveButton.disabled=true;
							}
						 </script>
                       </tr>
                       <tr>
                         <td class="field">授權者備註</td>
                         <td colspan="5">
                           <input type="text" name="entity.authorizeMemo" maxlength="80" size="81" value="" />
                         </td>
                       </tr>
                     </table>
                     </fieldset>
                     <script type='text/javascript' language='javascript'>
                         var inputBlock = new FoldingObj('inputBlock',false,'授權資料','../images/expand_mark.gif','../images/collapse_mark.gif');
                         autoFolding(dataBlock,false);
                     </script>                                                        
                     </div>
                   </td>
                 </tr>
               </table>		
               </div>                          
               </s:form>	
            </c:if>
		</div>
	</body>
</html>
<script>
	function goAuthLogQuery()
	{
		window.open("<c:url value='/auth/authorizerLogQuery.do?entity.pagingInfo.enablePaging=true' />","授權記錄查詢",
		            "width=800,height=600,menubar=yes,resizable=yes,scrollbars=yes");	
	}
	function radioAuthManuEntry()
	{
		var form = document.forms[0];
       	var r = form['entity.authManuEntry'];
		for(var i=0; i<r.length; i++)
		   if(r[i].checked)
		     return r[i].value;
	}
	function radioForceAuth()
	{
		var form = document.forms[0];
       	var r = form['entity.forceAuth'];
		for(var i=0; i<r.length; i++)
		   if(r[i].checked)
		     return i;
	}
	function radioAddFlag()
	{
		var form = document.forms[0];
       	var r = form['entity.addFlag'];
		for(var i=0; i<r.length; i++)
		   
		   if(r[i].checked)
		   {
		     return i;
		     }
	}
	function goGetApproveNo()
	{
	  // 20090424 芳英表示敲選連線授權後該按鈕及更正鍵便不可再點選 
	  document.forms[0].goGetApproveButton.disabled=true;
	  document.forms[0].goHomeButton.disabled=true;
		document.forms[0].action="<c:url value='/auth/mAuthorizerGetApproveNo.do'/>";
		document.forms[0].submit();
	}
	
	function goConfirmAuth()
	{
		var form = document.forms[0];
		if ( radioAddFlag() == 0 )
		{
		  if( form['entity.approvalNo'].value == "")
		  {
			// Y , C 需輸入
			if(radioAuthManuEntry()=="Y" ||radioAuthManuEntry()=="C")
			{
				alert("授權碼必須輸入 !!");
				return;
			}
		  }
		  else
	 	  {
			// 非Y且非 C 時,授權碼不可輸入
			if(radioAuthManuEntry()!="Y" && radioAuthManuEntry()!="C")
			{
				alert("授權碼不可輸入 !!");
				return;
			}
		  }		
		}
		// D時,判斷來源為C1,B1
		if( radioAuthManuEntry()=="D" && form['entity.authorizeReason'].value!='C1' && form['entity.authorizeReason'].value!='B1' )
		{
			alert("授權接受否值為'D-未先刷卡'時，授權來源須為'C1-後端授權系統'，或'B1-BASE-24'");
			return;
		}
		document.forms[0].action="<c:url value='/auth/mAuthorizerCreateAuthorize.do'/>";
		document.forms[0].submit();
	}
	function goHome()
	{
		document.forms[0].action="<c:url value='/auth/goBackHomePf8.do'/>";
		document.forms[0].submit();
	}
	function checkForm()
			{					
				
				return false;
			}
			
			function initialForm()
			{
			    $(form).observe("submit", AjaxUtil.ajaxSubmitHandler.bindAsEventListener(form, checkForm) );
			    $('defaultSubmitButton').focus();
			}
			EdsEvent.addOnLoad( initialForm );
	
</script>
