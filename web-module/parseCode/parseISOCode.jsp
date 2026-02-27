<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/directive.jsp"%>
<%
	request.setAttribute("newLineChar", "\n");
    request.setAttribute("spaceChar", " ");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@include file="/jsp/header.jsp"%>
<jsp:include page="../parseCode/tooltipIC.jsp"/>
<jsp:include page="../parseCode/tooltipCrdVefyFlg.jsp"/>
<jsp:include page="../parseCode/tooltipPOSEntryMode.jsp"/>
<jsp:include page="../parseCode/tooltipStandIn.jsp"/>
<script type="text/javascript" src="../scripts/addToolTip.js"></script>
<style>
	.preClass {
	  width:auto;
	  max-width: 950px;
	  white-space: pre-wrap; /* css-3 */
	  white-space: -moz-pre-wrap; /* Mozilla, since 1999 */
	  white-space: -pre-wrap; /* Opera 4-6 */
	  white-space: -o-pre-wrap; /* Opera 7 */
	  word-wrap: break-word; /* Internet Explorer 5.5+ */
	  padding: 10px;
	  word-break: break-all;
	}	
</style>

</head>


<body class="ieVersion">
	<div id="mainContentBlock" style="text-align: center;width: 98%">

		<jsp:include page="/jsp/messages.jsp" />
		
		<div id='criteriaBlock' class='foldingBlock' style="width: 98%">

			<table border="0" cellpadding="4" cellspacing="1" class="listTable" style="table-layout: fixed">
				<thead>
					<tr>
						<th width="5%" style="word-break: break-all;">ISO Field</th>
						<th width="30%" style="word-break: break-all;">欄位說明</th>
						<th width="65%" style="word-break: break-all;">Value</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="item" items="${resultList}" varStatus="status">
						<tr class="<edstw:rowStyle value='${status.index}'/>">
							<td class="alignCenter"><c:out value="${item.title}" /></td>
							<td class="alignLeft"><c:out value="${item.desc}" /></td>
							<td class="alignLeft" style="word-break: break-all;">
								<c:if test="${(item.length != null) && !('' eq item.length)}">
									Length: <c:out value="${item.length}" /><br />
								</c:if>
								<c:if test="${'V' eq item.type}">
									${fn:replace(item.value, spaceChar, '&nbsp;')}
								</c:if>
								<c:if test="${'TK' eq item.type}">
									<table border="0" cellpadding="4" cellspacing="1" class="listTable" style="table-layout: fixed">
										<tr>
											<th width="10%">Token</th>
											<th width="90%">Value</th>
										</tr>
										<c:forEach var="subItem" items="${item.value}" varStatus="subStatus">
											<tr class="<edstw:rowStyle value='${subStatus.index}'/>">
												<td class="alignCenter" style="word-break: break-all;">
													<c:out value="${subItem.title}" />
												</td>
												<td class="alignLeft" style="word-break: break-all;">
													<table border="0" cellpadding="4" cellspacing="1" class="listTable" style="table-layout: fixed">
														<tr>
															<th width="70%">Value</th>
															<th width="30%">說明</th>
														</tr>
														<c:forEach var="subsubItem" items="${subItem.value}" varStatus="subsubStatus">
															<tr
																class="<edstw:rowStyle value='${subsubStatus.index}'/>">
																<td class="alignCenter" style="word-break: break-all;">
																	${fn:replace(subsubItem.value, spaceChar, '&nbsp;')}
																</td>
																<td class="alignLeft" style="word-break: break-all;">
																	<c:out value="${subsubItem.desc}" />
																</td>
															</tr>
														</c:forEach>
													</table>
												</td>
											</tr>
										</c:forEach>
									</table>
								</c:if>
								<c:if test="${'SC' eq item.type}">
									<table border="0" cellpadding="4" cellspacing="1" class="listTable" style="table-layout: fixed">
										<c:forEach var="subItem" items="${item.value}" varStatus="subStatus">
											<tr class="<edstw:rowStyle value='${subStatus.index}'/>">
												<th class="alignCenter" style="word-break: break-all;" width="25%">
													<c:out value="${subItem.desc}" />
												</th>
												<td class="alignLeft" style="word-break: break-all;" width="75%">
													<c:out value="${subItem.value}" />
												</td>
											</tr>
										</c:forEach>
									</table>
								</c:if>
								<c:if test="${'ST' eq item.type}">
									<table border="0" cellpadding="4" cellspacing="1"
										class="listTable" style="table-layout: fixed">
										<tr>
											<th width="8%">Tag</th>
											<th width="42%">Name</th>
											<th width="50%">Value</th>
										</tr>
										<c:forEach var="subItem" items="${item.value}"
											varStatus="subStatus">
											<tr class="<edstw:rowStyle value='${subStatus.index}'/>">
												<td class="alignCenter" style="word-break: break-all;">
													<c:out value="${subItem.title}" />
												</td>
												<td class="alignCenter" style="word-break: break-all;">
													<c:out value="${subItem.desc}" />
												</td>
												<td class="alignLeft" style="word-break: break-all;">
													${fn:replace(subItem.value, spaceChar, '&nbsp;')}
												</td>
											</tr>
										</c:forEach>
									</table>
								</c:if>
								<c:if test="${'TC' eq item.type}">
									<table border="0" cellpadding="4" cellspacing="1" class="listTable" style="table-layout: fixed">
										<tr>
											<th width="10%">Tag</th>
											<th width="10%">Length</th>
											<th width="80%">Value</th>
										</tr>
										<c:forEach var="subItem" items="${item.value}" varStatus="subStatus">
											<tr class="<edstw:rowStyle value='${subStatus.index}'/>">
												<td class="alignCenter" style="word-break: break-all;">
													<c:out value="${subItem.title}" />
												</td>
												<td class="alignCenter" style="word-break: break-all;">
													<c:out value="${subItem.length}" />
												</td>
												<td class="alignLeft" style="word-break: break-all;">
													${fn:replace(subItem.value, spaceChar, '&nbsp;')}
												</td>
											</tr>
										</c:forEach>
									</table>
								</c:if>
								<c:if test="${'TT' eq item.type}">
									<table border="0" cellpadding="4" cellspacing="1" class="listTable" style="table-layout: fixed">
										<tr>
											<th width="10%">Table Id</th>
											<th width="30%">Table說明</th>
											<th width="10%">Length</th>
											<th width="50%">Data</th>
										</tr>
										<c:forEach var="subItem" items="${item.value}" varStatus="subStatus">
											<tr class="<edstw:rowStyle value='${subStatus.index}'/>">
												<td class="alignCenter" style="word-break: break-all;">
													<c:out value="${subItem.title}" />
												</td>
												<td class="alignLeft" style="word-break: break-all;"><c:out
														value="${subItem.desc}" /></td>
												<td class="alignCenter" style="word-break: break-all;">
													<c:out value="${subItem.length}" />
												</td>
												
												<td class="alignLeft" style="word-break: break-all;">
												
												 <c:if test="${'UP' ne subItem.title }"><%-- //UPLAN-M2018187 --%>
												 
												 <c:if test="${'X' eq subItem.title }">
														<button type="button" class="button"
															onclick="showIFrameDialog('<c:url value="/trans/parseDCCData.do?rawType=${rawType}&seqId=${seqId}&edcSpec=${edcSpec}&tagName=${subItem.title}&logTime=${logTime}"/>',{width:940, height:'330',title: '${subItem.desc}'});return false;">詳細資料</button>
													</c:if>
													<c:if test="${'E1' eq subItem.title || 'E2' eq subItem.title}">
														<button type="button" class="button"
															onclick="showIFrameDialog('<c:url value="/trans/parseE1E2Data.do?rawType=${rawType}&seqId=${seqId}&edcSpec=${edcSpec}&tagName=${subItem.title}&logTime=${logTime}"/>',{width:940, height:'330',title: '${subItem.desc}'});return false;">詳細資料</button>
													</c:if>
													<c:if test="${'X' ne subItem.title && 'E1' ne subItem.title && 'E2' ne subItem.title}">
													<table border="0" cellpadding="4" cellspacing="1" class="listTable" style="table-layout: fixed">
														<tr>
															<th width="70%">Value</th>
															<th width="30%">說明</th>
														</tr>
														<c:forEach var="subsubItem" items="${subItem.value}" varStatus="subsubStatus">
															<tr class="<edstw:rowStyle value='${subsubStatus.index}'/>">
																<c:if test="${'ZIP' eq subsubItem.type }">
																	<td class="alignLeft" style="word-break: break-all;">                                   
																		<table border="0" cellpadding="4" cellspacing="1" class="listTable" style="table-layout: fixed">              
																			<tr>                                                        
																				<th width="50%">Value</th>                                
																				<th width="50%">說明</th>                                 
																			</tr>                                                       
																			<c:forEach var="subsubsubItem" items="${subsubItem.value}" varStatus="subsubsubStatus">                                 
																				<tr                                                       
																					class="<edstw:rowStyle value='${subsubsubStatus.index}'/>">
																					<td class="alignCenter" style="word-break: break-all;"> 
																						${fn:replace(subsubsubItem.value, spaceChar, '&nbsp;')}  
																					</td>                                                   
																					<td class="alignLeft" style="word-break: break-all;">   
																						<c:out value="${subsubsubItem.desc}" />                  
																					</td>                                                   
																				</tr>                                                     
																			</c:forEach>                                                
																		</table>                                                      
																	</td>      
																 </c:if> 
																<c:if test="${'ZIP' ne subsubItem.type }">
																	<td class="alignCenter" style="word-break: break-all;">
																		${fn:replace(subsubItem.value, spaceChar, '&nbsp;')}
																	</td>
																</c:if>
																<td class="alignLeft" style="word-break: break-all;">
																	<c:out value="${subsubItem.desc}" />
																</td>
															</tr>
														</c:forEach>
													</table>
													</c:if>
													</c:if> 
													 <c:if test="${'UP' eq subItem.title }"> 
													<c:if test="${'X' eq subItem.title }">
														<button type="button" class="button"
															onclick="showIFrameDialog('<c:url value="/trans/parseDCCData.do?rawType=${rawType}&seqId=${seqId}&edcSpec=${edcSpec}&tagName=${subItem.title}&logTime=${logTime}"/>',{width:940, height:'330',title: '${subItem.desc}'});return false;">詳細資料</button>
													</c:if>
													<c:if test="${'E1' eq subItem.title || 'E2' eq subItem.title}">
														<button type="button" class="button"
															onclick="showIFrameDialog('<c:url value="/trans/parseE1E2Data.do?rawType=${rawType}&seqId=${seqId}&edcSpec=${edcSpec}&tagName=${subItem.title}&logTime=${logTime}"/>',{width:940, height:'330',title: '${subItem.desc}'});return false;">詳細資料</button>
													</c:if>
													<c:if test="${'X' ne subItem.title && 'E1' ne subItem.title && 'E2' ne subItem.title}">
													<table border="0" cellpadding="4" cellspacing="1" class="listTable" style="table-layout: fixed">
														<tr>
															<th width="15%">Tag</th>
															<th width="20%">Length</th>
															<th width="30%">Value</th>
															<th width="35%">說明</th>
														</tr>
														<c:forEach var="subsubItem" items="${subItem.value}" varStatus="subsubStatus">
														<tr class="<edstw:rowStyle value='${subsubStatus.index}'/>">
															<td class="alignCenter" style="word-break: break-all;">
																<c:out value="${subsubItem.title}" />
															</td>
															<td class="alignCenter" style="word-break: break-all;">
																<c:out value="${subsubItem.length}" />
															</td>
															<td class="alignLeft" style="word-break: break-all;">
																${fn:replace(subsubItem.value, spaceChar, '&nbsp;')}
															</td>
															<td class="alignLeft" style="word-break: break-all;">
																	<c:out value="${subsubItem.desc}" />
																</td>
														</tr>
														</c:forEach>
													</table>
													</c:if>
													</c:if>
												</td>
											</tr>
										</c:forEach>
									</table>
								</c:if>
								<c:if test="${'TL' eq item.type}">
									<table border="0" cellpadding="4" cellspacing="1" class="listTable" style="table-layout: fixed">
										<tr>
											<th width="70%">Value</th>
											<th width="30%">說明</th>
										</tr>
										<c:forEach var="subItem" items="${item.value}" varStatus="subStatus">
											<tr class="<edstw:rowStyle value='${subStatus.index}'/>">
												<td class="alignCenter" style="word-break: break-all;">
													${fn:replace(subItem.value, spaceChar, '&nbsp;')}
												</td>
												<td class="alignLeft" style="word-break: break-all;">
													<c:out value="${subItem.desc}" />
												</td>
											</tr>
										</c:forEach>
									</table>
								</c:if>
								<c:if test="${'TLT' eq item.type}">
									<table border="0" cellpadding="4" cellspacing="1" class="listTable" style="table-layout: fixed">
										<tr>
											<th width="15%">電票</th>
											<th width="40%">Value</th>
											<th width="45%">說明</th>
										</tr>
										<c:forEach var="subItem" items="${item.value}" varStatus="subStatus">
											<tr class="<edstw:rowStyle value='${subStatus.index}'/>">
												<c:if test="${subStatus.index % 13 == 0 }">
													<td class="alignCenter" rowspan="13"><c:out value="${subItem.tksName}" /></td>
												</c:if>
												<td class="alignCenter" style="word-break: break-all;">
													${fn:replace(subItem.value, spaceChar, '&nbsp;')}
												</td>
												<td class="alignLeft" style="word-break: break-all;">
													<c:out value="${subItem.desc}" />
												</td>
											</tr>
										</c:forEach>
									</table>
								</c:if>
								<c:if test="${'TLL' eq item.type}">
									<table border="0" cellpadding="4" cellspacing="1" class="listTable" style="table-layout: fixed">
										<c:forEach var="subItem" items="${item.value}" varStatus="subStatus">
											<tr class="<edstw:rowStyle value='${subStatus.index}'/>">
												<th class="alignLeft" style="word-break: break-all;" width=25%>
													<c:out value="${subItem.desc}" />
												</th>
												<td class="alignLeft" style="word-break: break-all;"  width=75%>
													 ${fn:replace(subItem.value, spaceChar, '&nbsp;')} 
													<%-- <pre style="word-break: break-all;"><c:out value="${subItem.value}" /></pre> --%>
												</td>
											</tr>
										</c:forEach>
									</table>
								</c:if>
								<c:if test="${'ET' eq item.type}">
									<table border="0" cellpadding="4" cellspacing="1" class="listTable" style="table-layout: fixed">
										<tr>
											<th width="10%">Table Id</th>
											<th width="30%">Table說明</th>
											<th width="10%">Length</th>
											<th width="50%">Data</th>
										</tr>
										<c:forEach var="subItem" items="${item.value}" varStatus="subStatus">
											<tr class="<edstw:rowStyle value='${subStatus.index}'/>">
												<td class="alignCenter" style="word-break: break-all;">
													<c:out value="${subItem.title}" />
												</td>
												<td class="alignLeft" style="word-break: break-all;">
													<c:out value="${subItem.desc}" /></td>
												<td class="alignCenter" style="word-break: break-all;">
													<c:out value="${subItem.length}" />
												</td>
												<td class="alignLeft" style="word-break: break-all;">
													<c:if test="${'button' eq subItem.value }">
														<button type="button" class="button"
															onclick="showIFrameDialog('<c:url value="/trans/showETData.do?rawType=${rawType}&seqId=${seqId}&edcSpec=${edcSpec}&tagName=${subItem.title}"/>',{width:940, height:'320',title: '${subItem.desc}'});return false;">詳細資料</button><br />
													</c:if>
													<c:if test="${'TMS' eq subItem.value }">
														<button type="button" class="button"
															onclick="showIFrameDialog('<c:url value="/trans/praseETData.do?rawType=${rawType}&seqId=${seqId}&edcSpec=${edcSpec}&tagName=${subItem.title}"/>',{width:940, height:'320',title: '${subItem.desc}'});return false;">詳細資料</button>
													</c:if>
													<c:if test="${ 'button' ne subItem.value && 'TMS' ne subItem.value}">
														
														<table border="0" cellpadding="4" cellspacing="1" class="listTable" style="table-layout: fixed">              
															<tr>                                                        
																<th width="50%">Value</th>                                
																<th width="50%">說明</th>                                 
															</tr>                                                       
															<c:forEach var="subsubItem" items="${subItem.value}" varStatus="subsubStatus">                                 
																<tr>
																	<td class="alignCenter" style="word-break: break-all;"> 
																		${fn:replace(subsubItem.value, spaceChar, '&nbsp;')}  
																	</td>                                                   
																	<td class="alignLeft" style="word-break: break-all;">   
																		<c:out value="${subsubItem.desc}" />                  
																	</td>                                                   
																</tr>                                                     
															</c:forEach>                                                
														</table>
														
													</c:if>
													
												</td>
											</tr>
										</c:forEach>
									</table>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<c:if test="${etData ne null }">
			<br />
			<table border="0" cellpadding="4" cellspacing="1" class="listTable">
			<thead>
				<tr>
					<th width="100%" style="text-align:left;">
						Raw Data
					</th>
					
				</tr>
			</thead>
			<tbody>
				<tr class="oddRow">
					<td class="alignLeft">
					<div class="preClass">${fn:replace(etData, spaceChar, '&nbsp;')}</div>
					</td>
				</tr>
			</tbody>
			</table>
			</c:if>
		</div>
		<br />
	</div>
</body>
</html>