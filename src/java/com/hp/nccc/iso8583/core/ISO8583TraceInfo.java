package com.hp.nccc.iso8583.core;

import java.io.Serializable;
import java.util.List;

public class ISO8583TraceInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 491024431817696422L;
	private List<ISO8583FieldTraceInfo> traceInfos;

	public List<ISO8583FieldTraceInfo> getTraceInfos() {
		return traceInfos;
	}

	public void setTraceInfos(List<ISO8583FieldTraceInfo> traceInfos) {
		this.traceInfos = traceInfos;
	}
	
	private ISO8583TraceInfo traceInfo;

	public ISO8583TraceInfo getTraceInfo() {
		return traceInfo;
	}

	public void setTraceInfo(ISO8583TraceInfo traceInfo) {
		this.traceInfo = traceInfo;
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		if(traceInfos != null){
			int lastIndex = 0;
			for(ISO8583FieldTraceInfo info:traceInfos){
				if(lastIndex == traceInfos.size() -1){
					sb.append(String.format("last data[%s]",info.toString())).append("\n");
					break;
				}
				sb.append(info.toString()).append("\n");
				lastIndex++;
			}
		}
		if(traceInfo != null){
			sb.append("***** sub fields start *****\n");
			sb.append(traceInfo.toString());
			sb.append("\n***** sub fields end *****");
		}
		return sb.toString();
	}
}
