<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/directive.jsp"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@include file="/jsp/header.jsp"%>
<jsp:include page="../parseCode/tooltipIC.jsp" />
<jsp:include page="../parseCode/tooltipCrdVefyFlg.jsp" />
<jsp:include page="../parseCode/tooltipPOSEntryMode.jsp" />
<jsp:include page="../parseCode/tooltipStandIn.jsp" />
<script type="text/javascript" src="../scripts/addToolTip.js"></script>
<style>
.edsToolTip {
	background-color: #CCCCFF;
	opacity: 0.9;
}

.preClass {
  width:auto;
  max-width: 950px;
  white-space: pre-wrap; /* css-3 */
  white-space: -moz-pre-wrap; /* Mozilla, since 1999 */
  white-space: -pre-wrap; /* Opera 4-6 */
  white-space: -o-pre-wrap; /* Opera 7 */
  word-wrap: break-word; /* Internet Explorer 5.5+ */
  padding: 10px;
  word-break: break-all;
}	
		</style>

</head>

<c:set var="respRole" value="" />
<body class="ieVersion">
	<div  style="text-align: center;width: 90%;margin:0 auto">

		<jsp:include page="/jsp/messages.jsp" />
		<div id='criteriaBlock' class='foldingBlock' style="width: 100%">
		<c:forEach var="item" items="${resultList}" varStatus="status">
		<table border="0" cellpadding="4" cellspacing="1" class="listTable">
			<thead>
				<tr>
					<th width="100%" style="text-align:left;">
						<c:out value="${item.rawType}"/> (<c:out value="${item.logDate }" /> <c:out value="${item.logTime }" />)
					</th>
					
				</tr>
			</thead>
			<tbody>
				<tr class="oddRow">
					<td class="alignLeft">
					<div class="preClass">
<c:out value="${item.rawData}"/>
					</div>
					</td>
				</tr>
			</tbody>
		</table>
		<br />
		</c:forEach>
		</div>
	</div>
	<br />


</body>
</html>