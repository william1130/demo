package proj.nccc.atsLog.batch.util.compress.unix;

import java.awt.*;

public class Statistics extends Panel {
    
    Statistics (int raw, int comp) {
        
        rawbits = raw;
        compbits = comp;
    }        
    
    void addRaw(int num) {
        rawsent += num;
        repaint();
    }
    
    void addComp(int num ) {
        compsent += num;
        repaint();
    }
       
    
    public void paint(Graphics g) {
        
        Dimension size = size();
        int compression;
        
        g.setColor(Color.black);
        g.fillRect(0, 0, size.width, size.height);
        
        g.setColor(Color.white);
        font = new Font("Dialog", Font.BOLD, 12);
        g.setFont(font);
        
        g.drawString("Raw   Comp ", 160, 15);
        g.drawString("No. of bits in each code", 0, 40);
        g.drawString("No. of codes sent", 0, 55);
        g.drawString("Total bits", 0, 70);
        g.drawString("Compression:", 0, 90);
        
        font = new Font("Coutier", Font.PLAIN, 15);
        g.setFont(font);
        
        g.drawString("" + rawbits, 170, 40);
        g.drawString("" + compbits, 210, 40);

        g.drawString("" + rawsent, 170, 55);
        g.drawString("" + compsent, 210, 55);
        
        g.drawString("" + rawbits*rawsent, 170, 70);
        g.drawString("" + compbits*compsent, 210, 70);
        
        if (rawsent != 0)   
            compression = (int)((1 - ((float)(compsent*compbits))/((float)(rawsent*rawbits))) * 100);
        else
            compression = 0;
        
        g.drawString("" +  compression + "%", 190, 90);
            
        font = new Font("Helvetica", Font.BOLD, 15);
        g.setFont(font);
        g.drawString(" Statistics", 0, 12);
        
    }
    
    
    int rawbits, compbits;
    int rawsent = 0;
    int compsent = 0;
    
    Label line[];
    int lines = 5;
    Font font = new Font("Dialog", Font.PLAIN, 12);
    
}