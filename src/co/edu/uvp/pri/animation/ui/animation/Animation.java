package co.edu.uvp.pri.animation.ui.animation;

import java.awt.Color;
import java.awt.Graphics;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;

public class Animation extends JComponent implements Runnable {

    private Thread thread = null;
    private int posx = 0;
    private int posy = 10;
    private int dx = 1;
    private int dy = 1;
    public int speed = 50;

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.red);
        g.fillOval(posx, posy, 20, 20);
        g.setColor(Color.white);
        g.drawOval(posx, posy, 20, 20);
    }
    

    @Override
    public void run() {
        while (this.thread != null) {
            posx += dx;
            posy += dy; 
            
            if (posx + 20 == (this.getWidth())) {
//                back = true;
                dx = -1;
            }
            if(posy +20 == this.getHeight()){
                dy=-1;
            }
            if(posx <=0){
//                back = false;
                dx = 1;
            }
            if(posy <=0){
                dy = 1;
            }
//            if(posx > this.getWidth() && posy > this.getHeight() ){
//                posx = 0;
//            }
            repaint();
            try {
                Thread.sleep(speed);
            } catch (InterruptedException ex) {
                Logger.getLogger(Animation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void init() {
        if (this.thread == null) {
            this.thread = new Thread(this);
            this.thread.start();
        }
    }

    public void pause() {
        this.thread = null;
    }

    public void restart() {
        this.pause();
        posx = 0;
        posy = 10;
        this.init();
    }

}
