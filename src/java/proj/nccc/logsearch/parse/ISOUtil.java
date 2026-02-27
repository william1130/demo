package proj.nccc.logsearch.parse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import com.edstw.process.ProcessException;
import com.hp.nccc.iso8583.common.CommonFunction;
import com.hp.nccc.iso8583.core.ISO8583Factory;
import com.hp.nccc.iso8583.core.ISO8583Fields;
import com.hp.nccc.iso8583.core.ISO8583VO;
import com.hp.nccc.iso8583.core.ISOCheckSum;
import com.hp.nccc.iso8583.core.ISOChip;
import com.hp.nccc.iso8583.core.ISOTable;
import com.hp.nccc.iso8583.core.ISOToken;
import com.hp.nccc.iso8583.core.ISOType;
import com.hp.nccc.iso8583.core.ISOValue;
import com.hp.nccc.iso8583.exception.ISO8583ParseException;
import com.hp.nccc.iso8583.parse.ConfigParse;

import proj.nccc.logsearch.beans.ParamBean;
import proj.nccc.logsearch.vo.IsoInfoValue;

public class ISOUtil {

	private static final ISOUtil instance = new ISOUtil();

	//	atslog_raw_log.raw_type 對映的類型
	private static Map<String, String> headerKeyMap = new HashMap<String, String>();

	//	atslog_raw_log.raw_type 對映的XML檔案
	private static Map<String, String> xmlMap = new HashMap<String, String>();
	
	//UPLAN-M2018187
	private static Map<String, String> upMap = new HashMap<String, String>();

	//	存放已產生的MessageFactoryObject, Key=Xml檔名
	private static Map<String, ISO8583Factory<ISO8583VO>> factoryMap = null;

	static {

		headerKeyMap.put("FROM EDC", "EDC");
		headerKeyMap.put("TO EDC", "EDC");
		headerKeyMap.put("FROM B24", "BASE24");
		headerKeyMap.put("TO B24", "BASE24");
		headerKeyMap.put("FROM ASM", "ASM");
		headerKeyMap.put("TO ASM", "ASM");
		headerKeyMap.put("FROM TMS", "TMS");
		headerKeyMap.put("TO TMS", "TMS");
		headerKeyMap.put("FROM EDC CONFIRM", "EDC");
		headerKeyMap.put("TO EDC CONFIRM", "EDC");
		headerKeyMap.put("TO SP SWITCH CONFIRM", "SPSW");
		headerKeyMap.put("FROM SP SWITCH", "SPSW");
		headerKeyMap.put("TO SP SWITCH", "SPSW");
		//20170310 票證
		headerKeyMap.put("FROM TKS SWITCH", "TKS");
		headerKeyMap.put("TO TKS SWITCH", "TKS");
		headerKeyMap.put("FORM AE", "AE");
		headerKeyMap.put("TO AE", "AE");
//		UPLAN-M2018187
		headerKeyMap.put("FROM UPLAN", "BASE24");
		headerKeyMap.put("TO UPLAN", "BASE24");
		// M2020020 ESC
		headerKeyMap.put("FROM ESC", "ESC");
		headerKeyMap.put("TO ESC", "ESC");
		// M2020020 DCC
		headerKeyMap.put("FROM DCC", "DCC");
		headerKeyMap.put("TO DCC", "DCC");

		xmlMap.put("FROM EDC", "ISO8583_EDC.xml");
		xmlMap.put("TO EDC", "ISO8583_EDC.xml");
		xmlMap.put("FROM B24", "ISO8583_B24.xml");
		xmlMap.put("TO B24", "ISO8583_B24.xml");
		xmlMap.put("FROM ASM", "ISO8583_EDC.xml");
		xmlMap.put("TO ASM", "ISO8583_EDC.xml");
		xmlMap.put("FROM TMS", "ISO8583_TMS.xml");
		xmlMap.put("TO TMS", "ISO8583_TMS.xml");
		xmlMap.put("RFES", "ISO8583_RFES.xml");
		xmlMap.put("FROM EDC CONFIRM", "ISO8583_EDC.xml");
		xmlMap.put("TO EDC CONFIRM", "ISO8583_EDC.xml");
		xmlMap.put("TO SP SWITCH CONFIRM", "ISO8583_SPSW.xml");
		xmlMap.put("FROM SP SWITCH", "ISO8583_SPSW.xml");
		xmlMap.put("TKS SWITCH", "ISO8583_TKS.xml");
		xmlMap.put("TO SP SWITCH", "ISO8583_SPSW.xml");
		xmlMap.put("AE",  "ISO8583_EDC_AE.xml");
//		M2016169
		xmlMap.put("GEDC",  "ISO8583_EDC_GEDC.xml");
		//UPLAN-M2018187
		xmlMap.put("FROM UPLAN", "ISO8583_B24.xml");
		xmlMap.put("TO UPLAN", "ISO8583_B24.xml");
		// M2020020 ESC
		xmlMap.put("FROM ESC", "ISO8583_ESC.xml");
		xmlMap.put("TO ESC", "ISO8583_ESC.xml");
		// M2020020 DCC
		xmlMap.put("FROM DCC", "ISO8583_DCC.xml");
		xmlMap.put("TO DCC", "ISO8583_DCC.xml");
		
		upMap.put("01", "優計畫-優惠訊息(Coupon Information)");
		upMap.put("02", "優計畫-折價後金額(Discounted Amount)");
		upMap.put("03", "優計畫-優惠金額(Preferential Amount)");
		upMap.put("04", "優計畫-備註訊息(Remarks column information)");
	}

	/**
	 * @return
	 */
	public static ISOUtil getInstance() {
		return instance;
	}

	/**
	 * to Parse ISO Data
	 * @return  IsoValue List
	 * @throws ProcessException
	 * @param isoHeader  atslog_raw_log.raw_type
	 * @param data  atslog_raw_log.raw_data
	 * @throws ISO8583ParseException 
	 */
	@SuppressWarnings("unchecked")
	public List<IsoInfoValue> parse(String isoHeader, String edcSpec, String data, boolean showMaskCardNo, String cardType) throws ProcessException, ISO8583ParseException {

		List<IsoInfoValue> resultList = new ArrayList<IsoInfoValue>();

		try {

			byte[] byteData = null;
			if(headerKeyMap.get(isoHeader).endsWith("BASE24")){
				byteData = data.getBytes("BIG5");
			}else{
				byteData = CommonFunction.hexDecode(data);
			}
			ISO8583Factory<ISO8583VO> factory = getIFACTInstence(isoHeader, edcSpec, cardType);
			factory.setIsTraceEnable(true);
			/*CommonFunction.bytesToHex(byteData)*/
			ISO8583VO vo = factory.parseISO(byteData, byteData.length);

			boolean isSpsw = false;
			//	Add header line
			IsoInfoValue iv = new IsoInfoValue("V", "Header", "Message Header");
			if (vo.isTPDU()) {
				iv.setValue(vo.getTpdu().toString());
			} else {
				iv.setValue(vo.getIsoHeader());
				
			}
			if(iv.getValue().toString().indexOf("SPI") != -1){
				isSpsw = true;
			} 
			resultList.add(iv);

			//	Add message-type line
			String type = vo.getMti().toString();
			StringBuffer sbType = new StringBuffer(type);

			while (sbType.length() < 4)
				sbType.insert(0, "0");

			iv = new IsoInfoValue("V", "MTI", "Message Type Identifier");
			iv.setValue(sbType.toString());
			resultList.add(iv);

			String headerKey = (String) headerKeyMap.get(isoHeader);

			if (headerKey == null)
				throw new ProcessException("Undefined Header:" + isoHeader);

			String processCode = "";
			for (int i = 1; i <= 128; i++) {

				if (!vo.hasField(i))
					continue;

				iv = new IsoInfoValue("V", String.valueOf(i), vo.getField(i).getDescription());
				resultList.add(iv);

				if (vo.getField(i).getType() == ISOType.LLLTABLES) {
					iv.setLength(String.valueOf(vo.getField(i).getLength()));
					List<IsoInfoValue> list = new ArrayList<IsoInfoValue>();
					boolean isET = false;
					for (ISOTable table : (List<ISOTable>) (vo.getField(i).getValue())) {
						IsoInfoValue temp = new IsoInfoValue("V", "", "");
						temp.setTitle(table.getId());
						temp.setLength(String.valueOf(table.getLength()));
						List<IsoInfoValue> sublist = new ArrayList<IsoInfoValue>();
						List<IsoInfoValue> subSublist = new ArrayList<IsoInfoValue>();
						//M2016144電子票證需求
						if( (!table.getId().startsWith("SW") && table.getId().endsWith("01")) || table.getId().endsWith("02") ){
							if(processCode.equals("515300"))
								temp.setValue("TMS");
							else
								temp.setValue("button");
							isET = true;
						}else{
							if(table.getId().startsWith("IP") || table.getId().startsWith("EZ") || table.getId().startsWith("HC") || table.getId().startsWith("IC"))
								isET = true;
							
							for (int j = 1; j <= 128; j++) {	
								//M2016050 優惠平台需求
								if (table.getFields().hasField(j)) {
									if (table.getFields().getField(j).getType() == ISOType.CHECKSUM) {
										ISOCheckSum checkSum = (ISOCheckSum) table.getFields().getField(j).getValue();
										for (int k = 1; k <= 128; k++) {
											if(checkSum.getFields()!=null){
												if (checkSum.getFields().hasField(k)) {
													IsoInfoValue subTemp = new IsoInfoValue("V", "", "");
													subTemp.setValue(StringEscapeUtils.escapeHtml4(checkSum.getFields().getField(k).toString()));
													subTemp.setDesc(checkSum.getFields().getField(k).getDescription());
													sublist.add(subTemp);
												}
											}
										}
										IsoInfoValue subTemp = new IsoInfoValue("V", "", "");
										subTemp.setValue(StringEscapeUtils.escapeHtml4(CommonFunction.hexEncode(checkSum.getCheckSum())));
										subTemp.setDesc(table.getFields().getField(j).getDescription());
										sublist.add(subTemp);
									} else if(table.getFields().getField(j).getType() == ISOType.ZIP){ 
										ISO8583Fields fields = (ISO8583Fields) (table.getFields().getField(j).getValue());
										for (int jj = 1; jj <= 128; jj++) {
											if (fields.hasField(jj)) {
												ISOCheckSum checkSum = (ISOCheckSum) fields.getField(jj).getValue();
												for (int jjj = 1; jjj <= 128; jjj++) {
													if(checkSum.getFields()!=null){
														if (checkSum.getFields().hasField(jjj)) {
															IsoInfoValue subSunTemp = new IsoInfoValue("V", "", "");
															subSunTemp.setValue(StringEscapeUtils.escapeHtml4(checkSum.getFields().getField(jjj).toString()));
															subSunTemp.setDesc(checkSum.getFields().getField(jjj).getDescription());
															subSublist.add(subSunTemp);
														}
													}
												}
											}
										}
										IsoInfoValue subTemp = new IsoInfoValue("V", "", "");
										subTemp.setType("ZIP");
										subTemp.setValue(subSublist);
										subTemp.setDesc(table.getFields().getField(j).getDescription());
										sublist.add(subTemp);
									} else if(table.getId().equals("UP")) {  //UPLAN-M2018187
										String str = table.getFields().getField(j).toString().replace(" ","");
										while(str.length() > 0) {
											String tag = str.substring(0, 4);
											str = str.substring(4);
											int tagLen = Integer.parseInt(str.substring(0, 4));
											str = str.substring(4);
											String tagVale = str.substring(0,tagLen*2);
											str = str.substring(tagLen*2);
											IsoInfoValue subTemp = new IsoInfoValue("V", "", "");
											subTemp.setTitle(StringEscapeUtils.escapeHtml4(new String(CommonFunction.hexDecode(tag))));
											subTemp.setValue(StringEscapeUtils.escapeHtml4(new String(CommonFunction.hexDecode(tagVale))));
											subTemp.setLength(String.valueOf(tagLen));
											subTemp.setDesc(upMap.get(subTemp.getTitle()));
											sublist.add(subTemp);
										}
									}else {
										IsoInfoValue subTemp = new IsoInfoValue("V", "", "");
										subTemp.setValue(StringEscapeUtils.escapeHtml4(table.getFields().getField(j).toString()));
										subTemp.setDesc(table.getFields().getField(j).getDescription());
										sublist.add(subTemp);
									}
								}
							}	
							temp.setValue(sublist);
						}
						temp.setDesc(table.getDescription());
						list.add(temp);
					}
					if(isET){
						iv.setType("ET");
					}else{
						iv.setType("TT");
					}
					iv.setValue(list);
				} else if (vo.getField(i).getType() == ISOType.LLLCHIP) {
					iv.setLength(String.valueOf(vo.getField(i).getLength()));
					List<IsoInfoValue> list = new ArrayList<IsoInfoValue>();
					for (ISOChip chip : (List<ISOChip>) (vo.getField(i).getValue())) {
						IsoInfoValue temp = new IsoInfoValue("V", "", "");
						temp.setTitle(CommonFunction.bytesToHex(chip.getTag(), " "));
						temp.setLength(String.valueOf(chip.getLength()));
						temp.setValue(StringEscapeUtils.escapeHtml4(CommonFunction.bytesToHex(chip.getData(), " ")));
						temp.setDesc(chip.getDesc());
						list.add(temp);
					}
					iv.setType("TC");
					iv.setValue(list);
				} else if (vo.getField(i).getType() == ISOType.LLLTOKENS || vo.getField(i).getType() == ISOType.LLLLTOKENS) {
					// M2023015_R112022_授權欄位調整p57取代p63欄位
					iv.setLength(String.valueOf(vo.getField(i).getLength()));
					List<IsoInfoValue> list = new ArrayList<IsoInfoValue>();
					for (ISOToken token : (List<ISOToken>) (vo.getField(i).getValue())) {
						IsoInfoValue temp = new IsoInfoValue("V", "", "");
						temp.setTitle(token.getTknId());
						temp.setLength(String.valueOf(token.getLgth()));
						List<IsoInfoValue> sublist = new ArrayList<IsoInfoValue>();
						for (int j = 1; j <= 128; j++) {
							if (token.getFields().hasField(j)) {
								IsoInfoValue subTemp = new IsoInfoValue("V", "", "");
								if(token.getFields().getField(j).getType() == ISOType.LVAR){
									ISOValue<String[]> v = token.getFields().getField(j);
									ISOValue<String> vv = token.getFields().getField(j); 
									int scriptLength = 0;
									boolean isByte = false;
									try{
										scriptLength = v.getLength() / v.getValue().length;
									}catch (Exception e){
										scriptLength = vv.getLength() / vv.getValue().length();
										isByte = true;
									}
									StringBuffer sb = new StringBuffer();
									if(!isByte){
										for(String sdata:v.getValue()){
											sb.append(sdata);
										}
									}else{
										sb.append( vv.getValue());
									}
									subTemp = new IsoInfoValue("V", "", "LENGTH");
									subTemp.setValue(String.valueOf(scriptLength));
									sublist.add(subTemp);
									subTemp = new IsoInfoValue("V", "", "ISS-SCRIPT-DATA");
									subTemp.setValue(StringEscapeUtils.escapeHtml4(sb.toString()));
								}else if(token.getFields().getField(j).getType() == ISOType.REDEF){
									ISO8583Fields fields = (ISO8583Fields) (token.getFields().getField(j).getValue());
									for (int jj = 1; jj <= 128; jj++) {
										if (fields.hasField(jj)) {
											subTemp = new IsoInfoValue("V", "", "ISS-SCRIPT-DATA");
											subTemp.setValue(StringEscapeUtils.escapeHtml4(fields.getField(jj).toString()));
											subTemp.setDesc(fields.getField(jj).getDescription());
											sublist.add(subTemp);
										}
									}
								}else{
									subTemp.setValue(StringEscapeUtils.escapeHtml4(token.getFields().getField(j).toString()));
								}
								
								if(token.getFields().getField(j).getType() != ISOType.REDEF){
									subTemp.setDesc(token.getFields().getField(j).getDescription());
									sublist.add(subTemp);
								}
							}
						}
						temp.setValue(sublist);
						temp.setDesc(token.getDescription());
						list.add(temp);
					}
					iv.setType("TK");
					iv.setValue(list);
				} else if (vo.getField(i).getType() == ISOType.LLLVAR && vo.getField(i).isNestedValue()) {
					iv.setLength(String.valueOf(vo.getField(i).getLength()));
					List<IsoInfoValue> list = new ArrayList<IsoInfoValue>();
					ISO8583Fields fields = (ISO8583Fields) (vo.getField(i).getValue());
					if(fields.getField(1).getType() == ISOType.REDEF){
						ISO8583Fields field63 = (ISO8583Fields) (fields.getField(1).getValue());
						for (int k = 1; k <= 128; k++) {
							if (field63.hasField(k)) {
								IsoInfoValue subTemp = new IsoInfoValue("V", "", "");
	//							20170502 針對票證需求欄位63
								String[] descAry = field63.getField(k).getDescription().split("_");
								if(descAry.length > 1){
									subTemp.setDesc(descAry[0]);
									subTemp.setTksName(descAry[1]);
									iv.setType("TLT");
								}else{
									subTemp.setDesc(descAry[0]);
									iv.setType("TL");
								}
								subTemp.setValue(field63.getField(k).getValue());
								list.add(subTemp);
							}
						}
					}else{
						for (int j = 1; j <= 128; j++) {
							if (fields.hasField(j)) {
								IsoInfoValue subTemp = new IsoInfoValue("V", "", "");

								subTemp.setDesc(fields.getField(j).getDescription());

								if (fields.getField(j).getValue() instanceof byte[]) {													
									subTemp.setValue(StringEscapeUtils.escapeHtml4(fields.getField(j).toString()));
								}else{
									subTemp.setValue(fields.getField(j).getValue());
								}
								list.add(subTemp);
							}
						}
						iv.setType("TL");
					}
					iv.setValue(list);
				}else if (showMaskCardNo && ( vo.getField(i).getType() == ISOType.LLZ || (i == 35 && !vo.isTPDU())) && isoHeader.indexOf("SP") == -1) {
					String track2Data = (String) vo.getField(i).getValue();
					String s = track2Data.replaceAll("=", "D");
					int index = s.indexOf("D");
					if (index >= 0) {
						iv.setValue(getCardNumMask(s.substring(0, index)) + s.substring(index, s.length()));
					} else {
						iv.setValue(getCardNumMask(track2Data));
					}
				}else if (vo.getField(i).getType() == ISOType.LLBIN || vo.getField(i).getType() == ISOType.LLLBIN) {
					
					List<IsoInfoValue> list = new ArrayList<IsoInfoValue>();
					if(vo.getField(i).isNestedValue()){
						iv.setLength(String.valueOf(vo.getField(i).getLength()));
						ISO8583Fields fields = (ISO8583Fields) (vo.getField(i).getValue());
						if(fields.getField(1).getType() == ISOType.REDEF){
							ISO8583Fields field55 = (ISO8583Fields) (fields.getField(1).getValue());
							for (int k = 1; k <= 128; k++) {
								if (field55.hasField(k)) {
									IsoInfoValue subTemp = new IsoInfoValue("V", "", "");
									subTemp.setValue(StringEscapeUtils.escapeHtml4(field55.getField(k).toString()));
									subTemp.setDesc(field55.getField(k).getDescription());
									list.add(subTemp);
								}
							}
						}else{
							for (int j = 1; j <= 128; j++) {
								if (fields.hasField(j)) {
									IsoInfoValue subTemp = new IsoInfoValue("V", "", "");
									subTemp.setValue(StringEscapeUtils.escapeHtml4(fields.getField(j).toString()));
									subTemp.setDesc(fields.getField(j).getDescription());
									list.add(subTemp);
								}
							}
						}
						iv.setType("SC");
						iv.setValue(list);
					}else{
						if (vo.getField(i).getValue() instanceof byte[]) {
							if(i == 60 && vo.getField(i).getLength() == 6){
								iv.setValue(StringEscapeUtils.escapeHtml4(new String(CommonFunction.hexDecode(vo.getField(i).toString().replace(" ","")))));
							}else{
								iv.setValue(StringEscapeUtils.escapeHtml4(vo.getField(i).toString()));
							}
							
						}
						
					}
					

					
				}else if (vo.getField(i).getType() == ISOType.SMARTPAYTAG) {
					iv.setLength(String.valueOf(vo.getField(i).getLength()));
					List<IsoInfoValue> list = new ArrayList<IsoInfoValue>();
					for (ISOChip chip : (List<ISOChip>) (vo.getField(i).getValue())) {
						IsoInfoValue temp = new IsoInfoValue("V", "", "");
						if(isSpsw){
							temp.setTitle(CommonFunction.bytesToHex(chip.getTag(), " "));
						}else{
							temp.setTitle(new String(chip.getTag()));
						}
						temp.setLength(String.valueOf(chip.getLength()));
						if(temp.getTitle().equals("S2") || isSpsw){
							temp.setValue(StringEscapeUtils.escapeHtml4(CommonFunction.bytesToHex(chip.getData(), " ")));
						}else{
							temp.setValue(new String(chip.getData()));
						}
						temp.setDesc(chip.getDesc());
						list.add(temp);
					}
					iv.setType("ST");
					iv.setValue(list);
				}else if (vo.getField(i).getValue() instanceof ISO8583Fields) { //for smartpay field 63
					iv.setLength(String.valueOf(vo.getField(i).getLength()));
					List<IsoInfoValue> list = new ArrayList<IsoInfoValue>();
					ISO8583Fields fields = (ISO8583Fields) (vo.getField(i).getValue());
					for (int j = 1; j <= 128; j++) {
						if (fields.hasField(j)) {
							if(fields.getField(j).getValue() instanceof ISO8583Fields ){
								ISO8583Fields fields2 = (ISO8583Fields) (fields.getField(j).getValue());
								for (int k = 1; k <= 128; k++) {
									if (fields2.hasField(k)) {
										IsoInfoValue subTemp = new IsoInfoValue("V", "", "");
										subTemp.setValue(StringEscapeUtils.escapeHtml4(fields2.getField(k).toString()));
										subTemp.setDesc(fields2.getField(k).getDescription());
										list.add(subTemp);
									}
								}
								continue;
							}
							IsoInfoValue subTemp = new IsoInfoValue("V", "", "");
							
							if(i == 35 && showMaskCardNo  && isoHeader.indexOf("SP") == -1){
								String track2Data = StringEscapeUtils.escapeHtml4(fields.getField(j).toString());
								String s = track2Data.replaceAll("=", "D");
								int index = s.indexOf("D");
								if (index >= 0) {
									subTemp.setValue(getCardNumMask(s.substring(0, index)) + s.substring(index, s.length()));
								} else {
									subTemp.setValue(getCardNumMask(track2Data));
								}
							}else{
							
								subTemp.setValue(StringEscapeUtils.escapeHtml4(fields.getField(j).toString()));
							}
							subTemp.setDesc(fields.getField(j).getDescription());
							list.add(subTemp);
						}
					}
					iv.setType("TLL");
					iv.setValue(list);
				} else {
					if(i==3){
						processCode = StringEscapeUtils.escapeHtml4(vo.getField(i).toString());
					}
					iv.setValue(StringEscapeUtils.escapeHtml4(vo.getField(i).toString()));
					
				}
			}
		} catch(ISO8583ParseException e){
			throw e;
			/*super.saveError(request, "error", "action.fail",
					new Object[] { String.format("error message[%s]\ndetail message[%s]",
					 e.getMessage(),e.getTraceInfo() == null ? "" : e.getTraceInfo().toString())});
			return mapping.findForward(ProjConstants.FAIL_KEY);*/
		}catch (Exception e) {

			//e.printStackTrace();
			throw new ProcessException(e);
		}

		return resultList;
	}
	
	/**
	 * showETData
	 * @return  field 56 Data
	 * @throws ProcessException
	 * @param data  atslog_raw_log.raw_data
	 * @throws ISO8583ParseException 
	 */
	//M2016144電子票證需求
	@SuppressWarnings("unchecked")
	public String showETData(String isoHeader, String edcSpec, String data, String tagName) throws ProcessException, ISO8583ParseException {

		String etRawData = "";

		try {

			byte[] byteData = null;
			if(headerKeyMap.get(isoHeader).endsWith("BASE24")){
				byteData = data.getBytes("BIG5");
			}else{
				byteData = CommonFunction.hexDecode(data);
			}
			ISO8583Factory<ISO8583VO> factory = getIFACTInstence(isoHeader, edcSpec, "TKS");
			factory.setIsTraceEnable(true);

			ISO8583VO vo = factory.parseISO(byteData, byteData.length);

			for (int i = 1; i <= 128; i++) {

				if (!vo.hasField(i) || i != 56){
					continue;
				}else{
					if (vo.getField(i).getType() == ISOType.LLLTABLES) {
						for (ISOTable table : (List<ISOTable>) (vo.getField(i).getValue())) {		
							if(tagName.equals(table.getId())){
								for (int j = 1; j <= 128; j++) {
									if (table.getFields().hasField(j)) {
											etRawData = etRawData+StringEscapeUtils.escapeHtml4(table.getFields().getField(j).toString());			
										
									}
								}
							}
						}
					}
				}
				
			}
		} catch(ISO8583ParseException e){
			throw e;
		}catch (Exception e) {
			throw new ProcessException(e);
		}

		return etRawData.replace(" ","");
	}
	
	
	/**
	 * praseETData
	 * @return  field 56 Data
	 * @throws ProcessException
	 * @param data  atslog_raw_log.raw_data
	 * @throws ISO8583ParseException 
	 */
	//M2016144電子票證需求
	@SuppressWarnings("unchecked")
	public List<IsoInfoValue> parseETData(String isoHeader, String edcSpec, String data, String cardType) throws ProcessException, ISO8583ParseException {

		List<IsoInfoValue> resultList = new ArrayList<IsoInfoValue>();

		try {

			byte[] byteData = null;
			if(headerKeyMap.get(isoHeader).endsWith("BASE24")){
				byteData = data.getBytes("BIG5");
			}else{
				byteData = CommonFunction.hexDecode(data);
			}
			ISO8583Factory<ISO8583VO> factory = getIFACTInstence(isoHeader, edcSpec, cardType);
			factory.setIsTraceEnable(true);

			ISO8583VO vo = factory.parseISO(byteData, byteData.length);

			for (int i = 1; i <= 128; i++) {

				if (!vo.hasField(i) && i != 56){
					continue;
				}else{
					if (vo.getField(i).getType() == ISOType.LLLTABLES) {
						for (ISOTable table : (List<ISOTable>) (vo.getField(i).getValue())) {							
							for (int j = 1; j <= 128; j++) {
								if (table.getFields().hasField(1)) {
									// TODO 解析tms參數下載電文
//									etRawData = StringEscapeUtils.escapeHtml4(table.getFields().getField(j).toString());								
								}
							}	
						}
					}
				}
				
			}
		} catch(ISO8583ParseException e){
			throw e;
		}catch (Exception e) {
			throw new ProcessException(e);
		}

		return resultList;
	}
	
	/**
	 * M2020020 DCC Table ID "X"
	 * parseDCCData
	 * @return  field 59 Data
	 * @throws ProcessException
	 * @param data  ldss_raw_log.raw_data
	 * @throws ISO8583ParseException 
	 */
	@SuppressWarnings("unchecked")
	public List<IsoInfoValue> parseDCCData(String isoHeader, String data)
			throws ProcessException, ISO8583ParseException {

		List<IsoInfoValue> resultList = new ArrayList<IsoInfoValue>();

		try {

			byte[] byteData = null;
			if (headerKeyMap.get(isoHeader).endsWith("BASE24")) {
				byteData = data.getBytes("BIG5");
			} else {
				byteData = CommonFunction.hexDecode(data);
			}
			ISO8583Factory<ISO8583VO> factory = getIFACTInstence(isoHeader, "", "");
			factory.setIsTraceEnable(true);

			ISO8583VO vo = factory.parseISO(byteData, byteData.length);

			for (int i = 1; i <= 128; i++) {

				if (!vo.hasField(i) && i != 59) {
					continue;
				} else {
					if (vo.getField(i).getType() == ISOType.LLLTABLES) {
						IsoInfoValue subTemp = null;
						int k = 0;
						for (ISOTable table : (List<ISOTable>) (vo.getField(i).getValue())) {
							for (int j = 1; j <= 128; j++) {
								if (table.getFields().getField(j) != null) {
									String xData = StringEscapeUtils
											.escapeHtml4(table.getFields().getField(j).toString());
									if (StringUtils.isBlank(xData)) {
										continue;
									}
									if (Character.isLetter(xData.charAt(0))) {
										if (subTemp != null) {
											resultList.add(subTemp);
										}

										subTemp = new IsoInfoValue();
										subTemp.setType(xData);
										k = 1;
									} else if (k == 2) {
										subTemp.setTitle(xData);
									} else if (k == 3) {
										subTemp.setDesc(xData);
									} else if (k == 4) {
										subTemp.setValue(xData);
									}
									k += 1;
								} else {
									if (subTemp != null) {
										resultList.add(subTemp);
										break;
									}
								}
							}
						}
					}
				}

			}
		} catch (ISO8583ParseException e) {
			throw e;
		} catch (Exception e) {
			throw new ProcessException(e);
		}

		return resultList;
	}
	
	/**
	 * M2020020 ESC Table ID "E1","E2"
	 * parseE1E2Data
	 * @return  field 59 Data
	 * @throws ProcessException
	 * @param data  ldss_raw_log.raw_data
	 * @throws ISO8583ParseException 
	 */
	@SuppressWarnings("unchecked")
	public String parseE1E2Data(String isoHeader, String data) throws ProcessException, ISO8583ParseException {

		String rawData = "";

		try {

			byte[] byteData = null;
			if (headerKeyMap.get(isoHeader).endsWith("BASE24")) {
				byteData = data.getBytes("BIG5");
			} else {
				byteData = CommonFunction.hexDecode(data);
			}
			ISO8583Factory<ISO8583VO> factory = getIFACTInstence(isoHeader, "", "");
			factory.setIsTraceEnable(true);

			ISO8583VO vo = factory.parseISO(byteData, byteData.length);

			for (int i = 1; i <= 128; i++) {

				if (!vo.hasField(i) && i != 59) {
					continue;
				} else {
					if (vo.getField(i).getType() == ISOType.LLLTABLES) {
						for (ISOTable table : (List<ISOTable>) (vo.getField(i).getValue())) {
							for (int j = 1; j <= 128; j++) {
								if (table.getFields().getField(j) != null
										&& (table.getId().equals("E1") || table.getId().equals("E2"))) {
									rawData = rawData + table.getFields().getField(j).toString().replace(" ", "");
								}
							}
						}
					}
				}

			}
		} catch (ISO8583ParseException e) {
			throw e;
		} catch (Exception e) {
			throw new ProcessException(e);
		}

		return rawData;
	}

	public synchronized static ISO8583Factory<ISO8583VO> getIFACTInstence(String isoHeader, String edcSpec, String cardType) {
		String key = (String) xmlMap.get(isoHeader);
		
//		if("EDC".equals(isoHeader) && "RFES".equals(edcSpec)){
		if(isoHeader.endsWith("EDC") && "RFES".equals(edcSpec)){
				key = xmlMap.get(edcSpec);
		}
		
		if(isoHeader.endsWith("EDC") && "AE".equals(edcSpec)){
			key = xmlMap.get(edcSpec);
		}
		
		if(isoHeader.endsWith("EDC") && "GEDC".equals(edcSpec)){
			key = xmlMap.get(edcSpec);
		}
		
		/*
		 * 20170313 加入卡別判斷
		 * M2022086_收付訊息整合平台
		 */
		HashSet<String> tksCardTypeSet = new HashSet<String>();
		for (String ewallet : ParamBean.getInstance().geteWalletNameMap().keySet()) {
			if ("S".equals(ewallet)) continue;
			tksCardTypeSet.add(ewallet);
		}
		tksCardTypeSet.add("TKS");
		if(tksCardTypeSet.contains(cardType) && isoHeader.indexOf("SP") > 0 ){
			key = xmlMap.get("TKS SWITCH");
		}

		if (factoryMap == null){
			factoryMap = new HashMap<String, ISO8583Factory<ISO8583VO>>();
		}
		
		ISO8583Factory<ISO8583VO> IFACT = factoryMap.get(key);

		if (IFACT == null) {
			IFACT = new ISO8583Factory<ISO8583VO>();
			try {
				IFACT = ConfigParse.createFromClasspathConfig(key);
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			factoryMap.put(key, IFACT);
		}
		return IFACT;
	}

	public static String getCardNumMask(String cardNum) {
		if (cardNum == null)
			return cardNum;
		if (cardNum.length() <= 6)
			return cardNum;

		StringBuffer sb = new StringBuffer(cardNum);
		int end = sb.length() > 12 ? 12 : sb.length();

		for (int i = 6; i < end; i++)
			sb.setCharAt(i, '*');

		return sb.toString();
	}
	
	public static String getCVC2Mask(String cvc2) {
		if (cvc2 == null)
			return cvc2;
		if (cvc2.length() <= 2)
			return cvc2;

		StringBuffer sb = new StringBuffer(cvc2);
		int end = sb.length() > 3 ? 3 : sb.length();

		for (int i = 1; i < end; i++)
			sb.setCharAt(i, '*');

		return sb.toString();
	}
}