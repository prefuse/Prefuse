package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.font.GlyphVector;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * RenderingBenchmarks
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class RenderingBenchmarks extends JComponent {

    private static final Logger s_logger 
        = Logger.getLogger(RenderingBenchmarks.class.getName());
    
    private StringBuffer sbuf = new StringBuffer();
    private String testSuite;
    private String curTest;
    private int    numItems;
    private long   timein;
    private int    fps = 20;
    
    public RenderingBenchmarks() {
        this.setPreferredSize(new Dimension(500,500));
    }
    
    private void startTest(String name, int numItems) {
        if ( curTest != null )
            throw new IllegalStateException("In the middle of a test!");
        this.curTest = name;
        this.numItems = numItems;
        Toolkit tk = Toolkit.getDefaultToolkit();
        tk.sync();
        this.timein = System.currentTimeMillis();
    }
    
    private void endTest(boolean print) {
        if ( print ) {
            long t = System.currentTimeMillis() - timein;
            double pps = 1000*((double)numItems)/t;
            double ppf = pps/fps;
            sbuf.append(curTest).append(" ")
                .append(curTest.length() > 14 ? "\t" : "\t\t")
                .append(numItems).append(" \t")
                .append(t/1000.0).append("s\t")
                .append(((int)(pps*100))/100.0).append(" pr/s\t")
                .append(((int)(ppf*100))/100.0).append(" pr/fr")
                .append('\n');
        }
        curTest = null;
    }
    
    public void printHeader() {
        sbuf.append("PRIMITIVE\t\tCOUNT\tTIME\tPRIMITIVES/SEC\tPRIMITIVES/FRAME @ ");
        sbuf.append(fps).append("fps").append('\n');
    }
    
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        
        int x = 0, y = 0, w = 100, h = 100, c=10;
        float xf = 0f, yf = 0f, wf = 100f, hf = 100f, cf=10f;
        int n;
        
        g2.setColor(Color.BLACK);
        boolean print = false;
        
        for ( int j=0; j<3; ++j, print = true ) {
        
            if ( j == 1 ) {
                print = true;
                testSuite = "NORMAL";
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            } else if ( j == 2 ) {
                testSuite = "ANTI-ALIASING";
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }
            
            if ( print ) {
                printHeader();
            }
            
            // lines-direct
            n = 10000;
            startTest("lines-direct", n);
            for ( int i=0; i<n; ++i ) {
                g2.drawLine(x,y,w,h);
            }
            endTest(print);
            
            // lines-shape
            n = 10000;
            Line2D line = new Line2D.Float(xf,yf,wf,hf);
            startTest("lines-shape", n);
            for ( int i=0; i<n; ++i ) {
                g2.draw(line);
            }
            line = null;
            endTest(print);
            
            // rect-direct-draw
            n = 10000;
            startTest("rect-direct-draw", n);
            for ( int i=0; i<n; ++i ) {
                g2.drawRect(x,y,w,h);
            }
            endTest(print);
            
            // rect-shape-draw
            n = 10000;
            Rectangle2D rect = new Rectangle2D.Float(xf,yf,wf,hf);
            startTest("rect-shape-draw", n);
            for ( int i=0; i<n; ++i ) {
                g2.draw(rect);
            }
            rect = null;
            endTest(print);
            
            // rect-direct-fill
            n = 10000;
            startTest("rect-direct-fill", n);
            for ( int i=0; i<n; ++i ) {
                g2.fillRect(x,y,w,h);
            }
            endTest(print);
            
            // rect-shape-fill
            rect = new Rectangle2D.Float(xf,yf,wf,hf);
            startTest("rect-shape-fill", n);
            for ( int i=0; i<n; ++i ) {
                g2.fill(rect);
            }
            rect = null;
            endTest(print);

            // rrect-direct-draw
            startTest("rrect-direct-draw", n);
            for ( int i=0; i<n; ++i ) {
                g2.drawRoundRect(x,y,w,h,c,c);
            }
            endTest(print);
            
            // rrect-shape-draw
            RoundRectangle2D rrect = new RoundRectangle2D.Float(xf,yf,wf,hf,cf,cf);
            startTest("rrect-shape-draw", n);
            for ( int i=0; i<n; ++i ) {
                g2.draw(rrect);
            }
            rrect = null;
            endTest(print);
            
            // rrect-direct-fill
            startTest("rrect-direct-fill", n);
            for ( int i=0; i<n; ++i ) {
                g2.fillRoundRect(x,y,w,h,c,c);
            }
            endTest(print);
            
            // rrect-shape-fill
            rrect = new RoundRectangle2D.Float(xf,yf,wf,hf,cf,cf);
            startTest("rrect-shape-fill", n);
            for ( int i=0; i<n; ++i ) {
                g2.fill(rrect);
            }
            rrect = null;
            endTest(print);
            
            // text-direct-int
            String text = "This is some sample text.";
            startTest("text-direct-int", n);
            for ( int i=0; i<n; ++i ) {
                g2.drawString(text, x+2, h/2);
            }
            endTest(print);
            
            // text-direct-float
            startTest("text-direct-float", n);
            for ( int i=0; i<n; ++i ) {
                g2.drawString(text, xf+2, hf/2);
            }
            endTest(print);
            
            // text-glyph-vector
            Font f = g2.getFont();
            GlyphVector gvec 
                = f.createGlyphVector(g2.getFontRenderContext(), text);
            startTest("text-glyph-vector", n);
            for ( int i=0; i<n; ++i ) {
                g2.drawGlyphVector(gvec, xf+2, hf/2);
            }
            endTest(print);
            
            if ( print ) {
                s_logger.info("Rendering Benchmarks: "+testSuite+'\n'
                        +sbuf.toString());
                sbuf.replace(0, sbuf.length(), "");
            }
        }
        System.exit(0);
    }
    
    public static void main(String[] args) {
        JFrame f = new JFrame("Rendering Test");
        f.setSize(500, 500);
        f.getContentPane().add(new RenderingBenchmarks());
        f.pack();
        f.setVisible(true);
    }
    
}
