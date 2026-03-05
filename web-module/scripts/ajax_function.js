/**
 * ---------------------------------------------------------------------------------------------------
 * $Id: ajax_function.js,v 1.1 2015/11/12 08:30:25 linsteph2\cvsuser Exp $
 * ---------------------------------------------------------------------------------------------------
 */

var AjaxUtil =
{
	inProgressRequests: new Array(),
	addInProgress: function( transport )
	{
		this.inProgressRequests.push( transport );
    },
	stopUnfinished: function()
	{
		this.inProgressRequests.each( function( transport )
		{
			//只要未完成的, 都要將其中止.
			if( transport.readyState != 4 )
				transport.abort();
        });
		this.inProgressRequests.clear();
    },

	/**
	 * 利用ajax做資料查詢, 需搭配server端傳回之AjaxResult結構,
	 * 執行後若傳回之訊息為失敗, 會自動顯示錯誤訊息, 若傳回之訊息為成功, 則會將查詢結果(XML DOM root物件)傳給指定之onComplete function處理.
	 * 呼叫本函式時, 需要以下參數:
	 *	url: 要執行validate的URL, url中必須包含參數
	 *  params: 查詢時需傳遞額外參數時, 可利用JSON的格式設定參數.
	 *	onComplete: 當查詢完成後, 要執行之function, 必須為function物件.
	 */
	queryByAjax : function ( args )
	{
		if( !args.url )
			showAlert( 'validate.param.required'.i18n('url') );
		if( !args.onComplete )
			showAlert( 'validate.param.required'.i18n('onComplete') );

		var requestAsync = true;
		if (args.asynchronous != undefined) {
			requestAsync = true && args.asynchronous;
		}
	
		override_psSRAPRewriter_convert_expression();
		showLoadingMessage();
		var processed = false;
		new Ajax.Request( args.url,
			{
				asynchronous: requestAsync,
				contentType: 'application/x-www-form-urlencoded',
				encoding: 'utf8',
				method: 'post',
				parameters: args.params || {},
				/* postBody: , */
				/* requestHeaders: , */
				onCreate: function( transport )
				{
					AjaxUtil.addInProgress( transport.transport );
				},
				onSuccess: function( transport, json )
				{
					closeLoadingMessage();

					//注意, prototype支援使用header傳回X-JSON參數, 可直接parse成json參數後傳入本函式,
					//但若傳回的字串中含有中文字時, 則會出錯.
					var str = transport.responseText;
					try
					{
						var result = str.strip().unfilterJSON().evalJSON( str );
						if( result._success )
						{
							args.onComplete( result );
						}
						else
						{
							showAlert("message:" + result._message );
						}
					}
					catch( e )
					{
						showAlert( e.message );
					}
					/* XML version  */
					/*
					var xml = transport.responseXML;
					if( AjaxUtil.isValidXMLData( xml ) )
					{
						var root = xml.documentElement;
						var success = eval( root.getAttribute("success") );
						if( success )
						{
							args.onComplete( root );
						}
						else
						{
							var resultValue = root.getElementsByTagName("result-value")[0];
							if( ieBrowser )
								showAlert( resultValue.text );
							else
								showAlert( resultValue.textContent );
						}
					}
					 */
					processed = true;
				},
				onException: function( request, exception )
				{
					closeLoadingMessage();
					AjaxUtil.handleException( request, exception );
					processed = true;
				},
				onFailure: function( transport )
				{
					closeLoadingMessage();
					AjaxUtil.handleFailure( transport );
					processed = true;
				},
				onComplete: function( transport )
				{
					closeLoadingMessage();
					if( !processed )
					{
						AjaxUtil.handleNotHandled( transport );
					}
				},
				on401: function( transport )
				{
					closeLoadingMessage();
					showAlert( 'error.401'.i18n( transport.request.url ) );
				},
				on404: function( transport )
				{
					closeLoadingMessage();
					showAlert( 'error.404'.i18n( transport.request.url ) );
				}
			}
		);
	},
	/**
	 * 當form要做ajaxSubmit時, 可以使用本函式當成其onsubmit的handler, 例如:
	 * $(form).observe("submit", ajaxSubmitHandler.bindAsEventListener(form,checkForm) );
	 * 其中checkForm代表的是form的檢查function, 若該form有需要用javascript function做validate時, 可以指定.
	 * 注意, 在本函式中, bindAsEventListener的第二個參數只允許使用function當參數, 且會自動被視為是form的validate function.
	 * 若該function傳回false, 就會中止form的submit.
	 */
	ajaxSubmitHandler: function ( event )
	{
		Event.stop( event );
		var args = $A(arguments);
		if( args.length > 1 )
		{
			if( Object.isFunction( args[1] ) )
			{
				ajaxFormSubmit( {form: this, checkForm: args[1]} );
			}
			else
			{
				showAlert( 'ajax.need.func2'.i18n() );
			}
		}
		else
			ajaxFormSubmit( {form: this} );

		return false;
	},
	/**
	 * 處理AJAX過程中發生exception的event.
	 */
	handleException : function ( request, exception )
	{
		//當查詢結束後, 直接關閉dialog.
		closeSubmitMessage();
		var msg = 'ajax.exception'.i18n( exception.message );
		try
		{
			if( request.transport.status == "" )
			{
				msg = 'ajax.unknownStatus'.i18n() + msg;
			}
		}
		catch( e )
		{
			msg += 'ajax.transport.noStatus'.i18n();
		}
		showAlert( msg );
	},
	/**
	 * 處理AJAX過程中發生failure的event.
	 */
	handleFailure : function ( transport )
	{
		//當查詢結束後, 直接關閉dialog.
		closeSubmitMessage();
		showAlert("Failure! Status : " + transport.status +
			"<br>Response Text : " + transport.responseText +
			"<br>Response XML : " + transport.responseXML +
			"<br>Ready State : " + transport.readyState +
			"<br>Status : " + transport.status +
			"<br>Status Text : " + transport.statusText );
	},

	/**
	 * 當AJAX request結束後之結果沒有對應之function來處理時, 可呼叫本函式顯示錯誤訊息.
	 */
	handleNotHandled : function ( transport )
	{
		showAlert( 'ajax.response.notHandled'.i18n( transport.status, transport.responseText ) );
	},

	/**
	 * 檢查傳回之資料是否為正確的XML格式, 若不是, 會丟出錯誤訊息.
	 */
	isValidXMLData : function ( data )
	{
		if( data.documentElement == null || data.documentElement.nodeName == "parsererror" )
		{
			showAlert( 'ajax.response.notValid'.i18n() );
			return false;
		}
		return true;
	},

	/**
	 * 檢查傳回之資料是否不是登入畫面, 若為登入畫面會顯示錯誤訊惜並傳回false, 若不是, 則傳回true.
	 */
	isNotLoginPage : function ( data )
	{
		if( data.indexOf("login.do") != -1 )
		{
			showAlert( 'ajax.timeout'.i18n() );
			return false;
		}
		return true;
	}
}

/**
 * 使用AJAX的方式查詢某一欄位之值. 呼叫時需傳入以下參數:
 * url : 執行下拉選單選頁查詢之URL.
 * params: 查詢時需傳遞額外參數時, 可利用JSON的格式設定參數.
 * targets: 設定要傳回之參數及對應要設定的field及區塊, 可設定多筆, 每一筆之資料結構如下:
 *		name: 傳回之參數名稱, server端之程式必須使用此參數名稱將值傳回, 即使沒有值, 也必須傳回空值.
 *		field(optional) : form element物件, 查詢所得之值會設定給此field.
 *		displayBlock(optional) : html dom element, 若有指定, 則查詢所得之值會設定至該block的innerHTML, 主要用於當field為hidden欄位時, 可顯示查詢所得之值.
 * callbackFunction(optional): 當查詢完畢後要執行的funciton, 該function被呼叫時, 會傳入查詢之結果會其參數.
 * 使用範例如:
 * queryFieldValueByAjax( {
 *	url: "../ajax/queryMchtDatas.do",
 *	params: {
 *		masterId: '0101001240'
 *	},
 *	targets: {
 *		[name: 'mchtName', field document.forms[0]['entity.mchtName'], $('mchtNameBlock')],
 *		[name: 'owner', field document.forms[0]['entity.owner'], $('ownerBlock')]
 *	},
 *	callbackFunction: myFunction
 * });
 * 若有定義callbackFunction, 則另需定義指定之function, ajaxResult為查詢後傳回之結果.
 * function myFunction( ajaxResult )
 * {
 *		//do whatever you want...
 * }
 */
function queryFieldValueByAjax( args )
{
	//查詢前先清除
	$A(args.targets).each(
		function( target )
		{
			if( !target.name )
				showAlert( 'validate.param.required'.i18n('name') );
			if( target.field )
				FormUtils.clearFieldValue( target.field );
			if( target.displayBlock )
				target.displayBlock.innerHTML = "";
		}
	);
	/*
	args.onComplete = function( root )
	{
		if( args.field )
			args.field.value = "";
		if( args.displayBlock )
			args.displayBlock.innerHTML = "";
		var resultValue = root.getElementsByTagName("result-value")[0];
		var value;
		if( ieBrowser )
			value = resultValue.text;
		else
			value = resultValue.textContent;

		if( args.field )
			args.field.value = value;
		if( args.displayBlock )
			args.displayBlock.innerHTML = value;
	}
	 */
	args.onComplete = function( ajaxResult )
	{
		$A(args.targets).each(
			function( target )
			{
				var value = ajaxResult[ target.name ];
				//alert( target.name + ":" + value );
				if( value == undefined )
				{
					showAlert( 'ajax.response.result.absent'.i18n( target.name ) );
				}
				else
				{
					if( target.field )
						FormUtils.setFieldValue( target.field, value );
					if( target.displayBlock )
						target.displayBlock.innerHTML = value;
				}
			}
		);
		if( args.callbackFunction )
		{
			args.callbackFunction( ajaxResult );
		}
		/*
		var resultValue = root.getElementsByTagName("result-value")[0];
		var value;
		if( ieBrowser )
			value = resultValue.text;
		else
			value = resultValue.textContent;

		if( args.field )
			args.field.value = value;
		if( args.displayBlock )
			args.displayBlock.innerHTML = value;
		 */
	}
	AjaxUtil.queryByAjax( args );
}

/**
 * 使用AJAX的方式查詢下拉選單的選項. 呼叫時需傳入以下參數:
 * url : 執行下拉選單選頁查詢之URL.
 * selectObj : 下拉選單控制項, 必須為HTML DOM 物件
 * params: 查詢時需傳遞額外參數時, 可利用JSON的格式設定參數.
 * callbackFunction(optional): 當查詢完畢後要執行的funciton, 該function被呼叫時, 會傳入查詢之結果會其參數.
 * 使用範例如:
 * queryOptionsByAjax(
 *	{
 *		url: "../ajax/queryOptions.ax",
 *		selectObj: document.forms[0]['employee'],
 *		params: {
 *			optionType:"employee",
 *			masterId: '001',
 *			includeEmpty: true
 *		},
 *		callbackFunction: myFunction
 *	}
 * );
 * 若有定義callbackFunction, 則另需定義指定之function, ajaxResult為查詢後傳回之結果.
 * function myFunction( ajaxResult )
 * {
 *		//do whatever you want...
 * }
 */
function queryOptionsByAjax( args )
{
	//查詢前先全部清空
	args.selectObj.options.length = 0;
	args.onComplete = function( ajaxResult )
	{
		if( ajaxResult.includeEmpty )
		{
			var emptyValue = ajaxResult.emptyValue ? ajaxResult.emptyValue : "";
			var emptyLabel = ajaxResult.emptyLabel ? ajaxResult.emptyLabel : "";
			if( ieBrowser )
				args.selectObj.add( new Option( emptyLabel, emptyValue ) );
			else
				args.selectObj.add( new Option( emptyLabel, emptyValue ), null );
		}
		$A(ajaxResult.options).each(
			function( option )
			{
				var o = new Option( option.label, option.value );
				if( option.selected == "true" )
					o.selected = true;
				if( ieBrowser )
					args.selectObj.add( o );
				else
					args.selectObj.add( o, null );
			}
		);
		if( args.callbackFunction )
		{
			args.callbackFunction( ajaxResult );
		}
	}
	AjaxUtil.queryByAjax( args )
}

/**
 * 使用prototype的function來執行data中包含之所有script.
 */
function executeScripts( container, data )
{
	/*
	//可直接使用prototype的evalScripts來執行script, 但其僅執行一次, 若data中有定義javascript物件時, 在網頁上的後續動作都不會存在(scope的問題?).
	data.evalScripts();
	 */
	//prototype的函式, 可取出script, 變成陣列.
	var scripts = data.extractScripts();
	if( scripts.length == 0 )
		return;
	var code = "";
	for( var i = 0; i < scripts.length; i++){
		code += scripts[i];
	}
	code = code.replace("<!--","");
	code = code.replace("-->","");
	//在IE上, 必須使用window的execScript來執行script, 但在firefox上並無此method.
	if( window.execScript )
	{
		window.execScript( code );
	}
	else
	{
		var sc = document.createElement("script");
		//在IE上, 在sc裡加入node就會出錯.
		sc.appendChild(document.createTextNode(code));
		container.appendChild(sc);
	}
}

/**
 * 利用ajax做一般HTML資料的查詢, 查詢結果會顯示在指定的container中(透過innerHTML)或交由指定之onComplete function處理.
 *	url(require): 要執行validate的URL, url中必須包含參數
 *  params(optional): 查詢時需傳遞額外參數時, 可利用JSON的格式設定參數.
 *  container(optional): 查詢之結果要放置的element.
 *	onComplete(optional): 當查詢完成後, 要執行之function, 必須為function物件.
 */
function queryHtmlByAjax( args )
{
	if( !args.url )
		showAlert( 'validate.param.required'.i18n('url') );

	if( args.container )
	{
		$(args.container).innerHTML = '';
    }

	override_psSRAPRewriter_convert_expression();

	showLoadingMessage();
	var processed = false;
	new Ajax.Request( args.url,
		{
			asynchronous: true,
			contentType: 'application/x-www-form-urlencoded',
			encoding: 'utf8',
			method: 'post',
			parameters: args.params || {},
			/* postBody: , */
			/* requestHeaders: , */
			onCreate: function( transport )
			{
				AjaxUtil.addInProgress( transport.transport );
            },
			onSuccess: function( transport, json )
			{
				closeLoadingMessage();
				//因為連線逾時會傳回login page, 所以要檢查傳回之html是否為login.jsp
				if( AjaxUtil.isNotLoginPage( transport.responseText ) )
				{
					if( args.container )
					{
						$(args.container).innerHTML = transport.responseText;
						//使用prototype的方式執行資料中包含的javascript.
						executeScripts( $(args.container), transport.responseText );
						//傳回之資料中可能會註冊onload event的handler, 在此呼叫使其能執行.
						EdsEvent.runOnLoad();
                    }

					//若使用者有設定event函式, 就執行
					if( args.onComplete )
					{
						args.onComplete();
					}
				}
				processed = true;
			},
			onException: function( request, exception )
			{
				closeLoadingMessage();
				AjaxUtil.handleException( request, exception );
				processed = true;
			},
			onFailure: function( transport )
			{
				closeLoadingMessage();
				AjaxUtil.handleFailure( transport );
				processed = true;
			},
			onComplete: function( transport )
			{
				closeLoadingMessage();
				if( !processed )
				{
					AjaxUtil.handleNotHandled( transport );
				}
			},
			on401: function( transport )
			{
				closeLoadingMessage();
				showAlert( 'error.401'.i18n( transport.request.url ) );
			},
			on404: function( transport )
			{
				closeLoadingMessage();
				showAlert( 'error.404'.i18n( transport.request.url ) );
			}
		}
	);
}

/**
 * 將傳入之form的條件送出查詢, 並將查詢之結果(html)設定給content的innerHTML.
 * 呼叫本函式時, 需要傳入以下參數:
 * args:
 *		form: 要查詢之form, 此為必要參數.
 *		container(optional): 查詢結果所在之區塊, 必須為html element, 若未指定, 則預設使用$('ajaxResultBlock')
 *		content(optional): 查詢結果內容要顯示的區塊, 必須為html element, 若未指定, 則預設使用$('ajaxResultBlockContent')
 *		checkForm(optional): 若要在form submit之前執行之function, , 必須為function物件.
 *		onComplete(optional): 當查詢完成後, 要執行之function, 必須為function物件.
 * event: 引發此動作之event, 若有傳入, 在處理完後會將該event中止.
 */
function ajaxFormSubmit( args, event )
{
	if( event )
		 Event.stop( event );
	if( args.checkForm )
	{
		if( args.checkForm( args.form ) == false )
			return;
	}
	showSubmitMessage();
	var container;
	var content;
	if( args.container )
		container = $(args.container);
	else
		container = $('ajaxResultBlock');
	if( args.content )
		content = $(args.content);
	else
		content = $('ajaxResultBlockContent');
	var processed = false;

	override_psSRAPRewriter_convert_expression();

	//必須使用$(...), 否則在部份IE上會出錯.
	//new Ajax.Request( $(args.form).action,
	//EdsLog.debug("Ajax Form submit, action:'" + args.form.action + "'");
	$(args.form).request(
		{
			encoding: "MS950",
			method: 'post',
			contentType: 'application/x-www-form-urlencoded',
			/*parameters: $(args.form).serialize(true),*/
			onCreate: function( transport )
			{
				AjaxUtil.addInProgress( transport.transport );
				//在送出資料後才清除, 以避免若正好ajaxResultBlockContent的內容正好是要送出的form時, 若在送出request前先清空, 會造成在form中的參數也都會被清除.
				content.innerHTML = "";
            },
			onSuccess: function( transport )
			{
				//當查詢結束後, 直接關閉dialog.
				closeSubmitMessage();
				//因為連線逾時會傳回login page, 所以要檢查傳回之html是否為login.jsp
				if( AjaxUtil.isNotLoginPage( transport.responseText ) )
				{
					content.innerHTML = transport.responseText;
					if( preferenceConfig.animateEffects )
					{
						if( !container.hasClassName("visible") )
						{
							//透過css設定display為none時, 使用Effect.Appear將會無作用, 所以在此需做額外動作
							//1. 設定opacity為0, 使其看不到.
							//2. 設定css為visible, 使其display強迫設為block.
							//依以上動作, 則container的display為block, 且因為opacity為0, 所以依然看不到, 但Effect.Appear可以正常運作.
							container.setOpacity(0);
							container.addClassName("visible");
							new Effect.Appear( container );
						}
					}
					else
					{
						if( container.style.display != "block" )
						{
							container.style.display = "block";
						}
					}
					//若使用者有設定event函式, 就執行
					if( args.onComplete )
					{
						args.onComplete();
					}
					//使用prototype的方式執行資料中包含的javascript.
					executeScripts( container, transport.responseText );
					//傳回之資料中可能會註冊onload event的handler, 在此呼叫使其能執行.
					EdsEvent.runOnLoad();
				}
				processed = true;
			},
			onException: function( request, exception )
			{
				AjaxUtil.handleException( request, exception );
				processed = true;
			},
			onFailure: function( transport )
			{
				AjaxUtil.handleFailure( transport );
				processed = true;
			},
			onComplete: function( transport )
			{
				if( !processed )
				{
					AjaxUtil.handleNotHandled( transport );
				}
			},
			on401: function( transport )
			{
				//當查詢結束後, 直接關閉dialog.
				closeSubmitMessage();
				showAlert( 'error.401'.i18n( transport.request.url ) );
			},
			on404: function( transport )
			{
				//當查詢結束後, 直接關閉dialog.
				closeSubmitMessage();
				showAlert( 'error.404'.i18n( transport.request.url ) );
			}
		}
	);
	//傳回false, 避免button為submit時, 若傳回true, 會使原form再submit出去.
	return;
}

/**
 * 由於在Sun Portal 6裡, 會自動加入部份javascript來控制, 但在部份應用上會發生錯誤(如AJAX),
 * 以下主要是針對AJAX的部份提供修正.
 * 由於Sun自動加入的javascript在處理XMLHttpRequest.open時,
 * 會誤判open的第一個參數為url, 而使用psSRAPRewriter_convert_expression來加以組合URL,
 * 但實際上該參數是method(GET or POST),
 * 此處主要是重新包裝該method, 避開GET, POST的轉換.
 */
function override_psSRAPRewriter_convert_expression()
{
	try
	{
		//若該method存在, 且尚未被包裝過
		if( psSRAPRewriter_convert_expression && !psSRAPRewriter_convert_expression.orginalFunction )
		{
			var func = psSRAPRewriter_convert_expression;
			//改寫該函式.
			psSRAPRewriter_convert_expression = function( url )
			{
				//目前先排除GET及POST, 其餘仍由舊函式處理.
				if( url == 'GET' || url == 'POST' )
					return url
				else
					return psSRAPRewriter_convert_expression.orginalFunction( url );
			};
			psSRAPRewriter_convert_expression.orginalFunction = func;
		}
	}
	catch( e )
	{
		return null;
	}
	return func;
}
