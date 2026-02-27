package com.hp.nccc.iso8583.core;

import java.util.HashMap;
import java.util.Map;

public class B24TlvParseInfos {
	private Map<String, B24TlvFieldInfo> b24TlvFieldInfo = new HashMap<String, B24TlvFieldInfo>();

	public Map<String, B24TlvFieldInfo> getB24TlvFieldInfo() {
		return b24TlvFieldInfo;
	}

	public void setB24TlvFieldInfos(Map<String, B24TlvFieldInfo> b24TlvFieldInfo) {
		this.b24TlvFieldInfo = b24TlvFieldInfo;
	}

	public class B24TlvFieldInfo {
		private String tag;
		private int length;
		private String desc;

		public String getTag() {
			return tag;
		}

		public void setTag(String tag) {
			this.tag = tag;
		}

		public int getLength() {
			return length;
		}

		public void setLength(int length) {
			this.length = length;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}


	}
}
