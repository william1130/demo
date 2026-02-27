/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * Created On : 2011/6/21, 下午 06:19:28, By 許欽程(Shiu Vincent)
 * 
 * ==============================================================================================
 * $Id: ProjQS.java,v 1.3 2014/09/04 03:19:52 asiapacific\hsiehes Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.qs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.edstw.lang.DoubleString;
import com.edstw.lang.IntegerString;
import com.edstw.lang.LongString;
import com.edstw.persist.jdbc.AbstractJdbcPersistableQueryService;
import com.edstw.persist.jdbc.JdbcPersistableBuilder;
import com.edstw.service.QueryServiceException;
import com.edstw.service.ServiceException;
import com.edstw.sql.PagingInfo;

/**
 * Persistable對應的QS類別
 * 
 * @author 許欽程(Shiu Vincent)
 * @version $Revision: 1.3 $
 */
public class ProjQS extends AbstractJdbcPersistableQueryService {
	
	public static final int MAX_RECORD_COUNT = 800;

	public String getServiceName() {
		return this.getClass().getName();
	}

	public void setServiceParams(@SuppressWarnings("rawtypes") Map map) throws ServiceException {
		// do nothing
	}
	
	
	protected List<?> queryByPagingInfo(String cmd, List<Object> params, JdbcPersistableBuilder builder,
			PagingInfo pagingInfo) throws QueryServiceException {

		Connection conn = null;
		List<Object> result = new ArrayList<Object>();
		
		try {
			
			conn = this.getConnection();
			PreparedStatement ps = conn.prepareStatement(cmd,
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			int index = 1;
			
			for (Object vObj : params) {
				if (vObj instanceof Integer)
					ps.setInt(index++, ((Integer) vObj).intValue());
				else if (vObj instanceof Long)
					ps.setLong(index++, ((Long) vObj).longValue());
				else if (vObj instanceof Float)
					ps.setFloat(index++, ((Float) vObj).floatValue());
				else if (vObj instanceof Double)
					ps.setDouble(index++, ((Double) vObj).doubleValue());
				else if (vObj instanceof IntegerString)
					ps.setDouble(index++, ((IntegerString) vObj).intValue());
				else if (vObj instanceof LongString)
					ps.setDouble(index++, ((LongString) vObj).longValue());
				else if (vObj instanceof DoubleString)
					ps.setDouble(index++, ((DoubleString) vObj).doubleValue());
				else if (vObj instanceof Date)
					ps.setTimestamp(index++, new Timestamp(((Date) vObj).getTime()));
				else
					ps.setString(index++, String.valueOf(vObj));
			}
			
			ResultSet rs = ps.executeQuery();
			int rowCount = 0;
			int pageSize = pagingInfo.getPageSize();
			int currentPage = pagingInfo.getCurrentPage();
			boolean paging = false;
			
			if (pagingInfo.isEnablePaging()) {

				if (rs.last()) {
					
					rowCount = rs.getRow();
					rs.beforeFirst();
					
					if (pageSize <= 0)
						pageSize = 30;
					else if (pageSize > MAX_RECORD_COUNT)
						pageSize = MAX_RECORD_COUNT;
					
					int pageCount = (rowCount % pageSize) == 0 ?
							rowCount / pageSize :
							(int) (rowCount / pageSize + 1);
					
					if (currentPage <= 0)
						currentPage = 1;
					else if (currentPage > pageCount)
						currentPage = pageCount;
					
					int rowIndexFrom = (currentPage - 1) * pageSize;
					
					if (rowIndexFrom > 0)
						rs.absolute(rowIndexFrom);
					
					for (int i = 0; i < pageSize && rs.next(); i++) {
						
						Object obj = builder.processResultSet(rs);
						result.add(obj);
					}
					
					pagingInfo.setCurrentPage(currentPage);
					pagingInfo.setTotalCount(pageCount);
					paging = true;
				}
			}
			
			if (!paging) {
				
				int count = 0;
				
				while (rs.next() && count < MAX_RECORD_COUNT) {
					
					Object obj = builder.processResultSet(rs);
					result.add(obj);
					count++;
				}
				
				pagingInfo.setCurrentPage(1);
				pagingInfo.setTotalCount(1);
			}
			
			pagingInfo.setResultList(result);
		}
		catch (Exception e) {
			
			e.printStackTrace();
			throw new QueryServiceException(e);
		}
		finally {
			
			try {
				conn.close();
			}
			catch (Exception e) { }
		}
		
		return result;
	}
}
