<%--
 * ==============================================================================================
 * $Id: emvQuery.jsp,v 1.1 2017/01/19 10:09:17 linsteph2\cvsuser Exp $
 * ==============================================================================================
--%>
<%@page import="proj.nccc.logsearch.ProjConstants"%>
<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/directive.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title><c:out value='${currentProgramObj.name}'/></title>
		<%@include file="/jsp/header.jsp"%>	
	</head>
	<body <%-- background='<c:url value="/images/background.png" />' --%>>
		<div id="mainContentBlock" style="">
			<!--  div id="progHeader">
				<div id="progTitle" title="程式代碼 : <c:out value='${currentFunctionObj.id}'/>"><c:out value='${currentProgramObj.name}'/></div>
			</div -->
			<jsp:include page="/jsp/messages.jsp"/>
			<%-- <html:form action="/emv/toEmvIndex.do"> 
				<a id='opLink' href='<c:url value="/emv/emvIndex.jsp"/>'>1111111111</a>
			</html:form>  --%>
			
		</div>
		<script type='text/javascript'>
		    function initialForm() {
				toOP();
		    }
		    EdsEvent.addOnLoad( initialForm );
		   
		   function toOP() {
		    	/* var url = $j("#opLink").attr("href"); */
		    	var url = "../emv/emvIndex.jsp";
//				url += "?txnServer=TXN1";	    	
				window.open(url, "_blank" , "fullscreen=yes,status=yes,toolbar=yes,location=yes, scrollbars=yes, resizable=yes,menubar=yes");
		    }
		</script>
	</body>
</html>
