<%--
 * ==============================================================================================
 * $Id$
 * ==============================================================================================
--%>
<%@page pageEncoding="UTF-8"%>
<%-- 此為AJAX查詢結果的頁面, 必須額外設定contentType --%>
<%@page import="java.text.SimpleDateFormat"%>
<%@include file="/jsp/content_type_ajax.jsp"%>
<%@include file="/jsp/directive.jsp"%>
<jsp:include page="/jsp/messages.jsp"/>


<s:set var="pagingInfo" value="%{entity.pagingInfo}" /> 
<s:set var="resultList" value="%{pagingInfo.resultList}"/>


<div style="width: 100%; text-align: center;">
	<jsp:include page="/jsp/pagingInfo.jsp"/> 
</div>

<table border="0" cellpadding="4" cellspacing="1" class="listTable"  align="center" >
	<thead>
		<tr>
			<th width=10%>序號</th>
			<th width=20%>日期</th>
			<th width="10%">使用者</th>
			<th width="20%">操件功能</th>
			<th width="40%">其它</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="item" items="${resultList}" varStatus="status">
		<c:set var="datetime" value="${item.uiDate }" />
		<tr class="<edstw:rowStyle value='${status.index}'/>">
			<td><c:out value="${status.index + 1}"/></td>
			<td>
				<%
				SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String dateString = f.format(pageContext.getAttribute("datetime"));
				%>
				<c:out value="<%= dateString %>" />
			</td>
			<td><c:out value="${item.userId}"/></td>
			<td><c:out value="${item.uiFunction}"/></td>
			<td><c:out value="${item.uiOther}"/></td>
		</tr>
		</c:forEach>
	</tbody>
</table>


<%-- <display:table uid="entity" cellpadding="4" cellspacing="1" sort="external" partialList="true" size="${pagingInfo.totalCount}" name="${pagingInfo}">
	<display:column title="序號" class="seqNo" value="${(pagingInfo.startIndex + entity_rowNum)-1}"/>
	<display:column title="日期" class="alignCenter" >
		<edstw:formatDate value="${entity.uiDate}" pattern="yyyy/MM/dd"/><br>		
	</display:column>
	<display:column title="時間" class="alignCenter" >		
		<edstw:formatDate value="${entity.uiDate}" pattern="HH:mm:ss"/>
	</display:column>
	<display:column title="使用者" class="alignCenter">
		<c:out value="${entity.userId}"/>
	</display:column>
	<display:column title="操件功能" class="alignCenter">
		<c:out value="${entity.uiFunction}"/>
	</display:column>
	<display:column title="其它" class="alignCenter">
		<c:out value="${entity.uiOther}"/>
	</display:column>
	<display:column title="交易伺服器" class="alignCenter">
		<c:out value="${entity.txnServer}"/>
	</display:column>
			
</display:table> 

<script type="text/javascript">
	//必須提供本函式, 將分頁資訊傳回.
	function createDisplayPagingInfo()
	{
		var dpi = new DisplayPagingInfo();
		//設定執行查詢的form物件.
		dpi.form = document.forms[0];
		//設定使用ajax方式查詢
		dpi.enableAjaxSubmit = true;
		//設定display tag用來設定頁數的參數名稱

		//設定原查詢條件所能查得的全部個數.
		dpi.totalCount = <c:out value='${pagingInfo.totalCount}'/>;
		//設定sever端用來放置分頁資訊的pagingInfo物件的屬性名稱.
		dpi.pagingInfoProperty = "entity.pagingInfo";
		return dpi;
	}
	
</script>
--%>