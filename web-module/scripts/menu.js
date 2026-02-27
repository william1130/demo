/**
 * ---------------------------------------------------------------------------------------------------
 * $Id: menu.js,v 1.1 2014/01/02 02:54:41 asiapacific\hungmike Exp $
 * ---------------------------------------------------------------------------------------------------
 */
var Menu = Class.create();
Menu.prototype = 
{
	initialize: function()
	{
		this.menuItems = new Array();
		this.menuBlockStyleClass = "menuBlock";
		this.menuListStyleClass = "menuList";
		this.menuItemStyleClass = "menuItem";
		this.disabledStyleClass = "disabledMenu";
		this.defaultOpened = false;
		this.openFirst = true;
		this.notAuthorizedDisplayType = "show";
		this.openOnStartMenuItem = null;
	},
	//加入MenuItem
	addMenuItem: function( menuItem )
	{
		this.menuItems.push( menuItem );
	},
	setOpenedImageInfo: function( im )
	{
		this.openedImageInfo = im;
	},
	setClosedImageInfo: function( im )
	{
		this.closedImageInfo = im;
	},
	setLinkImageInfo: function( im )
	{
		this.linkImageInfo = im;
	},
	setMenuBlockStyleClass: function( className )
	{
		this.menuBlockStyleClass = className;
	},
	setMenuListStyleClass: function( className )
	{
		this.menuListStyleClass = className;
	},
	setMenuItemStyleClass: function( className )
	{
		this.menuItemStyleClass = className;
	},
	setDisabledStyleClass: function( className )
	{
		this.disabledStyleClass = className;
	},
	//設定是否所有folder都預設為打開
	setDefaultOpened: function( defaultOpened )
	{
		this.defaultOpened = defaultOpened;
	},
	//設定第一個folder是否預設為打開
	setOpenFirst: function( openFirst )
	{
		this.openFirst = openFirst;
	},
	setNotAuthorizedDisplayType: function( notAuthorizedDisplayType )
	{
		this.notAuthorizedDisplayType = notAuthorizedDisplayType;
	},
	setOpenOnStartMenuItem: function( openOnStartMenuItem )
	{
		this.openOnStartMenuItem = openOnStartMenuItem;
	},
	//以progId查詢對應的MenuItem.
	findMenuItem: function( progId )
	{
		var menuItem;
		$A( this.menuItems ).each( 
			function( mi )
			{
				menuItem = mi.findMenuItem( progId );
				if( menuItem != null )
					throw $break;
			}
		);
		return menuItem;
		/*
		for( i=0; i<this.menuItems.length ; i++ )
		{
			var mi = this.menuItems[i].findMenuItem( progId );
			if( mi != null )
				return mi;
		}
		alert("not found.");
		 */
	},

	showMenu: function()
	{
		var imgInfo = this.closedImageInfo;
		var folderType = "closed";
		if( this.defaultOpened )
		{
			imgInfo = this.openedImageInfo;
			folderType = "opened";
		}
		//進入for loop後, this不是指向menu, 而是指向mi, 所以在此先設定menu變數.
		menu = this;
		document.write('<div class="' + this.menuBlockStyleClass +'">');
		$A( this.menuItems ).each( 
			function( mi, index )
			{
				if( mi.hasSubMenu )
				{
					if( index == 0 && menu.openFirst )
						menu.showFolder( mi, "opened", menu.openedImageInfo );
					else
						menu.showFolder( mi, folderType, imgInfo );
				}
				else
				{
					menu.showMenuItem( mi );
				}
			}
		);
		document.write('</div>');
	},
	
	/**
	 * 顯示目錄型式的menu item.
	 */
	showFolder: function( menuItem, folderType, imgInfo )
	{
		if( menuItem.isAuthorized() || menu.notAuthorizedDisplayType != 'hide' )
		{
			document.write('	<div class="' + folderType + '">');
			document.write('		<a href="#" title="' + menuItem.args.name + '" onclick="return menu.openCloseFolder(this);" class="' + this.menuListStyleClass + '">');
			document.write('			<img border="0" src="' + imgInfo.url + '" width="' + imgInfo.width + '" height="' + imgInfo.height + '"/> ' + menuItem.args.name );
			document.write('		</a>');
			//進入for loop後, this不是指向menu, 而是指向mi, 所以在此先設定menu變數.
			menu = this;
			$A( menuItem.menuItems ).each( 
				function( mi )
				{
					if( mi.hasSubMenu )
					{
						//目錄下之子目錄預設不用打開
						menu.showFolder( mi, "closed", menu.closedImageInfo );
					}
					else
					{
						menu.showMenuItem( mi );
					}
					
				}
			);
			document.write('	</div>');
		}
	},
	
	/**
	 * 顯示可點選之menu item.
	 */
	showMenuItem: function( menuItem )
	{
		if( menuItem.isAuthorized() || this.notAuthorizedDisplayType != 'hide' )
		{
			var str;
			var styleClass;
			
			if( menuItem.isAuthorized() || this.notAuthorizedDisplayType == 'show' )
			{
				str = this.getMenuItemLink( menuItem );
				styleClass = this.menuItemStyleClass;
			}
			else if( this.notAuthorizedDisplayType == 'disable' )
			{
				str = '<a href="#" onclick="showAlert(\''+ 'menu.notAuthorized'.i18n() +'\')">'
				styleClass = this.disabledStyleClass;
			}
			document.write('		<div class="' + styleClass + '">');
			document.write( str );
			document.write('				<img border="0" src="' + this.linkImageInfo.url + '" width="' + this.linkImageInfo.width + '" height="' + this.linkImageInfo.height + '"/> ' + menuItem.args.name );
			document.write('			</a>');
			document.write('		</div>');
		}
	},
	
	getMenuItemLink: function( menuItem )
	{
		return '<a href="' + menuItem.args.url + '?' + menuItem.args.params.toQueryString() + '" title="' + menuItem.args.name + '(' +menuItem.args.progId + ')" target="' + menuItem.args.target + '">';
	},
	
	/**
	 * 控制menu的開關.
	 */
	openCloseFolder: function( node )
	{
		if( node.parentNode.className == "opened" )
		{
			node.parentNode.className = "closed";
			node.getElementsByTagName("img")[0].src = this.closedImageInfo.url;
		}
		else if( node.parentNode.className == "closed" )
		{
			node.parentNode.className = "opened";
			node.getElementsByTagName("img")[0].src = menu.openedImageInfo.url;
		}
		return false;
	},
	
	openLinkOnStart: function()
	{
		if( this.openOnStartMenuItem )
		{
			this.openLink( this.openOnStartMenuItem );
		}
	},
	
	/**
	 * 當使用者點選menu item時, 打開連結的方式.
	 */
	openLink: function( menuItem )
	{
	}
}
/* Menu輸出後之格式大致如下 :
	<div class="menuBlock">
		<div class="closed">
			<a href="#" onclick="openClose(this,'<c:out value="openedImg"/>','<c:out value="closedImg"/>'); return false;" class="menuList">
				<img border="0" src="<c:out value='${closedImg}'/>" width="18px" height="15px"/> 需求單維護作業
			</a>
			<div class="menuItem"><a href="#"><img border="0" src="<c:out value='${linkImg}'/>" width="16px" height="15px"/> 需求單新增</a></div>
			<div class="menuItem"><a href="#"><img border="0" src="<c:out value='${linkImg}'/>" width="16px" height="15px"/> 需求單維護</a></div>
		</div>
		<div class="closed">
			<a href="#" onclick="openClose(this,'<c:out value="openedImg"/>','<c:out value="closedImg"/>'); return false;" class="menuList">
				<img border="0" src="<c:out value='${closedImg}'/>" width="18px" height="15px"/> 需求單管制報表
			</a>
			<div class="menuItem"><a href="#"><img border="0" src="<c:out value='${linkImg}'/>" width="16px" height="15px"/> 一般報表查詢</a></div>
		</div>
		<div class="closed">
			<a href="#" onclick="openClose(this,'<c:out value="openedImg"/>','<c:out value="closedImg"/>'); return false;" class="menuList">
				<img border="0" src="<c:out value='${closedImg}'/>" width="18px" height="15px"/> 會辦單維護作業
			</a>
			<div class="menuItem"><a href="#"><img border="0" src="<c:out value='${linkImg}'/>" width="16px" height="15px"/> 會辦單新增</a></div>
			<div class="menuItem"><a href="#"><img border="0" src="<c:out value='${linkImg}'/>" width="16px" height="15px"/> 會辦單維護</a></div>
		</div>
		<div class="closed">
			<a href="#" onclick="openClose(this,'<c:out value="openedImg"/>','<c:out value="closedImg"/>'); return false;" class="menuList">
				<img border="0" src="<c:out value='${closedImg}'/>" width="18px" height="15px"/> 系統參數維護
			</a>
			<div class="menuItem"><a href="#"><img border="0" src="<c:out value='${linkImg}'/>" width="16px" height="15px"/> 組別資料維護</a></div>
			<div class="menuItem"><a href="#"><img border="0" src="<c:out value='${linkImg}'/>" width="16px" height="15px"/> 組別資料維護</a></div>
		</div>
	</div>
	
	當useDojoLayout為true, multiWindow為true時, 連結的格式如下
	<a href="#" onclick="openUrlInWindow( {windowId:'A01', containerId:'mainContentPane', url:'<c:url value="/ticket/toNewTicket.do"/>', title:'需求單新增', taskBarId:'mytaskbar', iconSrc:'<c:url value="/images/link.gif"/>'} )">
	當useDojoLayout為true, multiWindow為false時, 連結的格式如下
	<a href="#" onclick="openUrlInContentPane( {paneId: 'mainContentPane',url: '<c:url value="/ticket/toNewTicket.do"/>',onLoadFunc: initialEffect} )">
*/
var MenuItem = Class.create();
MenuItem.prototype = 
{
	/**
	 * 建立MenuItem, 可用之參數如下:
	 * name (required) : 程式名稱.
	 * url (required, 若為folder時則可不必指定) : 程式的網址.
	 * target : 若Menu不使用dojo的function時, 設定target可指定連結開啟所使用的視窗.
	 * params : 若有額外的參數要送出時, 可以此參數設定, 格式如同 {name1: value1, name2: value2}.
	 * progId : 程式的ID, 此ID必須為唯一. 當使用dojo的window時, 此ID會當成windowId來使用.
	 * authorized : 設定使用者對本項目是否有執行權限.
	 * beforeOpenFuncName : callback function名稱, 若有設定, 則在開啟連結前會先被呼叫, 本函式若傳回false, 就會中止執行, 不會開啟連結.
	 * afterOpenFuncName : callback function名稱, 若有設定, 則在開啟連結後會被呼叫. 本函式無需傳回值.
	 */
	initialize: function( args )
	{
		this.args = args;
		if( this.args.params )
		{
			this.args.params = $H(params);
			this.args.params.merge( {_requestTimestamp: new Date().getTime() } );
		}
		else
		{
			this.args.params = $H( {_requestTimestamp: new Date().getTime() } );
		}
		this.menuItems = new Array();
		this.hasSubMenu = false;
	},
	//加入MenuItem, 若MenuItem中加入MenuItem, 則該MenuItem即為一個folder. 會以folder的型式顯示. 該MenuItem中的url將沒有任何作用.
	addMenuItem: function( menuItem )
	{
		this.menuItems.push( menuItem );
		this.hasSubMenu = true;
	},
	isAuthorized: function()
	{
		return eval(this.args.authorized);
	},
	//以progId查詢對應的MenuItem.
	findMenuItem: function( progId )
	{
		if( this.hasSubMenu )
		{
			var menuItem;
			$A( this.menuItems ).each( 
				function( mi )
				{
					menuItem = mi.findMenuItem( progId );
					if( menuItem != null )
						throw $break;
				}
			);
			return menuItem;
		}
		else
		{
			if( this.args.progId == progId )
			{
				return this;
			}
			return null;
		}
	}
}

var ImageInfo = Class.create();
ImageInfo.prototype = 
{
	initialize: function(url, width, height)
	{
		this.url = url;
		this.width = width;
		this.height = height;
	}
}

var WindowStyleMenu = Class.create();
//直接繼承物件, Menu中初始化的動作也會被繼承, 若是繼承自Menu.prototype, 則必須自訂initialize.
WindowStyleMenu.prototype = Object.extend( new Menu(), 
	{
		initialize: function()
		{
		},
		setOptions: function( options )
		{
			this.options = options;
		},
	
		getMenuItemLink: function( menuItem )
		{
			return '<a href="#" onclick="menu.openLink( menu.findMenuItem(\''+ menuItem.args.progId +'\') )" title="' + menuItem.args.name + '(' + menuItem.args.progId + ')">';
		},
		
		openLink: function( menuItem )
		{
			showAlert( 'menu.window.notSupport'.i18n() );
		}
	}
);

var FrameStyleMenu = Class.create();
FrameStyleMenu.prototype = Object.extend( new Menu(), 
	{
		initialize: function()
		{
		},
		/**
		 * 設定參數, 有以下參數可設定.
		 * mainContentPaneId : 指定包含frame的container的id.
		 * mainContentFrameId : 指定frame的id.
		 */
		setOptions: function( options )
		{
			this.options = options;
		},
	
		getMenuItemLink: function( menuItem )
		{
			return '<a href="#" onclick="menu.openLink( menu.findMenuItem(\''+ menuItem.args.progId +'\') )" title="' + menuItem.args.name + '(' + menuItem.args.progId + ')">';
		},
		openLink: function( menuItem )
		{
			var args = menuItem.args;
			if( args.progId != null && Widget.findWidgetById(args.progId) )
			{
				showAlert( 'menu.error.rerun'.i18n( args.name ) );
				return;
			}
			if( args.beforeOpenFuncName )
			{
				//若beforeOpenFuncName傳回值為false時, 代表不要繼續執行.
				if( eval( args.beforeOpenFuncName + "()") == false )
					return;
			}
			var frame = $(this.options.mainContentFrameId);
			frame.src = args.url + '?' + args.params.toQueryString();
			if( args.afterOpenFuncName )
				args.afterOpenFuncName();
		}
	}
);

var TabStyleMenu = Class.create();
TabStyleMenu.prototype = Object.extend( new Menu(), 
	{
		initialize: function()
		{
		},
		/**
		 * 設定參數, 有以下參數可設定.
		 * mainContentPaneId : 指定連結時視窗要顯示的container的id, 該container必須為DOJO的ContentPane.
		 */
		setOptions: function( options )
		{
			this.options = options;
		},
	
		getMenuItemLink: function( menuItem )
		{
			return '<a href="#" onclick="menu.openLink( menu.findMenuItem(\''+ menuItem.args.progId +'\') )" title="' + menuItem.args.name + '(' + menuItem.args.progId + ')">';
		},
		openLink: function( menuItem )
		{
			if( !this.tabContainer )
				this.tabContainer = Widget.findWidgetById( this.options.mainContentPaneId );
			if( preferenceConfig.maxTabCount != 0 && this.tabContainer.tabCounts >= preferenceConfig.maxTabCount )
			{
				showAlert( 'menu.tab.error.max'.i18n( preferenceConfig.maxTabCount ) );
				return;
            }
			var args = menuItem.args;
			if( args.progId != null && Widget.findWidgetById(args.progId) )
			{
				showAlert( 'menu.error.rerun'.i18n( args.name ) );
				return;
			}
			if( args.beforeOpenFuncName )
			{
				//若beforeOpenFuncName傳回值為false時, 代表不要繼續執行.
				if( eval( args.beforeOpenFuncName + "()") == false )
					return;
			}
			var options = 
			{
				id: args.progId,
				url: args.url + '?' + args.params.toQueryString()
			}
			var pane = new ContentPane( options );
			this.tabContainer.addTab( {tabContent: pane, closable: true, selected: true, reloadable: false, title: args.name} );
			if( args.afterOpenFuncName )
				args.afterOpenFuncName();
		}
	}
);
