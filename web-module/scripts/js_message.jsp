<%--
 * ==============================================================================================
 * $Id: js_message.jsp,v 1.1 2014/11/14 03:14:12 asiapacific\hsiehes Exp $
 * ==============================================================================================
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.*;"%>
<%@include file="/jsp/directive.jsp"%>
<fmt:setBundle basename="js-message" scope="page" var="jsMessage" />
<c:set var="rb" value="${jsMessage.resourceBundle}"/>
<jsp:useBean id="rb" type="java.util.ResourceBundle" scope="page"/>
<script type="text/javascript">

var i18nMsg = {
<% 
boolean lastFlag=false;
int length = rb.keySet().size();
int index = 1;
for(String key:rb.keySet()){
	if(index==length){
		lastFlag = true;
	}
%>
'<%=key%>': '<%=rb.getString(key)%>'<%=lastFlag?"":","%>
<%index++;}%>
};
</script>
