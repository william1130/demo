<%@page pageEncoding="UTF-8"%>


<!-- POS Entry Mode -->
<div id="tooltipPOSEntryMode" style="display: none;">
	<table border="0" cellpadding="2" cellspacing="1"
		class="listTable" align="center" style="width: 360px;">
		<thead>
			<tr>
				<th width="20%">代碼</th>
				<th width="80%">說明</th>
			</tr>
		</thead>
		<tbody>
			<tr class="oddRow">
				<td align="center">00</td>
				<td align="left">未讀磁條(人工授權)</td>
			</tr>
			<tr class="evenRow">
				<td align="center">01</td>
				<td align="left">未讀磁條(edc人工輸入) </td>
			</tr>
			<tr class="oddRow">
				<td align="center">02</td>
				<td align="left">磁條交易。(不保證磁條資料完整傳輸)</td>
			</tr>
			<tr class="evenRow"><%-- //UPLAN-M2018187 --%>
				<td align="center">03</td>
				<td align="left">QR交易。(被掃模式)</td>
			</tr>
			<tr class="oddRow">
				<td align="center">05</td>
				<td align="left">晶片交易</td>
			</tr>
			<tr class="evenRow">
				<td align="center">07</td>
				<td align="left">Contactless晶片交易<br/>DFS EC交易</td>
			</tr>
			<tr class="oddRow">
				<td align="center">09</td>
				<td align="left">PAN entry via electronic commerce, including remote chip</td>
			</tr>
			<tr class="evenRow">
				<td align="center">10</td>
				<td align="left">Credential on file (VISA卡檔)</td>
			</tr>
			<tr class="oddRow">
				<td align="center">81</td>
				<td align="left">MasterCard EC或JCB EC交易</td>
			</tr>
			<tr class="evenRow">
				<td align="center">82</td>
				<td align="left">卡號資料由主機自動帶出(PAN auto entry via server)<br />DFS EC交易(mobile)</td>
			</tr>
			<tr class="oddRow">
				<td align="center">83</td>
				<td align="left">DFS感應交易</td>
			</tr>
			<tr class="evenRow">
				<td align="center">85</td>
				<td align="left">DFS chip fallback交易</td>
			</tr>
			<tr class="oddRow">
				<td align="center">90</td>
				<td align="left">磁條交易(磁條資料完整傳輸)<br />DFS voice授權交易</td>
			</tr>
			<tr class="evenRow">
				<td align="center">91</td>
				<td align="left">感應交易(Contactless)</td>
			</tr>
		</tbody>
	</table>
</div>