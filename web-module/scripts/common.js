/**
 * ---------------------------------------------------------------------------------------------------
 * $Id: common.js,v 1.1 2014/01/02 02:54:40 asiapacific\hungmike Exp $
 * ---------------------------------------------------------------------------------------------------
 */
String.prototype.i18n = function()
{
	var ret = i18nMsg[this];
	if( ret )
	{
		if( arguments.length > 0 )
		{
			var args = "{";
			for( var i=0,size=arguments.length; i<size; i++ )
			{
				args += "\"arg" + (i+1) + "\": \"" + arguments[i] + "\"";
				if( i<size-1 )
					args += ",";
			}
			args += "}";
			ret = ret.interpolate( args.evalJSON() );
		}
	}
	else
	{
		ret = "???" + this + "???";
	}
	return ret;
}
/**
 * 依傳入之字串及指定的長度, 當字串長度不足時, 在字串左方補入指定的字元
 */
String.prototype.paddingLeft = function( length, padChar )
{
	var str = "";
	if(this.length < length)
	{
		for( var i = 0;i < length - this.length ;i++)
		{
			str = str + padChar;
		}
		return str + this;
	}
	return this;
}
/**
 * 依傳入之字串及指定的長度, 當字串長度不足時, 在字串左方補入指定的字元
 */
String.prototype.paddingRight = function( length, padChar )
{
	var str = "";
	if(this.length < length)
	{
		for( var i = 0;i < length - this.length ;i++)
		{
			str = str + padChar;
		}
		return this + str;
	}
	return this;
}

/**
 * 系統設定值類別.
 * 為了未來新增參數時, 便於在不同系統中更新javascript版本, 所以將系統設定值定義成DefaultProjConfig類別, 各個project則直接於建立EdsProjConfig物件, 並於建立時指定參數,
 * 為指定之參數則使用類別中定義的預設值, 如此一來, 若新增了新參數, 即使EdsProjConfig中沒有加入, 系統仍可以預設值繼續運作, 且在複製javascript時, 也避免掉覆蓋到project.js的危險.
 */
var DefaultProjConfig = Class.create(
	{
		initialize: function( args )
		{
			//設定本系統設定的cookie名稱
			this.projectCookieName = args.projectCookieName || "defaultConfig";
			//預設的畫面style.
			this.defaultUIStyle = args.defaultUIStyle || "tab";
			//frame style的首頁
			this.frameStyleHomePage = args.frameStyleHomePage || "index_frame.html";
			//tab style的首頁
			this.tabStyleHomePage = args.tabStyleHomePage || "index_tab.html";
			//window style的首頁
			this.windowStyleHomePage = args.windowStyleHomePage || "";
			//設定工作區預設寬度(用於innerFrame的預設最大寬度)
			this.workspaceWidth = args.workspaceWidth || '95%';
			//設定工作區預設高度(用於innerFrame的預設最大高度)
			this.workspaceHeight = args.workspaceHeight || '92%';
			//設定本系統中dialog的factory類別名稱
			this.dialogFactory = args.dialogFactory || "DialogFactory";
			//在IE6以前, div元件無法蓋住下拉選單, 指定此參數可於使用dialog時, 自動隱藏下拉選單.
			this.hideSelectOnIE = args.hideSelectOnIE || true;
			//用來指定系統中哪些id在載入時要做圓角的設定(但仍要視PreferenceConfig.roundBlocks若為true時, 才會做設定).
			this.roundBlockIds = args.roundBlockIds || "ajaxResultBlock progTitle criteriaBlock dataBlock dataBlock1 dataBlock2 dataBlock3 inputBlock inputBlock1 confirmBlock";
			//設定此系統中最多開啟的Tab數.
			this.maxTabCount = args.maxTabCount || 5;
			//設定是否要使用動態效果.
			this.animateEffects = true;
			//設定是否要做roundBlock
			this.roundBlocks = true;
			//IFrame是否為draggable
			this.iframeDraggable = false;
		}
	}
);

var preferenceConfig;
var PreferenceConfig = Class.create(
	{
		initialize: function()
		{
			this.uiStyle = EdsProjConfig.defaultUIStyle;
			this.roundBlocks = EdsProjConfig.roundBlocks;
			this.animateEffects = EdsProjConfig.animateEffects;
			this.iframeDraggable = EdsProjConfig.iframeDraggable;
			this.maxTabCount = EdsProjConfig.maxTabCount;
			this.hideSelectOnIE = EdsProjConfig.hideSelectOnIE;
		},
		loadPreference: function()
		{
			if( CookieUtils.isCookieEnabled )
			{
				var cookie = CookieUtils.getCookie( EdsProjConfig.projectCookieName );
				if( cookie )
				{
					//必須先呼叫.toJSON(), 否則在IE上會出錯.
					var json = cookie.evalJSON(true);
					if( json.uiStyle )
						this.uiStyle = json.uiStyle;

					//讀取boolean參數時, 需考量參數不存在時, 必須使用原本的預設值.
					//設定預設之區塊是否要有rounded效果
					if( json.roundBlocks == true )
						this.roundBlocks = true;
					else if( json.roundBlocks == false )
						this.roundBlocks = false;

					//設定預設之區塊是否要有動態效果
					if( json.animateEffects == true )
						this.animateEffects = true;
					else if( json.animateEffects == false )
						this.animateEffects = false;

					//開啟之iframe是否可拖拉.
					if( json.iframeDraggable == true )
						this.iframeDraggable = true;
					else if( json.iframeDraggable == false )
						this.iframeDraggable = false;
				}
			}
		},
		save: function()
		{
			if( CookieUtils.isCookieEnabled )
			{
				CookieUtils.setCookie( EdsProjConfig.projectCookieName, Object.toJSON( this ), 90 );
			}
		},
		clear: function()
		{
			if( CookieUtils.isCookieEnabled )
			{
				CookieUtils.setCookie( EdsProjConfig.projectCookieName, "" );
			}
		}
	}
);
PreferenceConfig.getInstance = function()
{
	if( !preferenceConfig )
	{
		preferenceConfig = new PreferenceConfig();
		preferenceConfig.loadPreference();
	}
	return preferenceConfig;
}
/**
 * 為向前相容, 所以定義此function以便傳回roundBlocks之值
 */
PreferenceConfig.roundBlocks = function()
{
	return PreferenceConfig.getInstance().roundBlocks;
}

//*************************************************************
//	系統變數 begin
//*************************************************************
//判斷是否為IE.
var ieBrowser = Prototype.Browser.IE;
var geckoBrowser = Prototype.Browser.Gecko;
var webKitBrowser = Prototype.Browser.WebKit;
var operaBrowser = Prototype.Browser.Opera;
//*************************************************************
//	系統變數 end
//*************************************************************

//*************************************************************
//	系統event register begin
//*************************************************************
var EdsEvent =
{
	/**
	 * 將傳入之function註冊到window onload event. 會依加入之順序呼叫.
	 */
	addOnLoad: function( func )
	{
		//Event.observe( window, "load", func );
		if( !this.onLoadListeners )
			this.onLoadListeners = new Array();
		this.onLoadListeners.push( func );
	},
	/**
	 * 將傳入之function註冊到window onunload event. 會依加入之順序呼叫.
	 */
	addOnUnload: function( func )
	{
		//Event.observe( window, "unload", func );
		if( !this.onUnloadListeners )
			this.onUnloadListeners = new Array();
		this.onUnloadListeners.push( func );
	},
	addOnChange: function( field, func )
	{
		Event.observe( field, "change", func );
	},
	/**
	 * 執行已註冊onLoad event發生時所要執行的所有function. 執行後所有註冊的function都會被清除.
	 * 此函式不應直接呼叫, 應由系統發生onload event時, 由該event handler呼叫,
	 * 或是在使用AJAX技術載入資料時, 由AJAX的handler function呼叫, 此舉主要是考量透過AJAX載入之資料並不會有onLoad event發生, 所以就由AJAX的handler function扮演該角色,
	 * 在載入資料後執行傳回之資料中註冊要在onload時執行之function.
	 * 採用此種作法也可同時兼顧同樣頁面透過AJAX取得或直接request取得時都能正確運作.
	 */
	runOnLoad: function( event )
	{
		if( this.onLoadListeners )
		{
			while( this.onLoadListeners.length > 0 )
			{
				var func = this.onLoadListeners.shift();
				func();
			}
		}
	},
	_onLoad: function( event )
	{
		this.runOnLoad();
	},
	_onUnload: function( event )
	{
		if( this.onUnloadListeners )
		{
			while( this.onUnloadListeners.length > 0 )
			{
				var func = this.onUnloadListeners.shift();
				func();
			}
		}
	}
}
Event.observe( window, "load", EdsEvent._onLoad.bindAsEventListener( EdsEvent ) );
Event.observe( window, "unload", EdsEvent._onUnload.bindAsEventListener( EdsEvent ) );

//*************************************************************
//	系統event register end
//*************************************************************
/*
var EdsLog =
{
	debug: function( msg )
	{
		window.console.debug( msg );
	},
	log: function( msg )
	{
		window.console.log( msg );
	},
	info: function( msg )
	{
		window.console.info( msg );
	},
	warn: function( msg )
	{
		window.console.warn( msg );
	},
	error: function( msg )
	{
		window.console.error( msg );
	}
}
*/


//*************************************************************
//	Browser, DOM及CSS相關工具 begin
//*************************************************************

var BrowserUtils =
{
	/**
     * 在onload時可呼叫本函式, 將要載入之圖片當成參數依序傳入即可.
     */
	preloadImages: function()
	{
		$A(arguments).each( function( arg )
			{
				var img = new Image();
				img.src = arg;
            }
		)
    },
	/**
     * 將本視窗的opener重新載入, 若該opener原為form, 就submit該form, 若不是form, 就直接reload該頁面.
     */
	reloadOpener: function()
	{
		var openerWindow = BrowserUtils.findOpenerWindow();
		if( !openerWindow )
			alert("Can't refresh opener, no opener or parent exist");
		else
		{
			//若原網頁有form, 就submit該form, 若沒有form, 就做refresh().
			if( openerWindow.document.forms[0] )
			{
				//若submit button存在, 就觸發submit button的onsubmit(), 這樣該form若有定義檢查資料的函式, 才會被呼叫.
				if( openerWindow.document.forms[0]['submitButton'] )
				{
					openerWindow.document.forms[0]['submitButton'].click();
				}
				else
				{
					openerWindow.document.forms[0].submit();
				}
			}
			else
			{
				openerWindow.location.reload();
			}
		}
    },
	/**
	 * 呼叫本函式, 將function名稱(字串)及參數傳入, 則本函式會自動呼叫該function執行.
	 */
	callFunction: function ( functionName, args )
	{
		var fun = eval(functionName);
		if( Object.isFunction(fun) )
			fun( args );
		else
			showAlert( 'browser.function.notExist'.i18n( functionName ) );
	},

	/**
	 * 在子視窗中若要呼叫父視窗中的某一函式時, 可呼叫本函式, 將function名稱(字串)及參數傳入, 則本函式會自動呼叫該function執行.
	 */
	callOpenerFunction: function ( functionName, args )
	{
		var windowObj = BrowserUtils.findOpenerWindow();
		if( windowObj == null )
		{
			alert( 'browser.opener.notExist'.i18n() );
		}
		else
		{
			//利用callFunction來執行指定的function.
			windowObj.callFunction( functionName, args );
		}
	},
	/**
     * 由於IE6之前版本的select元件是使用window做出來的, 所以在顯示時會顯示在div之上, 無法以zIndex來將其蓋住,
     * 所以提供此函式, 可將目前document中所有的select元件全部設成visibility為hidden.
     * 若有需要在畫面上暫時以區塊蓋住select時, 可先呼叫本函式. 並於區塊移除時, 再呼叫showSelectOnIE()將select元件顯現出來.
     */
	hideSelectOnIE: function()
	{
		if( ieBrowser )
		{
			var objs = $(document.body).select('select');
			$A(objs).each(
				function( obj )
				{
					obj.setStyle("visibility: hidden;");
				}
			);
		}
    },
	/**
     * 搭配hideSelectOnIE()使用, 將被隱藏的select元件顯示出來.
     */
	showSelectOnIE: function()
	{
		if( ieBrowser )
		{
			var objs = $(document.body).select('select');
			$A(objs).each(
				function( obj )
				{
					obj.setStyle("visibility: visible;");
				}
			);
		}
    },
	hideElements: function( objs )
	{
		if( typeof(objs) == 'string' )
		{
			$w(objs).each(
				function( id )
				{
					var obj = $(id);
					if( obj )
					{
						obj.setStyle("visibility: hidden;");
					}
				}
			)
		}
		else
		{
			$A(objs).each(
				function( obj )
				{
					obj.setStyle("visibility: hidden;");
				}
			)
		}
	},
	showElements: function( objs )
	{
		if( typeof(objs) == 'string' )
		{
			$w(objs).each(
				function( id )
				{
					var obj = $(id);
					if( obj )
					{
						obj.setStyle("visibility: visible;");
					}
				}
			)
		}
		else
		{
			$A(objs).each(
				function( obj )
				{
					obj.setStyle("visibility: visible;");
				}
			)
		}
	},
	/**
	 * 檢查傳入之id是否存在於目前的docuemnt中. 若沒有, 會顯示錯誤訊息並傳回false.
	 * 若傳在, 則會傳回true.
	 */
	checkIdExists: function ( id )
	{
		if( !$(id) )
		{
			showAlert( 'browser.id.notExist'.i18n( id ) );
			return false;
		}
		return true;
	},

	/**
	 * 尋找目前視窗的opener或parent視窗, 若沒有, 就傳回undefined.
	 */
	findOpenerWindow: function ()
	{
		var windowObj;
		if( opener != null )
		{
			windowObj = opener;
		}
		else if( parent != null )
		{
			windowObj = parent;
		}
		return windowObj;
	},

	/**
	 * 關閉目前的視窗, 本函式會同時考量獨立window及IFrameDialog.
	 */
	closeCurrentWindow: function()
	{
		var windowObj = BrowserUtils.findOpenerWindow();
		if( windowObj == null )
		{
			window.close();
			//alert( 'browser.opener.notExist'.i18n() );
		}
		else
		{
			try
			{
				var closed = false;
				//因為可能是獨立開啟的視窗, 所以若未正確關閉, 則直接關閉視窗.
				if( windowObj.closeIFrameDialog )
				{
					closed = windowObj.closeIFrameDialog();
				}
				if( !closed )
				{
					window.close();
				}
			}
			catch( e )
			{
				window.close();
				alert( e.message );
			}
		}
	}

}

var ProjEffectUtils =
{
	//定義初始化頁面的動作.
	initialPage: function()
	{
		if( Prototype.Browser.IE )
			$(document.body).addClassName('ieVersion');
		else if( Prototype.Browser.Gecko )
			$(document.body).addClassName('geckoVersion');
		else if( Prototype.Browser.Opera )
			$(document.body).addClassName('operaVersion');
		else if( Prototype.Browser.WebKit )
			$(document.body).addClassName('webKitVersion');
		else
			$(document.body).addClassName('geckoVersion');
		//載入個人喜好設定
		if( !preferenceConfig )
		{
			preferenceConfig = PreferenceConfig.getInstance();
		}
		if( preferenceConfig.roundBlocks )
		{
			ProjEffectUtils.roundBlocks();
		}
	},
	//定義頁面結束時的動作, 物件清除應在此處完成.
	destroyPage: function()
	{
		//在頁面被關閉時清除頁面上所有event物件, 以便回收memory.(主要是針對IE環境)
		DomUtils.destroyNode( document );
	},
	roundBlocks : function()
	{
		var blockIds = EdsProjConfig.roundBlockIds;
		$w( blockIds ).each(
			function( blockId )
			{
				if( $(blockId) )
				{
					var child = $(blockId).firstDescendant();
					//依child element來調整寬度.
					if( child )
					{
						//EdsLog.debug( $(blockId).id + ":width: " + Element.getWidth($(blockId)) + ":" + Element.getWidth($(blockId).firstChild ) );
						if( Element.getWidth($(blockId)) < Element.getWidth(child) )
							$(blockId).setStyle( { width: Element.getWidth(child) } );
					}
					Rico.Corner.round( $(blockId),{color: 'transparent'} );
					/*
					if( geckoBrowser )
					{
						$(blockId).addClassName("roundedBlock");
					}
					else
					{
						var child = $(blockId).firstDescendant();
						//依child element來調整寬度.
						if( child )
						{
							//EdsLog.debug( $(blockId).id + ":width: " + Element.getWidth($(blockId)) + ":" + Element.getWidth($(blockId).firstChild ) );
							if( Element.getWidth($(blockId)) < Element.getWidth(child) )
								$(blockId).setStyle( { width: Element.getWidth(child) } );
						}
						Rico.Corner.round( $(blockId),{color: 'transparent'} );
					}
					*/
				}
			}
		);
    },
	roundBlock : function( block )
	{
		if( $(block) )
		{
			var child = $(block).firstDescendant();
			//依child element來調整寬度.
			if( child )
			{
				//EdsLog.debug( $(blockId).id + ":width: " + Element.getWidth($(blockId)) + ":" + Element.getWidth($(blockId).firstChild ) );
				if( Element.getWidth($(block)) < Element.getWidth(child) )
					$(block).setStyle( { width: Element.getWidth(child) } );
			}
			Rico.Corner.round( $(block),{color: 'transparent'} );
		}
    }
}
//設定每個頁面載入時, 都要呼叫initialPage().
EdsEvent.addOnLoad( ProjEffectUtils.initialPage );
EdsEvent.addOnUnload( ProjEffectUtils.destroyPage );

var ValidationUtils =
{
	/**
	 * 檢查輸入欄位是否為數字的event handler.
     * 呼叫方式如:
     * $(form['property']).observe("change", ValidationUtils.checkNumber.bindAsEventListener(form['property'],{name: '屬性名稱'}) );
     * 注意, 使用時必須將要做submit的form當成其event objejct, 如上例所示.
	 */
	checkNumber : function( event, args )
	{
		var value = $F(this);
		var nan = Number(value).toString() == 'NaN';
		if( nan )
		{
			if( args.name )
				alert( 'validate.number'.i18n( args.name ) );
			else
				alert( 'validate.number'.i18n("") );
			this.value = '';
		}
	}
}

var DomUtils =
{
	/**
	 * 在IE上, dom物件若與javsacript object彼此有關聯(例如event)時, IE的garbage collector無法正確回收memory而造成leak,
	 * 可以呼叫本函式, 將要清除之dom物件傳入即可. 詳細說明, 請參考 http://javascript.crockford.com/memory/leak.html
	 */
	clearEvent: function( node )
	{
		var a = node.attributes, i, l, n;
		if (a) {
			l = a.length;
			for (i = 0; i < l; i += 1) {
				n = a[i].name;
				if (typeof node[n] === 'function') {
					node[n] = null;
				}
			}
		}
		a = node.childNodes;
		if (a) {
			l = a.length;
			for (i = 0; i < l; i += 1) {
				this.clearEvent(node.childNodes[i]);
			}
		}
	},
	destroyNode: function( node )
	{
		if( node )
		{
			if( node.parentNode )
				node.parentNode.removeChild( node );
			// ingore TEXT_NODE
			if( node.nodeType != 3 )
			{
				//先去除clearEvent, 避免在FF上速度過慢.
				//this.clearEvent( node );
				//在IE上, 即使由DOM中去除, 仍會造成Memory leak, 所以必須使用以下方式處理.
				if( ieBrowser )
					node.outerHTML = '';
			}
		}
	}
};

var StyleUtils =
{
	extractNumber: function( /*string*/str, defaultValue )
	{
		var num = new Array();
		if( str )
			str.scan(/^\d+/, function(match) { num.push( Number(match) ) }); //只取字串頭出現的數字.
		if( num.length > 0 )
			return num[0];
		if( defaultValue )
			return Number(defaultValue);
		return null;
	},
	/**
	 * 取得element的border size, 傳回之物件中包含以下變數, 儲存對應之尺寸, 若該尺寸不存在或為字串時, 則會傳回0.
	 * top, left, right, bottom, width(即left+rigth), height(即top+bottom).
	 * 使用時應注意, border設定之值可能會為字串(thin, medium, thick...) 本函式目前之會傳回0.
	 * TODO: 計算字串代表之數值.
	 */
	borderSize: function( /*html element*/element )
	{
		var top = this.extractNumber( $(element).getStyle("borderTopWidth"), 0 );
		var bottom = this.extractNumber( $(element).getStyle("borderBottomWidth"), 0 );
		var left = this.extractNumber( $(element).getStyle("borderLeftWidth"), 0 );
		var right = this.extractNumber( $(element).getStyle("borderRightWidth"), 0 );
		//EdsLog.debug( element.id + ":border:" + top + ":" + bottom + ":" + left + ":" + right + ":" + (left+right) + ":" + (top+bottom) );
		return {top: top, left: left, right: right, bottom: bottom, width: left+right, height: top+bottom};
	},
	/**
	 * 取得element的padding size, 傳回之物件中包含以下變數, 儲存對應之尺寸, 若該尺寸不存在時, 則會傳回0.
	 * top, left, right, bottom, width(即left+rigth), height(即top+bottom).
	 * TODO: 在IE中若設定為1em時, 會傳回1, 其值並非真正尺寸, 需做計算.
	 */
	paddingSize: function( /*html element*/element )
	{
		var top = this.extractNumber( $(element).getStyle("paddingTop"), 0 );
		var bottom = this.extractNumber( $(element).getStyle("paddingBottom"), 0 );
		var left = this.extractNumber( $(element).getStyle("paddingLeft"), 0 );
		var right = this.extractNumber( $(element).getStyle("paddingRight"), 0 );
		//EdsLog.debug( element.id + ":padding:" + top + ":" + bottom + ":" + left + ":" + right + ":" + (left+right) + ":" + (top+bottom) );
		return {top: top, left: left, right: right, bottom: bottom, width: left+right, height: top+bottom};
	},
	/**
	 * 取得element的margin size, 傳回之物件中包含以下變數, 儲存對應之尺寸, 若該尺寸不存在時, 則會傳回0.
	 * top, left, right, bottom, width(即left+rigth), height(即top+bottom).
	 * TODO: 在IE中若設定為1em時, 會傳回1, 其值並非真正尺寸, 需做計算.
	 */
	marginSize: function( /*html element*/element )
	{
		var top = this.extractNumber( $(element).getStyle("marginTop"), 0 );
		var bottom = this.extractNumber( $(element).getStyle("marginBottom"), 0 );
		var left = this.extractNumber( $(element).getStyle("marginLeft"), 0 );
		var right = this.extractNumber( $(element).getStyle("marginRight"), 0 );
		return {top: top, left: left, right: right, bottom: bottom, width: left+right, height: top+bottom};
	},
	/**
	 * 取得element的border及padding size, 傳回之物件中包含以下變數, 儲存對應之尺寸, 若該尺寸不存在時, 則會傳回0.
	 * top, left, right, bottom, width(即left+rigth), height(即top+bottom).
	 */
	borderPaddingSize: function( /*html element*/element )
	{
		var border = this.borderSize( element );
		var padding = this.paddingSize( element );
		var top = (border.top||0) + (padding.top||0);
		var bottom = (border.bottom||0) + (padding.bottom||0);
		var left = (border.left||0) + (padding.left||0);
		var right = (border.right||0) + (padding.right||0);
		//EdsLog.debug( element.id + ":bp:" + top + ":" + bottom + ":" + left + ":" + right + ":" + (left+right) + ":" + (top+bottom) );
		return {top: top, left: left, right: right, bottom: bottom, width: left+right, height: top+bottom};
	},
	/**
	 * 由於直接透過style設定寬或高時, 在IE上, 指定的寬高會包含border及padding的空間, 但在FF上, 指定的尺寸並不包含border及padding的空間,
	 * 所以在設定物件尺寸時, 依據外框大小, 在IE上無需扣除border及padding尺寸, 但在FF上, 則需扣除,
	 * 此函式主要便是在計算尺寸時, 依browser特性, 傳回該扣除之border, padding的尺寸.
	 */
	borderPaddingOffset: function( /*html element*/element )
	{
		if( ieBrowser )
			return {top: 0, left: 0, right: 0, bottom: 0, width: 0, height: 0};
		else
			return this.borderPaddingSize( element );
	},
	/**
	 * 在IE上, 若設定給style(尺寸,位置)的數字為負時, 會發生錯誤.
	 * 此函式會針對傳入之值做檢查, 若大於0, 則傳回原傳入之值, 若小於0, 則會顯示錯誤訊息並傳回0.
	 */
	checkStyleValue: function( size )
	{
		if( size < 0 )
		{
			//EdsLog.debug("Size不能小於0.");
			showAlert( 'validate.ge0'.i18n("Size") );
			return 0;
		}
		return size;
	}
};

//*************************************************************
//	DOM及CSS相關工具 end
//*************************************************************
//
//*************************************************************
//	Cookie相關工具 start
//*************************************************************
/**
 * 用來處理cookie的物件
 */
var CookieUtils =
{
	isCookieEnabled: navigator.cookieEnabled,
	/**
	 * 取得指定名稱之cookie, 若不存在, 會傳回null, 若存在, 則傳回對應之字串.
	 */
	getCookie: function( cookieName )
	{
		if( document.cookie.length>0 )
		{
			var cookie = document.cookie;
			c_start = cookie.indexOf( cookieName + "=" );
			if( c_start != -1 )
			{
				c_start = c_start + cookieName.length + 1;
				c_end = cookie.indexOf(";",c_start);
				if(c_end==-1)
					c_end = cookie.length
				return unescape(cookie.substring(c_start,c_end));
			}
		}
		return null;
	},
	/**
	 * 設定cookie.
	 * cookieName: cookie名稱.
	 * cookieValue: cookie之值.
	 * expiredays: 有效天數.
	 */
	setCookie: function( cookieName, cookieValue, expiredays )
	{
		var exdate = new Date();
		exdate.setDate( exdate.getDate() + expiredays );
		document.cookie = cookieName+ "=" + escape(cookieValue) + ((expiredays==null) ? "" : ";expires="+exdate.toGMTString() );
	}
}
//*************************************************************
//	Cookie相關工具 end
//*************************************************************

//*************************************************************
//	JavaScript 語法擴充 begin
//*************************************************************

/**
 * 建立帶有name屬性的element.
 * 因為在IE中, element建立後無法再指定name, 必須於建立element時便指定, 但該方式只在IE上適用,
 * 所以在此加入一method至document物件中, 使其可直接建立有name的element物件.
 */
document.createElementWithName = function( type, name )
{
	return Try.these
	(
		function()
		{
			//for IE.
			return document.createElement('<'+type+' name="'+name+'" />');
		},
		function()
		{
			var element = document.createElement(type);
			element.setAttribute( 'name', name );
			return element;
		}
	);
}

/**
 * 在Date物件中加入新的function.
 */
Object.extend(Date.prototype,
{
	SimpleFormat: function(format, data)
	{
		data = data || {};
		var bits = new Array();
		bits['d'] = this.getDate();
		bits['dd'] = String(this.getDate()).zerofill(2);

		bits['M'] = this.getMonth()+1;
		bits['MM'] = String(this.getMonth()+1).zerofill(2);
		if(data.AbbreviatedMonthNames)
			bits['MMM'] = data.AbbreviatedMonthNames[this.getMonth()];
		if(data.MonthNames)
			bits['MMMM'] = data.MonthNames[this.getMonth()];
		var yearStr = "" + this.getFullYear();
		yearStr = (yearStr.length == 2) ? '19' + yearStr: yearStr;
		bits['yyyy'] = yearStr;
		bits['yy'] = bits['yyyy'].toString().substr(2,2);


		// do some funky regexs to replace the format string
		// with the real values
		var frm = new String(format);
		var removeDash = false;
		//因為後面regular expression是用"整個字"來比對, 所以必須先在字中間有分隔字元, 使其能正確替換, 最後再將分隔字元去除.
		if( format == 'yyyyMMdd')
		{
			frm = new String('yyyy-MM-dd');
			removeDash = true;
		}
		for (var sect in bits)
		{
			var reg = new RegExp("\\b"+sect+"\\b" ,"g");
			frm = frm.replace(reg, bits[sect]);
		}
		if( removeDash )
			frm = frm.gsub("-","");
		return frm;
	},
	format: function( pattern )
	{
		return this.SimpleFormat( pattern );
	},
	toISODate : function()
	{
		var y = this.getFullYear();
		var m = String(this.getMonth() + 1).zerofill(2);
		var d = String(this.getDate()).zerofill(2);
		return String(y) + String(m) + String(d);
	}
});

Object.extend(Date,
{
	SimpleParse: function(value, format)
	{
		val=String(value);
		format=String(format);

		if(val.length <= 0) return null;

		if(format.length <= 0) return new Date(value);

		var isInteger = function (val)
		{
			var digits="1234567890";
			for (var i=0; i < val.length; i++)
			{
				if (digits.indexOf(val.charAt(i))==-1) { return false; }
			}
			return true;
		};

		var getInt = function(str,i,minlength,maxlength)
		{
			for (var x=maxlength; x>=minlength; x--)
			{
				var token=str.substring(i,i+x);
				if (token.length < minlength) { return null; }
				if (isInteger(token)) { return token; }
			}
			return null;
		};

		var i_val=0;
		var i_format=0;
		var c="";
		var token="";
		var token2="";
		var x,y;
		var now=new Date();
		var year=now.getFullYear();
		var month=now.getMonth()+1;
		var date=1;

		while (i_format < format.length)
		{
			// Get next token from format string
			c=format.charAt(i_format);
			token="";
			while ((format.charAt(i_format)==c) && (i_format < format.length))
			{
				token += format.charAt(i_format++);
			}
			// Extract contents of value based on format token
			if (token=="yyyy" || token=="yy" || token=="y")
			{
				if (token=="yyyy") { x=4;y=4; }
				if (token=="yy")   { x=2;y=2; }
				if (token=="y")    { x=2;y=4; }
				year=getInt(val,i_val,x,y);
				if (year==null) { return null; }
				i_val += year.length;
				if (year.length==2)
				{
					if (year > 70) { year=1900+(year-0); }
					else { year=2000+(year-0); }
				}
			}

			else if (token=="MM"||token=="M")
			{
				month=getInt(val,i_val,token.length,2);
				if(month==null||(month<1)||(month>12)){return null;}
				i_val+=month.length;
			}
			else if (token=="dd"||token=="d")
			{
				date=getInt(val,i_val,token.length,2);
				if(date==null||(date<1)||(date>31)){return null;}
				i_val+=date.length;
			}
			else
			{
				if (val.substring(i_val,i_val+token.length)!=token) {return null;}
				else {i_val+=token.length;}
			}
		}

		// If there are any trailing characters left in the value, it doesn't match
		if (i_val != val.length) { return null; }

		// Is date valid for month?
		if (month==2)
		{
			// Check for leap year
			if ( ( (year%4==0)&&(year%100 != 0) ) || (year%400==0) ) { // leap year
				if (date > 29){ return null; }
			}
			else { if (date > 28) { return null; } }
		}

		if ((month==4)||(month==6)||(month==9)||(month==11))
		{
			if (date > 30) { return null; }
		}

		var newdate=new Date(year,month-1,date, 0, 0, 0);
		return newdate;
	}
});

/**
 * 在Array中加入addAll的method
 */
Object.extend(Array.prototype,
	{
		addAll: function( obj )
		{
			if( obj != undefined)
			{
				if( Object.isArray( obj ) )
				{
					for( var i=0,size=obj.length; i<size; i++ )
						this.push( obj[i] );
				}
				else
					this.push( obj );
			}
		}
	}
);
//*************************************************************
//	JavaScript 語法擴充 end
//*************************************************************

//*************************************************************
//	分頁/排序功能 start
//*************************************************************
/**
 * DisplayPagingInfo是用來搭配display tag, 由列表畫面將分頁相關的參數由本物件帶入到javascript的function中, 以便執行分頁動作.
 * 使用時有以下參數要設定:
 * form: 要執行查詢的form物件.
 * enableAjaxSubmit: 是否使用ajax form submit的功能, 若原查詢動作是以ajax方式進行, 則此參數必須設為true, 若是使用一般submit的查詢動作, 則應設為false,
 *					 此參數若為true時, 會呼叫ajaxFormSubmit()來執行查詢動作.
 * pageParameterName: 設定Display Tag用來傳遞分頁資料時所用之參數名稱. 此值應直接由Java程式中的DisplayTagPagingInfo中取得.
 * sortParameterName: 設定Display Tag用來傳遞排序對象資料時所用之參數名稱. 此值應直接由Java程式中的DisplayTagPagingInfo中取得.
 * orderParameterName: 設定Display Tag用來傳遞排序方向資料時所用之參數名稱. 此值應直接由Java程式中的DisplayTagPagingInfo中取得.
 * totalCount: 因為是使用partial查詢, 只查詢當頁要顯示的所有資料, 所以必須設定此屬性, 傳入原查詢條件下查得的總筆數.
 * pagingInfoProperty: 因為相關分頁參數必須送至server上的pagingInfo物件, 所以必須使用此屬性指定pagingInfo的屬性名稱.
 * removeParameterAfterSubmit: 在送出查詢後, 是否將分頁相關之參數移除, 預設為true, 若需要一直保留, 可設為false.
 */
var DisplayPagingInfo = Class.create();
DisplayPagingInfo.prototype =
{
	initialize: function()
	{
		this.form = null;
		this.enableAjaxSubmit = false;
		this.totalCount = 0;
		this.pagingInfoProperty = "";
		//display tag中使用external list並實作PaginatedList時, 會使用以下預設的變數名稱傳遞參數,
		//若不是使用PaginatedList時, 則需設定為DisplayTag的編碼後之名稱(此部份必須搭配Java程式來取得).
		this.pageParameterName = "page";
		this.sortParameterName = "sort";
		this.orderParameterName = "dir";
		this.removeParameterAfterSubmit = true;
	},
	constructParameter: function( form, name, valueMap )
	{
		var field = null;
		if( name != undefined && name != "" )
		{
			if( valueMap[name] )
			{
				var fields = $(form).select("input[name='" + name + "']");
				if( fields.length > 0 )
				{
					field = fields[0];
					FormUtils.setFieldValue( field, valueMap[name]);
				}
				else
				{
					field = FormUtils.createHiddenField( name, valueMap[name] );
					form.appendChild( field );
				}
			}
		}
		return field;
	}
}

/**
 * 將傳入之URL中包含的param製作成Hash後傳回.
 * 本實作是修改自prototype的toQueryParams, 修改內容如function說明.
 */
function construtParams( url )
{
	var match = url.strip().match(/([^?#]*)(#.*)?$/);
	if (!match) return {};

	return match[1].split('&').inject({},
		function(hash, pair)
		{
			if ((pair = pair.split('='))[0])
			{
				var key = decodeURIComponent(pair.shift());
				var value = pair.length > 1 ? pair.join('=') : pair[0];
				if (value != undefined)
				{
					//在prototype的實作中, 會使用decodeURIComponent來檢查format, 但若遇到編碼過的value會發生錯誤, 此處即為避開此錯誤.
					try
					{
						value = decodeURIComponent(value);
					}
					catch( e )
					{
						//EdsLog.debug("轉換param時發生錯誤 : " + e.message + ". 原value:'" + value + "'." );
					}
				}

				if (key in hash)
				{
					if (hash[key].constructor != Array) hash[key] = [hash[key]];
					hash[key].push(value);
				}
				else
					hash[key] = value;
			}
			return hash;
		}
	);
}

/**
 * 執行分頁查詢的動作. 此函式主要是搭配display tag使用, 傳入之anchor為display tag產生query string, 其中帶有分頁所需的資訊.
 */
function pagingSubmit( anchor )
{
	var exists = false;
	try
	{
		if( createDisplayPagingInfo )
			exists = true;
	}
	catch( e )
	{
		showAlert( 'pagingInfo.absent'.i18n() );
	}
	if( exists )
	{
		var totalCountField;
		var pageField;
		var sortField;
		var orderField;
		var sortUsingNameField;
		try
		{
			var displayPagingInfo = createDisplayPagingInfo();
			//在IE上, 若前一頁送出之資料太多時, 在下一頁分頁連結上的長度太長時, IE會自動截斷, 且anchor也會不太正確, 會造成錯誤, 所以此處必須使用readAttribute來讀取, 避開此問題.
			var url = $(anchor).readAttribute("href");
			var params = construtParams( url );
			var form = displayPagingInfo.form;
			var fields = $(form).select("input[name='" + displayPagingInfo.pagingInfoProperty+".totalCount" + "']");
			if( fields.length > 0 )
			{
				totalCountField = fields[0];
				FormUtils.setFieldValue( totalCountField, displayPagingInfo.totalCount);
			}
			else
			{
				totalCountField = FormUtils.createHiddenField( displayPagingInfo.pagingInfoProperty+".totalCount", displayPagingInfo.totalCount );
				form.appendChild( totalCountField );
			}
			pageField = displayPagingInfo.constructParameter( form, displayPagingInfo.pageParameterName, params );
			sortField = displayPagingInfo.constructParameter( form, displayPagingInfo.sortParameterName, params );
			orderField = displayPagingInfo.constructParameter( form, displayPagingInfo.orderParameterName, params );
			sortUsingNameField = displayPagingInfo.constructParameter( form, displayPagingInfo.sortUsingNameParameterName, params );
			//若有開啟ajax submit功能時, 就呼叫ajaxFormSubmit來執行查詢動作.
			if( displayPagingInfo.enableAjaxSubmit )
				ajaxFormSubmit( {form:form} );
			else
				form.submit();
		}
		catch( e )
		{
			showAlert( 'exception'.i18n() + e.message );
		}

		//submit後將參數移除, 避免影響下次之查詢動作.
		if( displayPagingInfo.removeParameterAfterSubmit)
		{
			if( totalCountField )
				form.removeChild( totalCountField );
			if( pageField )
				form.removeChild( pageField );
			if( sortField )
				form.removeChild( sortField );
			if( orderField )
				form.removeChild( orderField );
			if( sortUsingNameField )
				form.removeChild( sortUsingNameField );
		}
	}
}

/**
 * 執行排序的動作. 此函式主要是搭配display tag使用, 傳入之event應為span物件.
 */
function sortingSubmit( event )
{
	Event.stop( event );
	var obj = Event.element( event );
	if( "A" == obj.parentNode.tagName.toUpperCase() )
		pagingSubmit( obj.parentNode );
	else
		showAlert( 'paging.error.sorting'.i18n() );
}

/**
 * 當使用pagingSubmit時, 若將分項資訊留存form中時, 後續可呼叫此method將欄位清除
 * TODO: 目前尚有問題, 欄位移除後雖然看不見, 但卻還找得到, 因此後續變成沒有create新的field, 無法設定參數, 因此無法進入指定之頁面.
 */
function removePagingInfoHandler( event )
{
	var exists = false;
	try
	{
		if( createDisplayPagingInfo )
			exists = true;
	}
	catch( e )
	{
	}
	if( exists )
	{
		var displayPagingInfo = createDisplayPagingInfo();
		var form = $(displayPagingInfo.form);
		var totalCountField = form[displayPagingInfo.pagingInfoProperty+".totalCount"];
		var pageField = form[displayPagingInfo.pageParameterName];
		var sortField = form[displayPagingInfo.sortParameterName];
		var orderField = form[displayPagingInfo.orderParameterName];
		var sortUsingNameField = form[displayPagingInfo.sortUsingNameParameterName];
		if( totalCountField )
			form.removeChild( totalCountField );
		if( pageField )
			form.removeChild( pageField );
		if( sortField )
			form.removeChild( sortField );
		if( orderField )
			form.removeChild( orderField );
		if( sortUsingNameField )
			form.removeChild( sortUsingNameField );
	}
}

//*************************************************************
//	分頁功能 end
//*************************************************************


//*************************************************************
//	新增/移除 table row start
//*************************************************************

function updateDetailRow( tableId, options )
{
	if( options.actionType == 'insert' )
	{
		addDetailRow( tableId, options );
	}
	else if( options.actionType == 'update' )
	{
		modifyDetailRow( tableId, options );
	}
	else if( options.actionType == 'delete' )
	{
		removeDetailRow( tableId, options.rowIndex );
	}
	else
	{
		showAlert( 'table.error.updateDetailRow'.i18n() );
	}
	closeIFrameDialog();
}

function addDetailRow( tableId, options )
{
	var table = $(tableId);
	var rowLength = table.rows.length;
	var lastRow = table.rows[rowLength-1];
	var newRow = table.insertRow( rowLength );
	newRow.className = ((newRow.rowIndex-1)%2==1)? "oddRow" : "evenRow";

	//由最後一row的cell數目決定新row的cell數.
	for( var i=0; i<lastRow.cells.length; i++ )
	{
		var newCell = newRow.insertCell(i);
		if( i == 0 )
		{
			newCell.className = 'seqNo';
			newCell.innerHTML = newRow.rowIndex;
		}
		else if( i == 1 )
		{
			newCell.className = 'alignCenter';
			var innerHTML = "";
			var height = "400";
			if( options.modifyOption )
			{
				if( !options.modifyOption.url )
				{
					showAlert( 'validate.param.required'.i18n('url') );
					return;
				}
				if( options.modifyOption.height )
				{
					height = options.modifyOption.height;
				}
				innerHTML += '<a href="#" onclick="showIFrameDialog(\''+options.modifyOption.url+'\',{height: \''+height+'\'})" title="'+ 'label.modify'.i18n() +'"><img src="../images/pen.gif" border="0"></a>';
			}
			if( options.deleteOption )
			{
				if( !options.deleteOption.url )
				{
					showAlert( 'validate.param.required'.i18n('url') );
					return;
				}
				if( options.deleteOption.height )
				{
					height = options.deleteOption.height;
				}
				innerHTML += '<a href="#" onclick="showIFrameDialog(\''+options.deleteOption.url+'\',{height: \''+height+'\'});" title="'+ 'table.row.delete'.i18n() +'"><img src="../images/can.gif" border="0"></a>'
			}
			newCell.innerHTML = innerHTML;
		}
		else
		{
			var field = options.fields[i-2];
			newCell.className = field.cellClass;
			if( field.innerHTML )
			{
				newCell.innerHTML = field.innerHTML;
			}
			else
			{
				newCell.innerHTML = field.value;
			}
		}
	}
}

function modifyDetailRow( tableId, options )
{
	var table = $(tableId);
	if( !options.rowIndex )
	{
		showAlert( 'need.rowIndex'.i18n() );
		return;
	}
	var row = table.rows[options.rowIndex];

	//由最後一row的cell數目決定新row的cell數.
	for( var i=0; i<row.cells.length; i++ )
	{
		var cell = row.cells[i];
		if( i == 0 )
		{
			continue;
		}
		else if( i == 1 )
		{
			var innerHTML = "";
			var height = "400";
			if( options.modifyOption )
			{
				if( !options.modifyOption.url )
				{
					showAlert( 'validate.param.required'.i18n('url') );
					return;
				}
				if( options.modifyOption.height )
				{
					height = options.modifyOption.height;
				}
				innerHTML += '<a href="#" onclick="showIFrameDialog(\''+options.modifyOption.url+'\',{height: \''+height+'\'})" title="'+ 'label.modify'.i18n() +'"><img src="../images/pen.gif" border="0"></a>';
			}
			if( options.deleteOption )
			{
				if( !options.deleteOption.url )
				{
					showAlert( 'validate.param.required'.i18n('url') );
					return;
				}
				if( options.deleteOption.height )
				{
					height = options.deleteOption.height;
				}
				innerHTML += '<a href="#" onclick="showIFrameDialog(\''+options.deleteOption.url+'\',{height: \''+height+'\'});" title="'+ 'table.row.delete'.i18n() +'"><img src="../images/can.gif" border="0"></a>'
			}
			cell.innerHTML = innerHTML;
		}
		else
		{
			var field = options.fields[i-2];
			cell.className = field.cellClass;
			if( field.innerHTML )
			{
				cell.innerHTML = field.innerHTML;
			}
			else
			{
				cell.innerHTML = field.value;
			}
		}
	}
}

function removeDetailRow( tableId, rowIndex )
{
	if( !rowIndex )
	{
		showAlert( 'table.need.rowIndex'.i18n() );
		return;
	}
	var table = $(tableId);
	table.deleteRow( rowIndex );
	//從delete之後的row都要重設其序號及style.
	var rowLength = table.rows.length;
	if( rowLength >= rowIndex )
	{
		for( var i=rowIndex; i<rowLength ; i++ )
		{
			var row = table.rows[i];
			row.className = ((row.rowIndex-1)%2)==1 ? "oddRow" : "evenRow";
			row.cells[0].innerHTML = row.rowIndex;
			row.cells[1].innerHTML = '<a href="#" onclick="if( confirm(\''+'table.row.delete.confirm'.i18n()+'\') ) removeDetailRow(\'' + tableId +'\',' + row.rowIndex +');" title="'+ 'table.row.delete'.i18n() +'"><img src="../images/can.gif" border="0"></a>'
		}
	}
}

function addInputRow( tableId, options )
{
	var table = $(tableId);
	var rowLength = table.rows.length;
	var lastRow = table.rows[rowLength-1];
	var newRow = table.insertRow( rowLength );
	newRow.className = ((newRow.rowIndex-1)%2==1)? "oddRow" : "evenRow";

	//由最後一row的cell數目決定新row的cell數.
	for( var i=0; i<lastRow.cells.length; i++ )
	{
		var newCell = newRow.insertCell(i);
		if( i == 0 )
		{
			newCell.className = 'seqNo';
			newCell.innerHTML = newRow.rowIndex;
		}
		else if( i == 1 )
		{
			if( options.needDelete )
			{
				newCell.className = 'alignCenter';
				newCell.innerHTML = '<a href="#" onclick="if( confirm(\''+'table.row.delete.confirm'.i18n()+'\') ) removeDetailRow(\'' + tableId +'\',' + newRow.rowIndex +');" title="'+ 'table.row.delete'.i18n() +'"><img src="../images/can.gif" border="0"></a>'
			}
		}
		else
		{
			var field = options.fields[i-2];
			newCell.className = field.cellClass;
			if( !field.useDatePicker )
			{
				newCell.innerHTML = field.innerHTML;
			}
			else
			{
				var input = document.createElement("input");
				input.type = "text";
				input.name = field.fieldName;
				input.maxlength = 8;
				input.size = 10;
				if( field.defaultValue )
					input.value = field.defaultValue;
				input.title = 'table.date.input.title'.i18n();
				newCell.appendChild( input );
				var image = document.createElement("img");
				image.src = "../images/dateIcon.gif";
				image.id = "_datePicker_" + new Date().getTime();
				image.border = 0;
				image.width = 19;
				image.height = 19;
				image.title = 'table.date.image.hint'.i18n();
				image.alt = 'table.date.image.hint'.i18n();
				image.className = "datePickerImage";
				newCell.appendChild( image );
				new DatePicker( {imageId: image.id, dateField: input} );
			}
		}
	}
}

/**
 * 使用複製的方式, 將table中最後一筆資料複製成新的一筆輸入項目.
 * 本函式可支援一筆資料跨多個row的情況.
 * 此外, 當一筆資料跨多個row時, 其中序號及移除等欄位(若有的話), 都必須在該筆資料的第一個row中出現.
 * tableId: 要加入新row的table id或table物件.
 * options: 額外之參數, 可設定之值如下:
 *  rowCount(int): 要新增的筆數, 預設值為1.
 *	rowSpan(int): 若一筆輸入資料會跨多個row時, 可設定此值, 預設為1
 *	headerRows(int): 指定位於table開頭(通常為表頭)與輸入項目無關的row數量, 預設與rowSpan相同.
 *	footerRows(int): 指定table尾端(通常為表尾)與輸入項目無關的row數量, 預設為0.
 *	seqNoColumn(int): 若table中有序號欄位, 則要使用此值來設定其所在的column, 由0開始, 若傳入值為-1, 代表沒有序號欄位, 預設值為-1.
 *	removable(boolean): 若table中需要有remove的連結欄位時, 可設定此值為true, 預設值為false.
 *	removableColumn(int): 當removable為true時, 使用此值來設定其所在的column, 由0開始, 預設值為1.
 *	form(form object): 若需要指定indexFields參數時, 也必須指定讓欄位所在之form物件.
 *	indexFields(string array): 若要複製之欄位中, 含有以index為其value的欄位時(由0開始), 可使用此參數來設定欄位名稱(陣列型式).
 */
function copyNewDataRow( tableId, options )
{
	var table = $(tableId);
	var _options = {rowCount: 1, rowSpan: 1, headerRows: 1, footerRows:0, seqNoColumn: -1, removable: false, removableColumn: 1, form: null, indexFields:[]};
	Object.extend( _options, options || {} );
	//若傳入之參數未指定headerRows時, 則以rowSpan為其預設值.
	if( options.headerRows == undefined && _options.rowSpan != _options.headerRows )
		_options.headerRows = _options.rowSpan;
	var rowArray = new Array(_options.rowSpan);
	var rowLength = table.rows.length - _options.footerRows;
	if( rowLength - _options.headerRows <= 0 )
	{
		showAlert( 'table.error.noRow'.i18n() );
		return;
    }
	var count = 0;
	while( _options.rowSpan>count )
	{
		//由最後的row取得要複製之row.
		rowArray[count] = table.rows[rowLength-_options.rowSpan+count];
		if( $(rowArray[count]).select('input[type="radio"]').length > 0 )
		{
			showAlert( 'table.error.notSupport'.i18n('Radio') );
			return;
        }
		count++;
	}
	var last = $(table.rows[rowLength-1]);
	for( var i=0; i<_options.rowCount; i++ )
	{
		count = 0;
		if( ieBrowser )
		{
			var html = "";
			while( _options.rowSpan > count )
			{
				html += rowArray[count].outerHTML;
				count++;
			}
			last.insert( {after: html} );
			//new Insertion.After( rowArray[_options.rowSpan-1], html );
		}
		else //其他browser使用以下方式.
		{
			while( _options.rowSpan > count )
			{
				var newRow = table.insertRow(rowLength+count);
				newRow.innerHTML = rowArray[count].innerHTML;
				FormUtils.copyAllFieldValue( $(rowArray[count]).select('input','select','textField'), $(newRow).select('input','select','textField') );
				count++;
			}
		}
		last = $(table.rows[rowLength+_options.rowSpan-1]);
	}
	//在IE上, 無法直接利用newRow的innerHTML來設定內容, 所以必須先將要寫入之資料做成字串, 最後再插入到最後一個tr後面.
	/*
	count = 0;
	if( ieBrowser )
	{
		var html = "";
		while( _options.rowSpan > count )
		{
			html += rowArray[count].outerHTML;
			count++;
		}
		new Insertion.After( rowArray[_options.rowSpan-1], html );
    }
	else //其他browser使用以下方式.
	{
		while( _options.rowSpan > count )
		{
			var newRow = table.insertRow(rowLength+count);
			newRow.innerHTML = rowArray[count].innerHTML;
			FormUtils.copyAllFieldValue( $(rowArray[count]).getElementsBySelector('input','select','textField'), $(newRow).getElementsBySelector('input','select','textField') );
			count++;
		}
    }
	*/
	//移除連結統一在resetRowStyleAndSeqNo裡處理.
	resetDataTable( table, _options );
}

/**
 * 移除指定之資料列(若一筆資料列跨table中多個row時, 也都會一併刪除).
 * tableId: 要加入新row的table id或table物件.
 * dataRow: 資料列(注意, 並非table的row).
 * options: 額外之參數, 請參考function copyNewDataRow()的說明.
 */
function removeDataRow( tableId, dataRow, options )
{
	if( dataRow == null || dataRow == undefined )
	{
		showAlert( 'table.need.rowIndex'.i18n() );
		return;
	}
	var _options = {rowSpan: 1, headerRows: 1, footerRows:0, seqNoColumn: -1, removable: false, removableColumn: 1, form: null, indexFields:[]};
	Object.extend( _options, options || {} );
	var table = $(tableId);
	var startRow = dataRow*_options.rowSpan + _options.headerRows;
	//若剩下最後一筆資料列, 不能將其刪除, 要將其內的資料全部清空就好
	if( table.rows.length == _options.headerRows + _options.footerRows + _options.rowSpan )
	{
		for( var i=0; i<_options.rowSpan; i++ )
		{
			var fields = table.rows[startRow+i].getElementsBySelector('input','select','textarea');
			$A(fields).each( clearFieldValue );
		}
    }
	else
	{
		for( i=0; i<_options.rowSpan; i++ )
		{
			table.deleteRow( startRow );
		}
		//移除連結統一在resetRowStyleAndSeqNo裡處理.
		resetDataTable( table, _options );
    }

}

/**
 * 依目前table內容重新設定其各種狀態(如rowStyle, 序號... )
 * tableId: 要加入新row的table id或table物件.
 * options: 額外之參數, 請參考function copyNewDataRow()的說明.
 */
function resetDataTable( table, options )
{
	var len = table.rows.length - options.footerRows - options.headerRows;
	var count = 0;
	var optionsString = "{rowSpan:" + options.rowSpan + ", headerRows: " + options.headerRows +
		", footerRows: " + options.footerRows + ", seqNoColumn: " +	options.seqNoColumn +
		", removable: " + options.removable + ", removableColumn: " + options.removableColumn +"}";

	for( var i=0; i<len; i++ )
	{
		var row = table.rows[i+options.headerRows];
		row.className = count%2 == 0 ? "evenRow" : "oddRow";
		var tmp = i%options.rowSpan;
		//每筆資料列的第一個row
		if( tmp == 0 )
		{
			if( options.seqNoColumn != -1 )
				row.cells[options.seqNoColumn].innerHTML = count+1;
			if( options.removable == true )
				row.cells[options.removableColumn].innerHTML = '<a href="#" onclick="if( confirm(\''+'table.row.delete.confirm'.i18n()+'\') ) removeDataRow(\'' + table.id +'\',' + count +',' + optionsString + ');" title="'+ 'table.row.delete'.i18n() +'"><img src="../images/can.gif" border="0"></a>';
        }
		if( tmp == options.rowSpan-1 )
		{
			count++
		}
	}
	//若有指定index fields, 則要重設其值.
	if( options.form && options.indexFields.length > 0 )
	{
		if( count == 1 )
		{
			for( var j=0; j<options.indexFields.length; j++ )
			{
				if( !options.form[options.indexFields[j]] )
				{
					showAlert( 'form.field.notExist'.i18n( options.indexFields[j] ) );
					return;
                }
				options.form[options.indexFields[j]].value = "0";
            }
        }
		else
		{
			for( i=0; i<count; i++ )
			{
				for( j=0; j<options.indexFields.length; j++ )
				{
					if( !options.form[options.indexFields[j]] )
					{
						showAlert( 'form.field.notExist'.i18n( options.indexFields[j] ) );
						return;
					}
					options.form[options.indexFields[j]][i].value = ""+i;
				}
            }
		}
    }
}
//*************************************************************
//	新增/移除 table row end
//*************************************************************

//*************************************************************
//	form input relative effect start
//*************************************************************
var FormUtils =
{
	/**
	 * 將傳入之form物件的action及target換成新的action及target後submit, 並於submit後將form的action及target再回覆成原值.
	 * 此外, 由於submit動作是由javascript處理, 所以在執行後會將原event中止, 避免該form又做一次submit.
	 * form: 要submit的form.
	 * args中可傳遞以下參數:
	 *		newAction (required): url, 指向新的action.
	 *		newTarget (optional): 指定新的target, 設定畫面要開在哪個視窗.
     *		checkForm (optional): 用來檢查form內容是否正確的function.
	 * event: 引發動作的event, 若有傳入, 於執行完畢後會中止該event.
	 */
	changeActionAndSubmit: function ( form, args, event )
	{
		//先中止event.
		if( event )
			Event.stop( event );
		if( args.checkForm && Object.isFunction( args.checkForm ) )
		{
			if( args.checkForm() == false )
				return;
        }
		var origAction = form.action;
		form.action = args.newAction;
		var	origTarget = form.target;
		if( args.newTarget )
		{
			form.target = args.newTarget;
		}
		form.submit();
		form.action = origAction;
		form.target = origTarget;
	},
	/**
	 * event handler, 專門用來當作要呼叫changeActionAndSubmit()時的event handler.
     * 呼叫方式如:
     * $('excelButton').observe("click", FormUtils.changeActionAndSubmitHandler.bindAsEventListener(form,{newAction: $('shareStatusBatchExcelReport')}) );
     * 注意, 使用時必須將要做submit的form當成其event objejct, 如上例所示.
	 */
	changeActionAndSubmitHandler: function ( event, args )
	{
		//先中止event.
		Event.stop( event );
		FormUtils.changeActionAndSubmit( this, args );
	},
	/**
     * 統一處理列表型式的刪除檢查, 若檢查通過, 就會將form submit, 並將結果使用IframeDialog顯示出來.
     */
	submitDeleteAction: function ( form, checkboxes, event )
	{
		var checked = false;
		if( checkboxes.length )
		{
			$A( checkboxes ).each(
				function( checkbox )
				{
					if( checkbox.checked )
					{
						checked = true;
					}
				}
			);
		}
		else
		{
			checked = checkboxes.checked;
		}
		if( !checked )
		{
			showAlert( 'form.check.none'.i18n() );
		}
		else
		{
			if( confirm( 'form.check.delete.confirm'.i18n() ) )
				submitAndShowIFrameDialog( form,{height: '300'}, event );
		}
	},
	/**
	 * 判斷傳入之form的field是陣列或是單一物件.
	 * 單純只用field.length無法正確判斷(因為select物件有length屬性).
	 */
	isArrayField: function ( field )
	{
		//若有type屬性, 就是單一物件.
		if( field.type )
			return false;
		else
			return true;
	},

	/**
	 * 在列表畫面上, 用來實作全選動作的function.
	 * checked (boolean): 傳入全選控制欄位之值(true or false),
	 * checkboxes: 傳入所有要控制之checkbox欄位(HTMLElement)
	 * ignoreDisabled (boolean): 若checkbox為disabled時, 是否要排除不設定, 預設為true.
	 */
	checkAll: function ( checked, checkboxes, ignoreDisabled )
	{
		var _ignore = true;
		if( ignoreDisabled != undefined )
			_ignore = eval( ignoreDisabled );
		if( !checkboxes )
			return;
		if( checkboxes.length )
		{
			$A(checkboxes).each(
				function( checkbox )
				{
					if( !_ignore || !checkbox.disabled )
						checkbox.checked = checked;
				}
			);
		}
		else
		{
			if( !_ignore || !checkboxes.disabled )
				checkboxes.checked = checked;
		}
	},

	/**
	 * 在列表畫面上, 檢查全部可勾選之欄位狀態, 以同步全選的選項狀態.
	 * checkboxes: 傳入所有要控制之checkbox欄位(HTMLElement)
	 * controlCheckbox (checkbox element): 全選欄位
	 * ignoreDisabled (boolean): 若checkbox為disabled時, 是否要排除其狀態.
	 */
	recheckAll: function ( checkboxes, controlCheckbox, ignoreDisabled )
	{
		if( !checkboxes )
			 return;
		var _ignore = false;
		if( ignoreDisabled != undefined )
			_ignore = eval( ignoreDisabled );
		var checkedFound = false;
		var uncheckedFound = false;
		if( checkboxes.length )
		{
			$A(checkboxes).each(
				function( checkbox )
				{
					if( !_ignore || !checkbox.disabled )
					{
						if( checkbox.checked )
							checkedFound = true;
						else
							uncheckedFound = true;
					}
				}
			);
		}
		else
		{
			if( !_ignore || !checkboxes.disabled )
			{
				if( checkboxes.checked )
					checkedFound = true;
				else
					uncheckedFound = true;
			}
		}
		if( checkedFound && !uncheckedFound )
			controlCheckbox.checked = true;
		else if( uncheckedFound )
			controlCheckbox.checked = false;
	},

	/**
	 * 當使用者輸入之資料需自動複製至其他欄位時(通常是陣列型式的參數), 可使用本函式. args的參數內容說明如下:
	 * allowCopy : true or false, 代表是否開啟複製功能, 若為true, 才會執行複製, 若為false, 則不複製任何資料. 若有複製的控制項, 可直接傳入其值.
	 * srcField: 要被複製之欄位, 應傳入發生onchange event的控制項(與value擇一使用, 以srcField為主).
	 * value (deprecated): 要被複製之值, 應傳入發生onchange event的控制項之值(與srcField擇一使用).
	 * fields : 要複製傳入之值的所有輸入項.
	 * fieldCtrls : 若每個輸入頁(fields)都有各自獨立的複製控制項(必須為checkbox)時, 則由此參數設定, 若未設定, 則視為要複製.
	 * ignoreHidden : hidden欄位是否要忽略不複製. 預設值為true.
	 */
	copyFields: function ( args )
	{
		if( args.allowCopy )
		{
			var ignoreHidden = true;
			if( args.ignoreHidden != undefined )
				ignoreHidden = args.ignoreHidden;
			if( args.fields )
			{
				var array = FormUtils.isArrayField( args.fields );
				if( array )
				{
					//若有指定用來控制各欄位是否複製的控制項, 則會檢查其陣列長度是否相符.
					if( args.fieldCtrls && args.fieldCtrls.length != args.fields.length )
					{
						showAlert( 'form.copy.fields.notMatch'.i18n() );
					}
					$A(args.fields).each(
						function( field , index)
						{
							if( !ignoreHidden || field.type != 'hidden' )
							{
								if( !args.fieldCtrls || args.fieldCtrls[index].checked )
								{
									if( args.srcField )
										FormUtils.copyFieldValue( args.srcField, field );
									//向前相容, 只能處理部份type
									else
									{
										if( field.type == 'checkbox' )
											field.checked = args.value;
										else
											field.value = args.value;
									}
								}
							}
						}
					);
				}
				else
				{
					if( !ignoreHidden || args.fields.type != 'hidden' )
					{
						if( !args.fieldCtrls || args.fieldCtrls.checked )
						{
							if( args.srcField )
								FormUtils.copyFieldValue( args.srcField, args.fields );
							//向前相容, 只能處理部份type
							else
							{
								if( args.fields.type == 'checkbox' )
									args.fields.checked = args.value;
								else
									args.fields.value = args.value;
							}
						}
					}
				}
			}
		}
	},

	/**
	 * 取得form中指定之index及指定之名稱的所有欄位物件. 傳回之物件為一Hash, 以其名稱存放對應的Input Element.
	 * 本函式會自動判斷form中的屬性是陣列或單一元件.
	 */
	/*
	findInputElements: function ( form, inputNames, index )
	{
		if( inputNames.length == undefined )
		{
			showAlert( 'validate.array'.i18n("inputNames") );
			return;
		}
		//以第一個欄位判斷是陣列或單一物件.
		var array = FormUtils.isArrayField( form[inputNames[0]] );
		if( !array && index != undefined && index > 0 )
			showAlert( 'form.error.notArray'.i18n() );

		var result = new Hash();
		$A(inputNames).each(
			function( inputName )
			{
				if( array )
					result.set( form[inputName][index] );
				else
					result.set( form[inputName] );
			}
		);
	},
	*/

	/**
	 * 清除傳入之field之值.
	 * 若field的type為:
	 * text, textarea, hidden : 設成空字串.
	 * select-one, select-multiple : selectedIndex設為0.
	 * checkbox, radio : checked設為false.
	 */
	clearFieldValue: function ( field )
	{
		if( field.type == "text" || field.type == "textarea" || field.type == "hidden" )
			field.value = "";
		else if( field.type == "select-one" || field.type == "select-multiple" )
			field.selectedIndex = 0;
		else if( field.type == "checkbox" || field.type == "radio" )
			field.checked = false;
	},

	/**
	 * 設定field之值
	 * 若field的type為:
	 * text, textarea, hidden : value會指定給該field的value.
	 * select-one, select-multiple : 若value與select中的某一option值相同時, 設定其selectedIndex, 反之則設定為0.
	 *	注意, 即使是select-multiple, 仍只會設定一個值.
	 * checkbox, radio : 若value與field.value相等時, 會將其設為checked, 反之, 則checked為false.
	 * 若傳入之field為陣列, 會檢查該陣列是否為radio, 若是radio, 會依傳回之值之各個radio比對, 將value相同的勾選起來.
	 */
	setFieldValue: function ( field, value )
	{
		if( field.length && field[0].type == 'radio' )
		{
			$A(field).each(
				function( f )
				{
					if( f.value == value )
					{
						f.checked = true;
						$break;
					}
				}
			);
		}
		else
		{
			if( field.type == "text" || field.type == "textarea" || field.type == "hidden" )
				field.value = value;
			else if( field.type == "select-one" || field.type == "select-multiple" )
			{
				var index = 0;
				for( var i=0; i<field.options.length; i++ )
				{
					if( field.options[i].value == value )
						index = i;
				}
				field.selectedIndex = index;
			}
			else if( field.type == "checkbox" || field.type == "radio" )
			{
				field.checked = field.value == value;
			}
		}
	},
	getFieldValue: function ( field )
	{
		var fieldValue;
		var tagName;
		//先判斷傳入之欄位是否為陣列(select或radio group).
		if( field.length == undefined )
		{
			tagName = field.tagName.toLowerCase();
			switch( tagName )
			{
				case 'select' :
					fieldValue = field.value;
					break;
				case 'input' :
					var typeName = field.type.toLowerCase();
					switch( typeName )
					{
						case 'checkbox' :
						case 'radio' :
							if( field.checked )
								fieldValue = field.value;
							break;
						default :
							fieldValue = field.value;
							break;
					}
					break;
				default :
					alert("unsupportted tag : " + tagName );
					break;
			}
		}
		else
		{
			tagName = field[0].tagName.toLowerCase();
			switch( tagName )
			{
				case 'input' :
					typeName = field[0].type.toLowerCase();
					switch( typeName )
					{
						case 'radio' :
							for( var i=0; i<field.length ; i++ )
							{
								if( field[i].checked )
								{
									fieldValue = field[i].value;
									break;
								}
							}
							break;
						default :
							alert("unsupportted type : " + typeName );
							break;
					}
					break;
				case 'option' :
					var found = false;
					for( i=0; i<field.length ; i++ )
					{
						if( field[i].selected )
						{
							if( !found )
							{
								fieldValue = field[i].value;
								found = true;
							}
							else
								alert( 'form.notSupport'.i18n("Multiple Select") );
						}
					}
					break;
				default :
					alert("unsupportted tag : " + tagName );
					break;
			}
		}
		return fieldValue;
	},
	/**
	 * 將sourceField的值複製給targetField.
	 */
	copyFieldValue: function ( sourceField, targetField )
	{
		if( sourceField == null || sourceField == undefined )
		{
			showAlert( 'form.copy.notNullable'.i18n("Source Field") );
			return;
		}
		if( targetField == null || targetField == undefined )
		{
			showAlert( 'form.copy.notNullable'.i18n("Target Field") );
			return;
		}
		if( sourceField.type != targetField.type )
		{
			showAlert( 'form.copy.notMatch'.i18n("Source", "Target", "Field type") );
			return;
		}

		if( targetField.type == "text" || targetField.type == "textarea" || targetField.type == "hidden" )
			targetField.value = sourceField.value;
		else if( targetField.type == "select-one" || targetField.type == "select-multiple" )
			targetField.selectedIndex = sourceField.selectedIndex;
		else if( targetField.type == "checkbox" || targetField.type == "radio" )
			targetField.checked = sourceField.checked;
	},

	/**
	 * 依序將sourceFields的值複製給targetFields.
	 */
	copyAllFieldValue: function ( sourceFields, targetFields )
	{
		if( sourceFields.length && targetFields.length )
		{
			if( sourceFields.length != targetFields.length )
			{
				showAlert( 'form.copy.countNotMatch'.i18n() );
				return;
			}
			for( var i=0; i<sourceFields.length; i++ )
			{
				FormUtils.copyFieldValue( sourceFields[i], targetFields[i] );
			}
		}
		else
			FormUtils.copyFieldValue( sourceFields, targetFields );
	},

	/**
	 * 依指定之paramName及paramValue建立一hidden欄位的物件後傳回.
	 */
	createHiddenField: function ( paramName, paramValue )
	{
		var input = document.createElement("input");
		input.type = "hidden";
		input.name = paramName;
		input.value = paramValue;
		return input;
	},
	/**
	 * 當form的內容不允許修改, 但又要保留原form的輸入項目時, 可呼叫本函式, 若form中的資料有被更動, 便會出現錯誤訊息, 並呼叫form的reset()來恢復原值.
	 */
	unmodifiable: function( form )
	{
		if( form )
		{
			$(form).select("input").each(
				function( field )
				{
					var typeName = field.type.toLowerCase();
					switch( typeName )
					{
						case 'checkbox' :
						case 'radio' :
							field.observe("click", FormUtils._unmodifiableHandler.bindAsEventListener( this, form ) );
							break;
						default :
							field.observe("change", FormUtils._unmodifiableHandler.bindAsEventListener( this, form ) );
							break;
					}
				}
			);
			$(form).select("select").each(
				function( field )
				{
					field.observe("change", FormUtils._unmodifiableHandler.bindAsEventListener( this, form ) );
				}
			);
			$(form).select("textarea").each(
				function( field )
				{
					field.observe("change", FormUtils._unmodifiableHandler.bindAsEventListener( this, form ) );
				}
			);
		}
	},
	_unmodifiableHandler: function( event, form )
	{
		Event.stop( event );
		alert( 'form.notModifiable.page'.i18n() );
		form.reset();
	},
	findInputElements: function ( block )
	{
		if( BrowserUtils.checkIdExists( block ) )
		{
			var fieldsArray = new Array();
			fieldsArray.addAll( $(block).select("input") );
			fieldsArray.addAll( $(block).select("select") );
			fieldsArray.addAll( $(block).select("textarea") );
			return fieldsArray;
		}
		return new Array();
	},
	/**
	 * 將傳入之block設定為顯示, 並將block中所包含的各種input設定為enable
	 */
	showAndEnable: function( block )
	{
		if( BrowserUtils.checkIdExists( block ) )
		{
			$(block).show();
			var fields = FormUtils.findInputElements( block );
			fields.invoke('enable');
		}
	},
	/**
	 * 將傳入之block設定為隱藏, 並將block中所包含的各種input設定為disable
	 */
	hideAndDisable: function( block )
	{
		if( BrowserUtils.checkIdExists( block ) )
		{
			$(block).hide();
			var fields = FormUtils.findInputElements( block );
			fields.invoke('disable');
		}
	}
}

/**
 * 可依據某一欄位之值, 當其為某一指定值時, 設定其他欄位及區塊是否顯示.
 * 依switchType不同, 可以有不同的變化:
 * 'on_off': 可指定某一欄位為某一特定值時, 就顯示/隱藏某些欄位及區塊, 若不為指定值時, 則反向, 類似On,Off的動作.
 * 'multiple': 可依某一欄位的值不同時, 顯示/隱某些某些欄位及區塊.
 */
var ToggleSwitch = Class.create();
ToggleSwitch.prototype =
{
	/**
	 * 初始化, 所需參數如下:
	 * field: 指定之欄位(Form element)
	 * eventName: 要處理的event name, 若有指定, 會自動為指定之field observe該event
	 * switchType: 開關型態, 合法值為'on_off', 'multiple', 若未指定本參數時, 當switches中項目大於1時, 此值自動設定為multiple, 若switches項目為1時, 則預設為'on_off'.
	 *		on_off: switches中只能有一個項目, 當欄位值符合value時, 就執行該項目中的項目
	 *		multiple: switches可有多個項目, 依field value之值處理對應的動作
	 * switches: 設定要轉換的值及對應之欄位, 區塊, 其參數如下:
	 *		value: 本toggle指定之值, 若指定之欄位值與此值相同時, 則將指定之欄位enable, 並顯示區塊, 若不同, 則將指定之欄位disalbe, 並隱藏區塊.
	 *		show: 要顯示的區塊, 欄位及title的參數, 參數內容如下:
	 *			fields: 指定要顯示的所有欄位(form element), 此為陣列型式, 可指定多個欄位.
	 *			blocks: 指定要顯示的所有區塊資訊, 其參數如下:
	 *				blockId: block id.
	 *				includeFields: true or false, 設定是否直接搜尋block內之所有field, 直接套用enable, 就可以不必在fields中指定.
	 *				disableOnly: true or false, 設定是否只要將block中的field disable, 而不用隱藏, 此值的設定必須在includeFields設定為true時才有作用.
	 *			titles(optional): 當顯示時要設定的欄位title. 其參數如下:
	 *				titleId: title所在之區塊的id.
	 *				title: 要顯示之欄位名稱.
	 *		hide: 要隱藏的區塊, 欄位及title的參數, 參數內容如下:
	 *			fields: 指定要隱藏的所有欄位(form element), 此為陣列型式, 可指定多個欄位.
	 *			blocks: 指定要隱藏的所有區塊資訊, 其參數如下:
	 *				blockId: block id.
	 *				includeFields: true or false, 設定是否直接搜尋block內之所有field, 直接套用disable, 就可以不必在fields中指定.
	 * defaultSwitch: 當欄位之值未在指定的switches裡出現時, 會套用此預設值. 其參數如下:
	 *		show: 同上.
	 *		hide: 同上.
	 * 使用範例如下:
	 *	var toggleFields = new ToggleSwitch(
	 *		{
	 *			field: form['option2'],
	 *			eventName: 'change',
	 *			toggles:
	 *			[
	 *				{
	 *					value: '1',
	 *					effect: 'show_hide',
	 *					show:
	 *					{
	 *						fields:
	 *						[
	 *							form['ticket.expectedResponseDate'],
	 *							form['ticket.ownerTeamUniId'],
	 *							form['ticket.description']
	 *						],
	 *						blocks:
	 *						[
	 *							{blockId: 'respDateTitle'},
	 *							{blockId: 'respDateField'},
	 *							{blockId: 'otherBlock'},
	 *							{blockId: 'teamTitle'},
	 *							{blockId: 'teamField'},
	 *							{blockId: 'descBlock'},
	 *						],
	 *						titles:
	 *						[
	 *							{titleId: 'respDateTitle', title: '新title1'},
	 *							{titleId: 'teamTitle', title: '新title2'}
	 *						]
	 *					}
	 *				},
	 *				{
	 *					value: '2',
	 *					effect: 'show_hide',
	 *					show:
	 *					{
	 *						fields:
	 *						[
	 *							form['ticket.expectedResponseDate']
	 *						],
	 *						blocks:
	 *						[
	 *							{blockId: 'respDateTitle'},
	 *							{blockId: 'respDateField'}
	 *						],
	 *						titles:
	 *						[
	 *							{titleId: 'respDateTitle', title: '預計回覆日'},
	 *						]
	 *					},
	 *					hide:
	 *					{
	 *						fields:
	 *						[
	 *							form['ticket.ownerTeamUniId'],
	 *							form['ticket.description']
	 *						],
	 *						blocks:
	 *						[
	 *							{blockId: 'otherBlock'},
	 *							{blockId: 'teamTitle'},
	 *							{blockId: 'teamField'},
	 *							{blockId: 'descBlock'}
	 *						]
	 *					}
	 *				},
	 *				{
	 *					value: '3',
	 *					effect: 'show_hide',
	 *					show:
	 *					{
	 *						fields:
	 *						[
	 *							form['ticket.ownerTeamUniId'],
	 *							form['ticket.description']
	 *						],
	 *						blocks:
	 *						[
	 *							{blockId: 'otherBlock'},
	 *							{blockId: 'teamTitle'},
	 *							{blockId: 'teamField'},
	 *							{blockId: 'descBlock'}
	 *						],
	 *						titles:
	 *						[
	 *							{titleId: 'teamTitle', title: '負責組別'},
	 *						]
	 *					},
	 *					hide:
	 *					{
	 *						fields:
	 *						[
	 *							form['ticket.expectedResponseDate']
	 *						],
	 *						blocks:
	 *						[
	 *							{blockId: 'respDateTitle'},
	 *							{blockId: 'respDateField'}
	 *						]
	 *					}
	 *				}
	 *			],
	 *			defaultToggle:
	 *			{
	 *				effect: 'show_hide',
	 *				show:
	 *				{
	 *					fields:
	 *					[
	 *						form['ticket.expectedResponseDate'],
	 *						form['ticket.ownerTeamUniId'],
	 *						form['ticket.description']
	 *					],
	 *					blocks:
	 *					[
	 *						{blockId: 'respDateTitle'},
	 *						{blockId: 'respDateField'},
	 *						{blockId: 'otherBlock'},
	 *						{blockId: 'teamTitle'},
	 *						{blockId: 'teamField'},
	 *						{blockId: 'descBlock'},
	 *					],
	 *					titles:
	 *					[
	 *						{titleId: 'respDateTitle', title: '預計回覆日'},
	 *						{titleId: 'teamTitle', title: '負責組別'}
	 *					]
	 *				}
	 *			}
	 *		}
	 *	);
	 */
	initialize: function( args )
	{
		//因為被ToggleFields繼承, 繼承時無法傳遞參數, 所以要判斷是否存在.
		if( args )
		{
			this.field = $(args.field);
			if( args.eventName )
				this.eventName = args.eventName;
			else
				this.eventName = "change";
			this.switches = args.switches;
			this.defaultSwitch = args.defaultSwitch;
			if( args.switchType )
			{
				this.switchType = args.switchType;
				if( this.switchType == 'on_off' && this.switches.length > 1 )
				{
					alert( 'toggle.error.swtichType'.i18n() );
					return;
				}
			}
			else
			{
				if( this.switches.length > 1 || this.defaultSwitch )
					this.switchType = "multiple";
				else
					this.switchType = "on_off";
			}
			if( this.eventName )
			{
				if( FormUtils.isArrayField( this.field ) )
				{
					for( var i=0,size=this.field.length; i<size; i++ )
						$(this.field[i]).observe( this.eventName, this.toggle.bindAsEventListener( this ) );
				}
				else
					$(this.field).observe( this.eventName, this.toggle.bindAsEventListener( this ) );
			}
			this.toggle();
		}
	},
	toggle: function( event )
	{
		if( FormUtils.isArrayField( this.field ) )
			this.eventTargetId = this.field[0].name
		else
			this.eventTargetId = this.field.name;
		if( event )
		{
			var target = event.element();
			this.eventTargetId = target.name;
		}
		switch( this.switchType )
		{
			case 'on_off' :
				this._toggleOnOff();
				break;
			case 'multiple' :
				this._toggleMultiple();
				break;
			default :
				alert( 'toggle.error.switchType.unknown'.i18n() + this.switchType );
		}
	},
	_toggleOnOff: function()
	{
		var fieldValue = FormUtils.getFieldValue( this.field );
		var showBlocks = new Array();
		var hideBlocks = new Array();
		var enableFields = new Array();
		var disableFields = new Array();

		var sw = this.switches[0];
		if( sw.show )
		{
			enableFields.addAll( sw.show.fields );
			showBlocks.addAll( sw.show.blocks );
			enableFields.addAll( this.findFieldsInBlocks( sw.show.blocks ) );
		}
		if( sw.hide )
		{
			disableFields.addAll( sw.hide.fields );
			hideBlocks.addAll( sw.hide.blocks );
			disableFields.addAll( this.findFieldsInBlocks( sw.hide.blocks ) );
		}
		var equals;
		if( typeof( sw.value ) == 'boolean' )
			equals = eval(fieldValue) == sw.value;
		else
			equals = fieldValue == sw.value;
		if( equals )
		{
			if( sw.show )
				this.processTitles( sw.show.titles );
			this.play( showBlocks, hideBlocks, enableFields, disableFields );
		}
		else//若不相等就執行反向動作
		{
			this.play( hideBlocks, showBlocks, disableFields, enableFields );
		}
	},
	_toggleMultiple: function()
	{
		var fieldValue = FormUtils.getFieldValue( this.field );
		var showBlocks = new Array();
		var hideBlocks = new Array();
		var enableFields = new Array();
		var disableFields = new Array();
		var valueFound = false;
		for( var i=0 ; i<this.switches.length ; i++ )
		{
			var sw = this.switches[i];
			var equals;
			if( typeof( sw.value ) == 'boolean' )
				equals = eval(fieldValue) == sw.value;
			else
				equals = fieldValue == sw.value;
			if( equals )
			{
				valueFound = true;
				if( sw.show )
				{
					enableFields.addAll( sw.show.fields );
					showBlocks.addAll( sw.show.blocks );
					enableFields.addAll( this.findFieldsInBlocks( sw.show.blocks ) );
					this.processTitles( sw.show.titles );
				}
				if( sw.hide )
				{
					disableFields.addAll( sw.hide.fields );
					hideBlocks.addAll( sw.hide.blocks );
					disableFields.addAll( this.findFieldsInBlocks( sw.hide.blocks ) );
				}
			}
		}
		if( !valueFound )
		{
			//若傳入的欄位並不符合指定之值時, 使用default的設定, 若default不存在時就return.
			if( !this.defaultSwitch )
				return;
			if( this.defaultSwitch.show )
			{
				enableFields.addAll( this.defaultSwitch.show.fields );
				showBlocks.addAll( this.defaultSwitch.show.blocks );
				enableFields.addAll( this.findFieldsInBlocks( this.defaultSwitch.show.blocks ) );
				this.processTitles( this.defaultSwitch.show.titles );
			}
			if( this.defaultSwitch.hide )
			{
				disableFields.addAll( this.defaultSwitch.hide.fields );
				hideBlocks.addAll( this.defaultSwitch.hide.blocks );
				disableFields.addAll( this.findFieldsInBlocks( this.defaultSwitch.hide.blocks ) );
			}
		}
		this.play( showBlocks, hideBlocks, enableFields, disableFields );
	},
	processTitles: function ( titles )
	{
		if( !titles )
			return;
		titles.each(
			function( title )
			{
				if( BrowserUtils.checkIdExists( title.titleId ) )
				{
					$(title.titleId).innerHTML = title.title;
				}
				else
				{
					return;
				}
			}
		);
	},
	findFieldsInBlocks: function ( blocks )
	{
		if( !blocks )
			return null;
		var fieldsArray = new Array();
		for( var i=0,size=blocks.length; i<size; i++ )
		{
			if( eval( blocks[i].includeFields ) )
			{
				fieldsArray.addAll( FormUtils.findInputElements( blocks[i].blockId ) );
			}
		}
		return fieldsArray;
	},
	play: function( showBlocks, hideBlocks, enableFields, disableFields )
	{
		//考慮多重開關的情況下, 同一區塊或欄位可能被隱藏多次, 為正確控制隱藏之狀態, 所以要以disableCount來計算隱藏次數並決定顯示與否
		//此外, 同一組開關所引發的隱藏動作只能算一次, 所以必須同時考量引發動作的element
		var i,size;
		if( hideBlocks )
		{
			for( i=0; i<hideBlocks.length; i++ )
			{
				var hideBlock = $(hideBlocks[i].blockId);
				if( hideBlock )
				{
				if( this.eventTargetId )
				{
					if( hideBlock.hideBy )
					{
						if( hideBlock.hideBy.indexOf( this.eventTargetId ) == -1 )
							hideBlock.hideBy.push( this.eventTargetId );
					}
					else
					{
						hideBlock.hideBy = new Array();
						hideBlock.hideBy.push( this.eventTargetId );
					}
				}
				hideBlock.style.display = "none";
			}
			}
			hideBlocks.clear();
		}
		if( showBlocks )
		{
			for( i=0; i<showBlocks.length; i++ )
			{
				var showBlock = $(showBlocks[i].blockId);
				if( showBlock )
				{
					if( showBlock.hideBy )
					{
						if( showBlock.hideBy.length > 0 )
						{
							showBlock.hideBy = showBlock.hideBy.without( this.eventTargetId );
						}
						if( showBlock.hideBy.length == 0 )
						{
							showBlock.style.display = "";
							showBlock.style.opacity = 100;
						}
					}
					else
					{
						showBlock.style.display = "";
						showBlock.style.opacity = 100;
					}
				}
			}
			showBlocks.clear();
		}
		if( enableFields )
		{
			for( i=0,size=enableFields.length; i<size; i++ )
			{
				if( enableFields[i].disabledBy )
				{
					if( enableFields[i].disabledBy.length > 0 )
					{
						enableFields[i].disabledBy = enableFields[i].disabledBy.without( this.eventTargetId );
					}
					if( enableFields[i].disabledBy.length == 0 )
						$(enableFields[i]).enable();
				}
				else
					$(enableFields[i]).enable();
			}
			enableFields.clear();
		}
		if( disableFields )
		{
			for( i=0,size=disableFields.length; i<size; i++ )
			{
				if( this.eventTargetId )
				{
					if( disableFields[i].disabledBy )
					{
						if( disableFields[i].disabledBy.indexOf( this.eventTargetId ) == -1 )
							disableFields[i].disabledBy.push( this.eventTargetId );
					}
					else
					{
						disableFields[i].disabledBy = new Array();
						disableFields[i].disabledBy.push( this.eventTargetId );
					}
				}
				$(disableFields[i]).disable();
			}
			disableFields.clear();
		}
	}
}

/**
 * Deprecated, 改用ToggleSwitch.
 */
var ToggleOnOff = Class.create();
ToggleOnOff.prototype =
{
	initialize: function( args )
	{
		if( args )
		{
			if( args.toggle.action == 'on' )
				this.toggleSwitch = new ToggleSwitch( {field: args.field, switches: [{value: args.toggle.value, show: {fields: args.toggle.fields, blocks: args.toggle.blocks} }] } );
			else
				this.toggleSwitch = new ToggleSwitch( {field: args.field, switches: [{value: args.toggle.value, hide: {fields: args.toggle.fields, blocks: args.toggle.blocks} }] } );
		}
	},
	doToggle: function()
	{
		this.toggleSwitch.toggle();
	}
}

/**
 * Deprecated, 改用ToggleSwitch.
 */
var ToggleFields = Class.create();
ToggleFields.prototype =
{
	initialize: function( args )
	{
		if( args )
		{
			this.toggleSwitch = new ToggleSwitch( {field: args.field, switches: args.toggles, defaultSwitch: args.defaultToggle } );
		}
	},
	doToggle: function()
	{
		this.toggleSwitch.toggle();
	}
}
//*************************************************************
//	form input relative effect end
//*************************************************************
var FoldingBlock = Class.create();
FoldingBlock.prototype =
{
	initialize: function( _options )
	{
		this.options = {
			expendedImage : "../images/expand_mark.gif",
			collapsedImage : "../images/collapse_mark.gif",
			description : "Folding Block",
			collapsed : false,
			contentId : null
		};
		Object.extend( this.options, _options || {} );
		this.oldVersion = false;
		if( this.options.oldId != undefined && $(this.options.oldId) )
		{
			this.oldVersion = true;
		}

		if( this.oldVersion )
			this._preProcessOldVersion();
		else
		{
			this._preProcess();
			this._buildFoldingBlock();
		}
		this.collapsed = this.options.collapsed;
		if( this.id )
		{
			if( !$(this.id).hasClassName("foldingBlock") )
				$(this.id).addClassName("foldingBlock");
		}
	},
	_preProcess: function()
	{
		if( this.options.id == undefined )
			alert( 'folding.error.id.required'.i18n() );
		else if( !$(this.options.id) )
			alert( 'folding.error.notExist'.i18n("id") );
		else
			this.id = this.options.id;
		if( this.options.contentId != null )
		{
			if( !$(this.options.contentId) )
				alert( 'folding.error.notExist'.i18n("contentId") );
			else
				this.contentId = this.options.contentId;
		}
		else
		{
			this.contentId = this._createContentId();
		}
	},
	_buildFoldingBlock: function()
	{
		var content = $(this.contentId);
		var table = new Element("table", {border: '0', cellspacing: '0', cellpadding:'0', 'class':'foldingTable'} );
		content.insert( {'before': table } );
		var tr = new Element("tr");
		table.update( tr );
		var imgTd = new Element("td", {'class':'foldingMark'});
		var img = new Element("img", {'class': 'foldingImage'});
		this.imgId = img.identify();
		imgTd.update( img );
		if( this.collapsed )
			img.src = this.options.expendedImage;
		else
			img.src = this.options.collapsedImage;
		img.observe("click", this.autoFolding.bindAsEventListener( this ) );
		img.observe("mouseover", function ( event ) {event.element().setStyle({'cursor': 'pointer'}) } )
		tr.insert( imgTd );

		var contentTd = new Element("td", {'class': this.collpased ? 'foldingCollapsed' : 'foldingOpened'});
		tr.insert( contentTd );

		var div = new Element("div");
		contentTd.insert( div );
		this.contentBlockId = div.identify();
		div.insert( content );
		this._createDescriptionSpan();
	},
	_createContentId: function()
	{
		var block = $(this.id);
		var children = block.childElements();
		if( children && children.length > 0 )
		{
			if( children.length > 1 )
				alert('folding.error.noMoreChild');
			return children[0].identify();
		}
		else
		{
			var emptyDiv = new Element("div");
			block.update( emptyDiv );
			return emptyDiv.identify();
		}
	},
	_preProcessOldVersion: function()
	{
		this.id = this.options.oldId;
		var markTd = $(this.id).select("[class=foldingMark]")[0];
		var img = markTd.select('img')[0];
		img.className = "foldingImage";
		this.imgId = img.identify();
		var contentTd = markTd.next();
		var contentDiv = contentTd.select('div')[0];
		this.contentBlockId = contentDiv.identify();
		this._createDescriptionSpan();
	},
	_createDescriptionSpan: function()
	{
		var span = new Element("span", {'class': 'foldingDescription'} );
		span.update( this.options.description );
		$(this.contentBlockId).insert( {'before':span} );
		this.descriptionId = span.identify();
		span.hide();
	},
//	autoFolding: function( collapsed )
//	{
//		if( this.oldVersion )
//		{
//			alert("此物件為舊版FoldingObj, 應呼叫autoFoldingOld");
//			return;
//		}
//		//若有傳入collapsed, 則以傳入之值為準, 若沒有, 則將原物件中的狀態反向.
//		if( collapsed != undefined && typeof(collapsed) == 'boolean' )
//			this.collapsed = eval( collapsed );
//		else
//			this.collapsed = !this.collapsed;
//		var block = $(this.id);
//		var img = block.select("img[class=foldingImage]")[0];
//		if( this.collapsed )
//			img.src = this.options.expendedImage;
//		else
//			img.src = this.options.collapsedImage;
//		var handler = block.select("div[class=foldingHandlerDiv]")[0];
//		if( this.collapsed )
//		{
//			$(this.contentId).hide();
//			handler.setStyle( {'float':'none', position:'static'} );
//			handler.select("span")[0].show();
//		}
//		else
//		{
//			handler.select("span")[0].hide();
//			handler.setStyle( {'float':'left', position:'absolute'} );
//			$(this.contentId).show();
//		}
//	},
//	_insertHandler: function()
//	{
//		var block = $(this.id);
//		if( !block.hasClassName("foldingBlock") )
//			block.addClassName("foldingBlock");
//		var div = new Element("div", {'class': 'foldingHandlerDiv'} );
//		var img = new Element("img", {'class': 'foldingImage'});
//		div.update( img );
//		var content = $(this.contentId);
//		content.setStyle( {'padding-left': '20px', position: 'relative'} );
//		content.insert( {"before": div} );
//		if( this.collapsed )
//			img.src = this.options.expendedImage;
//		else
//			img.src = this.options.collapsedImage;
//		var span = new Element("span", {'class': 'foldingDescription'} );
//		span.update( this.options.description );
//		img.insert( {'after': span} );
//		img.observe("click", this.autoFolding.bindAsEventListener( this ) );
//		img.observe("mouseover", function ( event ) {event.element().setStyle({'cursor': 'pointer'}) } )
//		span.hide();
//	},
	autoFolding: function(collapsed)
	{
//		if( !this.oldVersion )
//		{
//			alert("此物件非舊版FoldingObj, 應呼叫autoFolding");
//			return;
//		}
		//若有傳入collapsed, 則以傳入之值為準, 若沒有, 則將原物件中的狀態反向.
		if( collapsed != undefined && typeof(collapsed) == 'boolean' )
			this.collapsed = eval( collapsed );
		else
			this.collapsed = !this.collapsed;
		var img = $(this.imgId);
		if( this.collapsed )
			img.src = this.options.expendedImage;
		else
			img.src = this.options.collapsedImage;

		var contentBlock = $(this.contentBlockId);
		if( this.collapsed )
		{
			contentBlock.hide();
			contentBlock.up().className = "foldingCollapsed";
			$(this.descriptionId).show();
		}
		else
		{
			contentBlock.show();
			contentBlock.up().className = "foldingOpened";
			$(this.descriptionId).hide();
		}
	}
}

//*************************************************************
//  the following is for backward compatibilites
//*************************************************************
function changeActionAndSubmit( form, args, event )
{
	FormUtils.changeActionAndSubmit( form, args, event );
}

function checkAll( checked, checkboxes, ignoreDisabled )
{
	FormUtils.checkAll( checked, checkboxes, ignoreDisabled );
}

function recheckAll( checkboxes, controlCheckbox, ignoreDisabled )
{
	FormUtils.recheckAll( checkboxes, controlCheckbox, ignoreDisabled );
}

function copyFields( args )
{
	FormUtils.copyFields( args );
}
