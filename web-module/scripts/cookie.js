/**
 * ---------------------------------------------------------------------------------------------------
 * $Id: cookie.js,v 1.1 2014/01/02 02:54:42 asiapacific\hungmike Exp $
 * ---------------------------------------------------------------------------------------------------
 */
/**
 * 用來處理cookie的物件
 */
var EdsCookie = 
{
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

var PreferenceConfig = {
	uiStyle: EdsProjConfig.defaultUIStyle,
	roundBlocks: true,
	animateEffects: false,
	iframeDraggable: false,
	maxTabCount: EdsProjConfig.maxTabCount,
	hideSelectOnIE: EdsProjConfig.hideSelectOnIE,
	initialize: function()
	{
		var cookie = EdsCookie.getCookie( EdsProjConfig.projectCookieName );
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
	},
	save: function()
	{
		EdsCookie.setCookie( EdsProjConfig.projectCookieName, Object.toJSON( this ), 90 );
	},
	clear: function()
	{
		EdsCookie.setCookie( EdsProjConfig.projectCookieName, "" );
	}
}
PreferenceConfig.initialize();
