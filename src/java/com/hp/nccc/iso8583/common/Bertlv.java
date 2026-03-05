package com.hp.nccc.iso8583.common;

import java.util.ArrayList;
import java.util.List;

import com.hp.nccc.iso8583.core.ISOChip;

public class Bertlv {
	
	private static final byte tagAfter = (byte) (Integer.parseInt("1f", 16) & 0xff);
	private static final byte tagSecAfter =(byte) (Integer.parseInt("80", 16) & 0xff);
	private static final byte lengthAfter =(byte) (Integer.parseInt("80", 16) & 0xff);
	private static final byte length2Byte =(byte) (Integer.parseInt("81", 16) & 0xff);
	private static final byte length3Byte =(byte) (Integer.parseInt("82", 16) & 0xff);
	private static final byte length4Byte =(byte) (Integer.parseInt("83", 16) & 0xff);
	private static final byte length5Byte =(byte) (Integer.parseInt("84", 16) & 0xff);
	
	public boolean tryParse(byte[] chipData, List<ISOChip> chips){
		if(chips == null){
			throw new IllegalArgumentException("chips can not be null");
		}
		if(chipData == null || chipData.length == 0){
			throw new IllegalArgumentException("chipData is empty");
		}
		int index = 0;
		while (true) {
			ISOChip chip = new ISOChip();
			int tagLength = 1;
			
			if((chipData[index] & tagAfter) == tagAfter){
				tagLength++;
				int tempIndex = 1;
				while(true){
					if((chipData[index + tempIndex] & tagSecAfter) == tagSecAfter){
						tempIndex++;
						tagLength++;
					}else{
						break;
					}
				}
			}
			
			if(tagLength > chipData.length - index){
				return false;
			}
			
			byte[] tag = new byte[tagLength];
			
			System.arraycopy(chipData, index, tag, 0, tagLength);
			chip.setTag(tag);
			
			index += tagLength;
			
			int tempLength = 1;
			if((chipData[index] & lengthAfter) == lengthAfter){
				index++;
				if(chipData[index] == length2Byte){
					tempLength = 2;
				} else if(chipData[index] == length3Byte){
					tempLength = 3;
				} else if(chipData[index] == length4Byte){
					tempLength = 4;
				} else if(chipData[index] == length5Byte){
					tempLength = 5;
				}
			}
			
			if(tempLength > chipData.length - index){
				return false;
			}
			
			byte[] lengthBytes = new byte[tempLength];
			System.arraycopy(chipData, index, lengthBytes, 0, tempLength);
			int chiplength = CommonFunction.getHexLength(lengthBytes);
			chip.setLength(chiplength);
			index += tempLength;

			byte[] data = null;
			if(chip.getLength() != 0){
				if(chip.getLength() > chipData.length - index){
					return false;
				}
				data = new byte[chip.getLength()];
				System.arraycopy(chipData, index, data, 0, chip.getLength());
			}else{
				return false;
			}
			chip.setData(data);
			index += chip.getLength();
			chips.add(chip);
			if (index == chipData.length) {
				break;
			}else if(index > chipData.length){
				return false;
			}
		}
		return true;
	}
	
	public boolean formatCheck(byte[] chipData){
		List<ISOChip> chips = new ArrayList<ISOChip>();
		int index = 0;
		while (true) {
			ISOChip chip = new ISOChip();
			int tagLength = 1;
			
			if((chipData[index] & tagAfter) == tagAfter){
				tagLength++;
				int tempIndex = 1;
				while(true){
					if((chipData[index + tempIndex] & tagSecAfter) == tagSecAfter){
						tempIndex++;
						tagLength++;
					}else{
						break;
					}
				}
			}
			
			if(tagLength > chipData.length - index){
				return false;
			}
			
			byte[] tag = new byte[tagLength];
			
			System.arraycopy(chipData, index, tag, 0, tagLength);
			chip.setTag(tag);
			
			index += tagLength;
			
			int tempLength = 1;
			if((chipData[index] & lengthAfter) == lengthAfter){
				index++;
				if(chipData[index] == length2Byte){
					tempLength = 2;
				} else if(chipData[index] == length3Byte){
					tempLength = 3;
				} else if(chipData[index] == length4Byte){
					tempLength = 4;
				} else if(chipData[index] == length5Byte){
					tempLength = 5;
				}
			}
			
			if(tempLength > chipData.length - index){
				return false;
			}
			
			byte[] lengthBytes = new byte[tempLength];
			System.arraycopy(chipData, index, lengthBytes, 0, tempLength);
			int chiplength = CommonFunction.getHexLength(lengthBytes);
			chip.setLength(chiplength);
			index += tempLength;

			byte[] data = null;
			if(chip.getLength() != 0){
				if(chip.getLength() > chipData.length - index){
					return false;
				}
				data = new byte[chip.getLength()];
				System.arraycopy(chipData, index, data, 0, chip.getLength());
			}else{
				return false;
			}
			chip.setData(data);
			index += chip.getLength();
			chips.add(chip);
			if (index == chipData.length) {
				break;
			}else if(index > chipData.length){
				return false;
			}
		}
		return true;
	}
	
	public List<ISOChip> parseChip(byte[] chipData){
		List<ISOChip> chips = new ArrayList<ISOChip>();
		int index = 0;
		while (true) {
			ISOChip chip = new ISOChip();
			int tagLength = 1;
			
			if((chipData[index] & tagAfter) == tagAfter){
				tagLength++;
				int tempIndex = 1;
				while(true){
					if((chipData[index + tempIndex] & tagSecAfter) == tagSecAfter){
						tempIndex++;
						tagLength++;
					}else{
						break;
					}
				}
			}
			
			byte[] tag = new byte[tagLength];
			System.arraycopy(chipData, index, tag, 0, tagLength);
			chip.setTag(tag);
			
			index += tagLength;
			
			int tempLength = 1;
			if((chipData[index] & lengthAfter) == lengthAfter){
				index++;
				if(chipData[index] == length2Byte){
					tempLength = 2;
				} else if(chipData[index] == length3Byte){
					tempLength = 3;
				} else if(chipData[index] == length4Byte){
					tempLength = 4;
				} else if(chipData[index] == length5Byte){
					tempLength = 5;
				}
			}
			
			byte[] lengthBytes = new byte[tempLength];
			System.arraycopy(chipData, index, lengthBytes, 0, tempLength);
			int chiplength = CommonFunction.getHexLength(lengthBytes);
			chip.setLength(chiplength);
			index += tempLength;

			byte[] data = null;
			if(chip.getLength() != 0){
				data = new byte[chip.getLength()];
				System.arraycopy(chipData, index, data, 0, chip.getLength());
			}
			chip.setData(data);
			index += chip.getLength();
			chips.add(chip);
			if (index == chipData.length) {
				break;
			}
		}
		return chips;
	}
	
	public static void main(String args[]){
		String test = "5F200C5320484749204D50204355505F24031912319C01009F02060000000022009F03060000000000009F390102DFAE027812D3E83C5CE496BD8D2DA8FAE9186B59B59669BF9B2709128C57ADB00583EFE5EE0D90EC470A31F438366D0F21C75443B5933BEF6C5D59B2B3454696A78217684449D45414464E8C0A489FDF5E76C13B5497440F342A6FA0DADF6DD85F0F984AD33F1E463583F95F97239A86EE62D3719CEFB9A48927BAA6DFAE030AFF991B00000000E00022DFAE5A08493817FFFFFF1213";
		byte[] data = CommonFunction.hexDecode(test);
		Bertlv be = new Bertlv();
		be.parseChip(data);
	}
}
