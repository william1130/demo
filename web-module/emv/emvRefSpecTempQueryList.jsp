
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="proj.nccc.logsearch.ProjConstants"%>
<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/content_type_ajax.jsp"%>
<%@include file="/jsp/directive.jsp"%>
<style>
	td{text-align:center;}
</style>
<s:set var="pagingInfo" value="%{entity.pagingInfo}" /> 
<s:set var="resultList" value="%{pagingInfo.resultList}"/>
<c:if test="${resultList.size() > 0 }">
<table border="0" cellpadding="4" cellspacing="1" class="listTable"  align="center" >
	<thead>
		<tr>
			<th width=5%>序號</th>
			<th width=25%>日期</th>
			<th width=10%>動作</th>
			<th width=10%>操作要求</th>
			<th width="50%">參考規格</th>
		</tr>
	</thead>
	<c:forEach var="item" items="${resultList}" varStatus="status">
	<c:set var="para" value="${resultTempList[status.index] }" />
	<c:set var="datetime" value="${item.requestInfo.date }" />
	<c:choose>
		<c:when test="${item.activeCode eq 'A' }" ><c:set var="actionName" value="新增" /></c:when>
		<c:when test="${item.activeCode eq 'U' }" ><c:set var="actionName" value="修改" /></c:when>
		<c:when test="${item.activeCode eq 'D' }" ><c:set var="actionName" value="刪除" /></c:when>
	</c:choose>
	<c:set var="specName" value="${item.specName}" />
	<c:set var="specID" value="${item.specID}" />
	<c:set var="uiLogOther" value="${actionName } : 參考規格[${item.specName}] " />
		<tr class="<edstw:rowStyle value='${status.index}'/>">
			<td>
				<c:out value="${status.index + 1}"/>
			</td>
			<td>
				<%
				SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String dateString = f.format(pageContext.getAttribute("datetime"));
				%>
				<c:out value="<%= dateString %>" />
			</td>
			<td>
			<a onclick="approveList('<%=pageContext.getAttribute("specID") %>','<%=pageContext.getAttribute("uiLogOther") %>')" title="核准" style="cursor:pointer;"><img src="../images/accept.png" width="25px"></a>
			<a onclick="rejectList('<%=pageContext.getAttribute("specID") %>','<%=pageContext.getAttribute("uiLogOther") %>')" title="拒絕" style="cursor:pointer;"><img src="../images/cancel.png" width="25px"></a>
			</td>
			<td><c:out value="${actionName}"/></td>
			<td style="text-align:left;">
			 <c:choose>
				<c:when test="${item.activeCode eq 'U' and not (item.specName eq para.specName) }">
				    <c:out value="${para.specName}"/><br />
					<span style="color:red;" ><c:out value="${item.specName}"/></span>
				</c:when>
				<c:otherwise>
					<c:out value="${item.specName}"/>
				</c:otherwise>
			</c:choose> 
			</td>
		</tr>
		
	</c:forEach>
	</tbody>
	
</table>

<s:form action="approveEmvRefSpecTemp" id="form1">
	<s:hidden name="entity.specID" value=""/>
	<s:hidden name="entity.uiLogOther" value=""/>
	<s:hidden name="entity.uiLogAction" value="APPROVE"/>
	<s:hidden name="entity.uiLogFunctionName" value="EMV_SPEC"/>
</s:form>

<s:form action="rejectEmvRefSpecTemp" id="form2">
	<s:hidden name="entity.specID" value=""/>
	<s:hidden name="entity.uiLogOther" value=""/>
	<s:hidden name="entity.uiLogAction" value="REJECT"/>
	<s:hidden name="entity.uiLogFunctionName" value="EMV_SPEC"/>
</s:form>
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

<script type="text/javascript">
function approveList(var1,var2){
	var form1 = document.forms["form1"];
	if( confirm('是否確定核准') ) {
		form1["entity.specID"].value = var1;
		form1["entity.uiLogOther"].value = var2;
		submitAndShowIFrameDialog(form1,{title: '<c:out value='${currentProgramObj.name}'/>'});
		
	}else{
		return false;
	}
}

function rejectList(var1,var2){
	var form2 = document.forms["form2"];
	if( confirm('是否確定拒絕') ) {
		form2["entity.specID"].value = var1;
		form2["entity.uiLogOther"].value = var2;
		submitAndShowIFrameDialog(form2,{title: '<c:out value='${currentProgramObj.name}'/>'});		
	}else{
		return false;
	}
}
</script>