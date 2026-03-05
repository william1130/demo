//取得現行使用中的UIStyle的menu物件.
function showMenu()
{
	var menu = currentUIStyle.menu;
	menu.setNotAuthorizedDisplayType('disable');

	mi = new MenuItem({name: 'EMV查詢', authorized: true});

	subMi = new MenuItem( {name: 'EMV查詢', url: 'toEmvTagRecordQuery.do', progId: 'emvQuery', authorized: true, target: 'mainFrame'});
	mi.addMenuItem( subMi );
	menu.addMenuItem( mi );
	
	mi = new MenuItem({name: 'EMV維護', authorized: true});
	subMi = new MenuItem( {name: 'EMV新增', url: 'toEmvTagRecordCreate.do', progId: 'addEmv', authorized: true, target: 'mainFrame'});
	mi.addMenuItem( subMi );
	subMi = new MenuItem( {name: 'EMV修改', url: 'toEmvTagRecordModQuery.do', progId: 'modEmv', authorized: true, target: 'mainFrame'});
	mi.addMenuItem( subMi );
	subMi = new MenuItem( {name: 'EMV刪除', url: 'toEmvTagRecordDelQuery.do', progId: 'delEmv', authorized: true, target: 'mainFrame'});
	mi.addMenuItem( subMi );
	subMi = new MenuItem( {name: 'EMV覆核', url: 'toEmvTagRecordTemp.do', progId: 'checkEmv', authorized: true, target: 'mainFrame'});
	mi.addMenuItem( subMi );
	menu.addMenuItem( mi );
	
	mi = new MenuItem({name: '卡別維護', authorized: true});			
	subMi = new MenuItem( {name: '卡別維護', url: 'toMaintainEmvCardType.do', progId: 'maintainCardType', authorized: true, target: 'mainFrame'});
	mi.addMenuItem( subMi );
	subMi = new MenuItem( {name: '卡別覆核', url: 'toEmvCardTypeTemp.do', progId: 'checkCardType', authorized: true, target: 'mainFrame'});
	mi.addMenuItem( subMi );
	menu.addMenuItem( mi );
	
	mi = new MenuItem({name: '參考規格維護', authorized: true});			
	subMi = new MenuItem( {name: '參考規格維護', url: 'toMaintainEmvRefSpec.do', progId: 'maintainSpec', authorized: true, target: 'mainFrame'});
	mi.addMenuItem( subMi );
	subMi = new MenuItem( {name: '參考規格覆核', url: 'toEmvRefSpecTemp.do', progId: 'checkSpec', authorized: true, target: 'mainFrame'});
	mi.addMenuItem( subMi );
	menu.addMenuItem( mi );
	
	mi = new MenuItem({name: 'UI歷程查詢', authorized: true});							
	subMi = new MenuItem( {name: 'UI歷程查詢', url: 'toEmvUiLogQuery.do', progId: 'uiLogQuery', authorized: true, target: 'mainFrame'});
	mi.addMenuItem( subMi );
	menu.addMenuItem( mi );
	
	
	mi = new MenuItem( {name: '待辦事項', url: 'toBeApprove.do', progId: 'todoList', authorized: true, target: 'mainFrame'});
	menu.addMenuItem( mi );

	menu.setMenuBlockStyleClass("menuBlock");
	menu.setMenuListStyleClass("menuList");
	menu.setMenuItemStyleClass("menuItem");
	menu.setDisabledStyleClass("disabledMenu");
	menu.setOpenedImageInfo( new ImageInfo("../images/folder_opened.gif","18px","15px") );
	menu.setClosedImageInfo( new ImageInfo("../images/folder_closed.gif","18px","15px") );
	menu.setLinkImageInfo( new ImageInfo("../images/link.gif","16px","15px") );
	menu.setDefaultOpened( false );
	menu.setOpenFirst( true );

	menu.showMenu();
}

showMenu();
/**
 * 開啟預設要執行之功能.
 * 透過dojo呼叫時, 若傳入menu.openLinkOnStart無法正常運作, 所以利用自訂function來呼叫.
 */
 /*
function openLinkOnStart()
{
	menu.openLinkOnStart();
}
dojo.addOnLoad( openLinkOnStart );
*/
