package proj.nccc.atsLog.batch.util.compress.unix;

import java.awt.*;

public class InputField extends TextField {

	public char whatKey(int key) throws NotValidChar {

	    int last;
	    char smallest = ' ';
	    char largest  = '~';

	    if ((key >= smallest) && (key <= largest))  {
	        this.setText(this.getText() +  int2string(key) );
	        last = this.getText().length();
	        this.select(last, last);
	        return (char)key;
	    } else {
	        throw new NotValidChar();
	    }
	}

	static public String int2string(int i) {

        StringBuffer s = new StringBuffer(1);
        s.append( (char)(i) );
        return s.toString();
    }

}

class NotValidChar extends Exception {
    NotValidChar() {
    }

}

