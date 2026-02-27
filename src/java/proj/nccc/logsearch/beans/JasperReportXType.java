/*
 * JasperReportXType.java
 * 
 * Created on 2008年3月4日, 下午 3:07
 * ==============================================================================================
 * $Id: JasperReportXType.java,v 1.2 2017/04/24 02:20:14 leered Exp $
 * ==============================================================================================
 */

package proj.nccc.logsearch.beans;

import java.util.List;
import java.util.Map;

import com.edstw.process.ProcessException;
import com.edstw.report.Report;
import com.edstw.report.jasper.ExcelReportExporter;
import com.edstw.report.jasper.PdfReportExporter;
import com.edstw.user.UserLogger;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * 
 * @author Stephen Lin
 * @versionType $Revision: 1.2 $
 */
public class JasperReportXType {

	private static final String TYPE_XLS = "EXCEL";
	public static final String TYPE_PDF = "PDF";

	public JasperReportXType() {
	}

	public Report getReport(String importJasper, boolean importExtend, List datas, Map param, String reportType,
			String fileName) {
		try {
			if (importExtend)
				importJasper = importJasper.substring(0, importJasper.length() - 7) + reportType + ".jasper";
			else
				importJasper = importJasper.substring(0, importJasper.length() - 7) + ".jasper";
			JasperReport jasperReport = (JasperReport) JRLoader
					.loadObject(JRLoader.getResourceInputStream(importJasper));
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param,
					new JRBeanCollectionDataSource(datas));
			if (reportType.equals(TYPE_XLS)) {
				ExcelReportExporter exporter = new ExcelReportExporter();
				exporter.setExporterParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
				exporter.setExporterParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
				exporter.setExporterParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);

				exporter.setJasperPrint(jasperPrint);
				exporter.setReportFileName(fileName.toString() + ".xls");
				return exporter;
			} else if (reportType.equals(TYPE_PDF)) {
				PdfReportExporter rep = new PdfReportExporter();
				rep.setJasperPrint(jasperPrint);
				rep.setReportFileName(fileName.toString() + ".pdf");
				return rep;
			} else {
				throw new Exception("Report- unknow type : " + importJasper);
			}
		} catch (Exception e) {
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			throw new ProcessException(e.getMessage(), e);
		}

	}

}
