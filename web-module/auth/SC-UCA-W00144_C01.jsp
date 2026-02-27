<%--
 * ==============================================================================================
 * $Id: SC-UCA-W00144_C01.jsp,v 1.4 2019/10/08 11:26:30 \jjih Exp $
 * ==============================================================================================
--%>
<%@page pageEncoding="UTF-8"%>
<%@page contentType="text/html"%>
<%@include file="/jsp/directive.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
            <title><c:out value='${currentFunctionObj.name}'/></title>
            <%@include file="/jsp/header.jsp"%>
    </head>
    <body>
            <div id="mainContentBlock">
                    <div id="progHeader">
                            <div id="progTitle" title="程式代碼 : <c:out value='${currentFunctionObj.id}'/>"><c:out value='${currentFunctionObj.name}'/></div>
                    </div>
                    <jsp:include page="/jsp/messages.jsp"/>
                    <div id="inputBlock">

                    <s:form action="queryCreateAuthorize" method="post">
                    <edstw:folding foldingId="criteriaBlock" description="查詢條件">
                      <fieldset>
                      <legend>授權資料</legend>
                        <table border="0" align="center" cellpadding="4" cellspacing="0">
                          <tr>
                            <td class="require">*特店代號</td>
                            <td>
                           	  <s:textfield name="entity.merchantNo" size="20" maxlength="10" onkeyup="moveCard();" onchange="getMchtInfo();" />
                              <br>
                              <input type="hidden" name="merchantName"/>
                              <span id="merchantNameArea">--</span>
                            </td>
                          </tr>
                          <tr>
                            <th class="require">*卡號</th>
                            <td>
                              <s:textfield name="entity.cardNo1" size="6" maxlength="4" onkeyup="autoFocus(1);" onchange="getBankInfo();"/>-
                              <s:textfield name="entity.cardNo2" size="6" maxlength="4" onkeyup="autoFocus(2);" onchange="getBankInfo();"/>-
                              <s:textfield name="entity.cardNo3" size="6" maxlength="4" onkeyup="autoFocus(3);" onchange="getBankInfo();"/>-
                              <s:textfield name="entity.cardNo4" size="6" maxlength="4" onkeyup="autoFocus(4);" onchange="getBankInfo();"/>-
                              <s:textfield name="entity.cardNo5" size="6" maxlength="3" onkeyup="autoFocus(5);" onchange="getBankInfo();"/>
                              <br>

                              	<input type="hidden" name="bankInfo"/>
                               	<span id="bankInfoArea">--</span>

                            </td>
                          </tr>
                          <tr>
                            <td class="require">*有效日期(YYMM 西元年)</td>
                            <td>
                              <s:textfield name="entity.expireDate" size="6" maxlength="4" onkeyup="moveAmt();"/>
                            </td>
                          </tr>
                          <tr>
                            <th class="require">*金額</th>
                            <td>
                              <s:textfield name="entity.purchaseAmt" size="10" maxlength="8"/>
                            </td>
                          </tr>
                          <tr>
                            <th class="require">*授權日期</th>
                            <td>
                              <s:textfield name="entity.processDate" size="12" maxlength="8" title="請輸入西元年(YYYYMMDD)" disabled="true"/>
                              <img id="_authDate" class="datePickerImage" src="../images/dateIcon.gif" border="0" align="bottom" width="19px" height="19px" title="選擇日期" alt="選擇日期"/>
                              <script type='text/javascript' language='javascript'>
                                var form = document.forms[0];
                                new DatePicker( {imageId:'_authDate', dateField:form['entity.processDate']} );
                              </script>
                            </td>
                          </tr>
                          <tr>
                            <th class="require">*授權時間</th>
                            <td><s:textfield name="entity.processTime" size="10" maxlength="6" disabled="true"/>(輸入格式:HHMMSS)</td>
                          </tr>
                          <tr>
                            <th class="optional">補登交易</th>
                            <td>

                              <s:radio name="entity.addFlag" list="#{'Y':'是'}"/>&nbsp;&nbsp;
                              <s:radio name="entity.addFlag" list="#{'N':'否'}"/>
                              <s:hidden name="entity.authorizeReason" value="C1"/>
                            </td>
                          </tr>
                          <tr>
                            <th class="optional">CVC2</th>
                            <td><input type="password" name="entity.cvc2" size="4" maxlength="4" /></td>
                          </tr>
                          <tr>
                            <td colspan="2" class="buttonCell">
                              <input id="defaultSubmitButton" type="submit" class="button" name="submitButton" value="確定" />
                              <input type="button" class="button" name="resetButton" value="重設" onclick="clean();"/>
                            </td>
                          </tr>
                        </table>
                        </fieldset>
                      </edstw:folding>
                      </s:form>
				      <script language="javascript" type="text/javascript">
				         var form = document.forms[0];
                         function onSubmitHandler( event )
                         {
                            var msg = "資料錯誤:\n\n";
                            var warnMsg = "注意:\n\n";
                            if( form['entity.merchantNo'].value == "")
                                msg = msg + "特店代號必須輸入.\n";
                            else
                            {
                            	if(addFlagRadio()==1 && form['entity.merchantNo'].value.length < 10)
                            	{
                            	   msg = msg + "特店代號為4,6,7碼時,必須是補登交易.\n";
                            	}
                            }

                            if( form['entity.cardNo1'].value == "")
                                msg = msg + "卡號必須輸入.\n";
                            else
                            {
                               	form['entity.cvc2'].value=form['entity.cvc2'].value.trim();

                               	/** M2020178 CVC2異動為選擇性欄位
                               	if(form['entity.merchantNo'].value.length>5)
                               	{
	                               var mc = form['entity.merchantNo'].value;
	                               if((mc.substring(2,5)=="016" || mc.substring(2,5)=="316") && form['entity.cvc2'].value=="" && addFlagRadio()==1 )
								   {
									  var card = form['entity.cardNo1'].value+form['entity.cardNo2'].value+form['entity.cardNo3'].value+form['entity.cardNo4'].value;
									  [**
									  if(card.length==15 && form['entity.cvc2'].value.length!=4)
		                                 msg = msg + "CVC2 必須輸入4碼.\n";
		                              else
		                              **]
		                                  if(card.length !=15 && form['entity.cvc2'].value.length!=3)
		                                     msg = msg + "CVC2 必須輸入3碼.\n";
		                            }
		                         }
                               	M2020178 CVC2異動為選擇性欄位  **/
                            }

                            if( form['entity.cvc2'].value != "" && !isNumber( form['entity.cvc2'].value ,true ) )
                               	msg = msg + "CVC2 必須為數字.\n";


                            if( form['entity.expireDate'].value == "" )
                                msg = msg + "有效日期必須輸入.\n";
                            else
                            {
							 	if(!isNumber( form['entity.expireDate'].value ,true) )
							 		msg = msg + "有效日期必須為數字.\n";
							 	else
							 	{
									var vDate=form['entity.expireDate'].value;
									if( isNaN( vDate ) )
										msg = msg + "有效日期必須為數字.\n";
									else
									{
										if(vDate.length!=4)
											msg = msg + "有效日期長度錯誤.\n";
										else
										{
				                        if( ! isDateValidate(form['entity.expireDate'].value) )
			                                 msg = msg + "有效日期必須大於等於系統日期.\n";
			                            if(! checkYYMMDateValid(form['entity.expireDate'].value))
			                                 msg = msg + "有效日期格式錯誤.\n";
										}
									}
								}

                            }

                            if( form['entity.purchaseAmt'].value == "" )
                                msg = msg + "金額必須輸入.\n";
                            else
                            {
                                if( !isNumber( form['entity.purchaseAmt'].value ,true ) )
                                      msg = msg + "金額必須為數字.\n";
                                else
                                {
                                    if( parseInt( form['entity.purchaseAmt'].value ,10 ) <= 0 )
                                        msg = msg + "金額必須大於0.\n";
                                    else
                                    	if ( form['entity.purchaseAmt'].value.indexOf(".") > -1 )
                                             msg = msg + "消費金額必須為整數值.\n";

                                }
                            }
                            /** 改由後端檢核較仔細
                            if(! CardNumCheck(form['entity.cardNo1'].value+form['entity.cardNo2'].value+form['entity.cardNo3'].value+form['entity.cardNo4'].value))
                                 msg = msg + "卡號:"+form['entity.cardNo1'].value+form['entity.cardNo2'].value+form['entity.cardNo3'].value+form['entity.cardNo4'].value+" 邏輯檢查錯誤!!\n ";
                            */
							if( form['entity.processDate'].value == "" )
							    msg = msg + "消費日期必須輸入.\n";
					        else
					            if (form['entity.processDate'].value != "" && String(form['entity.processDate'].value).length != 8)
					                msg = msg + "消費日期必須輸入8位.\n";
					            else
					                if (form['entity.processDate'].value != "" &&  String(form['entity.processDate'].value).length == 8 && !(JSIsDate(form['entity.processDate'].value)))
					                    msg = msg + "消費日期必須輸入有效之日期.\n";
					                else
					                    if ( ! isLessToday(form['entity.processDate'].value) )
					                           msg = msg + "消費日期必須小於等於系統日期.\n";

							if( form['entity.processTime'].value == "" )
								msg = msg + "消費時間必須輸入.\n";
					        else
					            if (form['entity.processTime'].value != "" && String(form['entity.processTime'].value).length != 6)
					                 msg = msg + "消費時間必須輸入6位.\n";
					            else
					            	if (!form['entity.processTime'].value.match (/^([0-9]|0[0-9]|1[0-9]|2[0-3])([0-5][0-9])([0-5][0-9])$/))
					            		msg = msg + "消費時間必須輸入有效之時間.\n";

                            if( msg.length > 12 )
                            {
                                alert( msg );
                                Event.stop( event );
                            }
                            else
                                showSubmitMessage();
                         }

                         function addFlagRadio()
                         {

                            if (form['entity.addFlag'][0].checked)
                            	return 0;
                            else
                            	return 1;
						 }

                         function isAlphaNumeric(testVar)
                         {
                            var test = testVar + "a";
                            re = /([A-Za-z0-9_])+/;
                            var found = test.match(re);
                            if (found== null || found[0] == null )
                                okay = false;
                            else
                            {
                                if (found[0] == test)
                                    okay = true;
                                else
                                    okay = false;
                            }

                            return okay;
                         }

                         function moveCard()
                         {
                          	if(form['entity.merchantNo'].value.length == 10)
                               form['entity.cardNo1'].focus();
                         }

                         function moveAmt()
                         {
                            if(form['entity.expireDate'].value.length == 4)
                               form['entity.purchaseAmt'].focus();
                         }

                         function autoFocus(step)
                         {
                             if ( step == 1 && form['entity.cardNo1'].value.length == 4 )
                                  form['entity.cardNo2'].focus();
                             if ( step == 2 && form['entity.cardNo2'].value.length == 4  )
                                  form['entity.cardNo3'].focus();
                             if ( step == 3 && form['entity.cardNo3'].value.length == 4  )
                                  form['entity.cardNo4'].focus();
                             if ( step == 4 && form['entity.cardNo4'].value.length == 4  )
                                  form['entity.cardNo5'].focus();
                             if ( step == 5&& form['entity.cardNo5'].value.length == 3  )
                                  form['entity.expireDate'].focus();
                         }

                         function JSIsDate(sDate)
                         {
                           yea=sDate.substr(0,4);
                           mon=sDate.substr(4,2);
                           da=sDate.substr(6,2);
                           if( !isNumber(parseInt(yea,10)) || !isNumber(parseInt(mon,10)) || !isNumber(parseInt(da,10)) )
                                return false;
                           if( (parseInt(yea,10) < 1899) || (parseInt(yea,10) > 2999) ) return false;
                           if( (parseInt(mon,10) < 1) || (parseInt(mon,10) > 12) ) return false;
                           if (parseInt(da,10) < 1) return false;
                           if (((parseInt(mon,10) == 1) || (parseInt(mon,10) == 3) || (parseInt(mon,10) == 5) || (parseInt(mon,10) == 7) ||
                                (parseInt(mon,10) == 8) || (parseInt(mon,10) == 10) || (parseInt(mon,10) == 12)) && (parseInt(da,10) > 31) )
                                 return false;
                           if( ((parseInt(mon,10) == 2) || (parseInt(mon,10) == 4) || (parseInt(mon,10) == 6) || (parseInt(mon,10) == 9) ||
                                (parseInt(mon,10) == 11)) && (parseInt(da,10) > 30) )
                                return false;
                           if(parseInt(mon,10) == 2)
                           {
                              if(( (parseInt(yea,10) % 4) == 0) && (parseInt(da,10) > 29) )
                                    return false;
                               else
                                  if((parseInt(yea,10) % 4 != 0) && (parseInt(da,10) > 28))
                                      return false;
                           }
                           return true;
                         }

                         function isLessToday(dateString)
                         {
                             var now = new Date();
                             var theday = new Date(dateString.substr(0,4)+"/"+
                                                   dateString.substr(4,2)+"/"+
                                                   dateString.substr(6,2));
                             if (theday <= now)
                                 return true;
                             else
                                 return false;
                         }

                         function isDateValidate(dateString)
                         {
				                	/* 20090201 , get current date fail , stephen */
				                	/* fixed 20090202 as */
				                    var now = new Date();
				                    var yy=(""+now.getYear()).substring(1);
				                    if(yy.length==1 && yy<10)
				                       yy="0"+yy;
				                    var mm =now.getMonth()+1;
				                    var myMM="";
				                    if(mm < 10) {
				                       myMM="0"+mm;
				                       }
				                    else
				                    {
				                    	 myMM=""+mm;
				                    	 }
				                    var today=yy+myMM;
				                    if (dateString >= today)
				                        return true;
				                    else
				                        return false;
                         }

                         function clean()
                         {
                             form['entity.merchantNo'].value = "";
                             form['entity.cardNo1'].value = "";
                             form['entity.cardNo2'].value = "";
                             form['entity.cardNo3'].value = "";
                             form['entity.cardNo4'].value = "";
                             form['entity.cardNo5'].value = "";
                             form['entity.expireDate'].value = "";
                             form['entity.purchaseAmt'].value = "";
                             form['entity.cvc2'].value = "";
                             form['entity.addFlag'][1].checked=true;
                             getProcessDateTime();
                         }

                         function getProcessDateTime()
                         {
                             var refreshDate = new Date();
                             var sYear = new String(refreshDate.getFullYear());
                             var sMonth = fillZero(new String(refreshDate.getMonth() + 1));
                             var sDay = fillZero(new String(refreshDate.getDate()));
                             var sHours = fillZero(new String(refreshDate.getHours()));
                             var sMinutes = fillZero(new String(refreshDate.getMinutes()));
                             var sSconds = fillZero(new String(refreshDate.getSeconds()));
                             form['entity.processDate'].value = sYear+sMonth+sDay;
                             form['entity.processTime'].value = sHours+sMinutes+sSconds;
                         }

                         function fillZero(obj)
                         {
            	             if (obj.length < 2)
            	                 return "0"+obj;
            	             return obj;
                         }

                         function getMchtInfo()
                         {
	                         if(form['entity.merchantNo'].value.length >= 10 &&
	                            form['entity.merchantNo'].value != "0000000000")
	                         {
	                            queryFieldValueByAjax( {
	                            url: "../ajax/queryMchtBase.do",
	                            params: {masterId: form['entity.merchantNo'].value},
 		                        targets:
 		                        [
 		                        {name: 'merchantName', field: form['merchantName'], displayBlock: $('merchantNameArea')}
 		                        ],
 	                            callbackFunction: notThingToDo
                                });
	                         }
                         }

                         function getBankInfo()
                         {
                             var cardNo = form['entity.cardNo1'].value.trim() + form['entity.cardNo2'].value.trim()
                                        + form['entity.cardNo3'].value.trim() + form['entity.cardNo4'].value.trim()
                                        + form['entity.cardNo5'].value.trim() ;
                             if (cardNo.length >= 13)
                             {
                                 queryFieldValueByAjax( {
	                             url: "../ajax/queryBankBase.do",
	                             params: {cardNo: cardNo},
 		                         targets:
 		                         [
 		                         {name: 'bankInfo', field: form['bankInfo'], displayBlock: $('bankInfoArea')}
 		                         ],
 	                             callbackFunction: notThingToDo
                                 });
                             }
                         }


                         function notThingToDo()
                         {

                         }

                         function dateTimeDisabled()
                         {
                         	if(addFlagRadio()==1)
                         	{
                         	   form['entity.processDate'].disabled=true;
                         	   form['entity.processTime'].disabled=true;
                         	   form['entity.authorizeReason'].value="C1";
                         	   document.getElementById("_authDate").style.visibility = "hidden";
                         	   getProcessDateTime();
                         	}
                         	else
                         	{
                         	   form['entity.processDate'].disabled=false;
                         	   form['entity.processTime'].disabled=false;
                         	   form['entity.authorizeReason'].value="T2";
                         	   document.getElementById("_authDate").style.visibility = "visible";
                         	}
                         }

				         function initialForm()
				         {
				 	         var form = document.forms[0];
				 	         form.setAttribute("autocomplete","off");
					         $(form).observe("submit", onSubmitHandler );
					         $(form).getElements('entity.addFlag').each(
					             function(obj)
					             {
					               obj.observe( "click", dateTimeDisabled );
					             }
					         );
					         document.getElementById("_authDate").style.visibility = "hidden";
					         $('defaultSubmitButton').focus();
					         getProcessDateTime();
					         /*clean();*/
				         }

				         EdsEvent.addOnLoad( initialForm );
                      </script>
                      </div>
            </div>
    </body>
</html>

