package proj.nccc.atsLog.batch.util.compress.unix;

import java.awt.*;

class Send extends Panel {

    Send() {

        Font defaultFont = this.getFont();


        in = new TextField();
        in.setFont(defaultFont);
        in.setEditable(false);
        add(in);

        out = new TextField();
        out.setFont(defaultFont);
        out.setEditable(false);
        add(out);

        thru = new Scroller();
        add(thru);


    }

    public void wire(String str) {

        int last;

        in.setText(in.getText() + str + " ");
	    last = in.getText().length();
	    in.select(last, last);

	    thru.send(str);


        out.setText(out.getText() + str + " ");
	    last = out.getText().length();
	    out.select(last, last);


    }


    public void paint(Graphics g) {
        Dimension size = size();
        int fieldwidth = size.width/4;

        in.reshape(0, 0, fieldwidth , size.height);
        out.reshape( fieldwidth*3 , 0, fieldwidth , size.height);

        thru.reshape(fieldwidth, 0, fieldwidth * 2, size.height);
    }

    TextField in;
    TextField out;
    Scroller thru;
}
