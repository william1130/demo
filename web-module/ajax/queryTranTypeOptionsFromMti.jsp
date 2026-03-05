<%@ page language="java" contentType="text/html; charset=BIG5"
	pageEncoding="BIG5"%>
<%@include file="/jsp/directive.jsp"%>

<a id="ajaxQueryTranTypeOptionsFromMti"
	href="<c:url value='/ajax/queryTranTypeOptionsFromMti.do'/>"
	class="disabledLink"></a>

<script type='text/javascript'>

function queryTranTypeOptionsFromMti(selectObj, value1, value2)
{	
	queryOptionsByAjax( {url: $('ajaxQueryTranTypeOptionsFromMti'), selectObj: selectObj, params:{mti: value1, hostAccord: value2}});
}	
	
</script>