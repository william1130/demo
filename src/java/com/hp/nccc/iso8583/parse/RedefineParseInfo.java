package com.hp.nccc.iso8583.parse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import com.hp.nccc.iso8583.core.ISO8583Fields;
import com.hp.nccc.iso8583.core.ISO8583VO;
import com.hp.nccc.iso8583.core.ISOType;
import com.hp.nccc.iso8583.core.ISOValue;
import com.hp.nccc.iso8583.exception.ISO8583ParseException;

public class RedefineParseInfo extends FieldParseInfo {

	private Map<String, String> router;

	public Map<String, String> getRouter() {
		return router;
	}

	public void setRouter(Map<String, String> router) {
		this.router = router;
	}

	public RedefineParseInfo() {
		super(ISOType.REDEF, 0);
	}

	@Override
	public <T> ISOValue<?> parse(int field, byte[] buf, int pos, ISO8583VO vo, Object custom, boolean isTraceEnable)
			throws ISO8583ParseException, UnsupportedEncodingException {
		//find right define
		ExpressionParser parser = new SpelExpressionParser();
		ReDefineVo reVo = new ReDefineVo();
		reVo.setBuf(buf);
		reVo.setVo(vo);
		reVo.setPos(pos);
		reVo.setField(field);
		reVo.setCustom(custom);
		reVo.setTraceEnable(isTraceEnable);
		Boolean match = false;
		String matchId = "";
		for (String id : router.keySet()) {
			String expContent = router.get(id);
			if (expContent.equals("default")) {
				matchId = id;
				continue;
			}
			Expression exp = parser.parseExpression(expContent);
			match = exp.getValue(reVo, Boolean.class);
			if (match) {
				matchId = id;
				break;
			}
		}
		ISO8583Fields result = null;
		if(getAlt() != null && !getAlt().isEmpty() && getAlt().equals("s")){
			int l = buf.length - pos;
			byte[] temp = new byte[l];
			System.arraycopy(buf, pos, temp, 0, l);
			result = parseNestFields(temp, matchId, vo, isTraceEnable, (String[]) custom);
		}else{
			result = parseNestFields(buf, matchId, vo, isTraceEnable, (String[]) custom);
		}
		result.setId(matchId);
		return new ISOValue<ISO8583Fields>(type, result, result.getLength(true), getDescription(), true);
	}

	@Override
	public <T> void write(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		if (value.getValue() instanceof ISO8583Fields) {
			ISO8583Fields fields = (ISO8583Fields) value.getValue();

			if (fields.getId() != null && this.getMapProvider().containsKey(fields.getId())) {
				outs.write(writeNestFields(fields, fields.getId(), vo, ""));
			} else {
				for (int i = 0; i < 128; i++) {
					if (fields.hasField(i)) {
						ISOValue<?> v = fields.getField(i);
						FieldParseInfo fpi = FieldParseInfo.getInstance(v.getType(), v.getLength(),
								getCharacterEncoding(), "", "", "");
						if (v != null) {
							try {
								fpi.write(v, outs, vo, "");
							} catch (IOException ex) {
								// should never happen, writing to a ByteArrayOutputStream
							}
						}
					}
				}
			}
		}
	}

	@Override
	public <T> ISOValue<?> parseString(int field, byte[] buf, int pos, ISO8583VO vo, Object custom, boolean isTraceEnable)
			throws ISO8583ParseException, UnsupportedEncodingException {
		//find right define
		ExpressionParser parser = new SpelExpressionParser();
		ReDefineVo reVo = new ReDefineVo();
		reVo.setBuf(buf);
		reVo.setVo(vo);
		reVo.setPos(pos);
		reVo.setField(field);
		reVo.setCustom(custom);
		reVo.setTraceEnable(isTraceEnable);
		Boolean match = false;
		String matchId = "";
		for (String id : router.keySet()) {
			String expContent = router.get(id);
			if (expContent.equals("default")) {
				matchId = id;
				continue;
			}
			Expression exp = parser.parseExpression(expContent);
			match = exp.getValue(reVo, Boolean.class);
			if (match) {
				matchId = id;
				break;
			}
		}
		ISO8583Fields result;
		if(getAlt() != null && !getAlt().isEmpty() && getAlt().equals("s")){
			int l = buf.length - pos;
			byte[] temp = new byte[l];
			System.arraycopy(buf, pos, temp, 0, l);
			result = parseStringNestFields(temp, matchId, vo, isTraceEnable, (String[]) custom);
		}else{
			result = parseStringNestFields(buf, matchId, vo, isTraceEnable, (String[]) custom);
		}
		result.setId(matchId);
		return new ISOValue<ISO8583Fields>(type, result, result.getLength(false), getDescription(), true);
	}

	@Override
	public <T> void writeString(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		if (value.getValue() instanceof ISO8583Fields) {
			ISO8583Fields fields = (ISO8583Fields) value.getValue();

			if (fields.getId() != null && this.getMapProvider().containsKey(fields.getId())) {
				outs.write(writeStringNestFields(fields, fields.getId(), vo, ""));
			} else {
				for (int i = 0; i < 128; i++) {
					if (fields.hasField(i)) {
						ISOValue<?> v = fields.getField(i);
						FieldParseInfo fpi = FieldParseInfo.getInstance(v.getType(), v.getLength(),
								getCharacterEncoding(), "", "", "");
						if (v != null) {
							try {
								fpi.writeString(v, outs, vo, "");
							} catch (IOException ex) {
								// should never happen, writing to a ByteArrayOutputStream
							}
						}
					}
				}
			}
		}
	}

	//20210927_add redefine param
	public class ReDefineVo {
		private byte[] buf;
		private ISO8583VO vo;
		private int pos;
		private int field;
		private Object custom;
		private boolean isTraceEnable;
		
		public byte[] getBuf() {
			return buf;
		}

		public void setBuf(byte[] buf) {
			this.buf = buf;
		}

		public ISO8583VO getVo() {
			return vo;
		}

		public void setVo(ISO8583VO vo) {
			this.vo = vo;
		}
		
		public int getPos() {
			return pos;
		}

		public void setPos(int pos) {
			this.pos = pos;
		}

		public int getField() {
			return field;
		}

		public void setField(int field) {
			this.field = field;
		}

		public Object getCustom() {
			return custom;
		}

		public void setCustom(Object custom) {
			this.custom = custom;
		}

		public boolean isTraceEnable() {
			return isTraceEnable;
		}

		public void setTraceEnable(boolean isTraceEnable) {
			this.isTraceEnable = isTraceEnable;
		}
		
		

	}

}
