package proj.nccc.logsearch.report;

import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

import com.edstw.report.poi.AbstractExcel;

import proj.nccc.logsearch.beans.MyDateUtil;

/**
 *
 * @author 許欽程(Shiu Vincent, sz12tk)
 * @version $Revision: 1.2 $
 */
@SuppressWarnings({ "rawtypes" })
public abstract class BaseExcelReport extends AbstractExcel {
	/** 日期欄位的寬度 */
	protected static final int DATE_WIDTH = 10;

	protected HSSFCellStyle numStyle;
	protected HSSFCellStyle doubleStyle;
	protected HSSFCellStyle titleStyle;
	protected HSSFCellStyle headStyle;
	protected HSSFCellStyle normalStyle;
	protected HSSFCellStyle centerStyle;
	protected HSSFCellStyle rightStyle;

	/**
	 * 建立本物件常用之HSSFCellStyle, 初始化本類別中宣告之各個HSSFCellStyle, 若預先宣告之HSSFCellStyle不夠使用,
	 * 使用者可自行覆寫此method, 建立所需之HSSFCellStyle, 但覆寫時必須呼叫此method,
	 * 以確保預設之HSSFCellStyle均會被建立.
	 */
	protected void createStyle() {

		HSSFFont titleFont = book.createFont();
		titleFont.setBold(true);
		titleFont.setFontName("標楷體");
		titleFont.setFontHeightInPoints((short) 20);
		titleStyle = book.createCellStyle();
		titleStyle.setFont(titleFont);
		titleStyle.setAlignment(HorizontalAlignment.CENTER);
		HSSFFont headfont = book.createFont();
		headfont.setBold(true);
		headStyle = book.createCellStyle();
		headStyle.setFont(headfont);
		headStyle.setAlignment(HorizontalAlignment.CENTER);
		headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		headStyle.setWrapText(false);
		numStyle = book.createCellStyle();
		numStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.###"));
		numStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		doubleStyle = book.createCellStyle();
		doubleStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.###"));
		normalStyle = book.createCellStyle();
		normalStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
		normalStyle.setAlignment(HorizontalAlignment.LEFT);
		normalStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		centerStyle = book.createCellStyle();
		centerStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
		centerStyle.setAlignment(HorizontalAlignment.CENTER);
		centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		rightStyle = book.createCellStyle();
		rightStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
		rightStyle.setAlignment(HorizontalAlignment.RIGHT);
		rightStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	}

	protected void setRegionBorder(BorderStyle borderStyle, CellRangeAddress r, HSSFSheet sheet) throws Exception {
		RegionUtil.setBorderBottom(borderStyle, r, sheet);
		RegionUtil.setBorderLeft(borderStyle, r, sheet);
		RegionUtil.setBorderRight(borderStyle, r, sheet);
		RegionUtil.setBorderTop(borderStyle, r, sheet);
	}

	protected void setStyleBorder(HSSFCellStyle style, BorderStyle borderStyle) {
		style.setBorderBottom(borderStyle);
		style.setBorderTop(borderStyle);
		style.setBorderLeft(borderStyle);
		style.setBorderRight(borderStyle);
	}

	protected HSSFCell createCell(HSSFRow row, int index, HSSFCellStyle style, CellType type) {
		HSSFCell cell = row.createCell((short) index);
		if (style != null)
			cell.setCellStyle(style);
		cell.setCellType(type);
		return cell;
	}

	protected void createTitleHeader(HSSFSheet sheet, String title, int cols) throws Exception {
		createTitleHeader(sheet, title, cols, 0);
	}

	protected void createTitleHeader(HSSFSheet sheet, String title, int cols, int rowIndex) throws Exception {
		HSSFRow titleRow = sheet.createRow(rowIndex);
		HSSFCell titleCell = this.createCell(titleRow, 0, titleStyle, CellType.STRING);
		titleCell.setCellValue(new HSSFRichTextString(title));
		this.mergeCell(sheet, BorderStyle.NONE, rowIndex, (short) 0, rowIndex, (short) cols);

		rowIndex++;
		HSSFRow info = sheet.createRow(rowIndex);
		HSSFCell dateCell = this.createCell(info, 0, rightStyle, CellType.STRING);
		dateCell.getCellStyle().setAlignment(HorizontalAlignment.RIGHT);
		String dateFrom = MyDateUtil.getSysDateTime(MyDateUtil.FULL_DATETIME_2);
		dateCell.setCellValue(new HSSFRichTextString("製表日：" + dateFrom));
		this.mergeCell(sheet, BorderStyle.NONE, rowIndex, (short) 0, rowIndex, (short) cols);
	}

	protected void mergeCell(HSSFSheet sheet, BorderStyle style, int startRow, short startCol, int endRow, short endCol)
			throws Exception {
		CellRangeAddress r = new CellRangeAddress(startRow, endRow, (short) startCol, (short) endCol);
		this.setRegionBorder(style, r, sheet);
		sheet.addMergedRegion(r);
	}

	protected void createHeader(HSSFSheet sheet, String title, List headerInfos) throws Exception {
		createHeader(sheet, title, headerInfos, 0);
	}

	protected void createHeader(HSSFSheet sheet, String title, List headerInfos, int rowIndex) throws Exception {
		this.createTitleHeader(sheet, title, headerInfos.size() - 1, rowIndex);
		this.createHeader(sheet, headerInfos, rowIndex + 2);
	}

	protected void createHeader(HSSFSheet sheet, String title, List[] headerInfosArray) throws Exception {
		this.createHeader(sheet, title, headerInfosArray, 0);
	}

	protected void createHeader(HSSFSheet sheet, String title, List[] headerInfosArray, int rowIndex) throws Exception {
		int cellCount = 0;
		for (Iterator iter = headerInfosArray[0].iterator(); iter.hasNext();) {
			HeaderInfo hi = (HeaderInfo) iter.next();

			cellCount += hi.colspan;
		}

		this.createTitleHeader(sheet, title, cellCount - 1, rowIndex);
		this.createHeader(sheet, headerInfosArray, rowIndex + 2);
	}

	protected void createHeader(HSSFSheet sheet, List[] headerInfosArray, int rowIndex) {
		for (int i = 0; i < headerInfosArray.length; i++) {
			createHeader(sheet, headerInfosArray[i], rowIndex + i);
		}
	}

	protected void createHeader(HSSFSheet sheet, List headerInfos, int rowIndex) {
		HSSFRow header = sheet.createRow(rowIndex);
		int i = 0;
		int colIndex = i;
		for (Iterator iter = headerInfos.iterator(); iter.hasNext(); i++) {
			HeaderInfo hi = (HeaderInfo) iter.next();

			HSSFCellStyle style = hi.style != null ? hi.style : headStyle;

			HSSFCell cell;
			if (hi.colspan == 1) {
				sheet.setColumnWidth((short) i, (short) (hi.width * WIDTH_UNIT));
				cell = createCell(header, colIndex, style, CellType.STRING);
				colIndex++;
			} else {
				int c = colIndex;
				for (int j = 1; j < hi.colspan; j++, colIndex++) {
					createCell(header, colIndex, style, CellType.STRING);
				}

				try {
					CellRangeAddress r = new CellRangeAddress(rowIndex, rowIndex, (short) c, (short) colIndex);
					RegionUtil.setBorderBottom(style.getBorderBottom(), r, sheet);
					RegionUtil.setBorderLeft(style.getBorderLeft(), r, sheet);
					RegionUtil.setBorderRight(style.getBorderRight(), r, sheet);
					RegionUtil.setBorderTop(style.getBorderTop(), r, sheet);

					sheet.addMergedRegion(r);
				} catch (Exception e) {
					throw new java.lang.IllegalStateException(e);
				}

				cell = header.getCell((short) c);

				colIndex++;
			}

			cell.setCellValue(new HSSFRichTextString(hi.name));
		}
	}

	protected HSSFRow createRow(HSSFSheet sheet, int rowIndex, float height) {
		HSSFRow row = sheet.createRow(rowIndex);
		row.setHeightInPoints(height);
		return row;
	}

	protected HSSFCell addStringCell(HSSFRow row, int index, HSSFCellStyle style, String value) {
		HSSFCell cell = this.createCell(row, index, style, CellType.STRING);
		cell.setCellValue(new HSSFRichTextString(value));
		return cell;
	}

	protected HSSFCell addNumberCell(HSSFRow row, int index, HSSFCellStyle style, int value) {
		HSSFCell cell = this.createCell(row, index, style, CellType.NUMERIC);
		cell.setCellValue(value);
		return cell;
	}

	protected HSSFCell addNumberCell(HSSFRow row, int index, HSSFCellStyle style, double value) {
		HSSFCell cell = this.createCell(row, index, style, CellType.NUMERIC);
		cell.setCellValue(value);
		return cell;
	}

	protected class HeaderInfo {
		public String name;
		public int width;
		public HSSFCellStyle style;
		public int colspan = 1;

		public HeaderInfo(String name, int width, HSSFCellStyle style, int colspan) {
			if (colspan < 1)
				throw new IllegalArgumentException("colspan must be greater then 0.");
			this.name = name;
			this.width = width;
			this.style = style;
			this.colspan = colspan;
		}

		public HeaderInfo(String name, int width) {
			this(name, width, null, 1);
			this.name = name;
			this.width = width;
		}
	}
}
