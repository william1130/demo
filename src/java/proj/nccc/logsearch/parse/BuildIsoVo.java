package proj.nccc.logsearch.parse;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.hp.nccc.iso8583.common.CommonFunction;
import com.hp.nccc.iso8583.common.ISO8583Context.MTIClass;
import com.hp.nccc.iso8583.common.ISO8583Context.MTIMode;
import com.hp.nccc.iso8583.core.ISO8583Factory;
import com.hp.nccc.iso8583.core.ISO8583Fields;
import com.hp.nccc.iso8583.core.ISO8583VO;
import com.hp.nccc.iso8583.core.ISOTable;

import proj.nccc.logsearch.beans.MyDateUtil;
import proj.nccc.logsearch.beans.Util;
import proj.nccc.logsearch.vo.NcccParm;

public class BuildIsoVo {

	private static ISO8583Factory<ISO8583VO> factory = new ISO8583Factory<ISO8583VO>("ISO8583_EDC_MA.xml");

	public BuildIsoVo() {
	}

	public ISO8583VO buildResponse(byte[] buf) throws Exception, ParseException {
		return factory.parseISO(buf, buf.length);
	}

	public byte[] getByteArray(ISO8583VO vo) {
		return factory.writeData(vo);
	}

	@SuppressWarnings("unchecked")
	public ISO8583VO buildRevRequest(ISO8583VO request) throws ParseException, Exception {

		ISO8583VO revVo = new ISO8583VO();

		MTIClass mtic = MTIClass.Reversal;// 04
		MTIMode mtim = MTIMode.Interactive;// 00
		revVo.setMti(revVo.new MTI());
		revVo.getMti().setMtic(mtic);
		revVo.getMti().setMtim(mtim);
		byte id = 0x60;
		byte[] destinationAddr = new byte[] { 0x00, 0x00 };
		byte[] originationAddr = new byte[] { 0x01, 0x75 };
		revVo.setTpdu(revVo.new TPDU());
		revVo.getTpdu().setID(id);
		revVo.getTpdu().setDestinationAddr(destinationAddr);
		revVo.getTpdu().setOriginationAddr(originationAddr);
		factory.createISOValue(revVo, 3, "000000");
		factory.createISOValue(revVo, 4, request.getField(4).getValue());
		factory.createISOValue(revVo, 11, request.getField(11).getValue());
		factory.createISOValue(revVo, 22, request.getField(22).getValue());
		factory.createISOValue(revVo, 24, request.getField(24).getValue());
		factory.createISOValue(revVo, 25, request.getField(25).getValue());
		factory.createISOValue(revVo, 32, request.getField(32).getValue());
		factory.createISOValue(revVo, 35, request.getField(35).getValue());
		factory.createISOValue(revVo, 37, request.getField(37).getValue());
		if (request.hasField(38)) {
			factory.createISOValue(revVo, 38, request.getField(38).getValue());
		}
		factory.createISOValue(revVo, 41, request.getField(41).getValue());
		factory.createISOValue(revVo, 42, request.getField(42).getValue());
		if (request.hasField(59)) {
			List<ISOTable> tables = (List<ISOTable>) request.getField(59).getValue();
			ISO8583Fields n9Fields = new ISO8583Fields();
			boolean haveN8 = false;
			for (Iterator<ISOTable> itr = tables.iterator(); itr.hasNext();) {
				ISOTable token = itr.next();
				if (token.getId().equals("N8")) {
					itr.remove();
					haveN8 = true;
				}
				if (token.getId().equals("N9")) {
					n9Fields = token.getFields();
				}
			}
			if (haveN8) {
				factory.createNestFieldsValue(request, n9Fields, 59, "N9", 2, "0", "");
			}
			factory.createISOValue(revVo, 59, request.getField(59).getValue());
		}
		factory.createISOValue(revVo, 60, request.getField(60).getValue());
		factory.createISOValue(revVo, 62, request.getField(62).getValue());
		return revVo;
	}

	public ISO8583VO buildRequest(NcccParm inp, String bankNo) {
		ISO8583VO requestVo = new ISO8583VO();
		MTIClass mtic = MTIClass.Financial_Transaction;// 02
		MTIMode mtim = MTIMode.Interactive;// 00
		requestVo.setMti(requestVo.new MTI());
		requestVo.getMti().setMtic(mtic);
		requestVo.getMti().setMtim(mtim);
		byte id = 0x60;
		byte[] destinationAddr = new byte[] { 0x00, 0x00 };
		byte[] originationAddr = new byte[] { 0x01, 0x75 };
		requestVo.setTpdu(requestVo.new TPDU());
		requestVo.getTpdu().setID(id);
		requestVo.getTpdu().setDestinationAddr(destinationAddr);
		requestVo.getTpdu().setOriginationAddr(originationAddr);
		String cType = inp.getCardType();
		factory.createISOValue(requestVo, 3, "000000");
		String cond = posCondition(cType, inp);

		if (inp.getInstType().equals("I")) {
			inp.setRedeemType(" ");
		} else if (inp.getRedeemType().equals("1")) {
			inp.setInstType(" ");
		}

		factory.createISOValue(requestVo, 4, inp.getTransAmount());
		Random rnd = new Random();
		int traceNum = rnd.nextInt(999999);
		rnd = null;
		String traceNumStr = String.format("%06d", traceNum);
		String sysTime = MyDateUtil.getSysDateTime(MyDateUtil.HHMMSS);
		factory.createISOValue(requestVo, 11, traceNumStr);

		if (cType.equals("D") || cType.equals("T")) {
			factory.createISOValue(requestVo, 22, "010");
		} else {
			factory.createISOValue(requestVo, 22, "000");
		}
		factory.createISOValue(requestVo, 24, "0296");
		factory.createISOValue(requestVo, 25, cond);

//		if( inp.getAcquirerId().length() == 0 ) {
//			factory.createISOValue(requestVo, 32, "493817");
//		} else {
//			factory.createISOValue(requestVo, 32, inp.getAcquirerId());
//		}
		// ATS F32 端末機送銀行代碼
		if (bankNo == null || bankNo.length() == 0) {
			factory.createISOValue(requestVo, 32, "956");
		} else {
			factory.createISOValue(requestVo, 32, bankNo);
		}

		String track2 = inp.getCardNum().trim() + "D" + inp.getCardExpireDate();
		ISO8583Fields f35Fields = new ISO8583Fields();
		f35Fields.setId("f35_default");
		factory.createNestFieldsValue(requestVo, f35Fields, 35, "f35_default", 1, track2, "");
		factory.createISOValue(requestVo, 35, f35Fields);

		GregorianCalendar cal = new GregorianCalendar();
		int dateofYear = cal.get(GregorianCalendar.DAY_OF_YEAR);
		int year = cal.get(GregorianCalendar.YEAR);
		year = year % 10;

		StringBuffer julianDay = new StringBuffer(new Integer(year).toString())
				.append(new Integer(dateofYear).toString());

		if (cType.equals("D") || cType.equals("T")) {
			factory.createISOValue(requestVo, 37,
					julianDay.toString() + inp.getCardNum().substring(14, 16) + traceNumStr);
		} else {
			factory.createISOValue(requestVo, 37, sysTime + traceNumStr);
		}

		if (inp.getCancelFlag().equals("Y")) {
			factory.createISOValue(requestVo, 38, inp.getAuthCode());
		}
		String termId = "VOIC  ##";
		if (inp.getTerminalId() != null && inp.getTerminalId().length() == 8) {
			termId = inp.getTerminalId();
		}
		factory.createISOValue(requestVo, 41, Util.padRight(termId, 8));
		factory.createISOValue(requestVo, 42, Util.padRight(inp.getMerchantId(), 15));

		String txCode = "";
		if (cond.equals("08") && !inp.getCancelFlag().equals("Y")) {
			txCode = "51";
		} else if (inp.getCancelFlag().equals("Y")) {
			txCode = "61";
		} else {
			txCode = "11";
		}
		List<ISOTable> tables = new ArrayList<ISOTable>();

		// CUP
		ISOTable n1 = new ISOTable();
		n1.setId("N1");
		ISO8583Fields n1Fields = new ISO8583Fields();
		factory.createNestFieldsValue(requestVo, n1Fields, 59, "N1", 1, "0", "");
		factory.createNestFieldsValue(requestVo, n1Fields, 59, "N1", 2, "0", "");
		n1.setFields(n1Fields);
		tables.add(n1);

//		ATS 不清算，欄位 59 之 Table ID “N2”須帶 AU
		ISOTable n2 = new ISOTable();
		n2.setId("N2");
		ISO8583Fields n2Fields = new ISO8583Fields();
		factory.createNestFieldsValue(requestVo, n2Fields, 59, "N2", 1, "AU", "");
		n2.setFields(n2Fields);
		tables.add(n2);

		ISOTable n3 = new ISOTable();
		n3.setId("N3");
		ISO8583Fields n3Fields = new ISO8583Fields();
		factory.createNestFieldsValue(requestVo, n3Fields, 59, "N3", 1, "0000", "");
		n3.setFields(n3Fields);
		tables.add(n3);

		// ESC flag
		ISOTable ne = new ISOTable();
		ne.setId("NE");
		ISO8583Fields neFields = new ISO8583Fields();
		factory.createNestFieldsValue(requestVo, neFields, 59, "NE", 1, "N", "");
		ne.setFields(neFields);
		tables.add(ne);

		// FES indicator
		ISOTable nf = new ISOTable();
		nf.setId("NF");
		ISO8583Fields nfFields = new ISO8583Fields();
		factory.createNestFieldsValue(requestVo, nfFields, 59, "NF", 1, "ATS", "");
		nf.setFields(nfFields);
		tables.add(nf);

		// 電子發票
		ISOTable ni = new ISOTable();
		ni.setId("NI");
		ISO8583Fields niFields = new ISO8583Fields();
		factory.createNestFieldsValue(requestVo, niFields, 59, "NI", 1, "      ", "");
		ni.setFields(niFields);
		tables.add(ni);

		// 免簽判斷
		ISOTable nq = new ISOTable();
		nq.setId("NQ");
		ISO8583Fields nqFields = new ISO8583Fields();
		factory.createNestFieldsValue(requestVo, nqFields, 59, "NQ", 1, "N", "");
		nq.setFields(nqFields);
		tables.add(nq);

		// MCP indicator
		ISOTable nc = new ISOTable();
		nc.setId("NC");
		ISO8583Fields ncFields = new ISO8583Fields();
		factory.createNestFieldsValue(requestVo, ncFields, 59, "NC", 1, " ", "");
		factory.createNestFieldsValue(requestVo, ncFields, 59, "NC", 2, " ", "");
		factory.createNestFieldsValue(requestVo, ncFields, 59, "NC", 3, " ", "");
		factory.createNestFieldsValue(requestVo, ncFields, 59, "NC", 4, " ", "");
		nc.setFields(ncFields);
		tables.add(nc);

		// settle trace
		ISOTable td = new ISOTable();
		td.setId("TD");
		ISO8583Fields tdFields = new ISO8583Fields();
		factory.createNestFieldsValue(requestVo, tdFields, 59, "TD", 1, " ", "");
		factory.createNestFieldsValue(requestVo, tdFields, 59, "TD", 2, " ", "");
		td.setFields(tdFields);
		tables.add(td);

		ISOTable tp = new ISOTable();
		tp.setId("TP");
		ISO8583Fields tpFields = new ISO8583Fields();
		factory.createNestFieldsValue(requestVo, tpFields, 59, "TP", 1, "A", "");
		factory.createNestFieldsValue(requestVo, tpFields, 59, "TP", 2, " ", "");
		factory.createNestFieldsValue(requestVo, tpFields, 59, "TP", 3, " ", "");
		factory.createNestFieldsValue(requestVo, tpFields, 59, "TP", 4, " ", "");

		tp.setFields(tpFields);
		tables.add(tp);

		// CVV 2 、 CVC2 、 CVN2 或 4DBC
		boolean haveN8 = false;
		if (inp.getCvv2() != null && inp.getCvv2().trim().length() > 0) {
			ISOTable n8 = new ISOTable();
			n8.setId("N8");
			ISO8583Fields n8Fields = new ISO8583Fields();
			//20210422_bugfix_將cvv2轉ascii code
			String cvv2 = Util.padRight(inp.getCvv2(), 4); 
			cvv2 = CommonFunction.bytesToHex(cvv2.getBytes());
			factory.createNestFieldsValue(requestVo, n8Fields, 59, "N8", 1, cvv2, "");
			n8.setFields(n8Fields);
			tables.add(n8);
			haveN8 = true;
		}
		
		if ("Y".equalsIgnoreCase(inp.getMotoFlag()) || "Y".equalsIgnoreCase(inp.getRecurringFlag())
				|| "08".equalsIgnoreCase(cond)) {
			ISOTable n9 = new ISOTable();
			n9.setId("N9");
			ISO8583Fields n9Fields = new ISO8583Fields();
			if ("Y".equalsIgnoreCase(inp.getRecurringFlag())) {
				factory.createNestFieldsValue(requestVo, n9Fields, 59, "N9", 1, "2", "");
			} else {
				factory.createNestFieldsValue(requestVo, n9Fields, 59, "N9", 1, "1", "");
			}
			if (haveN8) {
				factory.createNestFieldsValue(requestVo, n9Fields, 59, "N9", 2, "1", "");
			} else {
				factory.createNestFieldsValue(requestVo, n9Fields, 59, "N9", 2, "0", "");
			}
			factory.createNestFieldsValue(requestVo, n9Fields, 59, "N9", 3, "0", "");
			factory.createNestFieldsValue(requestVo, n9Fields, 59, "N9", 4, " ", "");
			n9.setFields(n9Fields);
			tables.add(n9);

		}
		factory.createISOValue(requestVo, 59, tables);
		factory.createISOValue(requestVo, 60, CommonFunction.bytesToHex("000001".getBytes()));
		factory.createISOValue(requestVo, 62, getInvoice());

//		String specFlag = "";
//		String ecCheck = "Y";
//		if (inp.getInstType().equals("I") || inp.getInstType().equals("E") || inp.getRedeemType().equals("1")
//				|| inp.getRedeemType().equals("2")) {
//			specFlag = "Y";
//			ecCheck = "Y";
//		}
//		if (inp.getCavv().length() > 10) {
//			ecCheck = "Y";
//		}
//		if (inp.getEci() == null || inp.getEci().trim().length() <= 0) {
//			inp.setEci("0");
//		}
//		String ecFlag = inp.getEci();
//		String cvv2token = "1";
//		if (inp.getCavv().equals(" ") || inp.getCavv().trim().length() <= 0) {
//			cvv2token = "0";
//			inp.setCvv2(" ");
//		}
//		int tokenCount = 1;
//		String iduToken = "0";
//		if (inp.getIduToken() != null && inp.getIduToken().trim().length() <= 0) {
//			iduToken = "0";
//		} else {
//			iduToken = inp.getIduToken();
//		}
//		tokenCount++;

		return requestVo;
	}

	private String posCondition(String cType, NcccParm inp) {
		String cond = "00";

		String iduCode = inp.getMerchantId().substring(2, 5);
		if ((inp.getMccCode().compareTo("5960") >= 0 && inp.getMccCode().compareTo("5962") <= 0)
				|| (inp.getMccCode().compareTo("5964") >= 0 && inp.getMccCode().compareTo("5969") <= 0)
				|| iduCode.equals("016") || iduCode.equals("316") || iduCode.equals("006")
				|| inp.getRecurringFlag().equals("Y")) {
			cond = "08";
			inp.setMotoFlag("Y");
		}
		return cond;
	}

	private String getInvoice() {
		double d = 0;
		long l = 0;
		do {
			d = Math.random() * 100;
			l = Math.round(d);
		} while (l <= 0 || l >= 100);
		Calendar calendar = Calendar.getInstance();
		int h24 = calendar.get(Calendar.HOUR_OF_DAY);
		int mm = calendar.get(Calendar.MINUTE);

		String invoice = String.format("%02d%02d%02d", h24, mm, l);
		invoice = invoice.substring(invoice.length() - 6, invoice.length());
		return invoice;
	}
}