<%--
 * ==============================================================================================
 * $Id: result.jsp,v 1.1 2014/11/14 03:14:24 asiapacific\hsiehes Exp $
 * ==============================================================================================
--%>
<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/directive.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html >
	<head>
		<title><c:out value='${currentProgramObj.name}'/></title>
		<%@include file="/jsp/header.jsp"%>	
	</head>
	<body>
		<div id="mainContentBlock">
			<div id="progHeader">
				<div id="progTitle" title="程式代碼 : <c:out value='${currentFunctionObj.id}'/>"><c:out value='${currentProgramObj.name}'/></div>
			</div>
			<%--
			若作業完成後沒有明確的顯示頁面時, 可直接forward至本JSP.
			--%>
			<jsp:include page="/jsp/messages.jsp"/>
			<script type="text/javascript">
				<%-- 若是獨立window, 則秀出關閉的按鈕. --%>
				if( opener == window )
					document.write("<p align='center'><input type='button' class='button' value='關閉本視窗' onclick='window.close()'/></p>");
				else
					document.write("<p align='center'><input type='button' class='button' value='關閉本視窗' onclick='closeOpenerIFrameDialog()'/></p>");
			</script>
		</div>
	</body>
</html>
