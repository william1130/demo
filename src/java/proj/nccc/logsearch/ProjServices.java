package proj.nccc.logsearch;

import com.dxc.nccc.aplog.edstw.proc.LogFunctionStateProc;
import com.dxc.nccc.aplog.edstw.sql.LogFunctionStateQS;
import com.edstw.service.ServiceManager;

import proj.nccc.logsearch.proc.AuthBizOut0200Proc;
import proj.nccc.logsearch.proc.AuthLogDataProc;
import proj.nccc.logsearch.proc.AuthLogReportProc;
import proj.nccc.logsearch.proc.ChesgDailyTotalsProc;
import proj.nccc.logsearch.proc.EchoMessageProc;
import proj.nccc.logsearch.proc.EmvCardTypeProc;
import proj.nccc.logsearch.proc.EmvCardTypeTempProc;
import proj.nccc.logsearch.proc.EmvRefSpecProc;
import proj.nccc.logsearch.proc.EmvRefSpecTempProc;
import proj.nccc.logsearch.proc.EmvTagRecordDetailProc;
import proj.nccc.logsearch.proc.EmvTagRecordDetailTempProc;
import proj.nccc.logsearch.proc.EmvTagRecordMasterProc;
import proj.nccc.logsearch.proc.EmvTagRecordMasterTempProc;
import proj.nccc.logsearch.proc.EmvUiLogProc;
import proj.nccc.logsearch.proc.ProjProc;
import proj.nccc.logsearch.qs.AuthLogDataQS;
import proj.nccc.logsearch.qs.AuthRuleQS;
import proj.nccc.logsearch.qs.BinBankParmQS;
import proj.nccc.logsearch.qs.BinBinoParmQS;
import proj.nccc.logsearch.qs.CallBankParmQS;
import proj.nccc.logsearch.qs.EmvCardTypeQS;
import proj.nccc.logsearch.qs.EmvCardTypeTempQS;
import proj.nccc.logsearch.qs.EmvRefSpecQS;
import proj.nccc.logsearch.qs.EmvRefSpecTempQS;
import proj.nccc.logsearch.qs.EmvTagRecordDetailQS;
import proj.nccc.logsearch.qs.EmvTagRecordDetailTempQS;
import proj.nccc.logsearch.qs.EmvTagRecordMasterQS;
import proj.nccc.logsearch.qs.EmvTagRecordMasterTempQS;
import proj.nccc.logsearch.qs.EmvUiLogQS;
import proj.nccc.logsearch.qs.MerchantQS;
import proj.nccc.logsearch.qs.MfesUcsApplyCodeQS;
import proj.nccc.logsearch.qs.MfesUcsBatchHeaderQS;
import proj.nccc.logsearch.qs.ProjQS;
import proj.nccc.logsearch.qs.SetlBinDatasQS;

public class ProjServices {

	public static ProjQS getAtslogTransLogQS() {
		return (ProjQS) ServiceManager.getInstance().getService("query.AtslogTransLog");
	}

	public static ProjProc getAtslogTransLogProc() {
		return (ProjProc) ServiceManager.getInstance().getService("proc.AtslogTransLog");
	}

	public static ProjQS getAtslogRawLogQS() {
		return (ProjQS) ServiceManager.getInstance().getService("query.AtslogRawLog");
	}

	public static ProjQS getNssmbankQS() {
		return (ProjQS) ServiceManager.getInstance().getService("query.Nssmbank");
	}

	public static ProjQS getMerchantChinQS() {
		return (ProjQS) ServiceManager.getInstance().getService("query.MerchantChin");
	}

	public static ProjQS getAtsRejectCodeSetupQS() {
		return (ProjQS) ServiceManager.getInstance().getService("query.AtsRejectCodeSetup");
	}

	public static ProjQS getAtsTransTypeQS() {
		return (ProjQS) ServiceManager.getInstance().getService("query.AtsTransType");
	}

	public static ProjQS getAtsResponseCodeSetupQS() {
		return (ProjQS) ServiceManager.getInstance().getService("query.AtsResponseCodeSetup");
	}

	public static ProjQS getAtsCpcMerchantQS() {
		return (ProjQS) ServiceManager.getInstance().getService("query.AtsCpcMerchant");
	}

	public static ProjQS getChipcardInfoQS() {
		return (ProjQS) ServiceManager.getInstance().getService("query.ChipcardInfo");
	}

	public static ProjQS getIso8583InfoQS() {
		return (ProjQS) ServiceManager.getInstance().getService("query.Iso8583Info");
	}

	public static ProjQS getSpswReasonCodeQS() {
		return (ProjQS) ServiceManager.getInstance().getService("query.SpswReasonCode");
	}

	public static ProjQS getSpswResponseCodeSetupQS() {
		return (ProjQS) ServiceManager.getInstance().getService("query.SpswResponseCode");
	}

	public static ProjQS getEmvCardTypeSetupQS() {
		return (ProjQS) ServiceManager.getInstance().getService("query.EmvCardTypeSetup");
	}

	public static ProjQS getEmvUiLogQueryQS() {
		return (ProjQS) ServiceManager.getInstance().getService("query.EmvUiLogQuery");
	}

	public static ProjProc getEmvUiLogQueryProc() {
		return (ProjProc) ServiceManager.getInstance().getService("proc.EmvUiLogQuery");
	}

	public static EmvCardTypeQS getEmvCardTypeQS() {
		return (EmvCardTypeQS) ServiceManager.getInstance().getService("query.EmvCardType");
	}

	public static EmvCardTypeProc getEmvCardTypeProc() {
		return (EmvCardTypeProc) ServiceManager.getInstance().getService("proc.EmvCardType");
	}

	public static EmvCardTypeTempQS getEmvCardTypeTempQS() {
		return (EmvCardTypeTempQS) ServiceManager.getInstance().getService("query.EmvCardTypeTemp");
	}

	public static EmvCardTypeTempProc getEmvCardTypeTempProc() {
		return (EmvCardTypeTempProc) ServiceManager.getInstance().getService("proc.EmvCardTypeTemp");
	}

	public static EmvUiLogQS getEmvUiLogQS() {
		return (EmvUiLogQS) ServiceManager.getInstance().getService("query.EmvUiLog");
	}

	public static EmvUiLogProc getEmvUiLogProc() {
		return (EmvUiLogProc) ServiceManager.getInstance().getService("proc.EmvUiLog");
	}

	public static EmvRefSpecQS getEmvRefSpecQS() {
		return (EmvRefSpecQS) ServiceManager.getInstance().getService("query.EmvRefSpec");
	}

	public static EmvRefSpecProc getEmvRefSpecProc() {
		return (EmvRefSpecProc) ServiceManager.getInstance().getService("proc.EmvRefSpec");
	}

	public static EmvRefSpecTempQS getEmvRefSpecTempQS() {
		return (EmvRefSpecTempQS) ServiceManager.getInstance().getService("query.EmvRefSpecTemp");
	}

	public static EmvRefSpecTempProc getEmvRefSpecTempProc() {
		return (EmvRefSpecTempProc) ServiceManager.getInstance().getService("proc.EmvRefSpecTemp");
	}

	public static EmvTagRecordMasterQS getEmvTagRecordMasterQS() {
		return (EmvTagRecordMasterQS) ServiceManager.getInstance().getService("query.EmvTagRecordMaster");
	}

	public static EmvTagRecordDetailQS getEmvTagRecordDetailQS() {
		return (EmvTagRecordDetailQS) ServiceManager.getInstance().getService("query.EmvTagRecordDetail");
	}

	public static EmvTagRecordMasterProc getEmvTagRecordMasterProc() {
		return (EmvTagRecordMasterProc) ServiceManager.getInstance().getService("proc.EmvTagRecordMaster");
	}

	public static EmvTagRecordDetailProc getEmvTagRecordDetailProc() {
		return (EmvTagRecordDetailProc) ServiceManager.getInstance().getService("proc.EmvTagRecordDetail");
	}

	public static EmvTagRecordMasterTempQS getEmvTagRecordMasterTempQS() {
		return (EmvTagRecordMasterTempQS) ServiceManager.getInstance().getService("query.EmvTagRecordMasterTemp");
	}

	public static EmvTagRecordDetailTempQS getEmvTagRecordDetailTempQS() {
		return (EmvTagRecordDetailTempQS) ServiceManager.getInstance().getService("query.EmvTagRecordDetailTemp");
	}

	public static EmvTagRecordMasterTempProc getEmvTagRecordMasterTempProc() {
		return (EmvTagRecordMasterTempProc) ServiceManager.getInstance().getService("proc.EmvTagRecordMasterTemp");
	}

	public static EmvTagRecordDetailTempProc getEmvTagRecordDetailTempProc() {
		return (EmvTagRecordDetailTempProc) ServiceManager.getInstance().getService("proc.EmvTagRecordDetailTemp");
	}

	public static ProjQS getIFESTransLogQS() {
		return (ProjQS) ServiceManager.getInstance().getService("query.IFESTransLog");
	}

	public static ProjProc getIFESTransLogProc() {
		return (ProjProc) ServiceManager.getInstance().getService("proc.IFESTransLog");
	}

	public static ProjQS getIFESRawLogQS() {
		return (ProjQS) ServiceManager.getInstance().getService("query.IFESRawLog");
	}

	public static ProjQS getIFESTransTypeMappingQS() {
		return (ProjQS) ServiceManager.getInstance().getService("query.IFESTransTypeMapping");
	}

	public static ProjQS getTKSTransTypeMappingQS() {
		return (ProjQS) ServiceManager.getInstance().getService("query.TKSTransTypeMapping");
	}

	public static ProjQS getLDSSTransLogQS() {
		return (ProjQS) ServiceManager.getInstance().getService("query.LDSSTransLog");
	}

	public static ProjProc getLDSSTransLogProc() {
		return (ProjProc) ServiceManager.getInstance().getService("proc.LDSSTransLog");
	}

	public static ProjQS getLDSSRawLogQS() {
		return (ProjQS) ServiceManager.getInstance().getService("query.LDSSRawLog");
	}

	public static ProjQS getLDSSTransTypeMappingQS() {
		return (ProjQS) ServiceManager.getInstance().getService("query.LDSSTransTypeMapping");
	}

	public static ProjProc getMFESCheckoutLogProc() {
		return (ProjProc) ServiceManager.getInstance().getService("proc.MFESCheckoutLog");
	}

	public static ProjQS getMFESCheckoutLogQS() {
		return (ProjQS) ServiceManager.getInstance().getService("query.MFESCheckoutLog");
	}

	public static ProjQS getMFESCheckoutLogGetTerminalQS() {
		return (ProjQS) ServiceManager.getInstance().getService("query.MFESCheckoutLogGetTermina");
	}

	public static ProjQS getParameterQS() {
		return (ProjQS) ServiceManager.getInstance().getService("query.Parameter");
	}

	// -- M2017092 : ATSLOG Merchant Side
	public static MfesUcsApplyCodeQS getMfesUcsApplyCodeQS() {
		return (MfesUcsApplyCodeQS) ServiceManager.getInstance().getService("query.MfesUcsApplyCode");
	}

	public static MfesUcsBatchHeaderQS getMfesUcsBatchHeaderQS() {
		return (MfesUcsBatchHeaderQS) ServiceManager.getInstance().getService("query.MfesUcsBatchHeader");
	}

	// APLOG
	public static LogFunctionStateProc getLogFunctionStateProc() {
		return (LogFunctionStateProc) ServiceManager.getInstance().getService("proc.logFunctionState");
	}

	public static LogFunctionStateQS getLogFunctionStateQS() {
		return (LogFunctionStateQS) ServiceManager.getInstance().getService("query.logFunctionState");
	}

	public static AuthLogDataQS getAuthLogDataQS() {
		return (AuthLogDataQS) ServiceManager.getInstance().getService("query.authLogData");
	}

	public static AuthLogDataProc getAuthLogDataProc() {
		return (AuthLogDataProc) ServiceManager.getInstance().getService("proc.authLogData");
	}

	public static BinBinoParmQS getBinBinoParmQS() {
		return (BinBinoParmQS) ServiceManager.getInstance().getService("query.binBinoParm");
	}

	public static BinBankParmQS getBinBankParmQS() {
		return (BinBankParmQS) ServiceManager.getInstance().getService("query.binBankParm");
	}

	public static AuthRuleQS getAuthRuleQS() {
		return (AuthRuleQS) ServiceManager.getInstance().getService("query.authRule");
	}

	public static SetlBinDatasQS getSetlBinDatasQS() {
		return (SetlBinDatasQS) ServiceManager.getInstance().getService("query.setlBinDatas");
	}

	public static AuthLogReportProc getAuthLogReportProc() {
		return (AuthLogReportProc) ServiceManager.getInstance().getService("report.authLogReport");
	}

	public static MerchantQS getMerchantQS() {
		return (MerchantQS) ServiceManager.getInstance().getService("query.merchant");
	}

	public static AuthBizOut0200Proc getAuthBizOut0200Proc() {
		return (AuthBizOut0200Proc) ServiceManager.getInstance().getService("proc.authBizOut0200");
	}

	public static CallBankParmQS getCallBankParmQS() {
		return (CallBankParmQS) ServiceManager.getInstance().getService("query.callBankParm");
	}
	/** Echo Message */
	public static EchoMessageProc getEchoMessageProc() {
		return (EchoMessageProc) ServiceManager.getInstance().getService("proc.echoMessage");
	}

	public static ChesgDailyTotalsProc getChesgDailyTotalsProc() {
		return (ChesgDailyTotalsProc) ServiceManager.getInstance().getService("proc.ChesgDailyTotals");
	}
}
