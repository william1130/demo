<%@ page language="java" contentType="text/html; charset=BIG5"
	pageEncoding="BIG5"%>
<%@include file="/jsp/directive.jsp"%>

<a id="ajaxQueryTranTypeOptionsFromHost"
	href="<c:url value='/ajax/queryTranTypeOptionsFromHost.do'/>"
	class="disabledLink"></a>

<script type='text/javascript'>

function queryTranTypeOptionsFromHost(selectObj, value1)
{	
	queryOptionsByAjax( {url: $('ajaxQueryTranTypeOptionsFromHost'), selectObj: selectObj, params:{hostAccord: value1}});
}	
	
</script>