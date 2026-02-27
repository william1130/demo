<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/directive.jsp"%>

<c:set var="pageCount" value="${pagingInfo.totalCount}" />
<c:set var="currentPage" value="${pagingInfo.currentPage}" />
<span class="pagelinks"> [ <c:if test="${currentPage <= 1}"><< | < </c:if>
	<c:if test="${currentPage > 1}">
		<a href="javascript:gotoPage(1);"><<</a> |
		<a href="javascript:gotoPage(<c:out value="${currentPage - 1}"/>);"><</a>
	</c:if> ] <c:set var="pageIdxFrom" value="${currentPage - 7}" /> <c:set
		var="pageIdxTo" value="${currentPage + 7}" /> <c:if
		test="${pageIdxFrom < 1}">
		<c:set var="pageIdxFrom" value="${1}" />
		<c:set var="pageIdxTo" value="${15}" />
		<c:if test="${pageIdxTo > pageCount}">
			<c:set var="pageIdxTo" value="${pageCount}" />
		</c:if>
	</c:if> <c:if test="${pageIdxTo > pageCount}">
		<c:set var="pageIdxTo" value="${pageCount}" />
		<c:set var="pageIdxFrom" value="${pageIdxTo - 15}" />
		<c:if test="${pageIdxFrom < 1}">
			<c:set var="pageIdxFrom" value="${1}" />
		</c:if>
	</c:if> <c:forEach var="pageIdx" begin="${pageIdxFrom}" end="${pageIdxTo}">
		<c:if test="${pageIdx <= pageCount}">
			<c:if test="${pageIdx == currentPage}">
				<strong><c:out value="${pageIdx}" /></strong>
			</c:if>
			<c:if test="${pageIdx != currentPage}">
				<a href="javascript:gotoPage(<c:out value="${pageIdx}"/>);"><c:out
						value="${pageIdx}" /></a>
			</c:if>
		</c:if>
	</c:forEach> [ <c:if test="${currentPage >= pageCount}">> | >></c:if> <c:if
		test="${currentPage < pageCount}">
		<a href="javascript:gotoPage(<c:out value="${currentPage + 1}"/>);">></a> |
		<a href="javascript:gotoPage(<c:out value="${pageCount}"/>);">>></a>
	</c:if> ] 共 <c:out value="${pageCount}" /> 頁 <c:if test="${entity.dataTotalCount != 0}">(<c:out value="${entity.dataTotalCount}" />筆) </c:if>&nbsp;跳至<s:textfield name="jumpPageText" value="%{currentPage}" size="2" maxlength="3"></s:textfield>頁&nbsp;
	<!-- <html:button property="jumpPageButton" value="確定" onclick="gotoPage(document.getElementsByName('jumpPageText')[0].value)"></html:button> -->
	<input name="resetButton" type="button" value="確定" class="button" onclick="gotoPage(document.getElementsByName('jumpPageText')[0].value);">
</span>



<script type='text/javascript'>
	function gotoPage(pageIdx) {

		try {
			submitPage(pageIdx);
		} catch (e) {
			if (e.description){
				alert(e.description);
			}else{
				alert(e);
			}
		}

	}
</script>