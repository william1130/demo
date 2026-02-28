package proj.nccc.atsLog.batch.util.compress.unix;

/*
 *   <title> LZW Compression Demonstration </title>
 *
 *	<applet code="Compress" width=600 height=500>
 *   </applet>
 */

import java.awt.*;
import java.applet.*;

public class Compress extends Applet {

	public void init() {
		super.init();
		coder = new LZW();

		tracker = new MediaTracker(this);

        int fontSize;
        d = size();
		gc = this.getGraphics();


		setBackground(Color.black);


        art=getImage(getCodeBase(), "lzw.gif");     // Load image
        tracker.addImage(art, 0);                   // Add to tracker
        try {
            tracker.waitForAll();                   // Wait for all images to load
        } catch (InterruptedException e) {
            showStatus("The loading process was interrupted.");
        }

	    try {
	        fontSize = Integer.parseInt(getParameter("fontsize"));
	    } catch (NumberFormatException e) {
	        fontSize = 10;
	    }

	    defaultFont = new Font("Helvetica", Font.PLAIN, fontSize);
	    this.setFont(defaultFont);

        _metrics = this.getFontMetrics(defaultFont);
        _height = _metrics.getHeight();
	    _ascent = _metrics.getAscent();
	    _descent = _metrics.getDescent();


		setLayout(null);
		addNotify();
		resize(600,500);
		setBackground(Color.white);

		gc.drawImage(art, 0, 0, this);              // display the image

		inputField = new InputField();
		inputField.reshape(12,100,180,(_height + _descent));
		add(inputField);

		outputField = new OutputField();
		outputField.reshape(400,100,180,(_height + _descent));
		add(outputField);

		sender = new Send();
		add(sender);
		sender.reshape(12,450,570,(_height + _descent));

		add(coder.inputDict);
		coder.inputDict.place(20, 140);

		add(coder.outputDict);
		coder.outputDict.place(425, 140);

		stats = new Statistics(7,8);
		stats.setFont(defaultFont);
		add(stats);
		stats.reshape(185, 300, 235, 110);
    }

    public void update(Graphics g) {
        g.drawImage(art, 0, 0, this);
    }


    public void paint(Graphics g) {
        g.drawImage(art, 0, 0, this);
    }

    public void start() {
    }

	public boolean handleEvent(Event event) {
		return super.handleEvent(event);
	}

	public boolean keyDown(Event e, int key) {

	    int code;
	    String hold;

	    try {
	        code = coder.Encode( inputField.whatKey(key) );
	        sender.wire( "" + code );
	        stats.addComp(1);

	        hold = new String( coder.Decode(code) );
	        outputField.addString( hold );
	        stats.addRaw( hold.length() );


	    } catch (NotValidChar err) {
	    } catch (DoNotSend err) {
	    }

        return true;
	}


	InputField inputField;
	OutputField outputField;
	public LZW coder;
	Send sender;
	Statistics stats;

    MediaTracker tracker;
    Graphics gc;
    Dimension d;
    Image art;
    Font defaultFont;
    FontMetrics _metrics;
    int _height;
    int _ascent;
    int _descent;


	//{{DECLARE_CONTROLS
	//}}
}
