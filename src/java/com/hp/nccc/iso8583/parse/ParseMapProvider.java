package com.hp.nccc.iso8583.parse;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ParseMapProvider {

	private Map<String, Map<Integer, FieldParseInfo>> parseMap = new HashMap<String, Map<Integer, FieldParseInfo>>();
	private Map<String, String> descMap = new HashMap<String, String>();
	private Map<String, String> lengthTypeMap = new HashMap<String, String>();
	private Map<String, Set<String>> idSet = new HashMap<String, Set<String>>();

	public Map<Integer, FieldParseInfo> getFieldsParseInfo(String id, String... conditions) {
		if (conditions.length == 0) {
			conditions = new String[]{""};
		}
		return parseMap.get(getFieldsParseKey(id, conditions));
	}
	
	public String getFieldsParseKey(String id, String... conditions){
		if (conditions.length == 0) {
			conditions = new String[]{""};
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < conditions.length; i++) {
			if (conditions[i] != null) {
				sb.append(conditions[i]);
			}
		}
		if (parseMap.containsKey(id + sb.toString())) {
			return id + sb.toString();
		} else {
			if (conditions.length == 1) {
				return id;
			}
			String[] newConditions = new String[conditions.length - 1];
			System.arraycopy(conditions, 0, newConditions, 0, newConditions.length);
			return getFieldsParseKey(id, newConditions);
		}
	}

	public void setFieldsParseInfo(Map<Integer, FieldParseInfo> infos, String id, String... conditions) {
		StringBuffer sb = new StringBuffer();
		for (String s : conditions) {
			if (s != null) {
				sb.append(s);
			}
		}
		parseMap.put(id + sb.toString(), infos);
		if(containsKey(id)){
			if(!idSet.get(id).contains(id + sb.toString())){
				idSet.get(id).add(id + sb.toString());
			}
		}else{
			Set<String> ids = new HashSet<String>();
			ids.add(id + sb.toString());
			idSet.put(id, ids);
		}
	}

	public String getDesc(String id, String... conditions) {
		if (conditions.length == 0) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < conditions.length; i++) {
			if (conditions[i] != null) {
				sb.append(conditions[i]);
			}
		}
		if (descMap.containsKey(sb.toString())) {
			return descMap.get(sb.toString());
		} else {
			if (conditions.length == 1) {
				return descMap.get(id);
			}
			String[] newConditions = new String[conditions.length - 1];
			System.arraycopy(conditions, 0, newConditions, 0, newConditions.length);
			return getDesc(id, newConditions);
		}
	}

	public void setDesc(String desc, String id, String... conditions) {
		StringBuffer sb = new StringBuffer();
		for (String s : conditions) {
			if (s != null) {
				sb.append(s);
			}
		}
		descMap.put(id + sb.toString(), desc);
	}
	
	public void setLengthType(String lengthType, String id, String... conditions) {
		StringBuffer sb = new StringBuffer();
		for (String s : conditions) {
			if (s != null) {
				sb.append(s);
			}
		}
		lengthTypeMap.put(id + sb.toString(), lengthType);
	}
	
	public Map<String, String> getLengthTypeMap() {
		return lengthTypeMap;
	}

	public Map<String, Map<Integer, FieldParseInfo>> getMap() {
		return parseMap;
	}

	public boolean containsKey(String id) {
		return idSet.containsKey(id);
	}
	
	public ParseMapProvider getSubParseMapProvider(String id){
		ParseMapProvider p = new ParseMapProvider();
		if(idSet.containsKey(id)){
			for(String i:idSet.get(id)){
				String conditions = i.substring(id.length());
				p.setFieldsParseInfo(parseMap.get(i), id, conditions);
				p.setDesc(descMap.get(i), id, conditions);
				p.setLengthType(lengthTypeMap.get(i), id, conditions);
			}
		}
		return p;
	}
}
