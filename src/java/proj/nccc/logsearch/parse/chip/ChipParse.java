package proj.nccc.logsearch.parse.chip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import proj.nccc.logsearch.persist.ChipcardInfo;
import proj.nccc.logsearch.vo.ChipcardInfoValue;

/**
 * Parse 一般的 EMV Chip Data
 * @author Red Lee
 * @version 1.0
 */
public class ChipParse {
	
	//	Bit mask from left
	protected static final int[] LEFT_LENGTH_MASK = new int[] {
		0x00, 0x01, 0x03, 0x07, 0x0F, 0x1F, 0x3F, 0x7F, 0xFF
	};
	
	//	Bit mask from right
	protected static final int[] RIGHT_LENGTH_MASK = new int[] {
		0x00, 0x80, 0xC0, 0xE0, 0xF0, 0xF8, 0xFC, 0xFE, 0xFF
	};

	//	chipcard_info data
	protected List<ChipcardInfo> chipcardInfoList;
	
	/**
	 * 
	 */
	public ChipParse() {
		
		chipcardInfoList = new ArrayList<ChipcardInfo>();
	}

	/**
	 * @param info
	 */
	public void addChipcardInfo(ChipcardInfo info) {
		
		chipcardInfoList.add(info);
	}
	
	/**
	 * 初始化=排序
	 */
	public void init() {
		
		Comparator<ChipcardInfo> comparator =  new Comparator<ChipcardInfo>() {
			public int compare(ChipcardInfo c1, ChipcardInfo c2) {
				
				return c1.getByteIndex() < c2.getByteIndex() ? -1 :
					c1.getByteIndex() > c2.getByteIndex() ? 1 :
						c1.getBitIndex() > c2.getBitIndex() ? -1 :
							c1.getBitIndex() < c2.getBitIndex() ? 1 :
								0;
			}
		};
		
		Collections.sort(chipcardInfoList, comparator);
	}
	
	/**
	 * Parse Data
	 * @param data toHex後的字串
	 * @return
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

		for (ChipcardInfo info:chipcardInfoList) {
			
			ChipcardInfoValue val = new ChipcardInfoValue();
			resultList.add(val);
			
			val.setType("V");
			val.setDesc(info.getMeaning());
			val.setByteIndex(info.getByteIndex());
			val.setBitIndex(info.getBitIndex());
			val.setBitLength(info.getBitLength());
			String displayType = info.getDisplayType();
			
			//	B=Binary顯示, H=Hex顯示, D=Deciaml顯示
			if ("B".equals(displayType) ||
					"H".equals(displayType) ||
					"D".equals(displayType)) {

				byte[] values = getValue(info.getByteIndex() - 1,
						info.getBitIndex(), info.getBitLength(), data);
				String value = display(values, displayType, info.getBitLength());
				val.setValue(value);
			}
			//	直接原字元顯示
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
				val.setValue(value);
			}
		}

		return resultList;
	}
	
	
	//	------------------------------------------------
	
	/**
	 * 轉換顯示
	 * @param src
	 * @param displayType
	 * @param binLen
	 * @return
	 */
	protected String display(byte[] src, String displayType, int binLen) {
		
		StringBuffer result = new StringBuffer();
		
		//	轉換整數值, 最多4byte
		if ("D".equals(displayType)) {
			
			long val = 0;
			
			for (int i = 0; i < 4 && i < src.length; i++) {
				
				val <<= 8;
				val |= src[i];
			}
			
			result.append(val);
		}
		//	2進位顯示, 依bit length前面補0
		else if ("B".equals(displayType)) {
	
			int val = ((int) src[0]) & 0xFF;
			result.append(Integer.toBinaryString(val));
			
			while (result.length() < binLen) {
				result.insert(0, "0");
			}
		}
		//	16進位顯示, 如果bit數大於4則1byte用2字元顯示
		else if ("H".equals(displayType)) {
			
			for (int i = 0; i < src.length; i++) {
				
				int val = ((int) src[i]) & 0xFF;
				String hex = Integer.toHexString(val).toUpperCase();
				
				if (binLen > 4) {
					
					result.append(hex.length() <= 1 ? "0" : "");
				}
				
				result.append(hex);
			}
		}
		//	將byte轉成字元顯示
		else if ("C".equals(displayType)) {
				
			try {
				
				result.append(new String(src));
			}
			catch (Exception e) {
				
				e.printStackTrace();
			}
		}
		
		return result.toString();
	}
	
	/**
	 * 取得bit
	 * @param byteIndex byte 位置 1 開始
	 * @param bitIndex bit 位置 8-1
	 * @param bitLength bit 長度
	 * @param data byte array
	 * @return
	 */
	protected byte[] getValue(int byteIndex, int bitIndex, int bitLength, byte[] data) {
		
		if (byteIndex < 0)
			byteIndex = 0;
		
		if (bitIndex > 8)
			bitIndex = 8;
		else if (bitIndex < 1)
			bitIndex = 1;
		
		if (bitLength <= 0)
			bitLength = 1;
		else if (bitLength > 256)
			bitLength = 256;
		
		byte[] result = null;
		
		//	計算取得的 byte 數, 無條件進位
		if (bitLength <= 8) {
			result = new byte[1];
		}
		else {
			
			if ((bitLength % 8) == 0)
				result = new byte[bitLength / 8];
			else
				result = new byte[bitLength / 8 + 1];
		}
		
		/*
		 * 計算取得的最後一位bit位置, 計算應右位移幾位元
		 * EX: from bit 4, 取 9 位, 則最後一位在 4, 應往右移 3 位
		 * 當減去後的數值小於0, 表示在同一 byte 內可取完
		 */
		int shiftIndex = bitIndex - bitLength;
		
		//	同一 byte 內
		if (shiftIndex >= 0) {
			
			result[0] = byteIndex >= data.length ? 0x00 : (byte)
					((data[byteIndex] >> shiftIndex) & LEFT_LENGTH_MASK[bitLength]);
			
			return result;
		}
		
		//	bit 數剛好為 8 的倍數, 且由bit位置8開始取值, 則直接用 byte 計算
		if (bitIndex == 8 && ((bitLength % 8) == 0)) {
			
			for (int i = 0; i < result.length && data.length > (byteIndex + i); i++) {
				
				result[i] = data[byteIndex + i];
			}
			
			return result;
		}
		
		//	暫存第1位 byte
		byte tempByte =  byteIndex >= data.length ? 0x00 : data[byteIndex];
		
		//	取得2-N位byte, 存於的長度=取得的長度+1
		for (int i = 0; i < result.length && data.length > (byteIndex + i + 1); i++) {
			
			result[i] = data[byteIndex + i + 1];
		}
		
		shiftIndex = 8 - ((bitLength - bitIndex) % 8);
		
		//	右移計算
		for (int i = result.length - 1; i >= 1; i--) {
			
			result[i] = (byte) (
					((result[i] >> shiftIndex) & LEFT_LENGTH_MASK[8 - shiftIndex]) |
					((result[i - 1] << (8 - shiftIndex)) & RIGHT_LENGTH_MASK[shiftIndex])
				);
		}
		
		//	計算最前 byte 右移
		result[0] = (byte) (
					((result[0] >> shiftIndex) & LEFT_LENGTH_MASK[8 - shiftIndex]) |
					((tempByte << (8 - shiftIndex)) & RIGHT_LENGTH_MASK[shiftIndex])
				);
		
		return result;
	}
}
