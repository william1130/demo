package proj.nccc.atsLog.batch.process;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.dao.HouseKeepingDao;
import proj.nccc.atsLog.batch.dao.connection.ExadbConn;
import proj.nccc.atsLog.batch.dao.entity.HouseKeeping;
import proj.nccc.atsLog.batch.util.ExpireDateFileNameFilter;
import proj.nccc.atsLog.batch.util.FileUtil;
import proj.nccc.atsLog.batch.util.MyDateUtil;
import proj.nccc.atsLog.batch.vo.ReturnVO;

@Log4j2
public class HouseKeepingProcImpl {
	private HouseKeepingDao dao = null;
	private ExadbConn exaConn = null;

	public HouseKeepingProcImpl() {
		dao = new HouseKeepingDao();
		exaConn = new ExadbConn();
	}

	public ReturnVO process() throws Exception {
		ReturnVO returnVo = new ReturnVO();
		int tableTotalCnt = 0;
		int fileTotalCnt = 0;
		int tableDelCnt = 0;
		int fileDelCnt = 0;
		try {
			System.out.println(">>" + new java.util.Date());

			List<HouseKeeping> list = dao.getHouseKeepingTable();
			if (list != null && list.size() > 0) {
				log.debug("Total " + list.size() + " job waiting for process.");
				for (int i = 0; i < list.size(); i++) {
					HouseKeeping o = (HouseKeeping) list.get(i);
					Date expiredDate = null;
					expiredDate = MyDateUtil.getDateBeforeAfterByDay(new Date(), o.getKeepDays(), "BEFORE");

					String expiredDay = MyDateUtil.yyyymmdd(expiredDate);
					o.setExpireDate(expiredDate);
					if ("TABLE".equalsIgnoreCase(o.getItemCategory())) {
						String tableName = o.getTablePath();
						String whereColumn = o.getLogicalRule();
						log.debug("Data prepation on HouseKeeping Table:" + tableName
								+ ",ExpireDay:" + expiredDay);
						// build SQL
						String sqlCmd = "delete from " + tableName + " where " + whereColumn;
						log.debug(sqlCmd);
						List<String> paraList = new LinkedList<String>();
						paraList.add(expiredDay);
						if ("TABLE_AP".equalsIgnoreCase(o.getFieldFile())) {
							tableDelCnt = exaConn.execSqlRetCnt(sqlCmd, paraList);
						} else {
							tableDelCnt = dao.execSqlRetCnt(sqlCmd, paraList);
						}
						tableTotalCnt += tableDelCnt;
						log.debug("Success to clean " + tableName + ":" + tableDelCnt
								+ " records.");
					} else if ("FILE".equalsIgnoreCase(o.getItemCategory())) {
						String path = o.getTablePath();
						String name = o.getFieldFile();
						log.debug("Data prepation on HouseKeeping File :" + name + " in path:"
								+ path + ",ExpireDay:" + expiredDay);
						fileDelCnt = this.fileExecutor(o);
						fileTotalCnt += fileDelCnt;
						log.debug("Success to delete " + fileDelCnt + " records in path:" + path);
					} else if ("FILE_DATE".equalsIgnoreCase(o.getItemCategory())) {
						String path = o.getTablePath();
						String name = o.getFieldFile();
						log.debug("Data prepation on HouseKeeping File [FILE_DATE]:" + name
								+ " in path:" + path + ",ExpireDay:" + expiredDay);
						fileDelCnt = this.deleteFileByLastModifyDate(o);
						fileTotalCnt += fileDelCnt;
						log.debug("Success to delete " + fileDelCnt + " records in path:" + path);
					}
				}
			} else
				log.debug("no expired table waiting for process.");

			log.debug("Job finished, Total record : Table=" + tableTotalCnt + ", file=" + fileTotalCnt);
			returnVo.setSuccess(true);
			returnVo.setMessage("清除筆數: Table=" + tableTotalCnt + ", file=" + fileTotalCnt);
		} catch (Exception x) {
			x.printStackTrace();
			throw new Exception(x.getMessage(), x);
		} finally {
			if (dao != null)
				dao.close();
		}
		return returnVo;
	}

	private int fileExecutor(HouseKeeping o) {
		log.info(String.format("process file executor .. expire date [%s]", o.getExpireDate()));
		int delCnt = 0;
		o.setDatePattern(o.getLogicalRule());
		o.setName(o.getFieldFile());
		File f = new File(o.getTablePath());
		if (f.isDirectory()) {
			ExpireDateFileNameFilter filter = new ExpireDateFileNameFilter();
			filter.setConfig(o);
			File[] files = f.listFiles(filter);
			for (File deleteFile : files) {
				log.info(String.format("delete file [%s]", deleteFile.toString()));
				boolean b = deleteFile.delete();
				if (b)
					delCnt++;
				else
					log.info(String.format("delete file [%s] fail", deleteFile.toString()));
			}
		}
		return delCnt;
	}

	private int deleteFileByLastModifyDate(HouseKeeping o) throws IOException, ParseException {
		log.info(String.format("process file [FILE_DATE] .. expire date [%s]", o.getExpireDate()));
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-hh:mm:ss");
		int delCnt = 0;
		File target = new File(o.getTablePath());
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -(o.getKeepDays()));
		Date deleteTime = c.getTime();

		boolean regex = false;
		String pattern = "";
		if (o.getFieldFile().contains("*")) {
			pattern = o.getFieldFile().replaceAll("\\*", ".\\*");
			regex = true;
		}

		if (target.isDirectory()) {
			for (File file : target.listFiles()) {
				if (regex) {
					if (file.getName().matches(pattern)) {
						if (checkDeleteFileLastTime(file, deleteTime)) {
							delCnt++;
						}
					}
				} else if (file.getName().startsWith(o.getFieldFile())) {
					if (checkDeleteFileLastTime(file, deleteTime)) {
						delCnt++;
					}
				}
				if ("FILE_DATE".equals(o.getFieldFile())) {
					// ------------------------------------
					// -- delete by file date
					if (checkDeleteFileLastTime(file, deleteTime)) {
						delCnt++;
					}
				}
			}
		} else {
			File file = target;
			FileUtil.delete(file.getAbsolutePath(), false);
			delCnt++;
		}
		return delCnt;
	}

	private boolean checkDeleteFileLastTime(File f, Date deleteTime) throws IOException {
		BasicFileAttributes fa = Files.readAttributes(Paths.get(f.getAbsolutePath()), BasicFileAttributes.class,
				LinkOption.NOFOLLOW_LINKS);
		Date fileTime = new Date(fa.lastModifiedTime().toMillis());
		log.info("File {}, {}, {}", f.getAbsolutePath(), fileTime, deleteTime);
		if (fileTime.before(deleteTime)) {
			log.info(String.format("delete file [%s] : [%s] < [%s]", f.getName(), fileTime, deleteTime));
			f.delete();
			return true;
		}
		return false;
	}
}
