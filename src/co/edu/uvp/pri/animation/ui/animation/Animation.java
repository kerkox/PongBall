package co.edu.uvp.pri.animation.ui.animation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;

public class Animation extends JComponent implements Runnable {

    private Thread thread = null;
    private int posx = 0;
    private int posy = 10;
    private int posxR = 0;
    private int dx = 1;
    private int dy = 1;
    public int speed = 50;

    @Override
    public void paintComponent(Graphics g) {
//        posxR =getWidth()/2;
        g.setColor(Color.red);
        g.fillOval(posx, posy, 20, 20);
        g.setColor(Color.white);
        g.drawOval(posx, posy, 20, 20);
        //Rectangulo de rebote
        g.setColor(Color.yellow);
        g.fillRect(posxR, getHeight() - 10, 80, 10);
        g.setColor(Color.white);
        g.drawRect(posxR, getHeight() - 10, 80, 10);

    }

    @Override
    public void run() {
        while (this.thread != null) {
            posx += dx;
            posy += dy;

            //posicion del rectangulo segun el mouse
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent evento) {
                    posxR = evento.getX() - 40;
                }

            });
            System.out.println("Valor de posxR: " + posxR);
            if (posx + 20 == (this.getWidth())) {
//                back = true;
                dx = -1;
            }
            if (posy + 20 == this.getHeight()) {
                dy = -1;
            }
            if (posx <= 0) {
//                back = false;
                dx = 1;
            }
            if (posy <= 0) {
                dy = 1;
            }

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
        posxR = (getWidth() / 2)-40;
        this.init();
    }

}
