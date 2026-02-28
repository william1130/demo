package proj.nccc.atsLog.batch.util;

public class CommonInfo {

	public static final String CARDTYPE_VISA = "1";
	public static final String CARDTYPE_MASTERCARD = "2";
	public static final String CARDTYPE_JCB = "4";
	public static final String STYLE_FILE_SPEC = "fileSpec";
	public static final String STYLE_FILE_IMPORT = "fileImport";
	public static final String STYLE_FILE_EXPORT = "fileExport";
	public static final String STYLE_CALCULATE = "calculate";
	public static final String STYLE_TABLE = "tableTrans";

	public static final String TYPE_JOB_STATUS_WAITING = "W";
	public static final String TYPE_JOB_STATUS_RUNNING = "R";
	public static final String TYPE_JOB_STATUS_SUCCESS = "S";
	public static final String TYPE_JOB_STATUS_FAIL = "F";
	public static final String TYPE_JOB_RESULT_FILENOTFOUND = "File Not Found.";
	public static final String TYPE_JOB_RESULT_DATANOTFOUND = "Data Not Found.";
	public static final String TYPE_JOB_RESULT_NOTREADY = "批次未備妥";
	public static final String TYPE_MSG_01 = "資料檔不存在，請確認檔案是否已存在[argu_1]";
	public static final String TYPE_MSG_02 = "argu_1讀檔錯誤";
	public static final String TYPE_MSG_92 = "無法連接其他系統資料庫, 連接失敗 [argu_1]";
	public static final String TYPE_MSG_93 = " argu_1 ";
	public static final String TYPE_MSG_CODE_93 = "errorMsg-93";
	public static final String TYPE_MSG_CODE_94 = "errorMsg-94";
	public static final String TYPE_MSG_95 = "更新資料失敗";
	public static final String TYPE_MSG_CODE_95 = "errorMsg-95";
	public static final String TYPE_MSG_96 = "新增資料失敗";
	public static final String TYPE_MSG_CODE_96 = "errorMsg-96";
	public static final String TYPE_MSG_97 = "新增資料失敗後, 更新也失敗. ";
	public static final String TYPE_MSG_CODE_97 = "errorMsg-97";
	public static final String TYPE_MSG_AA = "批次執行完成. ";

	public CommonInfo() {
	}

}