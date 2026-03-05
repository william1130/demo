package proj.nccc.logsearch.proc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.edstw.persist.PersistableManager;
import com.edstw.persist.PersistableTransaction;
import com.edstw.persist.StatefulPersistableUtil;
import com.edstw.process.ProcessException;
import com.edstw.report.Report;
import com.edstw.service.ServiceException;

import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.beans.MyDateUtil;
import proj.nccc.logsearch.persist.ChesgDailyTotals;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.qs.AtslogTransLogQSImpl;
import proj.nccc.logsearch.qs.BaseCRUDQS;
import proj.nccc.logsearch.qs.MerchantChinQSImpl;
import proj.nccc.logsearch.report.AtslogChesgExcel;
import proj.nccc.logsearch.vo.AtslogTransLogArg;
import proj.nccc.logsearch.vo.ChesgDailyTotalsArg;
import proj.nccc.logsearch.vo.ProjPersistableArg;

public class ChesgDailyTotalsProcImpl extends AbstractBaseCRUDProc implements ChesgDailyTotalsProc {

	public ChesgDailyTotalsProcImpl() {
	}

	public BaseCRUDQS getBaseCRUDQS() {
		return ProjServices.getEmvCardTypeQS();
	}

	public EmvProjPersistable createEmptyProjPersistable() {
		return new ChesgDailyTotals();
	}

	protected void mergeOnModify(ProjPersistableArg arg, EmvProjPersistable entity) throws Exception {
		StatefulPersistableUtil.copyPropertiesWithoutState(entity, arg);
	}

	public String getServiceName() {
		return "Chesg Daily Totals Process";
	}

	public void setServiceParams(Map map) throws ServiceException {
		// do nothing
	}

	/**
	 * Get CHESG List (M2025074_R114117) </br>
	 * 使用「交易日期、端末機代號、特店代號」做群組，將「總交易筆數、CHESG FLAG交易筆數」分別加總
	 * 
	 * @param arg
	 * @return list
	 * @throws ProcessException
	 */
	public List<ChesgDailyTotalsArg> getChesgList(ChesgDailyTotalsArg chesgArg) throws ProcessException {
		LocalDate dateFrom = LocalDate.parse(chesgArg.getTranDateFrom(), MyDateUtil.YYYYMMDD);
		LocalDate dateTo = LocalDate.parse(chesgArg.getTranDateTo(), MyDateUtil.YYYYMMDD);
		LocalDate currentDate = dateFrom;

		AtslogTransLogQSImpl qs = (AtslogTransLogQSImpl) ProjServices.getAtslogTransLogQS();

		while (currentDate.isBefore(dateTo) || currentDate.isEqual(dateTo)) {
			String queryDate = currentDate.format(MyDateUtil.YYYYMMDD);
			List<ChesgDailyTotalsArg> list = qs.queryChesgList(null, queryDate, false);

			// 若查日期為空，再去TransLog確認是否需要從TransLog新增資料。
			if (list != null && list.isEmpty()) {
				updateChesg(qs.queryChesgfromTranslog("", queryDate, false));
			}

			currentDate = currentDate.plusDays(1);
		}

		List<ChesgDailyTotalsArg> resultList = qs.queryChesgList(chesgArg, "", true);

		// get Merchant Chinese Name begin
		MerchantChinQSImpl mcQs = (MerchantChinQSImpl) ProjServices.getMerchantChinQS();
		List<String> mchtIds = resultList.stream().map(ChesgDailyTotalsArg::getMerchantId).collect(Collectors.toList());
		Map<String, String> merchantNameMap = mcQs.queryNameMap(mchtIds);

		for (ChesgDailyTotalsArg obj : resultList) {
			obj.setMerchantChinName((String) merchantNameMap.get(obj.getMerchantId()));
		}

		return resultList;
	}

	public int queryChesgCount(ChesgDailyTotalsArg arg) {
		AtslogTransLogQSImpl qs = (AtslogTransLogQSImpl) ProjServices.getAtslogTransLogQS();
		return qs.queryChesgCount(arg, true);
	}

	/**
	 * Generate CHESG Report (M2025074_R114117)
	 * 
	 * @param list
	 * @return
	 * @throws ProcessException
	 */
	public Report genChesgReport(List<?> list, String queryRange) throws ProcessException {

		try {
			AtslogChesgExcel rep = new AtslogChesgExcel();
			rep.setReportName("持卡人簽聯帳單筆數統計表");
			rep.setReportFileName("AtslogChesgReport.xls");
			rep.setQueryRange(queryRange);
			rep.buildWorkbook(list);
			return rep;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ProcessException(e);
		}
	}

	public void ChesgDailyTotalsTask() {
		// SysDate - 1
		LocalDate previousDate = LocalDate.now().minusDays(1);

		String dateFrom = "", dateTo = "";
		AtslogTransLogQSImpl qs = (AtslogTransLogQSImpl) ProjServices.getAtslogTransLogQS();
		
		// Step1.先查詢前一天的資料、Table中是否為空
		dateTo = previousDate.format(MyDateUtil.YYYYMMDD);
		List<ChesgDailyTotalsArg> chesgList = qs.queryChesgList(null, dateTo, false);
		int count = qs.queryChesgCount(null, false);

		// Step2.1.若前一天查無資料、Table中也是空的，則判斷為首次加總，查詢180天內的資料加總。Housekeeing 720 days.
		// Step2.2.若非首次執行，只需加總前一天資料。
		boolean isFirstRun = false;
		if (chesgList != null && chesgList.isEmpty() && count == 0) {
			dateFrom = previousDate.minusDays(180).format(MyDateUtil.YYYYMMDD);
			isFirstRun = true;
		}

		// Step3.查詢/計算交易量
		List<AtslogTransLogArg> list = qs.queryChesgfromTranslog(dateFrom, dateTo, isFirstRun);

		// Step4.若執行Task時，發現當日無交易資料，將該日加總為0。
		List<String> logDates = list.stream().map(AtslogTransLogArg::getLogDate).collect(Collectors.toList());
		dateFrom = isFirstRun ? dateFrom : dateTo;
		LocalDate startDate = LocalDate.parse(dateFrom, MyDateUtil.YYYYMMDD);
		LocalDate endDate = LocalDate.parse(dateTo, MyDateUtil.YYYYMMDD);
		LocalDate currentDate = startDate;
		while (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
			String queryDate = currentDate.format(MyDateUtil.YYYYMMDD);
			if (logDates.indexOf(queryDate) < 0) {
				AtslogTransLogArg atl= new AtslogTransLogArg();
				atl.setLogDate(queryDate);
				atl.setMerchantId("----------");
				atl.setTermId("--------");
				list.add(atl);
			}
			currentDate = currentDate.plusDays(1);
		}

		// Step5.儲存資料
		updateChesg(list);
	}

	/**
	 * 更新ChesgDaily
	 * 
	 * @param list
	 * @param chesgList
	 */
	private void updateChesg(List<AtslogTransLogArg> atsList) {
		if (atsList == null || atsList.isEmpty())
			return;

		PersistableManager pm = super.currentPersistableManager();
		PersistableTransaction pt = pm.getTransaction();
		try {
			pt = pm.getTransaction();
			pt.begin();
			for (AtslogTransLogArg ats : atsList) {
				AtslogTransLogQSImpl qs = (AtslogTransLogQSImpl) ProjServices.getAtslogTransLogQS();
				ChesgDailyTotals chesg = qs.queryChesg(ats);
				if (chesg != null) {
					chesg.setTotalCount(ats.getTotalCount());
					chesg.setChesgCount(ats.getChesgCount());
					pm.persist(chesg);
				} else {
					pm.persist(new ChesgDailyTotals(ats));
				}
			}

			pt.commit();
		} catch (Exception e) {
			try {
				pt.rollback();
			} catch (Exception e1) {
				getLog().error(e1.getMessage(), e);
			}
			getLog().error(e.getMessage(), e);
			throw new ProcessException(e.getMessage(), e);
		}
	}
}
