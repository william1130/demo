<%--
 * ==============================================================================================
 * $Id: SC-UCA-W00145_Q01.jsp,v 1.2 2017/04/26 05:59:15 leered Exp $
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
	  <style type="text/css">
			.article {
                        BORDER-BOTTOM: black 1px solid; 
                        BORDER-LEFT: black 1px solid; 
                        BORDER-RIGHT: black 1px solid; 
                        BORDER-TOP: black 1px solid;  
                        POSITION: absolute; 
                        background-color:#CCFF99;
                        padding-top:5px;
                        padding-bottom:5px;
                        padding-left:5px;
                        padding-right:5px;
                        width: 200px;
                      } 
      </style>
      <script language="javascript" type="text/javascript">
         <!--//
         function ShowLoyaltyObj(divid) 
         { 
            BrowserUtils.hideSelectOnIE();
            var divObj = $(divid);
            divObj.style.display='';
            divObj.style.left = 480;
            var mainContentObj = $('ajaxResultBlockContent');
            divObj.style.top=window.event.y-150;
         } 
         function ShowCardObj(divid) 
         { 
            BrowserUtils.hideSelectOnIE();
            //document.getElementById(divid).style.display='';
            //取得顯示的div物件
            var divObj = $(divid);
            //將物件的設為顯示在頁面上
            divObj.style.display='';
            //將物件左邊位置設定為220px
            divObj.style.left = 220;
            //取得bodyframe物件id
            var mainContentObj = $('ajaxResultBlockContent');
            divObj.style.top=window.event.y-350;
         } 
         function ShowLIObj(divid)
         {
            BrowserUtils.hideSelectOnIE();
            var divObj = $(divid);
            divObj.style.display='';
         }
         function HideObj(divid) 
         { 
            BrowserUtils.showSelectOnIE();
            document.getElementById(divid).style.display='none';
         } 
         //-->
      </script>
	</head>
	<body>
	  <div id="mainContentBlock" style="width:1000">
	  <div id="progHeader">
		<div id="progTitle" title="程式代碼 : <c:out value='${currentFunctionObj.id}'/>"><c:out value='${currentFunctionObj.name}'/></div>
	  </div>
	  <jsp:include page="/jsp/messages.jsp"/>
	  <s:form action="queryAuthLogByAuthTrans">
	  <%-- 設定開啟分頁功能 --%>
	  <s:hidden name="entity.pagingInfo.enablePaging" value="true"/>
	  <edstw:folding foldingId="criteriaBlock" description="查詢條件">
		<fieldset>
		<legend>查詢條件</legend>
		<table border="0" align="center" cellpadding="4" cellspacing="0">
          <tr>
            <th class="require" scope="row">*卡號</th>
			<td colspan=5>
              <s:textfield name="entity.cardNo" maxlength="19" size="24"/>									
			</td>
          </tr>
          <tr>
            <th  class="optional">卡號錯誤強制查詢</th>
			<td colspan=5>
              <s:checkboxlist name="entity.bolSuppCardNoError" list="#{'Y'}"/>
			</td>
          </tr>
          <tr>
            <th  class="optional">授權碼</th>
			<td colspan=5>
              <s:textfield name="entity.approvalNo" maxlength="6" size="8"/>
			</td>
          </tr>
          <tr>
			<th class="optional">消費日期</th>
			<td colspan=5>
			  <s:textfield name="entity.purchaseDate1" maxlength="8" size="10" title="請輸入西元年(YYYYMMDD)"/>
			  <img id="_entity.purchaseDate1" class="datePickerImage" src="../images/dateIcon.gif" border="0" align="bottom" width="19px" height="19px" title="選擇日期" alt="選擇日期"/>
			  <script type='text/javascript' language='javascript'>
				 var form = document.forms[0];
				 new DatePicker( {imageId:'_entity.purchaseDate1', dateField:form['entity.purchaseDate1']} );
			  </script>
			  &nbsp;&nbsp;~&nbsp;&nbsp;
			  <s:textfield name="entity.purchaseDate2" maxlength="8" size="10" title="請輸入西元年(YYYYMMDD)"/>
			  <img id="_entity.purchaseDate2" class="datePickerImage" src="../images/dateIcon.gif" border="0" align="bottom" width="19px" height="19px" title="選擇日期" alt="選擇日期"/>
			  <script type='text/javascript' language='javascript'>
		         var form = document.forms[0];
				 new DatePicker( {imageId:'_entity.purchaseDate2', dateField:form['entity.purchaseDate2']} );
			  </script>
		    </td>
		  </tr>
		  <tr>
			<td colspan="6" class="buttonCell">
			  <input id="defaultSubmitButton" type="submit" name="submitButton" value="確定" class="button">
			  <input type="button" name="submitButton" value="產生報表" onclick="genReport();" class="button">
			  <s:select name="entity.reportType" list="#{'PDF':'PDF','EXCEL':'EXCEL'}"/>									
			</td>
		  </tr>
		</table>
		</fieldset>
	  </edstw:folding>
	  <script>
		 function checkForm(form)
		 {
			var msg = "資料錯誤:\n\n";
			var warnMsg = "注意:\n\n";
			if( form['entity.cardNo'].value == "")
				msg = msg + "卡號必須輸入.\n";

            if (form['entity.purchaseDate1'].value != "" && String(form['entity.purchaseDate1'].value).length != 8)
                msg = msg + "消費日期起必須輸入8位.\n";     
            else
                 if (form['entity.purchaseDate1'].value != "" &&  String(form['entity.purchaseDate1'].value).length == 8 && !(JSIsDate(form['entity.purchaseDate1'].value)))                                            
                     msg = msg + "消費日期起必須輸入有效之日期.\n"; 

            if (form['entity.purchaseDate2'].value != "" && String(form['entity.purchaseDate2'].value).length != 8)
                msg = msg + "消費日期迄必須輸入8位.\n";     
            else
                 if (form['entity.purchaseDate2'].value != "" &&  String(form['entity.purchaseDate2'].value).length == 8 && !(JSIsDate(form['entity.purchaseDate2'].value)))                                            
                     msg = msg + "消費日期迄必須輸入有效之日期.\n";            
                                        
            if (form['entity.purchaseDate1'].value != "" && form['entity.purchaseDate2'].value != "" && 
                parseInt(form['entity.purchaseDate2'].value ,10) < parseInt(form['entity.purchaseDate1'].value,10))
                msg = msg + "消費日期迄必須大於消費日期起.\n";                                                    
                                                
			if( msg.length > 12 )
			{
				alert( msg );
				return false;
			}
			return true;
			/**
			else
			{
				ajaxFormSubmit( {form:this.form} );
			}
			**/
		}
                                
        function JSIsDate(sDate)
        {
            yea=sDate.substr(0,4);
            mon=sDate.substr(4,2);       
            da=sDate.substr(6,2);
            if( !isNumber(parseInt(yea,10)) || !isNumber(parseInt(mon,10)) || !isNumber(parseInt(da,10)) )
                 return false;
            if( (parseInt(yea,10) < 1899) || (parseInt(yea,10) > 2099) ) 
                 return false;
            if( (parseInt(mon,10) < 1) || (parseInt(mon,10) > 12) ) 
                 return false;
            if (parseInt(da,10) < 1) 
                 return false;                                          
            if( ((parseInt(mon,10) == 1) || (parseInt(mon,10) == 3) || (parseInt(mon,10) == 5) || (parseInt(mon,10) == 7) || 
                 (parseInt(mon,10) == 8) || (parseInt(mon,10) == 10) || (parseInt(mon,10) == 12)) && (parseInt(da,10) > 31) )              
                 return false;     
            if( ((parseInt(mon,10) == 2) || (parseInt(mon,10) == 4) || (parseInt(mon,10) == 6) || (parseInt(mon,10) == 9) || 
                 (parseInt(mon,10) == 11)) && (parseInt(da,10) > 30) )              
                 return false;                                   
            if(parseInt(mon,10) == 2)
            {             
               if(( (parseInt(yea,10) % 4) == 0) && (parseInt(da,10) > 29) )
                     return false;          
               else
                   if((parseInt(yea,10) % 4 != 0) && (parseInt(da,10) > 28))       
                       return false; 
            }
            return true;
        }
                
        function genReport()
        {
 	       document.forms[0].action="<c:url value='/auth/queryAuthLogAuthTransReport.do'/>";
	       document.forms[0].submit();
	       document.forms[0].action="<c:url value='/auth/queryAuthLogByAuthTrans.do'/>";
        }
        
        function initialForm()
		{
		    var form = document.forms[0];
		    $(form).observe("submit", AjaxUtil.ajaxSubmitHandler.bindAsEventListener(form, checkForm) );
		    $('defaultSubmitButton').focus();
		}
		
		EdsEvent.addOnLoad( initialForm );
	  </script>
	  </s:form>
	  <edstw:folding foldingId="ajaxResultBlock" description="查詢結果">
	  <fieldset>
		<legend>查詢結果</legend>
		<div id="ajaxResultBlockContent"></div>
	  </fieldset>                                
	  </edstw:folding>
	  </div>
	</body>
</html>

