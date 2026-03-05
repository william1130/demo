package com.hp.nccc.iso8583.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChipParseInfo {
	private Map<String, ChipFieldInfo> chipFieldInfos = new HashMap<String, ChipFieldInfo>();
	private Set<Byte> doubleBytesTags = new HashSet<Byte>();
	private Set<String> tripleBytesTags = new HashSet<String>();

	public Map<String, ChipFieldInfo> getChipFieldInfos() {
		return chipFieldInfos;
	}

	public void setChipFieldInfos(Map<String, ChipFieldInfo> chipFieldInfos) {
		this.chipFieldInfos = chipFieldInfos;
	}

	public Set<Byte> getDoubleBytesTags() {
		return doubleBytesTags;
	}

	public void setDoubleBytesTags(Set<Byte> doubleBytesTags) {
		this.doubleBytesTags = doubleBytesTags;
	}

	public Set<String> getTripleBytesTags() {
		return tripleBytesTags;
	}

	public void setTripleBytesTags(Set<String> tripleBytesTags) {
		this.tripleBytesTags = tripleBytesTags;
	}

	public class ChipFieldInfo {
		private byte[] tag;
		private int length;
		private String desc;

		public byte[] getTag() {
			return tag;
		}

		public void setTag(byte[] tag) {
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
