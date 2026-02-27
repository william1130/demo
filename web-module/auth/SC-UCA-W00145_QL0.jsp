<%--
 * ==============================================================================================
 * $Id: SC-UCA-W00145_QL0.jsp,v 1.2 2017/04/26 05:59:15 leered Exp $
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
	  <s:form action="authorizerLogQuery">
	    <%-- 設定開啟分頁功能 --%>
	    <s:hidden name="entity.pagingInfo.enablePaging" value="true"/>
	    <s:hidden name="entity.submitAgain" value="Y"/>
	  </s:form>
		<edstw:folding foldingId="ajaxResultBlock" description="查詢結果" style="display: block">
			<fieldset>
				<div id="ajaxResultBlockContent">
					<jsp:include page="./SC-UCA-W00145_QL1.jsp"></jsp:include>
				</div>
			</fieldset>
		</edstw:folding>
	</body>
</html>

