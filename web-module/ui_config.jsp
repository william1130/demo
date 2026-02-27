<%--
 * ==============================================================================================
 * $Id: ui_config.jsp,v 1.1 2014/11/14 03:14:31 asiapacific\hsiehes Exp $
 * ==============================================================================================
--%>
<%@page pageEncoding="UTF-8"%>
<%@include file="/jsp/directive.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html >
	<head>
		<%@include file="/jsp/header.jsp"%>
		<title><c:out value='${currentProgramObj.name}'/></title>
	</head>
	<body>
		<div id="mainContentBlock">
			<div id="progHeader">
				<div id="progTitle" title="程式代碼 : <c:out value='${currentFunctionObj.id}'/>"><c:out value='${currentProgramObj.name}'/></div>
			</div>
			<jsp:include page="/jsp/messages.jsp"/>
			<div id="criteriaBlock" style="width: 30em;">
				<form action="">
					<fieldset>
						<legend>介面參數設定</legend>
						<table cellpadding="4" cellspacing="0" border="0">
							<tr>
								<td class="require">操作介面</td>
								<td>
									<input type="radio" name="uiStyle" value="frame">一般&nbsp;&nbsp;
									<!--<input type="radio" name="uiStyle" value="window">視窗&nbsp;&nbsp;-->
									<input type="radio" name="uiStyle" value="tab">Tab&nbsp;&nbsp;
								</td>
							</tr>
							<tr>
								<td class="require">圓角效果</td>
								<td>
									<input type="radio" name="roundBlocks" value="true">開&nbsp;&nbsp;
									<input type="radio" name="roundBlocks" value="false">關&nbsp;&nbsp;
								</td>
							</tr>
							<tr>
								<td class="require">動態效果</td>
								<td>
									<input type="radio" name="animateEffects" value="true">開&nbsp;&nbsp;
									<input type="radio" name="animateEffects" value="false">關&nbsp;&nbsp;
								</td>
							</tr>
							<!--
							<tr>
								<td class="require">IFrame拖拉功能</td>
								<td>
									<input type="radio" name="iframeDraggable" value="true">開&nbsp;&nbsp;
									<input type="radio" name="iframeDraggable" value="false">關&nbsp;&nbsp;
								</td>
							</tr>
							-->
							<tr>
								<td colspan="2" class="buttonCell">
									<button type="button" class="button" onclick="setConfig( this.form );"><fmt:message key="button.confirm"/></button>					
									<button type="button" class="button" onclick="resetConfig();">回覆預設值</button>					
								</td>
							</tr>
						</table>
					</fieldset>
				</form>
			</div>
			<script type="text/javascript">
			function initSetting()
			{
				form = document.forms[0];
				var pc = PreferenceConfig.getInstance();
				if( pc.uiStyle )
				{
					for( i=0; i<form['uiStyle'].length ; i++ )
					{
						if( form['uiStyle'][i].value == pc.uiStyle )
						{
							form['uiStyle'][i].checked = true;
							break;
						}
					}
				}
				if( pc.roundBlocks )
					form['roundBlocks'][0].checked = true;
				else
					form['roundBlocks'][1].checked = true;
				if( pc.animateEffects )
					form['animateEffects'][0].checked = true;
				else
					form['animateEffects'][1].checked = true;
			}

			function setConfig( form )
			{
				var pc = PreferenceConfig.getInstance();
				var uiStyle = 'frame';
				for( i=0; i<form['uiStyle'].length ; i++ )
				{
					if( form['uiStyle'][i].checked )
					{
						uiStyle = form['uiStyle'][i].value;
						break;
					}
				}
				pc.uiStyle = uiStyle;
				if( form['roundBlocks'][0].checked )
					pc.roundBlocks = true;
				else
					pc.roundBlocks = false;
				if( form['animateEffects'][0].checked )
					pc.animateEffects = true;
				else
					pc.animateEffects = false;

				pc.save();
				showAlert("設定已儲存完畢, 請按重新整理或重開視窗即可生效.");
			}

			function resetConfig()
			{
				PreferenceConfig.clear()
				initSetting();
				showAlert("設定已儲存完畢, 請按重新整理或重開視窗即可生效.");
			}
			EdsEvent.addOnLoad( initSetting );
			</script>
		</div>
	</body>
</html>
