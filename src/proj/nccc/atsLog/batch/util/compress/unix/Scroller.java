package proj.nccc.atsLog.batch.util.compress.unix;

/*
	stripped down version of
	ScrollingText by Matt Howittm <howitt@fas.harvard.edu>

	#include <std_disclaimer.h>
*/

import java.awt.*;

public class Scroller extends Canvas {

	// Variables that control the location of text
	int x, y;
	int xstart, ystart;
	int xstop, ystop;

	// Other control variables
	Font font;
	FontMetrics metrics;
	Graphics gc;
	Image offscreen;
	Dimension offDim;
	Dimension apprect;

	// Command line argument variables
	Color bgColor;
	Color fgColor;
	String text = "101110011";
	int xdelta = 5;
	int ydelta = 5;

    public void send(String str) {

        text = str;
        Thread t = Thread.currentThread();
        x = 0;
        while (x <= xstop) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }
            paint(this.getGraphics());
        }
        t = null;
    }

   	public void update(Graphics g) {
        	paint(g);
   	}

	public void paint(Graphics g) {
	    Dimension size = size();

	    if ((offscreen == null) || (size.width != offDim.width) || (size.height != offDim.height) ) {
    		offDim = size;
    		offscreen = createImage(size.width, size.height);
	    	gc = offscreen.getGraphics();
    		font = this.getFont();
    		gc.setFont(font);
    		metrics = gc.getFontMetrics(font);

    		int fheight = metrics.getMaxAscent() - metrics.getMaxDescent() + 2;
		    int fwidth = metrics.stringWidth(text);


		    y = (size.height - fheight) / 2 + fheight;

		 // Left to Right
			xstart = x = -fwidth;
			xstop = size.width;
			int dist = Math.abs(xstop - xstart);
			if(dist % xdelta != 0)
				xstop += xdelta - (dist % xdelta);
			ystart = y;
			ystop = ydelta = 0;

		}


		resize(size.width, size.height);

        gc.setColor(Color.black);
        gc.fillRect(0, 0, size.width, size.height);

		gc.setColor(Color.white);
        gc.drawString(text, x, y);
		g.drawImage(offscreen, 0, 0, this);

		if(y != ystop) {
			y += ydelta;
		}
		else {
			y = ystart;
		}
       		if(x <= xstop) {
			x += xdelta;
		}
		else {
			x = xstart;
		}
        }
}
