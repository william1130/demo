package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import proj.nccc.logsearch.vo.EmvUiLogArg;
import com.edstw.persist.jdbc.AbstractJdbcPersistableBuilder;
import com.edstw.persist.jdbc.JdbcPersistable;
import com.edstw.persist.jdbc.JdbcPersistableBuilder;
import com.edstw.service.QueryServiceException;
import com.edstw.sql.PagingInfo;
import com.edstw.util.ValidateUtil;

public class EmvUiLogQueryQSImpl extends ProjQS {

	private static JdbcPersistableBuilder builder = new AbstractJdbcPersistableBuilder() {
		protected JdbcPersistable build(ResultSet rs) throws SQLException {
			EmvUiLogArg obj = new EmvUiLogArg();
			obj.setUiDate(rs.getTimestamp("UI_DATE"));
			obj.setUserId(rs.getString("USER_ID"));
			obj.setUuid(rs.getString("UUID"));
			obj.setUiFunction(rs.getString("UI_FUNCTION"));
			obj.setUiOther(rs.getString("UI_OTHER"));
			return obj;
		}
	};

	public int queryCount(EmvUiLogArg arg) throws QueryServiceException {
		List<Object> params = new ArrayList<Object>();

		StringBuffer cmd = new StringBuffer().append("select count(*) cnt from emv_ui_log where 1 = 1 ");

		prepareArg(arg, params, cmd);

		String value = this.queryString(cmd.toString(), params, "cnt");

		return Integer.parseInt(value);
	}

	private void prepareArg(EmvUiLogArg arg, List<Object> params, StringBuffer cmd) {

		if (ValidateUtil.isNotBlank(arg.getDateFrom())) {
			// cmd.append("AND UI_DATE >= CONVERT( VARCHAR , ?)");
			cmd.append("AND TO_CHAR(UI_DATE, 'YYYYMMDD') >= ? ");
			params.add(arg.getDateFrom());
		}

		// 日期迄
		if (ValidateUtil.isNotBlank(arg.getDateTo())) {
			// cmd.append("AND UI_DATE < DATEADD(DAY , 1 , ?) ");
			cmd.append("AND UI_DATE < TO_DATE(? ,'YYYY-MM-DD') + INTERVAL '1' DAY  ");
			params.add(arg.getDateTo());
		}

		if (ValidateUtil.isNotBlank(arg.getUserId())) {
			cmd.append("AND USER_ID=?  ");
			params.add(arg.getUserId());
		}

	}

	/**
	 * @return
	 * @throws QueryServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<EmvUiLogArg> query(EmvUiLogArg arg) throws QueryServiceException {

		List<Object> params = new ArrayList<Object>();

		StringBuffer cmd = new StringBuffer().append("select * from emv_ui_log where 1 = 1 ");

		prepareArg(arg, params, cmd);

		// 排序選擇 回應碼需要另外處理 排序順序為 空白、A、D、C、P 在 AtslogTransLogProcImpl 處理
		List<EmvUiLogArg> list = null;

		StringBuffer sortStr = new StringBuffer("order by ui_date desc");

		cmd.append(sortStr);
		// list = (List<EmvUiLogArg>) this.queryByPagingInfo(cmd.toString(), params,
		// builder, arg.getPagingInfo());
		/*
		 * list = this.queryObjectList(cmd.toString(), params, builder); if(list != null
		 * && list.size() > 0){ list = setPageInfo(arg.getPagingInfo(), list); }
		 */
		list = (List<EmvUiLogArg>) this.queryByPagingInfo(cmd.toString(), params, builder, arg.getPagingInfo());

		return list;
	}

	private List<EmvUiLogArg> setPageInfo(PagingInfo pagingInfo, List<EmvUiLogArg> list) {
		int rowCount = 0;
		int pageSize = pagingInfo.getPageSize();
		int currentPage = pagingInfo.getCurrentPage();
		boolean paging = false;
		List<EmvUiLogArg> result = new ArrayList<EmvUiLogArg>();
		if (pagingInfo.isEnablePaging()) {

			rowCount = list.size();

			if (pageSize <= 0)
				pageSize = 30;
			else if (pageSize > MAX_RECORD_COUNT)
				pageSize = MAX_RECORD_COUNT;

			int pageCount = (rowCount % pageSize) == 0 ? rowCount / pageSize : (int) (rowCount / pageSize + 1);

			if (currentPage <= 0)
				currentPage = 1;
			else if (currentPage > pageCount)
				currentPage = pageCount;

			int rowIndexFrom = (currentPage - 1) * pageSize;

			for (int i = rowIndexFrom; i < pageSize * currentPage && i < list.size(); i++) {
				result.add(list.get(i));
			}

			pagingInfo.setCurrentPage(currentPage);
			pagingInfo.setTotalCount(pageCount);
			paging = true;
		}

		if (!paging) {

			result.addAll(list);

			pagingInfo.setCurrentPage(1);
			pagingInfo.setTotalCount(1);
		}

		pagingInfo.setResultList(result);
		return result;
	}

}
