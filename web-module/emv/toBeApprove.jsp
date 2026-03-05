<%--
 * ==============================================================================================
 * $Id$
 * ==============================================================================================
--%>
<%@page import="java.util.Date" %>
<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/directive.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>待辦事項</title>
		<%@include file="/jsp/header.jsp"%>	
	</head>
	<body>
		<div id="mainContentBlock" style="width: 100%;">
			<s:form action="toBeApprove">
			<div id='criteriaBlock' class='foldingBlock' style='width: 500px;margin-left:-175px;'>			
				<jsp:include page="/jsp/messages.jsp"/>
				<table border="0" cellspacing="0" cellpadding="0" style="width:100%">
					<tr>
						<td style="text-align:center;"><span style="font-size:18px;font-weight:bold">待辦事項</span></td>
					</tr>
					<tr>
						<td style="padding: 5px"><HR color=red></td>
					</tr>
					<tr>
						<td style="padding-left: 5px;text-align: center;">
							<fieldset>
							<%pageContext.setAttribute("_Now", new Date()); %>
								<legend>
									<fmt:formatDate value="${_Now}" pattern="yyyy/MM/dd"/> &nbsp;
									<fmt:formatDate value="${_Now}" pattern="HH:mm:ss"/>
								</legend>
								<table cellpadding="4" cellspacing="0">
									<tr>
										<td class="require">EMV TAG覆核:</td>
										<td align=center>
											<c:choose>
												<c:when test='${(approveCount["emvTag"]) > 0 }' >
													<a href="<c:url value='/emv/toEmvTagRecordTemp.do' />" style="color:blue;"><c:out value='${approveCount["emvTag"] }'/>筆 </a>
												</c:when>
												<c:otherwise>
													<c:out value='${approveCount["emvTag"] }'/>筆
												</c:otherwise>
											</c:choose>																	
										</td>
									</tr>
									<tr>
										<td class="require">卡別參數待覆核:</td>
										<td align=center>
											<c:choose>
												<c:when test='${(approveCount["cardType"]) > 0 }' >
													<a href="<c:url value='/emv/toEmvCardTypeTemp.do' />" style="color:blue;"><c:out value='${approveCount["cardType"] }'/>筆 </a>
												</c:when>
												<c:otherwise>
													<c:out value='${approveCount["cardType"] }'/>筆
												</c:otherwise>
											</c:choose>																	
										</td>
									</tr>
									<tr>
										<td class="require">參考規格待覆核:</td>
										<td align=center>
											<c:choose>
												<c:when test='${(approveCount["emvSpec"]) > 0 }' >
													<a href="<c:url value='/emv/toEmvRefSpecTemp.do' />" style="color:blue;"><c:out value='${approveCount["emvSpec"] }'/>筆 </a>
												</c:when>
												<c:otherwise>
													<c:out value='${approveCount["emvSpec"] }'/>筆
												</c:otherwise>
											</c:choose>																	
										</td>
									</tr>
								</table>
							</fieldset>
						</td>
					</tr>
				</table>
			</div>
			</s:form>
		</div>		
	</body>
</html>
