
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
		.button1{background:white;width:80px;height:24px;line-height:24px;border:1px solid #000;display:block;float:left;text-align:center;margin:0 1px;}
		.button2{background:#164BA8;color:white;width:80px;height:24px;line-height:24px;border:1px solid #000;display:block;float:left;text-align:center;margin:0 1px;}
		.checkbox1{min-width:40px;display:block;left;text-align:left;}
		</style>	
	</head>
	<body>
	<s:set var="pagingInfo" value="%{entity.pagingInfo}" /> 
	<s:set var="resultList" value="%{pagingInfo.resultList}"/>
	<s:set var="resultTempList" value="%{pagingInfo.resultTempList}"/>
	<c:set var="cardTypeSzie" value="1" />
		<div id="mainContentBlock">
			<div style="width: 100%;font-size:18px;text-align:left;font-weight:bold">
			<c:out value="${entity.emvName }" /> (<c:out value="${entity.emvAbbr }" />), <c:out value="${entity.emvTag }" />
			</div>
<div id='criteriaBlock' class='foldingBlock' style="text-align:left;width: 100%;margin-top:1%;background:transparent">
		<c:set var="string2" value="${fn:split( entity.cardType, ',')}" />
			
		<div style="height:25px;margin-bottom:5px;border-bottom:1px solid #000">
		<c:forEach var="ct" items="${string2}" varStatus="num">
			<span class="button2">${paramBean.emvCardTypeNameMap[ct]}</span>
		</c:forEach>

		</div>	
		<b>Description:</b><br />
		<c:out value="${entity.emvDesc }" /><br />
		<br />
		<b>Length :</b><c:out value="${entity.emvLen }" /> Bytes<br />
		<br />
		<b>Values:</b>(請輸入0~9, A~F，共<c:out value="${entity.emvLen*2 }" />碼)<br />
		<form name="frm1" method="post" action="emvTagRecordDetail.jsp">
		<input type="text" name="result1" id="result1" size="20" maxlength="15" style="border:1px ridge gray" />
		<input type="button" name="submitButton" value="查詢" onclick="mySubmit(<c:out value='${entity.emvLen}' />);"	class="button" />
		<input type="button" name="submitButton" value="轉換" onclick="myReverse(${entity.emvLen});"	class="button" />
		<input name="resetButton" type="button" value="重設" class="button" onclick="resetResult();">
		
			
	    <div id="ajaxResultBlock" style="background:transparent">
			<table style="width: 600px;" align="left" class="listTable">
			<c:set var="bytePos" value="" />
			<tr class="oddRow">
				<td>
			<c:forEach var="item" items="${resultTempList}" varStatus="status">
				<c:if test="${bytePos ne item.posByte}">
					<c:if test="${item.posByte ne 1 }"><hr></c:if>
					<c:set var="bytePos" value="${item.posByte }" />
					<span style="font-weight:bold;">Byte<c:out value="${item.posByte }" />(<c:out value="${item.valueByte }" />)</span><br />
				</c:if>
				<c:if test="${bytePos eq item.posByte}">
					<span class="checkbox1">
						<input type=checkbox name="bit${item.posByte }${item.posBit }" id="bit${item.posByte }${item.posBit }" />${item.valueBit }
					</span>
				</c:if>
			</c:forEach>
			</td>
			</tr>
		</table>
		</div>
		</form>
<div id="errorMessageArea" style="display: none;margin-top:10px;">
			<div id="errorMessageBlock" style="border-radius: 10px;">
				<table border="0" cellspacing="0" cellpadding="5"
					class="errorMessageTable">
					<tr>
						<th>Error Message</th>
					</tr>
					<tr>
						<td class="alignCenter">
							<ul>
								<li id="errorMessage">參數 必須輸入.</li>
							</ul>
						</td>
					</tr>
				</table>
			</div>
			<br />
		</div>	
	
		</div>

	</div>

<script type='text/javascript' language='javascript'>
			var container;
			container = $('ajaxResultBlock');
			var errorContainer;
			errorContainer = $('errorMessageArea');
			errorContainer.style.display = "none";
			
			function mySubmit(len){
				if($('result1').value.length ==0 )
				{
					$('result1').style.border="2px solid red";
					$('result1').title="";
				  	errorContainer.style.display = "block";
				 	container.style.display = "none";
				 	$('errorMessage').innerHTML="請輸入0~9, A~F，共"+len*2+"碼";
					return false;
				}else{
					if($('result1').value.length != len * 2){
						$('result1').style.border="2px solid red";
						$('result1').title="";
					 	errorContainer.style.display = "block";
					 	container.style.display = "none";
					 	$('errorMessage').innerHTML="請輸入0~9, A~F，共"+len*2+"碼";
						return false;
					}
					if(!isNum16($('result1').value)){
						$('result1').style.border="2px solid red";
						$('result1').title="";
					  	errorContainer.style.display = "block";
					 	container.style.display = "none";
					 	$('errorMessage').innerHTML="請輸入0~9, A~F，共"+len*2+"碼";
						return false;
					}
					$('result1').style.border="1px ridge gray";
					errorContainer.style.display = "none";
				}
				var value16 = parseInt($('result1').value,16);
				var binval = value16.toString(2);
				var binvalLen = binval.length;
				if(binvalLen < 2*4*len){
					for(a=0; a < 2*4*len - binvalLen;a++){
	          		binval = "0"+binval;
	          		} 
				}	
				var resultTemp = "";
				var ind = 0;
				for(var i=1;i<=len;i++)
				{
					for(var j=8; j>=1; j--)
					{	
						if( binval.charAt(ind) == "1"){
								$('bit'+i+j).checked= true;
						}else{
								$('bit'+i+j).checked= false;
						}
						ind += 1;
					}				
				}	

				container.style.display = "block";			
			}
			
			function myReverse(len){
				getBinary(len);
				$('result1').style.border="2px solid blue";
				errorContainer.style.display = "none";
			}
			
			function getBinary(len){
				var bValue= "";
				var value16 = "";
				for(var i=1;i<=len;i++)
				{
					for(var j=8; j>=1; j--)
					{
						if($('bit'+i+j).checked == true){
								bValue = bValue+"1";
						}else{
								bValue = bValue+"0";
						}						
					}				
				}	
					var binval = parseInt(bValue, 2);
		        if(!isNaN(binval)){
		            value16 = value16 + binval.toString(16).toUpperCase();
		        }else{
	            value16 = "";
	          }
	       
	       var valueLen = value16.length;
	       if(valueLen < len*2){
	          	for(var a=0; a < len*2 - valueLen;a++){
	          		value16 = "0"+value16;
	          }   
	       } 
				 $('result1').value=value16;
				 container.style.display = "block";
			}
			
			
			function resetResult(){
					$('result1').value = '';
					$('result1').style.border="1px ridge gray";
				}
			
			function isNum16(valueS){  
				var validateValue = "1234567890ABCDEFabcdef";  
				var c;
				var valueLen = valueS.length;
				for(var aa = 0; aa < valueLen; aa++)
				{
					c = valueS.charAt(aa);
					if(validateValue.indexOf(c) < 0){
						return false;
					}	
				}
				return true;
			}
			</script>

	</body>
</html>
