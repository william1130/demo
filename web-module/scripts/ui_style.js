/**
 * ---------------------------------------------------------------------------------------------------
 * $Id: ui_style.js,v 1.1 2014/01/02 02:54:42 asiapacific\hungmike Exp $
 * ---------------------------------------------------------------------------------------------------
 */

var currentUIStyle;

var UIStyle = Class.create();
/**
 * 功能說明：依使用者設定之uiStyle, 建立對應之UIStyle物件後傳回.
 */
UIStyle.getUIStyle = function()
{
	if( !currentUIStyle )
	{
		if( !preferenceConfig )
		{
			PreferenceConfig.getInstance();
		}
		switch( preferenceConfig.uiStyle )
		{
			case 'window' :
				currentUIStyle = new WindowUIStyle();
				break;
			case 'frame' :
				currentUIStyle = new FrameUIStyle();
				break;
			case 'tab' :
				currentUIStyle = new TabUIStyle();
				break;
			default :
				alert( 'ui.unknownStyle'.i18n( preferenceConfig.uiStyle ) );
				currentUIStyle = new FrameUIStyle();
				break;
		}
	}
	return currentUIStyle;
}

/**
 * 檢查傳入之uiStyle是否與使用者設定相同, 若不相同, 則依實際指定之uiStyle連至對應的首頁.
 */
UIStyle.checkUIStyle = function( uiStyle )
{
	if( !currentUIStyle )
	{
		UIStyle.getUIStyle();
	}
	if( currentUIStyle.uiStyle != uiStyle )
		currentUIStyle.linkToHomePage();
}

UIStyle.prototype =
{
	initialize: function()
	{
		this.homePage = 'undefined';
		this.uiStyle = 'undefined';
	},
	linkToHomePage: function()
	{
		//window.top.location.href = this.homePage;
		window.location.href = this.homePage;
	}
}

var WindowUIStyle = Class.create();
WindowUIStyle.prototype = Object.extend( new UIStyle(),
	{
		initialize: function()
		{
			this.uiStyle = 'window';
			this.homePage = EdsProjConfig.windowStyleHomePage;
			this.menu = new WindowStyleMenu();
			this.menu.setOptions( {
				mainContentPaneId: 'mainContentPane',
				windowIconSrc: "images/link.gif",
				taskBarId: "mytaskbar"
				}
			);
		}
	}
);

var FrameUIStyle = Class.create();
FrameUIStyle.prototype = Object.extend( new UIStyle(),
	{
		initialize: function()
		{
			this.uiStyle = 'frame';
			this.homePage = EdsProjConfig.frameStyleHomePage;
			this.menu = new FrameStyleMenu();
			this.menu.setOptions( {
				mainContentPaneId: 'mainContentPane',
				mainContentFrameId: 'mainContentFrame'
				}
			);
		}
	}
);

var TabUIStyle = Class.create();
TabUIStyle.prototype = Object.extend( new UIStyle(),
	{
		initialize: function()
		{
			this.uiStyle = 'tab';
			this.homePage = EdsProjConfig.tabStyleHomePage;
			this.menu = new TabStyleMenu();
			this.menu.setOptions( {
				mainContentPaneId: 'mainContentPane'
				//mainTabContainer: mainTabContainer
				}
			);
		}
	}
);
