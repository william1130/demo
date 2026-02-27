<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!--
<meta http-equiv="Content-Language" content="zh-TW">
-->
<meta http-equiv="Context-Script-Type" content="text/javascript">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="-1">
<meta http-equiv="cache-control" content="no-cache">
<link href='<c:url value="/css/common.css"/>' rel="stylesheet" type="text/css">
<link href='<c:url value="/css/pane.css"/>' rel="stylesheet" type="text/css">
<link href='<c:url value="/css/folding.css"/>' rel='stylesheet' type='text/css'>
<link href='<c:url value="/css/project.css"/>' rel='stylesheet' type='text/css'>
<link href='<c:url value="/css/widget.css"/>' rel='stylesheet' type='text/css'>
<script type="text/javascript" src="<c:url value='/scripts/jquery-3.5.1.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
<%@include file="/scripts/js_message.jsp"%>
<script type="text/javascript" src="<c:url value='/scripts/fastinit.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/tablekit.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/common.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/project.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/widget.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/ajax_function.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/folding.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validate.js'/>"></script>
<%--
<script type="text/javascript" src="<c:url value='/scripts/firebug/firebug.js'/>"></script>
--%>
<script type="text/javascript" src="<c:url value='/scripts/rico/rico.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/rico/ricoCommon.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/rico/ricoStyles.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/scriptaculous/scriptaculous.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/scriptaculous/effects.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/scriptaculous/builder.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/prado/prado.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/prado/scriptaculous-adapter.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/prado/controls/controls.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/prado/datepicker/datepicker.js'/>"></script>
<link href='<c:url value="/css/prado/prado.css"/>' rel='stylesheet' type='text/css'>
<link href='<c:url value="/scripts/prado/datepicker/default.css"/>' rel='stylesheet' type='text/css'>

<%
	/* pageContext.setAttribute("para_autoClose", Util.checkBooleanValue(request.getParameter("autoClose")));
	pageContext.setAttribute("para_timer", Util.checkIntValue(request.getParameter("timer")));
	pageContext.setAttribute("para_refreshOpener", Util.checkBooleanValue(request.getParameter("refreshOpener"))); */
%>

<script type="text/javascript">
	var rootPath = "<c:url value="/" />";
	var timeoutId;
	function closeCurrentWindow()
	{
		if( timeoutId )
		{
			window.clearTimeout(timeoutId);
		}
		closeOpenerIFrameDialog();
	}	
	
	EdsEvent.addOnLoad(
		function()	{			
			<c:if test="${para_autoClose}">
				var timer = 3;
				
				<c:if test="${not empty para_timer}" >
					timer = <c:out value="${para_timer}"/>;
				</c:if>
				$('autoCloseBlock').update("<p align='center'>本視窗將於 " + timer + " 秒後自動關閉.</p>");
				timeoutId = window.setTimeout( closeCurrentWindow, timer*1000 );
			</c:if>;
			<c:if test="${para_refreshOpener}">			
				BrowserUtils.reloadOpener();
			</c:if>;
		}
	);
</script>
