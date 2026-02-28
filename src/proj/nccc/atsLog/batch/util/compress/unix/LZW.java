package proj.nccc.atsLog.batch.util.compress.unix;


public class LZW {
    
    LZW() {
		
		inputDict = new Dictionary();
		outputDict = new Dictionary();
		
		w = new StringBuffer("");
		v = new StringBuffer("");
	}
       
    
	public int Encode(char k) throws DoNotSend {
	    int temp;
	    if (inputDict.findEntry("" + w + k) != -1) {
	        w = w.append(k);
	        throw new DoNotSend();
	    } else {
	        inputDict.addEntry("" + w + k);
	        temp = inputDict.findEntry("" + w);
	        w = new StringBuffer("" + k);
	        return temp;
	    }
	}
	
	public String Decode(int k) {
	    
	    String entry;
	    
 	    if (v.length() == 0) {
	        v = new StringBuffer( outputDict.returnEntry(k) );
	        return (v.toString());
	    } else {
	        entry = new String( outputDict.returnEntry(k) );

	        if (entry.length() == 0) {          // remove this conditional to cause LZW bug
	            entry = new String( v.toString() + v.charAt(0) );
	        }
	        
	        outputDict.addEntry("" + v + entry.charAt(0) );
	        
	        v = new StringBuffer(entry);
	        return entry;
	    }
	}
	        

	Dictionary inputDict;
	Dictionary outputDict;
	StringBuffer w;
	StringBuffer v;

}

class DoNotSend extends Exception {
}