<%--
 * ==============================================================================================
 * $Id: ajax_result.jsp,v 1.1 2014/01/02 02:54:49 asiapacific\hungmike Exp $
 * ==============================================================================================
--%>
<%@page pageEncoding="UTF-8" isELIgnored="false" contentType="text/html;charset=UTF-8"%>
${ajaxResult.jsonString}
<%--
/*-secure-{
<c:forEach var="result" items="${ajaxResult.resultList}">
	"<c:out value="${result.name}"/>": "<c:out value="${result.value}"/>",
</c:forEach>
"_success": <c:out value='${ajaxResult.success}'/>,
"_message": "<c:out value='${ajaxResult.message}'/>"
}*/
--%>
<%--
<result success="<c:out value='${result.success}'/>">
	<result-value><c:out value="${result.resultValue}"/></result-value>
</result>
--%>