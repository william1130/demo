
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
<s:set var="resultTempList" value="%{pagingInfo.resultTempList }"/>
<c:if test="${resultList.size() > 0 }">
<table border="0" cellpadding="4" cellspacing="1" class="listTable"  align="center" >
	<thead>
		<tr>
			<th width=5%>序號</th>
			<th width=15%>日期</th>
			<th width=8%>動作</th>
			<th width=8%>操作要求</th>
			<th width="10%">卡別</th>
			<th width="10%">TAG名稱</th>
			<th width="15%">TAG全名</th>
			<th width="5%">縮寫</th>
			<th width="20%">描述</th>
			<th width="5%">異動<br />明細</th>
		</tr>
	</thead>
	<c:set var="tempTag" value="" />
		<c:forEach var="item" items="${resultList}" varStatus="status">
			<c:if test="${tempTag eq ''}">
				<c:set var="tempTag" value="${item.emvTag }" />
			</c:if>
			<c:if test="${item.emvTag eq tempTag}">
			</c:if>
			<c:set var="countTag" value="1" />
			<%--<s:set var="para" value="%{resultTempList.get(status.index) }"/> --%>
			<c:set var="para" value="${resultTempList[status.index] }" />
			<%-- <c:set var="para" value="${emvTagRecordTempForm.resultTempList[status.index] }" /> --%>
			<c:set var="datetime" value="${item.requestInfo.date }" />
			<c:choose>
				<c:when test="${item.activeCode eq 'A' }">
					<c:set var="actionName" value="新增" />
				</c:when>
				<c:when test="${item.activeCode eq 'U' }">
					<c:set var="actionName" value="修改" />
				</c:when>
				<c:when test="${item.activeCode eq 'D' }">
					<c:set var="actionName" value="刪除" />
				</c:when>
			</c:choose>
			<c:set var="emvTag" value="${item.emvTag}" />
			<c:set var="sameValueFlag" value="${item.sameValueFlag}" />
			<c:if test="${sameValueFlag eq 'Y' }">
				<c:set var="uiLogOther"
					value="${actionName } : TAG名稱[${item.emvTag}] " />
			</c:if>
			<c:if test="${ not (sameValueFlag eq 'Y') }">
				<c:set var="uiLogOther"
					value="${actionName } : TAG名稱[${item.emvTag}],卡別[${item.sameValueFlag}] " />
			</c:if>
			<tr class="<edstw:rowStyle value='${status.index}'/>">
				<td><c:out value="${status.index + 1}" /></td>
				<td>
					<%
						SimpleDateFormat f = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss");
						String dateString = f.format(pageContext .getAttribute("datetime"));
					%> 
					<c:out value="<%=dateString%>" />
				</td>
				<td><a
					onclick="approveList('<%=pageContext.getAttribute("emvTag")%>','<%=pageContext.getAttribute("sameValueFlag")%>','<%=pageContext.getAttribute("uiLogOther")%>')"
					title="核准" style="cursor: pointer;"><img src="../images/accept.png" width="25px"></a> <a
					onclick="rejectList('<%=pageContext.getAttribute("emvTag")%>','<%=pageContext.getAttribute("sameValueFlag")%>','<%=pageContext.getAttribute("uiLogOther")%>')"
					title="拒絕" style="cursor: pointer;"><img src="../images/cancel.png" width="25px"></a></td>
				<td><c:out value="${actionName}" /></td>
				<td><c:choose>
						<c:when
							test="${item.activeCode eq 'U' and not (item.cardType eq para.cardType) }">
							<c:out value="${para.cardType}" />
							<br />
							<span style="color: red;"><c:out value="${item.cardType}" /></span>
						</c:when>
						<c:otherwise>
							<c:out value="${item.cardType}" />
						</c:otherwise>
					</c:choose></td>
				<td><c:choose>
						<c:when
							test="${item.activeCode eq 'U' and not (item.emvTag eq para.emvTag) }">
							<c:out value="${para.emvTag}" />
							<br />
							<span style="color: red;"><c:out value="${item.emvTag}" /></span>
						</c:when>
						<c:otherwise>
							<c:out value="${item.emvTag}" />
						</c:otherwise>
					</c:choose></td>
				<td><c:choose>
						<c:when
							test="${item.activeCode eq 'U' and not (item.emvName eq para.emvName) }">
							<c:out value="${para.emvName}" />
							<br />
							<span style="color: red;"><c:out value="${item.emvName}" /></span>
						</c:when>
						<c:otherwise>
							<c:out value="${item.emvName}" />
						</c:otherwise>
					</c:choose></td>
				<td><c:choose>
						<c:when
							test="${item.activeCode eq 'U' and not (item.emvAbbr eq para.emvAbbr) }">
							<c:out value="${para.emvAbbr}" />
							<br />
							<span style="color: red;"><c:out value="${item.emvAbbr}" /></span>
						</c:when>
						<c:otherwise>
							<c:out value="${item.emvAbbr}" />
						</c:otherwise>
					</c:choose></td>
				<td><c:choose>
						<c:when
							test="${item.activeCode eq 'U' and not (item.emvDesc eq para.emvDesc) }">
							<c:out value="${para.emvDesc}" />
							<br />
							<span style="color: red;"><c:out value="${item.emvDesc}" /></span>
						</c:when>
						<c:otherwise>
							<c:out value="${item.emvDesc}" />
						</c:otherwise>
					</c:choose></td>
				<td><c:if test="${item.activeCode eq 'U'}">
						<a
				href="<c:url value='/emv/toShowModDetail.do?entity.emvTag=${item.emvTag}&entity.sameValueFlag=${item.sameValueFlag }'/>"
				onclick="showIFrameDialog( this, {width:'900', height:'450', title: '<c:out value='異動明細'/>'}); return false;" title="修改"><img src="../images/search.gif"></a>
			
					</c:if></td>
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