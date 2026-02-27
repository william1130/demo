/**
 * ---------------------------------------------------------------------------------------------------
 * $Id: project.js,v 1.1 2014/11/14 03:14:14 asiapacific\hsiehes Exp $
 * ---------------------------------------------------------------------------------------------------
 */
/**
 * 系統初始設定物件.
 */
/**
 * 建立系統初始設定物件. common.js中的DefaultProjConfig可能還包含其他參數.
 */
var EdsProjConfig = new DefaultProjConfig(
	{
		//設定本系統設定的cookie名稱
		projectCookieName: "LogSearchConfig",
		//預設的畫面style.
		defaultUIStyle: "tab",
		//frame style的首頁
		frameStyleHomePage: "index_frame.jsp",
		//tab style的首頁
		tabStyleHomePage: "index_tab.jsp",
		//window style的首頁
		windowStyleHomePage: "",
		//設定工作區預設寬度(用於innerFrame的預設最大寬度)
		workspaceWidth: '95%',
		//設定工作區預設高度(用於innerFrame的預設最大高度)
		workspaceHeight: '92%',
		//設定本系統中dialog的factory類別名稱
		dialogFactory: "DialogFactory",
		//在IE6以前, div元件無法蓋住下拉選單, 指定此參數可於使用dialog時, 自動隱藏下拉選單.
		hideSelectOnIE: true,
		//用來指定系統中哪些id在載入時要做圓角的設定(但仍要視PreferenceConfig.roundBlocks若為true時, 才會做設定).
		roundBlockIds: "ajaxResultBlock progTitle criteriaBlock dataBlock dataBlock1 dataBlock2 dataBlock3 inputBlock inputBlock1 confirmBlock",
		//設定此系統中最多開啟的Tab數.
		maxTabCount: 5
	}
);

function confirmDelete()
{
	return confirm( 'label.delete.confirm'.i18n() );
}

function confirmLogout()
{
	return confirm("是否確定登出?");
}

function activate( event, args )
{
	var theForm = $(this);
	if( args.enableNames )
	{
		$w(args.enableNames).each(
			function( name )
			{
				theForm.select("[name='" + name + "']").invoke('enable');
			}
		);
	}
	if( args.disableNames )
	{
		$w(args.disableNames).each(
			function( name )
			{
				theForm.select("[name='" + name + "']").invoke('disable');
			}
		);
	}
}

function confirmLogout()
{
	return confirm("是否確定登出?");
}

function toggle( event )
{
	this.doToggle();
}

function toggleFieldsHandler( event )
{
	toggleFields( event.element() );
}

function toggleFields( field )
{
	var span = $(field.name+"Span");
	if( field.checked )
	{
		span.setStyle( {display: ""} );
		span.select("input").each( Form.Element.enable );
	}
	else
	{
		span.setStyle( {display: "none"} );
		span.select("input").each( Form.Element.disable );
	}
}

function openDataWindow( event, url, width, height )
{
	if( event )
		Event.stop( event );
	var w = width;
	if( !w )
		w = "750";
	var h = height;
	if( !h )
		h = "600";
	window.open( url, '_blank','width=' + w + 'px, height=' + h + 'px, location=no, menubar=no, status=yes, toolbar=no, resizeable=yes, scrollbars=yes');
}
function checkYYMMDateValid(yymm)
{    
	var mm = yymm.substring(2);	
	if ( !isNumber(parseInt(yymm,10)) )
	{		  
	    return false;
	}
	else
	{		
		if(parseInt(mm,10)==0 || parseInt(mm,10)>12)
		{ 
		  return false;
		}
	}
	return true;
}
