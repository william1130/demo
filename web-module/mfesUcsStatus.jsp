<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/directive.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<%@include file="/jsp/header.jsp"%>
	<meta http-equiv="refresh" content="30">
	<title>MFES未結帳端末機查詢</title>
</head>

<c:set var="resultList" value="${resultList}"/>
<c:set var="actionResult" value="${actionResult}"/>

<body class="ieVersion">
	<div id="mainContentBlock" style="text-align: center;width: 600">
		<div id="progHeader">
			<div id="progTitle">MFES未結帳端末機查詢</div>
		</div>
		<jsp:include page="/jsp/messages.jsp" />
		<s:form action="mfesUncloseStat">
		<s:hidden name="mchtNo"/>
		<table border="0" cellpadding="2" cellspacing="1" class="listTable"
			align="center">
			<thead>
				<tr>
					<th width="20%">序號</th>
					<th width="80%">未結帳端末機代號</th>
				</tr>
			</thead>
			<tbody>
			<c:choose>
				<c:when test='${actionResult eq "Y"}'>
				<c:if test="${not empty resultList}"> 
					<c:forEach var="item" items="${resultList}" varStatus="status">
						<tr class="<edstw:rowStyle value='${status.index}'/>">
							<td align="center"><c:out value="${status.index + 1}"/></td>
							<td align="center"><c:out value="${item.itemValue}"/></td>
						</tr>
					</c:forEach>
				</c:if>
				<c:if test="${empty resultList}">
					<tr><td colspan =2><center><font Color = RED>查無未結帳之端末機代號</font></center></td></tr>
				</c:if>
				</c:when>
				<c:when test='${actionResult eq "Z"}'>
					<tr><td colspan =2><center><font Color = RED>查無資料或非MFES特店的請款代碼</font></center></td></tr>
				</c:when>
				<c:otherwise>
					<tr><td colspan =2><center><Color = RED>發生錯誤.</Color></center></td></tr>
				</c:otherwise>
			</c:choose>
			</tbody>
		</table>
		</s:form>
	</div>
</body>
</html>