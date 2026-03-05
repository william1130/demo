function ZipCodeInfo( zipCode , name , engName )
{
	this.zipCode = zipCode;
	this.name = name;
	this.engName = engName;
}

/**
 * 依傳入之zipCode欄位之值, 查詢對應之城市名, 若lang為'E', 則傳回英文名
 */
function setCityName( zipCodeField , field , lang )
{
	var code = zipCodeField.value;
	if( code.length == 0 )
	{
		field.value = '';
		return;
	}
	if( code.length != 0 && code.length != 3 )
	{
		alert("郵遞區號長度必須為3碼或不指定");
		field.value = '';
		zipCodeField.focus();
		zipCodeField.select();
		return;
	}
	var a = code.charAt(0);
	var name = '';
	switch( a )
	{
		case '1' :
			name = findCityNameByZipCode( zipArray1 , code , lang );
			break;
		case '2' :
			name = findCityNameByZipCode( zipArray2 , code , lang );
			break;
		case '3' :
			name = findCityNameByZipCode( zipArray3 , code , lang );
			break;
		case '4' :
			name = findCityNameByZipCode( zipArray4 , code , lang );
			break;
		case '5' :
			name = findCityNameByZipCode( zipArray5 , code , lang );
			break;
		case '6' :
			name = findCityNameByZipCode( zipArray6 , code , lang );
			break;
		case '7' :
			name = findCityNameByZipCode( zipArray7 , code , lang );
			break;
		case '8' :
			name = findCityNameByZipCode( zipArray8 , code , lang );
			break;
		case '9' :
			name = findCityNameByZipCode( zipArray9 , code , lang );
			break;
		default :
			break;
	}
	if( name == '' )
	{
		alert("郵遞區號 '" + code + "' 錯誤!!");
		field.value = '';
		zipCodeField.focus();
		zipCodeField.select();
		return;
	}
	field.value = name;
}

function findCityNameByZipCode( array , zipCode , lang )
{
	for( i=0 ; i<array.length ; i++ )
	{
		var info = array[i];
		if( info.zipCode == zipCode )
		{
			if( lang == 'E' )
				return info.engName;
			else
				return info.name;
		}
	}
	return '';
}

var zipArray1 = new Array();
var zipArray2 = new Array();
var zipArray3 = new Array();
var zipArray4 = new Array();
var zipArray5 = new Array();
var zipArray6 = new Array();
var zipArray7 = new Array();
var zipArray8 = new Array();
var zipArray9 = new Array();

var i;
i=0;
zipArray1[i++] = new ZipCodeInfo("100","台北市中正區","Jhongjheng District, Taipei City");
zipArray1[i++] = new ZipCodeInfo("103","台北市大同區","Datong District, Taipei City");
zipArray1[i++] = new ZipCodeInfo("104","台北市中山區","Jhongshan District, Taipei City");
zipArray1[i++] = new ZipCodeInfo("105","台北市松山區","Songshan District, Taipei City");
zipArray1[i++] = new ZipCodeInfo("106","台北市大安區","Da-an District, Taipei City");
zipArray1[i++] = new ZipCodeInfo("108","台北市萬華區","Wanhua District, Taipei City");
zipArray1[i++] = new ZipCodeInfo("110","台北市信義區","Sinyi District, Taipei City");
zipArray1[i++] = new ZipCodeInfo("111","台北市士林區","Shihlin District, Taipei City");
zipArray1[i++] = new ZipCodeInfo("112","台北市北投區","Beitou District, Taipei City");
zipArray1[i++] = new ZipCodeInfo("114","台北市內湖區","Neihu District, Taipei City");
zipArray1[i++] = new ZipCodeInfo("115","台北市南港區","Nangang District, Taipei City");
zipArray1[i++] = new ZipCodeInfo("116","台北市文山區","Wunshan District, Taipei City");

i=0;
zipArray2[i++] = new ZipCodeInfo("200","基隆市仁愛區","Ren-ai District, Keelung City");
zipArray2[i++] = new ZipCodeInfo("201","基隆市信義區","Sinyi District, Keelung City");
zipArray2[i++] = new ZipCodeInfo("202","基隆市中正區","Jhongjheng District, Keelung City");
zipArray2[i++] = new ZipCodeInfo("203","基隆市中山區","Jhongshan District, Keelung City");
zipArray2[i++] = new ZipCodeInfo("204","基隆市安樂區","Anle District, Keelung City");
zipArray2[i++] = new ZipCodeInfo("205","基隆市暖暖區","Nuannuan District, Keelung City");
zipArray2[i++] = new ZipCodeInfo("206","基隆市七堵區","Cidu District, Keelung City");
zipArray2[i++] = new ZipCodeInfo("207","台北縣萬里鄉","Wanli Township, Taipei County");
zipArray2[i++] = new ZipCodeInfo("208","台北縣金山鄉","Jinshan Township, Taipei County");
zipArray2[i++] = new ZipCodeInfo("209","連江縣南竿鄉","Nangan Township, Lienchiang County");
zipArray2[i++] = new ZipCodeInfo("210","連江縣北竿鄉","Beigan Township, Lienchiang County");
zipArray2[i++] = new ZipCodeInfo("211","連江縣莒光鄉","Jyuguang Township, Lienchiang County");
zipArray2[i++] = new ZipCodeInfo("212","連江縣東引鄉","Dongyin Township, Lienchiang County");
zipArray2[i++] = new ZipCodeInfo("220","台北縣板橋市","Banciao City, Taipei County");
zipArray2[i++] = new ZipCodeInfo("221","台北縣汐止市","Sijhih City, Taipei County");
zipArray2[i++] = new ZipCodeInfo("222","台北縣深坑鄉","Shenkeng Township, Taipei County");
zipArray2[i++] = new ZipCodeInfo("223","台北縣石碇鄉","Shihding Township, Taipei County");
zipArray2[i++] = new ZipCodeInfo("224","台北縣瑞芳鎮","Rueifang Township, Taipei County");
zipArray2[i++] = new ZipCodeInfo("226","台北縣平溪鄉","Pingsi Township, Taipei County");
zipArray2[i++] = new ZipCodeInfo("227","台北縣雙溪鄉","Shuangsi Township, Taipei County");
zipArray2[i++] = new ZipCodeInfo("228","台北縣貢寮鄉","Gongliao Township, Taipei County");
zipArray2[i++] = new ZipCodeInfo("231","台北縣新店市","Sindian City, Taipei County");
zipArray2[i++] = new ZipCodeInfo("232","台北縣坪林鄉","Pinglin Township, Taipei County");
zipArray2[i++] = new ZipCodeInfo("233","台北縣烏來鄉","Wulai Township, Taipei County");
zipArray2[i++] = new ZipCodeInfo("234","台北縣永和市","Yonghe City, Taipei County");
zipArray2[i++] = new ZipCodeInfo("235","台北縣中和市","Jhonghe City, Taipei County");
zipArray2[i++] = new ZipCodeInfo("236","台北縣土城市","Tucheng City, Taipei County");
zipArray2[i++] = new ZipCodeInfo("237","台北縣三峽鎮","Sansia Township, Taipei County");
zipArray2[i++] = new ZipCodeInfo("238","台北縣樹林市","Shulin City, Taipei County");
zipArray2[i++] = new ZipCodeInfo("239","台北縣鶯歌鎮","Yingge Township, Taipei County");
zipArray2[i++] = new ZipCodeInfo("241","台北縣三重市","Sanchong City, Taipei County");
zipArray2[i++] = new ZipCodeInfo("242","台北縣新莊市","Sinjhuang City, Taipei County");
zipArray2[i++] = new ZipCodeInfo("243","台北縣泰山鄉","Taishan Township, Taipei County");
zipArray2[i++] = new ZipCodeInfo("244","台北縣林口鄉","Linkou Township, Taipei County");
zipArray2[i++] = new ZipCodeInfo("247","台北縣蘆洲市","Lujhou City, Taipei County");
zipArray2[i++] = new ZipCodeInfo("248","台北縣五股鄉","Wugu Township, Taipei County");
zipArray2[i++] = new ZipCodeInfo("249","台北縣八里鄉","Bali Township, Taipei County");
zipArray2[i++] = new ZipCodeInfo("251","台北縣淡水鎮","Danshuei Township, Taipei County");
zipArray2[i++] = new ZipCodeInfo("252","台北縣三芝鄉","Sanjhih Township, Taipei County");
zipArray2[i++] = new ZipCodeInfo("253","台北縣石門鄉","Shihmen Township, Taipei County");
zipArray2[i++] = new ZipCodeInfo("260","宜蘭縣宜蘭市","Yilan City, Yilan County");
zipArray2[i++] = new ZipCodeInfo("261","宜蘭縣頭城鎮","Toucheng Township, Yilan County");
zipArray2[i++] = new ZipCodeInfo("262","宜蘭縣礁溪鄉","Jiaosi Township, Yilan County");
zipArray2[i++] = new ZipCodeInfo("263","宜蘭縣壯圍鄉","Jhuangwei Township, Yilan County");
zipArray2[i++] = new ZipCodeInfo("264","宜蘭縣員山鄉","Yuanshan Township, Yilan County");
zipArray2[i++] = new ZipCodeInfo("265","宜蘭縣羅東鎮","Luodong Township, Yilan County");
zipArray2[i++] = new ZipCodeInfo("266","宜蘭縣三星鄉","Sansing Township, Yilan County");
zipArray2[i++] = new ZipCodeInfo("267","宜蘭縣大同鄉","Datong Township, Yilan County");
zipArray2[i++] = new ZipCodeInfo("268","宜蘭縣五結鄉","Wujie Township, Yilan County");
zipArray2[i++] = new ZipCodeInfo("269","宜蘭縣冬山鄉","Dongshan Township, Yilan County");
zipArray2[i++] = new ZipCodeInfo("270","宜蘭縣蘇澳鎮","Su-ao Township, Yilan County");
zipArray2[i++] = new ZipCodeInfo("272","宜蘭縣南澳鄉","Nan-ao Township, Yilan County");
zipArray2[i++] = new ZipCodeInfo("290","釣魚台列嶼","Diaoyutai Archipelago");

i=0;
zipArray3[i++] = new ZipCodeInfo("300","新竹市","Hsinchu City");
zipArray3[i++] = new ZipCodeInfo("302","新竹縣竹北市","Jhubei City, Hsinchu County");
zipArray3[i++] = new ZipCodeInfo("303","新竹縣湖口鄉","Hukou Township, Hsinchu County");
zipArray3[i++] = new ZipCodeInfo("304","新竹縣新豐鄉","Sinfong Township, Hsinchu County");
zipArray3[i++] = new ZipCodeInfo("305","新竹縣新埔鎮","Sinpu Township, Hsinchu County");
zipArray3[i++] = new ZipCodeInfo("306","新竹縣關西鎮","Guansi Township, Hsinchu County");
zipArray3[i++] = new ZipCodeInfo("307","新竹縣芎林鄉","Cyonglin Township, Hsinchu County");
zipArray3[i++] = new ZipCodeInfo("308","新竹縣寶山鄉","Baoshan Township, Hsinchu County");
zipArray3[i++] = new ZipCodeInfo("310","新竹縣竹東鎮","Jhudong Township, Hsinchu County");
zipArray3[i++] = new ZipCodeInfo("311","新竹縣五峰鄉","Wufong Township, Hsinchu County");
zipArray3[i++] = new ZipCodeInfo("312","新竹縣橫山鄉","Hengshan Township, Hsinchu County");
zipArray3[i++] = new ZipCodeInfo("313","新竹縣尖石鄉","Jianshih Township, Hsinchu County");
zipArray3[i++] = new ZipCodeInfo("314","新竹縣北埔鄉","Beipu Township, Hsinchu County");
zipArray3[i++] = new ZipCodeInfo("315","新竹縣峨眉鄉","Emei Township, Hsinchu County");
zipArray3[i++] = new ZipCodeInfo("320","桃園縣中壢市","Jhongli City, Taoyuan County");
zipArray3[i++] = new ZipCodeInfo("324","桃園縣平鎮市","Pingjhen City, Taoyuan County");
zipArray3[i++] = new ZipCodeInfo("325","桃園縣龍潭鄉","Longtan Township, Taoyuan County");
zipArray3[i++] = new ZipCodeInfo("326","桃園縣楊梅鎮","Yangmei Township, Taoyuan County");
zipArray3[i++] = new ZipCodeInfo("327","桃園縣新屋鄉","Sinwu Township, Taoyuan County");
zipArray3[i++] = new ZipCodeInfo("328","桃園縣觀音鄉","Guanyin Township, Taoyuan County");
zipArray3[i++] = new ZipCodeInfo("330","桃園縣桃園市","Taoyuan City, Taoyuan County");
zipArray3[i++] = new ZipCodeInfo("333","桃園縣龜山鄉","Gueishan Township, Taoyuan County");
zipArray3[i++] = new ZipCodeInfo("334","桃園縣八德市","Bade City, Taoyuan County");
zipArray3[i++] = new ZipCodeInfo("335","桃園縣大溪鎮","Dasi Township, Taoyuan County");
zipArray3[i++] = new ZipCodeInfo("336","桃園縣復興鄉","Fusing Township, Taoyuan County");
zipArray3[i++] = new ZipCodeInfo("337","桃園縣大園鄉","Dayuan Township, Taoyuan County");
zipArray3[i++] = new ZipCodeInfo("338","桃園縣蘆竹鄉","Lujhu Township, Taoyuan County");
zipArray3[i++] = new ZipCodeInfo("350","苗栗縣竹南鎮","Jhunan Township, Miaoli County");
zipArray3[i++] = new ZipCodeInfo("351","苗栗縣頭份鎮","Toufen Township, Miaoli County");
zipArray3[i++] = new ZipCodeInfo("352","苗栗縣三灣鄉","Sanwan Township, Miaoli County");
zipArray3[i++] = new ZipCodeInfo("353","苗栗縣南庄鄉","Nanjhuang Township, Miaoli County");
zipArray3[i++] = new ZipCodeInfo("354","苗栗縣獅潭鄉","Shihtan Township, Miaoli County");
zipArray3[i++] = new ZipCodeInfo("356","苗栗縣後龍鎮","Houlong Township, Miaoli County");
zipArray3[i++] = new ZipCodeInfo("357","苗栗縣通霄鎮","Tongsiao Township, Miaoli County");
zipArray3[i++] = new ZipCodeInfo("358","苗栗縣苑裡鎮","Yuanli Township, Miaoli County");
zipArray3[i++] = new ZipCodeInfo("360","苗栗縣苗栗市","Miaoli City, Miaoli County");
zipArray3[i++] = new ZipCodeInfo("361","苗栗縣造橋鄉","Zaociao Township, Miaoli County");
zipArray3[i++] = new ZipCodeInfo("362","苗栗縣頭屋鄉","Touwu Township, Miaoli County");
zipArray3[i++] = new ZipCodeInfo("363","苗栗縣公館鄉","Gongguan Township, Miaoli County");
zipArray3[i++] = new ZipCodeInfo("364","苗栗縣大湖鄉","Dahu Township, Miaoli County");
zipArray3[i++] = new ZipCodeInfo("365","苗栗縣泰安鄉","Tai-an Township, Miaoli County");
zipArray3[i++] = new ZipCodeInfo("366","苗栗縣銅鑼鄉","Tongluo Township, Miaoli County");
zipArray3[i++] = new ZipCodeInfo("367","苗栗縣三義鄉","Sanyi Township, Miaoli County");
zipArray3[i++] = new ZipCodeInfo("368","苗栗縣西湖鄉","Sihu Township, Miaoli County");
zipArray3[i++] = new ZipCodeInfo("369","苗栗縣卓蘭鎮","Jhuolan Township, Miaoli County");

i = 0;
zipArray4[i++] = new ZipCodeInfo("400","台中市中區","Central District, Taichung City");
zipArray4[i++] = new ZipCodeInfo("401","台中市東區","East District, Taichung City");
zipArray4[i++] = new ZipCodeInfo("402","台中市南區","South District, Taichung City");
zipArray4[i++] = new ZipCodeInfo("403","台中市西區","West District, Taichung City");
zipArray4[i++] = new ZipCodeInfo("404","台中市北區","North District, Taichung City");
zipArray4[i++] = new ZipCodeInfo("406","台中市北屯區","Beitun District, Taichung City");
zipArray4[i++] = new ZipCodeInfo("407","台中市西屯區","Situn District, Taichung City");
zipArray4[i++] = new ZipCodeInfo("408","台中市南屯區","Nantun District, Taichung City");
zipArray4[i++] = new ZipCodeInfo("411","台中縣太平市","Taiping City, Taichung County");
zipArray4[i++] = new ZipCodeInfo("412","台中縣大里市","Dali City, Taichung County");
zipArray4[i++] = new ZipCodeInfo("413","台中縣霧峰鄉","Wufong Township, Taichung County");
zipArray4[i++] = new ZipCodeInfo("414","台中縣烏日鄉","Wurih Township, Taichung County");
zipArray4[i++] = new ZipCodeInfo("420","台中縣豐原市","Fongyuan City, Taichung County");
zipArray4[i++] = new ZipCodeInfo("421","台中縣后里鄉","Houli Township, Taichung County");
zipArray4[i++] = new ZipCodeInfo("422","台中縣石岡鄉","Shihgang Township, Taichung County");
zipArray4[i++] = new ZipCodeInfo("423","台中縣東勢鎮","Dongshih Township, Taichung County");
zipArray4[i++] = new ZipCodeInfo("424","台中縣和平鄉","Heping Township, Taichung County");
zipArray4[i++] = new ZipCodeInfo("426","台中縣新社鄉","Sinshe Township, Taichung County");
zipArray4[i++] = new ZipCodeInfo("427","台中縣潭子鄉","Tanzih Township, Taichung County");
zipArray4[i++] = new ZipCodeInfo("428","台中縣大雅鄉","Daya Township, Taichung County");
zipArray4[i++] = new ZipCodeInfo("429","台中縣神岡鄉","Shengang Township, Taichung County");
zipArray4[i++] = new ZipCodeInfo("432","台中縣大肚鄉","Dadu Township, Taichung County");
zipArray4[i++] = new ZipCodeInfo("433","台中縣沙鹿鎮","Shalu Township, Taichung County");
zipArray4[i++] = new ZipCodeInfo("434","台中縣龍井鄉","Longjing Township, Taichung County");
zipArray4[i++] = new ZipCodeInfo("435","台中縣梧棲鎮","Wuci Township, Taichung County");
zipArray4[i++] = new ZipCodeInfo("436","台中縣清水鎮","Cingshuei Township, Taichung County");
zipArray4[i++] = new ZipCodeInfo("437","台中縣大甲鎮","Dajia Township, Taichung County");
zipArray4[i++] = new ZipCodeInfo("438","台中縣外埔鄉","Waipu Township, Taichung County");
zipArray4[i++] = new ZipCodeInfo("439","台中縣大安鄉","Da-an Township, Taichung County");

i=0;
zipArray5[i++] = new ZipCodeInfo("500","彰化縣彰化市","Changhua City, Changhua County");
zipArray5[i++] = new ZipCodeInfo("502","彰化縣芬園鄉","Fenyuan Township, Changhua County");
zipArray5[i++] = new ZipCodeInfo("503","彰化縣花壇鄉","Huatan Township, Changhua County");
zipArray5[i++] = new ZipCodeInfo("504","彰化縣秀水鄉","Sioushuei Township, Changhua County");
zipArray5[i++] = new ZipCodeInfo("505","彰化縣鹿港鎮","Lugang Township, Changhua County");
zipArray5[i++] = new ZipCodeInfo("506","彰化縣福興鄉","Fusing Township, Changhua County");
zipArray5[i++] = new ZipCodeInfo("507","彰化縣線西鄉","Siansi Township, Changhua County");
zipArray5[i++] = new ZipCodeInfo("508","彰化縣和美鎮","Hemei Township, Changhua County");
zipArray5[i++] = new ZipCodeInfo("509","彰化縣伸港鄉","Shengang Township, Changhua County");
zipArray5[i++] = new ZipCodeInfo("510","彰化縣員林鎮","Yuanlin Township, Changhua County");
zipArray5[i++] = new ZipCodeInfo("511","彰化縣社頭鄉","Shetou Township, Changhua County");
zipArray5[i++] = new ZipCodeInfo("512","彰化縣永靖鄉","Yongjing Township, Changhua County");
zipArray5[i++] = new ZipCodeInfo("513","彰化縣埔心鄉","Pusin Township, Changhua County");
zipArray5[i++] = new ZipCodeInfo("514","彰化縣溪湖鎮","Sihu Township, Changhua County");
zipArray5[i++] = new ZipCodeInfo("515","彰化縣大村鄉","Dacun Township, Changhua County");
zipArray5[i++] = new ZipCodeInfo("516","彰化縣埔鹽鄉","Puyan Township, Changhua County");
zipArray5[i++] = new ZipCodeInfo("520","彰化縣田中鎮","Tianjhong Township, Changhua County");
zipArray5[i++] = new ZipCodeInfo("521","彰化縣北斗鎮","Beidou Township, Changhua County");
zipArray5[i++] = new ZipCodeInfo("522","彰化縣田尾鄉","Tianwei Township, Changhua County");
zipArray5[i++] = new ZipCodeInfo("523","彰化縣埤頭鄉","Pitou Township, Changhua County");
zipArray5[i++] = new ZipCodeInfo("524","彰化縣溪州鄉","Sijhou Township, Changhua County");
zipArray5[i++] = new ZipCodeInfo("525","彰化縣竹塘鄉","Jhutang Township, Changhua County");
zipArray5[i++] = new ZipCodeInfo("526","彰化縣二林鎮","Erlin Township, Changhua County");
zipArray5[i++] = new ZipCodeInfo("527","彰化縣大城鄉","Dacheng Township, Changhua County");
zipArray5[i++] = new ZipCodeInfo("528","彰化縣芳苑鄉","Fangyuan Township, Changhua County");
zipArray5[i++] = new ZipCodeInfo("530","彰化縣二水鄉","Ershuei Township, Changhua County");
zipArray5[i++] = new ZipCodeInfo("540","南投縣南投市","Nantou City, Nantou County");
zipArray5[i++] = new ZipCodeInfo("541","南投縣中寮鄉","Jhongliao Township, Nantou County");
zipArray5[i++] = new ZipCodeInfo("542","南投縣草屯鎮","Caotun Township, Nantou County");
zipArray5[i++] = new ZipCodeInfo("544","南投縣國姓鄉","Guosing Township, Nantou County");
zipArray5[i++] = new ZipCodeInfo("545","南投縣埔里鎮","Puli Township, Nantou County");
zipArray5[i++] = new ZipCodeInfo("546","南投縣仁愛鄉","Ren-ai Township, Nantou County");
zipArray5[i++] = new ZipCodeInfo("551","南投縣名間鄉","Mingjian Township, Nantou County");
zipArray5[i++] = new ZipCodeInfo("552","南投縣集集鎮","Jiji Township, Nantou County");
zipArray5[i++] = new ZipCodeInfo("553","南投縣水里鄉","Shueili Township, Nantou County");
zipArray5[i++] = new ZipCodeInfo("555","南投縣魚池鄉","Yuchih Township, Nantou County");
zipArray5[i++] = new ZipCodeInfo("556","南投縣信義鄉","Sinyi Township, Nantou County");
zipArray5[i++] = new ZipCodeInfo("557","南投縣竹山鎮","Jhushan Township, Nantou County");
zipArray5[i++] = new ZipCodeInfo("558","南投縣鹿谷鄉","Lugu Township, Nantou County");

i=0;
zipArray6[i++] = new ZipCodeInfo("600","嘉義市","Chiayi City");
zipArray6[i++] = new ZipCodeInfo("602","嘉義縣番路鄉","Fanlu Township, Chiayi County");
zipArray6[i++] = new ZipCodeInfo("603","嘉義縣梅山鄉","Meishan Township, Chiayi County");
zipArray6[i++] = new ZipCodeInfo("604","嘉義縣竹崎鄉","Jhuci Township, Chiayi County");
zipArray6[i++] = new ZipCodeInfo("605","嘉義縣阿里山鄉","Alishan Township, Chiayi County");
zipArray6[i++] = new ZipCodeInfo("606","嘉義縣中埔鄉","Jhongpu Township, Chiayi County");
zipArray6[i++] = new ZipCodeInfo("607","嘉義縣大埔鄉","Dapu Township, Chiayi County");
zipArray6[i++] = new ZipCodeInfo("608","嘉義縣水上鄉","Shueishang Township, Chiayi County");
zipArray6[i++] = new ZipCodeInfo("611","嘉義縣鹿草鄉","Lucao Township, Chiayi County");
zipArray6[i++] = new ZipCodeInfo("612","嘉義縣太保市","Taibao City, Chiayi County");
zipArray6[i++] = new ZipCodeInfo("613","嘉義縣朴子市","Puzih City, Chiayi County");
zipArray6[i++] = new ZipCodeInfo("614","嘉義縣東石鄉","Dongshih Township, Chiayi County");
zipArray6[i++] = new ZipCodeInfo("615","嘉義縣六腳鄉","Lioujiao Township, Chiayi County");
zipArray6[i++] = new ZipCodeInfo("616","嘉義縣新港鄉","Singang Township, Chiayi County");
zipArray6[i++] = new ZipCodeInfo("621","嘉義縣民雄鄉","Minsyong Township, Chiayi County");
zipArray6[i++] = new ZipCodeInfo("622","嘉義縣大林鎮","Dalin Township, Chiayi County");
zipArray6[i++] = new ZipCodeInfo("623","嘉義縣溪口鄉","Sikou Township, Chiayi County");
zipArray6[i++] = new ZipCodeInfo("624","嘉義縣義竹鄉","Yijhu Township, Chiayi County");
zipArray6[i++] = new ZipCodeInfo("625","嘉義縣布袋鎮","Budai Township, Chiayi County");
zipArray6[i++] = new ZipCodeInfo("630","雲林縣斗南鎮","Dounan Township, Yunlin County");
zipArray6[i++] = new ZipCodeInfo("631","雲林縣大埤鄉","Dapi Township, Yunlin County");
zipArray6[i++] = new ZipCodeInfo("632","雲林縣虎尾鎮","Huwei Township, Yunlin County");
zipArray6[i++] = new ZipCodeInfo("633","雲林縣土庫鎮","Tuku Township, Yunlin County");
zipArray6[i++] = new ZipCodeInfo("634","雲林縣褒忠鄉","Baojhong Township, Yunlin County");
zipArray6[i++] = new ZipCodeInfo("635","雲林縣東勢鄉","Dongshih Township, Yunlin County");
zipArray6[i++] = new ZipCodeInfo("636","雲林縣台西鄉","Taisi Township, Yunlin County");
zipArray6[i++] = new ZipCodeInfo("637","雲林縣崙背鄉","Lunbei Township, Yunlin County");
zipArray6[i++] = new ZipCodeInfo("638","雲林縣麥寮鄉","Mailiao Township, Yunlin County");
zipArray6[i++] = new ZipCodeInfo("640","雲林縣斗六市","Douliou City, Yunlin County");
zipArray6[i++] = new ZipCodeInfo("643","雲林縣林內鄉","Linnei Township, Yunlin County");
zipArray6[i++] = new ZipCodeInfo("646","雲林縣古坑鄉","Gukeng Township, Yunlin County");
zipArray6[i++] = new ZipCodeInfo("647","雲林縣莿桐鄉","Cihtong Township, Yunlin County");
zipArray6[i++] = new ZipCodeInfo("648","雲林縣西螺鎮","Siluo Township, Yunlin County");
zipArray6[i++] = new ZipCodeInfo("649","雲林縣二崙鄉","Erlun Township, Yunlin County");
zipArray6[i++] = new ZipCodeInfo("651","雲林縣北港鎮","Beigang Township, Yunlin County");
zipArray6[i++] = new ZipCodeInfo("652","雲林縣水林鄉","Shueilin Township, Yunlin County");
zipArray6[i++] = new ZipCodeInfo("653","雲林縣口湖鄉","Kouhu Township, Yunlin County");
zipArray6[i++] = new ZipCodeInfo("654","雲林縣四湖鄉","Sihhu Township, Yunlin County");
zipArray6[i++] = new ZipCodeInfo("655","雲林縣元長鄉","Yuanchang Township, Yunlin County");

i=0;
zipArray7[i++] = new ZipCodeInfo("700","台南市中西區","West Central District, Tainan City");
zipArray7[i++] = new ZipCodeInfo("701","台南市東區","East District, Tainan City");
zipArray7[i++] = new ZipCodeInfo("702","台南市南區","South District, Tainan City");
zipArray7[i++] = new ZipCodeInfo("704","台南市北區","North District, Tainan City");
zipArray7[i++] = new ZipCodeInfo("708","台南市安平區","Anping District, Tainan City");
zipArray7[i++] = new ZipCodeInfo("709","台南市安南區","Annan District, Tainan City");
zipArray7[i++] = new ZipCodeInfo("710","台南縣永康市","Yongkang City, Tainan County");
zipArray7[i++] = new ZipCodeInfo("711","台南縣歸仁鄉","Gueiren Township, Tainan County");
zipArray7[i++] = new ZipCodeInfo("712","台南縣新化鎮","Sinhua Township, Tainan County");
zipArray7[i++] = new ZipCodeInfo("713","台南縣左鎮鄉","Zuojhen Township, Tainan County");
zipArray7[i++] = new ZipCodeInfo("714","台南縣玉井鄉","Yujing Township, Tainan County");
zipArray7[i++] = new ZipCodeInfo("715","台南縣楠西鄉","Nansi Township, Tainan County");
zipArray7[i++] = new ZipCodeInfo("716","台南縣南化鄉","Nanhua Township, Tainan County");
zipArray7[i++] = new ZipCodeInfo("717","台南縣仁德鄉","Rende Township, Tainan County");
zipArray7[i++] = new ZipCodeInfo("718","台南縣關廟鄉","Guanmiao Township, Tainan County");
zipArray7[i++] = new ZipCodeInfo("719","台南縣龍崎鄉","Longci Township, Tainan County");
zipArray7[i++] = new ZipCodeInfo("720","台南縣官田鄉","Guantian Township, Tainan County");
zipArray7[i++] = new ZipCodeInfo("721","台南縣麻豆鎮","Madou Township, Tainan County");
zipArray7[i++] = new ZipCodeInfo("722","台南縣佳里鎮","Jiali Township, Tainan County");
zipArray7[i++] = new ZipCodeInfo("723","台南縣西港鄉","Sigang Township, Tainan County");
zipArray7[i++] = new ZipCodeInfo("724","台南縣七股鄉","Cigu Township, Tainan County");
zipArray7[i++] = new ZipCodeInfo("725","台南縣將軍鄉","Jiangjyun Township, Tainan County");
zipArray7[i++] = new ZipCodeInfo("726","台南縣學甲鎮","Syuejia Township, Tainan County");
zipArray7[i++] = new ZipCodeInfo("727","台南縣北門鄉","Beimen Township, Tainan County");
zipArray7[i++] = new ZipCodeInfo("730","台南縣新營市","Sinying City, Tainan County");
zipArray7[i++] = new ZipCodeInfo("731","台南縣後壁鄉","Houbi Township, Tainan County");
zipArray7[i++] = new ZipCodeInfo("732","台南縣白河鎮","Baihe Township, Tainan County");
zipArray7[i++] = new ZipCodeInfo("733","台南縣東山鄉","Dongshan Township, Tainan County");
zipArray7[i++] = new ZipCodeInfo("734","台南縣六甲鄉","Lioujia Township, Tainan County");
zipArray7[i++] = new ZipCodeInfo("735","台南縣下營鄉","Siaying Township, Tainan County");
zipArray7[i++] = new ZipCodeInfo("736","台南縣柳營鄉","Liouying Township, Tainan County");
zipArray7[i++] = new ZipCodeInfo("737","台南縣鹽水鎮","Yanshuei Township, Tainan County");
zipArray7[i++] = new ZipCodeInfo("741","台南縣善化鎮","Shanhua Township, Tainan County");
zipArray7[i++] = new ZipCodeInfo("742","台南縣大內鄉","Danei Township, Tainan County");
zipArray7[i++] = new ZipCodeInfo("743","台南縣山上鄉","Shanshang Township, Tainan County");
zipArray7[i++] = new ZipCodeInfo("744","台南縣新市鄉","Sinshih Township, Tainan County");
zipArray7[i++] = new ZipCodeInfo("745","台南縣安定鄉","Anding Township, Tainan County");

i=0;
zipArray8[i++] = new ZipCodeInfo("800","高雄市新興區","Sinsing District, Kaohsiung City");
zipArray8[i++] = new ZipCodeInfo("801","高雄市前金區","Cianjin District, Kaohsiung City");
zipArray8[i++] = new ZipCodeInfo("802","高雄市苓雅區","Lingya District, Kaohsiung City");
zipArray8[i++] = new ZipCodeInfo("803","高雄市鹽埕區","Yancheng District, Kaohsiung City");
zipArray8[i++] = new ZipCodeInfo("804","高雄市鼓山區","Gushan District, Kaohsiung City");
zipArray8[i++] = new ZipCodeInfo("805","高雄市旗津區","Cijin District, Kaohsiung City");
zipArray8[i++] = new ZipCodeInfo("806","高雄市前鎮區","Cianjhen District, Kaohsiung City");
zipArray8[i++] = new ZipCodeInfo("807","高雄市三民區","Sanmin District, Kaohsiung City");
zipArray8[i++] = new ZipCodeInfo("811","高雄市楠梓區","Nanzih District, Kaohsiung City");
zipArray8[i++] = new ZipCodeInfo("812","高雄市小港區","Siaogang District, Kaohsiung City");
zipArray8[i++] = new ZipCodeInfo("813","高雄市左營區","Zuoying District, Kaohsiung City");
zipArray8[i++] = new ZipCodeInfo("814","高雄縣仁武鄉","Renwu Township, Kaohsiung County");
zipArray8[i++] = new ZipCodeInfo("815","高雄縣大社鄉","Dashe Township, Kaohsiung County");
zipArray8[i++] = new ZipCodeInfo("817","南海諸島東沙","Dongsha, Nanhai Islands");
zipArray8[i++] = new ZipCodeInfo("819","南海諸島南沙","Nansha, Nanhai Islands");
zipArray8[i++] = new ZipCodeInfo("820","高雄縣岡山鎮","Gangshan Township, Kaohsiung County");
zipArray8[i++] = new ZipCodeInfo("821","高雄縣路竹鄉","Lujhu Township, Kaohsiung County");
zipArray8[i++] = new ZipCodeInfo("822","高雄縣阿蓮鄉","Alian Township, Kaohsiung County");
zipArray8[i++] = new ZipCodeInfo("823","高雄縣田寮鄉","Tianliao Township, Kaohsiung County");
zipArray8[i++] = new ZipCodeInfo("824","高雄縣燕巢鄉","Yanchao Township, Kaohsiung County");
zipArray8[i++] = new ZipCodeInfo("825","高雄縣橋頭鄉","Ciaotou Township, Kaohsiung County");
zipArray8[i++] = new ZipCodeInfo("826","高雄縣梓官鄉","Zihguan Township, Kaohsiung County");
zipArray8[i++] = new ZipCodeInfo("827","高雄縣彌陀鄉","Mituo Township, Kaohsiung County");
zipArray8[i++] = new ZipCodeInfo("828","高雄縣永安鄉","Yong-an Township, Kaohsiung County");
zipArray8[i++] = new ZipCodeInfo("829","高雄縣湖內鄉","Hunei Township, Kaohsiung County");
zipArray8[i++] = new ZipCodeInfo("830","高雄縣鳳山市","Fongshan City, Kaohsiung County");
zipArray8[i++] = new ZipCodeInfo("831","高雄縣大寮鄉","Daliao Township, Kaohsiung County");
zipArray8[i++] = new ZipCodeInfo("832","高雄縣林園鄉","Linyuan Township, Kaohsiung County");
zipArray8[i++] = new ZipCodeInfo("833","高雄縣鳥松鄉","Niaosong Township, Kaohsiung County");
zipArray8[i++] = new ZipCodeInfo("840","高雄縣大樹鄉","Dashu Township, Kaohsiung County");
zipArray8[i++] = new ZipCodeInfo("842","高雄縣旗山鎮","Cishan Township, Kaohsiung County");
zipArray8[i++] = new ZipCodeInfo("843","高雄縣美濃鎮","Meinong Township, Kaohsiung County");
zipArray8[i++] = new ZipCodeInfo("844","高雄縣六龜鄉","Liouguei Township, Kaohsiung County");
zipArray8[i++] = new ZipCodeInfo("845","高雄縣內門鄉","Neimen Township, Kaohsiung County");
zipArray8[i++] = new ZipCodeInfo("846","高雄縣杉林鄉","Shanlin Township, Kaohsiung County");
zipArray8[i++] = new ZipCodeInfo("847","高雄縣甲仙鄉","Jiasian Township, Kaohsiung County");
zipArray8[i++] = new ZipCodeInfo("848","高雄縣桃源鄉","Taoyuan Township, Kaohsiung County");
zipArray8[i++] = new ZipCodeInfo("849","高雄縣三民鄉","Sanmin Township, Kaohsiung County");
zipArray8[i++] = new ZipCodeInfo("851","高雄縣茂林鄉","Maolin Township, Kaohsiung County");
zipArray8[i++] = new ZipCodeInfo("852","高雄縣茄萣鄉","Jiading Township, Kaohsiung County");
zipArray8[i++] = new ZipCodeInfo("880","澎湖縣馬公市","Magong City, Penghu County");
zipArray8[i++] = new ZipCodeInfo("881","澎湖縣西嶼鄉","Siyu Township, Penghu County");
zipArray8[i++] = new ZipCodeInfo("882","澎湖縣望安鄉","Wang-an Township, Penghu County");
zipArray8[i++] = new ZipCodeInfo("883","澎湖縣七美鄉","Cimei Township, Penghu County");
zipArray8[i++] = new ZipCodeInfo("884","澎湖縣白沙鄉","Baisha Township, Penghu County");
zipArray8[i++] = new ZipCodeInfo("885","澎湖縣湖西鄉","Husi Township, Penghu County");
zipArray8[i++] = new ZipCodeInfo("890","金門縣金沙鎮","Jinsha Township, Kinmen County");
zipArray8[i++] = new ZipCodeInfo("891","金門縣金湖鎮","Jinhu Township, Kinmen County");
zipArray8[i++] = new ZipCodeInfo("892","金門縣金寧鄉","Jinning Township, Kinmen County");
zipArray8[i++] = new ZipCodeInfo("893","金門縣金城鎮","Jincheng Township, Kinmen County");
zipArray8[i++] = new ZipCodeInfo("894","金門縣烈嶼鄉","Lieyu Township, Kinmen County");
zipArray8[i++] = new ZipCodeInfo("896","金門縣烏坵鄉","Wuciou Township, Kinmen County");

i=0;
zipArray9[i++] = new ZipCodeInfo("900","屏東縣屏東市","Pingtung City, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("901","屏東縣三地門鄉","Sandimen Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("902","屏東縣霧台鄉","Wutai Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("903","屏東縣瑪家鄉","Majia Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("904","屏東縣九如鄉","Jiouru Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("905","屏東縣里港鄉","Ligang Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("906","屏東縣高樹鄉","Gaoshu Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("907","屏東縣鹽埔鄉","Yanpu Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("908","屏東縣長治鄉","Changjhih Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("909","屏東縣麟洛鄉","Linluo Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("911","屏東縣竹田鄉","Jhutian Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("912","屏東縣內埔鄉","Neipu Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("913","屏東縣萬丹鄉","Wandan Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("920","屏東縣潮州鎮","Chaojhou Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("921","屏東縣泰武鄉","Taiwu Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("922","屏東縣來義鄉","Laiyi Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("923","屏東縣萬巒鄉","Wanluan Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("924","屏東縣崁頂鄉","Kanding Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("925","屏東縣新埤鄉","Sinpi Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("926","屏東縣南州鄉","Nanjhou Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("927","屏東縣林邊鄉","Linbian Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("928","屏東縣東港鎮","Donggang Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("929","屏東縣琉球鄉","Liouciou Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("931","屏東縣佳冬鄉","Jiadong Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("932","屏東縣新園鄉","Sinyuan Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("940","屏東縣枋寮鄉","Fangliao Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("941","屏東縣枋山鄉","Fangshan Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("942","屏東縣春日鄉","Chunrih Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("943","屏東縣獅子鄉","Shihzih Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("944","屏東縣車城鄉","Checheng Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("945","屏東縣牡丹鄉","Mudan Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("946","屏東縣恆春鎮","Hengchun Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("947","屏東縣滿洲鄉","Manjhou Township, Pingtung County");
zipArray9[i++] = new ZipCodeInfo("950","台東縣台東市","Taitung City, Taitung County");
zipArray9[i++] = new ZipCodeInfo("951","台東縣綠島鄉","Lyudao Township, Taitung County");
zipArray9[i++] = new ZipCodeInfo("952","台東縣蘭嶼鄉","Lanyu Township, Taitung County");
zipArray9[i++] = new ZipCodeInfo("953","台東縣延平鄉","Yanping Township, Taitung County");
zipArray9[i++] = new ZipCodeInfo("954","台東縣卑南鄉","Beinan Township, Taitung County");
zipArray9[i++] = new ZipCodeInfo("955","台東縣鹿野鄉","Luye Township, Taitung County");
zipArray9[i++] = new ZipCodeInfo("956","台東縣關山鎮","Guanshan Township, Taitung County");
zipArray9[i++] = new ZipCodeInfo("957","台東縣海端鄉","Haiduan Township, Taitung County");
zipArray9[i++] = new ZipCodeInfo("958","台東縣池上鄉","Chihshang Township, Taitung County");
zipArray9[i++] = new ZipCodeInfo("959","台東縣東河鄉","Donghe Township, Taitung County");
zipArray9[i++] = new ZipCodeInfo("961","台東縣成功鎮","Chenggong Township, Taitung County");
zipArray9[i++] = new ZipCodeInfo("962","台東縣長濱鄉","Changbin Township, Taitung County");
zipArray9[i++] = new ZipCodeInfo("963","台東縣太麻里鄉","Taimali Township, Taitung County");
zipArray9[i++] = new ZipCodeInfo("964","台東縣金峰鄉","Jinfong Township, Taitung County");
zipArray9[i++] = new ZipCodeInfo("965","台東縣大武鄉","Dawu Township, Taitung County");
zipArray9[i++] = new ZipCodeInfo("966","台東縣達仁鄉","Daren Township, Taitung County");
zipArray9[i++] = new ZipCodeInfo("970","花蓮縣花蓮市","Hualien City, Hualien County");
zipArray9[i++] = new ZipCodeInfo("971","花蓮縣新城鄉","Sincheng Township, Hualien County");
zipArray9[i++] = new ZipCodeInfo("972","花蓮縣秀林鄉","Sioulin Township, Hualien County");
zipArray9[i++] = new ZipCodeInfo("973","花蓮縣吉安鄉","Ji-an Township, Hualien County");
zipArray9[i++] = new ZipCodeInfo("974","花蓮縣壽豐鄉","Shoufong Township, Hualien County");
zipArray9[i++] = new ZipCodeInfo("975","花蓮縣鳳林鎮","Fonglin Township, Hualien County");
zipArray9[i++] = new ZipCodeInfo("976","花蓮縣光復鄉","Guangfu Township, Hualien County");
zipArray9[i++] = new ZipCodeInfo("977","花蓮縣豐濱鄉","Fongbin Township, Hualien County");
zipArray9[i++] = new ZipCodeInfo("978","花蓮縣瑞穗鄉","Rueisuei Township, Hualien County");
zipArray9[i++] = new ZipCodeInfo("979","花蓮縣萬榮鄉","Wanrong Township, Hualien County");
zipArray9[i++] = new ZipCodeInfo("981","花蓮縣玉里鎮","Yuli Township, Hualien County");
zipArray9[i++] = new ZipCodeInfo("982","花蓮縣卓溪鄉","Jhuosi Township, Hualien County");
zipArray9[i++] = new ZipCodeInfo("983","花蓮縣富里鄉","Fuli Township, Hualien County");
