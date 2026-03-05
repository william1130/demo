<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/directive.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<%@include file="/jsp/header.jsp"%>
</head>

<c:set var="resultList" value="${parseCodeForm.resultList}"/>

<body class="ieVersion">
	<div id="mainContentBlock" style="text-align: center;width: 100%">
	
		<jsp:include page="/jsp/messages.jsp"/>
		
	
		<div id='criteriaBlock' class='foldingBlock' style="width: 90%">

			<table border="0" cellpadding="2" cellspacing="1" class="listTable"
				align="center">
				<thead>
					<tr>
						<th width="10%">Byte</th>
						<th width="10%">Bit</th>
						<th width="10%">長度</th>
						<th width="20%">值</th>
						<th width="50%">說明</th>
					</tr>
				</thead>
				<tbody>
				
				<c:forEach var="item" items="${resultList}" varStatus="status">
				
					<tr class="<edstw:rowStyle value='${status.index}'/>">
						<td align="center"><c:out value="${item.byteIndex}"/></td>
						<td align="center"><c:out value="${item.bitIndex}"/></td>
						<td align="center"><c:out value="${item.bitLength}"/></td>
						<td align="center"><c:out value="${item.value}"/></td>
						<td align="left"><c:out value="${item.desc}" escapeXml="false"/></td>
					</tr>
					
				</c:forEach>
				
				</tbody>
			</table>
		</div>
	
	</div>
	
</body>
</html>