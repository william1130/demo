package proj.nccc.logsearch.parse.chip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import proj.nccc.logsearch.persist.ChipcardInfo;
import proj.nccc.logsearch.vo.ChipcardInfoValue;

/**
 * Parse 9F10 EMV Chip Data
 * @author Red Lee
 * @version 1.0
 */
public class ChipParseMulti extends ChipParse {

	//	存放 Header 值
	protected List<ChipcardInfo> chipcardHeaderList;
	
	/**
	 * 
	 */
	public ChipParseMulti() {
		
		chipcardHeaderList = new ArrayList<ChipcardInfo>();
	}


	/* (non-Javadoc)
	 * @see proj.nccc.logsearch.parse.chip.ChipParse#addChipcardInfo(proj.nccc.logsearch.persist.ChipcardInfo)
	 */
	public void addChipcardInfo(ChipcardInfo info) {
		
		if ("H".equals(info.getDataType()))
			chipcardHeaderList.add(info);
		else
			chipcardInfoList.add(info);
	}
	


	/* (non-Javadoc)
	 * @see proj.nccc.logsearch.parse.chip.ChipParse#init()
	 */
	public void init() {
		
		//	初始化=排序
		Comparator<ChipcardInfo> comparator =  new Comparator<ChipcardInfo>() {
			public int compare(ChipcardInfo c1, ChipcardInfo c2) {
				
				return c1.getByteIndex() < c2.getByteIndex() ? -1 :
					c1.getByteIndex() > c2.getByteIndex() ? 1 :
						c1.getBitIndex() > c2.getBitIndex() ? -1 :
							c1.getBitIndex() < c2.getBitIndex() ? 1 :
								0;
			}
		};
		
		Collections.sort(chipcardHeaderList, comparator);
		Collections.sort(chipcardInfoList, comparator);
	}


	/* (non-Javadoc)
	 * @see proj.nccc.logsearch.parse.chip.ChipParse#parse(java.lang.String)
	 */
	public List<ChipcardInfoValue> parse(String src) {
		
		String dataSrc = src;
		
		//	確保字元數為雙位數 1byte=2字元
		if ((dataSrc.length() % 2) != 0)
			dataSrc = dataSrc + "0";
		
		//	轉換為 byte array 處理
		byte[] data = new byte[dataSrc.length() / 2];
		
		for (int i = 0; i < data.length; i++) {
			
			int from = i * 2;
			String byteStr = dataSrc.substring(from, from + 2);
			
			try {
				
				data[i] = (byte) Integer.parseInt(byteStr, 16);
			}
			catch (Exception e) { }
		}
		
		List<ChipcardInfoValue> resultList = new ArrayList<ChipcardInfoValue>();

		for (ChipcardInfo info : chipcardHeaderList) {
			
			ChipcardInfoValue val = new ChipcardInfoValue();
			resultList.add(val);
			
			val.setDesc(info.getMeaning());
			String displayType = info.getDisplayType();
			
			if ("B".equals(displayType) ||
					"H".equals(displayType) ||
					"D".equals(displayType)) {

				byte[] values = getValue(info.getByteIndex() - 1,
						info.getBitIndex(), info.getBitLength(), data);
				String value = display(values, displayType, info.getBitLength());
				val.setValue(value);
				val.setType("V");
			}
			else {

				int fromIndex = (info.getByteIndex() - 1) * 2;
				
				if (fromIndex < 0)
					fromIndex = 0;
				else if (fromIndex >= src.length())
					fromIndex = src.length() - 1;
				
				int toIndex = fromIndex + (info.getBitLength() / 4);
				
				if (toIndex < fromIndex)
					toIndex = fromIndex;
				else if (toIndex > src.length())
					toIndex = src.length();
				
				String value = src.substring(fromIndex, toIndex);
				
				//	Card Verification Result 欄位, 再進行一次 parse
				if ("M".equals(displayType)) {
					
					List<ChipcardInfoValue> listValue = super.parse(value);
					val.setValue(listValue);
					val.setType("T");
				}
				else {
					
					val.setValue(value);
					val.setType("V");
				}
			}
		}
		
		return resultList;
	}
}
