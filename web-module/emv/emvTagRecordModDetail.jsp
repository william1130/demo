
<%@page import="proj.nccc.logsearch.ProjConstants"%>
<%@page import="proj.nccc.logsearch.beans.Util"%>
<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/directive.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title><c:out value='${currentProgramObj.name}'/></title>
		<%@include file="/jsp/header.jsp"%>
		<style>
		.noBg{background:transparent;}
		.noBg tr{background:transparent;}
		.noBg td{background:transparent;}

		</style>	
	</head>
	<body>

	<s:set var="pagingInfo" value="%{entity.pagingInfo}" /> 
	<s:set var="masterData" value="%{pagingInfo.masterData}"/>
	<s:set var="modDetailList" value="%{pagingInfo.modDetailList}"/>
	<s:set var="oriDetailList" value="%{pagingInfo.oriDetailList}"/>

	<c:set var="cardTypeSzie" value="1" />
		<div id="mainContentBlock" style="width:90%">
			<br />
			<table border="0" cellpadding="4" cellspacing="1" class="listTable" align="center" style="width:90%">
				<thead>
				<tr>
					<th width=10%></th>
					<th width=45%>修改前</th>
					<th width=45%>修改後</th>
				</tr>
				</thead>
				<tbody>
					<tr class="oddRow">
						<th class="alignLeft">卡別</th>
						<td class="alignLeft"><c:out value="${masterData.cardType }" /></td>
						<td class="alignLeft">
							<c:if test="${entity.cardType ne masterData.cardType }">
								<span style="color: red;"><c:out value="${entity.cardType }" /></span>
							</c:if>
							<c:if test="${entity.cardType eq masterData.cardType }">
								<c:out value="${entity.cardType }" />
							</c:if>
						</td>
					</tr>
					<tr class="evenRow">
						<th class="alignLeft">TAG名稱</th>
						<td class="alignLeft"><c:out value="${masterData.emvTag }" /></td>
						<td class="alignLeft"><c:out value="${entity.emvTag }" /></td>
					</tr>
					<tr class="oddRow">
						<th class="alignLeft">TAG全名</th>
						<td class="alignLeft"><c:out value="${masterData.emvName }" /></td>
						<td class="alignLeft">
							<c:if test="${entity.emvName ne masterData.emvName }">
								<span style="color: red;"><c:out value="${entity.emvName }" /></span>
							</c:if>
							<c:if test="${entity.emvName eq masterData.emvName }">
								<c:out value="${entity.emvName }" />
							</c:if>
						</td>
					</tr>
					<tr class="evenRow">
						<th class="alignLeft">縮寫</th>
						<td class="alignLeft"><c:out value="${masterData.emvAbbr }" /></td>
						<td class="alignLeft">
							<c:if test="${entity.emvAbbr ne masterData.emvAbbr }">
								<span style="color: red;"><c:out value="${entity.emvAbbr }" /></span>
							</c:if>
							<c:if test="${entity.emvAbbr eq masterData.emvAbbr }">
								<c:out value="${entity.emvAbbr }" />
							</c:if>
						</td>
					</tr>
					<tr class="oddRow">
						<th class="alignLeft">描述</th>
						<td class="alignLeft"><c:out value="${masterData.emvDesc }" /></td>
						<td class="alignLeft">
							<c:if test="${entity.emvDesc ne masterData.emvDesc }">
								<span style="color: red;"><c:out value="${entity.emvDesc }" /></span>
							</c:if>
							<c:if test="${entity.emvDesc eq masterData.emvDesc }">
								<c:out value="${entity.emvDesc }" />
							</c:if>
						</td>
					</tr>
					
					<c:set var="oriBytePos" value="" />
					<tr class="evenRow">
					<th class="alignLeft">Value</th>
					<td class="alignLeft">
					<c:forEach var="item" items="${oriDetailList}" varStatus="status">
						<c:if test="${oriBytePos ne item.posByte}">
							<c:if test="${item.posByte ne 1 }"><hr></c:if>
							<c:set var="oriBytePos" value="${item.posByte }" />	
							<span style="font-weight:bold">Byte<c:out value="${item.posByte }" />(<c:out value="${item.valueByte }" />)</span><br />
						</c:if>
						<c:if test="${oriBytePos eq item.posByte}">
							<c:out value="${item.posByte }" />-<c:out value="${item.posBit }" />:<c:out value="${item.valueBit }" /><br />
						</c:if>
					</c:forEach>
					</td>
					<td class="alignLeft">
					<c:set var="oriBytePos" value="" />
					<c:forEach var="modItem" items="${modDetailList}" varStatus="status">
					<c:set var="para" value="${oriDetailList[status.index] }" />
						<c:if test="${oriBytePos ne modItem.posByte}">
							<c:if test="${modItem.posByte ne 1 }"><hr></c:if>
							<c:set var="oriBytePos" value="${modItem.posByte }" />
							<c:if test="${modItem.valueByte ne para.valueByte}">
								<span style="font-weight:bold;color:red;">Byte<c:out value="${modItem.posByte }" />(<c:out value="${modItem.valueByte }" />)</span><br />
							</c:if>
							<c:if test="${modItem.valueByte eq para.valueByte}">
								<span style="font-weight:bold;">Byte<c:out value="${modItem.posByte }" />(<c:out value="${modItem.valueByte }" />)</span><br />
							</c:if>
						</c:if>
						<c:if test="${oriBytePos eq modItem.posByte}">
							<c:if test="${modItem.valueBit ne para.valueBit}">
								<span style="color:red;"><c:out value="${modItem.posByte }" />-<c:out value="${modItem.posBit }" />:<c:out value="${modItem.valueBit }" /></span><br />
							</c:if>
							<c:if test="${modItem.valueBit eq para.valueBit}">
								<c:out value="${modItem.posByte }" />-<c:out value="${modItem.posBit }" />:<c:out value="${modItem.valueBit }" /><br />
							</c:if>
						</c:if>
					</c:forEach>
					</td>
					</tr>	
						<%-- <c:if test="${modBytePos ne para.posByte}">
							<c:set var="modBytePos" value="${para.posByte }" />	
							<tr ${statusIndex }>
								<td class="alignLeft">
								Byte<c:out value="${para.posByte }" />(<c:out value="${para.valueByte }" />)<br />
						</c:if>
						<c:if test="${modBytePos eq para.posByte}">
							<c:out value="${para.posByte }" />-<c:out value="${para.posBit }" />:<c:out value="${para.valueBit }" /><br />
							<c:if test="${para.posBit == 1 }">
								</td>
								</tr>
							</c:if>
						</c:if>		
					
					<tr>
						<th class="alignLeft" rowspan="${entity.emvLen }">Value</th>
						<td>
							<table>
								<c:set var="bytePos" value="" />
								<c:forEach var="item" items="${modDetailList}" varStatus="status">
									<c:if test="${item.posByte % 2 == 0 }">
										<c:set var="statusIndex" value="class=\"oddRow\"" />
									</c:if>
									<c:if test="${item.posByte % 2 == 1}">
										<c:set var="statusIndex" value="class=\"evenRow\"" />
									</c:if>
									
									
									<c:if test="${bytePos ne item.posByte}" >
										<c:set var="bytePos" value="${item.posByte }" />
										<tr ${statusIndex }>
										<td class="alignLeft">
											Byte<c:out value="${item.posByte }" />(<c:out value="${item.valueByte }" />)<br />
											<c:out value="${item.posByte }" />-<c:out value="${item.posBit }" />:<c:out value="${item.valueBit }" /><br />	
									</c:if>									
									<c:if test="${bytePos eq item.posByte}">
										<c:out value="${item.posByte }" />-<c:out value="${item.posBit }" />:<c:out value="${item.valueBit }" /><br />
										<c:if test="${item.posBit eq 1 }">
											</td>
											</tr>
										</c:if>
									</c:if>
								</c:forEach>
							</table>	
						</td>
						<td>
							<table border=0>
								<c:forEach var="oriItem" items="${oriDetailList}" varStatus="status2">
									<c:if test="${oriItem.posByte % 2 == 0 }">
										<c:set var="statusIndex" value="class=\"oddRow\"" />
									</c:if>
									<c:if test="${oriItem.posByte % 2 == 1}">
										<c:set var="statusIndex" value="class=\"evenRow\"" />
									</c:if>
									<c:if test="${bytePos ne oriItem.posByte}" >
										<c:set var="bytePos" value="${oriItem.posByte }" />
										<tr ${statusIndex }>
										<td class="alignLeft">
											Byte<c:out value="${oriItem.posByte }" />(<c:out value="${oriItem.valueByte }" />)<br />
											<c:out value="${oriItem.posByte }" />-<c:out value="${oriItem.posBit }" />:<c:out value="${oriItem.valueBit }" /><br />	
									</c:if>									
									<c:if test="${bytePos eq oriItem.posByte}">
										<c:out value="${oriItem.posByte }" />-<c:out value="${oriItem.posBit }" />:<c:out value="${oriItem.valueBit }" /><br />
										<c:if test="${oriItem.posBit eq 1 }">
											</td>
											</tr>
										</c:if>
									</c:if>
								</c:forEach>
							</table>
						</td>
					
					</tr>
 --%>					
				</tbody>
				
			</table>
			</div>
	</body>
</html>
