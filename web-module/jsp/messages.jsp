<%--
 * ---------------------------------------------------------------------------------------------------
 * $Id: messages.jsp,v 1.1 2017/01/19 10:10:16 linsteph2\cvsuser Exp $
 * ---------------------------------------------------------------------------------------------------
--%>
<%@page pageEncoding="MS950"%>
<%-- 此頁有可能是AJAX查詢結果的頁面, 必須額外設定contentType --%>
<%@include file="/jsp/content_type_ajax.jsp"%>
<%@include file="/jsp/directive.jsp"%>
<%-- fieldErrors一定要用isEmpty()才能判斷, actionErrors與actionMessages則不用 --%>
<s:if test="%{not fieldErrors.isEmpty() or not actionErrors.empty}">
	<div id="errorMessageBlock">
		<table border="0" cellspacing="0" cellpadding="5" class="errorMessageTable">
			<tr>
				<th colspan="3">Error Message</th>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td class="alignLeft nowrap">
					<s:fielderror />
					<s:actionerror />
				</td>
			</tr>
		</table>
	</div>
	<br/>
</s:if>
<s:if test="%{not actionMessages.empty}">
	<div id="messageBlock">
		<table border="0" cellspacing="0" cellpadding="5" class="messageTable">
			<tr>
				<th colspan="3">Message</th>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td class="alignLeft nowrap">
					<s:actionmessage />
				</td>
			</tr>
		</table>
	</div>
	<br/>
</s:if>
<s:if test="%{not #request['com.edstw.web.extraMessage'].empty}">
	<s:set var="extraMessage" value="#request['com.edstw.web.extraMessage']"/>
	<div id="${extraMessage.errorMessage ? "errorMessageBlock" : "extraMessageBlock"}">
		<table cellspacing="0" cellpadding="5" class="${extraMessage.errorMessage ? "errorMessageTable" : "extraMessageTable"}">
			<tr>
				<td><c:out value="${extraMessage.message}"/></td>
			</tr>
			<c:if test="${not empty extraMessage.messageDetails}">
				<tr>
					<td>
						<ul>
							<c:forEach var="msg" items="${extraMessage.messageDetails}">
								<%-- 使用extraMessage時, 允許使用format, 所以escapeXml要設定為false. --%>
								<li><c:out value="${msg}" escapeXml="false"/></li>
							</c:forEach>
						</ul>
					</td>
				</tr>
			</c:if>
		</table>
	</div>
	<br/>
</s:if>
<s:if test="%{not actionErrors.empty or not actionMessages.empty or not fieldErrors.isEmpty() or not #request['com.edstw.web.extraMessage'].empty}">
<script type="text/javascript">
//TODO: 此動作應在載入之後做, 但須考慮, 若直接呼叫, 則在畫面還未載入完畢前, 可能已經啟動, 而造成失敗.
function showMessageBlock()
{
	if( $('errorMessageBlock') != null )
	{
		Rico.Corner.round( $('errorMessageBlock'),{color: 'transparent'} );
	}
	if( $('messageBlock') != null )
	{
		Rico.Corner.round( $('messageBlock'),{color: 'transparent'} );
	}
	if( $('extraMessageBlock') != null )
	{
		Rico.Corner.round( $('extraMessageBlock'),{color: 'transparent'} );
	}
}
//當page載入完畢時, 呼叫showMessageBlock().
//TODO : 若是以ajax的型式載入時, 因為page已存在, showMessageBlock不會被呼叫.
EdsEvent.addOnLoad( showMessageBlock );
</script>
</s:if>