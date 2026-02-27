<%@page pageEncoding="UTF-8"%>


<!-- 驗證結果 -->
<div id="tooltipCrdVefyFlg" style="display: none;">
	<table border="0" cellpadding="2" cellspacing="1"
		class="listTable" align="center" style="width: 480px;">
		<thead>
			<tr>
				<th width="20%">代碼</th>
				<th width="80%">說明</th>
			</tr>
		</thead>
		<tbody>
			<tr class="oddRow">
				<td align="center">C</td>
				<td align="left">Card Verification wa performed and the CVD was invalid</td>
			</tr>
			<tr class="evenRow">
				<td align="center">D</td>
				<td align="left">Card Verification wa performed and the CVD was invalid<br/>
				The ERR-FLG was set to C</td>
			</tr>
			<tr class="oddRow">
				<td align="center">N</td>
				<td align="left">Could not verify the CVD due to a security device error</td>
			</tr>
			<tr class="oddRow">
				<td align="center">O</td>
				<td align="left">CVD checking was not performed because the CVD was not on card</td>
			</tr>
			<tr class="oddRow">
				<td align="center">U</td>
				<td align="left">Issuer unregistered for CVD processing</td>
			</tr>
			<tr class="oddRow">
				<td align="center">Y</td>
				<td align="left">Card Verification was performed and the CVD was valid</td>
			</tr>
		</tbody>
	</table>
</div>
