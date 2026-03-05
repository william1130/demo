<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/directive.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>卡別維護</title>
<meta http-equiv="Content-Type" content="text/html; charset=big5">
<meta http-equiv="Context-Script-Type" content="text/javascript">

<%@include file="/jsp/header.jsp"%>
	
<script type="text/javascript" src="/scripts/addToolTip.js"></script>

<style>
.edsToolTip {
	background-color: #8080FF;
	opacity: 0.9;
}
#addBlock{margin-bottom:5px;text-align:left;}
</style>

</head>
<body class="ieVersion">

	<div id="mainContentBlock">
<div id="progHeader">
			<div id="progTitle" title="程式代碼 : RP-UC-XXX-001">卡別維護</div>
	</div>
	<div style="width:65%;margin:0 auto;">
	<div id="addBlock">
		<button type="button" onclick="showIFrameDialog('addCardType.html',{width:'800', height:'280',title: '新增'});return false;" class="button">新增</button>
	</div>
	
	<table border="0" cellpadding="4" cellspacing="1" class="listTable" align="center">
		<thead>
			<tr>
				<th>序號</th>
				<th>動作</th>
				<th>卡別</th>											
			</tr>
		</thead>
		<tbody>
			<tr class="oddRow">
				<td class="alignCenter">1</td>
				<td class="alignCenter">
					待覆核
				</td>
				<td class="alignLeft">VISA</td>
			</tr>
			<tr class="evenRow">
				<td class="alignCenter">2</td>
				<td class="alignCenter">
					<a href="#" onclick="showIFrameDialog('modCardType.html',{width:'700', height:'280',title: '修改'});return false;"><img src="../images/pen.gif" border="0" /></a>
					<a href="#" onclick="if( confirm('是否確定刪除') ) showIFrameDialog( '../../common/result.html', {width:'800', height:'280',title: '刪除'}); return false;" title="刪除"><img src="../images/close.gif" border="0" /></a>
				</td>
				<td class="alignLeft">MASTER</td>									
			</tr>
			
			<tr class="oddRow">
				<td class="alignCenter">3</td>
				<td class="alignCenter">
					<a href="#" onclick="showIFrameDialog('modCardType.html',{width:'700', height:'280',title: '修改'});return false;"><img src="../images/pen.gif" border="0" /></a>
					<a href="#" onclick="if( confirm('是否確定刪除') ) showIFrameDialog( '../../common/result.html', {width:'800', height:'280',title: '刪除'}); return false;" title="刪除"><img src="../images/close.gif" border="0" /></a>
				</td>
				<td class="alignLeft">JCB</td>									
			</tr>
			<tr class="evenRow">
				<td class="alignCenter">4</td>
				<td class="alignCenter">
					<a href="#" onclick="showIFrameDialog('modCardType.html',{width:'700', height:'280',title: '修改'});return false;"><img src="../images/pen.gif" border="0" /></a>
					<a href="#" onclick="if( confirm('是否確定刪除') ) showIFrameDialog( '../../common/result.html', {width:'800', height:'280',title: '刪除'}); return false;" title="刪除"><img src="../images/close.gif" border="0" /></a>
				</td>
				<td class="alignLeft">CUP</td>									
			</tr>
		</tbody>
	</table>								
</div>
					
</div>

	<script type='text/javascript' language='javascript'>
			function mySubmit(){
				$('cardNo').style.border="";
				$('cardNo').title="";
				$('tremailNo').style.border="";
				$('tremailNo').title="";
				var errorContainer;
				errorContainer = $('errorMessageArea');
				errorContainer.style.display = "none";
				var container;
				container = $('ajaxResultBlock');
				container.style.display = "none";
				container.style.width="95%";
				

				if($('transNo').value.length!=0&&$('tremailNo').value.length==0)
				{
					$('tremailNo').style.border="2px solid red";
					$('tremailNo').title="交易編號輸入時,端末機編號必須輸入";
				  errorContainer.style.display = "block";
				  $('errorMessage').innerHTML="交易編號輸入時,端末機編號必須輸入";
					return false;
				}
				
				if($('checkCardNo').checked)
				{
					$('cardNo').style.border="2px solid red";
					$('cardNo').title="卡號檢核錯誤";
					errorContainer.style.display = "block";
				  $('errorMessage').innerHTML="卡號檢核錯誤";
					return false;
				}
				container.style.display = "block";
			}
			
			function onSelectAll()
			{
				for(i=1;i<=10;i++)
				{
					$('row'+i).checked=$('selectAll').checked
					
				}
			}
			</script>

</body>

</html>

