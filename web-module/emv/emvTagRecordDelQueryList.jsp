
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="proj.nccc.logsearch.ProjConstants"%>
<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/content_type_ajax.jsp"%>
<%@include file="/jsp/directive.jsp"%>

<style>
	#queryList td{text-align:center;}
</style>
<s:set var="pagingInfo" value="%{entity.pagingInfo}" /> 
<s:set var="resultList" value="%{pagingInfo.resultList}"/>
<c:if test="${resultList.size() > 0 }">
<div style="width: 100%; text-align: center;">
	<jsp:include page="/jsp/pagingInfo.jsp"/>
</div> 
<table border="0" cellpadding="4" cellspacing="1" class="listTable"  align="center" id="queryList">
	<thead>
		<tr>
			<th rowspan="2" width=10%></th>
			<th width=20%>TAG名稱</th>
			<th width=30%>TAG全名</th>
			<th width=15%>縮寫</th>
			<th width="25%">卡別</th>
		</tr>
		<tr>
			<th colspan=4>描述</th>
		</tr>
	</thead>
	<tbody>
	<c:forEach var="item" items="${resultList}" varStatus="status">
		<tr class="<edstw:rowStyle value='${status.index}'/>">
			<td rowspan=2>
				<c:if test="${item.status eq 'A' }">
				覆核中
				</c:if>
				<c:if test="${item.status eq 'Y' }">
				<a 
				href="<c:url value='/emv/deleteEmvTagRecord.do?entity.emvTag=${item.emvTag}&entity.sameValueFlag=${item.sameValueFlag}'/>"
				onclick="if( confirm('是否確定刪除') ) showIFrameDialog( this, {title: '<c:out value='EMV卡別維護'/>'}); return false;" title="刪除">
				<img src="../images/close.gif"></a>
				</c:if>
			</td>
			<td><c:out value="${item.emvTag}"/></td>
			<td><c:out value="${item.emvName}"/></td>
			<td><c:out value="${item.emvAbbr}"/></td>
			<td>		
				<c:out value="${item.cardType}"/>
			</td>
		</tr>
		<tr>
			<td colspan=4><c:out value="${item.emvDesc}"/></td>
		</tr>
	</c:forEach>
	</tbody>
	
</table>

<s:form action="approveEmvTagRecordTemp" id="form1">
	<s:hidden name="entity.emvTag" value=""/>
	<s:hidden name="entity.sameValueFlag" value=""/>
	<s:hidden name="entity.uiLogOther" value=""/>
	<s:hidden name="entity.uiLogAction" value="APPROVE"/>
	<s:hidden name="entity.uiLogFunctionName" value="EMV_PARA"/>
</s:form>

<s:form action="rejectEmvTagRecordTemp" id="form2">
	<s:hidden name="entity.emvTag" value=""/>
	<s:hidden name="entity.sameValueFlag" value=""/>
	<s:hidden name="entity.uiLogOther" value=""/>
	<s:hidden name="entity.uiLogAction" value="REJECT"/>
	<s:hidden name="entity.uiLogFunctionName" value="EMV_PARA"/>
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
function approveList(var1,var2,var3){
	var form1 = document.forms["form1"];
	if( confirm('是否確定核准') ) {
		form1["entity.emvTag"].value = var1;
		form1["entity.sameValueFlag"].value = var2;
		form1["entity.uiLogOther"].value = var3;
		submitAndShowIFrameDialog(form1,{title: '<c:out value='${currentProgramObj.name}'/>'});
		
	}else{
		return false;
	}
}

function rejectList(var1,var2,var3){
	var form2 = document.forms["form2"];
	if( confirm('是否確定拒絕') ) {
		form2["entity.emvTag"].value = var1;
		form2["entity.sameValueFlag"].value = var2;
		form2["entity.uiLogOther"].value = var3;
		submitAndShowIFrameDialog(form2,{title: '<c:out value='${currentProgramObj.name}'/>'});		
	}else{
		return false;
	}
}
</script>