/**
 * 
 */
package com.hp.nccc.iso8583.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.nccc.iso8583.common.CommonFunction;
import com.hp.nccc.iso8583.common.ISO8583Context.MTIClass;
import com.hp.nccc.iso8583.common.ISO8583Context.MTIMode;
import com.hp.nccc.iso8583.exception.HeaderParseException;
import com.hp.nccc.iso8583.exception.ISO8583ParseException;
import com.hp.nccc.iso8583.parse.ConfigParse;
import com.hp.nccc.iso8583.parse.FieldParseInfo;
import com.rits.cloning.Cloner;

/**
 * @author hsiehes
 * 
 */
public class ISO8583Factory<T extends ISO8583VO> {
	public static final String ISO8583_TYPE_STRING = "STRING";
	public static final String ISO8583_TYPE_BINARY = "BINARY";
	public static final String ISO8583_TYPE_AUTO = "AUTO";

	public ISO8583Factory() {
	}

	public ISO8583Factory(String configPath) {
		try {
			ConfigParse.configureFromClasspathConfig(this, configPath);
		} catch (IOException e) {
			log.error("can't config by classpath", e);
		}
	}

	private static final Logger log = LogManager.getLogger(ISO8583Factory.class);

	/** Stores the information needed to parse messages sorted by type. */
	protected Map<String, Map<Integer, FieldParseInfo>> parseMap = new HashMap<String, Map<Integer, FieldParseInfo>>();
	/** Stores the field numbers to be parsed, in order of appearance. */
	protected Map<String, List<Integer>> parseOrder = new HashMap<String, List<Integer>>();
	/** The ISO header to be included in each message type. */
	private Map<String, String> isoHeaders = new HashMap<String, String>();

	// private TraceNumberGenerator traceGen;
	/** Indicates if the current date should be set on new messages (field 7). */
	private boolean setDate;
	private int etx = -1;
	/**
	 * Flag to specify if missing fields should be ignored as long as they're at
	 * the end of the message.
	 */
	private boolean ignoreLast;
	private boolean forceb2;
	private boolean forceStringEncoding;
	private String encoding = System.getProperty("file.encoding");
	private String type = ISO8583_TYPE_AUTO;
	private boolean dataOnly = false;

	public boolean isDataOnly() {
		return dataOnly;
	}

	public void setDataOnly(boolean dataOnly) {
		this.dataOnly = dataOnly;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/** Creates a BitSet for the bitmap. */
	public BitSet createBitmapBitSet(T m) {
		BitSet bs = new BitSet(forceb2 ? 128 : 64);
		for (int i = 2; i < 129; i++) {
			if (m.getField(i) != null) {
				bs.set(i - 1);
			}
		}
		if (forceb2) {
			bs.set(0);
		} else if (bs.length() > 64) {
			// Extend to 128 if needed
			BitSet b2 = new BitSet(128);
			b2.or(bs);
			bs = b2;
			bs.set(0);
		}
		return bs;
	}

	/**
	 * Creates a Iso message, override this method in the subclass to provide
	 * your own implementations of ISO8583VO.
	 * 
	 * @param header
	 *            The optional ISO header that goes before the message type
	 * @return ISO8583VO
	 */
	@SuppressWarnings("unchecked")
	protected T createISO8583VO() {
		return (T) new ISO8583VO();
	}

	public <TT> void createISOValue(T m, int fieldNum, TT value) {
		List<Integer> index = parseOrder.get(m.getId());
		if (index == null) {
			log.error(String.format("ISO8583 MessageFactory has no parsing guide for id [%s]", m.getId()));
			return;
		}
		Map<Integer, FieldParseInfo> parseGuide = parseMap.get(m.getId());
		FieldParseInfo fpi = parseGuide.get(fieldNum);
		if (fpi.getType().needsLength()) {
//			if length is 0, set length equals the value length (for TYPE VAR)
			if(fpi.getLength() == 0){
				fpi.setVarLength(value.toString().length());
				m.setField(fieldNum, new ISOValue<TT>(fpi.getType(), value, fpi.getVarLength(), fpi.getDescription()));
			} else {
				m.setField(fieldNum, new ISOValue<TT>(fpi.getType(), value, fpi.getLength(), fpi.getDescription()));
			}
		} else {
			m.setField(fieldNum, new ISOValue<TT>(fpi.getType(), value, fpi.getDescription()));
		}
	}

	public <TT> void createISOField(String id, int fieldNum, TT value, ISO8583Fields fields) {
		List<Integer> index = parseOrder.get(id);
		if (index == null) {
			log.error(String.format("ISO8583 MessageFactory has no parsing guide for id [%s]", id));
			return;
		}
		Map<Integer, FieldParseInfo> parseGuide = parseMap.get(id);
		FieldParseInfo fpi = parseGuide.get(fieldNum);
		if (fpi.getType().needsLength()) {
//			if length is 0, set length equals the value length (for TYPE VAR)
			if(fpi.getLength() == 0){
				fpi.setVarLength(value.toString().length());
				fields.setField(fieldNum, new ISOValue<TT>(fpi.getType(), value, fpi.getVarLength(), fpi.getDescription()));
			} else {
				fields.setField(fieldNum, new ISOValue<TT>(fpi.getType(), value, fpi.getLength(), fpi.getDescription()));
			}
		} else {
			fields.setField(fieldNum, new ISOValue<TT>(fpi.getType(), value, fpi.getDescription()));
		}
	}

	public <TT> void createNestFieldsValue(T m, ISO8583Fields fields, int fieldNum, String nestFieldsId,
			int nestFieldsNum, TT value, String... conditions) {
		createNestFieldsValue(m.getId(), fields, fieldNum, nestFieldsId, nestFieldsNum, value, conditions);
	}
	
	public <TT> void createNestFieldsValue(String id, ISO8583Fields fields, int fieldNum, String nestFieldsId,
			int nestFieldsNum, TT value, String... conditions) {
		List<Integer> index = parseOrder.get(id);
		if (index == null) {
			log.error(String.format("ISO8583 MessageFactory has no parsing guide for id [%s]", id));
			return;
		}
		Map<Integer, FieldParseInfo> parseGuide = parseMap.get(id);
		FieldParseInfo fpi = parseGuide.get(fieldNum);
		Map<Integer, FieldParseInfo> nestParseGuide = fpi.getMapProvider().getFieldsParseInfo(nestFieldsId, conditions);
		FieldParseInfo nestFpi = null;
		//因應品牌卡field127規格不同, parseMap非default會找不到, 直接從parseMap中拿
		if(nestParseGuide == null) {
			nestFpi = parseMap.get(nestFieldsId).get(nestFieldsNum);
		}else {
		 nestFpi = nestParseGuide.get(nestFieldsNum);
		}
		if (nestFpi.getType().needsLength()) {
//			if length is 0, set length equals the value length (for TYPE VAR)
			if(nestFpi.getLength() == 0) {
				nestFpi.setVarLength(value.toString().length());
				fields.setField(nestFieldsNum, new ISOValue<TT>(nestFpi.getType(), value, nestFpi.getVarLength(), nestFpi.getDescription()));
			} else {
				fields.setField(nestFieldsNum, new ISOValue<TT>(nestFpi.getType(), value, nestFpi.getLength(), nestFpi.getDescription()));
			}
		} else {
			fields.setField(nestFieldsNum, new ISOValue<TT>(nestFpi.getType(), value, nestFpi.getDescription()));
		}
	}

	/**
	 * Creates a message to respond to a request. Increments the message type by
	 * 16, sets all fields from the template if there is one, and copies all
	 * values from the request, overwriting fields from the template if they
	 * overlap.
	 * 
	 * @param request
	 *            An ISO8583 message with a request type (ending in 00).
	 */
	public T createResponse(T request) {
		T resp = createImage(request);
		resp.getMti().setMtim(request.getMti().getResponse());
		if (request.getTpdu() != null) {
			resp.getTpdu().setDestinationAddr(request.getTpdu().getOriginationAddr());
			resp.getTpdu().setOriginationAddr(request.getTpdu().getDestinationAddr());
		}
		return resp;
	}
	
	public T createImage(T request) {
		return (T) new Cloner().deepClone(request);
	}
	

	/**
	 * Returns true if the factory is assigning the current date to newly
	 * created messages (field 7). Default is false.
	 */
	public boolean getAssignDate() {
		return setDate;
	}

	/**
	 * Returns the encoding used to parse ALPHA, LLVAR and LLLVAR fields. The
	 * default is the file.encoding system property.
	 */
	public String getCharacterEncoding() {
		return encoding;
	}

	public int getEtx() {
		return etx;
	}

	/**
	 * This flag indicates if the MessageFactory throws an exception if the last
	 * field of a message is not really present even though it's specified in
	 * the bitmap. Default is false which means an exception is thrown.
	 */
	public boolean getIgnoreLastMissingField() {
		return ignoreLast;
	}

	/** Returns the ISO header used for the specified type. */
	public String getIsoHeader(String id) {
		return isoHeaders.get(id);
	}

	public boolean isForceSecondaryBitmap() {
		return forceb2;
	}

	public boolean isForceStringEncoding() {
		return forceStringEncoding;
	}

	public T parseCompleteISO(byte[] iso) throws UnsupportedEncodingException, ParseException {
		return parseISO(iso, iso.length);
	}
	
	public T parseCompleteISO(byte[] iso, int lengthBytes, boolean isBinary) throws UnsupportedEncodingException, ParseException {
		InputStream in = new ByteArrayInputStream(iso);
		return parseCompleteISO(in, lengthBytes, isBinary );
	}

	public T parseCompleteISO(byte[] iso, int lengthBytes) throws UnsupportedEncodingException, ParseException {
		InputStream in = new ByteArrayInputStream(iso);
		return parseCompleteISO(in, lengthBytes, false);
	}
		
	public T parseCompleteISO(InputStream iso, int lengthBytes) throws UnsupportedEncodingException, ParseException {
		return parseCompleteISO(iso, lengthBytes, false);
	}

	public T parseCompleteISO(InputStream iso, int lengthBytes, boolean isBinary) throws UnsupportedEncodingException, ParseException {
		if (lengthBytes > 4) {
			throw new IllegalArgumentException("The length header can have at most 4 bytes");
		}

		int totLen = 0;
		for (int i = 0; i < lengthBytes; i++) {
			int b = 0;
			try {
				b = iso.read();
			} catch (IOException e) {
				log.error(e.getMessage());
			}
			totLen = totLen * 256 + (b & 0xFF);
		}

		byte[] data;
		if(isBinary) {
			data = new byte[totLen*2];
			try {
				iso.read(data, 0, totLen*2);
				data = CommonFunction.hexDecode(new String(data));
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}else {
			data = new byte[totLen];
			try {
				iso.read(data, 0, totLen);
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
		return parseISO(data, totLen);
	}

	public T parseISO(byte[] buf, int totLen) throws ParseException, UnsupportedEncodingException {
		T m = this.parseISOHeader(buf, totLen);
		return this.parseISOData(m);
	}
	
	private boolean isTraceEnable(){
		return isTraceEnable?isTraceEnable:(log.isDebugEnabled() || log.isTraceEnabled());
	}
	
	private boolean isTraceEnable = false;
	
	public void setIsTraceEnable(boolean isTraceEnable){
		this.isTraceEnable = isTraceEnable;
	}

	public T parseISOData(T m) throws UnsupportedEncodingException, ISO8583ParseException {
		List<ISO8583FieldTraceInfo> traceInfos = null;
		if(isTraceEnable()){
			traceInfos = new ArrayList<ISO8583FieldTraceInfo>();
		}
		
		byte[] data = m.getData();

		m.setCharacterEncoding(encoding);

		// Parse each field
		List<Integer> index = parseOrder.get(m.getId());
		if (index == null) {
			log.error(String.format("ISO8583 MessageFactory has no parsing guide for id [%s]", m.getId()));
			return null;
		}
		Map<Integer, FieldParseInfo> parseGuide = parseMap.get(m.getId());

		BitSet bs = m.getBitMap();
		int pos = 0;
		// Now we parse each field
		for (Integer i : index) {
			FieldParseInfo fpi = parseGuide.get(i);
			if (bs.get(i - 1)) {
				String[] conditions = { m.getMti() == null ? "" : m.getMti().toString() };
				ISO8583FieldTraceInfo field = null;
				if(isTraceEnable()){
					field = new ISO8583FieldTraceInfo();
					field.setBinary(isBinary());
					field.setStartPos(pos);
					field.setConfigParseId(m.getId());
					field.setDesc(fpi.getDescription());
					field.setField(i);
					field.setSuperConfigParseId(null);
					field.setSuperField(0);
				}
				try {
					if (isBinary()) {
						if (ignoreLast && pos >= data.length && i.intValue() == index.get(index.size() - 1)) {
							log.warn("Field {" + i + "} is not really in the message even though it's in the bitmap");
							bs.clear(i - 1);
						} else {
							ISOValue<?> val = fpi.parse(i, data, pos, m, conditions, isTraceEnable());
							m.setField(i, val);
							if (val != null) {
								if (val.getType() == ISOType.NUMERIC || val.getType() == ISOType.DATE10
										|| val.getType() == ISOType.DATE4 || val.getType() == ISOType.DATE_EXP
										|| val.getType() == ISOType.AMOUNT || val.getType() == ISOType.TIME) {
									pos += (val.getLength() / 2) + (val.getLength() % 2);
								} else {
									pos += val.getLength();
								}
								if (val.getType() == ISOType.LLVAR || val.getType() == ISOType.LLBIN
										|| val.getType() == ISOType.LLZ) {
									pos++;
								} else if (val.getType() == ISOType.LLLVAR || val.getType() == ISOType.LLLBIN
										|| val.getType() == ISOType.LLLTABLES || val.getType() == ISOType.LLLCHIP
										|| val.getType() == ISOType.SMARTPAYCHIP || val.getType() == ISOType.SMARTPAYTAG
										|| val.getType() == ISOType.LLLBITMAPVAR) {
									pos += 2;
								}
							}
						}
					} else {
						if (ignoreLast && pos >= data.length && i.intValue() == index.get(index.size() - 1)) {
							log.warn("Field {" + i + "} is not really in the message even though it's in the bitmap");
							bs.clear(i - 1);
						} else {
							ISOValue<?> val = fpi.parseString(i, data, pos, m, conditions, isTraceEnable());
							m.setField(i, val);
							//To get the correct next position, we need to get the number of bytes, not chars
							pos += val.getLength();
							if (val.getType() == ISOType.LLVAR || val.getType() == ISOType.LLBIN
									|| val.getType() == ISOType.LLZ) {
								pos += 2;
							} else if (val.getType() == ISOType.LLLVAR || val.getType() == ISOType.LLLBIN
									|| val.getType() == ISOType.LLLTOKENS || val.getType() == ISOType.LLLCHIP) {
								pos += 3;
							} else if (val.getType() == ISOType.LLLLTOKENS) {
								pos += 4;
							}
						}
					}
				} catch (ISO8583ParseException e) {
					if(isTraceEnable()){
						processPos(traceInfos, data, field);
						ISO8583TraceInfo info = new ISO8583TraceInfo();;
						info.setTraceInfos(traceInfos);
						ISO8583TraceInfo subInfo = e.getTraceInfo();
						if(subInfo != null){
							for(ISO8583FieldTraceInfo subFieldInfo : subInfo.getTraceInfos()){
								subFieldInfo.setSuperConfigParseId(m.getId());
								subFieldInfo.setSuperField(i);
							}
						}
						info.setTraceInfo(subInfo);
						e.setTraceInfo(info);
					}
					e.setRootFieldNo(i);
					throw e;
				} catch (Exception e) {
					ISO8583ParseException ie = new ISO8583ParseException(String.format("exception [%s] field %d", e.getMessage(), i), pos, i);
					if(isTraceEnable()){
						processPos(traceInfos, data, field);
						ISO8583TraceInfo info = new ISO8583TraceInfo();;
						info.setTraceInfos(traceInfos);
						ie.setTraceInfo(info);
					}
					ie.setRootFieldNo(i);
					throw ie;
				}
				if(isTraceEnable()){
					field.setEndPos(pos-1);
					int length = field.getEndPos() - field.getStartPos() + 1;
					if(length > data.length - field.getStartPos()){
						length = data.length - field.getStartPos();
					}
					byte[] tempByte = new byte[length];
					System.arraycopy(data, field.getStartPos(), tempByte, 0, length);
					field.setByteData(tempByte);
					field.setValue(m.getField(i));
					traceInfos.add(field);
				}
			}
		}
		return m;
	}

	private void processPos(List<ISO8583FieldTraceInfo> traceInfos, byte[] data, ISO8583FieldTraceInfo field) {
		field.setEndPos(data.length-1);
		int length = field.getEndPos() - field.getStartPos() + 1;
		if(length > data.length - field.getStartPos()){
			length = data.length - field.getStartPos();
		}
		byte[] tempByte = new byte[length];
		System.arraycopy(data, field.getStartPos(), tempByte, 0, length);
		field.setByteData(tempByte);
		traceInfos.add(field);
	}

	public T parseISOHeader(byte[] inData, int totLen) throws HeaderParseException {
		int isoHeaderLength = 5;
		if (isoHeaders != null && isoHeaders.size() > 0) {
			for (String id : isoHeaders.keySet()) {
				if (id.equals("default")) {
					String isoHeader = isoHeaders.get(id);
					if ("TPDU".equals(isoHeader)) {
						isoHeaderLength = 5;
					} else {
						isoHeaderLength = isoHeader.length();
					}
				}
			}
		} else {
			if(isDataOnly()){
				isoHeaderLength = 0;
			}else{
				isoHeaderLength = 5;
			}
		}
		return parseISOHeader(inData, totLen, isoHeaderLength);
	}

	public boolean isTPDU() {
		boolean isTPDU = true;
		if (isoHeaders != null && isoHeaders.size() > 0) {
			for (String id : isoHeaders.keySet()) {
				if (id.equals("default")) {
					String isoHeader = isoHeaders.get(id);
					if ("TPDU".equals(isoHeader)) {
						isTPDU = true;
					} else {
						isTPDU = false;
					}
				}
			}
		} else {
			if(isDataOnly()){
				isTPDU = false;
			}else{
				isTPDU = true;
			}
		}
		return isTPDU;
	}

	public boolean isBinary() {
		boolean isBinary = false;
		if (type.equals(ISO8583_TYPE_AUTO) || type.equals("")) {
			if (isoHeaders != null && isoHeaders.size() > 0) {
				for (String id : isoHeaders.keySet()) {
					if (id.equals("default")) {
						String isoHeader = isoHeaders.get(id);
						if ("".equals(isoHeader)) {
							isBinary = true;
						} else if (isTPDU()) {
							isBinary = true;
						} else {
							isBinary = false;
						}
					}
				}
			} else {
				isBinary = true;
			}
		} else {
			if (type.equals(ISO8583_TYPE_BINARY)) {
				isBinary = true;
			} else if (type.equals(ISO8583_TYPE_STRING)) {
				isBinary = false;
			}
		}
		return isBinary;
	}

	public T parseISOHeader(byte[] inData, int totLen, int isoHeaderLength) throws HeaderParseException {
		final int minlength = isDataOnly() ? 0 : isoHeaderLength + (2 + 8) * (isBinary() ? 1 : 2);
		if (inData.length < minlength) {
			throw new HeaderParseException("Insufficient buffer length, needs to be at least " + minlength, 0, 0);
		}
		final T m = createISO8583VO();
		m.setIsoHeader(new String(inData, 0, isoHeaderLength));
		m.setLength(totLen);

		if (isDataOnly()) {
			m.setTpdu(null);
			m.setMti(null);
		} else {
			if(isBinary()){
				if(isTPDU()){
					m.setTpdu(m.new TPDU());
					m.getTpdu().setID(inData[0]);
					m.getTpdu().setDestinationAddr(new byte[] { inData[1], inData[2] });
					m.getTpdu().setOriginationAddr(new byte[] { inData[3], inData[4] });
					m.getMti().setMtic(MTIClass.valueOf(inData[0 + isoHeaderLength]));
					m.getMti().setMtim(MTIMode.valueOf(inData[1 + isoHeaderLength]));
				}else{
					m.getMti().setMtic(MTIClass.valueOf(inData[0 + isoHeaderLength]));
					m.getMti().setMtim(MTIMode.valueOf(inData[1 + isoHeaderLength]));
				}
			}else{
				byte[] mti = CommonFunction.hexDecode(new String(new byte[] { inData[0 + isoHeaderLength],
						inData[1 + isoHeaderLength], inData[2 + isoHeaderLength], inData[3 + isoHeaderLength] }));
				m.getMti().setMtic(MTIClass.valueOf(mti[0]));
				m.getMti().setMtim(MTIMode.valueOf(mti[1]));
			}
		}

		int pos = 0;
		final BitSet bs = new BitSet(64);
		if (isDataOnly()) {
			for (int i = 0; i < 64; i++) {
				bs.set(i, true);
			}
		} else {
			// Parse the bitmap (primary first)

			if (isBinary()) {
				final int bitmapStart = 2 + isoHeaderLength;
				for (int i = bitmapStart; i < 8 + bitmapStart; i++) {
					int bit = 128;
					for (int b = 0; b < 8; b++) {
						bs.set(pos++, (inData[i] & bit) != 0);
						bit >>= 1;
					}
				}
				// Check for secondary bitmap and parse if necessary
				if (bs.get(0)) {
					for (int i = 8 + bitmapStart; i < 16 + bitmapStart; i++) {
						int bit = 128;
						for (int b = 0; b < 8; b++) {
							bs.set(pos++, (inData[i] & bit) != 0);
							bit >>= 1;
						}
					}
					pos = minlength + 8;
				} else {
					pos = minlength;
				}
			} else {
				//ASCII parsing
				try {
					final byte[] bitmapBuffer;
					if (forceStringEncoding) {
						byte[] _bb;
						try {
							_bb = new String(inData, isoHeaderLength + 4, 16, encoding).getBytes();
						} catch (UnsupportedEncodingException e) {
							throw new HeaderParseException(e.getMessage(), 0, 0);
						}
						bitmapBuffer = new byte[36 + isoHeaderLength];
						System.arraycopy(_bb, 0, bitmapBuffer, 4 + isoHeaderLength, 16);
					} else {
						bitmapBuffer = inData;
					}
					for (int i = isoHeaderLength + 4; i < isoHeaderLength + 20; i++) {
						if (bitmapBuffer[i] >= '0' && bitmapBuffer[i] <= '9') {
							bs.set(pos++, ((bitmapBuffer[i] - 48) & 8) > 0);
							bs.set(pos++, ((bitmapBuffer[i] - 48) & 4) > 0);
							bs.set(pos++, ((bitmapBuffer[i] - 48) & 2) > 0);
							bs.set(pos++, ((bitmapBuffer[i] - 48) & 1) > 0);
						} else if (bitmapBuffer[i] >= 'A' && bitmapBuffer[i] <= 'F') {
							bs.set(pos++, ((bitmapBuffer[i] - 55) & 8) > 0);
							bs.set(pos++, ((bitmapBuffer[i] - 55) & 4) > 0);
							bs.set(pos++, ((bitmapBuffer[i] - 55) & 2) > 0);
							bs.set(pos++, ((bitmapBuffer[i] - 55) & 1) > 0);
						} else if (bitmapBuffer[i] >= 'a' && bitmapBuffer[i] <= 'f') {
							bs.set(pos++, ((bitmapBuffer[i] - 87) & 8) > 0);
							bs.set(pos++, ((bitmapBuffer[i] - 87) & 4) > 0);
							bs.set(pos++, ((bitmapBuffer[i] - 87) & 2) > 0);
							bs.set(pos++, ((bitmapBuffer[i] - 87) & 1) > 0);
						}
					}
					//Check for secondary bitmap and parse it if necessary
					if (bs.get(0)) {
						if (inData.length < minlength + 16) {
							throw new HeaderParseException("Insufficient length for secondary bitmap" + minlength, 0,
									minlength);
						}
						if (forceStringEncoding) {
							byte[] _bb;
							try {
								_bb = new String(inData, isoHeaderLength + 4, 16, encoding).getBytes();
							} catch (UnsupportedEncodingException e) {
								throw new HeaderParseException(e.getMessage(), 0, 0);
							}
							System.arraycopy(_bb, 0, bitmapBuffer, 20 + isoHeaderLength, 16);
						}
						for (int i = isoHeaderLength + 20; i < isoHeaderLength + 36; i++) {
							if (bitmapBuffer[i] >= '0' && bitmapBuffer[i] <= '9') {
								bs.set(pos++, ((bitmapBuffer[i] - 48) & 8) > 0);
								bs.set(pos++, ((bitmapBuffer[i] - 48) & 4) > 0);
								bs.set(pos++, ((bitmapBuffer[i] - 48) & 2) > 0);
								bs.set(pos++, ((bitmapBuffer[i] - 48) & 1) > 0);
							} else if (bitmapBuffer[i] >= 'A' && bitmapBuffer[i] <= 'F') {
								bs.set(pos++, ((bitmapBuffer[i] - 55) & 8) > 0);
								bs.set(pos++, ((bitmapBuffer[i] - 55) & 4) > 0);
								bs.set(pos++, ((bitmapBuffer[i] - 55) & 2) > 0);
								bs.set(pos++, ((bitmapBuffer[i] - 55) & 1) > 0);
							} else if (bitmapBuffer[i] >= 'a' && bitmapBuffer[i] <= 'f') {
								bs.set(pos++, ((bitmapBuffer[i] - 87) & 8) > 0);
								bs.set(pos++, ((bitmapBuffer[i] - 87) & 4) > 0);
								bs.set(pos++, ((bitmapBuffer[i] - 87) & 2) > 0);
								bs.set(pos++, ((bitmapBuffer[i] - 87) & 1) > 0);
							}
						}
						pos = 16 + minlength;
					} else {
						pos = minlength;
					}
				} catch (NumberFormatException ex) {
					HeaderParseException _e = new HeaderParseException("Invalid ISO8583 bitmap", pos, 0);
					_e.initCause(ex);
					throw _e;
				}
			}
		}

		m.setBitMap(bs);
		byte[] data = new byte[totLen - pos];
		System.arraycopy(inData, pos, data, 0, totLen - pos);
		m.setData(data);
		return m;
	}

	/**
	 * Sets whether the factory should set the current date on newly created
	 * messages, in field 7. Default is false.
	 */
	public void setAssignDate(boolean flag) {
		setDate = flag;
	}

	/**
	 * Sets the character encoding used for parsing ALPHA, LLVAR and LLLVAR
	 * fields.
	 */
	public void setCharacterEncoding(String value) {
		if (encoding == null) {
			throw new IllegalArgumentException("Cannot set null encoding.");
		}
		encoding = value;
		if (!parseMap.isEmpty()) {
			for (Map<Integer, FieldParseInfo> pt : parseMap.values()) {
				for (FieldParseInfo fpi : pt.values()) {
					fpi.setCharacterEncoding(encoding);
				}
			}
		}
	}

	/**
	 * Tells the receiver to read the configuration at the specified path. This
	 * just calls ConfigParser.configureFromClasspathConfig() with itself and
	 * the specified path at arguments, but is really convenient in case the
	 * MessageFactory is being configured from within, say, Spring.
	 */
	public void setConfigPath(String path) throws IOException {
		ConfigParse.configureFromClasspathConfig(this, path);
		// Now re-set some properties that need to be propagated down to the
		// recently assigned objects
		setCharacterEncoding(encoding);
		setForceStringEncoding(forceStringEncoding);
	}

	/**
	 * Sets the ETX character to be sent at the end of the message. This is
	 * optional and the default is -1, which means nothing should be sent as
	 * terminator.
	 * 
	 * @param value
	 *            The ASCII value of the ETX character or -1 to indicate no
	 *            terminator should be used.
	 */
	public void setEtx(int value) {
		etx = value;
	}

	/**
	 * Sets or clears the flag to pass to new messages, to include a secondary
	 * bitmap even if it's not needed.
	 */
	public void setForceSecondaryBitmap(boolean flag) {
		forceb2 = flag;
	}

	/**
	 * This flag gets passed on to newly created messages and also sets this
	 * value for all field parsers in parsing guides.
	 */
	public void setForceStringEncoding(boolean flag) {
		forceStringEncoding = flag;
		for (Map<Integer, FieldParseInfo> pm : parseMap.values()) {
			for (FieldParseInfo parser : pm.values()) {
				parser.setForceStringDecoding(flag);
			}
		}
	}

	/**
	 * Setting this property to true avoids getting a ParseException when
	 * parsing messages that don't have the last field specified in the bitmap.
	 * This is common with certain providers where field 128 is specified in the
	 * bitmap but not actually included in the messages. Default is false, which
	 * has been the behavior in previous versions when this option didn't exist.
	 */
	public void setIgnoreLastMissingField(boolean flag) {
		ignoreLast = flag;
	}

	/**
	 * Sets the ISO header for a specific message type.
	 * 
	 * @param id
	 *            The parse id.
	 * @param value
	 *            The ISO header, or NULL to remove any headers for this message
	 *            type.
	 */
	public void setIsoHeader(String id, String value) {
		if (value == null) {
			isoHeaders.remove(id);
		} else {
			isoHeaders.put(id, value);
		}
	}

	/**
	 * Sets the ISO header to be used in each message type.
	 * 
	 * @param value
	 *            A map where the keys are the parse id and the values are the
	 *            ISO headers.
	 */
	public void setIsoHeaders(Map<String, String> value) {
		isoHeaders.clear();
		isoHeaders.putAll(value);
	}

	/**
	 * Sets a map with the fields that are to be expected when parsing a certain
	 * type of message.
	 * 
	 * @param type
	 *            The message type.
	 * @param map
	 *            A map of FieldParseInfo instances, each of which define what
	 *            type and length of field to expect. The keys will be the field
	 *            numbers.
	 */
	public void setParseMap(Map<Integer, FieldParseInfo> map, String id) {
		parseMap.put(id, map);
		ArrayList<Integer> index = new ArrayList<Integer>();
		index.addAll(map.keySet());
		Collections.sort(index);
		log.trace(String.format("ISO8583 MessageFactory adding parse map for type %s with fields %s", id, index));
		parseOrder.put(id, index);
	}

	/**
	 * Writes a message to a stream, after writing the specified number of bytes
	 * indicating the message's length. The message will first be written to an
	 * internal memory stream which will then be dumped into the specified
	 * stream. This method flushes the stream after the write. There are at most
	 * three write operations to the stream: one for the length header, one for
	 * the message, and the last one with for the ETX.
	 * 
	 * @param outs
	 *            The stream to write the message to.
	 * @param lengthBytes
	 *            The size of the message length header. Valid ranges are 0 to
	 *            4.
	 * @throws IllegalArgumentException
	 *             if the specified length header is more than 4 bytes.
	 * @throws IOException
	 *             if there is a problem writing to the stream.
	 */
	public synchronized void write(OutputStream outs, int lengthBytes, T m) throws IOException {
		if (lengthBytes > 4) {
			throw new IllegalArgumentException("The length header can have at most 4 bytes");
		}
		byte[] data = writeData(m);

		if (lengthBytes > 0) {
			int l = data.length;
			if (etx > -1) {
				l++;
			}
			byte[] buf = new byte[lengthBytes];
			int pos = 0;
			if (lengthBytes == 4) {
				buf[0] = (byte) ((l & 0xff000000) >> 24);
				pos++;
			}
			if (lengthBytes > 2) {
				buf[pos] = (byte) ((l & 0xff0000) >> 16);
				pos++;
			}
			if (lengthBytes > 1) {
				buf[pos] = (byte) ((l & 0xff00) >> 8);
				pos++;
			}
			buf[pos] = (byte) (l & 0xff);
			outs.write(buf);
		}
		if(isBinary()) {
			outs.write(CommonFunction.bytesToHex(data).getBytes());
		}else {
			outs.write(data);
		}
		// ETX
		if (etx > -1) {
			outs.write(etx);
		}
		outs.flush();
	}

	/**
	 * This calls writeInternal(), allowing applications to get the byte buffer
	 * containing the message data, without the length header.
	 */
	public synchronized byte[] writeData(T m) {
		byte[] result = null;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();

		if (isDataOnly()) {
			//only data element
		} else if (isBinary()) {
			if (m.isTPDU()) {
				// TPDU
				try {
					bout.write(m.getTpdu().toBytes());
				} catch (IOException e) {
					// should never happen, writing to a ByteArrayOutputStream
				}

				// Message Type
				try {
					bout.write(m.getMti().toBytes());
				} catch (IOException e) {
					// should never happen, writing to a ByteArrayOutputStream
				}

				// Bitmap
				BitSet bs = createBitmapBitSet(m);
				// Write bitmap to stream
				int pos = 128;
				int b = 0;
				for (int i = 0; i < bs.size(); i++) {
					if (bs.get(i)) {
						b |= pos;
					}
					pos >>= 1;
					if (pos == 0) {
						bout.write(b);
						pos = 128;
						b = 0;
					}
				}
			} else {
				// ISO Header
				try {
					bout.write(m.getIsoHeader().getBytes(getCharacterEncoding()));
				} catch (IOException e) {
					// should never happen, writing to a ByteArrayOutputStream
				}
				// Message Type
				try {
					bout.write(m.getMti().toBytes());
				} catch (IOException e) {
					// should never happen, writing to a ByteArrayOutputStream
				}

				// Bitmap
				BitSet bs = createBitmapBitSet(m);
				// Write bitmap to stream
				int pos = 128;
				int b = 0;
				for (int i = 0; i < bs.size(); i++) {
					if (bs.get(i)) {
						b |= pos;
					}
					pos >>= 1;
					if (pos == 0) {
						bout.write(b);
						pos = 128;
						b = 0;
					}
				}
			}
		} else {
			// ISO Header
			try {
				bout.write(m.getIsoHeader().getBytes(getCharacterEncoding()));
			} catch (IOException e) {
				// should never happen, writing to a ByteArrayOutputStream
			}
			// Message Type
			try {
				bout.write(m.getMti().toString().getBytes());
			} catch (IOException e) {
				// should never happen, writing to a ByteArrayOutputStream
			}

			// Bitmap
			BitSet bs = createBitmapBitSet(m);
			// Write bitmap to stream
			for (int i = 0; i < bs.size() / 4; i++) {
				int value = 0;
				for (int j = 0; j < 4; j++) {
					value = 2 * value + (bs.get((i * 4) + j) ? 1 : 0);
				}
				try {
					bout.write(CommonFunction.intToHex(value).getBytes());
				} catch (IOException e) {
					// should never happen, writing to a ByteArrayOutputStream
				}
			}
		}

		try {
			// Fields
			writeField(m);
			bout.write(m.getData());
			result = bout.toByteArray();
			bout.close();
		} catch (IOException e) {
			// should never happen, writing to a ByteArrayOutputStream
		}
		return result;
	}

	public synchronized void writeField(T m) throws IOException {
		m.setCharacterEncoding(encoding);

		// Parse each field
		List<Integer> index = parseOrder.get(m.getId());
		if (index == null) {
			log.error(String.format("ISO8583 MessageFactory has no parsing guide for id [%s]", m.getId()));
			return;
		}
		Map<Integer, FieldParseInfo> parseGuide = parseMap.get(m.getId());

		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		for (int i = 1; i < 129; i++) {
			ISOValue<?> v = m.getField(i);
			FieldParseInfo fpi = parseGuide.get(i);
			String[] conditions = { m.getMti() == null ? "" : m.getMti().toString() };
			if (v != null) {
				try {
					if (isBinary()) {
						fpi.write(v, bout, m, conditions);
					} else {
						fpi.writeString(v, bout, m, conditions);
					}
				} catch (IOException ex) {
					// should never happen, writing to a ByteArrayOutputStream
				}
			}
		}
		m.setData(bout.toByteArray());
		bout.close();
	}

	/**
	 * Creates and returns a ByteBuffer with the data of the message, including
	 * the length header. The returned buffer is already flipped, so it is ready
	 * to be written to a Channel.
	 */
	public synchronized ByteBuffer writeToBuffer(int lengthBytes, T m) {
		if (lengthBytes > 4) {
			throw new IllegalArgumentException("The length header can have at most 4 bytes");
		}

		byte[] data = writeData(m);
		ByteBuffer buf = ByteBuffer.allocate(lengthBytes + data.length + (etx > -1 ? 1 : 0));
		if (lengthBytes > 0) {
			int l = data.length;
			if (etx > -1) {
				l++;
			}
			if (lengthBytes == 4) {
				buf.put((byte) ((l & 0xff000000) >> 24));
			}
			if (lengthBytes > 2) {
				buf.put((byte) ((l & 0xff0000) >> 16));
			}
			if (lengthBytes > 1) {
				buf.put((byte) ((l & 0xff00) >> 8));
			}
			buf.put((byte) (l & 0xff));
		}
		buf.put(data);
		// ETX
		if (etx > -1) {
			buf.put((byte) etx);
		}
		buf.flip();
		return buf;
	}
}
