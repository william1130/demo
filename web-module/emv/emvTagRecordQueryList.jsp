
<%@page import="java.text.SimpleDateFormat"%>

<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/content_type_ajax.jsp"%>
<%@include file="/jsp/directive.jsp"%>

<style>
	td{text-align:center;}
</style>

<s:set var="pagingInfo" value="%{entity.pagingInfo}" /> 
<s:set var="resultList" value="%{pagingInfo.resultList}"/>
<c:if test="${resultList.size() > 0 }">
<div style="width: 100%; text-align: center;">
	<jsp:include page="/jsp/pagingInfo.jsp"/>
</div> 
<table border="0" cellpadding="4" cellspacing="1" class="listTable"  align="center" >
	<thead>
		<tr>
			<th width=5%>序號</th>
			<th width=15%>TAG</th>
			<th width=55%>Tag Desc.</th>
			<th width=25%>CardType</th>
		</tr>
	</thead>
		<c:forEach var="item" items="${resultList}" varStatus="status">

			<tr class="<edstw:rowStyle value='${status.index}'/>">
				<td><c:out value="${status.index + 1}" /></td>
<%-- 				<td><a
					onclick="approveList('<%=pageContext.getAttribute("emvTag")%>','<%=pageContext.getAttribute("sameValueFlag")%>','<%=pageContext.getAttribute("uiLogOther")%>')"
					title="核准" style="cursor: pointer;"><img src="../images/accept.png" width="25px"></a> <a
					onclick="rejectList('<%=pageContext.getAttribute("emvTag")%>','<%=pageContext.getAttribute("uiLogOther")%>')"
					title="拒絕" style="cursor: pointer;"><img src="../images/cancel.png" width="25px"></a></td>--%>
 				<td><a
				href="<c:url value='/emv/toShowDetail.do?entity.emvTag=${item.emvTag}&entity.sameValueFlag=${item.sameValueFlag }'/>"
				onclick="showIFrameDialog( this, {width:800, height:'480', title: '<c:out value='${item.emvTag}'/>'}); return false;"
				title="修改"><c:out value="${item.emvTag}" /></a></td>
				<td style="text-align:left"><a
				href="<c:url value='/emv/toShowDetail.do?entity.emvTag=${item.emvTag}&entity.sameValueFlag=${item.sameValueFlag }'/>"
				onclick="showIFrameDialog( this, {width:800, height:'480', title: '<c:out value='${item.emvTag}'/>'}); return false;"
				title="修改"><c:out value="${item.emvDesc}" /></a></td>
				<td><a
				href="<c:url value='/emv/toShowDetail.do?entity.emvTag=${item.emvTag}&entity.sameValueFlag=${item.sameValueFlag }'/>"
				onclick="showIFrameDialog( this, {width:800, height:'480', title: '<c:out value='${item.emvTag}'/>'}); return false;"
				title="修改"><c:out value="${item.cardType}" /></a></td>
			</tr>

		</c:forEach>
		</tbody>
	
</table>


</c:if>
<c:if test="${resultList.size() == 0 }">
<table border="0" cellpadding="4" cellspacing="1" class="listTable"  align="center" >
	<thead>
	<tr>
		<th>查無資料</th>
	</tr>
	</thead>
</table>
</c:if>

