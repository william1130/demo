<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/directive.jsp"%>
<%
	request.setAttribute("newLineChar", "\n");
    request.setAttribute("spaceChar", " ");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@include file="/jsp/header.jsp"%>
<jsp:include page="../parseCode/tooltipIC.jsp"/>
<jsp:include page="../parseCode/tooltipCrdVefyFlg.jsp"/>
<jsp:include page="../parseCode/tooltipPOSEntryMode.jsp"/>
<jsp:include page="../parseCode/tooltipStandIn.jsp"/>
<script type="text/javascript" src="../scripts/addToolTip.js"></script>
</head>


<body class="ieVersion">
	<div id="mainContentBlock" style="text-align: center;width: 98%">

		<jsp:include page="/jsp/messages.jsp" />
		
		<div id='criteriaBlock' class='foldingBlock' style="width: 98%">

			<table border="0" cellpadding="4" cellspacing="1" class="listTable" style="table-layout: fixed">
				<thead>
					<tr>
						<th width="10%" style="word-break: break-all;"></th>
						<th width="10%" style="word-break: break-all;">Alphabetic <br/>Currency Code </th>
						<th width="10%" style="word-break: break-all;">Numeric <br/>Currency Code </th>
						<th width="10%" style="word-break: break-all;">Exchange Rate</th>
						<th width="10%" style="word-break: break-all;">Invert Rate</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="item" items="${resultList}" varStatus="status">
						<tr class="<edstw:rowStyle value='${status.index}'/>">
							<td class="alignCenter"><c:out value="${status.index + 1}"/></td>
							<td class="alignLeft"><c:out value="${item.type}" /></td>
							<td class="alignLeft"><c:out value="${item.title}" /></td>
							<td class="alignLeft"><c:out value="${item.desc}" /></td>
							<td class="alignLeft"><c:out value="${item.value}" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<br />
	</div>
</body>
</html>