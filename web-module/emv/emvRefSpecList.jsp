
<%@page import="proj.nccc.logsearch.ProjConstants"%>
<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/content_type_ajax.jsp"%>
<%@include file="/jsp/directive.jsp"%>
<style>
	td{text-align:center;}
</style>
<s:set var="pagingInfo" value="%{entity.pagingInfo}" /> 
<s:set var="resultList" value="%{pagingInfo.resultList}"/>

<div style="margin:10px 0;text-align:left;">
	<a 
	href="<c:url value='/emv/toCreateEmvRefSpec.do'/>"
	onclick="showIFrameDialog( this, {title: '<c:out value='參考規格維護'/>'}); return false;" title="新增">
	<input type="Button" name="reportButton" value="新增" class="button">
	</a>
</div>

<table border="0" cellpadding="4" cellspacing="1" class="listTable"  align="center" >
	<thead>
		<tr>
			<th width=20%>序號</th>
			<th width=20%>動作</th>
			<th width="60%">參考規格</th>
		</tr>
	</thead>
	<tbody>
	<c:forEach var="item" items="${resultList}" varStatus="status">
		<tr class="<edstw:rowStyle value='${status.index}'/>">
			<td>
				<c:out value="${status.index + 1}"/>
			</td>
			<td>
			<c:choose>
				<c:when test="${item.status eq 'Y'}">
				<a
				href="<c:url value='/emv/toModifyEmvRefSpec.do?entity.specID=${item.specID}'/>"
				onclick="showIFrameDialog( this, {title: '<c:out value='參考規格維護'/>'}); return false;"
				title="修改"><img src="../images/pen.gif"></a>
				<a 
				href="<c:url value='/emv/deleteEmvRefSpec.do?entity.specID=${item.specID}'/>"
				onclick="if( confirm('是否確定刪除') ) showIFrameDialog( this, {title: '<c:out value='EMV卡別維護'/>'}); return false;" title="刪除">
				<img src="../images/close.gif"></a>
				</c:when>
				<c:when test="${item.status eq 'A'}">覆核中</c:when>
				</c:choose>
			</td>
			<td style="text-align:left">
				<c:out value="${item.specName}"/>
			</td>
		</tr>
		
	</c:forEach>
	</tbody>
	
</table>

<%-- <display:table uid="entity" cellpadding="4" cellspacing="1"
	sort="external" partialList="false" size="${pagingInfo.totalCount}"
	name="${pagingInfo}">

	<display:column title="序號" class="seqNo"
		value="${(pagingInfo.startIndex + entity_rowNum)-1}" />
	<display:column title="動作" class="alignCenter" style="width:64">

		<c:if test="${entity.status eq 'Y' }">
			<a
				href="<c:url value='/para/toModifyEmvCardType.do?entity.cardType=${entity.cardType}'/>"
				onclick="showIFrameDialog( this, {title: '<c:out value='${currentProgramObj.name}'/>'}); return false;"
				title="修改"><img src="../images/pen.gif"></a>
		</c:if>
	</display:column>
	<display:column title="卡別" class="alignCenter">
		<c:out value="${entity.cardType}" />
	</display:column>
	<display:column title="狀態" class="alignCenter">
		<c:choose>
			<c:when test="${entity.status eq 'Y'}">正常</c:when>
			<c:when test="${entity.status eq 'A'}">覆核中</c:when>
		</c:choose>
	(<c:out value="${entity.status}" />)
	</display:column>

</display:table> --%>




