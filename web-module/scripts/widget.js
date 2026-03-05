/**
 * ---------------------------------------------------------------------------------------------------
 * $Id: widget.js,v 1.2 2014/08/13 02:58:59 asiapacific\hsiehes Exp $
 * ---------------------------------------------------------------------------------------------------
 */

var Widget = Class.create();
Widget.prototype =
{
	initialize: function()
	{
		this.widgetElement = null;
	},
	setOptions: function( options )
	{
		//預設的options參數, 可以透過setOptions來更改預設值.
		this.options =
		{
			//本widget的parent node.
			parent: document.body,
			onComplete: function() {}
		};
		Object.extend( this.options, options || {} );
	},
	reset: function()
	{
		this.options = {};
		this.onReset();
	},
	onReset: function()
	{
	},
	resizeTo: function( width, height )
	{
	},
	getWidgetDomObject: function()
	{
		return this.widgetElement;
	},
	show: function()
	{
		if( this.widgetElement )
			this.widgetElement.show();
	},
	hide: function()
	{
		if( this.widgetElement )
			this.widgetElement.hide();
	},
	destroy: function()
	{
		this._beforeDestroy();
		Widget.destroyWidget( this );
		this.widgetElement = null;
		this._afterDestroy();
		this.reset();
	},
	_beforeDestroy: function()
	{
	},
	_afterDestroy: function()
	{
	},
	_findChildWidgetElements: function( /*string*/ element )
	{
		var tags = this.widgetElement.childElements();
		var childWidgetElements = new Array();
		$A(tags).each(
			function(tag)
			{
				if( !element || tag.tagName.toLowerCase() == element.toLowerCase() )
				{
					var attr = tag.attributes['widgettype'];
					if( attr )
					{
						childWidgetElements.push( tag );
					}
				}
			}
		);
		return childWidgetElements;
	},
	_createDivElement: function(/*id(string) or element*/ id )
	{
		if( id )
		{
			if( $(id) )
			{
				return $(id);
			}
			else
			{
				var div = new Element("div", {id: id});
				return $(div);
			}
		}
		else
		{
			alert( 'validate.param.required'.i18n("id") );
			return null;
		}
	}
}

Widget.parseWidget = function( /* id(string) or element */obj )
{
	var o = $(obj);
	if( !o )
	{
		showAlert( 'widget.notExist'.i18n() + obj );
		return null;
	}
	var attr = o.readAttribute('widgettype');
	if( !attr )
	{
		showAlert( 'widget.error.notWidget'.i18n() );
		return null;
	}

	switch( attr )
	{
		case "LayoutContainer":
			return LayoutContainer.parse( obj );
			break;
		case "ContentPane":
			return ContentPane.parse( obj );
			break;
		case "TabContainer":
			return TabContainer.parse( obj );
			break;
		default:
			showAlert( 'widget.notSupport'.i18n() + attr );
			return null;
	}
}

var _allWidgets = new Array();
Widget.findWidgetById = function( /*string*/ id )
{
	//return _allWidgets[id];
	for( var i=0; i<_allWidgets.length; i++ )
	{
		var widget = _allWidgets[i];
		if( widget.getWidgetDomObject().id == id )
			return widget;
	}
	return null;
}

Widget.addWidget = function( widget )
{
	if( widget instanceof Widget )
	{
		_allWidgets.push( widget );
	}
	else
		showAlert( 'widget.error.notWidget'.i18n() );
}

Widget.destroyWidget = function( widget )
{
	for( var i=0; i<_allWidgets.length; i++ )
	{
		var w = _allWidgets[i];
		if( w == widget )
		{
			//widget.destroy();
			DomUtils.destroyNode( widget.getWidgetDomObject() );
			_allWidgets.splice( i, 1 );
			break;
		}
	}
}

Widget.clearAllWidgets = function()
{
	for( var i=0; i<_allWidgets.length; i++ )
	{
		var widget = _allWidgets[i];
		widget.destroy();
	}
	_allWidgets.clear();
}
//於unload時, 清除尚未被移除的widget.
EdsEvent.addOnUnload( Widget.clearAllWidgets );

Widget.checkWidgetType = function( /*string or element*/obj, /*string*/widgettype )
{
	if( !$(obj) )
	{
		showAlert( 'widget.notExist'.i18n()+obj );
		return false;
	}
	var attr = $(obj).attributes['widgettype'];
	if( !attr || attr.nodeValue != widgettype )
	{
		showAlert( 'widget.notMatch'.i18n( widgettype ) );
		return false;
	}
	return true;
}

/**
 * 將指定之select物件改成可輸入的下拉選單.
 * 經轉換後的element元件實際上是一個text field.
 * 目前針對select的event, 只支援onchange, 亦即在select上定義之onchange會被複製到對應的text field中.
 * TODO: 支援其他event.
 * 建立Combox時所需的參數如下:
 * selectElement (required): 指定畫面上要轉換成Combox的select物件.
 * defaultValue (optional): 用來指定Combox的預設值, 若未指定, 則會以select物件之值為預設值, 但必須注意,
 *		由於Combox允許使用者自行輸入, 其值在原select物件中可能不存在, 所以即使原先有指定該值給select物件也可能會自動被清除,
 *		所以必須統一透過defaultValue來設定初始值.
 * minWidth (optional): 用來指定Combox的寬度, 預設值為50px.
 * hideSelectIE: 在IE上, 當發生dropdown時, 是否要隱藏其他select物件, 預設為false.
 * hideElements: 當發生dropdown時, 指定要隱藏的物件, 可指定id(以空白分隔多個id), 或傳入多個Html物件..
 * 使用範例如下:
 *	new ComboBox( {selectElement: form['entity.ncccFunction.tel1'], defaultValue: '<c:out value="${entity.ncccFunction.tel1}"/>', minWidth: 100} );
 */
var ComboBox = Class.create();
ComboBox.prototype = Object.extend( new Widget(),
	{
		initialize: function( options )
		{
			this.widgetElement = null;
			this.inputElement = null;
			this.image = null;
			this.optionsPanel = null;
			this.optionElements = new Array();
			this.selectedOption = null;
			this.mouseOverFunction = this._onMouseOver.bindAsEventListener( this );
			this.mouseOutFunction = this._onMouseOut.bindAsEventListener( this );
			this.mouseClickFunction = this._onMouseClick.bindAsEventListener( this );
			this.documentClickFunction = this._onDocumentClick.bindAsEventListener( this );
			this.optionPanelClickFunction = this._onOptionPanelClick.bindAsEventListener( this );
			this.setOptions( options );
			/* 下拉選單最多顯示筆數 */
			this.optionsShown = 10;
			this._createContainerDiv();
			Widget.addWidget( this );
		},
		setOptions: function( options )
		{
			this.options =
			{
				imageSrc: "../images/scroll_down.gif",
				imageOverSrc: "../images/scroll_down_over.gif",
				/* 預設最小寬度 */
				minWidth: 50,
				hideSelectIE: false,
				hideElements: null
			};
			Object.extend( this.options, options || {} );
		},
		onReset: function()
		{
			this.inputElement = null;
			this.image = null;
			this.optionsPanel = null;
			this.optionElements.clear();
			this.selectedOption = null;
		},
		_createContainerDiv: function()
		{
			var selectElement = $(this.options.selectElement);
			if( !selectElement )
				showAlert( 'widget.combo.selectObj'.i18n() );
			if( selectElement.tagName.toLowerCase() != 'select' )
				showAlert( 'widget.combo.notSelectObj'.i18n() );
			this.widgetElement = new Element("span", {'class': 'comboBoxContainer'} );
			Element.wrap( selectElement, this.widgetElement );
			var wrapper = new Element("span", {'class': 'comboBoxWrapper'} );
			this.widgetElement.appendChild( wrapper );

			this.inputElement = new Element("input", {name: selectElement.name, type: "text", 'class':'comboBoxInput'} );
			wrapper.appendChild( this.inputElement );
			if( selectElement.onchange )
			{
				this.inputElement.onchange = selectElement.onchange;
			}
			if( this.options.defaultValue )
				this.inputElement.value = this.options.defaultValue;
			else
				this.inputElement.value = selectElement.value;

			this.image = new Element("img", {'class':'comboBoxButton', src: this.options.imageSrc, border: '0px'} );
			wrapper.appendChild( this.image );
			Event.observe( this.image, "mouseover", this.mouseOverFunction );
			Event.observe( this.image, "mouseout", this.mouseOutFunction );
			Event.observe( this.image, "click", this.mouseClickFunction );

			this.optionsPanel = new Element("div", {'class':'comboBoxOptionPanel'} );
			this.widgetElement.appendChild( this.optionsPanel );

			this.optionElements = new Array();
			for( var i=0; i<selectElement.options.length; i++ )
			{
				var option = selectElement.options[i];
				var o = new Element("div", {'class':'comboBoxOption', title: option.value} );
				if( option.selected )
				{
					o.addClassName("comboBoxOptionSelected");
					this.selectedOption = o;
				}
				o.innerHTML = option.text || "&nbsp;";
				this.optionElements.push( o );
				this.optionsPanel.appendChild( o );
				Event.observe( o, "mouseover", this.mouseOverFunction );
				Event.observe( o, "mouseout", this.mouseOutFunction );
			}

			if( this.optionElements.length > this.optionsShown )
			{
				this.optionsPanel.addClassName("comboBoxOptionPanelHeight");
			}

			var id = selectElement.id;
			DomUtils.destroyNode( selectElement );
			if( id )
				this.inputElement.id = id;

			if( ieBrowser )
			{
				var wrapperHeight = Element.getHeight( wrapper );
				if( wrapperHeight == 0 )
					this.optionsPanel.style.top = 20;
				else
					this.optionsPanel.style.top = wrapperHeight;
			}

			//設定寬度, 但當原select所在之區塊一開始是被隱藏的狀態時, 就會沒有寬度, 此時便直接以參數之值來設定
			if( Element.getWidth( this.inputElement ) == 0 )
			{
				var width = this.options.minWidth + 16;
				this.optionsPanel.style.width = width;
				this.inputElement.style.width = width;
				wrapper.style.width = width;
			}
			else
			{
				var wrapperBP = StyleUtils.borderPaddingOffset( wrapper );
				var optionsPanelBP = StyleUtils.borderPaddingOffset( this.optionsPanel );
				var panelWidth = Element.getWidth( this.optionsPanel );
				if( panelWidth < this.options.minWidth )
					panelWidth = this.options.minWidth;
				width = panelWidth + Element.getWidth( this.image );
				this.optionsPanel.style.width = width - optionsPanelBP.width;
				this.inputElement.style.width = Element.getWidth( this.optionsPanel ) - Element.getWidth( this.image ) - StyleUtils.borderPaddingOffset( this.inputElement ).width;
				wrapper.style.width = Element.getWidth( this.optionsPanel ) - wrapperBP.width;
			}
			Element.hide( this.optionsPanel );
		},
		_showDropDownList: function()
		{
			Element.show( this.optionsPanel );
			Event.observe( this.optionsPanel, "click", this.optionPanelClickFunction );
			Event.observe( document, "click", this.documentClickFunction );
			if( this.options.hideSelectOnIE )
				BrowserUtils.hideSelectOnIE();
			if( ieBrowser && this.options.hideElements )
				BrowserUtils.hideElements( this.options.hideElements );
		},
		_hideDropDownList: function()
		{
			Element.hide( this.optionsPanel );
			Event.stopObserving( this.optionsPanel, "click", this.optionPanelClickFunction );
			Event.stopObserving( document, "click", this.documentClickFunction );
			if( this.options.hideSelectOnIE )
				BrowserUtils.showSelectOnIE();
			if( ieBrowser && this.options.hideElements )
				BrowserUtils.showElements( this.options.hideElements );
		},
		_onMouseOver: function( event )
		{
			//若本ComboBox的input element被disable, 就不處理.
			if( this.inputElement.disabled )
				return;
			var element = Event.element( event );
			if( element.tagName.toLowerCase() == "img" )
				element.src = this.options.imageOverSrc;
			else if( element.tagName.toLowerCase() == "div" )
				element.addClassName("comboBoxOptionOver");
		},
		_onMouseOut: function( event )
		{
			//若本ComboBox的input element被disable, 就不處理.
			if( this.inputElement.disabled )
				return;
			var element = Event.element( event );
			if( element.tagName.toLowerCase() == "img" )
				element.src = this.options.imageSrc;
			else if( element.tagName.toLowerCase() == "div" )
				element.removeClassName("comboBoxOptionOver");
		},
		_onMouseClick: function( event )
		{
			//EdsLog.debug("_onMouseClick:" + Event.element( event ).tagName + ":" + this.inputElement.name );
			//若本ComboBox的input element被disable, 就不允許下拉.
			if( this.inputElement.disabled )
				return;
			var element = Event.element( event );
			var container = element.up(".comboBoxContainer");
			if( this.widgetElement != container || Element.visible( this.optionsPanel ) )
				this._hideDropDownList();
			else
				this._showDropDownList();
		},
		_onDocumentClick: function( event )
		{
			//EdsLog.debug("_onDocumentClick:" + Event.element( event ).tagName + ":" + this.inputElement.name );
			if( Event.element( event ) != this.image )
				this._hideDropDownList();
		},
		_onOptionPanelClick: function( event )
		{
			//EdsLog.debug("_onOptionPanelClick:" + Event.element( event ).tagName + ":" + this.inputElement.name );
			var element = Event.element( event );
			if( this.selectedOption != element )
			{
				if( this.selectedOption )
					this.selectedOption.removeClassName("comboBoxOptionSelected");
				element.addClassName("comboBoxOptionSelected");
				this.selectedOption = element;
				this.inputElement.value = this.selectedOption.title;
				if( this.inputElement.onchange )
					this.inputElement.onchange();
			}
			this._hideDropDownList();
		}
	}
);

var ContentPane = Class.create();
ContentPane.prototype = Object.extend( new Widget(),
	{
		initialize: function( options )
		{
			this.widgetElement = null;
			this.setOptions( options );

			this._createContentDiv();
			Widget.addWidget( this );
		},
		setOptions: function( options )
		{
			//預設的options參數.
			this.options =
			{
				//本widget的parent node.
				parent: null,
				//widget寬度
				width: "",
				//widget高度
				height: "",
				id: null,
				content: null,
				url: null,
				backgroundColor: "",
				showOnCreate: true,
				onComplete: function() {}
			};
			Object.extend( this.options, options || {} );
		},
		resizeTo: function( width, height )
		{
			var bp = StyleUtils.borderPaddingOffset( this.widgetElement );
			if( width )
				this.widgetElement.style.width = width - bp.width;
			if( height )
				this.widgetElement.style.height = height - bp.height;
		},
		_createContentDiv: function()
		{
			this.widgetElement = this._createDivElement( this.options.id );
			if( this.options.width != "" )
				this.widgetElement.style.width = this.options.width;
			if( this.options.height != "" )
				this.widgetElement.style.height = this.options.height;
			if( this.options.backgroundColor != "" )
				this.widgetElement.style.backgroundColor = this.options.backgroundColor;
			if( this.options.showOnCreate )
				this.widgetElement.style.display = "";
			else
				this.widgetElement.style.display = "none";

			if( this.options.parent )
				this.options.parent.appendChild( this.widgetElement );

			if( this.options.content )
			{
				this.setContent( this.options.content );
			}
			else if( this.options.url )
			{
				this.setUrl( this.options.url );
			}
		},
		_parseChildWidgets: function()
		{

		},
		_removeChildElement: function()
		{
			if( this.childElement )
				DomUtils.destroyNode( this.childElement );
		},
		_beforeDestroy: function()
		{
			this._removeChildElement();
		},
		setContent: function( obj )
		{
			this._removeChildElement();
			if( typeof(obj) == 'string' )
			{
				this.widgetElement.innerHTML = obj;
				this.childElement = this.widgetElement.childNodes[0];
			}
			else
			{
				this.widgetElement.appendChild( obj );
				this.childElement = obj;
			}
		},
		setUrl: function( url )
		{
			this._removeChildElement();
			var iframe = document.createElement("iframe");
			iframe.src = url;
			iframe.width = "100%";
			iframe.height = "100%";
			iframe.frameBorder = "0";
			this.widgetElement.appendChild( iframe );
			this.childElement = iframe;
		}
	}
);

ContentPane.parse = function( /*id(string) or widget element*/obj )
{
	if( Widget.checkWidgetType( obj, "ContentPane") )
	{
		return new ContentPane( {id: obj} );
	}
	return null;
}

var TabContainer = Class.create();
TabContainer.prototype = Object.extend( new Widget(),
	{
		initialize: function( options )
		{
			this.widgetElement = null;
			this.tabHolderDiv = null;
			this.tabContentDiv = null;
			this.tabNameDivArray = new Array();
			this.tabContentPaneArray = new Array();
			this.selectedTabId = null;
			/* 被設定為預選的tab id */
			this.defaultTabId = null;
			this.currentTabName = null;
			this.currentTabContent = null;
			this.setOptions( options );
			/* tab內容最大的高度 */
			this.maxContentHeight = 0;
			this.selectTabFunction = this._selectTabEvent.bindAsEventListener( this );
			this.destroyFunction = this.destroy.bindAsEventListener( this );
			this.closeTabFunction = this.closeTab.bindAsEventListener( this );
			this.tabCounts = 0;
			this._createContainerDiv();
			Widget.addWidget( this );
		},
		onReset: function()
		{
			this.widgetElement = null;
			this.tabHolderDiv = null;
			this.tabContentDiv = null;
			this.tabNameDivArray.clear();
			this.tabContentPaneArray.clear();
			this.selectedTabId = null;
			this.tabCounts = 0;
			/* 被設定為預選的tab id */
			this.defaultTabId = null;
			this.currentTabName = null;
			this.currentTabContent = null;
			this.selectTabFunction = null;
			this.destroyFunction = null;
			this.closeTabFunction = null;
		},
		setOptions: function( options )
		{
			//預設的options參數, 可以透過setOptions來更改預設值.
			this.options =
			{
				//本widget的parent node.
				parent: null,
				//widget寬度
				width: "",
				//widget高度
				height: "",
				id: null,
				backgroundColor: "",
				showOnCreate: true,
				searchChildTab: false,
				onComplete: function() {}
			};
			Object.extend( this.options, options || {} );
		},
		resizeTo: function( width, height )
		{
			var bp = StyleUtils.borderPaddingOffset( this.widgetElement );
			if( width )
				this.widgetElement.style.width = width - bp.width;
			if( height )
				this.widgetElement.style.height = height - bp.height;
			this._resizeHolderAndContent();
		},
		_createContainerDiv: function()
		{
			this.widgetElement = this._createDivElement( this.options.id );
			this.widgetElement.className = "tabContainer";
			if( this.options.width != "" )
				this.widgetElement.style.width = this.options.width;
			if( this.options.height != "" )
				this.widgetElement.style.height = this.options.height;
			if( this.options.showOnCreate )
				this.widgetElement.style.display = "";
			else
				this.widgetElement.style.display = "none";
			if( this.options.parent )
				this.options.parent.appendChild( this.widgetElement );
			else if( !this.widgetElement.parentNode )
				document.body.appendChild( this.widgetElement );

			this._createTabHolderDiv();
			this._createTabContentDiv();
			Event.observe( window, "unload", this.destroyFunction );
			this._parseChildWidgets();
		},
		_createTabHolderDiv: function()
		{
			this.tabHolderDiv = new Element("div", {'class': 'tabHolder'});
			this.widgetElement.appendChild( this.tabHolderDiv );
		},
		_createTabContentDiv: function()
		{
			this.tabContentDiv = new Element("div", {'class': 'tabContent'});
			if( this.options.backgroundColor != "" )
				this.tabContentDiv.style.backgroundColor = this.options.backgroundColor;
			this.widgetElement.appendChild( this.tabContentDiv );
		},
		_createTabName: function( tabId, args )
		{
			//自訂tabId屬性來存tabId.
			var tabNameDiv = new Element("div", {id: tabId+'_tabName', 'class': 'tabName', tabId: tabId});
			this.tabHolderDiv.appendChild( tabNameDiv );
			this.tabNameDivArray.push( tabNameDiv );
			var innerDiv = new Element("div");
			tabNameDiv.appendChild( innerDiv );
			innerDiv.innerHTML = "<span>" + args.title + "</span>";
			Event.observe( tabNameDiv, "click", this.selectTabFunction );
			if( args.reloadable )
			{
				this._createReloadHandler( innerDiv, args );
			}
			if( args.closable )
			{
				this._createCloseHandler( innerDiv );
			}
			//在IE6中, tabNameDiv加入到tabHolderDiv時, div已存在, 直接parse的情況下, 第二個tab之後的高度會比第一個tab小, 會造成tab顯示時與下面內容有空隙,
			//所以針對IE版本做檢查, 當後面tab與前面不同時, 就計算offset, 然後加至padding-top以補足高度.
			//之所以設定在padding-top, 是因為padding-bottom在其他style中有設定, 此處若使用padding-bottom, 會造成其值無法再套用其他style.
			//TODO : 在使用此方式後, 原tabHolderDiv的高度會被改變, 反而會造成原本動態加入tab的部份出問題. 將靜態parsing的部份改寫成動態加入?
			/*
			if( ieBrowser )
			{
				var childs = $(this.tabHolderDiv).select("div");
				if( childs.length > 1 )
				{
					var offset = childs[0].getHeight() - tabNameDiv.getHeight();
					if( offset > 0 )
					{
						alert( $(this.tabHolderDiv).getHeight() );
						alert( $(tabNameDiv).getHeight() );
						alert("offset:" + offset );
						innerDiv.setStyle( {paddingTop : StyleUtils.extractNumber(innerDiv.getStyle("padding-top")) + offset+'px'} );
					}
				}
			}
			*/
		},
		_createReloadHandler: function( innerDiv, args )
		{
			if( args.tabContent && args.tabContent.options && args.tabContent.options.url )
			{
				var iframe = $(args.tabContent.widgetElement).down();
				if( iframe && iframe.tagName.toLowerCase() == 'iframe' )
				{
					var reloadSpan = new Element("a", {href: '#', 'class': 'reloadImage', title: 'label.refresh'.i18n(), target: args.tabContent.widgetElement.id})
					reloadSpan.innerHTML = "&nbsp;&nbsp;&nbsp;";
					innerDiv.appendChild( reloadSpan );
					reloadSpan.observe("click", this._reloadContent.bindAsEventListener(this, args.tabContent.options.url, iframe.identify() ) );
				}
			}
		},
		_reloadContent: function( event, link, iframeId )
		{
			Event.stop( event );
			if( $(iframeId) )
			{
				$(iframeId).src = link;
			}
		},
		_createCloseHandler: function( innerDiv )
		{
			var closeSpan = new Element("a", {href: '#', 'class': 'closeImage', title: 'widget.tab.closeTab'.i18n()})
			closeSpan.innerHTML = "&nbsp;&nbsp;&nbsp;";
			innerDiv.appendChild( closeSpan );
			Event.observe( closeSpan,"click", this.closeTabFunction );
		},
		_resizeHolderAndContent: function()
		{
			var containerBP = StyleUtils.borderPaddingSize( this.widgetElement );
			var containerHeight = Element.getHeight(this.widgetElement) - containerBP.height;
			var containerWidth = Element.getWidth(this.widgetElement) - containerBP.width;
			//若外框寬或高為0, 就不做調整, 避免因為tab被隱藏時, 其內容尺寸全被設為0, 而造成顯示上的問題.
			if( containerHeight == 0 || containerWidth == 0 )
				return;

			var tabHolderBP = StyleUtils.borderPaddingSize( this.tabHolderDiv );
			var tabContentBP = StyleUtils.borderPaddingSize( this.tabContentDiv );
			this.tabHolderDiv.setStyle({width: StyleUtils.checkStyleValue( containerWidth - tabHolderBP.width ), top: containerBP.top, left: containerBP.left});
			var holderHeight = Element.getHeight( this.tabHolderDiv );
			/** 若未指定container高度時, 且有tab的最大高度時, 就以該高度+tabHolder的高度為container的高度 */
			if( !this.options.height || this.options.height == "" )
			{
				if( this.maxContentHeight > 0 )
				{
					this.widgetElement.setStyle( {height: holderHeight+this.maxContentHeight+1} );
					containerHeight = Element.getHeight(this.widgetElement) - containerBP.height;
				}
			}

			//top必須減1, 被選擇之tab才能蓋住tabContent的border.
			var top = $(this.tabHolderDiv).positionedOffset().top + holderHeight - 1;
			if( geckoBrowser )
			{
				var containerB = StyleUtils.borderSize( this.widgetElement );
				top += containerB.top;
			}
			this.tabContentDiv.setStyle({
				width: StyleUtils.checkStyleValue( containerWidth - tabContentBP.width ),
				top: top,
				left: containerBP.left,
				height: StyleUtils.checkStyleValue( containerHeight - holderHeight - tabContentBP.height )
			});
			/*
			if( geckoBrowser )
			{
				if( Element.getWidth( this.tabContentDiv ) == containerWidth )
				{
					this.tabContentDiv.setStyle( {width: Element.getWidth( this.tabContentDiv ) -1} );
				}
			}
			*/
		},
		closeTab: function( /**event or tabId */obj )
		{
			var tabId;
			if( typeof( obj ) == "string" )
			{
				tabId = obj;
			}
			else
			{
				var src = Event.element( obj );
				//在IE上, 因為滑鼠點選時點的位子不同, 實際取得的element可能是<span>也可能是<span>裡的文字, 所以利用判斷何者有id來取得實際tabId.
				if( src.id )
					tabId = $(src).readAttribute("tabId");
				else
					tabId = $(src).up("div[class~='tabName']").readAttribute("tabId");

				Event.stop( obj );
			}
			var reselect = false;
			if( this.currentTab.readAttribute("tabId") == tabId )
			{
				reselect = true;
			}
			this._removeTab( tabId );
			//若原被選取之tab被關閉或已無其他tab時, 都要呼叫selectTab(), 以便選擇第一個或清除變數.
			if( reselect || this.tabNameDivArray.length == 0 )
			{
				this.selectTab( null );
			}
		},
		_removeTab: function( tabId )
		{
			var index = this._findIndexOfTabId( tabId );
			var tabNameDiv = this.tabNameDivArray[index];
			Event.stopObserving( tabNameDiv, "click", this.selectTabFunction );
			//在WebKit上tabNameDiv的select功能不知為何失效, 找不到任何children. 且無法直接使用'.closeImage'的方式來找element
			if( webKitBrowser )
				Event.stopObserving( tabNameDiv.children[0].down("a[class='closeImage']"),"click", this.closeTabFunction );
			else
				Event.stopObserving( tabNameDiv.down(".closeImage"),"click", this.closeTabFunction );
			this.tabNameDivArray.splice(index,1);
			DomUtils.destroyNode( tabNameDiv );
			var tabContent = this.tabContentPaneArray[index];
			tabContent.destroy();
			this.tabContentPaneArray.splice(index,1);
			this._resizeHolderAndContent();
			this.tabCounts--;
		},
		_createTab: function( args )
		{
			var id;
			var pane;
			var valid = true;
			if( args.tabContent )
			{
				//直接建立div當成tab.
				if( typeof( args.tabContent ) == "string" )
				{
					id = args.tabId;
					pane = new ContentPane( {parent: this.tabContentDiv, id: id, content: args.tabContent} );
				}
				//使用原widget物件當成tab.
				else if( args.tabContent instanceof ContentPane || args.tabContent instanceof TabContainer )
				{
					id = args.tabContent.getWidgetDomObject().id;
					pane = args.tabContent;
					this.tabContentDiv.appendChild( pane.getWidgetDomObject() );
				}
				else
				{
					id = args.tabId;
					pane = new ContentPane( {parent: this.tabContentDiv, id: id, content: args.tabContent, width: "100%", height: "100%"} );
				}
			}
			else if( args.tabContentUrl )
			{
				id = args.tabId;
				pane = new ContentPane( {parent: this.tabContentDiv, id: id, url: args.tabContentUrl, width: "100%", height: "100%"} );
			}
			else
			{
				showAlert( 'validate.param.required'.i18n('tabContent or tabContentUrl') );
				valid = false;
			}
			if( !id )
			{
				showAlert( 'validate.param.required'.i18n('tabId') );
				valid = false;
			}

			if( !valid && pane )
			{
				Widget.destroyWidget( pane );
				return null;
			}

			var paneDiv = pane.getWidgetDomObject();
			paneDiv.className = (paneDiv.className || "") + " tabContentWrapper";

			this._createTabName(id, args );
			this.tabContentPaneArray.push( pane );
			if( args.selected )
			{
				this.defaultTabId = id;
			}
			this.tabCounts++;
			return pane;
		},
		addTab: function( args )
		{
			var pane;
			if( args instanceof Widget )
				pane = this._createTab( {tabContent: args} );
			else
				pane = this._createTab( args );
			if( args.selected )
			{
				this.selectTab( this.defaultTabId );
			}
			else
			{
				//若此tab未指定要被選擇, 且目前尚無tab被選擇時, 就選擇第一個tab來顯示.
				if( !this.currentTab )
				{
					this.selectTab( null );
				}
				else
				{
					pane.hide();
				}
			}

			this._resizeHolderAndContent();
		},
		_parseChildWidgets: function()
		{
			var container = this;
			//var tags = this.widgetElement.childElements("div");
			var widgetDivs = this._findChildWidgetElements("div");
			//在parse child widget時, 順便記錄下最大的高度.
			var maxHeight = 0;
			var panes = new Array();
			$A(widgetDivs).each(
				function(div)
				{
					var h = Element.getHeight( div );
					if( h > maxHeight )
						maxHeight = h;
					//建立tab content
					panes.push( container._createTab(
						{
							tabId: div.id+"_tabId",
							tabContent: div,
							title: div.title || div.id,
							closable: eval(div.attributes['closable']),
							selected: eval(div.attributes['selected'])
						}
					) );
				}
			);
			this.maxContentHeight = maxHeight;
			//全部新增完後再做resize動作(不論有沒有child widget).
			this._resizeHolderAndContent();
			//若沒有child widget, 就無需執行以下動作.
			if( widgetDivs.length == 0 )
				return;
			//在各個pane尚未被隱藏前, 先將child widget建立, 其尺寸才會正確.
			$A(widgetDivs).each(
				function( div )
				{
					Widget.parseWidget( div );

				}
			);
			//先隱藏所有tab content.
			$A(panes).each(
				function( pane )
				{
					pane.hide();
				}
			);
			//選擇預選之tab.
			if( this.defaultTabId )
			{
				this.selectTab( this.defaultTabId );
			}
			else
			{
				this.selectTab( null );
			}
		},
		_selectTabEvent: function( event )
		{
			var obj = Event.element( event );
			//在IE上, 因為滑鼠點選時點的位子不同, 實際取得的element可能是<span>也可能是<span>裡的文字, 所以利用判斷何者有id來取得實際tabId.
			var tabId;
			if( obj.id )
				tabId = $(obj).readAttribute("tabId");
			else
				tabId = $(obj).up("div[class~='tabName']").readAttribute("tabId");
			this.selectTab( tabId );
		},
		_findIndexOfTabId: function( tabId )
		{
			var idx = 0;
			this.tabNameDivArray.each( function( tabName, index )
				{
					if( tabName.readAttribute("tabId") == tabId )
					{
						idx = index;
						return;
					}
				}
			);
			return idx;
		},
		/**
		 * 選擇要顯示的tabId, 若未指定tabId, 則顯示第一個tab.
		 */
		selectTab: function( tabId )
		{
			//若沒有tab, 則將目前使用之變數清除, 避免物件無法release.
			if( this.tabNameDivArray.length == 0 )
			{
				this.currentTab = null;
				this.currentTabContent = null;
				return;
			}

			var index = 0;
			if( tabId )
				index = this._findIndexOfTabId( tabId );
			if( index == -1 )
			{
				showAlert( 'widget.tab.notExist'.i18n( tabId ) );
				return;
			}
			if( this.currentTab )
				this.currentTab.className = "tabName";
			this.currentTab = this.tabNameDivArray[index];
			this.currentTab.className = "tabName currentTabName";
			if( this.currentTabContent )
			{
				this.currentTabContent.hide();
			}
			this.currentTabContent = this.tabContentPaneArray[index];
			this.currentTabContent.show();
		}
	}
);

TabContainer.parse = function( /*id(string) or div element*/obj, options )
{
	if( Widget.checkWidgetType( obj, "TabContainer") )
	{
		var tmpOptions =
		{
			id: obj,
			parent: $(obj).parentNode
		};
		Object.extend( tmpOptions, options || {} );
		var tab = new TabContainer( tmpOptions );
		return tab;
	}
	return null;
}

/**
 * 用來做layout的container類別. 主要用來分割畫面.
 */
var LayoutContainer = Class.create();
LayoutContainer.prototype = Object.extend( new Widget(),
	{
		initialize: function( options )
		{
			this.setOptions( options );

			this.widgetElement = null;
			//layout方式.
			this.layoutStyle = "top-bottom";
			//合法的layout方式
			this.validLayoutStyle = "top-bottom left-right";
			//不同panel間sizer的間距
			this.splitSize = 0;
			//是否開啟調整大小功能.
			this.activeSizing = false;
			//儲存此widget下所有widget.
			this.childWidgets = null;
			//儲存此widget下要做layout的widget.
			this.layoutWidgets = null;
			//儲存此container設定要做顯示/隱藏功能的layout position.
			this.togglePosition = null;
			//當指定的layout position被隱藏時, 用來儲存隱藏前的offset(寬或高), 以便後續能回覆原本的寬或高.
			this.toggleOffset = null;
			//當會被收合的區塊的寬(高)小於此值時, 會自動視為已關閉, 以避免因縮放及開關動作交互使用時產生狀態不正確的情況.
			this.toggleSwitchValue = 5;
			//目前可被收合之區塊是否為開啟.
			this.toggleOpened = true;
			//用來控制開關的控制物件.
			this.toggleCtrl = null;
			//用來儲存拖拉用的div物件.
			this.sizerDiv = null;
			//判斷sizerDiv是否被按下, 以便處理拖拉的動作.
			this.sizerDivClicked = false;
			//在拖拉時用來遮避event的div物件., 避免container中其他inner frame中的document影響到event的接收.
			this.sizerPanelDiv = null;

			this.destroyFunction = this.destroy.bindAsEventListener( this );
			this.onResizeFunction = this._onResize.bindAsEventListener( this );
			this.startSizingFunction = this._startSizing.bindAsEventListener( this );
			this.stopSizingFunction = this._stopSizing.bindAsEventListener( this );
			this.sizingFunction = this._sizing.bindAsEventListener( this );
			this.toggleFunction = this.toggle.bindAsEventListener( this );

			this._createContainerDiv();
			Widget.addWidget( this );
		},
		setOptions: function( options )
		{
			//預設的options參數, 可以透過setOptions來更改預設值.
			this.options =
			{
				id: null,
				onComplete: function() {}
			};
			Object.extend( this.options, options || {} );
		},
		resizeTo: function( width, height )
		{
			var bp = StyleUtils.borderPaddingOffset( this.widgetElement );
			if( width )
				this.widgetElement.style.width = width - bp.width;
			if( height )
				this.widgetElement.style.height = height - bp.height;
			this._resizeLayoutWidgets();
		},
		toggle: function( event )
		{
			if( !this.activeSizing )
			{
				showAlert( 'widget.layout.activeSizing'.i18n() );
				return;
			}
			if( !this.togglePosition )
			{
				showAlert( 'widget.layout.togglePosition'.i18n() );
				return;
			}
			switch( this.layoutStyle )
			{
				case "top-bottom":
					this._toggleTopBottom();
					break;
				case "left-right":
					this._toggleLeftRight();
					break;
			}
			Event.stop( event );
		},
		_toggleTopBottom: function()
		{
			var topDiv, bottomDiv, height, topHeight;
			if( this.togglePosition == 'top' )
			{
				topDiv = this.layoutWidgets[0].getWidgetDomObject();
				bottomDiv = this.layoutWidgets[1].getWidgetDomObject();
				//因為取得之height會包含border, padding等, 所以實際高度被設定為0時, 其仍具有高度, 所以必須記錄該高度, 以做為是否被隱藏之依據
				height = Element.getHeight( topDiv );
				if( this.toggleOpened )
				{
					topDiv.style.height = 0;
					this.layoutWidgets[1].resizeTo( null, Element.getHeight( bottomDiv ) + height );
					this.sizerDiv.style.top = 0;
					bottomDiv.style.top = this.splitSize;
					this.toggleOffset = height;
					this._toggleStatus();
				}
				else
				{
					//若要打開時, offset太小, 就指定預設值.
					if( this.toggleOffset < this.toggleSwitchValue )
						this.toggleOffset = 100;
					topDiv.style.height = this.toggleOffset;
					this.layoutWidgets[1].resizeTo( null, Element.getHeight( bottomDiv ) - this.toggleOffset );
					this.sizerDiv.style.top = this.toggleOffset;
					bottomDiv.style.top = this.toggleOffset + this.splitSize;
					this.toggleOffset = 0;
					this._toggleStatus();
				}
			}
			else //if( this.togglePosition == 'bottom' )
			{
				topDiv = this.layoutWidgets[0].getWidgetDomObject();
				bottomDiv = this.layoutWidgets[1].getWidgetDomObject();
				var containerHeight = Element.getHeight( this.widgetElement );
				height = Element.getHeight( bottomDiv );
				var sizerHeight = Element.getHeight( this.sizerDiv );
				if( this.toggleOpened )
				{
					this.layoutWidgets[0].resizeTo( null, containerHeight-sizerHeight );
					bottomDiv.style.height = 0;
					topHeight = Element.getHeight( topDiv );
					this.sizerDiv.style.top = topHeight;
					bottomDiv.style.top = containerHeight;
					this.toggleOffset = height;
					this._toggleStatus();
				}
				else
				{
					//若要打開時, offset太小, 就指定預設值.
					if( this.toggleOffset < this.toggleSwitchValue )
						this.toggleOffset = 100;
					this.layoutWidgets[0].resizeTo( null, containerHeight - this.toggleOffset - sizerHeight );
					topHeight = Element.getHeight( topDiv );
					this.sizerDiv.style.top = topHeight
					bottomDiv.style.top = topHeight + sizerHeight;
					this.layoutWidgets[1].resizeTo( null, containerHeight - topHeight - sizerHeight );
					this.toggleOffset = 0;
					this._toggleStatus();
				}
			}
		},
		_toggleLeftRight: function()
		{
			var leftDiv, rightDiv, width, sizerWidth;
			if( this.togglePosition == 'left' )
			{
				leftDiv = this.layoutWidgets[0].getWidgetDomObject();
				rightDiv = this.layoutWidgets[1].getWidgetDomObject();
				//因為取得之width會包含border, padding等, 所以實際寬度被設定為0時, 其仍具有寬度, 所以必須記錄該寬度, 以做為是否被隱藏之依據
				width = Element.getWidth( leftDiv );
				sizerWidth = Element.getWidth( this.sizerDiv );
				if( this.toggleOpened )
				{
					leftDiv.style.width = 0;
					//EdsLog.debug( this.widgetElement.id + ":" + Element.getWidth( this.widgetElement ) + ":" + sizerWidth );
					this.layoutWidgets[1].resizeTo( Element.getWidth( this.widgetElement ) - sizerWidth, null );
					this.sizerDiv.style.left = 0;
					rightDiv.style.left = sizerWidth;
					this.toggleOffset = width;
					this._toggleStatus();
				}
				else
				{
					//若要打開時, offset太小, 就指定預設值.
					if( this.toggleOffset < this.toggleSwitchValue )
						this.toggleOffset = 100;
					leftDiv.style.width = this.toggleOffset;
					this.layoutWidgets[1].resizeTo( Element.getWidth( this.widgetElement ) - this.toggleOffset - sizerWidth, null );
					this.sizerDiv.style.left = this.toggleOffset;
					rightDiv.style.left = this.toggleOffset + sizerWidth;
					this.toggleOffset = 0;
					this._toggleStatus();
				}
			}
			else //if( this.togglePosition == 'right' )
			{
				leftDiv = this.layoutWidgets[0].getWidgetDomObject();
				rightDiv = this.layoutWidgets[1].getWidgetDomObject();
				width = Element.getWidth( rightDiv );
				sizerWidth = Element.getWidth( this.sizerDiv );
				var containerWidth = Element.getWidth( this.widgetElement );
				if( this.toggleOpened )
				{
					this.layoutWidgets[0].resizeTo( containerWidth - sizerWidth, null );
					//不使用resize的方式, 加快速度.
					rightDiv.style.width = 0;
					this.sizerDiv.style.left = containerWidth - sizerWidth;
					rightDiv.style.left = containerWidth;
					this.toggleOffset = width;
					this._toggleStatus();
				}
				else
				{
					//若要打開時, offset太小, 就指定預設值.
					if( this.toggleOffset < this.toggleSwitchValue )
						this.toggleOffset = 100;
					this.layoutWidgets[0].resizeTo( containerWidth - this.toggleOffset, null );
					var leftWidth = Element.getWidth( leftDiv );
					this.sizerDiv.style.left = leftWidth;
					rightDiv.style.left = leftWidth + sizerWidth;
					//使用resize, 尺寸才會正確.
					this.layoutWidgets[1].resizeTo( containerWidth - leftWidth - sizerWidth, null );
					this.toggleOffset = 0;
					this._toggleStatus();
				}
			}
		},
		/**
         * 將目前toggleOpened的狀態反向, 並修改對應的圖示.
         */
		_toggleStatus: function()
		{
			if( this.toggleOpened )
			{
				switch( this.togglePosition )
				{
					case 'left':
						this.toggleCtrl.setAttribute("src","images/bullet_right.gif");
						break;
					case 'right':
						this.toggleCtrl.setAttribute("src","images/bullet_left.gif");
						break;
					case 'top':
						this.toggleCtrl.setAttribute("src","images/bullet_down.gif");
						break;
					case 'bottom':
						this.toggleCtrl.setAttribute("src","images/bullet_up.gif");
						break;
                }
            }
			else
			{
				switch( this.togglePosition )
				{
					case 'left':
						this.toggleCtrl.setAttribute("src","images/bullet_left.gif");
						break;
					case 'right':
						this.toggleCtrl.setAttribute("src","images/bullet_right.gif");
						break;
					case 'top':
						this.toggleCtrl.setAttribute("src","images/bullet_up.gif");
						break;
					case 'bottom':
						this.toggleCtrl.setAttribute("src","images/bullet_down.gif");
						break;
                }
            }
			this.toggleOpened = !this.toggleOpened;

        },
		_resizeLayoutWidgets: function()
		{
			var dim = Element.getDimensions(this.widgetElement);
			var containerWidth = dim.width;
			var containerHeight = dim.height;
			switch( this.layoutStyle )
			{
				case "top-bottom":
					var sizerHeight = 0
					if( this.sizerDiv )
					{
						this.sizerDiv.style.width = containerWidth;
						sizerHeight = Element.getHeight( this.sizerDiv);
					}
					this.layoutWidgets[0].resizeTo( containerWidth, null );
					this.layoutWidgets[1].resizeTo( containerWidth, containerHeight - Element.getHeight( this.layoutWidgets[0].getWidgetDomObject() ) - sizerHeight );
					break;
				case "left-right":
					var sizerWidth = 0
					if( this.sizerDiv )
					{
						this.sizerDiv.style.height = containerHeight;
						sizerWidth = Element.getWidth( this.sizerDiv);
					}
					this.layoutWidgets[0].resizeTo( null, containerHeight );

					this.layoutWidgets[1].resizeTo( containerWidth - Element.getWidth( this.layoutWidgets[0].getWidgetDomObject() ) - sizerWidth , containerHeight );
					break;
			}
		},
		_onResize: function( event )
		{
			this._resizeLayoutWidgets();
		},
		_checkLayoutStyle: function( layoutStyle )
		{
			if(this.validLayoutStyle.indexOf( layoutStyle ) == -1 )
			{
				showAlert( 'widget.layout.notValid'.i18n( layoutStyle, this.validLayoutStyle ) );
				return false;
			}
			return true;
		},
		_createContainerDiv: function()
		{
			if( this.options.id )
			{
				this.widgetElement = $(this.options.id);
			}
			else
			{
				showAlert( 'validate.param.required'.i18n('id') );
				return;
			}
			this.widgetElement.className = (this.widgetElement.className || "") + " layoutContainer";

			var attr = this.widgetElement.attributes['layoutstyle'];
			if( attr )
			{
				if( this._checkLayoutStyle( attr.nodeValue ) )
					this.layoutStyle = attr.nodeValue;
				else
					return;
			}

			attr = this.widgetElement.attributes['activesizing'];
			if( attr )
				this.activeSizing = eval(attr.nodeValue);

			var needSizerDiv = false;
			var needSizerCtrl = false;
			if( this.activeSizing )
			{
				needSizerDiv = true;
				needSizerCtrl = true;
				attr = this.widgetElement.attributes['toggleposition'];
				if( attr )
				{
					this.togglePosition = attr.nodeValue;
					if( this.layoutStyle.indexOf( this.togglePosition ) == -1 )
					{
						showAlert( 'widget.layout.notValidToggle'.i18n( this.togglePosition ) );
						return;
					}
				}
				this.sizerPanelDiv = document.createElement("div");
				this.sizerPanelDiv.className = "layoutSizerPanel";
				this.widgetElement.appendChild( this.sizerPanelDiv );
			}
			attr = this.widgetElement.attributes['splitsize'];
			if( attr )
			{
				needSizerDiv = true;
				this.splitSize = eval(attr.nodeValue);
			}

			if( needSizerDiv )
			{
				this.sizerDiv = new Element('div');
				this.widgetElement.appendChild(this.sizerDiv);
				if( this.layoutStyle == 'top-bottom' )
				{
					if( needSizerCtrl )
					{
						if( this.togglePosition == 'top' )
							this.toggleCtrl = new Element("img", {src : 'images/bullet_up.gif'} );
						else
							this.toggleCtrl = new Element("img", {src : 'images/bullet_down.gif'} );
						this.sizerDiv.appendChild( this.toggleCtrl );
						//var height = Element.getHeight( this.toggleCtrl );
						if( !this.splitSize || this.splitSize < 6 )
							this.splitSize = 6;
					}
					this.sizerDiv.setStyle( {height: this.splitSize} );
				}
				else
				{
					if( needSizerCtrl )
					{
						if( this.togglePosition == 'left' )
							this.toggleCtrl = new Element("img", {src : 'images/bullet_left.gif'} );
						else
							this.toggleCtrl = new Element("img", {src : 'images/bullet_right.gif'} );
						this.sizerDiv.appendChild( this.toggleCtrl );
						//var width = Element.getWidth( this.toggleCtrl );
						if( !this.splitSize || this.splitSize < 6 )
							this.splitSize = 6;
					}
					this.sizerDiv.setStyle( {width: this.splitSize} );
				}
			}

			this.layoutElements = this._findLayoutElements();
			//先將layout尺寸設定好
			this._constructLayout();
			//再建立child widget.
			this.layoutWidgets = new Array();
			for( var i=0; i<this.layoutElements.length; i++ )
			{
				var div = this.layoutElements[i];
				if( div.attributes['widgettype'] )
				{
					var widget  = Widget.parseWidget( div );
					this.layoutWidgets.push( widget );
				}
				else
					this.layoutWidgets.push( new ContentPane( {parent: this.widgetElement, id: this.widgetElement.id+"_c"+i, content: div} ) );

			}

			Event.observe( window, "resize", this.onResizeFunction );
			Event.observe( window, "unload", this.destroyFunction );
			if( this.activeSizing )
			{
				Event.observe( this.toggleCtrl, "click", this.toggleFunction );
				Event.observe( this.sizerDiv, "mousedown", this.startSizingFunction );
				Event.observe( document, "mouseup", this.stopSizingFunction );
				Event.observe( document, "mousemove", this.sizingFunction );
				//必須使用window來接受blur event, 使用docuemnt時, 在IE切換畫面並不會引發blur event.
				//TODO: 在IE上, 若設定blur event handler, 在一開始就按sizerDiv時, 會立即引發blur event, 造成無法拖拉, 但若先按畫面其他地方, 則不會出錯.
                //所以暫時先mark起來.
				//if( ieBrowser )
				//	Event.observe( window, "blur", this.stopSizingFunction );
			}
		},
		_findLayoutElements: function()
		{
			var first;
			var second;
			var firstValue;
			var secondValue;
			switch( this.layoutStyle )
			{
				case "top-bottom":
					firstValue = "top";
					secondValue = "bottom";
					break;
				case "left-right":
					firstValue = "left";
					secondValue = "right";
					break;
			}

			var elements = new Array();
			var others = new Array();
			//取得第一層element中的div element.
			var childDivs = this.widgetElement.childElements("div");
			$A(childDivs).each(
				function( div )
				{
					var attr = div.attributes['layoutalign'];
					if( attr )
					{
						if( attr.nodeValue == firstValue )
						{
							if( first )
							{
								showAlert( 'widget.layout.align.duplicate'.i18n( firstValue ) );
								return;
							}
							first = div;
						}
						else if( attr.nodeValue == secondValue )
						{
							if( second )
							{
								showAlert( 'widget.layout.align.duplicate'.i18n( secondValue ) );
								return;
							}
							second = div;
						}
						else
						{
							showAlert( 'widget.layout.align.error'.i18n( attr.nodeValue, firstValue +" or " + secondValue ) );
							return;
						}
					}
					else
					{
						others.push( div );
					}
				}
			);
			//若使用者未指定layoutalign, 則由出現的先後順序指定之.
			if( !first )
				first = others.shift();
			if( !second )
				second = others.shift();
			elements.push( first );
			elements.push( second );
			return elements;
		},
		_constructLayout: function()
		{
			switch( this.layoutStyle )
			{
				case "top-bottom":
					this._constructTopBottom();
					break;
				case "left-right":
					this._constructLeftRight();
					break;
			}
		},
		_constructTopBottom: function()
		{
			var topDiv = this.layoutElements[0];
			var bottomDiv = this.layoutElements[1];

			topDiv.setStyle({position: "absolute", top: 0, left: 0});
			bottomDiv.setStyle({position: "absolute", left: 0});
			//取得container實際可用的高度及寬度
			var containerHeight = this.widgetElement.clientHeight;
			var containerWidth = this.widgetElement.clientWidth;
			var sizerHeight = 0;
			//由於top-bottom的layout裡, top, bottom都是使用relative, 只有sizer是用absolute, 所以其位置及高度的計算方式與left-right不同.
			if( this.sizerDiv )
			{
				this.sizerDiv.className = "layoutSizerV";
				this.sizerDiv.setStyle({position: "absolute", height: this.splitSize, left: 0, width: containerWidth});

				//取得sizer實際佔用之高度.
				sizerHeight = Element.getHeight( this.sizerDiv );
				//可用的高度需扣掉sizer實際所佔之高度.
				containerHeight = containerHeight - sizerHeight;
			}

			//先計算上方佔據之大小
			var topHeight;
			if( topDiv.attributes['sizeshare'] || bottomDiv.attributes['sizeshare'] )
			{
				if( topDiv.attributes['sizeshare'] && bottomDiv.attributes['sizeshare'] )
				{
					var topSize = new Number( topDiv.attributes['sizeshare'].nodeValue );
					var bottomSize = new Number( bottomDiv.attributes['sizeshare'].nodeValue );
					topHeight = Math.floor( containerHeight * (topSize/(topSize+bottomSize)) );
				}
				else if( topDiv.attributes['sizeshare'] )
				{
					topHeight = new Number( topDiv.attributes['sizeshare'].nodeValue );
				}
				else
				{
					topHeight = containerHeight - new Number( bottomDiv.attributes['sizeshare'].nodeValue );
				}
			}
			else
			{
				topHeight = Element.getHeight( topDiv );
			}

			topDiv.setStyle( {width: containerWidth, height: topHeight} );
			//取得上方的實際高度, 以便計算位置.
			var topPos = Element.getHeight( topDiv );
			if( this.sizerDiv )
			{
				this.sizerDiv.style.top = topPos;
			}
			//注意, 此處無需再扣除sizerHeight, 因為在前面已先扣除.
			bottomDiv.setStyle( {width: containerWidth, height: containerHeight - topPos, top: topPos + sizerHeight} );
		},
		_constructLeftRight: function()
		{
			var leftDiv = this.layoutElements[0];
			var rightDiv = this.layoutElements[1];

			leftDiv.setStyle({position: "absolute", top: 0, left: 0});
			leftDiv.setStyle({position: "absolute", top: 0});
			//取得container實際可用的高度及寬度
			var containerHeight = this.widgetElement.clientHeight;
			var containerWidth = this.widgetElement.clientWidth;
			var sizerWidth = 0;
			if( this.sizerDiv )
			{
				this.sizerDiv.className = "layoutSizerH";
				this.sizerDiv.setStyle({position: "absolute", width: this.splitSize, top: 0, height: containerHeight});
				//取得sizer實際佔用之寬度.
				sizerWidth = Element.getWidth( this.sizerDiv );
				//可用的寬度需扣掉sizer實際所佔之寬度.
				containerWidth = containerWidth - sizerWidth;
			}

			//先計算左邊佔據之大小
			var leftWidth;
			if( leftDiv.attributes['sizeshare'] || rightDiv.attributes['sizeshare'] )
			{
				if( leftDiv.attributes['sizeshare'] && rightDiv.attributes['sizeshare'] )
				{
					var leftSize = new Number( leftDiv.attributes['sizeshare'].nodeValue );
					var rightSize = new Number( rightDiv.attributes['sizeshare'].nodeValue );
					leftWidth = Math.floor( containerWidth * (leftSize/(leftSize+rightSize)) );
				}
				else if( leftDiv.attributes['sizeshare'] )
				{
					leftWidth = new Number( leftDiv.attributes['sizeshare'].nodeValue );
				}
				else
				{
					leftWidth = containerWidth - new Number( rightDiv.attributes['sizeshare'].nodeValue );
				}
			}
			else
			{
				leftWidth = leftDiv.clientWidth;
			}
			var leftBP = StyleUtils.borderPaddingOffset( leftDiv );
			leftDiv.setStyle( {width: leftWidth - leftBP.width, height: containerHeight- leftBP.height} );
			//取得左邊的實際寬度, 以便計算位置.
			var leftPos = Element.getWidth( leftDiv );
			if( this.sizerDiv )
			{
				this.sizerDiv.setStyle( {left: leftPos} );
			}
			var rightBP = StyleUtils.borderPaddingOffset( rightDiv );
			//注意, 寬度無需再扣除sizerWidth, 因為在前面已先扣除.
			rightDiv.setStyle( {width: containerWidth - leftPos - rightBP.width, height: containerHeight - rightBP.height, left: leftPos + sizerWidth} );
		},
		_startSizing: function( event )
		{
			//避開使用者按住toggleCtrl的情況
			if( Event.element( event ) != this.toggleCtrl )
			{
				this.sizerDivClicked = true;
				//將sizerPanel顯示出來, 避免event被inner frame的docuemnt截走.
				this.sizerPanelDiv.style.display = "block";

				this._originalPosition = Position.cumulativeOffset(this.sizerDiv);
				this._clickX = Event.pointerX( event );
				this._clickY = Event.pointerY( event );
            }
		},
		_stopSizing: function( event )
		{
			if( this.sizerDivClicked )
			{
				this.sizerDivClicked = false;
				this.sizerPanelDiv.style.display = "none";
				this._clearSizingEvent();
            }
		},
		_sizing: function( event )
		{
			if( this.sizerDivClicked )
			{
				switch( this.layoutStyle )
				{
					case "top-bottom" :
						this._sizingTopBottom( event );
						break;
					case "left-right" :
						this._sizingLeftRight( event );
						break;
				}
            }
		},
		_sizingLeftRight: function( event )
		{
			var x = Event.pointerX(event);
			var offsetX = x - this._clickX;
			var leftWidget = this.layoutWidgets[0];
			var rightWidget = this.layoutWidgets[1];
			var leftDiv = leftWidget.getWidgetDomObject();
			var rightDiv = rightWidget.getWidgetDomObject();
			this.sizerDiv.style.left = this._originalPosition[0] + offsetX;
			var sizerWidth = Element.getWidth( this.sizerDiv );
			//呼叫widget的resize, 以便各個widget可以處理resize的對應動作.
			leftWidget.resizeTo( Position.cumulativeOffset(this.sizerDiv)[0], null );
			var leftWidth = Element.getWidth( leftDiv );
			rightDiv.style.left = leftWidth + sizerWidth;
			//呼叫widget的resize, 以便各個widget可以處理resize的對應動作.
			rightWidget.resizeTo( Element.getWidth( this.widgetElement ) - leftWidth - sizerWidth, null );
			var width;
			if( this.togglePosition == 'left' )
				width = leftWidth;
			else
				width = Element.getWidth( rightDiv );
			if( (this.toggleOpened && width < this.toggleSwitchValue) || (!this.toggleOpened && width > this.toggleSwitchValue) )
			{
				this._toggleStatus();
			}
		},
		_sizingTopBottom: function( event )
		{
			var y = Event.pointerY(event);
			var offsetY = y - this._clickY;

			var topWidget = this.layoutWidgets[0];
			var bottomWidget = this.layoutWidgets[1];
			var topDiv = topWidget.getWidgetDomObject();
			var bottomDiv = bottomWidget.getWidgetDomObject();

			this.sizerDiv.style.top = this._originalPosition[1] + offsetY;
			//呼叫widget的resize, 以便各個widget可以處理resize的對應動作.
			topWidget.resizeTo( null, Position.cumulativeOffset(this.sizerDiv)[1] );
			var topHeight = Element.getHeight( topDiv );

			bottomDiv.style.top = topHeight + this.splitSize;
			//呼叫widget的resize, 以便各個widget可以處理resize的對應動作.
			bottomWidget.resizeTo( null, Element.getHeight( this.widgetElement ) - topHeight - this.splitSize );
			var heigth;
			if( this.togglePosition == 'top' )
				heigth = topHeight;
			else
				heigth = Element.getWidth( bottomDiv );
			if( (this.toggleOpened && heigth < this.toggleSwitchValue) || (!this.toggleOpened && heigth > this.toggleSwitchValue) )
			{
				this._toggleStatus();
			}
		},
		_clearSizingEvent: function()
		{
			//改寫不做清除.
		}
	}
);

LayoutContainer.parse = function( /*id(string) or widget element*/obj )
{
	if( Widget.checkWidgetType( obj, "LayoutContainer") )
	{
		return new LayoutContainer( {id: $(obj)} );
	}
	return null;
}

//*************************************************************
//	Dialogs object begin
//*************************************************************

var DialogFactory = Class.create();
DialogFactory.prototype =
{
	initialize: function()
	{
	},
	createDivDialog: function( options )
	{
		return new DivDialog(options);
	},
	createIFrameDialog: function(options)
	{
		return new IFrameDialog(options);
	}
}

var dialogFactory;
DialogFactory.getInstance = function()
{
	if( !dialogFactory )
	{
		dialogFactory = eval("new " + EdsProjConfig.dialogFactory + "()");
	}
	return dialogFactory;
}

/**
 * 自訂的dialog類別.
 */
var CustomDialog = Class.create();
CustomDialog.prototype =
{
	initialize: function()
	{
		this.reset();
	},
	/**
	 * 設定options參數, 若與原options參數相同者, 會以傳入之參數為主.
	 */
	setOptions: function( options )
	{
		//預設的options參數, 可以透過setOptions來更改預設值.
		this.options =
		{
			//本dialog的parent node.
			parent: document.body,
			//是否為modal dialog, 若為true, 則在dialog關閉前, 背景將無法動作.
			modalDialog: true,
			//背景色(modalDialog必須為true時才有效)
			bgColor: "white",
			//背景透明程度(modalDialog必須為true時才有效)
			opacity: 0.5,
			//是否顯示title.
			showTitle: true,
			//dialog的title.
			title: "Dialog",
			//dialog寬度
			width: "",
			//dialog高度
			height: "",
			//dialog要套用的style(使用json格式設定)
			dialogStyle: null,
			//是否要置中
			centralize: true,
			//是否要產生dialog的影子
			dialogShadow: true,
			//dialog的影子的offset(dialogShadow為true時才有效)
			dialogShadowOffset: 3,
			//dialog是否可以拖拉(若showTitle為true時才有效).
			draggable: true,
			//是否顯示關閉的按鈕.
			showCloseButton: true,
			//是否顯示關閉的連結(title), 若showTitle為true時才有效.
			showCloseControl: true,
			//在IE6以前, div元件無法蓋住下拉選單, 指定此參數可於使用dialog時, 自動隱藏下拉選單.
			hideSelectOnIE: preferenceConfig.hideSelectOnIE,
			//在建立dialog後會呼叫本函式, 可依據dialog需求自行定義(該函式會bind在dialog物件下執行).
			onComplete: function() {},
			//在dialog的後會呼叫本函式, 可依據dialog需求自行定義(該函式會bind在dialog物件下執行).
			afterDestroy: function() {}
		};
		Object.extend( this.options, options || {} );
	},
	reset: function()
	{
		//判斷dialog是否已打開
		this.opened = false;
		//背景的div
		this.backgroundDiv = null;
		//dialog的div
		this.dialogDiv = null;
		//dialog影子的div
		this.shadowDiv = null;
		//關閉控制項
		this.closeCtrlHandler = null;
		//關閉按鈕
		this.closeBtnHandler = null;
		//拖拉的handler物件.
		this.dragHandler = null;
		//目前是否處於拖拉狀態.
		this.dragging = false;
		this.options = {};
		this.onReset();
	},
	onReset: function()
	{
	},
	/**
	 * 清除所有使用中的物件.
	 * 注意, 為避免memory leak, 所有指向html element的handler均應全部清除. 有用到的變數, 也應將其清為預設值或null.
	 */
	destroy: function()
	{
		if( this.backgroundDiv )
		{
			DomUtils.destroyNode( this.backgroundDiv );
		}
		if( this.dialogDiv )
		{
			this.clearDialogContent();
			DomUtils.destroyNode( this.dialogDiv );
		}
		if( this.shadowDiv )
		{
			DomUtils.destroyNode( this.shadowDiv );
		}
		this.onDestroy();
		this.reset();
	},
	onDestroy: function()
	{
	},
	_adjustBackgroundDimension: function()
	{
		if( this.backgroundDiv )
		{
			this.backgroundDiv.style.width = Math.max( this.options.parent.scrollWidth, this.options.parent.clientWidth );
			this.backgroundDiv.style.height = Math.max( this.options.parent.scrollHeight, this.options.parent.clientHeight );
        }
	},
	_adjustDialogDimension: function()
	{
		var diaWidth = Element.getWidth( this.dialogDiv );
		var tableWidth = Element.getWidth( $('dialogTable') );
		if( diaWidth < tableWidth )
		{
			var parWidth = Element.getWidth( this.options.parent ) - 20;
			if( tableWidth > parWidth )
			{
				this.dialogDiv.style.width = parWidth;
				this.dialogDiv.style.overflowX = "auto";
			}
			else
			{
				this.dialogDiv.style.width = tableWidth;
			}
		}
		var diaHeight = Element.getHeight( this.dialogDiv );
		var tableHeight = Element.getHeight( $('dialogTable') );
		if( diaHeight < tableHeight )
		{
			var parHeight = Element.getHeight( this.options.parent ) - 20;
			if( tableHeight > parHeight )
			{
				this.dialogDiv.style.height = parHeight;
				this.dialogDiv.style.overflowY = "auto";
			}
			else
			{
				this.dialogDiv.style.height = tableHeight;
			}
		}
	},
	_createBackgroundLayer: function()
	{
		this.backgroundDiv = new Element("div", {'style' : 'position: absolute; left: 0; top: 0; z-index: 997; background-color:'+ this.options.bgColor});
		this._adjustBackgroundDimension();
		this.backgroundDiv.setOpacity( this.options.opacity );
		this.options.parent.appendChild( this.backgroundDiv );
	},
	_centerize: function()
	{
		var dialogDim = Element.getDimensions( this.dialogDiv );
		var parentDim = Element.getDimensions( this.options.parent );
		var scrollOffset = Element.cumulativeScrollOffset( this.options.parent );
		var x = ((parentDim.width-dialogDim.width)/2) + scrollOffset.left;
		var y = ((parentDim.height-dialogDim.height)/2) + scrollOffset.top;
		if( x < 1 )
			x = 1;
		if( y < 1 )
			y = 1;
		this._moveTo( x, y );
	},
	_createDialogDiv: function()
	{
		this.dialogDiv = new Element("div", {'id' : this.options.id, 'style' : 'position: absolute; z-index: 999; visibility: hidden'});
		if( this.options.width != "" )
			this.dialogDiv.style.width = this.options.width;
		if( this.options.height != "" )
			this.dialogDiv.style.height = this.options.height

		var html = '<table cellpadding="4" cellspacing="0" id="dialogTable">';
		var colSpan = 1;
		if( this.options.showTitle )
		{
			html = html + '<tr><th id="dragHandler" style="width: 100%" class="alignLeft">' + this.options.title + '</th>';
			if( this.options.showCloseControl )
			{
				html = html + '<th class="alignRight" style="width:16"><a href="#" id="dialogCloseCtrl">&nbsp;&nbsp;&nbsp;&nbsp;</a></th>';
				colSpan = 2;
			}
			html = html + '</tr>';
		}
		html = html + '<tr><td class="alignCenter" colspan="' + colSpan + '"><div id="dialogContent" style="width: 100%; height: 100%; margin:0 auto;">';

		html = html + this.createDialogContent();

		html = html + '</div></td></tr>';
		if( this.options.showCloseButton )
			html = html + '<tr><td align="center" colspan="' + colSpan + '"><button id="dialogCloseBtn" class="button">'+ 'label.confirm'.i18n() +'</button></td></tr>';
		html = html + '</table>';
		this.dialogDiv.innerHTML = html;
		this.dialogDiv.style.visibility = "hidden";
		this.options.parent.appendChild( this.dialogDiv );
		if( this.options.dialogStyle )
			$(this.dialogDiv).setStyle( this.options.dialogStyle );
		this._adjustDialogDimension();
		if( this.options.centralize )
			this._centerize();
		this.dialogDiv.style.visibility = "visible";

		if( this.options.showTitle && this.options.showCloseControl )
		{
			this.closeCtrlHandler = $('dialogCloseCtrl');
			Event.observe( this.closeCtrlHandler, "click", this.closeFunction );
		}
		if( this.options.showCloseButton )
		{
			this.closeBtnHandler = $('dialogCloseBtn');
			Event.observe( this.closeBtnHandler, "click", this.closeFunction );
		}
		if( this.options.showTitle && this.options.draggable )
		{
			this.dragHandler = $('dragHandler');
			this.dragHandler.className = (this.dragHandler.className || "") + " draggable";
			this.startDraggingFunction = this._startDragging.bindAsEventListener( this );
			Event.observe( this.dragHandler, "mousedown", this.startDraggingFunction );
		}
		this.afterCreateDialogDiv();
	},
	//for extention
	afterCreateDialogDiv: function()
	{
	},
	_moveTo: function( x, y )
	{
		this.dialogDiv.setStyle( {left : x, top : y} );
		if( this.options.dialogShadow && this.shadowDiv )
		{
			this._positionDialogShadow();
		}
	},
	_createDialogShadow: function()
	{
		this.shadowDiv = new Element("div", {'style' : 'position: absolute; z-index: 998; background-color: #666666'} );
		this._positionDialogShadow();
		this.options.parent.appendChild( this.shadowDiv );
	},
	_positionDialogShadow: function()
	{
		if( this.shadowDiv )
		{
			this.shadowDiv.clonePosition( this.dialogDiv, {offsetLeft : this.options.dialogShadowOffset, offsetTop : this.options.dialogShadowOffset});
        }
	},
	_onResize: function()
	{
		if( this.opened )
		{
			this._adjustBackgroundDimension();
			//this._adjustDimension();
			this._centerize();
			this._positionDialogShadow();
		}
	},
	_startDragging: function( event )
	{
		this._originalPosition = Position.cumulativeOffset(this.dialogDiv);
		this._clickX = Event.pointerX( event );
		this._clickY = Event.pointerY( event );
		//確保使用同一個function, 在stop時才能正確停止event.
		if( !this.draggingFunction )
			this.draggingFunction = this._dragging.bindAsEventListener( this );
		if( !this.stopDraggingFunction )
			this.stopDraggingFunction = this._stopDragging.bindAsEventListener( this );
		Event.observe( document, "mousemove", this.draggingFunction );
		Event.observe( document, "mouseup", this.stopDraggingFunction );
		//必須使用window來接受blur event, 使用docuemnt時, 在IE切換畫面並不會引發blur event.
		Event.observe( window, "blur", this.stopDraggingFunction );
		this.dragging = true;
		this.onStartDragging( event );
	},
	/**
	 * 當開始drag時會呼叫本函式, 各子類別可自行改寫, 提供必要之實作.
	 */
	onStartDragging: function( event )
	{
	},
	_dragging: function( event )
	{
		var x = Event.pointerX(event);
		var y = Event.pointerY(event);
		var offsetX = x - this._clickX;
		var offsetY = y - this._clickY;
		this._moveTo( this._originalPosition[0] + offsetX, this._originalPosition[1] + offsetY );
		this.onDragging( event );
	},
	/**
	 * 在Dragging時會呼叫本函式, 各子類別可自行改寫, 提供必要之實作.
	 */
	onDragging: function( event )
	{
	},
	_stopDragging: function( event )
	{
		this.dragging = false;
		this._clearDraggingEvent();
		this.onStopDragging( event );
	},
	_clearDraggingEvent: function()
	{
		if( this.options.showTitle && this.options.draggable )
		{
			if( this.draggingFunction )
				Event.stopObserving( document, "mousemove", this.draggingFunction );
			if( this.stopDraggingFunction )
			{
				Event.stopObserving( document, "mouseup", this.stopDraggingFunction );
				Event.stopObserving( window, "blur", this.stopDraggingFunction );
            }
			this.draggingFunction = null;
			this.stopDraggingFunction = null;
		}
	},
	/**
	 * 在停止drag時會呼叫本函式, 各子類別可自行改寫, 提供必要之實作.
	 */
	onStopDragging: function( event )
	{
	},
	open: function()
	{
		if( !this.opened )
		{
			if( this.options.hideSelectOnIE )
				BrowserUtils.hideSelectOnIE();
			this.closeFunction = this.close.bindAsEventListener( this );
			if( this.options.modalDialog )
				this._createBackgroundLayer();
			this._createDialogDiv();
			//onComplete必須在畫shadow之前完成, 因為 onComplete中若改變了原dialog大小, shadow若先畫, 其尺寸會不正確.
			this.options.onComplete.bind( this )();
			if( this.options.dialogShadow )
				this._createDialogShadow();
			//因為background的大小與實際頁面最後顯示結果有關(若出現scroll, 其大小應該跟scroll大小一樣), 所以必須在完成所有動作後, 再呼叫_adjustBackgroundDimension
			if( this.options.modalDialog )
				this._adjustBackgroundDimension();
			this.onResizeFunction = this._onResize.bindAsEventListener( this );
			Event.observe( window, "resize", this.onResizeFunction );
			Event.observe( window, "unload", this.closeFunction );
			this.opened = true;
		}
	},
	_clearAllEvent: function()
	{
		if( this.closeCtrlHandler )
			Event.stopObserving( this.closeCtrlHandler, "click", this.closeFunction );
		if( this.closeBtnHandler )
			Event.stopObserving( this.closeBtnHandler, "click", this.closeFunction );
		if( this.dragHandler )
			Event.stopObserving( this.dragHandler, "mousedown", this.startDraggingFunction );
		if( this.closeFunction )
			Event.stopObserving( window, "unload", this.closeFunction );
		if( this.onResizeFunction )
			Event.stopObserving( window, "resize", this.onResizeFunction );
		this.startDraggingFunction = null;
		this.closeFunction = null;
		this.onResizeFunction = null;
		this._clearDraggingEvent();
	},
	close: function(event)
	{
		if( event )
			Event.stop(event);
		this.opened = false;
		this._clearAllEvent();
		this.onClose();
		var afterDestroy;
		if( this.options.afterDestroy )
			afterDestroy = this.options.afterDestroy;
		if( this.options.hideSelectOnIE )
			BrowserUtils.showSelectOnIE();
		this.destroy();
		if( afterDestroy )
			afterDestroy();
	},
	onClose: function()
	{
	},
	//for extention, used to clear custom created dialog content if needed.
	clearDialogContent: function()
	{
    }
}

var DivDialog = Class.create();
/**
 * 定義一個Div為內容的對話框.
 * 傳入之參數除了CustomDialog中定義的以外, 尚需以下參數:
 * divId: 定義div的ID, 整個頁面中必須唯一.
 * divContent: 要顯示在div中的內容.
 * divStyle: 定義div要套用的style(字串).
 */
DivDialog.prototype = Object.extend( new CustomDialog(),
	{
		initialize: function( options )
		{
			var _options = {id: 'divDialog', divId: 'divContent', title: 'Div Dialog', divStyle: null, dialogShadow: true};
			Object.extend( _options, options || {} );
			this.setOptions( _options );
		},
		createDialogContent: function()
		{
			return "<div id='"+ this.options.divId + "'>" + this.options.divContent + "</div>";
		},
		afterCreateDialogDiv: function()
		{
			if( this.options.divStyle )
			{
				$(this.options.divId).setStyle( this.options.divStyle );
			}
		}
	}
);

/**
 * 顯示輔助說明對話框.
 * 呼叫後, 可透過helpDialog操作該dialog.
 */
function showHelp( id )
{
	var obj;
	if( id )
	{
		if( !$(id) )
			showAlert( 'browser.id.notExist'.i18n( id ) );
		else
			obj = $(id);
	}
	else
	{
		if( !$('helpContent') )
			showAlert( 'dialog.id.notEixst'.i18n( 'helpContent' ) );
		else
			obj = $('helpContent');
	}
	if( obj )
	{
		options =
		{
			id: 'helpDialog',
			divId: 'helpDialogContent',
			divContent: '<fieldset><legend>'+ 'label.description'.i18n() +'</legend>' + obj.innerHTML + "</fieldset>",
			title: 'label.help'.i18n(),
			dialogShadow: true,
			onComplete: function()
			{
				Rico.Corner.round( $('helpDialogContent'),{color: 'transparent'} );
			}
		}
		DialogFactory.getInstance().createDivDialog( options ).open();
	}
}

/**
 * 顯示輔助說明對話框.
 * 呼叫後, 可透過informationDialog操作該dialog.
 */
function showInformation( args )
{
	if( !args.informationId )
	{
		showAlert( 'validate.param.required'.i18n('informationId') );
		return;
	}
	if( !args.title )
	{
		showAlert( 'validate.param.required'.i18n('title') );
		return;
	}
	options =
	{
		id: 'informationDialog',
		divId: 'informationDialogContent',
		divContent: '<fieldset><legend>'+ 'label.information'.i18n() +'</legend>' + $(args.informationId).innerHTML + "</fieldset>",
		title: args.title,
		dialogShadow: true,
		hideSelectOnIE: true,
		onComplete: function()
		{
			Rico.Corner.round( $('informationDialogContent'),{color: 'transparent'} );
		}
	}
	DialogFactory.getInstance().createDivDialog( options ).open();
}

//因為submitDialog沒有提供可關閉dialog的控制項, 所以必須使用變數來控制其關閉.
var submitDialog;
/**
 * 顯示submit的message dialog. 必須呼叫closeSubmitMessage()將其關閉.
 * 呼叫後, 可透過submitDialog操作該dialog.
 */
function showSubmitMessage()
{
	if( !submitDialog )
	{
		var options =
		{
			id: 'submitDialog',
			divId: 'submitDialogContent',
			divContent: '<img border="0" src="../images/indicator.gif"><span id="submitMessage">'+ 'label.submit.message'.i18n() +'</span><img border="0" src="../images/indicator.gif">',
			showTitle: false,
			dialogShadow: true,
			showCloseButton: false
		};
		submitDialog = DialogFactory.getInstance().createDivDialog( options );
		submitDialog.open();
	}
}

/**
 * 關閉submitDialog.
 */
function closeSubmitMessage()
{
	if( submitDialog )
	{
		submitDialog.close();
		submitDialog = null;
	}
}

/**
 * 顯示警示訊息.
 * 呼叫後, 可透過alertDialog操作該dialog.
 */
function showAlert( msg )
{
	var options =
	{
		id: 'alertDialog',
		divId: 'alertDialogContent',
		divContent: msg,
		title: 'label.alert'.i18n(),
		dialogShadow: true,
		hideSelectOnIE: true
	}
	DialogFactory.getInstance().createDivDialog( options ).open();
}

//因為loadingMsgBlock沒有提供可關閉dialog的控制項, 所以必須使用變數來控制其關閉.
var loadingMsgDialog;
/**
 * 顯示載入中的message dialog. 必須呼叫closeLoadingMessage()將其關閉.
 * 呼叫後, 可透過submitDialog操作該dialog.
 */
function showLoadingMessage()
{
	if( !loadingMsgDialog )
	{
		//預設使用fixed, 但因IE6之前版本不支援, 只能以absolute取代之.
		pos = "fixed";
		if( ieBrowser )
			pos = 'absolute';
		options =
		{
			divId: 'loadingMsgId',
			divContent: 'label.loading'.i18n(),
			modalDialog: false,
			showTitle: false,
			dialogStyle: {zIndex: '999', position: pos, right: '0', width: 100, top: '0', hegith: '20', backgroundColor: 'brown'},
			divStyle: {textAlign: 'center', fontWeight: 'bold', color: 'white'},
			centralize: false,
			dialogShadow: false,
			draggable: false,
			showCloseButton: false,
			showCloseControl: false
		}
		loadingMsgDialog = DialogFactory.getInstance().createDivDialog( options );
		loadingMsgDialog.open();
	}
}

/**
 * 關閉loadingMsgDialog.
 */
function closeLoadingMessage()
{
	if( loadingMsgDialog )
	{
		loadingMsgDialog.close();
		loadingMsgDialog = null;
	}
}

/**
 * 定義一個Tooltip的Dialog. 在建立物件時必須傳入以下參數
 * contentId : 用來指定畫面上已存在的element, Tooltip會以該element的內容為要顯示之內容(注意, 只有其內容, 並不包含該element).
 * observer : 設定要用來引發event的element.
 */
var TooltipDialog = Class.create();
TooltipDialog.prototype = Object.extend( new CustomDialog(),
	{
		initialize: function( options )
		{
			var _options =
			{
				id: options.contentId+"Tooltip",
				observer: null,
				showTitle: false,
				dialogShadow: false,
				modalDialog: false,
				centralize: false,
				showCloseButton: false
			}
			this.showed = false;
			Object.extend( _options, options || {} );
			this.setOptions( _options );
			$(this.options.observer).observe("mouseover", this.show.bindAsEventListener( this ) );
			$(this.options.observer).observe("mouseout", this.hide.bindAsEventListener( this ) );
		},
		createDialogContent: function()
		{
			if( this.options.observer == null )
			{
				showAlert( 'dialog.tooltip.observer.required'.i18n() );
				return "";
            }
			return $(this.options.contentId).innerHTML;
		},
		afterCreateDialogDiv: function()
		{
			if( !this.dialogDiv.hasClassName("tooltipDialog") )
				this.dialogDiv.addClassName("tooltipDialog");
		},
		show: function( event )
		{
			if( !this.showed )
			{
				if( !this.opened )
					this.open();
				else
				{
					if( this.options.hideSelectOnIE )
						BrowserUtils.hideSelectOnIE();
                }
				this.showed = true;
				this.dialogDiv.show();
				this._moveTo( event.pointerX()+3, event.pointerY()-this.dialogDiv.getHeight()-3 );
            }
        },
		hide: function( event )
		{
			if( this.showed )
			{
				if( this.options.hideSelectOnIE )
					BrowserUtils.showSelectOnIE();
				this.showed = false;
				this.dialogDiv.hide();
            }
        }
	}
);

var IFrameDialog = Class.create();
/**
 * 定義用來放置iframe的對話框. 除了CustomDialog定義之參數外, 要建立本dialog需要以下參數.
 * iframeId: 指定iframe之id及name.
 * 此外, 要開啟此dialog, 必須在建立後呼叫showUrl()或showForm(), 傳入要開啟之url或form.
 * TODO: 關閉IFrameDialog時會有memory leak(在iframe所指的頁面中)
 */
IFrameDialog.prototype = Object.extend( new CustomDialog(),
	{
		initialize: function( options )
		{
			var _options = {id: 'iframeDialog', title: 'IFrame', dialogShadow: true, showCloseButton: false, iframeId: 'iframe'};
			Object.extend( _options, options || {} );
			this.setOptions( _options );
			this.setIFrameDimension( _options );
		},
		onReset: function()
		{
			this.dialogContentCover = null;
			this.url = null;
			this._monitorMouseMoveFunction = null;
		},
		onDestroy: function()
		{
		},
		onClose: function()
		{
			iframeDialog = null;
			//在部份IE上當iframe dialog關閉時, input欄位的cursor常會不見也無法點入, 測試後發現只要focus停留在button上就會出現這種情況.
            //因此在關閉時直接設定foucus到其他的input欄位(要避開button, hidden及submit), 即可解決此問題
            //但在IE11時, 其userAgent使用的是Gecko的核心, 無法直接判讀是IE, 所以此處就判斷, 只要有form, 就一律做update
            if( document.forms && document.forms[0] )
            {
                            //$(document.forms[0]).insert("");
                            //var inputs = $(document.forms[0]).select("input:enabled[type!=button][type!=hidden][type!=submit],textarea:enabled,select:enabled");
                            var inputs = $(document.forms[0]).select("input[type!=button][type!=hidden][type!=submit] ,select,textarea ");
                            if( inputs && inputs.length > 0 )
                            {
                                            inputs[0].focus();
                            }
            }
		},
		onStartDragging: function( event )
		{
			if( !this._monitorMouseMoveFunction )
				this._monitorMouseMoveFunction = this._monitorMouseMove.bindAsEventListener( this );
			var dim = $('dialogContent').getDimensions();
			this.dialogContentCover.setStyle( {'width': dim.width, 'height': dim.height, 'display': ''} );
			Event.observe( this.dialogContentCover, "mousemove", this._monitorMouseMoveFunction );
			var iframePos = Position.cumulativeOffset($(this.options.iframeId));
			this._iframeOffsetX = Event.pointerX( event ) - iframePos[0];
			this._iframeOffsetY = Event.pointerY( event ) - iframePos[1];
		},
		onDragging: function( event )
		{
		},
		onStopDragging: function( event )
		{
			Event.stopObserving( this.dialogContentCover, "mousemove", this._monitorMouseMoveFunction );
			this.dialogContentCover.setStyle( {'width': '0px', 'height': '0px', 'display': 'none'} );
		},
		_monitorMouseMove: function( event )
		{
			var pos = Position.cumulativeOffset(this.dialogDiv);
			var x = pos[0] + Event.pointerX( event ) - this._iframeOffsetX;
			var y = pos[1] + Event.pointerY( event ) - this._iframeOffsetY;
			this._moveTo( x, y);
		},
		setIFrameDimension: function( args )
		{
			var width = EdsProjConfig.workspaceWidth;
			if( args && args.width )
			{
				//width = Number(args.width);
				//可接受百分比
				width = args.width;
			}
			/*
			//取得實際畫面上目前之寬跟高, 並依其值調整實際顯示的dialog大小, 避免過大而無法操作.
			var availWidth = this.options.parent.clientWidth;
			if( width+50 > availWidth )
				width = availWidth - 50;
			*/

			var height = EdsProjConfig.workspaceHeight;
			if( args && args.height )
			{
				//height = Number(args.height);
				//可接受百分比
				height = args.height;
			}
			/*
			var availHeight = this.options.parent.clientHeight;
			if( height+70 > availHeight )
				height = availHeight - 70;
			 */

			this.options.width = width;
			this.options.height = height;
		},
		createDialogContent: function()
		{
			if( this.url )
			{
				return '<iframe id="' + this.options.iframeId + '" name="' + this.options.iframeId + '" frameborder="0" src="' + this.url + '" scrolling="auto"></iframe>';
			}
			else
			{
				return '<iframe id="' + this.options.iframeId + '" name="' + this.options.iframeId + '" frameborder="0" scrolling="auto"></iframe>';
			}
		},
		afterCreateDialogDiv: function()
		{
			this.dialogContentCover = new Element("div", {'id': 'dialogContentCover'} );
			this.dialogDiv.insert( this.dialogContentCover, {position: 'bottom'} );

			this.dialogContentCover.setStyle( {'z-index': 999, 'position': 'absolute', 'top': $('dragHandler').getHeight(), 'left':'0px'} );
			/*
			//IE中只有contentWindow, FF中contentWindow及contentDocument都有, Safari則只有contentDocument, 所以必須加以處理, 取得iframe中的document.
			//this.contentDocument = this.iframe.contentWindow || this.iframe.contentDocument;
			this.contentDocument = iframe.contentWindow || iframe.contentDocument;
			if( this.contentDocument.document )
				this.contentDocument = this.contentDocument.document;
			*/
		},
		clearDialogContent: function()
		{
			var iframe = $(this.options.iframeId);
			if( iframe )
			{
				DomUtils.destroyNode( iframe );
            }
        },
		showUrl: function( url )
		{
			this.url = url;
			this.open();
		},
		showForm: function( form )
		{
			//先開啟iframe, 再將form的target指定為iframe之id.
			this.open();
			var oldTarget = form.target;
			form.target = this.options.iframeId;
			form.submit();
			//重設target的動作在chrome上會造成submit的動作送至oldTarget, 所以使用setTimeout, 在submit後隔一秒再重設
			setTimeout( function(){form.target = oldTarget;}, 1000 );
		}
	}
);

var iframeDialog;
/**
 * 使用dialog來包裝iframe, 並將要連結之頁面顯示在iframe中.
 * 呼叫後, 可透過iframeDialog操作該dialog.
 * url: 要開啟之連結.
 * options: IFrameDialog所需的參數.
 * event: 引發此動作的event, 若有傳入, 本函式最後會將該event中止.
 */
function showIFrameDialog( url, options, event )
{
	if( event )
		Event.stop( event );
	iframeDialog = DialogFactory.getInstance().createIFrameDialog( options );
	iframeDialog.showUrl( url );
}

/**
 * 使用dialog來包裝iframe, 並將傳入之form submit後的結果顯示在iframe中.
 * 呼叫後, 可透過iframeDialog操作該dialog.
 * form: 要submit之form.
 * options: IFrameDialog所需的參數.
 * event: 引發此動作的event, 若有傳入, 本函式最後會將該event中止.
 */
function submitAndShowIFrameDialog( form, options, event )
{
	if( event )
		Event.stop( event );
	iframeDialog = DialogFactory.getInstance().createIFrameDialog( options );
	iframeDialog.showForm( form );
}

/**
 * 用來當做event listener的function, 處理將form submit至IFrameDialog中之event.
 * 當成event listener時, 除了第一個參數是form以外, 可以傳入以下參數:
 * checkForm: 指定用來檢查要submit之form的資料正確性的function, 若該function傳回false, 就不會做submit的動作.
 * 其餘參數可直接參考IFrameDialog所需之參數.
 * 呼叫方式如:
 * $('form').observe("submit", iframeDialogSubmitHandler.bindAsEventListener(form,{checkForm: validateForm, title: '作業名稱'}) );
 * 注意, 使用時必須將要做submit的form當成其event objejct, 如上例所示.
 */
function iframeDialogSubmitHandler( event, args )
{
	Event.stop( event );
	if( args && args.checkForm )
	{
		if( !args.checkForm( this ) )
			return;
    }
	submitAndShowIFrameDialog( this, args );
}

/**
 * 將傳入之form物件的action換成新的action後submit, 顯示在IFrameDialog中, 並於submit後將form的action再回覆成原值.
 * 此外, 由於submit動作是由javascript處理, 所以在執行後會將原event中止, 避免該form又做一次submit.
 * options: IFrameDialog所需的參數.
 * form: 要submit之form.
 * args: 可傳遞以下參數:
 *		newAction (required): url, 指向新的action.
 * event: 引發此動作的event, 若有傳入, 本函式最後會將該event中止.
 */
function changeActionAndShowIFrameDialog( form, options, args, event )
{
	//先中止event.
	if( event )
		Event.stop( event );
	var origAction = form.action;
	form.action = args.newAction;
	submitAndShowIFrameDialog( form, options );
	form.action = origAction;
}

/**
 * 呼叫此函式, 可將目前頁面中的IFrameDialog關閉.
 */
function closeIFrameDialog()
{
	if( iframeDialog )
	{
		iframeDialog.close();
		iframeDialog = null;
		return true;
	}
	return false;
}

/**
 * 被置於IFrameDialog中的頁面, 可透過呼叫此函式, 將父視窗的IFrameDialog關閉.
 */
function closeOpenerIFrameDialog()
{
	var windowObj = BrowserUtils.findOpenerWindow();
	if( windowObj == null )
	{
		alert( 'browser.opener.notExist'.i18n() );
	}
	else
	{
		try
		{
			//因為可能是獨立開啟的視窗, 所以若未正確關閉, 則直接關閉視窗.
			if( !windowObj.closeIFrameDialog() )
			{
				window.close();
			}
		}
		catch( e )
		{
			alert( e.message );
		}
	}
}

//*************************************************************
//	Dialogs object end
//*************************************************************

var DatePicker = Class.create();
DatePicker.prototype =
{
	initialize: function( options )
	{
		var curDate = new Date();
		var maxYear = curDate.getFullYear();
		var mixYear = maxYear - 5;
		new Prado.WebUI.TDatePicker({
			'ID': options.dateField,'Trigger': options.imageId,'InputMode':'TextBox','Format':'yyyyMMdd','FirstDayOfWeek':1,'CalendarStyle':'default','FromYear':mixYear,'UpToYear':maxYear,
			'MonthNames':[ 'label.jan'.i18n(), 'label.feb'.i18n(), 'label.mar'.i18n(), 'label.apr'.i18n(), 'label.may'.i18n(), 'label.jun'.i18n(), 'label.jul'.i18n(), 'label.aug'.i18n(), 'label.sep'.i18n(), 'label.oct'.i18n(), 'label.nov'.i18n(), 'label.dec'.i18n()],
			'AbbreviatedMonthNames':[ 'label.jan'.i18n(), 'label.feb'.i18n(), 'label.mar'.i18n(), 'label.apr'.i18n(), 'label.may'.i18n(), 'label.jun'.i18n(), 'label.jul'.i18n(), 'label.aug'.i18n(), 'label.sep'.i18n(), 'label.oct'.i18n(), 'label.nov'.i18n(), 'label.dec'.i18n()],
			'ShortWeekDayNames':[ 'label.sun'.i18n(), 'label.mon'.i18n(), 'label.tue'.i18n(), 'label.wed'.i18n(), 'label.thu'.i18n(), 'label.fri'.i18n(), 'label.sat'.i18n()]
		});
	}
}

/**
 * Table的額外應用, 可排序, 修改cell內容及resize欄位大小, 並可將table做成fixed header的型式..
 * 需注意的是, 若要排序之table包含了form的資料時, 則需注意以下事項:
 * 1. 因為排序時是以row順序的調換來達成, 而在form中的資料順序也跟row有關, 當row調換時, 欄位順序也必須跟著調換, 因此即使在form的某些欄位在table中沒有顯示,
 *    仍必須將那些欄位放置在每一個row(tr)中的某一個cell(td)中. 這樣當row在調換時, form的所有欄位才會跟著調換.
 * 2. 若form中某些欄位之值是由資料順序來定義時, 必須指定欄位名稱, 以便排序後能正確重新依順序設定其值.
 *
 * 例如:
 * new AdvancedTable( {table: $('theTable'), sequenceFieldNames: 'arg.rcvNoIndexes arg.contlessUFlags arg.contlessVFlags arg.contlessMFlags arg.contlessJFlags arg.contlessAFlags'} );
 * 其中sequenceFieldNames用來指定是依出現之順序(由0開始)當成其值的欄位名稱, 若有多個欄位, 就以空白間隔.
 */
var AdvancedTable = Class.create(
	{

		initialize: function( options )
		{
			var _options = {
				sequenceClass: 'seqNo',
				rowEvenClass: 'evenRow',
				rowOddClass: 'oddRow',
				columnClass: 'sortable',
				ascendingClass: 'ascending',
				descendingClass: 'descending',
				editable: false,
				resizable: false,
				sortable: false,
				fixedHead: false , //是否做成fixed header的型式.
				fixedBlockHeight: 400, //當fixedHeader為true時, 指定固定區塊的高度.
				sequenceFieldNames: null
			};
			Object.extend( _options, options || {} );
			if( !_options.table )
			{
				showAlert( 'table.table.required'.i18n() );
            }
			else if( !$(_options.table ) )
			{
				showAlert( 'validate.param.required'.i18n( _options.table ) );
            }
			else
			{
				TableKitHelper.init();
				this.table = new TableKit( _options.table, _options );
				if( _options.fixedHead )
				{
					this.fixTableHeader( _options );
				}
			}
		},
		fixTableHeader: function( _options )
		{
			var theTable = _options.table;
			//插入一div至table的parent.
			var block = new Element("div", {'class':'fixedHeadTableBlock'} );
			theTable.up().appendChild( block );
			block.update( theTable );
			var body = theTable.tBodies ? $(theTable.tBodies[0]) : null;
			if( body )
			{
				//要先針對block及body設定高度, 此動作會影響到table欄位寬度(會有scrollbar)
				if( !ieBrowser ) //若不是ieBrowser, 要設定外框及tbody的高度
				{
					if( theTable.getHeight() >= _options.fixedBlockHeight || block.getHeight() >= _options.fixedBlockHeight)
					{
						block.setStyle( {height: _options.fixedBlockHeight} );
						body.setStyle( {height: theTable.getHeight()-$(theTable.tHead).getHeight()-(theTable.getHeight()-block.getHeight())-5} );
					}
				}
				else //IE只要設定外框高度即可.
					block.setStyle( {height: _options.fixedBlockHeight} );

				//IE及FF直接設定class即可.
				if( ieBrowser || geckoBrowser )
				{
					if( !theTable.hasClassName("fixedHeadTable") )
						theTable.addClassName("fixedHeadTable");
				}
				//非IE及非FF的才要使用javascript設定欄位寬度, 並做必要的調整.
				else
				{
					//由於table在套用fixedHeadTable後, thead及tbody都變成獨立的block, 會造成上下欄位的欄位不一致.
					//所以在套用fixedHeadTable前, 先取得header目前的寬度, 以便做為預設欄位的寬度.
					var headers = TableKitHelper.findHeaderRows( theTable );
					var widthArray = new Array();
					var borderPaddingWidthArray = new Array();
					var i;
					for( i=0; i<headers.length; i++ )
					{
						widthArray[i] = headers[i].getWidth();
						//透過getWidth()取得的寬度是整個cell的寬度, 會包含border及padding,
						//但在後續透過style設定寬度時, 設定之值並不包含border及padding, 所以需先紀錄每個header的borderPadding.
						borderPaddingWidthArray[i] = StyleUtils.borderPaddingSize( headers[i] ).width;
					}

					//先設定欄位寬度當成預設值.
					var cells = body.rows ? $A(body.rows[0].cells) : $A([]);
					for( i=0; i<headers.length; i++ )
					{
						//透過setStyle設定寬度時, 設定之值並不包含border及padding,
						//亦即實際畫面上的寬度會比指定之值再多出原cell的border-padding的寬度, 因此在設定前要先減掉.
						var width = widthArray[i]-borderPaddingWidthArray[i];
						headers[i].setStyle( {width: width} );
						//排除rows[0]不存在或header與body cell數目不同的情況
						if( cells[i] )
							cells[i].setStyle( {width: width} );
					}
					//先紀錄套用style前的block及table寬度
					var origBlockWidth = block.getWidth();
					var origTableWidth = theTable.getWidth();
					//alert( origBlockWidth + ":" + origTableWidth );
					//套用table style, 此動作會造成header及body各自變成block, 而header上的scrollbar會消失, 造成header欄位寬度會有微調
					if( !theTable.hasClassName("fixedHeadTable") )
						theTable.addClassName("fixedHeadTable");
					//alert( block.getWidth() + ":" + theTable.getWidth() );

					//預設scrollbar的寬度值
					var scrollbarWidth = 17;
					if( webKitBrowser ) //Safari上scrollbar寬度差不多是15px
						scrollbarWidth = 15;
					if( operaBrowser )
					{
						//針對Opera欄位寬度微調的後續調整如下:
						//cell寬度會依內容長短而變化, 但因為主要是以body cell寬度為主, 所以直接比對header及body中各cell的欄寬,
						//若header cell較長時, 代表body對應的cell要加寬, 連帶table(以及外框)也要加寬,
						//若body cell較長時, 則只要把header對應的cell加寬即可, 不必加大table寬度,
						//並將較寬之寬度依序儲存, 以便後續當成各欄位的寬度.
						//而針對最後一個cell, 由於body必須包含scrollbar而header沒有, 所以header最後一個cell寬度要較body最後一個cell寬17px(scrollbar寬度).
						//由於此方式是以body cell寬度為主, 當header較長時, 即使有其他cell可以縮小欄寬加以補足, 但因目前無從判斷cell欄寬是否足夠, 所以只能統一加寬table寬度.
						//因此table會較實際所需的大一些.

						//記錄需加大的尺寸
						var offset = 0;
						for( i=0; i<headers.length; i++ )
						{
							var hw = headers[i].getWidth();
							var cw = cells[i].getWidth();
							//計算header cell與body cell的寬度差.
							var num = hw - cw;
							//若是最後一個cell, 要再多減去scrollbarWidth.
							if( i == headers.length-1 )
								num = num - scrollbarWidth;
							//若num大於0, 代表header寬, 外框需加大
							if( num > 0 )
							{
								//alert( i + ":" + num );
								offset += num;
								widthArray[i] = hw;
							}
							else
								widthArray[i] = cw;
						}
						//若有需要就加大外框寬度.
						if( offset > 0 )
						{
							block.setStyle( {width: block.getWidth()+offset} );
						}
					}
					if( webKitBrowser )
					{
						//針對Safari欄位寬度微調的後續調整如下:
						//在套用table style後, header, body都會變短, 但考量原本的block寬度便已可容納原table寬度,
						//所以只要考慮scrollbar的寬度即可
						var totalHeaderWidth = 0;
						widthArray.each( function( num ) {totalHeaderWidth+=num;});
						var tableWidth = theTable.getWidth();
						var tbOffset = origBlockWidth - origTableWidth;
						//alert( widthArray + ":" + totalHeaderWidth );
						offset = 0;
						var a = scrollbarWidth - tbOffset;
						//alert( a );
						if( a == 0  )
							offset = tableWidth - origTableWidth;
						else if( a > 0 )
						{
							offset = origTableWidth - tableWidth;
						}
						block.setStyle( {width: origBlockWidth+offset } );
						//theTable.setStyle( {width: origTableWidth+offset} );
						theTable.setStyle( {width: '100%'} );
					}
					//調整好table與block寬度後, 再設定cell寬度
					for( i=0; i<headers.length; i++ )
					{
						width = widthArray[i]-borderPaddingWidthArray[i];
						if( i != headers.length-1 )
							headers[i].setStyle( {width: width} );
						else
							//因為最後一欄欄寬已去掉了scrollbar, 所以header要加上scrollbarWidth.
							headers[i].setStyle( {width: width+scrollbarWidth} );
						cells[i].setStyle( {width: width} );
						//var w = cells[i].getWidth();
						//alert( w + ":" + width + ":" + StyleUtils.marginSize( cells[i] ).width );
					}
				}
			}
		}
    }
);

var hasTableKit = false;
try
{
	if( TableKit )
		hasTableKit = true;
}
catch( e )
{
}
//若有使用TableKit, 就使用TableKitHelper來增加其功能或做必要之覆寫.
if( hasTableKit )
{
	TableKitHelper = {
		initialized : false,
		init : function()
		{
			if( !this.initialized )
			{
				this.initialized = true;
				this._wrapFunction();
            }
        },
		/**
		 * 因為必須在實際sort後做後續處理, 所以利用自定的AdvancedTable.sort保留原TableKit.Sortable.sort,
		 * 然後再將wrapper指定給TableKit.Sortable.sort, 即可達成此一目的.
		 */
		_wrapFunction: function()
		{
			//TableKit.Sortable.sort = this._sortWrapper;
			TableKit.Sortable.sort = TableKit.Sortable.sort.wrap(this.sortWrapper);
			TableKit.getBodyRows = TableKit.getBodyRows.wrap(this.getBodyRowsWrapper);
			TableKit.getHeaderCells = TableKit.getHeaderCells.wrap(this.getHeaderCellsWrapper);
			TableKit.getCellIndex = TableKit.getCellIndex.wrap(this.getCellIndexWrapper);
        },
		/**
		 * 因為必須在實際sort後做後續處理, 所以利用自定的AdvancedTable.sort保留原TableKit.Sortable.sort,
		 * 然後再將wrapper指定給TableKit.Sortable.sort, 即可達成此一目的.
		 * 此為用來包裝TableKit.Sortable.sort的method.
		 */
		sortWrapper: function( originalFunction, table, index, order )
		{
			//呼叫原sort method. 注意, 由於本function會被指定給TableKit當event handler, 所以在本函式中若使用this, 代表的是TableKit,
			//而不是AdvancedTable.
			//AdvancedTable.sort( table, index, order );
            originalFunction( table, index, order );
			//若有定義sequenceClass, 就要重設其中的序號.
			if( TableKit.options.sequenceClass )
			{
				if(typeof index === 'number') {
					if(!table || (table.tagName && table.tagName !== "TABLE")) {
						return;
					}
					table = $(table);
				} else {
					var cell = $(index);
					table = table ? $(table) : cell.up('table');
				}
				var cells = $(table).select("td." + TableKit.options.sequenceClass )
				cells.each(function(c,i) {
					//保留input element, 其餘均清除.
					var inputs = c.select("input");
					//設定序號
					c.innerHTML = i+1;
					//將input全數放回去cell中.
					inputs.each( function(input)
						{
							c.insert( input );
						}
					);
				});
			}
			//若有欄位是依順序當成其值時, 也要重設其value.
			if( TableKit.options.sequenceFieldNames )
			{
				$w( TableKit.options.sequenceFieldNames ).each(
					function( name )
					{
						var fields = $(table).select("input[name='"+name+"']");
						if( fields == '' )
							showAlert( 'form.field.notExist'.i18n( name ) );
						else
						{
							fields.each(
								function( field, i )
								{
									field.value = i;
								}
							);
						}
                    }
				)
            }
		},
		/**
         * 因為原函式會將找到的row暫存在TableKit.rows裡, 但使用Ajax查詢所得之table會被整個換掉, 造成暫存之rows與實際的不符合,
         * 所以改寫成每次都重新取得rows.
         */
		getBodyRowsWrapper : function(originalFunction, table)
		{
			table = $(table);
			//若沒有tBody, 就直接return空的陣列, 以避免錯誤.
			if( table.tBodies.length == 0 )
				return new Array();
			var id = table.id;
			TableKit.rows[id] = (table.tHead && table.tHead.rows.length > 0) ? $A(table.tBodies[0].rows) : $A(table.rows).without(table.rows[0]);
			return TableKit.rows[id];
		},
		/**
         * 因為原函式會將找到的header暫存在TableKit.heads裡, 但使用Ajax查詢所得之table會被整個換掉, 造成暫存之heads與實際的不符合,
         * 所以改寫成每次都重新取得heads.
         */
		getHeaderCellsWrapper : function(originalFunction, table, cell)
		{
			if(!table) { table = $(cell).up('table'); }
			//若沒有tBody, 就直接return空的陣列, 以避免錯誤.
			if( table.tBodies.length == 0 )
				return new Array();
			var id = table.id;
			TableKit.heads[id] = TableKitHelper.findHeaderRows( table );
			return TableKit.heads[id];
		},
		/**
         * 原函式在計算cellIndex時, 未考慮colSpan的問題, 而由於header的部份已改寫, 加入rowSpan及colSpan的考量, 所以此處要改寫, 才能與header一致.
         */
		getCellIndexWrapper : function(originalFunction, cell)
		{
			var cells = $A(cell.parentNode.cells);
			var index = 0;
			var cellIndex = cells.indexOf(cell);
			for( var i=0; i<cellIndex; i++ )
			{
				index += cells[i].colSpan;
            }
			return index;
		},
		findHeaderRows: function( table )
		{
			var columnCount = 0;
			if( table.tBodies && table.tBodies.length > 0 && table.tBodies[0].rows && table.tBodies[0].rows.length > 0 )
			{
				columnCount = table.tBodies[0].rows[0].cells.length;
            }
			if( columnCount > 0 && table.tHead && table.tHead.rows.length > 0 )
			{
				if( table.tHead.rows.length == 1 )
					return $A(table.tHead.rows[0].cells);
				else
				{
					//若header最後一列與tbody的欄位數相同, 就以最後一列做為其header cell.
					if( table.tHead.rows[table.tHead.rows.length-1].cells.length == columnCount )
						return $A(table.tHead.rows[table.tHead.rows.length-1].cells);
					else
					{
						var cells = new Array();
						//在有rowSpan的情況下, 以一般習慣而言, 最接近tbody的td會是實際tbody中對應欄位的header,
						//所以在此就以此前提來收集每個最接近tbody的header cell
						var rows = table.tHead.rows;
						var firstRow = rows[0];
						var lastRow = rows[table.tHead.rows.length-1];
					    var lastRowCellIndex = 0;
						for( var i=0; i<firstRow.cells.length; i++ )
						{
							var cell = firstRow.cells[i];
//							//window.console.debug( cell.colSpan );
							//若正好rowSpan與rows.length相同, 代表為最近tbody的header cell.
							if(cell.rowSpan == rows.length )
							{
								cells.push( cell );
                            }
							else
							{
								//若rowSpan與rows.length不相同, 以前述之前題, 直接取最後一個row的cell, 應該就是所需要的header cell,
								//但同時需考慮colSpan.
								for( var j=0; j<cell.colSpan; j++ )
								{
									cells.push( lastRow.cells[lastRowCellIndex++] );
                                }
                            }
						}
						return $A(cells);
                    }
                }
            }
			else
				return $A(table.rows[0].cells);
        }
    }
}
