/**
 * ---------------------------------------------------------------------------------------------------
 * $Id$
 * ---------------------------------------------------------------------------------------------------
 */

/**
 * 檢查傳入之參數 data 是否為null或空字串, 若是, 則會出現alert訊息, 並傳回false.
 * data : 要檢查之參數
 * name : 參數名稱.
 */
function notEmpty( data , name )
{
	if( data == null || data.value == "" )
	{
		alert("參數 '" + name + "' 不可為空白, 請重新輸入!!");
		return false;
	}
	return true;
}

/**
 * 檢查輸入之字串是否為數字.
 * num : 數字
 * allowFloat : true或false, 是否可為浮點數.
 */
function isNumber( num , allowFloat )
{
	var pattern;
	if (allowFloat) {
		pattern = /^-?[0-9]+(\.[0-9]*)?$/;
	} else {
		pattern = /^-?[0-9]+$/;
	}
	
	return pattern.test(num);
	/*
	var n;
	if( allowFloat )
		n = parseFloat( num );
	else
	{
		n = parseInt( num );
		if( n != parseFloat( num ) )
			return false;
	}
	if( isNaN(n) || n.toString().length != num.length)
		return false;
	*/
	return true;
}

/**
 * Check if input number is between max and min, or smaller than max if min is not given.
 */
function isInsideRange( num , max , min )
{
    var result = true;
    if( num > max )
        result = false;
    if( min != null )
    {
        if( num < min )
            result = false;
    }
    return result;
}

/**
 * 檢查輸入之字串是否為數字. 若不是, 則會出現錯誤訊息.
 * num : 數字
 * allowFloat : true或false, 是否可為浮點數.
 * name : 參數名稱.
 */
function checkNumber( num , allowFloat , name )
{
	if( !isNumber( num , allowFloat ) )
	{
		if( allowFloat )
			alert("參數 '" + name + "' 必須為數字!");
		else
			alert("參數 '" + name + "' 必須為整數!");
		return false;
	}
	return true;
}

/**
 * 比較傳入之兩數字大小, 若num1大, 則傳回1, 若相同, 則傳回0, 若num1小, 則傳回-1
 */
function compare( num1 , num2 )
{
	var n1 = parseInt( num1 );
	var n2 = parseInt( num2 );
	if( n1 > n2 )
		return 1;
	else if( n1 == n2 )
		return 0;
	else
		return -1;
}

/**
 * 查詢傳入之日期是否在今天之後.
 * dateString : 日期字串(yyyyMMdd)(西元年)
 * includeToday : 若為true, 則傳入日期可與今天日期相同, 會傳回false, 若為false, 則傳入之日期一定要大於今天.
 */
function isAfterToday( dateString , includeToday )
{
	var now = new Date();
	var year = now.getFullYear();
	var month = now.getMonth() + 1;
	var date = now.getDate();
	var today = "" + year + ((month<10)?"0"+month:""+month) + ((date<10)?"0"+date:""+date);
	if( includeToday )
		return dateString >= today;
	else
		return dateString > today;
}

/**
 * 查詢傳入之日期是否在今天之前.
 * dateString : 日期字串(yyyyMMdd)(西元年)
 * includeToday : 若為true, 則傳入日期可與今天日期相同, 會傳回false, 若為false, 則傳入之日期一定要小於今天.
 */
function isBeforeToday( dateString , includeToday )
{
	var now = new Date();
	var year = now.getFullYear();
	var month = now.getMonth() + 1;
	var date = now.getDate();
	var today = "" + year + ((month<10)?"0"+month:""+month) + ((date<10)?"0"+date:""+date);
	if( includeToday )
		return dateString <= today;
	else
		return dateString < today;
}

/**
 * 查詢傳入之日期1是否在日期2之後.
 * dateString1 : 日期字串(yyyyMMdd)(西元年)
 * dateString2 : 日期字串(yyyyMMdd)(西元年)
 * include : 若為true, 則傳入日期1可與日期2相同, 會傳回false, 若為false, 則傳入之日期1一定要大於日期2.
 */
function isAfter( dateString1 , dateString2 , include )
{
	if( include )
		return dateString1 >= dateString2;
	else
		return dateString1 > dateString2;
}

/**
 * 檢查傳入之日期字串(yyyyMMdd)是否正確
 * dateString : 要檢查之日期字串
 */
function isValidDate( dateString )
{
	var invalid = false;
	if( dateString.length != 8 )
		invalid = true;
	if( isNaN( dateString ) )
		invalid = true;
	if( parseInt( dateString ) < 0 )
		invalid = true;
	
	var year = Number( dateString.substring(0,4) );
	var month = Number( dateString.substring(4,6) );
	var date = Number( dateString.substring(6,8) );
	
	if( year < 1970 )
		invalid = true;
	if( month < 1 || month > 12 )
		invalid = true;
	if(date < 1)
		invalid = true;

	switch( month )
	{
		case 1 :
		case 3 :
		case 5 :
		case 7 :
		case 8 :
		case 10 :
		case 12 :
			if( date > 31 )
				invalid = true;
			break;
		case 4 :
		case 6 :
		case 9 :
		case 11 :
			if( date > 30 )
				invalid = true;
			break;
		case 2 :
			if( year % 4 == 0 && date > 29 )
				invalid = true;
			else if( year % 4 != 0 && date > 28 )
				invalid = true;
			break;
	}
	if( invalid )
	{
		return false;
	}
	return true;
}

/**
 * 檢查傳入之日期字串(yyyyMMdd)是否正確, 若不正確會出現alert.
 * dateString : 要檢查之日期字串
 * title : 該參數之欄位名稱
 */
function checkValidDate( dateString , title )
{
	if( !isValidDate( dateString ) )
	{
		alert("參數 '" + title + "' 的字串 '" + dateString + "' 不是正確的日期字串(yyyyMMdd)");
		return false;
	}
	return true;
}

/**
 * 檢查傳入之日期字串(yyyyMM)是否正確, 若不正確會出現alert.
 * monthString : 要檢查之年月字串
 * title : 該參數之欄位名稱
 */
function checkValidMonth( monthString , title )
{
	if( !isValidDate( monthString+"01" ) )
	{
		alert("參數 '" + title + "' 的字串 '" + monthString + "' 不是正確的日期字串(yyyyMM)");
		return false;
	}
	return true;
}

/**
 * 檢查傳入之日期字串(hhmmss, 24小時制)是否正確
 * timeString : 要檢查之日期字串
 */
function isValidTime( timeString ) {
	var invalid = false;

	if( timeString.length != 6 )
		invalid = true;
	else if( isNaN( timeString ) )
		invalid = true;
	else if( parseInt( timeString ) < 0 )
		invalid = true;
	else {
		var hour = Number( timeString.substring(0, 2) );
		var minute = Number( timeString.substring(2, 4) );
		var second = Number( timeString.substring(4, 6) );
		
		//alert(hour + "/" + minute + "/" + second);
		if (isNaN(hour) || hour < 0 || hour > 23 || isNaN(minute) || minute < 0 || minute > 60
				|| isNaN(second) || second < 0 || second > 60) {
			invalid = true;
		}
	}
	
	if( invalid ) {
		return false;
	}

	return true;	
}


/**
 * 確認是否執行刪除動作.
 */
function confirmDelete()
{
	return confirm("確定刪除本資料?");
}

function isBlank(str) {
	if (str == null) return true;
	return str.trim() == "";
}

function isValidUrl(str) {
	var pattern = new RegExp('^(https?:\\/\\/)?'+ // protocol
		'((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|'+ // domain name
		'((\\d{1,3}\\.){3}\\d{1,3}))'+ // OR ip (v4) address
		'(\\:\\d+)?(\\/[-a-z\\d%_.~+]*)*'+ // port and path
		'(\\?[;&a-z\\d%_.~+=-]*)?'+ // query string
		'(\\#[-a-z\\d_]*)?$','i'); // fragment locator

	if(!pattern.test(str)) {
		return false;
	} else {
		return true;
	}
}

function checkDateRange(dateFrom, dateTo, fieldTitle) {
	var msg = "";

	if (isBlank(dateFrom) && isBlank(dateTo)) {
		msg = fieldTitle + "必須輸入!\n";
	} else {
		var s = "";

    	if (isBlank(dateFrom)) {
    		s = s + fieldTitle + "(起)必須輸入!\n";
    	} else if (!isValidDate(dateFrom)) {
    		s = s + fieldTitle + "(起)不是正確的日期!\n";
    	}

		if (isBlank(dateTo)) {
			s = s + fieldTitle + "(迄)必須輸入!\n";
		} else if (!isValidDate(dateTo)) {
			s = s + fieldTitle + "(迄)不是正確的日期!\n";
		}

		if (s == "") {
			if (!isAfter(dateTo,dateFrom, true)) {
	    		s = s + fieldTitle + "(迄)的日期應該在" + fieldTitle + "(起)的日期之後!\n";
			}
		}

		msg = msg + s;
	}
	
	return msg;
}

function isValidateDigit(str, length) {
	if (str.length != length ) return false;
	
	var pattern = new RegExp("[0-9]{" + length + "}");
	return pattern.test(str);
}

function isValidateDigitRange(str, minLength, maxLength) {
	if (minLength != null && str.length < minLength) return false;
	if (maxLength != null && str.length > maxLength) return false;
	if (minLength == null && maxLength == null) return false; 

	var pattern = new RegExp("^[0-9]{" + 
			(minLength == null ? "" : minLength) + "," +
			(maxLength == null ? "" : maxLength) + "}$");

	return pattern.test(str);
}