package com.hp.nccc.iso8583.common;


public class ServerContext {
	public static ServerStaus serverStatus = ServerStaus.STOP;
	public static int MonitorServiceCnt = 0;
	public static int ServerServiceCnt = 0;
	public static final String CMD_STOP_SERVICE = "STOP";
	public static final String CMD_START_SERVICE = "START";
	public static final String CMD_GET_STATUS = "STATUS";
	public static final String CMD_INIT = "INIT";
	public static final byte[] CLRF = "\r\n".getBytes();
	
	public enum ServerStaus {
		RUNNING, STOP, STARTING, STOPING, STOPSERVICE
	};
}
