/**
 * Copyright (c) 2004-2006 Regents of the University of California.
 * See "license-prefuse.txt" for licensing terms.
 */
package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class RenderingAccuracy extends JComponent {

    public RenderingAccuracy() {
        setPreferredSize(new Dimension(100,120));
        setBackground(Color.WHITE);
        setForeground(new Color(100,100,255));
    }
    
    public void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D)g;
        g2D.setColor(getBackground());
        g2D.fillRect(0, 0, getWidth(), getHeight());
        g2D.setColor(getForeground());
        rects(g2D);
        lines(g2D);
    }
    
    public void rects(Graphics2D g2D) {
        Rectangle2D r = new Rectangle2D.Double();
        double x = 10, y = 4;
        int inc = 5;
        
        // g2D.draw() - vary start coord
        for ( int i=0; i<=10; ++i ) {
            r.setFrame(x+i*0.1, y+5*i, 2, 3);
            g2D.draw(r);
        } x += inc;
        
        // g2D.draw() - vary width
        for ( int i=0; i<=10; ++i ) {
            r.setFrame(x, y+5*i, 2+i*0.1, 3);
            g2D.draw(r);
        } x += inc;
        
        // g2D.fill() - vary start coord
        for ( int i=0; i<=10; ++i ) {
            r.setFrame(x+i*0.1, y+5*i, 2, 3);
            g2D.fill(r);
        } x += inc;
        
        // g2D.fill() - vary width
        for ( int i=0; i<=10; ++i ) {
            r.setFrame(x, y+5*i, 2+i*0.1, 3);
            g2D.fill(r);
        }  x += inc;
        
        x += inc;
        
        // optimized draw - vary start coord
        for ( int i=0; i<=10; ++i ) {
            r.setFrame(x+i*0.1, y+5*i, 2, 3);
            draw(g2D, r);
        } x += inc;
        
        // optimized draw - vary width
        for ( int i=0; i<=10; ++i ) {
            r.setFrame(x, y+5*i, 2+i*0.1, 3);
            draw(g2D, r);
        } x += inc;
        
        // optimized fill - vary start coord
        for ( int i=0; i<=10; ++i ) {
            r.setFrame(x+i*0.1, y+5*i, 2, 3);
            fill(g2D, r);
        } x += inc;
        
        // optimized fill - vary width
        for ( int i=0; i<=10; ++i ) {
            r.setFrame(x, y+5*i, 2+i*0.1, 3);
            fill(g2D, r);
        }  x += inc;
        
        // -- next row -------------------------------
        x = 10.35; y += 60;
        
        // g2D.draw() - vary start coord
        for ( int i=0; i<=10; ++i ) {
            r.setFrame(x+i*0.1, y+5*i, 2, 3);
            g2D.draw(r);
        } x += inc;
        
        // g2D.draw() - vary width
        for ( int i=0; i<=10; ++i ) {
            r.setFrame(x, y+5*i, 2+i*0.1, 3);
            g2D.draw(r);
        } x += inc;
        
        // g2D.fill() - vary start coord
        for ( int i=0; i<=10; ++i ) {
            r.setFrame(x+i*0.1, y+5*i, 2, 3);
            g2D.fill(r);
        } x += inc;
        
        // g2D.fill() - vary width
        for ( int i=0; i<=10; ++i ) {
            r.setFrame(x, y+5*i, 2+i*0.1, 3);
            g2D.fill(r);
        }  x += inc;
        
        x += inc;
        
        // optimized draw - vary start coord
        for ( int i=0; i<=10; ++i ) {
            r.setFrame(x+i*0.1, y+5*i, 2, 3);
            draw(g2D, r);
        } x += inc;
        
        // optimized draw - vary width
        for ( int i=0; i<=10; ++i ) {
            r.setFrame(x, y+5*i, 2+i*0.1, 3);
            draw(g2D, r);
        } x += inc;
        
        // optimized fill - vary start coord
        for ( int i=0; i<=10; ++i ) {
            r.setFrame(x+i*0.1, y+5*i, 2, 3);
            fill(g2D, r);
        } x += inc;
        
        // optimized fill - vary width
        for ( int i=0; i<=10; ++i ) {
            r.setFrame(x, y+5*i, 2+i*0.1, 3);
            fill(g2D, r);
        }  x += inc;
    }
    
    public void lines(Graphics2D g2D) {
        Line2D r = new Line2D.Double();
        double x = 60, y = 4;
        int inc = 5;
        
        // g2D.draw() - vary start coord
        for ( int i=0; i<=10; ++i ) {
            double x1 = x+i*0.1;
            double y1 = y+5*i;
            r.setLine(x1, y1, x1+2, y1);
            g2D.draw(r);
        } x += inc;
        
        // g2D.draw() - vary width
        for ( int i=0; i<=10; ++i ) {
            double x2 = x + 2+i*0.1;
            double y1 = y+5*i;
            r.setLine(x, y1, x2, y1);
            g2D.draw(r);
        } x += inc;
        
        x += inc;
        
        // optimized draw - vary start coord
        for ( int i=0; i<=10; ++i ) {
            double x1 = x+i*0.1;
            double y1 = y+5*i;
            r.setLine(x1, y1, x1+2, y1);
            draw(g2D, r);
        } x += inc;
        
        // optimized draw - vary width
        for ( int i=0; i<=10; ++i ) {
            double x2 = x + 2+i*0.1;
            double y1 = y+5*i;
            r.setLine(x, y1, x2, y1);
            draw(g2D, r);
        } x += inc;
        
        // -- next row -------------------------------
        x = 60+0.35; y += 60;
        
        // g2D.draw() - vary start coord
        for ( int i=0; i<=10; ++i ) {
            double x1 = x+i*0.1;
            double y1 = y+5*i;
            r.setLine(x1, y1, x1+2, y1);
            g2D.draw(r);
        } x += inc;
        
        // g2D.draw() - vary width
        for ( int i=0; i<=10; ++i ) {
            double x2 = x + 2+i*0.1;
            double y1 = y+5*i;
            r.setLine(x, y1, x2, y1);
            g2D.draw(r);
        } x += inc;
        
        x += inc;
        
        // optimized draw - vary start coord
        for ( int i=0; i<=10; ++i ) {
            double x1 = x+i*0.1;
            double y1 = y+5*i;
            r.setLine(x1, y1, x1+2, y1);
            draw(g2D, r);
        } x += inc;
        
        // optimized draw - vary width
        for ( int i=0; i<=10; ++i ) {
            double x2 = x + 2+i*0.1;
            double y1 = y+5*i;
            r.setLine(x, y1, x2, y1);
            draw(g2D, r);
        } x += inc;
    }
    
    public void draw(Graphics2D g, Line2D r) {
        int x1 = (int)r.getX1();
        int y1 = (int)r.getY1();
        int x2 = (int)r.getX2();
        int y2 = (int)r.getY2();
        g.drawLine(x1, y1, x2, y2);
    }
    
    public void draw(Graphics2D g, Rectangle2D r) {
        int x = (int)r.getMinX();
        int y = (int)r.getMinY();
        int w = (int)(r.getWidth() +r.getMinX()-x);
        int h = (int)(r.getHeight()+r.getMinY()-y);
        g.drawRect(x, y, w, h);
    }
    
    public void fill(Graphics2D g, Rectangle2D r) {
        int x = (int)r.getMinX();
        int y = (int)r.getMinY();
        int w = (int)(r.getWidth() +r.getMinX()-x);
        int h = (int)(r.getHeight()+r.getMinY()-y);
        g.fillRect(x, y, w, h);
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        JFrame f = new JFrame("rendering accuracy");
        f.setContentPane(new RenderingAccuracy());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }

}
