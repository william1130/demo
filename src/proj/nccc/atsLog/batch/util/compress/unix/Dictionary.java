package proj.nccc.atsLog.batch.util.compress.unix;

import java.awt.*;

public class Dictionary extends List {
    String entry[];
    StringBuffer temp;
    int end = -1;
    
    Dictionary() {
        
        entry = new String[256];
        char c;
        
        for (int i = 32; i <= 126; i++) {
            addEntry(i);
        }
    }
 
    void place(int x, int y) {
        this.reshape(x,y,150,300);
    }
    
    void addEntry(String word) {
        end++;
        entry[end] = new String(word);
        this.addItem(end + " : " + word, end);
        this.select(end);               
    }
    
    void addEntry(char letter) {
        end++;
        entry[end] = new String("" + letter);
        this.addItem(end + " : " + letter, end);
        this.select(end);               
    }

    void addEntry(int letter) {
        char c = (char)(letter);
        end++;
        entry[end] = new String("" + c );
        this.addItem(end + " : " + c, end);
        this.select(end);               
    }
        
    
    int findEntry(String word) {
        
    // returns the index number if found, -1 if not
    
        for (int i = 0; i <= end; i++) {
            if ( word.equals(entry[i]) ) {
               // this.select(i);
                return i;
            }
        }
        return -1;
    }
    
    String returnEntry(int code) {
        this.select(code);
        if ( code > end )
            return "";
        else        
            return entry[code];
    }
        
    
}