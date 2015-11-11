package co.edu.uvp.pri.animation.ui.animation;

import com.sun.javafx.geom.Rectangle;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

public class Animation extends JComponent implements Runnable {

    private Thread thread = null;
    private int posx = 100;
    private int posy = 180;
    private int posxR = 0;
    private int dx = 1;
    private int dy = 1;
    public int speed = 20; // Velocidad de movimiento de la bola
    public Rectangle rebote;
    private Rectangle r1 = null, r2 = null, r3 = null, r4 = null,
                r5 = null, r6 = null, r7 = null, r8 = null;
    private Rectangle[] rects = {r1, r2, r3, r4, r5, r6, r7, r8};

    @Override
    public void paintComponent(Graphics g) {

        
        g.setColor(Color.red);
        g.fillOval(posx, posy, 20, 20);
        g.setColor(Color.white);
        g.drawOval(posx, posy, 20, 20);
        //Rectangulo de rebote

        rebote = new Rectangle(posxR, getHeight() - 10, 80, 10);
        g.setColor(Color.yellow);
        g.fillRect(posxR, getHeight() - 10, 80, 10);
        g.setColor(Color.white);
        g.drawRect(rebote.x, rebote.y, rebote.width, rebote.height);
        level1(g);
        

    }

    public void level1(Graphics g) {

        
        
        int widthPanel = getWidth();
        int xpos = 0, ypos = 0, width = widthPanel / 8, height = 20;
        for (Rectangle r : rects) {
            r = new Rectangle(xpos, ypos, width, height);
            g.setColor(Color.BLUE);
            g.fillRect(r.x, r.y, r.width, r.height);
            g.setColor(Color.WHITE);
            g.drawRect(r.x, r.y, r.width, r.height);
            xpos += width;
        }
        
    }
    public int destroyRect(int px, int py){
        for(Rectangle rect : rects){
            if(rect==null) break;
            if((py==rect.height)&&(px>=rect.x&&(px<=rect.x+rect.width))){
                rect = null;
                return 1;
            }
        }
        if(py<=0){
            return 1;
        }
        return -1;
        
        
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

            if (posx + 20 == (this.getWidth())) {
                dx = -1;
            }

            if (posy + 20 == rebote.y) {
                if (posx + 10 < rebote.x) {
                    JOptionPane.showMessageDialog(null, "Game Over");
                    this.pause();
                }

                dy = -1;
            }

            if (posx <= 0) {
                dx = 1;
            }
            if (posy <= 0) {
                dy = 1;
            }
            
            //Evaluar cuando toque un rectangulo superior
//            dy = destroyRect(posx, posy);

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
        posxR = (getWidth() / 2) - 40;
        this.init();
    }

}
